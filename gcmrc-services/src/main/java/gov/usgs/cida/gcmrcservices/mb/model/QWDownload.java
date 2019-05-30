package gov.usgs.cida.gcmrcservices.mb.model;

/**
 *
 * @author kmschoep
 */
public class QWDownload {

	private String sample_id;   
	private String station_name;
	private String station_num; 
	private String start_dt;
	private String mean_dt; 
	private String end_dt;  
	private String time_zone;   
	private String usgs_data_lead;  
	private String sample_method;   
	private String sample_location; 
	private String sampler_name;
	private String nozzle;  
	private String num_verticals;   
	private String transits_each_vertical;  
	private String cableway_station_loc;
	private String water_depth; 
	private String elevation_above_bed; 
	private String sampling_duration;   
	private String pump_sampler;
	private String pump_carousel_num;   
	private String dataset_complete;
	private String cross_sect_calib_req;
	private String use_for_load_calc;   
	private String notes;   
	private String air_temp;
	private String water_temp;  
	private String spec_cond;   
	private String tds; 
	private String silt_clay_color; 
	private String conc_laboratory; 
	private String grain_size_laboratory;   
	private String lab_notes;   
	private String lab_method;  
	private String silt_clay_conc_xs;   
	private String sand_conc_xs;
	private String sand_d16_xs; 
	private String sand_d50_xs; 
	private String sand_d84_xs; 
	private String sand_pct_lt_074_xs;  
	private String sand_pct_lt_088_xs;  
	private String sand_pct_lt_105_xs;  
	private String sand_pct_lt_125_xs;  
	private String sand_pct_lt_149_xs;  
	private String sand_pct_lt_177_xs;  
	private String sand_pct_lt_210_xs;  
	private String sand_pct_lt_250_xs;  
	private String sand_pct_lt_297_xs;  
	private String sand_pct_lt_354_xs;  
	private String sand_pct_lt_420_xs;  
	private String sand_pct_lt_500_xs;  
	private String sand_pct_lt_595_xs;  
	private String sand_pct_lt_707_xs;  
	private String sand_pct_lt_841_xs;  
	private String sand_pct_lt_1000_xs; 
	private String sample_mass; 
	private String mass_le_63;  
	private String mass_gt_63;  
	private String conc_le_63;  
	private String conc_gt_63;  
	private String silt_clay_conc_lab;  
	private String sand_conc_lab;   
	private String sand_d16_lab;
	private String sand_d50_lab;
	private String sand_d84_lab;
	private String sand_pct_lt_074_lab; 
	private String sand_pct_lt_088_lab; 
	private String sand_pct_lt_105_lab; 
	private String sand_pct_lt_125_lab; 
	private String sand_pct_lt_149_lab; 
	private String sand_pct_lt_177_lab; 
	private String sand_pct_lt_210_lab; 
	private String sand_pct_lt_250_lab; 
	private String sand_pct_lt_297_lab; 
	private String sand_pct_lt_354_lab; 
	private String sand_pct_lt_420_lab; 
	private String sand_pct_lt_500_lab; 
	private String sand_pct_lt_595_lab; 
	private String sand_pct_lt_707_lab; 
	private String sand_pct_lt_841_lab; 
	private String sand_pct_lt_1000_lab;
	private String silt_clay_field_95er;
	private String sand_field_95er; 
	private String sand_d50_field_95er; 
	private String silt_clay_lab_bias_cor;  
	private String silt_clay_lab_95er;  
	private String sand_lab_95er;   
	private String sand_d50_lab_95er;   
	private String silt_clay_tot_95er;  
	private String sand_tot_95er;   
	private String sand_d50_tot_95er;

	public QWDownload() {
	}

