package gov.usgs.cida.gcmrcservices.mb.endpoint.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class SuccessResponse<T> {
	private static final Logger log = LoggerFactory.getLogger(SuccessResponse.class);
	
	private final ResponseEnvelope<T> success;

	public SuccessResponse(ResponseEnvelope<T> success) {
		this.success = success;
	}

	public ResponseEnvelope getSuccess() {
		return success;
	}
}
