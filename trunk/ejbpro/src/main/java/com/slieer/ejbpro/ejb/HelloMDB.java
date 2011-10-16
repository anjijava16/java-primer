package com.slieer.ejbpro.ejb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;

@MessageDriven(activationConfig = { 
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/HelloQueue") 
		}
)
public class HelloMDB implements MessageListener {

	public void onMessage(Message arg0) {
		System.out.println("----------------");
		System.out.println("Received message");
		System.out.println("----------------");
	}
}
