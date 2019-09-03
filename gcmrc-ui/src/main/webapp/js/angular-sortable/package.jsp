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
        String vAngularJs = getProp("version.angularjs");
        String vAngularUiSortable = getProp("version.angular_ui_sortable");
        String vAngularUiBootstrap = getProp("version.angular_ui_bootstrap");
        String relPath = request.getContextPath();
%>
<script type="text/javascript" src="<%= relPath %>/webjars/angularjs/<%=vAngularJs%>/angular<%= development ? "" : ".min"%>.js"></script>
<script type="text/javascript" src="<%= relPath %>/webjars/angular-ui-sortable/<%=vAngularUiSortable%>/sortable<%= development ? "" : ".min"%>.js"></script>
<script type="text/javascript" src="<%= relPath %>/webjars/angular-ui-bootstrap/<%=vAngularUiBootstrap%>/ui-bootstrap-tpls<%= development ? "" : ".min"%>.js"></script>