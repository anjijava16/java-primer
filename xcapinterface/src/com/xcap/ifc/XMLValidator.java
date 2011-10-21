package com.xcap.ifc;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class XMLValidator {
	private final static Logger log = Logger.getLogger(XMLValidator.class);
	
	public static final int RESULT_OK = 0;
	public static final int RESULT_STRUCTURE_ERROR = 1;
	public static final int RESULT_SCHEMA_VALIDATE_ERROR = 2;
	
	/**
	 * 
	 * @param xmlFilePath
	 * @param schemaFilePath
	 * @param xmlFileOrXmlText
	 *            true xml file; false xml text.
	 * @return 0 Ok, 1 XML document structures, 2 schema validate error.
	 * @throws SAXException
	 * @throws IOException
	 */
	public static int xmlValidator(String xml, String schemaFilePath,
			boolean xmlFileOrXmlText) throws IOException {
		StringReader xmlStringReader = new StringReader(xml);

		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(true);
		XMLReader xmlReader;
		try {
			xmlReader = spf.newSAXParser().getXMLReader();
			xmlReader.parse(new InputSource(xmlStringReader));
		} catch (Exception e1) {
			log.error(e1.getMessage());
			return RESULT_STRUCTURE_ERROR;
		}
		
		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

		Source source = null;
		if (!xmlFileOrXmlText) {
			source = new StreamSource(new StringReader(xml));
		} else {
			source = new StreamSource(xml);
		}
		
		try {
			File schemaLocation = new File(schemaFilePath);
			Schema schema = factory.newSchema(schemaLocation);
			Validator validator = schema.newValidator();
			validator.validate(source);
			return RESULT_OK;
		} catch (SAXException ex) {

			System.out.println(ex.getMessage());
			return RESULT_SCHEMA_VALIDATE_ERROR;
		}
	}
	
	public static void main(String[] args) {
		String xml = "E:/jboss-5.1.0.GA-jdk6/jboss-5.1.0.GA/server/default/tmp/4sg02c-mqxk54-gtxn6gvd-1-gty56r37-a5/xcap-root.war/WEB-INF/classes/com/xcap/web/xmlschema/contacts.xsd";
		String xsd = "src/doc/contact-list-1.xsd";

		try {
			int result = xmlValidator(xml, xsd, true);
			System.out.println(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}
