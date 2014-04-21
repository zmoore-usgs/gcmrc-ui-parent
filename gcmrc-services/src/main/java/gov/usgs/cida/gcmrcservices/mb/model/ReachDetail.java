package gov.usgs.cida.gcmrcservices.mb.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class ReachDetail {
	private static final Logger log = LoggerFactory.getLogger(ReachDetail.class);

	protected final String upstreamStation;
	protected final String reachGroup;
	protected final String majorGroup;
	protected final String minorGroup;
	protected final String majorStation;
	protected final String minorStation;

	public ReachDetail() {
		this.upstreamStation = null;
		this.reachGroup = null;
		this.majorGroup = null;
		this.minorGroup = null;
		this.majorStation = null;
		this.minorStation = null;
	}

	public ReachDetail(String upstreamStation, String reachGroup, String majorGroup, String minorGroup, String majorStation, String minorStation) {
		this.upstreamStation = upstreamStation;
		this.reachGroup = reachGroup;
		this.majorGroup = majorGroup;
		this.minorGroup = minorGroup;
		this.majorStation = majorStation;
		this.minorStation = minorStation;
	}
	
	public String getReachGroup() {
		return reachGroup;
	}

	public String getMajorGroup() {
		return majorGroup;
	}
	
	public String getMinorGroup() {
		return minorGroup;
	}

	public String getMajorStation() {
		return majorStation;
	}

	public String getMinorStation() {
		return minorStation;
	}
	
	
	
}
