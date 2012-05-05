package com.slieer.ejbpro;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import javax.xml.namespace.QName;

import org.junit.Test;

/**
 * http://community.jboss.org/wiki/JBossWS-UserGuide
 * 
 * @author me
 */
public class WebServiceClientTest {

	@Test
	public void test1(){
		URL wsdlLocation;
		try {
			wsdlLocation = new URL(wsdlUrl);
			QName serviceName = new QName(nameSpaceUri, WebServiceClientTest.serviceName);   
			javax.xml.ws.Service service = javax.xml.ws.Service.create(wsdlLocation, serviceName);	
			//System.out.println(service);
			
			Iterator<QName> it = service.getPorts();
			while(it.hasNext()){
				System.out.println(it.next().toString());
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	}

	final static String wsdlUrl = "http://127.0.0.1:8080/jbossws-samples-jsr181pojo?wsdl";
	final static String nameSpaceUri = "http://jbossWS.webservices.org/";
	final static String serviceName = "JSEBean01Service";	
}