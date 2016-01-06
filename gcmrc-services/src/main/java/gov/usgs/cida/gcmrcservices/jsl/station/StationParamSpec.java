package gov.usgs.cida.gcmrcservices.jsl.station;

import gov.usgs.webservices.jdbc.spec.Spec;
import gov.usgs.webservices.jdbc.spec.mapping.ColumnMapping;
import gov.usgs.webservices.jdbc.spec.mapping.SearchMapping;
import gov.usgs.webservices.jdbc.spec.mapping.WhereClauseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class StationParamSpec extends Spec {
	private static final Logger log = LoggerFactory.getLogger(StationParamSpec.class);

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
			new ColumnMapping(C_GROUP_NAME, S_GROUP_NAME),
			new ColumnMapping(C_START_DT, S_START_DT),
			new ColumnMapping(C_END_DT, S_END_DT),
			new ColumnMapping(C_SITE_NAME, S_SITE_NAME),
			new ColumnMapping(C_SITE_DISPLAY_NAME, S_SITE_DISPLAY_NAME),
			new ColumnMapping(C_DISPLAY_ORDER, S_DISPLAY_ORDER),
			new ColumnMapping(C_DISPLAY_NAME, S_DISPLAY_NAME),
			new ColumnMapping(C_IS_VISIBLE, S_IS_VISIBLE),
			new ColumnMapping(C_IS_DOWNLOADABLE, S_IS_DOWNLOADABLE),
			new ColumnMapping(C_UNITS, S_UNITS),
			new ColumnMapping(C_UNITS_SHORT, S_UNITS_SHORT),
			new ColumnMapping(C_DECIMAL_PLACES, S_DECIMAL_PLACES),
			new ColumnMapping(C_PPQ, S_PPQ)
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
			new SearchMapping(S_SITE_NAME, C_SITE_NAME, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(S_GROUP_NAME, C_GROUP_NAME, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(S_IS_VISIBLE, C_IS_VISIBLE, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(S_IS_DOWNLOADABLE, C_IS_DOWNLOADABLE, null, WhereClauseType.equals, null, null, null)
		};
	}

	@Override
	public String setupTableName() {
		StringBuilder result = new StringBuilder();

		result.append("  (SELECT /*+ OPT_PARAM('optimizer_features_enable' '11.2.0.3') */ T_POR.GROUP_ID,");
		result.append("    GROUP_NAME,");
		result.append("    SITE_NAME,");
		result.append("    SITE_DISPLAY_NAME,");
		result.append("    TO_CHAR(EARLIEST_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS') AS START_DT,");
		result.append("    TO_CHAR(LATEST_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS')   AS END_DT,");
		result.append("    DISPLAY_ORDER,");
		result.append("    DISPLAY_NAME,");
		result.append("    UNITS,");
		result.append("    UNITS_SHORT,");
		result.append("    DECIMAL_PLACES,");
		result.append("    DISPLAY AS IS_VISIBLE,");
		result.append("    DOWNLOADABLE AS IS_DOWNLOADABLE,");
		result.append("    TIME_SERIES_DISPLAY.DESCRIPTION");
		result.append("  FROM (SELECT SITE_ID, GROUP_ID, EARLIEST_DT, LATEST_DT FROM TIME_SERIES_POR");
		result.append("    ) T_POR");
		result.append("  LEFT OUTER JOIN");
		result.append("    (SELECT SITE_ID,");
		result.append("      CASE");
		result.append("        WHEN NWIS_SITE_NO IS NULL");
		result.append("        THEN SHORT_NAME");
		result.append("        ELSE NWIS_SITE_NO");
		result.append("      END AS SITE_NAME,");
		result.append("      NAME AS SITE_DISPLAY_NAME");
		result.append("    FROM SITE_STAR");
		result.append("    ) SITE");
		result.append("  ON T_POR.SITE_ID = SITE.SITE_ID");
		result.append("  LEFT OUTER JOIN");
		result.append("    (SELECT GROUP_ID,");
		result.append("      NAME AS GROUP_NAME,");
		result.append("      DISPLAY_ORDER,");
		result.append("      NAME_DISPLAY     AS DISPLAY_NAME,");
		result.append("      UNITS_NAME       AS UNITS,");
		result.append("      UNITS_NAME_SHORT AS UNITS_SHORT,");
		result.append("      DECIMAL_PLACES");
		result.append("    FROM GROUP_NAME");
		result.append("    ) T_GROUP");
		result.append("  ON T_POR.GROUP_ID = T_GROUP.GROUP_ID");
		result.append("  LEFT OUTER JOIN");
		result.append("    TIME_SERIES_DISPLAY");
		result.append("  ON TIME_SERIES_DISPLAY.GROUP_ID = T_POR.GROUP_ID");
		result.append("  AND TIME_SERIES_DISPLAY.SITE_ID = T_POR.SITE_ID");
		result.append("  ) T_A_MAIN");
		
		return result.toString();
	}

	public static final String C_GROUP_ID = "GROUP_ID";
	public static final String S_GROUP_ID = "groupId";
	public static final String C_GROUP_NAME = "GROUP_NAME";
	public static final String S_GROUP_NAME = "groupName";
	public static final String C_START_DT = "START_DT";
	public static final String S_START_DT = "beginPosition";
	public static final String C_END_DT = "END_DT";
	public static final String S_END_DT = "endPosition";
	public static final String C_SITE_NAME = "SITE_NAME";
	public static final String S_SITE_NAME = "site";
	public static final String C_SITE_DISPLAY_NAME = "SITE_DISPLAY_NAME";
	public static final String S_SITE_DISPLAY_NAME = "SiteDisplayName";
	public static final String C_DISPLAY_ORDER = "DISPLAY_ORDER";
	public static final String S_DISPLAY_ORDER = "displayOrder";
	public static final String C_DISPLAY_NAME = "DISPLAY_NAME";
	public static final String S_DISPLAY_NAME = "displayName";
	public static final String C_IS_VISIBLE = "IS_VISIBLE";
	public static final String S_IS_VISIBLE = "isVisible";
	public static final String C_IS_DOWNLOADABLE = "IS_DOWNLOADABLE";
	public static final String S_IS_DOWNLOADABLE = "isDownloadable";
	public static final String C_UNITS = "UNITS";
	public static final String S_UNITS = "units";
	public static final String C_UNITS_SHORT = "UNITS_SHORT";
	public static final String S_UNITS_SHORT = "unitsShort";
	public static final String C_DECIMAL_PLACES = "DECIMAL_PLACES";
	public static final String S_DECIMAL_PLACES = "decimalPlaces";
	public static final String C_PPQ = "DESCRIPTION";
	public static final String S_PPQ = "ppq";
}
