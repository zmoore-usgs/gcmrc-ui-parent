<%
    String debug = Boolean.parseBoolean(request.getParameter("debug-qualifier")) ? "" : ".min";
%>
<!-- Pulled from https://github.com/eternicode/bootstrap-datepicker -->
<link rel="stylesheet" href="${param['relPath']}js/slider/css/overcast/jquery-ui-1.10.3.custom<%=debug%>.css"/>
<script src="${param['relPath']}js/slider/js/jquery-ui-1.10.3.custom<%=debug%>.js"></script>