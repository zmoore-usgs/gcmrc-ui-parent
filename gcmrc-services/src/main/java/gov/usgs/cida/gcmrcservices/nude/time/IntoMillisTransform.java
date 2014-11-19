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
public class IntoMillisTransform implements ColumnTransform {
	private static final Logger log = LoggerFactory.getLogger(IntoMillisTransform.class);
	
	protected final Column time;

	public IntoMillisTransform(Column timeColumn) {
		this.time = timeColumn;
	}
	
	@Override
	public String transform(TableRow row) {
		String result = null;

		String val = row.getValue(time);
		if (null != val) {
			DateTime d = TimeUtil.DB_DATE_FORMAT.withZone(DateTimeZone.forOffsetHours(-7)).parseDateTime(val);
			result = "" + d.getMillis();
		}

		return result;
	}

}
