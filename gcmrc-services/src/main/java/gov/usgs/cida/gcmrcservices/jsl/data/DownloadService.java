package gov.usgs.cida.gcmrcservices.jsl.data;

import gov.usgs.webservices.jdbc.service.WebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class DownloadService extends WebService {
	private static final Logger log = LoggerFactory.getLogger(DownloadService.class);

	public DownloadService() {
		this.specMapping.put("samples", QWDownloadSpec.class);
	}
	
}
