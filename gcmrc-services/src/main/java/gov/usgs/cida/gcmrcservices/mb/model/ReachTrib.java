package gov.usgs.cida.gcmrcservices.mb.model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kmschoep
 */
public class ReachTrib {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(ReachTrib.class);

	protected final String reachName;
	protected final String reachGroup;
	protected final String upstreamStation;
	protected final String upstreamDisplayName;
	protected final String downstreamStation;
	protected final String downstreamDisplayName;
	protected final String majorTribRiver;
	protected final String majorTribSite;
	protected final String majorGroup;
	protected final String minorTribSite;
	protected final String minorGroup;
	protected final String network;
	protected final String displayOrder;
	protected final List<String> groupName;
	protected final String endStaticRec;
	protected final String newestSuspSed;
	protected final String downstreamDischargeStation;
	protected final String downstreamDischargeName;

	public ReachTrib() {
		this.reachName = null;
		this.reachGroup = null;
		this.upstreamStation = null;
		this.upstreamDisplayName = null;
		this.downstreamStation = null;
		this.downstreamDisplayName = null;
		this.majorTribRiver = null;
		this.majorTribSite = null;
		this.majorGroup = null;
		this.minorTribSite = null;
		this.minorGroup = null;
		this.network = null;
		this.displayOrder = null;
		this.groupName = null;
		this.endStaticRec = null;
		this.newestSuspSed = null;
		this.downstreamDischargeStation = null;
		this.downstreamDischargeName = null;
	}

	public ReachTrib(String reachName
			, String reachGroup
			, String upstreamStation
			, String upstreamDisplayName
			, String downstreamStation
			, String downstreamDisplayName
			, String majorTribRiver
			, String majorTribSite
			, String majorGroup
			, String minorTribSite
			, String minorGroup
			, String network
			, String displayOrder
			, List<String> groupName
			, String endStaticRec
			, String newestSuspSed
			, String downstreamDischargeStation
			, String downstreamDischargeName) {
		this.reachName = reachName;
		this.reachGroup = reachGroup;
		this.upstreamStation = upstreamStation;
		this.upstreamDisplayName = upstreamDisplayName;
		this.downstreamStation = downstreamStation;
		this.downstreamDisplayName = downstreamDisplayName;
		this.majorTribRiver = majorTribRiver;
		this.majorTribSite = majorTribSite;
		this.majorGroup = majorGroup;
		this.minorTribSite = minorTribSite;
		this.minorGroup = minorGroup;
		this.network = network;
		this.displayOrder = displayOrder;
		this.groupName = groupName;
		this.endStaticRec = endStaticRec;
		this.newestSuspSed = newestSuspSed;
		this.downstreamDischargeStation = downstreamDischargeStation;
		this.downstreamDischargeName = downstreamDischargeName;
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

	public List<String> getGroupName() {
		return groupName;
	}

	public String getReachName() {
		return reachName;
	}

	public String getUpstreamStation() {
		return upstreamStation;
	}

	public String getUpstreamDisplayName() {
		return upstreamDisplayName;
	}

	public String getDownstreamStation() {
		return downstreamStation;
	}

	public String getDownstreamDisplayName() {
		return downstreamDisplayName;
	}

	public String getMajorTribRiver() {
		return majorTribRiver;
	}

	public String getMajorTribSite() {
		return majorTribSite;
	}

	public String getMinorTribSite() {
		return minorTribSite;
	}

	public String getNetwork() {
		return network;
	}

	public String getDisplayOrder() {
		return displayOrder;
	}

	public String getEndStaticRec() {
		return endStaticRec;
	}

	public String getNewestSuspSed() {
		return newestSuspSed;
	}

	public String getDownstreamDischargeStation() {
		return downstreamDischargeStation;
	}

	public String getDownstreamDischargeName() {
		return downstreamDischargeName;
	}
	
	
	
}
