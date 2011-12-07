package com.sc.ch04;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(endpointInterface = "com.sc.ch04.Hello")
public class HelloWS implements Hello {

	@WebMethod
	public String sayHello(String name) {
		return "Hello, " + name + "!";
	}
}
