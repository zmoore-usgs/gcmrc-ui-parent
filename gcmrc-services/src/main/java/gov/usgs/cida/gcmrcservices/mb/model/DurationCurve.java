package gov.usgs.cida.gcmrcservices.mb.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley, zmoore
 */
public class DurationCurve {
	private static final Logger log = LoggerFactory.getLogger(DurationCurve.class);
	
	private String binNumber;
	private String cumulativeBinPerc;
	private String binValue;
	private String inBinMinutes;
	private String cumulativeInBinMinutes;
	private String lowBound;
	private String highBound;

	public DurationCurve() {
		this.binNumber = null;
		this.cumulativeBinPerc = null;
		this.binValue = null;
		this.inBinMinutes = null;
		this.cumulativeInBinMinutes = null;
		this.lowBound = null;
		this.highBound = null;
	}

	public DurationCurve(String binNumber, String cumulativeBinPerc, String binValue, String inBinMinutes, String cumulativeInBinMinutes, String lowBound, String highBound) {
		this.binNumber = binNumber;
		this.cumulativeBinPerc = cumulativeBinPerc;
		this.binValue = binValue;
		this.inBinMinutes = inBinMinutes;
		this.cumulativeInBinMinutes = cumulativeInBinMinutes;
		this.lowBound = lowBound;
		this.highBound = highBound;
	}

	public String getBinNumber() {
		return binNumber;
	}
	
	public String getCumulativeBinPerc() {
		return cumulativeBinPerc;
	}
	
	public String getBinValue() {
		return binValue;
	}
	
	public String getInBinMinutes() {
		return inBinMinutes;
	}
	
	public String getCumulativeInBinMinutes() {
		return cumulativeInBinMinutes;
	}
	
	public String getLowBound() {
		return lowBound;
	}
	
	public String getHighBound() {
		return highBound;
	}
}
