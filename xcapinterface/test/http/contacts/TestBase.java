package http.contacts;

import java.io.ByteArrayInputStream;
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
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
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
	
	public static String CODING = "UTF-8";
	
	/**
	 * [
	 */
	public static String LEFT_SQUARE_BRACKET = "%5B";
	/**
	 * ]
	 */
	public static String RIGHT_SQUARE_BRACKET = "%5D";
	/**
	 * @
	 */
	public static String AT = "%40";
	/**
	 * "
	 */
	public static String DOUBLE_QUOTATION_MARKS  = "%22";
	
	public enum TagName{contacts,contact,list,contactName, description, createDate, method, rawId, deviceId};
		
	public static String constructUrl(String phoneNo, String token) {
		String baseUrl = "http://{0}:{1}/xcap-root/UABContacts/{2}/{3}/index";
		MessageFormat form = new MessageFormat(baseUrl);
		Object[] args = {HOST, PORT, phoneNo, token };
		return form.format(args);
	}
	
	public static String constructSelectorByIndex(int index){
		String nodeSelector = "/~~/contacts/contact{0}{1}{2}";
		
		MessageFormat form = new MessageFormat(nodeSelector);
		Object[] args = {LEFT_SQUARE_BRACKET, index, RIGHT_SQUARE_BRACKET};
		return form.format(args);
	}
	
	public static String constructSelectorByUniqueAttr(String method){
		String nodeSelector = "/~~/contacts/contact{0}{1}method={2}{3}{2}{4}";
		
		MessageFormat form = new MessageFormat(nodeSelector);
		Object[] args = {LEFT_SQUARE_BRACKET, AT,DOUBLE_QUOTATION_MARKS, method, RIGHT_SQUARE_BRACKET};
		return form.format(args);
	}

	public static String constructSelectorByTagName(){
		String nodeSelector = "/~~/contacts/contact";
		return nodeSelector;
	}
	
	public static String construct_T_T_Selector(TagName tageName){
		String firstLevelSelector = constructSelectorByTagName();
		return firstLevelSelector.concat("/").concat(tageName.name());
	}
	
	public static String construct_I_T_Selector(int index ,TagName tageName){
		String firstLevelSelector = constructSelectorByIndex(index);
		firstLevelSelector = firstLevelSelector.concat("/").concat(tageName.name());
		
		return firstLevelSelector;
	}
	
	/**
	 * 
	 * @param uniqueAttr 2nd layer selector.
	 * @param tageName 3rd layer selector.
	 * @return
	 */
	public static String construct_A_T_Selector(String uniqueAttr, TagName tageName){
		String firstLevelSelector = constructSelectorByUniqueAttr(uniqueAttr);
		String nodeSelector = firstLevelSelector.concat("/").concat(tageName.name());
		return nodeSelector;
	}	
	
	/**
	 * @param tageName: 2nd layer selector is tag name selector.
	 * @param index: 3rd layer selector is index selector.
	 * @return
	 */
	public static String construct_T_I_Selector(TagName tageName, int index){
		return construct_T_T_Selector(tageName).concat(LEFT_SQUARE_BRACKET).concat(String.valueOf(index)).concat(RIGHT_SQUARE_BRACKET);
	}	
	
	public static String construct_I_I_Selector(int index, TagName _3rdLayerTagName, int _3rdLayerIndex){
		String conatctSelecotor = constructSelectorByIndex(index);
		return conatctSelecotor.concat("/").concat(_3rdLayerTagName.name()).concat(LEFT_SQUARE_BRACKET).concat(String.valueOf(_3rdLayerIndex)).concat(RIGHT_SQUARE_BRACKET);
	}	

	public static String construct_A_I_Selector(String method, TagName _3rdLayerTagName, int _3rdLayerIndex){
		String conatctSelecotor = constructSelectorByUniqueAttr(method);
		return conatctSelecotor.concat("/").concat(_3rdLayerTagName.name()).concat(LEFT_SQUARE_BRACKET).concat(String.valueOf(_3rdLayerIndex)).concat(RIGHT_SQUARE_BRACKET);
	}	
	
	public static File getXmlFilePath(String fileName){
		return new File("http/contacts/".concat(fileName));
	}
	
	public static void getReqClient(String url) {
		HttpGet httppost = new HttpGet(url);
		System.out.println("executing request " + httppost.getRequestLine());
		
		Scheme scheme = new Scheme("http",80, PlainSocketFactory.getSocketFactory());
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(scheme);

		ClientConnectionManager cm = new ThreadSafeClientConnManager(schemeRegistry);
		HttpClient httpClient = new DefaultHttpClient(cm);

		//HttpClient httpClient = new DefaultHttpClient();
		response(httpClient, httppost); 	
	}
	
	public static void putReqClient(String url, File file) throws Exception {
		putReqClient(url, new FileInputStream(file));
	}
	
	public static void putReqClient(String url, InputStream in) throws Exception {

        HttpPut httpPut = new HttpPut(url);
        InputStreamEntity reqEntity = new InputStreamEntity(in, -1);
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
		//ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager();
	    //cm.setMaxTotal(100);		
		//HttpClient httpclient = new DefaultHttpClient(cm);
		HttpClient httpclient = new DefaultHttpClient();
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
	
	public static InputStream getInputSteam(String str){
		return new ByteArrayInputStream(str.getBytes());
	}
}

