package com.slieer.ejbpro.ejb.webservice;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
/**
 * @author me
 * 此webservice 被打进ejb jar包里
 */

@Stateless
@WebService(name = "HelloWebService")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class HelloWebService {
	
	@WebMethod
	public String sayHello(String name) {
		System.out.println("客户端调用了服务器端的代码");
		return "你好:" + name + ",传授给你武功--旋空斩";
	}
}
