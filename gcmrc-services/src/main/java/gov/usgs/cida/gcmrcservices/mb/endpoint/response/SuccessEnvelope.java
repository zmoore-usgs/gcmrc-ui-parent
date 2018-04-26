package gov.usgs.cida.gcmrcservices.mb.endpoint.response;

import java.util.List;

/**
 *
 * @author dmsibley, zmoore
 */
public class SuccessEnvelope<T> {	
	private final int rowCount;
	private final List<T> data;
	
	public SuccessEnvelope(List<T> data) {
		Integer count = -1;
		if (null != data) {
			count = data.size();
		}
		this.rowCount = count;
		this.data = data;
	}
	
	public List<T> getData() {
		return data;
	}

	public Integer getRowCount() {
		return rowCount;
	}
}
