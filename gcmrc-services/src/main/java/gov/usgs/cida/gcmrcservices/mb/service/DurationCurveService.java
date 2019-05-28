package gov.usgs.cida.gcmrcservices.mb.service;

import gov.usgs.cida.gcmrcservices.TSVUtil;
import gov.usgs.cida.gcmrcservices.mb.dao.DurationCurveDAO;
import gov.usgs.cida.gcmrcservices.mb.model.DurationCurve;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zmoore
 */
public class DurationCurveService {
	private static final Logger log = LoggerFactory.getLogger(DurationCurveService.class);
	public static final int MAX_BINS = 10000;
	public static enum COLUMNS {BIN_NUMBER, BIN_VALUE, CUMULATIVE_BIN_PERC, IN_BIN_MINUTES, CUMULATIVE_IN_BIN_MINUTES, LOW_BOUND, HIGH_BOUND};
	public static final String[] COLUMN_HEADERS = {"Bin Number", "Bin Value", "Percentage of Time Equaled or Exceeded", "In Bin Minutes", "Cumulative In Bin Minutes", "Low Bound", "High Bound"};
	
	private static DurationCurve getDurationCurve(String siteName, String startTime, String endTime, int binCount, String binType, final Integer groupId){
		String binSQL;
		DurationCurve result;
				
		if(binCount > MAX_BINS){
			log.error("Too many bins: " + binCount + " (Max: " + MAX_BINS + ")");
			throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity("Too many bins: " + binCount + " (Max: " + MAX_BINS + ")").build());
		}
		
		if(binType!= null && binType.equalsIgnoreCase("log")){
			binSQL = "LOG_BINS";
		} else if(binType!= null && binType.equalsIgnoreCase("lin")){
			binSQL = "LIN_BINS";
		} else {
			log.error("Invalid bin type in final request: '" + binType + "' (Valid: 'lin' or 'log')");
			throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity("Invalid bin type in final request: '" + binType + "' (Valid: 'lin' or 'log')").build());
		}
		
		result = new DurationCurveDAO().getDurationCurve(siteName, startTime, endTime, groupId, binCount, binSQL);
		
		return result;
	}
	
	public static List<DurationCurve> getDurationCurves(String siteName, String startTime, String endTime, int binCount, String binType, final List<Integer> groupIds) {
		List<DurationCurve> durationCurves = new ArrayList<>();
		ArrayList<String> binTypes = new ArrayList<>();
		
		if(binCount > MAX_BINS){
			log.error("Too many bins: " + binCount + " (Max: " + MAX_BINS + ")");
			throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity("Too many bins: " + binCount + " (Max: " + MAX_BINS + ")").build());
		}
				
		if(binType!= null && binType.equalsIgnoreCase("log")){
			binTypes.add("log");
		} else if(binType!= null && binType.equalsIgnoreCase("lin")){
			binTypes.add("lin");
		} else if(binType != null && binType.equalsIgnoreCase("both")){
			binTypes.add("log");
			binTypes.add("lin");
		} else {
			log.error("Invalid requested bin type: '" + binType + "' (Valid: 'lin' or 'log' or 'both')");
			throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity("Invalid bin type: '" + binType + "' (Valid: 'lin' or 'log' or 'both')").build());
		}
				
		for(int groupId : groupIds){
			for(String selectedBinType : binTypes){
				durationCurves.add(getDurationCurve(siteName, startTime, endTime, binCount, selectedBinType, groupId));
			}
		}
		
		return durationCurves;
	}
	
	private static Pair<List<String>, List<List<String>>> getDurationCurveDownloadData(DurationCurve data, List<COLUMNS> outputColumns, String groupName){
		List<List<String>> columns = new ArrayList<>();
		List<String> headers = new ArrayList<>();
				
		//Build headers and extract relevant data
		for(int i = 0; i < outputColumns.size(); i++){
			headers.add(data.getSiteName() + " " + groupName + " (" + data.getBinType() + ") " + COLUMN_HEADERS[outputColumns.get(i).ordinal()]);
			
			switch(outputColumns.get(i)){
				case BIN_VALUE:
					columns.add(toStringList(data.extractBinValues()));
					break;
				case CUMULATIVE_BIN_PERC:
					columns.add(toStringList(data.extractCumulativeBinPercs()));
					break;
				case IN_BIN_MINUTES:
					columns.add(toStringList(data.extractInBinMinutes()));
					break;
				case CUMULATIVE_IN_BIN_MINUTES:
					columns.add(toStringList(data.extractCumulativeInBinMinutes()));
					break;
				case LOW_BOUND: 
					columns.add(toStringList(data.extractLowBounds()));
					break;
				case HIGH_BOUND:
					columns.add(toStringList(data.extractHighBounds()));
					break;
				default:
					break;
			}
		}
		
		//Build output
						
		return new ImmutablePair<List<String>, List<List<String>>>(headers, columns);
	}

	private static List<String> toStringList(List<Double> input) {
		List<String> result = new ArrayList<>();

		for(Double v : input) {
			result.add(v.toString());
		}

		return result;
	}
	
	public static String getTSVForDurationCurves(List<DurationCurve> data, List<COLUMNS> outputColumns, List<Integer> groupIds, List<String> groupNames, int binCount) {		
		List<List<String>> columns = new ArrayList<>();
		List<String> headers = new ArrayList<>();
		String output;
		
		//Get all necessary data
		for(int i = 0; i < data.size(); i++){
			String groupName = groupNames.get(groupIds.indexOf(data.get(i).getGroupId()));
			Pair<List<String>, List<List<String>>> result = getDurationCurveDownloadData(data.get(i), outputColumns, groupName);			
			columns.addAll(result.getRight());			
			headers.addAll(result.getLeft());
		}
						
		//Verify Data
		if(columns.size() > 0 && headers.size() > 0){
			output = TSVUtil.createTSV(headers, columns, binCount, false, true);
		} else {
			output = "NO DURATION CURVE DATA RETURNED FOR SPECIFIED PARAMETERS";
		}
				
		//Create TSV File
		return output;
	}
	
}
