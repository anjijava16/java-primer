package com.xcap.web;

import java.io.IOException;
import java.util.EnumMap;
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

import sun.reflect.generics.tree.Tree;

import com.xcap.ifc.Constants;
import com.xcap.web.AuthFilter.Url.Field;

/**
 * 
 * version 1.0
 * Create Date 2011-10-14
 * @author slieer
 */

public class AuthFilter implements Filter {
	public static final Logger log = Logger.getLogger(AuthFilter.class);

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		String url = req.getRequestURI();
		if(url.contains("test")){
			chain.doFilter(request, response);
			return;
		}
		if(! Url.validateUrl(url)){
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
		
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		
		Map<Field, String> urlResult = Url.parserUrl(url);
		log.info("-----------urlResult:" + urlResult);
		if(urlResult != null && urlResult.size() > 0){
			String token = urlResult.get(Field.TOKEN);
			String auid = urlResult.get(Field.AUID);
			String msisdnParam= urlResult.get(Field.MSISDN);
			String queryString = urlResult.get(Field.QUERYSTRING);
			
			Map<Token.TokenInfo, String> tokenInfo = Token.parseTokenXML(token);
			log.info("tokenInfo:" + tokenInfo);
			
			String tokenStatus = tokenInfo.get(Token.TokenInfo.STATUS_TAG);
			String msisdn = tokenInfo.get(Token.TokenInfo.MSISDN_TAG);
			String uid = tokenInfo.get(Token.TokenInfo.UID_TAG);
			if (tokenStatus == null || msisdnParam == null || !tokenStatus.equals(Constants.TOKEN_OK) || ! msisdnParam.equals(msisdn)) {
				resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}			
			
			req.setAttribute("auid", auid);
			req.setAttribute("queryString", queryString);
			req.setAttribute("uid", uid);
			req.setAttribute("method", req.getMethod());
			
			chain.doFilter(request, response);
		}else{
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);	
		}		
	}
	
	public void init(FilterConfig arg0) throws ServletException {
	}

	public void destroy() {
	}

	static class Url{
		enum Field{
			AUID,MSISDN,TOKEN,QUERYSTRING
		}
		
		public static boolean validateUrl(String url){
			String[] temps = url.split(Constants.INTERVAL_SIGN);
			if(url == null || temps.length > 2){  //~~ should only one
				return false;
			}
			if( !temps[0].endsWith("/index")){
				return false;
			}
			return true;
		}

		//url:
		// /xcap-root/contacts/13658654865/q-5cNwHjuSmigz4upDSTAbz9TnI8YYnseENltm9MztlSwAeSqwV0bw**/index~~/contacts/list%5B@id=%22classmate@facebook%22%5D,
		//queryString: null
		/**
		 * @param url
		 * @return <li>map key: auid, uid, token, queryString;</li>
		 *         <li> map is null, parameter url is null</li>
		 *         <li> map size==0, url exception(~~ should only one)</li>
		 */
		public static Map<Field,String> parserUrl(String url){
			if(url != null){
				//log.info("---------url:" + url);
				
				String[] temps = url.split(Constants.INTERVAL_SIGN);  //split docmuent selector /node document.
				Map<Field,String> result = new EnumMap<Field, String>(Field.class);
				
				String[] urlInfo = null;
				String queryString = null;
				if(temps.length == 1){
					urlInfo = temps[0].split("/"); 
				}else if(temps.length == 2){
					urlInfo = temps[0].split("/");
					queryString = temps[1];
				}
				
				int index = 2;
				String auid = urlInfo[index];
				String msisdn = urlInfo[index + 1];
				String token = urlInfo[index + 2];
				
				result.put(Field.AUID, auid);
				result.put(Field.MSISDN, msisdn);
				result.put(Field.TOKEN, token);
				if(queryString != null){
					result.put(Field.QUERYSTRING, queryString);
				}
				
				//log.info("---------------url map:" + result);
				return result;
			}else{
				return null;				
			}
		}
	}
}
