package gov.usgs.cida.gcmrcservices.mb.model;

/**
 *
 * @author kmschoep
 */
public class BedSedimentDownload {

	private String station_name;
	private String station_num;
	private String bed_dt;
	private String bed_tm;
	private String time_zone;
	private Integer sample_set;
	private String notes;
	private Integer station_location;
	private Double sample_weight;
	private Double sand_d50;
	private Double fines_d50;
	private Double total_d50;
	private Double pct_btwn_063_125;
	private Double size_dist_lt_037;
	private Double size_dist_lt_044;
	private Double size_dist_lt_053;
	private Double size_dist_lt_063;
	private Double size_dist_lt_074;
	private Double size_dist_lt_088;
	private Double size_dist_lt_105;
	private Double size_dist_lt_125;
	private Double size_dist_lt_149;
	private Double size_dist_lt_177;
	private Double size_dist_lt_210;
	private Double size_dist_lt_250;
	private Double size_dist_lt_297;
	private Double size_dist_lt_354;
	private Double size_dist_lt_420;
	private Double size_dist_lt_500;
	private Double size_dist_lt_595;
	private Double size_dist_lt_707;
	private Double size_dist_lt_841;
	private Double size_dist_lt_1_00;
	private Double size_dist_lt_1_41;
	private Double size_dist_lt_2_00;
	private Double size_dist_lt_2_80;
	private Double size_dist_lt_4_00;
	private Double size_dist_lt_5_60;
	private Double size_dist_lt_8_00;
	private Double size_dist_lt_11_3;
	private Double size_dist_lt_16_0;
	private Double size_dist_lt_22_6;
	private Double size_dist_lt_32_0;
	private Double size_dist_lt_45_0;
	private Double size_dist_lt_64_0;
	private Double size_dist_lt_91_0;
	private Double size_dist_lt_128_0;

	public BedSedimentDownload() {
	}

