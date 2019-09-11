<%@page import="gov.usgs.cida.gcmrc.util.PropertiesLoader"%>
<%@page import="gov.usgs.cida.gcmrc.util.PathUtil"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="java.util.Map"%>
<%@page import="javax.naming.Context"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%!private static final Logger log = LoggerFactory.getLogger("networkreachview_jsp");
	protected PropertiesLoader propertiesLoader = new PropertiesLoader();
	protected Context context = propertiesLoader.getContextProps();
	
	boolean development = Boolean.parseBoolean(propertiesLoader.getProp(context, "all.development")) || Boolean.parseBoolean(propertiesLoader.getProp(context, "${project.artifactId}.development"));
	protected String warningMessage = propertiesLoader.getProp(context, "gcmrc.site.warning.message", "");%>

<%
	request.setAttribute("development", development);
	request.setAttribute("logLevel", development?"debug":"info");
	
	String pageName = page.getClass().getSimpleName().substring(0, page.getClass().getSimpleName().indexOf("_jsp"));
	request.setAttribute("pageName", pageName);

	String relativePath = PathUtil.calculateRelativePath(request.getRequestURI(), request.getContextPath());
	request.setAttribute("relativePath", relativePath);
	
	String basePath = "reaches";
	Map<String, String> restOfPath = PathUtil.calculateRestOfURI(request.getRequestURI(), basePath, "networkName");
	String networkName = restOfPath.get("networkName");
	if (StringUtils.isEmpty(networkName)) {
		networkName = "Unknown";
	}
	request.setAttribute("networkName", networkName);
%>

<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html lang="en" class="no-js"> <!--<![endif]-->
    <head>
        <jsp:include page="template/GCMRCHead.jsp">
			<jsp:param name="relativePath" value="${relativePath}" />
			<jsp:param name="shortName" value="GCMRC" />
			<jsp:param name="title" value="" />
			<jsp:param name="description" value="" />
			<jsp:param name="author" value="" />
			<jsp:param name="keywords" value="" />
			<jsp:param name="publisher" value="" />
			<jsp:param name="revisedDate" value="" />
			<jsp:param name="nextReview" value="" />
			<jsp:param name="expires" value="never" />
			<jsp:param name="development" value="${development}" />
		</jsp:include>
		<jsp:include page="app/libs.jsp">
                    <jsp:param name="relativePath" value="${relativePath}" />
                </jsp:include>
                <script src="${relativePath}app/CanvasOverride.js" type="text/javascript"></script>
		<script src="${relativePath}app/DeclusterCanvas.js" type="text/javascript"></script>
		<script type="text/javascript">
			var CONFIG = {};
			
			CONFIG.development = ${development};
			CONFIG.relativePath = '${relativePath}';
			CONFIG.networkName = '${networkName}';
		</script>

		<jsp:include page="app/gcmrc.jsp"></jsp:include>
		<jsp:include page="pages/page.jsp">
			<jsp:param name="relativePath" value="${relativePath}" />
			<jsp:param name="pageName" value="${pageName}" />
		</jsp:include>
                <jsp:include page="js/dygraphs/dygraphs.jsp">
			<jsp:param name="relPath" value="${relativePath}" />
			<jsp:param name="debug-qualifier" value="${development}" />
		</jsp:include>
		<script src="${relativePath}services/rest/reach/${networkName}?jsonp_callback=GCMRC.Page.reachLoad"></script>
    </head>
    <body>
		<div class="container-fluid">
			<jsp:include page="template/GCMRCHeader.jsp">
				<jsp:param name="relativePath" value="${relativePath}" />
				<jsp:param name="header-class" value="" />
			</jsp:include>
			<!--[if lt IE 7]>
            <p class="chromeframe">You are using an outdated browser. <a href="http://browsehappy.com/">Upgrade your browser today</a> or <a href="http://www.google.com/chromeframe/?redirect=true">install Google Chrome Frame</a> to better experience this site.</p>
			<![endif]-->
			<div class="application-body">
				<h2>Grand Canyon Monitoring and Research Center</h2>
				<h3><span class="network-name"></span> Reaches</h3>
				<% if (!warningMessage.isEmpty()) { %>
					<div class="alert alert-danger site-alert"><b>NOTICE:&nbsp;</b><%= warningMessage%></div>
				<% } %>
				<div id="breadcrumbs">
					<span>
						<span><a href="https://www.gcmrc.gov/gcmrc.aspx">Home</a></span>
						<span> &gt; </span>
						<span><a href="${relativePath}index.jsp">Discharge, Sediment and Water Quality</a></span>
						<span> &gt; </span>
						<span><span class="network-name"></span> Reaches</span>
					</span>
				</div>
				<div id="content" class="row-fluid">
					<div class="span8">
						<div class="well">
							<div id="openlayers-map"></div>
						</div>
					</div>
					<div class="span4">
						<div class="sectionWideTitle">Reaches</div>
						<div id="reachList" class="well">
						</div>
					</div>
				</div>
			</div>
			<jsp:include page="template/GCMRCFooter.jsp">
				<jsp:param name="header-class" value="" />
				<jsp:param name="contact-info" value="" />
			</jsp:include>
		</div>
    </body>
</html>
