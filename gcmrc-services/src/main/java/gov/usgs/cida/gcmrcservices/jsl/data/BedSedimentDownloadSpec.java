package gov.usgs.cida.gcmrcservices.jsl.data;

import gov.usgs.cida.gcmrcservices.nude.Endpoint;
import gov.usgs.webservices.jdbc.spec.Spec;
import static gov.usgs.webservices.jdbc.spec.Spec.FIELD_NAME_KEY;
import static gov.usgs.webservices.jdbc.spec.Spec.USER_VALUE_KEY;
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
public class BedSedimentDownloadSpec extends Spec implements CentralizableTimezone{
	private static final Logger log = LoggerFactory.getLogger(BedSedimentDownloadSpec.class);

	@Override
	public String getTimezoneDisplay() {
		return "MST";
	}

	@Override
	public String getTimezoneSql() {
		return "";
	}
	
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
			new ColumnMapping(C_STATION_NAME, S_STATION_NAME),
			new ColumnMapping(C_STATION_NUM, S_STATION_NUM),
			new ColumnMapping(C_BED_DATE, S_BED_DATE, ASCENDING_ORDER, S_BED_DATE, null, null, null, "TO_CHAR(" + C_BED_DATE + ", 'YYYY-MM-DD')", null, null),
			new ColumnMapping(C_BED_TIME, S_BED_TIME, ASCENDING_ORDER, S_BED_TIME + " (" + getTimezoneDisplay() + ")", null, null, null, "TO_CHAR(" + C_BED_TIME + ", 'HH24:MI:SS')", null, null),
			new ColumnMapping(C_SAMPLE_SET, S_SAMPLE_SET),
			new ColumnMapping(C_NOTES, S_NOTES),
			new ColumnMapping(C_STATION_LOCATION, S_STATION_LOCATION),
			new ColumnMapping(C_SAMPLE_WEIGHT, S_SAMPLE_WEIGHT),
			new ColumnMapping(C_SAND_D50, S_SAND_D50),
			new ColumnMapping(C_FINES_D50, S_FINES_D50),
			new ColumnMapping(C_TOTAL_D50, S_TOTAL_D50),
			new ColumnMapping(C_PCT_BTWN_063_125, S_PCT_BTWN_063_125),
			new ColumnMapping(C_SIZE_DIST_LT_037, S_SIZE_DIST_LT_037),
			new ColumnMapping(C_SIZE_DIST_LT_044, S_SIZE_DIST_LT_044),
			new ColumnMapping(C_SIZE_DIST_LT_053, S_SIZE_DIST_LT_053),
			new ColumnMapping(C_SIZE_DIST_LT_063, S_SIZE_DIST_LT_063),
			new ColumnMapping(C_SIZE_DIST_LT_074, S_SIZE_DIST_LT_074),
			new ColumnMapping(C_SIZE_DIST_LT_088, S_SIZE_DIST_LT_088),
			new ColumnMapping(C_SIZE_DIST_LT_105, S_SIZE_DIST_LT_105),
			new ColumnMapping(C_SIZE_DIST_LT_125, S_SIZE_DIST_LT_125),
			new ColumnMapping(C_SIZE_DIST_LT_149, S_SIZE_DIST_LT_149),
			new ColumnMapping(C_SIZE_DIST_LT_177, S_SIZE_DIST_LT_177),
			new ColumnMapping(C_SIZE_DIST_LT_210, S_SIZE_DIST_LT_210),
			new ColumnMapping(C_SIZE_DIST_LT_250, S_SIZE_DIST_LT_250),
			new ColumnMapping(C_SIZE_DIST_LT_297, S_SIZE_DIST_LT_297),
			new ColumnMapping(C_SIZE_DIST_LT_354, S_SIZE_DIST_LT_354),
			new ColumnMapping(C_SIZE_DIST_LT_420, S_SIZE_DIST_LT_420),
			new ColumnMapping(C_SIZE_DIST_LT_500, S_SIZE_DIST_LT_500),
			new ColumnMapping(C_SIZE_DIST_LT_595, S_SIZE_DIST_LT_595),
			new ColumnMapping(C_SIZE_DIST_LT_707, S_SIZE_DIST_LT_707),
			new ColumnMapping(C_SIZE_DIST_LT_841, S_SIZE_DIST_LT_841),
			new ColumnMapping(C_SIZE_DIST_LT_1_00, S_SIZE_DIST_LT_1_00),
			new ColumnMapping(C_SIZE_DIST_LT_1_41, S_SIZE_DIST_LT_1_41),
			new ColumnMapping(C_SIZE_DIST_LT_2_00, S_SIZE_DIST_LT_2_00),
			new ColumnMapping(C_SIZE_DIST_LT_2_80, S_SIZE_DIST_LT_2_80),
			new ColumnMapping(C_SIZE_DIST_LT_4_00, S_SIZE_DIST_LT_4_00),
			new ColumnMapping(C_SIZE_DIST_LT_5_60, S_SIZE_DIST_LT_5_60),
			new ColumnMapping(C_SIZE_DIST_LT_8_00, S_SIZE_DIST_LT_8_00),
			new ColumnMapping(C_SIZE_DIST_LT_11_3, S_SIZE_DIST_LT_11_3),
			new ColumnMapping(C_SIZE_DIST_LT_16_0, S_SIZE_DIST_LT_16_0),
			new ColumnMapping(C_SIZE_DIST_LT_22_6, S_SIZE_DIST_LT_22_6),
			new ColumnMapping(C_SIZE_DIST_LT_32_0, S_SIZE_DIST_LT_32_0),
			new ColumnMapping(C_SIZE_DIST_LT_45_0, S_SIZE_DIST_LT_45_0),
			new ColumnMapping(C_SIZE_DIST_LT_64_0, S_SIZE_DIST_LT_64_0),
			new ColumnMapping(C_SIZE_DIST_LT_91_0, S_SIZE_DIST_LT_91_0),
			new ColumnMapping(C_SIZE_DIST_LT_128_0, S_SIZE_DIST_LT_128_0)
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
			new SearchMapping(SE_STATION_NUM, C_STATION_NUM, null, WhereClauseType.equals, CleaningOption.none, null, null),
			new SearchMapping(Endpoint.BEGIN_KEYWORD, C_BED_DATE, null, WhereClauseType.special, CleaningOption.none, FIELD_NAME_KEY + " >= TO_DATE(" + USER_VALUE_KEY + ", 'YYYY-MM-DD\"T\"HH24:MI:SS')", null),
			new SearchMapping(Endpoint.END_KEYWORD, C_BED_DATE, null, WhereClauseType.special, CleaningOption.none, FIELD_NAME_KEY + " <= TO_DATE(" + USER_VALUE_KEY + ", 'YYYY-MM-DD\"T\"HH24:MI:SS')", null)
		};
		
		return result;
	}

	@Override
	public String setupTableName() {
		StringBuilder result = new StringBuilder();

		result.append("(SELECT s.name station_name,");
		result.append("    nvl2(s.nwis_site_no, s.nwis_site_no, s.short_name) station_num,");
		result.append("    b.bed_meas_dt ").append(getTimezoneSql()).append(" AS bed_dt,");
		result.append("    b.bed_meas_dt ").append(getTimezoneSql()).append(" AS bed_tm,");
		result.append("    b.sample_set,");
		result.append("    b.notes,");
		result.append("    b.station_location,");
		result.append("    b.sample_weight,");
		result.append("    b.sand_d50,");
		result.append("    b.fines_d50,");
		result.append("    b.total_d50,");
		result.append("    b.pct_btwn_063_125,");
		result.append("    b.size_dist_lt_037,");
		result.append("    b.size_dist_lt_044,");
		result.append("    b.size_dist_lt_053,");
		result.append("    b.size_dist_lt_063,");
		result.append("    b.size_dist_lt_074,");
		result.append("    b.size_dist_lt_088,");
		result.append("    b.size_dist_lt_105,");
		result.append("    b.size_dist_lt_125,");
		result.append("    b.size_dist_lt_149,");
		result.append("    b.size_dist_lt_177,");
		result.append("    b.size_dist_lt_210,");
		result.append("    b.size_dist_lt_250,");
		result.append("    b.size_dist_lt_297,");
		result.append("    b.size_dist_lt_354,");
		result.append("    b.size_dist_lt_420,");
		result.append("    b.size_dist_lt_500,");
		result.append("    b.size_dist_lt_595,");
		result.append("    b.size_dist_lt_707,");
		result.append("    b.size_dist_lt_841,");
		result.append("    b.size_dist_lt_1_00,");
		result.append("    b.size_dist_lt_1_41,");
		result.append("    b.size_dist_lt_2_00,");
		result.append("    b.size_dist_lt_2_80,");
		result.append("    b.size_dist_lt_4_00,");
		result.append("    b.size_dist_lt_5_60,");
		result.append("    b.size_dist_lt_8_00,");
		result.append("    b.size_dist_lt_11_3,");
		result.append("    b.size_dist_lt_16_0,");
		result.append("    b.size_dist_lt_22_6,");
		result.append("    b.size_dist_lt_32_0,");
		result.append("    b.size_dist_lt_45_0,");
		result.append("    b.size_dist_lt_64_0,");
		result.append("    b.size_dist_lt_91_0,");
		result.append("    b.size_dist_lt_128_0");
		result.append("  FROM ");
		result.append("    (");
		result.append("    SELECT b.site_id,");
		result.append("      b.bed_meas_dt,");
		result.append("      b.group_id,");
		result.append("      b.sample_set,");
		result.append("      b.notes,");
		result.append("      b.station_location,");
		result.append("      b.bed_value");
		result.append("    FROM bed_material b)");
		result.append("  PIVOT");
		result.append("  (");
		result.append("    MAX(bed_value)");
		result.append("    FOR group_id");
		result.append("    IN (");
		result.append("      14 AS sample_weight,");
		result.append("      15 AS sand_d50,");
		result.append("      16 AS fines_d50,");
		result.append("      17 AS total_d50,");
		result.append("      18 AS pct_btwn_063_125,");
		result.append("      19 AS size_dist_lt_037,");
		result.append("      20 AS size_dist_lt_044,");
		result.append("      21 AS size_dist_lt_053,");
		result.append("      22 AS size_dist_lt_063,");
		result.append("      23 AS size_dist_lt_074,");
		result.append("      24 AS size_dist_lt_088,");
		result.append("      25 AS size_dist_lt_105,");
		result.append("      26 AS size_dist_lt_125,");
		result.append("      27 AS size_dist_lt_149,");
		result.append("      28 AS size_dist_lt_177,");
		result.append("      29 AS size_dist_lt_210,");
		result.append("      30 AS size_dist_lt_250,");
		result.append("      31 AS size_dist_lt_297,");
		result.append("      32 AS size_dist_lt_354,");
		result.append("      33 AS size_dist_lt_420,");
		result.append("      34 AS size_dist_lt_500,");
		result.append("      35 AS size_dist_lt_595,");
		result.append("      36 AS size_dist_lt_707,");
		result.append("      37 AS size_dist_lt_841,");
		result.append("      38 AS size_dist_lt_1_00,");
		result.append("      39 AS size_dist_lt_1_41,");
		result.append("      40 AS size_dist_lt_2_00,");
		result.append("      41 AS size_dist_lt_2_80,");
		result.append("      42 AS size_dist_lt_4_00,");
		result.append("      43 AS size_dist_lt_5_60,");
		result.append("      44 AS size_dist_lt_8_00,");
		result.append("      45 AS size_dist_lt_11_3,");
		result.append("      46 AS size_dist_lt_16_0,");
		result.append("      47 AS size_dist_lt_22_6,");
		result.append("      48 AS size_dist_lt_32_0,");
		result.append("      49 AS size_dist_lt_45_0,");
		result.append("      50 AS size_dist_lt_64_0,");
		result.append("      51 AS size_dist_lt_91_0,");
		result.append("      52 AS size_dist_lt_128_0");
		result.append("      )");
		result.append("  ) b,");
		result.append("  site_star s");
		result.append("  WHERE b.site_id = s.site_id");
		result.append(") T_A_BED_SEDIMENT");

		return result.toString();
	}

	public static final String C_STATION_NAME = "STATION_NAME";
	public static final String S_STATION_NAME = "Station name";
	public static final String C_STATION_NUM = "STATION_NUM";
	public static final String S_STATION_NUM = "Station #";
	public static final String SE_STATION_NUM = "stationNum";
	public static final String C_BED_DATE = "BED_DT";
	public static final String S_BED_DATE = "date";
	public static final String C_BED_TIME = "BED_TM";
	public static final String S_BED_TIME = "time";
	public static final String C_SAMPLE_SET = "SAMPLE_SET";
	public static final String S_SAMPLE_SET = "set";
	public static final String C_NOTES = "NOTES";
	public static final String S_NOTES = "NOTES";
	public static final String C_STATION_LOCATION = "STATION_LOCATION";
	public static final String S_STATION_LOCATION = "station location (ft)";
	public static final String C_SAMPLE_WEIGHT = "SAMPLE_WEIGHT";
	public static final String S_SAMPLE_WEIGHT = "sample mass (g)";
	public static final String C_SAND_D50 = "SAND_D50";
	public static final String S_SAND_D50 = "Sand D50 (mm)";
	public static final String C_FINES_D50 = "FINES_D50";
	public static final String S_FINES_D50 = "Fines D50 (mm)";
	public static final String C_TOTAL_D50 = "TOTAL_D50";
	public static final String S_TOTAL_D50 = "Total D50 (mm)";
	public static final String C_PCT_BTWN_063_125 = "PCT_BTWN_063_125";
	public static final String S_PCT_BTWN_063_125 = "% of sand finer than 0.125 mm";
	public static final String C_SIZE_DIST_LT_037 = "SIZE_DIST_LT_037";
	public static final String S_SIZE_DIST_LT_037 = "%< 0.037 mm";
	public static final String C_SIZE_DIST_LT_044 = "SIZE_DIST_LT_044";
	public static final String S_SIZE_DIST_LT_044 = "%< 0.044 mm";
	public static final String C_SIZE_DIST_LT_053 = "SIZE_DIST_LT_053";
	public static final String S_SIZE_DIST_LT_053 = "%< 0.053 mm";
	public static final String C_SIZE_DIST_LT_063 = "SIZE_DIST_LT_063";
	public static final String S_SIZE_DIST_LT_063 = "%< 0.063 mm";
	public static final String C_SIZE_DIST_LT_074 = "SIZE_DIST_LT_074";
	public static final String S_SIZE_DIST_LT_074 = "%< 0.074 mm";
	public static final String C_SIZE_DIST_LT_088 = "SIZE_DIST_LT_088";
	public static final String S_SIZE_DIST_LT_088 = "%< 0.088 mm";
	public static final String C_SIZE_DIST_LT_105 = "SIZE_DIST_LT_105";
	public static final String S_SIZE_DIST_LT_105 = "%< 0.105 mm";
	public static final String C_SIZE_DIST_LT_125 = "SIZE_DIST_LT_125";
	public static final String S_SIZE_DIST_LT_125 = "%< 0.125 mm";
	public static final String C_SIZE_DIST_LT_149 = "SIZE_DIST_LT_149";
	public static final String S_SIZE_DIST_LT_149 = "%< 0.149 mm";
	public static final String C_SIZE_DIST_LT_177 = "SIZE_DIST_LT_177";
	public static final String S_SIZE_DIST_LT_177 = "%< 0.177 mm";
	public static final String C_SIZE_DIST_LT_210 = "SIZE_DIST_LT_210";
	public static final String S_SIZE_DIST_LT_210 = "%< 0.210 mm";
	public static final String C_SIZE_DIST_LT_250 = "SIZE_DIST_LT_250";
	public static final String S_SIZE_DIST_LT_250 = "%< 0.250 mm";
	public static final String C_SIZE_DIST_LT_297 = "SIZE_DIST_LT_297";
	public static final String S_SIZE_DIST_LT_297 = "%< 0.297 mm";
	public static final String C_SIZE_DIST_LT_354 = "SIZE_DIST_LT_354";
	public static final String S_SIZE_DIST_LT_354 = "%< 0.354 mm";
	public static final String C_SIZE_DIST_LT_420 = "SIZE_DIST_LT_420";
	public static final String S_SIZE_DIST_LT_420 = "%< 0.420 mm";
	public static final String C_SIZE_DIST_LT_500 = "SIZE_DIST_LT_500";
	public static final String S_SIZE_DIST_LT_500 = "%< 0.500 mm";
	public static final String C_SIZE_DIST_LT_595 = "SIZE_DIST_LT_595";
	public static final String S_SIZE_DIST_LT_595 = "%< 0.595 mm";
	public static final String C_SIZE_DIST_LT_707 = "SIZE_DIST_LT_707";
	public static final String S_SIZE_DIST_LT_707 = "%< 0.707 mm";
	public static final String C_SIZE_DIST_LT_841 = "SIZE_DIST_LT_841";
	public static final String S_SIZE_DIST_LT_841 = "%< 0.841 mm";
	public static final String C_SIZE_DIST_LT_1_00 = "SIZE_DIST_LT_1_00";
	public static final String S_SIZE_DIST_LT_1_00 = "%< 1.00 mm";
	public static final String C_SIZE_DIST_LT_1_41 = "SIZE_DIST_LT_1_41";
	public static final String S_SIZE_DIST_LT_1_41 = "%< 1.41 mm";
	public static final String C_SIZE_DIST_LT_2_00 = "SIZE_DIST_LT_2_00";
	public static final String S_SIZE_DIST_LT_2_00 = "%< 2.0 mm";
	public static final String C_SIZE_DIST_LT_2_80 = "SIZE_DIST_LT_2_80";
	public static final String S_SIZE_DIST_LT_2_80 = "%< 2.8 mm";
	public static final String C_SIZE_DIST_LT_4_00 = "SIZE_DIST_LT_4_00";
	public static final String S_SIZE_DIST_LT_4_00 = "%< 4.0 mm";
	public static final String C_SIZE_DIST_LT_5_60 = "SIZE_DIST_LT_5_60";
	public static final String S_SIZE_DIST_LT_5_60 = "%< 5.6 mm";
	public static final String C_SIZE_DIST_LT_8_00 = "SIZE_DIST_LT_8_00";
	public static final String S_SIZE_DIST_LT_8_00 = "%< 8.0 mm";
	public static final String C_SIZE_DIST_LT_11_3 = "SIZE_DIST_LT_11_3";
	public static final String S_SIZE_DIST_LT_11_3 = "%< 11.3 mm";
	public static final String C_SIZE_DIST_LT_16_0 = "SIZE_DIST_LT_16_0";
	public static final String S_SIZE_DIST_LT_16_0 = "%< 16.0 mm ";
	public static final String C_SIZE_DIST_LT_22_6 = "SIZE_DIST_LT_22_6";
	public static final String S_SIZE_DIST_LT_22_6 = "%< 22.6 mm ";
	public static final String C_SIZE_DIST_LT_32_0 = "SIZE_DIST_LT_32_0";
	public static final String S_SIZE_DIST_LT_32_0 = "%< 32.0 mm";
	public static final String C_SIZE_DIST_LT_45_0 = "SIZE_DIST_LT_45_0";
	public static final String S_SIZE_DIST_LT_45_0 = "%<45.0 mm";
	public static final String C_SIZE_DIST_LT_64_0 = "SIZE_DIST_LT_64_0";
	public static final String S_SIZE_DIST_LT_64_0 = "%< 64.0 mm";
	public static final String C_SIZE_DIST_LT_91_0 = "SIZE_DIST_LT_91_0";
	public static final String S_SIZE_DIST_LT_91_0 = "%< 91.0 mm";
	public static final String C_SIZE_DIST_LT_128_0 = "SIZE_DIST_LT_128_0";
	public static final String S_SIZE_DIST_LT_128_0 = "%< 128.0 mm";
}
