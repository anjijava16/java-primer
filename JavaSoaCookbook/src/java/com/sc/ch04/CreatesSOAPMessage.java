package com.sc.ch04;

import java.io.IOException;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.junit.Test;

/**
 * Creates a SOAP Message and writes it to the console.
 */
public class CreatesSOAPMessage {
	
	@Test
	public void printSOAPMessage() {
		try {
			SOAPMessage soapMsg = MessageFactory.newInstance().createMessage();

			// Get the body from the envelope
			SOAPPart soapPart = soapMsg.getSOAPPart();
			SOAPEnvelope env = soapPart.getEnvelope();
			SOAPBody body = env.getBody();

			// Create a qualified name for the namespace of the
			// objects used by the service.
			String iNs = "http://ns.soacookbook.com/catalog";
			String elementName = "isbn";
			QName isbnQName = new QName(iNs, elementName);

			// Add the <isbn> element to the SOAP body
			// as its only child
			body.addBodyElement(isbnQName).setValue("12345");

			// debug print what we're sending
			soapMsg.writeTo(System.out);
		} catch (SOAPException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
