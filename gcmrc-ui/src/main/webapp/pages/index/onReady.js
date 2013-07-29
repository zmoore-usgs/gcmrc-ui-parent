$(document).ready(function() {
	GCMRC.Page.createLargeMap();

	var $networkList = $('#networkList');
	GCMRC.Networks.keys(function(key) {
		if (!GCMRC.Networks[key].hidden && GCMRC.Networks[key].displayName) {
			$networkList.append('<h4>' + GCMRC.Networks[key].displayName + '</h4>' +
				'<ul><li><a href="' + CONFIG.relativePath + 'stations/' + key +'">Monitoring Stations</a></li>' +
				'<li><a href="' + CONFIG.relativePath + 'reaches/' + key +'">Sediment Budget Reaches</a></li></ul>')
		}
	});

})