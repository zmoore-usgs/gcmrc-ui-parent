//***** Pulled from Dygraph.numericTicks in dygraph-tickers.js
// main alteration is within the "if (ticks.length === 0) {" block
GCMRC.Dygraphs.ScaledTicker = function(decimalPlaces) {
	// This masks some numeric issues in older versions of Firefox,
	// where 1.0/Math.pow(10,2) != Math.pow(10,-2).
	/** @type {function(number,number):number} */
	var pow = function(base, exp) {
		if (exp < 0) {
			return 1.0 / Math.pow(base, -exp);
		}
		return Math.pow(base, exp);
	};

	var parameter_scale = pow(10, -decimalPlaces);


	return function(a, b, pixels, opts, dygraph, vals) {
		var pixels_per_tick = /** @type{number} */(opts('pixelsPerLabel'));
		var ticks = [];
		var i, j, tickV, nTicks;
		if (vals) {
			for (i = 0; i < vals.length; i++) {
				ticks.push({v: vals[i]});
			}
		} else {
			// TODO(danvk): factor this log-scale block out into a separate function.
			if (opts("logscale")) {
				nTicks = Math.floor(pixels / pixels_per_tick);
				var minIdx = Dygraph.binarySearch(a, Dygraph.PREFERRED_LOG_TICK_VALUES, 1);
				var maxIdx = Dygraph.binarySearch(b, Dygraph.PREFERRED_LOG_TICK_VALUES, -1);
				if (minIdx == -1) {
					minIdx = 0;
				}
				if (maxIdx == -1) {
					maxIdx = Dygraph.PREFERRED_LOG_TICK_VALUES.length - 1;
				}
				// Count the number of tick values would appear, if we can get at least
				// nTicks / 4 accept them.
				var lastDisplayed = null;
				if (maxIdx - minIdx >= nTicks / 4) {
					for (var idx = maxIdx; idx >= minIdx; idx--) {
						var tickValue = Dygraph.PREFERRED_LOG_TICK_VALUES[idx];
						var pixel_coord = Math.log(tickValue / a) / Math.log(b / a) * pixels;
						var tick = {v: tickValue};
						if (lastDisplayed === null) {
							lastDisplayed = {
								tickValue: tickValue,
								pixel_coord: pixel_coord
							};
						} else {
							if (Math.abs(pixel_coord - lastDisplayed.pixel_coord) >= pixels_per_tick) {
								lastDisplayed = {
									tickValue: tickValue,
									pixel_coord: pixel_coord
								};
							} else {
								tick.label = "";
							}
						}
						ticks.push(tick);
					}
					// Since we went in backwards order.
					ticks.reverse();
				}
			}

			// ticks.length won't be 0 if the log scale function finds values to insert.
			if (ticks.length === 0) {
				// Basic idea:
				// Try labels every 1, 2, 5, 10, 20, 50, 100, etc.
				// Calculate the resulting tick spacing (i.e. this.height_ / nTicks).
				// The first spacing greater than pixelsPerYLabel is what we use.
				// TODO(danvk): version that works on a log scale.
				var kmg2 = opts("labelsKMG2");
				var mults;
				if (kmg2) {
					mults = [1, 2, 4, 8];
				} else {
					mults = [1, 2, 5];
				}
				var scale, low_val, high_val;
				for (i = -10; i < 50; i++) {
					var base_scale;
					if (kmg2) {
						base_scale = pow(16, i);
					} else {
						base_scale = pow(10, i);
					}
					var spacing = 0;
					for (j = 0; j < mults.length; j++) {
						scale = base_scale * mults[j];
						if (parameter_scale <= scale) {
							low_val = Math.floor(a / scale) * scale;
							high_val = Math.ceil(b / scale) * scale;
							nTicks = Math.abs(high_val - low_val) / scale;
							spacing = pixels / nTicks;
							// wish I could break out of both loops at once...
							if (spacing > pixels_per_tick)
								break;
						}
					}
					if (spacing > pixels_per_tick)
						break;
				}


				// Construct the set of ticks.
				// Allow reverse y-axis if it's explicitly requested.
				if (low_val > high_val)
					scale *= -1;
				for (i = 0; i < nTicks; i++) {
					tickV = low_val + i * scale;
					ticks.push({v: tickV});
				}
			}
		}

		// Add formatted labels to the ticks.
		var k = 1;
		var k_labels = [];
		var m_labels = [];
		if (opts("labelsKMB")) {
			k = 1000;
			k_labels = ["K", "M", "B", "T", "Q"];
		}
		if (opts("labelsKMG2")) {
			if (k)
				Dygraph.warn("Setting both labelsKMB and labelsKMG2. Pick one!");
			k = 1024;
			k_labels = ["k", "M", "G", "T", "P", "E", "Z", "Y"];
			m_labels = ["m", "u", "n", "p", "f", "a", "z", "y"];
		}

		var formatter = /**@type{AxisLabelFormatter}*/(opts('axisLabelFormatter'));

		// Add labels to the ticks.
		var digitsAfterDecimal = /** @type{number} */(opts('digitsAfterDecimal'));
		for (i = 0; i < ticks.length; i++) {
			if (ticks[i].label !== undefined)
				continue;  // Use current label.
			tickV = ticks[i].v;
			var absTickV = Math.abs(tickV);
			// TODO(danvk): set granularity to something appropriate here.
			var label = formatter(tickV, 0, opts, dygraph);
			if (k_labels.length > 0) {
				// TODO(danvk): should this be integrated into the axisLabelFormatter?
				// Round up to an appropriate unit.
				var n = pow(k, k_labels.length);
				for (j = k_labels.length - 1; j >= 0; j--, n /= k) {
					if (absTickV >= n) {
						label = Dygraph.round_(tickV / n, digitsAfterDecimal) + k_labels[j];
						break;
					}
				}
			}
			if (opts("labelsKMG2")) {
				tickV = String(tickV.toExponential());
				if (tickV.split('e-').length === 2 && tickV.split('e-')[1] >= 3 && tickV.split('e-')[1] <= 24) {
					if (tickV.split('e-')[1] % 3 > 0) {
						label = Dygraph.round_(tickV.split('e-')[0] /
								pow(10, (tickV.split('e-')[1] % 3)),
								digitsAfterDecimal);
					} else {
						label = Number(tickV.split('e-')[0]).toFixed(2);
					}
					label += m_labels[Math.floor(tickV.split('e-')[1] / 3) - 1];
				}
			}
			ticks[i].label = label;
		}

		return ticks;
	};

};

