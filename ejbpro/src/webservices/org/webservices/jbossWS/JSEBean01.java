package org.webservices.jbossWS;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;


/**
 * @author me
 * 此webservice 被打进war包里.
 */
@WebService    
@SOAPBinding(style = SOAPBinding.Style.RPC)    
public class JSEBean01    {       
	@WebMethod       
	public String echo(String input){               
		System.out.println("------" + input);
		return "----==" + input;
	}    
}