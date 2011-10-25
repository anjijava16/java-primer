package http;

import java.io.File;
import java.io.FileInputStream;
import java.text.MessageFormat;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.junit.Test;


public class TestPut extends TestBase{
	String phoneNo = "8613714532530"; 
	String token = "YPRG0MWas7e6lM6sQ9CL4mxbtGk49jB-SNgtULIamd1SwAeSqwV0bw**";

	@Test
	public void putDocument(){
		String url = constructUrl(phoneNo, token);
		try {
			File file = getXmlFilePath("xml/example-new-contacts.xml");
			putDocument(url,file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	@Test
	public void putNodeByIndex(){
		String url = constructUrl(phoneNo, token);
		
		String nodeSelector = "/~~/contacts/contact{0}1{1}";
		MessageFormat form = new MessageFormat(nodeSelector);
		Object[] args = {LEFT_SQUARE_BRACKET, RIGHT_SQUARE_BRACKET};
		nodeSelector = form.format(args);

		url = url.concat(nodeSelector);
		try {
			File file = getXmlFilePath("xml/example-new-contact.xml");
			putDocument(url,file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public static void putDocument(String url, File file) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();

        HttpPut httppost = new HttpPut(url);
        InputStreamEntity reqEntity = new InputStreamEntity(
                new FileInputStream(file), -1);
        reqEntity.setContentType("text/xml");
        reqEntity.setChunked(true);
        
        httppost.setEntity(reqEntity);
        httpclient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT,3000); //超时设置
        httpclient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 3000);//连接超时
        System.out.println("executing request " + httppost.getRequestLine());
        try {
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            
            System.out.println("StatusCode:" + response.getStatusLine().getStatusCode());
            Scanner s = new Scanner(resEntity.getContent());
            while(s.hasNextLine()){
            	System.out.println(s.nextLine());
            }
/*            if(response.getStatusLine().getStatusCode() == 200){
            }
*/
        }catch(Exception e){
        	e.printStackTrace();
        } 
	}
}
