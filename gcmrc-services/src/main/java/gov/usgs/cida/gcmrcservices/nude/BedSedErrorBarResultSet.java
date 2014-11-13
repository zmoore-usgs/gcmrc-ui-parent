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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BedSedErrorBarResultSet extends PeekingResultSet {
	private static final Logger LOGGER = LoggerFactory.getLogger(BedSedErrorBarResultSet.class);
	
	protected final ResultSet in;
	protected final Column valueColumn;
	protected final Column conf95Column;
	
	public BedSedErrorBarResultSet(ResultSet in, ColumnGrouping colGroup, Column valueColumn, Column conf95Column) {
		this.in = in;
		this.columns = colGroup;
		this.valueColumn = valueColumn;
		this.conf95Column = conf95Column;
	}
	
	@Override
	protected void addNextRow() throws SQLException {
		LinkedList<TableRow> result = new LinkedList<>();
		while(0 >= result.size() && in.next() && !in.isAfterLast()) {
			TableRow rowIn = TableRow.buildTableRow(in);
			TableRow rowOut = buildRowOut(rowIn, valueColumn, conf95Column);
			result.add(rowOut);
		}
		this.nextRows.addAll(result);
	}
	
	public static TableRow buildRowOut(TableRow rowIn, Column valueColumn, Column conf95Column) {
		TableRow result = null;
		ColumnGrouping inColGroup = rowIn.getColumns();
		Map<Column, String> modMap = new HashMap<>();
		for (Column col : inColGroup) {
			modMap.put(col, rowIn.getValue(col));
		}
	
		//Calculate upper and lower 95% limits
		try {
			BigDecimal avgSizeValue = null;
			if (null != modMap.get(valueColumn)) {
				avgSizeValue = new BigDecimal(modMap.get(valueColumn));		
			}
			BigDecimal conf95Value = null;
			if (null != modMap.get(conf95Column)) {
				conf95Value = new BigDecimal(modMap.get(conf95Column));
			}
			BigDecimal lowerLimitValue = avgSizeValue.subtract(conf95Value, new MathContext(conf95Value.precision(), RoundingMode.HALF_EVEN));
			//zero out if negative
			if (lowerLimitValue.compareTo(BigDecimal.ZERO) < 0) {
				lowerLimitValue = BigDecimal.ZERO;
			}
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
			
			result = new TableRow(inColGroup, modMap);
			
		} catch (Exception e) {
			LOGGER.trace("could not calculate upper and lower 95% limits");
		}
		
		return result;
	}
	
	@Override
	public String getCursorName() throws SQLException {
		return this.in.getCursorName();
	}

}
