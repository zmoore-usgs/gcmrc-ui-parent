package gov.usgs.cida.gcmrcservices.mb.model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kmschoep
 */
public class StationSite {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(StationSite.class);

	protected final String siteId;
	protected final String siteName;
	protected final String displayName;
	protected final String lat;
	protected final String lon;
	protected final String network;
	protected final String active;
	protected final String displayOrder;

	public StationSite() {
		this.siteId = null;
		this.siteName = null;
		this.displayName = null;
		this.lat = null;
		this.lon = null;
		this.network = null;
		this.active = null;
		this.displayOrder = null;
	}

	public StationSite(String siteId
			, String siteName
			, String displayName
			, String lat
			, String lon
			, String network
			, String active
			, String displayOrder) {
		this.siteId = siteId;
		this.siteName = siteName;
		this.displayName = displayName;
		this.lat = lat;
		this.lon = lon;
		this.network = network;
		this.active = active;
		this.displayOrder = displayOrder;
	}
	
	public String getSiteId() {
		return siteId;
	}
	
	public String getSiteName() {
		return siteName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getLat() {
		return lat;
	}

	public String getLon() {
		return lon;
	}

	public String getnetwork() {
		return network;
	}

	public String getActive() {
		return active;
	}

	public String getDisplayOrder() {
		return displayOrder;
	}

	
}
