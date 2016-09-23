package gov.usgs.cida.gcmrcservices.jsl.derivation;

import gov.usgs.webservices.jdbc.spec.GCMRCSpec;
import gov.usgs.webservices.jdbc.spec.mapping.ColumnMapping;
import gov.usgs.webservices.jdbc.spec.mapping.SearchMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley, eeverman, zmoore
 */
public class DurationCurveSpec extends GCMRCSpec {
	private static final Logger log = LoggerFactory.getLogger(DurationCurveSpec.class);
	
	private final String siteId = "09522100";
	private final String fromTime = "2014-03-30";
	private final String toTime = "2014-04-30";
	private final String binCount = "100";
	
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
			new ColumnMapping(C_BIN_NUMBER, S_BIN_NUMBER),
			new ColumnMapping(C_CUMUL_BIN_PERC, S_CUMUL_BIN_PERC),
			new ColumnMapping(C_BIN_VALUE, S_BIN_VALUE),
			new ColumnMapping(C_BIN_MIN, S_BIN_MIN),
			new ColumnMapping(C_CUMUL_BIN_MIN, S_CUMUL_BIN_MIN),
			new ColumnMapping(C_LOW_BOUND, S_LOW_BOUND),
			new ColumnMapping(C_HIGH_BOUND, S_HIGH_BOUND)
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
			new SearchMapping(null, null, null, null, null, null, null),
		};
	}

	@Override
	public String setupTableName() {
		StringBuilder result = new StringBuilder();
		
		result.append("(");
		result.append("WITH CONST (DISCHARGE_MEASURE_ID, ONE_DAY_OF_MINUTES, DATA_GAP_MARKER, MAX_GAP_MINUTES, MAX_INTERPOLATION_DAYS, MIN_LOG_BIN_VALUE) AS");
		result.append(" (");
		result.append("  SELECT");
		result.append("    2 DISCHARGE_MEASURE_ID,");
		result.append("    60*24 ONE_DAY_OF_MINUTES,");
		result.append("    -999 DATA_GAP_MARKER, ");
		result.append("    60 MAX_GAP_MINUTES, ");
		result.append("    60 MAX_INTERPOLATION_DAYS,");
		result.append("    1 MIN_LOG_BIN_VALUE ");
		result.append("  From DUAL");
		result.append("), RAW_MTI_DATA (START_VALUE, END_VALUE, START_TIME, END_TIME, GAP_MINUTES) AS (");
		result.append("  SELECT");
		result.append("    START_VALUE,");
		result.append("    CASE WHEN END_VALUE = (SELECT DATA_GAP_MARKER FROM CONST) THEN LEAD(END_VALUE) OVER (ORDER BY START_TIME) ELSE END_VALUE END as END_VALUE,");
		result.append("    START_TIME,");
		result.append("    CASE WHEN END_VALUE = (SELECT DATA_GAP_MARKER FROM CONST) THEN LEAD(END_TIME) OVER (ORDER BY START_TIME) ELSE END_TIME END AS END_TIME,");
		result.append("    GAP_MINUTES ");
		result.append("  FROM (");
		result.append("    SELECT * FROM ( ");
		result.append("      SELECT");
		result.append("        START_VALUE,");
		result.append("        END_VALUE,");
		result.append("        START_TIME, ");
		result.append("        END_TIME,");
		result.append("        CASE");
		result.append("          WHEN LEAD(START_VALUE) OVER (ORDER BY START_TIME) = (SELECT DATA_GAP_MARKER FROM CONST) AND LEAD(END_VALUE) OVER (ORDER BY START_TIME) = (SELECT DATA_GAP_MARKER FROM CONST)");
		result.append("            THEN EXTRACT(DAY FROM ((LEAD(END_TIME) OVER (ORDER BY START_TIME) - LEAD(END_TIME) OVER (ORDER BY START_TIME))*60*24))");
		result.append("          ELSE 0");
		result.append("        END AS GAP_MINUTES");
		result.append("      FROM (");
		result.append("        Select");
		result.append("          FINAL_VALUE AS START_VALUE,");
		result.append("          LEAD(FINAL_VALUE) OVER (ORDER BY MEASUREMENT_DATE) AS END_VALUE,");
		result.append("          MEASUREMENT_DATE AS START_TIME, ");
		result.append("          LEAD(MEASUREMENT_DATE) OVER (ORDER BY MEASUREMENT_DATE) AS END_TIME");
		result.append("        From TIME_SERIES_STAR");
		result.append("        Where ");
		result.append("          SITE_ID=" + this.siteId + " ");
		result.append("          And GROUP_ID= (SELECT DISCHARGE_MEASURE_ID FROM CONST) ");
		result.append("          AND MEASUREMENT_DATE > (to_timestamp('" + this.fromTime + "', 'YYYY-MM-DD\"T\"HH24:MI:SS') - (SELECT MAX_INTERPOLATION_DAYS FROM CONST))");
		result.append("          AND MEASUREMENT_DATE < (to_timestamp('" + this.toTime + "', 'YYYY-MM-DD\"T\"HH24:MI:SS') + (SELECT MAX_INTERPOLATION_DAYS FROM CONST))");
		result.append("      )");
		result.append("    ) WHERE");
		result.append("      NOT (START_VALUE = (SELECT DATA_GAP_MARKER FROM CONST) AND END_VALUE = (SELECT DATA_GAP_MARKER FROM CONST)) ");
		result.append("  )");
		result.append("), CLEAN_MTI_DATA (START_VALUE, END_VALUE, MIN_VALUE, MAX_VALUE, START_TIME, END_TIME, DURATION_MINUTES, GAP_MINUTES, IS_START_INTERPOLATED, IS_END_INTERPOLATED) AS (  ");
		result.append("  SELECT");
		result.append("    START_VALUE, END_VALUE, ");
		result.append("    LEAST(START_VALUE, END_VALUE) AS MIN_VALUE,");
		result.append("    GREATEST(START_VALUE, END_VALUE) AS MAX_VALUE,");
		result.append("    START_TIME,");
		result.append("    END_TIME,");
		result.append("    EXTRACT(DAY FROM ((END_TIME - START_TIME)*60*24)) AS DURATION_MINUTES,");
		result.append("    GAP_MINUTES, IS_START_INTERPOLATED, IS_END_INTERPOLATED ");
		result.append("  FROM (");
		result.append("    SELECT");
		result.append("      CASE");
		result.append("        WHEN START_TIME < to_timestamp('" + this.fromTime + "', 'YYYY-MM-DD\"T\"HH24:MI:SS') THEN");
		result.append("        START_VALUE + (EXTRACT(DAY FROM ((to_timestamp('" + this.fromTime + "', 'YYYY-MM-DD\"T\"HH24:MI:SS') - START_TIME) * (SELECT ONE_DAY_OF_MINUTES FROM CONST)))) * ((END_VALUE - START_VALUE)/(EXTRACT(DAY FROM ((END_TIME - START_TIME) * (SELECT ONE_DAY_OF_MINUTES FROM CONST)))))");
		result.append("        ELSE START_VALUE");
		result.append("      END AS START_VALUE,");
		result.append("      CASE");
		result.append("        WHEN END_TIME > to_timestamp('" + this.toTime + "', 'YYYY-MM-DD\"T\"HH24:MI:SS') THEN");
		result.append("        START_VALUE + (EXTRACT(DAY FROM ((to_timestamp('" + this.toTime + "', 'YYYY-MM-DD\"T\"HH24:MI:SS') - START_TIME) * (SELECT ONE_DAY_OF_MINUTES FROM CONST)))) * ((END_VALUE - START_VALUE)/(EXTRACT(DAY FROM ((END_TIME - START_TIME) * (SELECT ONE_DAY_OF_MINUTES FROM CONST)))))");
		result.append("        ELSE END_VALUE");
		result.append("      END AS END_VALUE,");
		result.append("      CASE WHEN START_TIME < to_timestamp('" + this.fromTime + "', 'YYYY-MM-DD\"T\"HH24:MI:SS') THEN to_timestamp('" + this.fromTime + "', 'YYYY-MM-DD\"T\"HH24:MI:SS') ELSE START_TIME END AS START_TIME,");
		result.append("      CASE WHEN END_TIME > to_timestamp('" + this.toTime + "', 'YYYY-MM-DD\"T\"HH24:MI:SS') THEN to_timestamp('" + this.toTime + "', 'YYYY-MM-DD\"T\"HH24:MI:SS') ELSE END_TIME END AS END_TIME,");
		result.append("      GAP_MINUTES,");
		result.append("      CASE WHEN START_TIME < to_timestamp('" + this.fromTime + "', 'YYYY-MM-DD\"T\"HH24:MI:SS') THEN 1 ELSE 0 END AS IS_START_INTERPOLATED,");
		result.append("      CASE WHEN END_TIME > to_timestamp('" + this.toTime + "', 'YYYY-MM-DD\"T\"HH24:MI:SS') THEN 1 ELSE 0 END AS IS_END_INTERPOLATED");
		result.append("    FROM RAW_MTI_DATA");
		result.append("    WHERE");
		result.append("      START_VALUE != (SELECT DATA_GAP_MARKER FROM CONST) ");
		result.append("      AND END_TIME > to_timestamp('" + this.fromTime + "', 'YYYY-MM-DD\"T\"HH24:MI:SS')");
		result.append("      AND START_TIME < to_timestamp('" + this.toTime + "', 'YYYY-MM-DD\"T\"HH24:MI:SS')");
		result.append("      AND NOT GAP_MINUTES > (SELECT MAX_GAP_MINUTES FROM CONST)");
		result.append("      AND NOT EXTRACT(DAY FROM (END_TIME - START_TIME)) > (SELECT MAX_INTERPOLATION_DAYS FROM CONST)");
		result.append("    Order by START_TIME ");
		result.append("  ) Order by START_TIME ");
		result.append("), CONST_DATA (FIRST_MEASURE_DATE, LAST_MEASURE_DATE, OVERALL_DURATION_MINUTES, USER_DURATION_MINUTES, OVERALL_MIN_VALUE, OVERALL_MAX_VALUE, OVERALL_VALUE_RANGE, MEASUREMENT_COUNT) AS");
		result.append(" (");
		result.append("  SELECT");
		result.append("    MIN(START_TIME) as FIRST_MEASURE_DATE,");
		result.append("    MAX(END_TIME) as LAST_MEASURE_DATE,");
		result.append("    SUM(DURATION_MINUTES) as OVERALL_DURATION_MINUTES,");
		result.append("    EXTRACT(DAY FROM (to_timestamp('" + this.toTime + "', 'YYYY-MM-DD\"T\"HH24:MI:SS') - to_timestamp ('" + this.fromTime + "', 'YYYY-MM-DD\"T\"HH24:MI:SS'))*60*24) as USER_DURATION_MINUTES,");
		result.append("    MIN(MIN_VALUE) AS OVERALL_MIN_VALUE,");
		result.append("    MAX(MAX_VALUE) AS OVERALL_MAX_VALUE,");
		result.append("    MAX(MAX_VALUE) - MIN(MIN_VALUE) AS OVERALL_VALUE_RANGE,");
		result.append("    COUNT(*) AS MEASUREMENT_COUNT");
		result.append("  From CLEAN_MTI_DATA");
		result.append("), INT_BINS (BIN_NUMBER) AS");
		result.append("(");
		result.append("  SELECT level BIN_NUMBER   ");
		result.append("  FROM DUAL   ");
		result.append("  CONNECT BY level <='" + this.binCount + "'");
		result.append("), LIN_BINS (BIN_NUMBER, LOW_BOUND, HIGH_BOUND) AS (");
		result.append("  SELECT");
		result.append("    BIN_NUMBER,");
		result.append("    (SELECT ((OVERALL_VALUE_RANGE / " + this.binCount + ") * (BIN_NUMBER - 1)) + OVERALL_MIN_VALUE FROM CONST_DATA) AS LOW_BOUND,");
		result.append("    CASE");
		result.append("    WHEN BIN_NUMBER = " + this.binCount + " THEN (SELECT OVERALL_MAX_VALUE FROM CONST_DATA) ");
		result.append("    ELSE (SELECT ((OVERALL_VALUE_RANGE / " + this.binCount + ") * BIN_NUMBER) + OVERALL_MIN_VALUE FROM CONST_DATA) ");
		result.append("    END AS HIGH_BOUND");
		result.append("  FROM INT_BINS");
		result.append("  ORDER BY BIN_NUMBER");
		result.append("), LOG_BINS (BIN_NUMBER, LOW_BOUND, HIGH_BOUND) AS (");
		result.append("  SELECT 0 BIN_NUMBER, 0 LOW_BOUND, (SELECT MIN_LOG_BIN_VALUE FROM CONST) HIGH_BOUND FROM DUAL");
		result.append("  UNION ALL");
		result.append("  SELECT");
		result.append("    BIN_NUMBER,");
		result.append("    CASE WHEN BIN_NUMBER = 1 THEN (SELECT GREATEST(OVERALL_MIN_VALUE, (SELECT MIN_LOG_BIN_VALUE FROM CONST)) FROM CONST_DATA) ELSE POWER(10, LOG_LOW_BOUND) END AS LOW_BOUND,");
		result.append("    CASE WHEN BIN_NUMBER = " + this.binCount + " THEN (SELECT OVERALL_MAX_VALUE FROM CONST_DATA) ELSE POWER(10, LOG_HIGH_BOUND) END AS HIGH_BOUND");
		result.append("  FROM (");
		result.append("    SELECT");
		result.append("      BIN_NUMBER,");
		result.append("      (SELECT (((LOG(10, OVERALL_MAX_VALUE) - LOG(10, GREATEST(OVERALL_MIN_VALUE, (SELECT MIN_LOG_BIN_VALUE FROM CONST)))) / " + this.binCount + ") * (BIN_NUMBER - 1)) + LOG(10, GREATEST(OVERALL_MIN_VALUE, (SELECT MIN_LOG_BIN_VALUE FROM CONST))) FROM CONST_DATA) AS LOG_LOW_BOUND,");
		result.append("      (SELECT (((LOG(10, OVERALL_MAX_VALUE) - LOG(10, GREATEST(OVERALL_MIN_VALUE, (SELECT MIN_LOG_BIN_VALUE FROM CONST)))) / " + this.binCount + ") * BIN_NUMBER) + LOG(10, GREATEST(OVERALL_MIN_VALUE, (SELECT MIN_LOG_BIN_VALUE FROM CONST))) FROM CONST_DATA) AS LOG_HIGH_BOUND");
		result.append("    FROM INT_BINS");
		result.append("    ORDER BY BIN_NUMBER");
		result.append("  )");
		result.append(")");
		result.append("SELECT");
		result.append("  BIN_NUMBER,");
		result.append("  SUM(IN_BIN_MINUTES) OVER (ORDER BY BIN_NUMBER DESC ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) /");
		result.append("    (SELECT OVERALL_DURATION_MINUTES FROM CONST_DATA) * 100 AS CUMULATIVE_BIN_PERC,");
		result.append("  CASE");
		result.append("    WHEN BIN_NUMBER = 1 THEN LOW_BOUND");
		result.append("    WHEN BIN_NUMBER = " + this.binCount + " THEN HIGH_BOUND");
		result.append("    ELSE (LOW_BOUND + HIGH_BOUND) / 2");
		result.append("  END AS BIN_VALUE,");
		result.append("  IN_BIN_MINUTES,");
		result.append("  SUM(IN_BIN_MINUTES) OVER (ORDER BY BIN_NUMBER DESC ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) AS CUMULATIVE_IN_BIN_MINUTES,");
		result.append("  LOW_BOUND,");
		result.append("  HIGH_BOUND");
		result.append("  FROM (");
		result.append("  SELECT");
		result.append("    SUM(IN_BIN_MINUTES) AS IN_BIN_MINUTES,");
		result.append("    BIN_NUMBER,");
		result.append("    MAX(LOW_BOUND) AS LOW_BOUND,");
		result.append("    MAX(HIGH_BOUND) AS HIGH_BOUND");
		result.append("  FROM (");
		result.append("    SELECT");
		result.append("      CASE");
		result.append("        WHEN data.MIN_VALUE < bin.LOW_BOUND AND data.MAX_VALUE > bin.HIGH_BOUND THEN ((bin.HIGH_BOUND - bin.LOW_BOUND) / (data.MAX_VALUE - data.MIN_VALUE)) * data.DURATION_MINUTES");
		result.append("        WHEN data.MIN_VALUE < bin.LOW_BOUND AND data.MAX_VALUE <= bin.HIGH_BOUND THEN ((data.MAX_VALUE - bin.LOW_BOUND) / (data.MAX_VALUE - data.MIN_VALUE)) * data.DURATION_MINUTES");
		result.append("        WHEN data.MIN_VALUE < bin.HIGH_BOUND AND data.MAX_VALUE > bin.HIGH_BOUND THEN ((bin.HIGH_BOUND - data.MIN_VALUE) / (data.MAX_VALUE - data.MIN_VALUE)) * data.DURATION_MINUTES");
		result.append("        ELSE data.DURATION_MINUTES");
		result.append("      END AS IN_BIN_MINUTES,");
		result.append("      MIN_VALUE, MAX_VALUE, LOW_BOUND, HIGH_BOUND, DURATION_MINUTES,");
		result.append("      START_VALUE, END_VALUE, START_TIME, END_TIME, BIN_NUMBER, GAP_MINUTES");
		result.append("    FROM LOG_BINS bin INNER JOIN CLEAN_MTI_DATA data ON");
		result.append("      (data.MIN_VALUE < bin.HIGH_BOUND AND data.MAX_VALUE > bin.LOW_BOUND) ");
		result.append("      OR (data.MIN_VALUE = data.MAX_VALUE AND data.MIN_VALUE = bin.LOW_BOUND) ");
		result.append("      OR (data.MIN_VALUE = bin.HIGH_BOUND AND bin.BIN_NUMBER = " + this.binCount + ") ");
		result.append("    ORDER BY bin.BIN_NUMBER, START_TIME");
		result.append("  ) ");
		result.append("  GROUP BY BIN_NUMBER");
		result.append("  ORDER BY BIN_NUMBER ");
		result.append(")");
		result.append("ORDER BY BIN_NUMBER");
		result.append(")");
		
		System.out.println(result.toString());
		
		return result.toString();
	}

	public static final String C_BIN_NUMBER = "BIN_NUMBER";
	public static final String S_BIN_NUMBER = "BIN_NUMBER";
	
	public static final String C_CUMUL_BIN_PERC = "CUMULATIVE_BIN_PERC";
	public static final String S_CUMUL_BIN_PERC = "CUMULATIVE_BIN_PERC";
	
	public static final String C_BIN_VALUE = "BIN_VALUE";
	public static final String S_BIN_VALUE = "BIN_VALUE";
	
	public static final String C_BIN_MIN = "IN_BIN_MINUTES";
	public static final String S_BIN_MIN = "IN_BIN_MINUTES";
	
	public static final String C_CUMUL_BIN_MIN = "CUMULATIVE_IN_BIN_MINUTES";
	public static final String S_CUMUL_BIN_MIN = "CUMULATIVE_IN_BIN_MINUTES";
	
	public static final String C_LOW_BOUND = "LOW_BOUND";
	public static final String S_LOW_BOUND = "LOW_BOUND";
	
	public static final String C_HIGH_BOUND = "HIGH_BOUND";
	public static final String S_HIGH_BOUND = "HIGH_BOUND";
}
