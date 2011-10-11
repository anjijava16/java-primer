package com.xcap.ifc;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

public class XMLValidator {

	public static void main(String[] args) {
		String xml = "src/doc/example.xml";
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

		// 4. Parse the document you want to check.\
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

	public static boolean xmlValidator(Reader reader, String schemaFilePath)
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

		// 4. Parse the document you want to check.\
		Source source = null;
		source = new StreamSource(reader);

		// 5. Check the document
		try {
			validator.validate(source);   //no error is OK
			return true;
		} catch (SAXException ex) {

			System.out.println(ex.getMessage());
			return false;
		}
	}

}
