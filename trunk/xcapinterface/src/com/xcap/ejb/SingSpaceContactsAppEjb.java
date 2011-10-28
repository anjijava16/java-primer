package com.xcap.ejb;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.ejb3.annotation.LocalBinding;

import com.xcap.ifc.XCAPDatebaseLocalIfc;

/**
 * 
 * @author slieer
 * Create Date2011-10-28
 * version 1.0
 */
@Stateless
@Local(value = XCAPDatebaseLocalIfc.class)
@LocalBinding(jndiBinding = XCAPDatebaseLocalIfc.SING_SPACE_CONTACTS_LOCAL_JNDI)
public class SingSpaceContactsAppEjb implements XCAPDatebaseLocalIfc{

	@Override
	public ResultData get(String userId, String nodeSelector) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultData put(String userId, String nodeSelector, String xml) {
		return null;
	}

	@Override
	public ResultData delete(String userId, String nodeSelector) {
		return null;
	}

	
}
