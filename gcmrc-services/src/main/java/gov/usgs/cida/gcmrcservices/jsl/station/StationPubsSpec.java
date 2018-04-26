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
public class StationPubsSpec extends GCMRCSpec {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(StationPubsSpec.class);

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
			new ColumnMapping(C_SITE, S_SITE),
			new ColumnMapping(C_URL, S_URL),
			new ColumnMapping(C_DISPLAY_NAME, S_DISPLAY_NAME),
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
			new SearchMapping(S_SITE, C_SITE, null, WhereClauseType.equals, null, null, null)
		};
		
		return result;
	}

	@Override
	public String setupTableName() {
		StringBuilder result = new StringBuilder();
		
		result.append("(");
		result.append(" SELECT");
		result.append(" CASE");
		result.append("   WHEN S.NWIS_SITE_NO IS NOT NULL");
		result.append("   THEN S.NWIS_SITE_NO");
		result.append("   ELSE S.SHORT_NAME");
		result.append("  END AS SITE_NM,");
		result.append("  P.PUB_LINK URL,");
		result.append("  P.PUB_NM DISPLAY_NAME,");
		result.append("  P.PRIORITY_VA DISPLAY_ORDER");
		result.append(" FROM");
		result.append("  PUB_SITE P,");
		result.append("  SITE_STAR S");
		result.append(" WHERE");
		result.append("  P.SITE_ID = S.SITE_ID");
		result.append(") T_A_PUBS");
		
		return result.toString();
	}
	
	public static final String C_SITE = "SITE_NM";
	public static final String S_SITE = "site";
	public static final String C_URL = "URL";
	public static final String S_URL = "url";
	public static final String C_DISPLAY_NAME = "DISPLAY_NAME";
	public static final String S_DISPLAY_NAME = "displayName";
	public static final String C_DISPLAY_ORDER = "DISPLAY_ORDER";
	public static final String S_DISPLAY_ORDER = "displayOrder";
}
