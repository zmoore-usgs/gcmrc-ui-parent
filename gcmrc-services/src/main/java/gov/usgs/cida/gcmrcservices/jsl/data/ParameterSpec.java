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
				new ColumnMapping(C_TSM_DT, S_TSM_DT, ASCENDING_ORDER, S_TSM_DT, null, null, null, "TO_CHAR(" + C_TSM_DT + ", 'YYYY-MM-DD\"T\"HH24:MI:SS')", null, null),
				new ColumnMapping("s" + Math.abs(this.stationName.hashCode()) + "p" + Math.abs(this.parameterCode.hashCode()), S_FINAL_VALUE, ASCENDING_ORDER, S_FINAL_VALUE, null, null, null, C_FINAL_VALUE, null, null)
			};
		} else {
			log.trace("setupColumnMap stationName=" + this.stationName + " parameterCode=" + this.parameterCode);
		}
		
		return result;
	}

	@Override
	public SearchMapping[] setupSearchMap() {
		SearchMapping[] result = new SearchMapping[] {
			new SearchMapping(S_SITE_NAME, C_SITE_NAME, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(S_GROUP_NAME, C_GROUP_NAME, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(Endpoint.BEGIN_KEYWORD, C_TSM_DT, null, WhereClauseType.special, CleaningOption.none, FIELD_NAME_KEY + " >= TO_DATE(" + USER_VALUE_KEY + ", 'YYYY-MM-DD\"T\"HH24:MI:SS')", null),
			new SearchMapping(Endpoint.END_KEYWORD, C_TSM_DT, null, WhereClauseType.special, CleaningOption.none, FIELD_NAME_KEY + " <= TO_DATE(" + USER_VALUE_KEY + ", 'YYYY-MM-DD\"T\"HH24:MI:SS')", null)
		};
		
		return result;
	}

	@Override
	public String setupTableName() {
		StringBuilder result = new StringBuilder();
		
		result.append("  (SELECT");
		result.append("    MEASUREMENT_DATE AS TSM_DT,");
		result.append("    FINAL_VALUE,");
		result.append("    SITE_NAME,");
		result.append("    GROUP_NAME");
		result.append("  FROM");
		result.append("    TIME_SERIES_STAR");
		result.append("  LEFT OUTER JOIN");
		result.append("    (SELECT");
		result.append("      SITE_ID,");
		result.append("      CASE");
		result.append("        WHEN NWIS_SITE_NO IS NULL");
		result.append("        THEN SHORT_NAME");
		result.append("        ELSE NWIS_SITE_NO");
		result.append("      END AS SITE_NAME");
		result.append("    FROM");
		result.append("      SITE_STAR) SITE");
		result.append("  ON");
		result.append("    TIME_SERIES_STAR.SITE_ID = SITE.SITE_ID");
		result.append("  LEFT OUTER JOIN");
		result.append("    (SELECT");
		result.append("      GROUP_ID,");
		result.append("      NAME AS GROUP_NAME");
		result.append("    FROM");
		result.append("      GROUP_NAME) T_GROUP");
		result.append("  ON");
		result.append("    TIME_SERIES_STAR.GROUP_ID = T_GROUP.GROUP_ID");
		result.append(") T_A_MAIN");

		return result.toString();
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
	public static final String S_TSM_DT = "TSM_DT";
	public static final String C_FINAL_VALUE = "FINAL_VALUE";
	public static final String S_FINAL_VALUE = "FINAL_VALUE";
	public static final String C_SITE_NAME = "SITE_NAME";
	public static final String S_SITE_NAME = "SITE_NAME";
	public static final String C_GROUP_NAME = "GROUP_NAME";
	public static final String S_GROUP_NAME = "GROUP_NAME";
	
	
}
