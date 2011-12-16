package com.xml.stax;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.junit.Test;

public class StaxTest {
	@Test
	public void staxTest() throws Exception {
		URL url = this.getClass().getResource("/");
		String xmlFile = url.getPath() + "com/xml/unformattedXML.xml";
		File file = new File(xmlFile);

		// Reader stream = new FileReader(file);  //error
		InputStream stream = new FileInputStream(file);
		XMLEventReader xsr = XMLInputFactory.newInstance()
				.createXMLEventReader(stream);

		while (xsr.hasNext()) {
			XMLEvent evt = xsr.nextEvent();
			switch (evt.getEventType()) {
			case XMLEvent.START_ELEMENT: {
				StartElement s = evt.asStartElement();
				System.out.print(s.toString());
				break;
			}
			case XMLEvent.END_ELEMENT :{
				EndElement e = evt.asEndElement();
				System.out.println(e);
				break;
			}
			case XMLEvent.CHARACTERS :{
				Characters se = evt.asCharacters();
				System.out.print(se.toString());
				break;
			}
			}
		}
	}
	
	/**
	 * http://java.sun.com/webservices/reference/tutorials/jaxp/html/stax.html
	 * @throws Exception
	 */
	public void staxStream() throws Exception{
		XMLOutputFactory output = XMLOutputFactory.newInstance();
		XMLStreamWriter writer = output.createXMLStreamWriter(System.out);
		
	}
	
}
