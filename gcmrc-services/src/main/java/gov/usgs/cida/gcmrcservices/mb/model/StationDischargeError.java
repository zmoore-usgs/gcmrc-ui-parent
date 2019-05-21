package gov.usgs.cida.gcmrcservices.mb.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kmschoep
 */
public class StationDischargeError {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(StationDischargeError.class);

	protected final String groupId;
	protected final String groupName;
	protected final String site;
	protected final String displayOrder;
	protected final String displayName;
	protected final String isVisible;
	protected final String units;
	protected final String unitsShort;
	protected final String decimalPlaces;
	protected final String sampleMethod;
	protected final String usePumpColoring;

	public StationDischargeError() {
		this.groupId = null;
		this.groupName = null;
		this.sampleMethod = null;
		this.usePumpColoring = null;
		this.site = null;
		this.displayOrder = null;
		this.displayName = null;
		this.isVisible = null;
		this.units = null;
		this.unitsShort = null;
		this.decimalPlaces = null;
	}

	public StationDischargeError(String groupId
			, String groupName
			, String sampleMethod
			, String usePumpColoring
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
		this.sampleMethod = sampleMethod;
		this.usePumpColoring =usePumpColoring;
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

	public String getSampleMethod() {
		return sampleMethod;
	}

	public String getUsePumpColoring() {
		return usePumpColoring;
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
