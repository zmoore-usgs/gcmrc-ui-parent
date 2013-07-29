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
			new ColumnMapping(C_PCODE, S_PCODE),
			new ColumnMapping(C_TS_GRP_NM, S_TS_GRP_NM),
			new ColumnMapping(C_START_DT, S_START_DT),
			new ColumnMapping(C_END_DT, S_END_DT),
			new ColumnMapping(C_SITE_NO, S_SITE_NO),
			new ColumnMapping(C_SHORT_NM, S_SHORT_NM),
			new ColumnMapping(C_DISPLAY_ORDER, S_DISPLAY_ORDER),
			new ColumnMapping(C_DISPLAY_NAME, S_DISPLAY_NAME),
			new ColumnMapping(C_PUBLIC_PORTAL_QUALIFIER, S_PUBLIC_PORTAL_QUALIFIER),
			new ColumnMapping(C_UNITS, S_UNITS),
			new ColumnMapping(C_UNITS_SHORT, S_UNITS_SHORT),
			new ColumnMapping(C_DECIMAL_PLACES, S_DECIMAL_PLACES)
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
			new SearchMapping(S_PCODE, C_PCODE, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(S_SITE_NO, C_SITE_NO, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(S_SHORT_NM, C_SHORT_NM, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(S_SITE, C_SITE_NO + "," + C_SHORT_NM, null, WhereClauseType.equals, null, null, null)
		};
	}

	@Override
	public String setupTableName() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("(SELECT TGP.PARM_CD PCODE,");
		sb.append("    TGP.TS_GRP_NM,");
		sb.append("    TO_CHAR(TGP.EARLIEST_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS') START_DT,");
		sb.append("    TO_CHAR(TGP.LATEST_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS') END_DT,");
		sb.append("    S.NWIS_SITE_NO SITE_NO,");
		sb.append("    S.SITE_SHORT_NM SHORT_NM,");
		sb.append("    IPTG.PORTAL_TS_PUBLIC_DS PUBLIC_PORTAL_QUALIFIER,");
		sb.append("    IPTG.DISPLAY_ORDER_VA DISPLAY_ORDER,");
		sb.append("    P.PARM_ESSENCE_NM DISPLAY_NAME,");
		sb.append("    P.UNITS_NM UNITS,");
		sb.append("    P.UNITS_SHORT_NM UNITS_SHORT,");
		sb.append("    P.BASIC_NUM_DEC_PLACES_VA DECIMAL_PLACES");
		sb.append("  FROM TS_GROUP_POR TGP,");
		sb.append("    PARM P,");
		sb.append("    SITE S,");
		sb.append("    INFO_PORTAL_TS_GROUP IPTG");
		sb.append("  WHERE TGP.SITE_ID = S.SITE_ID");
		sb.append("  AND IPTG.SITE_ID = TGP.SITE_ID");
		sb.append("  AND IPTG.TS_GRP_NM = TGP.TS_GRP_NM");
		sb.append("  AND IPTG.PARM_CD = TGP.PARM_CD");
		sb.append("  AND TGP.PARM_CD = P.PARM_CD");
		sb.append("  AND IPTG.PRIORITY_VA <= 10");
		sb.append("  ) T_A_SUMMARY");
		
		return sb.toString();
	}

	
	public static final String S_START_DT = "beginPosition";
	public static final String C_START_DT = "START_DT";
	public static final String S_END_DT = "endPosition";
	public static final String C_END_DT = "END_DT";
	public static final String S_PCODE = "pCode";
	public static final String C_PCODE = "PCODE";
	public static final String S_TS_GRP_NM = "tsGroup";
	public static final String C_TS_GRP_NM = "TS_GRP_NM";
	public static final String S_SITE = "site";
	public static final String S_SITE_NO = "nwisSite";
	public static final String C_SITE_NO = "SITE_NO";
	public static final String S_SHORT_NM = "shortName";
	public static final String C_SHORT_NM = "SHORT_NM";
	public static final String S_PUBLIC_PORTAL_QUALIFIER = "ppq";
	public static final String C_PUBLIC_PORTAL_QUALIFIER = "PUBLIC_PORTAL_QUALIFIER";
	public static final String S_DISPLAY_ORDER = "displayOrder";
	public static final String C_DISPLAY_ORDER = "DISPLAY_ORDER";
	public static final String S_DISPLAY_NAME = "displayName";
	public static final String C_DISPLAY_NAME = "DISPLAY_NAME";
	public static final String S_UNITS = "units";
	public static final String C_UNITS = "UNITS";
	public static final String S_UNITS_SHORT = "unitsShort";
	public static final String C_UNITS_SHORT = "UNITS_SHORT";
	public static final String S_DECIMAL_PLACES = "decimalPlaces";
	public static final String C_DECIMAL_PLACES = "DECIMAL_PLACES";
}
