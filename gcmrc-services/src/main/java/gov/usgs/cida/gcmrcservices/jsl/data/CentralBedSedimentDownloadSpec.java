package gov.usgs.cida.gcmrcservices.jsl.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class CentralBedSedimentDownloadSpec extends BedSedimentDownloadSpec {
	private static final Logger log = LoggerFactory.getLogger(CentralBedSedimentDownloadSpec.class);

	@Override
	public String getTimezoneDisplay() {
		return "CST";
	}

	@Override
	public String getTimezoneSql() {
		return " + 1/24 ";
	}
}
