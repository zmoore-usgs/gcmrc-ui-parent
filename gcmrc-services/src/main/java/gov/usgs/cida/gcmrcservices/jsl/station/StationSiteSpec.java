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
			new ColumnMapping(C_SITE_NO, S_SITE_NO),
			new ColumnMapping(C_SHORT_NM, S_SHORT_NM),
			new ColumnMapping(C_FULL_NM, S_FULL_NM),
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
		StringBuilder sb = new StringBuilder();
		
		sb.append("  (");
		sb.append("  SELECT DISTINCT S.NWIS_SITE_NO SITE_NO,");
		sb.append("    S.SITE_SHORT_NM SHORT_NM,");
		sb.append("    S.SITE_NM FULL_NM,");
		sb.append("    S.DEC_LAT_VA LAT,");
		sb.append("    S.DEC_LONG_VA LON,");
		sb.append("    S.DISPLAY_ORDER_VA DISPLAY_ORDER,");
		sb.append("    CASE");
		sb.append("      WHEN S.NETWORK_NM='GCDAMP'");
		sb.append("      THEN 'GCDAMP'");
		sb.append("      WHEN S.NETWORK_NM='Dinosaur'");
		sb.append("      THEN 'DINO'");
		sb.append("      WHEN S.NETWORK_NM='BigBend'");
		sb.append("      THEN 'BIBE'");
		sb.append("      ELSE 'GCDAMP'");
		sb.append("    END AS NET");
		sb.append("  FROM ");
		sb.append("    SITE S,");
		sb.append("    (SELECT ");
		sb.append("        DISTINCT IPTG.SITE_ID");
		sb.append("      FROM ");
		sb.append("        INFO_PORTAL_TS_GROUP IPTG");
		sb.append("      WHERE ");
		sb.append("        IPTG.PRIORITY_VA <= 10");
		sb.append("      UNION ");
		sb.append("      SELECT ");
		sb.append("        DISTINCT QWP.SITE_ID");
		sb.append("      FROM ");
		sb.append("      QW_POR QWP) ALLSITES");
		sb.append("  WHERE ALLSITES.SITE_ID = S.SITE_ID");
		sb.append("  ) T_A_SUMMARY");
		
		return sb.toString();
	}

	public static final String C_SITE_NO = "SITE_NO";
	public static final String S_SITE_NO = "nwisSite";
	public static final String C_SHORT_NM = "SHORT_NM";
	public static final String S_SHORT_NM = "shortName";
	public static final String C_FULL_NM = "FULL_NM";
	public static final String S_FULL_NM = "displayName";
	public static final String C_LAT = "LAT";
	public static final String S_LAT = "lat";
	public static final String C_LON = "LON";
	public static final String S_LON = "lon";
	public static final String C_NET = "NET";
	public static final String S_NET = "network";
	public static final String C_DISPLAY_ORDER = "DISPLAY_ORDER";
	public static final String S_DISPLAY_ORDER = "displayOrder";
}
