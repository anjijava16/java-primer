package com.sc.ch02.stax;

import static java.lang.System.out;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 * Accepts an XPath expression to perform searching against the Catalog.xml
 * document.
 */
public class BasicXPath {

	public static void main(String... args) throws Exception {
		String xmlSource = "src/xml/ch02/Catalog.xml";

		// Get all titles with price between $5 and $9.99
		String xpath = "//book[price > 5.00 and price < 9.99]/title";
		/*
		 * Prints: Value=King Lear Value=Hamlet
		 */

		search(xmlSource, xpath);
	}

	public static void search(String fileIn, String xpathExp)
			throws IOException {

		// Set up the DOM parser
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();

		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(fileIn);
			XPath xpath = XPathFactory.newInstance().newXPath();

			// Evaluate XPath expression against parsed document
			NodeList nodes = (NodeList) xpath.evaluate(xpathExp, doc,
					XPathConstants.NODESET);

			// We could return these instead to let caller deal
			for (int i = 0, len = nodes.getLength(); i < len; i++) {
				Node node = nodes.item(i);

				String value = node.getTextContent();

				out.println("Value=" + value);
			}

		} catch (XPathExpressionException xpee) {
			out.println(xpee);
			throw new IOException("Cannot parse XPath.", xpee);
		} catch (DOMException dome) {
			out.println(dome);
			throw new IOException("Cannot create DOM tree", dome);
		} catch (ParserConfigurationException pce) {
			out.println(pce);
			throw new IOException("Cannot create parser.", pce);
		} catch (SAXException saxe) {
			out.println(saxe);
			throw new IOException("Error parsing XML document.", saxe);
		}
	}
}
