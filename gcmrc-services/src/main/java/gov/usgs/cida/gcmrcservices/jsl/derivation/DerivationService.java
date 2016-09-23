package gov.usgs.cida.gcmrcservices.jsl.derivation;

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
public class DerivationService extends WebService {
	private static final Logger log = LoggerFactory.getLogger(DerivationService.class);

	public DerivationService() {
		this.specMapping.put("durationCurve", DurationCurveSpec.class);
	}

	@Override
	protected Map<String, String[]> defineParameters(HttpServletRequest req, UriRouter router, Map<String, String[]> params) throws InvalidServiceException {
		Map<String, String[]> result = new HashMap<String, String[]>();
		
		result.putAll(super.defineParameters(req, router, params));
		
		return result;
	}
	
	@Override
	protected void checkForValidParams(Spec spec) {
		//empty method to allow for query without search parameters
	}
	
}
