package com.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

/***
 * http://hc.apache.org/httpcomponents-client-ga/tutorial/html/fundamentals.html
 * #d4e37
 * 
 * @author me
 * 
 */

public class HttpclientTutorial {
	// google china main page.
	static String URL = "http://www.google.com.hk/webhp?hl=zh-CN&sourceid=cnhp";

	public static void main(String[] args) {
		try {
			// one();
			//uriRequest();
			responseHandlers();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void client(HttpUriRequest request) throws ClientProtocolException, IOException{
		HttpClient httpclient = new DefaultHttpClient();
		
//		HttpParams params = httpclient.getParams();
//		params.s.setHttpElementCharset("GBK");
//		params.setContentCharset("GBK");
		
		HttpResponse response = httpclient.execute(request);		
		
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			if (entity != null) {
		    entity = new BufferedHttpEntity(entity);
		  }

			InputStream instream = entity.getContent();
			Scanner scanner = new Scanner(instream);
			while (scanner.hasNextLine()) {
				System.out.println(scanner.nextLine());
			}
			instream.close();
		}
	}
		
	// 乱码?
	static void simpleRequest() throws Exception {
		HttpGet httpget = new HttpGet(URL);
		client(httpget);
	}

	static void uriRequest() throws Exception {
		URI uri = URIUtils.createURI("http", "www.google.com", -1, "/search", "q=httpclient&btnG=Google+Search&aq=f&oq=", null);
		HttpGet httpget = new HttpGet(uri);
		System.out.println(httpget.getURI());

		client(httpget);
	}

	static void thrid() throws Exception {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("q", "httpclient"));
		qparams.add(new BasicNameValuePair("btnG", "Google Search"));
		qparams.add(new BasicNameValuePair("aq", "f"));
		qparams.add(new BasicNameValuePair("oq", null));
		URI uri = URIUtils.createURI("http", "www.google.com", -1, "/search", URLEncodedUtils.format(qparams, "UTF-8"), null);
		HttpGet httpget = new HttpGet(uri);
		System.out.println(httpget.getURI());

		client(httpget);

		//HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, 
		//		HttpStatus.SC_OK, "OK");
	}
	
	static void file() throws Exception {
		File file = new File("somefile.txt");
		FileEntity entity = new FileEntity(file, "text/plain; charset=\"UTF-8\"");
		HttpPost httppost = new HttpPost("http://localhost/action.do");

		httppost.setEntity(entity);
		client(httppost);
	}
	
	
	static void formRequest() throws Exception {
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("param1", "value1"));
		formparams.add(new BasicNameValuePair("param2", "value2"));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "gb2312");
		HttpPost httppost = new HttpPost("http://localhost/handler.do");
		httppost.setEntity(entity);
		
		client(httppost);
	}
	
	
	static void responseHandlers() throws Exception{
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet("http://www.baidu.com");

		ResponseHandler<byte[]> handler = new ResponseHandler<byte[]>() {
		    public byte[] handleResponse(
		            HttpResponse response) throws ClientProtocolException, IOException {
		        HttpEntity entity = response.getEntity();
		        if (entity != null) {
		            return EntityUtils.toByteArray(entity);
		        } else {
		            return null;
		        }
		    }
		};

		byte[] response = httpclient.execute(httpget, handler);

		String str = new String(response,"utf-8");
		System.out.println(str);
	}
}
