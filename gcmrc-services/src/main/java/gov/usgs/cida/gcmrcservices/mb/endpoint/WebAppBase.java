package gov.usgs.cida.gcmrcservices.mb.endpoint;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author dmsibley
 */
@ApplicationPath("rest")
public class WebAppBase extends ResourceConfig {
	
	public WebAppBase() {
		packages(this.getClass().getPackage().getName());
		register(JacksonFeature.class);
	}
}
