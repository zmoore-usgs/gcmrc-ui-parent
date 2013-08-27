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
			
	if (el.siteName) {
		name = el.siteName;
	}

	GCMRC.Stations[name] = Object.merge({}, GCMRC.Stations[name], true).merge(el, true);
});
</script>
<script type="text/javascript" src="${relativePath}app/networkdata.js"></script>
<script type="text/javascript" src="${relativePath}app/mapping.js"></script>
<script type="text/javascript" src="${relativePath}app/orgs.js"></script>

<script type="text/javascript" src="${relativePath}app/onReady.js"></script>
