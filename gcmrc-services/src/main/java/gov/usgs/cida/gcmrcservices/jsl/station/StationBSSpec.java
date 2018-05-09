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
public class StationBSSpec extends GCMRCSpec {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(StationBSSpec.class);
	private static final long serialVersionUID = 153252L;

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
			new SearchMapping(S_GROUP_NAME, C_GROUP_NAME, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(S_DISPLAY_NAME, C_DISPLAY_NAME, null, WhereClauseType.equals, null, null, null)
		};
	}

	@Override
	public String setupTableName() {
		StringBuilder result = new StringBuilder();
		
		result.append("  (");
		result.append("  SELECT BS.GROUP_ID,");
		result.append("    TO_CHAR(EARLIEST_DT, 'YYYY-MM-DD') START_DT,");
		result.append("    TO_CHAR(LATEST_DT, 'YYYY-MM-DD') END_DT,");
		result.append("    NVL(S.NWIS_SITE_NO, S.SHORT_NAME) AS SITE_NAME,");
		result.append("    G.NAME_DISPLAY DISPLAY_NAME,");
		result.append("    G.NAME GROUP_NAME,");
		result.append("    G.UNITS_NAME UNITS,");
		result.append("    G.UNITS_NAME_SHORT UNITS_SHORT,");
		result.append("    G.DECIMAL_PLACES,");
		result.append("    G.DISPLAY_ORDER,");
		result.append("    'Y' IS_VISIBLE");
		result.append("  FROM ");
		result.append("  (SELECT ");
		result.append("    SITE_ID,");
		result.append("    MIN(BED_MEAS_DT) EARLIEST_DT,");
		result.append("    MAX(BED_MEAS_DT) LATEST_DT,");
		result.append("    GROUP_ID");
		result.append("  FROM BED_MATERIAL");
		result.append("  GROUP BY SITE_ID, GROUP_ID) BS,");
		result.append("    SITE_STAR S,");
		result.append("    GROUP_NAME G");
		result.append("  WHERE BS.SITE_ID = S.SITE_ID");
		result.append("  AND BS.GROUP_ID = G.GROUP_ID");
		result.append("  AND BS.GROUP_ID IN (").append(String.join(",", BS_GROUP_ID_LIST)).append(")");
		result.append("  ) T_A_BED_SEDIMENT");
		
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
	public static final String[] BS_GROUP_ID_LIST = {"15","18"};
}
