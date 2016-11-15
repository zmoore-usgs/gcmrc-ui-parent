/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.usgs.cida.gcmrcservices;

import java.util.List;
import javax.ws.rs.WebApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zmoore
 */
public class TSVUtil {
	private static final Logger log = LoggerFactory.getLogger(TSVUtil.class);
	
	public static String listToTSVRow(List data){
		StringBuilder outputBuilder = new StringBuilder();
		for(int i = 0; i < data.size(); i++){
			outputBuilder.append(data.get(i).toString());
			
			if(i < data.size() - 1)	{			
				outputBuilder.append("\t");
			}
		}		
		return outputBuilder.toString();
	}
	
	public static String createTSV(List<String> headers, List<List<Object>> dataColumns, int rowCount, boolean dropEmptyColumns, boolean ignoreEmptyValues){
		StringBuilder outputBuilder = new StringBuilder();
				
		//Verify Data
		//Same number of columns and headers
		if(headers.size() != dataColumns.size()){
			log.error("Error: " + (headers.size() > dataColumns.size() ? "More" : "Fewer") + " headers than data columns.");
			throw new WebApplicationException(500);
		}
		
		//Clean Missing Data
		for(int i = 0; i < dataColumns.size(); i++){
			if(dataColumns.get(i).size() != rowCount && (dataColumns.get(i).size() > 0 || !dropEmptyColumns) && !ignoreEmptyValues){
				log.error("Error: Data columns are not all the same length.");
				throw new WebApplicationException(500);
			} else if(dataColumns.get(i).isEmpty() && dropEmptyColumns) {
				dataColumns.remove(i);
				headers.remove(i);
				i--;
			} else if(ignoreEmptyValues) {
				for(int j = dataColumns.get(i).size(); j < rowCount; j++){
					dataColumns.get(i).add("");
				}
			}
		}
		
		//Output Data
		//Headers
		outputBuilder.append(listToTSVRow(headers));
		outputBuilder.append("\n");
		
		//Data Rows
		for(int i = 0; i < rowCount; i++){
			for(int j = 0; j < dataColumns.size(); j++){
				outputBuilder.append(dataColumns.get(j).get(i));
				
				if(j < dataColumns.size() -1){
					outputBuilder.append("\t");
				}
			}
			
			if(i < rowCount - 1){
				outputBuilder.append("\n");
			}
		}
				
		return outputBuilder.toString();
	}
}
