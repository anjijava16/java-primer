package com.xcap;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.xcap.web.XCAPCapsServlet;

public class Utils {
	public static final Logger log = Logger.getLogger(XCAPCapsServlet.class);

	public void basicAuth(HttpServletRequest req, HttpServletResponse resp, String pass) throws IOException{		
		if(req.getHeader("Authorization")==null){ 
			resp.setStatus(401); 
			resp.setHeader("WWW-authenticate","Basic realm=\"" + Constants.BASIC_AUTH_LOST_PASSWORD + "\""); 
		}else if( !(req.getHeader("Authorization").equals(pass))){
			resp.setStatus(401);     
			resp.setHeader("WWW-authenticate","Basic realm=\"" + Constants.BASIC_AUTH_PASSWORD_ERROR + "\"");
			resp.getOutputStream().print(Constants.BASIC_AUTH_PASSWORD_ERROR_INFO);
			return;
		}
	}
	
	public static String[] urlParser(HttpServletRequest request, HttpServletResponse resp){
		String url = request.getRequestURI();
		String query = request.getQueryString();

		if(! url.endsWith(Constants.APP_USAGE)){
			log.warn("app usage invalidate.");
			try {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND);
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
