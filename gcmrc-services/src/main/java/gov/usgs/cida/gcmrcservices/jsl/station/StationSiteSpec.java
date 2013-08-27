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
public class StationSiteSpec extends Spec {
	private static final Logger log = LoggerFactory.getLogger(StationSiteSpec.class);

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
			new ColumnMapping(C_SITE_ID, S_SITE_ID),
			new ColumnMapping(C_SITE_NAME, S_SITE_NAME),
			new ColumnMapping(C_DISPLAY_NAME, S_DISPLAY_NAME),
			new ColumnMapping(C_LAT, S_LAT),
			new ColumnMapping(C_LON, S_LON),
			new ColumnMapping(C_NET, S_NET),
			new ColumnMapping(C_DISPLAY_ORDER, S_DISPLAY_ORDER)
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
			new SearchMapping(S_NET, C_NET, null, WhereClauseType.equals, null, null, null)
		};
	}

	@Override
	public String setupTableName() {
		StringBuilder result = new StringBuilder();
		
		result.append("(SELECT DISTINCT");
		result.append("  TSD.SITE_ID,");
		result.append("  CASE");
		result.append("    WHEN NWIS_SITE_NO IS NULL");
		result.append("    THEN SHORT_NAME");
		result.append("    ELSE NWIS_SITE_NO");
		result.append("  END AS SITE_NAME,");
		result.append("  NAME AS DISPLAY_NAME,");
		result.append("  DECIMAL_LATITUDE LAT,");
		result.append("  DECIMAL_LONGITUDE LON,");
		result.append("  CASE");
		result.append("    WHEN NETWORK_NAME='GCDAMP'");
		result.append("    THEN 'GCDAMP'");
		result.append("    WHEN NETWORK_NAME='Dinosaur'");
		result.append("    THEN 'DINO'");
		result.append("    WHEN NETWORK_NAME='BigBend'");
		result.append("    THEN 'BIBE'");
		result.append("    ELSE 'GCDAMP'");
		result.append("  END AS NET,");
		result.append("  DISPLAY_ORDER");
		result.append("  FROM");
		result.append("    (SELECT");
		result.append("      SITE_ID,");
		result.append("      DISPLAY");
		result.append("    FROM");
		result.append("      TIME_SERIES_DISPLAY");
		result.append("    UNION");
		result.append("    SELECT DISTINCT");
		result.append("      QWP.SITE_ID,");
		result.append("      'Y' AS DISPLAY");
		result.append("    FROM");
		result.append("      QW_POR QWP");
		result.append("      ) TSD");
		result.append("  LEFT OUTER JOIN");
		result.append("    SITE_STAR");
		result.append("  ON");
		result.append("    TSD.SITE_ID = SITE_STAR.SITE_ID");
		result.append("  WHERE");
		result.append("    DISPLAY = 'Y') T_A_MAIN");
		
		return result.toString();
	}

	public static final String C_SITE_ID = "SITE_ID";
	public static final String S_SITE_ID = "";
	public static final String C_SITE_NAME = "SITE_NAME";
	public static final String S_SITE_NAME = "siteName";
	public static final String C_DISPLAY_NAME = "DISPLAY_NAME";
	public static final String S_DISPLAY_NAME = "displayName";
	public static final String C_LAT = "LAT";
	public static final String S_LAT = "lat";
	public static final String C_LON = "LON";
	public static final String S_LON = "lon";
	public static final String C_NET = "NET";
	public static final String S_NET = "network";
	public static final String C_DISPLAY_ORDER = "DISPLAY_ORDER";
	public static final String S_DISPLAY_ORDER = "displayOrder";
}
