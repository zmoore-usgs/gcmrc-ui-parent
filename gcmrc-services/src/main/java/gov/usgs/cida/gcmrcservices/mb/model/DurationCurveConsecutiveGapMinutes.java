package gov.usgs.cida.gcmrcservices.mb.model;

/**
 *
 * @author kmschoep
 */
public class DurationCurveConsecutiveGapMinutes {
	
	private String gapStart;
	private String gapEnd;
	private Double gapMinutes;

	public DurationCurveConsecutiveGapMinutes() {
	}
	
	public DurationCurveConsecutiveGapMinutes(String gapStart, String gapEnd, Double gapMinutes) {
		this.gapStart = gapStart;
		this.gapEnd = gapEnd;
		this.gapMinutes = gapMinutes;
	}
	
	public String getGapStart() {
		return gapStart;
	}
	
	public String getGapEnd() {
		return gapEnd;
	}
	
	public Double getGapMinutes() {
		return gapMinutes;
	}
	
	public void setGapStart(String val) {
		gapStart = val;
	}
	
	public void setGapEnd(String val) {
		gapEnd = val;
	}
	
	public void setGapMinutes(Double val) {
		gapMinutes = val;
	}
}
