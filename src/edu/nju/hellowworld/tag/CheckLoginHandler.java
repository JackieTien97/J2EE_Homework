package edu.nju.hellowworld.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class CheckLoginHandler extends BodyTagSupport {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1547645880017454545L;

	@Override
	public int doEndTag() throws JspException {
		HttpSession session = pageContext.getSession();
		if (session == null || (Boolean)session.getAttribute("guest")) {
			HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			try {
				response.sendRedirect(request.getContextPath() + "/Login");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return SKIP_PAGE;
		}
		return EVAL_PAGE;
	}

	public void doTag() throws JspException, IOException {
		
	}

}
