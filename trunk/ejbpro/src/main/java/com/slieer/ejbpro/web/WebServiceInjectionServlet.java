package com.slieer.ejbpro.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceRef;

import com.slieer.ejbpro.ifc.soacookbook.ns.ws.catalog.Catalog;
import com.slieer.ejbpro.ifc.soacookbook.ns.ws.catalog.CatalogService;

public class WebServiceInjectionServlet extends HttpServlet {
	private static final long serialVersionUID = -3117483311296459568L;

	// @WebServiceRef(type=Catalog.class)
	// @WebServiceRef(wsdlLocation="http://127.0.0.1:8080/ejbpro_ejb/CatalogEJB?wsdl")
	// private CatalogService service;
	private Catalog catalog;

	@Override
	public void init(ServletConfig config) throws ServletException {
		// CatalogService svc = new CatalogService();
		// catalog = svc.getCatalogPort();

		URL wsdlLocation = null;
		try {
			wsdlLocation = new URL("http://127.0.0.1:8080/ejbpro_ejb/CatalogEJB?wsdl");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		QName serviceName = new QName("http://ns.soacookbook.com/ws/catalog", "CatalogService");
		CatalogService wsStub = new CatalogService(wsdlLocation, serviceName);
		catalog = wsStub.getCatalogPort();
	}

	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		processRequest(arg0, arg1);
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		// service instance injected...
		// Catalog port = service.getCatalogPort();
		String title = catalog.getTitle("12345");

		try {
			out.println("<html>");
			out.println("<head>");
			out.println("<title>WebServiceRef Test</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Title= " + title + "</h1>");
			out.println("</body>");
			out.println("</html>");

		} finally {
			out.close();
		}
	}
}
