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
	public SuccessResponse<DurationCurvePoint> getDurationCurve(@QueryParam("siteId") int siteId, @QueryParam("startTime") String startTime, @QueryParam("endTime") String endTime, @QueryParam("binCount") int binCount, @QueryParam("groupId") int groupId) {
		SuccessResponse<DurationCurvePoint> result = null;
		List<DurationCurvePoint> durationCurve = new ArrayList<>();
		
		if(binCount > MAX_BINS){
			log.error("Too many bins: ", binCount);
		} else {
			try {
				durationCurve = new DurationCurveDAO().getDurationCurve(siteId, startTime, endTime, binCount, groupId);
			} catch (Exception e) {
				log.error("Could not get duration curve!", e);
			}
		}
		
		result = new SuccessResponse<>(new ResponseEnvelope<>(durationCurve));
		
		return result;
	}
}
