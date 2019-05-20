package gov.usgs.cida.gcmrcservices.mb.endpoint;

import gov.usgs.cida.gcmrcservices.mb.dao.StationDAO;
import gov.usgs.cida.gcmrcservices.mb.endpoint.response.SuccessEnvelope;
import gov.usgs.cida.gcmrcservices.mb.endpoint.response.GCMRCResponse;
import gov.usgs.cida.gcmrcservices.mb.model.StationBs;
import gov.usgs.cida.gcmrcservices.mb.model.StationQW;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.glassfish.jersey.server.JSONP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kmschoep
 */
@Path("param")
public class ParamEndpoint {
	private static final Logger log = LoggerFactory.getLogger(ParamEndpoint.class);
	
	@GET
	@JSONP(queryParam="jsonp_callback")
	@Path("qw/{site}")
	@Produces("application/javascript")
	public GCMRCResponse getSiteQW(@PathParam("site") String site) {
		GCMRCResponse result = null;
		List<StationQW> sites = new ArrayList<StationQW>();
		
		try {
			sites = new StationDAO().getSiteQW(site);
		} catch (Exception e) {
			log.error("Could not get site QW!", e);
		}
		
		result = new GCMRCResponse(new SuccessEnvelope<>(sites));
		
		return result;
	}
	
	@GET
	@JSONP(queryParam="jsonp_callback")
	@Path("bs/{site}")
	@Produces("application/javascript")
	public GCMRCResponse getSiteBs(@PathParam("site") String site) {
		GCMRCResponse result = null;
		List<StationBs> sites = new ArrayList<StationBs>();
		
		try {
			sites = new StationDAO().getSiteBs(site);
		} catch (Exception e) {
			log.error("Could not get site QW!", e);
		}
		
		result = new GCMRCResponse(new SuccessEnvelope<>(sites));
		
		return result;
	}
}
