package com.http.auth;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.auth.DigestScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class DigestTest {
	public static void main(String[] args) {
		try {
			//f();
			fff();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static String url = "http://localhost:8080/simpleweb/protected";
	
	static void f() throws ClientProtocolException, IOException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpGet httpget = new HttpGet(url);
		
		//Header.
		//httpget.addHeader();
		HttpResponse response = httpclient.execute(httpget, localContext);

		AuthState proxyAuthState = (AuthState) localContext.getAttribute(ClientContext.PROXY_AUTH_STATE);

		System.out.println("Proxy auth scope: " + proxyAuthState.getAuthScope());
		System.out.println("Proxy auth scheme: " + proxyAuthState.getAuthScheme());
		System.out.println("Proxy auth credentials: " + proxyAuthState.getCredentials());

		AuthState targetAuthState = (AuthState) localContext.getAttribute(ClientContext.TARGET_AUTH_STATE);
		System.out.println("Target auth scope: " + targetAuthState.getAuthScope());
		System.out.println("Target auth scheme: " + targetAuthState.getAuthScheme());
		System.out.println("Target auth credentials: " + targetAuthState.getCredentials());
		
		response.getEntity();
	}
	
	static void ff() throws ClientProtocolException, IOException{
		HttpHost targetHost = new HttpHost("localhost", 8080, "http"); 

		DefaultHttpClient httpclient = new DefaultHttpClient();

		httpclient.getCredentialsProvider().setCredentials(
		        new AuthScope(targetHost.getHostName(), targetHost.getPort()), 
		        new UsernamePasswordCredentials("username", "password"));

		// Create AuthCache instance
		AuthCache authCache = new BasicAuthCache();
		// Generate BASIC scheme object and add it to the local auth cache
		BasicScheme basicAuth = new BasicScheme();
		
		authCache.put(targetHost, basicAuth);

		// Add AuthCache to the execution context
		BasicHttpContext localcontext = new BasicHttpContext();
		localcontext.setAttribute(ClientContext.AUTH_CACHE, authCache);        

		HttpGet httpget = new HttpGet("/");
		for (int i = 0; i < 3; i++) {
		    HttpResponse response = httpclient.execute(targetHost, httpget, localcontext);
		    HttpEntity entity = response.getEntity();
		    EntityUtils.consume(entity);
		}
		
	}
	
	static void fff() throws ClientProtocolException, IOException{
		HttpHost targetHost = new HttpHost("localhost", 8080, "http"); 

		DefaultHttpClient httpclient = new DefaultHttpClient();

		httpclient.getCredentialsProvider().setCredentials(
		        new AuthScope(targetHost.getHostName(), targetHost.getPort()), 
		        new UsernamePasswordCredentials("newuser", "tomcat"));

		// Create AuthCache instance
		AuthCache authCache = new BasicAuthCache();
		// Generate BASIC scheme object and add it to the local auth cache
		DigestScheme bigestScheme = new DigestScheme();
		
		authCache.put(targetHost, bigestScheme);

		// Add AuthCache to the execution context
		BasicHttpContext localcontext = new BasicHttpContext();
		localcontext.setAttribute(ClientContext.AUTH_CACHE, authCache);        

		HttpGet httpget = new HttpGet("/simpleweb/protected");
		for (int i = 0; i < 3; i++) {
		    HttpResponse response = httpclient.execute(targetHost, httpget, localcontext);
		    HttpEntity entity = response.getEntity();
		    EntityUtils.consume(entity);
		}
		
	}
	
}
