package gov.usgs.cida.gcmrcservices.mb.endpoint.response;

/**
 *
 * @author zmoore
 */
public class FailureEnvelope {	
	public int errorCode;
	public String error;
	
	public FailureEnvelope(String error, int code) {
		this.error = error;
		this.errorCode = code;
	}
		
	public String getError() {
		return this.error;
	}
	
	public int getErrorCode() {
		return this.errorCode;
	}
}
