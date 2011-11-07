package http.singcontacts;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class TestSingGet extends TestBase{
	public static Logger log = Logger.getLogger(TestSingGet.class);	
	
	final static String phoneNo = "8613480783139"; 
	final static String token = "DDcs3x7JwQQwqvOT751dhxPUTartlMV70DRk28fiJjRSwAeSqwV0bw**";

	final static String url = constructUrl(phoneNo, token);
	
	@Before 
	public void runBeforeTest(){
	} 
	
	@Test
	public void getDocument(){
		log.info("url:" + url);
		getReqClient(url);
	}

	@Test
	public void getDocumentByTagName(){
		String u = url.concat("/~~/contacts");
		getReqClient(u);
	}
	
	@Test
	public void getContactNodebyTagName(){
		String sel = "/~~/contacts/contact";
		String u = url.concat(sel);
		getReqClient(u);
	}
	
	@Test
	public void getContactNodebyIndex(){
		String sel = constructSelectorByIndex(1);
		String u = url.concat(sel);
		getReqClient(u);		
	}

	@Test
	public void getContactNodebyUniqueAttr(){
		long contactId = 23479;
		String sel = constructSelectorByUniqueAttr(String.valueOf(contactId));
		String u = url.concat(sel);
		getReqClient(u);
	}
	
	@Test
	public void getTagName(){
		
	}

	@Test
	public void getByIndex(){
		
	}

}
