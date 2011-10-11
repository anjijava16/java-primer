package com.xcap.ejb;

import org.apache.log4j.Logger;

import com.xcap.ifc.XCAPDatebaseIfc;

//@stateless
//@local()
public class ContactListsApp implements XCAPDatebaseIfc{
	public static final Logger log = Logger.getLogger(ContactListsApp.class);
	
	
	final static String CONTACT_LISTS_NODE = "contact-lists";
	final static String CONTACT_NODE = "contact";
	final static String LIST_NODE = "list";
	final static String NIKENAME_NODE = "nikename";


	public String get(String userId, String documentSelector,
			String nodeSelector) {
		if(nodeSelector == null){
			
		}
		
		return null;
	}

	public String put(String userId, String documentSelector,
			String nodeSelector) {
		return null;
	}

	public String delete(String userId, String documentSelector,
			String nodeSelector) {
		return null;
	}
}
