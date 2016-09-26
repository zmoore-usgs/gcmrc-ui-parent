package gov.usgs.cida.gcmrcservices.jsl.derivation;

import gov.usgs.webservices.jdbc.routing.InvalidServiceException;
import gov.usgs.webservices.jdbc.routing.UriRouter;
import gov.usgs.webservices.jdbc.service.WebService;
import gov.usgs.webservices.jdbc.spec.GCMRCSpec;
import gov.usgs.webservices.jdbc.spec.Spec;
import java.security.InvalidParameterException;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley, zmoore
 */
public class DerivationService extends WebService {
	private static final Logger log = LoggerFactory.getLogger(DerivationService.class);
	
	public static HashMap<String, String> parameters = new HashMap<String, String>();

	public DerivationService() {
		this.specMapping.put("durationCurve", DurationCurveSpec.class);
	}

	@Override
	protected Map<String, String[]> defineParameters(HttpServletRequest req, UriRouter router, Map<String, String[]> params) throws InvalidServiceException {
		Map<String, String[]> result = new HashMap<String, String[]>();
				
		result.putAll(super.defineParameters(req, router, params));
		
		parameters.clear();
		
		//Store params locally
		for(Map.Entry<String, String[]> entry : result.entrySet()){
			parameters.put(entry.getKey(), entry.getValue()[0]);
		}
		
		return result;
	}
	
	@Override
	protected void checkForValidParams(Spec spec) {		
		if(spec instanceof GCMRCSpec){
			for(String param : ((GCMRCSpec) spec).getRequiredParams()){
				if(!parameters.containsKey(param)){
					throw new InvalidParameterException("A required parameter was missing from the request.");
				}
			}
		}
	}
	
}
