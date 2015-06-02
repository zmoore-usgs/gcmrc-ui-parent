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
public class QWDownloadSpec extends Spec implements CentralizableTimezone {
	private static final Logger log = LoggerFactory.getLogger(QWDownloadSpec.class);

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
		return new ColumnMapping[] {
			new ColumnMapping(C_SAMPLE_ID, null),
			new ColumnMapping(C_STATION_NAME, S_STATION_NAME),
			new ColumnMapping(C_STATION_NUM, S_STATION_NUM),
			new ColumnMapping(C_START_DATE, SE_DEFAULT_ORDERING, ASCENDING_ORDER, S_START_DATE + " (" + getTimezoneDisplay() + ")", null, null, null, "TO_CHAR(" + C_START_DATE + ", 'YYYY-MM-DD\" \"HH24:MI:SS')", null, null),
			new ColumnMapping(C_MEAN_DATE, SE_MEAN_DATE, ASCENDING_ORDER, S_MEAN_DATE + " (" + getTimezoneDisplay() + ")", null, null, null, "TO_CHAR(" + C_MEAN_DATE + ", 'YYYY-MM-DD\" \"HH24:MI:SS')", null, null),
			new ColumnMapping(C_END_DATE, SE_END_DATE, ASCENDING_ORDER, S_END_DATE + " (" + getTimezoneDisplay() + ")", null, null, null, "TO_CHAR(" + C_END_DATE + ", 'YYYY-MM-DD\" \"HH24:MI:SS')", null, null),
			new ColumnMapping(C_USGS_DATA_LEAD, S_USGS_DATA_LEAD),
			new ColumnMapping(C_SAMPLE_METHOD, S_SAMPLE_METHOD),
			new ColumnMapping(C_SAMPLE_LOCATION, S_SAMPLE_LOCATION),
			new ColumnMapping(C_SAMPLER_NAME, S_SAMPLER_NAME),
			new ColumnMapping(C_NOZZLE, S_NOZZLE),
			new ColumnMapping(C_NUM_VERTICALS, S_NUM_VERTICALS),
			new ColumnMapping(C_TRANSITS_EACH_VERTICAL, S_TRANSITS_EACH_VERTICAL),
			new ColumnMapping(C_CABLEWAY_STATION_LOC, S_CABLEWAY_STATION_LOC),
			new ColumnMapping(C_WATER_DEPTH, S_WATER_DEPTH),
			new ColumnMapping(C_ELEVATION_ABOVE_BED, S_ELEVATION_ABOVE_BED),
			new ColumnMapping(C_SAMPLING_DURATION, S_SAMPLING_DURATION),
			new ColumnMapping(C_PUMP_SAMPLER, S_PUMP_SAMPLER),
			new ColumnMapping(C_PUMP_CAROUSEL_NUM, S_PUMP_CAROUSEL_NUM),
			new ColumnMapping(C_DATASET_COMPLETE, S_DATASET_COMPLETE),
			new ColumnMapping(C_CROSS_SECT_CALIB_REQ, S_CROSS_SECT_CALIB_REQ),
			new ColumnMapping(C_USE_FOR_LOAD_CALC, S_USE_FOR_LOAD_CALC),
			new ColumnMapping(C_NOTES, S_NOTES),
			new ColumnMapping(C_AIR_TEMP, S_AIR_TEMP),
			new ColumnMapping(C_WATER_TEMP, S_WATER_TEMP),
			new SlashOkColumnMapping(C_SPEC_COND, S_SPEC_COND),
			new SlashOkColumnMapping(C_TDS, S_TDS),
			new ColumnMapping(C_SILT_CLAY_COLOR, S_SILT_CLAY_COLOR),
			new ColumnMapping(C_CONC_LABORATORY, S_CONC_LABORATORY),
			new ColumnMapping(C_GRAIN_SIZE_LABORATORY, S_GRAIN_SIZE_LABORATORY),
			new ColumnMapping(C_LAB_NOTES, S_LAB_NOTES),
			new SlashOkColumnMapping(C_LAB_METHOD, S_LAB_METHOD),
			new SlashOkColumnMapping(C_SILT_CLAY_CONC_XS, S_SILT_CLAY_CONC_XS),
			new SlashOkColumnMapping(C_SAND_CONC_XS, S_SAND_CONC_XS),
			new ColumnMapping(C_SAND_D16_XS, S_SAND_D16_XS),
			new ColumnMapping(C_SAND_D50_XS, S_SAND_D50_XS),
			new ColumnMapping(C_SAND_D84_XS, S_SAND_D84_XS),
			new ColumnMapping(C_SAND_PCT_LT_074_XS, S_SAND_PCT_LT_074_XS),
			new ColumnMapping(C_SAND_PCT_LT_088_XS, S_SAND_PCT_LT_088_XS),
			new ColumnMapping(C_SAND_PCT_LT_105_XS, S_SAND_PCT_LT_105_XS),
			new ColumnMapping(C_SAND_PCT_LT_125_XS, S_SAND_PCT_LT_125_XS),
			new ColumnMapping(C_SAND_PCT_LT_149_XS, S_SAND_PCT_LT_149_XS),
			new ColumnMapping(C_SAND_PCT_LT_177_XS, S_SAND_PCT_LT_177_XS),
			new ColumnMapping(C_SAND_PCT_LT_210_XS, S_SAND_PCT_LT_210_XS),
			new ColumnMapping(C_SAND_PCT_LT_250_XS, S_SAND_PCT_LT_250_XS),
			new ColumnMapping(C_SAND_PCT_LT_297_XS, S_SAND_PCT_LT_297_XS),
			new ColumnMapping(C_SAND_PCT_LT_354_XS, S_SAND_PCT_LT_354_XS),
			new ColumnMapping(C_SAND_PCT_LT_420_XS, S_SAND_PCT_LT_420_XS),
			new ColumnMapping(C_SAND_PCT_LT_500_XS, S_SAND_PCT_LT_500_XS),
			new ColumnMapping(C_SAND_PCT_LT_595_XS, S_SAND_PCT_LT_595_XS),
			new ColumnMapping(C_SAND_PCT_LT_707_XS, S_SAND_PCT_LT_707_XS),
			new ColumnMapping(C_SAND_PCT_LT_841_XS, S_SAND_PCT_LT_841_XS),
			new ColumnMapping(C_SAND_PCT_LT_1000_XS, S_SAND_PCT_LT_1000_XS),
			new ColumnMapping(C_SAMPLE_MASS, S_SAMPLE_MASS),
			new ColumnMapping(C_MASS_LE_63, S_MASS_LE_63),
			new ColumnMapping(C_MASS_GT_63, S_MASS_GT_63),
			new SlashOkColumnMapping(C_CONC_LE_63, S_CONC_LE_63),
			new SlashOkColumnMapping(C_CONC_GT_63, S_CONC_GT_63),
			new SlashOkColumnMapping(C_SILT_CLAY_CONC_LAB, S_SILT_CLAY_CONC_LAB),
			new SlashOkColumnMapping(C_SAND_CONC_LAB, S_SAND_CONC_LAB),
			new ColumnMapping(C_SAND_D16_LAB, S_SAND_D16_LAB),
			new ColumnMapping(C_SAND_D50_LAB, S_SAND_D50_LAB),
			new ColumnMapping(C_SAND_D84_LAB, S_SAND_D84_LAB),
			new ColumnMapping(C_SAND_PCT_LT_074_LAB, S_SAND_PCT_LT_074_LAB),
			new ColumnMapping(C_SAND_PCT_LT_088_LAB, S_SAND_PCT_LT_088_LAB),
			new ColumnMapping(C_SAND_PCT_LT_105_LAB, S_SAND_PCT_LT_105_LAB),
			new ColumnMapping(C_SAND_PCT_LT_125_LAB, S_SAND_PCT_LT_125_LAB),
			new ColumnMapping(C_SAND_PCT_LT_149_LAB, S_SAND_PCT_LT_149_LAB),
			new ColumnMapping(C_SAND_PCT_LT_177_LAB, S_SAND_PCT_LT_177_LAB),
			new ColumnMapping(C_SAND_PCT_LT_210_LAB, S_SAND_PCT_LT_210_LAB),
			new ColumnMapping(C_SAND_PCT_LT_250_LAB, S_SAND_PCT_LT_250_LAB),
			new ColumnMapping(C_SAND_PCT_LT_297_LAB, S_SAND_PCT_LT_297_LAB),
			new ColumnMapping(C_SAND_PCT_LT_354_LAB, S_SAND_PCT_LT_354_LAB),
			new ColumnMapping(C_SAND_PCT_LT_420_LAB, S_SAND_PCT_LT_420_LAB),
			new ColumnMapping(C_SAND_PCT_LT_500_LAB, S_SAND_PCT_LT_500_LAB),
			new ColumnMapping(C_SAND_PCT_LT_595_LAB, S_SAND_PCT_LT_595_LAB),
			new ColumnMapping(C_SAND_PCT_LT_707_LAB, S_SAND_PCT_LT_707_LAB),
			new ColumnMapping(C_SAND_PCT_LT_841_LAB, S_SAND_PCT_LT_841_LAB),
			new ColumnMapping(C_SAND_PCT_LT_1000_LAB, S_SAND_PCT_LT_1000_LAB),
			new SlashOkColumnMapping(C_SILT_CLAY_FIELD_95ER, S_SILT_CLAY_FIELD_95ER),
			new SlashOkColumnMapping(C_SAND_FIELD_95ER, S_SAND_FIELD_95ER),
			new ColumnMapping(C_SAND_D50_FIELD_95ER, S_SAND_D50_FIELD_95ER),
			new SlashOkColumnMapping(C_SILT_CLAY_LAB_BIAS_COR, S_SILT_CLAY_LAB_BIAS_COR),
			new SlashOkColumnMapping(C_SILT_CLAY_LAB_95ER, S_SILT_CLAY_LAB_95ER),
			new SlashOkColumnMapping(C_SAND_LAB_95ER, S_SAND_LAB_95ER),
			new ColumnMapping(C_SAND_D50_LAB_95ER, S_SAND_D50_LAB_95ER),
			new SlashOkColumnMapping(C_SILT_CLAY_TOT_95ER, S_SILT_CLAY_TOT_95ER),
			new SlashOkColumnMapping(C_SAND_TOT_95ER, S_SAND_TOT_95ER),
			new ColumnMapping(C_SAND_D50_TOT_95ER, S_SAND_D50_TOT_95ER)
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
			new SearchMapping(SE_SAMPLE_ID, C_SAMPLE_ID, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(SE_STATION_NAME, C_STATION_NAME, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(SE_STATION_NUM, C_STATION_NUM, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(Endpoint.BEGIN_KEYWORD, C_START_DATE, null, WhereClauseType.special, CleaningOption.none, FIELD_NAME_KEY + " >= TO_DATE(" + USER_VALUE_KEY + ", 'YYYY-MM-DD\"T\"HH24:MI:SS')", null),
			new SearchMapping(Endpoint.END_KEYWORD, C_START_DATE, null, WhereClauseType.special, CleaningOption.none, FIELD_NAME_KEY + " <= TO_DATE(" + USER_VALUE_KEY + ", 'YYYY-MM-DD\"T\"HH24:MI:SS')", null),
			new SearchMapping(SE_USGS_DATA_LEAD, C_USGS_DATA_LEAD, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(SE_SAMPLE_METHOD, C_SAMPLE_METHOD, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(SE_SAMPLE_LOCATION, C_SAMPLE_LOCATION, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(SE_SAMPLER_NAME, C_SAMPLER_NAME, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(SE_NOZZLE, C_NOZZLE, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(SE_NUM_VERTICALS, C_NUM_VERTICALS, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(SE_TRANSITS_EACH_VERTICAL, C_TRANSITS_EACH_VERTICAL, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(SE_CABLEWAY_STATION_LOC, C_CABLEWAY_STATION_LOC, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(SE_WATER_DEPTH, C_WATER_DEPTH, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(SE_ELEVATION_ABOVE_BED, C_ELEVATION_ABOVE_BED, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(SE_SAMPLING_DURATION, C_SAMPLING_DURATION, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(SE_PUMP_SAMPLER, C_PUMP_SAMPLER, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(SE_PUMP_CAROUSEL_NUM, C_PUMP_CAROUSEL_NUM, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(SE_DATASET_COMPLETE, C_DATASET_COMPLETE, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(SE_CROSS_SECT_CALIB_REQ, C_CROSS_SECT_CALIB_REQ, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(SE_USE_FOR_LOAD_CALC, C_USE_FOR_LOAD_CALC, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(SE_SERVE, C_SERVE, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(SE_NOTES, C_NOTES, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(SE_SILT_CLAY_COLOR, C_SILT_CLAY_COLOR, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(SE_CONC_LABORATORY, C_CONC_LABORATORY, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(SE_GRAIN_SIZE_LABORATORY, C_GRAIN_SIZE_LABORATORY, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(SE_LAB_NOTES, C_LAB_NOTES, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(SE_LAB_METHOD, C_LAB_METHOD, null, WhereClauseType.equals, null, null, null)
		};
	}

	@Override
	public String setupTableName() {
		StringBuilder result = new StringBuilder();
		
		result.append("(SELECT s.sample_id,");
		result.append("        si.name station_name,");
		result.append("        nvl2(si.nwis_site_no, si.nwis_site_no, si.short_name) station_num,");
		result.append("        s.start_date ").append(getTimezoneSql()).append(" AS START_DT,");
		result.append("        s.average_date ").append(getTimezoneSql()).append(" AS MEAN_DT,");
		result.append("        s.end_date ").append(getTimezoneSql()).append(" AS END_DT,");
		result.append("        d.name usgs_data_lead,");
		result.append("        sm.sample_method,");
		result.append("        sub.name sample_location,");
		result.append("        sa.sampler_name,");
		result.append("        s.nozzle,");
		result.append("        s.num_verticals,");
		result.append("        s.transits_each_vertical,");
		result.append("        s.cableway_station_loc,");
		result.append("        s.water_depth,");
		result.append("        s.elevation_above_bed,");
		result.append("        s.sampling_duration,");
		result.append("        s.pump_sampler,");
		result.append("        s.pump_carousel_num,");
		result.append("        s.dataset_complete,");
		result.append("        s.cross_sect_calib_req,");
		result.append("        s.use_for_load_calc,");
		result.append("        s.serve,");
		result.append("        s.notes,");
		result.append("        f.fines_color silt_clay_color,");
		result.append("        lc.code conc_laboratory,");
		result.append("        gs.code grain_size_laboratory,");
		result.append("        s.lab_notes,");
		result.append("        lm.lab_method,");
		result.append("        r.spec_cond,");
		result.append("        r.air_temp,");
		result.append("        r.water_temp,");
		result.append("        r.silt_clay_conc_lab,");
		result.append("        r.sand_conc_lab,");
		result.append("        r.sample_mass,");
		result.append("        r.mass_le_63,");
		result.append("        r.mass_gt_63,");
		result.append("        r.conc_le_63,");
		result.append("        r.conc_gt_63,");
		result.append("        r.tds,");
		result.append("        r.SAND_D16_LAB,");
		result.append("        r.SAND_D50_LAB,");
		result.append("        r.SAND_D84_LAB,");
		result.append("        r.SAND_PCT_LT_074_LAB,");
		result.append("        r.sAND_PCT_LT_088_LAB,");
		result.append("        r.SAND_PCT_LT_105_LAB,");
		result.append("        r.SAND_PCT_LT_125_LAB,");
		result.append("        r.SAND_PCT_LT_149_LAB,");
		result.append("        r.SAND_PCT_LT_177_LAB,");
		result.append("        r.SAND_PCT_LT_210_LAB,");
		result.append("        r.SAND_PCT_LT_250_LAB,");
		result.append("        r.SAND_PCT_LT_297_LAB,");
		result.append("        r.SAND_PCT_LT_354_LAB,");
		result.append("        r.SAND_PCT_LT_420_LAB,");
		result.append("        r.SAND_PCT_LT_500_LAB,");
		result.append("        r.SAND_PCT_LT_595_LAB,");
		result.append("        r.SAND_PCT_LT_707_LAB,");
		result.append("        r.SAND_PCT_LT_841_LAB,");
		result.append("        r.SAND_PCT_LT_1000_LAB,");
		result.append("        r.SILT_CLAY_CONC_XS,");
		result.append("        r.SAND_CONC_XS,");
		result.append("        r.SAND_D16_XS,");
		result.append("        r.SAND_D50_XS,");
		result.append("        r.SAND_D84_XS,");
		result.append("        r.SAND_PCT_LT_074_XS,");
		result.append("        r.SAND_PCT_LT_088_XS,");
		result.append("        r.SAND_PCT_LT_105_XS,");
		result.append("        r.SAND_PCT_LT_125_XS,");
		result.append("        r.SAND_PCT_LT_149_XS,");
		result.append("        r.SAND_PCT_LT_177_XS,");
		result.append("        r.SAND_PCT_LT_210_XS,");
		result.append("        r.SAND_PCT_LT_250_XS,");
		result.append("        r.SAND_PCT_LT_297_XS,");
		result.append("        r.SAND_PCT_LT_354_XS,");
		result.append("        r.SAND_PCT_LT_420_XS,");
		result.append("        r.SAND_PCT_LT_500_XS,");
		result.append("        r.SAND_PCT_LT_595_XS,");
		result.append("        r.SAND_PCT_LT_707_XS,");
		result.append("        r.SAND_PCT_LT_841_XS,");
		result.append("        r.SAND_PCT_LT_1000_XS,");
		result.append("        rs.silt_clay_field_95er,");
		result.append("        rs.sand_field_95er,");
		result.append("        rs.sand_d50_field_95er,");
		result.append("        rs.silt_clay_lab_bias_cor,");
		result.append("        rs.silt_clay_lab_95er,");
		result.append("        rs.sand_lab_95er,");
		result.append("        rs.sand_d50_lab_95er,");
		result.append("        rs.silt_clay_tot_95er,");
		result.append("        rs.sand_tot_95er,");
		result.append("        rs.sand_d50_tot_95er");
		result.append(" FROM");
		result.append("    (");
		result.append("    SELECT sample_id,");
		result.append("        group_id,");
		result.append("        suspsed_value");
		result.append("    FROM result_star");
		result.append("    )");
		result.append(" PIVOT");
		result.append(" (");
		result.append("    AVG(suspsed_value) ");
		result.append("    FOR group_id");
		result.append("    IN (");
		result.append("        110 AS spec_cond,");
		result.append("        112 AS air_temp,");
		result.append("        113 AS water_temp,");
		result.append("        63 AS silt_clay_conc_lab,");
		result.append("        64 AS sand_conc_lab,");
		result.append("        84 AS sample_mass,");
		result.append("        85 AS mass_le_63,");
		result.append("        86 AS mass_gt_63,");
		result.append("        87 AS conc_le_63,");
		result.append("        88 AS conc_gt_63,");
		result.append("        111 AS tds,");
		result.append("        65 AS SAND_D16_LAB,");
		result.append("        66 AS SAND_D50_LAB,");
		result.append("        67 AS SAND_D84_LAB,");
		result.append("        68 AS SAND_PCT_LT_074_LAB,");
		result.append("        69 AS sAND_PCT_LT_088_LAB,");
		result.append("        70 AS SAND_PCT_LT_105_LAB,");
		result.append("        71 AS SAND_PCT_LT_125_LAB,");
		result.append("        72 AS SAND_PCT_LT_149_LAB,");
		result.append("        73 AS SAND_PCT_LT_177_LAB,");
		result.append("        74 AS SAND_PCT_LT_210_LAB,");
		result.append("        75 AS SAND_PCT_LT_250_LAB,");
		result.append("        76 AS SAND_PCT_LT_297_LAB,");
		result.append("        77 AS SAND_PCT_LT_354_LAB,");
		result.append("        78 AS SAND_PCT_LT_420_LAB,");
		result.append("        79 AS SAND_PCT_LT_500_LAB,");
		result.append("        80 AS SAND_PCT_LT_595_LAB,");
		result.append("        81 AS SAND_PCT_LT_707_LAB,");
		result.append("        82 AS SAND_PCT_LT_841_LAB,");
		result.append("        83 AS SAND_PCT_LT_1000_LAB,");
		result.append("        89 AS SILT_CLAY_CONC_XS,");
		result.append("        90 AS SAND_CONC_XS,");
		result.append("        91 AS SAND_D16_XS,");
		result.append("        92 AS SAND_D50_XS,");
		result.append("        93 AS SAND_D84_XS,");
		result.append("        94 AS SAND_PCT_LT_074_XS,");
		result.append("        95 AS SAND_PCT_LT_088_XS,");
		result.append("        96 AS SAND_PCT_LT_105_XS,");
		result.append("        97 AS SAND_PCT_LT_125_XS,");
		result.append("        98 AS SAND_PCT_LT_149_XS,");
		result.append("        99 AS SAND_PCT_LT_177_XS,");
		result.append("        100 AS SAND_PCT_LT_210_XS,");
		result.append("        101 AS SAND_PCT_LT_250_XS,");
		result.append("        102 AS SAND_PCT_LT_297_XS,");
		result.append("        103 AS SAND_PCT_LT_354_XS,");
		result.append("        104 AS SAND_PCT_LT_420_XS,");
		result.append("        105 AS SAND_PCT_LT_500_XS,");
		result.append("        106 AS SAND_PCT_LT_595_XS,");
		result.append("        107 AS SAND_PCT_LT_707_XS,");
		result.append("        108 AS SAND_PCT_LT_841_XS,");
		result.append("        109 AS SAND_PCT_LT_1000_XS");
		result.append("    )");
		result.append(" ) r,");
		result.append(" sample_star s, ");
		result.append(" site_star si,");
		result.append(" data_lead_star d,");
		result.append(" sample_method_star sm,");
		result.append(" subsite_star sub,");
		result.append(" sampler_star sa,");
		result.append(" fines_color_star f,");
		result.append(" laboratory_star lc,");
		result.append(" laboratory_star gs,");
		result.append(" lab_method_star lm,");
		result.append(" (SELECT r.sample_id,");
		result.append("        MAX(CASE");
		result.append("            WHEN r.group_id = 89 THEN r.field_95conf");
		result.append("            ELSE NULL");
		result.append("        END) silt_clay_field_95er,");
		result.append("        MAX(CASE");
		result.append("            WHEN r.group_id = 90 THEN r.field_95conf");
		result.append("            ELSE NULL");
		result.append("        END) sand_field_95er,");
		result.append("        MAX(CASE");
		result.append("            WHEN r.group_id = 92 THEN r.field_95conf");
		result.append("            ELSE NULL");
		result.append("        END) sand_d50_field_95er,");
		result.append("        MAX(CASE");
		result.append("            WHEN r.group_id = 89 THEN r.bias_correction");
		result.append("            ELSE NULL");
		result.append("        END) silt_clay_lab_bias_cor,");
		result.append("        MAX(CASE");
		result.append("            WHEN r.group_id = 89 THEN r.lab_95conf");
		result.append("            ELSE NULL");
		result.append("        END) silt_clay_lab_95er,");
		result.append("        MAX(CASE");
		result.append("            WHEN r.group_id = 90 THEN r.lab_95conf");
		result.append("            ELSE NULL");
		result.append("        END) sand_lab_95er,");
		result.append("        MAX(CASE");
		result.append("            WHEN r.group_id = 92 THEN r.lab_95conf");
		result.append("            ELSE NULL");
		result.append("        END) sand_d50_lab_95er,");
		result.append("        MAX(CASE");
		result.append("            WHEN r.group_id = 89 THEN r.total_95conf");
		result.append("            ELSE NULL");
		result.append("        END) silt_clay_tot_95er,");
		result.append("        MAX(CASE");
		result.append("            WHEN r.group_id = 90 THEN r.total_95conf");
		result.append("            ELSE NULL");
		result.append("        END) sand_tot_95er,");
		result.append("        MAX(CASE");
		result.append("            WHEN r.group_id = 92 THEN r.total_95conf");
		result.append("            ELSE NULL");
		result.append("        END) sand_d50_tot_95er");
		result.append("    FROM result_star r");
		result.append("    GROUP BY r.sample_id)  rs");
		result.append(" WHERE s.site_id = si.site_id");
		result.append("    AND s.data_lead_id = d.data_lead_id (+)");
		result.append("    AND s.sample_method_id = sm.sample_method_id (+)");
		result.append("    AND s.subsite_id = sub.subsite_id (+)");
		result.append("    AND s.sampler_id = sa.sampler_id (+)");
		result.append("    AND s.fines_color_id = f.fines_color_id (+)");
		result.append("    AND s.concentration_lab_id = lc.laboratory_id (+)");
		result.append("    AND s.lab_method_id = lm.lab_method_id (+)");
		result.append("    AND s.grain_size_lab_id = gs.laboratory_id (+)");
		result.append("    AND s.sample_id = r.sample_id (+)");
		result.append("    AND s.sample_id = rs.sample_id (+)");
		result.append("    AND s.serve = 'YES'");
		result.append(" ) T_A_INNER");
		
		return result.toString();
	}
	
	public static final String C_SAMPLE_ID = "SAMPLE_ID";
	public static final String C_STATION_NAME = "STATION_NAME";
	public static final String C_STATION_NUM = "STATION_NUM";
	public static final String C_START_DATE = "START_DT";
	public static final String C_MEAN_DATE = "MEAN_DT";
	public static final String C_END_DATE = "END_DT";
	public static final String C_USGS_DATA_LEAD = "USGS_DATA_LEAD";
	public static final String C_SAMPLE_METHOD = "SAMPLE_METHOD";
	public static final String C_SAMPLE_LOCATION = "SAMPLE_LOCATION";
	public static final String C_SAMPLER_NAME = "SAMPLER_NAME";
	public static final String C_NOZZLE = "NOZZLE";
	public static final String C_NUM_VERTICALS = "NUM_VERTICALS";
	public static final String C_TRANSITS_EACH_VERTICAL = "TRANSITS_EACH_VERTICAL";
	public static final String C_CABLEWAY_STATION_LOC = "CABLEWAY_STATION_LOC";
	public static final String C_WATER_DEPTH = "WATER_DEPTH";
	public static final String C_ELEVATION_ABOVE_BED = "ELEVATION_ABOVE_BED";
	public static final String C_SAMPLING_DURATION = "SAMPLING_DURATION";
	public static final String C_PUMP_SAMPLER = "PUMP_SAMPLER";
	public static final String C_PUMP_CAROUSEL_NUM = "PUMP_CAROUSEL_NUM";
	public static final String C_DATASET_COMPLETE = "DATASET_COMPLETE";
	public static final String C_CROSS_SECT_CALIB_REQ = "CROSS_SECT_CALIB_REQ";
	public static final String C_USE_FOR_LOAD_CALC = "USE_FOR_LOAD_CALC";
	public static final String C_SERVE = "SERVE";
	public static final String C_NOTES = "NOTES";
	public static final String C_SILT_CLAY_COLOR = "SILT_CLAY_COLOR";
	public static final String C_CONC_LABORATORY = "CONC_LABORATORY";
	public static final String C_GRAIN_SIZE_LABORATORY = "GRAIN_SIZE_LABORATORY";
	public static final String C_LAB_NOTES = "LAB_NOTES";
	public static final String C_LAB_METHOD = "LAB_METHOD";
	public static final String C_SPEC_COND = "SPEC_COND";
	public static final String C_AIR_TEMP = "AIR_TEMP";
	public static final String C_WATER_TEMP = "WATER_TEMP";
	public static final String C_SILT_CLAY_CONC_LAB = "SILT_CLAY_CONC_LAB";
	public static final String C_SAND_CONC_LAB = "SAND_CONC_LAB";
	public static final String C_SAMPLE_MASS = "SAMPLE_MASS";
	public static final String C_MASS_LE_63 = "MASS_LE_63";
	public static final String C_MASS_GT_63 = "MASS_GT_63";
	public static final String C_CONC_LE_63 = "CONC_LE_63";
	public static final String C_CONC_GT_63 = "CONC_GT_63";
	public static final String C_TDS = "TDS";
	public static final String C_SAND_D16_LAB = "SAND_D16_LAB";
	public static final String C_SAND_D50_LAB = "SAND_D50_LAB";
	public static final String C_SAND_D84_LAB = "SAND_D84_LAB";
	public static final String C_SAND_PCT_LT_074_LAB = "SAND_PCT_LT_074_LAB";
	public static final String C_SAND_PCT_LT_088_LAB = "SAND_PCT_LT_088_LAB";
	public static final String C_SAND_PCT_LT_105_LAB = "SAND_PCT_LT_105_LAB";
	public static final String C_SAND_PCT_LT_125_LAB = "SAND_PCT_LT_125_LAB";
	public static final String C_SAND_PCT_LT_149_LAB = "SAND_PCT_LT_149_LAB";
	public static final String C_SAND_PCT_LT_177_LAB = "SAND_PCT_LT_177_LAB";
	public static final String C_SAND_PCT_LT_210_LAB = "SAND_PCT_LT_210_LAB";
	public static final String C_SAND_PCT_LT_250_LAB = "SAND_PCT_LT_250_LAB";
	public static final String C_SAND_PCT_LT_297_LAB = "SAND_PCT_LT_297_LAB";
	public static final String C_SAND_PCT_LT_354_LAB = "SAND_PCT_LT_354_LAB";
	public static final String C_SAND_PCT_LT_420_LAB = "SAND_PCT_LT_420_LAB";
	public static final String C_SAND_PCT_LT_500_LAB = "SAND_PCT_LT_500_LAB";
	public static final String C_SAND_PCT_LT_595_LAB = "SAND_PCT_LT_595_LAB";
	public static final String C_SAND_PCT_LT_707_LAB = "SAND_PCT_LT_707_LAB";
	public static final String C_SAND_PCT_LT_841_LAB = "SAND_PCT_LT_841_LAB";
	public static final String C_SAND_PCT_LT_1000_LAB = "SAND_PCT_LT_1000_LAB";
	public static final String C_SILT_CLAY_CONC_XS = "SILT_CLAY_CONC_XS";
	public static final String C_SAND_CONC_XS = "SAND_CONC_XS";
	public static final String C_SAND_D16_XS = "SAND_D16_XS";
	public static final String C_SAND_D50_XS = "SAND_D50_XS";
	public static final String C_SAND_D84_XS = "SAND_D84_XS";
	public static final String C_SAND_PCT_LT_074_XS = "SAND_PCT_LT_074_XS";
	public static final String C_SAND_PCT_LT_088_XS = "SAND_PCT_LT_088_XS";
	public static final String C_SAND_PCT_LT_105_XS = "SAND_PCT_LT_105_XS";
	public static final String C_SAND_PCT_LT_125_XS = "SAND_PCT_LT_125_XS";
	public static final String C_SAND_PCT_LT_149_XS = "SAND_PCT_LT_149_XS";
	public static final String C_SAND_PCT_LT_177_XS = "SAND_PCT_LT_177_XS";
	public static final String C_SAND_PCT_LT_210_XS = "SAND_PCT_LT_210_XS";
	public static final String C_SAND_PCT_LT_250_XS = "SAND_PCT_LT_250_XS";
	public static final String C_SAND_PCT_LT_297_XS = "SAND_PCT_LT_297_XS";
	public static final String C_SAND_PCT_LT_354_XS = "SAND_PCT_LT_354_XS";
	public static final String C_SAND_PCT_LT_420_XS = "SAND_PCT_LT_420_XS";
	public static final String C_SAND_PCT_LT_500_XS = "SAND_PCT_LT_500_XS";
	public static final String C_SAND_PCT_LT_595_XS = "SAND_PCT_LT_595_XS";
	public static final String C_SAND_PCT_LT_707_XS = "SAND_PCT_LT_707_XS";
	public static final String C_SAND_PCT_LT_841_XS = "SAND_PCT_LT_841_XS";
	public static final String C_SAND_PCT_LT_1000_XS = "SAND_PCT_LT_1000_XS";
	public static final String C_SILT_CLAY_FIELD_95ER = "SILT_CLAY_FIELD_95ER";
	public static final String C_SAND_FIELD_95ER = "SAND_FIELD_95ER";
	public static final String C_SAND_D50_FIELD_95ER = "SAND_D50_FIELD_95ER";
	public static final String C_SILT_CLAY_LAB_BIAS_COR = "SILT_CLAY_LAB_BIAS_COR";
	public static final String C_SILT_CLAY_LAB_95ER = "SILT_CLAY_LAB_95ER";
	public static final String C_SAND_LAB_95ER = "SAND_LAB_95ER";
	public static final String C_SAND_D50_LAB_95ER = "SAND_D50_LAB_95ER";
	public static final String C_SILT_CLAY_TOT_95ER = "SILT_CLAY_TOT_95ER";
	public static final String C_SAND_TOT_95ER = "SAND_TOT_95ER";
	public static final String C_SAND_D50_TOT_95ER = "SAND_D50_TOT_95ER";

	public static final String S_SAMPLE_ID = "sampleId";
	public static final String S_STATION_NAME = "Station name";
	public static final String S_STATION_NUM = "USGS Station #";
	public static final String S_START_DATE = "start time";
	public static final String S_MEAN_DATE = "mean time";
	public static final String S_END_DATE = "end time";
	public static final String S_USGS_DATA_LEAD = "USGS data lead";
	public static final String S_SAMPLE_METHOD = "Sampling method";
	public static final String S_SAMPLE_LOCATION = "Location";
	public static final String S_SAMPLER_NAME = "Sampler";
	public static final String S_NOZZLE = "Nozzle";
	public static final String S_NUM_VERTICALS = "Verticals";
	public static final String S_TRANSITS_EACH_VERTICAL = "Transits at each vertical";
	public static final String S_CABLEWAY_STATION_LOC = "Cableway station location (ft)";
	public static final String S_WATER_DEPTH = "Water depth (m)";
	public static final String S_ELEVATION_ABOVE_BED = "Sample elevation above bed (m)";
	public static final String S_SAMPLING_DURATION = "Sampling duration (s)";
	public static final String S_PUMP_SAMPLER = "Pump sampler A or B";
	public static final String S_PUMP_CAROUSEL_NUM = "Pump carousel number";
	public static final String S_DATASET_COMPLETE = "Dataset complete through this sample";
	public static final String S_CROSS_SECT_CALIB_REQ = "Cross-section calibration applied";
	public static final String S_USE_FOR_LOAD_CALC = "Use in load calculations";
	public static final String S_SERVE = "SERVE?";
	public static final String S_NOTES = "Notes";
	public static final String S_SILT_CLAY_COLOR = "Color of silt&clay";
	public static final String S_CONC_LABORATORY = "Concentration analysis laboratory";
	public static final String S_GRAIN_SIZE_LABORATORY = "Grain-size analysis laboratory";
	public static final String S_LAB_NOTES = "Lab notes";
	public static final String S_LAB_METHOD = "Method used for determining sand/fines break and sand grain-size distribution";
	public static final String S_SPEC_COND = "Specific conductance (microsiemens/cm at 25 deg. C)";
	public static final String S_AIR_TEMP = "Air temp (deg C)";
	public static final String S_WATER_TEMP = "Water temp (deg C)";
	public static final String S_SILT_CLAY_CONC_LAB = "Laboratory silt&clay concentration (mg/L)";
	public static final String S_SAND_CONC_LAB = "Laboratory sand concentration (mg/L)";
	public static final String S_SAMPLE_MASS = "Sample Mass (g)";
	public static final String S_MASS_LE_63 = "Mass passing through 63 micron sieve (g)";
	public static final String S_MASS_GT_63 = "Mass retained on 63 micron sieve (g)";
	public static final String S_CONC_LE_63 = "Concentration <63 micron sieve (mg/L)";
	public static final String S_CONC_GT_63 = "Concentration >63 micron sieve (mg/L)";
	public static final String S_TDS = "TDS (mg/L)";
	public static final String S_SAND_D16_LAB = "Raw lab sand D16(mm)";
	public static final String S_SAND_D50_LAB = "Raw lab sand D50(mm)";
	public static final String S_SAND_D84_LAB = "Raw lab sand D84(mm)";
	public static final String S_SAND_PCT_LT_074_LAB = "Raw lab sand %< 0.074 mm";
	public static final String S_SAND_PCT_LT_088_LAB = "Raw lab sand %< 0.088 mm";
	public static final String S_SAND_PCT_LT_105_LAB = "Raw lab sand %< 0.105 mm";
	public static final String S_SAND_PCT_LT_125_LAB = "Raw lab sand %< 0.125 mm";
	public static final String S_SAND_PCT_LT_149_LAB = "Raw lab sand %< 0.149 mm";
	public static final String S_SAND_PCT_LT_177_LAB = "Raw lab sand %< 0.177 mm";
	public static final String S_SAND_PCT_LT_210_LAB = "Raw lab sand %< 0.210 mm";
	public static final String S_SAND_PCT_LT_250_LAB = "Raw lab sand %< 0.250 mm";
	public static final String S_SAND_PCT_LT_297_LAB = "Raw lab sand %< 0.297 mm";
	public static final String S_SAND_PCT_LT_354_LAB = "Raw lab sand %< 0.354 mm";
	public static final String S_SAND_PCT_LT_420_LAB = "Raw lab sand %< 0.420 mm";
	public static final String S_SAND_PCT_LT_500_LAB = "Raw lab sand %< 0.500 mm";
	public static final String S_SAND_PCT_LT_595_LAB = "Raw lab sand %< 0.595 mm";
	public static final String S_SAND_PCT_LT_707_LAB = "Raw lab sand %< 0.707 mm";
	public static final String S_SAND_PCT_LT_841_LAB = "Raw lab sand %< 0.841 mm";
	public static final String S_SAND_PCT_LT_1000_LAB = "Raw lab sand %< 1.0 mm";
	public static final String S_SILT_CLAY_CONC_XS = "Cross-section silt&clay concentration (mg/L)";
	public static final String S_SAND_CONC_XS = "Cross-section sand concentration (mg/L)";
	public static final String S_SAND_D16_XS = "Cross-section sand D16(mm)";
	public static final String S_SAND_D50_XS = "Cross-section sand D50(mm)";
	public static final String S_SAND_D84_XS = "Cross-section sand D84(mm)";
	public static final String S_SAND_PCT_LT_074_XS = "Cross-section sand %< 0.074 mm";
	public static final String S_SAND_PCT_LT_088_XS = "Cross-section sand %< 0.088 mm";
	public static final String S_SAND_PCT_LT_105_XS = "Cross-section sand %< 0.105 mm";
	public static final String S_SAND_PCT_LT_125_XS = "Cross-section sand %< 0.125 mm";
	public static final String S_SAND_PCT_LT_149_XS = "Cross-section sand %< 0.149 mm";
	public static final String S_SAND_PCT_LT_177_XS = "Cross-section sand %< 0.177 mm";
	public static final String S_SAND_PCT_LT_210_XS = "Cross-section sand %< 0.210 mm";
	public static final String S_SAND_PCT_LT_250_XS = "Cross-section sand %< 0.250 mm";
	public static final String S_SAND_PCT_LT_297_XS = "Cross-section sand %< 0.297 mm";
	public static final String S_SAND_PCT_LT_354_XS = "Cross-section sand %< 0.354 mm";
	public static final String S_SAND_PCT_LT_420_XS = "Cross-section sand %< 0.420 mm";
	public static final String S_SAND_PCT_LT_500_XS = "Cross-section sand %< 0.500 mm";
	public static final String S_SAND_PCT_LT_595_XS = "Cross-section sand %< 0.595 mm";
	public static final String S_SAND_PCT_LT_707_XS = "Cross-section sand %< 0.707 mm";
	public static final String S_SAND_PCT_LT_841_XS = "Cross-section sand %< 0.841 mm";
	public static final String S_SAND_PCT_LT_1000_XS = "Cross-section sand %< 1.0 mm";
	public static final String S_SILT_CLAY_FIELD_95ER = "95%-confidence-level field error in silt&clay concentration (mg/L)";
	public static final String S_SAND_FIELD_95ER = "95%-confidence-level field error in sand concentration (mg/L)";
	public static final String S_SAND_D50_FIELD_95ER = "95%-confidence-level field error in sand D50 (mm)";
	public static final String S_SILT_CLAY_LAB_BIAS_COR = "Correction for negative laboratory bias in silt&clay conc. (mg/L)";
	public static final String S_SILT_CLAY_LAB_95ER = "95%-confidence-level lab error in bias-corr. silt&clay concentration (mg/L)";
	public static final String S_SAND_LAB_95ER = "95%-confidence-level lab error in sand concentration (mg/L)";
	public static final String S_SAND_D50_LAB_95ER = "95%-confidence-level lab error in sand D50 (mm)";
	public static final String S_SILT_CLAY_TOT_95ER = "95%-confidence-level total error in bias-corr. silt&clay concentration (mg/L)";
	public static final String S_SAND_TOT_95ER = "95%-confidence-level total error in sand concentration (mg/L)";
	public static final String S_SAND_D50_TOT_95ER = "95%-confidence-level total error in sand D50(mm)";
	
	public static final String SE_DEFAULT_ORDERING = "defaultOrder";
	public static final String SE_SECONDARY_ORDERING = "secondaryOrder";
	
	public static final String SE_SAMPLE_ID = "sampleId";
	public static final String SE_STATION_NAME = "stationName";
	public static final String SE_STATION_NUM = "stationNum";
	public static final String SE_START_DATE = "startDate";
	public static final String SE_MEAN_DATE = "meanDate";
	public static final String SE_END_DATE = "endDate";
	public static final String SE_USGS_DATA_LEAD = "usgsDataLead";
	public static final String SE_SAMPLE_METHOD = "sampleMethod";
	public static final String SE_SAMPLE_LOCATION = "sampleLocation";
	public static final String SE_SAMPLER_NAME = "samplerName";
	public static final String SE_NOZZLE = "nozzle";
	public static final String SE_NUM_VERTICALS = "numVerticals";
	public static final String SE_TRANSITS_EACH_VERTICAL = "transitsEachVertical";
	public static final String SE_CABLEWAY_STATION_LOC = "cablewayStationLoc";
	public static final String SE_WATER_DEPTH = "waterDepth";
	public static final String SE_ELEVATION_ABOVE_BED = "elevationAboveBed";
	public static final String SE_SAMPLING_DURATION = "samplingDuration";
	public static final String SE_PUMP_SAMPLER = "pumpSampler";
	public static final String SE_PUMP_CAROUSEL_NUM = "pumpCarouselNum";
	public static final String SE_DATASET_COMPLETE = "datasetComplete";
	public static final String SE_CROSS_SECT_CALIB_REQ = "crossSectCalibReq";
	public static final String SE_USE_FOR_LOAD_CALC = "useForLoadCalc";
	public static final String SE_SERVE = "serve";
	public static final String SE_NOTES = "notes";
	public static final String SE_SILT_CLAY_COLOR = "siltClayColor";
	public static final String SE_CONC_LABORATORY = "concLaboratory";
	public static final String SE_GRAIN_SIZE_LABORATORY = "grainSizeLaboratory";
	public static final String SE_LAB_NOTES = "labNotes";
	public static final String SE_LAB_METHOD = "labMethod";
	public static final String SE_SPEC_COND = "specCond";
	public static final String SE_AIR_TEMP = "airTemp";
	public static final String SE_WATER_TEMP = "waterTemp";
	public static final String SE_SILT_CLAY_CONC_LAB = "siltClayConcLab";
	public static final String SE_SAND_CONC_LAB = "sandConcLab";
	public static final String SE_SAMPLE_MASS = "sampleMass";
	public static final String SE_MASS_LE_63 = "massLe63";
	public static final String SE_MASS_GT_63 = "massGt63";
	public static final String SE_CONC_LE_63 = "concLe63";
	public static final String SE_CONC_GT_63 = "concGt63";
	public static final String SE_TDS = "tds";
	public static final String SE_SAND_D16_LAB = "sandD16Lab";
	public static final String SE_SAND_D50_LAB = "sandD50Lab";
	public static final String SE_SAND_D84_LAB = "sandD84Lab";
	public static final String SE_SAND_PCT_LT_074_LAB = "sandPctLt074Lab";
	public static final String SE_SAND_PCT_LT_088_LAB = "sandPctLt088Lab";
	public static final String SE_SAND_PCT_LT_105_LAB = "sandPctLt105Lab";
	public static final String SE_SAND_PCT_LT_125_LAB = "sandPctLt125Lab";
	public static final String SE_SAND_PCT_LT_149_LAB = "sandPctLt149Lab";
	public static final String SE_SAND_PCT_LT_177_LAB = "sandPctLt177Lab";
	public static final String SE_SAND_PCT_LT_210_LAB = "sandPctLt210Lab";
	public static final String SE_SAND_PCT_LT_250_LAB = "sandPctLt250Lab";
	public static final String SE_SAND_PCT_LT_297_LAB = "sandPctLt297Lab";
	public static final String SE_SAND_PCT_LT_354_LAB = "sandPctLt354Lab";
	public static final String SE_SAND_PCT_LT_420_LAB = "sandPctLt420Lab";
	public static final String SE_SAND_PCT_LT_500_LAB = "sandPctLt500Lab";
	public static final String SE_SAND_PCT_LT_595_LAB = "sandPctLt595Lab";
	public static final String SE_SAND_PCT_LT_707_LAB = "sandPctLt707Lab";
	public static final String SE_SAND_PCT_LT_841_LAB = "sandPctLt841Lab";
	public static final String SE_SAND_PCT_LT_1000_LAB = "sandPctLt1000Lab";
	public static final String SE_SILT_CLAY_CONC_XS = "siltClayConcXs";
	public static final String SE_SAND_CONC_XS = "sandConcXs";
	public static final String SE_SAND_D16_XS = "sandD16Xs";
	public static final String SE_SAND_D50_XS = "sandD50Xs";
	public static final String SE_SAND_D84_XS = "sandD84Xs";
	public static final String SE_SAND_PCT_LT_074_XS = "sandPctLt074Xs";
	public static final String SE_SAND_PCT_LT_088_XS = "sandPctLt088Xs";
	public static final String SE_SAND_PCT_LT_105_XS = "sandPctLt105Xs";
	public static final String SE_SAND_PCT_LT_125_XS = "sandPctLt125Xs";
	public static final String SE_SAND_PCT_LT_149_XS = "sandPctLt149Xs";
	public static final String SE_SAND_PCT_LT_177_XS = "sandPctLt177Xs";
	public static final String SE_SAND_PCT_LT_210_XS = "sandPctLt210Xs";
	public static final String SE_SAND_PCT_LT_250_XS = "sandPctLt250Xs";
	public static final String SE_SAND_PCT_LT_297_XS = "sandPctLt297Xs";
	public static final String SE_SAND_PCT_LT_354_XS = "sandPctLt354Xs";
	public static final String SE_SAND_PCT_LT_420_XS = "sandPctLt420Xs";
	public static final String SE_SAND_PCT_LT_500_XS = "sandPctLt500Xs";
	public static final String SE_SAND_PCT_LT_595_XS = "sandPctLt595Xs";
	public static final String SE_SAND_PCT_LT_707_XS = "sandPctLt707Xs";
	public static final String SE_SAND_PCT_LT_841_XS = "sandPctLt841Xs";
	public static final String SE_SAND_PCT_LT_1000_XS = "sandPctLt1000Xs";
	public static final String SE_SILT_CLAY_FIELD_95ER = "siltClayField95Er";
	public static final String SE_SAND_FIELD_95ER = "sandField95Er";
	public static final String SE_SAND_D50_FIELD_95ER = "sandD50Field95Er";
	public static final String SE_SILT_CLAY_LAB_BIAS_COR = "siltClayLabBiasCor";
	public static final String SE_SILT_CLAY_LAB_95ER = "siltClayLab95Er";
	public static final String SE_SAND_LAB_95ER = "sandLab95Er";
	public static final String SE_SAND_D50_LAB_95ER = "sandD50Lab95Er";
	public static final String SE_SILT_CLAY_TOT_95ER = "siltClayTot95Er";
	public static final String SE_SAND_TOT_95ER = "sandTot95Er";
	public static final String SE_SAND_D50_TOT_95ER = "sandD50Tot95Er";
	
	protected static class SlashOkColumnMapping extends ColumnMapping {
		
		public SlashOkColumnMapping(String columnName, String modsElementString) {
			super(columnName, modsElementString);
		}

		@Override
		public String getXmlElement(int depthParam) {
			return super.getXmlElementString();
		}

		@Override
		public int getDepth() {
			return 1;
		}
	}
}
