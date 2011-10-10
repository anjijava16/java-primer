package com.xcap.ifc;

import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.borqs.contact.ifc.Contact;

public class XMLValidator {

	public static void main(String[] args) {
		String xml = "src/doc/example.xml";
		String xsd = "src/doc/contact-list-1.xsd";

		try {
			boolean re = xmlValidator(xml, xsd);
			System.out.println(re);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean xmlValidator(String xmlFilePath, String schemaFilePath)
			throws SAXException, IOException {
		// 1. Lookup a factory for the W3C XML Schema language
		SchemaFactory factory = SchemaFactory
				.newInstance("http://www.w3.org/2001/XMLSchema");

		// 2. Compile the schema.
		// Here the schema is loaded from a java.io.File, but you could use
		// a java.net.URL or a javax.xml.transform.Source instead.
		File schemaLocation = new File(schemaFilePath);
		Schema schema = factory.newSchema(schemaLocation);

		// 3. Get a validator from the schema.
		Validator validator = schema.newValidator();

		// 4. Parse the document you want to check.
		Source source = new StreamSource(xmlFilePath);

		// 5. Check the document
		try {
			validator.validate(source);
			return true;
		} catch (SAXException ex) {

			System.out.println(ex.getMessage());
			return false;
		}
	}
}
