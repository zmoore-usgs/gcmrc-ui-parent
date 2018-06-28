GCMRC.Page = {
	createLargeMap: function(config) {
		if (!config)
			config = {};
		
		var divId = config['divId'] || 'openlayers-map';

		var popups = [];
		var options = {
			controls : [
//				new OpenLayers.Control.LayerSwitcher(),
//				new OpenLayers.Control.MousePosition({
//					displayProjection : new OpenLayers.Projection("EPSG:4326")
//				})
			]
		};
		GCMRC.Mapping.maps[divId] = new OpenLayers.Map(divId, options);

		var layersToAdd = [];
		layersToAdd.push(GCMRC.Mapping.layers.esri.esriStreet);
//		[].push.apply(layersToAdd, GCMRC.Mapping.layers.esri.values());
		layersToAdd.push(GCMRC.Mapping.layers.network);
		GCMRC.Mapping.maps[divId].addLayers(layersToAdd);

		OpenLayers.Popup.COLOR = "transparent";
		
		GCMRC.Networks.keys(function(key, network) {
			if (GCMRC.Features.checkFeature(network)) {
				var box = {
					tl : new OpenLayers.Geometry.Point(network.bbox.left, network.bbox.top).transform(
						new OpenLayers.Projection("EPSG:4326"),
						GCMRC.Mapping.maps[divId].getProjectionObject()
						),
					tr : new OpenLayers.Geometry.Point(network.bbox.right, network.bbox.top).transform(
						new OpenLayers.Projection("EPSG:4326"),
						GCMRC.Mapping.maps[divId].getProjectionObject()
						),
					br : new OpenLayers.Geometry.Point(network.bbox.right, network.bbox.bottom).transform(
						new OpenLayers.Projection("EPSG:4326"),
						GCMRC.Mapping.maps[divId].getProjectionObject()
						),
					bl : new OpenLayers.Geometry.Point(network.bbox.left, network.bbox.bottom).transform(
						new OpenLayers.Projection("EPSG:4326"),
						GCMRC.Mapping.maps[divId].getProjectionObject()
						)
				};

				var networkBBox = new OpenLayers.Geometry.Polygon([
					new OpenLayers.Geometry.LinearRing([
						box.tl,
						box.tr,
						box.br,
						box.bl
					])
				]);
				var siteFeature = new OpenLayers.Feature.Vector(networkBBox, {
					networkName: key,
					networkDisplayName: network.displayName,
					networkResearchFolder: network.researchItemId,
					networkTopoSurveyFolder: network.topoSurveyId,
					networkGisFolder: network.gisDataId,
					networkPhotoFolder: network.photoItemId
				});
				GCMRC.Mapping.layers.network.addFeatures([siteFeature]);
				
				var labelLoc = (network.labelLoc)?network.labelLoc : "tr";
				var oppositeLoc = function(loc) {
					var result = loc;
					
					if (loc && loc.length > 1) {
						var side = loc.charAt(1);
						if (side === 'r') {
							result = loc.charAt(0) + "l";
						} else if (side === 'l') {
							result = loc.charAt(0) + "r";
						} else {
							// what the heck?
						}
					}
					
					return result;
				};
				var sitePopupClass = OpenLayers.Class(OpenLayers.Popup.Anchored, {
					autoSize: true,
					contentDisplayClass: "sitePopupContent",
					displayClass: "sitePopup",
					calculateRelativePosition: function(px) {
						return oppositeLoc(labelLoc);
					}
				});
				
				popups.push(new sitePopupClass(
						"hoverPopup",
						new OpenLayers.LonLat(box[labelLoc].x, box[labelLoc].y),
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
				$('#networkResearchFolder').attr('href', 'https://www.sciencebase.gov/catalog/item/' + vector.attributes.networkResearchFolder);
				if (GCMRC.Networks[vector.attributes.networkName].topo) {
					$('#networkTopoSurveyFolder').attr('href', 'https://www.sciencebase.gov/catalog/item/' + vector.attributes.networkTopoSurveyFolder);
					$('#networkTopoSurveyFolder').parent().show();
				} else {
					$('#networkTopoSurveyFolder').parent().hide();
				}
				if (GCMRC.Networks[vector.attributes.networkName].gis) {
					$('#networkGisFolder').attr('href', 'https://www.sciencebase.gov/catalog/item/' + vector.attributes.networkGisFolder);
					$('#networkGisFolder').parent().show();
				} else {
					$('#networkGisFolder').parent().hide();
				}
				$('#networkPhotoFolder').attr('href', 'https://www.sciencebase.gov/catalog/item/' + vector.attributes.networkPhotoFolder);
				if (GCMRC.Networks[vector.attributes.networkName].reaches) {
					$('#networkReach').attr('href', CONFIG.relativePath + 'reaches/' + vector.attributes.networkName);					
					$('#networkReach').parent().show();										
				}
				else {
					$('#networkReach').parent().hide();
				}
				$('#networkPopup').modal();
			}
		});

		GCMRC.Mapping.maps[divId].addControl(clickcontrol);
		clickcontrol.activate();

	}
};