package gov.usgs.webservices.jdbc.spec;

import gov.usgs.webservices.jdbc.spec.value.Value;
import gov.usgs.webservices.jdbc.util.SqlUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
/**
 * Project subclass of Spec that provides additional logging.
 * 
 * @author eeverman
 *
 */
@SuppressWarnings("serial")
public abstract class GCMRCSpec extends Spec {
	private final static Logger log = Logger.getLogger(GCMRCSpec.class);
	
	public static final int DEFAULT_FETCH_SIZE = 1000;

	@Override
	protected ResultSet getMainResultSet(Connection con) throws SQLException {
		SqlString query = this.getQuery();
		
		int conId = con.hashCode();
		
		if (log.isDebugEnabled()) {
			log.debug("" + conId + " QUERY:" + this.getQuery());
			for (Value v: query.getValues()) {
				log.debug("" + conId + " ARG: " + v.toString());
			}
		}
		
		long start = System.currentTimeMillis();

		try {
			ResultSet result = new GCMRCResultSet(getQueryResults(this.getQuery(), con));
			long end = System.currentTimeMillis();

			log.debug("" + conId + " Total Time (Seconds): " + ((end - start) / 1000L));
			return result;
		} catch (Throwable t) {
			long end = System.currentTimeMillis();
			log.error("" + conId + " through an Exception rather than complete after " + ((end - start) / 1000L) + " seconds.", t);
			throw t;
		}
		

	}
	
		/**
	 * Executes a database query via a PreparedStatement
	 * 
	 * @param query The SqlString criteria for the query  
	 * @param con An open database connection object
	 * @return ResultSet object containing the results of the query 
	 * @throws SQLException
	 */
	public ResultSet getQueryResults(SqlString query, Connection con) throws SQLException {
		
		PreparedStatement statement = con.prepareStatement(query.getClause(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		statement.setFetchSize(DEFAULT_FETCH_SIZE);
		
		Value[] values = new Value[0];
		values = query.getValues().toArray(values);
		for (int i = 0; i < values.length; i++) {
			statement.setString(i+1, values[i].toString());
		}
		
		return statement.executeQuery();
	}

}