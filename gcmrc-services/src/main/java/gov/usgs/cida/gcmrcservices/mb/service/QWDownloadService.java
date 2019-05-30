package gov.usgs.cida.gcmrcservices.mb.service;

import gov.usgs.cida.gcmrcservices.TSVUtil;
import gov.usgs.cida.gcmrcservices.mb.model.QWDownload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kmschoep
 */
public class QWDownloadService {
	private static final Logger log = LoggerFactory.getLogger(QWDownloadService.class);
	public static enum COLUMNS {SAMPLE_ID, STATION_NAME, STATION_NUM, START_DT, MEAN_DT, END_DT, USGS_DATA_LEAD, SAMPLE_METHOD, SAMPLE_LOCATION, SAMPLER_NAME, NOZZLE, NUM_VERTICALS, TRANSITS_EACH_VERTICAL, CABLEWAY_STATION_LOC, WATER_DEPTH, ELEVATION_ABOVE_BED, SAMPLING_DURATION, PUMP_SAMPLER, PUMP_CAROUSEL_NUM, DATASET_COMPLETE, CROSS_SECT_CALIB_REQ, USE_FOR_LOAD_CALC, NOTES, AIR_TEMP, WATER_TEMP, SPEC_COND, TDS, SILT_CLAY_COLOR, CONC_LABORATORY, GRAIN_SIZE_LABORATORY, LAB_NOTES, LAB_METHOD, SILT_CLAY_CONC_XS, SAND_CONC_XS, SAND_D16_XS, SAND_D50_XS, SAND_D84_XS, SAND_PCT_LT_074_XS, SAND_PCT_LT_088_XS, SAND_PCT_LT_105_XS, SAND_PCT_LT_125_XS, SAND_PCT_LT_149_XS, SAND_PCT_LT_177_XS, SAND_PCT_LT_210_XS, SAND_PCT_LT_250_XS, SAND_PCT_LT_297_XS, SAND_PCT_LT_354_XS, SAND_PCT_LT_420_XS, SAND_PCT_LT_500_XS, SAND_PCT_LT_595_XS, SAND_PCT_LT_707_XS, SAND_PCT_LT_841_XS, SAND_PCT_LT_1000_XS, SAMPLE_MASS, MASS_LE_63, MASS_GT_63, CONC_LE_63, CONC_GT_63, SILT_CLAY_CONC_LAB, SAND_CONC_LAB, SAND_D16_LAB, SAND_D50_LAB, SAND_D84_LAB, SAND_PCT_LT_074_LAB, SAND_PCT_LT_088_LAB, SAND_PCT_LT_105_LAB, SAND_PCT_LT_125_LAB, SAND_PCT_LT_149_LAB, SAND_PCT_LT_177_LAB, SAND_PCT_LT_210_LAB, SAND_PCT_LT_250_LAB, SAND_PCT_LT_297_LAB, SAND_PCT_LT_354_LAB, SAND_PCT_LT_420_LAB, SAND_PCT_LT_500_LAB, SAND_PCT_LT_595_LAB, SAND_PCT_LT_707_LAB, SAND_PCT_LT_841_LAB, SAND_PCT_LT_1000_LAB, SILT_CLAY_FIELD_95ER, SAND_FIELD_95ER, SAND_D50_FIELD_95ER, SILT_CLAY_LAB_BIAS_COR, SILT_CLAY_LAB_95ER, SAND_LAB_95ER, SAND_D50_LAB_95ER, SILT_CLAY_TOT_95ER, SAND_TOT_95ER, SAND_D50_TOT_95ER};
	
