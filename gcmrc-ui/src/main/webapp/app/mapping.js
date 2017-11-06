GCMRC.Mapping = function() {
	var map = {};

	var activeStyle = function(main, alt) {
		return function(feature, prop) {
			var result = main;
			if (feature.data.active !== GCMRC.Page.activeSelect) {
				result = alt;
			}
			return result;
		};
	};

	var layers = {
		esri : {
		esriWorldImagery: new OpenLayers.Layer.XYZ("World Imagery",
				"https://services.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/${z}/${y}/${x}",
				{
					sphericalMercator: true,
					isBaseLayer: true,
					numZoomLevels: 20,
					wrapDateLine: true
				}
		),
		esriStreet: new OpenLayers.Layer.XYZ("Street",
				"https://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/${z}/${y}/${x}",
				{
					sphericalMercator: true,
					isBaseLayer: true,
					numZoomLevels: 20,
					wrapDateLine: true
				}
		),
		esriTopo: new OpenLayers.Layer.XYZ("Topo",
				"https://services.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer/tile/${z}/${y}/${x}",
				{
					sphericalMercator: true,
					isBaseLayer: true,
					numZoomLevels: 20,
					wrapDateLine: true
				}
		),
		esriTerrain: new OpenLayers.Layer.XYZ("Terrain",
				"https://services.arcgisonline.com/ArcGIS/rest/services/World_Terrain_Base/MapServer/tile/${z}/${y}/${x}",
				{
					sphericalMercator: true,
					isBaseLayer: true,
					numZoomLevels: 14,
					wrapDateLine: true
				}
		),
		esriShadedRelief: new OpenLayers.Layer.XYZ("Shaded Relief",
				"https://services.arcgisonline.com/ArcGIS/rest/services/World_Shaded_Relief/MapServer/tile/${z}/${y}/${x}",
				{
					sphericalMercator: true,
					isBaseLayer: true,
					numZoomLevels: 14,
					wrapDateLine: true
				}
		),
		esriPhysical: new OpenLayers.Layer.XYZ("Physical",
				"https://services.arcgisonline.com/ArcGIS/rest/services/World_Physical_Map/MapServer/tile/${z}/${y}/${x}",
				{
					sphericalMercator: true,
					isBaseLayer: true,
					numZoomLevels: 9,
					wrapDateLine: true
				}
		),
		esriOcean: new OpenLayers.Layer.XYZ("Ocean",
				"https://services.arcgisonline.com/ArcGIS/rest/services/Ocean_Basemap/MapServer/tile/${z}/${y}/${x}",
				{
					sphericalMercator: true,
					isBaseLayer: true,
					numZoomLevels: 17,
					wrapDateLine: true
				}
		)},
		markers : new OpenLayers.Layer.Markers(
			'SiteMarkers',
			{
				projection: new OpenLayers.Projection("EPSG:26949")
			}),
		vector : new OpenLayers.Layer.Vector(
			'SiteVectors',
			{
				projection: new OpenLayers.Projection("EPSG:900913"),
				styleMap: new OpenLayers.StyleMap({
					"default": new OpenLayers.Style(OpenLayers.Util.applyDefaults({
						strokeColor : 'black',
						strokeOpacity : .8,
						strokeWidth : 2,
						pointRadius : 6,
//						fillColor : 'orange',
						fillOpacity : 1,
						cursor : 'pointer',
						graphicZIndex : "${getZIndex}",
						fillColor : "${getColor}"
					}, OpenLayers.Feature.Vector.style["default"]),{
						context : {
							getColor : activeStyle("orange", "lightgrey"),
							getZIndex : activeStyle(100, 10)
						}
					}),
					"select": new OpenLayers.Style(OpenLayers.Util.applyDefaults({
						strokeColor : 'black',
						strokeOpacity : .8,
						strokeWidth : 2,
						pointRadius : 6,
						fillColor : 'yellow',
						fillOpacity : 1,
						cursor : 'pointer',
						graphicZIndex : 1000
					}, OpenLayers.Feature.Vector.style["select"]))
				}),
				renderers: ["DeclusterCanvas"],
				rendererOptions : {
					declusterStrokeColor : "#000000",
					zIndexing: true,
					yOrdering: true
				}
			}),
		network : new OpenLayers.Layer.Vector(
			'NetworkVectors',
			{
				projection: new OpenLayers.Projection("EPSG:900913"),
				styleMap: new OpenLayers.StyleMap({
					"default": new OpenLayers.Style(OpenLayers.Util.applyDefaults({
						strokeColor : 'black',
						strokeOpacity : .8,
						strokeWidth : 2,
						pointRadius : 6,
						fillColor : 'orange',
						fillOpacity : 0.5,
						cursor : 'pointer'
					}, OpenLayers.Feature.Vector.style["default"])),
					"select": new OpenLayers.Style(OpenLayers.Util.applyDefaults({
						strokeColor : 'black',
						strokeOpacity : .8,
						strokeWidth : 2,
						pointRadius : 6,
						fillColor : 'orange',
						fillOpacity : 0.5,
						cursor : 'pointer'
					}, OpenLayers.Feature.Vector.style["select"]))
				})
			}),
		satmap : new OpenLayers.Layer.ArcGIS93Rest( "ArcGIS Server Layer",
                "http://137.227.239.42/ArcGIS/rest/services/GC_2009_05_4BAND_EARTHDATA/ImageServer/exportImage",
                {
					layers: "show:0",
					srs : "EPSG:26949"
				},
				{
					maxExtent:[50248.1168657801, 527666.290170765, 252527.543128164, 661680.890517618]
				}),
		flowlines : {
			allzones : new OpenLayers.Layer.WMS(
				'Flow Lines',
				CONFIG.relativePath + 'geoserver/sample/wms',
				{
					SRS: 'EPSG:900913',
					CRS: 'EPSG:900913',
					LAYERS: 'sample:CR_CLIP',
					STYLES: 'CR_CLIP_LONG',
					transparent : true
				},
				{
					buffer: 0,
					displayOutsideMaxExtent: true,
					yx : {'EPSG:900913' : false}
				}
			),
			zone9380000 : new OpenLayers.Layer.WMS(
				'Flow Lines',
				CONFIG.relativePath + 'geoserver/sample/wms',
				{
					SRS: 'EPSG:900913',
					CRS: 'EPSG:900913',
					LAYERS: 'sample:CR_CLIP',
					STYLES: 'CR_CLIP_ZONE_9380000',
					transparent : true
				},
				{
					buffer: 0,
					displayOutsideMaxExtent: true,
					yx : {'EPSG:900913' : false}
				}
			),
			zone9383050 : new OpenLayers.Layer.WMS(
				'Flow Lines',
				CONFIG.relativePath + 'geoserver/sample/wms',
				{
					SRS: 'EPSG:900913',
					CRS: 'EPSG:900913',
					LAYERS: 'sample:CR_CLIP',
					STYLES: 'CR_CLIP_ZONE_9383050',
					transparent : true
				},
				{
					buffer: 0,
					displayOutsideMaxExtent: true,
					yx : {'EPSG:900913' : false}
				}
			),
			zone9383100 : new OpenLayers.Layer.WMS(
				'Flow Lines',
				CONFIG.relativePath + 'geoserver/sample/wms',
				{
					SRS: 'EPSG:900913',
					CRS: 'EPSG:900913',
					LAYERS: 'sample:CR_CLIP',
					STYLES: 'CR_CLIP_ZONE_9383100',
					transparent : true
				},
				{
					buffer: 0,
					displayOutsideMaxExtent: true,
					yx : {'EPSG:900913' : false}
				}
			),
			zone9402500 : new OpenLayers.Layer.WMS(
				'Flow Lines',
				CONFIG.relativePath + 'geoserver/sample/wms',
				{
					SRS: 'EPSG:900913',
					CRS: 'EPSG:900913',
					LAYERS: 'sample:CR_CLIP',
					STYLES: 'CR_CLIP_ZONE_9402500',
					transparent : true
				},
				{
					buffer: 0,
					displayOutsideMaxExtent: true,
					yx : {'EPSG:900913' : false}
				}
			),
			zone9404120 : new OpenLayers.Layer.WMS(
				'Flow Lines',
				CONFIG.relativePath + 'geoserver/sample/wms',
				{
					SRS: 'EPSG:900913',
					CRS: 'EPSG:900913',
					LAYERS: 'sample:CR_CLIP',
					STYLES: 'CR_CLIP_ZONE_9404120',
					transparent : true
				},
				{
					buffer: 0,
					displayOutsideMaxExtent: true,
					yx : {'EPSG:900913' : false}
				}
			),
			zone9404200 : new OpenLayers.Layer.WMS(
				'Flow Lines',
				CONFIG.relativePath + 'geoserver/sample/wms',
				{
					SRS: 'EPSG:900913',
					CRS: 'EPSG:900913',
					LAYERS: 'sample:CR_CLIP',
					STYLES: 'CR_CLIP_ZONE_9404200',
					transparent : true
				},
				{
					buffer: 0,
					displayOutsideMaxExtent: true,
					yx : {'EPSG:900913' : false}
				}
			),
			zone8374549 : new OpenLayers.Layer.WMS(
				'Flow Lines',
				CONFIG.relativePath + 'geoserver/sample/wms',
				{
					SRS: 'EPSG:900913',
					CRS: 'EPSG:900913',
					LAYERS: 'sample:gcmrc_subset_rio_grande_short',
					STYLES: 'RG_CLIP_ZONE_8374549',
					transparent : true
				},
				{
					buffer: 0,
					displayOutsideMaxExtent: true,
					yx : {'EPSG:900913' : false}
				}
			),
			zone8374535 : new OpenLayers.Layer.WMS(
				'Flow Lines',
				CONFIG.relativePath + 'geoserver/sample/wms',
				{
					SRS: 'EPSG:900913',
					CRS: 'EPSG:900913',
					LAYERS: 'sample:gcmrc_subset_rio_grande_short',
					STYLES: 'RG_CLIP_ZONE_8374535',
					transparent : true
				},
				{
					buffer: 0,
					displayOutsideMaxExtent: true,
					yx : {'EPSG:900913' : false}
				}
			),
			zone9251000 : new OpenLayers.Layer.WMS(
				'Flow Lines',
				CONFIG.relativePath + 'geoserver/sample/wms',
				{
					SRS: 'EPSG:900913',
					CRS: 'EPSG:900913',
					LAYERS: 'sample:dinosaur_reaches_final',
					STYLES: 'dinosaur',
					transparent : true
				},
				{
					buffer: 0,
					displayOutsideMaxExtent: true,
					yx : {'EPSG:900913' : false}
				}
			),
			zone9260050 : new OpenLayers.Layer.WMS(
				'Flow Lines',
				CONFIG.relativePath + 'geoserver/sample/wms',
				{
					SRS: 'EPSG:900913',
					CRS: 'EPSG:900913',
					LAYERS: 'sample:dinosaur_reaches_final',
					STYLES: 'dinosaur',
					transparent : true
				},
				{
					buffer: 0,
					displayOutsideMaxExtent: true,
					yx : {'EPSG:900913' : false}
				}
			),
			zone9272300 : new OpenLayers.Layer.WMS(
				'Flow Lines',
				CONFIG.relativePath + 'geoserver/sample/wms',
				{
					SRS: 'EPSG:900913',
					CRS: 'EPSG:900913',
					LAYERS: 'sample:dinosaur_reaches_final',
					STYLES: 'dinosaur',
					transparent : true
				},
				{
					buffer: 0,
					displayOutsideMaxExtent: true,
					yx : {'EPSG:900913' : false}
				}
			),		
			dinosaur_reaches : new OpenLayers.Layer.WMS(
				'Dinosaur Reaches',
				CONFIG.relativePath + 'geoserver/sample/wms',
				{
					SRS: 'EPSG: 900913',
					CRS: 'EPSG:900913',
					LAYERS: 'sample:dinosaur_reaches_final',
					styles: 'dinosaur',
					transparent: true
				},
				{
					buffer: 0,
					displayOutsideMaxExtent: true,
					yx : {'EPSG:900913' : false}
				}
			),
			zone9522000 : new OpenLayers.Layer.WMS(
					'Colorado River Delta Reaches',
					CONFIG.relativePath + 'geoserver/sample/wms',
					{
						SRS: 'EPSG: 900913',
						CRS: 'EPSG:900913',
						LAYERS: 'sample:colorado_river_delta',
						STYLES: 'colorado_river_delta',
						transparent: true
					},
					{
						buffer: 0,
						displayOutsideMaxExtent: true,
						yx : {'EPSG:900913' : false}
					}
			),
			zone9522100 : new OpenLayers.Layer.WMS(
					'Colorado River Delta Reaches',
					CONFIG.relativePath + 'geoserver/sample/wms',
					{
						SRS: 'EPSG: 900913',
						CRS: 'EPSG:900913',
						LAYERS: 'sample:colorado_river_delta',
						STYLES: 'colorado_river_delta',
						transparent: true
					},
					{
						buffer: 0,
						displayOutsideMaxExtent: true,
						yx : {'EPSG:900913' : false}
					}
			),
			zone9522150 : new OpenLayers.Layer.WMS(
					'Colorado River Delta Reaches',
					CONFIG.relativePath + 'geoserver/sample/wms',
					{
						SRS: 'EPSG: 900913',
						CRS: 'EPSG:900913',
						LAYERS: 'sample:colorado_river_delta',
						STYLES: 'colorado_river_delta',
						transparent: true
					},
					{
						buffer: 0,
						displayOutsideMaxExtent: true,
						yx : {'EPSG:900913' : false}
					}
			),
			zone9522200 : new OpenLayers.Layer.WMS(
					'Colorado River Delta Reaches',
					CONFIG.relativePath + 'geoserver/sample/wms',
					{
						SRS: 'EPSG: 900913',
						CRS: 'EPSG:900913',
						LAYERS: 'sample:colorado_river_delta',
						STYLES: 'colorado_river_delta',
						transparent: true
					},
					{
						buffer: 0,
						displayOutsideMaxExtent: true,
						yx : {'EPSG:900913' : false}
					}
			),
			colorado_river_delta_reaches : new OpenLayers.Layer.WMS(
				'Colorado River Delta Reaches',
				CONFIG.relativePath + 'geoserver/sample/wms',
				{
					SRS: 'EPSG: 900913',
					CRS: 'EPSG:900913',
					LAYERS: 'sample:colorado_river_delta',
					transparent: true
				},
				{
					buffer: 0,
					displayOutsideMaxExtent: true,
					yx : {'EPSG:900913' : false}
				}
			)
		}
	};

	var styleTemplates = {
				active: {
					'Y' : {
						fillColor:'orange',
						graphicZIndex: 10
					},
					'N' : {
						fillColor:'grey',
						graphicZIndex: 100
					}
				},
				inactive: {
					'Y' : {
						fillColor:'grey',
						graphicZIndex: 100
					},
					'N' : {
						fillColor:'orange',
						graphicZIndex: 10
					}
				}
			};

	return {
		maps : map,
		layers : layers,
		styleTemplates : styleTemplates
	};
}();


