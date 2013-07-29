<%
    String debug = Boolean.parseBoolean(request.getParameter("debug-qualifier")) ? "base" : "base";
%>
<script src="${param['relPath']}js/closure-library/goog/<%=debug%>.js"></script>
