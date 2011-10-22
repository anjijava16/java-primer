package com.slieer.ejbpro.ejb.webservice;

import javax.ejb.Stateless;
import javax.jws.WebService;

@Stateless
@WebService(name = "HelloWebService")
public class HelloWebService {
	
	public String sayHello(String name) {
		System.out.println("客户端调用了服务器端的代码");
		return "你好:" + name + ",传授给你武功--旋空斩";
	}
}
