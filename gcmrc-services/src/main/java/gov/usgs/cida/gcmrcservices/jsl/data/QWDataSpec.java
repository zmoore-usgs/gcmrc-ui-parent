package gov.usgs.cida.gcmrcservices.jsl.data;

import gov.usgs.cida.gcmrcservices.nude.Endpoint;
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
public class QWDataSpec extends DataSpec {

	private static final Logger log = LoggerFactory.getLogger(QWDataSpec.class);

	public QWDataSpec(String stationName, ParameterCode parameterCode, SpecOptions options) {
		super(stationName, parameterCode, options);
	}

	@Override
	public ColumnMapping[] setupColumnMap() {
		ColumnMapping[] result = null;
		
		if (null != this.stationName && null != this.parameterCode) {
			result = new ColumnMapping[] {
				new ColumnMapping(C_SAMPLE_ID, S_SAMPLE_ID),
				new ColumnMapping(ParameterSpec.C_TSM_DT, ParameterSpec.S_TSM_DT, ASCENDING_ORDER, ParameterSpec.S_TSM_DT, null, null, null, "CASE WHEN USE_LAGGED = 'true' THEN TO_CHAR(" + C_LAGGED_SAMPLE_START_DT + ", 'YYYY-MM-DD\"T\"HH24:MI:SS') ELSE TO_CHAR(" + C_SAMPLE_START_DT + ", 'YYYY-MM-DD\"T\"HH24:MI:SS') END", null, null),
				new ColumnMapping("s" + Math.abs(this.stationName.hashCode()) + "p" + Math.abs(this.parameterCode.hashCode()), S_RESULT_VA, ASCENDING_ORDER, S_RESULT_VA, null, null, null, "CASE WHEN TOTAL_95CONF_VA IS NOT NULL THEN (CASE WHEN ERRORBAR_LOWER_VA < 0 THEN 0 ELSE ERRORBAR_LOWER_VA END) || ';' || RESULT_VA || ';' || ERRORBAR_UPPER_VA ELSE TO_CHAR(RESULT_VA) END", null, null),
				new ColumnMapping(C_ERRORBAR_UPPER_VA, S_ERRORBAR_UPPER_VA),
				new ColumnMapping(C_ERRORBAR_LOWER_VA, S_ERRORBAR_LOWER_VA),
				new ColumnMapping(C_SAMP_METH_CD, S_SAMP_METH_CD),
				new ColumnMapping(C_TOTAL_95CONF_VA, S_TOTAL_95CONF_VA),
				new ColumnMapping(C_LAB_BIAS_CORR_VA, S_LAB_BIAS_CORR_VA),
				new ColumnMapping(C_SITE_NO, null),
				new ColumnMapping(C_SHORT_NM, null),
				new ColumnMapping(C_PARM_CD, null)
			};
		} else {
			log.trace("setupColumnMap stationName=" + this.stationName + " parameterCode=" + this.parameterCode);
		}
		
		return result;
	}

