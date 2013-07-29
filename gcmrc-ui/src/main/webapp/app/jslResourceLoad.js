var JSL = {};

JSL.ResourceLoad = function(forEachCallback, afterAllCallback) {
	return function(resp) {
		if (resp && resp.success) {
			if (!resp.success.data) {
				resp.success.data = [];
			}
			
			if (!Object.isArray(resp.success.data)) {
				resp.success.data = [resp.success.data];
			}
			
			if (forEachCallback) {
				resp.success.data.forEach(forEachCallback);
			}
			if (afterAllCallback) {
				afterAllCallback(resp.success.data);
			}
			
		} else {
			//There was a problem.
		}
	};
};


