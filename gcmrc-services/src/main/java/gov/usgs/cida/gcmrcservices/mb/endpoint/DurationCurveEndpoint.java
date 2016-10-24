package gov.usgs.cida.gcmrcservices.mb.endpoint;

import gov.usgs.cida.gcmrcservices.mb.endpoint.response.ResponseEnvelope;
import gov.usgs.cida.gcmrcservices.mb.endpoint.response.SuccessResponse;
import gov.usgs.cida.gcmrcservices.mb.model.DurationCurve;
import gov.usgs.cida.gcmrcservices.mb.service.DurationCurveService;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
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
	
	@GET
	@JSONP(queryParam="jsonp_callback")
	@Produces("application/javascript")
	public SuccessResponse<DurationCurve> getDurationCurve(@QueryParam("siteId") int siteId, @QueryParam("startTime") String startTime, @QueryParam("endTime") String endTime, @QueryParam("binCount") int binCount, @QueryParam("binType") String binType, @QueryParam(value = "groupId[]") final List<Integer> groupIds) {
		SuccessResponse<DurationCurve> result = null;
		List<DurationCurve> durationCurves = new ArrayList<>();
		
		try {
			durationCurves = DurationCurveService.getDurationCurves(siteId, startTime, endTime, binCount, binType, groupIds);
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).type("text/plain").entity("Could not output file response. Error: " + e.getMessage()).build());
		}
		
		result = new SuccessResponse<>(new ResponseEnvelope<>(durationCurves));
		
		return result;
	}
	
	@Path("download")
	@GET
	@Produces("application/tsv")
	public void getDurationCurveDownload(@QueryParam("siteId") int siteId, @QueryParam("startTime") String startTime, @QueryParam("endTime") String endTime, @QueryParam("binCount") int binCount, @QueryParam("binType") String binType, @QueryParam(value = "groupId[]") final List<Integer> groupIds, @QueryParam(value = "groupName[]") final List<String> groupNames, @Context HttpServletResponse response) {										
		try {
			//Get Duration Cruve Data
			List<DurationCurve> durationCurves = DurationCurveService.getDurationCurves(siteId, startTime, endTime, binCount, binType, groupIds);
			
			//Create output file
			List<DurationCurveService.COLUMNS> outputColumns = Arrays.asList(DurationCurveService.COLUMNS.CUMULATIVE_BIN_PERC, DurationCurveService.COLUMNS.BIN_VALUE);
			String result = DurationCurveService.getTSVForDurationCurves(durationCurves, outputColumns, groupIds, groupNames, binCount);
			
			// Write the header line
			response.addHeader("Content-Disposition", "attachment; filename=GCMRC.tsv");
			response.addHeader("Pragma", "public");
			response.addHeader("Cache-Control", "max-age=0");
			response.setContentType("application/ms-excel"); 
			OutputStream out = response.getOutputStream();
			out.write(result.getBytes());
			out.flush();
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).type("text/plain").entity("Could not download duration curve data. Error: " + e.getMessage()).build());
		}
	}
}
