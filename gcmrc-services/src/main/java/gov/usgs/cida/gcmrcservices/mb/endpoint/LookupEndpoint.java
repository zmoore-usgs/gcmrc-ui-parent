package gov.usgs.cida.gcmrcservices.mb.endpoint;

import gov.usgs.cida.gcmrcservices.mb.dao.LookupDAO;
import gov.usgs.cida.gcmrcservices.mb.endpoint.response.SuccessEnvelope;
import gov.usgs.cida.gcmrcservices.mb.endpoint.response.GCMRCResponse;
import gov.usgs.cida.gcmrcservices.mb.model.AncillaryData;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.glassfish.jersey.server.JSONP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zmoore
 */
@Path("lookup")
public class LookupEndpoint {
	private static final Logger log = LoggerFactory.getLogger(LookupEndpoint.class);
	
	@GET
	@JSONP(queryParam="jsonp_callback")
	@Path("ancillary")
	@Produces("application/javascript")
	public GCMRCResponse getAncilliaryData() {
		GCMRCResponse result = null;
		List<AncillaryData> data = new ArrayList<AncillaryData>();
		
		try {
			data = new LookupDAO().getAncilliaryData();
		} catch (Exception e) {
			log.error("Could not get ancilliary data!", e);
		}
		
		result = new GCMRCResponse(new SuccessEnvelope<>(data));
		
		return result;
	}
}
