/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.usgs.cida.gcmrcservices.nude.time;

import gov.usgs.cida.nude.column.Column;
import gov.usgs.cida.nude.column.ColumnGrouping;
import gov.usgs.cida.nude.plan.PlanStep;
import java.sql.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class CutoffTimesPlanStep implements PlanStep {
	private static final Logger log = LoggerFactory.getLogger(CutoffTimesPlanStep.class);
	
	protected final Column timeColumn;
	protected final ColumnGrouping outColumns;
	protected final TimeConfig timeConfig;

	public CutoffTimesPlanStep(Column timeColumn, ColumnGrouping outColumns, TimeConfig timeConfig) {
		this.timeColumn = timeColumn;
		this.outColumns = outColumns;
		this.timeConfig = timeConfig;
	}
	
	@Override
	public ResultSet runStep(ResultSet rs) {
		ResultSet result = null;
		
		if (null != rs) {
			result = new CutoffTimesResultSet(rs, timeColumn, outColumns, timeConfig);
		}
		
		return result;
	}

	@Override
	public ColumnGrouping getExpectedColumns() {
		return this.outColumns;
	}

}
