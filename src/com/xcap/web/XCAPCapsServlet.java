package com.xcap.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class XCAPCapsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final Logger log = Logger.getLogger(XCAPCapsServlet.class);

	public XCAPCapsServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//url query : xcap-root/xcap-caps/global/index
		String ser = request.getServerName();
		int port = request.getServerPort();
		String contextPath = request.getSession().getServletContext().getContextPath();
		String url = ser.concat(":").concat(String.valueOf(port));
		
		String str = "<?xml version='1.0' encoding='UTF-8'?>"
				.concat("<xcap-caps xmlns=\"urn:ietf:params:xml:ns:xcap-caps\">")
				.concat("<auids>")
				.concat("  <auid>UABContacts</auid>")
				.concat("  <auid>SingSpacesContacts</auid>")
				.concat("</auids>")
				.concat("<extensions/>")
				.concat(" <namespaces>")
				.concat("  <namespace>http://").concat(url).concat(contextPath).concat("/xcap-schema/UABContacts</namespace>")
				.concat("  <namespace>http://").concat(url).concat(contextPath).concat("/xcap-schema/SingSpacesContacts</namespace>")
				.concat(" </namespaces>")
				.concat("</xcap-caps>");
		
		response.getWriter().print(str);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
		doGet(request, response);
	}
}
