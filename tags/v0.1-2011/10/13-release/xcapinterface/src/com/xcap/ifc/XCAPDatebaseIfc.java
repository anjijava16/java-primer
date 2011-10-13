package com.xcap.ifc;


public interface XCAPDatebaseIfc {
	public static String CONTACT_JNDI = "ContactListsApp/local";
	
	public String get(String userId, String nodeSelector);
	
	public String put(String userId, String nodeSelector);
	
	public String delete(String userId, String nodeSelector);
	
}
