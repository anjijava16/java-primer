package com.slieer.ejbpro.ejb;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.jboss.ejb3.annotation.ResourceAdapter;

/*
<!--  write to messaging\destinations-service.xml
	or create file what name is xx-service.xml, 
	e.g. src\doc\HelloMDB-service.xml,and copy JBOSS_HOME/server/default/deploy.
-->
mbean code="org.jboss.mq.server.jmx.Queue" 
      name="jboss.mq.destination:service=Queue,
      name=HelloQueue"> 
      
      <attribute name="JNDIName">queue/HelloQueue</attribute> 
      <depends optional-attribute-name="DestinationManager">
      	jboss.mq:service=DestinationManager
      </depends> 
</mbean> 


查看已发布的web service: http://127.0.0.1:8080/jbossws/
@MessageDriven(activationConfig = { 
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/HelloQueue") 
		}
)
*/

@MessageDriven(name = "MDBExample",
activationConfig =
      {
         @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
         @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/HelloQueue")
      })
public class HelloMDB implements MessageListener {

	int i = 0;
	public void onMessage(Message arg0) {
		System.out.println("----------------class instance variable:" + i++);
		int j = 0;
		j++;
		System.out.println("local variable:" + j);
		TextMessage t = (TextMessage)arg0;
		try {
			String text = t.getText();
			System.out.println("Received message" + text);
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		URL url = null;
		try {
			url = new URL("http://baidu.com");
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			
			System.out.print("baidu ResponseCode: " + con.getResponseCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Runnable r = new Runnable(){
			@Override
			public void run() {
				System.out.println(".............MDB. thread..........");
			}
			
		};
		
		Thread th = new Thread(r);
		th.start();
		System.out.println("----------------");
	}
}