	public QWDownload(String sample_id, String station_name, String station_num, String start_dt, String mean_dt, String end_dt, String time_zone, String usgs_data_lead, String sample_method, String sample_location, String sampler_name, String nozzle, String num_verticals, String transits_each_vertical, String cableway_station_loc, String water_depth, String elevation_above_bed, String sampling_duration, String pump_sampler, String pump_carousel_num, String dataset_complete, String cross_sect_calib_req, String use_for_load_calc, String notes, String air_temp, String water_temp, String spec_cond, String tds, String silt_clay_color, String conc_laboratory, String grain_size_laboratory, String lab_notes, String lab_method, String silt_clay_conc_xs, String sand_conc_xs, String sand_d16_xs, String sand_d50_xs, String sand_d84_xs, String sand_pct_lt_074_xs, String sand_pct_lt_088_xs, String sand_pct_lt_105_xs, String sand_pct_lt_125_xs, String sand_pct_lt_149_xs, String sand_pct_lt_177_xs, String sand_pct_lt_210_xs, String sand_pct_lt_250_xs, String sand_pct_lt_297_xs, String sand_pct_lt_354_xs, String sand_pct_lt_420_xs, String sand_pct_lt_500_xs, String sand_pct_lt_595_xs, String sand_pct_lt_707_xs, String sand_pct_lt_841_xs, String sand_pct_lt_1000_xs, String sample_mass, String mass_le_63, String mass_gt_63, String conc_le_63, String conc_gt_63, String silt_clay_conc_lab, String sand_conc_lab, String sand_d16_lab, String sand_d50_lab, String sand_d84_lab, String sand_pct_lt_074_lab, String sand_pct_lt_088_lab, String sand_pct_lt_105_lab, String sand_pct_lt_125_lab, String sand_pct_lt_149_lab, String sand_pct_lt_177_lab, String sand_pct_lt_210_lab, String sand_pct_lt_250_lab, String sand_pct_lt_297_lab, String sand_pct_lt_354_lab, String sand_pct_lt_420_lab, String sand_pct_lt_500_lab, String sand_pct_lt_595_lab, String sand_pct_lt_707_lab, String sand_pct_lt_841_lab, String sand_pct_lt_1000_lab, String silt_clay_field_95er, String sand_field_95er, String sand_d50_field_95er, String silt_clay_lab_bias_cor, String silt_clay_lab_95er, String sand_lab_95er, String sand_d50_lab_95er, String silt_clay_tot_95er, String sand_tot_95er, String sand_d50_tot_95er) {
		this.sample_id = sample_id; 
		this.station_name = station_name;
		this.station_num = station_num;  
		this.start_dt = start_dt;   
		this.mean_dt = mean_dt; 
		this.end_dt = end_dt;   
		this.time_zone = time_zone; 
		this.usgs_data_lead = usgs_data_lead; 
		this.sample_method = sample_method;   
		this.sample_location = sample_location;   
		this.sampler_name = sampler_name;
		this.nozzle = nozzle;   
		this.num_verticals = num_verticals;   
		this.transits_each_vertical = transits_each_vertical;   
		this.cableway_station_loc = cableway_station_loc;   
		this.water_depth = water_depth;  
		this.elevation_above_bed = elevation_above_bed;
		this.sampling_duration = sampling_duration;    
		this.pump_sampler = pump_sampler;
		this.pump_carousel_num = pump_carousel_num;    
		this.dataset_complete = dataset_complete; 
		this.cross_sect_calib_req = cross_sect_calib_req;   
		this.use_for_load_calc = use_for_load_calc;    
		this.notes = notes;
		this.air_temp = air_temp;   
		this.water_temp = water_temp;    
		this.spec_cond = spec_cond; 
		this.tds = tds;    
		this.silt_clay_color = silt_clay_color;   
		this.conc_laboratory = conc_laboratory;   
		this.grain_size_laboratory = grain_size_laboratory; 
		this.lab_notes = lab_notes; 
		this.lab_method = lab_method;    
		this.silt_clay_conc_xs = silt_clay_conc_xs;    
		this.sand_conc_xs = sand_conc_xs;
		this.sand_d16_xs = sand_d16_xs;  
		this.sand_d50_xs = sand_d50_xs;  
		this.sand_d84_xs = sand_d84_xs;  
		this.sand_pct_lt_074_xs = sand_pct_lt_074_xs;  
		this.sand_pct_lt_088_xs = sand_pct_lt_088_xs;  
		this.sand_pct_lt_105_xs = sand_pct_lt_105_xs;  
		this.sand_pct_lt_125_xs = sand_pct_lt_125_xs;  
		this.sand_pct_lt_149_xs = sand_pct_lt_149_xs;  
		this.sand_pct_lt_177_xs = sand_pct_lt_177_xs;  
		this.sand_pct_lt_210_xs = sand_pct_lt_210_xs;  
		this.sand_pct_lt_250_xs = sand_pct_lt_250_xs;  
		this.sand_pct_lt_297_xs = sand_pct_lt_297_xs;  
		this.sand_pct_lt_354_xs = sand_pct_lt_354_xs;  
		this.sand_pct_lt_420_xs = sand_pct_lt_420_xs;  
		this.sand_pct_lt_500_xs = sand_pct_lt_500_xs;  
		this.sand_pct_lt_595_xs = sand_pct_lt_595_xs;  
		this.sand_pct_lt_707_xs = sand_pct_lt_707_xs;  
		this.sand_pct_lt_841_xs = sand_pct_lt_841_xs;  
		this.sand_pct_lt_1000_xs = sand_pct_lt_1000_xs;
		this.sample_mass = sample_mass;  
		this.mass_le_63 = mass_le_63;    
		this.mass_gt_63 = mass_gt_63;    
		this.conc_le_63 = conc_le_63;    
		this.conc_gt_63 = conc_gt_63;    
		this.silt_clay_conc_lab = silt_clay_conc_lab;  
		this.sand_conc_lab = sand_conc_lab;   
		this.sand_d16_lab = sand_d16_lab;
		this.sand_d50_lab = sand_d50_lab;
		this.sand_d84_lab = sand_d84_lab;
		this.sand_pct_lt_074_lab = sand_pct_lt_074_lab;
		this.sand_pct_lt_088_lab = sand_pct_lt_088_lab;
		this.sand_pct_lt_105_lab = sand_pct_lt_105_lab;
		this.sand_pct_lt_125_lab = sand_pct_lt_125_lab;
		this.sand_pct_lt_149_lab = sand_pct_lt_149_lab;
		this.sand_pct_lt_177_lab = sand_pct_lt_177_lab;
		this.sand_pct_lt_210_lab = sand_pct_lt_210_lab;
		this.sand_pct_lt_250_lab = sand_pct_lt_250_lab;
		this.sand_pct_lt_297_lab = sand_pct_lt_297_lab;
		this.sand_pct_lt_354_lab = sand_pct_lt_354_lab;
		this.sand_pct_lt_420_lab = sand_pct_lt_420_lab;
		this.sand_pct_lt_500_lab = sand_pct_lt_500_lab;
		this.sand_pct_lt_595_lab = sand_pct_lt_595_lab;
		this.sand_pct_lt_707_lab = sand_pct_lt_707_lab;
		this.sand_pct_lt_841_lab = sand_pct_lt_841_lab;
		this.sand_pct_lt_1000_lab = sand_pct_lt_1000_lab;   
		this.silt_clay_field_95er = silt_clay_field_95er;   
		this.sand_field_95er = sand_field_95er;   
		this.sand_d50_field_95er = sand_d50_field_95er;
		this.silt_clay_lab_bias_cor = silt_clay_lab_bias_cor;   
		this.silt_clay_lab_95er = silt_clay_lab_95er;  
		this.sand_lab_95er = sand_lab_95er;   
		this.sand_d50_lab_95er = sand_d50_lab_95er;    
		this.silt_clay_tot_95er = silt_clay_tot_95er;  
		this.sand_tot_95er = sand_tot_95er;   
		this.sand_d50_tot_95er = sand_d50_tot_95er;
	}

