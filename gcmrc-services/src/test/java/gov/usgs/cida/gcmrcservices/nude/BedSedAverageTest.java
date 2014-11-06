package gov.usgs.cida.gcmrcservices.nude;

import gov.usgs.cida.gcmrcservices.ResultSetUtils;
import static gov.usgs.cida.gcmrcservices.ResultSetUtils.checkEqualRows;
import gov.usgs.cida.nude.column.Column;
import gov.usgs.cida.nude.column.ColumnGrouping;
import gov.usgs.cida.nude.column.SimpleColumn;
import gov.usgs.cida.nude.resultset.inmemory.IteratorWrappingResultSet;
import gov.usgs.cida.nude.resultset.inmemory.TableRow;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class BedSedAverageTest {
	private static final Logger log = LoggerFactory.getLogger(BedSedAverageTest.class);

	public BedSedAverageTest() {
	}
	
	@Test
	public void testSampleJiraTable() throws SQLException {
		//Test taken from GCMON-144
		
		ResultSet expected = new IteratorWrappingResultSet(expectedSampleJiraDataset.iterator());
		ResultSet in = new IteratorWrappingResultSet(incomingSampleJiraDataset.iterator());
		
		BedSedAveragePlanStep bsaps = new BedSedAveragePlanStep(timeColumn, sampleSetColumn, valueColumn, sampleMassColumn, errorColumn, conf95Column);
		
		ResultSet actual = bsaps.runStep(in);
		
		assertTrue(checkEqualRows(expected, actual));
	}
	
	@Test
	public void testRealWorldTable() throws SQLException {
		//Test taken from GCMON-144
		
		ResultSet expected = new IteratorWrappingResultSet(expectedRealWorldDataset.iterator());
		ResultSet in = new IteratorWrappingResultSet(incomingRealWorldDataset.iterator());
		
		BedSedAveragePlanStep bsaps = new BedSedAveragePlanStep(timeColumn, sampleSetColumn, valueColumn, sampleMassColumn, errorColumn, conf95Column);
		
		ResultSet actual = bsaps.runStep(in);
		
		assertTrue(checkEqualRows(expected, actual));
	}
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		timeColumn = new SimpleColumn("time");
		sampleSetColumn = new SimpleColumn("sampleset");
		valueColumn = new SimpleColumn("param1");
		sampleMassColumn = new SimpleColumn("param2");
		errorColumn = new SimpleColumn("param3");
		conf95Column = new SimpleColumn("param4");
		
		incomingSampleJiraColGroup = new ColumnGrouping(Arrays.asList(new Column[] {
			timeColumn,
			sampleSetColumn,
			valueColumn,
			sampleMassColumn
		}));
		incomingSampleJiraDataset = ResultSetUtils.createTableRows(incomingSampleJiraColGroup, new String[][] {
			new String[] {"1010","1","90","50"},
			new String[] {"1020","1","80","30"},
			new String[] {"1030","1","70","70"},
			new String[] {"1041","2","20","90"},
			new String[] {"1051","2","30","20"},
			new String[] {"1061","2","40","50"},
			new String[] {"1071","2","50","80"},
			new String[] {"1081","2","60","30"},
			new String[] {"1092","3","30","50"},
			new String[] {"1103","4","40","40"},
			new String[] {"1114","5","50","20"}
		});
		expectedSampleJiraColGroup = new ColumnGrouping(Arrays.asList(new Column[] {
			timeColumn,
			sampleSetColumn,
			valueColumn,
			sampleMassColumn,
			errorColumn,
			conf95Column
		}));
		expectedSampleJiraDataset = ResultSetUtils.createTableRows(expectedSampleJiraColGroup, new String[][] {
			new String[] {"1020","1","80","50","6", "11.7"},
			new String[] {"1061","2","40","54","7", "13.6"},
		});
		
		DateTimeFormatter dtf = ISODateTimeFormat.dateTimeNoMillis();
		incomingRealWorldColGroup = new ColumnGrouping(timeColumn, Arrays.asList(new Column[] {
			new SimpleColumn("siteColumn"),
			timeColumn,
			sampleSetColumn,
			sampleMassColumn,
			valueColumn
		}));
		incomingRealWorldDataset = ResultSetUtils.createTableRows(incomingRealWorldColGroup, new String[][] {
			new String[] {"09404200","" + dtf.parseDateTime("1997-11-04T12:55:00-07:00").getMillis(),"1","96.6","0.461"},
			new String[] {"09404200","" + dtf.parseDateTime("1997-11-04T13:00:00-07:00").getMillis(),"1","348.6","0.345"},
			new String[] {"09404200","" + dtf.parseDateTime("1997-11-04T13:05:00-07:00").getMillis(),"1","366.8","0.212"},
			new String[] {"09404200","" + dtf.parseDateTime("1997-11-04T16:10:00-07:00").getMillis(),"2","411.2","0.637"},
			new String[] {"09404200","" + dtf.parseDateTime("1997-11-04T16:15:00-07:00").getMillis(),"2","350.0","0.388"},
			new String[] {"09404200","" + dtf.parseDateTime("1997-11-04T16:20:00-07:00").getMillis(),"2","47.0","0.394"},
			new String[] {"09404200","" + dtf.parseDateTime("1997-11-06T11:25:00-07:00").getMillis(),"3","305.7","0.407"},
			new String[] {"09404200","" + dtf.parseDateTime("1997-11-06T11:30:00-07:00").getMillis(),"3","266.1","0.327"},
			new String[] {"09404200","" + dtf.parseDateTime("1997-11-06T11:35:00-07:00").getMillis(),"3","356.4","0.377"},
			new String[] {"09404200","" + dtf.parseDateTime("1997-11-07T09:55:00-07:00").getMillis(),"4","443.9","0.398"},
			new String[] {"09404200","" + dtf.parseDateTime("1997-11-07T10:00:00-07:00").getMillis(),"4","438.7","0.342"},
			new String[] {"09404200","" + dtf.parseDateTime("1997-11-07T10:05:00-07:00").getMillis(),"4","350.2","0.307"},
			new String[] {"09404200","" + dtf.parseDateTime("2001-07-23T17:00:00-07:00").getMillis(),"5","443.92","0.395"},
			new String[] {"09404200","" + dtf.parseDateTime("2001-07-23T17:10:00-07:00").getMillis(),"5","381.74","0.424"},
			new String[] {"09404200","" + dtf.parseDateTime("2001-07-23T17:20:00-07:00").getMillis(),"5","411.24","0.383"},
			new String[] {"09404200","" + dtf.parseDateTime("2001-08-08T13:17:00-07:00").getMillis(),"6","452.55","0.319"},
			new String[] {"09404200","" + dtf.parseDateTime("2001-08-08T13:25:00-07:00").getMillis(),"6","345.10","0.351"},
			new String[] {"09404200","" + dtf.parseDateTime("2001-08-15T14:30:00-07:00").getMillis(),"7","450.04","0.283"},
			new String[] {"09404200","" + dtf.parseDateTime("2001-08-15T14:40:00-07:00").getMillis(),"7","448.13","0.328"},
			new String[] {"09404200","" + dtf.parseDateTime("2001-09-04T15:20:00-07:00").getMillis(),"8","410.51","0.332"},
			new String[] {"09404200","" + dtf.parseDateTime("2001-09-04T15:30:00-07:00").getMillis(),"8","413.94","0.337"},
			new String[] {"09404200","" + dtf.parseDateTime("2001-09-04T15:40:00-07:00").getMillis(),"8","397.59","0.359"},
			new String[] {"09404200","" + dtf.parseDateTime("2001-09-13T13:30:00-07:00").getMillis(),"9","387.55","0.298"},
			new String[] {"09404200","" + dtf.parseDateTime("2001-09-13T13:45:00-07:00").getMillis(),"9","434.65","0.389"},
			new String[] {"09404200","" + dtf.parseDateTime("2001-09-13T14:00:00-07:00").getMillis(),"9","398.00","0.157"},
			new String[] {"09404200","" + dtf.parseDateTime("2001-10-10T12:50:00-07:00").getMillis(),"10","432.09","0.362"},
			new String[] {"09404200","" + dtf.parseDateTime("2001-10-10T13:00:00-07:00").getMillis(),"10","434.86","0.364"},
			new String[] {"09404200","" + dtf.parseDateTime("2001-10-10T13:05:00-07:00").getMillis(),"10","425.40","0.394"}
		});
		
		expectedRealWorldColGroup = new ColumnGrouping(timeColumn, Arrays.asList(new Column[] {
			timeColumn,
			sampleSetColumn,
			valueColumn,
			sampleMassColumn,
			errorColumn,
			conf95Column
		}));
		expectedRealWorldDataset = ResultSetUtils.createTableRows(expectedRealWorldColGroup, new String[][] {
			new String[] {"" + dtf.parseDateTime("1997-11-04T13:00:00-07:00").getMillis(),"1","0.339","270.7","0.072","0.140"},
			new String[] {"" + dtf.parseDateTime("1997-11-04T16:15:00-07:00").getMillis(),"2","0.473","269.4","0.082","0.160"},
			new String[] {"" + dtf.parseDateTime("1997-11-06T11:30:00-07:00").getMillis(),"3","0.370","309.4","0.0234","0.0456"},
			new String[] {"" + dtf.parseDateTime("1997-11-07T10:00:00-07:00").getMillis(),"4","0.349","410.9","0.0265","0.0517"},
			new String[] {"" + dtf.parseDateTime("2001-07-23T17:10:00-07:00").getMillis(),"5","0.400","412.30","0.0121","0.0236"},
			new String[] {"" + dtf.parseDateTime("2001-08-08T13:21:00-07:00").getMillis(),"6","0.335","398.83","0.0160","0.0312"},
			new String[] {"" + dtf.parseDateTime("2001-08-15T14:35:00-07:00").getMillis(),"7","0.305","449.08","0.0228","0.0445"},
			new String[] {"" + dtf.parseDateTime("2001-09-04T15:30:00-07:00").getMillis(),"8","0.342","407.35","0.0086","0.0168"},
			new String[] {"" + dtf.parseDateTime("2001-09-13T13:45:00-07:00").getMillis(),"9","0.282","406.73","0.068","0.133"},
			new String[] {"" + dtf.parseDateTime("2001-10-10T12:58:20-07:00").getMillis(),"10","0.373","430.78","0.0105","0.0205"}
		});
	}
	
	@Before
	public void setUp() throws Exception {
		
	}
	
	@AfterClass
	public static void tearDownClass() throws Exception {
		
	}
	
	@After
	public void tearDown() throws Exception {
		
	}

	protected static Column timeColumn = null;
	protected static Column sampleSetColumn = null;
	protected static Column valueColumn = null;
	protected static Column sampleMassColumn = null;
	protected static Column errorColumn = null;
	protected static Column conf95Column = null;
	
	protected static ColumnGrouping incomingSampleJiraColGroup = null;
	protected static Iterable<TableRow> incomingSampleJiraDataset = null;
	protected static ColumnGrouping expectedSampleJiraColGroup = null;
	protected static Iterable<TableRow> expectedSampleJiraDataset = null;
	
	protected static ColumnGrouping incomingRealWorldColGroup = null;
	protected static Iterable<TableRow> incomingRealWorldDataset = null;
	protected static ColumnGrouping expectedRealWorldColGroup = null;
	protected static Iterable<TableRow> expectedRealWorldDataset = null;
}
