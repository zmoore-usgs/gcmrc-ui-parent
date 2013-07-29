package gov.usgs.cida.gcmrcservices.nude.transform;

import gov.usgs.cida.nude.column.Column;
import gov.usgs.cida.nude.filter.ColumnTransform;
import gov.usgs.cida.nude.resultset.inmemory.TableRow;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Midpoint method
 * @author dmsibley
 */
public class IntegratingBySecondTransform implements ColumnTransform {
	private static final Logger log = LoggerFactory.getLogger(IntegratingBySecondTransform.class);
	
	protected final Column col;
	
	protected BigDecimal sum;
	protected int precision = 0;

	protected static final Long SECOND = 1000L;
	protected Long lastTime;
	protected BigDecimal lastVal;
	
	public IntegratingBySecondTransform(Column col) {
		this.col = col;
		this.sum = BigDecimal.ZERO;
		this.lastTime = null;
		this.lastVal = null;
	}
	
	@Override
	public String transform(TableRow row) {
		String result = null;
		
		String timeStr = row.getValue(row.getColumns().getPrimaryKey());
		String valStr = row.getValue(col);
		if (StringUtils.isNotBlank(valStr)) {
			Long time = Long.parseLong(timeStr); //TODO not completely safe
			BigDecimal val = new BigDecimal(valStr);
			
			if (null != lastTime && null != lastVal) {
				this.precision = (this.sum.precision() > val.precision()) ? this.sum.precision() : val.precision();
				
				BigDecimal intervalMillis = new BigDecimal(time).subtract(new BigDecimal(this.lastTime));
				BigDecimal intervalSec = intervalMillis.divide(new BigDecimal(SECOND), MathContext.DECIMAL64);
				
				BigDecimal twoPtAvg = (val.add(lastVal, new MathContext(precision, RoundingMode.HALF_EVEN)))
						.divide(new BigDecimal(2), new MathContext(precision, RoundingMode.HALF_EVEN));
				
				BigDecimal integrated = twoPtAvg.multiply(intervalSec, new MathContext(precision, RoundingMode.HALF_EVEN));
				
				this.sum = this.sum.add(integrated, new MathContext(precision, RoundingMode.HALF_EVEN));
			}
			this.lastTime = time;
			this.lastVal = val;
		}
		
		result = this.sum.toPlainString();
		
		return result;
	}

}
