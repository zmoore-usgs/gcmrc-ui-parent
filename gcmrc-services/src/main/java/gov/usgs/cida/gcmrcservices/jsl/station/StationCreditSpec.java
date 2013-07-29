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
public class StationCreditSpec extends Spec {
	private static final Logger log = LoggerFactory.getLogger(StationCreditSpec.class);

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
		ColumnMapping[] result = new ColumnMapping[] {
			new ColumnMapping(C_SITE_NO, S_SITE_NO),
			new ColumnMapping(C_SHORT_NM, S_SHORT_NM),
			new ColumnMapping(C_ORG_CD, S_ORG_CD),
			new ColumnMapping(C_DISPLAY_ORDER, S_DISPLAY_ORDER)
		};
		
		return result;
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
		SearchMapping[] result = new SearchMapping[] {
			new SearchMapping(S_SITE_NO, C_SITE_NO, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(S_SHORT_NM, C_SHORT_NM, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(S_SITE, C_SITE_NO + "," + C_SHORT_NM, null, WhereClauseType.equals, null, null, null)
		};
		
		return result;
	}

	@Override
	public String setupTableName() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("(");
		sb.append(" SELECT");
		sb.append("  S.NWIS_SITE_NO SITE_NO,");
		sb.append("  S.SITE_SHORT_NM SHORT_NM,");
		sb.append("  CCS.PROTO_ORG_CD ORG_CD,");
		sb.append("  CCS.PRIORITY_VA DISPLAY_ORDER");
		sb.append(" FROM");
		sb.append("  COLLECTION_CRED_SITE CCS,");
		sb.append("  SITE S");
		sb.append(" WHERE");
		sb.append("  CCS.SITE_ID = S.SITE_ID");
		sb.append(") T_A_CREDIT");
		
		return sb.toString();
	}
	
	public static final String S_SITE = "site";
	public static final String C_SITE_NO = "SITE_NO";
	public static final String S_SITE_NO = "nwisSite";
	public static final String C_SHORT_NM = "SHORT_NM";
	public static final String S_SHORT_NM = "shortName";
	public static final String C_ORG_CD = "ORG_CD";
	public static final String S_ORG_CD = "orgCode";
	public static final String C_DISPLAY_ORDER = "DISPLAY_ORDER";
	public static final String S_DISPLAY_ORDER = "displayOrder";
}
