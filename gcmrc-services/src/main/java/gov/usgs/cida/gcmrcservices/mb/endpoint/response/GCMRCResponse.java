package gov.usgs.cida.gcmrcservices.mb.endpoint.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zmoore
 */
public class GCMRCResponse {
	private static final Logger log = LoggerFactory.getLogger(GCMRCResponse.class);
	
	private final ResponseEnvelope success;
	private final ResponseEnvelope failure;

	public GCMRCResponse(ResponseEnvelope response) {
		if(response.successful){
			this.success = response;
			this.failure = null;
		} else {
			this.failure = response;
			this.success = null;
		}
	}

	public ResponseEnvelope getSuccess() {
		return this.success;
	}
	
	public ResponseEnvelope getFailure() {
		return this.failure;
	}
}
