package gov.usgs.cida.gcmrcservices.jsl.data;

import gov.usgs.cida.gcmrcservices.column.ColumnMetadata;
import gov.usgs.cida.gcmrcservices.nude.Endpoint;
import gov.usgs.webservices.jdbc.spec.mapping.ColumnMapping;
import gov.usgs.webservices.jdbc.spec.mapping.SearchMapping;
import gov.usgs.webservices.jdbc.spec.mapping.WhereClauseType;
import gov.usgs.webservices.jdbc.util.CleaningOption;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BedMaterialSpec extends DataSpec {

	private static final long serialVersionUID = 2263816089456993501L;
	private static final Logger log = LoggerFactory.getLogger(BedMaterialSpec.class);

	public BedMaterialSpec(String stationName, ParameterCode parameterCode, SpecOptions options) {
		super(stationName, parameterCode, options);
	}

	@Override
	public ColumnMapping[] setupColumnMap() {
		ColumnMapping[] result = null;
		
		if (null != this.stationName && null != this.parameterCode) {
			result = new ColumnMapping[] {
				new ColumnMapping(ParameterSpec.C_TSM_DT, ParameterSpec.S_TSM_DT, ASCENDING_ORDER, ParameterSpec.S_TSM_DT, null, null, null, null, null, null),
				new ColumnMapping(C_SITE_NAME, S_SITE_NAME),
				new ColumnMapping(ColumnMetadata.createColumnName(this.stationName, this.parameterCode), S_BED_VALUE, ASCENDING_ORDER, S_BED_VALUE, null, null, null, null, null, null),
				new ColumnMapping(C_GROUP_NAME, S_GROUP_NAME)
			};
		} else {
			log.trace("setupColumnMap stationName=" + this.stationName + " parameterCode=" + this.parameterCode);
		}
		
		return result;
	}
	
	@Override
	public SearchMapping[] setupSearchMap() {
		SearchMapping[] result = new SearchMapping[] {
			new SearchMapping(Endpoint.BEGIN_KEYWORD, C_SAMPLE_START_DT, null, WhereClauseType.special, CleaningOption.none, FIELD_NAME_KEY + " >= TO_DATE(" + USER_VALUE_KEY + ", 'YYYY-MM-DD\"T\"HH24:MI:SS')", null),
			new SearchMapping(Endpoint.END_KEYWORD, C_SAMPLE_START_DT, null, WhereClauseType.special, CleaningOption.none, FIELD_NAME_KEY + " <= TO_DATE(" + USER_VALUE_KEY + ", 'YYYY-MM-DD\"T\"HH24:MI:SS')", null)
		};
		
		return result;
	}
	
	@Override
	public String setupTableName() {
		StringBuilder result = new StringBuilder();

		result.append("(");
		result.append("  SELECT BM.SAMPLE_SET,");
		result.append("  TO_CHAR(BM.BED_MEAS_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS') AS TSM_DT,");
		result.append("  NVL(S.NWIS_SITE_NO, S.SHORT_NAME) AS SITE_NAME,");
		result.append("  BM.BED_MEAS_DT AS SAMP_START_DT,");
		result.append("  BM.BED_VALUE AS " + ColumnMetadata.createColumnName(this.stationName, this.parameterCode) + ",");
		result.append("  BM.GROUP_ID,");
		result.append("  G.NAME AS GROUP_NAME");
		result.append(" FROM BED_MATERIAL BM,");
		result.append("  SITE_STAR S,");
		result.append("  GROUP_NAME G");
		result.append(" WHERE BM.SITE_ID         = S.SITE_ID");
		result.append("  AND BM.GROUP_ID         = G.GROUP_ID");
		result.append("  AND G.GROUP_ID          = 15");
		result.append(") T_A_BED_MATERIAL");

		return result.toString();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append("TODO I'm a " + this.stationName + " station " + this.parameterCode.toString() + " Bed Material Spec!")
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) { return false; }
		if (obj == this) { return true; }
		if (obj instanceof BedMaterialSpec) {
			BedMaterialSpec rhs = (BedMaterialSpec) obj;
			return new EqualsBuilder()
					.append(this.stationName, rhs.stationName)
					.append(this.parameterCode, rhs.parameterCode)
					.isEquals();
		}
		return false;
	}
	
	public static final String C_SITE_ID = "SITE_ID";
	public static final String C_SAMPLE_START_DT = "SAMP_START_DT";
	public static final String S_SAMPLE_START_DT = "SAMP_START_DT";
	
	public static final String S_SITE_NAME = "site";
	public static final String C_SITE_NAME = "SITE_NAME";
	public static final String S_BED_VALUE = "bedValue";
	public static final String C_BED_VALUE = "BED_VALUE";
	public static final String S_GROUP_NAME = "groupName";
	public static final String C_GROUP_NAME = "GROUP_NAME";
	
}
