package gov.usgs.cida.gcmrcservices.nude.transform;

import gov.usgs.cida.nude.column.Column;
import gov.usgs.cida.nude.filter.ColumnTransform;
import gov.usgs.cida.nude.resultset.inmemory.TableRow;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class BedSedErrorBarTransform implements ColumnTransform {
	private static final Logger log = LoggerFactory.getLogger(BedSedErrorBarTransform.class);
	private final static String DELIMITER = ";";

	protected final Column valueColumn;
	protected final Column conf95Column;

	public BedSedErrorBarTransform(Column valueColumn, Column conf95Column) {
		this.valueColumn = valueColumn;
		this.conf95Column = conf95Column;
	}
	
	@Override
	public String transform(TableRow tr) {
		String result = null;
	
		//Calculate upper and lower 95% limits
		try {
			BigDecimal avgSizeValue = null;
			if (null != tr.getValue(valueColumn)) {
				avgSizeValue = new BigDecimal(tr.getValue(valueColumn));		
			}
			BigDecimal conf95Value = null;
			if (null != tr.getValue(conf95Column)) {
				conf95Value = new BigDecimal(tr.getValue(conf95Column));
			}
			BigDecimal lowerConfValue = avgSizeValue.subtract(conf95Value, new MathContext(conf95Value.precision(), RoundingMode.HALF_EVEN));
			String lowerConfResult = null;
			if (null != lowerConfValue) {
				lowerConfResult = lowerConfValue.toPlainString();
			}
			
			BigDecimal upperConfValue = avgSizeValue.add(conf95Value, new MathContext(conf95Value.precision(), RoundingMode.HALF_EVEN));
			String upperConfResult = null;
			if (null != upperConfValue) {
				upperConfResult = upperConfValue.toPlainString();
			}
			
			result = lowerConfResult + DELIMITER + avgSizeValue.toPlainString() + DELIMITER + upperConfResult;
		} catch (Exception e) {
			log.trace("could not calculate upper and lower 95% limits");
		}
		
		return result;
	}

}
