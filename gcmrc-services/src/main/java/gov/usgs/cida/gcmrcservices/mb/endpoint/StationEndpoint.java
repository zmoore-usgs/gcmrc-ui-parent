package gov.usgs.cida.gcmrcservices.mb.endpoint;

import gov.usgs.cida.gcmrcservices.mb.dao.StationDAO;
import gov.usgs.cida.gcmrcservices.mb.endpoint.response.SuccessEnvelope;
import gov.usgs.cida.gcmrcservices.mb.endpoint.response.GCMRCResponse;

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
@Path("station")
public class StationEndpoint {
	private static final Logger log = LoggerFactory.getLogger(StationEndpoint.class);
	
	@GET
	@JSONP(queryParam="jsonp_callback")
	@Path("site/{network}")
	@Produces("application/javascript")
	public GCMRCResponse getSites(@PathParam("network") String network) {
		GCMRCResponse result = null;
		List<Object> sites = new ArrayList<Object>();
		
		try {
			sites = new StationDAO().getSites(network, "site");
		} catch (Exception e) {
			log.error("Could not get sites!", e);
		}
		
		result = new GCMRCResponse(new SuccessEnvelope<>(sites));
		
		return result;
	}
	
	@GET
	@JSONP(queryParam="jsonp_callback")
	@Path("allsite/{network}")
	@Produces("application/javascript")
	public GCMRCResponse getAllSites(@PathParam("network") String network) {
		GCMRCResponse result = null;
		List<Object> allSites = new ArrayList<Object>();
		
		try {
			allSites = new StationDAO().getSites(network, "allsite");
		} catch (Exception e) {
			log.error("Could not get all sites!", e);
		}
		
		result = new GCMRCResponse(new SuccessEnvelope<>(allSites));
		
		return result;
	}
	
	@GET
	@JSONP(queryParam="jsonp_callback")
	@Path("pubs/{site}")
	@Produces("application/javascript")
	public GCMRCResponse getSitePubs(@PathParam("site") String site) {
		GCMRCResponse result = null;
		List<Object> pubs = new ArrayList<Object>();
		
		try {
			pubs = new StationDAO().getSitePubs(site);
		} catch (Exception e) {
			log.error("Could not get sites!", e);
		}
		
		result = new GCMRCResponse(new SuccessEnvelope<>(pubs));
		
		return result;
	}
	
	@GET
	@JSONP(queryParam="jsonp_callback")
	@Path("credit/{site}")
	@Produces("application/javascript")
	public GCMRCResponse getSiteCredits(@PathParam("site") String site) {
		GCMRCResponse result = null;
		List<Object> credits = new ArrayList<Object>();
		
		try {
			credits = new StationDAO().getSiteCredits(site);
		} catch (Exception e) {
			log.error("Could not get site credits!", e);
		}
		
		result = new GCMRCResponse(new SuccessEnvelope<>(credits));
		
		return result;
	}
	
	@GET
	@JSONP(queryParam="jsonp_callback")
	@Path("param/{site}")
	@Produces("application/javascript")
	public GCMRCResponse getSiteParams(@PathParam("site") String site) {
		GCMRCResponse result = null;
		List<Object> params = new ArrayList<Object>();
		
		try {
			params = new StationDAO().getSiteParams(site);
		} catch (Exception e) {
			log.error("Could not get site parameters!", e);
		}
		
		result = new GCMRCResponse(new SuccessEnvelope<>(params));
		
		return result;
	}
}
