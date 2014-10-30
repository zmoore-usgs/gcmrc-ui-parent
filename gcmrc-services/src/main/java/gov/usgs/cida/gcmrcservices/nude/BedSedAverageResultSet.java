package gov.usgs.cida.gcmrcservices.nude;

import gov.usgs.cida.nude.column.ColumnGrouping;
import gov.usgs.cida.nude.resultset.inmemory.PeekingResultSet;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class BedSedAverageResultSet extends PeekingResultSet {
	private static final Logger log = LoggerFactory.getLogger(BedSedAverageResultSet.class);

	protected ResultSet in;
	
	public BedSedAverageResultSet(ResultSet incoming, ColumnGrouping colGroup) {
		this.columns = colGroup;
		this.in = incoming;
	}
	
	@Override
	protected void addNextRow() throws SQLException {
		
	}

	@Override
	public String getCursorName() throws SQLException {
		return this.in.getCursorName();
	}

	
}
