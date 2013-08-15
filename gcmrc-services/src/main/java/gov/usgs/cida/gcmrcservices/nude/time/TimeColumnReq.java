package gov.usgs.cida.gcmrcservices.nude.time;

import gov.usgs.cida.gcmrcservices.jsl.data.ParameterSpec;
import gov.usgs.cida.nude.out.mapping.ColumnToXmlMapping;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class TimeColumnReq {
	private static final Logger log = LoggerFactory.getLogger(TimeColumnReq.class);
	
	public static final String TIME_COLUMN_NAME = "time";
	
	protected final String displayName;

	public TimeColumnReq(String displayName) {
		this.displayName = displayName;
	}
	
	public ColumnToXmlMapping getMapping() {
		ColumnToXmlMapping result = null;
		
		result = new ColumnToXmlMapping(ParameterSpec.C_TSM_DT, displayName);
		
		return result;
	}
	
	public static TimeColumnReq parseTimeColumn(String colName, String displayName) {
		TimeColumnReq result = null;
		
		String cleanCol = StringUtils.trimToNull(colName);
		String cleanDisplayName = StringUtils.trimToNull(displayName);
		if (null == cleanDisplayName) {
			cleanDisplayName = TIME_COLUMN_NAME;
		}
		
		if (null != cleanCol && StringUtils.startsWithIgnoreCase(cleanCol, TIME_COLUMN_NAME)) {
			result = new TimeColumnReq(cleanDisplayName);
		} else {
			log.trace("This isn't a time column request: " + cleanCol);
		}
		
		return result;
	}
}
