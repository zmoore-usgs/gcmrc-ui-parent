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
public class StationQWSpec extends Spec {
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
			new ColumnMapping(C_PCODE, S_PCODE),
			new ColumnMapping(C_SAMPLE_METHOD, S_SAMPLE_METHOD),
			new ColumnMapping(C_START_DT, S_START_DT),
			new ColumnMapping(C_END_DT, S_END_DT),
			new ColumnMapping(C_SITE_NO, null),
			new ColumnMapping(C_SHORT_NM, null),
			new ColumnMapping(C_PCODE_NAME, S_PCODE_NAME),
			new ColumnMapping(C_DISPLAY_NAME, S_DISPLAY_NAME),
			new ColumnMapping(C_DISPLAY_ORDER, S_DISPLAY_ORDER),
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
			new SearchMapping(S_SITE, C_SITE_NO + "," + C_SHORT_NM, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(S_SAMPLE_METHOD, C_SAMPLE_METHOD, null, WhereClauseType.equals, null, null, null)
		};
	}

	@Override
	public String setupTableName() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("  (SELECT QWP.PARM_CD PCODE,");
		sb.append("    TO_CHAR(EARLIEST_DT, 'YYYY-MM-DD') START_DT,");
		sb.append("    TO_CHAR(LATEST_DT, 'YYYY-MM-DD') END_DT,");
		sb.append("    NWIS_SITE_NO SITE_NO,");
		sb.append("    SITE_SHORT_NM SHORT_NM,");
		sb.append("    QWP.SAMP_METH_CD SAMPLE_METHOD,");
		sb.append("    SMT.SAMP_METH_DISP_NM DISPLAY_NAME,");
		sb.append("    P.PARM_ESSENCE_NM PCODE_NAME,");
		sb.append("    P.UNITS_NM UNITS,");
		sb.append("    P.UNITS_SHORT_NM UNITS_SHORT,");
		sb.append("    P.BASIC_NUM_DEC_PLACES_VA DECIMAL_PLACES,");
		sb.append("    CASE");
		sb.append("      WHEN QWP.PARM_CD='80222'");
		sb.append("      THEN 700");
		sb.append("      WHEN QWP.PARM_CD='80220'");
		sb.append("      THEN 800");
		sb.append("      WHEN QWP.PARM_CD='100200'");
		sb.append("      THEN 900");
		sb.append("      ELSE 9999");
		sb.append("    END AS DISPLAY_ORDER");
		sb.append("  FROM QW_POR QWP,");
		sb.append("    SITE S,");
		sb.append("    PARM P,");
		sb.append("    SAMP_METH_TP SMT");
		sb.append("  WHERE QWP.SITE_ID = S.SITE_ID");
		sb.append("  AND QWP.PARM_CD = P.PARM_CD");
		sb.append("  AND QWP.SAMP_METH_CD = SMT.SAMP_METH_CD");
		sb.append("  ) T_A_SUMMARY");
		
		return sb.toString();
	}

	
	public static final String S_START_DT = "beginPosition";
	public static final String C_START_DT = "START_DT";
	public static final String S_END_DT = "endPosition";
	public static final String C_END_DT = "END_DT";
	public static final String S_PCODE = "pCode";
	public static final String C_PCODE = "PCODE";
	public static final String S_SITE = "site";
	public static final String S_SITE_NO = "nwisSite";
	public static final String C_SITE_NO = "SITE_NO";
	public static final String S_SHORT_NM = "shortName";
	public static final String C_SHORT_NM = "SHORT_NM";
	public static final String S_PUBLIC_PORTAL_QUALIFIER = "ppq";
	public static final String C_PUBLIC_PORTAL_QUALIFIER = "PUBLIC_PORTAL_QUALIFIER";
	public static final String S_DISPLAY_ORDER = "displayOrder";
	public static final String C_DISPLAY_ORDER = "DISPLAY_ORDER";
	public static final String S_SAMPLE_METHOD = "sampleMethod";
	public static final String C_SAMPLE_METHOD = "SAMPLE_METHOD";
	public static final String S_PCODE_NAME = "pCodeName";
	public static final String C_PCODE_NAME = "PCODE_NAME";
	public static final String S_DISPLAY_NAME = "displayName";
	public static final String C_DISPLAY_NAME = "DISPLAY_NAME";
	public static final String S_UNITS = "units";
	public static final String C_UNITS = "UNITS";
	public static final String S_UNITS_SHORT = "unitsShort";
	public static final String C_UNITS_SHORT = "UNITS_SHORT";
	public static final String S_DECIMAL_PLACES = "decimalPlaces";
	public static final String C_DECIMAL_PLACES = "DECIMAL_PLACES";
}
