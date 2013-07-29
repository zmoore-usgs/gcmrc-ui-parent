var buildGraphClicked = function() {
	var begin = $("input[name='beginPosition']").val();
	var end = $("input[name='endPosition']").val();

	var beginMillis = Date.create(begin).getUTCTime() + (CONFIG.networkHoursOffset * 60 * 60 * 1000);
	var endMillis = Date.create(end).getUTCTime() + (CONFIG.networkHoursOffset * 60 * 60 * 1000);
	if (endMillis >= beginMillis) {
		var expectedGraphColumns = GCMRC.Page.getExpectedGraphColumns();
		if (GCMRC.Page.hasData(expectedGraphColumns.map(function(el) {return el.pCode;}), begin, end)) {
			var chosenParameters = [];
			expectedGraphColumns.forEach(function(el) {
				[].push.apply(this, el.columns.map(function(col) {
					return col.split("-")[0];
				}));
			}, chosenParameters);

			var serviceOptions = {
				station: [CONFIG.stationName],
				beginPosition: begin,
				endPosition: end,
				column: chosenParameters,
				tz: '-' + CONFIG.networkHoursOffset,
				timeFormat: 'UTCMillis',
				every: CONFIG.everyPeriod,
				noDataFilter: 'true',
				useLagged: 'true'
			};

			var aggTime = GCMRC.Page.checkIfAgg(serviceOptions);

			if (aggTime) {
				serviceOptions['downscale'] = aggTime;
			}

			GCMRC.Graphing.createDataGraph(
					'agg',
					{
						divId: 'data-dygraph',
						labelDivId: 'legend-dygraph',
						graphsToMake : expectedGraphColumns,
						dateWindow : [beginMillis, endMillis]
					},
			serviceOptions);
		} else {
			if (0 < expectedGraphColumns.length) {
				GCMRC.Graphing.clearErrorMsg();
				GCMRC.Graphing.showErrorMsg("Please choose a date range that overlaps the selected period of records.<br>Start Date is inclusive, End Date is exclusive.");
			} else {
				GCMRC.Graphing.clearErrorMsg();
				GCMRC.Graphing.showErrorMsg("Please select a parameter to graph!");
			}
		}
	} else {
		GCMRC.Graphing.clearErrorMsg();
		GCMRC.Graphing.showErrorMsg("Please choose an End that is after Start");
	}
};

var startMeClicked = function() {
	var putItHere = $("#putStuffHere");
	putItHere.append("<div>Started!</div>");

	oboe.doGet(CONFIG.relativePath + "services/agg/?station%5B%5D=09402000&beginPosition=2002-08-01&endPosition=2002-08-30&column%5B%5D=inst!00060!Discharge&tz=-7&timeFormat=UTCMillis&every=P1D&noDataFilter=true&useLagged=true&output=json",
	function(wholeResp) {
		putItHere.append("<div>Done!</div>");
	}).onFind("!.success.data.*", function(el) {
		putItHere.append("<div>" + JSON.stringify(el) + "</div>");
	});
};

GCMRC.Page = {};
GCMRC.Page.getExpectedGraphColumns = function() {
	return [{"pCode":"00060","columns":["inst!00060!Discharge-09402000"]}];
};
GCMRC.Page.hasData = function() {
	return true;
};
GCMRC.Page.checkIfAgg = function() {
	return false;
};
GCMRC.Page.params = {
	"00060" : {"inst":{"pCode":"00060","tsGroup":"Discharge","beginPosition":"1947-06-01T00:00:00","endPosition":"2013-04-17T10:45:00","nwisSite":"09402000","shortName":"LCR nr Cameron","displayOrder":"200","displayName":"Discharge","units":"cubic feet per second","unitsShort":"cfs","decimalPlaces":"0","sampleMethod":"inst","color":"#006666","highlightColor":"#FF0033"}}
}

GCMRC.incoming = [];
var d3countClicked = function() {
	var width = 960;
	var height = 500;
	var svg = d3.select("#data-d3count").append("svg")
			.attr("width", width).attr("height", height)
		.append("g")
//			.attr("transform", "translate(32," + (height / 2) + ")");
	
	var update = (function(data) {
		var text = svg.selectAll("circle")
			.data(data);
	
		text.attr("class", "update");
		
		text.enter().append("circle")
			.attr("class", "enter")
			.attr("cx", function(d, i){
				return i / 3; 
			})
			.attr("cy", function(d, i){
				return (height - (d / 2));
			})
			.attr("r", function(d, i){return 1;});
		
		text.text(function(d) {return d;});
		
		text.exit().remove();
	}).lazy(1000, 1);
	
	update(GCMRC.incoming);
	
	oboe.doGet(CONFIG.relativePath + "services/agg/?station%5B%5D=09402000&beginPosition=2002-08-01&endPosition=2002-08-30&column%5B%5D=inst!00060!Discharge&tz=-7&timeFormat=UTCMillis&every=P1D&noDataFilter=true&useLagged=true&output=json").onFind("!.success.data.*", function(el) {
		GCMRC.incoming.push(el["inst!00060!Discharge-09402000"]);
		update(GCMRC.incoming);
	});
};

$(document).ready(function() {
	initializeLogging({LOG4JS_LOG_THRESHOLD: 'info'});
	
	$("#buildGraph").click(buildGraphClicked);
	$('#startMe').click(startMeClicked);
	$("#d3count").click(d3countClicked);
	
})