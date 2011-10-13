package com.xcap.web;

import java.io.IOException;
import java.io.PrintWriter;
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
		String queryString = req.getQueryString();  //query=index or query=index~~/contacts/list[1]
		String url = req.getRequestURI();
		String method = req.getMethod();
		log.info("url, queryString-->" + url + " " + queryString);
				
		String userId = (String)req.getAttribute("uid");
		String appUsage = (String)req.getAttribute("auid");
		log.info("-------------------------uid, auid = " + userId + "," + appUsage);
		
		String nodeSelector = getNodeSelector(queryString);
			//log.warn("app usage invalidate.");				
			//resp.sendError(HttpServletResponse.SC_NOT_FOUND, "app usage invalidate.");
			//request.getRequestDispatcher("404.jsp").forward(request, resp);
		String jndi = null;
		
		if(appUsage == null){
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "auid is null");
			throw new IllegalStateException("auid is null");
		}
		if(appUsage.equals(Constants.APP_USAGE_CONTACT)){
			jndi = XCAPDatebaseIfc.CONTACT_JNDI;			
		}else{
			throw new IllegalStateException("other auid not implement...");
			//other app usage.
		}
		
		XCAPDatebaseIfc xcapIfc = (XCAPDatebaseIfc) Utils.lookupEJB("ContactListsApp");
		log.info("----------------------------ejb:" + xcapIfc);
		//XCAPDatebaseIfc xcapIfc = (XCAPDatebaseIfc) Utils.lookupEJB(jndi);

		if(xcapIfc != null){
			log.info("request method is " + method);
			HttpMethod httpMethod = HttpMethod.valueOf(method);
			
			switch (httpMethod) {
			case GET :				
			case POST:								
				//call ifc
				if(appUsage.equals(Constants.APP_USAGE_CONTACT)){
					String result = xcapIfc.get(userId, nodeSelector);
					PrintWriter writer = resp.getWriter();
					writer.print(result);
					//writer.close();
					
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
				if(nodeSelector == null){
					//document operate
					//调用ifc
				}else {
					//node operate
					//调用ifc
				}				
				break;
				
			default:
				break;
			}			
		}else{
			throw new IllegalStateException("get jndi is null");
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
	private static String getNodeSelector(String queryString) throws IOException{
		queryString = null;
		if(queryString != null){
			String[] params = queryString.split(Constants.INTERVAL_SIGN);			
			if(params != null){
				int fromIndex = "query=/users/".length();
				int endIndex = params[0].indexOf("/", fromIndex);
				String userName = params[0].substring(fromIndex, endIndex); 
				
				String domInfo = null;
				if(params.length == 2){
					domInfo = params[1];  //node selector.
				}
				return domInfo;
			}
		}
		return null;
	}
}
