GCMRC.Page = {
	createLargeMap: function(config) {
		if (!config)
			config = {};
		
		var divId = config['divId'] || 'openlayers-map';

		var popups = [];
		var options = {
			controls : [
//				new OpenLayers.Control.LayerSwitcher()
			]
		};
		GCMRC.Mapping.maps[divId] = new OpenLayers.Map(divId, options);

		var layersToAdd = [];
		layersToAdd.push(GCMRC.Mapping.layers.esri.esriStreet);
//		[].push.apply(layersToAdd, GCMRC.Mapping.layers.esri.values());
		layersToAdd.push(GCMRC.Mapping.layers.network);
		GCMRC.Mapping.maps[divId].addLayers(layersToAdd);

		OpenLayers.Popup.COLOR = "transparent";
		var sitePopupClass = OpenLayers.Class(OpenLayers.Popup.Anchored, {
			autoSize: true,
			relativePosition: "tr",
			contentDisplayClass: "sitePopupContent",
			displayClass: "sitePopup",
			calculateRelativePosition: function(px) {
				return "tl";
			}
		});

		GCMRC.Networks.keys(function(key, network) {
			if (!network.hidden) {
				var leftTop = new OpenLayers.Geometry.Point(network.bbox.left, network.bbox.top).transform(
						new OpenLayers.Projection("EPSG:4326"),
						GCMRC.Mapping.maps[divId].getProjectionObject()
						);
				var rightTop = new OpenLayers.Geometry.Point(network.bbox.right, network.bbox.top).transform(
						new OpenLayers.Projection("EPSG:4326"),
						GCMRC.Mapping.maps[divId].getProjectionObject()
						);
				var rightBottom = new OpenLayers.Geometry.Point(network.bbox.right, network.bbox.bottom).transform(
						new OpenLayers.Projection("EPSG:4326"),
						GCMRC.Mapping.maps[divId].getProjectionObject()
						);
				var leftBottom = new OpenLayers.Geometry.Point(network.bbox.left, network.bbox.bottom).transform(
						new OpenLayers.Projection("EPSG:4326"),
						GCMRC.Mapping.maps[divId].getProjectionObject()
						);

				var networkBBox = new OpenLayers.Geometry.Polygon([
					new OpenLayers.Geometry.LinearRing([
						leftTop,
						rightTop,
						rightBottom,
						leftBottom
					])
				]);
				var siteFeature = new OpenLayers.Feature.Vector(networkBBox, {
					networkName: key,
					networkDisplayName: network.displayName
				});
				GCMRC.Mapping.layers.network.addFeatures([siteFeature]);

				popups.push(new sitePopupClass(
						"hoverPopup",
						new OpenLayers.LonLat(network.bbox.right, network.bbox.top).transform(
						new OpenLayers.Projection("EPSG:4326"),
						GCMRC.Mapping.maps[divId].getProjectionObject()
						),
						new OpenLayers.Size(80, 12),
						siteFeature.attributes.networkDisplayName,
						null,
						false,
						null
						));
			}
		});

		var addLabels = function(obj, el) {
			popups.each(function(pop) {
				GCMRC.Mapping.maps[divId].addPopup(pop);
			});
			GCMRC.Mapping.maps[divId].events.unregister('zoomend', null, addLabels);
		};
		GCMRC.Mapping.maps[divId].events.register('zoomend', null, addLabels);

		GCMRC.Mapping.maps[divId].zoomToExtent(GCMRC.Mapping.layers.network.getDataExtent());

		var clickcontrol = new OpenLayers.Control.SelectFeature(GCMRC.Mapping.layers.network, {
			onSelect: function(vector) {
				this.unselectAll();
				$('#networkPopup .network-name').html(vector.attributes.networkDisplayName);
				$('#networkStation').attr('href', CONFIG.relativePath + 'stations/' + vector.attributes.networkName);
				$('#networkReach').attr('href', CONFIG.relativePath + 'reaches/' + vector.attributes.networkName);
				$('#networkPopup').modal();
			}
		});

		GCMRC.Mapping.maps[divId].addControl(clickcontrol);
		clickcontrol.activate();

	}
};