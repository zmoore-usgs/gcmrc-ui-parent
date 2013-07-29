package gov.usgs.cida.gcmrcservices.nude.time;

import gov.usgs.cida.nude.time.DateRange;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class TimeConfig {
	private static final Logger log = LoggerFactory.getLogger(TimeConfig.class);

	protected final DateRange dateRange;
	protected final DateRange interp;
	protected final DateRange downscale;
	protected final DateTime cutoffBefore;
	protected final DateTime cutoffAfter;
	protected final int timezone;

	public TimeConfig(DateRange dateRange, DateRange interpDateRange, DateRange downscaleDateRange, DateTime cutoffBefore, DateTime cutoffAfter, int timezoneOffset) {
		this.dateRange = dateRange;
		this.interp = interpDateRange;
		this.downscale = downscaleDateRange;
		this.cutoffBefore = cutoffBefore;
		this.cutoffAfter = cutoffAfter;
		this.timezone = timezoneOffset;
	}

	public DateRange getDateRange() {
		return dateRange;
	}
	
	public DateRange getInterp() {
		return interp;
	}
	
	public DateRange getDownscale() {
		return downscale;
	}
	
	public DateTime getCutoffBefore() {
		return this.cutoffBefore;
	}
	
	public DateTime getCutoffAfter() {
		return this.cutoffAfter;
	}
	
	public int getTimezone() {
		return timezone;
	}
	
}
