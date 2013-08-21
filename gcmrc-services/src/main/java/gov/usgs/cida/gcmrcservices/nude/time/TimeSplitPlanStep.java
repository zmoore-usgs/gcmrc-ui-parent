package gov.usgs.cida.gcmrcservices.nude.time;

import gov.usgs.cida.nude.column.Column;
import gov.usgs.cida.nude.column.ColumnGrouping;
import gov.usgs.cida.nude.column.SimpleColumn;
import gov.usgs.cida.nude.filter.ColumnTransform;
import gov.usgs.cida.nude.filter.FilterStageBuilder;
import gov.usgs.cida.nude.filter.FilterStep;
import gov.usgs.cida.nude.filter.NudeFilterBuilder;
import gov.usgs.cida.nude.plan.PlanStep;
import gov.usgs.cida.nude.resultset.inmemory.TableRow;
import java.sql.ResultSet;
import java.util.List;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class TimeSplitPlanStep implements PlanStep {
	private static final Logger log = LoggerFactory.getLogger(TimeSplitPlanStep.class);
	
	protected final FilterStep fs;

	public TimeSplitPlanStep(final ColumnGrouping inCols, List<TimeColumnReq> tcrs) {
		
		NudeFilterBuilder nfb = new NudeFilterBuilder(inCols);
		FilterStageBuilder fsb = new FilterStageBuilder(nfb.getCurrOutCols());
		
		if (null != tcrs) {
			for (TimeColumnReq tcr : tcrs) {
				fsb.addTransform(
						new SimpleColumn(inCols.getPrimaryKey().getName() + tcr.getFormat().hashCode()), 
						new TimeFormattingTransform(inCols.getPrimaryKey(), tcr.getFormatter())
						);
			}
		}
		
		
		this.fs = new FilterStep(nfb.addFilterStage(fsb.buildFilterStage()).buildFilter());
	}
	
	protected static class TimeFormattingTransform implements ColumnTransform {
		
		protected final Column inCol;
		protected final DateTimeFormatter dtf;

		public TimeFormattingTransform(Column inCol, DateTimeFormatter dtf) {
			this.inCol = inCol;
			this.dtf = dtf;
		}

		@Override
		public String transform(TableRow row) {
			String result = null;

			String val = row.getValue(inCol);
			if (null != val) {
				result = dtf.print(Long.parseLong(val));
			}

			return result;
		}
	}
	
	@Override
	public ResultSet runStep(ResultSet input) {
		return this.fs.runStep(input);
	}

	@Override
	public ColumnGrouping getExpectedColumns() {
		return this.fs.getExpectedColumns();
	}
}