	@Override
	public SearchMapping[] setupSearchMap() {
		SearchMapping[] result = new SearchMapping[] {
			new SearchMapping(ParameterSpec.S_SITE_NO, C_SITE_NO + "," + C_SHORT_NM, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(S_PARM_CD, C_PARM_CD, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(Endpoint.BEGIN_KEYWORD, C_SAMPLE_START_DT, null, WhereClauseType.special, CleaningOption.none, FIELD_NAME_KEY + " >= TO_DATE(" + USER_VALUE_KEY + ", 'YYYY-MM-DD\"T\"HH24:MI:SS')", null),
			new SearchMapping(Endpoint.END_KEYWORD, C_SAMPLE_START_DT, null, WhereClauseType.special, CleaningOption.none, FIELD_NAME_KEY + " <= TO_DATE(" + USER_VALUE_KEY + ", 'YYYY-MM-DD\"T\"HH24:MI:SS')", null)
		};
		
		return result;
	}

	@Override
	public String setupTableName() {
		StringBuilder sb = new StringBuilder();

		sb.append("(SELECT QWS.SAMPLE_ID, ");
		sb.append("QWS.SAMPLE_START_DT AS SAMP_START_DT, ");
		sb.append("QWS.SAMPLE_START_DT + NUMTODSINTERVAL(SUBSITE.TIME_LAG_CORR_SEC_VA, 'second') AS LAGGED_SAMP_START_DT, ");
		if (null != this.options) {
			sb.append("'").append(this.options.useLaggedTime).append("' AS USE_LAGGED, ");
		} else {
			log.trace("null options");
		}
		sb.append("QWR.RESULT_VA, ");
		sb.append("CASE WHEN QWR.LAB_BIAS_CORR_VA IS NULL THEN QWR.RESULT_VA + QWR.TOTAL_95CONF_VA ");
		sb.append("ELSE QWR.RESULT_VA + QWR.TOTAL_95CONF_VA + QWR.LAB_BIAS_CORR_VA ");
		sb.append("END AS ERRORBAR_UPPER_VA, ");
		sb.append("CASE WHEN QWR.LAB_BIAS_CORR_VA IS NULL THEN QWR.RESULT_VA - QWR.TOTAL_95CONF_VA ");
		sb.append("ELSE QWR.RESULT_VA - QWR.TOTAL_95CONF_VA + QWR.LAB_BIAS_CORR_VA ");
		sb.append("END AS ERRORBAR_LOWER_VA, ");
		sb.append("QWS.SAMP_METH_CD, QWR.TOTAL_95CONF_VA, QWR.LAB_BIAS_CORR_VA, QWS.SITE_ID, QWR.PARM_CD, SITE.NWIS_SITE_NO, SITE.SITE_SHORT_NM, ");
		sb.append("SUBSITE.SUBSITE_NM, SUBSITE.TIME_LAG_CORR_SEC_VA AS DISPLAY_TIME_OFFSET ");
		sb.append("FROM QW_SAMPLE QWS, QW_RESULT QWR, SITE, SUBSITE ");
		sb.append("WHERE QWS.SAMPLE_ID=QWR.SAMPLE_ID ");
		sb.append("AND SITE.SITE_ID = QWS.SITE_ID ");
		sb.append("AND SUBSITE.SITE_ID = QWS.SITE_ID ");
		sb.append("AND SUBSITE.SUBSITE_NM = QWS.SUBSITE_NM ");
		if (null != this.parameterCode) {
			sb.append("AND QWS.ANL_STAT_CD<>'X' AND SAMP_METH_CD='").append(this.parameterCode.sampleMethod).append("' ");
			sb.append("AND QWR.ANL_ENT_CD='GCMRRSCH' AND ANL_SCHED_NM='MassPsdXsecCorr' ");
			sb.append("AND QWR.PARM_CD='").append(this.parameterCode.pcode).append("' ");
		}
		sb.append(") T_A_LDVIEW");

		return sb.toString();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append("TODO I'm a " + this.stationName + " station " + this.parameterCode.toString() + " Lab Data Spec!")
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) { return false; }
		if (obj == this) { return true; }
		if (obj instanceof QWDataSpec) {
			QWDataSpec rhs = (QWDataSpec) obj;
			return new EqualsBuilder()
					.append(this.stationName, rhs.stationName)
					.append(this.parameterCode, rhs.parameterCode)
					.isEquals();
		}
		return false;
	}
	
	public static final String C_SAMPLE_ID = "SAMPLE_ID";
	public static final String C_SAMPLE_START_DT = "SAMP_START_DT";
	public static final String C_LAGGED_SAMPLE_START_DT = "LAGGED_SAMP_START_DT";
	public static final String C_RESULT_VA = "RESULT_VA";
	public static final String C_ERRORBAR_UPPER_VA = "ERRORBAR_UPPER_VA";
	public static final String C_ERRORBAR_LOWER_VA = "ERRORBAR_LOWER_VA";
	public static final String C_PLOT_COLOR_CD = "PLOT_COLOR_CD";
	public static final String C_SAMP_METH_CD = "SAMP_METH_CD";
	public static final String C_TOTAL_95CONF_VA = "TOTAL_95CONF_VA";
	public static final String C_LAB_BIAS_CORR_VA = "LAB_BIAS_CORR_VA";
	public static final String C_SITE_ID = "SITE_ID";
	public static final String C_SITE_NO = "NWIS_SITE_NO";
	public static final String C_SHORT_NM = "SITE_SHORT_NM";
	public static final String C_PARM_CD = "PARM_CD";
	
	public static final String S_SAMPLE_ID = "SAMPLE_ID";
	public static final String S_SAMPLE_START_DT = "SAMP_START_DT";
	public static final String S_LAGGED_SAMPLE_START_DT = "LAGGED_SAMP_START_DT";
	public static final String S_RESULT_VA = "RESULT_VA";
	public static final String S_ERRORBAR_UPPER_VA = "ERRORBAR_UPPER_VA";
	public static final String S_ERRORBAR_LOWER_VA = "ERRORBAR_LOWER_VA";
	public static final String S_PLOT_COLOR_CD = "PLOT_COLOR_CD";
	public static final String S_SAMP_METH_CD = "SAMP_METH_CD";
	public static final String S_TOTAL_95CONF_VA = "TOTAL_95CONF_VA";
	public static final String S_LAB_BIAS_CORR_VA = "LAB_BIAS_CORR_VA";
	public static final String S_SITE_ID = "SITE_ID";
	public static final String S_SITE_NO = "NWIS_SITE_NO";
	public static final String S_SHORT_NM = "SITE_SHORT_NM";
	public static final String S_PARM_CD = "PARM_CD";
}
