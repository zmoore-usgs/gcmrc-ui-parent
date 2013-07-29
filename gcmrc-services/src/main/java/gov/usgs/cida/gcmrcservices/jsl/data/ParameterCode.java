package gov.usgs.cida.gcmrcservices.jsl.data;

import com.google.common.base.Objects;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class ParameterCode {
	private static final Logger log = LoggerFactory.getLogger(ParameterCode.class);
	
	public final String pcode;
	public final String sampleMethod;
	public final String tsGroupName;

	public ParameterCode(String pcode, String sampleMethod, String tsGroupName) {
		this.pcode = pcode;
		this.sampleMethod = sampleMethod;
		this.tsGroupName = tsGroupName;
	}
	
	public static ParameterCode parseParameterCode(String parseThis) {
		ParameterCode result = null;
		
		String param = null;
		String sampleMethod = null;
		String tsGrpNm = null;
		if (null != parseThis) {
			String[] split = parseThis.split("!");
			if (null != split && 0 < split.length) {
				sampleMethod = split[0];
				if (1 < split.length) {
					param = split[1];
					if (2 < split.length) {
						tsGrpNm = split[2];
					}
				}
			}
		}
		result = new ParameterCode(param, sampleMethod, tsGrpNm);
		
		return result;
	}

	@Override
	public String toString() {
		String result = null;
		
		result = Objects.toStringHelper(ParameterCode.class)
				.add("pcode", this.pcode)
				.add("sampleMethod", this.sampleMethod)
				.add("tsGroupName", this.tsGroupName)
				.toString();
		
		return result;
	}

	@Override
	public int hashCode() {
		int result = new HashCodeBuilder()
				.append(this.pcode)
				.append(this.sampleMethod)
				.append(this.tsGroupName)
				.toHashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) { return false; }
		if (obj == this) { return true; }
		if (obj instanceof ParameterCode) {
			ParameterCode rhs = (ParameterCode) obj;
			return new EqualsBuilder()
					.append(this.pcode, rhs.pcode)
					.append(this.sampleMethod, rhs.sampleMethod)
					.append(this.tsGroupName, rhs.tsGroupName)
					.isEquals();
		}
		return false;
	}
}
