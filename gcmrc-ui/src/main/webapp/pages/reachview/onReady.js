$(document).ready(function() {
	$("span.network-name").html(GCMRC.Networks[CONFIG.networkName].displayName);
	var upstreamStation = GCMRC.Stations[CONFIG.upstreamStationName];
	var downstreamStation = GCMRC.Stations[CONFIG.downstreamStationName];

	$('#station-title').html(GCMRC.Page.reach.reachName);

	$('#loading').hide();
	$('#parameterLoading').hide();

	GCMRC.Page.addCredits();

	GCMRC.Page.createMiniMap({
		upstreamStationName: (!upstreamStation) ? null : upstreamStation.nwisSite || upstreamStation.shortName,
		downstreamStationName: (!downstreamStation) ? null : downstreamStation.nwisSite || downstreamStation.shortName
	});

	if ("BIBE" === CONFIG.networkName) {
		var budgetMsg = "These sediment budgets do not currently include the "+ 
				"sediment supplied by tributaries between the above Castolon and "+
				"above Rio Grande Village stations.  Therefore, the complete "+
				"sediment budgets are more positive than those shown except "+
				"during periods of no tributary activity.";
		GCMRC.Graphing.showInfoMsg("#specialMsg", budgetMsg);
	}
	
	GCMRC.Page.buildPORView($('#porContainer'), GCMRC.Page.earliestPositionISO, GCMRC.Page.latestPositionISO);

	var bedLoadList = [GCMRC.Page.sliderConfig.bedLoad];
	
	GCMRC.Page.createParameterList($('#bedLoadList'), bedLoadList);

	var paramList = [
		GCMRC.Page.sliderConfig.riverLoad
	];
	if (GCMRC.Page.reach.pcode.some("100600")) {
		paramList.push(GCMRC.Page.sliderConfig.riverFinesLoad);
	}
	if (GCMRC.Page.reach.majorTrib) {
		if (GCMRC.Page.reach.pcode.some("100600")) {
			paramList.push(GCMRC.Page.sliderConfig.majorTribFinesLoad);
		}
		paramList.push(GCMRC.Page.sliderConfig.majorTribLoad);
	}
	if (GCMRC.Page.reach.minorTrib) {
		if (GCMRC.Page.reach.pcode.some("100600")) {
			paramList.push(GCMRC.Page.sliderConfig.minorTribFinesLoad);
		}
		paramList.push(GCMRC.Page.sliderConfig.minorTribLoad);
	}
	GCMRC.Page.createDateList($('#lastSedDates'), GCMRC.Page.reach);
	GCMRC.Page.createParameterList($('#parameterList'), paramList);
	
	$('#buildGraph').click(GCMRC.Page.buildGraphClicked);
	$('#downloadData').click(GCMRC.Page.downloadDataClicked);

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

	$('#resetDefaults').click(GCMRC.Page.resetSliders);

	$('#loading').ajaxStart(function() {
		$(this).show();
	}).ajaxStop(function() {
		$(this).hide();
	});
});
