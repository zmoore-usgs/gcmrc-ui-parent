package gov.usgs.cida.gcmrcservices.nude;

import gov.usgs.cida.nude.column.Column;
import gov.usgs.cida.nude.column.ColumnGrouping;
import gov.usgs.cida.nude.column.SimpleColumn;
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
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class BedSedAverageResultSet extends PeekingResultSet {
	private static final Logger log = LoggerFactory.getLogger(BedSedAverageResultSet.class);

	protected static final BigDecimal cutoffMassInGrams = new BigDecimal("20.000");
	protected static final SortedSet<SampleSetRule> rules = new TreeSet(Arrays.asList(new SampleSetRule[] {
		new SampleSetRule(1, 1),
		new SampleSetRule(3, 2),
		new SampleSetRule(4, 3)
	}));
	
	protected final ResultSet in;
	protected final LinkedList<TableRow> queuedRows;
	protected final Column timeColumn;
	protected final Column sampleSetColumn;
	protected final Column valueColumn;
	protected final Column sampleMassColumn;
	protected final Column errorColumn;
	protected final Column conf95Column;

	public BedSedAverageResultSet(ResultSet in, ColumnGrouping colGroup, 
			Column timeColumn, Column sampleSetColumn, Column valueColumn, Column sampleMassColumn, Column errorColumn, Column conf95Column) {
		this.in = in;
		this.columns = colGroup;
		this.queuedRows = new LinkedList<>();
		this.timeColumn = timeColumn;
		this.sampleSetColumn = sampleSetColumn;
		this.valueColumn = valueColumn;
		this.sampleMassColumn = sampleMassColumn;
		this.errorColumn = errorColumn;
		this.conf95Column = conf95Column;
	}
	
	@Override
	protected void addNextRow() throws SQLException {
		LinkedList<TableRow> result = new LinkedList<>();
		String sampleSet = null;
		
		while(0 >= result.size() && in.next() && !in.isAfterLast()) {
			TableRow now = TableRow.buildTableRow(in);
			queuedRows.add(now);
			if (null == sampleSet) {
				sampleSet = queuedRows.peek().getValue(sampleSetColumn);
			}
			
			if (null != sampleSet && 
					(queuedRows.size() > 1 && !sampleSet.equals(queuedRows.peekLast().getValue(sampleSetColumn)))) {
				TableRow averagedRow = flushNextSampleSet();
				if (null != averagedRow) {
					result.add(averagedRow);
				}
				sampleSet = null;
			}
		}
		
		if(0 >= result.size()) {
			while (in.isAfterLast() && queuedRows.size() > 0) {
				//If we're at the end and we have queued rows, flush
				TableRow averagedRow = flushNextSampleSet();
				if (null != averagedRow) {
					result.add(averagedRow);
				}
			}
		}
		
		
		this.nextRows.addAll(result);
	}
	
	protected TableRow flushNextSampleSet() {
		TableRow result = null;
		
		LinkedList<TableRow> groupedSampleSet = groupSampleSet(this.queuedRows, this.sampleSetColumn);
		String sampleSet = groupedSampleSet.peekFirst().getValue(this.sampleSetColumn);
		result = averageRow(groupedSampleSet, timeColumn, valueColumn, sampleMassColumn, errorColumn, conf95Column);
		dropSampleSet(this.queuedRows, sampleSet);
		
		return result;
	}
	
	public static LinkedList<TableRow> groupSampleSet(LinkedList<TableRow> queuedRows, Column sampleSetColumn) {
		LinkedList<TableRow> result = new LinkedList<>();
		
		if (null != queuedRows && !queuedRows.isEmpty()) {
			String sampleSet = queuedRows.peekFirst().getValue(sampleSetColumn);
			if (null == sampleSet) {
				result.add(queuedRows.pollFirst());
			} else {
				while (!queuedRows.isEmpty() && sampleSet.equals(queuedRows.peekFirst().getValue(sampleSetColumn))) {
					result.add(queuedRows.pollFirst());
				}
			}
		}
		
		return result;
	}
	
	public static TableRow averageRow(LinkedList<TableRow> groupedSampleSet, Column timeColumn,
			Column valueColumn, Column sampleMassColumn, Column errorColumn, Column conf95Column) {
		TableRow result = null;
		
		LinkedList<TableRow> validSamples = new LinkedList<>();
		for (TableRow sample : groupedSampleSet) {
			try {
				BigDecimal sampleMass = new BigDecimal(sample.getValue(sampleMassColumn));
				if (null != sample.getValue(valueColumn) && cutoffMassInGrams.compareTo(sampleMass) <= 0) {
					validSamples.add(sample);
				}
			} catch (Exception e) {
				log.trace("can't work with sample mass");
			}
		}
		
		int sampleSetSize = groupedSampleSet.size();
		boolean isValid = false;
		for (SampleSetRule rule : rules) {
			if ((!rule.equals(rules.last())
						&& sampleSetSize == rule.sampleSetSize
						&& validSamples.size() >= rule.minValidSamples)
					|| (rule.equals(rules.last())
						&& sampleSetSize >= rule.sampleSetSize
						&& validSamples.size() >= rule.minValidSamples)) {
				isValid = true;
			}
		}
		if (!isValid) {
			validSamples.clear();
		}
		
		if (0 < validSamples.size()) {
			ColumnGrouping inColGroup = validSamples.peek().getColumns();
			ColumnGrouping addedColGroup = new ColumnGrouping(inColGroup.getPrimaryKey(), Arrays.asList(new Column[] {
				inColGroup.getPrimaryKey(),
				errorColumn
			}));
			ColumnGrouping outColGroup = ColumnGrouping.join(Arrays.asList(new ColumnGrouping[] {inColGroup, addedColGroup}));
			ColumnGrouping cumulColGroup = new ColumnGrouping(timeColumn, Arrays.asList(new Column[] {
				timeColumn,
				valueColumn,
				sampleMassColumn
			}));
			Map<Column, String> modMap = new HashMap<>();
			
			String cuMeanColPrefix = "CUMEAN_";
			String lastCuMeanColPrefix = "LASTCUMEAN_";
			String stDevColPrefix = "STDEV_";
			String lastQColPrefix = "LASTQ_";
			String stErrColPrefix = "STERR_";
			String conf95ColPrefix = "CONF95_";
			int n = 0;
			for (TableRow sample : validSamples) {
				for (Column col : inColGroup) {
					modMap.put(col, sample.getValue(col));
				}
				n++;
				for (Column col : cumulColGroup) {
					//CUMULATIVE MOVING AVERAGE
					//http://en.wikipedia.org/wiki/Moving_average#Cumulative_moving_average
					Column lastCuMeanColumn = new SimpleColumn(lastCuMeanColPrefix + col.getName());
					Column cuMeanColumn = new SimpleColumn(cuMeanColPrefix + col.getName());
					String lastCumulativeValue = null;
					String cumulativeValue = modMap.get(cuMeanColumn);
					try {
						BigDecimal x1 = new BigDecimal(sample.getValue(col));
						BigDecimal cmaN = BigDecimal.ZERO;
						if (null != cumulativeValue) {
							cmaN = new BigDecimal(cumulativeValue);
						}
						lastCumulativeValue = cmaN.toPlainString();
						BigDecimal top = x1.subtract(cmaN);
						BigDecimal cmaN1 = cmaN.add(top.divide(new BigDecimal(n), RoundingMode.HALF_EVEN));
						cumulativeValue = cmaN1.toPlainString();
					} catch (Exception e) {
						log.trace("could not average value", e);
					}
					modMap.put(cuMeanColumn, cumulativeValue);
					modMap.put(lastCuMeanColumn, lastCumulativeValue);
				}
				
				//STANDARD DEVIATION
				//http://en.wikipedia.org/wiki/Standard_deviation#Corrected_sample_standard_deviation
				//http://en.wikipedia.org/wiki/Standard_deviation#Rapid_calculation_methods
				try {
					Column lastCuMeanColumn = new SimpleColumn(lastCuMeanColPrefix + valueColumn.getName());
					Column cuMeanColumn = new SimpleColumn(cuMeanColPrefix + valueColumn.getName());
					Column lastQColumn = new SimpleColumn(lastQColPrefix + valueColumn.getName());
					Column stdDevColumn = new SimpleColumn(stDevColPrefix + valueColumn.getName());
					
					BigDecimal lastCuMeanValue = BigDecimal.ZERO;
					if (null != modMap.get(lastCuMeanColumn)) {
						lastCuMeanValue = new BigDecimal(modMap.get(lastCuMeanColumn));
					}
					BigDecimal cuMeanValue = null;
					if (null != modMap.get(cuMeanColumn)) {
						cuMeanValue = new BigDecimal(modMap.get(cuMeanColumn));
					}
					BigDecimal lastQValue = BigDecimal.ZERO;
					if (null != modMap.get(lastQColumn)) {
						lastQValue = new BigDecimal(modMap.get(lastQColumn));
					}
					BigDecimal stdDevValue = null;
					if (null != modMap.get(stdDevColumn)) {
						stdDevValue = new BigDecimal(modMap.get(stdDevColumn));
					}
					BigDecimal currVal = null;
					if (null != sample.getValue(valueColumn)) {
						currVal = new BigDecimal(sample.getValue(valueColumn));
					}
					
					BigDecimal qValue = null;
					if (null != currVal) {
						MathContext mc = new MathContext(currVal.precision(), RoundingMode.HALF_EVEN);
						BigDecimal xAk1 = currVal.subtract(lastCuMeanValue);
						BigDecimal xAk = currVal.subtract(cuMeanValue);
						qValue = lastQValue.add(xAk1.multiply(xAk));
						BigDecimal sampleVariance = qValue.divide(new BigDecimal(n - 1), RoundingMode.HALF_EVEN);
						stdDevValue = new BigDecimal(Math.sqrt(sampleVariance.doubleValue()), mc);
					} else {
						log.error("BAD THINGS! We should never have a null value in this area!");
					}
					String qResult = null;
					if (null != qValue) {
						qResult = qValue.toPlainString();
					}
					modMap.put(lastQColumn, qResult);
					
					String stDevResult = null;
					if (null != stdDevValue) {
						stDevResult = stdDevValue.toPlainString();
					}
					modMap.put(stdDevColumn, stDevResult);
				} catch (Exception e) {
					log.trace("could not calculate Standard Deviation", e);
				}
				
				//STANDARD ERROR
				//http://en.wikipedia.org/wiki/Standard_error#Standard_error_of_the_mean
				try {
					Column stdDevColumn = new SimpleColumn(stDevColPrefix + valueColumn.getName());
					Column stdErrColumn = new SimpleColumn(stErrColPrefix + valueColumn.getName());
					
					BigDecimal stdDevValue = null;
					if (null != modMap.get(stdDevColumn)) {
						stdDevValue = new BigDecimal(modMap.get(stdDevColumn));
					}
					
					BigDecimal stdErrValue = stdDevValue.divide(new BigDecimal(Math.sqrt(n), new MathContext(stdDevValue.precision(), RoundingMode.HALF_EVEN)), RoundingMode.HALF_EVEN);
					
					String stdErrResult = null;
					if (null != stdErrValue) {
						stdErrResult = stdErrValue.toPlainString();
					}
					modMap.put(stdErrColumn, stdErrResult);
				} catch (Exception e) {
					log.trace("could not calculate Standard Error", e);
				}
				
				try {
					Column stdErrColumn = new SimpleColumn(stErrColPrefix + valueColumn.getName());
					Column conf95Col = new SimpleColumn(conf95ColPrefix + valueColumn.getName());
					
					BigDecimal stdErrValue = null;
					if (null != modMap.get(stdErrColumn)) {
						stdErrValue = new BigDecimal(modMap.get(stdErrColumn));
					}
					BigDecimal confidenceInterval = new BigDecimal(1.95, new MathContext(3, RoundingMode.HALF_EVEN));
					BigDecimal conf95Value = confidenceInterval.multiply(stdErrValue, new MathContext(confidenceInterval.precision(), RoundingMode.HALF_EVEN));
					
					String conf95Result = null;
					if (null != conf95Value) {
						conf95Result = conf95Value.toPlainString();
					}
					modMap.put(conf95Col, conf95Result);
				} catch (Exception e) {
					log.trace("could not calculate 95% confidence interval");
				}
			}
			
			for (Column col : outColGroup) {
				String val = modMap.get(new SimpleColumn(cuMeanColPrefix + col.getName()));
				if (null == val) {
					val = modMap.get(col);
				}
				modMap.put(col, val);
			}
			modMap.put(errorColumn, modMap.get(new SimpleColumn(stErrColPrefix + valueColumn.getName())));
			modMap.put(conf95Column, modMap.get(new SimpleColumn(conf95ColPrefix + valueColumn.getName())));
			
			result = new TableRow(outColGroup, modMap);
		}
		
		return result;
	}
	
	/**
	 * WARNING SIDE-EFFECTS
	 */
	private void dropSampleSet(LinkedList<TableRow> queuedRows, String sampleSet) {
		if (null != queuedRows && null != sampleSet) {
			while (queuedRows.size() > 0
					&& sampleSet.equals(queuedRows.peekFirst().getValue(sampleSetColumn))) {
				queuedRows.pollFirst();
			}
		}
	}
	
	@Override
	public String getCursorName() throws SQLException {
		return this.in.getCursorName();
	}
	
	public static final class SampleSetRule implements Comparable<SampleSetRule> {
		public final int sampleSetSize;
		public final int minValidSamples;

		public SampleSetRule(int sampleSetSize, int minValidSamples) {
			this.sampleSetSize = sampleSetSize;
			this.minValidSamples = minValidSamples;
		}

		@Override
		public int compareTo(SampleSetRule o) {
			return Integer.compare(this.sampleSetSize, o.sampleSetSize);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) { return false; }
			if (obj == this) { return true; }
			if (obj instanceof SampleSetRule) {
				SampleSetRule rhs = (SampleSetRule) obj;
				return new EqualsBuilder()
						.append(this.sampleSetSize, rhs.sampleSetSize)
						.append(this.minValidSamples, rhs.minValidSamples)
						.isEquals();
			}
			return false;
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder()
					.append(this.sampleSetSize)
					.append(this.minValidSamples)
					.toHashCode();
		}
	}
}
