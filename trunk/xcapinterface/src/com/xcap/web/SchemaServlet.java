package com.xcap.web;

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
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String auid = req.getParameter("");
		PrintWriter writer = resp.getWriter();
		
		String schemaName = "";
		if(auid.equals(Constants.APP_USAGE_CONTACT)){
			schemaName = Constants.XML_SCHEMA_CONTACT;
		}else if(auid.equals(Constants.APP_USAGE_SINGSPACE_CONTACT)){
			schemaName = "";
		}
		String schemaPath = Constants.SCHEMA_DIR.concat(schemaName);
		Scanner scanner = new Scanner(new FileReader(schemaPath));
		while(scanner.hasNextLine()){
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
