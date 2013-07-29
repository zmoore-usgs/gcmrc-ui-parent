<%
    String debug = Boolean.parseBoolean(request.getParameter("debug-qualifier")) ? "" : ".min";
%>

<script src="${param['relPath']}js/oboejs/oboe<%=debug%>.js"></script>