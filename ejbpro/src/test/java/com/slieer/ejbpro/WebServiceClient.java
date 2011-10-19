package com.slieer.ejbpro;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.apache.log4j.Logger;

import com.slieer.ejbpro.ifc.HelloWebserviceIfc;


public class WebServiceClient {
	static Logger log = Logger.getLogger(WebServiceClient.class);

    static String host = "localhost";
    static String portType = "HelloBean";
    static String serviceName = "Greeter";
    static String serviceEndpointAddress = "http://" + host + ":8080/" + serviceName;
    static String nameSpace = "http://ws.examples/";

    public static void main(String[] args) throws Exception
    {
        URL wsdlLocation = new URL(serviceEndpointAddress + "/" + portType + "?WSDL");
        QName serviceNameQ = new QName(nameSpace, serviceName);
        
        Service service = Service.create(wsdlLocation, serviceNameQ);
        
        HelloWebserviceIfc client = (HelloWebserviceIfc)service.getPort(HelloWebserviceIfc.class);

        log.info(client.hello());
    }
}
