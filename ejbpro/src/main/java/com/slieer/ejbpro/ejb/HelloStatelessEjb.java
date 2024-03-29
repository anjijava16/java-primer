package com.slieer.ejbpro.ejb;

import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.LocalBinding;
import org.jboss.ejb3.annotation.RemoteBinding;

import com.slieer.ejbpro.ifc.HelloStatelessLocalIfc;
import com.slieer.ejbpro.ifc.HelloStatelessRemoteIfc;

@Stateless
@Local(value = HelloStatelessLocalIfc.class)
@Remote(value = HelloStatelessRemoteIfc.class)
@LocalBinding(jndiBinding = "HelloStatelessLocal")
@RemoteBinding(jndiBinding = "HelloStatelessRemote")
public class HelloStatelessEjb implements HelloStatelessLocalIfc, HelloStatelessRemoteIfc {
	Logger log = Logger.getLogger(HelloStatelessEjb.class);
	
	@Resource
	private SessionContext ctx;
	
	@Resource
	private TimerService timer;

	@Override
	public String hello() {
		return "hello I'm slieer.";
	}

	@Override
	public void scheduleTimer(long milliseconds) {
		count = 1;
		//TimerService timer = ctx.getTimerService();
		
		Date newDatePoint = new Date(new Date().getTime() + milliseconds);
		timer.createTimer(newDatePoint, milliseconds, "大家好，这是我的第一个定时器");
	}

	private int count = 1; // 不太好啊

	@Timeout
	public void timeoutHandler(Timer timer) {
		log.info("---------------------");
		log.info("定时器事件发生,传进的参数为: " + timer.getInfo());
		log.info("定时器事件发生,传进的参数为: " + timer.getNextTimeout());
		log.info("---------------------");
		if (count >= 5) {
			log.info("timer stop!");
			timer.cancel();// 如果定时器触发5次，便终止定时器
		}
		count++;
	}

	/**
	 * 消息队列使用完后应该关闭,不然会抛出以下 wrn.
	 * 
	 * 13:57:14,938 WARN  [org.hornetq.jms.client.HornetQConnection] I'm closing a JMS connection you left open. Please make sure you close all JMS connections explicitly before letting them go out of scope!
13:57:14,939 WARN  [org.hornetq.jms.client.HornetQConnection] The JMS connection you didn't close was created here:: java.lang.Exception
	at org.hornetq.jms.client.HornetQConnection.<init>(HornetQConnection.java:152)
	at org.hornetq.jms.client.HornetQQueueConnection.<init>(HornetQQueueConnection.java:35)

	 * 
	 * 
	 */
	@Override
	public void testMDB() {
		QueueConnection cnn = null;;
		QueueSession session = null;
		
		try {
			Queue queue = (Queue) ctx.lookup("queue/HelloQueue");
			QueueConnectionFactory factory = (QueueConnectionFactory) ctx.lookup("ConnectionFactory");
			cnn = factory.createQueueConnection();
			
			session = cnn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			TextMessage msg = session.createTextMessage("Hello World");
			QueueSender sender = session.createSender(queue);
			sender.send(msg);
			System.out.println("Message sent successfully to remote queue.");
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				if (session != null) {
					session.close();
				}
				
				if(cnn != null){
					cnn.close();
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
			
		}

	}
	
	/***
	 * http://stackoverflow.com/questions/533783/why-spawning-threads-in-j2ee-container-is-discouraged
	 */
	public void ejbMyThread(){
		Runnable r = new Runnable(){

			@Override
			public void run() {
				System.out.println("------------ejbMyThread");
			}
			
		};
		
		new Thread(r).start();
	}
}
