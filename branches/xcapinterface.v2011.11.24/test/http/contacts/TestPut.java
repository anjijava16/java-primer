package http.contacts;


import java.io.File;

import org.junit.Test;


public class TestPut extends TestBase{
	final static String phoneNo = "8613480783139"; 
	final static String token = "DDcs3x7JwQQwqvOT751dhxPUTartlMV70DRk28fiJjRSwAeSqwV0bw**";

	final String url = constructUrl(phoneNo, token);

	/**
	 * put contacts node.
	 */
	@Test
	public void putDocument(){
		try {
			File file = getXmlFilePath("xml/example-new-contacts.xml");
			putReqClient(url,file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * put contact node by index.
	 */
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
	
	/**
	 * put contact node by node tag name.
	 */
	@Test
	public void putContactNodeByTagName(){
		String nodeSelector = constructSelectorByTagName();
		String u = url.concat(nodeSelector);
		
		String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + 
					 "	<contact method=\"14712580\">" + 
					 "		<contactName>super dan</contactName>" + 
					 "      <deviceId>09</deviceId>" + 
					 "      <rawId>10</rawId>" + 
					 "	</contact>";

		try {
			putReqClient(u, getInputSteam(str));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * put contact node by unique attr.
	 */
	@Test
	public void putContactNodeByUniqueAttr(){
		String method = "12593";
		String nodeSelector = constructSelectorByUniqueAttr(method);
		String u = url.concat(nodeSelector);
		
		String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + 
		 "	<contact method=\"12593\">" + 
		 "		<contactName>super star</contactName>" + 
		 "      <deviceId>1235422</deviceId>" + 
		 "      <rawId>154558772</rawId>" + 
		 "	</contact>";

		try {
			putReqClient(u, getInputSteam(str));
		
		} catch (Exception e) {
		e.printStackTrace();
		}
	}
	
	/**
	 * put contactName node.
	 * 
	 * 1st layer selector by unique attr.
	 * 2nd layer selector by tag name.
	 */
	@Test
	public void putContactNameByAttr(){		
		//six case.
		String method = "12593";
		String nodeSelector = construct_A_T_Selector(method, TagName.contactName);
		String u = url.concat(nodeSelector);
				
		String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><contactName>c plus plus</contactName>"; 
		try {
			putReqClient(u, getInputSteam(str));
		
		} catch (Exception e) {
		e.printStackTrace();
		}
	}
	
	/**
	 * put rawId node
	 * 
	 * 1st layer selector by unique attr.
	 * 2nd layer selector by tag name.
	 */
	@Test
	public void putRawIdByAttr(){		
		//six case.
		String method = "12593";
		String nodeSelector = construct_A_T_Selector(method, TagName.rawId);
		String u = url.concat(nodeSelector);
				
		String str = "<rawId>123456</rawId>"; 
		try {
			putReqClient(u, getInputSteam(str));
		
		} catch (Exception e) {
		e.printStackTrace();
		}
	}
	
	/**
	 * put deviceId node.
	 * 
	 * 1st layer selector by unique attr.
	 * 2nd layer selector by tag name.
	 */
	@Test
	public void putDeviceIdByAttr(){		
		//six case.
		String method = "12593";
		String nodeSelector = construct_A_T_Selector(method, TagName.deviceId);
		String u = url.concat(nodeSelector);
				
		String str = "<deviceId>111</deviceId>"; 
		try {
			putReqClient(u, getInputSteam(str));
		
		} catch (Exception e) {
		e.printStackTrace();
		}
	}
	
	/**
	 * put contactName node.
	 * 
	 * 1st layer selector by unique attr.
	 * 2nd layer selector by index.
	 */
	@Test
	public void putContactNameByIndex(){		
		//six case.
		String method = "12593";
		int index = 1;
		String nodeSelector = construct_A_T_Selector(method, TagName.contactName).concat(LEFT_SQUARE_BRACKET) + index + RIGHT_SQUARE_BRACKET;
		String u = url.concat(nodeSelector);
				
		String str = "<contactName>c++</contactName>"; 
		try {
			putReqClient(u, getInputSteam(str));
		
		} catch (Exception e) {
		e.printStackTrace();
		}
	}
	
}
