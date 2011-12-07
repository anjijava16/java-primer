package com.slieer.ejbpro;

import static java.lang.System.out;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;

import org.junit.Test;

public class CatalogTest {
	
	@Test
	public void dispatchMsgIsbnTest() {
		try {
			URL wsdl = new URL("http://127.0.0.1:8080/ejbpro_ejb/CatalogEJB?wsdl");

			String ns = "http://ns.soacookbook.com/ws/catalog";

			// Create the Service qualified name
			String svcName = "CatalogService";
			QName svcQName = new QName(ns, svcName);

			// Get a delegate wrapper
			Service service = Service.create(wsdl, svcQName);

			// Create the Port name
			String portName = "CatalogPort";
			QName portQName = new QName(ns, portName);

			// Create the delegate to send the request:
			Dispatch<SOAPMessage> dispatch = service.createDispatch(portQName, SOAPMessage.class, Service.Mode.MESSAGE);

			String xml =  "<?xml version='1.0' encoding='utf-8'?>" + 
										"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
										"<soap:Body>" + 
										"<i:isbn xmlns:i=\"http://ns.soacookbook.com/catalog\">12345</i:isbn>" +
										"</soap:Body>" + 
										"</soap:Envelope>";
			
			//InputStream fis = new ByteArrayInputStream(xml.getBytes());
			URL u = this.getClass().getResource("");
			String path = u.getFile() + "isbnMsg.xml";
			FileInputStream fis = new FileInputStream(path);

			
			// create the message, using contents of file as envelope
			SOAPMessage request = MessageFactory.newInstance().createMessage(null, fis);

			// debug print what we're sending
			request.writeTo(out);

			out.println("\nInvoking...");

			// send the message as request to service and get response
			SOAPMessage response = dispatch.invoke(request);

			// just show in the console for now
			response.writeTo(System.out);

		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} catch (WebServiceException wsex) {
			wsex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
