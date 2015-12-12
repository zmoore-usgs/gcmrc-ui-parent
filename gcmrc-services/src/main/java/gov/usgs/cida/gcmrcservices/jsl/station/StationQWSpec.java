package gov.usgs.cida.gcmrcservices.jsl.station;

import gov.usgs.webservices.jdbc.spec.GCMRCSpec;
import gov.usgs.webservices.jdbc.spec.mapping.ColumnMapping;
import gov.usgs.webservices.jdbc.spec.mapping.SearchMapping;
import gov.usgs.webservices.jdbc.spec.mapping.WhereClauseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class StationQWSpec extends GCMRCSpec {
	private static final Logger log = LoggerFactory.getLogger(StationQWSpec.class);

	@Override
	public boolean setupAccess_DELETE() {
		return false;
	}

	@Override
	public boolean setupAccess_INSERT() {
		return false;
	}

	@Override
	public boolean setupAccess_READ() {
		return true;
	}

	@Override
	public boolean setupAccess_UPDATE() {
		return false;
	}

	@Override
	public ColumnMapping[] setupColumnMap() {
		return new ColumnMapping[] {
			new ColumnMapping(C_GROUP_ID, S_GROUP_ID),
			new ColumnMapping(C_START_DT, S_START_DT),
			new ColumnMapping(C_END_DT, S_END_DT),
			new ColumnMapping(C_SITE_NAME, S_SITE_NAME),
			new ColumnMapping(C_SAMPLE_METHOD, S_SAMPLE_METHOD),
			new ColumnMapping(C_USE_PUMP_COLORING, S_USE_PUMP_COLORING),
			new ColumnMapping(C_GROUP_NAME, S_GROUP_NAME),
			new ColumnMapping(C_DISPLAY_NAME, S_DISPLAY_NAME),
			new ColumnMapping(C_UNITS, S_UNITS),
			new ColumnMapping(C_UNITS_SHORT, S_UNITS_SHORT),
			new ColumnMapping(C_DECIMAL_PLACES, S_DECIMAL_PLACES),
			new ColumnMapping(C_DISPLAY_ORDER, S_DISPLAY_ORDER),
			new ColumnMapping(C_IS_VISIBLE, S_IS_VISIBLE)
		};
	}

	@Override
	public String setupDocTag() {
		return "success";
	}

	@Override
	public String setupRowTag() {
		return "data";
	}

	@Override
	public SearchMapping[] setupSearchMap() {
		return new SearchMapping[] {
			new SearchMapping(S_GROUP_ID, C_GROUP_ID, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(S_SITE_NAME, C_SITE_NAME, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(S_SAMPLE_METHOD, C_SAMPLE_METHOD, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(S_GROUP_NAME, C_GROUP_NAME, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(S_DISPLAY_NAME, C_DISPLAY_NAME, null, WhereClauseType.equals, null, null, null)
		};
	}

	@Override
	public String setupTableName() {
		StringBuilder result = new StringBuilder();
		
		result.append("(");
		result.append("  SELECT QWP.GROUP_ID,");
		result.append("    TO_CHAR(EARLIEST_DT, 'YYYY-MM-DD') START_DT,");
		result.append("    TO_CHAR(LATEST_DT, 'YYYY-MM-DD') END_DT,");
		result.append("    SITE_NAME,");
		result.append("    QWP.SAMPLE_METHOD SAMPLE_METHOD,");
		result.append("    QWP.USE_PUMP_COLORING USE_PUMP_COLORING,");
		result.append("    QWP.GROUP_NAME DISPLAY_NAME,");
		result.append("    G.NAME GROUP_NAME,");
		result.append("    G.UNITS_NAME UNITS,");
		result.append("    G.UNITS_NAME_SHORT UNITS_SHORT,");
		result.append("    G.DECIMAL_PLACES,");
		result.append("    G.DISPLAY_ORDER,");
		result.append("    'Y' IS_VISIBLE");
		result.append("  FROM QW_POR_STAR QWP,");
		result.append("    GROUP_NAME G");
		result.append("  WHERE QWP.GROUP_ID = G.GROUP_ID");
		result.append("  AND QWP.GROUP_ID IN (89, 90, 92)");
		result.append(") T_A_SUMMARY");
		
		return result.toString();
	}

	public static final String S_GROUP_ID = "groupId";
	public static final String C_GROUP_ID = "GROUP_ID";
	public static final String S_START_DT = "beginPosition";
	public static final String C_START_DT = "START_DT";
	public static final String S_END_DT = "endPosition";
	public static final String C_END_DT = "END_DT";
	public static final String S_SITE_NAME = "site";
	public static final String C_SITE_NAME = "SITE_NAME";
	public static final String S_SAMPLE_METHOD = "sampleMethod";
	public static final String C_SAMPLE_METHOD = "SAMPLE_METHOD";
	public static final String S_USE_PUMP_COLORING = "usePumpColoring";
	public static final String C_USE_PUMP_COLORING = "USE_PUMP_COLORING";
	public static final String S_DISPLAY_NAME = "displayName";
	public static final String C_DISPLAY_NAME = "DISPLAY_NAME";
	public static final String S_GROUP_NAME = "groupName";
	public static final String C_GROUP_NAME = "GROUP_NAME";
	public static final String S_UNITS = "units";
	public static final String C_UNITS = "UNITS";
	public static final String S_UNITS_SHORT = "unitsShort";
	public static final String C_UNITS_SHORT = "UNITS_SHORT";
	public static final String S_DECIMAL_PLACES = "decimalPlaces";
	public static final String C_DECIMAL_PLACES = "DECIMAL_PLACES";
	public static final String S_DISPLAY_ORDER = "displayOrder";
	public static final String C_DISPLAY_ORDER = "DISPLAY_ORDER";
	public static final String S_IS_VISIBLE = "isVisible";
	public static final String C_IS_VISIBLE = "IS_VISIBLE";
}
