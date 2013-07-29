var values = [51,23,56,2,9,77,66,11,26,42,55,151,26,15,94,64,37,15,67,35,26,13,21];

var margin = {
		top: 10, 
		right: 50, 
		bottom: 20, 
		left: 50
	},
	width = 120 - margin.left - margin.right,
	height = 390 - margin.top - margin.bottom;

	var min = 2,
	max = 151;

var chart, data, vis;

$(document).ready(function() {
	initializeLogging({
		LOG4JS_LOG_THRESHOLD : 'info'
	});
	
	chart = boxChart()
	.whiskers(iqr(1.5))
	.width(width)
	.height(height);

	data = [];
	
	d3.tsv("pages/d3boxplot/acoustic.tsv", function(tsv) {
		tsv.forEach(function(x) {
			
			var params = ['SILT_CLAY_CONC_VALUE', 'SAND_CONC_VALUE'];
			
			params.forEach(function(paramName, i) {
				if (x[paramName]) {
					var paramValue = parseFloat(x[paramName]);
					if (!data[i]) data[i] = [paramValue];
					else data[i].push(paramValue);
					if (paramValue > max) max = paramValue;
					if (paramValue < min) min = paramValue;
				}
			});
		});
		
		chart.domain([min, max]);
		
		vis = d3.select("#chart").selectAll("svg")
			.data(data)
			.enter().append("svg")
			.attr("class", "box")
			.attr("width", width + margin.left + margin.right)
			.attr("height", height + margin.bottom + margin.top)
			.append("g")
			.attr("transform", "translate(" + margin.left + "," + margin.top + ")")
			.call(chart);

		chart.duration(1000);
		window.transition = function() {
			vis.datum(randomize).call(chart);
		}
		
//		d3.select("#chart")
//			.selectAll("svg")
//			.each(function(d, i) {
//				fabric.parseSVGDocument(this, function(a, b, c) {
//					LOG.info("omg look at me!");
//				});
//			});
	});
})