package gov.usgs.cida.gcmrcservices.nude;

import static gov.usgs.cida.gcmrcservices.ResultSetUtils.checkEqualRows;
import static org.junit.Assert.assertTrue;
import gov.usgs.cida.gcmrcservices.ResultSetUtils;
import gov.usgs.cida.nude.column.Column;
import gov.usgs.cida.nude.column.ColumnGrouping;
import gov.usgs.cida.nude.column.SimpleColumn;
import gov.usgs.cida.nude.resultset.inmemory.IteratorWrappingResultSet;
import gov.usgs.cida.nude.resultset.inmemory.TableRow;

import java.sql.ResultSet;
import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BedSedErrorBarTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BedSedErrorBarTest.class);

	public BedSedErrorBarTest() {
	}

	@BeforeClass		
	public static void setUpClass() throws Exception {
		timeColumn = new SimpleColumn("time");
		sampleSetColumn = new SimpleColumn("sampleset");
		sampleSizeColumn = new SimpleColumn("avgSize");
		sampleMassColumn = new SimpleColumn("mass");
		errorColumn = new SimpleColumn("stdError");
		conf95Column = new SimpleColumn("conf95");
		
		valueColumn = new SimpleColumn("value");
		
		inputSampleColGroup = new ColumnGrouping(Arrays.asList(new Column[] {
				timeColumn,
				sampleSetColumn,
				sampleSizeColumn,
				sampleMassColumn,
				errorColumn,
				conf95Column
			}));
		
		inputSampleDataset = ResultSetUtils.createTableRows(inputSampleColGroup, new String[][] {
			new String[] {"1020","1","80","50","6", "11.7"},
			new String[] {"1061","2","40","54","7", "13.6"}
		});
		
		expectedSampleColGroup = new ColumnGrouping(Arrays.asList(new Column[] {
				timeColumn,
				valueColumn,
			}));
		
		expectedSampleDataset = ResultSetUtils.createTableRows(expectedSampleColGroup, new String[][] {
			new String[] {"1020","68.3:80:91.7"},
			new String[] {"1061","26.4:40:53.6"}
		});
		
	}
	
	@Test
	public void testBedSedErrorBarResultSet() throws Exception {
		ResultSet expected = new IteratorWrappingResultSet(expectedSampleDataset.iterator());
		ResultSet in = new IteratorWrappingResultSet(inputSampleDataset.iterator());
		ResultSet actual = runStep(in);
		assertTrue(checkEqualRows(expected, actual));
	}
	
	private	ResultSet runStep(ResultSet rs) {
		ResultSet result = null;
		if (null != rs) {
			result = new BedSedErrorBarResultSet(rs, expectedSampleColGroup, timeColumn, valueColumn);
		}
		return result;
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
	protected static Column sampleSizeColumn = null;
	protected static Column sampleMassColumn = null;
	protected static Column errorColumn = null;
	protected static Column conf95Column = null;
	
	protected static Column valueColumn = null;
	
	protected static ColumnGrouping inputSampleColGroup;
	protected static Iterable<TableRow> inputSampleDataset = null;
	protected static ColumnGrouping expectedSampleColGroup = null;
	protected static Iterable<TableRow> expectedSampleDataset = null;
	
}
