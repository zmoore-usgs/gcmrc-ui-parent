package gov.usgs.cida.gcmrcservices.jsl.data;

import static gov.usgs.cida.gcmrcservices.jsl.data.ParameterSpec.C_TSM_DT;
import gov.usgs.cida.gcmrcservices.column.ColumnMetadata;
import gov.usgs.cida.gcmrcservices.jsl.station.StationBSSpec;
import gov.usgs.cida.gcmrcservices.nude.BedSedAverageResultSet;
import gov.usgs.cida.gcmrcservices.nude.DBConnectorPlanStep;
import gov.usgs.cida.gcmrcservices.nude.Endpoint;
import gov.usgs.cida.gcmrcservices.nude.time.IntoMillisTransform;
import gov.usgs.cida.gcmrcservices.nude.time.OutOfMillisTransform;
import gov.usgs.cida.gcmrcservices.nude.transform.BedSedErrorBarTransform;
import gov.usgs.cida.gcmrcservices.nude.transform.SandGrainSizeLimiterTransform;
import gov.usgs.cida.nude.column.Column;
import gov.usgs.cida.nude.column.ColumnGrouping;
import gov.usgs.cida.nude.column.SimpleColumn;
import gov.usgs.cida.nude.filter.FilterStageBuilder;
import gov.usgs.cida.nude.filter.NudeFilter;
import gov.usgs.cida.nude.filter.NudeFilterBuilder;
import gov.usgs.webservices.jdbc.spec.SpecResponse;
import gov.usgs.webservices.jdbc.spec.mapping.ColumnMapping;
import gov.usgs.webservices.jdbc.spec.mapping.SearchMapping;
import gov.usgs.webservices.jdbc.spec.mapping.WhereClauseType;
import gov.usgs.webservices.jdbc.util.CleaningOption;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BedSedimentSpec extends DataSpec {

	private static final long serialVersionUID = 2263816089456993501L;
	private static final Logger log = LoggerFactory.getLogger(BedSedimentSpec.class);

	public BedSedimentSpec(String stationName, ParameterCode parameterCode, SpecOptions options) {
		super(stationName, parameterCode, options);
	}

	@Override
	public ColumnMapping[] setupColumnMap() {
		ColumnMapping[] result = null;
		
		if (null != this.stationName && null != this.parameterCode) {
			result = new ColumnMapping[] {
				new ColumnMapping(ParameterSpec.C_TSM_DT, ParameterSpec.S_TSM_DT),
				new ColumnMapping(C_SITE_NAME, S_SITE_NAME),
				new ColumnMapping(ColumnMetadata.createColumnName(this.stationName, this.parameterCode) + C_BED_VALUE, S_BED_VALUE),
				new ColumnMapping(ColumnMetadata.createColumnName(this.stationName, this.parameterCode) + C_SAMPLE_MASS, S_SAMPLE_MASS),
				new ColumnMapping(ColumnMetadata.createColumnName(this.stationName, this.parameterCode) + C_SAMPLE_SET, S_SAMPLE_SET)
			};
		} else {
			log.trace("setupColumnMap stationName=" + this.stationName + " parameterCode=" + this.parameterCode);
		}
		
		return result;
	}
	
	@Override
	public SearchMapping[] setupSearchMap() {
		SearchMapping[] result = new SearchMapping[] {
			new SearchMapping(ParameterSpec.S_SITE_NAME, C_SITE_NAME, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(Endpoint.BEGIN_KEYWORD, C_TSM_DT, null, WhereClauseType.special, CleaningOption.none, "TO_DATE(" + FIELD_NAME_KEY + ", 'YYYY-MM-DD\"T\"HH24:MI:SS') >= TO_DATE(" + USER_VALUE_KEY + ", 'YYYY-MM-DD\"T\"HH24:MI:SS')", null),
			new SearchMapping(Endpoint.END_KEYWORD, C_TSM_DT, null, WhereClauseType.special, CleaningOption.none, "TO_DATE(" + FIELD_NAME_KEY + ", 'YYYY-MM-DD\"T\"HH24:MI:SS') <= TO_DATE(" + USER_VALUE_KEY + ", 'YYYY-MM-DD\"T\"HH24:MI:SS')", null)
		};
		
		return result;
	}
	
	@Override
	public String setupTableName() {
		StringBuilder result = new StringBuilder();

		result.append("(");
		result.append("    SELECT");
		result.append("    TO_CHAR(VALUE_TABLE.BED_MEAS_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS') AS TSM_DT,");
		result.append("    VALUE_TABLE.SITE_NAME,");
		result.append("    VALUE_TABLE.BED_VALUE AS ").append(ColumnMetadata.createColumnName(this.stationName, this.parameterCode)).append(C_BED_VALUE).append(",");
		result.append("    MASS_TABLE.SAMPLE_MASS AS ").append(ColumnMetadata.createColumnName(this.stationName, this.parameterCode)).append(C_SAMPLE_MASS).append(",");
		result.append("    VALUE_TABLE.SAMPLE_SET AS ").append(ColumnMetadata.createColumnName(this.stationName, this.parameterCode)).append(C_SAMPLE_SET).append("");
		result.append("    FROM ( SELECT");
		result.append("        BM.BED_MEAS_DT,");
		result.append("        NVL(S.NWIS_SITE_NO, S.SHORT_NAME) AS SITE_NAME,");
		result.append("        BM.BED_VALUE,");
		result.append("        BM.SAMPLE_SET");
		result.append("        FROM BED_MATERIAL BM, SITE_STAR S, GROUP_NAME G");
		result.append("        WHERE BM.SITE_ID = S.SITE_ID");
		result.append("        AND BM.GROUP_ID = G.GROUP_ID");
		result.append("        AND BM.GROUP_ID IN (").append(String.join(",",StationBSSpec.BS_GROUP_ID_LIST)).append(")");

		if(this.parameterCode != null && this.parameterCode.groupName != null) {
			result.append("        AND G.NAME = '").append(cleanSql(this.parameterCode.groupName)).append("'");
		}

		result.append("    ) VALUE_TABLE ");
		result.append("    INNER JOIN (  SELECT");
		result.append("        BM.BED_MEAS_DT,");
		result.append("        NVL(S.NWIS_SITE_NO, S.SHORT_NAME) AS SITE_NAME,");
		result.append("        BM.BED_VALUE AS SAMPLE_MASS,");
		result.append("        BM.SAMPLE_SET");
		result.append("        FROM BED_MATERIAL BM, SITE_STAR S, GROUP_NAME G");
		result.append("        WHERE BM.SITE_ID = S.SITE_ID");
		result.append("        AND BM.GROUP_ID = G.GROUP_ID");
		result.append("        AND BM.GROUP_ID = 14");
		result.append("    ) MASS_TABLE");
		result.append("    ON MASS_TABLE.BED_MEAS_DT = VALUE_TABLE.BED_MEAS_DT ");
		result.append("        AND MASS_TABLE.SITE_NAME = VALUE_TABLE.SITE_NAME");
		result.append("        AND MASS_TABLE.SAMPLE_SET = VALUE_TABLE.SAMPLE_SET");
		result.append(") T_A_BED_SEDIMENT");
		
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
		if (obj instanceof BedSedimentSpec) {
			BedSedimentSpec rhs = (BedSedimentSpec) obj;
			return new EqualsBuilder()
					.append(this.stationName, rhs.stationName)
					.append(this.parameterCode, rhs.parameterCode)
					.isEquals();
		}
		return false;
	}

	@Override
	public SpecResponse readAction(Connection con) throws SQLException {
		SpecResponse result = null;
		SpecResponse superSR = super.readAction(con);
		
		String common = ColumnMetadata.createColumnName(this.stationName, this.parameterCode);
		
		final Column timeColumn = new SimpleColumn(ParameterSpec.C_TSM_DT);
		final Column sampleSetColumn = new SimpleColumn(common + C_SAMPLE_SET);
		final Column valueColumn = new SimpleColumn(common + C_BED_VALUE);
		final Column sampleMassColumn = new SimpleColumn(common + C_SAMPLE_MASS);
		final Column errorColumn = new SimpleColumn(common + "ERROR");
		final Column conf95Column = new SimpleColumn(common + "CONF95");
		
		ColumnGrouping cols = DBConnectorPlanStep.buildColumnGroupingFromSpec(this, timeColumn);
		NudeFilter prefilter = new NudeFilterBuilder(cols)
				.addFilterStage(new FilterStageBuilder(cols).addTransform(timeColumn, new IntoMillisTransform(timeColumn)).buildFilterStage())
				.buildFilter();
		
		ColumnGrouping colGroup = new ColumnGrouping(Arrays.asList(new Column[] {
				 timeColumn 
				,sampleSetColumn
				,valueColumn
				,sampleMassColumn
				,errorColumn
				,conf95Column
				}));
		
		ResultSet avg = new BedSedAverageResultSet(prefilter.filter(superSR.rset),
				colGroup,
				timeColumn, sampleSetColumn, valueColumn, sampleMassColumn, errorColumn, conf95Column);
		
		NudeFilter postfilter = new NudeFilterBuilder(colGroup)
			.addFilterStage(new FilterStageBuilder(colGroup).addTransform(valueColumn, new BedSedErrorBarTransform(valueColumn, conf95Column)).buildFilterStage())
			.addFilterStage(new FilterStageBuilder(colGroup).addTransform(valueColumn, new SandGrainSizeLimiterTransform(valueColumn)).buildFilterStage())
			.addFilterStage(new FilterStageBuilder(colGroup).addTransform(timeColumn, new OutOfMillisTransform(timeColumn)).buildFilterStage())
			.buildFilter();
		
		result = new SpecResponse(superSR.responseSpec, postfilter.filter(avg), superSR.fullRowCount, superSR.validationErrors);
		
		return result;
	}

	public static String cleanSql(String input) {
		if (input == null) {
			return null;
		}
		String output = input.trim();
		output = output.replace("'", "");
		output = output.replace("|", "");
		output = output.replace(";", "");

		return output;
	}
	
	public static final String C_SITE_ID = "SITE_ID";
	public static final String S_SITE_NAME = "site";
	public static final String C_SITE_NAME = "SITE_NAME";
	public static final String S_BED_VALUE = "bedValue";
	public static final String C_BED_VALUE = "";
	public static final String S_SAMPLE_MASS = "sampleMass";
	public static final String C_SAMPLE_MASS = "MASS";
	public static final String S_SAMPLE_SET = "sampleSet";
	public static final String C_SAMPLE_SET = "SET";
}
