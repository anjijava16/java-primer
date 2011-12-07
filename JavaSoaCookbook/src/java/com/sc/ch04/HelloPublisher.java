package com.sc.ch04;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;

import org.junit.Test;

/**
 * http://localhost:9999/hello?wsdl
 * @author me
 *
 */
public class HelloPublisher {
	public static final String URI = "http://localhost:9999/hello";
	
	@Test
	public void test(){
		// Create instance of service implementation
		HelloWS impl = new HelloWS();

		// Make available
		Endpoint endpoint = Endpoint.publish(URI, impl);

		// Test that it is available
		boolean status = endpoint.isPublished();
		System.out.println("Web service status = " + status);		
	}
	
	@Test
	public void qname(){
		QName bodyName = new QName("http://example.com", "getQuote", "e");
		System.out.println(bodyName);
	}
}
