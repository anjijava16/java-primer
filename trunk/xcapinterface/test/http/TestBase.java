package http;

import java.io.File;
import java.text.MessageFormat;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public abstract class TestBase {
	public static String CODING = "UTF-8";
	public static String LEFT_SQUARE_BRACKET = "%5B";
	public static String RIGHT_SQUARE_BRACKET = "%5D";
	public static String AT = "%40";
	public static String DOUBLE_QUOTATION_MARKS  = "%22";
	
	/**
	 *  # pHJlE_jdISlHeopL0RSI_QZ8T7ZzPcTXyKRTRUHHjMNSwAeSqwV0bw**  8613828724021<br>
	 *	# eE60nQE4So54sNTD59vl-JzFNkMl1lxDqbgptVFd1-FSwAeSqwV0bw**  8613923406916<br>
	 *
	 *	# DDcs3x7JwQQwqvOT751dhyp3s2od75lFbuwRL9UfCpJSwAeSqwV0bw**  8613480783139<br>
	 *	
	 *	# _MfVVlhCuyfw-nlZpt6AqncsOzPC3jjYQ3Pw6wyJoAZSwAeSqwV0bw**  8615989380390<br>
	 *	
	 *	# YPRG0MWas7e6lM6sQ9CL4mxbtGk49jB-SNgtULIamd1SwAeSqwV0bw**  8613714532530<br>
	*/
	public static String constructUrl(String phoneNo, String token) {
		String url = "http://localhost:8080/xcap-root/contacts/{0}/{1}/index";
		MessageFormat form = new MessageFormat(url);
		Object[] args = { phoneNo, token };
		return form.format(args);
	}
	
	public static File getXmlFilePath(String fileName){
		return new File("test/http/".concat(fileName));
	}
	

	public static void client(String url) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httppost = new HttpGet(url);
        try {
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            
            Scanner s = new Scanner(resEntity.getContent());
            while(s.hasNextLine()){
            	System.out.println(s.nextLine());
            }
            /*          
            if(response.getStatusLine().getStatusCode() == 200){
            }
            */
        }catch(Exception e){
        	e.printStackTrace();
        }
	}
	
}
