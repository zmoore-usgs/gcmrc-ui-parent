package gov.usgs.cida.gcmrcservices.nude;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.usgs.cida.gcmrcservices.nude.time.IntoMillisTransform;
import gov.usgs.cida.nude.column.Column;
import gov.usgs.cida.nude.column.ColumnGrouping;
import gov.usgs.cida.nude.column.SimpleColumn;
import gov.usgs.cida.nude.filter.FilterStageBuilder;
import gov.usgs.cida.nude.filter.NudeFilter;
import gov.usgs.cida.nude.filter.NudeFilterBuilder;
import gov.usgs.cida.nude.plan.PlanStep;
import gov.usgs.cida.nude.provider.sql.SQLProvider;
import gov.usgs.cida.nude.resultset.inmemory.MuxResultSet;
import gov.usgs.webservices.jdbc.routing.ActionType;
import gov.usgs.webservices.jdbc.spec.Spec;
import gov.usgs.webservices.jdbc.spec.SpecResponse;
import gov.usgs.webservices.jdbc.spec.mapping.ColumnMapping;

/**
 *
 * @author dmsibley
 */
public class DBConnectorPlanStep implements PlanStep {
	private static final Logger log = LoggerFactory.getLogger(DBConnectorPlanStep.class);

	protected final Column time;
	protected final Iterable<Spec> specs;
	protected final SQLProvider sqlProvider;
	protected final UUID requestId;
	
	public DBConnectorPlanStep(Column timeColumn, Iterable<Spec> specs, SQLProvider provider, UUID requestId) {
		this.time = timeColumn;
		this.specs = specs;
		this.sqlProvider = provider;
		this.requestId = requestId;
	}
	
	@Override
	public ResultSet runStep(ResultSet input) {
		ResultSet result = null;
				
		Connection con = null;
		try {
			con = sqlProvider.getHoldableConnection(requestId);
		} catch (Exception e) {
			log.trace("wah wahhhh.", e);
		}

		if (null != con) {
			List<ResultSet> results = new ArrayList<ResultSet>();
			
			for (Spec spec : specs) {
				SpecResponse sr = Spec.runSpecAction(spec, ActionType.read, con);

				NudeFilterBuilder nfb = new NudeFilterBuilder(buildColumnGroupingFromSpec(spec, time));
				NudeFilter nf = nfb.addFilterStage(new FilterStageBuilder(nfb.getCurrOutCols()).addTransform(time, new IntoMillisTransform(time))
						.buildFilterStage()).buildFilter();
		
				results.add(nf.filter(sr.rset));
			}
			
			result = new MuxResultSet(results);
		}

		return result;
	}

	@Override
	public ColumnGrouping getExpectedColumns() {
		ColumnGrouping result = null;
		List<ColumnGrouping> colGroups = new ArrayList<ColumnGrouping>();
		
		for (Spec spec : specs) {
			colGroups.add(buildColumnGroupingFromSpec(spec, time));
		}
		
		result = ColumnGrouping.join(colGroups);
		return result;
	}

	
	public static ColumnGrouping buildColumnGroupingFromSpec(Spec spec, Column primaryKey) {
		ColumnGrouping result = null;
		
		if (null != spec && null != primaryKey) {
			List<Column> cols = new ArrayList<Column>();
			
			ColumnMapping[] cm = spec.getCOLUMN_MAP();
			for (ColumnMapping c : cm) {
				cols.add(new SimpleColumn(c.getColumnName()));
			}
			
			result = new ColumnGrouping(primaryKey, cols);
		}
		
		return result;
	}
}