	private static HashMap getQWDownloadData(List<QWDownload> data, List<COLUMNS> outputColumns){
		List<List<Object> > columns = new ArrayList<>();
		List<String> headers = new ArrayList<>();
		HashMap result = new HashMap<>();
		String[] COLUMN_HEADERS = {"sampleId", "Station name", "USGS Station #", "start time (" + data.get(0).getTime_zone() + ")", "mean time (" + data.get(0).getTime_zone() + ")", "end time (" + data.get(0).getTime_zone() + ")", "USGS data lead", "Sampling method", "Location", "Sampler", "Nozzle", "Verticals", "Transits at each vertical", "Cableway station location (ft)", "Water depth (m)", "Sample elevation above bed (m)", "Sampling duration (s)", "Pump sampler A or B", "Pump carousel number", "Dataset complete through this sample", "Cross-section calibration applied", "Use in load calculations", "Notes", "Air temp (deg C)", "Water temp (deg C)", "Specific conductance (microsiemens/cm at 25 deg. C)", "TDS (mg/L)", "Color of silt&clay", "Concentration analysis laboratory", "Grain-size analysis laboratory", "Lab notes", "Method used for determining sand/fines break and sand grain-size distribution", "Cross-section silt&clay concentration (mg/L)", "Cross-section sand concentration (mg/L)", "Cross-section sand D16(mm)", "Cross-section sand D50(mm)", "Cross-section sand D84(mm)", "Cross-section sand %< 0.074 mm", "Cross-section sand %< 0.088 mm", "Cross-section sand %< 0.105 mm", "Cross-section sand %< 0.125 mm", "Cross-section sand %< 0.149 mm", "Cross-section sand %< 0.177 mm", "Cross-section sand %< 0.210 mm", "Cross-section sand %< 0.250 mm", "Cross-section sand %< 0.297 mm", "Cross-section sand %< 0.354 mm", "Cross-section sand %< 0.420 mm", "Cross-section sand %< 0.500 mm", "Cross-section sand %< 0.595 mm", "Cross-section sand %< 0.707 mm", "Cross-section sand %< 0.841 mm", "Cross-section sand %< 1.0 mm", "Sample Mass (g)", "Mass passing through 63 micron sieve (g)", "Mass retained on 63 micron sieve (g)", "Concentration <63 micron sieve (mg/L)", "Concentration >63 micron sieve (mg/L)", "Laboratory silt&clay concentration (mg/L)", "Laboratory sand concentration (mg/L)", "Raw lab sand D16(mm)", "Raw lab sand D50(mm)", "Raw lab sand D84(mm)", "Raw lab sand %< 0.074 mm", "Raw lab sand %< 0.088 mm", "Raw lab sand %< 0.105 mm", "Raw lab sand %< 0.125 mm", "Raw lab sand %< 0.149 mm", "Raw lab sand %< 0.177 mm", "Raw lab sand %< 0.210 mm", "Raw lab sand %< 0.250 mm", "Raw lab sand %< 0.297 mm", "Raw lab sand %< 0.354 mm", "Raw lab sand %< 0.420 mm", "Raw lab sand %< 0.500 mm", "Raw lab sand %< 0.595 mm", "Raw lab sand %< 0.707 mm", "Raw lab sand %< 0.841 mm", "Raw lab sand %< 1.0 mm", "95%-confidence-level field error in silt&clay concentration (mg/L)", "95%-confidence-level field error in sand concentration (mg/L)", "95%-confidence-level field error in sand D50 (mm)", "Correction for negative laboratory bias in silt&clay conc. (mg/L)", "95%-confidence-level lab error in bias-corr. silt&clay concentration (mg/L)", "95%-confidence-level lab error in sand concentration (mg/L)", "95%-confidence-level lab error in sand D50 (mm)", "95%-confidence-level total error in bias-corr. silt&clay concentration (mg/L)", "95%-confidence-level total error in sand concentration (mg/L)", "95%-confidence-level total error in sand D50(mm)"};
		
		//Build headers and extract relevant data
		for(int i = 0; i < outputColumns.size(); i++){

			headers.add(COLUMN_HEADERS[outputColumns.get(i).ordinal()]);
			
			switch(outputColumns.get(i)){
			case SAMPLE_ID:
				columns.add(data.stream()
					.map(QWDownload::getSample_id)
					.collect(Collectors.toList()));
				break;
			
			case STATION_NAME:
				columns.add(data.stream()
					.map(QWDownload::getStation_name)
					.collect(Collectors.toList()));
				break;

			case STATION_NUM:
				columns.add(data.stream()
					.map(QWDownload::getStation_num)
					.collect(Collectors.toList()));
				break;

			case START_DT:
				columns.add(data.stream()
					.map(QWDownload::getStart_dt)
					.collect(Collectors.toList()));
				break;

			case MEAN_DT:
				columns.add(data.stream()
					.map(QWDownload::getMean_dt)
					.collect(Collectors.toList()));
				break;

			case END_DT:
				columns.add(data.stream()
					.map(QWDownload::getEnd_dt)
					.collect(Collectors.toList()));
				break;

			case USGS_DATA_LEAD:
				columns.add(data.stream()
					.map(QWDownload::getUsgs_data_lead)
					.collect(Collectors.toList()));
				break;

			case SAMPLE_METHOD:
				columns.add(data.stream()
					.map(QWDownload::getSample_method)
					.collect(Collectors.toList()));
				break;

			case SAMPLE_LOCATION:
				columns.add(data.stream()
					.map(QWDownload::getSample_location)
					.collect(Collectors.toList()));
				break;

			case SAMPLER_NAME:
				columns.add(data.stream()
					.map(QWDownload::getSampler_name)
					.collect(Collectors.toList()));
				break;

			case NOZZLE:
				columns.add(data.stream()
					.map(QWDownload::getNozzle)
					.collect(Collectors.toList()));
				break;

			case NUM_VERTICALS:
				columns.add(data.stream()
					.map(QWDownload::getNum_verticals)
					.collect(Collectors.toList()));
				break;

			case TRANSITS_EACH_VERTICAL:
				columns.add(data.stream()
					.map(QWDownload::getTransits_each_vertical)
					.collect(Collectors.toList()));
				break;

			case CABLEWAY_STATION_LOC:
				columns.add(data.stream()
					.map(QWDownload::getCableway_station_loc)
					.collect(Collectors.toList()));
				break;

			case WATER_DEPTH:
				columns.add(data.stream()
					.map(QWDownload::getWater_depth)
					.collect(Collectors.toList()));
				break;

			case ELEVATION_ABOVE_BED:
				columns.add(data.stream()
					.map(QWDownload::getElevation_above_bed)
					.collect(Collectors.toList()));
				break;

			case SAMPLING_DURATION:
				columns.add(data.stream()
					.map(QWDownload::getSampling_duration)
					.collect(Collectors.toList()));
				break;

			case PUMP_SAMPLER:
				columns.add(data.stream()
					.map(QWDownload::getPump_sampler)
					.collect(Collectors.toList()));
				break;

			case PUMP_CAROUSEL_NUM:
				columns.add(data.stream()
					.map(QWDownload::getPump_carousel_num)
					.collect(Collectors.toList()));
				break;

			case DATASET_COMPLETE:
				columns.add(data.stream()
					.map(QWDownload::getDataset_complete)
					.collect(Collectors.toList()));
				break;

			case CROSS_SECT_CALIB_REQ:
				columns.add(data.stream()
					.map(QWDownload::getCross_sect_calib_req)
					.collect(Collectors.toList()));
				break;

			case USE_FOR_LOAD_CALC:
				columns.add(data.stream()
					.map(QWDownload::getUse_for_load_calc)
					.collect(Collectors.toList()));
				break;

			case NOTES:
				columns.add(data.stream()
					.map(QWDownload::getNotes)
					.collect(Collectors.toList()));
				break;

			case AIR_TEMP:
				columns.add(data.stream()
					.map(QWDownload::getAir_temp)
					.collect(Collectors.toList()));
				break;

			case WATER_TEMP:
				columns.add(data.stream()
					.map(QWDownload::getWater_temp)
					.collect(Collectors.toList()));
				break;

			case SPEC_COND:
				columns.add(data.stream()
					.map(QWDownload::getSpec_cond)
					.collect(Collectors.toList()));
				break;

			case TDS:
				columns.add(data.stream()
					.map(QWDownload::getTds)
					.collect(Collectors.toList()));
				break;

			case SILT_CLAY_COLOR:
				columns.add(data.stream()
					.map(QWDownload::getSilt_clay_color)
					.collect(Collectors.toList()));
				break;

			case CONC_LABORATORY:
				columns.add(data.stream()
					.map(QWDownload::getConc_laboratory)
					.collect(Collectors.toList()));
				break;

			case GRAIN_SIZE_LABORATORY:
				columns.add(data.stream()
					.map(QWDownload::getGrain_size_laboratory)
					.collect(Collectors.toList()));
				break;

			case LAB_NOTES:
				columns.add(data.stream()
					.map(QWDownload::getLab_notes)
					.collect(Collectors.toList()));
				break;

			case LAB_METHOD:
				columns.add(data.stream()
					.map(QWDownload::getLab_method)
					.collect(Collectors.toList()));
				break;

			case SILT_CLAY_CONC_XS:
				columns.add(data.stream()
					.map(QWDownload::getSilt_clay_conc_xs)
					.collect(Collectors.toList()));
				break;

			case SAND_CONC_XS:
				columns.add(data.stream()
					.map(QWDownload::getSand_conc_xs)
					.collect(Collectors.toList()));
				break;

			case SAND_D16_XS:
				columns.add(data.stream()
					.map(QWDownload::getSand_d16_xs)
					.collect(Collectors.toList()));
				break;

			case SAND_D50_XS:
				columns.add(data.stream()
					.map(QWDownload::getSand_d50_xs)
					.collect(Collectors.toList()));
				break;

			case SAND_D84_XS:
				columns.add(data.stream()
					.map(QWDownload::getSand_d84_xs)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_074_XS:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_074_xs)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_088_XS:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_088_xs)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_105_XS:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_105_xs)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_125_XS:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_125_xs)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_149_XS:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_149_xs)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_177_XS:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_177_xs)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_210_XS:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_210_xs)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_250_XS:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_250_xs)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_297_XS:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_297_xs)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_354_XS:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_354_xs)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_420_XS:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_420_xs)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_500_XS:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_500_xs)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_595_XS:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_595_xs)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_707_XS:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_707_xs)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_841_XS:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_841_xs)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_1000_XS:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_1000_xs)
					.collect(Collectors.toList()));
				break;

			case SAMPLE_MASS:
				columns.add(data.stream()
					.map(QWDownload::getSample_mass)
					.collect(Collectors.toList()));
				break;

			case MASS_LE_63:
				columns.add(data.stream()
					.map(QWDownload::getMass_le_63)
					.collect(Collectors.toList()));
				break;

			case MASS_GT_63:
				columns.add(data.stream()
					.map(QWDownload::getMass_gt_63)
					.collect(Collectors.toList()));
				break;

			case CONC_LE_63:
				columns.add(data.stream()
					.map(QWDownload::getConc_le_63)
					.collect(Collectors.toList()));
				break;

			case CONC_GT_63:
				columns.add(data.stream()
					.map(QWDownload::getConc_gt_63)
					.collect(Collectors.toList()));
				break;

			case SILT_CLAY_CONC_LAB:
				columns.add(data.stream()
					.map(QWDownload::getSilt_clay_conc_lab)
					.collect(Collectors.toList()));
				break;

			case SAND_CONC_LAB:
				columns.add(data.stream()
					.map(QWDownload::getSand_conc_lab)
					.collect(Collectors.toList()));
				break;

			case SAND_D16_LAB:
				columns.add(data.stream()
					.map(QWDownload::getSand_d16_lab)
					.collect(Collectors.toList()));
				break;

			case SAND_D50_LAB:
				columns.add(data.stream()
					.map(QWDownload::getSand_d50_lab)
					.collect(Collectors.toList()));
				break;

			case SAND_D84_LAB:
				columns.add(data.stream()
					.map(QWDownload::getSand_d84_lab)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_074_LAB:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_074_lab)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_088_LAB:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_088_lab)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_105_LAB:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_105_lab)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_125_LAB:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_125_lab)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_149_LAB:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_149_lab)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_177_LAB:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_177_lab)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_210_LAB:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_210_lab)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_250_LAB:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_250_lab)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_297_LAB:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_297_lab)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_354_LAB:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_354_lab)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_420_LAB:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_420_lab)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_500_LAB:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_500_lab)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_595_LAB:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_595_lab)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_707_LAB:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_707_lab)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_841_LAB:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_841_lab)
					.collect(Collectors.toList()));
				break;

			case SAND_PCT_LT_1000_LAB:
				columns.add(data.stream()
					.map(QWDownload::getSand_pct_lt_1000_lab)
					.collect(Collectors.toList()));
				break;

			case SILT_CLAY_FIELD_95ER:
				columns.add(data.stream()
					.map(QWDownload::getSilt_clay_field_95er)
					.collect(Collectors.toList()));
				break;

			case SAND_FIELD_95ER:
				columns.add(data.stream()
					.map(QWDownload::getSand_field_95er)
					.collect(Collectors.toList()));
				break;

			case SAND_D50_FIELD_95ER:
				columns.add(data.stream()
					.map(QWDownload::getSand_d50_field_95er)
					.collect(Collectors.toList()));
				break;

			case SILT_CLAY_LAB_BIAS_COR:
				columns.add(data.stream()
					.map(QWDownload::getSilt_clay_lab_bias_cor)
					.collect(Collectors.toList()));
				break;

			case SILT_CLAY_LAB_95ER:
				columns.add(data.stream()
					.map(QWDownload::getSilt_clay_lab_95er)
					.collect(Collectors.toList()));
				break;

			case SAND_LAB_95ER:
				columns.add(data.stream()
					.map(QWDownload::getSand_lab_95er)
					.collect(Collectors.toList()));
				break;

			case SAND_D50_LAB_95ER:
				columns.add(data.stream()
					.map(QWDownload::getSand_d50_lab_95er)
					.collect(Collectors.toList()));
				break;

			case SILT_CLAY_TOT_95ER:
				columns.add(data.stream()
					.map(QWDownload::getSilt_clay_tot_95er)
					.collect(Collectors.toList()));
				break;

			case SAND_TOT_95ER:
				columns.add(data.stream()
					.map(QWDownload::getSand_tot_95er)
					.collect(Collectors.toList()));
				break;

			case SAND_D50_TOT_95ER:
				columns.add(data.stream()
					.map(QWDownload::getSand_d50_tot_95er)
					.collect(Collectors.toList()));
				break; 
			}
		}
		
		//Build output
		result.put("headers", headers);
		result.put("columns", columns);
						
		return result;
	}
	
	public static String getTSVForQWDownload(List<QWDownload> data, List<COLUMNS> outputColumns) {		
		List<List<Object> > columns = new ArrayList<>();
		List<String> headers = new ArrayList<>();
		String output;
		
		//Get all necessary data
		HashMap<String, List> result = getQWDownloadData(data, outputColumns);
		columns = result.get("columns");			
		headers = result.get("headers");
						
		//Verify Data
		if(columns.size() > 0 && headers.size() > 0){
			output = TSVUtil.createTSV(headers, columns, data.size(), false, true);
		} else {
			output = "NO QW DATA RETURNED FOR SPECIFIED PARAMETERS";
		}
				
		//Create TSV File
		return output;
	}
	
}
