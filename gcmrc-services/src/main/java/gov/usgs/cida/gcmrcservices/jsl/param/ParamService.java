package gov.usgs.cida.gcmrcservices.jsl.param;

import gov.usgs.cida.gcmrcservices.jsl.station.StationBSSpec;
import gov.usgs.cida.gcmrcservices.jsl.station.StationParamSpec;
import gov.usgs.cida.gcmrcservices.jsl.station.StationQWSpec;
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
public class ParamService extends WebService {
	private static final Logger log = LoggerFactory.getLogger(ParamService.class);

	public ParamService() {
		this.specMapping.put("default", StationParamSpec.class);
		this.specMapping.put("param", StationParamSpec.class);
		this.specMapping.put("bs", StationBSSpec.class);
		this.specMapping.put("qw", StationQWSpec.class);
	}

	@Override
	protected Map<String, String[]> defineParameters(HttpServletRequest req, UriRouter router, Map<String, String[]> params) throws InvalidServiceException {
		Map<String, String[]> result = new HashMap<String, String[]>();
		
		result.putAll(super.defineParameters(req, router, params));
		
		if (!result.containsKey(Spec.ORDER_BY_PARAM)) {
			result.put(Spec.ORDER_BY_PARAM, new String[] {StationParamSpec.S_DISPLAY_ORDER});
		}
		
		return result;
	}
	
	
}
