package gov.usgs.cida.gcmrcservices.mb.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kmschoep
 */
public class StationBs {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(StationBs.class);

	protected final String groupId;
	protected final String groupName;
	protected final String beginPosition;
	protected final String endPosition;
	protected final String site;
	protected final String displayOrder;
	protected final String displayName;
	protected final String isVisible;
	protected final String units;
	protected final String unitsShort;
	protected final String decimalPlaces;

	public StationBs() {
		this.groupId = null;
		this.groupName = null;
		this.beginPosition = null;
		this.endPosition = null;
		this.site = null;
		this.displayOrder = null;
		this.displayName = null;
		this.isVisible = null;
		this.units = null;
		this.unitsShort = null;
		this.decimalPlaces = null;
	}

	public StationBs(String groupId
			, String groupName
			, String beginPosition
			, String endPosition
			, String site
			, String displayOrder
			, String displayName
			, String isVisible
			, String units
			, String unitsShort
			, String decimalPlaces
			) {
		this.groupId = groupId;
		this.groupName = groupName;
		this.beginPosition = beginPosition;
		this.endPosition = endPosition;
		this.site = site;
		this.displayOrder = displayOrder;
		this.displayName = displayName;
		this.isVisible = isVisible;
		this.units = units;
		this.unitsShort = unitsShort;
		this.decimalPlaces = decimalPlaces;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getBeginPosition() {
		return beginPosition;
	}

	public String getEndPosition() {
		return endPosition;
	}

	public String getSite() {
		return site;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getGroupName() {
		return groupName;
	}

	public String getUnits() {
		return units;
	}

	public String getUnitsShort() {
		return unitsShort;
	}

	public String getDecimalPlaces() {
		return decimalPlaces;
	}

	public String getIsVisible() {
		return isVisible;
	}

	public String getDisplayOrder() {
		return displayOrder;
	}

	
}
