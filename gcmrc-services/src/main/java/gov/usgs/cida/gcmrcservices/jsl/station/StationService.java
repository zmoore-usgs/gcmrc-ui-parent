package gov.usgs.cida.gcmrcservices.jsl.station;

import gov.usgs.cida.gcmrcservices.jsl.data.QWDownloadSpec;
import gov.usgs.webservices.jdbc.routing.InvalidServiceException;
import gov.usgs.webservices.jdbc.routing.UriRouter;
import gov.usgs.webservices.jdbc.service.WebService;
import gov.usgs.webservices.jdbc.spec.Spec;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class StationService extends WebService {

	public static final String S_DISPLAY_ORDER = "displayOrder";

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(StationService.class);

	public StationService() {
		this.specMapping.put("qwdownload", QWDownloadSpec.class);
	}

	@Override
	protected Map<String, String[]> defineParameters(HttpServletRequest req, UriRouter router, Map<String, String[]> params) throws InvalidServiceException {
		Map<String, String[]> result = new HashMap<String, String[]>();
		
		result.putAll(super.defineParameters(req, router, params));
		
		if (!result.containsKey(Spec.ORDER_BY_PARAM)) {
			result.put(Spec.ORDER_BY_PARAM, new String[] {S_DISPLAY_ORDER});
		}
		
		return result;
	}
	
	
}
