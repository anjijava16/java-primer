package com.xcap.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.xcap.Utils;

public class XCAPServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final Logger log = Logger.getLogger(XCAPCapsServlet.class);
	
	public XCAPServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String[] str = Utils.getUrlInfo(req,resp);
		resp.getOutputStream().print(str[0] + "<br/>");
		resp.getOutputStream().print(str[1] + "<br/>");
		
		
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}
	
}
