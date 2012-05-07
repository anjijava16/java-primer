package com.slieer.ejbpro.web;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

//jsp call.
public class MDBean {
	public static void callMDB(InitialContext ctx) {
		QueueConnection cnn = null;
		QueueSession session = null;
		try {
			Queue queue = (Queue) ctx.lookup("/queue/HelloQueue");
			QueueConnectionFactory factory = (QueueConnectionFactory) ctx.lookup("ConnectionFactory");
			cnn = factory.createQueueConnection();
			
			session = cnn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			TextMessage msg = session.createTextMessage("Hello World");
			QueueSender sender = session.createSender(queue);
			sender.send(msg);
			System.out.println("Message sent successfully to remote queue.");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(session != null){
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
}
