package gov.usgs.cida.gcmrcservices.nude;

import gov.usgs.cida.nude.column.Column;
import gov.usgs.cida.nude.column.ColumnGrouping;
import gov.usgs.cida.nude.resultset.inmemory.PeekingResultSet;
import gov.usgs.cida.nude.resultset.inmemory.TableRow;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BedSedErrorBarResultSet extends PeekingResultSet {
	private static final Logger LOGGER = LoggerFactory.getLogger(BedSedErrorBarResultSet.class);
	
	protected final ResultSet in;
	protected final Column timeColumn;
	protected final Column valueColumn;
	
	public BedSedErrorBarResultSet(ResultSet in, ColumnGrouping colGroup, Column timeColumn, Column valueColumn) {
		this.in = in;
		this.columns = colGroup;
		this.timeColumn = timeColumn;
		this.valueColumn = valueColumn;
	}
	
	@Override
	protected void addNextRow() throws SQLException {
		LinkedList<TableRow> result = new LinkedList<>();
		//Spin through and transform all incoming result set rows 
		while(0 >= result.size() && in.next() && !in.isAfterLast()) {
			TableRow rowIn = TableRow.buildTableRow(in);
			TableRow rowOut = buildRowOut(rowIn, timeColumn, valueColumn);
			result.add(rowOut);
		}
		this.nextRows.addAll(result);
	}
	
	
	public static TableRow buildRowOut(TableRow rowIn, Column timeColumn, Column valueColumn) {
		TableRow result = null;
		ColumnGrouping inColGroup = rowIn.getColumns();
		ColumnGrouping outColGroup = new ColumnGrouping(timeColumn, Arrays.asList(new Column[] {
				timeColumn,
				valueColumn
			}));
		
		Map<Column, String> modMap = new HashMap<>();
	
		Column avgSize =  null;
		Column conf95 = null;
		for (Column col : inColGroup) {
			modMap.put(col, rowIn.getValue(col));
			if (col.getName().equalsIgnoreCase("avgSize")) { 
				avgSize = col ;
			}
			if (col.getName().equalsIgnoreCase("conf95")) { 
				conf95 = col ;
			}
		}
		
		//Calculate Upper and Lower 95% limit
		try {
			BigDecimal avgSizeValue = null;
			if (null != modMap.get(avgSize)) {
				avgSizeValue = new BigDecimal(modMap.get(avgSize));		}
			BigDecimal conf95Value = null;
			if (null != modMap.get(conf95)) {
				conf95Value = new BigDecimal(modMap.get(conf95));
			}
			BigDecimal lowerLimitValue = avgSizeValue.subtract(conf95Value, new MathContext(conf95Value.precision(), RoundingMode.HALF_EVEN));
			BigDecimal upperLimitValue = avgSizeValue.add(conf95Value, new MathContext(conf95Value.precision(), RoundingMode.HALF_EVEN));
			
			String lowerLimitResult = null;
			if (null != lowerLimitValue) {
				lowerLimitResult = lowerLimitValue.toPlainString();
			}
			
			String upperLimitResult = null;
			if (null != upperLimitValue) {
				upperLimitResult = upperLimitValue.toPlainString();
			}
			
			modMap.put(valueColumn, lowerLimitResult+":"+avgSizeValue.toPlainString()+":"+upperLimitResult);
			
			LOGGER.debug(""+modMap);
			
			result = new TableRow(outColGroup, modMap);
			
		} catch (Exception e) {
			LOGGER.trace("could not calculate upper and lower 95% limits");
		}
		
		LOGGER.debug(""+result);
		
		return result;
	}
	
	@Override
	public String getCursorName() throws SQLException {
		return this.in.getCursorName();
	}

}
