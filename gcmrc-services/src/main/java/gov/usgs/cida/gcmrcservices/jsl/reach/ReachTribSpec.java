package gov.usgs.cida.gcmrcservices.jsl.reach;

import gov.usgs.webservices.jdbc.spec.Spec;
import gov.usgs.webservices.jdbc.spec.mapping.ColumnMapping;
import gov.usgs.webservices.jdbc.spec.mapping.SearchMapping;
import gov.usgs.webservices.jdbc.spec.mapping.WhereClauseType;
import gov.usgs.webservices.jdbc.util.CleaningOption;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class ReachTribSpec extends Spec {

	private static final Logger log = LoggerFactory.getLogger(ReachTribSpec.class);

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
			new ColumnMapping(C_REACH_NM, S_REACH_NM),
			new ColumnMapping(C_SITE_UP, S_SITE_UP),
			new ColumnMapping(C_DISPLAY_NAME_UP, S_DISPLAY_NAME_UP),
			new ColumnMapping(C_SITE_DOWN, S_SITE_DOWN),
			new ColumnMapping(C_DISPLAY_NAME_DOWN, S_DISPLAY_NAME_DOWN),
			new ColumnMapping(C_MAJOR_TRIB_RIVER, S_MAJOR_TRIB_RIVER),
			new ColumnMapping(C_MAJOR_TRIB_SITE, S_MAJOR_TRIB_SITE),
			new ColumnMapping(C_MAJOR_GROUP, S_MAJOR_GROUP),
			new ColumnMapping(C_MINOR_TRIB_SITE, S_MINOR_TRIB_SITE),
			new ColumnMapping(C_MINOR_GROUP, S_MINOR_GROUP),
			new ColumnMapping(C_DISCHARGE_DOWN, S_DISCHARGE_DOWN),
			new ColumnMapping(C_DISCHARGE_DISPLAY_DOWN, S_DISCHARGE_DISPLAY_DOWN),
			new ColumnMapping(C_NETWORK_NAME, S_NETWORK_NAME),
			new ColumnMapping(C_DISPLAY_ORDER, S_DISPLAY_ORDER),
			new ColumnMapping(new ReachTribPCodeAuxSpec(), "groupName"),
			new ColumnMapping(C_END_STATIC_REC, S_END_STATIC_REC),
			new ColumnMapping(C_NEWEST_SUSPSED, S_NEWEST_SUSPSED)
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
	public List<String[]> getRepeatedTags() {
		List<String[]> result = super.getRepeatedTags();
		result.add(new String[] {"data", "groupName"});
		return result;
	}
	
	@Override
	public SearchMapping[] setupSearchMap() {
		SearchMapping[] result = new SearchMapping[]{
			new SearchMapping(S_REACH_NM, C_REACH_NM, null, WhereClauseType.equals, CleaningOption.none, null, null),
			new SearchMapping(S_SITE_UP, C_SITE_UP, null, WhereClauseType.equals, CleaningOption.none, null, null),
			new SearchMapping(S_DISPLAY_NAME_UP, C_DISPLAY_NAME_UP, null, WhereClauseType.equals, CleaningOption.none, null, null),
			new SearchMapping(S_SITE_DOWN, C_SITE_DOWN, null, WhereClauseType.equals, CleaningOption.none, null, null),
			new SearchMapping(S_DISPLAY_NAME_DOWN, C_DISPLAY_NAME_DOWN, null, WhereClauseType.equals, CleaningOption.none, null, null),
			new SearchMapping(S_MAJOR_TRIB_RIVER, C_MAJOR_TRIB_RIVER, null, WhereClauseType.equals, CleaningOption.none, null, null),
			new SearchMapping(S_MAJOR_TRIB_SITE, C_MAJOR_TRIB_SITE, null, WhereClauseType.equals, CleaningOption.none, null, null),
			new SearchMapping(S_MAJOR_GROUP, C_MAJOR_GROUP, null, WhereClauseType.equals, CleaningOption.none, null, null),
			new SearchMapping(S_MINOR_GROUP, C_MINOR_GROUP, null, WhereClauseType.equals, CleaningOption.none, null, null),
			new SearchMapping(S_NETWORK_NAME, C_NETWORK_NAME, null, WhereClauseType.equals, CleaningOption.none, null, null),
			new SearchMapping(S_DISPLAY_ORDER, C_DISPLAY_ORDER, null, WhereClauseType.equals, CleaningOption.none, null, null)
		};

		return result;
	}

	@Override
	public String setupTableName() {
		StringBuilder result = new StringBuilder();

		result.append("(SELECT DISTINCT RTD.REACH_NAME,");
		result.append("    (SELECT (");
		result.append("      CASE");
		result.append("        WHEN NWIS_SITE_NO IS NOT NULL");
		result.append("        THEN NWIS_SITE_NO");
		result.append("        ELSE SHORT_NAME");
		result.append("      END) SITE_UP");
		result.append("    FROM SITE_STAR S");
		result.append("    WHERE S.SITE_ID = RTD.SITE_ID_UP");
		result.append("    ) SITE_UP,");
		result.append("    (SELECT S.NAME FROM SITE_STAR S WHERE S.SITE_ID = RTD.SITE_ID_UP");
		result.append("    ) DISPLAY_NAME_UP,");
		result.append("    (SELECT (");
		result.append("      CASE");
		result.append("        WHEN NWIS_SITE_NO IS NOT NULL");
		result.append("        THEN NWIS_SITE_NO");
		result.append("        ELSE SHORT_NAME");
		result.append("      END)");
		result.append("    FROM SITE_STAR S");
		result.append("    WHERE S.SITE_ID = RTD.SITE_ID_DOWN");
		result.append("    ) SITE_DOWN,");
		result.append("    (SELECT S.NAME FROM SITE_STAR S WHERE S.SITE_ID = RTD.SITE_ID_DOWN");
		result.append("    ) DISPLAY_NAME_DOWN,");
		result.append("    (SELECT (");
		result.append("      CASE");
		result.append("        WHEN NWIS_SITE_NO IS NOT NULL");
		result.append("        THEN NWIS_SITE_NO");
		result.append("        ELSE SHORT_NAME");
		result.append("      END)");
		result.append("    FROM SITE_STAR S");
		result.append("    WHERE S.SITE_ID = RTD.DISCHARGE_DOWN");
		result.append("    ) DISCHARGE_SITE_DOWN,");
		result.append("    (SELECT S.NAME FROM SITE_STAR S WHERE S.SITE_ID = RTD.DISCHARGE_DOWN");
		result.append("    ) DISCHARGE_DISPLAY_NAME_DOWN,");
		result.append("    (SELECT S.RIVER_NAME");
		result.append("    FROM SITE_STAR S");
		result.append("    WHERE RTD.MAJOR_SITE = S.SITE_ID");
		result.append("    ) AS MAJOR_TRIB_RIVER,");
		result.append("    (SELECT (");
		result.append("      CASE");
		result.append("        WHEN NWIS_SITE_NO IS NOT NULL");
		result.append("        THEN NWIS_SITE_NO");
		result.append("        ELSE SHORT_NAME");
		result.append("      END)");
		result.append("    FROM SITE_STAR S");
		result.append("    WHERE S.SITE_ID = RTD.MAJOR_SITE");
		result.append("    ) MAJOR_TRIB_SITE_NAME,");
		result.append("    (SELECT NAME FROM GROUP_NAME WHERE RTD.MAJOR_GROUP = GROUP_NAME.GROUP_ID) MAJOR_GROUP,");
		result.append("    (SELECT (");
		result.append("      CASE");
		result.append("        WHEN NWIS_SITE_NO IS NOT NULL");
		result.append("        THEN NWIS_SITE_NO");
		result.append("        ELSE SHORT_NAME");
		result.append("      END)");
		result.append("    FROM SITE_STAR S");
		result.append("    WHERE S.SITE_ID = RTD.MINOR_SITE");
		result.append("    ) MINOR_TRIB_SITE_NAME,");
		result.append("    (SELECT NAME FROM GROUP_NAME WHERE GROUP_NAME.GROUP_ID = RTD.MINOR_GROUP) MINOR_GROUP,");
		result.append("    CASE");
		result.append("      WHEN RTD.NETWORK_NAME='GCDAMP'");
		result.append("      THEN 'GCDAMP'");
		result.append("      WHEN RTD.NETWORK_NAME='Dinosaur'");
		result.append("      THEN 'DINO'");
		result.append("      WHEN RTD.NETWORK_NAME='BigBend'");
		result.append("      THEN 'BIBE'");
		result.append("      WHEN RTD.NETWORK_NAME='Canyonlands'");
		result.append("      THEN 'CL'");
		result.append("      WHEN RTD.NETWORK_NAME='RiverDelta'");
		result.append("      THEN 'CRD'");
		result.append("      ELSE RTD.NETWORK_NAME");
		result.append("    END AS NETWORK_NAME,");
		result.append("    RTD.DISPLAY_ORDER,");
		result.append("    TO_CHAR(END_STATIC_REC, 'YYYY-MM-DD\"T\"HH24:MI:SS') END_STATIC_REC,");
		result.append("    TO_CHAR(NEWEST_SUSPSED, 'YYYY-MM-DD\"T\"HH24:MI:SS') NEWEST_SUSPSED");
		result.append("  FROM REACH_DISPLAY RTD");
		result.append("  ) T_A_REACHES");

		return result.toString();
	}
	
	public static final String C_REACH_NM = "REACH_NAME";
	public static final String S_REACH_NM = "reachName";
//	public static final String C_REACH_GROUP = "REACH_GROUP";
//	public static final String S_REACH_GROUP = "reachGroup";
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
	public static final String C_MAJOR_TRIB_SITE = "MAJOR_TRIB_SITE_NAME";
	public static final String S_MAJOR_TRIB_SITE = "majorTribSite";
	public static final String C_MAJOR_GROUP = "MAJOR_GROUP";
	public static final String S_MAJOR_GROUP = "majorGroup";
	public static final String C_MINOR_TRIB_SITE = "MINOR_TRIB_SITE_NAME";
	public static final String S_MINOR_TRIB_SITE = "minorTribSite";
	public static final String C_MINOR_GROUP = "MINOR_GROUP";
	public static final String S_MINOR_GROUP = "minorGroup";
	public static final String C_NETWORK_NAME = "NETWORK_NAME";
	public static final String S_NETWORK_NAME = "network";
	public static final String C_DISPLAY_ORDER = "DISPLAY_ORDER";
	public static final String S_DISPLAY_ORDER = "displayOrder";
	public static final String C_END_STATIC_REC = "END_STATIC_REC";
	public static final String S_END_STATIC_REC = "endStaticRec";
	public static final String C_NEWEST_SUSPSED = "NEWEST_SUSPSED";
	public static final String S_NEWEST_SUSPSED = "newestSuspSed";
	public static final String C_DISCHARGE_DOWN = "DISCHARGE_SITE_DOWN";
	public static final String S_DISCHARGE_DOWN = "downstreamDischargeStation";
	public static final String C_DISCHARGE_DISPLAY_DOWN = "DISCHARGE_DISPLAY_NAME_DOWN";
	public static final String S_DISCHARGE_DISPLAY_DOWN = "downstreamDischargeName";
}
