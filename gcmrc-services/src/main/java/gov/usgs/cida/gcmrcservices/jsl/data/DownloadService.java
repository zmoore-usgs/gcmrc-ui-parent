package gov.usgs.cida.gcmrcservices.jsl.data;

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
public class DownloadService extends WebService {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(DownloadService.class);

	public DownloadService() {   //HACK  !!!! HOLY HACK BATMAN
		this.specMapping.put("samples", QWDownloadSpec.class);
		this.specMapping.put("GCDAMPsamples", QWDownloadSpec.class);
		this.specMapping.put("CLsamples", QWDownloadSpec.class);
		this.specMapping.put("BIBEsamples", CentralQWDownloadSpec.class);
		this.specMapping.put("CRDsamples", QWDownloadSpec.class);
		this.specMapping.put("DINOsamples", QWDownloadSpec.class);
		this.specMapping.put("CHIPsamples", CentralQWDownloadSpec.class);
		this.specMapping.put("URGsamples", QWDownloadSpec.class);
		this.specMapping.put("dischargeError", DischargeErrorSpec.class);
	}
	
	@Override
	protected Map<String, String[]> defineParameters(HttpServletRequest req, UriRouter router, Map<String, String[]> params) throws InvalidServiceException {
		Map<String, String[]> result = new HashMap<String, String[]>();
		
		result.putAll(super.defineParameters(req, router, params));
		
		if (!result.containsKey(Spec.ORDER_BY_PARAM)) {
			result.put(Spec.ORDER_BY_PARAM, new String[] {QWDownloadSpec.SE_DEFAULT_ORDERING+","+QWDownloadSpec.SE_SECONDARY_ORDERING});
		}
		
		return result;
	}
}
