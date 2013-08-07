<%@page import="gov.usgs.cida.path.PathUtil"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@page import="gov.usgs.cida.config.DynamicReadOnlyProperties"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%!
private static final Logger log = LoggerFactory.getLogger("index.jsp");
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
	String relativePath = PathUtil.calculateRelativePath(request.getRequestURI(), request.getContextPath());
	request.setAttribute("relativePath", relativePath);
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
		<link type="text/css" rel="stylesheet" href="pages/index/index.css" />
		
		<jsp:include page="js/log4javascript/log4javascript.jsp">
			<jsp:param name="relPath" value="" />
		</jsp:include>
		<jsp:include page="js/jquery/jquery.jsp">
			<jsp:param name="relPath" value="" />
			<jsp:param name="debug-qualifier" value="<%= development %>" />
		</jsp:include>
		<jsp:include page="js/bootstrap/package.jsp">
			<jsp:param name="relPath" value="" />
			<jsp:param name="debug-qualifier" value="<%= development %>" />
		</jsp:include>
		<script type="text/javascript">
			var CONFIG = {};
			
			CONFIG.development = <%= development %>;
		</script>
		
		<script type="text/javascript" src="pages/index/onReady.js"></script>
    </head>
    <body>
		<jsp:include page="template/USGSHeader.jsp">
			<jsp:param name="relPath" value="" />
			<jsp:param name="header-class" value="" />
			<jsp:param name="site-title" value="${project.name} - ${project.version}" />
		</jsp:include>
		<div class="application-body">
			<h1>Hello World!</h1>
			<ul>
				<li><a href="${relativePath}pmcodes.jsp">Parameter Codes</a></li>
				<li><a href="${relativePath}boxplot.jsp">Box Plots</a></li>
				<li><a href="${relativePath}d3boxplot.jsp">d3 Box Plots</a></li>
				<li><a href="${relativePath}time">Time Exchanger</a></li>
				<li><a href="${relativePath}odt">OPeNDAP Time Exchanger</a></li>
				<li><a href="${relativePath}oboetest.jsp">Oboe Test</a></li>
				<li><a href="${relativePath}openid/id">OpenID URL Test</a></li>
			</ul>
		</div>
		<jsp:include page="template/USGSFooter.jsp">
			<jsp:param name="relPath" value="" />
			<jsp:param name="header-class" value="" />
			<jsp:param name="site-url" value="" />
			<jsp:param name="contact-info" value="" />
		</jsp:include>
    </body>
</html>
