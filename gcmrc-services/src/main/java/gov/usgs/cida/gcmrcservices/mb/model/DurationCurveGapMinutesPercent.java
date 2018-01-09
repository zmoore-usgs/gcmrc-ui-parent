package gov.usgs.cida.gcmrcservices.mb.model;

/**
 *
 * @author kmschoep
 */
public class DurationCurveGapMinutesPercent {
	
	private Double gapMinutesPercent;

	public DurationCurveGapMinutesPercent() {
	}
	
	public DurationCurveGapMinutesPercent(Double gapMinutes) {
		this.gapMinutesPercent = gapMinutes;
	}
	
	public Double getGapMinutesPercent() {
		return gapMinutesPercent;
	}
	
	public void setGapMinutesPercent(Double val) {
		gapMinutesPercent = val;
	}
}
