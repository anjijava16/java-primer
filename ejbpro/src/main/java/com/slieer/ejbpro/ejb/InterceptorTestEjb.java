package com.slieer.ejbpro.ejb;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.jboss.ejb3.annotation.RemoteBinding;

import com.slieer.ejbpro.ifc.InterceptorTestRemoteIfc;


@Stateless
@Remote(value=InterceptorTestRemoteIfc.class)
@RemoteBinding(jndiBinding="SampleEJB")
@Interceptors(value=MyInterceptor.class)

public class InterceptorTestEjb implements InterceptorTestRemoteIfc {

	public void doSomething(String param) {
		 //
	}
}
