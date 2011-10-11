package com.xcap.ifc;


public interface XCAPDatebaseIfc {
	public String get(String userId,String documentSelector,String nodeSelector);
	
	public String put(String userId,String documentSelector,String nodeSelector);
	
	public String delete(String userId,String documentSelector,String nodeSelector);
	
}
