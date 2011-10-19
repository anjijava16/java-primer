package com.http;

import java.io.File;
import java.io.FileInputStream;
import java.text.MessageFormat;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;


public class Fundamentals {
	public static void main(String[] args) {
		String phoneNo = "8613714532530"; 
		String token = "YPRG0MWas7e6lM6sQ9CL4mxbtGk49jB-SNgtULIamd1SwAeSqwV0bw**";
		
		String url = constructUrl(phoneNo, token);
		try {
			File file = new File("JavaSE/com/http/example.xml");
			put(url,file);
			//get(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  # pHJlE_jdISlHeopL0RSI_QZ8T7ZzPcTXyKRTRUHHjMNSwAeSqwV0bw**  8613828724021<br>
		# eE60nQE4So54sNTD59vl-JzFNkMl1lxDqbgptVFd1-FSwAeSqwV0bw**  8613923406916<br>
		
		# DDcs3x7JwQQwqvOT751dhyp3s2od75lFbuwRL9UfCpJSwAeSqwV0bw**  8613480783139<br>
		
		# _MfVVlhCuyfw-nlZpt6AqncsOzPC3jjYQ3Pw6wyJoAZSwAeSqwV0bw**  8615989380390<br>
		
		# YPRG0MWas7e6lM6sQ9CL4mxbtGk49jB-SNgtULIamd1SwAeSqwV0bw**  8613714532530<br>

	 * @param phoneNo
	 * @param token
	 * @return
	 */
	public static String constructUrl(String phoneNo, String token) {
		String url = "http://localhost:8080/xcap-root/contacts/{0}/{1}/index";
		MessageFormat form = new MessageFormat(url);
		Object[] args = { phoneNo, token };
		return form.format(args);
	}

	public static void get(String url){
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
	
	public static void put(String url, File file) throws Exception {
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
