package gov.usgs.cida.gcmrcservices.nude.time;

import static gov.usgs.cida.gcmrcservices.ResultSetUtils.*;
import gov.usgs.cida.nude.column.Column;
import gov.usgs.cida.nude.column.ColumnGrouping;
import gov.usgs.cida.nude.column.SimpleColumn;
import gov.usgs.cida.nude.resultset.inmemory.IteratorWrappingResultSet;
import gov.usgs.cida.nude.resultset.inmemory.TableRow;
import gov.usgs.cida.nude.time.DateRange;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class TimeSummaryFilteringTest {
	private static final Logger log = LoggerFactory.getLogger(TimeSummaryFilteringTest.class);
	
	@Test
	public void normalFilteringTest() throws Exception {
		ResultSet expected = new IteratorWrappingResultSet(normalFilteredDataset.iterator());
		ResultSet in = new IteratorWrappingResultSet(normalDataset.iterator());
		
		TimeSummaryFilteringPlanStep tsfps = new TimeSummaryFilteringPlanStep(normalColumnGrouping, Collections.EMPTY_SET, normalDateRange);
		
		ResultSet actual = tsfps.runStep(in);
		
		assertTrue(checkEqualRows(expected, actual));
	}
	
	@Test
	public void gappedFilteringTest() throws Exception {
		ResultSet expected = new IteratorWrappingResultSet(gappedFilteredDataset.iterator());
		ResultSet in = new IteratorWrappingResultSet(gappedDataset.iterator());
		
		TimeSummaryFilteringPlanStep tsfps = new TimeSummaryFilteringPlanStep(gappedColumnGrouping, Collections.EMPTY_SET, gappedDateRange);
		
		ResultSet actual = tsfps.runStep(in);
		
		assertTrue(checkEqualRows(expected, actual));
	}
	
	@Test
	public void exemptColumnFilteringTest() throws Exception {
		ResultSet expected = new IteratorWrappingResultSet(longTimeSmallDataFilteredDataset.iterator());
		ResultSet in = new IteratorWrappingResultSet(longTimeSmallDataDataset.iterator());
		
		Set<Column> exemptColumns = new HashSet<Column>();
		exemptColumns.add(ltsdExemptColumn);
		
		TimeSummaryFilteringPlanStep tsfps = new TimeSummaryFilteringPlanStep(longTimeSmallDataColumnGrouping, exemptColumns, longTimeSmallDataDateRange);
		
		ResultSet actual = tsfps.runStep(in);
		
		assertTrue(checkEqualRows(expected, actual));
	}
	
	protected static DateRange normalDateRange;
	protected static ColumnGrouping normalColumnGrouping;
	protected static Iterable<TableRow> normalDataset;
	protected static Iterable<TableRow> normalFilteredDataset;
	
	protected static DateRange gappedDateRange;
	protected static ColumnGrouping gappedColumnGrouping;
	protected static Iterable<TableRow> gappedDataset;
	protected static Iterable<TableRow> gappedFilteredDataset;
	
	protected static Column ltsdExemptColumn;
	protected static DateRange longTimeSmallDataDateRange;
	protected static ColumnGrouping longTimeSmallDataColumnGrouping;
	protected static Iterable<TableRow> longTimeSmallDataDataset;
	protected static Iterable<TableRow> longTimeSmallDataFilteredDataset;
	
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		normalDateRange = new DateRange(Arrays.asList(new DateTime[] {
				new DateTime(1990, 10, 28, 0, 0, 0, DateTimeZone.forOffsetHours(-7)), 
				new DateTime(1990, 10, 29, 0, 0, 0, DateTimeZone.forOffsetHours(-7)), 
				new DateTime(1990, 10, 30, 0, 0, 0, DateTimeZone.forOffsetHours(-7))
		}), Period.parse("PT24H"), Period.ZERO, Period.parse("PT24H"));
		
		normalColumnGrouping = new ColumnGrouping(Arrays.asList(new Column[] {
			new SimpleColumn("time"),
			new SimpleColumn("inst!00060-09403000")
		}));
		
		normalDataset = createTableRows(
				normalColumnGrouping,
				new String[][] {
					new String[] {"657097200000",	"19.09"}, //10-28 00:00
					new String[] {"657098100000",	"19.09"},
					new String[] {"657099000000",	"18.56"},
					new String[] {"657099900000",	"18.56"},
					new String[] {"657100800000",	"19.09"},
					new String[] {"657101700000",	"18.56"},
					new String[] {"657102600000",	"18.56"},
					new String[] {"657103500000",	"19.09"},
					new String[] {"657104400000",	"19.09"},
					new String[] {"657105300000",	"19.09"},
					new String[] {"657106200000",	"18.56"},
					new String[] {"657107100000",	"19.09"},
					new String[] {"657108000000",	"18.56"},
					new String[] {"657108900000",	"19.09"},
					new String[] {"657109800000",	"18.56"},
					new String[] {"657110700000",	"18.56"},
					new String[] {"657111600000",	"19.09"},
					new String[] {"657112500000",	"18.56"},
					new String[] {"657113400000",	"19.09"},
					new String[] {"657114300000",	"19.09"},
					new String[] {"657115200000",	"18.56"},
					new String[] {"657116100000",	"19.09"},
					new String[] {"657117000000",	"18.56"},
					new String[] {"657117900000",	"19.09"},
					new String[] {"657118800000",	"18.56"},
					new String[] {"657119700000",	"19.09"},
					new String[] {"657120600000",	"18.56"},
					new String[] {"657121500000",	"18.56"},
					new String[] {"657122400000",	"18.56"},
					new String[] {"657123300000",	"19.09"},
					new String[] {"657124200000",	"19.09"},
					new String[] {"657125100000",	"18.56"},
					new String[] {"657126000000",	"19.09"},
					new String[] {"657126900000",	"18.56"},
					new String[] {"657127800000",	"19.09"},
					new String[] {"657128700000",	"19.09"},
					new String[] {"657129600000",	"18.56"},
					new String[] {"657130500000",	"19.09"},
					new String[] {"657131400000",	"19.09"},
					new String[] {"657132300000",	"19.09"},
					new String[] {"657133200000",	"19.09"},
					new String[] {"657134100000",	"19.09"},
					new String[] {"657135000000",	"18.56"},
					new String[] {"657135900000",	"19.09"},
					new String[] {"657136800000",	"19.09"},
					new String[] {"657137700000",	"19.09"},
					new String[] {"657138600000",	"19.09"},
					new String[] {"657139500000",	"19.09"},
					new String[] {"657140400000",	"19.09"},
					new String[] {"657141300000",	"19.09"},
					new String[] {"657142200000",	"19.09"},
					new String[] {"657143100000",	"19.09"},
					new String[] {"657144000000",	"19.09"},
					new String[] {"657144900000",	"18.56"},
					new String[] {"657145800000",	"19.09"},
					new String[] {"657146700000",	"19.09"},
					new String[] {"657147600000",	"18.56"},
					new String[] {"657148500000",	"18.56"},
					new String[] {"657149400000",	"18.56"},
					new String[] {"657150300000",	"18.56"},
					new String[] {"657151200000",	"18.56"},
					new String[] {"657152100000",	"18.56"},
					new String[] {"657153000000",	"19.63"},
					new String[] {"657153900000",	"19.09"},
					new String[] {"657154800000",	"18.56"},
					new String[] {"657155700000",	"18.56"},
					new String[] {"657156600000",	"18.56"},
					new String[] {"657157500000",	"18.56"},
					new String[] {"657158400000",	"18.56"},
					new String[] {"657159300000",	"18.56"},
					new String[] {"657160200000",	"18.56"},
					new String[] {"657161100000",	"19.09"},
					new String[] {"657162000000",	"19.09"},
					new String[] {"657162900000",	"19.09"},
					new String[] {"657163800000",	"19.09"},
					new String[] {"657164700000",	"18.56"},
					new String[] {"657165600000",	"18.56"},
					new String[] {"657166500000",	"18.56"},
					new String[] {"657167400000",	"18.56"},
					new String[] {"657168300000",	"18.56"},
					new String[] {"657169200000",	"18.56"},
					new String[] {"657170100000",	"18.56"},
					new String[] {"657171000000",	"19.09"},
					new String[] {"657171900000",	"19.09"},
					new String[] {"657172800000",	"19.09"},
					new String[] {"657173700000",	"18.56"},
					new String[] {"657174600000",	"19.09"},
					new String[] {"657175500000",	"19.09"},
					new String[] {"657176400000",	"19.09"},
					new String[] {"657177300000",	"19.09"},
					new String[] {"657178200000",	"18.56"},
					new String[] {"657179100000",	"18.56"},
					new String[] {"657180000000",	"18.56"},
					new String[] {"657180900000",	"18.56"},
					new String[] {"657181800000",	"19.09"},
					new String[] {"657182700000",	"19.09"}, //10-28 23:45
					new String[] {"657183600000",	"18.56"}, //10-29 00:00
					new String[] {"657184500000",	"18.56"},
					new String[] {"657185400000",	"18.56"},
					new String[] {"657186300000",	"19.09"},
					new String[] {"657187200000",	"19.09"},
					new String[] {"657188100000",	"18.56"},
					new String[] {"657189000000",	"19.09"},
					new String[] {"657189900000",	"19.09"},
					new String[] {"657190800000",	"19.09"},
					new String[] {"657191700000",	"19.09"},
					new String[] {"657192600000",	"19.09"},
					new String[] {"657193500000",	"18.56"},
					new String[] {"657194400000",	"19.09"},
					new String[] {"657195300000",	"19.09"},
					new String[] {"657196200000",	"19.09"},
					new String[] {"657197100000",	"19.09"},
					new String[] {"657198000000",	"19.09"},
					new String[] {"657198900000",	"19.09"},
					new String[] {"657199800000",	"19.09"},
					new String[] {"657200700000",	"19.09"},
					new String[] {"657201600000",	"19.09"},
					new String[] {"657202500000",	"19.09"},
					new String[] {"657203400000",	"19.09"},
					new String[] {"657204300000",	"19.09"},
					new String[] {"657205200000",	"19.09"},
					new String[] {"657206100000",	"19.09"},
					new String[] {"657207000000",	"19.09"},
					new String[] {"657207900000",	"19.09"},
					new String[] {"657208800000",	"19.09"},
					new String[] {"657209700000",	"19.09"},
					new String[] {"657210600000",	"19.09"},
					new String[] {"657211500000",	"19.09"},
					new String[] {"657212400000",	"18.56"},
					new String[] {"657213300000",	"19.09"},
					new String[] {"657214200000",	"18.56"},
					new String[] {"657215100000",	"19.09"},
					new String[] {"657216000000",	"19.09"},
					new String[] {"657216900000",	"19.09"},
					new String[] {"657217800000",	"18.56"},
					new String[] {"657218700000",	"19.09"},
					new String[] {"657219600000",	"19.09"},
					new String[] {"657220500000",	"19.09"},
					new String[] {"657221400000",	"19.09"},
					new String[] {"657222300000",	"19.63"},
					new String[] {"657223200000",	"19.09"},
					new String[] {"657224100000",	"19.09"},
					new String[] {"657225000000",	"19.09"},
					new String[] {"657225900000",	"19.09"},
					new String[] {"657226800000",	"19.09"},
					new String[] {"657227700000",	"19.09"},
					new String[] {"657228600000",	"19.09"},
					new String[] {"657229500000",	"19.09"},
					new String[] {"657230400000",	"19.09"},
					new String[] {"657231300000",	"19.09"},
					new String[] {"657232200000",	"19.09"},
					new String[] {"657233100000",	"19.09"},
					new String[] {"657234000000",	"18.56"},
					new String[] {"657234900000",	"18.56"},
					new String[] {"657235800000",	"19.09"},
					new String[] {"657236700000",	"18.56"},
					new String[] {"657237600000",	"18.56"},
					new String[] {"657238500000",	"18.56"},
					new String[] {"657239400000",	"18.56"},
					new String[] {"657240300000",	"18.56"},
					new String[] {"657241200000",	"18.56"},
					new String[] {"657242100000",	"18.56"},
					new String[] {"657243000000",	"18.56"},
					new String[] {"657243900000",	"18.56"},
					new String[] {"657244800000",	"18.56"},
					new String[] {"657245700000",	"18.56"},
					new String[] {"657246600000",	"19.09"},
					new String[] {"657247500000",	"18.56"},
					new String[] {"657248400000",	"18.56"},
					new String[] {"657249300000",	"18.56"},
					new String[] {"657250200000",	"19.09"},
					new String[] {"657251100000",	"19.09"},
					new String[] {"657252000000",	"19.09"},
					new String[] {"657252900000",	"18.56"},
					new String[] {"657253800000",	"18.56"},
					new String[] {"657254700000",	"18.56"},
					new String[] {"657255600000",	"18.56"},
					new String[] {"657256500000",	"18.56"},
					new String[] {"657257400000",	"18.56"},
					new String[] {"657258300000",	"19.09"},
					new String[] {"657259200000",	"18.56"},
					new String[] {"657260100000",	"18.56"},
					new String[] {"657261000000",	"18.56"},
					new String[] {"657261900000",	"18.56"},
					new String[] {"657262800000",	"18.56"},
					new String[] {"657263700000",	"18.56"},
					new String[] {"657264600000",	"18.56"},
					new String[] {"657265500000",	"19.09"},
					new String[] {"657266400000",	"18.56"},
					new String[] {"657267300000",	"19.09"},
					new String[] {"657268200000",	"19.09"},
					new String[] {"657269100000",	"19.09"}, //10-29 23:45
					new String[] {"657270000000",	"19.09"}, //10-30 00:00
					new String[] {"657270900000",	"19.09"},
					new String[] {"657271800000",	"19.09"},
					new String[] {"657272700000",	"18.56"},
					new String[] {"657273600000",	"18.56"},
					new String[] {"657274500000",	"19.09"},
					new String[] {"657275400000",	"19.09"},
					new String[] {"657276300000",	"19.09"},
					new String[] {"657277200000",	"19.09"},
					new String[] {"657278100000",	"19.09"},
					new String[] {"657279000000",	"19.09"},
					new String[] {"657279900000",	"18.56"},
					new String[] {"657280800000",	"19.09"},
					new String[] {"657281700000",	"19.09"},
					new String[] {"657282600000",	"19.09"},
					new String[] {"657283500000",	"19.09"},
					new String[] {"657284400000",	"18.56"},
					new String[] {"657285300000",	"19.09"},
					new String[] {"657286200000",	"19.09"},
					new String[] {"657287100000",	"19.09"},
					new String[] {"657288000000",	"19.09"},
					new String[] {"657288900000",	"19.09"},
					new String[] {"657289800000",	"19.09"},
					new String[] {"657290700000",	"19.09"},
					new String[] {"657291600000",	"19.09"},
					new String[] {"657292500000",	"19.09"},
					new String[] {"657293400000",	"19.09"},
					new String[] {"657294300000",	"19.09"},
					new String[] {"657295200000",	"18.56"},
					new String[] {"657296100000",	"19.09"},
					new String[] {"657297000000",	"19.09"},
					new String[] {"657297900000",	"18.56"},
					new String[] {"657298800000",	"19.09"},
					new String[] {"657299700000",	"18.56"},
					new String[] {"657300600000",	"19.09"},
					new String[] {"657301500000",	"19.09"},
					new String[] {"657302400000",	"19.09"},
					new String[] {"657303300000",	"19.09"},
					new String[] {"657304200000",	"19.09"},
					new String[] {"657305100000",	"19.09"},
					new String[] {"657306000000",	"19.09"},
					new String[] {"657306900000",	"19.09"},
					new String[] {"657307800000",	"19.09"},
					new String[] {"657308700000",	"19.09"},
					new String[] {"657309600000",	"19.09"},
					new String[] {"657310500000",	"19.09"},
					new String[] {"657311400000",	"19.09"},
					new String[] {"657312300000",	"19.09"},
					new String[] {"657313200000",	"19.09"},
					new String[] {"657314100000",	"18.56"},
					new String[] {"657315000000",	"19.09"},
					new String[] {"657315900000",	"19.09"},
					new String[] {"657316800000",	"19.09"},
					new String[] {"657317700000",	"19.09"},
					new String[] {"657318600000",	"18.56"},
					new String[] {"657319500000",	"18.56"},
					new String[] {"657320400000",	"18.56"},
					new String[] {"657321300000",	"18.56"},
					new String[] {"657322200000",	"18.56"},
					new String[] {"657323100000",	"18.56"},
					new String[] {"657324000000",	"18.56"},
					new String[] {"657324900000",	"18.56"},
					new String[] {"657325800000",	"18.03"},
					new String[] {"657326700000",	"18.56"},
					new String[] {"657327600000",	"19.09"},
					new String[] {"657328500000",	"18.56"},
					new String[] {"657329400000",	"18.56"},
					new String[] {"657330300000",	"18.56"},
					new String[] {"657331200000",	"18.56"},
					new String[] {"657332100000",	"18.56"},
					new String[] {"657333000000",	"19.09"},
					new String[] {"657333900000",	"18.56"},
					new String[] {"657334800000",	"18.56"},
					new String[] {"657335700000",	"19.09"},
					new String[] {"657336600000",	"18.56"},
					new String[] {"657337500000",	"19.09"},
					new String[] {"657338400000",	"19.09"},
					new String[] {"657339300000",	"19.09"},
					new String[] {"657340200000",	"18.56"},
					new String[] {"657341100000",	"18.56"},
					new String[] {"657342000000",	"18.56"},
					new String[] {"657342900000",	"18.56"},
					new String[] {"657343800000",	"18.56"},
					new String[] {"657344700000",	"19.09"},
					new String[] {"657345600000",	"19.09"},
					new String[] {"657346500000",	"18.56"},
					new String[] {"657347400000",	"19.09"},
					new String[] {"657348300000",	"18.56"},
					new String[] {"657349200000",	"19.09"},
					new String[] {"657350100000",	"19.09"},
					new String[] {"657351000000",	"19.09"},
					new String[] {"657351900000",	"19.09"},
					new String[] {"657352800000",	"19.09"},
					new String[] {"657353700000",	"19.09"},
					new String[] {"657354600000",	"18.56"},
					new String[] {"657355500000",	"19.09"}
				});
		normalFilteredDataset = createTableRows(
				normalColumnGrouping,
				new String[][] {
					new String[] {"657099000000",	"18.56"},
					new String[] {"657153000000",	"19.63"},
					new String[] {"657183600000",	"18.56"},
					new String[] {"657222300000",	"19.63"},
					new String[] {"657270000000",	"19.09"},
					new String[] {"657325800000",	"18.03"}
				});
		
		gappedDateRange = new DateRange(Arrays.asList(new DateTime[] {
				new DateTime(1974, 04, 1, 0, 0, 0, DateTimeZone.forOffsetHours(-7)),
				new DateTime(1990, 10, 28, 0, 0, 0, DateTimeZone.forOffsetHours(-7)), 
				new DateTime(1990, 10, 29, 0, 0, 0, DateTimeZone.forOffsetHours(-7)), 
				new DateTime(1990, 10, 30, 0, 0, 0, DateTimeZone.forOffsetHours(-7))
		}), Period.parse("PT24H"), Period.ZERO, Period.parse("PT24H"));
		
		gappedColumnGrouping = new ColumnGrouping(Arrays.asList(new Column[] {
			new SimpleColumn("time"),
			new SimpleColumn("inst!00060-09403000")
		}));
		
		gappedDataset = createTableRows(
				gappedColumnGrouping,
				new String[][] {
					new String[] {"134059200000",	"24"},
					new String[] {"657057600000",	null}, //10-27 13:00
					new String[] {"657058500000",	"18.56"},
					new String[] {"657059400000",	"19.09"},
					new String[] {"657060300000",	"18.56"},
					new String[] {"657061200000",	"18.56"},
					new String[] {"657062100000",	"18.56"},
					new String[] {"657063000000",	"18.56"},
					new String[] {"657063900000",	"18.56"},
					new String[] {"657064800000",	"18.56"},
					new String[] {"657065700000",	"18.56"},
					new String[] {"657066600000",	"18.56"},
					new String[] {"657067500000",	"18.56"},
					new String[] {"657068400000",	"18.56"},
					new String[] {"657069300000",	"18.56"},
					new String[] {"657070200000",	"19.09"},
					new String[] {"657071100000",	"18.56"},
					new String[] {"657072000000",	"18.56"},
					new String[] {"657072900000",	"18.56"},
					new String[] {"657073800000",	"19.09"},
					new String[] {"657074700000",	"18.56"},
					new String[] {"657075600000",	"18.56"},
					new String[] {"657076500000",	"19.09"},
					new String[] {"657077400000",	"18.56"},
					new String[] {"657078300000",	"19.09"},
					new String[] {"657079200000",	"19.09"},
					new String[] {"657080100000",	"18.56"},
					new String[] {"657081000000",	"18.56"},
					new String[] {"657081900000",	"18.56"},
					new String[] {"657082800000",	"18.56"},
					new String[] {"657083700000",	"18.56"},
					new String[] {"657084600000",	"19.09"},
					new String[] {"657085500000",	"18.56"},
					new String[] {"657086400000",	"18.56"},
					new String[] {"657087300000",	"18.56"},
					new String[] {"657088200000",	"19.09"},
					new String[] {"657089100000",	"18.56"},
					new String[] {"657090000000",	"18.56"},
					new String[] {"657090900000",	"18.56"},
					new String[] {"657091800000",	"19.09"},
					new String[] {"657092700000",	"19.09"},
					new String[] {"657093600000",	"18.56"},
					new String[] {"657094500000",	"18.56"},
					new String[] {"657095400000",	"19.09"},
					new String[] {"657096300000",	"19.09"},
					new String[] {"657097200000",	"19.09"}, //10-28 00:00
					new String[] {"657098100000",	"19.09"},
					new String[] {"657099000000",	"18.56"},
					new String[] {"657099900000",	"18.56"},
					new String[] {"657100800000",	"19.09"},
					new String[] {"657101700000",	"18.56"},
					new String[] {"657102600000",	"18.56"},
					new String[] {"657103500000",	"19.09"},
					new String[] {"657104400000",	"19.09"},
					new String[] {"657105300000",	"19.09"},
					new String[] {"657106200000",	"18.56"},
					new String[] {"657107100000",	"19.09"},
					new String[] {"657108000000",	"18.56"},
					new String[] {"657108900000",	"19.09"},
					new String[] {"657109800000",	"18.56"},
					new String[] {"657110700000",	"18.56"},
					new String[] {"657111600000",	"19.09"},
					new String[] {"657112500000",	"18.56"},
					new String[] {"657113400000",	"19.09"},
					new String[] {"657114300000",	"19.09"},
					new String[] {"657115200000",	"18.56"},
					new String[] {"657116100000",	"19.09"},
					new String[] {"657117000000",	"18.56"},
					new String[] {"657117900000",	"19.09"},
					new String[] {"657118800000",	"18.56"},
					new String[] {"657119700000",	"19.09"},
					new String[] {"657120600000",	"18.56"},
					new String[] {"657121500000",	"18.56"},
					new String[] {"657122400000",	"18.56"},
					new String[] {"657123300000",	"19.09"},
					new String[] {"657124200000",	"19.09"},
					new String[] {"657125100000",	"18.56"},
					new String[] {"657126000000",	"19.09"},
					new String[] {"657126900000",	"18.56"},
					new String[] {"657127800000",	"19.09"},
					new String[] {"657128700000",	"19.09"},
					new String[] {"657129600000",	"18.56"},
					new String[] {"657130500000",	"19.09"},
					new String[] {"657131400000",	"19.09"},
					new String[] {"657132300000",	"19.09"},
					new String[] {"657133200000",	"19.09"},
					new String[] {"657134100000",	"19.09"},
					new String[] {"657135000000",	"18.56"},
					new String[] {"657135900000",	"19.09"},
					new String[] {"657136800000",	"19.09"},
					new String[] {"657137700000",	"19.09"},
					new String[] {"657138600000",	"19.09"},
					new String[] {"657139500000",	"19.09"},
					new String[] {"657140400000",	"19.09"},
					new String[] {"657141300000",	"19.09"},
					new String[] {"657142200000",	"19.09"},
					new String[] {"657143100000",	"19.09"},
					new String[] {"657144000000",	"19.09"},
					new String[] {"657144900000",	"18.56"},
					new String[] {"657145800000",	"19.09"},
					new String[] {"657146700000",	"19.09"},
					new String[] {"657147600000",	"18.56"},
					new String[] {"657148500000",	"18.56"},
					new String[] {"657149400000",	"18.56"},
					new String[] {"657150300000",	"18.56"},
					new String[] {"657151200000",	"18.56"},
					new String[] {"657152100000",	"18.56"},
					new String[] {"657153000000",	"19.63"},
					new String[] {"657153900000",	"19.09"},
					new String[] {"657154800000",	"18.56"},
					new String[] {"657155700000",	"18.56"},
					new String[] {"657156600000",	"18.56"},
					new String[] {"657157500000",	"18.56"},
					new String[] {"657158400000",	"18.56"},
					new String[] {"657159300000",	"18.56"},
					new String[] {"657160200000",	"18.56"},
					new String[] {"657161100000",	"19.09"},
					new String[] {"657162000000",	"19.09"},
					new String[] {"657162900000",	"19.09"},
					new String[] {"657163800000",	"19.09"},
					new String[] {"657164700000",	"18.56"},
					new String[] {"657165600000",	"18.56"},
					new String[] {"657166500000",	"18.56"},
					new String[] {"657167400000",	"18.56"},
					new String[] {"657168300000",	"18.56"},
					new String[] {"657169200000",	"18.56"},
					new String[] {"657170100000",	"18.56"},
					new String[] {"657171000000",	"19.09"},
					new String[] {"657171900000",	"19.09"},
					new String[] {"657172800000",	"19.09"},
					new String[] {"657173700000",	"18.56"},
					new String[] {"657174600000",	"19.09"},
					new String[] {"657175500000",	"19.09"},
					new String[] {"657176400000",	"19.09"},
					new String[] {"657177300000",	"19.09"},
					new String[] {"657178200000",	"18.56"},
					new String[] {"657179100000",	"18.56"},
					new String[] {"657180000000",	"18.56"},
					new String[] {"657180900000",	"18.56"},
					new String[] {"657181800000",	"19.09"},
					new String[] {"657182700000",	"19.09"}, //10-27 23:45
					new String[] {"657183600000",	"18.56"}, //10-29 00:00
					new String[] {"657184500000",	"18.56"},
					new String[] {"657185400000",	"18.56"},
					new String[] {"657186300000",	"19.09"},
					new String[] {"657187200000",	"19.09"},
					new String[] {"657188100000",	"18.56"},
					new String[] {"657189000000",	"19.09"},
					new String[] {"657189900000",	"19.09"},
					new String[] {"657190800000",	"19.09"},
					new String[] {"657191700000",	"19.09"},
					new String[] {"657192600000",	"19.09"},
					new String[] {"657193500000",	"18.56"},
					new String[] {"657194400000",	"19.09"},
					new String[] {"657195300000",	"19.09"},
					new String[] {"657196200000",	"19.09"},
					new String[] {"657197100000",	"19.09"},
					new String[] {"657198000000",	"19.09"},
					new String[] {"657198900000",	"19.09"},
					new String[] {"657199800000",	"19.09"},
					new String[] {"657200700000",	"19.09"},
					new String[] {"657201600000",	"19.09"},
					new String[] {"657202500000",	"19.09"},
					new String[] {"657203400000",	"19.09"},
					new String[] {"657204300000",	"19.09"},
					new String[] {"657205200000",	"19.09"},
					new String[] {"657206100000",	"19.09"},
					new String[] {"657207000000",	"19.09"},
					new String[] {"657207900000",	"19.09"},
					new String[] {"657208800000",	"19.09"},
					new String[] {"657209700000",	"19.09"},
					new String[] {"657210600000",	"19.09"},
					new String[] {"657211500000",	"19.09"},
					new String[] {"657212400000",	"18.56"},
					new String[] {"657213300000",	"19.09"},
					new String[] {"657214200000",	"18.56"},
					new String[] {"657215100000",	"19.09"},
					new String[] {"657216000000",	"19.09"},
					new String[] {"657216900000",	"19.09"},
					new String[] {"657217800000",	"18.56"},
					new String[] {"657218700000",	"19.09"},
					new String[] {"657219600000",	"19.09"},
					new String[] {"657220500000",	"19.09"},
					new String[] {"657221400000",	"19.09"},
					new String[] {"657222300000",	"19.63"},
					new String[] {"657223200000",	"19.09"},
					new String[] {"657224100000",	"19.09"},
					new String[] {"657225000000",	"19.09"},
					new String[] {"657225900000",	"19.09"},
					new String[] {"657226800000",	"19.09"},
					new String[] {"657227700000",	"19.09"},
					new String[] {"657228600000",	"19.09"},
					new String[] {"657229500000",	"19.09"},
					new String[] {"657230400000",	"19.09"},
					new String[] {"657231300000",	"19.09"},
					new String[] {"657232200000",	"19.09"},
					new String[] {"657233100000",	"19.09"},
					new String[] {"657234000000",	"18.56"},
					new String[] {"657234900000",	"18.56"},
					new String[] {"657235800000",	"19.09"},
					new String[] {"657236700000",	"18.56"},
					new String[] {"657237600000",	"18.56"},
					new String[] {"657238500000",	"18.56"},
					new String[] {"657239400000",	"18.56"},
					new String[] {"657240300000",	"18.56"},
					new String[] {"657241200000",	"18.56"},
					new String[] {"657242100000",	"18.56"},
					new String[] {"657243000000",	"18.56"},
					new String[] {"657243900000",	"18.56"},
					new String[] {"657244800000",	"18.56"},
					new String[] {"657245700000",	"18.56"},
					new String[] {"657246600000",	"19.09"},
					new String[] {"657247500000",	"18.56"},
					new String[] {"657248400000",	"18.56"},
					new String[] {"657249300000",	"18.56"},
					new String[] {"657250200000",	"19.09"},
					new String[] {"657251100000",	"19.09"},
					new String[] {"657252000000",	"19.09"},
					new String[] {"657252900000",	"18.56"},
					new String[] {"657253800000",	"18.56"},
					new String[] {"657254700000",	"18.56"},
					new String[] {"657255600000",	"18.56"},
					new String[] {"657256500000",	"18.56"},
					new String[] {"657257400000",	"18.56"},
					new String[] {"657258300000",	"19.09"},
					new String[] {"657259200000",	"18.56"},
					new String[] {"657260100000",	"18.56"},
					new String[] {"657261000000",	"18.56"},
					new String[] {"657261900000",	"18.56"},
					new String[] {"657262800000",	"18.56"},
					new String[] {"657263700000",	"18.56"},
					new String[] {"657264600000",	"18.56"},
					new String[] {"657265500000",	"19.09"},
					new String[] {"657266400000",	"18.56"},
					new String[] {"657267300000",	"19.09"},
					new String[] {"657268200000",	"19.09"},
					new String[] {"657269100000",	"19.09"} //10-29 23:45
				});
		
		gappedFilteredDataset = createTableRows(gappedColumnGrouping, 
				new String[][] {
					new String[] {"134059200000",	"24"},
					new String[] {"657099000000",	"18.56"},
					new String[] {"657153000000",	"19.63"},
					new String[] {"657183600000",	"18.56"},
					new String[] {"657222300000",	"19.63"}
				});
		
		List<DateTime> dts = new LinkedList<DateTime>();
		for (long i = 878626800000L; i < 1366182000000L ; i += 86400000L) {
			dts.add(new DateTime(i, DateTimeZone.forOffsetHours(-7)));
		}
		
		ltsdExemptColumn = new SimpleColumn("EWI!80222-09404200");
		longTimeSmallDataDateRange = new DateRange(dts, Period.parse("PT24H"), Period.ZERO, Period.parse("PT24H"));
		
		longTimeSmallDataColumnGrouping = new ColumnGrouping(Arrays.asList(new Column[] {
			new SimpleColumn("time"),
			ltsdExemptColumn
		}));
		
		longTimeSmallDataDataset = createTableRows(
				longTimeSmallDataColumnGrouping,
				new String[][] {
					new String[] {"997297800000", "131.38;139;151.34"},
					new String[] {"997908300000", "4293.99;4620;4946.01"},
					new String[] {"999637200000", "67.8;70.6;78.1"},
					new String[] {"1012421460000", "54.74;57.1;63.06"},
					new String[] {"1012853880000", "25.96;26.8;29.9"},
					new String[] {"1013200080000", "9.18;9.37;10.58"},
					new String[] {"1013547000000", "10.95;11.2;12.61"},
					new String[] {"1061225700000", "5822.96;6350;6877.04"}
				});
		longTimeSmallDataFilteredDataset = createTableRows(
				longTimeSmallDataColumnGrouping,
				new String[][] {
					new String[] {"997297800000", "131.38;139;151.34"},
					new String[] {"997908300000", "4293.99;4620;4946.01"},
					new String[] {"999637200000", "67.8;70.6;78.1"},
					new String[] {"1012421460000", "54.74;57.1;63.06"},
					new String[] {"1012853880000", "25.96;26.8;29.9"},
					new String[] {"1013200080000", "9.18;9.37;10.58"},
					new String[] {"1013547000000", "10.95;11.2;12.61"},
					new String[] {"1061225700000", "5822.96;6350;6877.04"}
				});
	}
}
