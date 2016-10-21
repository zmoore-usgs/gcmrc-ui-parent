package gov.usgs.cida.gcmrcservices.mb.model;

import java.util.List;

/**
 *
 * @author zmoore
 */
public class DurationCurve {	
	private List<DurationCurvePoint> points;
	private int groupId;
	private String binType;

	public DurationCurve(List<DurationCurvePoint> pts, int id, String bin) {
		points = pts;
		groupId = id;
		binType = bin.equalsIgnoreCase("LOG_BINS") ? "log" : "lin";
	}
	
	public List<DurationCurvePoint> getPoints() {
		return points;
	}
	
	public int getGroupId() {
		return groupId;
	}
	
	public String getBinType() {
		return binType;
	}

}
