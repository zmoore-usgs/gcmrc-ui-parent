/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.usgs.cida.gcmrcservices.nude.time;

import gov.usgs.cida.nude.column.Column;
import gov.usgs.cida.nude.column.ColumnGrouping;
import gov.usgs.cida.nude.resultset.inmemory.PeekingResultSet;
import gov.usgs.cida.nude.resultset.inmemory.TableRow;
import gov.usgs.cida.nude.time.DateRange;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class CutoffTimesResultSet extends PeekingResultSet {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(CutoffTimesResultSet.class);
	
	protected final ResultSet in;
	protected final Column timeColumn;
	protected final DateRange keepDateRange;

	public CutoffTimesResultSet(ResultSet in, Column timeColumn, ColumnGrouping outColumns, DateRange keepDateRange) {
		try {
			this.closed = in.isClosed();
		} catch (Exception e) {
			this.closed = true;
		}
		
		this.in = in;
		this.timeColumn = timeColumn;
		this.columns = outColumns;
		this.keepDateRange = keepDateRange;
	}
	
	@Override
	protected void addNextRow() throws SQLException {
		List<TableRow> results = new LinkedList<TableRow>();
		
		while (results.isEmpty() && !in.isClosed()) {
			if (!in.isClosed()) {
				if (in.next()) {
					boolean weGood = true;
					TableRow nextBuilt = TableRow.buildTableRow(in);
					DateTime nextTime = getPrimaryKey(nextBuilt);
					
					if (null != keepDateRange && null != keepDateRange.begin) {
						if (keepDateRange.begin.compareTo(nextTime) > 0) {
							weGood = false;
						}
					}
					if (null != keepDateRange && null != keepDateRange.end) {
						if (keepDateRange.end.compareTo(nextTime) < 0) {
							weGood = false;
						}
					}
					
					if (weGood) {
						results.add(nextBuilt);
					}
				} else {
					in.close();
				}
			}
		}
		
		this.nextRows.addAll(results);
	}
	
	//Terribly stolen from TimeSummaryFilteringResultSet. that we should extract that.
	public static DateTime getPrimaryKey(TableRow t) {
		DateTime result = null;
		
		if (null != t) {
			Column primaryKeyCol = t.getColumns().getPrimaryKey();
			String primKeyStr = t.getValue(primaryKeyCol);
			Long primKeyLong = new Long(primKeyStr);
			
			result = new DateTime(primKeyLong);
		}
		
		return result;
	}

	@Override
	public String getCursorName() throws SQLException {
		throwIfClosed(this);
		return in.getCursorName();
	}

}
