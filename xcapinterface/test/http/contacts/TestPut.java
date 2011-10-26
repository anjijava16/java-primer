package http.contacts;


import java.io.File;

import org.junit.Test;


public class TestPut extends TestBase{
	final static String phoneNo = "8613714532530"; 
	final static String token = "YPRG0MWas7e6lM6sQ9CL4mxbtGk49jB-SNgtULIamd1SwAeSqwV0bw**";

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
		int index = 3; // example
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
		String method = "";
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
	}
}
