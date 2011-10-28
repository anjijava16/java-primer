package com.slieer.ejbpro;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.apache.log4j.Logger;
import org.junit.Test;

public class WebServiceClient {
	static Logger log = Logger.getLogger(WebServiceClient.class);

	static String serviceName = "HelloWebServiceService";
	static String nameSpace = "http://ejb.ejbpro.slieer.com/";

	@Test
	void webServiceTest() throws Exception {
		URL wsdlLocation = new URL("http://127.0.0.1:8080/ejbpro_ejb/HelloWebService?wsdl");
		QName serviceNameQ = new QName(nameSpace, serviceName);

		Service service = Service.create(wsdlLocation, serviceNameQ);
		log.info("service:" + service);
		System.out.println(service);
	}
}
