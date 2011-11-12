package http.singcontacts;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.xcap.dao.entity.SingSpacesContactEntity;

public class TestSingPut extends TestBase{
	final static String phoneNo = "8613480783139"; 
	final static String token = "DDcs3x7JwQQwqvOT751dhxPUTartlMV70DRk28fiJjRSwAeSqwV0bw**";

	final static String url = constructUrl(phoneNo, token);
	
	File contactsFile = getXmlFilePath("example-new-contacts.xml");
	File contactFile = getXmlFilePath("example-new-contact.xml");

	@Test
	public void putContactsDocument(){
		if(contactsFile.exists()){
			try {
				putReqClient(url, contactsFile);
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}else{
			System.out.println("file not find.");
		}
	}
	
	@Test
	public void putContactByAttrSelector(){
		String contactId = "1";
		String u = url.concat(constructSecondLayerSelectorByUniqueAttr(contactId));
		if(contactFile.exists()){
			try {
				putReqClient(u, contactFile);
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}else{
			System.out.println("file not find.");
		}		
	}
	
	@Test
	public void putContactByIndexSelector(){
		
	}
	
	@Test
	public void putContactByTagNameSelector(){
		
	}
	
	@Test
	public void putNameByTagName(){
		
	}
	
	@Test
	public void putNameByAttr(){
		
	}

	
	@Test
	public void putNameByTagIndex(){
		
	}
	
	
	/**
	 * 在jboss 5.1下 ,put 操作有错。
	 */
	@Test
	public void testHttpPut(){
		String url = "http://localhost:8080/xcap-root/http-put-test";
		
		try {
			putReqClient(url, contactsFile);
			//putReqClient(url, contactFile);
			//File txtFile =  getXmlFilePath("http-put-test.xml");
			//putReqClient(url, txtFile);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Ignore
	@Test
	public void testContactsXmlParser(){
		File xmlFile = getXmlFilePath("example-new-contacts.xml");
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setNamespaceAware(true);
	    
	    Document doc = null;
	    Element element = null;
	    String topTagName = null;
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(xmlFile);	
			element = doc.getDocumentElement();
			topTagName = element.getTagName();		

			//System.out.println("topTagName:" + topTagName);
			NodeList nodes = element.getElementsByTagName("contact");
			
			List<SingSpacesContactEntity> list = new ArrayList<SingSpacesContactEntity>();
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				NodeList children = node.getChildNodes();
				
				SingSpacesContactEntity entity = new SingSpacesContactEntity();
				list.add(entity);
				
				for(int j = 0; j < children.getLength(); j++){
					Node ch = children.item(j);
					String nodeName = ch.getNodeName();
					
					if(! nodeName.equals(XML_TEXT_TAGNAME)){
						//System.out.println("--" + ch);
						
						Node node1 = null;
						String nodeValue = null;
						if(ch.getChildNodes().getLength() == 1){
							node1 = ch.getChildNodes().item(0);
							nodeValue = node1.getNodeValue();
							
						}else if(ch.getChildNodes().getLength() > 1){
							NodeList chs = ch.getChildNodes();
							node1 = ch;//.item(1);
							//NodeList itemList = chs.item(1).getChildNodes();
							
							StringBuilder multiValue = new StringBuilder();
							for(int k = 0; k < chs.getLength(); k++){
								Node itemNode = chs.item(k);
								if(! itemNode.getNodeName().equals("#text")){
									String value = itemNode.getNodeValue();
									String type = "";
									NamedNodeMap attrsNode = itemNode.getAttributes();
									if(attrsNode != null){
										Node attr = attrsNode.getNamedItem("type");
										if(attr != null)
											type = attr.getNodeValue();
									}
									System.out.println("type:" + type + " value:" + value);
									//multiValue.append(type.concat(":").concat(value).concat(";"));
									
									//nodeValue = multiValue.toString();
								}
							}
						}
						if(node1 != null){
							System.out.println(nodeName + ":" + node1.getNodeName() + ":" + nodeValue);							
						}else{
							System.out.println(nodeName + ": is null");
						}
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
