package com.xcap.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.ValidatorHandler;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.borqs.util.Utils;
import com.sun.tools.ws.processor.model.Request;
import com.xcap.ifc.Constants;
import com.xcap.ifc.Constants.HttpMethod;
import com.xcap.ifc.XCAPDatebaseIfc.ResultData;
import com.xcap.ifc.XCAPDatebaseIfc;
import com.xcap.ifc.XMLValidator;

public class XCAPServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final Logger log = Logger.getLogger(XCAPServlet.class);
	
	final static String SCHEMA_DIR = "/WEB-INF/classes/com/xcap/web/xmlschema";
	final static String XML_SCHEMA_CONTACT = "contacts.xsd";
	
	
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
				BufferedReader reader = req.getReader();
				Scanner scanner = new Scanner(reader);
				StringBuilder xmlBuilder = new StringBuilder();
				while(scanner.hasNextLine()){
					xmlBuilder.append(scanner.nextLine());
				}				
				//log.info("-----------------------put:" + xmlBuilder + " length:" + xmlBuilder.length());
				
				if( xmlBuilder.length() > 0){
					//xml form not-well-formed
					
					SAXParserFactory spf = SAXParserFactory.newInstance();
					spf.setNamespaceAware(true);
					//XMLReader reader1 = spf.newSAXParser().getXMLReader(); 
					//reader.parse(xml);


					//http://www.bitscn.com/plus/view.php?aid=22784					
					
					//validate document.
					String appSchema = null;
					
					if(auid.equals(Constants.APP_USAGE_CONTACT)){
						appSchema = XML_SCHEMA_CONTACT;
					}else{
						//other app usage.
						resp.sendError(HttpServletResponse.SC_NOT_FOUND, "auid is not implement");
					}
					
					try {
						String filePath = this.getServletContext().getRealPath(SCHEMA_DIR.concat("/").concat(appSchema));
						//log.info("file path:" + filePath + " " + new File(filePath));
						XMLValidator.xmlValidator(xmlBuilder.toString(), filePath, false);
					} catch (SAXException e) {
						e.printStackTrace();
					} catch (IOException e) {			
						e.printStackTrace();
					}
					
					xcapIfc.put(userId, queryString, xmlBuilder.toString());
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
