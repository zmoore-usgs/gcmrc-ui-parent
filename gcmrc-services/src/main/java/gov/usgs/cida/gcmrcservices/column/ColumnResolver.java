package gov.usgs.cida.gcmrcservices.column;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.usgs.cida.gcmrcservices.jsl.data.ParameterCode;
import gov.usgs.cida.gcmrcservices.jsl.data.ParameterSpec;
import gov.usgs.cida.gcmrcservices.jsl.data.QWDataSpec;
import gov.usgs.cida.gcmrcservices.jsl.data.SpecOptions;
import gov.usgs.cida.gcmrcservices.jsl.station.StationBSSpec;
import gov.usgs.cida.nude.column.Column;
import gov.usgs.cida.nude.column.SimpleColumn;
import gov.usgs.cida.nude.out.Closers;
import gov.usgs.cida.nude.provider.sql.ParameterizedString;
import gov.usgs.cida.nude.provider.sql.SQLProvider;
import gov.usgs.cida.nude.resultset.inmemory.TableRow;
import gov.usgs.webservices.jdbc.spec.Spec;

public class ColumnResolver {

	private static final Logger log = LoggerFactory.getLogger(ColumnResolver.class);

	public static final int columnIdentifierLength = 2; // HACK HAAAAAAAAAAAAAACK

	private SQLProvider sqlProvider;

	public ColumnResolver(SQLProvider sqlProvider) {
		this.sqlProvider = sqlProvider;
	}
	
