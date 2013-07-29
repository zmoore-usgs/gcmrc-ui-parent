$(document).ready(function() {
	$("span.network-name").html(GCMRC.Networks[CONFIG.networkName].displayName);
	GCMRC.Page.createLargeMap();
	GCMRC.Page.addReaches($("#reachList"));
});