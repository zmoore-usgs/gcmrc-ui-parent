$(document).ready(function() {
	$("span.network-name").html(GCMRC.Networks[CONFIG.networkName].displayName);
	GCMRC.Page.createLargeMap();
	
	var $stationList = $('#stationList');
	
	GCMRC.Stations.map(function(key, val) {return {key : key, val : val}})
			.values().sortBy(function(el){return el.val.displayOrder || "99999999999"})
			.map(function(el){return el.key})
			.each(function(key) {
		if (!GCMRC.Stations[key].hidden && GCMRC.Stations[key].network === CONFIG.networkName) {
//			$stationList.append('<li><a href="' + CONFIG.relativePath + 'station/' + CONFIG.networkName + '/' + key + '">' + GCMRC.Stations[key].displayName + ' - ' + key + '</a></li>')
			
			var result = [];

			// Hackity Hack Hack~!!!
			var linkit = true;

			result.push('<div class="media">');

			if (linkit) {
				[].push.apply(result, [
				'<a href="',CONFIG.relativePath,'station/',CONFIG.networkName,
				'/',key,'">']);
			}

			[].push.apply(result, [
				'<img class="pull-left" src="',CONFIG.relativePath,'photo/',CONFIG.networkName,
				'/',key,'/',key,'_01sm.jpg" class="media-object" alt="" width="128px" height="128px"/>']);

			if (linkit) {
				result.push('</a>');
			}

			result.push('<div class="media-body">');

			if (linkit) {
				[].push.apply(result, [
				'<a href="',CONFIG.relativePath,'station/',CONFIG.networkName,
				'/',key,'">']);
			}

			[].push.apply(result, [
				'<h4 class="media-heading">',GCMRC.Stations[key].displayName,'</h4>']);

			if (linkit) {
				result.push('</a>');
			}

			[].push.apply(result, [
				key,
				'</div>',
				'</div>'
			]);

			$stationList.append(result.join(""));
		}
	});
	
//	GCMRC.Page.bindWindowResize();
});