	protected static String stripColName(String colName) {
		String result = colName;
		String cleanName = StringUtils.trimToNull(colName);
		if (null != cleanName) {
			int colLength = columnIdentifierLength;
			if (cleanName.startsWith("time")) {
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

	public ColumnMetadata resolveColumn(String uncleanName) {
		ColumnMetadata result = null;
//		CM_LOOKUP.putAll(buildInstantaneousParametersCols(sqlProvider));
//		CM_LOOKUP.putAll(buildAncillaryCols(sqlProvider));
//		String cleanName = stripColName(uncleanName);
//		if (null != cleanName) {
//			result = CM_LOOKUP.get(cleanName);
//		}

		return result;
	}
	
	public Spec createSpecs(String colName, SpecOptions specOptions) {
		Spec result = null;
		
		ColumnMetadata cmd = resolveColumn(colName);
		String station = ColumnResolver.getStation(colName);

		if (null != cmd && null != station) {
			List<ColumnMetadata.SpecEntry> specEntries = cmd.getSpecEntries();
			for (ColumnMetadata.SpecEntry se : specEntries) {
				Map<String, String[]> modMap = new HashMap<String, String[]>();
				modMap.put(ParameterSpec.S_SITE_NAME, new String[] {station});
				modMap.put(ParameterSpec.S_GROUP_NAME, new String[] {se.parameterCode.groupName});
				modMap.put(QWDataSpec.S_SAMPLE_METHOD, new String[] {se.parameterCode.sampleMethod});
				Spec spec = se.getSpec(station, specOptions);
				Spec.loadParameters(spec, modMap);
				result = spec;
			}
		} else {
			log.debug("No column by the name of: " + colName);
		}
		
		return result;
	}
	
	protected Map<String, ColumnMetadata> buildInstantaneousParametersCols() {
		Map<String, ColumnMetadata> result = new HashMap<String, ColumnMetadata>();
		ResultSet rs = null;
		try {
			Column tsGrpNm = new SimpleColumn("NAME");
			Column displayName = new SimpleColumn("NAME_DISPLAY");
			Column unitsShort = new SimpleColumn("UNITS_NAME_SHORT");
			
			ParameterizedString ps = new ParameterizedString();
			ps.append("  SELECT DISTINCT GROUP_ID,");
			ps.append("    NAME,");
			ps.append("    NAME_DISPLAY,");
			ps.append("    UNITS_NAME,");
			ps.append("    UNITS_NAME_SHORT");
			ps.append("  FROM GROUP_NAME");
			
			rs = sqlProvider.getResults(null, ps);
			while (rs.next()) {
				TableRow row = TableRow.buildTableRow(rs);
				
				String columnTsGroupName = row.getValue(tsGrpNm);
				String parameterCode = "inst!" + columnTsGroupName;
				
				String columnDisplayName = row.getValue(displayName);
				
				String columnTitle = columnDisplayName + "(" + row.getValue(unitsShort) + ")";
				
				result.put(parameterCode, new ColumnMetadata(parameterCode, columnTitle, 
						new ColumnMetadata.SpecEntry(ParameterCode.parseParameterCode(parameterCode), ColumnMetadata.SpecEntry.SpecType.PARAM)));
			}
			log.debug("Instantaneous parameter columns constructed : " + result.keySet().toString());
		} catch (Exception e) {
			log.error("Could not get columns", e);
		} finally {
			Closers.closeQuietly(rs);
		}
		return result;
	}
	
	public Map<String, ColumnMetadata> buildBedMaterialParametersCols() {
		Map<String, ColumnMetadata> result = new HashMap<String, ColumnMetadata>();
		ResultSet rs = null;
		try {
			Column tsGrpNm = new SimpleColumn("NAME");
			Column displayName = new SimpleColumn("NAME_DISPLAY");
			Column unitsShort = new SimpleColumn("UNITS_NAME_SHORT");
			
			ParameterizedString ps = new ParameterizedString();
			ps.append("  SELECT DISTINCT GROUP_ID,");
			ps.append("    NAME,");
			ps.append("    NAME_DISPLAY,");
			ps.append("    UNITS_NAME,");
			ps.append("    UNITS_NAME_SHORT");
			ps.append("  FROM GROUP_NAME");
			ps.append("  WHERE GROUP_ID IN (").append(String.join(",",StationBSSpec.BS_GROUP_ID_LIST)).append(")");
			
			rs = sqlProvider.getResults(null, ps);
			while (rs.next()) {
				TableRow row = TableRow.buildTableRow(rs);
				
				String columnTsGroupName = row.getValue(tsGrpNm);
				String parameterCode = "bed!" + columnTsGroupName;
				
				String columnDisplayName = row.getValue(displayName);
				
				String columnTitle = columnDisplayName + "(" + row.getValue(unitsShort) + ")";
				
				result.put(parameterCode, new ColumnMetadata(parameterCode, columnTitle, 
						new ColumnMetadata.SpecEntry(ParameterCode.parseParameterCode(parameterCode), ColumnMetadata.SpecEntry.SpecType.BEDMATERIAL)));
			}
			log.debug("Bed Material parameter columns constructed : " + result.keySet().toString());
		} catch (Exception e) {
			log.error("Could not get columns", e);
		} finally {
			Closers.closeQuietly(rs);
		}
		return result;
	}
	
	public Map<String, ColumnMetadata> buildQWParametersCols() {
		Map<String, ColumnMetadata> result = new HashMap<String, ColumnMetadata>();
		ResultSet rs = null;
		try {
			Column sampleMethod = new SimpleColumn("SAMPLE_METHOD");
			Column pCode = new SimpleColumn("NAME");
			Column displayName = new SimpleColumn("NAME_DISPLAY");
			Column unitsShort = new SimpleColumn("UNITS_NAME_SHORT");
			
			ParameterizedString ps = new ParameterizedString();
			ps.append("SELECT");
			ps.append("  QWP.SAMPLE_METHOD,");
			ps.append("  G.NAME,");
			ps.append("  G.NAME_DISPLAY,");
			ps.append("  G.UNITS_NAME_SHORT");
			ps.append(" FROM ");
			ps.append("  QW_POR_STAR QWP,");
			ps.append("  GROUP_NAME G");
			ps.append(" WHERE");
			ps.append("  QWP.GROUP_ID = G.GROUP_ID");
			ps.append(" GROUP BY ");
			ps.append("  G.NAME,");
			ps.append("  QWP.SAMPLE_METHOD,");
			ps.append("  G.NAME_DISPLAY,");
			ps.append("  G.UNITS_NAME_SHORT");
			
			rs = sqlProvider.getResults(null, ps);
			while (rs.next()) {
				TableRow row = TableRow.buildTableRow(rs);
				
				String parameterCode = row.getValue(sampleMethod) + "!" + row.getValue(pCode);
				String columnTitle = row.getValue(sampleMethod) + " Sampled " + row.getValue(displayName) + "(" + row.getValue(unitsShort) + ")";
				
				result.put(parameterCode, new ColumnMetadata(parameterCode, columnTitle, 
						new ColumnMetadata.SpecEntry(ParameterCode.parseParameterCode(parameterCode), ColumnMetadata.SpecEntry.SpecType.LABDATA)));
			}
			log.debug("QW parameter columns constructed : " + result.keySet().toString());
		} catch (Exception e) {
			log.error("Could not get columns", e);
		} finally {
			Closers.closeQuietly(rs);
		}
		return result;
	}
	
	protected Map<String, ColumnMetadata> buildAncillaryCols() {
		Map<String, ColumnMetadata> result = new HashMap<String, ColumnMetadata>();
		ResultSet rs = null;
		try {
			Column displayName = new SimpleColumn("ANCILLARY_NAME");
			Column serviceName = new SimpleColumn("ANCILLARY_SERVICE_COLUMN");
			
			ParameterizedString ps = new ParameterizedString();
			ps.append("SELECT");
			ps.append("  ANC.ANCILLARY_NAME,");
			ps.append("  ANC.ANCILLARY_SERVICE_COLUMN");
			ps.append(" FROM ");
			ps.append("  ANCILLARY_COLUMN ANC");
			ps.append(" GROUP BY ");
			ps.append("  ANC.ANCILLARY_NAME,");
			ps.append("  ANC.ANCILLARY_SERVICE_COLUMN");
			
			rs = sqlProvider.getResults(null, ps);
			while (rs.next()) {
				TableRow row = TableRow.buildTableRow(rs);
				
				String parameterCode = row.getValue(serviceName);
				String columnTitle = row.getValue(displayName);
				
				result.put(parameterCode, new ColumnMetadata(parameterCode, columnTitle, 
						new ColumnMetadata.SpecEntry(ParameterCode.parseParameterCode(parameterCode), ColumnMetadata.SpecEntry.SpecType.PARAM)));
			}
			log.debug("Ancillary data columns constructed : " + result.keySet().toString());
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
	public Map<String, ColumnMetadata> buildCumulativeParametersCols() {
		Map<String, ColumnMetadata> result = new HashMap<String, ColumnMetadata>();
		
		//WAYYY HAAACK
		result.put("inst!S Sand Cumul Load", new ColumnMetadata("inst!S Sand Cumul Load", "Cumulative Suspended Sand Load (Metric Tons)", 
				new ColumnMetadata.SpecEntry(ParameterCode.parseParameterCode("inst!S Sand Cumul Load"), ColumnMetadata.SpecEntry.SpecType.PARAM)));
		result.put("inst!Minor Trib S Sand Cumul Load", new ColumnMetadata("inst!Minor Trib S Sand Cumul Load", "Cumulative Suspended Sand Load (Metric Tons)", 
				new ColumnMetadata.SpecEntry(ParameterCode.parseParameterCode("inst!Minor Trib S Sand Cumul Load"), ColumnMetadata.SpecEntry.SpecType.PARAM)));
		result.put("inst!S Fines Cumul Load", new ColumnMetadata("inst!S Fines Cumul Load", "Cumulative Silt-and-Clay Load (Metric Tons)", 
				new ColumnMetadata.SpecEntry(ParameterCode.parseParameterCode("inst!S Fines Cumul Load"), ColumnMetadata.SpecEntry.SpecType.PARAM)));
		result.put("inst!Minor Trib S Fines Cumul Load", new ColumnMetadata("inst!Minor Trib S Fines Cumul Load", "Cumulative Silt-and-Clay Load (Metric Tons)", 
				new ColumnMetadata.SpecEntry(ParameterCode.parseParameterCode("inst!Minor Trib S Fines Cumul Load"), ColumnMetadata.SpecEntry.SpecType.PARAM)));
		
		//ugh. this is horrible.
		result.put("inst!Sand Cumul Load", new ColumnMetadata("inst!Sand Cumul Load", "Cumulative Sand Load (Metric Tons)", 
				new ColumnMetadata.SpecEntry(ParameterCode.parseParameterCode("inst!Sand Cumul Load"), ColumnMetadata.SpecEntry.SpecType.PARAM)));
                
         //need a shower after this.
         result.put("inst!Calc Cumul Sand Bedload", new ColumnMetadata("inst!Calc Cumul Sand Bedload", "Calculated Cumulative Sand Bedload (Metric Tons)", 
        		 new ColumnMetadata.SpecEntry(ParameterCode.parseParameterCode("inst!Calc Cumul Sand Bedload"), ColumnMetadata.SpecEntry.SpecType.PARAM)));
		
		return result;
	}
	
	/**
	 * Cols for discharge error values
	 * @return 
	 */
	public Map<String, ColumnMetadata> buildQErrorCols() {
		Map<String, ColumnMetadata> result = new HashMap<String, ColumnMetadata>();
		
		Column methodCol = new SimpleColumn("METHOD");
		ResultSet rs = null;
		try {
			ParameterizedString ps = new ParameterizedString();
			ps.append("SELECT DISTINCT");
			ps.append("  METHOD");
			ps.append(" FROM ");
			ps.append("  DISCHARGE_ERROR_DATA");
			
			rs = sqlProvider.getResults(null, ps);
			while (rs.next()) {
				TableRow row = TableRow.buildTableRow(rs);
				
				String method = row.getValue(methodCol); //TODO may need to clean/camelCaseThis

				result.put(method + "!Discharge", 
						new ColumnMetadata(method + "!Discharge", "Discharge Measurements and Associated Error (" + method + ")", 
						new ColumnMetadata.SpecEntry(ParameterCode.parseParameterCode(method + "!Discharge"), 
								ColumnMetadata.SpecEntry.SpecType.DISCHARGEERROR)));
			}
			log.debug("Discharge Measurements data columns constructed : " + result.keySet().toString());
		} catch (Exception e) {
			log.error("Could not get columns", e);
		} finally {
			Closers.closeQuietly(rs);
		}
		return result;
	}
}
