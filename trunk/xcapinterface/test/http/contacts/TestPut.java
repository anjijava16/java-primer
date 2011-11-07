package http.contacts;


import java.io.File;

import org.junit.Test;


public class TestPut extends TestBase{
	final static String phoneNo = "8613480783139"; 
	final static String token = "DDcs3x7JwQQwqvOT751dhxPUTartlMV70DRk28fiJjRSwAeSqwV0bw**";

	final String url = constructUrl(phoneNo, token);

	@Test
	public void putDocument(){
		try {
			File file = getXmlFilePath("xml/example-new-contacts.xml");
			putReqClient(url,file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	@Test
	public void putContactNodeByIndex(){
		int index = 1; // example
		String nodeSelector = constructSelectorByIndex(index);
		String u = url.concat(nodeSelector);
		try {
			File file = getXmlFilePath("xml/example-new-contact.xml");
			putReqClient(u,file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	@Test
	public void putContactNodeByTagName(){
		String nodeSelector = constructSelectorByTagName();
		String u = url.concat(nodeSelector);
		try {
			File file = getXmlFilePath("xml/example-new-contact.xml");
			putReqClient(u,file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void putContactNodeByUniqueAttr(){
		String method = "9999939999";
		String nodeSelector = constructSelectorByUniqueAttr(method);
		String u = url.concat(nodeSelector);
		try {
			File file = getXmlFilePath("xml/example-new-contact.xml");
			putReqClient(u,file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void putAttr(){		
		//six case.
		String method = "9999939999";
		String nodeSelector = construct_A_T_Leaf(method, TageName.contactName);
		String u = url.concat(nodeSelector);
		try {
			File file = getXmlFilePath("xml/example-update-contactName.xml");
			putReqClient(u, file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
