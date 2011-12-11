package com.slieer;

import java.net.URL;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.slieer.ejbpro.ejb.webservice.Car;
import com.slieer.ejbpro.ejb.webservice.CarWebService;

public class CarClientTest {
	static  String wsdl = "http://localhost:8080/ejbpro_ejb/CarWebService?wsdl";
  public static void main(String[] args) throws Exception {

    //Specify the WSDL 
    URL wsdlLocation = new URL(wsdl);
    
    //Create a Qualified Name that represents the 
    //namespace and local part of the service
    QName serviceName = new QName("http://webservice.ejb.ejbpro.slieer.com/", 
            "CarWebServiceService");
    
    //Create a proxy to get a port stub from
    Service service = Service.create(wsdlLocation, serviceName);
            
    // Return a list of QNames of ports
    System.out.println("QNames of service endpoints:");
    Iterator<QName> it = service.getPorts();
    QName lastEndpoint = null;
    while (it.hasNext()) {
    lastEndpoint = it.next();
        System.out.println("Name: " + lastEndpoint);
    }

    // Get the Hello stub
    CarWebService hello = service.getPort(lastEndpoint, CarWebService.class);
    
    //Invoke the business method
    Car result = hello.callCar("Red car.");
    System.out.println("\nResponse: " + result);
  }
}
