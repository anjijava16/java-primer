package com.slieer.ejbpro.web;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MDBean {
	public static void callMDB(InitialContext ctx) throws NamingException, JMSException {
		QueueConnection cnn;
		QueueSender sender;
		QueueSession session;
		Queue queue = (Queue) ctx.lookup("queue/HelloQueue");
		QueueConnectionFactory factory = (QueueConnectionFactory) ctx.lookup("ConnectionFactory");
		cnn = factory.createQueueConnection();
		session = cnn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
		TextMessage msg = session.createTextMessage("Hello World");
		sender = session.createSender(queue);
		sender.send(msg);
		System.out.println("Message sent successfully to remote queue.");
	}
}
