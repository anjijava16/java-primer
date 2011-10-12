package com.xcap.web;

import java.io.IOException;
import java.io.Reader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.borqs.util.Utils;
import com.xcap.ifc.Constants;
import com.xcap.ifc.Constants.HttpMethod;
import com.xcap.ifc.XCAPDatebaseIfc;
import com.xcap.ifc.XMLValidator;

public class XCAPServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final Logger log = Logger.getLogger(XCAPServlet.class);
	
	final static String XML_SCHEMA_CONTACT = "./xmlschema/contact-list.xsd";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		executeRequest(req,  resp);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		executeRequest(req,  resp);		
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		executeRequest(req,  resp);
	}
		
	private String executeRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		String[] urlInfo = getUrlInfo(req,resp);
		
		XCAPDatebaseIfc contactIfc = (XCAPDatebaseIfc) Utils.lookupEJB(XCAPDatebaseIfc.CONTACT_JNDI);
		if(urlInfo != null && urlInfo.length > 0){
			String userId = urlInfo[0];
			String appUsage = urlInfo[1];
			String nodeSelector = urlInfo[2];
			
			String method = req.getMethod();
			log.info("request method is " + method);
			HttpMethod httpMethod = HttpMethod.valueOf(method);
			
			switch (httpMethod) {
			case GET :				
			case POST:								
				//call ifc
				if(appUsage.equals(Constants.APP_USAGE_CONTACT)){
					contactIfc.get(userId, nodeSelector);
				}else{
					//404
					
				}
				break;
			case PUT :
				//validate document.
				String appSchema = null;
				if(appUsage.equals(Constants.APP_USAGE_CONTACT)){
					appSchema = XML_SCHEMA_CONTACT;
				}else{
					//other app usage.
				}
							
				Reader reader = req.getReader();					
				try {
					XMLValidator.xmlValidator(reader, appSchema);
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {			
					e.printStackTrace();
				}				
				

				break;
			case DELETE:
				if(urlInfo.length == 1){
					//document operate
					//调用ifc
				}else if(urlInfo.length == 2){
					//node operate
					//调用ifc
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
	 * @return [userName,appUsage,nodeSelector], null if exception
	 * @throws IOException 
	 */
	private static String[] getUrlInfo(HttpServletRequest request, HttpServletResponse resp) throws IOException{
		String url = request.getRequestURI();
		String query = request.getQueryString();
		String appUsage = null;
		
		if(url.endsWith(Constants.APP_USAGE_CONTACT)){
			appUsage = Constants.APP_USAGE_CONTACT;
		}else {
			//other app usage.
			log.warn("app usage invalidate.");				
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "app usage invalidate.");
			//request.getRequestDispatcher("404.jsp").forward(request, resp);
		}
		
		if(query != null){
			String[] params = query.split(Constants.INTERVAL_SIGN);			
			if(params != null){
				int fromIndex = "query=/users/".length();
				int endIndex = params[0].indexOf("/", fromIndex);
				String userName = params[0].substring(fromIndex, endIndex); 
				
				String domInfo = null;
				if(params.length == 2){
					domInfo = params[1];  //node selector.
				}
				return new String[]{userName,appUsage,domInfo};
			}
		}
		resp.sendError(HttpServletResponse.SC_NOT_FOUND, "app usage invalidate.");
		
		return null;
	}
}
