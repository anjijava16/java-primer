package http.contacts;


import java.io.File;

import org.junit.Test;


public class TestPut extends TestBase{
	final static String phoneNo = "8613480783139"; 
	final static String token = "DDcs3x7JwQQwqvOT751dhxPUTartlMV70DRk28fiJjRSwAeSqwV0bw**";

	@Test
	public void putDocument(){
		String url = constructUrl(phoneNo, token);
		try {
			File file = getXmlFilePath("xml/example-new-contacts.xml");
			putReqClient(url,file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	@Test
	public void putContactNodeByIndex(){
		String url = constructUrl(phoneNo, token);
		int index = 1; // example
		String nodeSelector = constructSelectorByIndex(index);
		url = url.concat(nodeSelector);
		try {
			File file = getXmlFilePath("xml/example-new-contact.xml");
			putReqClient(url,file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	@Test
	public void putContactNodeByTagName(){
		String url = constructUrl(phoneNo, token);
		String nodeSelector = constructSelectorByTagName();
		url = url.concat(nodeSelector);
		try {
			File file = getXmlFilePath("xml/example-new-contact.xml");
			putReqClient(url,file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void putContactNodeByUniqueAttr(){
		String url = constructUrl(phoneNo, token);
		String method = "9999939999";
		String nodeSelector = constructSelectorByUniqueAttr(method);
		url = url.concat(nodeSelector);
		try {
			File file = getXmlFilePath("xml/example-new-contact.xml");
			putReqClient(url,file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void putAttr(){		
		//six case.
		String url = constructUrl(phoneNo, token);
		String method = "9999939999";
		String nodeSelector = construct_A_T_Leaf(method, TageName.contactName);
		url = url.concat(nodeSelector);
		try {
			File file = getXmlFilePath("xml/example-update-contactName.xml");
			putReqClient(url,file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
