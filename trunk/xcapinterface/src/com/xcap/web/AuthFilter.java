package com.xcap.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

import com.xcap.ifc.Constants;
import com.xcap.ifc.XCAPDatebaseLocalIfc.ResultData;
import com.xcap.ifc.error.XCAPErrors;
import com.xcap.web.AuthFilter.Url.Field;

/**
 * 
 * <li>version 1.0</li>
 * <li>Create Date 2011-10-14<li>
 * <li>author slieer<li>
 */

public class AuthFilter implements Filter {
	public static final Logger log = Logger.getLogger(AuthFilter.class);

	private String donotFilterWord;
	public void init(FilterConfig config) throws ServletException {
		donotFilterWord = config.getInitParameter("donotFilterWord");
	}
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		String url = req.getRequestURI();
		String[] words = donotFilterWord.split(",");
		for(int i = 0; i < words.length; i++){
			if(url.contains(words[i])){
				chain.doFilter(request, response);				
				return;
			}
		}
		if(! Url.validateUrl(url)){
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
		
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		
		Map<Field, String> urlResult = Url.parserUrl(url);
		log.info("xcap url data:" + urlResult);
		if(urlResult != null && urlResult.size() > 0){
			String token = urlResult.get(Field.TOKEN);
			String auid = urlResult.get(Field.AUID);
			String msisdnParam= urlResult.get(Field.MSISDN);
			String queryString = urlResult.get(Field.QUERYSTRING);
			
			Map<Token.TokenInfo, String> tokenInfo = Token.parseTokenXML(token);
			log.info("token info:" + tokenInfo);
			
			if(tokenInfo == null){
				resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;				
			}
			String tokenStatus = tokenInfo.get(Token.TokenInfo.STATUS_TAG);
			String msisdn = tokenInfo.get(Token.TokenInfo.MSISDN_TAG);
			String uid = tokenInfo.get(Token.TokenInfo.UID_TAG);
			if (tokenStatus == null || msisdnParam == null || !tokenStatus.equals(Constants.TOKEN_OK) || ! msisdnParam.equals(msisdn)) {
				resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}			
			
			try {
				if(queryString != null){
					queryString = URLDecoder.decode(queryString,"utf-8");					
				}
			} catch (UnsupportedEncodingException e) {
				log.error("url UnsupportedEncodingException");
				resp.sendError(ResultData.STATUS_409,new XCAPErrors.NotUTF8ConflictException().getResponseContent());
				return;
			}
			
			req.setAttribute("auid", auid);
			req.setAttribute("queryString", queryString);
			req.setAttribute("uid", uid);
			req.setAttribute("method", req.getMethod());
			
			chain.doFilter(request, response);
		}else{
			try{
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST);	
				return ;				
			}catch (Exception e) {
				log.error("send error Exception.");
			}
		}		
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
			//log.info("xcap request url:" + url);
			if(url != null){
				
				String[] temps = url.split(Constants.INTERVAL_SIGN);  //split docmuent selector /node document.
				Map<Field,String> result = new EnumMap<Field, String>(Field.class);
				
				String[] documentSelectorInfo = null;
				String nodeSelectorInfo = null;
				if(temps.length == 1){
					documentSelectorInfo = temps[0].split("/"); 
				}else if(temps.length == 2){
					//log.info("-------------document selector:" + temps[0] + "  queryString:" + temps[1] + " temp.length:" + temps.length);
					documentSelectorInfo = temps[0].split("/");
					nodeSelectorInfo = temps[1];
				}
				
				//log.info("----------------------documentSelectorInfo.length:" + documentSelectorInfo.length);
				if(documentSelectorInfo != null && documentSelectorInfo.length == 6){
					//documentSelectorInfo =[, xcap-root, contacts, 8613480783139, DDcs3x7JwQQwqvOT751dhyp3s2od75lFbuwRL9UfCpJSwAeSqwV0bw**, index]
					int index = 2;
					String auid = documentSelectorInfo[index];
					String msisdn = documentSelectorInfo[index + 1];
					String token = documentSelectorInfo[index + 2];
					
					result.put(Field.AUID, auid);
					result.put(Field.MSISDN, msisdn);
					result.put(Field.TOKEN, token);
					if(nodeSelectorInfo != null){
						result.put(Field.QUERYSTRING, nodeSelectorInfo);
					}					
					return result;
				}
				//log.info("---------------url map:" + result);
			}
			
			return null;				
		}
	}
}
