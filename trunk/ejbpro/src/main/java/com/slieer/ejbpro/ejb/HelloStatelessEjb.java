package com.slieer.ejbpro.ejb;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.ejb3.annotation.LocalBinding;

import com.slieer.ejbpro.ifc.HelloStatelessIfc;

@Stateless
@Local(value = HelloStatelessIfc.class)
@LocalBinding(jndiBinding = "HelloStatelessEjb")
public class HelloStatelessEjb implements HelloStatelessIfc {

	@Override
	public String hello() {
		return null;
	}

}
