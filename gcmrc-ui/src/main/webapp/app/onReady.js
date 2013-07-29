$(document).ready(function() {
	initializeLogging();
	
	$('html.lt-ie9').each(function(){
		var msg = "The browser you are using is not supported by this application.<br>" +
				"Browsers supported by this application are:<ul>" +
				'<li><a href="https://www.google.com/intl/en/chrome/browser/">Google Chrome Browser 12 or above</a></li>' +
				'<li><a href="http://www.mozilla.org/en-US/firefox/new/">Mozilla Firefox Browser 4 or above</a></li>' +
				"</ul>Otherwise, please Upgrade to: <br><ul>" +
				'<li><a href="http://windows.microsoft.com/en-US/internet-explorer/download-ie">Internet Explorer 9 or above</a></li>' +
				'</ul>';
		
		$('#content').prepend('<div class="alert alert-error">' + msg + '</div>')
	});
})


