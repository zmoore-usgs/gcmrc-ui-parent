package gov.usgs.cida.gcmrcservices.nude.time;

import gov.usgs.cida.gcmrcservices.TimeUtil;
import gov.usgs.cida.nude.column.Column;
import gov.usgs.cida.nude.filter.ColumnTransform;
import gov.usgs.cida.nude.resultset.inmemory.TableRow;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class OutOfMillisTransform implements ColumnTransform {
	private static final Logger log = LoggerFactory.getLogger(OutOfMillisTransform.class);
	
	protected final Column time;

	public OutOfMillisTransform(Column timeColumn) {
		this.time = timeColumn;
	}
	
	@Override
	public String transform(TableRow row) {
		String result = null;

		String val = row.getValue(time);
		if (null != val) {
			try {
				long d = Long.parseLong(val);
				result = TimeUtil.DB_DATE_FORMAT.withZone(DateTimeZone.forOffsetHours(-7)).print(d);
			} catch (Exception e) {
				log.trace("Could not parse " + val);
			}
			
		}

		return result;
	}

}
