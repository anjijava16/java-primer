package com.sc.ch02.stax;

import static java.lang.System.out;

import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class UpdateXMLValue {

	public static void main(String[] args) throws Exception {
		String xmlSource = "src/xml/ch02/Catalog.xml";

		// find the book titled 'Hamlet' and select its price.
		String xpath = "/catalog/book[title='Hamlet']/price";

		// this is the new price
		String value = "8.95";

		// we're throwing any exception out
		updateValueInXmlFile(xmlSource, xmlSource, xpath, value);

		out.println("All done.");
	}

	public static void updateValueInXmlFile(String fileIn, String fileOut,
			String xpathExpression, String newValue) throws IOException {

		// Set up the DOM evaluator
		final DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();

		try {
			final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			final Document doc = docBuilder.parse(fileIn);

			final XPath xpath = XPathFactory.newInstance().newXPath();
			NodeList nodes = (NodeList) xpath.evaluate(xpathExpression, doc,
					XPathConstants.NODESET);

			// Update the nodes we found
			for (int i = 0, len = nodes.getLength(); i < len; i++) {
				Node node = nodes.item(i);
				node.setTextContent(newValue);
			}

			// Get file ready to write
			final Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

			StreamResult result = new StreamResult(new FileWriter(fileOut));
			transformer.transform(new DOMSource(doc), result);

			// Write file out
			result.getWriter().flush();
			result.getWriter().close();

		} catch (XPathExpressionException xpee) {
			out.println(xpee);
			throw new IOException("Cannot parse XPath.", xpee);
		} catch (DOMException dome) {
			out.println(dome);
			throw new IOException("Cannot create DOM tree", dome);
		} catch (TransformerConfigurationException tce) {
			out.println(tce);
			throw new IOException("Cannot create transformer.", tce);
		} catch (IllegalArgumentException iae) {
			out.println(iae);
			throw new IOException("Illegal Argument.", iae);
		} catch (ParserConfigurationException pce) {
			out.println(pce);
			throw new IOException("Cannot create parser.", pce);
		} catch (SAXException saxe) {
			out.println(saxe);
			throw new IOException("Error reading XML document.", saxe);
		} catch (TransformerFactoryConfigurationError tfce) {
			out.println(tfce);
			throw new IOException("Cannot create trx factory.", tfce);
		} catch (TransformerException te) {
			out.println(te);
			throw new IOException("Cannot write values.", te);
		}
	}
}
