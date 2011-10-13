package com.xcap.web;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import biz.source_code.base64Coder.Base64Coder;

import com.xcap.ifc.Constants;

public class AuthFilter implements Filter {
	public static final Logger log = Logger.getLogger(AuthFilter.class);

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		String queryString = req.getQueryString();
		String url = req.getRequestURI();
		//log.info("url, queryString-->" + url + " " + queryString);
		if(url.contains("test")){
			chain.doFilter(request, response);
			return;
		}

		log.info("basic authentication .......");

		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");

		String authString = req.getHeader("Authorization");
		if (authString == null) {
			resp.setStatus(401);
			String lostPassword = new String(
					Constants.BASIC_AUTH_LOST_PASSWORD.getBytes("utf-8"),
					"ISO-8859-1");
			resp.setHeader("WWW-authenticate", "Basic realm=\""
					+ lostPassword + "\"");	
			return;
		}else{
			log.info("authString:" + authString);
			String[] userInfoBase64 = authString.split(" ");
			String userInfo = Base64Coder.decodeString(userInfoBase64[1]);
			String[] up = userInfo.split(":");
			
			String token = up[0];
			log.info("token value is:" + token);
			// String password = up[1];
			
			Map<String, String> parseResult = Token.parseTokenXML(token);
			log.info("parseResult:" + parseResult);
			String tokenStatus = parseResult.get(Constants.STATUS_TAG);
			// String tokenMsisdn = parseResult.get(Constants.MSISDN_TAG);
			// String tokenExpiredTime =
			// parseResult.get(Constants.TOKEN_EXPIRED_TIME_TAG);
			String tokenUid = parseResult.get(Constants.UID_TAG);
			
			if (!tokenStatus.equals(Constants.TOKEN_OK)) {
				log.info("authentication failure.........");
				resp.setStatus(401);
				
				String passwordError = Constants.BASIC_AUTH_PASSWORD_ERROR;
				resp.setHeader("WWW-authenticate", "Basic realm=\""
						+ passwordError + "\"");
				//resp.sendError(HttpServletResponse.SC_PARTIAL_CONTENT);	
				return;
			}
			
			log.info("authentication OK.........");
			
			log.info("-------------------- in AuthFilter -->url, queryString-->" + url + "," + queryString);
			String[] partUrls = url.split("/");
			
			String auid = null;
			if(partUrls.length > 2){
				auid = partUrls[2];				
			}
			req.setAttribute(Constants.AUID, auid);
			req.setAttribute(Constants.UID, tokenUid);
			chain.doFilter(request, response);
		}

	}
	
	public void init(FilterConfig arg0) throws ServletException {

	}

	public static class Utils {
		public static final Logger log = Logger.getLogger(Utils.class);
		public static final int successful = 0;
		public static final int failure = 1;
	}
}
