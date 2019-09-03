<%@page import="gov.usgs.cida.path.PathUtil"%>
<%@page import="java.io.File"%>
<%@page import="java.io.FileReader" %>
<%@page import="java.util.Properties"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%!
    private static final Logger log = LoggerFactory.getLogger("package_jsp");
	protected Properties props = new Properties();
	{
		try {
			File propsFile = new File(getClass().getClassLoader().getResource("application.properties").toURI());
			props.load(new FileReader(propsFile));
		} catch (Exception e) {
			log.error("Could not read application.properties. Application will not function", e);
		}
	}

    private String getProp(String key) {
        return props.getProperty(key, "");
    }

    protected boolean development = Boolean.parseBoolean(props.getProperty("all.development")) || Boolean.parseBoolean(props.getProperty("${project.artifactId}.development"));

%>
<%
    String vPro4JS = getProp("version.proj4js");
    String relPath = request.getContextPath();
%>
<script src="<%= relPath%>/webjars/proj4js/<%= vPro4JS%>/proj4.js" type="text/javascript"></script>
<script type="text/javascript">
    proj4.defs["EPSG:26949"] = "+proj=tmerc +lat_0=31 +lon_0=-111.9166666666667 +k=0.9999 +x_0=213360 +y_0=0 +ellps=GRS80 +datum=NAD83 +units=m +no_defs";
</script>