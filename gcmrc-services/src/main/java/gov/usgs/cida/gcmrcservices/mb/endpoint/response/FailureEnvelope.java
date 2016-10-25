package gov.usgs.cida.gcmrcservices.mb.endpoint.response;

/**
 *
 * @author zmoore
 */
public class FailureEnvelope extends ResponseEnvelope {	
	public int error;
	
	public FailureEnvelope(String error, int code) {
		super(error, false);
		this.error = code;
	}
	
	@Override
	public String getData() {
		return (String)data;
	}
	
	public int getError() {
		return this.error;
	}
}
