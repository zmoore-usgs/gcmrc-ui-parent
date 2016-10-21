package gov.usgs.cida.gcmrcservices.mb.endpoint;

import gov.usgs.cida.gcmrcservices.mb.dao.DurationCurveDAO;
import gov.usgs.cida.gcmrcservices.mb.endpoint.response.ResponseEnvelope;
import gov.usgs.cida.gcmrcservices.mb.endpoint.response.SuccessResponse;
import gov.usgs.cida.gcmrcservices.mb.model.DurationCurve;
import gov.usgs.cida.gcmrcservices.mb.model.DurationCurvePoint;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
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
	private final int MAX_BINS = 2000;
	
	@GET
	@JSONP(queryParam="jsonp_callback")
	@Produces("application/javascript")
	public SuccessResponse<DurationCurve> getDurationCurve(@QueryParam("siteId") int siteId, @QueryParam("startTime") String startTime, @QueryParam("endTime") String endTime, @QueryParam("binCount") int binCount, @QueryParam("binType") String binType, @QueryParam(value = "groupId[]") final List<Integer> groupIds) {
		SuccessResponse<DurationCurve> result = null;
		List<DurationCurve> durationCurves = new ArrayList<>();
		ArrayList<String> binTypes = new ArrayList<>();
		
		if(binCount > MAX_BINS){
			log.error("Too many bins: " + binCount + " (Max: " + MAX_BINS + ")");
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).type("text/plain").entity("Too many bins: " + binCount + " (Max: " + MAX_BINS + ")").build());
		}
		
		if(binType!= null && binType.equalsIgnoreCase("log")){
			binTypes.add("LOG_BINS");
		} else if(binType!= null && binType.equalsIgnoreCase("lin")){
			binTypes.add("LIN_BINS");
		} else if(binType != null && binType.equalsIgnoreCase("both")){
			binTypes.add("LOG_BINS");
			binTypes.add("LIN_BINS");
		} else {
			log.error("Invalid bin type: '" + binType + "' (Valid: 'lin' or 'log' or 'both')");
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).type("text/plain").entity("Invalid bin type: '" + binType + "' (Valid: 'lin' or 'log' or 'both')").build());
		}
				
		for(String selectedBinType : binTypes){
			for(int groupId : groupIds){
				try {
					durationCurves.add(new DurationCurve(new DurationCurveDAO().getDurationCurve(siteId, startTime, endTime, groupId, binCount, selectedBinType), groupId, selectedBinType));
				} catch (Exception e) {
					log.error("Could not get duration curve for groupId: " + groupId + " with binType: " + selectedBinType, e);
					throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).type("text/plain").entity("Unable to get duration curve for the specified parameters (failure on groupId: " + groupId + " with binType: " + selectedBinType + ".\n\nError: " + e.getMessage()).build());
				}
			}
		}
		
		result = new SuccessResponse<>(new ResponseEnvelope<>(durationCurves));
		
		return result;
	}
	
	@Path("download")
	@GET
	@Produces("application/tsv")
	public void getDurationCurveDownload(@QueryParam("siteId") int siteId, @QueryParam("startTime") String startTime, @QueryParam("endTime") String endTime, @QueryParam("binCount") int binCount, @QueryParam("binType") String binType, @QueryParam(value = "groupId[]") final List<Integer> groupIds, @QueryParam(value = "groupName[]") final List<String> groupNames, @Context HttpServletResponse response) {
		StringBuilder resultBuilder = new StringBuilder();
		List<DurationCurve> durationCurves = new ArrayList<>();
		ArrayList<String> binTypes = new ArrayList<>();
		ArrayList<ArrayList<Double> > outputDataRows = new ArrayList<>();
		
		if(binCount > MAX_BINS){
			log.error("Too many bins: " + binCount + " (Max: " + MAX_BINS + ")");
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).type("text/plain").entity("Too many bins: " + binCount + " (Max: " + MAX_BINS + ")").build());
		}		
		if(binType!= null && binType.equalsIgnoreCase("log")){
			binTypes.add("LOG_BINS");
		} else if(binType!= null && binType.equalsIgnoreCase("lin")){
			binTypes.add("LIN_BINS");
		} else if(binType != null && binType.equalsIgnoreCase("both")){
			binTypes.add("LOG_BINS");
			binTypes.add("LIN_BINS");
		} else {
			log.error("Invalid bin type: '" + binType + "' (Valid: 'lin' or 'log' or 'both')");
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).type("text/plain").entity("Invalid bin type: '" + binType + "' (Valid: 'lin' or 'log' or 'both')").build());
		}
		
		for(int groupId : groupIds){
			for(String selectedBinType : binTypes){
				try {
					durationCurves.add(new DurationCurve(new DurationCurveDAO().getDurationCurve(siteId, startTime, endTime, groupId, binCount, selectedBinType), groupId, selectedBinType));
					
					//Add Duration Curve Name to Header
					String name = groupNames.get(groupIds.indexOf(groupId));
					String binString = durationCurves.get(durationCurves.size()-1).getBinType();
					resultBuilder.append(siteId + " " + name + " (" + binString + ") " + "Percentage of Time Equaled or Exceeded\t");
					resultBuilder.append(siteId + " " + name + " (" + binString + ") " + "Daily Range in CFS\t");
				} catch (Exception e) {
					log.error("Could not get duration curve for groupId: " + groupId + " with binType: " + selectedBinType, e);
					throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).type("text/plain").entity("Unable to get duration curve for the specified parameters (failure on groupId: " + groupId + " with binType: " + selectedBinType + ".\n\nError: " + e.getMessage()).build());
				}
			}
		}
		
		//2. Build Data Rows		
		for(int i = 0; i < binCount; i++){
			outputDataRows.add(new ArrayList<Double>());
			
			for(int j = 0; j < durationCurves.size(); j++){
				outputDataRows.get(i).add(durationCurves.get(j).getPoints().get(i).getCumulativeBinPerc());
				outputDataRows.get(i).add(durationCurves.get(j).getPoints().get(i).getBinValue());
			}
		}
				
		//3. Output Data Rows
		for(int i = 0; i < outputDataRows.size(); i++){
			resultBuilder.append("\n");
			ArrayList<Double> row = outputDataRows.get(i);
			
			for(int j = 0; j < row.size(); j++){
				resultBuilder.append(row.get(j));
				resultBuilder.append("\t");
			}
		}
		
		response.addHeader("Content-Disposition", "attachment; filename=GCMRC.tsv");
		response.addHeader("Pragma", "public");
		response.addHeader("Cache-Control", "max-age=0");
		response.setContentType("application/ms-excel"); 
		
		try {
			// Write the header line
			OutputStream out = response.getOutputStream();
			out.write(resultBuilder.toString().getBytes());
			out.flush();
		} catch (Exception e) {
		   	log.error("Could not output file response. Error:", e);
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).type("text/plain").entity("Could not output file response. Error: " + e.getMessage()).build());
		}
	}
}
