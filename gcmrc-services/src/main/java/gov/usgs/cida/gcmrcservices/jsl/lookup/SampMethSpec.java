package gov.usgs.cida.gcmrcservices.jsl.lookup;

import gov.usgs.webservices.jdbc.spec.Spec;
import gov.usgs.webservices.jdbc.spec.mapping.ColumnMapping;
import gov.usgs.webservices.jdbc.spec.mapping.SearchMapping;
import gov.usgs.webservices.jdbc.spec.mapping.WhereClauseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class SampMethSpec extends Spec {
	private static final Logger log = LoggerFactory.getLogger(SampMethSpec.class);

	@Override
	public boolean setupAccess_DELETE() {
		return false;
	}

	@Override
	public boolean setupAccess_INSERT() {
		return false;
	}

	@Override
	public boolean setupAccess_READ() {
		return true;
	}

	@Override
	public boolean setupAccess_UPDATE() {
		return false;
	}

	@Override
	public ColumnMapping[] setupColumnMap() {
		return new ColumnMapping[] {
			new ColumnMapping(C_SAMP_METH_CD, S_SAMP_METH_CD),
			new ColumnMapping(C_SAMP_METH_DISP_NM, S_SAMP_METH_DISP_NM)
		};
	}

	@Override
	public String setupDocTag() {
		return "success";
	}

	@Override
	public String setupRowTag() {
		return "data";
	}

	@Override
	public SearchMapping[] setupSearchMap() {
		return new SearchMapping[] {
			new SearchMapping(S_SAMP_METH_CD, C_SAMP_METH_CD, null, WhereClauseType.equals, null, null, null),
			new SearchMapping(S_SAMP_METH_DISP_NM, C_SAMP_METH_DISP_NM, null, WhereClauseType.equals, null, null, null)
		};
	}

	@Override
	public String setupTableName() {
		return "SAMP_METH_TP";
	}
	
	public static final String C_SAMP_METH_CD = "SAMP_METH_CD";
	public static final String S_SAMP_METH_CD = "code";
	public static final String C_SAMP_METH_DISP_NM = "SAMP_METH_DISP_NM";
	public static final String S_SAMP_METH_DISP_NM = "displayName";

}
