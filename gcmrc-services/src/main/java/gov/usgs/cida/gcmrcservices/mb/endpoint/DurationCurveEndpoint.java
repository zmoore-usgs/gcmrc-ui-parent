package gov.usgs.cida.gcmrcservices.mb.endpoint;

import gov.usgs.cida.gcmrcservices.mb.dao.DurationCurveDAO;
import gov.usgs.cida.gcmrcservices.mb.endpoint.response.ResponseEnvelope;
import gov.usgs.cida.gcmrcservices.mb.endpoint.response.SuccessResponse;
import gov.usgs.cida.gcmrcservices.mb.model.DurationCurve;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.glassfish.jersey.server.JSONP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley, zmoore
 */
@Path("durationcurve")
public class DurationCurveEndpoint {
	private static final Logger log = LoggerFactory.getLogger(DurationCurveEndpoint.class);
	private final int MAX_BINS = 2000;
	
	@GET
	@JSONP(queryParam="jsonp_callback")
	@Produces("application/javascript")
	public SuccessResponse<DurationCurve> getDurationCurve(@QueryParam("siteId") int siteId, @QueryParam("startTime") String startTime, @QueryParam("endTime") String endTime, @QueryParam("binCount") int binCount, @QueryParam("binType") String binType, @QueryParam(value = "groupId[]") final List<Integer> groupIds) {
		SuccessResponse<DurationCurve> result = null;
		List<DurationCurve> durationCurves = new ArrayList<>();
		
		if(binCount > MAX_BINS){
			log.error("Too many bins: " + binCount + " (Max: " + MAX_BINS + ")");
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).type("text/plain").entity("Too many bins: " + binCount + " (Max: " + MAX_BINS + ")").build());
		}
		
		if(binType!= null && binType.equalsIgnoreCase("log")){
			binType = "LOG_BINS";
		} else if(binType!= null && binType.equalsIgnoreCase("lin")){
			binType = "LIN_BINS";
		} else {
			log.error("Invalid bin type: '" + binType + "' (Valid: 'lin' or 'log')");
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).type("text/plain").entity("Invalid bin type: '" + binType + "' (Valid: 'lin' or 'log')").build());
		}
		
		System.out.println("Here!");
		System.out.println(groupIds.toArray().length);
		
		for(int groupId : groupIds){
			try {
				durationCurves.add(new DurationCurve(new DurationCurveDAO().getDurationCurve(siteId, startTime, endTime, groupId, binCount, binType), groupId));
			} catch (Exception e) {
				log.error("Could not get duration curve for groupId: " + groupId, e);
				throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).type("text/plain").entity("Unable to get duration curve for the specified parameters (failure on groupId: " + groupId + ".\n\nError: " + e.getMessage()).build());
			}
		}		
		
		result = new SuccessResponse<>(new ResponseEnvelope<>(durationCurves));
		
		return result;
	}
}
