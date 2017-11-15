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
			itemId: '56461a54e4b0e2669b30f2db',
			labelLoc : 'bl',
			bbox : {
				"left": -114.003737,
				"bottom": 35.130507,
				"right": -110.84875,
				"top": 37.71811
			},
			reaches : true
		},
		"CL" : {
			displayName : 'Canyonlands',
			itemId: '56461a7de4b0e2669b30f2df',
			labelLoc: 'tl',
			bbox: {
				"left": -110.26759,
				"bottom": 38.02643,
				"right": -109.47657,
				"top": 38.60718
			},
			appFeatureId : "CANYONLANDS"
		},
		"CHIP" : {
			displayName : 'Chippewa',
			itemId: '5a0c952ae4b09af898cd417b',
			labelLoc: 'bl',
			bbox: {
				"left": -92.24,
				"bottom": 44.383,
				"right": -91.814,
				"top": 44.674
			},
		},
		"DINO" : {
			displayName : 'Dinosaur',
			itemId: '56461a8be4b0e2669b30f2e1',
			bbox : {
				"left": -109.734,
				"bottom": 39.98,
				"right": -108.075,
				"top": 40.93
			},
			reaches : true
		},
		"CRD" : {
			displayName : 'Colorado River Delta',
			itemId: '56461a96e4b0e2669b30f2e4',
			labelLoc: 'bl',
			bbox : {
				"left": -115.12630,
				"bottom": 32.13372,
				"right": -114.55501,
				"top": 32.72718
			},
			reaches : true
		},
		"BIBE" : {
			displayName : 'Big Bend',
			itemId: '56461aa1e4b0e2669b30f2e6',
			bbox : {
				"left": -103.950969,
				"bottom": 28.780138,
				"right": -102.625809,
				"top": 29.561388
			},
			reaches : true
		}
	};
}();