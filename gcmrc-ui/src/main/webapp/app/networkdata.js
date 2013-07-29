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
			bbox : {
				"left": -114.003737,
				"bottom": 35.130507,
				"right": -110.132732,
				"top": 37.828968
			}
		},
		"DINO" : {
			displayName : 'Dinosaur',
			bbox : {
				"left": -109.592736,
				"bottom": 40.067194,
				"right": -107.693084,
				"top": 41.093398
			}
		},
		"BIBE" : {
			displayName : 'Big Bend',
			bbox : {
				"left": -103.950969,
				"bottom": 28.780138,
				"right": -102.625809,
				"top": 29.561388
			}
		}
	};
}();