package gov.usgs.cida.gcmrcservices.mb.endpoint.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zmoore
 */
public class GCMRCResponse {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(GCMRCResponse.class);
	
	private final SuccessEnvelope success;
	private final FailureEnvelope failure;
	private final String status;

	public GCMRCResponse(SuccessEnvelope success) {
		this.success = success;
		this.failure = null;
		this.status = "success";
	}
	
	public GCMRCResponse(FailureEnvelope failure) {
		this.failure = failure;
		this.success = null;
		this.status = "failure";
	}
	
	public GCMRCResponse(SuccessEnvelope success, FailureEnvelope faliure) {
		this.success = success;
		this.failure = faliure;
		this.status = "mixed";
	}

	public SuccessEnvelope getSuccess() {
		return this.success;
	}
	
	public FailureEnvelope getFailure() {
		return this.failure;
	}
	
	public String getStatus() {
		return this.status;
	}
}
