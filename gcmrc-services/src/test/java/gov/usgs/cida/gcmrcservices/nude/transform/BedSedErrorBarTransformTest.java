package gov.usgs.cida.gcmrcservices.nude.transform;

import static gov.usgs.cida.gcmrcservices.ResultSetUtils.checkEqualRows;
import static org.junit.Assert.assertTrue;
import gov.usgs.cida.gcmrcservices.ResultSetUtils;
import gov.usgs.cida.nude.column.Column;
import gov.usgs.cida.nude.column.ColumnGrouping;
import gov.usgs.cida.nude.column.SimpleColumn;
import gov.usgs.cida.nude.filter.FilterStage;
import gov.usgs.cida.nude.filter.FilterStageBuilder;
import gov.usgs.cida.nude.filter.NudeFilter;
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

public class BedSedErrorBarTransformTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BedSedErrorBarTransformTest.class);

	public BedSedErrorBarTransformTest() {
	}

	@BeforeClass		
	public static void setUpClass() throws Exception {
		timeColumn = new SimpleColumn("timeColumn");
		sampleSetColumn = new SimpleColumn("sampleSetColumn");
		valueColumn = new SimpleColumn("valueColumn");
		sampleMassColumn = new SimpleColumn("sampleMassColumn");
		errorColumn = new SimpleColumn("errorColumn");
		conf95Column = new SimpleColumn("conf95Column");
		
		sampleColGroup = new ColumnGrouping(Arrays.asList(new Column[] {
			timeColumn,
			sampleSetColumn,
			valueColumn,
			sampleMassColumn,
			errorColumn,
			conf95Column
		}));
		
		inputSampleDataset = ResultSetUtils.createTableRows(sampleColGroup, new String[][] {
			new String[] {"1020","1","80","50","6","11.7"},
			new String[] {"1062","2","40","54","7","13.6"},
			new String[] {"1063","3",null,"54","7","13.6"},
			new String[] {"1064","4","40","54","7",null},
			new String[] {"1065","5",null,"54","7",null},
			new String[] {"1066","6","40","54",null,null},
			new String[] {"1067","7",null,"54",null,null},
			new String[] {"1071","8","10","64","10","10.1"}
		});
		
		expectedSampleDataset = ResultSetUtils.createTableRows(sampleColGroup, new String[][] {
			new String[] {"1020","1","68.3;80;91.7","50","6","11.7"},
			new String[] {"1062","2","26.4;40;53.6","54","7","13.6"},
			new String[] {"1063","3",null,"54","7","13.6"},
			new String[] {"1064","4","40","54","7",null},
			new String[] {"1065","5",null,"54","7",null},
			new String[] {"1066","6","40","54",null,null},
			new String[] {"1067","7",null,"54",null,null},
			new String[] {"1071","8","-0.1;10;20.1","64","10","10.1"}
		});
		
	}
	
	@Test
	public void testBedSedErrorBarResultSet() throws Exception {
		NudeFilter nf = new NudeFilter(Arrays.asList(new FilterStage[] {
			new FilterStageBuilder(sampleColGroup)
					.addTransform(valueColumn, new BedSedErrorBarTransform(valueColumn, conf95Column))
					.buildFilterStage()
		}));
		
		ResultSet expected = new IteratorWrappingResultSet(expectedSampleDataset.iterator());
		ResultSet in = new IteratorWrappingResultSet(inputSampleDataset.iterator());
		ResultSet actual = nf.filter(in);
		assertTrue(checkEqualRows(expected, actual));
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
	
	protected static ColumnGrouping sampleColGroup;
	protected static Iterable<TableRow> inputSampleDataset = null;
	protected static Iterable<TableRow> expectedSampleDataset = null;
	
}
