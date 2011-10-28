package com.slieer.ejbpro.ejb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;

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
*/
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
