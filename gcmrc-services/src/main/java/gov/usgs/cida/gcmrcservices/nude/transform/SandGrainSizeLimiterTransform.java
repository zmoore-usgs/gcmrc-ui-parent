package gov.usgs.cida.gcmrcservices.nude.transform;

import gov.usgs.cida.nude.column.Column;
import gov.usgs.cida.nude.filter.ColumnTransform;
import gov.usgs.cida.nude.resultset.inmemory.TableRow;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class SandGrainSizeLimiterTransform implements ColumnTransform {
	private static final Logger log = LoggerFactory.getLogger(SandGrainSizeLimiterTransform.class);
	private final static String DELIMITER = ";";

	protected static final BigDecimal LOWER_LIMIT = new BigDecimal("0.0625");
	protected static final BigDecimal UPPER_LIMIT = new BigDecimal("2.000");
	
	protected final Column inColumn;

	public SandGrainSizeLimiterTransform(Column inColumn) {
		this.inColumn = inColumn;
	}
	
	public static BigDecimal limitValue(BigDecimal inVal) {
		BigDecimal result = inVal;
		if (null != inVal) {
			if (LOWER_LIMIT.compareTo(inVal) > 0) {
				result = LOWER_LIMIT;
			} else if (UPPER_LIMIT.compareTo(inVal) < 0) {
				result = UPPER_LIMIT;
			}
		}
		return result;
	}
	
	@Override
	public String transform(TableRow tr) {
		String result = null;
		
		try {
			result = tr.getValue(inColumn);
			String inStr = tr.getValue(inColumn);
			List<String> values = Arrays.asList(StringUtils.split(inStr, DELIMITER));
			List<String> results = new LinkedList<>();
			for (String val : values) {
				BigDecimal inVal = new BigDecimal(val);
				results.add(limitValue(inVal).toPlainString());
			}
			result = StringUtils.join(results, DELIMITER);
		} catch (Exception e) {
			log.trace("Could not limit value", e);
		}
		
		return result;
	}

}
