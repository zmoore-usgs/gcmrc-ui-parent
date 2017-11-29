GCMRC.Page = {
	createLargeMap: function(config) {
		if (!config)
			config = {};

		var divId = config['divId'] || 'openlayers-map';

		var options = {controls: [
//				new OpenLayers.Control.LayerSwitcher()
		]};
		GCMRC.Mapping.maps[divId] = new OpenLayers.Map(divId, options);

		var layersToAdd = [];
		layersToAdd.push(GCMRC.Mapping.layers.esri.esriTopo);
//		[].push.apply(layersToAdd, GCMRC.Mapping.layers.esri.values());
		layersToAdd.push(GCMRC.Mapping.layers.flowlines.allzones);
		layersToAdd.push(GCMRC.Mapping.layers.flowlines.zone8374535);
		layersToAdd.push(GCMRC.Mapping.layers.flowlines.dinosaur_reaches);
		layersToAdd.push(GCMRC.Mapping.layers.flowlines.colorado_river_delta_reaches);
		layersToAdd.push(GCMRC.Mapping.layers.vector);
		GCMRC.Mapping.maps[divId].addLayers(layersToAdd);

		GCMRC.Stations.values(function(station) {
			if (!station.hidden && station.network === CONFIG.networkName) {
				var sitePoint = new OpenLayers.Geometry.Point(station.lon, station.lat).transform(
						new OpenLayers.Projection("EPSG:4326"),
						GCMRC.Mapping.maps[divId].getProjectionObject()
						);

				var siteFeature = new OpenLayers.Feature.Vector(sitePoint, {
					siteName: station.nwisSite || station.shortName
				});
				GCMRC.Mapping.layers.vector.addFeatures([siteFeature]);
			}
		});

		var resizeToDefault = true;
		if (1 < GCMRC.Mapping.layers.vector.features.length) {
			resizeToDefault = false;
			GCMRC.Mapping.maps[divId].zoomToExtent(GCMRC.Mapping.layers.vector.getDataExtent());
			if (9 < GCMRC.Mapping.maps[divId].zoom) {
				resizeToDefault = true;
			}
		}
		if (resizeToDefault) {
			if ('GCDAMP' === CONFIG.networkName) {
				GCMRC.Mapping.maps[divId].moveTo(new OpenLayers.LonLat(-12499366.619029, 4338225.8756765), 8);
			} else if ('DINO' === CONFIG.networkName) {
				//GCMRC.Mapping.maps[divId].moveTo(new OpenLayers.LonLat(-12097598.376886, 4950668.6874257), 9);
				GCMRC.Mapping.maps[divId].moveTo(new OpenLayers.LonLat(-12133916.55599665, 4937675.397935071), 9);
			} else if ('BIBE' === CONFIG.networkName) {
				GCMRC.Mapping.maps[divId].moveTo(new OpenLayers.LonLat(-11492369.987008, 3397774.0210003), 9);
			} else if ('CRD' === CONFIG.networkName) {
				GCMRC.Mapping.maps[divId].moveTo(new OpenLayers.LonLat(-12797833.5705, 3813164.78718), 9);
			} else {
				LOG.error("Invalid Network Name!");
			}
		}
	},
	reachColoring : [
		"#0967B0",
		"#FF9200",
		"#F30026",
		"#3CDF00",
		"#660AB6",
		"#000000",
		'#FFFFFF'
	],
	addReaches : function($div) {
		var addReachToPage = function(el) {
			var result = [];

			var backgroundColor = '#FFFFFF';
			var linkit = true; //!("09402500" === el.upstreamStation || "09404120" === el.upstreamStation);
			if (this.index < GCMRC.Page.reachColoring.length) {
				backgroundColor = GCMRC.Page.reachColoring[this.index];
				this.index++;
			}

			result.push('<div class="media">');

			if (linkit) {
				[].push.apply(result, [
				'<a href="',CONFIG.relativePath,'reach/',CONFIG.networkName,
				'/',el.upstreamStation,'/',el.downstreamStation,'">']);
			}

			[].push.apply(result, [
				'<img class="pull-left" src="',CONFIG.relativePath,'app/s.gif" class="media-object" alt="" width="32px" height="32px" style="background-color:',backgroundColor,';"/>']);

			if (linkit) {
				result.push('</a>');
			}

			result.push('<div class="media-body">');

			if (linkit) {
				[].push.apply(result, [
				'<a href="',CONFIG.relativePath,'reach/',CONFIG.networkName,
				'/',el.upstreamStation,'/',el.downstreamStation,'">']);
			}

			[].push.apply(result, [
				'<h4 class="media-heading">',el.reachName,'</h4>']);

			if (linkit) {
				result.push('</a>');
			}

			[].push.apply(result, [
				"(",
				el.upstreamDisplayName,
				el.upstreamSecondaryDisplayName ? " and " + el.upstreamSecondaryDisplayName : '',
				" to ",
				el.downstreamDisplayName,
				")",
				'</div>',
				'</div>'
			]);

			$div.append(result.join(""));
		};
		GCMRC.Page.reaches.forEach(addReachToPage, {index : 0});
	},
	reaches : [],
	reachLoad : JSL.ResourceLoad(function(el) {
		GCMRC.Page.reaches.push(el);
	})
};