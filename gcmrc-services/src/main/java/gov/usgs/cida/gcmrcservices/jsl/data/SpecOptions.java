package gov.usgs.cida.gcmrcservices.jsl.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Options to the specs contained in the gcmrcservices.jsl.data packages.
 * These shouldn't be spec-specific, as in, they get applied across all specs per
 * request.  Options being applied to a spec does not change the .equals() or 
 * .hashcode() functionality.
 * @author dmsibley
 */
public class SpecOptions {
	private static final Logger log = LoggerFactory.getLogger(SpecOptions.class);
	
	/**
	 * GCMRC-298:  Apply a timelag to results only if we're not downloading the data.
	 * USE "Y" or "N" please
	 */
	public final String useLaggedTime;

	/*
	 * Defaults.
	 */
	public SpecOptions() {
		this.useLaggedTime = "N";
	}
	
	public SpecOptions(String useLaggedTime) {
		this.useLaggedTime = useLaggedTime;
	}
}
