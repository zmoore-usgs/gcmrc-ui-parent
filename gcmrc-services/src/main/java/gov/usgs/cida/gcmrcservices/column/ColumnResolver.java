package gov.usgs.cida.gcmrcservices.column;

import gov.usgs.cida.gcmrcservices.jsl.data.ParameterCode;
import gov.usgs.cida.gcmrcservices.jsl.data.ParameterSpec;
import gov.usgs.cida.gcmrcservices.jsl.data.QWDataSpec;
import gov.usgs.cida.gcmrcservices.jsl.data.SpecOptions;
import gov.usgs.cida.nude.column.Column;
import gov.usgs.cida.nude.column.SimpleColumn;
import gov.usgs.cida.nude.out.Closers;
import gov.usgs.cida.nude.provider.sql.ParameterizedString;
import gov.usgs.cida.nude.provider.sql.SQLProvider;
import gov.usgs.cida.nude.resultset.inmemory.TableRow;
import gov.usgs.webservices.jdbc.spec.Spec;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class ColumnResolver {

	private static final Logger log = LoggerFactory.getLogger(ColumnResolver.class);

	private static ColumnResolver resolver = null;
	protected Map<String, ColumnMetadata> CM_LOOKUP;
	protected Map<String, ColumnMetadata> qwColumnMetadatas;
	protected Map<String, ColumnMetadata> cumulativeColumnMetadatas;
	protected Map<String, ColumnMetadata> ancillaryColumnMetadatas;
	protected Map<String, ColumnMetadata> bedSedimentColumnMetadatas;

	public static final int columnIdentifierLength = 2; // HACK HAAAAAAAAAAAAAACK

	private ColumnResolver(SQLProvider sqlProvider) {
		CM_LOOKUP = new HashMap<String, ColumnMetadata>();
		CM_LOOKUP.putAll(buildInstantaneousParametersCols(sqlProvider));
		qwColumnMetadatas = Collections.unmodifiableMap(buildQWParametersCols(sqlProvider));
		CM_LOOKUP.putAll(qwColumnMetadatas);
		ancillaryColumnMetadatas = Collections.unmodifiableMap(buildAncillaryCols(sqlProvider));
		CM_LOOKUP.putAll(ancillaryColumnMetadatas);
		bedSedimentColumnMetadatas = Collections.unmodifiableMap(buildBedMaterialParametersCols(sqlProvider));
		CM_LOOKUP.putAll(buildBedMaterialParametersCols(sqlProvider));
		CM_LOOKUP = Collections.unmodifiableMap(CM_LOOKUP);
		
		cumulativeColumnMetadatas = Collections.unmodifiableMap(buildCumulativeParametersCols());
	}
	
	public static Map<String, ColumnMetadata> getBedSedColumns(SQLProvider sqlProvider) {
		Map<String, ColumnMetadata> result = new HashMap<String, ColumnMetadata>();
		if (null == resolver) {
			resolver = new ColumnResolver(sqlProvider);
		}
		
		result.putAll(resolver.bedSedimentColumnMetadatas);
		
		return Collections.unmodifiableMap(result);
	}
	
	public static Map<String, ColumnMetadata> getQWColumns(SQLProvider sqlProvider) {
		Map<String, ColumnMetadata> result = new HashMap<String, ColumnMetadata>();
		if (null == resolver) {
			resolver = new ColumnResolver(sqlProvider);
		}
		
		result.putAll(resolver.qwColumnMetadatas);
		
		return Collections.unmodifiableMap(result);
	}
	
	public static Map<String, ColumnMetadata> getCumulativeColumns(SQLProvider sqlProvider) {
		Map<String, ColumnMetadata> result = new HashMap<String, ColumnMetadata>();
		if (null == resolver) {
			resolver = new ColumnResolver(sqlProvider);
		}
		
		result.putAll(resolver.cumulativeColumnMetadatas);
		
		return Collections.unmodifiableMap(result);
	}

	public static ColumnMetadata resolveColumn(String uncleanName, SQLProvider sqlProvider) {
		ColumnMetadata result = null;
		if (null == resolver) {
			resolver = new ColumnResolver(sqlProvider);
		}

		String cleanName = stripColName(uncleanName);
		if (null != cleanName) {
			result = resolver.CM_LOOKUP.get(cleanName);
		}

		return result;
	}
	
	public static Spec createSpecs(String colName, SpecOptions specOptions, SQLProvider sqlProvider) {
		Spec result = null;
		
		ColumnMetadata cmd = ColumnResolver.resolveColumn(colName, sqlProvider);
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
	
	protected static Map<String, ColumnMetadata> buildInstantaneousParametersCols(SQLProvider sqlProvider) {
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
	
	protected static Map<String, ColumnMetadata> buildBedMaterialParametersCols(SQLProvider sqlProvider) {
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
			ps.append("  WHERE GROUP_ID = 15");
			
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
			log.debug("Instantaneous parameter columns constructed : " + result.keySet().toString());
		} catch (Exception e) {
			log.error("Could not get columns", e);
		} finally {
			Closers.closeQuietly(rs);
		}
		return result;
	}
	
	protected static Map<String, ColumnMetadata> buildQWParametersCols(SQLProvider sqlProvider) {
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
	
	protected static Map<String, ColumnMetadata> buildAncillaryCols(SQLProvider sqlProvider) {
		Map<String, ColumnMetadata> result = new HashMap<String, ColumnMetadata>();
		String parameterCode;
		
		parameterCode = "notes!Discharge";
		result.put(parameterCode, new ColumnMetadata(parameterCode, "discharge notes", 
				new ColumnMetadata.SpecEntry(ParameterCode.parseParameterCode(parameterCode), ColumnMetadata.SpecEntry.SpecType.PARAM)));
		parameterCode = "iceAffected!Discharge";
		result.put(parameterCode, new ColumnMetadata(parameterCode, "discharge ice affected", 
				new ColumnMetadata.SpecEntry(ParameterCode.parseParameterCode(parameterCode), ColumnMetadata.SpecEntry.SpecType.PARAM)));
		
		parameterCode = "notes!Stage";
		result.put(parameterCode, new ColumnMetadata(parameterCode, "stage notes", 
				new ColumnMetadata.SpecEntry(ParameterCode.parseParameterCode(parameterCode), ColumnMetadata.SpecEntry.SpecType.PARAM)));
		parameterCode = "iceAffected!Stage";
		result.put(parameterCode, new ColumnMetadata(parameterCode, "stage ice affected", 
				new ColumnMetadata.SpecEntry(ParameterCode.parseParameterCode(parameterCode), ColumnMetadata.SpecEntry.SpecType.PARAM)));
		log.debug("Instantaneous ancillary columns constructed : " + result.keySet().toString());
		
		return result;
	}
	
	/**
	 * We need to know Cumulative Load parameters, so we can zero out the timeseries per request.
	 * @return 
	 */
	protected static Map<String, ColumnMetadata> buildCumulativeParametersCols() {
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
		
		
		return result;
	}
}
