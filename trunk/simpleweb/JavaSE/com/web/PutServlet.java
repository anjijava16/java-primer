package com.web;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PutServlet extends HttpServlet{
	private static final long serialVersionUID = 8210964834240904718L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		BufferedReader reader = req.getReader();
		String line = null;
		while((line = reader.readLine()) != null){
			System.out.println(line);
		}
	}
}
