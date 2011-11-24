package com.xcap.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.borqs.util.Utils;
import com.xcap.ifc.Constants;
import com.xcap.ifc.Constants.HttpMethod;
import com.xcap.ifc.XCAPDatebaseLocalIfc;
import com.xcap.ifc.XCAPDatebaseLocalIfc.ResultData;
import com.xcap.ifc.XMLValidator;
import com.xcap.ifc.error.XCAPErrors;

public class XCAPServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final Logger log = Logger.getLogger(XCAPServlet.class);


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		executeRequest(req, resp);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		executeRequest(req, resp);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		executeRequest(req, resp);
	}

	private void executeRequest(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		// String queryString = req.getQueryString();
		// String url = req.getRequestURI();
		// log.info("url, queryString-->" + url + " " + queryString);

		String userId = (String) req.getAttribute("uid");
		String auid = (String) req.getAttribute("auid");
		String nodeSelector = (String) req.getAttribute("queryString");
		String method = (String) req.getAttribute("method");
		String msisdn = (String)req.getAttribute("msisdn");

		log.info("(method,userId,msisdn,auid,nodeSelector) = " + method + ","
				+ userId + ","  + msisdn + "," + auid + "," + nodeSelector);

		String jndi = null;
		String userInfo = null;
		if (auid == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "auid is null");
			throw new IllegalStateException("auid is null");
		}
		if (auid.equals(Constants.APP_USAGE_CONTACT)) {
			jndi = XCAPDatebaseLocalIfc.UAB_CONTACTS_LOCAL_JNDI;
			userInfo = msisdn;
		} else if(auid.equals(Constants.APP_USAGE_SINGSPACE_CONTACT)){
			jndi = XCAPDatebaseLocalIfc.SING_SPACE_CONTACTS_LOCAL_JNDI;
			userInfo = userId;
		}else{
			throw new IllegalStateException("other auid not implement...");
			// other app usage.
		}

		XCAPDatebaseLocalIfc xcapIfc = (XCAPDatebaseLocalIfc) Utils.lookupEJB(jndi);
		//log.info("-----------xcapIfc proxy:" + xcapIfc);
		if (xcapIfc != null) {
			HttpMethod httpMethod = HttpMethod.valueOf(method);
			switch (httpMethod) {
			case GET:
			case POST:
				resp.setContentType("text/xml");
				// call ifc
				if (auid.equals(Constants.APP_USAGE_CONTACT) || auid.equals(Constants.APP_USAGE_SINGSPACE_CONTACT)) {
					ResultData data = xcapIfc.get(userInfo, nodeSelector);
					String result = data.getXml();
					if (data.getstatus() != HttpServletResponse.SC_OK) {
						log.info("------error status code is " + data.getstatus());
						resp.setStatus(data.getstatus());
						resp.sendError(data.getstatus(), data.getXml());
					} else {
						log.info("------result xml :" + result);
						PrintWriter writer = resp.getWriter();
						writer.print(result);
						writer.close();
					}
				} else {
					// 404
					resp.sendError(HttpServletResponse.SC_NOT_FOUND,
							"auid is not implement");
				}
				break;
			case PUT:				
				Scanner scanner = new Scanner(req.getReader());
				StringBuilder xmlBuilder = new StringBuilder();
				while (scanner.hasNextLine()) {
					xmlBuilder.append(scanner.nextLine());
				}

				if (xmlBuilder.length() > 0) {
					String appSchema = null;

					if (auid.equals(Constants.APP_USAGE_CONTACT)) {
						appSchema = Constants.XML_SCHEMA_UAB_CONTACT;
					} else if(auid.equals(Constants.APP_USAGE_SINGSPACE_CONTACT)){
						appSchema = Constants.XML_SCHEMA_SINGSPACES_CONTACT;
					}else{
						// other app usage.
						resp.sendError(HttpServletResponse.SC_NOT_FOUND,
								"auid is not implement");
					}

					int result = -1;
					try {
						String filePath = this.getServletContext().getRealPath(
								Constants.SCHEMA_DIR.concat("/").concat(appSchema));

						// xml form not-well-formed
						// validate document.
						result = XMLValidator.xmlValidator(
								xmlBuilder.toString(), filePath);

					} catch (IOException e) {
						e.printStackTrace();
						log.error(e.getMessage());
					}

					if (result == XMLValidator.RESULT_OK) {
						log.info("xml schema validated, put xml to ejb...");
						ResultData resultData = xcapIfc.put(userInfo, nodeSelector, xmlBuilder.toString());
						if(resultData.getstatus() == ResultData.STATUS_404){
							resp.sendError(HttpServletResponse.SC_NOT_FOUND);
						}else if(resultData.getstatus() == ResultData.STATUS_409){
							resp.setStatus(ResultData.STATUS_409);
							resp.setContentType("text/xml");
							resp.getWriter().append(resultData.getXml());
						}else if(resultData.getstatus() == ResultData.STATUS_200){
							resp.setStatus(HttpServletResponse.SC_OK);
						}
					} else {
						String xmlError = "";
						if (result == XMLValidator.RESULT_STRUCTURE_ERROR) {
							xmlError = new XCAPErrors.NotWellFormedConflictException()
									.getResponseContent();

						} else {
							xmlError = new XCAPErrors.SchemaValidationErrorConflictException()
									.getResponseContent();
						}
						resp.setStatus(ResultData.STATUS_409);
						resp.setContentType("text/xml");
						resp.getWriter().append(xmlError);
					}
				}

				break;
			case DELETE:
				ResultData re = xcapIfc.delete(userInfo, nodeSelector);
				if(ResultData.STATUS_200 == re.getstatus()){
					resp.setStatus(ResultData.STATUS_200);
				}else if(ResultData.STATUS_409 == re.getstatus()){
					resp.setStatus(ResultData.STATUS_409);
					resp.setContentType("text/xml");
					resp.getWriter().append(re.getXml());					
				}else if(ResultData.STATUS_404 == re.getstatus()){
					resp.sendError(HttpServletResponse.SC_NOT_FOUND);
				}
				
				break;
			}
		} else {
			resp.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			throw new IllegalStateException("get jndi is null");
		}
	}
}
