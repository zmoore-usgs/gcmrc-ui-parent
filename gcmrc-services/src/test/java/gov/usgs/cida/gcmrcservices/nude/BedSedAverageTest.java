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
import java.util.HashSet;
import org.joda.time.Period;
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
		
		BedSedAveragePlanStep bsaps = new BedSedAveragePlanStep(incomingSampleJiraColGroup);
		
		ResultSet actual = bsaps.runStep(in);
		
		assertTrue(checkEqualRows(expected, actual));
	}
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		incomingSampleJiraColGroup = new ColumnGrouping(Arrays.asList(new Column[] {
			new SimpleColumn("time"),
			new SimpleColumn("sampleset"),
			new SimpleColumn("param1"),
			new SimpleColumn("param2"),
			new SimpleColumn("param3")
		}));
		incomingSampleJiraDataset = ResultSetUtils.createTableRows(incomingSampleJiraColGroup, new String[][] {
			new String[] {"1010","1","9","5","7"},
			new String[] {"1020","1","8",null,null},
			new String[] {"1030","1","7",null,null},
			new String[] {"1041","2","2","9","6"},
			new String[] {"1051","2","3",null,null},
			new String[] {"1061","2",null,null,null},
			new String[] {"1071","2","5",null,null},
			new String[] {"1081","2","6",null,null},
			new String[] {"1092","3","3","5","7"},
			new String[] {"1103","4",null,"4","8"},
			new String[] {"1114","5","5","2","9"}
		});
		expectedSampleJiraDataset = ResultSetUtils.createTableRows(incomingSampleJiraColGroup, new String[][] {
			new String[] {"1020","1","8","5","7"},
			new String[] {"1061","2","4","9","6"},
			new String[] {"1092","3","3","5","7"},
			new String[] {"1103","4",null,"4","8"},
			new String[] {"1114","5","5","2","9"}
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

	protected static ColumnGrouping incomingSampleJiraColGroup = null;
	protected static Iterable<TableRow> incomingSampleJiraDataset = null;
	protected static Iterable<TableRow> expectedSampleJiraDataset = null;
	
}
