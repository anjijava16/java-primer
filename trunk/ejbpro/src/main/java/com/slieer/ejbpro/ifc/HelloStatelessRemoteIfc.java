package com.slieer.ejbpro.ifc;

public interface HelloStatelessRemoteIfc {
	public String HELLO_REMOTE_JNDI = "HelloStatelessRemote";
	
	public String hello();
	
	public void scheduleTimer(long milliseconds);  
	
	public void testMDB();
	
	public void ejbMyThread();
}
