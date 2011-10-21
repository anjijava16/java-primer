package http;

import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class TestGet extends Test{
	public static void main(String[] args) {
		String phoneNo = "8613714532530"; 
		String token = "YPRG0MWas7e6lM6sQ9CL4mxbtGk49jB-SNgtULIamd1SwAeSqwV0bw**";
		
		String url = constructUrl(phoneNo, token);
		getDocument(url);
	}
	
	public static void getDocument(String url){
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httppost = new HttpGet(url);
        try {
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            
            Scanner s = new Scanner(resEntity.getContent());
            while(s.hasNextLine()){
            	System.out.println(s.nextLine());
            }
/*          if(response.getStatusLine().getStatusCode() == 200){
            }
*/
        }catch(Exception e){
        	e.printStackTrace();
        }
	}
	
}
