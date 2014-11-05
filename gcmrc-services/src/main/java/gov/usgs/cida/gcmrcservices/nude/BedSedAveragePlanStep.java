package gov.usgs.cida.gcmrcservices.nude;

import gov.usgs.cida.nude.column.Column;
import gov.usgs.cida.nude.column.ColumnGrouping;
import gov.usgs.cida.nude.plan.PlanStep;
import java.sql.ResultSet;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class BedSedAveragePlanStep implements PlanStep {
	private static final Logger log = LoggerFactory.getLogger(BedSedAveragePlanStep.class);
	
	protected final ColumnGrouping colGroup;
	protected final Column timeColumn;
	protected final Column sampleSetColumn;
	protected final Column valueColumn;
	protected final Column sampleMassColumn;
	protected final Column errorColumn;
	protected final Column conf95Column;

	public BedSedAveragePlanStep(Column timeColumn, Column sampleSetColumn, Column valueColumn, Column sampleMassColumn, Column errorColumn, Column conf95Column) {
		this.timeColumn = timeColumn;
		this.sampleSetColumn = sampleSetColumn;
		this.valueColumn = valueColumn;
		this.sampleMassColumn = sampleMassColumn;
		this.errorColumn = errorColumn;
		this.conf95Column = conf95Column;
		this.colGroup = new ColumnGrouping(timeColumn, Arrays.asList(new Column[] {
			this.timeColumn,
			this.sampleSetColumn,
			this.valueColumn,
			this.sampleMassColumn,
			this.errorColumn,
			this.conf95Column
		}));
	}
	
	@Override
	public ResultSet runStep(ResultSet rs) {
		ResultSet result = null;
		
		if (null != rs) {
			result = new BedSedAverageResultSet(rs, colGroup, timeColumn, sampleSetColumn, valueColumn, sampleMassColumn, errorColumn, conf95Column);
		}
		
		return result;
	}

	@Override
	public ColumnGrouping getExpectedColumns() {
		return this.colGroup;
	}

}
