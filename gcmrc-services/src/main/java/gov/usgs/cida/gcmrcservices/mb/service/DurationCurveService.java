/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.usgs.cida.gcmrcservices.mb.service;

import gov.usgs.cida.gcmrcservices.mb.dao.DurationCurveDAO;
import gov.usgs.cida.gcmrcservices.mb.model.DurationCurve;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zmoore
 */
public class DurationCurveService {
	private static final Logger log = LoggerFactory.getLogger(DurationCurveService.class);
	private static final int MAX_BINS = 2000;
	
	public static List<DurationCurve> getDurationCurves(int siteId, String startTime, String endTime, int binCount, String binType, final List<Integer> groupIds) {
		List<DurationCurve> durationCurves = new ArrayList<>();
		ArrayList<String> binTypes = new ArrayList<>();
		
		if(binCount > MAX_BINS){
			log.error("Too many bins: " + binCount + " (Max: " + MAX_BINS + ")");
			throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity("Too many bins: " + binCount + " (Max: " + MAX_BINS + ")").build());
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
			throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity("Invalid bin type: '" + binType + "' (Valid: 'lin' or 'log' or 'both')").build());
		}
				
		for(int groupId : groupIds){
			for(String selectedBinType : binTypes){
				try {
					durationCurves.add(new DurationCurve(new DurationCurveDAO().getDurationCurve(siteId, startTime, endTime, groupId, binCount, selectedBinType), groupId, selectedBinType));
				} catch (Exception e) {
					log.error("Could not get duration curve for groupId: " + groupId + " with binType: " + selectedBinType, e);
					throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).type("text/plain").entity("Unable to get duration curve for the specified parameters (failure on groupId: " + groupId + " with binType: " + selectedBinType + ".\n\nError: " + e.getMessage()).build());
				}
			}
		}
		
		return durationCurves;
	}
}
