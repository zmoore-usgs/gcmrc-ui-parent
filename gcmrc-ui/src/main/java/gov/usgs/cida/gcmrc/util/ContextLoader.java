package gov.usgs.cida.gcmrc.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kmschoep
 */
public class ContextLoader {
	private static final Logger log = LoggerFactory.getLogger("stationview_jsp");
	
	public Context getContextProps() {
		Context envContext = null;
		
		try {
			Context context = new InitialContext();
			envContext = (Context) context.lookup("java:/comp/env");
		} catch (NamingException e) {
			log.error("Could not find JNDI");
		}
		return envContext;
	};
	
	public String getProp(Context envContext, String key, String defaultValue) {
		String value = getProp(envContext, key);
		return (value == null)? defaultValue: value;
	}
	
	public String getProp(Context envContext, String key) {
		String propValue = null;
		try {
			propValue = (String) envContext.lookup(key);
		} catch (NamingException e) {
			log.warn("Could not find key: " + key);
		}
		return propValue;
	}
}