	public String getSample_id() {
		return sample_id;
	}

	public void setSample_id(String sample_id) {
		this.sample_id = sample_id;
	}

	public String getStation_name() {
		return station_name;
	}

	public void setStation_name(String station_name) {
		this.station_name = station_name;
	}

	public String getStation_num() {
		return station_num;
	}

	public void setStation_num(String station_num) {
		this.station_num = station_num;
	}

	public String getStart_dt() {
		return start_dt;
	}

	public void setStart_dt(String start_dt) {
		this.start_dt = start_dt;
	}

	public String getMean_dt() {
		return mean_dt;
	}

	public void setMean_dt(String mean_dt) {
		this.mean_dt = mean_dt;
	}

	public String getEnd_dt() {
		return end_dt;
	}

	public void setEnd_dt(String end_dt) {
		this.end_dt = end_dt;
	}

	public String getTime_zone() {
		return time_zone;
	}

	public void setTime_zone(String time_zone) {
		this.time_zone = time_zone;
	}

	public String getUsgs_data_lead() {
		return usgs_data_lead;
	}

	public void setUsgs_data_lead(String usgs_data_lead) {
		this.usgs_data_lead = usgs_data_lead;
	}

	public String getSample_method() {
		return sample_method;
	}

	public void setSample_method(String sample_method) {
		this.sample_method = sample_method;
	}

