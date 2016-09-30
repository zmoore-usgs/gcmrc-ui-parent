package gov.usgs.webservices.jdbc.spec;

/**
 * Project subclass of Spec that adds a hint to Select queries.
 * 
 * @author eeverman
 *
 */
@SuppressWarnings("serial")
public abstract class GCMRCSpec extends Spec {

	/**
	 * This hint massively speeds up select queries on Oracle 12c.
	 * 
	 * Specifically, the Oracle 11 optimizer would apply the WHERE clause logic
	 * to the individual tables prior to merging into one large dataset.  Oracle
	 * 12 no longer does that if the tables are joined via 'OUTER'.  The result
	 * is that 12c is up to 30x slower for some of the larger star queries in
	 * the app.
	 * @See JIRA GCMON-337
	 * 
	 * IMPORTANT:  By inserting this hint here, it is applied to all queries in
	 * the app, possibly slowing other queries down.  If Oracle does fix this
	 * outer join issue, this hint should be removed.
	 * 
	 * @return The query hint.
	 */
	@Override
	protected String getSelectHint() {
		return "/*+ OPT_PARAM('optimizer_features_enable' '11.2.0.3') */";
	}
}