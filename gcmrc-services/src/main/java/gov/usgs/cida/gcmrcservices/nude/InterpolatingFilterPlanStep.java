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
public class InterpolatingFilterPlanStep implements PlanStep {
	private static final Logger log = LoggerFactory.getLogger(InterpolatingFilterPlanStep.class);
	
	protected final ColumnGrouping colGroup;
	protected final Period gapLimit;
	protected final Set<Column> exemptCols;
	protected final String exemptValueKeyword;

	public InterpolatingFilterPlanStep(ColumnGrouping colGroup, Set<Column> exemptColumns, String exemptValueKeyword, Period gapLimit) {
		this.colGroup = colGroup;
		this.gapLimit = gapLimit;
		this.exemptCols = exemptColumns;
		this.exemptValueKeyword = exemptValueKeyword;
	}
	
	@Override
	public ResultSet runStep(ResultSet rs) {
		ResultSet result = null;
		
		if (null != rs) {
			result = new InterpolatingFilterResultSet(rs, colGroup, exemptCols, exemptValueKeyword, gapLimit);
		}
		
		return result;
	}

	@Override
	public ColumnGrouping getExpectedColumns() {
		return this.colGroup;
	}

}
