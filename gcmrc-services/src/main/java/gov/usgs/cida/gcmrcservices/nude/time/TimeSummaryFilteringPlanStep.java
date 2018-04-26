package gov.usgs.cida.gcmrcservices.nude.time;

import gov.usgs.cida.nude.column.Column;
import gov.usgs.cida.nude.column.ColumnGrouping;
import gov.usgs.cida.nude.plan.PlanStep;
import gov.usgs.cida.nude.time.DateRange;
import java.sql.ResultSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class TimeSummaryFilteringPlanStep implements PlanStep {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(TimeSummaryFilteringPlanStep.class);

	protected final ColumnGrouping colGroup;
	protected final DateRange timeRange;
	protected final Set<Column> exemptCols;

	public TimeSummaryFilteringPlanStep(ColumnGrouping colGroup, Set<Column> exemptColumns, DateRange timeRange) {
		this.colGroup = colGroup;
		this.timeRange = timeRange;
		this.exemptCols = exemptColumns;
	}

	@Override
	public ResultSet runStep(ResultSet input) {
		ResultSet result = null;

		if (null != input) {
			result = new TimeSummaryFilteringResultSet(input, colGroup, exemptCols, timeRange);
		}

		return result;
	}

	@Override
	public ColumnGrouping getExpectedColumns() {
		return this.colGroup;
	}
}
