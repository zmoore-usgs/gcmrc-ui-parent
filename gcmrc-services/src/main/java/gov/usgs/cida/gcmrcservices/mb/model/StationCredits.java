package gov.usgs.cida.gcmrcservices.mb.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kmschoep
 */
public class StationCredits {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(StationCredits.class);

	protected final String nwisSite;
	protected final String shortName;
	protected final String orgCode;
	protected final String displayOrder;

	public StationCredits() {
		this.nwisSite = null;
		this.shortName = null;
		this.orgCode = null;
		this.displayOrder = null;
	}

	public StationCredits(String nwisSite
			, String shortName
			, String orgCode
			, String displayOrder) {
		this.nwisSite = nwisSite;
		this.shortName = shortName;
		this.orgCode = orgCode;
		this.displayOrder = displayOrder;
	}
	
	public String getNwisSite() {
		return nwisSite;
	}
	
	public String getShortName() {
		return shortName;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public String getDisplayOrder() {
		return displayOrder;
	}

	
}
