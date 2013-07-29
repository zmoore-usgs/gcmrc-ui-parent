/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.usgs.cida.gcmrcservices.jsl.lookup;

import gov.usgs.webservices.jdbc.service.WebService;
import gov.usgs.webservices.jdbc.spec.Spec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class LookupService extends WebService {
	private static final Logger log = LoggerFactory.getLogger(LookupService.class);

	public LookupService() {
		this.specMapping.put("samplemethod", SampMethSpec.class);
	}
	
	@Override
	protected void checkForValidParams(Spec spec) {
		//Nothin to do here
	}
	
	
}
