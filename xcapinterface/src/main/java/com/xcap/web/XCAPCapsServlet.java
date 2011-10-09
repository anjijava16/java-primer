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
		String str = "<?xml version='1.0' encoding='UTF-8'?>"
				.concat("<xcap-caps xmlns=\"urn:ietf:params:xml:ns:xcap-caps\">")
				.concat("<auids>")
				.concat("  <auid>contact-lists</auid>")
				.concat("</auids>")
				.concat("<extensions/>")
				.concat(" <namespaces>")
				.concat("  <namespace>contact-lists</namespace>")
				.concat(" </namespaces>")
				.concat("</xcap-caps>");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
		doGet(request, response);
	}
}
