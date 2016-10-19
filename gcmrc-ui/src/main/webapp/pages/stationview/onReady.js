$(document).ready(function() {
	$("span.network-name").html(GCMRC.Networks[CONFIG.networkName].displayName);
	var data = {};
	var gcmrcStation = GCMRC.Stations[CONFIG.stationName];
	var isNwisSite = !CONFIG.stationName.has(/[A-z]/);
	data['site'] = CONFIG.stationName;

	$('#loading').hide();
	$('#parameterLoading').hide();
	
	$('#station-title').html(gcmrcStation.displayName);
	
	GCMRC.Page.createDateList($('#lastSedDates'), GCMRC.Page.reach);
	GCMRC.Page.addParameters();
	GCMRC.Page.addCredits();
	GCMRC.Page.addPubs();
	if (isNwisSite) {
		var additionalText = "<li>Find data for this site in <a href='http://waterdata.usgs.gov/nwis/inventory?agency_code=USGS&site_no=" + CONFIG.stationName + "'>NWIS</a></li>";
		if (gcmrcStation.moreData) {
			additionalText += gcmrcStation.moreData;
		}
		$('#addlSourcesBody').append(additionalText);
	}
	GCMRC.Page.createMiniMap({stationName: CONFIG.stationName});

	$('#buildGraph').click(GCMRC.Page.buildGraphClicked);
	
	$('input[name^="toggle-curve-"]').live('change', GCMRC.Page.toggleDurationCurve);
	$('input[name^="toggle-scale-"]').live('change', GCMRC.Page.toggleDurationCurveScale);

	$('#bedSedimentDownloadButton').click(GCMRC.Page.downloadBedSedimentClicked);
	$('#physicalDownloadButton').click(GCMRC.Page.downloadSamplesClicked);
	$('#stupidlyBigDownloadButton').click(GCMRC.Page.downloadDataClicked);
	$('#downloadData').click(GCMRC.Page.downloadPopupClicked);
	
	if (!GCMRC.Page.earliestPosition) GCMRC.Page.earliestPosition = new Date().addMonths(-1).getTime();
	if (!GCMRC.Page.latestPosition) GCMRC.Page.latestPosition = new Date().getTime();
	
	$('#beginDatePicker').attr('value', new Date(GCMRC.Page.latestPosition).utc().addMonths(-1).format('{yyyy}-{MM}-{dd}'));
	$('#beginDatePicker').datepicker({
		format: 'yyyy-mm-dd',
		startDate: new Date(GCMRC.Page.earliestPosition),
		endDate: new Date(GCMRC.Page.latestPosition),
		startView: 0
	});
	$('#endDatePicker').attr('value', new Date(GCMRC.Page.latestPosition).utc().format('{yyyy}-{MM}-{dd}'));
	$('#endDatePicker').datepicker({
		format: 'yyyy-mm-dd',
		startDate: new Date(GCMRC.Page.earliestPosition),
		endDate: new Date(GCMRC.Page.latestPosition),
		startView: 0
	});
	
//	$('#downloadColumnOrdering').sortable();

	$('#loading').ajaxStart(function() {
		$(this).show();
		$('.buildButton').addClass("disabled");
	}).ajaxStop(function() {
		$(this).hide();
		$('.buildButton').removeClass("disabled");
	});
});