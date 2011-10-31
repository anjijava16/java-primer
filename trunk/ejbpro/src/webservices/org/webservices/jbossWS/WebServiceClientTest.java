package org.webservices.jbossWS;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Remote;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.ServiceFactory;
import javax.xml.ws.WebServiceClient;

import org.junit.Test;

/**
 * http://community.jboss.org/wiki/JBossWS-UserGuide
 * 
 * @author me
 */
public class WebServiceClientTest {
	@Test
	public void test(){
		try {
			Client.f();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void test1(){
		URL wsdlLocation;
		try {
			wsdlLocation = new URL("http://example.org/my.wsdl");
			QName serviceName = new QName("http://example.org/sample", "MyService");   
			javax.xml.ws.Service service = javax.xml.ws.Service.create(wsdlLocation, serviceName);	

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	}
}


class Client{
	public static void f() throws MalformedURLException, ServiceException, RemoteException {

		String wsdlUrl = "http://127.0.0.1:8080/jbossws-samples-jsr181pojo?wsdl";
		String nameSpaceUri = "http://jbossWS.webservices.org/";

		String serviceName = "JSEBean01Service";
		String portName = "JSEBean01Port";

		ServiceFactory serviceFactory = ServiceFactory.newInstance();
		Service afService = serviceFactory.createService(new URL(wsdlUrl), new QName(nameSpaceUri, serviceName));
		Remote proxy = afService.getPort(new QName(nameSpaceUri, portName), Object.class);
		System.out.println(proxy);
	}

}
