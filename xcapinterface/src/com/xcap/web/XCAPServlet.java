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
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.ValidatorHandler;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.borqs.util.Utils;
import com.sun.tools.ws.processor.model.Request;
import com.xcap.ifc.Constants;
import com.xcap.ifc.Constants.HttpMethod;
import com.xcap.ifc.XCAPDatebaseLocalIfc.ResultData;
import com.xcap.ifc.XCAPDatebaseLocalIfc;
import com.xcap.ifc.XMLValidator;
import com.xcap.ifc.error.XCAPErrors;

public class XCAPServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final Logger log = Logger.getLogger(XCAPServlet.class);

	final static String SCHEMA_DIR = "/WEB-INF/classes/com/xcap/web/xmlschema";
	final static String XML_SCHEMA_CONTACT = "contacts.xsd";

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

	private String executeRequest(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		// String queryString = req.getQueryString();
		// String url = req.getRequestURI();
		// log.info("url, queryString-->" + url + " " + queryString);

		String userId = (String) req.getAttribute("uid");
		String auid = (String) req.getAttribute("auid");
		String nodeSelector = (String) req.getAttribute("queryString");
		String method = (String) req.getAttribute("method");

		log.info("------(method,userId, auid,nodeSelector) = " + method + ","
				+ userId + "," + auid + "," + nodeSelector);

		String jndi = null;
		if (auid == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "auid is null");
			throw new IllegalStateException("auid is null");
		}
		if (auid.equals(Constants.APP_USAGE_CONTACT)) {
			jndi = XCAPDatebaseLocalIfc.CONTACT_LOCAL_JNDI;
		} else {
			throw new IllegalStateException("other auid not implement...");
			// other app usage.
		}

		XCAPDatebaseLocalIfc xcapIfc = (XCAPDatebaseLocalIfc) Utils.lookupEJB(jndi);
		if (xcapIfc != null) {
			HttpMethod httpMethod = HttpMethod.valueOf(method);
			switch (httpMethod) {
			case GET:
			case POST:
				resp.setContentType("text/xml");
				// call ifc
				if (auid.equals(Constants.APP_USAGE_CONTACT)) {
					// log.info("---------------------queryString:" +
					// queryString);
					ResultData data = xcapIfc.get(userId, nodeSelector);
					String result = data.getXml();
					if (data.getstatus() != HttpServletResponse.SC_OK) {
						log.info("error status code is " + data.getstatus());
						resp.setStatus(data.getstatus());
						resp.sendError(data.getstatus(), data.getXml());
					} else {
						log.info("result xml :" + result);
						PrintWriter writer = resp.getWriter();
						writer.print(result);
						// writer.close();
					}
				} else {
					// 404
					resp.sendError(HttpServletResponse.SC_NOT_FOUND,
							"auid is not implement");
				}
				break;
			case PUT:
				BufferedReader reader = req.getReader();
				Scanner scanner = new Scanner(reader);
				StringBuilder xmlBuilder = new StringBuilder();
				while (scanner.hasNextLine()) {
					xmlBuilder.append(scanner.nextLine());
				}

				if (xmlBuilder.length() > 0) {
					String appSchema = null;

					if (auid.equals(Constants.APP_USAGE_CONTACT)) {
						appSchema = XML_SCHEMA_CONTACT;
					} else {
						// other app usage.
						resp.sendError(HttpServletResponse.SC_NOT_FOUND,
								"auid is not implement");
					}

					int result = -1;
					try {
						String filePath = this.getServletContext().getRealPath(
								SCHEMA_DIR.concat("/").concat(appSchema));

						// xml form not-well-formed
						// validate document.
						result = XMLValidator.xmlValidator(
								xmlBuilder.toString(), filePath);

					} catch (IOException e) {
						e.printStackTrace();
						log.error(e.getMessage());
					}

					if (result == XMLValidator.RESULT_OK) {
						xcapIfc.put(userId, nodeSelector, xmlBuilder.toString());
					} else {
						String xmlError = "";
						if (result == XMLValidator.RESULT_STRUCTURE_ERROR) {
							xmlError = new XCAPErrors.NotWellFormedConflictException()
									.getResponseContent();

						} else {
							// result ==
							// XMLValidator.RESULT_SCHEMA_VALIDATE_ERROR
							xmlError = new XCAPErrors.SchemaValidationErrorConflictException()
									.getResponseContent();
						}
						resp.setStatus(XCAPDatebaseLocalIfc.ResultData.STATUS_409);
						resp.setContentType("text/xml");
						resp.getWriter().append(xmlError);
					}
				}

				break;
			case DELETE:
				break;

			default:
				break;
			}
		} else {
			throw new IllegalStateException("get jndi is null");
		}
		return null;
	}
}
