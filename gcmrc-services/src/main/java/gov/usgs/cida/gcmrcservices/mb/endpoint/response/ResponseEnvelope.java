package gov.usgs.cida.gcmrcservices.mb.endpoint.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley, zmoore
 */
public abstract class ResponseEnvelope<T> {
	protected static final Logger log = LoggerFactory.getLogger(ResponseEnvelope.class);
	protected T data;
	protected Boolean successful;
	
	public ResponseEnvelope(T data, Boolean successful){
		this.data = data;
		this.successful = successful;
	}
	
	public T getData() {
		return this.data;
	}
}
