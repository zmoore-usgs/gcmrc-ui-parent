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
		
		result.append("(SELECT ");
		result.append("  (SELECT CASE WHEN NWIS_SITE_NO IS NULL THEN SHORT_NAME ELSE NWIS_SITE_NO END FROM SITE_STAR WHERE SITE_ID_UP = SITE_ID) SITE_UP, ");
		result.append("  (SELECT ");
		result.append("    TO_CHAR(EARLIEST_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS') ");
		result.append("  FROM  ");
		result.append("    TIME_SERIES_POR TSP ");
		result.append("  WHERE ");
		result.append("    TSP.SITE_ID = D.SITE_ID_UP ");
		result.append("    AND TSP.GROUP_ID = D.REACH_GROUP) UP_EARLIEST_DT, ");
		result.append("  (SELECT ");
		result.append("    TO_CHAR(LATEST_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS') ");
		result.append("  FROM  ");
		result.append("    TIME_SERIES_POR TSP ");
		result.append("  WHERE ");
		result.append("    TSP.SITE_ID = D.SITE_ID_UP ");
		result.append("    AND TSP.GROUP_ID = D.REACH_GROUP) UP_LATEST_DT, ");
		result.append("  (SELECT CASE WHEN NWIS_SITE_NO IS NULL THEN SHORT_NAME ELSE NWIS_SITE_NO END FROM SITE_STAR WHERE SITE_ID_DOWN = SITE_ID) SITE_DOWN, ");
		result.append("  (SELECT ");
		result.append("    TO_CHAR(EARLIEST_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS') ");
		result.append("  FROM  ");
		result.append("    TIME_SERIES_POR TSP ");
		result.append("  WHERE ");
		result.append("    TSP.SITE_ID = D.SITE_ID_DOWN ");
		result.append("    AND TSP.GROUP_ID = D.REACH_GROUP) DOWN_EARLIEST_DT, ");
		result.append("  (SELECT ");
		result.append("    TO_CHAR(LATEST_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS') ");
		result.append("  FROM  ");
		result.append("    TIME_SERIES_POR TSP ");
		result.append("  WHERE ");
		result.append("    TSP.SITE_ID = D.SITE_ID_DOWN ");
		result.append("    AND TSP.GROUP_ID = D.REACH_GROUP) DOWN_LATEST_DT, ");
		result.append("  (SELECT CASE WHEN NWIS_SITE_NO IS NULL THEN SHORT_NAME ELSE NWIS_SITE_NO END FROM SITE_STAR WHERE DISCHARGE_DOWN = SITE_ID) DISCHARGE_DOWN, ");
		result.append("  (SELECT ");
		result.append("    TO_CHAR(EARLIEST_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS') ");
		result.append("  FROM  ");
		result.append("    TIME_SERIES_POR TSP ");
		result.append("  WHERE ");
		result.append("    TSP.SITE_ID = D.DISCHARGE_DOWN ");
		result.append("    AND TSP.GROUP_ID = 2) DISCHARGE_EARLIEST_DT, ");
		result.append("  (SELECT ");
		result.append("    TO_CHAR(LATEST_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS') ");
		result.append("  FROM  ");
		result.append("    TIME_SERIES_POR TSP ");
		result.append("  WHERE ");
		result.append("    TSP.SITE_ID = D.DISCHARGE_DOWN ");
		result.append("    AND TSP.GROUP_ID = 2) DISCHARGE_LATEST_DT, ");
		result.append("  (SELECT ");
		result.append("    TO_CHAR(EARLIEST_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS') ");
		result.append("  FROM  ");
		result.append("    TIME_SERIES_POR TSP ");
		result.append("  WHERE ");
		result.append("    TSP.SITE_ID = D.MAJOR_SITE ");
		result.append("    AND TSP.GROUP_ID = D.MAJOR_GROUP) EARLIEST_DT_MAJOR_TRIB, ");
		result.append("  (SELECT ");
		result.append("    TO_CHAR(LATEST_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS') ");
		result.append("  FROM  ");
		result.append("    TIME_SERIES_POR TSP ");
		result.append("  WHERE ");
		result.append("    TSP.SITE_ID = D.MAJOR_SITE ");
		result.append("    AND TSP.GROUP_ID = D.MAJOR_GROUP) LATEST_DT_MAJOR_TRIB, ");
		result.append("  (SELECT ");
		result.append("    TO_CHAR(EARLIEST_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS') ");
		result.append("  FROM  ");
		result.append("    TIME_SERIES_POR TSP ");
		result.append("  WHERE ");
		result.append("    TSP.SITE_ID = D.MINOR_SITE ");
		result.append("    AND TSP.GROUP_ID = D.MINOR_GROUP) EARLIEST_DT_MINOR_TRIB, ");
		result.append("  (SELECT ");
		result.append("    TO_CHAR(LATEST_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS') ");
		result.append("  FROM  ");
		result.append("    TIME_SERIES_POR TSP ");
		result.append("  WHERE ");
		result.append("    TSP.SITE_ID = D.MINOR_SITE ");
		result.append("    AND TSP.GROUP_ID = D.MINOR_GROUP) LATEST_DT_MINOR_TRIB ");
		result.append("FROM ");
		result.append("  REACH_DISPLAY D) T_A_MAIN ");

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
