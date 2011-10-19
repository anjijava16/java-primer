package com.slieer.ejbpro.ejb;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;

import com.slieer.ejbpro.ifc.HelloWebserviceIfc;

@Stateless 
@WebService(endpointInterface="examples.ws.HelloWorld",serviceName="Greeter",portName="GreeterPort")  
public class HelloWebserviceEjb implements HelloWebserviceIfc {
	
	@WebMethod
	public String hello() {
		return "Hello I'm slieer use webservice.";
	}

}
