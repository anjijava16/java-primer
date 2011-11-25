package http.singcontacts;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Scanner;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.log4j.Logger;

public abstract class TestBase {
	public static Logger log = Logger.getLogger(TestBase.class);
	static String HOST = "10.24.1.244";
	static String PORT = "80";
	
	public static String XML_TEXT_TAGNAME = "#text";
	public static String CODING = "UTF-8";
	/** [ */
	public static String LEFT_SQUARE_BRACKET = "%5B"; //[
	/**]*/
	public static String RIGHT_SQUARE_BRACKET = "%5D";  //]
	/**@*/
	public static String AT = "%40"; //@
	/**"*/
	public static String DOUBLE_QUOTATION_MARKS  = "%22"; //"
	
	public enum TagName{contacts,contact}
	public enum TagNameThirdLayer{adr,name,tel,email,org,url,dispName,bday,title,note,lastModify}
	public enum TagNameFourthLayer{fn,ln,item}
		
	public static String constructUrl(String phoneNo, String token) {
		String baseUrl = "http://{0}:{1}/xcap-root/SingSpacesContacts/{2}/{3}/index";
		MessageFormat form = new MessageFormat(baseUrl);
		Object[] args = {HOST, PORT, phoneNo, token };
		return form.format(args);
	}
	
	public static String constructSecondLayerSelectorByIndex(int index){
		String nodeSelector = "/~~/contacts/contact{0}{1}{2}";
		
		MessageFormat form = new MessageFormat(nodeSelector);
		Object[] args = {LEFT_SQUARE_BRACKET, index, RIGHT_SQUARE_BRACKET};
		return form.format(args);
	}
	
	public static String constructSecondLayerSelectorByUniqueAttr(String contactId){
		String nodeSelector = "/~~/contacts/contact{0}{1}id={2}{3}{2}{4}";
		
		MessageFormat form = new MessageFormat(nodeSelector);
		//form.setFormats(new Format[]{new DecimalFormat("")});
		Object[] args = {LEFT_SQUARE_BRACKET, AT,DOUBLE_QUOTATION_MARKS, contactId, RIGHT_SQUARE_BRACKET};
		return form.format(args);
	}
	
	public static File getXmlFilePath(String fileName){
		return new File("test/http/singcontacts/xml/".concat(fileName));
	}
	
	public static void getReqClient(String url) {
		HttpGet httppost = new HttpGet(url);
		System.out.println("executing request " + httppost.getRequestLine());
		
		HttpClient httpclient = new DefaultHttpClient();
		response(httpclient, httppost); 	
	}
	
	public static void putReqClient(String url, File file) throws Exception {

        HttpPut httpPut = new HttpPut(url);
        InputStreamEntity reqEntity = new InputStreamEntity(
                new FileInputStream(file), -1);
        reqEntity.setContentType("text/xml");
        reqEntity.setChunked(true);
        
        httpPut.setEntity(reqEntity);
        
        HttpClient httpclient = new DefaultHttpClient();
        HttpParams params = httpclient.getParams();
        params.setIntParameter(HttpConnectionParams.SO_TIMEOUT,3000); //超时设置
        params.setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 3000);//连接超时
        System.out.println("executing request " + httpPut.getRequestLine());
        response(httpclient, httpPut); 
	}
	
	public static void putReqClient(String url, InputStream in) throws Exception {

        HttpPut httpPut = new HttpPut(url);
        InputStreamEntity reqEntity = new InputStreamEntity(
                in, -1);
        reqEntity.setContentType("text/xml");
        reqEntity.setChunked(true);
        
        httpPut.setEntity(reqEntity);
        
        HttpClient httpclient = new DefaultHttpClient();
        HttpParams params = httpclient.getParams();
        params.setIntParameter(HttpConnectionParams.SO_TIMEOUT,3000); //超时设置
        params.setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 3000);//连接超时
        System.out.println("executing request " + httpPut.getRequestLine());
        response(httpclient, httpPut); 
	}
	
	public static void deleteReqClient(String url) throws Exception {
		ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager();
	    cm.setMaxTotal(100);		
		HttpClient httpclient = new DefaultHttpClient(cm);
 		HttpDelete httpDelete = new HttpDelete(url);
 		
		//httpclient.execute(httpDelete);
		System.out.println("executing request " + httpDelete.getRequestLine());
        response(httpclient, httpDelete);
	}
	
	private static void response(HttpClient httpclient, HttpRequestBase httpRes) {
		try {
            HttpResponse response = httpclient.execute(httpRes);
            HttpEntity resEntity = response.getEntity();
            
            Header[] headers = response.getAllHeaders();
            System.out.println("----------header-------------");
            System.out.println("Status-Code:" + response.getStatusLine().getStatusCode());
            for(int i = 0; i < headers.length; i++){
            	Header header = headers[i];
            	 System.out.println(header);
            }
            
            System.out.println("----------content-------------");
            Scanner s = new Scanner(resEntity.getContent());
            while(s.hasNextLine()){
            	 System.out.println(s.nextLine());
            }
			/*          
			 * if(response.getStatusLine().getStatusCode() == 200){}
			*/
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	httpclient.getConnectionManager().shutdown();
        }
	}	
}
