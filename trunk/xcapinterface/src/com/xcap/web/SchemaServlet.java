package com.xcap.web;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xcap.ifc.Constants;

public class SchemaServlet extends HttpServlet {
	private static final long serialVersionUID = -3000270206818419378L;

	// test put.
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		BufferedReader reader = req.getReader();
		String line = null;
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}
	}

	/**
	 http://localhost:8080/xcap-root/xcap-schema/UABContacts
	 http://localhost:8080/xcap-root/xcap-schema/SingSpacesContacts
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String auid = req.getParameter("auid");
		PrintWriter writer = resp.getWriter();
		
		if(auid.startsWith("/")){
			auid = auid.substring(1);
		}
		String schemaName = "";
		if (auid.equals(Constants.APP_USAGE_CONTACT)) {
			schemaName = Constants.XML_SCHEMA_UAB_CONTACT;
		} else if (auid.equals(Constants.APP_USAGE_SINGSPACE_CONTACT)) {
			schemaName = Constants.XML_SCHEMA_SINGSPACES_CONTACT;
		}else{
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		String schemaPath = Constants.SCHEMA_DIR.concat("/").concat(schemaName);
		//req.getRealPath("");
		String realPathUrl = req.getSession().getServletContext().getRealPath(schemaPath);
		System.out.println(realPathUrl);
		Scanner scanner = new Scanner(new FileReader(realPathUrl));
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			writer.print(line);
		}
		scanner.close();
		writer.flush();
		writer.close();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

}
