package gov.usgs.cida.gcmrcservices.nude.transform;

import gov.usgs.cida.nude.column.Column;
import gov.usgs.cida.nude.filter.ColumnTransform;
import gov.usgs.cida.nude.resultset.inmemory.TableRow;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BedSedimentTransform implements ColumnTransform {
	private static final Logger log = LoggerFactory.getLogger(BedSedimentTransform.class);
	
	protected final Column valueColumn;
	protected final Column conf95Column;
	
	public BedSedimentTransform(Column valueColumn, Column conf95Column) {
		this.valueColumn = valueColumn;
		this.conf95Column = conf95Column;
	}
	
	@Override
	public String transform(TableRow row) {
		String result = null;
		try {
			BigDecimal avgSizeValue = null;
			if (null != row.getValue(valueColumn)) {
				avgSizeValue = new BigDecimal(row.getValue(valueColumn));		
			}
			BigDecimal conf95Value = null;
			if (null != row.getValue(conf95Column)) {
				conf95Value = new BigDecimal(row.getValue(conf95Column));
			}
			BigDecimal lowerLimitValue = avgSizeValue.subtract(conf95Value, new MathContext(conf95Value.precision(), RoundingMode.HALF_EVEN));
			//zero out if negative
			if (lowerLimitValue.compareTo(BigDecimal.ZERO) < 0) {
				lowerLimitValue = BigDecimal.ZERO;
			}
			BigDecimal upperLimitValue = avgSizeValue.add(conf95Value, new MathContext(conf95Value.precision(), RoundingMode.HALF_EVEN));
		
			result = lowerLimitValue.toPlainString()+":"+avgSizeValue.toPlainString()+":"+upperLimitValue.toPlainString();
		} catch (Exception e) {
			log.trace("Could not parse ");
		}

		return result;
	}

}
