package com.xcap.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.xcap.ifc.Constants;

public class XCAPServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final Logger log = Logger.getLogger(XCAPCapsServlet.class);
	
	public XCAPServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String[] str = getUrlInfo(req,resp);
		resp.getOutputStream().print(str[0] + "<br/>");
		resp.getOutputStream().print(str[1] + "<br/>");
		
		
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}
	
	String parseQuerystring(HttpServletRequest req, HttpServletResponse resp,String op){
		String[] str = getUrlInfo(req,resp);
		if(str != null ){
			if(str.length == 1){
				//document operate
			}else if(str.length == 2){
				//node operate
			}else{
				//error
			}
		}
		return null;	
	}
	/**
	 * 
	 * @param request
	 * @param resp
	 * @return [userName,domInfo], null if exception
	 */
	public static String[] getUrlInfo(HttpServletRequest request, HttpServletResponse resp){
		String url = request.getRequestURI();
		String query = request.getQueryString();

		if(! url.endsWith(Constants.APP_USAGE)){
			log.warn("app usage invalidate.");
			try {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND, "app usage invalidate.");

				//request.getRequestDispatcher("404.jsp").forward(request, resp);
			} catch (Exception e) {
				log.error(e.getMessage());
			}		
		}
		
		if(query != null){
			String[] params = query.split(Constants.INTERVAL_SIGN);			
			if(params != null){
				int fromIndex = "query=/users/".length();
				int endIndex = params[0].indexOf("/", fromIndex);
				String userName = params[0].substring(fromIndex, endIndex); 
				
				String domInfo = null;
				if(params.length == 1){
					//document operate
				}else if(params.length == 2){
					//element operate
					domInfo = params[1];
				}else{
					//error.
				}
				
				return new String[]{userName,domInfo};
				//return url + "<br/>" + query + "<br/>" + userName + "<br/>" + domInfo;
			}else{
				
			}
		}else {
			//error
		}
		
		return null;
	}
}
