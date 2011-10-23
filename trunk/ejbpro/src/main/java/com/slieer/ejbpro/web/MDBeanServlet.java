package com.slieer.ejbpro.web;

import java.io.IOException;
import java.util.*;
import javax.jms.*;
import javax.jms.Queue;
import javax.naming.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class MDBeanServlet extends HttpServlet {
	private static final long serialVersionUID = -5924457543550363112L;
	
	Context context = null;
	QueueConnection queueConnection = null;
	QueueSession queueSession = null;
	Queue queue = null;
	QueueSender queueSender = null;
	TextMessage message = null;
	boolean esito = true;

	public void init() {

		try {
			context = new InitialContext();
			getQueueSender();
			context.close();
		} catch (Exception exc) {
			System.out.println(exc.toString());
			esito = false;
		}
	}

	public void getQueueSender() throws JMSException, NamingException {

		QueueConnectionFactory queueFactory = (QueueConnectionFactory) context.lookup("ConnectionFactory");
		queueConnection = queueFactory.createQueueConnection();
		queueSession = queueConnection.createQueueSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
		queue = (Queue) context.lookup("queue/HelloQueue");
		queueSender = queueSession.createSender(queue);
		message = queueSession.createTextMessage();
	}

	public void sendMsg(String testo) throws JMSException {
		message.setText(testo);
		queueSender.send(queue, message);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String whereToForward = null;
		String text = request.getParameter("text");
		if (text == null) {
			text = "dummy message";
		}
		if (esito) {
			try {
				sendMsg(text);
				whereToForward = "success";
			} catch (JMSException jme) {
				System.out.println(jme.toString());
				whereToForward = "failure";
			}
		} else {
			whereToForward = "failure";
		}
		System.out.println("Done!");

	}

	public void destroy() {
		try {
			if (queueSender != null)
				queueSender.close();
		} catch (JMSException jme) {
			System.out.println(jme.toString());
		} finally {
			queueSender = null;
		}
		try {
			if (queueSession != null)
				queueSession.close();
		} catch (JMSException jme) {
			System.out.println(jme.toString());
		} finally {
			queueSession = null;
		}
		try {
			if (queueConnection != null)
				queueConnection.close();
		} catch (JMSException jme) {
			System.out.println(jme.toString());
		} finally {
			queueConnection = null;
		}
	}
}
