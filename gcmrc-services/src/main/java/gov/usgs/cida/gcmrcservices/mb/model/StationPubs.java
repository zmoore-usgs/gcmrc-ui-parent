package gov.usgs.cida.gcmrcservices.mb.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kmschoep
 */
public class StationPubs {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(StationPubs.class);

	protected final String site;
	protected final String url;
	protected final String displayName;
	protected final String displayOrder;

	public StationPubs() {
		this.site = null;
		this.url = null;
		this.displayName = null;
		this.displayOrder = null;
	}

	public StationPubs(String site
			, String displayName
			, String url
			, String displayOrder) {
		this.site = site;
		this.url = url;
		this.displayName = displayName;
		this.displayOrder = displayOrder;
	}
	
	public String getSite() {
		return site;
	}
	
	public String getUrl() {
		return url;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getDisplayOrder() {
		return displayOrder;
	}

	
}
