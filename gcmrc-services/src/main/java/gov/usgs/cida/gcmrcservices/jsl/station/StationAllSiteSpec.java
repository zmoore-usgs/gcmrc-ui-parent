package gov.usgs.cida.gcmrcservices.jsl.station;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class StationAllSiteSpec extends StationSiteSpec {
	private static final Logger log = LoggerFactory.getLogger(StationAllSiteSpec.class);

	@Override
	public String setupTableName() {
		StringBuilder result = new StringBuilder();
		
		result.append("(SELECT");
		result.append("  SITE_ID,");
		result.append("  CASE");
		result.append("    WHEN NWIS_SITE_NO IS NULL");
		result.append("    THEN SHORT_NAME");
		result.append("    ELSE NWIS_SITE_NO");
		result.append("  END AS SITE_NAME,");
		result.append("  NAME AS DISPLAY_NAME,");
		result.append("  DECIMAL_LATITUDE LAT,");
		result.append("  DECIMAL_LONGITUDE LON,");
		result.append("  CASE");
		result.append("    WHEN NETWORK_NAME='GCDAMP'");
		result.append("    THEN 'GCDAMP'");
		result.append("    WHEN NETWORK_NAME='Dinosaur'");
		result.append("    THEN 'DINO'");
		result.append("    WHEN NETWORK_NAME='BigBend'");
		result.append("    THEN 'BIBE'");
		result.append("    ELSE 'GCDAMP'");
		result.append("  END AS NET,");
		result.append("  DISPLAY_ORDER");
		result.append("  FROM");
		result.append("    SITE_STAR) T_A_MAIN");
		
		return result.toString();
	}
}
