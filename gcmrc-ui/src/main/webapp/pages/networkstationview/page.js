GCMRC.Page = {
	activeSelect : 'Y',
	createLargeMap: function(config) {
		if (!config)
			config = {};

		var divId = config['divId'] || 'openlayers-map';

		var options = {
			//controls : []
		};
		
		GCMRC.Mapping.maps[divId] = new OpenLayers.Map(divId, options);
		var layersToAdd = [];
		layersToAdd.push(GCMRC.Mapping.layers.esri.esriTopo);
		layersToAdd.push(GCMRC.Mapping.layers.vector);
		GCMRC.Mapping.maps[divId].addLayers(layersToAdd);

		GCMRC.Stations.values(
			).filter(function(station) {
				return (!station.hidden && station.network === CONFIG.networkName);
			}).sortBy(function(station) {
				return station.active || 'N';
			}).each(function(station) {
				var sitePoint = new OpenLayers.Geometry.Point(station.lon, station.lat).transform(
						new OpenLayers.Projection("EPSG:4326"),
						GCMRC.Mapping.maps[divId].getProjectionObject()
						);

				var siteFeature = new OpenLayers.Feature.Vector(sitePoint, {
					siteName: station.siteName,
					active : station.active
				});
				GCMRC.Mapping.layers.vector.addFeatures([siteFeature]);
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
				GCMRC.Mapping.maps[divId].moveTo(new OpenLayers.LonLat(-12097598.376886, 4950668.6874257), 9);
			} else if ('BIBE' === CONFIG.networkName) {
				GCMRC.Mapping.maps[divId].moveTo(new OpenLayers.LonLat(-11492369.987008, 3397774.0210003), 9);
			} else {
				LOG.error("Invalid Network Name!");
			}
		}


		OpenLayers.Popup.COLOR = "transparent";
		var sitePopupClass = OpenLayers.Class(OpenLayers.Popup.Anchored, {
			autoSize: true,
			contentDisplayClass: "sitePopupContent",
			displayClass: "sitePopup"
		});
		var namedPopup = null;
		var hovercontrol = new OpenLayers.Control.SelectFeature(GCMRC.Mapping.layers.vector, {
			eventListeners: {
				featurehighlighted: function(event) {
					LOG.trace("hovering is cool.");
					
					var lonLat = new OpenLayers.LonLat(event.feature.geometry.x, event.feature.geometry.y);
					
					var clusterFeatures = event.feature.layer.renderer.clusterFeatures;
					if (clusterFeatures && clusterFeatures[event.feature.id]) {
						lonLat = new OpenLayers.LonLat(
							clusterFeatures[event.feature.id][0].geometry.x,
							clusterFeatures[event.feature.id][0].geometry.y);
					}
					
					namedPopup = new sitePopupClass(
							"hoverPopup",
							lonLat,
							new OpenLayers.Size(80, 15),
							GCMRC.Stations[event.feature.attributes.siteName].displayName + ' - ' + event.feature.attributes.siteName,
							{
								size: new OpenLayers.Size(0, 0),
								offset: new OpenLayers.Pixel(0, 0)
							},
					false,
							null
							);
					
					GCMRC.Mapping.maps[divId].addPopup(namedPopup);
					
					var offset = 10;
					if ("tr" === namedPopup.relativePosition || "br" === namedPopup.relativePosition) {
						namedPopup.anchor = {
								size: new OpenLayers.Size(0, 0),
								offset: new OpenLayers.Pixel(offset, 0)
							};
						LOG.trace(namedPopup.relativePosition);
					} else if ("tl" === namedPopup.relativePosition || "bl" === namedPopup.relativePosition) {
						namedPopup.anchor = {
								size: new OpenLayers.Size(0, 0),
								offset: new OpenLayers.Pixel(-offset, 0)
							};
						LOG.trace(namedPopup.relativePosition);
					} else {
						LOG.trace("what's going on? " + namedPopup.relativePosition);
					}
					namedPopup.draw();
					LOG.trace("see what I mean?");
				},
				featureunhighlighted: function(event) {
					LOG.trace("hovering is so passe.");
					GCMRC.Mapping.maps[divId].removePopup(namedPopup);
				}
			},
			hover: true,
			highlightOnly: true
		});

		var clickcontrol = new OpenLayers.Control.SelectFeature(GCMRC.Mapping.layers.vector, {
			onSelect: function(vector) {
				window.location.href = CONFIG.relativePath + 'station/' + CONFIG.networkName + '/' + vector.attributes.siteName;
			}
		});

		// order found here: http://lists.osgeo.org/pipermail/openlayers-users/2009-August/013002.html
		GCMRC.Mapping.maps[divId].addControl(hovercontrol);
		GCMRC.Mapping.maps[divId].addControl(clickcontrol);
		hovercontrol.activate();
		clickcontrol.activate();

	},
	/**
	 * Taken from Coastal Hazards
	 * @param {type} config
	 * @returns {undefined}
	 */
	bindWindowResize: function(config) {
		if (!config)
			config = {};

		var divId = config['divId'] || 'openlayers-map';
		
		var mapdiv = $(GCMRC.Mapping.maps[divId].div);
		
		$(window).resize(function() {
			var contentRowHeight = $(window).height() - $('#header-row').height() - $('#footer-row').height();
			$('#content-row').css('min-height', contentRowHeight);
			$('#map-wrapper').css('min-height', contentRowHeight);
			mapdiv.css('height', contentRowHeight);

			// Move the zoom control over to the right
			$('.olControlZoom').css('left', mapdiv.width() - $('.olControlZoom').width() - 20);
			// Move the layer switcher control down a bit to make room for zoom control
			$('.olControlLayerSwitcher').css('top', 60);
		});
	}
};

gcmrcModule.controller('StationListCtrl', function($scope) {
	$scope.CONFIG = CONFIG;
	$scope.GCMRC = GCMRC;
	
	$scope.getActive = function(stations) {
		return stations.filter(function(el) {return el.active === 'Y'});
	}
	$scope.getInactive = function(stations) {
		return stations.filter(function(el) {return el.active === 'N'});
	}
	$scope.getVisible = function(stations) {
		return stations.exclude(function(el) {
			return el.hidden;
		});
	}
	$scope.getNetwork = function(stations, network) {
		return stations.filter(function(el) {return el.network === network});
	}
	
	$scope.sortToArray = function(stations) {
		return stations.values().sortBy(function(el){return el.displayOrder || "99999999999"})
	}
	
	$scope.selectActiveTab = function() {
		GCMRC.Page.activeSelect = 'Y';
		GCMRC.Mapping.layers.vector.redraw();
	}
	
	$scope.selectInactiveTab = function() {
		GCMRC.Page.activeSelect = 'N';
		GCMRC.Mapping.layers.vector.redraw();
	}
});