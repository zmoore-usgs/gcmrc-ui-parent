var values = [51,23,56,2,9,77,66,11,26,42,55,151,26,15,94,64,37,15,67,35,26,13,21];

$(document).ready(function() {
	initializeLogging({LOG4JS_LOG_THRESHOLD : 'info'});
	
	createBoxPlot(values, 500, 'boxdiv');
})