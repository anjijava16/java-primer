package com.slieer.ejbpro.ejb;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Init;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.ejb.Stateful;

import org.jboss.ejb3.annotation.CacheConfig;

import com.slieer.ejbpro.ifc.HelloStatefulRemoteIfc;

@Stateful
@CacheConfig(idleTimeoutSeconds = 300)
@Remote(value = HelloStatefulRemoteIfc.class)
public class HelloStatefulEjb implements HelloStatefulRemoteIfc, Serializable {
	private static final long serialVersionUID = -5274979775209055639L;

	int counter;

	public HelloStatefulEjb() {
		System.out.println("in construction.");
	}

	public void doSomething() {
		counter++;
		System.out.println("Value of counter is " + counter);

	}

	@PostConstruct
	public void initialize() {
		// As with stateless EJB, the container immediately calls the
		// annotated method after a bean instance
		// is instantiated.
	}

	@PreDestroy
	public void exit() {
		// The annotated method is called before the container destroys
		// an unused or expired bean instance from its object pool.
	}

	@PrePassivate
	public void beforePassivate() {
		// Called before SFSB is passivated
	}

	@PostActivate
	public void afterActivation() {
		// Called before after SFSB is restored
	}

	@Init
	public void init() {
		// Called to initialize SFSB
	}

	@Remove
	public void stopSession() {
		// Call to this method signals the container
		// to remove this bean instance and terminates
		// the session. The method body can be empty.
	}

}