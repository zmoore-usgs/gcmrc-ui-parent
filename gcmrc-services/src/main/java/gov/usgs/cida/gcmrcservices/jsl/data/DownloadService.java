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

	public DownloadService() {   //HACK  !!!! HOLY HACK BATMAN
		this.specMapping.put("samples", QWDownloadSpec.class);
		this.specMapping.put("GCDAMPsamples", QWDownloadSpec.class);
		this.specMapping.put("CLsamples", QWDownloadSpec.class);
		this.specMapping.put("BIBEsamples", CentralQWDownloadSpec.class);
		this.specMapping.put("CRDsamples", QWDownloadSpec.class);
		this.specMapping.put("DINOsamples", QWDownloadSpec.class);
		this.specMapping.put("bedSediment", BedSedimentDownloadSpec.class);
		this.specMapping.put("GCDAMPbedSediment", BedSedimentDownloadSpec.class);
		this.specMapping.put("CLbedSediment", BedSedimentDownloadSpec.class);
		this.specMapping.put("BIBEbedSediment", CentralBedSedimentDownloadSpec.class);
		this.specMapping.put("CRDbedSediment", BedSedimentDownloadSpec.class);
		this.specMapping.put("DINObedSediment", BedSedimentDownloadSpec.class);
	}
	
}
