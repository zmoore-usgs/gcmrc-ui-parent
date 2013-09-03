<script type="text/javascript">
	var GCMRC = {
		administrator : 'cida_gcmrc'
	};
</script>
<link rel="shortcut icon" type="image/ico" href="${relativePath}app/favicon.ico" />

<script type="text/javascript" src="${relativePath}app/jslResourceLoad.js"></script>
<script type="text/javascript">
GCMRC.Stations = {
	"00000000" : {
		"hidden" : true,
		"network" : "none",
		"nwisSite" : "00000000",
		"displayName" : "ERROR",
		"lat" : 43.0731,
		"lon" : -89.4011
	},
	"08374500" : {
		"moreData" : '<li>Find data for this site in the <a href="http://www.ibwc.gov/wad/DDQTERLI.htm">IBWC historical dataset</a> or the <a href="http://www.ibwc.gov/wad/374500_a.txt">IBWC realtime dataset</a></li>'
	}
};
GCMRC.StationLoad = JSL.ResourceLoad(function(el) {
	var name = "00000000";
			
	if (el.nwisSite) {
		name = el.nwisSite;
	} else if (el.shortName) {
		name = el.shortName;
	} else {
		LOG.error("No nwisSite or shortName for station!");
	}

	GCMRC.Stations[name] = Object.merge({}, GCMRC.Stations[name], true).merge(el, true);
});
CONFIG.instColor = "#4DAF4A";
CONFIG.instHiColor = "#FF0033";
CONFIG.pumpColor = "#A6CEE3";
CONFIG.pumpHiColor = "#1F78B4";
CONFIG.sampColor = "#FF7F00";
CONFIG.sampHiColor = "#FF9900";
</script>
<script type="text/javascript" src="${relativePath}app/networkdata.js"></script>
<script type="text/javascript" src="${relativePath}app/mapping.js"></script>
<script type="text/javascript" src="${relativePath}app/orgs.js"></script>

<script type="text/javascript" src="${relativePath}app/onReady.js"></script>
