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
public class ReachTribPORSpec extends Spec {
	private static final Logger log = LoggerFactory.getLogger(ReachTribPORSpec.class);

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
			new ColumnMapping(C_SITE_UP, S_SITE_UP),
			new ColumnMapping(C_SITE_DOWN, S_SITE_DOWN),
			new ColumnMapping(C_EARLIEST_DT_MAJOR_TRIB, S_EARLIEST_DT_MAJOR_TRIB),
			new ColumnMapping(C_LATEST_DT_MAJOR_TRIB, S_LATEST_DT_MAJOR_TRIB),
			new ColumnMapping(C_EARLIEST_DT_MINOR_TRIB, S_EARLIEST_DT_MINOR_TRIB),
			new ColumnMapping(C_LATEST_DT_MINOR_TRIB, S_LATEST_DT_MINOR_TRIB)
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
			new SearchMapping(S_SITE_UP, C_SITE_UP, null, WhereClauseType.equals, CleaningOption.none, null, null),
			new SearchMapping(S_SITE_DOWN, C_SITE_DOWN, null, WhereClauseType.equals, CleaningOption.none, null, null)
		};
	}

	@Override
	public String setupTableName() {
		StringBuilder result = new StringBuilder();
		result.append("(");
		result.append("  SELECT ");
		result.append("    SITE_UP,");
		result.append("    SITE_DOWN,");
		result.append("    (SELECT TO_CHAR(EARLIEST_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS')");
		result.append("    FROM TS_GROUP_POR");
		result.append("    WHERE TS_GROUP_POR.SITE_ID = SITE_ID_MAJOR_TRIB");
		result.append("    AND TS_GROUP_POR.TS_GRP_NM = MAJOR_TRIB_TS_GRP_NM");
		result.append("    AND TS_GROUP_POR.PARM_CD = PARM_CD) EARLIEST_DT_MAJOR_TRIB,");
		result.append("    (SELECT TO_CHAR(LATEST_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS')");
		result.append("    FROM TS_GROUP_POR");
		result.append("    WHERE TS_GROUP_POR.SITE_ID = SITE_ID_MAJOR_TRIB");
		result.append("    AND TS_GROUP_POR.TS_GRP_NM = MAJOR_TRIB_TS_GRP_NM");
		result.append("    AND TS_GROUP_POR.PARM_CD = PARM_CD) LATEST_DT_MAJOR_TRIB,");
		result.append("    (SELECT TO_CHAR(EARLIEST_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS')");
		result.append("    FROM TS_GROUP_POR");
		result.append("    WHERE TS_GROUP_POR.SITE_ID = SITE_ID_MINOR_TRIB");
		result.append("    AND TS_GROUP_POR.TS_GRP_NM = MINOR_TRIB_TS_GRP_NM");
		result.append("    AND TS_GROUP_POR.PARM_CD = PARM_CD) EARLIEST_DT_MINOR_TRIB,");
		result.append("    (SELECT TO_CHAR(LATEST_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS')");
		result.append("    FROM TS_GROUP_POR");
		result.append("    WHERE TS_GROUP_POR.SITE_ID = SITE_ID_MINOR_TRIB");
		result.append("    AND TS_GROUP_POR.TS_GRP_NM = MINOR_TRIB_TS_GRP_NM");
		result.append("    AND TS_GROUP_POR.PARM_CD = PARM_CD) LATEST_DT_MINOR_TRIB");
		result.append("  FROM");
		result.append("  (SELECT ");
		result.append("    PARM_CD,");
		result.append("    (SELECT (");
		result.append("      CASE");
		result.append("        WHEN NWIS_SITE_NO IS NOT NULL");
		result.append("        THEN NWIS_SITE_NO");
		result.append("        ELSE SITE_SHORT_NM");
		result.append("      END)");
		result.append("    FROM SITE S");
		result.append("    WHERE S.SITE_ID = RTD.SITE_ID_UP");
		result.append("    ) SITE_UP,");
		result.append("    (SELECT (");
		result.append("      CASE");
		result.append("        WHEN NWIS_SITE_NO IS NOT NULL");
		result.append("        THEN NWIS_SITE_NO");
		result.append("        ELSE SITE_SHORT_NM");
		result.append("      END)");
		result.append("    FROM SITE S");
		result.append("    WHERE S.SITE_ID = RTD.SITE_ID_DOWN");
		result.append("    ) SITE_DOWN,");
		result.append("    RTD.MAJOR_TRIB_SITE SITE_ID_MAJOR_TRIB,");
		result.append("    (SELECT (");
		result.append("      CASE");
		result.append("        WHEN NWIS_SITE_NO IS NOT NULL");
		result.append("        THEN NWIS_SITE_NO");
		result.append("        ELSE SITE_SHORT_NM");
		result.append("      END)");
		result.append("    FROM SITE S");
		result.append("    WHERE S.SITE_ID = RTD.MAJOR_TRIB_SITE");
		result.append("    ) SITE_MAJOR_TRIB,");
		result.append("    (SELECT TS_GRP_NM");
		result.append("    FROM ");
		result.append("      TS");
		result.append("    WHERE TS.TS_ID = RTD.MAJOR_TRIB");
		result.append("    ) MAJOR_TRIB_TS_GRP_NM,");
		result.append("    (SELECT S.SITE_ID");
		result.append("    FROM ");
		result.append("      SITE S,");
		result.append("      TS");
		result.append("    WHERE TS.TS_ID = RTD.MINOR_TRIB");
		result.append("    AND S.SITE_ID = TS.SITE_ID");
		result.append("    ) SITE_ID_MINOR_TRIB,");
		result.append("    (SELECT (");
		result.append("      CASE");
		result.append("        WHEN NWIS_SITE_NO IS NOT NULL");
		result.append("        THEN NWIS_SITE_NO");
		result.append("        ELSE SITE_SHORT_NM");
		result.append("      END)");
		result.append("    FROM ");
		result.append("      SITE S,");
		result.append("      TS");
		result.append("    WHERE TS.TS_ID = RTD.MINOR_TRIB");
		result.append("    AND S.SITE_ID = TS.SITE_ID");
		result.append("    ) SITE_MINOR_TRIB,");
		result.append("    (SELECT TS_GRP_NM");
		result.append("    FROM ");
		result.append("      TS");
		result.append("    WHERE TS.TS_ID = RTD.MINOR_TRIB");
		result.append("    ) MINOR_TRIB_TS_GRP_NM");
		result.append("  FROM REACH_TRIBS_DISPLAY RTD) TA_STUPID");
		result.append(") TA_DUMDUM");

		return result.toString();
	}
	
	public static final String S_SITE_UP = "upstreamStation";
	public static final String C_SITE_UP = "SITE_UP";
	public static final String S_SITE_DOWN = "downstreamStation";
	public static final String C_SITE_DOWN = "SITE_DOWN";
	public static final String S_LATEST_DT_MAJOR_TRIB = "majorTribEndPosition";
	public static final String C_LATEST_DT_MAJOR_TRIB = "LATEST_DT_MAJOR_TRIB";
	public static final String S_EARLIEST_DT_MAJOR_TRIB = "majorTribBeginPosition";
	public static final String C_EARLIEST_DT_MAJOR_TRIB = "EARLIEST_DT_MAJOR_TRIB";
	public static final String S_LATEST_DT_MINOR_TRIB = "minorTribEndPosition";
	public static final String C_LATEST_DT_MINOR_TRIB = "LATEST_DT_MINOR_TRIB";
	public static final String S_EARLIEST_DT_MINOR_TRIB = "minorTribBeginPosition";
	public static final String C_EARLIEST_DT_MINOR_TRIB = "EARLIEST_DT_MINOR_TRIB";

}