	public String getSample_location() {
		return sample_location;
	}

	public void setSample_location(String sample_location) {
		this.sample_location = sample_location;
	}

	public String getSampler_name() {
		return sampler_name;
	}

	public void setSampler_name(String sampler_name) {
		this.sampler_name = sampler_name;
	}

	public String getNozzle() {
		return nozzle;
	}

	public void setNozzle(String nozzle) {
		this.nozzle = nozzle;
	}

	public String getNum_verticals() {
		return num_verticals;
	}

	public void setNum_verticals(String num_verticals) {
		this.num_verticals = num_verticals;
	}

	public String getTransits_each_vertical() {
		return transits_each_vertical;
	}

	public void setTransits_each_vertical(String transits_each_vertical) {
		this.transits_each_vertical = transits_each_vertical;
	}

	public String getCableway_station_loc() {
		return cableway_station_loc;
	}

	public void setCableway_station_loc(String cableway_station_loc) {
		this.cableway_station_loc = cableway_station_loc;
	}

	public String getWater_depth() {
		return water_depth;
	}

	public void setWater_depth(String water_depth) {
		this.water_depth = water_depth;
	}

	public String getElevation_above_bed() {
		return elevation_above_bed;
	}

	public void setElevation_above_bed(String elevation_above_bed) {
		this.elevation_above_bed = elevation_above_bed;
	}

	public String getSampling_duration() {
		return sampling_duration;
	}

	public void setSampling_duration(String sampling_duration) {
		this.sampling_duration = sampling_duration;
	}

	public String getPump_sampler() {
		return pump_sampler;
	}

	public void setPump_sampler(String pump_sampler) {
		this.pump_sampler = pump_sampler;
	}

	public String getPump_carousel_num() {
		return pump_carousel_num;
	}

	public void setPump_carousel_num(String pump_carousel_num) {
		this.pump_carousel_num = pump_carousel_num;
	}

	public String getDataset_complete() {
		return dataset_complete;
	}

	public void setDataset_complete(String dataset_complete) {
		this.dataset_complete = dataset_complete;
	}

	public String getCross_sect_calib_req() {
		return cross_sect_calib_req;
	}

	public void setCross_sect_calib_req(String cross_sect_calib_req) {
		this.cross_sect_calib_req = cross_sect_calib_req;
	}

	public String getUse_for_load_calc() {
		return use_for_load_calc;
	}

