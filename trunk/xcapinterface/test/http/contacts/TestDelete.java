package http.contacts;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Ignore;
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
		String nodeSelector = constructSelectorByIndex(2);
		url = url.concat(nodeSelector);
		try {
			deleteReqClient(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void deleteContactByUniqueAttr() {
		String method = "156445564641";
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
	
	@Ignore
	@Test
	public void del(){
 		HttpDelete httpDelete = new HttpDelete("http://www.oschina.net/");
 		
 		HttpClient httpclient = new DefaultHttpClient();
		try {
			httpclient.execute(httpDelete);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("executing request " + httpDelete.getRequestLine());		
	}
}
