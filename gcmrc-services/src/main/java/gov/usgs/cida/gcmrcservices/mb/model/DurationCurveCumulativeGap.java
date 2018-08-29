/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.usgs.cida.gcmrcservices.mb.model;

/**
 *
 * @author mhines
 */
public class DurationCurveCumulativeGap {
	
	private String gapMinutesPercent;
	private String gapMinutes;

	public DurationCurveCumulativeGap() {
	}
	
	public DurationCurveCumulativeGap(String gapMinutesPercent, String gapMinutes) {
		this.gapMinutesPercent = gapMinutesPercent;
		this.gapMinutes = gapMinutes;
	}
	
	public String getGapMinutesPercent() {
		return gapMinutesPercent;
	}
	
	public String getGapMinutes() {
		return gapMinutes;
	}
	
		public void setGapMinutesPercent(String val) {
		gapMinutesPercent = val;
	}
	
	public void setGapMinutes(String val) {
		gapMinutes = val;
	}
}
