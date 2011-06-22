package org.book.server;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.book.SmsObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyIoHandler extends IoHandlerAdapter {
	// 这里我们使用的SLF4J作为日志门面，至于为什么在后面说明。
	private final static Logger LOGGER = LoggerFactory.getLogger(MyIoHandler.class);

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {		
		SmsObject sms = (SmsObject) message;
		LOGGER.info("The message received is [" + sms.getReceiver() + "]");
		LOGGER.info("The message received is [" + sms.getSender() + "]");
		LOGGER.info("The message received is [" + sms.getMessage() + "]");
		
		
/*		String str = message.toString();
		log.info("The message received is [" + str + "]");
		
		if (str.endsWith("quit")) {
			session.close(true);
			return;
		}
*/	}
}
