$(document).ready(function() {
	GCMRC.Page.createLargeMap();

	var $networkList = $('#networkList');
	GCMRC.Networks.keys(function(key) {
		if (GCMRC.Features.checkFeature(GCMRC.Networks[key])) {
			var networkDesc = '<h4>' + GCMRC.Networks[key].displayName + '</h4>' +
				'<ul><li><a href="' + CONFIG.relativePath + 'stations/' + key +'">Monitoring Stations</a></li>';
			if (GCMRC.Networks[key].reaches) {
				networkDesc = networkDesc + '<li><a href="' + CONFIG.relativePath + 'reaches/' + key +'">Sediment Budget Reaches</a></li>';
			}
			networkDesc = networkDesc + '<li><a href="https://www.sciencebase.gov/catalog/item/'+ GCMRC.Networks[key].researchItemId +'">Research Trip Data</li>';
			networkDesc = networkDesc + '<li><a href="https://www.sciencebase.gov/catalog/item/'+ GCMRC.Networks[key].photoItemId +'">Historical Photographs</li>';
			networkDesc = networkDesc + '</ul>';
			$networkList.append(networkDesc)
		}
	});

})