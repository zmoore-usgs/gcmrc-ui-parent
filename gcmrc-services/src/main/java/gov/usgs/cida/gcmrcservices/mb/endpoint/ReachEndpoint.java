package gov.usgs.cida.gcmrcservices.mb.endpoint;

import gov.usgs.cida.gcmrcservices.mb.dao.ReachDAO;
import gov.usgs.cida.gcmrcservices.mb.endpoint.response.SuccessEnvelope;
import gov.usgs.cida.gcmrcservices.mb.endpoint.response.GCMRCResponse;
import gov.usgs.cida.gcmrcservices.mb.model.Reach;
import gov.usgs.cida.gcmrcservices.mb.model.ReachDetail;
import gov.usgs.cida.gcmrcservices.mb.model.ReachPOR;
import gov.usgs.cida.gcmrcservices.mb.model.ReachTrib;
import gov.usgs.cida.gcmrcservices.mb.model.StationCredits;

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
 * @author dmsibley
 */
@Path("reach")
public class ReachEndpoint {
	private static final Logger log = LoggerFactory.getLogger(ReachEndpoint.class);
	
	@GET
	@JSONP(queryParam="jsonp_callback")
	@Path("{network}")
	@Produces("application/javascript")
	public GCMRCResponse getReaches(@PathParam("network") String network) {
		GCMRCResponse result = null;
		List<Reach> reaches = new ArrayList<Reach>();
		
		try {
			reaches = new ReachDAO().getReaches(network);
		} catch (Exception e) {
			log.error("Could not get reaches!", e);
		}
		
		result = new GCMRCResponse(new SuccessEnvelope<>(reaches));
		
		return result;
	}
	
	@GET
	@JSONP(queryParam="jsonp_callback")
	@Path("trib/{station}")
	@Produces("application/javascript")
	public GCMRCResponse getReachTrib(@PathParam("station") String majorTribSite) {
		GCMRCResponse result = null;
		List<ReachTrib> reachTrib = new ArrayList<ReachTrib>();
		
		try {
			reachTrib = new ReachDAO().getReachTrib(majorTribSite);
		} catch (Exception e) {
			log.error("Could not get reach trib!", e);
		}
		
		result = new GCMRCResponse(new SuccessEnvelope<>(reachTrib));
		
		return result;
	}
	
	@GET
	@JSONP(queryParam="jsonp_callback")
	@Path("por/{station}")
	@Produces("application/javascript")
	public GCMRCResponse getReachPOR(@PathParam("station") String upstreamStation) {
		GCMRCResponse result = null;
		List<ReachPOR> reachPOR = new ArrayList<ReachPOR>();
		
		try {
			reachPOR = new ReachDAO().getReachPOR(upstreamStation);
		} catch (Exception e) {
			log.error("Could not get reach period of record!", e);
		}
		
		result = new GCMRCResponse(new SuccessEnvelope<>(reachPOR));
		
		return result;
	}
	
	@GET
	@JSONP(queryParam="jsonp_callback")
	@Path("{network}/{upstream}/{downstream}")
	@Produces("application/javascript")
	public GCMRCResponse getReach(@PathParam("network") String network, @PathParam("upstream") String upstreamStation, @PathParam("downstream") String downstreamStation) {
		GCMRCResponse result = null;
		List<Reach> reaches = new ArrayList<Reach>();
		
		try {
			reaches = new ReachDAO().getReach(network, upstreamStation, downstreamStation);
		} catch (Exception e) {
			log.error("Could not get reaches!", e);
		}
		
		result = new GCMRCResponse(new SuccessEnvelope<>(reaches));
		
		return result;
	}
	
	@GET
	@JSONP(queryParam="jsonp_callback")
	@Path("detail/{network}/{upstream}/{downstream}")
	@Produces("application/javascript")
	public GCMRCResponse getReachDetails(@PathParam("network") String network, @PathParam("upstream") String upstreamStation, @PathParam("downstream") String downstreamStation) {
		GCMRCResponse result = null;
		List<ReachDetail> reachDetails = new ArrayList<>();
		
		try {
			reachDetails = new ReachDAO().getReachDetails(network, upstreamStation, downstreamStation);
		} catch (Exception e) {
			log.error("Could not get reach for network '" + network + "' upstream '" + upstreamStation + "' and downstream '" + downstreamStation, e);
		}
		
		result = new GCMRCResponse(new SuccessEnvelope<>(reachDetails));
		
		return result;
	}
	
	@GET
	@JSONP(queryParam="jsonp_callback")
	@Path("credit/{siteUp}/{siteDown}")
	@Produces("application/javascript")
	public GCMRCResponse getSiteCredits(@PathParam("siteUp") String siteUp, @PathParam("siteDown") String siteDown) {
		GCMRCResponse result = null;
		List<StationCredits> credits = new ArrayList<StationCredits>();
		
		try {
			credits = new ReachDAO().getSiteCredits(siteUp, siteDown);
		} catch (Exception e) {
			log.error("Could not get reach site credits!", e);
		}
		
		result = new GCMRCResponse(new SuccessEnvelope<>(credits));
		
		return result;
	}
}
