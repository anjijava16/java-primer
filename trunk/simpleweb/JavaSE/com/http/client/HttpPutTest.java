package com.http.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.junit.Test;

public class HttpPutTest {
	String url = "http://localhost:8080/simpleweb/PutServlet";

	@Test
	public void put() throws Exception {
		String file = "E:/BorqsWorkspace/workspace/simpleweb/JavaSE/com/http/client/HttpclientTutorial.java";
		HttpPut httpPut = new HttpPut(url);
		InputStreamEntity reqEntity = new InputStreamEntity(
				new FileInputStream(file), -1);
		reqEntity.setContentType("text/xml");
		reqEntity.setChunked(true);

		httpPut.setEntity(reqEntity);

		HttpClient httpclient = new DefaultHttpClient();
		HttpParams params = httpclient.getParams();
		params.setIntParameter(HttpConnectionParams.SO_TIMEOUT, 3000); // 超时设置
		params.setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 3000);// 连接超时
		System.out.println("executing request " + httpPut.getRequestLine());
		response(httpclient, httpPut);
	}

	private static void response(HttpClient httpclient, HttpRequestBase httpRes) {
		try {
			HttpResponse response = httpclient.execute(httpRes);
			HttpEntity resEntity = response.getEntity();

			Header[] headers = response.getAllHeaders();
			System.out.println("----------header-------------");
			System.out.println("Status-Code:"
					+ response.getStatusLine().getStatusCode());
			for (int i = 0; i < headers.length; i++) {
				Header header = headers[i];
				System.out.println(header);
			}

			System.out.println("----------content-------------");
			Scanner s = new Scanner(resEntity.getContent());
			while (s.hasNextLine()) {
				System.out.println(s.nextLine());
			}
			/*
			 * if(response.getStatusLine().getStatusCode() == 200){}
			 */
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
	}

}
