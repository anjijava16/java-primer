package com.slieer.ejbpro;

import java.util.Date;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;

import com.slieer.ejbpro.ifc.HelloStatelessRemoteIfc;

public class HelloMessageClient {
	static Logger log = Logger.getLogger(HelloMessageClient.class);

	public static void main(String[] args) throws Exception {
		InitialContext ctx = getContext();

		//MDBean.callMDB(ctx);
		
		startTimer(ctx);
	}

	private static InitialContext getContext() throws NamingException {
		Properties properties = new Properties();
		properties.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
		properties.setProperty("java.naming.provider.url", "localhost:1099");
		InitialContext ctx = new InitialContext(properties);
		return ctx;
	}	
	
	public static void startTimer(Context ctx) {

		try {
			System.out.println(new Date().getTime());
			HelloStatelessRemoteIfc timer = (HelloStatelessRemoteIfc) ctx.lookup(HelloStatelessRemoteIfc.HELLO_REMOTE_JNDI);
			timer.scheduleTimer((long) 3000);
			log.info("定时器已经启动，请观察Jboss控制台输出，如果定时器触发5次，便终止定时器");
			
			
			log.info("server say:" + timer.hello());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
