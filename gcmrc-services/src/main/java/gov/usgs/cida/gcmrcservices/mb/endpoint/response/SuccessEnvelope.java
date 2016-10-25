package gov.usgs.cida.gcmrcservices.mb.endpoint.response;

import java.util.List;

/**
 *
 * @author dmsibley, zmoore
 */
public class SuccessEnvelope<T> extends ResponseEnvelope {	
	private final int rowCount;
	
	public SuccessEnvelope(List<T> data) {
		super(data, true);
		Integer count = -1;
		if (null != data) {
			count = data.size();
		}
		this.rowCount = count;
	}
	
	@Override
	public List<T> getData() {
		return (List)data;
	}

	public Integer getRowCount() {
		return rowCount;
	}
}
