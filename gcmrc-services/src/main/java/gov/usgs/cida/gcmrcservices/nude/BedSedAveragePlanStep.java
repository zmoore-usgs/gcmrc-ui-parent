package gov.usgs.cida.gcmrcservices.nude;

import gov.usgs.cida.nude.column.Column;
import gov.usgs.cida.nude.column.ColumnGrouping;
import gov.usgs.cida.nude.plan.PlanStep;
import java.sql.ResultSet;
import java.util.Set;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class BedSedAveragePlanStep implements PlanStep {
	private static final Logger log = LoggerFactory.getLogger(BedSedAveragePlanStep.class);
	
	protected final ColumnGrouping colGroup;

	public BedSedAveragePlanStep(ColumnGrouping colGroup) {
		this.colGroup = colGroup;
	}
	
	@Override
	public ResultSet runStep(ResultSet rs) {
		ResultSet result = null;
		
		if (null != rs) {
			result = new BedSedAverageResultSet(rs, colGroup);
		}
		
		return result;
	}

	@Override
	public ColumnGrouping getExpectedColumns() {
		return this.colGroup;
	}

}
