package gov.usgs.cida.gcmrcservices.jsl.reach;

import gov.usgs.webservices.jdbc.spec.GCMRCSpec;
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
public class ReachTribPORSpec extends GCMRCSpec {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
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
			new ColumnMapping(C_SITE_UP_SEC, S_SITE_UP_SEC),
			new ColumnMapping(C_SITE_DOWN, S_SITE_DOWN),
			new ColumnMapping(C_GROUP_NAME, S_GROUP_NAME),
			new ColumnMapping(C_UP_EARLIEST_DT, S_UP_EARLIEST_DT),
			new ColumnMapping(C_UP_LATEST_DT, S_UP_LATEST_DT),
			new ColumnMapping(C_UP_SEC_EARLIEST_DT, S_UP_SEC_EARLIEST_DT),
			new ColumnMapping(C_UP_SEC_LATEST_DT, S_UP_SEC_LATEST_DT),
			new ColumnMapping(C_DOWN_EARLIEST_DT, S_DOWN_EARLIEST_DT),
			new ColumnMapping(C_DOWN_LATEST_DT, S_DOWN_LATEST_DT),
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
			new SearchMapping(S_SITE_DOWN, C_SITE_DOWN, null, WhereClauseType.equals, CleaningOption.none, null, null),
			new SearchMapping(S_GROUP_NAME, C_GROUP_NAME, null, WhereClauseType.equals, CleaningOption.none, null, null)
		};
	}

	@Override
	public String setupTableName() {
		StringBuilder result = new StringBuilder();
		
		result.append("(SELECT ");
		result.append("  CASE WHEN SS_UP.NWIS_SITE_NO IS NULL THEN SS_UP.SHORT_NAME ELSE SS_UP.NWIS_SITE_NO END SITE_UP ");
		result.append("  ,TO_CHAR(TSP_UP.EARLIEST_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS') UP_EARLIEST_DT ");
		result.append("  ,TO_CHAR(TSP_UP.LATEST_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS') UP_LATEST_DT ");
		result.append("  ,CASE WHEN SS_SEC_UP.NWIS_SITE_NO IS NULL THEN SS_SEC_UP.SHORT_NAME ELSE SS_SEC_UP.NWIS_SITE_NO END SITE_UP_SEC  ");
		result.append("  ,TO_CHAR(TSP_SEC.EARLIEST_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS') UP_SEC_EARLIEST_DT ");
		result.append("  ,TO_CHAR(TSP_SEC.LATEST_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS') UP_SEC_LATEST_DT ");
		result.append("  ,CASE WHEN SS_DOWN.NWIS_SITE_NO IS NULL THEN SS_DOWN.SHORT_NAME ELSE SS_DOWN.NWIS_SITE_NO END SITE_DOWN ");
		result.append("  ,TO_CHAR(TSP_DOWN.EARLIEST_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS') DOWN_EARLIEST_DT ");
		result.append("  ,TO_CHAR(TSP_DOWN.LATEST_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS') DOWN_LATEST_DT ");
		result.append("  ,G.NAME GROUP_NAME ");
		result.append("  ,CASE WHEN SS_DIS_DOWN.NWIS_SITE_NO IS NULL THEN SS_DIS_DOWN.SHORT_NAME ELSE SS_DIS_DOWN.NWIS_SITE_NO END DISCHARGE_DOWN  ");
		result.append("  ,TO_CHAR(TSP_DIS.EARLIEST_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS') DISCHARGE_EARLIEST_DT ");
		result.append("  ,TO_CHAR(TSP_DIS.LATEST_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS') DISCHARGE_LATEST_DT ");
		result.append("  ,TO_CHAR(TSP_MAJ_TRIB.EARLIEST_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS') EARLIEST_DT_MAJOR_TRIB ");
		result.append("  ,TO_CHAR(TSP_MAJ_TRIB.LATEST_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS') LATEST_DT_MAJOR_TRIB ");
		result.append("  ,TO_CHAR(TSP_MIN_TRIB.EARLIEST_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS') EARLIEST_DT_MINOR_TRIB ");
		result.append("  ,TO_CHAR(TSP_MIN_TRIB.LATEST_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS') LATEST_DT_MINOR_TRIB ");
		result.append(" FROM ");
                result.append("     REACH_DISPLAY D ");
		result.append("     ,TIME_SERIES_POR TSP_MIN_TRIB  ");
		result.append("     ,TIME_SERIES_POR TSP_MAJ_TRIB ");
		result.append("     ,TIME_SERIES_POR TSP_DIS ");
		result.append("     ,TIME_SERIES_POR TSP_DOWN ");
		result.append("     ,TIME_SERIES_POR TSP_UP ");
		result.append("     ,TIME_SERIES_POR TSP_SEC ");
		result.append("     ,SITE_STAR SS_DIS_DOWN ");
		result.append("     ,SITE_STAR SS_DOWN  ");
		result.append("     ,SITE_STAR SS_SEC_UP ");
		result.append("     ,SITE_STAR SS_UP ");
		result.append("     ,GROUP_NAME G ");
		result.append(" WHERE ");
		result.append("     TSP_MIN_TRIB.SITE_ID(+) = D.MINOR_SITE ");
		result.append("     AND TSP_MIN_TRIB.GROUP_ID(+) = D.MINOR_GROUP ");
		result.append("     AND TSP_MAJ_TRIB.SITE_ID(+) = D.MAJOR_SITE ");
		result.append("     AND TSP_MAJ_TRIB.GROUP_ID(+) = D.MAJOR_GROUP  ");
		result.append("     AND TSP_DIS.SITE_ID(+) = D.DISCHARGE_DOWN ");
		result.append("     AND TSP_DIS.GROUP_ID = 2 ");
		result.append("     AND TSP_DOWN.SITE_ID(+) = D.SITE_ID_DOWN ");
		result.append("     AND TSP_DOWN.GROUP_ID(+) = D.REACH_GROUP ");
		result.append("     AND D.DISCHARGE_DOWN = SS_DIS_DOWN.SITE_ID(+) ");
		result.append("     AND D.REACH_GROUP = G.GROUP_ID ");
		result.append("     AND SITE_ID_DOWN = SS_DOWN.SITE_ID(+)  ");
		result.append("     AND TSP_SEC.SITE_ID(+) = D.SITE_ID_UP_SECONDARY ");
		result.append("     AND TSP_SEC.GROUP_ID(+) = D.REACH_GROUP ");
		result.append("     AND SITE_ID_UP_SECONDARY = SS_SEC_UP.SITE_ID(+) ");
		result.append("     AND TSP_UP.SITE_ID(+) = D.SITE_ID_UP ");
		result.append("     AND TSP_UP.GROUP_ID(+) = D.REACH_GROUP ");
		result.append("     AND SITE_ID_UP = SS_UP.SITE_ID(+)) T_A_MAIN ");

		return result.toString();
	}
	
	public static final String S_SITE_UP = "upstreamStation";
	public static final String C_SITE_UP = "SITE_UP";
	public static final String S_SITE_UP_SEC = "upstreamSecondaryStation";
	public static final String C_SITE_UP_SEC = "SITE_UP_SEC";
	public static final String S_UP_EARLIEST_DT = "upstreamBeginPosition";
	public static final String C_UP_EARLIEST_DT = "UP_EARLIEST_DT";
	public static final String S_UP_LATEST_DT = "upstreamEndPosition";
	public static final String C_UP_LATEST_DT = "UP_LATEST_DT";
	public static final String S_UP_SEC_EARLIEST_DT = "upstreamSecondaryBeginPosition";
	public static final String C_UP_SEC_EARLIEST_DT = "UP_SEC_EARLIEST_DT";
	public static final String S_UP_SEC_LATEST_DT = "upstreamSecondaryEndPosition";
	public static final String C_UP_SEC_LATEST_DT = "UP_SEC_LATEST_DT";
	public static final String S_SITE_DOWN = "downstreamStation";
	public static final String C_SITE_DOWN = "SITE_DOWN";
	public static final String S_DOWN_EARLIEST_DT = "downstreamBeginPosition";
	public static final String C_DOWN_EARLIEST_DT = "DOWN_EARLIEST_DT";
	public static final String S_DOWN_LATEST_DT = "downstreamEndPosition";
	public static final String C_DOWN_LATEST_DT = "DOWN_LATEST_DT";
	public static final String S_GROUP_NAME = "groupName";
	public static final String C_GROUP_NAME = "GROUP_NAME";
	public static final String S_LATEST_DT_MAJOR_TRIB = "majorTribEndPosition";
	public static final String C_LATEST_DT_MAJOR_TRIB = "LATEST_DT_MAJOR_TRIB";
	public static final String S_EARLIEST_DT_MAJOR_TRIB = "majorTribBeginPosition";
	public static final String C_EARLIEST_DT_MAJOR_TRIB = "EARLIEST_DT_MAJOR_TRIB";
	public static final String S_LATEST_DT_MINOR_TRIB = "minorTribEndPosition";
	public static final String C_LATEST_DT_MINOR_TRIB = "LATEST_DT_MINOR_TRIB";
	public static final String S_EARLIEST_DT_MINOR_TRIB = "minorTribBeginPosition";
	public static final String C_EARLIEST_DT_MINOR_TRIB = "EARLIEST_DT_MINOR_TRIB";

}
