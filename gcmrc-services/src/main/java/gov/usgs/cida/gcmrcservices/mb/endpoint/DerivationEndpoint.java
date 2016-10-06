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
@Path("derivation")
public class DerivationEndpoint {
	private static final Logger log = LoggerFactory.getLogger(DerivationEndpoint.class);
	
	@GET
	@JSONP(queryParam="jsonp_callback")
	@Path("durationCurve")
	@Produces("application/javascript")
	public SuccessResponse<DurationCurvePoint> getDurationCurve(@QueryParam("siteId") String siteId, @QueryParam("startTime") String startTime, @QueryParam("endTime") String endTime, @QueryParam("binCount") String binCount, @QueryParam("groupId") String groupId) {
		SuccessResponse<DurationCurvePoint> result = null;
		List<DurationCurvePoint> durationCurve = new ArrayList<>();
				
		try {
			durationCurve = new DurationCurveDAO().getDurationCurve(siteId, startTime, endTime, binCount, groupId);
		} catch (Exception e) {
			log.error("Could not get duration curve!", e);
		}
		
		result = new SuccessResponse<>(new ResponseEnvelope<>(durationCurve));
		
		return result;
	}
}
