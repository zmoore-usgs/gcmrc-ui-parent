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
private static final Logger log = LoggerFactory.getLogger("timeexchanger.jsp");
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
	
	String timeStr = PathUtil.calculateRestOfURI(request.getRequestURI(), "time", "timeMillis").get("timeMillis");
	Long timeLong = null;
	if (null != timeStr) {
		try {
			timeLong = Long.parseLong(timeStr);
		} catch (Exception e) {
			log.debug("invalid time: " + timeStr);
		}
	} else {
		timeStr = "";
	}
	
	DateTime time = null;
	if (null != timeLong) {
		time = new DateTime(timeLong, DateTimeZone.UTC);
	}
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
		<script type="text/javascript">
			var CONFIG = {};
			
			CONFIG.development = <%= development %>;
			CONFIG.relativePath = '<%= relativePath %>';
		</script>
		
		<link type="text/css" rel="stylesheet" href="${relativePath}pages/timeexchanger/page.css" />
		<script type="text/javascript" src="${relativePath}pages/timeexchanger/onReady.js"></script>
    </head>
    <body>
		<jsp:include page="template/USGSHeader.jsp">
			<jsp:param name="relPath" value="${relativePath}" />
			<jsp:param name="header-class" value="" />
			<jsp:param name="site-title" value="${project.name} - ${project.version}" />
		</jsp:include>
		<div class="application-body">
			<h1>Hello World!</h1>
			<div><h2>Time Request: <%=timeLong%></h2></div>
			<div>
				<% if (null != time) { %>
				<div><h3>Look at what that equates to!</h3></div>
				<ul>
					<li><strong>UTC: <%=time.withZone(DateTimeZone.UTC) %></strong></li>
					<li>Server default: <%=time.withZone(DateTimeZone.getDefault()) %></li>
					<li>Madison Standard: <%=time.withZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone("CST"))) %></li>
					<li><strong>AZ -7: <%=time.withZone(DateTimeZone.forOffsetHours(-7)) %></strong></li>
				</ul>
				<% } %>
			</div>
			<div>
				Milliseconds:<input type="text" id="nextTime" value="<%=timeStr%>"><button id="nextTimeButton">Go</button>
			</div>
		</div>
		<jsp:include page="template/USGSFooter.jsp">
			<jsp:param name="relPath" value="${relativePath}" />
			<jsp:param name="header-class" value="" />
			<jsp:param name="site-url" value="" />
			<jsp:param name="contact-info" value="" />
		</jsp:include>
    </body>
</html>
