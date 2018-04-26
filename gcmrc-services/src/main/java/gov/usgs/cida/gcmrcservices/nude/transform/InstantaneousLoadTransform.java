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
 *
 * @author dmsibley
 */
public class InstantaneousLoadTransform implements ColumnTransform {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(InstantaneousLoadTransform.class);

	protected final Column dischargeColumn;
	protected final Column sedimentColumn;
	
	protected static final BigDecimal CFS_TO_M_PER_S_CONSTANT = new BigDecimal(0.3048).pow(3);

	public InstantaneousLoadTransform(Column dischargeColumn, Column sedimentColumn) {
		this.dischargeColumn = dischargeColumn;
		this.sedimentColumn = sedimentColumn;
	}
	
	@Override
	public String transform(TableRow row) {
		String result = null;
		
		String sedimentStr = row.getValue(sedimentColumn);
		String dischargeStr = row.getValue(dischargeColumn);
		
		if (StringUtils.isNotBlank(sedimentStr) && StringUtils.isNotBlank(dischargeStr)) {
			BigDecimal sediment_mg_per_L = new BigDecimal(sedimentStr);
			BigDecimal discharge_cfs = new BigDecimal(dischargeStr);
			
			int precision = (discharge_cfs.precision() < sediment_mg_per_L.precision()) ? discharge_cfs.precision() : sediment_mg_per_L.precision();
			
			BigDecimal discharge_m3_per_s = CFS_TO_METERS_PER_SECOND(discharge_cfs);
			
			BigDecimal discharge_L_per_s = discharge_m3_per_s.multiply(new BigDecimal(1000), new MathContext(precision, RoundingMode.HALF_EVEN));
			BigDecimal sediment_kg_per_L = sediment_mg_per_L.divide(new BigDecimal(1000000), new MathContext(precision, RoundingMode.HALF_EVEN));
			
			BigDecimal Qs_kg_per_s = sediment_kg_per_L.multiply(discharge_L_per_s, new MathContext(precision, RoundingMode.HALF_EVEN));
			
			result = Qs_kg_per_s.toPlainString();
		}
		
		return result;
	}
	
	public static BigDecimal CFS_TO_METERS_PER_SECOND(BigDecimal cfs) {
		BigDecimal result = null;
		
		if (null != cfs) {
			result = cfs.multiply(CFS_TO_M_PER_S_CONSTANT);
		}
		
		return result;
	}
}
