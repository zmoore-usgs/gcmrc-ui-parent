GCMRC.Dygraphs.DataFormatter = function(decimalPlaces) {
	if (!decimalPlaces) {
		decimalPlaces = 0;
	}

	function numberWithCommas(x) {
		var parts = x.toString().split(".");
		parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		return parts.join(".");
	}

	return function(val, granularity, opts, dygraph) {
		var result;
		
		//GCMRC-420 "-0" values popping up.
		var cleanedVal = val.round(decimalPlaces);
		
		result = numberWithCommas(cleanedVal.toFixed(decimalPlaces));
		
		return result;
	};
};