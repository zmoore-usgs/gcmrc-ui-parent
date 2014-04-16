package gov.usgs.cida.gcmrcservices.mb.endpoint.response;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class ResponseEnvelope<T> {
	private static final Logger log = LoggerFactory.getLogger(ResponseEnvelope.class);
	
	private final Integer rowCount;
	private final List<T> data;

	public ResponseEnvelope(List<T> data) {
		this.rowCount = data.size();
		this.data = data;
	}

	public List<T> getData() {
		return data;
	}

	public Integer getRowCount() {
		return rowCount;
	}
	
}
