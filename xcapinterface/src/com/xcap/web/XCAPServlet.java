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
import com.xcap.ifc.XCAPDatebaseIfc.ResultData;
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
		//String queryString = req.getQueryString();  
		//String url = req.getRequestURI();
		//log.info("url, queryString-->" + url + " " + queryString);
		
		String userId = (String)req.getAttribute("uid");
		String auid = (String)req.getAttribute("auid");
		String queryString = (String)req.getAttribute("queryString");
		String method = (String)req.getAttribute("method");
		
		log.info("------(method,userId, auid,queryString) = " + method + "," + userId + "," + auid + "," + queryString);
				
		String jndi = null;
		if(auid == null){
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "auid is null");
			throw new IllegalStateException("auid is null");
		}
		if(auid.equals(Constants.APP_USAGE_CONTACT)){
			jndi = XCAPDatebaseIfc.CONTACT_LOCAL_JNDI;			
		}else{
			throw new IllegalStateException("other auid not implement...");
			//other app usage.
		}
		
		XCAPDatebaseIfc xcapIfc = (XCAPDatebaseIfc) Utils.lookupEJB(jndi);
		if(xcapIfc != null){
			HttpMethod httpMethod = HttpMethod.valueOf(method);
			switch (httpMethod) {
			case GET :				
			case POST:
				resp.setContentType("text/xml");
				//call ifc
				if(auid.equals(Constants.APP_USAGE_CONTACT)){
					//log.info("---------------------queryString:" + queryString);
					ResultData data = xcapIfc.get(userId, queryString);
					String result = data.getXml();
					if(data.getstatus() != HttpServletResponse.SC_OK){
						log.info("error status code is " + data.getstatus());
						resp.setStatus(data.getstatus());
						resp.sendError(data.getstatus(), data.getXml());
					}else{
						log.info("result xml :" + result);
						PrintWriter writer = resp.getWriter();
						writer.print(result);
						//writer.close();						
					}					
				}else{
					//404					
					resp.sendError(HttpServletResponse.SC_NOT_FOUND, "auid is not implement");
				}
				break;
			case PUT :
				//validate document.
				String appSchema = null;
				if(auid.equals(Constants.APP_USAGE_CONTACT)){
					appSchema = XML_SCHEMA_CONTACT;
				}else{
					//other app usage.
					resp.sendError(HttpServletResponse.SC_NOT_FOUND, "auid is not implement");
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
				break;
				
			default:
				break;
			}			
		}else{
			throw new IllegalStateException("get jndi is null");
		}
		return null;	
	}
}
