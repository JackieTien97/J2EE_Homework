package edu.nju.hellowworld.filters;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class EncodingFilter
 */
@WebFilter("/*")
public class EncodingFilter implements Filter {

    /**
     * Default constructor. 
     */
    public EncodingFilter() {
        
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = (HttpServletRequest) request;
			req.setCharacterEncoding("utf-8");
		}
		
		BufferedResponse responseWrapper = new BufferedResponse((HttpServletResponse)response);
		responseWrapper.setContentType("text/html;charset=utf-8");

		chain.doFilter(request, responseWrapper);
		
		if (responseWrapper.getOutputType() == BufferedResponse.OT_WRITER) {
			String resBody = new String(responseWrapper.toByteArray(), responseWrapper.getCharacterEncoding());
			PrintWriter writer = response.getWriter();
			writer.println(resBody);
		} else if (responseWrapper.getOutputType() == BufferedResponse.OT_OUTPUT_STREAM) {
			ServletOutputStream out = response.getOutputStream();
			out.write(responseWrapper.toByteArray());
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		
	}

}
