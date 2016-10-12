package gov.usgs.cida.gcmrcservices.mb.endpoint;

import gov.usgs.cida.gcmrcservices.mb.dao.DurationCurveDAO;
import gov.usgs.cida.gcmrcservices.mb.endpoint.response.ResponseEnvelope;
import gov.usgs.cida.gcmrcservices.mb.endpoint.response.SuccessResponse;
import gov.usgs.cida.gcmrcservices.mb.model.DurationCurvePoint;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
	public SuccessResponse<DurationCurvePoint> getDurationCurve(@QueryParam("siteId") int siteId, @QueryParam("startTime") String startTime, @QueryParam("endTime") String endTime, @QueryParam("groupId") int groupId, @QueryParam("binCount") int binCount, @QueryParam("binType") String binType) {
		SuccessResponse<DurationCurvePoint> result = null;
		List<DurationCurvePoint> durationCurve = new ArrayList<>();
		
		boolean validParams = true;
		
		if(binCount > MAX_BINS){
			log.error("Too many bins: " + binCount + " (Max: " + MAX_BINS + ")");
			validParams = false;
		}
		
		if(binType!= null && binType.compareTo("log") == 0){
			binType = "LOG_BINS";
		} else if(binType!= null && binType.compareTo("lin") == 0){
			binType = "LIN_BINS";
		} else {
			log.error("Invalid bin type: " + binType + " (Valid: 'lin' or 'log')");
			validParams = false;
		}
		
		if(validParams) {
			try {
				durationCurve = new DurationCurveDAO().getDurationCurve(siteId, startTime, endTime, groupId, binCount, binType);
			} catch (Exception e) {
				log.error("Could not get duration curve!", e);
			}
		}
		
		result = new SuccessResponse<>(new ResponseEnvelope<>(durationCurve));
		
		return result;
	}
}
