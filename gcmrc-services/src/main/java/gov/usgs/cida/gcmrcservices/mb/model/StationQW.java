package gov.usgs.cida.gcmrcservices.mb.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kmschoep
 */
public class StationQW {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(StationQW.class);

	protected final String groupId;
	protected final String beginPosition;
	protected final String endPosition;
	protected final String site;
	protected final String sampleMethod;
	protected final String usePumpColoring;
	protected final String displayName;
	protected final String groupName;
	protected final String units;
	protected final String unitsShort;
	protected final String decimalPlaces;
	protected final String displayOrder;
	protected final String isVisible;

	public StationQW() {
		this.groupId = null;
		this.beginPosition = null;
		this.endPosition = null;
		this.site = null;
		this.sampleMethod = null;
		this.usePumpColoring = null;
		this.displayName = null;
		this.groupName = null;
		this.units = null;
		this.unitsShort = null;
		this.decimalPlaces = null;
		this.displayOrder = null;
		this.isVisible = null;
	}

	public StationQW(String groupId
			, String beginPosition
			, String endPosition
			, String site
			, String sampleMethod
			, String usePumpColoring
			, String displayName
			, String groupName
			, String units
			, String unitsShort
			, String decimalPlaces
			, String displayOrder
			, String isVisible) {
		this.groupId = groupId;
		this.beginPosition = beginPosition;
		this.endPosition = endPosition;
		this.site = site;
		this.sampleMethod = sampleMethod;
		this.usePumpColoring = usePumpColoring;
		this.displayName = displayName;
		this.groupName = groupName;
		this.units = units;
		this.unitsShort = unitsShort;
		this.decimalPlaces = decimalPlaces;
		this.displayOrder = displayOrder;
		this.isVisible = isVisible;
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

	public String getSampleMethod() {
		return sampleMethod;
	}

	public String getUsePumpColoring() {
		return usePumpColoring;
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
