package gov.usgs.cida.gcmrcservices.nude;

import static gov.usgs.cida.gcmrcservices.ResultSetUtils.*;
import gov.usgs.cida.gcmrcservices.nude.InterpolatingFilterResultSet.Tuple;
import static gov.usgs.cida.gcmrcservices.nude.InterpolatingFilterResultSet.buildTuple;
import static gov.usgs.cida.gcmrcservices.nude.InterpolatingFilterResultSet.calcLinearInterp;
import gov.usgs.cida.nude.column.Column;
import gov.usgs.cida.nude.column.ColumnGrouping;
import gov.usgs.cida.nude.column.SimpleColumn;
import gov.usgs.cida.nude.resultset.inmemory.IteratorWrappingResultSet;
import gov.usgs.cida.nude.resultset.inmemory.TableRow;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import org.joda.time.Period;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class InterpolatingTest {
	private static final Logger log = LoggerFactory.getLogger(InterpolatingTest.class);

	public InterpolatingTest() {
	}
	
	@Test
	public void LinearInterpCalcTest() {
		Long x = new Long(2L);
		BigDecimal y = new BigDecimal("4.0");
		Tuple<Long, BigDecimal> xyA = buildTuple(new Long(1L), new BigDecimal(5));
		Tuple<Long, BigDecimal> xyB = buildTuple(new Long(3L), new BigDecimal(3));
		
		Tuple<Long, BigDecimal> expected = buildTuple(x, y);
		Tuple<Long, BigDecimal> actual = calcLinearInterp(xyA, x, xyB);
		assertEquals(expected, actual);
		
		xyA = buildTuple(new Long(ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T00:00:00-0700").getMillis()), new BigDecimal("103"));
		x = new Long(ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T00:14:00-0700").getMillis());
		y = new BigDecimal("80.78667");
		xyB = buildTuple(new Long(ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T00:15:00-0700").getMillis()), new BigDecimal("79.2"));
		
		expected = new Tuple<Long, BigDecimal>(x, y);
		actual = calcLinearInterp(xyA, x, xyB);
		assertEquals(expected, actual);
	}
	
	@Test
	public void EasyPaperInterpTest() throws SQLException {
		ResultSet expected = new IteratorWrappingResultSet(interpTrailDataset.iterator());
		ResultSet in = new IteratorWrappingResultSet(paperTrailDataset.iterator());
		
		InterpolatingFilterPlanStep ifps = new InterpolatingFilterPlanStep(paperTrailColGroup, new HashSet<Column>(), null, Period.parse("PT1H"));
		
		ResultSet actual = ifps.runStep(in);
		
		assertTrue(checkEqualRows(expected, actual));
	}
	
	@Test
	public void kitchenInterpTest() throws SQLException {
		ResultSet expected = new IteratorWrappingResultSet(interpolatedSinkDataset.iterator());
		ResultSet in = new IteratorWrappingResultSet(kitchenSinkDataset.iterator());
		
		InterpolatingFilterPlanStep ifps = new InterpolatingFilterPlanStep(kitchenSinkColGroup, new HashSet<Column>(), null, Period.parse("PT1H"));
		
		ResultSet actual = ifps.runStep(in);
		
		assertTrue(checkEqualRows(expected, actual));
	}
		
	@Test
	public void acoustAndLabInterpTest() throws SQLException {
		ResultSet expected = new IteratorWrappingResultSet(interpAcoustNotLabDataset.iterator());
		ResultSet in = new IteratorWrappingResultSet(acoustAndLabDataset.iterator());
		
		InterpolatingFilterPlanStep ifps = new InterpolatingFilterPlanStep(acoustAndLabColGroup, new HashSet<Column>(), null, Period.parse("PT1H"));
		
		ResultSet actual = ifps.runStep(in);
		
		assertTrue(checkEqualRows(expected, actual));
	}
	
	protected static ColumnGrouping paperTrailColGroup = null;
	protected static Iterable<TableRow> paperTrailDataset = null;
	protected static Iterable<TableRow> interpTrailDataset = null;
	
	protected static ColumnGrouping kitchenSinkColGroup = null;
	protected static Iterable<TableRow> kitchenSinkDataset = null;
	protected static Iterable<TableRow> interpolatedSinkDataset = null;
	
	protected static ColumnGrouping acoustAndLabColGroup = null;
	protected static Iterable<TableRow> acoustAndLabDataset = null;
	protected static Iterable<TableRow> interpAcoustNotLabDataset = null;
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		paperTrailColGroup = new ColumnGrouping(Arrays.asList(new Column[] {
			new SimpleColumn("time"),
			new SimpleColumn("colOdd"),
			new SimpleColumn("colEven"),
			new SimpleColumn("colGapped")
		}));
		
		paperTrailDataset = createTableRows(
				paperTrailColGroup,
				new String[][] {
					new String[] {"631179000000", "5", null, "3"},
					new String[] {"631179900000", null, "28", null},
					new String[] {"631180800000", "3", null, null},
					new String[] {"631181700000", null, "31", null},
					new String[] {"631182600000", "2", null, null},
					new String[] {"631183500000", null, "33", "10"},
					new String[] {"631184400000", "6", null, null},
					new String[] {"631185300000", null, "30", null},
					new String[] {"631186200000", "1", null, null}
				});
		
		interpTrailDataset = createTableRows(
				paperTrailColGroup,
				new String[][] {
					new String[] {"631179000000", "5", null, "3"},
					new String[] {"631179900000", "4.0", "28", null},
					new String[] {"631180800000", "3", "29.5", null},
					new String[] {"631181700000", "2.5", "31", null},
					new String[] {"631182600000", "2", "32.0", null},
					new String[] {"631183500000", "4.0", "33", "10"},
					new String[] {"631184400000", "6", "31.5", null},
					new String[] {"631185300000", "3.5", "30", null},
					new String[] {"631186200000", "1", null, null}
				});
		
		
		
		kitchenSinkColGroup = new ColumnGrouping(Arrays.asList(new Column[] {
			new SimpleColumn("time"), 
			new SimpleColumn("00060"), 
			new SimpleColumn("00010"), 
			new SimpleColumn("63680"), 
			new SimpleColumn("80222"), 
			new SimpleColumn("lab80222"), 
			new SimpleColumn("edi80222"), 
			new SimpleColumn("ewi80222"), 
			new SimpleColumn("100200"), 
			new SimpleColumn("lab100200"), 
			new SimpleColumn("edi100200"), 
			new SimpleColumn("ewi100200")
		}));
		
		kitchenSinkDataset = createTableRows(
				kitchenSinkColGroup,
				new String[][] {
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T00:00:00-0700").getMillis(), null, "7.97", "73.4", "103", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T00:14:00-0700").getMillis(), "9410", null, null, null, null, null, null, null, null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T00:15:00-0700").getMillis(), null, "7.97", "73.5", "79.2", null, null, null, ".14", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T00:29:00-0700").getMillis(), "9415", null, null, null, null, null, null, null, null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T00:30:00-0700").getMillis(), null, "8", "68.6", "90.1", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T00:44:00-0700").getMillis(), "9438", null, null, null, null, null, null, null, null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T00:45:00-0700").getMillis(), null, "8", "62.8", "120", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T00:59:00-0700").getMillis(), "9529", null, null, null, null, null, null, null, null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T01:00:00-0700").getMillis(), null, "8.02", "70.3", "72.3", null, null, null, ".14", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T01:14:00-0700").getMillis(), "9634", null, null, null, null, null, null, null, null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T01:15:00-0700").getMillis(), null, "8.05", "60.7", "94.9", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T01:29:00-0700").getMillis(), "9758", null, null, null, null, null, null, null, null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T01:30:00-0700").getMillis(), null, "8.05", "63.4", "78.3", null, null, null, ".14", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T01:44:00-0700").getMillis(), "9951", null, null, null, null, null, null, null, null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T01:45:00-0700").getMillis(), null, "8.07", "53.8", "95.5", null, null, null, ".14", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T01:59:00-0700").getMillis(), "10184", null, null, null, null, null, null, null, null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T02:00:00-0700").getMillis(), null, "8.1", "56.5", "78.4", null, null, null, ".14", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T02:14:00-0700").getMillis(), "10425", null, null, null, null, null, null, null, null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T02:15:00-0700").getMillis(), null, "8.1", "54.9", "76.8", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T02:29:00-0700").getMillis(), "10668", null, null, null, null, null, null, null, null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T02:30:00-0700").getMillis(), null, "8.12", "51.6", "83", null, null, null, ".14", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T02:44:00-0700").getMillis(), "10994", null, null, null, null, null, null, null, null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T02:45:00-0700").getMillis(), null, "8.15", "48.8", "76.6", null, null, null, ".14", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T02:59:00-0700").getMillis(), "11242", null, null, null, null, null, null, null, null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T03:00:00-0700").getMillis(), null, "8.17", "45.7", "113", null, null, null, ".14", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T03:14:00-0700").getMillis(), "11580", null, null, null, null, null, null, null, null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T03:15:00-0700").getMillis(), null, "8.2", "45.2", "92.8", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T03:29:00-0700").getMillis(), "11875", null, null, null, null, null, null, null, null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T03:30:00-0700").getMillis(), null, "8.22", "46", "110", null, null, null, ".14", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T03:44:00-0700").getMillis(), "12079", null, null, null, null, null, null, null, null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T03:45:00-0700").getMillis(), null, "8.22", "48", "120", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T03:59:00-0700").getMillis(), "12279", null, null, null, null, null, null, null, null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T04:00:00-0700").getMillis(), null, "8.25", "42.9", "101", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T04:14:00-0700").getMillis(), "12491", null, null, null, null, null, null, null, null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T04:15:00-0700").getMillis(), null, "8.25", "37.4", "75.5", null, null, null, ".14", null, null, null}
				});
		
		interpolatedSinkDataset = createTableRows(
				kitchenSinkColGroup,
				new String[][] {
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T00:00:00-0700").getMillis(), null, "7.97", "73.4", "103", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T00:14:00-0700").getMillis(), "9410", "7.970000", "73.49333", "80.78667", null, null, null, "0.1393333", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T00:15:00-0700").getMillis(), "9410.333", "7.97", "73.5", "79.2", null, null, null, ".14", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T00:29:00-0700").getMillis(), "9415", "7.998000", "68.92667", "89.37333", null, null, null, "0.1306667", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T00:30:00-0700").getMillis(), "9416.533", "8", "68.6", "90.1", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T00:44:00-0700").getMillis(), "9438", "8.000000", "63.18667", "118.0067", null, null, null, "0.1300000", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T00:45:00-0700").getMillis(), "9444.067", "8", "62.8", "120", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T00:59:00-0700").getMillis(), "9529", "8.018667", "69.80000", "75.48000", null, null, null, "0.1393333", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T01:00:00-0700").getMillis(), "9536.000", "8.02", "70.3", "72.3", null, null, null, ".14", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T01:14:00-0700").getMillis(), "9634", "8.048000", "61.34000", "93.39333", null, null, null, "0.1306667", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T01:15:00-0700").getMillis(), "9642.267", "8.05", "60.7", "94.9", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T01:29:00-0700").getMillis(), "9758", "8.050000", "63.22000", "79.40667", null, null, null, "0.1393333", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T01:30:00-0700").getMillis(), "9770.867", "8.05", "63.4", "78.3", null, null, null, ".14", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T01:44:00-0700").getMillis(), "9951", "8.068667", "54.44000", "94.35333", null, null, null, "0.1400000", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T01:45:00-0700").getMillis(), "9966.533", "8.07", "53.8", "95.5", null, null, null, ".14", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T01:59:00-0700").getMillis(), "10184", "8.098000", "56.32000", "79.54000", null, null, null, "0.1400000", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T02:00:00-0700").getMillis(), "10200.07", "8.1", "56.5", "78.4", null, null, null, ".14", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T02:14:00-0700").getMillis(), "10425", "8.100000", "55.00667", "76.90667", null, null, null, "0.1306667", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T02:15:00-0700").getMillis(), "10441.20", "8.1", "54.9", "76.8", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T02:29:00-0700").getMillis(), "10668", "8.118667", "51.82000", "82.58667", null, null, null, "0.1393333", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T02:30:00-0700").getMillis(), "10689.73", "8.12", "51.6", "83", null, null, null, ".14", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T02:44:00-0700").getMillis(), "10994", "8.148000", "48.98667", "77.02667", null, null, null, "0.1400000", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T02:45:00-0700").getMillis(), "11010.53", "8.15", "48.8", "76.6", null, null, null, ".14", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T02:59:00-0700").getMillis(), "11242", "8.168667", "45.90667", "110.5733", null, null, null, "0.1400000", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T03:00:00-0700").getMillis(), "11264.53", "8.17", "45.7", "113", null, null, null, ".14", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T03:14:00-0700").getMillis(), "11580", "8.198000", "45.23333", "94.14667", null, null, null, "0.1306667", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T03:15:00-0700").getMillis(), "11599.67", "8.2", "45.2", "92.8", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T03:29:00-0700").getMillis(), "11875", "8.218667", "45.94667", "108.8533", null, null, null, "0.1393333", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T03:30:00-0700").getMillis(), "11888.60", "8.22", "46", "110", null, null, null, ".14", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T03:44:00-0700").getMillis(), "12079", "8.220000", "47.86667", "119.3333", null, null, null, "0.1306667", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T03:45:00-0700").getMillis(), "12092.33", "8.22", "48", "120", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T03:59:00-0700").getMillis(), "12279", "8.248000", "43.24000", "102.2667", null, null, null, "0.1300000", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T04:00:00-0700").getMillis(), "12293.13", "8.25", "42.9", "101", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T04:14:00-0700").getMillis(), "12491", "8.250000", "37.76667", "77.20000", null, null, null, "0.1393333", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-01T04:15:00-0700").getMillis(), null, "8.25", "37.4", "75.5", null, null, null, ".14", null, null, null}
				});
		
		acoustAndLabColGroup = new ColumnGrouping(Arrays.asList(new Column[] {
			new SimpleColumn("time"), 
			new SimpleColumn("00060"), 
			new SimpleColumn("00010"), 
			new SimpleColumn("63680"), 
			new SimpleColumn("80222"), 
			new SimpleColumn("lab80222"), 
			new SimpleColumn("edi80222"), 
			new SimpleColumn("ewi80222"), 
			new SimpleColumn("100200"), 
			new SimpleColumn("lab100200"), 
			new SimpleColumn("edi100200"), 
			new SimpleColumn("ewi100200")
		}));
		
		acoustAndLabDataset = createTableRows(
				acoustAndLabColGroup,
				new String[][] {
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T07:00:00-0700").getMillis(), "11247", "8.05", "52.7", "103", null, null, null, ".14", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T07:15:00-0700").getMillis(), "11105", "8.07", "53.7", "91.3", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T07:30:00-0700").getMillis(), "10973", "8.07", "55.4", "89", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T07:45:00-0700").getMillis(), "10788", "8.07", "53.7", "91.5", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T08:00:00-0700").getMillis(), "10633", "8.1", "64.9", "70.2", null, null, null, ".14", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T08:15:00-0700").getMillis(), "10445", "8.1", "50.7", "72.6", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T08:28:00-0700").getMillis(), null, null, null, null, null, null, "86.12;89.1;97.02", null, null, null, ".099;.11;.121"},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T08:30:00-0700").getMillis(), "10292", "8.1", "51.2", "90.1", null, null, null, ".12", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T08:45:00-0700").getMillis(), "10101", "8.1", "51.6", "75.2", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T08:56:00-0700").getMillis(), null, null, null, null, null, null, "74.73;77.1;84.91", null, null, null, ".10031;.11131;.12231"},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T09:00:00-0700").getMillis(), "9936", "8.1", "51.6", "76.6", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T09:15:00-0700").getMillis(), "9763", "8.1", "47.7", "79.6", null, null, null, ".14", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T09:30:00-0700").getMillis(), "9614", "8.1", "54.5", "74.7", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T09:45:00-0700").getMillis(), "9438", "8.1", "51.2", "75.3", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T10:00:00-0700").getMillis(), "9306", "8.1", "47.7", "45.2", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T10:15:00-0700").getMillis(), "9128", "8.1", "50.5", "63.1", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T10:30:00-0700").getMillis(), "8997", "8.1", "51.6", "51.5", null, null, null, ".12", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T10:45:00-0700").getMillis(), "8854", "8.07", "46.4", "57.6", null, null, null, ".14", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T11:00:00-0700").getMillis(), "8693", "8.07", "53.2", "55.9", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T11:15:00-0700").getMillis(), "8597", "8.1", "60.9", "67.3", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T11:30:00-0700").getMillis(), "8474", "8.1", "49.5", "54.2", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T11:44:00-0700").getMillis(), "8361", null, null, null, null, null, null, null, null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T11:45:00-0700").getMillis(), null, "8.1", "47.8", "50.1", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T11:59:00-0700").getMillis(), "8289", null, null, null, null, null, null, null, null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T12:00:00-0700").getMillis(), null, "8.1", "47.6", "49.3", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T12:14:00-0700").getMillis(), "8181", null, null, null, null, null, null, null, null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T12:15:00-0700").getMillis(), null, "8.12", "50.5", "42.6", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T12:29:00-0700").getMillis(), "8145", null, null, null, null, null, null, null, null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T12:30:00-0700").getMillis(), null, "8.15", "43.2", "58.6", null, null, "69.28;71.4;78.72", ".14", null, null, ".10134;.11234;.12334"},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T12:44:00-0700").getMillis(), "8069", null, null, null, null, null, null, null, null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T12:45:00-0700").getMillis(), null, "8.15", "44.1", "45.2", null, null, null, ".14", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T12:59:00-0700").getMillis(), "8069", null, null, null, null, null, null, null, null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T13:00:00-0700").getMillis(), null, "8.2", "43.3", "51.6", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T13:13:00-0700").getMillis(), null, null, null, null, null, null, "63.48;65.3;71.52", null, null, null, ".1;.111;.122"},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T13:14:00-0700").getMillis(), "8065", null, null, null, null, null, null, null, null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T13:15:00-0700").getMillis(), null, "8.22", "42.1", "51.7", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T13:29:00-0700").getMillis(), "8092", null, null, null, null, null, null, null, null, null, null}
				});
		
		interpAcoustNotLabDataset = createTableRows(
				acoustAndLabColGroup,
				new String[][] {
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T07:00:00-0700").getMillis(), "11247", "8.05", "52.7", "103", null, null, null, ".14", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T07:15:00-0700").getMillis(), "11105", "8.07", "53.7", "91.3", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T07:30:00-0700").getMillis(), "10973", "8.07", "55.4", "89", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T07:45:00-0700").getMillis(), "10788", "8.07", "53.7", "91.5", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T08:00:00-0700").getMillis(), "10633", "8.1", "64.9", "70.2", null, null, null, ".14", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T08:15:00-0700").getMillis(), "10445", "8.1", "50.7", "72.6", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T08:28:00-0700").getMillis(), "10312.40", "8.100000", "51.13333", "87.76667", null, null, "86.12;89.1;97.02", "0.1213333", null, null, ".099;.11;.121"},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T08:30:00-0700").getMillis(), "10292", "8.1", "51.2", "90.1", null, null, null, ".12", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T08:45:00-0700").getMillis(), "10101", "8.1", "51.6", "75.2", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T08:56:00-0700").getMillis(), "9980.000", "8.100000", "51.60000", "76.22667", null, null, "74.73;77.1;84.91", "0.1300000", null, null, ".10031;.11131;.12231"},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T09:00:00-0700").getMillis(), "9936", "8.1", "51.6", "76.6", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T09:15:00-0700").getMillis(), "9763", "8.1", "47.7", "79.6", null, null, null, ".14", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T09:30:00-0700").getMillis(), "9614", "8.1", "54.5", "74.7", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T09:45:00-0700").getMillis(), "9438", "8.1", "51.2", "75.3", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T10:00:00-0700").getMillis(), "9306", "8.1", "47.7", "45.2", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T10:15:00-0700").getMillis(), "9128", "8.1", "50.5", "63.1", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T10:30:00-0700").getMillis(), "8997", "8.1", "51.6", "51.5", null, null, null, ".12", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T10:45:00-0700").getMillis(), "8854", "8.07", "46.4", "57.6", null, null, null, ".14", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T11:00:00-0700").getMillis(), "8693", "8.07", "53.2", "55.9", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T11:15:00-0700").getMillis(), "8597", "8.1", "60.9", "67.3", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T11:30:00-0700").getMillis(), "8474", "8.1", "49.5", "54.2", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T11:44:00-0700").getMillis(), "8361", "8.100000", "47.91333", "50.37333", null, null, null, "0.1300000", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T11:45:00-0700").getMillis(), "8356.200", "8.1", "47.8", "50.1", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T11:59:00-0700").getMillis(), "8289", "8.100000", "47.61333", "49.35333", null, null, null, "0.1300000", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T12:00:00-0700").getMillis(), "8281.800", "8.1", "47.6", "49.3", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T12:14:00-0700").getMillis(), "8181", "8.118667", "50.30667", "43.04667", null, null, null, "0.1300000", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T12:15:00-0700").getMillis(), "8178.600", "8.12", "50.5", "42.6", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T12:29:00-0700").getMillis(), "8145", "8.148000", "43.68667", "57.53333", null, null, null, "0.1393333", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T12:30:00-0700").getMillis(), "8139.933", "8.15", "43.2", "58.6", null, null, "69.28;71.4;78.72", ".14", null, null, ".10134;.11234;.12334"},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T12:44:00-0700").getMillis(), "8069", "8.150000", "44.04000", "46.09333", null, null, null, "0.1400000", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T12:45:00-0700").getMillis(), "8069.000", "8.15", "44.1", "45.2", null, null, null, ".14", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T12:59:00-0700").getMillis(), "8069", "8.196667", "43.35333", "51.17333", null, null, null, "0.1306667", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T13:00:00-0700").getMillis(), "8068.733", "8.2", "43.3", "51.6", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T13:13:00-0700").getMillis(), "8065.267", "8.217333", "42.26000", "51.68667", null, null, "63.48;65.3;71.52", "0.1300000", null, null, ".1;.111;.122"},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T13:14:00-0700").getMillis(), "8065", "8.218667", "42.18000", "51.69333", null, null, null, "0.1300000", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T13:15:00-0700").getMillis(), "8066.800", "8.22", "42.1", "51.7", null, null, null, ".13", null, null, null},
					new String[] {"" + ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2008-03-04T13:29:00-0700").getMillis(), "8092", null, null, null, null, null, null, null, null, null, null}
				});
	}
	
	@AfterClass
	public static void tearDownClass() throws Exception {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}
}
