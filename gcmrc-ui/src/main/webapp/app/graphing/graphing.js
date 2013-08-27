GCMRC.Dygraphs.overrides(CONFIG.networkHoursOffset);

GCMRC.Graphing = function(hoursOffset) {
	var blockRedraw = false;
	var blockHighlight = false;

	var graphs = {};

	var isResizeListenerAttached = false;

	var urls = {
		agg: 'services/agg/'
	};

	var showInfoMessage = function(locator, msg) {
		$(locator).append('<div class="alert alert-info"><button type="button" class="close" data-dismiss="alert">×</button>' + msg + '</div>');
	};
	
	var showWarningMessage = function(msg) {
		$("#errorMsg").append('<div class="alert"><button type="button" class="close" data-dismiss="alert">×</button>' + msg + '</div>');
	};
	var clearWarningMessage = function(msg) {
		$("#errorMsg").empty();
	};

	var showErrorMessage = function(msg) {
		$("#errorMsg").append('<div class="alert alert-error"><button type="button" class="close" data-dismiss="alert">×</button>' + msg + '</div>');
	};
	var clearErrorMessage = function(msg) {
		$("#errorMsg").empty();
	};
	
	var isNaN = function(x) {
		return x !== x;
	};
	
	var isNullUndefinedOrNaN = function(x, undefined) {
		return (x === null ||
				x === undefined ||
				isNaN(x));
	};
	
	var dealWithResponse = function(graphToMake, data, config, buildGraph) {
		var parseColData = function(str) {
			var result = null;
			var low = null, med = null, high = null;

			if (str) {
				var strSplit = str.split(';');
				if (2 < strSplit.length) {
					low = parseFloat(strSplit[0]);
					med = parseFloat(strSplit[1]);
					high = parseFloat(strSplit[2]);
				} else {
					med = parseFloat(str);
				}
			}

			if (!isNullUndefinedOrNaN(med)) {
				if (isNullUndefinedOrNaN(low)) {
					low = med;
				}
				if (isNullUndefinedOrNaN(high)) {
					high = med;
				}
			}
			result = [low, med, high];

			return result;
		};
		var timeColumn = "time";
		var conf = $.extend({}, config);
		var hasData = false;
		conf.data = data.success.data.map(function(el) {
			var result;

			result = [parseInt(el[timeColumn])];

			var dataColumns = graphToMake.columns.filter(function(n){return !n.startsWith(timeColumn)}).map(function(col) {
				return parseColData(el[col]);
			});

			[].push.apply(result, dataColumns);


			if (!hasData && dataColumns.reduce(function(a, b) {
				return a || b.some(function(e){
					return e === 0 || !!e;
				});
			}, false)) {
				hasData = true;
			}

			return result;
		});
		
		var identifier = graphToMake.groupId;
		var parameterMetadata = GCMRC.Page.params[identifier].description;
		var graphName = parameterMetadata['pCodeName'] || parameterMetadata['displayName'];
		
		if (hasData) {
			conf.labels = [timeColumn];
			[].push.apply(conf.labels, graphToMake.columns.filter(function(n){return !n.startsWith(timeColumn)}).map(function(el) {
				return GCMRC.Page.params[identifier][el.split("!")[0]]['displayName'];
			}));
			conf['yAxisLabel'] = graphToMake.yAxisLabel || graphName + " (" + parameterMetadata['unitsShort'] + ")";
			conf['dataformatter'] = GCMRC.Dygraphs.DataFormatter(parameterMetadata['decimalPlaces']);
			conf['decimalPlaces'] = parameterMetadata['decimalPlaces'];
			conf["parameterName"] = identifier;
			conf["div"] = $('#' + conf.divId + ' div.p' + identifier).get(0);
			conf["labelDiv"] = $('#' + conf.labelDivId + ' div.p' + identifier).get(0);
			conf["colors"] = [];
			conf["highlightColor"] = {};
			[].push.apply(conf.colors, graphToMake.columns.filter(function(n){return !n.startsWith(timeColumn)}).map(function(el) {
				var param = GCMRC.Page.params[identifier][el.split("!")[0]];
				this[param['displayName']] = param['highlightColor'];
				return param['color'];
			}, conf.highlightColor));

			conf["series"] = {};
			graphToMake.columns.filter(function(n){return !n.startsWith(timeColumn)}).map(function(el) {
				var param = GCMRC.Page.params[identifier][el.split("!")[0]];
				this[param['displayName']] = param.series;
			}, conf["series"]);

			buildGraph(conf);
		} else {
			showInfoMessage("#" + conf.divId + ' div.p' + identifier, "There were no data during this period for " + graphName);
		}

	};

	var buildGraph = function(config) {
		if (!config)
			config = {};

		var containerId = config.divId || "unknown";
		var parameterName = config.parameterName || "unknown";
		var div = config.div;
//		var labelDiv = config.labelDiv;
		
		var labels = config['labels'];
		
		var dateWindow = config['dateWindow'].clone();
		dateWindow[1] = dateWindow[1] + (24 * 60 * 60 * 1000) - 1;
		
		div.style.display = "inline-block";
		div.style.margin = "4px";

		var data = config['data'];

		var title = config['graphTitle'] || '';

		var yAxisLabel = config['yAxisLabel'] || 'Data';

		var dataformatter = config['dataformatter'] || GCMRC.Dygraphs.DataFormatter(0);
		var decimalPlaces = config['decimalPlaces'] || 0;
		
		var confColors = config['colors'] || ["#006666", "#333399", "#CC9966", "#CC9966"];
		var highlightColor = config['highlightColor'];

		var axes = {
			y: {
				axisLabelFormatter: dataformatter,
				valueFormatter: dataformatter
			},
			x: {
				ticker: Dygraph.dateTicker,
				axisLabelFormatter: GCMRC.Dygraphs.timeFormatter(hoursOffset),
				valueFormatter: GCMRC.Dygraphs.timeLabelFormatter(hoursOffset),
				pixelsPerLabel: 95
			}
		};

		var lighterColorHighlightPoint = function(g, name, ctx, canvasx, canvasy, color, radius) {
			var lighterColor = highlightColor[name] || color;
			ctx.beginPath();
			ctx.fillStyle = lighterColor;
			ctx.arc(canvasx, canvasy, radius, 0, 2 * Math.PI, false);
			ctx.fill();
		};
		
		var graphWidth = $('#' + containerId).width() - 15;//TODO add $(window).resize() listener
		
		var opts = {
			title: title,
			labelsDivStyles: {
				'textAlign': 'right'
			},
			width: graphWidth,
			height: 420,
			xlabel: 'Time',
			ylabel: yAxisLabel,
			axisLabelWidth: 85,
			yAxisLabelWidth: 85,
			xAxisLabelWidth: 85,
			xAxisHeight: 50,
			axes: axes,
			labels: labels,
			dateWindow : dateWindow,
			originalDateWindow : dateWindow,
			//To be used in "fixed" legend
//			labelsDiv: labelDiv,
//			labelsSeparateLines: true,
//			legend: 'always',
			showRangeSelector: true,
			connectSeparatedPoints: false,
			highlightCircleSize: 4,
			strokeWidth: 2,
			pixelsPerTimeLabel : 95,
			ticker: GCMRC.Dygraphs.ScaledTicker(decimalPlaces),
			drawHighlightPointCallback: lighterColorHighlightPoint,
			customBars: true,
			drawPoints: false,
			stackedGraph: false,
			colors: confColors,
			drawCallback: function(me, initial) {
				if (blockRedraw || initial)
					return;
				blockRedraw = true;
				var range = me.xAxisRange();
				$.each(graphs[containerId], function(key, val) {
					if (val !== me) {
						val.updateOptions({
							dateWindow: range
						});
					}
				});
				blockRedraw = false;
			},
			highlightCallback: function(event, x, points, row, seriesName) {
				if (blockHighlight) {
					return;
				}
				blockHighlight = true;
				$.each(graphs[containerId], function(key, graph) {
					graph.setSelection(row);
					var canvasx;
					if (graph && graph.selPoints_.length > 0 && graph.selPoints_[0]) {
						canvasx = graph.selPoints_[0].canvasx;
					} else {
						//if no selected point?  TODO, is this needed now that we interpolate?
						if (points && points.length > 0 && points[0]) { 
							canvasx = points[0].canvasx;
						} else {
							var canvasCoords = graph.eventToDomCoords(event);
							canvasx = canvasCoords[0];
						}
					}
					var ctx = graph.canvas_ctx_;
					ctx.fillStyle = '#FF0000';
					ctx.fillRect(canvasx - 0.5, 0, 1, graph.height_);
				});
				blockHighlight = false;
			},
			unhighlightCallback: function(event, x, points, row, seriesName) {
				if (blockHighlight) {
					return;
				}
				blockHighlight = true;
				$.each(graphs[containerId], function(key, val) {
					val.clearSelection();
				});
				blockHighlight = false;
			}
		};
		
		if (config.series) {
			opts.series = config.series.clone();
		}
		
		graphs[containerId][parameterName] = new Dygraph(
				div,
				data,
				opts
				);
	};

	return {
		graphs: graphs,
		urls: urls,
		isResizeListenerAttached : isResizeListenerAttached,
		showInfoMsg : showInfoMessage,
		showWarningMsg : showWarningMessage,
		clearWarningMsg : clearWarningMessage,
		showErrorMsg : showErrorMessage,
		clearErrorMsg : clearErrorMessage,
		createDataGraph: function(param, config, urlParams) {
			$('#infoMsg').empty();
			$("#errorMsg").empty();

			if (urlParams['downscale']) {
				clearWarningMessage();
				showWarningMessage('Due to the length of your request, some timesteps may be filtered from the graph. You may still download the unfiltered dataset, or shorten the date range of your request.');
			}

			urlParams["output"] = "json";
			$.ajax({
				type: 'GET',
				dataType: 'json',
				url: CONFIG.relativePath + urls[param],
				data: urlParams,
				timeout: 600000,
				success: function(data, textStatus, jqXHR) {
					if (!data || (!data.contentType && data.contentType === "text/xml")) {
						clearErrorMessage();
						showErrorMessage("An error has occurred.  Please contact <a href='mailto:" + GCMRC.administrator + "@usgs.gov'>" + GCMRC.administrator + "@usgs.gov</a>");
					} else {
						if (data.data && !data.data.ERROR && data.data.time) {
							data = {
								success : {
									"@rowCount" : "-1",
									data : [
										data.data
									]
								}
							};
						}
						//success
						if (data.success && data.success.data && $.isArray(data.success.data)) {
							var containerDiv = $("#" + config.divId);
							var labelDiv = $("#" + config.labelDivId);
							
							if (graphs[config.divId]) {
								graphs[config.divId].values(function(el) {
									el.destroy();
								});
							}
							
							/*
							 * Clean out and repopulated the container/graph divs
							 * for the correct display order
							 */
							containerDiv.empty();
							labelDiv.empty();
							GCMRC.Page.params.values().sortBy(function(n) {
								return parseFloat(n.description.displayOrder);
							}).map(function(n) {
								return n.description.groupId;
							}).forEach(function(el) {
								containerDiv.append($('<div class="p' + el + '"></div>'));
								labelDiv.append($('<div class="p' + el +'"></div>'));
							});
							
							graphs[config.divId] = {};
							
							config.graphsToMake.forEach(function(graphToMake) {
								if (graphToMake.dealWithResponse) {
									graphToMake.dealWithResponse(graphToMake, data, config, buildGraph);
								} else {
									dealWithResponse(graphToMake, data, config, buildGraph);
								}
							});
						} else if (data.data && data.data.ERROR) {
							clearErrorMessage();
							showErrorMessage("Please select a parameter to graph!");
						} else {
							LOG.error("what the heck just happened?");
						}
					}
				},
				error: function() {
					clearErrorMessage();
					showErrorMessage("A Network Error has occurred. Please check your configuration and try again.  If you repeatedly receive this message, contact <a href='mailto:" + GCMRC.administrator + "@usgs.gov'>" + GCMRC.administrator + "@usgs.gov</a>");
				}
			});
		}
	};
}(CONFIG.networkHoursOffset);