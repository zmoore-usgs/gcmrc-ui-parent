<%@page import="gov.usgs.cida.path.PathUtil"%>
<%@page import="java.io.File"%>
<%@page import="gov.usgs.cida.config.DynamicReadOnlyProperties"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%!
    private static final Logger log = LoggerFactory.getLogger("index_jsp");
    protected DynamicReadOnlyProperties props = new DynamicReadOnlyProperties();

{
        try {
            File propsFile = new File(getClass().getClassLoader().getResource("application.properties").toURI());
            props = new DynamicReadOnlyProperties(propsFile);
        } catch (Exception e) {
            log.error("Could not read application.properties. Application will not function", e);
        }
    }

    private String getProp(String key) {
        return props.getProperty(key, "");
    }

    protected boolean development = Boolean.parseBoolean(props.getProperty("all.development")) || Boolean.parseBoolean(props.getProperty("${project.artifactId}.development"));

%>
<%
        String vJquery = getProp("version.jquery");
        String vLog4JavaScript = getProp("version.log4javascript");
        String vOpenLayers = getProp("version.openlayers");
        String vModernizr = getProp("version.modernizr");
        String vBootstrap = getProp("version.bootstrap");
        String vJqueryUi = getProp("version.jqueryui");
        String vSugarJs = getProp("version.sugarjs");
        String relPath = request.getContextPath();
%>

<%-- Log4JavaScript --%>
<script type="text/javascript" src="<%= relPath %>/webjars/log4javascript/<%=vLog4JavaScript%>/log4javascript<%= development ? "_uncompressed" : ""%>.js"></script>
<script type="text/javascript">
    var LOG;
    /**
     * Initializes the log4javascript framework
     * 
     * @see http://log4javascript.org/
     * @param {type} params
     * @returns {undefined}
     */
    function initializeLogging(params) {
        if (!params) {
                params = {};
        }
        LOG = log4javascript.getLogger();
    
        var LOG4JS_PATTERN_LAYOUT = params.LOG4JS_PATTERN_LAYOUT || '' || "%rms - %-5p - %m%n";
        var LOG4JS_LOG_THRESHOLD = params.LOG4JS_LOG_THRESHOLD || 'debug' || "info"; // info will be default
    
        var appender = new log4javascript.BrowserConsoleAppender();
    
        appender.setLayout(new log4javascript.PatternLayout(LOG4JS_PATTERN_LAYOUT));
        var logLevel;
                switch (LOG4JS_LOG_THRESHOLD) {
                        case "trace" :
                                logLevel = log4javascript.Level.TRACE;
                                break;
                        case "debug" :
                                logLevel = log4javascript.Level.DEBUG;
                                break;
                        case "info" :
                                logLevel = log4javascript.Level.INFO;
                                break; 
                        case "warn" :
                                logLevel = log4javascript.Level.WARN;
                break;
            case "error" :
                logLevel = log4javascript.Level.ERROR;
                break;
            case "fatal" :
                logLevel = log4javascript.Level.FATAL;
                break;
            case "off" :
                logLevel = log4javascript.Level.OFF;
                break;
            default:
                logLevel = log4javascript.Level.INFO;
        }
        appender.setThreshold(logLevel);

        LOG.addAppender(appender);

        LOG.info('Log4javascript v.' + log4javascript.version + ' initialized.');
        LOG.info('Logging Appender Pattern Set to: ' + LOG4JS_PATTERN_LAYOUT);
        LOG.info('Logging Threshold Set To: ' + logLevel);
    }
</script>

<%-- Modernizr --%>
<script type="text/javascript" src="<%= relPath %>/webjars/modernizr/<%=vModernizr%>/modernizr<%= development ? "" : ".min"%>.js"></script>


<script type="text/javascript" src="<%= relPath %>/webjars/jquery/<%=vJquery%>/jquery<%= development ? "" : ".min"%>.js"></script>

<link rel="stylesheet" href="<%= relPath %>/webjars/bootstrap/<%=vBootstrap%>/css/bootstrap<%= development ? "" : ".min" %>.css"/>
<link rel="stylesheet" href="<%= relPath %>/webjars/bootstrap/<%=vBootstrap%>/css/bootstrap-responsive<%= development ? "" : ".min" %>.css"/>
<script type="text/javascript" src="<%= relPath %>/webjars/bootstrap/<%=vBootstrap%>/js/bootstrap<%= development ? "" : ".min"%>.js"></script>

<link rel="stylesheet" href="<%= relPath %>/webjars/jquery-ui/<%=vJqueryUi%>/themes/base/<%= development ? "/" : "minified/"%>jquery-ui<%= development ? "" : ".min"%>.css"/>
<script type="text/javascript" src="<%= relPath %>/webjars/jquery-ui/<%=vJqueryUi%>/ui/<%= development ? "/" : "minified/"%>jquery-ui<%= development ? "" : ".min"%>.js"></script>

<script type="text/javascript" src="<%= relPath %>/webjars/sugar/<%=vSugarJs%>/sugar-full<%= development ? "" : ".min"%>.js"></script>

<script type="text/javascript">
	Object.extend();
</script>

<jsp:include page="../js/closure-library/package.jsp">
	<jsp:param name="relPath" value="${relativePath}" />
	<jsp:param name="debug-qualifier" value="${development}" />
</jsp:include>

<script type="text/javascript" src="<%= relPath %>/webjars/openlayers/<%=vOpenLayers%>/OpenLayers<%= development ? "" : ".debug"%>.js"></script>