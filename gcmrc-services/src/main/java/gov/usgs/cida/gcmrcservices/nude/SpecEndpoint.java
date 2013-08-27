package gov.usgs.cida.gcmrcservices.nude;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import gov.usgs.cida.gcmrcservices.TimeUtil;
import static gov.usgs.cida.gcmrcservices.TimeUtil.TZ_CODE_LOOKUP;
import gov.usgs.cida.gcmrcservices.jsl.data.ParameterSpec;
import gov.usgs.cida.gcmrcservices.jsl.data.SpecOptions;
import static gov.usgs.cida.gcmrcservices.nude.Endpoint.COLUMN_KEYWORD;
import static gov.usgs.cida.gcmrcservices.nude.Endpoint.TIMEZONE_IN_HEADER_KEYWORD;
import static gov.usgs.cida.gcmrcservices.nude.Endpoint.TIMEZONE_KEYWORD;
import static gov.usgs.cida.gcmrcservices.nude.Endpoint.getDateRange;
import static gov.usgs.cida.gcmrcservices.nude.Endpoint.getParameter;
import static gov.usgs.cida.gcmrcservices.nude.Endpoint.getStations;
import static gov.usgs.cida.gcmrcservices.nude.Endpoint.getStation;
import gov.usgs.cida.gcmrcservices.nude.time.TimeColumnReq;
import gov.usgs.cida.gcmrcservices.nude.time.TimeConfig;
import gov.usgs.cida.gcmrcservices.nude.time.TimeSplitPlanStep;
import gov.usgs.cida.nude.column.Column;
import gov.usgs.cida.nude.column.SimpleColumn;
import gov.usgs.cida.nude.plan.ConfigPlanStep;
import gov.usgs.cida.nude.plan.Plan;
import gov.usgs.cida.nude.plan.PlanStep;
import gov.usgs.cida.nude.resultset.inmemory.TableRow;
import gov.usgs.webservices.jdbc.spec.Spec;
import java.util.*;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public abstract class SpecEndpoint extends Endpoint {
	private static final Logger log = LoggerFactory.getLogger(SpecEndpoint.class);
	public static final Column time = new SimpleColumn(ParameterSpec.C_TSM_DT);
	public static final DateTimeZone DATABASE_TIMEZONE = DateTimeZone.forOffsetHours(-7);

	@Override
	public Plan createPlan(UUID requestId, ListMultimap<String, String> params) {
		Plan result = null;

		Iterable<Spec> specs = createSpecifiedSpecs(params);
		Multimap<Column, Column> mux = createSpecMux(params);
		Map<String, String[]> modMap = createSpecParameters(params);
		TimeConfig timeConfig = getDateRange(params);
		boolean noDataFilter = (!"false".equals(getParameter(params, NODATA_FILTER_KEYWORD, "false")));
		
		modMap.put(BEGIN_KEYWORD, new String[]{timeConfig.getDateRange().getBegin().toString(TimeUtil.DB_DATE_FORMAT.withZone(DATABASE_TIMEZONE))});
		modMap.put(END_KEYWORD, new String[]{timeConfig.getDateRange().getEnd().toString(TimeUtil.DB_DATE_FORMAT.withZone(DATABASE_TIMEZONE))});
		
		boolean hasSpecs = false;
		for (Spec spec : specs) {
			Spec.loadParameters(spec, modMap);
			hasSpecs = true;
		}

		LinkedList<PlanStep> steps = new LinkedList<PlanStep>();
		
		if (hasSpecs) {
			List<String> stations = getStations(params);
			steps.addAll(configurePlan(requestId, stations, specs, mux, timeConfig, noDataFilter));
			steps.add(new TimeSplitPlanStep(steps.getLast().getExpectedColumns(), createTimeColumnReqs(params)));
		} else {
			List<TableRow> rows = new ArrayList<TableRow>();
			rows.add(new TableRow(new SimpleColumn("ERROR"), "No Columns Specified"));
			steps.add(new ConfigPlanStep(rows));
		}

		result = new Plan(steps);

		return result;
	}
	
	public abstract List<PlanStep> configurePlan(UUID requestId, List<String> stations, Iterable<Spec> specs, Multimap<Column, Column> mux, TimeConfig timeConfig, boolean noDataFilter);
	
	public Map<String, String[]> createSpecParameters(ListMultimap<String, String> params) {
		Map<String, String[]> result = new HashMap<String, String[]>();

		result.put(Spec.ORDER_BY_PARAM, new String[]{ParameterSpec.S_TSM_DT});

		return result;
	}
	
	public Iterable<Spec> createSpecifiedSpecs(ListMultimap<String, String> params) {
		Set<Spec> result = new HashSet<Spec>();
		
		SpecOptions specOptions = buildSpecOptions(params);

		List<String> userCols = params.get(COLUMN_KEYWORD);
		for (String colName : userCols) {
			ColumnMetadata cmd = getColumnMetadata(colName);
			String station = getStation(colName);
			
			if (null != cmd && null != station) {
				List<ColumnMetadata.SpecEntry> specEntries = cmd.getSpecEntries();
				for (ColumnMetadata.SpecEntry se : specEntries) {
					Map<String, String[]> modMap = new HashMap<String, String[]>();
					modMap.put(ParameterSpec.S_SITE_NAME, new String[] {station});
					modMap.put(ParameterSpec.S_GROUP_NAME, new String[] {se.parameterCode.groupName});
					Spec spec = se.getSpec(station, specOptions);
					Spec.loadParameters(spec, modMap);
					result.add(spec);
				}
			} else {
				log.debug("No column by the name of: " + colName);
			}
		}
		
		return result;
	}
	
	public Multimap<Column, Column> createSpecMux(ListMultimap<String, String> params) {
		HashMultimap<Column, Column> result = HashMultimap.<Column, Column>create();
		
		List<String> userCols = params.get(COLUMN_KEYWORD);
		for (String colName : userCols) {
			ColumnMetadata cmd = getColumnMetadata(colName);
			String station = getStation(colName);

			if (null != cmd && null!= station && !result.containsKey(cmd.getColumn(station))) {
				List<ColumnMetadata.SpecEntry> specEntries = cmd.getSpecEntries();
				for (ColumnMetadata.SpecEntry se : specEntries) {
					result.put(cmd.getColumn(station), se.getColumn(station));
				}
			} else {
				log.debug("No column by the name of: " + colName);
			}
		}
		
		return result;
	}
	
	public List<TimeColumnReq> createTimeColumnReqs(ListMultimap<String, String> params) {
		List<TimeColumnReq> result = new ArrayList<TimeColumnReq>();
		
		boolean timezoneInHeader = !"false".equals(getParameter(params, TIMEZONE_IN_HEADER_KEYWORD, "false"));
		TimeConfig timeConfig = getDateRange(params);

		String timeDisplayName = "time";
		if (timezoneInHeader) {
			String timezoneCode = TZ_CODE_LOOKUP.get(-7);

			String tzStr = getParameter(params, TIMEZONE_KEYWORD);
			if (StringUtils.isNotBlank(tzStr)) {
				try {
					int timezone = Integer.parseInt(tzStr);
					String tzCode = TZ_CODE_LOOKUP.get(timezone);
					if (null != tzCode) {
						timezoneCode = tzCode;
					} else {
						log.debug("Unknown tzcode");
					}
				} catch (NumberFormatException e) {
					log.debug("Timezone " + tzStr + " is invalid.");
				}
			}

			timeDisplayName = "time (" + timezoneCode + ")";
		}

		List<String> userCols = params.get(COLUMN_KEYWORD);
		for (String colName : userCols) {
			TimeColumnReq timeCol = TimeColumnReq.parseTimeColumn(colName, timeDisplayName, timeConfig.getTimezone());
			if (null != timeCol) {
				result.add(timeCol);
			}
		}

		return result;
	}

	protected static SpecOptions buildSpecOptions(ListMultimap<String, String> params) {
		SpecOptions result = null;
		
		String useLagged = getOptionParameter(params, "useLagged", "false", "true");
		
		result = new SpecOptions(useLagged);
		
		return result;
	}
}
