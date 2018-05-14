package gov.usgs.cida.gcmrcservices.nude.transform;

import gov.usgs.cida.gcmrcservices.ResultSetUtils;
import static gov.usgs.cida.gcmrcservices.jsl.data.BedSedimentSpec.LOWER_LIMIT_MM;
import static gov.usgs.cida.gcmrcservices.jsl.data.BedSedimentSpec.LOWER_LIMIT_PERCENT;
import static gov.usgs.cida.gcmrcservices.jsl.data.BedSedimentSpec.UPPER_LIMIT_MM;
import gov.usgs.cida.nude.column.Column;
import gov.usgs.cida.nude.column.ColumnGrouping;
import gov.usgs.cida.nude.column.SimpleColumn;
import gov.usgs.cida.nude.filter.FilterStage;
import gov.usgs.cida.nude.filter.FilterStageBuilder;
import gov.usgs.cida.nude.filter.NudeFilter;
import gov.usgs.cida.nude.resultset.inmemory.IteratorWrappingResultSet;
import gov.usgs.cida.nude.resultset.inmemory.TableRow;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class SandGrainSizeLimiterTransformTest {
	private static final Logger log = LoggerFactory.getLogger(SandGrainSizeLimiterTransformTest.class);
	
	public SandGrainSizeLimiterTransformTest() {
	}
	
	protected static Column valueColumn = new SimpleColumn("value");
	protected static Column unitsColumn = new SimpleColumn("units");
	
	protected static ColumnGrouping sampleColGroup;
	protected static Iterable<TableRow> inputSampleDataset = null;
	protected static Iterable<TableRow> expectedSampleDataset = null;
	
	@BeforeClass
	public static void setUpClass() {
		sampleColGroup = new ColumnGrouping(Arrays.asList(new Column[] {
			valueColumn,
			unitsColumn
		}));
		
		String lowValue = "0.00117";
		String medValue = "0.136";
		String highValue = "10.1";
		inputSampleDataset = ResultSetUtils.createTableRows(sampleColGroup, new String[][] {
			new String[] {null,null},
			new String[] {lowValue,"millimeters"},
			new String[] {medValue,"millimeters"},
			new String[] {highValue,"millimeters"},
			new String[] {lowValue+";"+lowValue+";"+lowValue,"millimeters"},
			new String[] {lowValue+";"+lowValue+";"+medValue,"millimeters"},
			new String[] {lowValue+";"+medValue+";"+medValue,"millimeters"},
			new String[] {medValue+";"+medValue+";"+medValue,"millimeters"},
			new String[] {medValue+";"+medValue+";"+highValue,"millimeters"},
			new String[] {medValue+";"+highValue+";"+highValue,"millimeters"},
			new String[] {highValue+";"+highValue+";"+highValue,"millimeters"},
			new String[] {lowValue+";"+medValue+";"+highValue,"millimeters"}
		});
		
		expectedSampleDataset = ResultSetUtils.createTableRows(sampleColGroup, new String[][] {
			new String[] {null,null},
			new String[] {LOWER_LIMIT_MM.toPlainString(),"millimeters"},
			new String[] {medValue,"millimeters"},
			new String[] {UPPER_LIMIT_MM.toPlainString(),"millimeters"},
			new String[] {LOWER_LIMIT_MM.toPlainString()+";"+LOWER_LIMIT_MM.toPlainString()+";"+LOWER_LIMIT_MM.toPlainString(),"millimeters"},
			new String[] {LOWER_LIMIT_MM.toPlainString()+";"+LOWER_LIMIT_MM.toPlainString()+";"+medValue,"millimeters"},
			new String[] {LOWER_LIMIT_MM.toPlainString()+";"+medValue+";"+medValue,"millimeters"},
			new String[] {medValue+";"+medValue+";"+medValue,"millimeters"},
			new String[] {medValue+";"+medValue+";"+UPPER_LIMIT_MM.toPlainString(),"millimeters"},
			new String[] {medValue+";"+UPPER_LIMIT_MM.toPlainString()+";"+UPPER_LIMIT_MM.toPlainString(),"millimeters"},
			new String[] {UPPER_LIMIT_MM.toPlainString()+";"+UPPER_LIMIT_MM.toPlainString()+";"+UPPER_LIMIT_MM.toPlainString(),"millimeters"},
			new String[] {LOWER_LIMIT_MM.toPlainString()+";"+medValue+";"+UPPER_LIMIT_MM.toPlainString(),"millimeters"}
		});
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of limitValue method, of class SandGrainSizeLimiterTransform.
	 */
	@Test
	public void testLimitValueMM() {
		log.info("limitValue");
		BigDecimal inVal = null;
		BigDecimal expResult = null;
		BigDecimal result = ValueRangeLimiterTransform.limitValue(inVal, LOWER_LIMIT_MM, UPPER_LIMIT_MM);
		assertEquals(expResult, result);
		
		inVal = new BigDecimal("0.000");
		expResult = new BigDecimal("0.0625");
		result = ValueRangeLimiterTransform.limitValue(inVal, LOWER_LIMIT_MM, UPPER_LIMIT_MM);
		assertEquals(expResult, result);
		
		inVal = new BigDecimal("0.630");
		expResult = new BigDecimal("0.630");
		result = ValueRangeLimiterTransform.limitValue(inVal, LOWER_LIMIT_MM, UPPER_LIMIT_MM);
		assertEquals(expResult, result);
		
		inVal = new BigDecimal("5.321");
		expResult = new BigDecimal("2.000");
		result = ValueRangeLimiterTransform.limitValue(inVal, LOWER_LIMIT_MM, UPPER_LIMIT_MM);
		assertEquals(expResult, result);
	}

	@Test
	public void testLimitValuePerc() {
		BigDecimal inVal = new BigDecimal("5.321");
		BigDecimal expResult = new BigDecimal("5.321");
		BigDecimal result = ValueRangeLimiterTransform.limitValue(inVal, LOWER_LIMIT_PERCENT, null);
		assertEquals(expResult, result);

		inVal = new BigDecimal("0.001");
		expResult = new BigDecimal("0.001");
		result = ValueRangeLimiterTransform.limitValue(inVal, LOWER_LIMIT_PERCENT, null);
		assertEquals(expResult, result);
		
		inVal = new BigDecimal("-5.321");
		expResult = new BigDecimal("0.000");
		result = ValueRangeLimiterTransform.limitValue(inVal, LOWER_LIMIT_PERCENT, null);
		assertEquals(expResult, result);
	}

	/**
	 * Test of transform method, of class ValueRangeLimiterTransform.
	 */
	@Test
	public void testTransform() throws SQLException {
		log.info("transform");
		ValueRangeLimiterTransform instance = new ValueRangeLimiterTransform(valueColumn, new BigDecimal[] {LOWER_LIMIT_MM, UPPER_LIMIT_MM});
		
		NudeFilter nf = new NudeFilter(Arrays.asList(new FilterStage[] {
			new FilterStageBuilder(sampleColGroup)
					.addTransform(valueColumn, instance)
					.buildFilterStage()
		}));
		
		ResultSet input = new IteratorWrappingResultSet(inputSampleDataset.iterator());
		ResultSet expected = new IteratorWrappingResultSet(expectedSampleDataset.iterator());
		ResultSet actual = nf.filter(input);
		assertTrue(ResultSetUtils.checkEqualRows(expected, actual));
	}
}
