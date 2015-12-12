package gov.usgs.cida.gcmrcservices.jsl.data;

import gov.usgs.webservices.jdbc.spec.GCMRCSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public abstract class DataSpec extends GCMRCSpec {
	private static final Logger log = LoggerFactory.getLogger(DataSpec.class);
	
	protected final String stationName;
	protected final ParameterCode parameterCode;
	protected final SpecOptions options;

	public DataSpec(String stationName, ParameterCode parameterCode, SpecOptions options) {
		String scode = stationName;
		if (null == scode) {
			scode = "00000000";
		}
		this.stationName = scode;
		
		ParameterCode pcode = parameterCode;
		if (null == pcode) {
			pcode = ParameterCode.parseParameterCode("err!00000");
		}
		this.parameterCode = pcode;
		
		SpecOptions specOptions = options;
		if (null == options) {
			specOptions = new SpecOptions();
		}
		this.options = specOptions;
		
		//Need to do this because I'm stupid and didn't allow for people to set
		//things before initialization ran.
		this.setTABLE_NAME(setupTableName());
		this.setCOLUMN_MAP(setupColumnMap());
		this.setSEARCH_MAP(setupSearchMap());
	}
		
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
	public String setupDocTag() {
		return "success";
	}

	@Override
	public String setupRowTag() {
		return "data";
	}
}
