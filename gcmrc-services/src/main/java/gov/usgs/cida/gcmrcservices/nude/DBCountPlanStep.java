package gov.usgs.cida.gcmrcservices.nude;

import gov.usgs.cida.nude.column.ColumnGrouping;
import gov.usgs.cida.nude.plan.PlanStep;
import java.sql.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class DBCountPlanStep implements PlanStep {
	private static final Logger log = LoggerFactory.getLogger(DBCountPlanStep.class);

	@Override
	public ResultSet runStep(ResultSet input) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ColumnGrouping getExpectedColumns() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
}
