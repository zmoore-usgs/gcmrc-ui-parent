package gov.usgs.cida.gcmrcservices.jsl.reach;

import gov.usgs.webservices.jdbc.spec.Spec;
import gov.usgs.webservices.jdbc.spec.mapping.ColumnMapping;
import gov.usgs.webservices.jdbc.spec.mapping.SearchMapping;
import gov.usgs.webservices.jdbc.spec.mapping.WhereClauseType;
import gov.usgs.webservices.jdbc.util.CleaningOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class ReachTribPCodeAuxSpec extends Spec {

	private static final Logger log = LoggerFactory.getLogger(ReachTribPCodeAuxSpec.class);

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
		ColumnMapping[] result = new ColumnMapping[]{
			new ColumnMapping(C_REACH_NM, null),
			new ColumnMapping(C_PARM_CD, S_PARM_CD, ASCENDING_ORDER, S_PARM_CD, null, null, null, null, null, null, false),
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
		SearchMapping[] result = new SearchMapping[]{
			new SearchMapping(S_REACH_NM, C_REACH_NM, null, WhereClauseType.equals, CleaningOption.none, null, null),
			new SearchMapping(S_SITE_UP, C_SITE_UP, null, WhereClauseType.equals, CleaningOption.none, null, null),
			new SearchMapping(S_PARM_CD, C_PARM_CD, null, WhereClauseType.equals, CleaningOption.none, null, null)
		};

		return result;
	}

	@Override
	public String setupTableName() {
		StringBuilder result = new StringBuilder();
		
		result.append("(");
		result.append("  SELECT DISTINCT REACH_NM, PARM_CD FROM");
		result.append("  REACH_TRIBS_DISPLAY");
		result.append(") T_A_INNER");

		return result.toString();
	}
	
	public static final String C_REACH_NM = "REACH_NM";
	public static final String S_REACH_NM = "reachName";
	public static final String C_SITE_UP = "SITE_UP";
	public static final String S_SITE_UP = "upstreamStation";
	public static final String C_DISPLAY_NAME_UP = "DISPLAY_NAME_UP";
	public static final String S_DISPLAY_NAME_UP = "upstreamDisplayName";
	public static final String C_SITE_DOWN = "SITE_DOWN";
	public static final String S_SITE_DOWN = "downstreamStation";
	public static final String C_DISPLAY_NAME_DOWN = "DISPLAY_NAME_DOWN";
	public static final String S_DISPLAY_NAME_DOWN = "downstreamDisplayName";
	public static final String C_MAJOR_TRIB_RIVER = "MAJOR_TRIB_RIVER";
	public static final String S_MAJOR_TRIB_RIVER = "majorTribRiver";
	public static final String C_MAJOR_TRIB = "MAJOR_TRIB";
	public static final String S_MAJOR_TRIB = "majorTrib";
	public static final String C_MINOR_TRIB = "MINOR_TRIB";
	public static final String S_MINOR_TRIB = "minorTrib";
	public static final String C_NETWORK_NAME = "NETWORK_NAME";
	public static final String S_NETWORK_NAME = "network";
	public static final String C_DISPLAY_ORDER = "DISPLAY_ORDER";
	public static final String S_DISPLAY_ORDER = "displayOrder";
	public static final String C_END_STATIC_REC = "END_STATIC_REC";
	public static final String S_END_STATIC_REC = "endStaticRec";
	public static final String C_NEWEST_SUSPSED = "NEWEST_SUSPSED";
	public static final String S_NEWEST_SUSPSED = "newestSuspSed";
	public static final String C_PARM_CD = "PARM_CD";
	public static final String S_PARM_CD = "pcode";
}