	public BedSedimentDownload(String station_name, String station_num, String bed_dt, String bed_tm, String time_zone, int sample_set, String notes, int station_location, Double sample_weight, Double sand_d50, Double fines_d50, Double total_d50, Double pct_btwn_063_125, Double size_dist_lt_037, Double size_dist_lt_044, Double size_dist_lt_053, Double size_dist_lt_063, Double size_dist_lt_074, Double size_dist_lt_088, Double size_dist_lt_105, Double size_dist_lt_125, Double size_dist_lt_149, Double size_dist_lt_177, Double size_dist_lt_210, Double size_dist_lt_250, Double size_dist_lt_297, Double size_dist_lt_354, Double size_dist_lt_420, Double size_dist_lt_500, Double size_dist_lt_595, Double size_dist_lt_707, Double size_dist_lt_841, Double size_dist_lt_1_00, Double size_dist_lt_1_41, Double size_dist_lt_2_00, Double size_dist_lt_2_80, Double size_dist_lt_4_00, Double size_dist_lt_5_60, Double size_dist_lt_8_00, Double size_dist_lt_11_3, Double size_dist_lt_16_0, Double size_dist_lt_22_6, Double size_dist_lt_32_0, Double size_dist_lt_45_0, Double size_dist_lt_64_0, Double size_dist_lt_91_0, Double size_dist_lt_128_0) {
		this.station_name = station_name;
		this.station_num = station_num;
		this.bed_dt = bed_dt;
		this.bed_tm = bed_tm;
		this.time_zone = time_zone;
		this.sample_set = sample_set;
		this.notes = notes;
		this.station_location = station_location;
		this.sample_weight = sample_weight;
		this.sand_d50 = sand_d50;
		this.fines_d50 = fines_d50;
		this.total_d50 = total_d50;
		this.pct_btwn_063_125 = pct_btwn_063_125;
		this.size_dist_lt_037 = size_dist_lt_037;
		this.size_dist_lt_044 = size_dist_lt_044;
		this.size_dist_lt_053 = size_dist_lt_053;
		this.size_dist_lt_063 = size_dist_lt_063;
		this.size_dist_lt_074 = size_dist_lt_074;
		this.size_dist_lt_088 = size_dist_lt_088;
		this.size_dist_lt_105 = size_dist_lt_105;
		this.size_dist_lt_125 = size_dist_lt_125;
		this.size_dist_lt_149 = size_dist_lt_149;
		this.size_dist_lt_177 = size_dist_lt_177;
		this.size_dist_lt_210 = size_dist_lt_210;
		this.size_dist_lt_250 = size_dist_lt_250;
		this.size_dist_lt_297 = size_dist_lt_297;
		this.size_dist_lt_354 = size_dist_lt_354;
		this.size_dist_lt_420 = size_dist_lt_420;
		this.size_dist_lt_500 = size_dist_lt_500;
		this.size_dist_lt_595 = size_dist_lt_595;
		this.size_dist_lt_707 = size_dist_lt_707;
		this.size_dist_lt_841 = size_dist_lt_841;
		this.size_dist_lt_1_00 = size_dist_lt_1_00;
		this.size_dist_lt_1_41 = size_dist_lt_1_41;
		this.size_dist_lt_2_00 = size_dist_lt_2_00;
		this.size_dist_lt_2_80 = size_dist_lt_2_80;
		this.size_dist_lt_4_00 = size_dist_lt_4_00;
		this.size_dist_lt_5_60 = size_dist_lt_5_60;
		this.size_dist_lt_8_00 = size_dist_lt_8_00;
		this.size_dist_lt_11_3 = size_dist_lt_11_3;
		this.size_dist_lt_16_0 = size_dist_lt_16_0;
		this.size_dist_lt_22_6 = size_dist_lt_22_6;
		this.size_dist_lt_32_0 = size_dist_lt_32_0;
		this.size_dist_lt_45_0 = size_dist_lt_45_0;
		this.size_dist_lt_64_0 = size_dist_lt_64_0;
		this.size_dist_lt_91_0 = size_dist_lt_91_0;
		this.size_dist_lt_128_0 = size_dist_lt_128_0;
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

	public String getBed_dt() {
		return bed_dt;
	}

	public void setBed_dt(String bed_dt) {
		this.bed_dt = bed_dt;
	}

	public String getBed_tm() {
		return bed_tm;
	}

	public void setBed_tm(String bed_tm) {
		this.bed_tm = bed_tm;
	}
	
	public String getTime_zone() {
		return time_zone;
	}

	public void setTime_zone(String time_zone) {
		this.time_zone = time_zone;
	}

	public int getSample_set() {
		return sample_set;
	}

	public void setSample_set(int sample_set) {
		this.sample_set = sample_set;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public int getStation_location() {
		return station_location;
	}

	public void setStation_location(int station_location) {
		this.station_location = station_location;
	}

	public Double getSample_weight() {
		return sample_weight;
	}

	public void setSample_weight(Double sample_weight) {
		this.sample_weight = sample_weight;
	}

	public Double getSand_d50() {
		return sand_d50;
	}

	public void setSand_d50(Double sand_d50) {
		this.sand_d50 = sand_d50;
	}

	public Double getFines_d50() {
		return fines_d50;
	}

	public void setFines_d50(Double fines_d50) {
		this.fines_d50 = fines_d50;
	}

	public Double getTotal_d50() {
		return total_d50;
	}

	public void setTotal_d50(Double total_d50) {
		this.total_d50 = total_d50;
	}

	public Double getPct_btwn_063_125() {
		return pct_btwn_063_125;
	}

	public void setPct_btwn_063_125(Double pct_btwn_063_125) {
		this.pct_btwn_063_125 = pct_btwn_063_125;
	}

	public Double getSize_dist_lt_037() {
		return size_dist_lt_037;
	}

	public void setSize_dist_lt_037(Double size_dist_lt_037) {
		this.size_dist_lt_037 = size_dist_lt_037;
	}

	public Double getSize_dist_lt_044() {
		return size_dist_lt_044;
	}

	public void setSize_dist_lt_044(Double size_dist_lt_044) {
		this.size_dist_lt_044 = size_dist_lt_044;
	}

	public Double getSize_dist_lt_053() {
		return size_dist_lt_053;
	}

	public void setSize_dist_lt_053(Double size_dist_lt_053) {
		this.size_dist_lt_053 = size_dist_lt_053;
	}

	public Double getSize_dist_lt_063() {
		return size_dist_lt_063;
	}

	public void setSize_dist_lt_063(Double size_dist_lt_063) {
		this.size_dist_lt_063 = size_dist_lt_063;
	}

	public Double getSize_dist_lt_074() {
		return size_dist_lt_074;
	}

	public void setSize_dist_lt_074(Double size_dist_lt_074) {
		this.size_dist_lt_074 = size_dist_lt_074;
	}

	public Double getSize_dist_lt_088() {
		return size_dist_lt_088;
	}

	public void setSize_dist_lt_088(Double size_dist_lt_088) {
		this.size_dist_lt_088 = size_dist_lt_088;
	}

	public Double getSize_dist_lt_105() {
		return size_dist_lt_105;
	}

	public void setSize_dist_lt_105(Double size_dist_lt_105) {
		this.size_dist_lt_105 = size_dist_lt_105;
	}

	public Double getSize_dist_lt_125() {
		return size_dist_lt_125;
	}

	public void setSize_dist_lt_125(Double size_dist_lt_125) {
		this.size_dist_lt_125 = size_dist_lt_125;
	}

	public Double getSize_dist_lt_149() {
		return size_dist_lt_149;
	}

	public void setSize_dist_lt_149(Double size_dist_lt_149) {
		this.size_dist_lt_149 = size_dist_lt_149;
	}

	public Double getSize_dist_lt_177() {
		return size_dist_lt_177;
	}

	public void setSize_dist_lt_177(Double size_dist_lt_177) {
		this.size_dist_lt_177 = size_dist_lt_177;
	}

	public Double getSize_dist_lt_210() {
		return size_dist_lt_210;
	}

	public void setSize_dist_lt_210(Double size_dist_lt_210) {
		this.size_dist_lt_210 = size_dist_lt_210;
	}

	public Double getSize_dist_lt_250() {
		return size_dist_lt_250;
	}

	public void setSize_dist_lt_250(Double size_dist_lt_250) {
		this.size_dist_lt_250 = size_dist_lt_250;
	}

	public Double getSize_dist_lt_297() {
		return size_dist_lt_297;
	}

	public void setSize_dist_lt_297(Double size_dist_lt_297) {
		this.size_dist_lt_297 = size_dist_lt_297;
	}

	public Double getSize_dist_lt_354() {
		return size_dist_lt_354;
	}

	public void setSize_dist_lt_354(Double size_dist_lt_354) {
		this.size_dist_lt_354 = size_dist_lt_354;
	}

	public Double getSize_dist_lt_420() {
		return size_dist_lt_420;
	}

	public void setSize_dist_lt_420(Double size_dist_lt_420) {
		this.size_dist_lt_420 = size_dist_lt_420;
	}

	public Double getSize_dist_lt_500() {
		return size_dist_lt_500;
	}

	public void setSize_dist_lt_500(Double size_dist_lt_500) {
		this.size_dist_lt_500 = size_dist_lt_500;
	}

	public Double getSize_dist_lt_595() {
		return size_dist_lt_595;
	}

	public void setSize_dist_lt_595(Double size_dist_lt_595) {
		this.size_dist_lt_595 = size_dist_lt_595;
	}

	public Double getSize_dist_lt_707() {
		return size_dist_lt_707;
	}

	public void setSize_dist_lt_707(Double size_dist_lt_707) {
		this.size_dist_lt_707 = size_dist_lt_707;
	}

	public Double getSize_dist_lt_841() {
		return size_dist_lt_841;
	}

	public void setSize_dist_lt_841(Double size_dist_lt_841) {
		this.size_dist_lt_841 = size_dist_lt_841;
	}

	public Double getSize_dist_lt_1_00() {
		return size_dist_lt_1_00;
	}

	public void setSize_dist_lt_1_00(Double size_dist_lt_1_00) {
		this.size_dist_lt_1_00 = size_dist_lt_1_00;
	}

	public Double getSize_dist_lt_1_41() {
		return size_dist_lt_1_41;
	}

	public void setSize_dist_lt_1_41(Double size_dist_lt_1_41) {
		this.size_dist_lt_1_41 = size_dist_lt_1_41;
	}

	public Double getSize_dist_lt_2_00() {
		return size_dist_lt_2_00;
	}

	public void setSize_dist_lt_2_00(Double size_dist_lt_2_00) {
		this.size_dist_lt_2_00 = size_dist_lt_2_00;
	}

	public Double getSize_dist_lt_2_80() {
		return size_dist_lt_2_80;
	}

	public void setSize_dist_lt_2_80(Double size_dist_lt_2_80) {
		this.size_dist_lt_2_80 = size_dist_lt_2_80;
	}

	public Double getSize_dist_lt_4_00() {
		return size_dist_lt_4_00;
	}

	public void setSize_dist_lt_4_00(Double size_dist_lt_4_00) {
		this.size_dist_lt_4_00 = size_dist_lt_4_00;
	}

	public Double getSize_dist_lt_5_60() {
		return size_dist_lt_5_60;
	}

	public void setSize_dist_lt_5_60(Double size_dist_lt_5_60) {
		this.size_dist_lt_5_60 = size_dist_lt_5_60;
	}

	public Double getSize_dist_lt_8_00() {
		return size_dist_lt_8_00;
	}

	public void setSize_dist_lt_8_00(Double size_dist_lt_8_00) {
		this.size_dist_lt_8_00 = size_dist_lt_8_00;
	}

	public Double getSize_dist_lt_11_3() {
		return size_dist_lt_11_3;
	}

	public void setSize_dist_lt_11_3(Double size_dist_lt_11_3) {
		this.size_dist_lt_11_3 = size_dist_lt_11_3;
	}

	public Double getSize_dist_lt_16_0() {
		return size_dist_lt_16_0;
	}

	public void setSize_dist_lt_16_0(Double size_dist_lt_16_0) {
		this.size_dist_lt_16_0 = size_dist_lt_16_0;
	}

	public Double getSize_dist_lt_22_6() {
		return size_dist_lt_22_6;
	}

	public void setSize_dist_lt_22_6(Double size_dist_lt_22_6) {
		this.size_dist_lt_22_6 = size_dist_lt_22_6;
	}

	public Double getSize_dist_lt_32_0() {
		return size_dist_lt_32_0;
	}

	public void setSize_dist_lt_32_0(Double size_dist_lt_32_0) {
		this.size_dist_lt_32_0 = size_dist_lt_32_0;
	}

	public Double getSize_dist_lt_45_0() {
		return size_dist_lt_45_0;
	}

	public void setSize_dist_lt_45_0(Double size_dist_lt_45_0) {
		this.size_dist_lt_45_0 = size_dist_lt_45_0;
	}

	public Double getSize_dist_lt_64_0() {
		return size_dist_lt_64_0;
	}

	public void setSize_dist_lt_64_0(Double size_dist_lt_64_0) {
		this.size_dist_lt_64_0 = size_dist_lt_64_0;
	}

	public Double getSize_dist_lt_91_0() {
		return size_dist_lt_91_0;
	}

	public void setSize_dist_lt_91_0(Double size_dist_lt_91_0) {
		this.size_dist_lt_91_0 = size_dist_lt_91_0;
	}

	public Double getSize_dist_lt_128_0() {
		return size_dist_lt_128_0;
	}

	public void setSize_dist_lt_128_0(Double size_dist_lt_128_0) {
		this.size_dist_lt_128_0 = size_dist_lt_128_0;
	}
	
}
