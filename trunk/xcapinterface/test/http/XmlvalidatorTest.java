package http;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.xcap.ifc.XMLValidator;

public class XmlvalidatorTest {

	@Test
	public void contactXmlParser() {
		String filePath = "test/http/xml/example-new-contact.xml";
		Element e = getDocElement(filePath);
		
		String attr = e.getAttribute("method");
		System.out.println(attr);

		NodeList nodeList = e.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (!node.getNodeName().equals("#text")) {
				NodeList nodeList2 = node.getChildNodes();
				System.out.println(node.getNodeName()
						+ ":"
						+ (nodeList2.getLength() > 0 ? nodeList2.item(0)
								.getNodeValue() : null));
			}
		}
	}

	@Test
	public void leafNodeParser() {
		String xml = "test/http/xml/example-update-contactName.xml";
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true); // never forget this!
		DocumentBuilder builder;

		Element e = null;
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(xml);
			//System.out.println(doc.getNodeValue());
			
			NodeList list1 = doc.getElementsByTagName("contactName");
			Node node1 = list1.item(0);
			
			System.out.println(node1.getFirstChild().getNodeValue());
			System.out.println(node1.getNodeName());
			
			Element element = doc.getDocumentElement();
			NodeList list = element.getElementsByTagName("contactName");
			Node node = list.item(0);
			System.out.println(node.getNodeName());
		}catch (Exception e1) {
		}
	}

	@Test
	public void docXmlValidate() {
		String xml = "test/http/xml/example-new-contacts.xml";
		xmlValidate(xml);
	}

	@Test
	public void nodeXmlValidate() {
		String xml = "test/http/xml/example-new-contact.xml";
		xmlValidate(xml);
	}

	@Test
	public void leafNodeXmlValidate() {
		String xml = "test/http/xml/example-update-contactName.xml";
		xmlValidate(xml);
	}

	@Test
	public void badNodeXmlValidate() {
		String xml = "test/http/xml/bad-example-new-contact.xml";
		xmlValidate(xml);
	}

	private void xmlValidate(String xml) {
		String schemaFilePath = "test/http/xml/contacts.xsd";
		try {
			int re = XMLValidator.xmlFileValidator(xml, schemaFilePath);
			System.out.println(re);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Element getDocElement(String filePath)
			throws FactoryConfigurationError {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true); // never forget this!
		DocumentBuilder builder;

		Element e = null;
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(filePath);
			e = doc.getDocumentElement();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return e;
	}
	
}
