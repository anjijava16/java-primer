package http.contacts;

import org.junit.Test;

public class TestDelete extends TestBase {
	final static String phoneNo = "8613480783139"; 
	final static String token = "DDcs3x7JwQQwqvOT751dhxPUTartlMV70DRk28fiJjRSwAeSqwV0bw**";
	
	String url = constructUrl(phoneNo, token);
	@Test
	public void deleteContacts() {
		try {
			deleteReqClient(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void deleteContactByIndex() {
		String nodeSelector = constructSelectorByIndex(1);
		url = url.concat(nodeSelector);
		try {
			deleteReqClient(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void deleteContactByUniqueAttr() {
		String method = "46546464646";
		url = url.concat(constructSelectorByUniqueAttr(method));
		try {
			deleteReqClient(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void deleteContactByTagName() {
		url = url.concat(constructSelectorByTagName());
		try {
			deleteReqClient(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
