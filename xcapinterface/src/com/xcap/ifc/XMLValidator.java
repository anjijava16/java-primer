package com.xcap.ifc;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

public class XMLValidator {

	/**
	 * 
	 * @param xmlFilePath
	 * @param schemaFilePath
	 * @param xmlFileOrXmlText
	 *            true xml file; false xml text.
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 */
	public static boolean xmlValidator(String xml, String schemaFilePath,
			boolean xmlFileOrXmlText) throws SAXException, IOException {
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

		// 4. Parse the document you want to check./
		Source source = null;
		if (!xmlFileOrXmlText) {
			source = new StreamSource(new StringReader(xml));
		} else {
			source = new StreamSource(xml);
		}

		// 5. Check the document
		try {
			validator.validate(source);
			return true;
		} catch (SAXException ex) {

			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	public static void main(String[] args) {
		String xml = "E:/jboss-5.1.0.GA-jdk6/jboss-5.1.0.GA/server/default/tmp/4sg02c-mqxk54-gtxn6gvd-1-gty56r37-a5/xcap-root.war/WEB-INF/classes/com/xcap/web/xmlschema/contacts.xsd";
		String xsd = "src/doc/contact-list-1.xsd";

		try {
			boolean re = xmlValidator(xml, xsd, true);
			System.out.println(re);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}
