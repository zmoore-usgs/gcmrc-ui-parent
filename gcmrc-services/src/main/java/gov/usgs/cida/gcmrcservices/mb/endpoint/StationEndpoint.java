package gov.usgs.cida.gcmrcservices.mb.endpoint;

import gov.usgs.cida.gcmrcservices.mb.dao.StationDAO;
import gov.usgs.cida.gcmrcservices.mb.endpoint.response.SuccessEnvelope;
import gov.usgs.cida.gcmrcservices.mb.endpoint.response.GCMRCResponse;
import gov.usgs.cida.gcmrcservices.mb.model.StationCredits;
import gov.usgs.cida.gcmrcservices.mb.model.StationParam;
import gov.usgs.cida.gcmrcservices.mb.model.StationPubs;
import gov.usgs.cida.gcmrcservices.mb.model.StationSite;

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
		List<StationSite> sites = new ArrayList<StationSite>();
		
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
		List<StationSite> allSites = new ArrayList<StationSite>();
		
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
		List<StationPubs> pubs = new ArrayList<StationPubs>();
		
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
		List<StationCredits> credits = new ArrayList<StationCredits>();
		
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
		List<StationParam> params = new ArrayList<StationParam>();
		
		try {
			params = new StationDAO().getSiteParams(site);
		} catch (Exception e) {
			log.error("Could not get site parameters!", e);
		}
		
		result = new GCMRCResponse(new SuccessEnvelope<>(params));
		
		return result;
	}
}
