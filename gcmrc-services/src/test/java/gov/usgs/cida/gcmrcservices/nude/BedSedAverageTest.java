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
	
}
