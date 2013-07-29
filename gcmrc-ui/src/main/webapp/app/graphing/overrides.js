GCMRC.Dygraphs.overrides = function(hoursOffset) {
	var dataGap = 30 * 24 * 60 * 60 * 1000;
	
	/**
	 * Initializes the Dygraph. This creates a new DIV and constructs the PlotKit
	 * and context &lt;canvas&gt; inside of it. See the constructor for details.
	 * on the parameters.
	 * @param {Element} div the Element to render the graph into.
	 * @param {String | Function} file Source data
	 * @param {Object} attrs Miscellaneous other options
	 * @private
	 */
	Dygraph.prototype.__init__ = function(div, file, attrs) {
		// Hack for IE: if we're using excanvas and the document hasn't finished
		// loading yet (and hence may not have initialized whatever it needs to
		// initialize), then keep calling this routine periodically until it has.
		if (/MSIE/.test(navigator.userAgent) && !window.opera &&
				typeof(G_vmlCanvasManager) != 'undefined' &&
				document.readyState != 'complete') {
			var self = this;
			setTimeout(function() {
				self.__init__(div, file, attrs);
			}, 100);
			return;
		}

		// Support two-argument constructor
		if (attrs === null || attrs === undefined) {
			attrs = {};
		}

		attrs = Dygraph.mapLegacyOptions_(attrs);

		if (typeof(div) == 'string') {
			div = document.getElementById(div);
		}

		if (!div) {
			Dygraph.error("Constructing dygraph with a non-existent div!");
			return;
		}

		this.isUsingExcanvas_ = typeof(G_vmlCanvasManager) != 'undefined';

		// Copy the important bits into the object
		// TODO(danvk): most of these should just stay in the attrs_ dictionary.
		this.maindiv_ = div;
		this.file_ = file;
		this.rollPeriod_ = attrs.rollPeriod || Dygraph.DEFAULT_ROLL_PERIOD;
		this.previousVerticalX_ = -1;
		this.fractions_ = attrs.fractions || false;
		this.dateWindow_ = attrs.dateWindow || null;

		this.is_initial_draw_ = true;
		this.annotations_ = [];

		// Zoomed indicators - These indicate when the graph has been zoomed and on what axis.
		this.zoomed_x_ = false;
		this.zoomed_y_ = false;

		// Clear the div. This ensure that, if multiple dygraphs are passed the same
		// div, then only one will be drawn.
		div.innerHTML = "";

		// For historical reasons, the 'width' and 'height' options trump all CSS
		// rules _except_ for an explicit 'width' or 'height' on the div.
		// As an added convenience, if the div has zero height (like <div></div> does
		// without any styles), then we use a default height/width.
		if (div.style.width === '' && attrs.width) {
			div.style.width = attrs.width + "px";
		}
		if (div.style.height === '' && attrs.height) {
			div.style.height = attrs.height + "px";
		}
		if (div.style.height === '' && div.clientHeight === 0) {
			div.style.height = Dygraph.DEFAULT_HEIGHT + "px";
			if (div.style.width === '') {
				div.style.width = Dygraph.DEFAULT_WIDTH + "px";
			}
		}
		// these will be zero if the dygraph's div is hidden.
		this.width_ = div.clientWidth;
		this.height_ = div.clientHeight;

		// TODO(danvk): set fillGraph to be part of attrs_ here, not user_attrs_.
		if (attrs.stackedGraph) {
			attrs.fillGraph = true;
			// TODO(nikhilk): Add any other stackedGraph checks here.
		}

		// DEPRECATION WARNING: All option processing should be moved from
		// attrs_ and user_attrs_ to options_, which holds all this information.
		//
		// Dygraphs has many options, some of which interact with one another.
		// To keep track of everything, we maintain two sets of options:
		//
		//  this.user_attrs_   only options explicitly set by the user.
		//  this.attrs_        defaults, options derived from user_attrs_, data.
		//
		// Options are then accessed this.attr_('attr'), which first looks at
		// user_attrs_ and then computed attrs_. This way Dygraphs can set intelligent
		// defaults without overriding behavior that the user specifically asks for.
		this.user_attrs_ = {};
		Dygraph.update(this.user_attrs_, attrs);

		// This sequence ensures that Dygraph.DEFAULT_ATTRS is never modified.
		this.attrs_ = {};
		Dygraph.updateDeep(this.attrs_, Dygraph.DEFAULT_ATTRS);

		this.boundaryIds_ = [];
		this.setIndexByName_ = {};
		this.datasetIndex_ = [];

		this.registeredEvents_ = [];
		this.eventListeners_ = {};

		this.attributes_ = new DygraphOptions(this);

		// Create the containing DIV and other interactive elements
		this.createInterface_();

		// Activate plugins.
		this.plugins_ = [];
		var plugins = Dygraph.PLUGINS.concat(this.getOption('plugins'));
		for (var i = 0; i < plugins.length; i++) {
			var Plugin = plugins[i];
			var pluginInstance = new Plugin();
			var pluginDict = {
				plugin: pluginInstance,
				events: {},
				options: {},
				pluginOptions: {}
			};

			var handlers = pluginInstance.activate(this);
			for (var eventName in handlers) {
				// TODO(danvk): validate eventName.
				pluginDict.events[eventName] = handlers[eventName];
			}

			this.plugins_.push(pluginDict);
		}

		// At this point, plugins can no longer register event handlers.
		// Construct a map from event -> ordered list of [callback, plugin].
		for (var i = 0; i < this.plugins_.length; i++) {
			var plugin_dict = this.plugins_[i];
			for (var eventName in plugin_dict.events) {
				if (!plugin_dict.events.hasOwnProperty(eventName))
					continue;
				var callback = plugin_dict.events[eventName];

				var pair = [plugin_dict.plugin, callback];
				if (!(eventName in this.eventListeners_) || !this.eventListeners_[eventName].isArray()) {
					this.eventListeners_[eventName] = [pair];
				} else {
					this.eventListeners_[eventName].push(pair);
				}
			}
		}

		this.createDragInterface_();

		this.start_();
	};
	
	// Overriding this from dygraph.js(1742)
	// The last line here where we're calling the callback is what I changed:
	// this.lastRow_ + this.getLeftBoundary_()
	// instead of not doing that.  we'll see what happens
	/**
	 * When the mouse moves in the canvas, display information about a nearby data
	 * point and draw dots over those points in the data series. This function
	 * takes care of cleanup of previously-drawn dots.
	 * @param {Object} event The mousemove event from the browser.
	 * @private
	 */
	Dygraph.prototype.mouseMove_ = function(event) {
		// This prevents JS errors when mousing over the canvas before data loads.
		var points = this.layout_.points;
		if (points === undefined || points === null)
			return;

		var canvasCoords = this.eventToDomCoords(event);
		var canvasx = canvasCoords[0];
		var canvasy = canvasCoords[1];

		var highlightSeriesOpts = this.attr_("highlightSeriesOpts");
		var selectionChanged = false;
		if (highlightSeriesOpts && !this.isSeriesLocked()) {
			var closest;
			if (this.attr_("stackedGraph")) {
				closest = this.findStackedPoint(canvasx, canvasy);
			} else {
				closest = this.findClosestPoint(canvasx, canvasy);
			}
			selectionChanged = this.setSelection(closest.row, closest.seriesName);
		} else {
			var idx = this.findClosestRow(canvasx);
			selectionChanged = this.setSelection(idx);
		}

		var callback = this.attr_("highlightCallback");
		if (callback && selectionChanged) {
			callback(event, this.lastx_, this.selPoints_, this.lastRow_ + this.getLeftBoundary_(), this.highlightSet_);
		}
	};

	Dygraph.LONG_TICK_PLACEMENTS = [];
	Dygraph.LONG_TICK_PLACEMENTS[Dygraph.MONTHLY] = {
		months: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11],
		year_mod: 1
	};
	Dygraph.LONG_TICK_PLACEMENTS[Dygraph.QUARTERLY] = {
		months: [0, 3, 6, 9],
		year_mod: 1
	};
	Dygraph.LONG_TICK_PLACEMENTS[Dygraph.BIANNUAL] = {
		months: [0, 6],
		year_mod: 1
	};
	Dygraph.LONG_TICK_PLACEMENTS[Dygraph.ANNUAL] = {
		months: [0],
		year_mod: 1
	};
	Dygraph.LONG_TICK_PLACEMENTS[Dygraph.DECADAL] = {
		months: [0],
		year_mod: 10
	};
	Dygraph.LONG_TICK_PLACEMENTS[Dygraph.CENTENNIAL] = {
		months: [0],
		year_mod: 100
	};

	/** @type {Dygraph.Ticker} */
	Dygraph.dateTicker = function(a, b, pixels, opts, dygraph, vals) {
		var chosen = Dygraph.pickDateTickGranularity(a, b, pixels, opts);

		if (chosen >= 0) {
			return Dygraph.getDateAxis(a, b, chosen, opts, dygraph);
		} else {
			// this can happen if self.width_ is zero.
			return [];
		}
	};

	/**
	 * @param {number} start_time
	 * @param {number} end_time
	 * @param {number} granularity (one of the granularities enumerated above)
	 * @param {function(string):*} opts Function mapping from option name -&gt; value.
	 * @param {Dygraph=} dg
	 * @return {!Dygraph.TickList}
	 */
	Dygraph.getDateAxis = function(start_time, end_time, granularity, opts, dg) {
		var formatter = /** @type{AxisLabelFormatter} */(
				opts("axisLabelFormatter"));
		var ticks = [];
		var t;

		if (granularity < Dygraph.MONTHLY) {
			// Generate one tick mark for every fixed interval of time.
			var spacing = Dygraph.SHORT_SPACINGS[granularity];

			// Find a time less than start_time which occurs on a "nice" time boundary
			// for this granularity.
			var g = spacing / 1000;
			var stationTimeStart = start_time - (3600000 * hoursOffset);
			var stationTimeEnd = end_time - (3600000 * hoursOffset);
			var d = new Date(stationTimeStart).utc(true);
			d.reset("millisecond");
			var x;
			if (g <= 60) {  // seconds
				x = d.getSeconds();
				d.setSeconds(x - x % g);
			} else {
				d.reset("second");
				g /= 60;
				if (g <= 60) {  // minutes
					x = d.getMinutes();
					d.setMinutes(x - x % g);
				} else {
					d.reset("minute");
					g /= 60;

					if (g <= 24) {  // days
						x = d.getUTCHours();
						d.setUTCHours(x - x % g);
					} else {
						d.utc(true).reset("hour");
						g /= 24;

						if (g == 7) {  // one week
							d.setUTCDate(d.getUTCDate() - d.getUTCDay());
						}
					}
				}
			}
			stationTimeStart = d.getTime();

			for (t = stationTimeStart; t <= stationTimeEnd; t += spacing) {
				var utcT = t + (3600000 * hoursOffset)
				ticks.push({v: utcT,
					label: formatter(t, granularity, opts, dg)
				});
			}
		} else {
			// Display a tick mark on the first of a set of months of each year.
			// Years get a tick mark iff y % year_mod == 0. This is useful for
			// displaying a tick mark once every 10 years, say, on long time scales.
			var months;
			var year_mod = 1;  // e.g. to only print one point every 10 years.

			if (granularity < Dygraph.NUM_GRANULARITIES) {
				months = Dygraph.LONG_TICK_PLACEMENTS[granularity].months;
				year_mod = Dygraph.LONG_TICK_PLACEMENTS[granularity].year_mod;
			} else {
				Dygraph.warn("Span of dates is too long");
			}

			var start_year = new Date(start_time).getFullYear();
			var end_year = new Date(end_time).getFullYear();
			var zeropad = Dygraph.zeropad;
			for (var i = start_year; i <= end_year; i++) {
				if (i % year_mod !== 0)
					continue;
				for (var j = 0; j < months.length; j++) {
					var date_str = i + "/" + zeropad(1 + months[j]) + "/01";
					t = Dygraph.dateStrToMillis(date_str);
					if (t < start_time || t > end_time)
						continue;
					ticks.push({v: t,
						label: formatter(new Date(t), granularity, opts, dg)
					});
				}
			}
		}

		return ticks;
	};

	/**
	 * This does the actual drawing of lines on the canvas, for just one series.
	 * Returns a list of [canvasx, canvasy] pairs for points for which a
	 * drawPointCallback should be fired.  These include isolated points, or all
	 * points if drawPoints=true.
	 * @param {Object} e The dictionary passed to the plotter function.
	 * @private
	 */
	DygraphCanvasRenderer._drawSeries = function(e,
			iter, strokeWidth, pointSize, drawPoints, drawGapPoints, stepPlot, color) {

		var prevCanvasX = null;
		var prevCanvasY = null;
		var nextCanvasY = null;
		var isIsolated; // true if this point is isolated (no line segments)
		var prevPoint;
		var point; // the point being processed in the while loop
		var pointsOnLine = []; // Array of [canvasx, canvasy] pairs.
		var first = true; // the first cycle through the while loop

		var ctx = e.drawingContext;
		ctx.beginPath();
		ctx.strokeStyle = color;
		ctx.lineWidth = strokeWidth;

		// NOTE: we break the iterator's encapsulation here for about a 25% speedup.
		var arr = iter.array_;
		var limit = iter.end_;
		var predicate = iter.predicate_;

		for (var i = iter.start_; i < limit; i++) {
			if ((i - 1) >= 0) {
				prevPoint = arr[i - 1];
			}
			point = arr[i];
			if (predicate) {
				while (i < limit && !predicate(arr, i)) {
					i++;
				}
				if (i == limit)
					break;
				point = arr[i];
			}

			if (point.canvasy === null || point.canvasy != point.canvasy) {
				if (stepPlot && prevCanvasX !== null) {
					// Draw a horizontal line to the start of the missing data
					ctx.moveTo(prevCanvasX, prevCanvasY);
					ctx.lineTo(point.canvasx, prevCanvasY);
				}
				prevCanvasX = prevCanvasY = null;
			} else {
				isIsolated = false;
				if (drawGapPoints || !prevCanvasX) {
					iter.nextIdx_ = i;
					iter.next();
					nextCanvasY = iter.hasNext ? iter.peek.canvasy : null;

					var isNextCanvasYNullOrNaN = nextCanvasY === null ||
							nextCanvasY != nextCanvasY;
					isIsolated = (!prevCanvasX && isNextCanvasYNullOrNaN);
					if (drawGapPoints) {
						// Also consider a point to be "isolated" if it's adjacent to a
						// null point, excluding the graph edges.
						if ((!first && !prevCanvasX) ||
								(iter.hasNext && isNextCanvasYNullOrNaN)) {
							isIsolated = true;
						}
					}
				}

				if (prevCanvasX !== null) {
					if (strokeWidth) {
						if (stepPlot) {
							ctx.moveTo(prevCanvasX, prevCanvasY);
							ctx.lineTo(point.canvasx, prevCanvasY);
						}

						if (prevPoint && prevPoint.xval && point && point.xval
								&& ((point.xval - prevPoint.xval) > dataGap)) {
							ctx.moveTo(point.canvasx, point.canvasy);
						} else {
							ctx.lineTo(point.canvasx, point.canvasy);
						}
					}
				} else {
					ctx.moveTo(point.canvasx, point.canvasy);
				}
				if (drawPoints || isIsolated) {
					pointsOnLine.push([point.canvasx, point.canvasy]);
				}
				prevCanvasX = point.canvasx;
				prevCanvasY = point.canvasy;
			}
			first = false;
		}
		ctx.stroke();
		return pointsOnLine;
	};

	/**
	 * Determine the correct granularity of ticks on a date axis.
	 *
	 * @param {number} a Left edge of the chart (ms)
	 * @param {number} b Right edge of the chart (ms)
	 * @param {number} pixels Size of the chart in the relevant dimension (width).
	 * @param {function(string):*} opts Function mapping from option name ->
	 *     value.
	 * @return {number} The appropriate axis granularity for this chart. See the
	 *     enumeration of possible values in dygraph-tickers.js.
	 */
	Dygraph.pickDateTickGranularity = function(a, b, pixels, opts) {
		var pixels_per_tick = /** @type{number} */(opts('pixelsPerTimeLabel')) || (opts('pixelsPerLabel'));
		for (var i = 0; i < Dygraph.NUM_GRANULARITIES; i++) {
			var num_ticks = Dygraph.numDateTicks(a, b, i);
			if (pixels / num_ticks >= pixels_per_tick) {
				return i;
			}
		}
		return -1;
	};

	/**
	 * @param {number} start_time
	 * @param {number} end_time
	 * @param {number} granularity (one of the granularities enumerated above)
	 * @return {number} Number of ticks that would result.
	 */
	Dygraph.numDateTicks = function(start_time, end_time, granularity) {
		if (granularity < Dygraph.MONTHLY) {
			// Generate one tick mark for every fixed interval of time.
			var spacing = Dygraph.SHORT_SPACINGS[granularity];
			return Math.floor(0.5 + 1.0 * (end_time - start_time) / spacing);
		} else {
			var tickPlacement = Dygraph.LONG_TICK_PLACEMENTS[granularity];

			var msInYear = 365.2524 * 24 * 3600 * 1000;
			var num_years = 1.0 * (end_time - start_time) / msInYear;
			return Math.floor(0.5 + 1.0 * num_years * tickPlacement.months.length / tickPlacement.year_mod);

			var msInYear = 365.2524 * 24 * 3600 * 1000;
			var num_years = 1.0 * (end_time - start_time) / msInYear;
			return Math.floor(0.5 + 1.0 * num_years * num_months / year_mod);
		}
	};

	/**
	 * @private
	 * Calculates the rolling average of a data set.
	 * If originalData is [label, val], rolls the average of those.
	 * If originalData is [label, [, it's interpreted as [value, stddev]
	 *   and the roll is returned in the same form, with appropriately reduced
	 *   stddev for each value.
	 * Note that this is where fractional input (i.e. '5/10') is converted into
	 *   decimal values.
	 * @param {Array} originalData The data in the appropriate format (see above)
	 * @param {Number} rollPeriod The number of points over which to average the
	 *                            data
	 */
	Dygraph.prototype.rollingAverage = function(originalData, rollPeriod) {
		rollPeriod = Math.min(rollPeriod, originalData.length);
		var rollingData = [];
		
		var sigma = this.attr_("sigma");

		var low, high, i, j, y, sum, num_ok, stddev;
		if (this.fractions_) {
			var num = 0;
			var den = 0;  // numerator/denominator
			var mult = 100.0;
			for (i = 0; i < originalData.length; i++) {
				num += originalData[i][1][0];
				den += originalData[i][1][1];
				if (i - rollPeriod >= 0) {
					num -= originalData[i - rollPeriod][1][0];
					den -= originalData[i - rollPeriod][1][1];
				}

				var date = originalData[i][0];
				var value = den ? num / den : 0.0;
				if (this.attr_("errorBars")) {
					if (this.attr_("wilsonInterval")) {
						// For more details on this confidence interval, see:
						// http://en.wikipedia.org/wiki/Binomial_confidence_interval
						if (den) {
							var p = value < 0 ? 0 : value, n = den;
							var pm = sigma * Math.sqrt(p * (1 - p) / n + sigma * sigma / (4 * n * n));
							var denom = 1 + sigma * sigma / den;
							low = (p + sigma * sigma / (2 * den) - pm) / denom;
							high = (p + sigma * sigma / (2 * den) + pm) / denom;
							rollingData[i] = [date,
								[p * mult, (p - low) * mult, (high - p) * mult]];
						} else {
							rollingData[i] = [date, [0, 0, 0]];
						}
					} else {
						stddev = den ? sigma * Math.sqrt(value * (1 - value) / den) : 1.0;
						rollingData[i] = [date, [mult * value, mult * stddev, mult * stddev]];
					}
				} else {
					rollingData[i] = [date, mult * value];
				}
			}
		} else if (this.attr_("customBars")) {
			low = 0;
			var mid = 0;
			high = 0;
			var count = 0;
			for (i = 0; i < originalData.length; i++) {
				var data = originalData[i][1];
				y = data[1];
				rollingData[i] = [originalData[i][0], [y, y - data[0], data[2] - y]];

				if (y !== null && !isNaN(y)) {
					low = (low + data[0]).round(8);
					mid = (mid + y).round(8);
					high = (high + data[2]).round(8);
					count += 1;
				}
				if (i - rollPeriod >= 0) {
					var prev = originalData[i - rollPeriod];
					if (prev[1][1] !== null && !isNaN(prev[1][1])) {
						low = (low - prev[1][0]).round(8);
						mid = (mid - prev[1][1]).round(8);
						high = (high - prev[1][2]).round(8);
						count -= 1;
					}
				}
				if (count) {
					rollingData[i] = [originalData[i][0], [1.0 * mid / count,
							1.0 * (mid - low) / count,
							1.0 * (high - mid) / count]];
				} else {
					rollingData[i] = [originalData[i][0], [null, null, null]];
				}
			}
		} else {
			// Calculate the rolling average for the first rollPeriod - 1 points where
			// there is not enough data to roll over the full number of points
			if (!this.attr_("errorBars")) {
				if (rollPeriod == 1) {
					return originalData;
				}

				for (i = 0; i < originalData.length; i++) {
					sum = 0;
					num_ok = 0;
					for (j = Math.max(0, i - rollPeriod + 1); j < i + 1; j++) {
						y = originalData[j][1];
						if (y === null || isNaN(y))
							continue;
						num_ok++;
						sum += originalData[j][1];
					}
					if (num_ok) {
						rollingData[i] = [originalData[i][0], sum / num_ok];
					} else {
						rollingData[i] = [originalData[i][0], null];
					}
				}

			} else {
				for (i = 0; i < originalData.length; i++) {
					sum = 0;
					var variance = 0;
					num_ok = 0;
					for (j = Math.max(0, i - rollPeriod + 1); j < i + 1; j++) {
						y = originalData[j][1][0];
						if (y === null || isNaN(y))
							continue;
						num_ok++;
						sum += originalData[j][1][0];
						variance += Math.pow(originalData[j][1][1], 2);
					}
					if (num_ok) {
						stddev = Math.sqrt(variance) / num_ok;
						rollingData[i] = [originalData[i][0],
							[sum / num_ok, sigma * stddev, sigma * stddev]];
					} else {
						// This explicitly preserves NaNs to aid with "independent series".
						// See testRollingAveragePreservesNaNs.
						var v = (rollPeriod == 1) ? originalData[i][1][0] : null;
						rollingData[i] = [originalData[i][0], [v, v, v]];
					}
				}
			}
		}
		

		return rollingData;
	};

	//******************************************
	//           ANNOTATIONS START
	//******************************************

	Dygraph.Plugins.Annotations.prototype.didDrawChart = function(e) {
		var g = e.dygraph;

		// Early out in the (common) case of zero annotations.
		var points = g.layout_.annotated_points;
		if (!points || points.length === 0)
			return;

		var containerDiv = e.canvas.parentNode;
		var annotationStyle = {
			"position": "absolute",
			"fontSize": g.getOption('axisLabelFontSize') + "px",
			"zIndex": 10,
			"overflow": "hidden"
		};

		var bindEvt = function(eventName, classEventName, pt) {
			return function(annotation_event) {
				var a = pt.annotation;
				if (a.hasOwnProperty(eventName)) {
					a[eventName](a, pt, g, annotation_event);
				} else if (g.getOption(classEventName)) {
					g.getOption(classEventName)(a, pt, g, annotation_event);
				}
			};
		};

		// Add the annotations one-by-one.
		var area = e.dygraph.plotter_.area;

		// x-coord to sum of previous annotation's heights (used for stacking).
		var xToUsedHeight = {};

		for (var i = 0; i < points.length; i++) {
			var p = points[i];
			if (p.canvasx < area.x || p.canvasx > area.x + area.w ||
					p.canvasy < area.y || p.canvasy > area.y + area.h) {
				continue;
			}

			var a = p.annotation;
			var tick_height = 6;
			if (a.hasOwnProperty("tickHeight")) {
				tick_height = a.tickHeight;
			}

			var div = document.createElement("div");
			for (var name in annotationStyle) {
				if (annotationStyle.hasOwnProperty(name)) {
					div.style[name] = annotationStyle[name];
				}
			}
			if (!a.hasOwnProperty('icon')) {
				div.className = "dygraphDefaultAnnotation";
			}
			if (a.hasOwnProperty('cssClass')) {
				div.className += " " + a.cssClass;
			}

			var width = a.hasOwnProperty('width') ? a.width : 16;
			var height = a.hasOwnProperty('height') ? a.height : 16;
			if (a.hasOwnProperty('icon')) {
				var img = document.createElement("img");
				img.src = a.icon;
				img.width = width;
				img.height = height;
				div.appendChild(img);
			} else if (p.annotation.hasOwnProperty('shortText')) {
				div.appendChild(document.createTextNode(p.annotation.shortText));
			}
			var left = p.canvasx - width / 2;
			div.style.left = left + "px";
			var divTop = 0;
			if (a.attachAtBottom) {
				var y = (area.y + area.h - height - tick_height);
				if (xToUsedHeight[left]) {
					y -= xToUsedHeight[left];
				} else {
					xToUsedHeight[left] = 0;
				}
				xToUsedHeight[left] += (tick_height + height);
				divTop = y;
			} else {
				divTop = p.canvasy - height - tick_height;
			}
			div.style.top = divTop + "px";
//    div.style.width = width + "px";  //the browser will calculate these
//			div.style.height = height + "px";
			div.title = p.annotation.text;
			div.style.color = g.colorsMap_[p.name];
			div.style.borderColor = g.colorsMap_[p.name];
			a.div = div;

			g.addEvent(div, 'click',
					bindEvt('clickHandler', 'annotationClickHandler', p, this));
			g.addEvent(div, 'mouseover',
					bindEvt('mouseOverHandler', 'annotationMouseOverHandler', p, this));
			g.addEvent(div, 'mouseout',
					bindEvt('mouseOutHandler', 'annotationMouseOutHandler', p, this));
			g.addEvent(div, 'dblclick',
					bindEvt('dblClickHandler', 'annotationDblClickHandler', p, this));

			containerDiv.appendChild(div);
			this.annotations_.push(div);

			var ctx = e.drawingContext;
			ctx.save();
			ctx.strokeStyle = g.colorsMap_[p.name];
			ctx.beginPath();
			if (!a.attachAtBottom) {
				ctx.moveTo(p.canvasx, p.canvasy);
				ctx.lineTo(p.canvasx, p.canvasy - 2 - tick_height);
			} else {
				var y = divTop + height;
				ctx.moveTo(p.canvasx, 0);
				ctx.lineTo(p.canvasx, y + tick_height);
			}
			ctx.closePath();
			ctx.stroke();
			ctx.restore();
		}
	};

	//******************************************
	//           RANGE SELECTOR START
	//******************************************

	/**
	 * @private
	 * Sets up the interaction for the range selector.
	 */
	Dygraph.Plugins.RangeSelector.prototype.initInteraction_ = function() {
		var self = this;
		var topElem = this.isIE_ ? document : window;
		var clientXLast = 0;
		var handle = null;
		var isZooming = false;
		var isPanning = false;
		var dynamic = !this.isMobileDevice_ && !this.isUsingExcanvas_;

		// We cover iframes during mouse interactions. See comments in
		// dygraph-utils.js for more info on why this is a good idea.
		var tarp = new Dygraph.IFrameTarp();

		// functions, defined below.  Defining them this way (rather than with
		// "function foo() {...}" makes JSHint happy.
		var toXDataWindow, onZoomStart, onZoom, onZoomEnd, doZoom, isMouseInPanZone,
				onPanStart, onPan, onPanEnd, doPan, onCanvasHover;

		// Touch event functions
		var onZoomHandleTouchEvent, onCanvasTouchEvent, addTouchEvents;

		toXDataWindow = function(zoomHandleStatus) {
			var xDataLimits = self.dygraph_.getOption("originalDateWindow") || self.dygraph_.xAxisExtremes();
			var fact = (xDataLimits[1] - xDataLimits[0]) / self.canvasRect_.w;
			var xDataMin = xDataLimits[0] + (zoomHandleStatus.leftHandlePos - self.canvasRect_.x) * fact;
			var xDataMax = xDataLimits[0] + (zoomHandleStatus.rightHandlePos - self.canvasRect_.x) * fact;
			return [xDataMin, xDataMax];
		};

		onZoomStart = function(e) {
			Dygraph.cancelEvent(e);
			isZooming = true;
			clientXLast = e.clientX;
			handle = e.target ? e.target : e.srcElement;
			if (e.type === 'mousedown' || e.type === 'dragstart') {
				// These events are removed manually.
				Dygraph.addEvent(topElem, 'mousemove', onZoom);
				Dygraph.addEvent(topElem, 'mouseup', onZoomEnd);
			}
			self.fgcanvas_.style.cursor = 'col-resize';
			tarp.cover();
			return true;
		};

		onZoom = function(e) {
			if (!isZooming) {
				return false;
			}
			Dygraph.cancelEvent(e);

			var delX = e.clientX - clientXLast;
			if (Math.abs(delX) < 4) {
				return true;
			}
			clientXLast = e.clientX;

			// Move handle.
			var zoomHandleStatus = self.getZoomHandleStatus_();
			var newPos;
			if (handle == self.leftZoomHandle_) {
				newPos = zoomHandleStatus.leftHandlePos + delX;
				newPos = Math.min(newPos, zoomHandleStatus.rightHandlePos - handle.width - 3);
				newPos = Math.max(newPos, self.canvasRect_.x);
			} else {
				newPos = zoomHandleStatus.rightHandlePos + delX;
				newPos = Math.min(newPos, self.canvasRect_.x + self.canvasRect_.w);
				newPos = Math.max(newPos, zoomHandleStatus.leftHandlePos + handle.width + 3);
			}
			var halfHandleWidth = handle.width / 2;
			handle.style.left = (newPos - halfHandleWidth) + 'px';
			self.drawInteractiveLayer_();

			// Zoom on the fly (if not using excanvas).
			if (dynamic) {
				doZoom();
			}
			return true;
		};

		onZoomEnd = function(e) {
			if (!isZooming) {
				return false;
			}
			isZooming = false;
			tarp.uncover();
			Dygraph.removeEvent(topElem, 'mousemove', onZoom);
			Dygraph.removeEvent(topElem, 'mouseup', onZoomEnd);
			self.fgcanvas_.style.cursor = 'default';

			// If using excanvas, Zoom now.
			if (!dynamic) {
				doZoom();
			}
			return true;
		};

		doZoom = function() {
			try {
				var zoomHandleStatus = self.getZoomHandleStatus_();
				self.isChangingRange_ = true;
//				if (!zoomHandleStatus.isZoomed) {
//					self.dygraph_.resetZoom();
//				} else {
				var xDataWindow = toXDataWindow(zoomHandleStatus);
				self.dygraph_.doZoomXDates_(xDataWindow[0], xDataWindow[1]);
//				}
			} finally {
				self.isChangingRange_ = false;
			}
		};

		isMouseInPanZone = function(e) {
			if (self.isUsingExcanvas_) {
				return e.srcElement == self.iePanOverlay_;
			} else {
				var rect = self.leftZoomHandle_.getBoundingClientRect();
				var leftHandleClientX = rect.left + rect.width / 2;
				rect = self.rightZoomHandle_.getBoundingClientRect();
				var rightHandleClientX = rect.left + rect.width / 2;
				return (e.clientX > leftHandleClientX && e.clientX < rightHandleClientX);
			}
		};

		onPanStart = function(e) {
			if (!isPanning && isMouseInPanZone(e) && self.getZoomHandleStatus_().isZoomed) {
				Dygraph.cancelEvent(e);
				isPanning = true;
				clientXLast = e.clientX;
				if (e.type === 'mousedown') {
					// These events are removed manually.
					Dygraph.addEvent(topElem, 'mousemove', onPan);
					Dygraph.addEvent(topElem, 'mouseup', onPanEnd);
				}
				return true;
			}
			return false;
		};

		onPan = function(e) {
			if (!isPanning) {
				return false;
			}
			Dygraph.cancelEvent(e);

			var delX = e.clientX - clientXLast;
			if (Math.abs(delX) < 4) {
				return true;
			}
			clientXLast = e.clientX;

			// Move range view
			var zoomHandleStatus = self.getZoomHandleStatus_();
			var leftHandlePos = zoomHandleStatus.leftHandlePos;
			var rightHandlePos = zoomHandleStatus.rightHandlePos;
			var rangeSize = rightHandlePos - leftHandlePos;
			if (leftHandlePos + delX <= self.canvasRect_.x) {
				leftHandlePos = self.canvasRect_.x;
				rightHandlePos = leftHandlePos + rangeSize;
			} else if (rightHandlePos + delX >= self.canvasRect_.x + self.canvasRect_.w) {
				rightHandlePos = self.canvasRect_.x + self.canvasRect_.w;
				leftHandlePos = rightHandlePos - rangeSize;
			} else {
				leftHandlePos += delX;
				rightHandlePos += delX;
			}
			var halfHandleWidth = self.leftZoomHandle_.width / 2;
			self.leftZoomHandle_.style.left = (leftHandlePos - halfHandleWidth) + 'px';
			self.rightZoomHandle_.style.left = (rightHandlePos - halfHandleWidth) + 'px';
			self.drawInteractiveLayer_();

			// Do pan on the fly (if not using excanvas).
			if (dynamic) {
				doPan();
			}
			return true;
		};

		onPanEnd = function(e) {
			if (!isPanning) {
				return false;
			}
			isPanning = false;
			Dygraph.removeEvent(topElem, 'mousemove', onPan);
			Dygraph.removeEvent(topElem, 'mouseup', onPanEnd);
			// If using excanvas, do pan now.
			if (!dynamic) {
				doPan();
			}
			return true;
		};

		doPan = function() {
			try {
				self.isChangingRange_ = true;
				self.dygraph_.dateWindow_ = toXDataWindow(self.getZoomHandleStatus_());
				self.dygraph_.drawGraph_(false);
			} finally {
				self.isChangingRange_ = false;
			}
		};

		onCanvasHover = function(e) {
			if (isZooming || isPanning) {
				return;
			}
			var cursor = isMouseInPanZone(e) ? 'move' : 'default';
			if (cursor != self.fgcanvas_.style.cursor) {
				self.fgcanvas_.style.cursor = cursor;
			}
		};

		onZoomHandleTouchEvent = function(e) {
			if (e.type == 'touchstart' && e.targetTouches.length == 1) {
				if (onZoomStart(e.targetTouches[0])) {
					Dygraph.cancelEvent(e);
				}
			} else if (e.type == 'touchmove' && e.targetTouches.length == 1) {
				if (onZoom(e.targetTouches[0])) {
					Dygraph.cancelEvent(e);
				}
			} else {
				onZoomEnd(e);
			}
		};

		onCanvasTouchEvent = function(e) {
			if (e.type == 'touchstart' && e.targetTouches.length == 1) {
				if (onPanStart(e.targetTouches[0])) {
					Dygraph.cancelEvent(e);
				}
			} else if (e.type == 'touchmove' && e.targetTouches.length == 1) {
				if (onPan(e.targetTouches[0])) {
					Dygraph.cancelEvent(e);
				}
			} else {
				onPanEnd(e);
			}
		};

		addTouchEvents = function(elem, fn) {
			var types = ['touchstart', 'touchend', 'touchmove', 'touchcancel'];
			for (var i = 0; i < types.length; i++) {
				self.dygraph_.addEvent(elem, types[i], fn);
			}
		};

		this.setDefaultOption_('interactionModel', Dygraph.Interaction.dragIsPanInteractionModel);
		this.setDefaultOption_('panEdgeFraction', 0.0001);

		var dragStartEvent = window.opera ? 'mousedown' : 'dragstart';
		this.dygraph_.addEvent(this.leftZoomHandle_, dragStartEvent, onZoomStart);
		this.dygraph_.addEvent(this.rightZoomHandle_, dragStartEvent, onZoomStart);

		if (this.isUsingExcanvas_) {
			this.dygraph_.addEvent(this.iePanOverlay_, 'mousedown', onPanStart);
		} else {
			this.dygraph_.addEvent(this.fgcanvas_, 'mousedown', onPanStart);
			this.dygraph_.addEvent(this.fgcanvas_, 'mousemove', onCanvasHover);
		}

		// Touch events
		if (this.hasTouchInterface_) {
			addTouchEvents(this.leftZoomHandle_, onZoomHandleTouchEvent);
			addTouchEvents(this.rightZoomHandle_, onZoomHandleTouchEvent);
			addTouchEvents(this.fgcanvas_, onCanvasTouchEvent);
		}
	};

	/**
	 * @private
	 * Draws the mini plot in the background canvas.
	 */
	Dygraph.Plugins.RangeSelector.prototype.drawMiniPlot_ = function() {
		var fillStyle = this.getOption_('rangeSelectorPlotFillColor');
		var strokeStyle = this.getOption_('rangeSelectorPlotStrokeColor');
		if (!fillStyle && !strokeStyle) {
			return;
		}

		var stepPlot = this.getOption_('stepPlot');

		var combinedSeriesData = this.computeCombinedSeriesAndLimits_();
		var yRange = combinedSeriesData.yMax - combinedSeriesData.yMin;

		// Draw the mini plot.
		var ctx = this.bgcanvas_ctx_;
		var margin = 0.5;

		var dateWindow = this.getOption_('originalDateWindow');
		var xExtremes = dateWindow || this.dygraph_.xAxisExtremes();
		var xRange = Math.max(xExtremes[1] - xExtremes[0], 1.e-30);
		var xFact = (this.canvasRect_.w - margin) / xRange;
		var yFact = (this.canvasRect_.h - margin) / yRange;
		var canvasWidth = this.canvasRect_.w - margin;
		var canvasHeight = this.canvasRect_.h - margin;

		var prevX = null, prevY = null;

		ctx.beginPath();
		ctx.moveTo(margin, canvasHeight);
		for (var i = 0; i < combinedSeriesData.data.length; i++) {
			var prevDataPoint;
			if (0 < i) {
				prevDataPoint = combinedSeriesData.data[i - 1];
			}
			var dataPoint = combinedSeriesData.data[i];
			var x = ((dataPoint[0] !== null) ? ((dataPoint[0] - xExtremes[0]) * xFact) : NaN);
			var y = ((dataPoint[1] !== null) ? (canvasHeight - (dataPoint[1] - combinedSeriesData.yMin) * yFact) : NaN);
			if (isFinite(x) && isFinite(y)) {
				if (prevX === null) {
					ctx.lineTo(x, canvasHeight);
				}
				else if (stepPlot) {
					ctx.lineTo(x, prevY);
				}
				if (prevDataPoint && (dataPoint[0] - prevDataPoint[0]) > dataGap) {
					ctx.lineTo(prevX, canvasHeight);
					ctx.lineTo(x, canvasHeight);
				}
				ctx.lineTo(x, y);
				prevX = x;
				prevY = y;
			}
			else {
				if (prevX !== null) {
					if (stepPlot) {
						ctx.lineTo(x, prevY);
						ctx.lineTo(x, canvasHeight);
					}
					else {
						ctx.lineTo(prevX, canvasHeight);
					}
				}
				prevX = prevY = null;
			}
		}
		if (prevX) {
			ctx.lineTo(prevX, canvasHeight);
		} else {
			ctx.lineTo(canvasWidth, canvasHeight);
		}
		ctx.closePath();

		if (fillStyle) {
			var lingrad = this.bgcanvas_ctx_.createLinearGradient(0, 0, 0, canvasHeight);
			lingrad.addColorStop(0, 'white');
			lingrad.addColorStop(1, fillStyle);
			this.bgcanvas_ctx_.fillStyle = lingrad;
			ctx.fill();
		}

		if (strokeStyle) {
			this.bgcanvas_ctx_.strokeStyle = strokeStyle;
			this.bgcanvas_ctx_.lineWidth = 1.5;
			ctx.stroke();
		}
	};

	/**
	 * @private
	 * Places the zoom handles in the proper position based on the current X data window.
	 */
	Dygraph.Plugins.RangeSelector.prototype.placeZoomHandles_ = function() {
		var xExtremes = this.getOption_('originalDateWindow') || this.dygraph_.xAxisExtremes();
		var xWindowLimits = this.dygraph_.xAxisRange();
		var xRange = xExtremes[1] - xExtremes[0];
		var leftPercent = Math.max(0, (xWindowLimits[0] - xExtremes[0]) / xRange);
		var rightPercent = Math.max(0, (xExtremes[1] - xWindowLimits[1]) / xRange);
		var leftCoord = this.canvasRect_.x + this.canvasRect_.w * leftPercent;
		var rightCoord = this.canvasRect_.x + this.canvasRect_.w * (1 - rightPercent);
		var handleTop = Math.max(this.canvasRect_.y, this.canvasRect_.y + (this.canvasRect_.h - this.leftZoomHandle_.height) / 2);
		var halfHandleWidth = this.leftZoomHandle_.width / 2;
		this.leftZoomHandle_.style.left = (leftCoord - halfHandleWidth) + 'px';
		this.leftZoomHandle_.style.top = handleTop + 'px';
		this.rightZoomHandle_.style.left = (rightCoord - halfHandleWidth) + 'px';
		this.rightZoomHandle_.style.top = this.leftZoomHandle_.style.top;

		this.leftZoomHandle_.style.visibility = 'visible';
		this.rightZoomHandle_.style.visibility = 'visible';
	};

};


