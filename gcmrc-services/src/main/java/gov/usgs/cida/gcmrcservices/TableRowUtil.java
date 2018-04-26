package gov.usgs.cida.gcmrcservices;

import gov.usgs.cida.nude.column.Column;
import gov.usgs.cida.nude.resultset.inmemory.TableRow;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class TableRowUtil {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(TableRowUtil.class);
	
	public static TableRow replace(TableRow row, Column column, String value) {
		TableRow result = row;
		
		HashMap<Column, String> modMap = new HashMap<Column, String>();
		modMap.putAll(row.getMap());
		modMap.put(column, value);
		
		result = new TableRow(row.getColumns(), modMap);
		
		return result;
	}
}
