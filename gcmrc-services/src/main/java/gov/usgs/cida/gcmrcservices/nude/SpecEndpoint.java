package gov.usgs.cida.gcmrcservices.nude;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import gov.usgs.cida.gcmrcservices.TimeUtil;
import gov.usgs.cida.gcmrcservices.jsl.data.ParameterSpec;
import gov.usgs.cida.gcmrcservices.jsl.data.SpecOptions;
import static gov.usgs.cida.gcmrcservices.nude.Endpoint.COLUMN_KEYWORD;
import static gov.usgs.cida.gcmrcservices.nude.Endpoint.STATION_KEYWORD;
import gov.usgs.cida.gcmrcservices.nude.time.TimeConfig;
import gov.usgs.cida.nude.column.Column;
import gov.usgs.cida.nude.column.SimpleColumn;
import gov.usgs.cida.nude.plan.ConfigPlanStep;
import gov.usgs.cida.nude.plan.Plan;
import gov.usgs.cida.nude.plan.PlanStep;
import gov.usgs.cida.nude.resultset.inmemory.TableRow;
import gov.usgs.cida.nude.time.OutputTimeFormatPlanStep;
import gov.usgs.webservices.jdbc.spec.Spec;
import java.util.*;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
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
		DateTimeFormatter outputTimeFormat = TimeUtil.getDateFormatter(getParameter(params, TIME_FORMAT_KEYWORD, "ISONOZONE"));
		if (null != outputTimeFormat) {
			outputTimeFormat = outputTimeFormat.withZone(DateTimeZone.forOffsetHours(timeConfig.getTimezone()));
		}
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
			List<String> stations = params.get(STATION_KEYWORD);
			steps.addAll(configurePlan(requestId, stations, specs, mux, timeConfig, noDataFilter));
			if (null != outputTimeFormat) {
				steps.add(new OutputTimeFormatPlanStep(steps.getLast().getExpectedColumns(), outputTimeFormat));
			}
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
		
		List<String> stations = params.get(STATION_KEYWORD);
		
		SpecOptions specOptions = buildSpecOptions(params);
		
		for (String station : stations) {
			Map<String, String[]> modMap = new HashMap<String, String[]>();
			modMap.put(ParameterSpec.S_SITE_NO, new String[] {station});
			
			List<String> userCols = params.get(COLUMN_KEYWORD);
			for (String colName : userCols) {
				ColumnMetadata cmd = getColumnMetadata(colName);

				if (null != cmd) {
					List<ColumnMetadata.SpecEntry> specEntries = cmd.getSpecEntries();
					for (ColumnMetadata.SpecEntry se : specEntries) {
						Spec spec = se.getSpec(station, specOptions);
						Spec.loadParameters(spec, modMap);
						result.add(spec);
					}
				} else {
					log.debug("No column by the name of: " + colName);
				}
			}
		}
		
		return result;
	}
	
	public Multimap<Column, Column> createSpecMux(ListMultimap<String, String> params) {
		HashMultimap<Column, Column> result = HashMultimap.<Column, Column>create();
		
		List<String> stations = params.get(STATION_KEYWORD);

		for (String station : stations) {
			List<String> userCols = params.get(COLUMN_KEYWORD);
			for (String colName : userCols) {
				ColumnMetadata cmd = getColumnMetadata(colName);

				if (null != cmd && !result.containsKey(cmd.getColumn(station))) {
					List<ColumnMetadata.SpecEntry> specEntries = cmd.getSpecEntries();
					for (ColumnMetadata.SpecEntry se : specEntries) {
						result.put(cmd.getColumn(station), se.getColumn(station));
					}
				} else {
					log.debug("No column by the name of: " + colName);
				}
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
