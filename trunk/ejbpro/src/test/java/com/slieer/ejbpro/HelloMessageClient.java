package com.slieer.ejbpro;

import javax.naming.InitialContext;

import com.slieer.ejbpro.web.MDBean;

public class HelloMessageClient {

	public static void main(String[] args) throws Exception {		
		/*
		Properties properties = new Properties();
		properties.setProperty(Context.INITIAL_CONTEXT_FACTORY,"org.jnp.interfaces.NamingContextFactory ");  
		properties.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces ");  
		properties.setProperty(Context.PROVIDER_URL, "localhost:1099"); 
		InitialContext ctx = new InitialContext(properties);
		 * */
		
		InitialContext ctx = new InitialContext();
		MDBean.callMDB(ctx);
	}
}
