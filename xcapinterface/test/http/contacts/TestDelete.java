package http.contacts;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Ignore;
import org.junit.Test;

public class TestDelete extends TestBase {
	final static String phoneNo = "8613714532530"; 
	final static String token = "YPRG0MWas7e6lM6sQ9CL4mxbtGk49jB-SNgtULIamd1SwAeSqwV0bw**";	
	
	@Test
	public void deleteContacts() {
		String url = constructUrl(phoneNo, token);
		try {
			deleteReqClient(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void deleteContactByIndex() {
		String url = constructUrl(phoneNo, token);
		try {
			deleteReqClient(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void deleteContactByUniqueAttr() {
		String url = constructUrl(phoneNo, token);
		try {
			deleteReqClient(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void deleteContactByTagName() {
		String url = constructUrl(phoneNo, token);
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