	public void setUse_for_load_calc(String use_for_load_calc) {
		this.use_for_load_calc = use_for_load_calc;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getAir_temp() {
		return air_temp;
	}

	public void setAir_temp(String air_temp) {
		this.air_temp = air_temp;
	}

	public String getWater_temp() {
		return water_temp;
	}

	public void setWater_temp(String water_temp) {
		this.water_temp = water_temp;
	}

	public String getSpec_cond() {
		return spec_cond;
	}

	public void setSpec_cond(String spec_cond) {
		this.spec_cond = spec_cond;
	}

	public String getTds() {
		return tds;
	}

	public void setTds(String tds) {
		this.tds = tds;
	}

	public String getSilt_clay_color() {
		return silt_clay_color;
	}

	public void setSilt_clay_color(String silt_clay_color) {
		this.silt_clay_color = silt_clay_color;
	}

	public String getConc_laboratory() {
		return conc_laboratory;
	}

	public void setConc_laboratory(String conc_laboratory) {
		this.conc_laboratory = conc_laboratory;
	}

	public String getGrain_size_laboratory() {
		return grain_size_laboratory;
	}

	public void setGrain_size_laboratory(String grain_size_laboratory) {
		this.grain_size_laboratory = grain_size_laboratory;
	}

	public String getLab_notes() {
		return lab_notes;
	}

	public void setLab_notes(String lab_notes) {
		this.lab_notes = lab_notes;
	}

	public String getLab_method() {
		return lab_method;
	}

	public void setLab_method(String lab_method) {
		this.lab_method = lab_method;
	}

	public String getSilt_clay_conc_xs() {
		return silt_clay_conc_xs;
	}

	public void setSilt_clay_conc_xs(String silt_clay_conc_xs) {
		this.silt_clay_conc_xs = silt_clay_conc_xs;
	}

	public String getSand_conc_xs() {
		return sand_conc_xs;
	}

	public void setSand_conc_xs(String sand_conc_xs) {
		this.sand_conc_xs = sand_conc_xs;
	}

	public String getSand_d16_xs() {
		return sand_d16_xs;
	}

	public void setSand_d16_xs(String sand_d16_xs) {
		this.sand_d16_xs = sand_d16_xs;
	}

	public String getSand_d50_xs() {
		return sand_d50_xs;
	}

	public void setSand_d50_xs(String sand_d50_xs) {
		this.sand_d50_xs = sand_d50_xs;
	}

	public String getSand_d84_xs() {
		return sand_d84_xs;
	}

	public void setSand_d84_xs(String sand_d84_xs) {
		this.sand_d84_xs = sand_d84_xs;
	}

	public String getSand_pct_lt_074_xs() {
		return sand_pct_lt_074_xs;
	}

	public void setSand_pct_lt_074_xs(String sand_pct_lt_074_xs) {
		this.sand_pct_lt_074_xs = sand_pct_lt_074_xs;
	}

	public String getSand_pct_lt_088_xs() {
		return sand_pct_lt_088_xs;
	}

	public void setSand_pct_lt_088_xs(String sand_pct_lt_088_xs) {
		this.sand_pct_lt_088_xs = sand_pct_lt_088_xs;
	}

	public String getSand_pct_lt_105_xs() {
		return sand_pct_lt_105_xs;
	}

	public void setSand_pct_lt_105_xs(String sand_pct_lt_105_xs) {
		this.sand_pct_lt_105_xs = sand_pct_lt_105_xs;
	}

	public String getSand_pct_lt_125_xs() {
		return sand_pct_lt_125_xs;
	}

	public void setSand_pct_lt_125_xs(String sand_pct_lt_125_xs) {
		this.sand_pct_lt_125_xs = sand_pct_lt_125_xs;
	}

	public String getSand_pct_lt_149_xs() {
		return sand_pct_lt_149_xs;
	}

	public void setSand_pct_lt_149_xs(String sand_pct_lt_149_xs) {
		this.sand_pct_lt_149_xs = sand_pct_lt_149_xs;
	}

	public String getSand_pct_lt_177_xs() {
		return sand_pct_lt_177_xs;
	}

	public void setSand_pct_lt_177_xs(String sand_pct_lt_177_xs) {
		this.sand_pct_lt_177_xs = sand_pct_lt_177_xs;
	}

	public String getSand_pct_lt_210_xs() {
		return sand_pct_lt_210_xs;
	}

	public void setSand_pct_lt_210_xs(String sand_pct_lt_210_xs) {
		this.sand_pct_lt_210_xs = sand_pct_lt_210_xs;
	}

	public String getSand_pct_lt_250_xs() {
		return sand_pct_lt_250_xs;
	}

	public void setSand_pct_lt_250_xs(String sand_pct_lt_250_xs) {
		this.sand_pct_lt_250_xs = sand_pct_lt_250_xs;
	}

	public String getSand_pct_lt_297_xs() {
		return sand_pct_lt_297_xs;
	}

	public void setSand_pct_lt_297_xs(String sand_pct_lt_297_xs) {
		this.sand_pct_lt_297_xs = sand_pct_lt_297_xs;
	}

	public String getSand_pct_lt_354_xs() {
		return sand_pct_lt_354_xs;
	}

	public void setSand_pct_lt_354_xs(String sand_pct_lt_354_xs) {
		this.sand_pct_lt_354_xs = sand_pct_lt_354_xs;
	}

	public String getSand_pct_lt_420_xs() {
		return sand_pct_lt_420_xs;
	}

	public void setSand_pct_lt_420_xs(String sand_pct_lt_420_xs) {
		this.sand_pct_lt_420_xs = sand_pct_lt_420_xs;
	}

	public String getSand_pct_lt_500_xs() {
		return sand_pct_lt_500_xs;
	}

	public void setSand_pct_lt_500_xs(String sand_pct_lt_500_xs) {
		this.sand_pct_lt_500_xs = sand_pct_lt_500_xs;
	}

	public String getSand_pct_lt_595_xs() {
		return sand_pct_lt_595_xs;
	}

	public void setSand_pct_lt_595_xs(String sand_pct_lt_595_xs) {
		this.sand_pct_lt_595_xs = sand_pct_lt_595_xs;
	}

	public String getSand_pct_lt_707_xs() {
		return sand_pct_lt_707_xs;
	}

	public void setSand_pct_lt_707_xs(String sand_pct_lt_707_xs) {
		this.sand_pct_lt_707_xs = sand_pct_lt_707_xs;
	}

	public String getSand_pct_lt_841_xs() {
		return sand_pct_lt_841_xs;
	}

	public void setSand_pct_lt_841_xs(String sand_pct_lt_841_xs) {
		this.sand_pct_lt_841_xs = sand_pct_lt_841_xs;
	}

	public String getSand_pct_lt_1000_xs() {
		return sand_pct_lt_1000_xs;
	}

	public void setSand_pct_lt_1000_xs(String sand_pct_lt_1000_xs) {
		this.sand_pct_lt_1000_xs = sand_pct_lt_1000_xs;
	}

	public String getSample_mass() {
		return sample_mass;
	}

	public void setSample_mass(String sample_mass) {
		this.sample_mass = sample_mass;
	}

	public String getMass_le_63() {
		return mass_le_63;
	}

	public void setMass_le_63(String mass_le_63) {
		this.mass_le_63 = mass_le_63;
	}

	public String getMass_gt_63() {
		return mass_gt_63;
	}

	public void setMass_gt_63(String mass_gt_63) {
		this.mass_gt_63 = mass_gt_63;
	}

	public String getConc_le_63() {
		return conc_le_63;
	}

	public void setConc_le_63(String conc_le_63) {
		this.conc_le_63 = conc_le_63;
	}

	public String getConc_gt_63() {
		return conc_gt_63;
	}

	public void setConc_gt_63(String conc_gt_63) {
		this.conc_gt_63 = conc_gt_63;
	}

	public String getSilt_clay_conc_lab() {
		return silt_clay_conc_lab;
	}

	public void setSilt_clay_conc_lab(String silt_clay_conc_lab) {
		this.silt_clay_conc_lab = silt_clay_conc_lab;
	}

	public String getSand_conc_lab() {
		return sand_conc_lab;
	}

	public void setSand_conc_lab(String sand_conc_lab) {
		this.sand_conc_lab = sand_conc_lab;
	}

	public String getSand_d16_lab() {
		return sand_d16_lab;
	}

	public void setSand_d16_lab(String sand_d16_lab) {
		this.sand_d16_lab = sand_d16_lab;
	}

	public String getSand_d50_lab() {
		return sand_d50_lab;
	}

	public void setSand_d50_lab(String sand_d50_lab) {
		this.sand_d50_lab = sand_d50_lab;
	}

	public String getSand_d84_lab() {
		return sand_d84_lab;
	}

	public void setSand_d84_lab(String sand_d84_lab) {
		this.sand_d84_lab = sand_d84_lab;
	}

	public String getSand_pct_lt_074_lab() {
		return sand_pct_lt_074_lab;
	}

	public void setSand_pct_lt_074_lab(String sand_pct_lt_074_lab) {
		this.sand_pct_lt_074_lab = sand_pct_lt_074_lab;
	}

	public String getSand_pct_lt_088_lab() {
		return sand_pct_lt_088_lab;
	}

	public void setSand_pct_lt_088_lab(String sand_pct_lt_088_lab) {
		this.sand_pct_lt_088_lab = sand_pct_lt_088_lab;
	}

	public String getSand_pct_lt_105_lab() {
		return sand_pct_lt_105_lab;
	}

	public void setSand_pct_lt_105_lab(String sand_pct_lt_105_lab) {
		this.sand_pct_lt_105_lab = sand_pct_lt_105_lab;
	}

	public String getSand_pct_lt_125_lab() {
		return sand_pct_lt_125_lab;
	}

	public void setSand_pct_lt_125_lab(String sand_pct_lt_125_lab) {
		this.sand_pct_lt_125_lab = sand_pct_lt_125_lab;
	}

	public String getSand_pct_lt_149_lab() {
		return sand_pct_lt_149_lab;
	}

	public void setSand_pct_lt_149_lab(String sand_pct_lt_149_lab) {
		this.sand_pct_lt_149_lab = sand_pct_lt_149_lab;
	}

	public String getSand_pct_lt_177_lab() {
		return sand_pct_lt_177_lab;
	}

	public void setSand_pct_lt_177_lab(String sand_pct_lt_177_lab) {
		this.sand_pct_lt_177_lab = sand_pct_lt_177_lab;
	}

	public String getSand_pct_lt_210_lab() {
		return sand_pct_lt_210_lab;
	}

	public void setSand_pct_lt_210_lab(String sand_pct_lt_210_lab) {
		this.sand_pct_lt_210_lab = sand_pct_lt_210_lab;
	}

	public String getSand_pct_lt_250_lab() {
		return sand_pct_lt_250_lab;
	}

	public void setSand_pct_lt_250_lab(String sand_pct_lt_250_lab) {
		this.sand_pct_lt_250_lab = sand_pct_lt_250_lab;
	}

	public String getSand_pct_lt_297_lab() {
		return sand_pct_lt_297_lab;
	}

	public void setSand_pct_lt_297_lab(String sand_pct_lt_297_lab) {
		this.sand_pct_lt_297_lab = sand_pct_lt_297_lab;
	}

	public String getSand_pct_lt_354_lab() {
		return sand_pct_lt_354_lab;
	}

	public void setSand_pct_lt_354_lab(String sand_pct_lt_354_lab) {
		this.sand_pct_lt_354_lab = sand_pct_lt_354_lab;
	}

	public String getSand_pct_lt_420_lab() {
		return sand_pct_lt_420_lab;
	}

	public void setSand_pct_lt_420_lab(String sand_pct_lt_420_lab) {
		this.sand_pct_lt_420_lab = sand_pct_lt_420_lab;
	}

	public String getSand_pct_lt_500_lab() {
		return sand_pct_lt_500_lab;
	}

	public void setSand_pct_lt_500_lab(String sand_pct_lt_500_lab) {
		this.sand_pct_lt_500_lab = sand_pct_lt_500_lab;
	}

	public String getSand_pct_lt_595_lab() {
		return sand_pct_lt_595_lab;
	}

	public void setSand_pct_lt_595_lab(String sand_pct_lt_595_lab) {
		this.sand_pct_lt_595_lab = sand_pct_lt_595_lab;
	}

	public String getSand_pct_lt_707_lab() {
		return sand_pct_lt_707_lab;
	}

	public void setSand_pct_lt_707_lab(String sand_pct_lt_707_lab) {
		this.sand_pct_lt_707_lab = sand_pct_lt_707_lab;
	}

	public String getSand_pct_lt_841_lab() {
		return sand_pct_lt_841_lab;
	}

	public void setSand_pct_lt_841_lab(String sand_pct_lt_841_lab) {
		this.sand_pct_lt_841_lab = sand_pct_lt_841_lab;
	}

	public String getSand_pct_lt_1000_lab() {
		return sand_pct_lt_1000_lab;
	}

	public void setSand_pct_lt_1000_lab(String sand_pct_lt_1000_lab) {
		this.sand_pct_lt_1000_lab = sand_pct_lt_1000_lab;
	}

	public String getSilt_clay_field_95er() {
		return silt_clay_field_95er;
	}

	public void setSilt_clay_field_95er(String silt_clay_field_95er) {
		this.silt_clay_field_95er = silt_clay_field_95er;
	}

	public String getSand_field_95er() {
		return sand_field_95er;
	}

	public void setSand_field_95er(String sand_field_95er) {
		this.sand_field_95er = sand_field_95er;
	}

	public String getSand_d50_field_95er() {
		return sand_d50_field_95er;
	}

	public void setSand_d50_field_95er(String sand_d50_field_95er) {
		this.sand_d50_field_95er = sand_d50_field_95er;
	}

	public String getSilt_clay_lab_bias_cor() {
		return silt_clay_lab_bias_cor;
	}

	public void setSilt_clay_lab_bias_cor(String silt_clay_lab_bias_cor) {
		this.silt_clay_lab_bias_cor = silt_clay_lab_bias_cor;
	}

	public String getSilt_clay_lab_95er() {
		return silt_clay_lab_95er;
	}

	public void setSilt_clay_lab_95er(String silt_clay_lab_95er) {
		this.silt_clay_lab_95er = silt_clay_lab_95er;
	}

	public String getSand_lab_95er() {
		return sand_lab_95er;
	}

	public void setSand_lab_95er(String sand_lab_95er) {
		this.sand_lab_95er = sand_lab_95er;
	}

	public String getSand_d50_lab_95er() {
		return sand_d50_lab_95er;
	}

	public void setSand_d50_lab_95er(String sand_d50_lab_95er) {
		this.sand_d50_lab_95er = sand_d50_lab_95er;
	}

	public String getSilt_clay_tot_95er() {
		return silt_clay_tot_95er;
	}

	public void setSilt_clay_tot_95er(String silt_clay_tot_95er) {
		this.silt_clay_tot_95er = silt_clay_tot_95er;
	}

	public String getSand_tot_95er() {
		return sand_tot_95er;
	}

	public void setSand_tot_95er(String sand_tot_95er) {
		this.sand_tot_95er = sand_tot_95er;
	}

	public String getSand_d50_tot_95er() {
		return sand_d50_tot_95er;
	}

	public void setSand_d50_tot_95er(String sand_d50_tot_95er) {
		this.sand_d50_tot_95er = sand_d50_tot_95er;
	}

	
	
}
