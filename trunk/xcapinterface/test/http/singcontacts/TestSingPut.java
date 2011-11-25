package http.singcontacts;

import java.io.ByteArrayInputStream;
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

import com.borqs.mspaces.contact.ifc.Contact;

public class TestSingPut extends TestBase{
	final static String phoneNo = "8613480783139"; 
	final static String token = "DDcs3x7JwQQwqvOT751dhxPUTartlMV70DRk28fiJjRSwAeSqwV0bw**";

	final static String url = constructUrl(phoneNo, token);
	
	File contactsFile = getXmlFilePath("example-new-contacts.xml");
	File contactFile = getXmlFilePath("example-new-contact.xml");
	File contactFile_1 = getXmlFilePath("example-new-contact-2nd.xml");
	
	File nameFile = getXmlFilePath("example-new-name.xml");
	File dispNameFile = getXmlFilePath("example-new-dispName.xml");
	
	File telFile = getXmlFilePath("example-new-tel.xml");
	File telItemFile = getXmlFilePath("example-new-tel-item.xml");
	
	File emailFile = getXmlFilePath("example-new-email.xml");
	File nameFnFile = getXmlFilePath("example-new-name-fn.xml");
	/**
	 * put contacts
	 */
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
	
	/**
	 * put contact node by uniqueAttr selector.
	 */
	@Test
	public void putContactByAttrSelector(){
		String contactId = "1"; //add
		//String contactId = "";
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
	
	/**
	 * put contact node by index selector.
	 */
	@Test
	public void putContactByIndexSelector(){
		int index = 2;
		String u = url.concat(constructSecondLayerSelectorByIndex(index));
		try {
			putReqClient(u, contactFile_1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * put contact node by tag name selector.
	 */
	@Test
	public void putContactByTagNameSelector(){
		String u = url.concat("/~~/conatcts/contact");
		try {
			putReqClient(u, contactFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * put dispName node
	 * 2nd layer selector : index selector. 
	 * 3rd layer selector : tag name.
	 */
	@Test
	public void putDispName(){
		String byIndex = LEFT_SQUARE_BRACKET.concat("1").concat(RIGHT_SQUARE_BRACKET);
		String reqUrl = url.concat("/~~/contacts/contact" + byIndex + "/dispName");
		try {
			putReqClient(reqUrl, dispNameFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * put title node
	 * 2nd selector : index,
	 * 3rd selector : tag name.
	 */
	@Test
	public void putTitle_i_t(){
		String contactIndex = "1";
		String byIndex = LEFT_SQUARE_BRACKET.concat(contactIndex).concat(RIGHT_SQUARE_BRACKET);
		String reqUrl = url.concat("/~~/contacts/contact" + byIndex + "/title");
		
		String xml = "<title>大元帅（generallissimo）</title>";
		xml = "<title></title>";  //set conatct field is null
		try {
			putReqClient(reqUrl, new ByteArrayInputStream(xml.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
//---------------------------------------------------------------------------------	
	/**
	 * put name node.
	 * 2nd selector: uniqueAttr/index
	 * 3rd selector: tag name
	 */
	@Test
	public void putNameNameByTagName(){
		int index = 1;
		String contactId = "73995";
		String url1 = url.concat(constructSecondLayerSelectorByIndex(index)).concat("/name");
		String url2 = url.concat(constructSecondLayerSelectorByUniqueAttr(contactId)).concat("/name");

		try {
			putReqClient(url1, nameFile);
			putReqClient(url2, nameFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	/**
	 * put name node.
	 * 2nd selector : uniqueAttr/index
	 * 3rd : index
	 * 
	 */
	@Test
	public void putNameByTagIndex(){
		int index = 1;
		String contactId = "73998";
		String byIndex = LEFT_SQUARE_BRACKET.concat("1").concat(RIGHT_SQUARE_BRACKET);
		
		String url1 = url.concat(constructSecondLayerSelectorByIndex(index))
			.concat("/name").concat(byIndex);   //by index
		String url2 = url.concat(constructSecondLayerSelectorByUniqueAttr(contactId))
			.concat("/name").concat(byIndex);   //by attr
		try {
			putReqClient(url1, nameFile);
			putReqClient(url2, nameFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * put email node.
	 * 2nd selector : uniqueAttr/index
	 * 3rd : tag name
	 */
	@Test
	public void putEmailByTagName(){
		int index = 1;
		String contactId = "74004";
		
		String url1 = url.concat(constructSecondLayerSelectorByIndex(index)).concat("/email");
		String url2 = url.concat(constructSecondLayerSelectorByUniqueAttr(contactId)).concat("/email");
		try {
			putReqClient(url1, emailFile);
			putReqClient(url2, emailFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * put email node.
	 * 2nd selector : uniqueAttr/index
	 * 3rd : index
	 */
	@Test
	public void putEmailByIndex(){
		int index = 1;
		String contactId = "73998";
				
		String byIndex = LEFT_SQUARE_BRACKET.concat("1").concat(RIGHT_SQUARE_BRACKET);

		String url1 = url.concat(constructSecondLayerSelectorByIndex(index)).concat("/email").concat(byIndex);
		String url2 = url.concat(constructSecondLayerSelectorByUniqueAttr(contactId)).concat("/email").concat(byIndex);
		try {
			putReqClient(url1, emailFile);
			putReqClient(url2, emailFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * put url node
	 */
	@Test
	public void putUrl(){
		int index = 1;
		String reqUrl = url.concat(constructSecondLayerSelectorByIndex(index)).concat("/url");
		String xml = null;
		xml = "<url><item type=\"hr\">http://hi.baidu.com/slieer/home</item><item type=\"\">http://oschina.net/slieer</item><item type=\"OT\">http://code.google.com/slieer</item></url>";  //set tel field is null
		try {
			putReqClient(reqUrl, new ByteArrayInputStream(xml.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
		
	@Test
	public void putFN(){
		String byIndex = LEFT_SQUARE_BRACKET.concat("1").concat(RIGHT_SQUARE_BRACKET);
		String reqUrl = url.concat("/~~/contacts/contact" + byIndex + "/name/fn");
		try {
			putReqClient(reqUrl, nameFnFile);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * put tel item 
	 */
	@Test
	public void putItemByTagName(){
		String contactId = "74003";
		String url2 = url.concat(constructSecondLayerSelectorByUniqueAttr(contactId)).concat("/tel/item");
		
		try {
			putReqClient(url2, telItemFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * put tel item 
	 */
	@Test
	public void putItemByIndex(){
		String contactId = "74003";
		String itemIndex = "2";
		String byIndex = LEFT_SQUARE_BRACKET.concat(itemIndex).concat(RIGHT_SQUARE_BRACKET);
		String url2 = url.concat(constructSecondLayerSelectorByUniqueAttr(contactId)).concat("/tel/item").concat(byIndex);
		
		try {
			putReqClient(url2, telItemFile);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	/**
	 * put tel item 
	 */
	@Test
	public void putItemByUniqueAttr(){
		String contactId = "74003";
		String url2 = url
				.concat(constructSecondLayerSelectorByUniqueAttr(contactId))
				.concat("/tel/item")
				.concat(LEFT_SQUARE_BRACKET + AT + "type=" + DOUBLE_QUOTATION_MARKS +  "work" + DOUBLE_QUOTATION_MARKS + RIGHT_SQUARE_BRACKET);

		try {
			putReqClient(url2, telItemFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * put tel node to null.
	 */
	@Test
	public void putTelByIndex(){
		int index = 1;
		String reqUrl = url.concat(constructSecondLayerSelectorByIndex(index)).concat("/tel");
		
		String xml0 = "<tel></tel>";  //set tel field is null
		String xml1 = "<tel><item></item></tel>";  //set tel field is null
		String xml2 = "<tel><item type=\"first\">8888888</item></tel>";
		String xml3 = "<tel><item>8888888</item></tel>";
		try {
			putReqClient(reqUrl, new ByteArrayInputStream(xml1.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * put tel node to null.
	 */
	@Test
	public void putEmail(){
		int index = 1;
		String reqUrl = url.concat(constructSecondLayerSelectorByIndex(index)).concat("/email");
		String xml = null;
		xml = "<email/>";  //set tel field is null
		try {
			putReqClient(reqUrl, new ByteArrayInputStream(xml.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	/**
	 * put item node to null
	 */
	public void putTelItem(){
		int index = 1;
		String reqUrl = url.concat(constructSecondLayerSelectorByIndex(index)).concat("/tel");
		
		String xml1 = "<item></item>";  //set tel field is null
		String xml2 = "<tel><item type=\"first\">8888888</item></tel>";
		String xml3 = "<tel><item>8888888</item></tel>";
		try {
			putReqClient(reqUrl, new ByteArrayInputStream(xml1.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**----------------------------------------------------------------------------------
	 *   other test,no matter with xcap test.
	 */
	
	@Ignore
	@Test
	public void testItemPut(){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setNamespaceAware(true);
	    
	    Document doc = null;
	    Element element = null;
	    String topTagName = null;
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			//doc = builder.parse(contactsFile);	
			doc = builder.parse(telItemFile);
			
			element = doc.getDocumentElement();
			topTagName = element.getTagName();		
		}catch (Exception e) {
			e.printStackTrace();
		}
		String type = element.getAttribute("type");
		Node n = element.getFirstChild();
		String value = n.getNodeValue();
		System.out.println("type,value:" + type + " " + value);
	}
		
	@Ignore
	@Test
	public void testContactPut(){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setNamespaceAware(true);
	    
	    Document doc = null;
	    Element element = null;
	    String topTagName = null;
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			//doc = builder.parse(contactsFile);	
			doc = builder.parse(nameFnFile);
			
			element = doc.getDocumentElement();
			topTagName = element.getTagName();		
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		Node n = element.getFirstChild();
		System.out.println(n.getNodeValue());
		
		//NodeList list = element.getElementsByTagName("contact");
		//NodeList lis = doc.getElementsByTagName("contact");
		//System.out.println(lis.getLength());
	}
	
	/**
	 * 在jboss 5.1下 ,put 操作有错。
	 */
	@Ignore
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
			
			List<Contact> list = new ArrayList<Contact>();
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				NodeList children = node.getChildNodes();
				
				Contact entity = new Contact();
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
