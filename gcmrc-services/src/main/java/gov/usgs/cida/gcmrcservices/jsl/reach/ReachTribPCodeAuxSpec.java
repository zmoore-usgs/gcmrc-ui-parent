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
			new ColumnMapping(C_REACH_NAME, null),
			new ColumnMapping(C_GROUP_ID, S_GROUP_ID)
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
			new SearchMapping(S_REACH_NAME, C_REACH_NAME, null, WhereClauseType.equals, CleaningOption.none, null, null),
			new SearchMapping(S_GROUP_ID, C_GROUP_ID, null, WhereClauseType.equals, CleaningOption.none, null, null)
		};

		return result;
	}

	@Override
	public String setupTableName() {
		StringBuilder result = new StringBuilder();
		
		result.append("  (SELECT DISTINCT");
		result.append("    REACH_NAME,");
		result.append("    (SELECT NAME FROM GROUP_NAME WHERE GROUP_NAME.GROUP_ID = RTD.REACH_GROUP) REACH_GROUP");
		result.append("  FROM");
		result.append("    REACH_DISPLAY RTD) T_A_INNER");

		return result.toString();
	}
	
	public static final String C_REACH_NAME = "REACH_NAME";
	public static final String S_REACH_NAME = "reachName";
	public static final String C_GROUP_ID = "REACH_GROUP";
	public static final String S_GROUP_ID = "groupName";
}
