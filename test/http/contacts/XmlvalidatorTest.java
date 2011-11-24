package http.contacts;

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
	public static final String XML_DIR = "test/http/contacts/xml/";
	public static String getFilePath(String fileName){
		return XML_DIR.concat(fileName);
	}
	
	@Test
	public void contactsXmlParser() {
		Element e = getDocElement( getFilePath("example-new-contacts.xml"));
		
		//NodeList nodeList = e.getChildNodes();
		NodeList nodeList = e.getElementsByTagName("contact");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeName().equals("contact")) {
				NodeList nodeList2 = node.getChildNodes();
				//System.out.println("-----------");
				String method = node.getAttributes().getNamedItem("method").getNodeValue();
				System.out.println(method);
				for(int j = 0; j < nodeList2.getLength(); j++){
					Node contactNode = nodeList2.item(j);
					//System.out.println(contactNode.getNodeName());
					NodeList leafList = contactNode.getChildNodes();
					if(leafList.item(0) != null){
						System.out.println(leafList.item(0).getNodeValue());						
					}
				}
			}
		}
	}
	
	@Test
	public void contactXmlParser() {
		String fileName = "example-new-contact.xml";
		String filePath = getFilePath(fileName);
		parsercontactsXml(filePath);
	}

	private void parsercontactsXml(String filePath)
			throws FactoryConfigurationError {
		Element e = getDocElement(filePath);
		
		String attr = e.getAttribute("method");
		System.out.println("uniqueAttr-->method:" + attr);

		NodeList nodeList = e.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			System.out.println(node.getNodeName());
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
		String fileName = "example-update-contactName.xml";
		String xmlFilePath = getFilePath(fileName);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true); // never forget this!
		DocumentBuilder builder;

		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(xmlFilePath);
			
			NodeList list1 = doc.getElementsByTagName("contactName");
			Node node1 = list1.item(0);
			
			System.out.println(node1.getNodeName() + ":" + node1.getFirstChild().getNodeValue());
		}catch (Exception e1) {
		}
	}

	@Test
	public void docXmlValidate() {
		String xml = getFilePath("example-new-contacts.xml");
		xmlValidate(xml);
	}

	@Test
	public void nodeXmlValidate() {
		String xml = getFilePath("example-new-contact.xml");
		xmlValidate(xml);
	}

	@Test
	public void leafNodeXmlValidate() {
		String xml = getFilePath("example-update-contactName.xml");
		xmlValidate(xml);
	}

	@Test
	public void badNodeXmlValidate() {
		String xml = getFilePath("bad-example-new-contact.xml");
		xmlValidate(xml);
	}

	private void xmlValidate(String xml) {
		String schemaFilePath = getFilePath("contacts.xsd");
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
