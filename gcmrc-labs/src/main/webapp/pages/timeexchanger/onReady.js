$(document).ready(function() {
	initializeLogging({LOG4JS_LOG_THRESHOLD: 'info'});
	$('#nextTimeButton').click(function() {
		document.location = CONFIG.relativePath + "time/" + $('#nextTime').val();
	});
})