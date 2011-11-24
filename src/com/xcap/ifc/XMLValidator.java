package com.xcap.ifc;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Scanner;

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
	 * @param xmlFilePath  xml text String.
	 * @param schemaFilePath
	 * @return 0 Ok, 1 XML document structures, 2 schema validate error.
	 * @throws SAXException
	 * @throws IOException
	 */
	public static int xmlValidator(String xml, String schemaFilePath) throws IOException {
		StringReader xmlStringReader = new StringReader(xml);

		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(true);
		XMLReader xmlReader;
		try {
			xmlReader = spf.newSAXParser().getXMLReader();
			xmlReader.parse(new InputSource(xmlStringReader));
		} catch (Exception e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			return RESULT_STRUCTURE_ERROR;
		}
		
		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

		Source source = new StreamSource(new StringReader(xml));
		 
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
	
	public static int xmlFileValidator(String xmlFilePath, String schemaFilePath) throws Exception{
		File file = new File(xmlFilePath);
		Scanner scanner = new Scanner(new FileReader(file));
		StringBuilder builder = new StringBuilder();
		while(scanner.hasNextLine()){
			builder.append(scanner.nextLine());
		}
		
		return xmlValidator(builder.toString(), schemaFilePath);
	}
}
