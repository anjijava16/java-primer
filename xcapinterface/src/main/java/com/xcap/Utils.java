package com.xcap;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import biz.source_code.base64Coder.Base64Coder;

import com.xcap.services.Token;

public class Utils {
	public static final Logger log = Logger.getLogger(Utils.class);
	public static final int successful = 0;
	public static final int failure = 1;
	/**
	 * @param req
	 * @param resp
	 * @return 0 authentication successful; 1 authentication failure; 
	 * @throws IOException
	 */
	public static int basicAuth(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		log.info("basic authentication .......");
		
		req.setCharacterEncoding("utf-8");
	    resp.setCharacterEncoding("utf-8");

		String authString = req.getHeader("Authorization");
		if(authString == null){ 
			resp.setStatus(401); 
			String lostPassword = new String(Constants.BASIC_AUTH_LOST_PASSWORD.getBytes("utf-8"),"ISO-8859-1");   
			resp.setHeader("WWW-authenticate","Basic realm=\"" + lostPassword + "\"");
			return failure;
		}
		
		String[] userInfoBase64 = authString.split(" ");
		String userInfo = Base64Coder.decodeString(userInfoBase64[1]);
		String[] up = userInfo.split(":");

		//String userName = up[0];
		String password = up[1];
		
		Map<String,String> parseResult = Token.parseTokenXML(password);
		String tokenStatus = parseResult.get(Constants.STATUS_TAG);
		//String tokenMsisdn = parseResult.get(Constants.MSISDN_TAG);
		//String tokenExpiredTime = parseResult.get(Constants.TOKEN_EXPIRED_TIME_TAG);
		//String tokenUid = parseResult.get(Constants.UID_TAG);
		
		if(! tokenStatus.equals(Constants.TOKEN_OK)){
			resp.setStatus(401);
			resp.setHeader("", "");
			
			String passwordError = Constants.BASIC_AUTH_PASSWORD_ERROR;   
			resp.setHeader("WWW-authenticate","Basic realm=\"" + passwordError + "\"");
			resp.sendError(HttpServletResponse.SC_PARTIAL_CONTENT);
			return failure;
		}
		
		return successful;
	}
	/**
	 * 
	 * @param request
	 * @param resp
	 * @return [userName,domInfo]
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
