package gov.usgs.cida.gcmrcservices.mb.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kmschoep
 */
public class ReachPOR {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(ReachPOR.class);

	protected final String upstreamStation;
	protected final String upstreamSecondaryStation;
	protected final String downstreamStation;
	protected final String groupName;
	protected final String upstreamBeginPosition;
	protected final String upstreamEndPosition;
	protected final String upstreamSecondaryBeginPosition;
	protected final String upstreamSecondaryEndPosition;
	protected final String downstreamBeginPosition;
	protected final String downstreamEndPosition;
	protected final String majorTribBeginPosition;
	protected final String majorTribEndPosition;
	protected final String minorTribBeginPosition;
	protected final String minorTribEndPosition;

	public ReachPOR() {
		this.upstreamStation = null;
		this.upstreamSecondaryStation = null;
		this.downstreamStation = null;
		this.groupName = null;
		this.upstreamBeginPosition = null;
		this.upstreamEndPosition = null;
		this.upstreamSecondaryBeginPosition = null;
		this.upstreamSecondaryEndPosition = null;
		this.downstreamBeginPosition = null;
		this.downstreamEndPosition = null;
		this.majorTribBeginPosition =  null;
		this.majorTribEndPosition = null;
		this.minorTribBeginPosition = null;
		this.minorTribEndPosition = null;
	}

	public ReachPOR(String upstreamStation
			, String upstreamSecondaryStation
			, String downstreamStation
			, String groupName
			, String upstreamBeginPosition
			, String upstreamEndPosition
			, String upstreamSecondaryBeginPosition
			, String upstreamSecondaryEndPosition
			, String downstreamBeginPosition
			, String downstreamEndPosition
			, String majorTribBeginPosition
			, String majorTribEndPosition
			, String minorTribBeginPosition
			, String minorTribEndPosition
			) {
		this.upstreamStation = upstreamStation;
		this.upstreamSecondaryStation = upstreamSecondaryStation;
		this.downstreamStation = downstreamStation;
		this.groupName = groupName;
		this.upstreamBeginPosition = upstreamBeginPosition;
		this.upstreamEndPosition = upstreamEndPosition;
		this.upstreamSecondaryBeginPosition = upstreamSecondaryBeginPosition;
		this.upstreamSecondaryEndPosition = upstreamSecondaryEndPosition;
		this.downstreamBeginPosition = downstreamBeginPosition;
		this.downstreamEndPosition = downstreamEndPosition;
		this.majorTribBeginPosition = majorTribBeginPosition;
		this.majorTribEndPosition = majorTribEndPosition;
		this.minorTribBeginPosition = minorTribBeginPosition;
		this.minorTribEndPosition = minorTribEndPosition;
	}

	public String getUpstreamSecondaryStation() {
		return upstreamSecondaryStation;
	}

	public String getUpstreamSecondaryBeginPosition() {
		return upstreamSecondaryBeginPosition;
	}

	public String getUpstreamSecondaryEndPosition() {
		return upstreamSecondaryEndPosition;
	}

	public String getMajorTribBeginPosition() {
		return majorTribBeginPosition;
	}

	public String getMajorTribEndPosition() {
		return majorTribEndPosition;
	}

	public String getUpstreamStation() {
		return upstreamStation;
	}

	public String getDownstreamStation() {
		return downstreamStation;
	}

	public String getGroupName() {
		return groupName;
	}

	public String getUpstreamBeginPosition() {
		return upstreamBeginPosition;
	}

	public String getUpstreamEndPosition() {
		return upstreamEndPosition;
	}

	public String getDownstreamBeginPosition() {
		return downstreamBeginPosition;
	}

	public String getDownstreamEndPosition() {
		return downstreamEndPosition;
	}

	public String getMinorTribBeginPosition() {
		return minorTribBeginPosition;
	}

	public String getMinorTribEndPosition() {
		return minorTribEndPosition;
	}

		
}
