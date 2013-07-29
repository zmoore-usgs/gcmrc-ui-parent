package gov.usgs.cida.gcmrcservices.jsl.data;

import gov.usgs.cida.gcmrcservices.nude.Endpoint;
import static gov.usgs.webservices.jdbc.spec.Spec.ASCENDING_ORDER;
import gov.usgs.webservices.jdbc.spec.mapping.ColumnMapping;
import gov.usgs.webservices.jdbc.spec.mapping.SearchMapping;
import gov.usgs.webservices.jdbc.spec.mapping.WhereClauseType;
import gov.usgs.webservices.jdbc.util.CleaningOption;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class ParameterSpec extends DataSpec {
	private static final Logger log = LoggerFactory.getLogger(ParameterSpec.class);

	public ParameterSpec(String stationName, ParameterCode parameterCode, SpecOptions options) {
		super(stationName, parameterCode, options);
	}

	@Override
	public ColumnMapping[] setupColumnMap() {
		ColumnMapping[] result = null;
		
		if (null != this.stationName && null != this.parameterCode) {
			result = new ColumnMapping[] {
				new ColumnMapping(C_TSM_DT, S_TSM_DT, ASCENDING_ORDER, S_TSM_DT, null, null, null, "CASE WHEN USE_LAGGED = 'true' THEN TO_CHAR(LAGGED_" + C_TSM_DT + ", 'YYYY-MM-DD\"T\"HH24:MI:SS') ELSE TO_CHAR(" + C_TSM_DT + ", 'YYYY-MM-DD\"T\"HH24:MI:SS') END", null, null),
				new ColumnMapping("s" + Math.abs(this.stationName.hashCode()) + "p" + Math.abs(this.parameterCode.hashCode()), S_FINAL_VAL, ASCENDING_ORDER, S_FINAL_VAL, null, null, null, C_FINAL_VAL, null, null)
			};
		} else {
			log.trace("setupColumnMap stationName=" + this.stationName + " parameterCode=" + this.parameterCode);
		}
		
		return result;
	}

	@Override
	public SearchMapping[] setupSearchMap() {
		SearchMapping[] result = new SearchMapping[] {
			new SearchMapping(S_FINAL_VAL, C_FINAL_VAL, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(S_SHORT_NM, C_SHORT_NM, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(S_SITE_NO, C_SITE_NO + "," + C_SHORT_NM + "," + C_TS_ID, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(Endpoint.BEGIN_KEYWORD, C_TSM_DT, null, WhereClauseType.special, CleaningOption.none, FIELD_NAME_KEY + " >= TO_DATE(" + USER_VALUE_KEY + ", 'YYYY-MM-DD\"T\"HH24:MI:SS')", null),
			new SearchMapping(Endpoint.END_KEYWORD, C_TSM_DT, null, WhereClauseType.special, CleaningOption.none, FIELD_NAME_KEY + " <= TO_DATE(" + USER_VALUE_KEY + ", 'YYYY-MM-DD\"T\"HH24:MI:SS')", null)
		};
		
		return result;
	}

	@Override
	public String setupTableName() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("(");
		sb.append(" SELECT ");
		sb.append("  SITE.SITE_SHORT_NM AS SHORT_NM,");
		sb.append("  SITE.NWIS_SITE_NO AS SITE_NO,");
		sb.append("  TS_MEAS.TS_MEAS_DT AS TSM_DT,");
		sb.append("  TS_MEAS.TS_MEAS_DT + NUMTODSINTERVAL(SUBSITE.TIME_LAG_CORR_SEC_VA, 'second') AS LAGGED_TSM_DT,");
		if (null != this.options) {
			sb.append("  '").append(this.options.useLaggedTime).append("' AS USE_LAGGED,");
		} else {
			log.trace("null options");
		}
		sb.append("  CASE WHEN TS_MEAS.MEAS_GRADE_CD = 'X' THEN -999");
		sb.append("  ELSE TS_MEAS.TS_MEAS_FINAL_VA END AS FINAL_VAL,");
		sb.append("  TO_CHAR(TS.TS_ID) AS TS_ID,");
		sb.append("  SUBSITE.TIME_LAG_CORR_SEC_VA AS DISPLAY_TIME_OFFSET");
		sb.append(" from ");
		sb.append("  INFO_PORTAL_TS_GROUP,");
		sb.append("  SITE, ");
		sb.append("  TS_GROUP, ");
		sb.append("  TS, ");
		sb.append("  TS_MEAS, ");
		sb.append("  SUBSITE ");
		sb.append(" where ");
		sb.append("  INFO_PORTAL_TS_GROUP.SITE_ID = TS_GROUP.SITE_ID ");
		sb.append("  and INFO_PORTAL_TS_GROUP.TS_GRP_NM = TS_GROUP.TS_GRP_NM");
		sb.append("  and INFO_PORTAL_TS_GROUP.PARM_CD = TS_GROUP.PARM_CD");
		sb.append("  and SITE.SITE_ID = TS_GROUP.SITE_ID ");
		sb.append("  and TS_GROUP.SITE_ID = TS.SITE_ID ");
		sb.append("  and TS_GROUP.TS_GRP_NM = TS.TS_GRP_NM ");
		sb.append("  and TS_GROUP.PARM_CD = TS.PARM_CD ");
		sb.append("  and TS.TS_ID = TS_MEAS.TS_ID ");
		sb.append("  and TS.SUBSITE_NM = SUBSITE.SUBSITE_NM ");
		sb.append("  and TS.SITE_ID = SUBSITE.SITE_ID ");
		sb.append("  and INFO_PORTAL_TS_GROUP.PRIORITY_VA <= 11");
		if (null != this.parameterCode) {
			if (null != this.parameterCode.pcode) {
				sb.append("  and TS_GROUP.PARM_CD = '").append(this.parameterCode.pcode).append("'");
			}
			if (null != this.parameterCode.tsGroupName) {
				sb.append("  and TS_GROUP.TS_GRP_NM = '").append(this.parameterCode.tsGroupName).append("'");
			}
		} else { 
			log.trace("null parameter code");
		}
		sb.append(" ) T_A_TSVIEW");
		
		return sb.toString();
	}

	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append("TODO I'm a " + this.stationName + " " + this.parameterCode.toString() + " param Spec!")
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) { return false; }
		if (obj == this) { return true; }
		if (obj instanceof ParameterSpec) {
			ParameterSpec rhs = (ParameterSpec) obj;
			return new EqualsBuilder()
					.append(this.stationName, rhs.stationName)
					.append(this.parameterCode, rhs.parameterCode)
					.isEquals();
		}
		return false;
	}
	
	public static final String C_TSM_DT = "TSM_DT";
	public static final String C_FINAL_VAL = "FINAL_VAL";
	public static final String C_SHORT_NM = "SHORT_NM";
	public static final String C_SITE_NO = "SITE_NO";
	public static final String C_TS_ID = "TS_ID";

	public static final String S_TSM_DT = "TSM_DT";
	public static final String S_FINAL_VAL = "FINAL_VAL";
	public static final String S_SHORT_NM = "SHORT_NM";
	public static final String S_SITE_NO = "SITE_NO";
	
	
}
