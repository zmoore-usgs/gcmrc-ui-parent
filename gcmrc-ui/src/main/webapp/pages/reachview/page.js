GCMRC.Page = {
	createMiniMap: function(config) {
		if (!config)
			config = {};

		var upstreamStationName = config['upstreamStationName'];
		var downstreamStationName = config['downstreamStationName'];
		var divId = config['divId'] || 'openlayers-map';

		var options = {};
		GCMRC.Mapping.maps[divId] = new OpenLayers.Map(divId, options);

		var layersToAdd = [];
		layersToAdd.push(GCMRC.Mapping.layers.esri.esriTopo);
		layersToAdd.push(GCMRC.Mapping.layers.flowlines["zone" + GCMRC.Page.reach.displayOrder]);
		layersToAdd.push(GCMRC.Mapping.layers.markers);

		GCMRC.Mapping.maps[divId].addLayers(layersToAdd);
		var makeIcon = function() {
			var size = new OpenLayers.Size(21, 25);
			var offset = new OpenLayers.Pixel(-(size.w / 2), -size.h);
			var icon = new OpenLayers.Icon(CONFIG.relativePath + 'app/marker.png', size, offset);

			return icon;
		};

		if (GCMRC.Stations[upstreamStationName]) {
			GCMRC.Mapping.layers.markers.addMarker(
					new OpenLayers.Marker(new OpenLayers.LonLat(GCMRC.Stations[upstreamStationName].lon, GCMRC.Stations[upstreamStationName].lat).transform(
					new OpenLayers.Projection("EPSG:4326"),
					GCMRC.Mapping.maps[divId].getProjectionObject()
					), makeIcon()));
		}

		if (GCMRC.Stations[downstreamStationName]) {
			GCMRC.Mapping.layers.markers.addMarker(new OpenLayers.Marker(
					new OpenLayers.LonLat(GCMRC.Stations[downstreamStationName].lon, GCMRC.Stations[downstreamStationName].lat).transform(
					new OpenLayers.Projection("EPSG:4326"),
					GCMRC.Mapping.maps[divId].getProjectionObject()
					), makeIcon()));
		}

		GCMRC.Mapping.maps[divId].zoomToExtent(GCMRC.Mapping.layers.markers.getDataExtent());
	},
	buildPORView : function(div, earliest, latest) {
		var result = [
			'<div>',
			'Records exist from ',
			'<a href="#" class="fromdate">',
			earliest.split("T")[0],
			'</a>',
			' through ',
			'<a href="#" class="todate">',
			latest.split("T")[0],
			'</a>',
			'</div>'
		];
		div.append(result.join(''));
	},
	buildSliderInfo : function(div, dateStr, multiplier) {
		div.append('<div>Uncertainty for Major Tributary Sand Loads <span class="c_qual' + multiplier + '"></span>% after ' + dateStr + '</div>');
	},
	buildGraphClicked: function() {
		var begin = $("input[name='beginPosition']").val();
		var end = $("input[name='endPosition']").val();
		
		var endStaticRecMillis = new Date(GCMRC.Page.reach.endStaticRec).getTime() + (CONFIG.networkHoursOffset * 60 * 60 * 1000);
		var newestSuspSedMillis = new Date(GCMRC.Page.reach.newestSuspSed).getTime() + (CONFIG.networkHoursOffset * 60 * 60 * 1000);
		
		var beginMillis = Date.create(begin).getUTCTime() + (CONFIG.networkHoursOffset * 60 * 60 * 1000);
		var endMillis = Date.create(end).getUTCTime() + (CONFIG.networkHoursOffset * 60 * 60 * 1000);
		if (endMillis >= beginMillis) {
			var expectedGraphColumns = GCMRC.Page.getExpectedGraphColumns()
			var chosenParameters = [];
			expectedGraphColumns.forEach(function(el) {
				[].push.apply(this, el.columns.map(function(col) {
					return col.split("-")[0];
				}).unique());
			}, chosenParameters);

			var expectedStations = [];
			if (CONFIG.upstreamStationName) {
				expectedStations.push(CONFIG.upstreamStationName);
			}
			if (CONFIG.downstreamStationName) {
				expectedStations.push(CONFIG.downstreamStationName);
			}
			if (GCMRC.Page.reach.majorTrib) {
				expectedStations.push(GCMRC.Page.reach.majorTrib);
			}
			if (GCMRC.Page.reach.minorTrib) {
				expectedStations.push(GCMRC.Page.reach.minorTrib);
			}
			if (GCMRC.Page.reach.downstreamDischargeStation) {
				expectedStations.push(GCMRC.Page.reach.downstreamDischargeStation);
			}

			var serviceOptions = {
				station: expectedStations,
				beginPosition: begin,
				endPosition: end,
				column: chosenParameters,
				tz: '-' + CONFIG.networkHoursOffset,
				cutoffBefore: GCMRC.Page.earliestPositionISO,
				cutoffAfter: GCMRC.Page.latestPositionISO,
				timeFormat: 'UTCMillis',
				every: 'P1D',
				noDataFilter: 'true',
				useLagged: 'true'
			};

			var aggTime = GCMRC.Page.checkIfAgg(serviceOptions);

			if (aggTime) {
				serviceOptions['downscale'] = aggTime;
			}
			
			var thingthing = $('div.c_qual');
			thingthing.empty();
			if ((endStaticRecMillis && endStaticRecMillis < endMillis) && 
					(!newestSuspSedMillis || (endStaticRecMillis < newestSuspSedMillis && newestSuspSedMillis > beginMillis))) {
				GCMRC.Page.buildSliderInfo(thingthing, GCMRC.Page.reach.endStaticRec.split("T")[0], 2);
				$('.c_qual2').html(parseFloat($('span[name=c_val]').html()) * 2);
			}
			if (newestSuspSedMillis && newestSuspSedMillis < endMillis) {
				GCMRC.Page.buildSliderInfo(thingthing, GCMRC.Page.reach.newestSuspSed.split("T")[0], 4);
				$('.c_qual4').html(parseFloat($('span[name=c_val]').html()) * 4);
			}
			
			GCMRC.Graphing.createDataGraph(
					'agg',
					{
						divId: 'data-dygraph',
						labelDivId: 'legend-dygraph',
						graphsToMake: expectedGraphColumns,
						endStaticRec : endStaticRecMillis,
						newestSuspSed : newestSuspSedMillis,
						dateWindow : [beginMillis, endMillis]
					},
			serviceOptions);
		} else {
			GCMRC.Graphing.clearErrorMsg();
			GCMRC.Graphing.showErrorMsg("Please choose an End that is after Start");
		}
	},
	fromDateClicked: function() {
		$('#beginDatePicker').val($(this).html());
	},
	toDateClicked: function() {
		var date = $(this).html();
		if ("Now" === date) {
			date = new Date();

			var formatted = {
				d: date.getUTCDate(),
				m: date.getUTCMonth() + 1,
				yyyy: date.getUTCFullYear()
			};

			formatted.dd = (formatted.d < 10 ? '0' : '') + formatted.d;
			formatted.mm = (formatted.m < 10 ? '0' : '') + formatted.m;

			date = [formatted['yyyy'], formatted['mm'], formatted['dd']].join('-');
		}
		$('#endDatePicker').val(date);
	},
	addCreditToList: function(e) {
		var org = GCMRC.Organizations[e];

		if (org) {
			var listing = [
				'<li><div><a href="',
				org.url,
				'"><img src="',
				org.logo.url,
				'" height="',
				org.logo.height,
				'px" width="',
				org.logo.width,
				'px">',
				org.displayName,
				'</a></div></li>'
			];

			$('#addlSourcesBody').append(listing.join(''));
		} else {
			LOG.info("Unknown Organizational Name, or no Additional Info associated with '" + e + "'");
		}
	},
	addCredits: function() {
		$('#addlSourcesBody').append('<b>Data provided by:</b>');
		GCMRC.Page.credits.forEach(GCMRC.Page.addCreditToList);
	},
	checkIfAgg: function(config) {
		var result = null;

		var conf = config || {};
		var begin = new Date(conf['beginPosition']).getTime();
		var end = new Date(conf['endPosition']).getTime();
		var cols = conf['column'] || [];

		var month = 1/*month*/ * 31/*days*/ * 24/*hours*/ * 60/*minutes*/ * 60/*seconds*/ * 1000/*millis*/;
		var year = 12 * month;
		var maxDifference = 6 * month;
		var minDifference = 2 * month;
		var interval = 2 * month;

		var diff = maxDifference;
		var i = (cols.length) - 1;
		diff = diff - (interval * i);
		if (diff < minDifference) {
			diff = minDifference;
		}

		var realDifference = end - begin;
		if (realDifference > diff) {
			if (realDifference > year) {
				result = 'PT24H';
			} else if (realDifference > maxDifference) {
				result = 'PT12H';
			} else {
				result = 'PT6H';
			}
		}

		return result;
	},
	sandworker: new Worker(CONFIG.relativePath + "app/graphing/matrixSandWorker.js"),
	isSandWorkerFed: false,
	finesworker: new Worker(CONFIG.relativePath + "app/graphing/matrixFinesWorker.js"),
	isFinesWorkerFed: false,
	latestSandReqId: 1,
	latestFinesReqId: 1,
	updateSandSummary: function(config) {
		if (config) {
			var upper = config.upper;
			var mid = config.mid;
			var lower = config.lower;

			var result = [
				'<div>',
				"<h3>Change in Sand Mass</h3>",
				"<ul><li>Zero Bias Value: <strong>",
				new Number(mid.toPrecision(2)).format(0, ",", "."),
				" Metric Tons</strong></li><li>Upper Uncertainty Bound: ",
				new Number(upper.toPrecision(2)).format(0, ",", "."),
				" Metric Tons</li><li>Lower Uncertainty Bound: ",
				new Number(lower.toPrecision(2)).format(0, ",", "."),
				" Metric Tons</li></ul>",
				"</div>"
			];

			var summaryDiv = $("#reach-sand-summary");
			summaryDiv.parent().addClass("well");
			summaryDiv.empty();
			summaryDiv.append(result.join(""));
		}
	},
	updateFinesSummary: function(config) {
		if (config) {
			var upper = config.upper;
			var mid = config.mid;
			var lower = config.lower;

			var result = [
				'<div>',
				"<h3>Change in Silt and Clay Mass</h3>",
				"<ul><li>Zero Bias Value: <strong>",
				new Number(mid.toPrecision(2)).format(0, ",", "."),
				" Metric Tons</strong></li><li>Upper Uncertainty Bound: ",
				new Number(upper.toPrecision(2)).format(0, ",", "."),
				" Metric Tons</li><li>Lower Uncertainty Bound: ",
				new Number(lower.toPrecision(2)).format(0, ",", "."),
				" Metric Tons</li></ul>",
				"</div>"
			];

			var summaryDiv = $("#reach-fines-summary");
			summaryDiv.parent().addClass("well");
			summaryDiv.empty();
			summaryDiv.append(result.join(""));
		}
	},
	drawBudget: function(config) {
		if (GCMRC.Page.isSandWorkerFed) {
			var msg = config.clone();
			msg.messageType = "transformArray";
			msg.reqId = ++GCMRC.Page.latestSandReqId;
			delete msg.graphsToMake;

			GCMRC.Page.sandworker.postMessage(msg);
		}
		if (GCMRC.Page.isFinesWorkerFed) {
			var msg = config.clone();
			msg.messageType = "transformArray";
			msg.reqId = ++GCMRC.Page.latestFinesReqId;
			delete msg.graphsToMake;

			GCMRC.Page.finesworker.postMessage(msg);
		}
	},
	getExpectedGraphColumns: function() {
		var minorTribHACK = "Minor Trib ";
		var sandBudgetColumns = [
			"inst!100400!" + GCMRC.Page.params["100400"]["inst"].tsGroup + "-" + CONFIG.upstreamStationName,
			"inst!100400!" + GCMRC.Page.params["100400"]["inst"].tsGroup + "-" + CONFIG.downstreamStationName
		];
		var finesBudgetColumns = [
			"inst!100600!" + GCMRC.Page.params["100600"]["inst"].tsGroup + "-" + CONFIG.upstreamStationName,
			"inst!100600!" + GCMRC.Page.params["100600"]["inst"].tsGroup + "-" + CONFIG.downstreamStationName
		];
		if (GCMRC.Page.reach.majorTrib) {
			sandBudgetColumns.push(  //OMG MAJOR HACK. ughhh.
					"inst!100401!" + GCMRC.Page.params["100400"]["inst"].tsGroup.substr(2) + "-" + GCMRC.Page.reach.majorTrib
					);
			finesBudgetColumns.push(
					"inst!100600!" + GCMRC.Page.params["100600"]["inst"].tsGroup + "-" + GCMRC.Page.reach.majorTrib
					);
		}
		if (GCMRC.Page.reach.minorTrib) {
			sandBudgetColumns.push(
					"inst!100400!" + minorTribHACK + GCMRC.Page.params["100400"]["inst"].tsGroup + "-" + GCMRC.Page.reach.minorTrib
					);
			finesBudgetColumns.push(
					"inst!100600!" + minorTribHACK + GCMRC.Page.params["100600"]["inst"].tsGroup + "-" + GCMRC.Page.reach.minorTrib
					);
		}


		var result = [
			{
				pCode: "00060",
				columns: ["inst!00060!" + "Discharge" + "-" + GCMRC.Page.reach.downstreamDischargeStation], //DESPICABLE HACK!
				yAxisLabel: "Discharge (cfs) at " + GCMRC.Page.reach.downstreamDischargeName
			}
		];
		if (GCMRC.Page.reach.pcode.some("100400")) {
			result.push({
				pCode: "sandbudget",
				columns: sandBudgetColumns,
				yAxisLabel: "Change in Sand Stored in Reach (Metric Tons)",
				dealWithResponse: function(graphToMake, data, config, buildGraph) {
					var datas = [];
					var times = [];

					var getValue = function(row, colName) {
						var result = 0.0;
						if (row[colName]) {
							result = parseFloat(row[colName]);
						}
						return result;
					}

					data.success.data.each(function(el) {
						datas.push([getValue(el, "inst!100400!" + GCMRC.Page.params["100400"]["inst"].tsGroup + "-" + GCMRC.Page.reach.upstreamStation),
							getValue(el, "inst!100401!" + GCMRC.Page.params["100400"]["inst"].tsGroup.substr(2) + "-" + GCMRC.Page.reach.majorTrib),
							getValue(el, "inst!100400!" + minorTribHACK + GCMRC.Page.params["100400"]["inst"].tsGroup + "-" + GCMRC.Page.reach.minorTrib),
							getValue(el, "inst!100400!" + GCMRC.Page.params["100400"]["inst"].tsGroup + "-" + GCMRC.Page.reach.downstreamStation)]);
						times.push(getValue(el, "time"));
					});

					GCMRC.Page.sandworker.postMessage({
						messageType: "setDataArray",
						divId: config.divId,
						data: datas,
						time: times,
						endStaticRec: config.endStaticRec,
						newestSuspSed: config.newestSuspSed
					});

					config.a = parseFloat($('span[name=a_val]').html()) / 100.0;
					config.b = parseFloat($('span[name=b_val]').html()) / 100.0;
					config.c = parseFloat($('span[name=c_val]').html()) / 100.0;
					config.d = parseFloat($('span[name=d_val]').html()) / 100.0;
					config.e = parseFloat($('span[name=e_val]').html()) / 100.0;
					config.f = parseFloat($('span[name=f_val]').html()) / 100.0;
					config.g = parseFloat($('span[name=g_val]').html()) / 100.0;

					var createCallback = function(response) {
						var conf = config.clone();
						conf.data = response.data.dataArray;
						conf['yAxisLabel'] = graphToMake.yAxisLabel || GCMRC.Page.params[graphToMake.pCode].inst['displayName'] + " (" + GCMRC.Page.params[graphToMake.pCode].inst['unitsShort'] + ")";
						conf["labels"] = ["Time", "Sand Storage Change", "High", "Low"];
						conf['dataformatter'] = GCMRC.Dygraphs.DataFormatter(GCMRC.Page.params[graphToMake.pCode].inst['decimalPlaces']);
						conf['decimalPlaces'] = GCMRC.Page.params[graphToMake.pCode].inst['decimalPlaces'];
						conf["parameterName"] = graphToMake.pCode;
						conf["div"] = $('#' + conf.divId + ' div.p' + graphToMake.pCode).get(0);
						conf["labelDiv"] = $('#' + conf.labelDivId + ' div.p' + graphToMake.pCode).get(0);
						conf["colors"] = ["#006666", "#006666", "#006666"];
						conf["highlightColor"] = {
							"Sand Storage Change": "#FF0033", //TODO HACK. Can't hardcode this! Rio will have both sand and fines.
							"High": "#FF0033",
							"Low": "#FF0033"
						};
						conf["series"] = {
							"High": {
								strokeWidth: 0.0,
								drawPoints: false,
								highlightCircleSize: 3
							},
							"Low": {
								strokeWidth: 0.0,
								drawPoints: false,
								highlightCircleSize: 3
							}
						};
						buildGraph(conf);

						//TODO Get these out of the hardcode
						GCMRC.Graphing.graphs["data-dygraph"].sandbudget.setAnnotations([{
								x: new Date(GCMRC.Page.reach.endStaticRec).getUTCTime() + (CONFIG.networkHoursOffset * 60 * 60 * 1000), //TODO  This isn't coming out right.
								shortText:'Incomplete Verification', 
								series:'Sand Storage Change',
								attachAtBottom: true,
								mouseOverHandler : function(annotation) {
									annotation.div.innerHTML = 'Verification dataset incomplete after this date';
								},
								mouseOutHandler : function(annotation) {
									annotation.div.innerHTML = 'Incomplete verification';
								}
							}]);
						GCMRC.Graphing.graphs["data-dygraph"].sandbudget.setAnnotations([{
								x: new Date(GCMRC.Page.reach.newestSuspSed).getUTCTime() + (CONFIG.networkHoursOffset * 60 * 60 * 1000), //TODO  This isn't coming out right.
								shortText:'End Verification', 
								series:'Sand Storage Change',
								attachAtBottom: true,
								width : 97,
								mouseOverHandler : function(annotation) {
									annotation.div.innerHTML = 'No verification data processed after this date';
								},
								mouseOutHandler : function(annotation) {
									annotation.div.innerHTML = "End verification";
								}
							}]);
					};

					GCMRC.Page.sandworker.onmessage = function(response) {
						if (response.data && GCMRC.Page.latestSandReqId === response.data.reqId) {
							if (GCMRC.Graphing.graphs[config.divId]["sandbudget"]) {
								LOG.trace("update");
								GCMRC.Graphing.graphs[config.divId]["sandbudget"].updateOptions({"file": response.data.dataArray});
							} else {
								LOG.trace("create");
								createCallback(response);
							}
							GCMRC.Page.updateSandSummary(response.data.config);
						}
					};
					GCMRC.Page.isSandWorkerFed = true;

					GCMRC.Page.drawBudget(config);
				}
			});
		}
		if (GCMRC.Page.reach.pcode.some("100600")) {
			result.push({
				pCode: "finesbudget",
				columns: finesBudgetColumns,
				yAxisLabel: "Change in Silt and Clay Stored in Reach (Metric Tons)",
				dealWithResponse: function(graphToMake, data, config, buildGraph) {
					var datas = [];
					var times = [];

					var getValue = function(row, colName) {
						var result = 0.0;
						if (row[colName]) {
							result = parseFloat(row[colName]);
						}
						return result;
					}

					data.success.data.each(function(el) {
						datas.push([getValue(el, "inst!100600!" + GCMRC.Page.params["100600"]["inst"].tsGroup + "-" + GCMRC.Page.reach.upstreamStation),
							getValue(el, "inst!100600!" + GCMRC.Page.params["100600"]["inst"].tsGroup + "-" + GCMRC.Page.reach.majorTrib),
							getValue(el, "inst!100600!" + minorTribHACK + GCMRC.Page.params["100600"]["inst"].tsGroup + "-" + GCMRC.Page.reach.minorTrib),
							getValue(el, "inst!100600!" + GCMRC.Page.params["100600"]["inst"].tsGroup + "-" + GCMRC.Page.reach.downstreamStation)]);
						times.push(getValue(el, "time"));
					});

					GCMRC.Page.finesworker.postMessage({
						messageType: "setDataArray",
						divId: config.divId,
						data: datas,
						time: times,
						endStaticRec: config.endStaticRec,
						newestSuspSed: config.newestSuspSed
					});

					config.a = parseFloat($('span[name=a_val]').html()) / 100.0;
					config.b = parseFloat($('span[name=b_val]').html()) / 100.0;
					config.c = parseFloat($('span[name=c_val]').html()) / 100.0;
					config.d = parseFloat($('span[name=d_val]').html()) / 100.0;
					config.e = parseFloat($('span[name=e_val]').html()) / 100.0;
					config.f = parseFloat($('span[name=f_val]').html()) / 100.0;
					config.g = parseFloat($('span[name=g_val]').html()) / 100.0;

					var createCallback = function(response) {
						var conf = config.clone();
						conf.data = response.data.dataArray;
						conf['yAxisLabel'] = graphToMake.yAxisLabel || GCMRC.Page.params[graphToMake.pCode].inst['displayName'] + " (" + GCMRC.Page.params[graphToMake.pCode].inst['unitsShort'] + ")";
						conf["labels"] = ["Time", "Silt and Clay Storage Change", "High", "Low"];
						conf['dataformatter'] = GCMRC.Dygraphs.DataFormatter(GCMRC.Page.params[graphToMake.pCode].inst['decimalPlaces']);
						conf['decimalPlaces'] = GCMRC.Page.params[graphToMake.pCode].inst['decimalPlaces'];
						conf["parameterName"] = graphToMake.pCode;
						conf["div"] = $('#' + conf.divId + ' div.p' + graphToMake.pCode).get(0);
						conf["labelDiv"] = $('#' + conf.labelDivId + ' div.p' + graphToMake.pCode).get(0);
						conf["colors"] = ["#006666", "#006666", "#006666"];
						conf["highlightColor"] = {
							"Silt and Clay Storage Change": "#FF0033", /////HACK
							"High": "#FF0033",
							"Low": "#FF0033"
						};
						conf["series"] = {
							"High": {
								strokeWidth: 0.0,
								drawPoints: false,
								highlightCircleSize: 3
							},
							"Low": {
								strokeWidth: 0.0,
								drawPoints: false,
								highlightCircleSize: 3
							}
						};
						buildGraph(conf);

						//TODO Get these out of the hardcode
						GCMRC.Graphing.graphs["data-dygraph"].finesbudget.setAnnotations([{
								x: new Date(GCMRC.Page.reach.endStaticRec).getUTCTime() + (CONFIG.networkHoursOffset * 60 * 60 * 1000), //TODO  This isn't coming out right.
								shortText:'Incomplete Verification', 
								series:'Silt and Clay Storage Change',
								attachAtBottom: true,
								mouseOverHandler : function(annotation) {
									annotation.div.innerHTML = 'Verification dataset incomplete after this date';
								},
								mouseOutHandler : function(annotation) {
									annotation.div.innerHTML = 'Incomplete verification';
								}
							}]);
						GCMRC.Graphing.graphs["data-dygraph"].finesbudget.setAnnotations([{
								x: new Date(GCMRC.Page.reach.newestSuspSed).getUTCTime() + (CONFIG.networkHoursOffset * 60 * 60 * 1000), //TODO  This isn't coming out right.
								shortText:'End Verification', 
								series:'Silt and Clay Storage Change',
								attachAtBottom: true,
								width : 97,
								mouseOverHandler : function(annotation) {
									annotation.div.innerHTML = 'No verification data processed after this date';
								},
								mouseOutHandler : function(annotation) {
									annotation.div.innerHTML = "End verification";
								}
							}]);
					};

					GCMRC.Page.finesworker.onmessage = function(response) {
						if (response.data && GCMRC.Page.latestFinesReqId === response.data.reqId) {
							if (GCMRC.Graphing.graphs[config.divId]["finesbudget"]) {
								LOG.trace("update");
								GCMRC.Graphing.graphs[config.divId]["finesbudget"].updateOptions({"file": response.data.dataArray});
							} else {
								LOG.trace("create");
								createCallback(response);
							}
							GCMRC.Page.updateFinesSummary(response.data.config);
						}
					};
					GCMRC.Page.isFinesWorkerFed = true;

					GCMRC.Page.drawBudget(config);
				}
			});
		}
		return result;
	},
	createDateList: function(container, dates) {
		var result = [];

		if (dates && dates.endStaticRec && dates.newestSuspSed) {
			var endStatic = dates.endStaticRec;
			var newest = dates.newestSuspSed;
			var endStaticSplit = dates.endStaticRec.split('T');
			var newestSplit = dates.newestSuspSed.split('T');
			if (1 < endStaticSplit.length && 1 < newestSplit.length) {
				endStatic = endStaticSplit[0];
				newest = newestSplit[0];
			}

			var majorTribName = "Major Tributary";
			if (GCMRC.Page.reach.majorTribRiver) {
				majorTribName = GCMRC.Page.reach.majorTribRiver;
			}

			[].push.apply(result, [
				'<div class="innerRightSidebar">',
				'<div class="sectionTitle">Data Status</div>',
				'<div><ul>',
				'<li>',
				"End of complete lab-processed suspended-sediment record for " + majorTribName + ": ",
				'<a href="#" class="todate">',
				endStatic,
				'</a>',
				'</li>',
				'<li>',
				"Most recent suspended-sediment sample from " + majorTribName + " used in calculations: ",
				'<a href="#" class="todate">',
				newest,
				'</a>',
				'</li>',
				'</ul></div>',
				'</div>']);
		}

		container.append(result.join(""));
	},
	createParameterList: function(container, params) {
		var html = [];

		var addParamToList = function(param) {
			var listing = ['<div class="parameterListing">',
				'<h5>',
				param["displayName"],
				'</h5>',
				'<ul class="unstyled"><li>',
				'<div class="pull-left">',
				param["adjustMin"],
				'%</div><div class="pull-right">',
				param["adjustMax"],
				'%</div>',
				'<div name="',
				param["name"],
				'"></div>',
				'</li>',
				'<li><div style="text-align:center;"><span name="',
				param["name"],
				'_val"></span>%</div></li>',
				'<li>',
				'<div class="',
				param["name"],
				'_qual"></div>',
				'</li>',
				'</ul>',
				'</div>'];
			html.push(listing.join(''));
		};

		params.forEach(addParamToList);

		container.html(html.join('\n'));

		var slidechange = function(paramName) {
			return function(event, ui) {
				$('span[name=' + paramName + '_val]').html(ui.value);
				var a = parseFloat($('span[name=a_val]').html()) / 100.0;
				var b = parseFloat($('span[name=b_val]').html()) / 100.0;
				var c = parseFloat($('span[name=c_val]').html()) / 100.0;
				var d = parseFloat($('span[name=d_val]').html()) / 100.0;
				var e = parseFloat($('span[name=e_val]').html()) / 100.0;
				var f = parseFloat($('span[name=f_val]').html()) / 100.0;
				var g = parseFloat($('span[name=g_val]').html()) / 100.0;

				GCMRC.Page.drawBudget({
					divId: 'data-dygraph',
					labelDivId: 'legend-dygraph',
					a: a,
					b: b,
					c: c,
					d: d,
					e: e,
					f: f,
					g: g
				});
			};
		};
		var slidequal = function(changeThis, multiplier) {
			return function(event, ui) {
				$(changeThis).html(ui.value * multiplier);
			};
		};
		params.forEach(function(param) {
			var slider = $('div[name=' + param["name"] + ']').slider({
				min: param['adjustMin'],
				max: param['adjustMax'],
				value: param["adjustDefault"],
				slide: slidechange(param["name"]),
				change: slidechange(param["name"])
			});
			
			slider.on("slidechange", slidequal('span.' + param["name"] + '_qual2', 2));
			slider.on("slide", slidequal('span.' + param["name"] + '_qual2', 2));
			slider.on("slidechange", slidequal('span.' + param["name"] + '_qual4', 4));
			slider.on("slide", slidequal('span.' + param["name"] + '_qual4', 4));
			
			var defaultLocation = $('div[name=' + param["name"] + '] a').attr("style");
			$('div[name=' + param["name"] + ']').append('<a class="ui-corner-all ui-slider-default-position" style="' + defaultLocation + '" href="#"></a>');
			$('span[name=' + param["name"] + '_val]').html(param["adjustDefault"]);
		});

		$('.fromdate').click(GCMRC.Page.fromDateClicked);
		$('.todate').click(GCMRC.Page.toDateClicked);
	},
	earliestPosition: null,
	earliestPositionISO: null,
	latestPosition: null,
	latestPositionISO: null,
	reachPORLoad : JSL.ResourceLoad(function(el) {
		if (el.majorTribBeginPosition) {
			var majorBeginPosition = new Date(el.majorTribBeginPosition).getTime();
			if (!GCMRC.Page.earliestPosition || majorBeginPosition > GCMRC.Page.earliestPosition) { //LATEST EARLIEST POSITION!
				GCMRC.Page.earliestPosition = majorBeginPosition;
				GCMRC.Page.earliestPositionISO = el.majorTribBeginPosition;
			}
		}
		if (el.minorTribBeginPosition) {
			var minorBeginPosition = new Date(el.minorTribBeginPosition).getTime();
			if (!GCMRC.Page.earliestPosition || minorBeginPosition > GCMRC.Page.earliestPosition) { //LATEST EARLIEST POSITION!
				GCMRC.Page.earliestPosition = minorBeginPosition;
				GCMRC.Page.earliestPositionISO = el.minorTribBeginPosition;
			}
		}
		
		if (el.majorTribEndPosition) {
			var majorEndPosition = new Date(el.majorTribEndPosition).getTime();
			if (!GCMRC.Page.latestPosition || majorEndPosition < GCMRC.Page.latestPosition) { ///EARLIEST LATEST POSITION!
				GCMRC.Page.latestPosition = majorEndPosition;
				GCMRC.Page.latestPositionISO = el.majorTribEndPosition;
			}
		}
		if (el.minorTribEndPosition) {
			var minorEndPosition = new Date(el.minorTribEndPosition).getTime();
			if (!GCMRC.Page.latestPosition || minorEndPosition < GCMRC.Page.latestPosition) { ///EARLIEST LATEST POSITION!
				GCMRC.Page.latestPosition = minorEndPosition;
				GCMRC.Page.latestPositionISO = el.minorTribEndPosition;
			}
		}
	}),
	params: {},
	paramsLoad: JSL.ResourceLoad(function(el) {
		if (!GCMRC.Page.params[el.pCode]) {
			GCMRC.Page.params[el.pCode] = {
				description : {
					pCode : el.pCode,
					displayName : el.displayName,
					units : el.units,
					unitsShort : el.unitsShort
				}
			};
		}
		el.sampleMethod = 'inst';
		el.color = "#006666";
		el.highlightColor = "#FF0033";
		GCMRC.Page.params[el.pCode][el.sampleMethod] = el;

		if ('100400' === el.pCode) {
			var thisBeginPosition = new Date(el.beginPosition).getTime();
			if (!GCMRC.Page.earliestPosition || thisBeginPosition > GCMRC.Page.earliestPosition) { //LATEST EARLIEST POSITION!
				GCMRC.Page.earliestPosition = thisBeginPosition;
				GCMRC.Page.earliestPositionISO = el.beginPosition;
			}

			var thisEndPosition = new Date(el.endPosition).getTime();
			if (!GCMRC.Page.latestPosition || thisEndPosition < GCMRC.Page.latestPosition) { ///EARLIEST LATEST POSITION!
				GCMRC.Page.latestPosition = thisEndPosition;
				GCMRC.Page.latestPositionISO = el.endPosition;
			}
		}
	}, function(data) {
		if (!GCMRC.Page.earliestPosition)
			GCMRC.Page.earliestPosition = new Date().addMonths(-1).getTime();
		if (!GCMRC.Page.latestPosition)
			GCMRC.Page.latestPosition = new Date().getTime();

		if ("BIBE" === CONFIG.networkName) { //HORRIFIC HACK!
			GCMRC.Page.params["00060"] = {
				"inst" : {
					"pCode":"00060",
					"tsGroup":"Discharge",
					"beginPosition":"2007-10-01",
					"endPosition":"2014-04-17",
					"nwisSite":"08375300",
					"shortName":"Rio Grande Village",
					"displayOrder":"200",
					"displayName":"Discharge",
					"units":"cubic feet per second",
					"unitsShort":"cfs",
					"decimalPlaces":"0",
					"sampleMethod":"inst",
					"color":"#006666",
					"highlightColor":"#FF0033"
				}
			}
		} else if ("09404200" === CONFIG.upstreamStationName) {
			GCMRC.Page.params["00060"] = {
				"inst" : {
					"pCode":"00060",
					"tsGroup":"Discharge",
					"beginPosition":"1983-10-01",
					"endPosition":"2014-04-17",
					"nwisSite":"09404200",
					"shortName":"Above Diamond",
					"displayOrder":"200",
					"displayName":"Discharge",
					"units":"cubic feet per second",
					"unitsShort":"cfs",
					"decimalPlaces":"0",
					"sampleMethod":"inst",
					"color":"#006666",
					"highlightColor":"#FF0033"
				}
			};
		}
		GCMRC.Page.params["finesbudget"] = {
			inst: {
				decimalPlaces: "0",
				displayName: "Silt and Clay Storage Change",
				displayOrder: "10",
				endPosition: GCMRC.Page.latestPositionISO.split("T")[0],
				pCode: "finesbudget",
				sampleMethod: "inst",
				beginPosition: GCMRC.Page.earliestPositionISO.split("T")[0],
				units: "Metric Tons",
				unitsShort: "Metric Tons",
				color: "#006666",
				highlightColor: "#FF0033"
			}
		};
		GCMRC.Page.params["sandbudget"] = {
			inst: {
				decimalPlaces: "0",
				displayName: "Sand Storage Change",
				displayOrder: "20",
				endPosition: GCMRC.Page.latestPositionISO.split("T")[0],
				pCode: "sandbudget",
				sampleMethod: "inst",
				beginPosition: GCMRC.Page.earliestPositionISO.split("T")[0],
				units: "Metric Tons",
				unitsShort: "Metric Tons",
				color: "#006666",
				highlightColor: "#FF0033"
			}
		};
	}),
	credits: ["USGSGCMR"],
	creditLoad: JSL.ResourceLoad(function(el) {
		if (GCMRC.Page.credits.none(el.orgCode)) {
			GCMRC.Page.credits.push(el.orgCode);
		} else {
			// bah, no log at this point LOG.info("Credits already contain " + el.orgCode);
		}
	}),
	reach: {},
	reachLoad: JSL.ResourceLoad(function(el) {
		GCMRC.Page.reach = el;
	}),
	sliderConfig: {
		bedLoad: {
			name: "a",
			displayName: "Bedload Coefficient for River Sand Loads",
			adjustMin: 0,
			adjustMax: 10,
			adjustDefault: 5
		},
		riverFinesLoad: {
			name: "e",
			displayName: "Magnitude of Possible Persistent Bias in Measured River Silt and Clay Loads",
			adjustMin: 0,
			adjustMax: 25,
			adjustDefault: ("BIBE" === CONFIG.networkName)?10:5
		},
		riverLoad: {
			name: "b",
			displayName: "Magnitude of Possible Persistent Bias in Measured River Sand Loads",
			adjustMin: 0,
			adjustMax: 25,
			adjustDefault: ("BIBE" === CONFIG.networkName)?10:5
		},
		majorTribFinesLoad: {
			name: "f",
			displayName: "Magnitude of Possible Persistent Bias in Measured Major Tributary Silt and Clay Loads",
			adjustMin: 0,
			adjustMax: 25,
			adjustDefault: 10
		},
		majorTribLoad: {
			name: "c",
			displayName: "Magnitude of Possible Persistent Bias in Measured Major Tributary Sand Loads",
			adjustMin: 0,
			adjustMax: 25,
			adjustDefault: 10
		},
		minorTribFinesLoad: {
			name: "g",
			displayName: "Magnitude of Possible Persistent Bias in Lesser Tributary Silt and Clay Loads",
			adjustMin: 0,
			adjustMax: 50,
			adjustDefault: 50
		},
		minorTribLoad: {
			name: "d",
			displayName: "Magnitude of Possible Persistent Bias in Lesser Tributary Sand Loads",
			adjustMin: 0,
			adjustMax: 50,
			adjustDefault: 50
		}
	},
	resetSliders: function() {
		GCMRC.Page.sliderConfig.values(function(el) {
			$('div[name=' + el.name + ']').slider("option", "value", el.adjustDefault);
		});
	}
};