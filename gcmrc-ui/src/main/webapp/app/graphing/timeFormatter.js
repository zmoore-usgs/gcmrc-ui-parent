GCMRC.Dygraphs.timeFormatter = function(hoursOffset) {
	return function(t, granularity, opts, dygraph) {
		var result = "ERROR";
		if (t) {
			var d = Date.utc.create(t).utc(true);
			result = d.format('{yyyy}-{MM}-{dd}');
			if (granularity < 13) {
				result = d.format('{HH}:{mm}') + "\n" + result;
			}
		}
		return result;
	};
};

GCMRC.Dygraphs.timeLabelFormatter = function(hoursOffset) {
	return function(utcMillis, granularity, opts, dygraph) {
		var result = "ERROR";
		if (utcMillis) {
			var stationTimeMillis = utcMillis - (3600000 * hoursOffset);
			var d = Date.utc.create(stationTimeMillis).utc(true);
			result = d.format('{yyyy}-{MM}-{dd} {HH}:{mm}');
		}
		return result;
	}; 
};

