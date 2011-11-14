package http.singcontacts;

import org.apache.log4j.Logger;
import org.junit.Test;

public class TestSingDelete extends TestBase{
	public static Logger log = Logger.getLogger(TestSingGet.class);	
	
	final static String phoneNo = "8613480783139"; 
	final static String token = "DDcs3x7JwQQwqvOT751dhxPUTartlMV70DRk28fiJjRSwAeSqwV0bw**";
	final static String url = constructUrl(phoneNo, token);

	@Test
	public void delContacts(){
		try {
			deleteReqClient(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void delContactByTagName(){
		String u = url.concat("/contact");
		try {
			deleteReqClient(u);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void delContactByIndex(){
		int index = 1;
		String u = url.concat(constructSecondLayerSelectorByIndex(index));
		try {
			deleteReqClient(u);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void delContactByUniqueAttr(){
		String contactId = "1";
		String u = url.concat(constructSecondLayerSelectorByUniqueAttr(contactId));
		try {
			deleteReqClient(contactId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
