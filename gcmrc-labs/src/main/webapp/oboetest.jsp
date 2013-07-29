<%@page import="java.util.TimeZone"%>
<%@page import="org.joda.time.DateTimeZone"%>
<%@page import="gov.usgs.cida.path.PathUtil"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@page import="gov.usgs.cida.config.DynamicReadOnlyProperties"%>
<%@page import="org.joda.time.DateTime"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%!
private static final Logger log = LoggerFactory.getLogger("oboetest.jsp");
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

<%
	log.debug(request.getRequestURI() + " " + request.getContextPath());
	String relativePath = PathUtil.calculateRelativePath(request.getRequestURI(), request.getContextPath());
	request.setAttribute("relativePath", relativePath);
%>
<html>
    <head>
        <jsp:include page="template/USGSHead.jsp">
			<jsp:param name="relPath" value="${relativePath}" />
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
		
		<jsp:include page="js/log4javascript/log4javascript.jsp">
			<jsp:param name="relPath" value="${relativePath}" />
		</jsp:include>
		<jsp:include page="js/jquery/jquery.jsp">
			<jsp:param name="relPath" value="${relativePath}" />
			<jsp:param name="debug-qualifier" value="<%= development %>" />
		</jsp:include>
		<jsp:include page="js/oboejs/package.jsp">
			<jsp:param name="relPath" value="${relativePath}" />
			<jsp:param name="debug-qualifier" value="<%= development %>" />
		</jsp:include>
		<jsp:include page="js/dygraphs/dygraphs.jsp">
			<jsp:param name="relPath" value="${relativePath}" />
			<jsp:param name="debug-qualifier" value="<%= development %>" />
		</jsp:include>
		<jsp:include page="js/sugar/package.jsp">
			<jsp:param name="relPath" value="${relativePath}" />
			<jsp:param name="debug-qualifier" value="<%= development %>" />
		</jsp:include>
		<script type="text/javascript">
			Object.extend();
		</script>
		
		<jsp:include page="js/d3/package.jsp">
			<jsp:param name="relPath" value="${relativePath}" />
			<jsp:param name="debug-qualifier" value="<%= development %>" />
		</jsp:include>
		
		<script type="text/javascript">
			var CONFIG = {};
			
			CONFIG.development = <%= development %>;
			CONFIG.relativePath = '<%= relativePath %>';
			CONFIG.networkHoursOffset = 7;
			CONFIG.stationName = "09402000";
		</script>

		<script type="text/javascript">
			var GCMRC = {};
			GCMRC.Dygraphs = {};
		</script>
		<script src="${relativePath}ui/app/graphing/overrides.js"></script>
		<script src="${relativePath}ui/app/graphing/dotPlotter.js"></script>
		<script src="${relativePath}ui/app/graphing/scaledTicker.js"></script>
		<script src="${relativePath}ui/app/graphing/timeFormatter.js"></script>
		<script src="${relativePath}ui/app/graphing/dataFormatter.js"></script>
		<script src="${relativePath}ui/app/graphing/graphing.js"></script>
		
		<link type="text/css" rel="stylesheet" href="${relativePath}pages/oboetest/page.css" />
		<script type="text/javascript" src="${relativePath}pages/oboetest/onReady.js"></script>
    </head>
    <body>
		<jsp:include page="template/USGSHeader.jsp">
			<jsp:param name="relPath" value="${relativePath}" />
			<jsp:param name="header-class" value="" />
			<jsp:param name="site-title" value="${project.name} - ${project.version}" />
		</jsp:include>
		<div class="application-body">
			<h1>Hello World!</h1>
			<div><h2>Oboe Test</h2></div>
			<p>
				<a href="https://github.com/jimhigson/oboe.js">Oboe</a> is a library
				that helps you use streaming JSON as it comes in.  Here we've combined it with
				<a href="http://d3js.org/">D3</a> to build a really crappy graph.
				Enjoy.
			</p>
			<input name="beginPosition" value="2002-08-01">
			<input name="endPosition" value="2002-08-30">
			<button id="buildGraph">Build Graph</button>
			<button id="startMe">[oboe] Start Data</button>
			<button id="d3count">[oboe] Build graph with D3</button>
			<div id="data-dygraph"></div>
			<div id="putStuffHere">
			</div>
			<div id="data-d3count"></div>
		</div>
		<jsp:include page="template/USGSFooter.jsp">
			<jsp:param name="relPath" value="${relativePath}" />
			<jsp:param name="header-class" value="" />
			<jsp:param name="site-url" value="" />
			<jsp:param name="contact-info" value="" />
		</jsp:include>
    </body>
</html>
