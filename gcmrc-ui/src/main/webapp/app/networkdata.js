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
			labelLoc : 'bl',
			bbox : {
				"left": -114.003737,
				"bottom": 35.130507,
				"right": -110.84875,
				"top": 37.71811
			},
			reaches : true,
			folderId: '56461a54e4b0e2669b30f2db'
		},
		"CL" : {
			displayName : 'Canyonlands',
			labelLoc: 'tl',
			bbox: {
				"left": -110.26759,
				"bottom": 38.02643,
				"right": -109.47657,
				"top": 38.60718
			},
			appFeatureId : "CANYONLANDS",
			folderId: '56461a7de4b0e2669b30f2df'
		},
		"DINO" : {
			displayName : 'Dinosaur',
			bbox : {
				"left": -109.592736,
				"bottom": 40.067194,
				"right": -107.693084,
				"top": 41.093398
			},
			reaches : true,
			folderId: '56461a8be4b0e2669b30f2e1'
		},
		"CRD" : {
			displayName : 'Colorado River Delta',
			labelLoc: 'bl',
			bbox : {
				"left": -115.12630,
				"bottom": 32.13372,
				"right": -114.55501,
				"top": 32.72718
			},
			reaches : true,
			folderId: '56461a96e4b0e2669b30f2e4'
		},
		"BIBE" : {
			displayName : 'Big Bend',
			bbox : {
				"left": -103.950969,
				"bottom": 28.780138,
				"right": -102.625809,
				"top": 29.561388
			},
			reaches : true,
			folderId: '56461aa1e4b0e2669b30f2e6'
		}
	};
}();