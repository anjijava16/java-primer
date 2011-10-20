package com.xml;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * 判断xml 中是否有书写错误
 * @author me
 *
 */
public class XmlCheck {
	static String xmlFile = "JavaSE/com/xml/book.xml";

	static String badXmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" 
		+ "<XMLDataRoot name=\"百度文库\" type=\"computer\">" 
		+ "<title>XML轻松学习手册</title>"
		+ "<author>张三</author>" 
		+ "<email>slieer@gmail.com</email>";// +
	
	static String goodXmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" 
		+ "<XMLDataRoot name=\"百度文库\" type=\"computer\">" 
		+ 	"<title>XML轻松学习手册</title>"
		+ 	"<author>张三</author>" 
		+ 	"<email>slieer@gmail.com</email>"
		+ 	"<date>2011-9-20</date>" 
		+ "</XMLDataRoot>";
	

	public static void main(String[] args) {
		dom();

	}

	private static void sax() {
		
	}
	
	private static void jdom(){
		
	}
	
	private static void dom() {
		DocumentBuilder parser;
		try {
			parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			// Document document = parser.parse(new File(xmlFile));
			Document document = parser.parse(new InputSource(new StringReader(goodXmlString)));
			
			System.out.println("------------------");
			Document document1 = parser.parse(new InputSource(new StringReader(badXmlString)));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
