package http.singcontacts;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

//import com.borqs.mspaces.contact.ifc.Contact;

public class TestSingPut extends TestBase{
	final static String phoneNo = "8613480783139"; 
	final static String token = "DDcs3x7JwQQwqvOT751dhxPUTartlMV70DRk28fiJjRSwAeSqwV0bw**";

	final static String url = constructUrl(phoneNo, token);
	
	File contactsFile = getXmlFilePath("example-new-contacts.xml");
	File contactFile = getXmlFilePath("example-new-contact.xml");
	File contactFile_1 = getXmlFilePath("example-new-contact-2nd.xml");

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
		String contactId = "147"; //add
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
		
		String xml = "<dispName>xxx</dispName>";
		try {
			putReqClient(reqUrl, xml);
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
			putReqClient(reqUrl, xml);
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
		
		String xml = "<name>" 
						   + "	<fn>da</fn>"
						   + "	<ln>hai</ln>"
						   + "</name>";
		try {
			putReqClient(url1, xml);
			putReqClient(url2, xml);
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

		String xml = "<name>" 
			   + "	<fn>da</fn>"
			   + "	<ln>hai</ln>"
			   + "</name>";
		try {
			putReqClient(url1, xml);
			putReqClient(url2, xml);
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
		
		String email =  "<email>			                      " +
						"	<item type=\"\">ax@g.com</item>       " +
						"	<item type=\"PREF\">by@h.com</item>   " +
						"	<item type=\"HOME\">cz@mail.com</item>" +
						"	<item type=\"WORK\">da@l.com</item>   " +
						"</email>                                 ";
		try {
			putReqClient(url1, email);
			putReqClient(url2, email);
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

		String email =  "<email>			                      " +
						"	<item type=\"\">ax@g.com</item>       " +
						"	<item type=\"PREF\">by@h.com</item>   " +
						"	<item type=\"HOME\">cz@mail.com</item>" +
						"	<item type=\"WORK\">da@l.com</item>   " +
						"</email>                                 ";
		
		try {
			putReqClient(url1, email);
			putReqClient(url2, email);
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
		xml = "<url>" +
				"<item type=\"hr\">http://hi.baidu.com/slieer/home</item>" +
				"<item type=\"\">http://oschina.net/slieer</item>" +
				"<item type=\"OT\">http://code.google.com/slieer</item>" +
				"</url>";  //set tel field is null
		try {
			putReqClient(reqUrl, xml);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
		
	@Test
	public void putFN(){
		String byIndex = LEFT_SQUARE_BRACKET.concat("1").concat(RIGHT_SQUARE_BRACKET);
		String reqUrl = url.concat("/~~/contacts/contact" + byIndex + "/name/fn");

		String nameCentent = "<fn>da</fn>";
		try {
			putReqClient(reqUrl, nameCentent);
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
		
		String telItem = "<item type=\"home\">0916-8452-426</item>";
		try {
			putReqClient(url2, telItem);
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
		
		String xml = "<item type=\"home\">0916-8452</item>";
		try {
			putReqClient(url2, xml);
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

		String xml = "<item type=\"home\">0916-8452-576</item>";		
		try {
			putReqClient(url2, xml);
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
			putReqClient(reqUrl, xml0);
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
			putReqClient(reqUrl, xml);
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
			putReqClient(reqUrl, xml1);
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
			
			InputSource telItemFile = new InputSource("<item type=\"first\">8888888</item>"); 
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
			InputSource nameFnFile = new InputSource("<fn>da</fn>"); 
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
}
