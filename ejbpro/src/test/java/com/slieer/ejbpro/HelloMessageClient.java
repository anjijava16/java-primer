package com.slieer.ejbpro;

import java.util.Date;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.slieer.ejbpro.ifc.HelloStatelessRemoteIfc;
import com.slieer.ejbpro.ifc.InterceptorTestRemoteIfc;
import com.slieer.ejbpro.web.MDBean;

public class HelloMessageClient {
	static Logger log = Logger.getLogger(HelloMessageClient.class);
	static InitialContext  ctx = null;
	static {
		Properties properties = new Properties();
		properties.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
		properties.setProperty("java.naming.provider.url", "localhost:1099");
		try {
			ctx = new InitialContext(properties);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testHello() throws Exception{
		HelloStatelessRemoteIfc timer = (HelloStatelessRemoteIfc) ctx.lookup(HelloStatelessRemoteIfc.HELLO_REMOTE_JNDI);
		System.out.println(timer.hello());		
	}
	
	@Test
	public void mdbTest(){
		MDBean.callMDB(ctx);
	}
	
	@Test
	public void startTimer() {

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
	
	@Test
	public void statelessBeanCallMDB(){
		try {
			HelloStatelessRemoteIfc stateless = (HelloStatelessRemoteIfc) ctx.lookup(HelloStatelessRemoteIfc.HELLO_REMOTE_JNDI);
			stateless.testMDB();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 在bean 中自定义线程 
	 */
	@Test
	public void ejbMyThread(){
		HelloStatelessRemoteIfc stateless = null;
		try {
			stateless = (HelloStatelessRemoteIfc) ctx.lookup(HelloStatelessRemoteIfc.HELLO_REMOTE_JNDI);
			stateless.ejbMyThread();

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void InterceptorTest(){
		try {
			//InterceptorTestRemoteIfc ifc = (InterceptorTestRemoteIfc)ctx.lookup("InterceptorTest");
			
			InterceptorTestRemoteIfc ifc2 = (InterceptorTestRemoteIfc)ctx.lookup("SampleEJB");
			
			//ifc.doSomething("hello");
			ifc2.doSomething("second hello");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
}
