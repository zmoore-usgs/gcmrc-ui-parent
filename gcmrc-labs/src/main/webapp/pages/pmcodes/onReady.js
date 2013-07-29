$(document).ready(function() {
	initializeLogging({LOG4JS_LOG_THRESHOLD : 'info'});
	
	
	var $content = $("#content");
	
	var table = "<table><thead><tr><th>Parameter Code</th><th>Parameter Name</th><th>Units</th></tr></thead><tbody>";
	$.each(pmArray, function(index, item) {
		
		var clazz = '';
		if (0 === index % 2) {
			clazz = ' class="alt"';
		}
		
		table += "<tr" + clazz + "><td>" + item + "</td><td>" + pmCodes[item].name + "</td><td>" + pmCodes[item].units + "</td></tr>";
		
	});
	table += "</tbody></table>";
	
	$content.html(table);
})