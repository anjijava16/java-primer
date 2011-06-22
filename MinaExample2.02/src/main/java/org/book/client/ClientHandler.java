package org.book.client;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.book.SmsObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHandler extends IoHandlerAdapter {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(ClientHandler.class);
	private final String values;

	public ClientHandler(String values) {
		this.values = values;
	}

	@Override
	public void sessionOpened(IoSession session) {
		SmsObject sms = new SmsObject();
		sms.setSender("15801012253");
		sms.setReceiver("18869693235");
		sms.setMessage("你好！Hello World!");
		session.write(sms);
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		LOGGER.info("session idle");
	}
}
