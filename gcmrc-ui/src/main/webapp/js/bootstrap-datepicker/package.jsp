<%@page import="gov.usgs.cida.gcmrc.util.PathUtil"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@page import="javax.naming.Context"%>
<%@page import="java.util.Properties"%>
<%@page import="gov.usgs.cida.gcmrc.util.PropertiesLoader"%>
<%!
	private static final Logger log = LoggerFactory.getLogger("package_jsp");
	protected PropertiesLoader propertiesLoader = new PropertiesLoader();
	protected Properties properties = propertiesLoader.getProperties();
	protected Context context = propertiesLoader.getContextProps();

	protected boolean development = Boolean.parseBoolean(propertiesLoader.getProp(context, "all.development")) || Boolean.parseBoolean(propertiesLoader.getProp(context, "${project.artifactId}.development"));
%>
<%
    String vBootstrapDatepicker = propertiesLoader.getProp(properties, "version.bootstrap_datepicker");
    String relPath = request.getContextPath();
%>
<link rel="stylesheet" href="<%= relPath %>/webjars/bootstrap-datepicker/<%=vBootstrapDatepicker%>/css/bootstrap-datepicker<%= development ? "" : ".min"%>.css"/>
<script src="<%= relPath %>/webjars/bootstrap-datepicker/<%=vBootstrapDatepicker%>/js/bootstrap-datepicker<%= development ? "" : ".min"%>.js"></script>