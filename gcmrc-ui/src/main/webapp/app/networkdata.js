GCMRC.Networks = function() {
//	var _toBoundsArray = function(network) {
//		var result = null;
//		
//		if (network && network.bbox) {
//			result = [network.bbox.left, network.bbox.bottom, network.bbox.right, network.bbox.top];
//		}
//		
//		return result;
//	}

	return {
		"GCDAMP" : {
			displayName : 'Grand Canyon',
			researchItemId: '56461a54e4b0e2669b30f2db',
			labelLoc : 'bl',
			photoItemId: '5ada096be4b0e2c2dd2894ed',
			topoSurveyId: '594b0cd2e4b062508e36f798',
			gisDataId: '5b33db2ce4b040769c173611',
			rainGaugeId: '5caf7e47e4b0c3b00654e038',
			bbox : {
				"left": -114.003737,
				"bottom": 35.130507,
				"right": -110.84875,
				"top": 37.71811
			},
			reaches : true,
			gis: true,
			topo: true,
			rainGauge: true,
			historicalPhotos: true
		},
		"DINO" : {
			displayName : 'Dinosaur',
			researchItemId: '56461a8be4b0e2669b30f2e1',
			bbox : {
				"left": -109.734,
				"bottom": 39.98,
				"right": -108.075,
				"top": 40.93
			},
			reaches : true,
			gis: false,
			topo: false,
			rainGauge: false,
			historicalPhotos: false
		},
		"CL" : {
			displayName : 'Canyonlands',
			researchItemId: '56461a7de4b0e2669b30f2df',
			labelLoc: 'tl',
			bbox: {
				"left": -110.26759,
				"bottom": 38.02643,
				"right": -109.47657,
				"top": 38.60718
			},
			appFeatureId : "CANYONLANDS",
			reaches:false,
			gis: false,
			topo: false,
			rainGauge: false,
			historicalPhotos: false
		},
		"CRD" : {
			displayName : 'Colorado River Delta',
			researchItemId: '56461a96e4b0e2669b30f2e4',
			labelLoc: 'bl',
			bbox : {
				"left": -115.12630,
				"bottom": 32.13372,
				"right": -114.55501,
				"top": 32.72718
			},
			reaches : true,
			gis: false,
			topo: false,
			rainGauge: false,
			historicalPhotos: false
		},
		"URG" : {
			displayName : 'Upper Rio Grande',
			researchItemId: '5a8dc5f6e4b0699060596dc0',
			labelLoc: 'tl',
			bbox : {
				"left": -107.118,
				"bottom": 34.085,
				"right": -106.604,
				"top": 34.454
			},
			reaches: false,
			gis: false,
			topo: false,
			rainGauge: false,
			historicalPhotos: false
		},
		"BIBE" : {
			displayName : 'Big Bend',
			researchItemId: '56461aa1e4b0e2669b30f2e6',
			bbox : {
				"left": -103.950969,
				"bottom": 28.780138,
				"right": -102.625809,
				"top": 29.561388
			},
			reaches : true,
			gis: false,
			topo: false,
			rainGauge: false,
			historicalPhotos: false
		},
		"CHIP" : {
			displayName : 'Chippewa',
			researchItemId: '5a0c952ae4b09af898cd417b',
			labelLoc: 'bl',
			bbox: {
				"left": -92.24,
				"bottom": 44.383,
				"right": -91.814,
				"top": 44.674
			},
			reaches: false,
			gis: false,
			topo: false,
			rainGauge: false,
			historicalPhotos: false
		}
	};
}();