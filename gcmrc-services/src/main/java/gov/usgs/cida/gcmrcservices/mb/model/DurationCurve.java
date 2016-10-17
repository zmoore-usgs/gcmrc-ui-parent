package gov.usgs.cida.gcmrcservices.mb.model;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley, zmoore
 */
public class DurationCurve {
	private static final Logger log = LoggerFactory.getLogger(DurationCurve.class);
	
	private List<DurationCurvePoint> points;
	private int groupId;

	public DurationCurve(List<DurationCurvePoint> pts, int id) {
		points = pts;
		groupId = id;
	}
	
	public List<DurationCurvePoint> getPoints() {
		return points;
	}
	
	public int getGroupId() {
		return groupId;
	}

}
