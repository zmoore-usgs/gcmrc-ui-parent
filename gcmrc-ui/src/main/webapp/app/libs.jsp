<jsp:include page="../js/log4javascript/log4javascript.jsp">
	<jsp:param name="relPath" value="${relativePath}" />
	<jsp:param name="debug-qualifier" value="${development}" />
	<jsp:param name="LOG4JS_LOG_THRESHOLD" value='${logLevel}' />
</jsp:include>

<jsp:include page="../js/modernizr/package.jsp">
	<jsp:param name="relPath" value="${relativePath}" />
	<jsp:param name="debug-qualifier" value="${development}" />
</jsp:include>

<script type="text/javascript" src="${relativePath}webjars/jquery/2.2.4/jquery.min.js"></script>
<script type="text/javascript" src="${relativePath}webjars/jquery-ui/1.11.4/jquery-ui.min.js"></script>

<jsp:include page="../js/bootstrap/package.jsp">
	<jsp:param name="relPath" value="${relativePath}" />
	<jsp:param name="debug-qualifier" value="${development}" />
</jsp:include>

<jsp:include page="../js/slider/package.jsp">
	<jsp:param name="relPath" value="${relativePath}" />
	<jsp:param name="debug-qualifier" value="${development}" />
</jsp:include>

<jsp:include page="../js/sugar/package.jsp">
	<jsp:param name="relPath" value="${relativePath}" />
	<jsp:param name="debug-qualifier" value="${development}" />
</jsp:include>
<script type="text/javascript">
	Object.extend();
</script>

<jsp:include page="../js/closure-library/package.jsp">
	<jsp:param name="relPath" value="${relativePath}" />
	<jsp:param name="debug-qualifier" value="${development}" />
</jsp:include>