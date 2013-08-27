package gov.usgs.cida.gcmrcservices.nude;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import gov.usgs.cida.gcmrcservices.TimeUtil;
import gov.usgs.cida.gcmrcservices.jsl.data.ParameterCode;
import gov.usgs.cida.gcmrcservices.nude.ColumnMetadata.SpecEntry;
import gov.usgs.cida.gcmrcservices.nude.ColumnMetadata.SpecEntry.SpecType;
import gov.usgs.cida.gcmrcservices.nude.time.TimeConfig;
import gov.usgs.cida.nude.column.Column;
import gov.usgs.cida.nude.column.SimpleColumn;
import gov.usgs.cida.nude.out.Closers;
import gov.usgs.cida.nude.out.Dispatcher;
import gov.usgs.cida.nude.out.StreamResponse;
import gov.usgs.cida.nude.out.TableResponse;
import gov.usgs.cida.nude.out.mapping.ColumnToXmlMapping;
import gov.usgs.cida.nude.plan.Plan;
import gov.usgs.cida.nude.provider.IProvider;
import gov.usgs.cida.nude.provider.Provider;
import gov.usgs.cida.nude.provider.sql.ParameterizedString;
import gov.usgs.cida.nude.provider.sql.SQLProvider;
import gov.usgs.cida.nude.resultset.inmemory.TableRow;
import gov.usgs.cida.nude.time.DateRange;
import gov.usgs.webservices.framework.basic.MimeType;
import java.io.IOException;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public abstract class Endpoint extends HttpServlet {

	private static final Logger log = LoggerFactory.getLogger(Endpoint.class);
	public static final String COLUMN_KEYWORD = "column[]";
	public static final String BEGIN_KEYWORD = "beginPosition";
	public static final String END_KEYWORD = "endPosition";
	/**
	 * determines the acceptable gap for linearly interpolating
	 */
	public static final String EVERY_KEYWORD = "every";
	/**
	 * the time period over which to run the downscale algorithm (min/max by default)
	 */
	public static final String DOWNSCALE_KEYWORD = "downscale";
	public static final String NODATA_FILTER_KEYWORD = "noDataFilter";
	public static final String INTERVAL_KEYWORD = "timeInt";
	
	public static final String DOWNLOAD_KEYWORD = "download";
	public static final String OUTPUT_FORMAT_KEYWORD = "output";
	public static final String TIME_FORMAT_KEYWORD = "timeFormat";
	public static final String TIMEZONE_KEYWORD = "tz";
	public static final String TIMEZONE_IN_HEADER_KEYWORD = "tzInHeader";
	public static final String CUTOFF_AFTER_KEYWORD = "cutoffAfter";
	public static final String CUTOFF_BEFORE_KEYWORD = "cutoffBefore";
	
	
	public static final int columnIdentifierLength = 2; // HACK HAAAAAAAAAAAAAACK
	
	protected Map<Provider, IProvider> providers;
	protected Map<String, ColumnMetadata> CM_LOOKUP;
	protected Map<String, ColumnMetadata> qwColumnMetadatas;
	protected Map<String, ColumnMetadata> cumLoadColumnMetadatas;
	
	@Override
	public void init() throws ServletException {
		providers = new EnumMap<Provider, IProvider>(Provider.class);
		SQLProvider sqlProvider = new SQLProvider("java:comp/env/jdbc/gcmrcDS");
		sqlProvider.init();
		providers.put(Provider.SQL, sqlProvider);
		providers = Collections.unmodifiableMap(providers);
		
		CM_LOOKUP = new HashMap<String, ColumnMetadata>();
		CM_LOOKUP.putAll(buildInstantaneousParametersCols(sqlProvider));
		qwColumnMetadatas = Collections.unmodifiableMap(buildQWParametersCols(sqlProvider));
		CM_LOOKUP.putAll(qwColumnMetadatas);
		CM_LOOKUP = Collections.unmodifiableMap(CM_LOOKUP);
		
		cumLoadColumnMetadatas = Collections.unmodifiableMap(buildCumLoadParametersCols());
		
		super.init();
	}
	
	protected Map<String, ColumnMetadata> buildInstantaneousParametersCols(SQLProvider sqlProvider) {
		Map<String, ColumnMetadata> result = new HashMap<String, ColumnMetadata>();
		ResultSet rs = null;
		try {
			Column pCode = new SimpleColumn("PARM_CD");
			Column tsGrpNm = new SimpleColumn("TS_GRP_NM");
			Column nwisName = new SimpleColumn("PARM_NM");
			Column displayName = new SimpleColumn("PARM_ESSENCE_NM");
			Column units = new SimpleColumn("UNITS_NM");
			Column unitsShort = new SimpleColumn("UNITS_SHORT_NM");
			
			ParameterizedString ps = new ParameterizedString();
			ps.append("  SELECT DISTINCT PARM.").append(pCode.getName()).append(",");
			ps.append("    TS_GROUP_POR.").append(tsGrpNm.getName()).append(",");
			ps.append("    PARM.").append(nwisName.getName()).append(",");
			ps.append("    PARM.").append(displayName.getName()).append(",");
			ps.append("    PARM.").append(units.getName()).append(",");
			ps.append("    PARM.").append(unitsShort.getName());
			ps.append("  FROM PARM,");
			ps.append("    TS_GROUP_POR,");
			ps.append("    INFO_PORTAL_TS_GROUP");
			ps.append("  WHERE PARM.PARM_CD = TS_GROUP_POR.PARM_CD");
			ps.append("  AND TS_GROUP_POR.PARM_CD = INFO_PORTAL_TS_GROUP.PARM_CD");
			ps.append("  AND TS_GROUP_POR.TS_GRP_NM = INFO_PORTAL_TS_GROUP.TS_GRP_NM");
			ps.append("  AND INFO_PORTAL_TS_GROUP.PRIORITY_VA <= 11");
			ps.append("  ORDER BY PARM_CD");
			
			rs = sqlProvider.getResults(null, ps);
			while (rs.next()) {
				TableRow row = TableRow.buildTableRow(rs);
				
				String columnTsGroupName = row.getValue(tsGrpNm);
				String parameterCode = "inst!" + columnTsGroupName;
				
				String columnDisplayName = row.getValue(displayName);
				
				String columnTitle = columnDisplayName + "(" + row.getValue(unitsShort) + ")";
				
				result.put(parameterCode, new ColumnMetadata(parameterCode, columnTitle, 
						new SpecEntry(ParameterCode.parseParameterCode(parameterCode), SpecType.PARAM)));
			}
			log.debug("Instantaneous parameter columns constructed : " + result.keySet().toString());
		} catch (Exception e) {
			log.error("Could not get columns", e);
		} finally {
			Closers.closeQuietly(rs);
		}
		return result;
	}
	
	protected Map<String, ColumnMetadata> buildQWParametersCols(SQLProvider sqlProvider) {
		Map<String, ColumnMetadata> result = new HashMap<String, ColumnMetadata>();
		ResultSet rs = null;
		try {
			Column pCode = new SimpleColumn("PARM_CD");
			Column sampleMethod = new SimpleColumn("SAMP_METH_CD");
			Column displayName = new SimpleColumn("PARM_ESSENCE_NM");
			Column unitsShort = new SimpleColumn("UNITS_SHORT_NM");
			
			ParameterizedString ps = new ParameterizedString();
			ps.append("SELECT ");
			ps.append("  QWP.PARM_CD, ");
			ps.append("  QWP.SAMP_METH_CD,");
			ps.append("  P.PARM_ESSENCE_NM,");
			ps.append("  P.UNITS_SHORT_NM");
			ps.append(" FROM ");
			ps.append("  QW_POR QWP,");
			ps.append("  PARM P");
			ps.append(" WHERE");
			ps.append("  QWP.PARM_CD = P.PARM_CD");
			ps.append(" GROUP BY ");
			ps.append("  QWP.PARM_CD, ");
			ps.append("  QWP.SAMP_METH_CD,");
			ps.append("  P.PARM_ESSENCE_NM,");
			ps.append("  P.UNITS_SHORT_NM");
			
			rs = sqlProvider.getResults(null, ps);
			while (rs.next()) {
				TableRow row = TableRow.buildTableRow(rs);
				
				String parameterCode = row.getValue(sampleMethod) + "!" + row.getValue(pCode);
				String columnTitle = row.getValue(sampleMethod) + " Sampled " + row.getValue(displayName) + "(" + row.getValue(unitsShort) + ")";
				
				result.put(parameterCode, new ColumnMetadata(parameterCode, columnTitle, 
						new SpecEntry(ParameterCode.parseParameterCode(parameterCode), SpecType.LABDATA)));
			}
			log.debug("QW parameter columns constructed : " + result.keySet().toString());
		} catch (Exception e) {
			log.error("Could not get columns", e);
		} finally {
			Closers.closeQuietly(rs);
		}
		return result;
	}
	
	/**
	 * We need to know Cumulative Load parameters, so we can zero out the timeseries per request.
	 * @return 
	 */
	protected Map<String, ColumnMetadata> buildCumLoadParametersCols() {
		Map<String, ColumnMetadata> result = new HashMap<String, ColumnMetadata>();
		
		//WAYYY HAAACK
		result.put("inst!S Sand Cumul Load", new ColumnMetadata("inst!S Sand Cumul Load", "Cumulative Suspended Sand Load (Metric Tons)", 
				new SpecEntry(ParameterCode.parseParameterCode("inst!S Sand Cumul Load"), SpecType.PARAM)));
		result.put("inst!Minor Trib S Sand Cumul Load", new ColumnMetadata("inst!Minor Trib S Sand Cumul Load", "Cumulative Suspended Sand Load (Metric Tons)", 
				new SpecEntry(ParameterCode.parseParameterCode("inst!Minor Trib S Sand Cumul Load"), SpecType.PARAM)));
		result.put("inst!S Fines Cumul Load", new ColumnMetadata("inst!S Fines Cumul Load", "Cumulative Silt-and-Clay Load (Metric Tons)", 
				new SpecEntry(ParameterCode.parseParameterCode("inst!S Fines Cumul Load"), SpecType.PARAM)));
		result.put("inst!Minor Trib S Fines Cumul Load", new ColumnMetadata("inst!Minor Trib S Fines Cumul Load", "Cumulative Silt-and-Clay Load (Metric Tons)", 
				new SpecEntry(ParameterCode.parseParameterCode("inst!Minor Trib S Fines Cumul Load"), SpecType.PARAM)));
		
		//ugh. this is horrible.
		result.put("inst!Sand Cumul Load", new ColumnMetadata("inst!Sand Cumul Load", "Cumulative Sand Load (Metric Tons)", 
				new SpecEntry(ParameterCode.parseParameterCode("inst!Sand Cumul Load"), SpecType.PARAM)));
		
		
		return result;
	}
	
	@Override
	public void destroy() {
		
		if (null != providers) {
			for (IProvider provider : providers.values()) {
				provider.destroy();
			}
		}
		
		CM_LOOKUP = Collections.unmodifiableMap(new HashMap<String, ColumnMetadata>());
		
		super.destroy();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		UUID requestUUID = UUID.randomUUID();
		try {
			log.info("Started Request " + requestUUID.toString() + " " + req.getRequestURI() + "?" + req.getQueryString());

			Map<String, String[]> reqArrayMap = req.getParameterMap();
			ListMultimap<String, String> reqMap = ArrayListMultimap.<String, String>create();
			for (Map.Entry<String, String[]> ent : reqArrayMap.entrySet()) {
				reqMap.putAll(ent.getKey(), Arrays.asList(ent.getValue()));
			}

			Plan plan = createPlan(requestUUID, reqMap);
			OutputConfig outConfig = createOutputConfig(reqMap);

			dispatchPlan(plan, resp, outConfig);
		} finally {
			SQLProvider.closeConnection(requestUUID);
			log.info("Ended Request " + requestUUID.toString());
		}
	}
	
	public abstract Plan createPlan(UUID requestId, ListMultimap<String, String> params);
	
	public OutputConfig createOutputConfig(ListMultimap<String, String> params) {
		OutputConfig result = null;

		String fill = null;

		ColumnToXmlMapping[] columnMappings = createSpecifiedColumnMappings(params);

		boolean isDownload = getIsDownload(params);

		String userMime = getParameter(params, OUTPUT_FORMAT_KEYWORD, "tab");
		log.debug(userMime);
		MimeType mime = MimeType.lookup(userMime);
		if (null == mime) {
			mime = MimeType.TAB;
		}
		log.debug(mime.getMimeType());
		

		result = new OutputConfig(fill, columnMappings, false, isDownload, mime);

		return result;
	}
	
	public abstract ColumnToXmlMapping[] createSpecifiedColumnMappings(ListMultimap<String, String> params);
	
	public static String getParameter(ListMultimap<String, String> map, String key) {
		return getParameter(map, key, null);
	}

	public static String getParameter(ListMultimap<String, String> map, String key, String defaultValue) {
		String result = null;

		List<String> vals = map.get(key);
		if (null != vals && 0 < vals.size()) {
			result = StringUtils.trimToNull(vals.get(0));
		}

		if (null == result) {
			result = defaultValue;
		}

		return result;
	}
	
	public static String getOptionParameter(ListMultimap<String, String> map, String key, String defaultValue, String... otherOptions) {
		String result = null;
		
		String pendingResult = getParameter(map, key, defaultValue);
		
		if (null != otherOptions && 0 < otherOptions.length) {
			if (null == pendingResult || !(StringUtils.equals(defaultValue, pendingResult) || Arrays.asList(otherOptions).contains(pendingResult))) {
				log.trace("invalid option '" + pendingResult + "'. setting to default '" + defaultValue + "'.");
				pendingResult = defaultValue;
			} else {
				log.trace("valid option " + pendingResult);
			}
		} else {
			log.trace("no other options");
		}
		
		result = pendingResult;
		
		return result;
	}

	public static void dispatchPlan(Plan plan, HttpServletResponse resp, OutputConfig outConfig) throws IOException {
		ResultSet rsResp = null;
		Writer out = null;
		try {
			resp.setCharacterEncoding("utf-8");
			resp.setContentType(outConfig.getMimeType());

			if (outConfig.isDownload()) {
				resp.setHeader("Content-Disposition", "attachment; filename=\"gcmrc"
						+ DateTime.now().toString(TimeUtil.FILENAME_FORMAT) + "." + outConfig.getStyle().getFileSuffix() + "\"");
			}
			resp.flushBuffer();
			out = resp.getWriter();

			try {
				rsResp = Plan.runPlan(plan);
			} catch (Exception e) {
				log.error("Could not run plan!", e);
			}

			if (null != rsResp) {
				TableResponse tr = new TableResponse(rsResp, outConfig.getFill(), outConfig.getColumnMappings(), outConfig.isTransparentMode());
				StreamResponse sr = null;
				try {
					sr = Dispatcher.buildFormattedResponse(outConfig.getStyle(), tr);
				} catch (SQLException ex) {
					log.error("Exception while creating StreamResponse", ex);
				} catch (XMLStreamException ex) {
					log.error("Exception while creating StreamResponse", ex);
				}

				if (null != sr && null != out) {
					StreamResponse.dispatch(sr, out);
					out.flush();
				}
			} else {
				log.debug("Tried dispatching null ResultSet");
			}
		} finally {
			Closers.closeQuietly(rsResp);
			Closers.closeQuietly(out);
		}
	}

	public static TimeConfig getDateRange(ListMultimap<String, String> params) {
		TimeConfig result = null;

		/**
		 * Date range config.
		 */
		String beginTime = getParameter(params, BEGIN_KEYWORD);
		String endTime = getParameter(params, END_KEYWORD);
		String every = getParameter(params, EVERY_KEYWORD);
		String downscale = getParameter(params, DOWNSCALE_KEYWORD);
		
		String cutoffBefore = getParameter(params, CUTOFF_BEFORE_KEYWORD);
		String cutoffAfter = getParameter(params, CUTOFF_AFTER_KEYWORD);
		
		String timeInt = getParameter(params, INTERVAL_KEYWORD);
		int acceptPeriod = 24;
		if (!StringUtils.isEmpty(timeInt)) {
			acceptPeriod = Integer.parseInt(timeInt);
		}
		
		int tz = -7;
		String timezone = getParameter(params, TIMEZONE_KEYWORD);
		if (!StringUtils.isEmpty(timezone)) {
			try {
				tz = Integer.parseInt(timezone);
			} catch (NumberFormatException e) {
				log.debug("Timezone '" + timezone + "' is not valid.");
			}
		} else {
			log.debug("Using default timezone -7");
		}
		
		DateTimeZone dtz = DateTimeZone.forOffsetHours(tz);
		
		DateRange dateRange = null;
		dateRange = DateRange.parseContinuousDateRange(beginTime, endTime, TimeUtil.INPUT_DATE_FORMAT, dtz);
		
		DateRange interpDateRange = null;
		if (StringUtils.isNotEmpty(every)) {
			//Prefill seems to act as a time shift? really freakin weird.  give it ZERO
			interpDateRange = DateRange.createRegularDiscreteDateRange(dateRange, every, acceptPeriod, Period.ZERO, dtz);
		}

		DateRange downscaleDateRange = null;
		if (StringUtils.isNotEmpty(downscale)) {
			downscaleDateRange = DateRange.createRegularDiscreteDateRange(dateRange, downscale, acceptPeriod, Period.ZERO, dtz);
		}
		
		DateTime cutoffBeforeDT = null;
		DateTime cutoffAfterDT = null;
		
		try {
			if (null != cutoffBefore) {
				cutoffBeforeDT = DateTime.parse(cutoffBefore + "-0700", ISODateTimeFormat.dateTimeNoMillis());
			}
		} catch (Exception e) {
			log.debug("couldn't parse cutoffBefore: " + cutoffBefore);
		}
		try {
			if (null != cutoffAfter) {
				cutoffAfterDT = DateTime.parse(cutoffAfter + "-0700", ISODateTimeFormat.dateTimeNoMillis());
			}
		} catch (Exception e) {
			log.debug("couldn't parse cutoffAfter: " + cutoffAfter);
		}
		
		result = new TimeConfig(dateRange, interpDateRange, downscaleDateRange, cutoffBeforeDT, cutoffAfterDT, tz);
		
		return result;
	}
	
	public static List<String> getStations(ListMultimap<String, String> params) {
		List<String> result = new ArrayList<String>();
		Set<String> toGo = new HashSet<String>();
		
		List<String> columnNames = params.get(COLUMN_KEYWORD);
		for (String colName : columnNames) {
			String station = getStation(colName);
			if (null != station) {
				toGo.add(station);
			}
		}
		
		result.addAll(toGo);
		return result;
	}
	public static String getStation(String colName) {
		String result = null;
		
		String stripped = stripColName(colName);
		String restOfStation = colName.substring(stripped.length());
		String[] otherThings = restOfStation.split("!");
		if (1 < otherThings.length) {
			result = otherThings[1];
		}
		
		return result;
	}
	
	public static String getCustomName(String colName) {
		String result = null;
		
		String stripped = stripColName(colName);
		String restOfStation = colName.substring(stripped.length());
		String[] otherThings = restOfStation.split("!");
		if (2 < otherThings.length) {
			result = otherThings[2];
		}
		
		return result;
	}
	
	public static String stripColName(String colName) {
		String result = colName;
		
		String cleanName = StringUtils.trimToNull(colName);
		if (null != cleanName) {
			int colLength = columnIdentifierLength;
			if (!cleanName.startsWith("inst")) {
				colLength--;
			}
			String[] tings = cleanName.split("!", colLength + 1);
			if (tings.length > colLength) {
				//Strip the extra tings
				int restOfInfo = cleanName.indexOf(tings[colLength]) - 1;
				if (0 < restOfInfo) {
					result = cleanName.substring(0, restOfInfo);
				} else { 
					log.error("could not find rest of string in column name");
				}
			} else {
				log.trace("No extra tings.");
			}
		}
		
		return result;
	}
	
	public ColumnMetadata getColumnMetadata(String uncleanName) {
		ColumnMetadata result = null;
		
		String cleanName = stripColName(uncleanName);
		if (null != cleanName) {
			result = CM_LOOKUP.get(cleanName);
		}
		
		return result;
	}
	
	public static boolean getIsDownload(ListMultimap<String, String> params) {
		boolean result = false;
		
		String userDownload = getParameter(params, DOWNLOAD_KEYWORD, "false");
		if (!StringUtils.equalsIgnoreCase(userDownload, "false")) {
			result = true;
		}
		
		return result;
	}
}
