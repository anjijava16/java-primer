package com.slieer.ejbpro.ifc;

public interface HelloStatelessLocalIfc {
	public String HELLO_LOCAL_JNDI = "HelloStatelessLocal";
	
	public String hello();
	
	public void scheduleTimer(long milliseconds);  
	
	public void testMDB();
}
