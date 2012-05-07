package com.slieer.ejbpro.ejb;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.jboss.ejb3.annotation.RemoteBinding;
import org.jboss.logging.Logger;

import com.slieer.ejbpro.ifc.InterceptorTestRemoteIfc;


@Stateless(name="InterceptorTest", mappedName="InterceptorTestEJB", description="")
@Remote(value=InterceptorTestRemoteIfc.class)
@RemoteBinding(jndiBinding="SampleEJB")
@Interceptors(value=MyInterceptor.class)

public class InterceptorTestEjb implements InterceptorTestRemoteIfc {
	private Logger log = Logger.getLogger(getClass());

	@Override
	public String doSomething(String param) {
		log.info("param:" + param);
		return "Hi,".concat(param); 
		
	}
}
