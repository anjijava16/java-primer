package com.sc.ch5;

import static java.lang.System.out;

import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * @author me
 */
public class SoapEnv {
	public static final String HTTP_WSDL_PATH = "http://127.0.0.1:8080/ejbpro_ejb/CatalogEJB?wsdl";
	/***
	 * 示例5.1: 手工创建完整的SOAP消息。
	 * SAAJ, SOAP with Attachments API form java
	 * 1、 构建dispath对象
	 * 2、构建要发送消息
	 * 3、发送消息
	 * 4、返回结果.
	 */
	@Test
	public void buildSoapEnv() {
		try {
		    URL wsdl =
		            new URL(HTTP_WSDL_PATH);

		    String ns = "http://ns.soacookbook.com/ws/catalog";

		    //Create the Service name
		    String svcName = "CatalogService";
		    QName svcQName = new QName(ns, svcName);

		    //Get a delegate wrapper
		    Service service = Service.create(wsdl, svcQName);

		    //Create the Port name
		    String portName = "CatalogPort";
		    QName portQName = new QName(ns, portName);

		    Dispatch<SOAPMessage> dispatch =
		            service.createDispatch(portQName,
		            SOAPMessage.class, Service.Mode.MESSAGE);
		    
		    //Create the message
		    SOAPMessage soapMsg =
		            MessageFactory.newInstance().createMessage();

		    //Get the body from the envelope
		    SOAPPart soapPart = soapMsg.getSOAPPart();
		    SOAPEnvelope env = soapPart.getEnvelope();
		    SOAPBody body = env.getBody();
		    
		    //Create a qualified name for the namespace of the
		    //objects used by the service. 
		    String iNs = "http://ns.soacookbook.com/catalog";
		    String elementName = "isbn";
		    QName isbnQName = new QName(iNs, elementName);
		    
		    //Add the <isbn> element to the SOAP body 
		    //as its only child
		    body.addBodyElement(isbnQName).setValue("12345");
		    //debug print what we're sending，  将SOAP信封的内容打印到输出。
		    soapMsg.writeTo(out);
		    
		    out.println("\nInvoking...");
		    //send the message as request to service and get response
		    SOAPMessage response = dispatch.invoke(soapMsg);

		    //just show in the console for now, 将SOAP信封的内容打印到输出。
		    response.writeTo(System.out);

		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	}
	
	/**
	 * 示例5.4 
	 */
	@Test
	public void dispatchMsgIsbnTest() {
		try {
			URL wsdl = new URL(HTTP_WSDL_PATH);
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

			String dataFile = this.getClass().getResource("").getPath() + "isbnMsg.txt";
			System.out.println("dataFile:" + dataFile);
			// read in the data to use in building the soap message from a file
			FileInputStream fis = new FileInputStream(dataFile);

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
	
	public void toDom(SOAPMessage message) throws SOAPException{
		Document doc = message.getSOAPBody().extractContentAsDocument();
		NodeList nodeList = doc.getChildNodes();
	}
	
}
