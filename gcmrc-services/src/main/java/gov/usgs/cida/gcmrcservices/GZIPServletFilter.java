package gov.usgs.cida.gcmrcservices;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.zip.GZIPOutputStream;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GZIPServletFilter implements Filter {
    private static final Logger LOG = LoggerFactory.getLogger(GZIPServletFilter.class);
    private static final String GZIPPED = "gov.usgs.cida.gzipped";

    @Override
    public void init(FilterConfig cfg) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req,
            ServletResponse resp,
            FilterChain chain)
            throws IOException, ServletException {
        LOG.debug("Gzip filter");

        boolean supportsGzip = false;
        Enumeration e = ((HttpServletRequest) req).getHeaders("Accept-Encoding");
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            if (name.matches("(?i).*gzip.*")) {
                supportsGzip = true;
                break;
            }
        }

        if (supportsGzip && req.getAttribute(GZIPPED) == null) {
            LOG.debug("Gzip supported");
            HttpServletResponse httpResp = (HttpServletResponse) resp;
            // This does NOT work for Tomcat! 
            // Tomcat JSP does not close the output stream upon finishing, so we have to close it explicitly.
            // chain.doFilter(req, new GZIPServletResponseWrapper(httpResp));
            HttpServletResponseWrapper wrapper = new GZIPServletResponseWrapper(httpResp);
            req.setAttribute(GZIPPED, true);
            chain.doFilter(req, wrapper);
            wrapper.getOutputStream().close();
        } else {
            LOG.debug("Gzip NOT supported!");
            chain.doFilter(req, resp);
        }
    }

    class GZIPServletResponseWrapper extends HttpServletResponseWrapper {

        private GZIPOutputStream gzipStream;
        private ServletOutputStream servletOutputStream;
        private PrintWriter printWriter;

        GZIPServletResponseWrapper(HttpServletResponse resp) throws IOException {
            super(resp);
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            if (servletOutputStream == null) {
                servletOutputStream = createOutputStream();
            }
            return servletOutputStream;
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            if (printWriter == null) {
                printWriter = new PrintWriter(new OutputStreamWriter(getOutputStream(), getCharacterEncoding())) {  
                    // This is important for I18N
                    // Workaround for Tomcat bug where flush is NOT called when JSP output finished
                                
                    @Override
                    public void write(char[] cb, int off, int len) {
                        super.write(cb, off, len);
//                        super.flush();
                    }
                };
            }
            return printWriter;
        }

        private ServletOutputStream createOutputStream() throws IOException {
            ServletResponse resp = this.getResponse();
            gzipStream = new GZIPOutputStream(resp.getOutputStream());
            addHeader("Content-Encoding", "gzip");
            addHeader("Vary", "Accept-Encoding");
            return new ServletOutputStream() {
                /* The first three methods must be overwritten */

                @Override
                public void write(int b) throws IOException {
                    gzipStream.write(b);
                }

                @Override
                public void flush() throws IOException {
                    gzipStream.flush();
                }

                @Override
                public void close() throws IOException {
                    gzipStream.close();
                }

                @Override
                public void write(byte[] b) throws IOException {
                    gzipStream.write(b);
                }

                @Override
                public void write(byte[] b, int off, int len) throws IOException {
                    gzipStream.write(b, off, len);
                }
            };
        }
    }
}
