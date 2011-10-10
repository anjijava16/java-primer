package com.xcap.ejb;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.borqs.contact.ifc.Contact;
import com.xcap.ifc.XCAPDatebaseIfc;

//@stateless
//@local()
public class ContactListsApp implements XCAPDatebaseIfc{
	final static String CONTACT_LISTS_NODE = "contact-lists";
	final static String CONTACT_NODE = "contact";
	final static String LIST_NODE = "list";
	final static String NIKENAME_NODE = "nikename";

	public boolean validateDocument(String xml) {
		// TODO Auto-generated method stub
		return false;
	}

	public String get(String userId, String documentSelector,
			String nodeSelector) {
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
