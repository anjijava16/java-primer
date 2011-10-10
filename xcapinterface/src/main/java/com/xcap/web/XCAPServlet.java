package com.xcap.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.xcap.ifc.Constants;
import com.xcap.ifc.Constants.HttpMethod;

public class XCAPServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final Logger log = Logger.getLogger(XCAPServlet.class);
	
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
	
	/**
	 * http://www.ietf.org/rfc/rfc4825.txt [Page 6]
	 * @return
	 */
	String afterTildeStringParser(String nodeSelector ){
		
		return null;
	}
	
	private String executeRequest(HttpServletRequest req, HttpServletResponse resp,String op){
		String[] urlInfo = getUrlInfo(req,resp);
		if(urlInfo != null && urlInfo.length > 0){
			String method = req.getMethod();
			log.info("request method is " + method);
			HttpMethod httpMethod = HttpMethod.valueOf(method);
			
			switch (httpMethod) {
			case GET :				
			case POST:
				if(urlInfo.length == 1){
					
				}else if(urlInfo.length == 2){
					
				}else{
					//error
				}
				break;
			case PUT :
				//validate document.
				if(urlInfo.length == 1){
					//document operate
				}else if(urlInfo.length == 2){
					//node operate
				}else{
					//error
				}
				break;
			case DELETE:
				if(urlInfo.length == 1){
					//document operate
				}else if(urlInfo.length == 2){
					//node operate
				}else{
					//error
				}				
				break;
				
			default:
				break;
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
	private static String[] getUrlInfo(HttpServletRequest request, HttpServletResponse resp){
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
				if(params.length == 2){
					domInfo = params[1];
				}
				return new String[]{userName,domInfo};
			}else{
				try {
					resp.sendError(HttpServletResponse.SC_NOT_FOUND, "app usage invalidate.");
				} catch (IOException e) {
					log.error("Exception:" + e.getMessage());
					e.printStackTrace();
				}				
			}
		}else {
			try {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND, "app usage invalidate.");
			} catch (IOException e) {
				log.error("Exception:" + e.getMessage());
				e.printStackTrace();
			}				
		}
		
		return null;
	}
}
