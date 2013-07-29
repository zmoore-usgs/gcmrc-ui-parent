<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
<meta charset="utf-8"/>
<title>${param['shortName']}</title>
<meta name="title" content="${param['title']}">
<meta name="description" content="${param['description']}">
<meta name="author" content="${param['author']}">
<meta name="keywords" content="${param['keywords']}">
<meta name="publisher" content="${param['publisher']}">
<meta name="country" content="USA">
<meta name="language" content="EN">
<meta name="revised" content="${param['revisedDate']}">
<meta name="review" content="${param['nextReview']}">
<%--<meta name="expires" content="${param['expires']}">--%>

<link type="text/css" media="screen" rel="stylesheet" href="http://www.gcmrc.gov/styles/common.css" title="default"/>
<link type="text/css" media="screen" rel="stylesheet" href="http://www.gcmrc.gov/styles/custom.css" title="default"/>
<link rel="alternate stylesheet" media="screen" type="text/css" href="http://www.gcmrc.gov/styles/none.css" title="no_style" />
<link rel="stylesheet" media="print" type="text/css" href="http://www.gcmrc.gov/styles/print.css" />
<%-- fix for macs --%>
<style type="text/css">
	#usgstitle p {
		padding : 4px;
	}
</style>
<script type="text/javascript" src="http://www.gcmrc.gov/scripts/styleswitch.js"></script>
<script type="text/javascript" src="http://www.gcmrc.gov/scripts/external.js"></script>

<% 
    String gaAccountCode = request.getParameter("google-analytics-account-code");
    String[] gaCommandList = request.getParameterValues("google-analytics-command-set");
    Boolean development = Boolean.parseBoolean(request.getParameter("development"));
    
    if (gaAccountCode != null && !gaAccountCode.trim().isEmpty()) { 
%>
<!-- Google Analytics Setup -->
<script type="text/javascript">
    var _gaq = _gaq || [];
    _gaq.push(['_setAccount', '<%= gaAccountCode %>']);
    _gaq.push (['_gat._anonymizeIp']);
    _gaq.push(['_trackPageview']);
    <% 
    if (gaCommandList != null && gaCommandList.length > 0) { 
        for (int commandIdx = 0;commandIdx < gaCommandList.length;commandIdx++) {
    %> 
        _gaq.push([<%= gaCommandList[commandIdx] %>]);
    <%
        }
    } 
    %>

        (function() {
            var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
            ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
            var scripts = document.getElementsByTagName('script');
            var s = scripts[scripts.length-1]; s.parentNode.insertBefore(ga, s);
        })();

</script>
<% } %>

<%-- https://insight.usgs.gov/web_reengineering/SitePages/Analytics_Instructions.aspx --%>
<%-- https://insight.usgs.gov/web_reengineering/SitePages/Analytics_FAQs.aspx --%>
<% if (!development) { %>
<script type="application/javascript" src="http://www.usgs.gov/scripts/analytics/usgs-analytics.js"></script>
<% } %>