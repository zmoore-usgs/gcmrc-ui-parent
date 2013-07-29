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
		
		result.append("(");
		result.append("  SELECT DISTINCT S.NWIS_SITE_NO SITE_NO,");
		result.append("    S.SITE_SHORT_NM SHORT_NM,");
		result.append("    S.SITE_NM FULL_NM,");
		result.append("    S.DEC_LAT_VA LAT,");
		result.append("    S.DEC_LONG_VA LON,");
		result.append("    S.DISPLAY_ORDER_VA DISPLAY_ORDER,");
		result.append("    CASE");
		result.append("      WHEN S.NETWORK_NM='GCDAMP'");
		result.append("      THEN 'GCDAMP'");
		result.append("      WHEN S.NETWORK_NM='Dinosaur'");
		result.append("      THEN 'DINO'");
		result.append("      WHEN S.NETWORK_NM='BigBend'");
		result.append("      THEN 'BIBE'");
		result.append("      ELSE 'GCDAMP'");
		result.append("    END AS NET");
		result.append("  FROM SITE S");
		result.append(") ALLSITES");
		
		return result.toString();
	}
}
