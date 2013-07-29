<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@page import="gov.usgs.cida.config.DynamicReadOnlyProperties"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%!
private static final Logger log = LoggerFactory.getLogger("pmcodes.jsp");
protected DynamicReadOnlyProperties props = new DynamicReadOnlyProperties();
{
	try {
		props = props.addJNDIContexts(new String[0]);
	} catch (Exception e) {
		log.error("Could not find JNDI");
	}
}
boolean development = Boolean.parseBoolean(props.getProperty("all.development")) || Boolean.parseBoolean(props.getProperty("${project.artifactId}.development"));
%>

<html>
    <head>
        <jsp:include page="template/USGSHead.jsp">
			<jsp:param name="relPath" value="" />
			<jsp:param name="shortName" value="${project.name}" />
			<jsp:param name="title" value="" />
			<jsp:param name="description" value="" />
			<jsp:param name="author" value="" />
			<jsp:param name="keywords" value="" />
			<jsp:param name="publisher" value="" />
			<jsp:param name="revisedDate" value="" />
			<jsp:param name="nextReview" value="" />
			<jsp:param name="expires" value="never" />
		</jsp:include>
		
		<link type="text/css" rel="stylesheet" href="pages/pmcodes/table.css" />
		
		<jsp:include page="js/log4javascript/log4javascript.jsp">
			<jsp:param name="relPath" value="" />
		</jsp:include>
		<jsp:include page="js/jquery/jquery.jsp">
			<jsp:param name="relPath" value="" />
			<jsp:param name="debug-qualifier" value="<%= development %>" />
		</jsp:include>
		<jsp:include page="js/jsplumb/jsplumb.jsp">
			<jsp:param name="relPath" value="" />
			<jsp:param name="debug-qualifier" value="<%= development %>" />
		</jsp:include>
		<jsp:include page="js/prettyprintjs/prettyprintjs.jsp">
			<jsp:param name="relPath" value="" />
		</jsp:include>
		<script type="text/javascript">
			var CONFIG = {};
			
			CONFIG.development = <%= development %>;
		</script>
		
		<script type="text/javascript" src="pages/pmcodes/pmcodes.js"></script>
		<script type="text/javascript" src="pages/pmcodes/09402500pm.js"></script>
		
		<script type="text/javascript" src="pages/pmcodes/onReady.js"></script>
    </head>
    <body>
		<jsp:include page="template/USGSHeader.jsp">
			<jsp:param name="relPath" value="" />
			<jsp:param name="header-class" value="" />
			<jsp:param name="site-title" value="${project.name}: Parameter Codes - ${project.version}" />
		</jsp:include>
		<div class="application-body">
			<h1>NWIS Parameter Codes!</h1>
			<div id="content"></div>
		</div>
		<jsp:include page="template/USGSFooter.jsp">
			<jsp:param name="relPath" value="" />
			<jsp:param name="header-class" value="" />
			<jsp:param name="site-url" value="" />
			<jsp:param name="contact-info" value="" />
		</jsp:include>
    </body>
</html>
