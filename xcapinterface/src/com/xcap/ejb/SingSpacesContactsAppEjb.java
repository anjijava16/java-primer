package com.xcap.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.ejb3.annotation.LocalBinding;

import com.xcap.dao.SingSpacesContactsDao;
import com.xcap.ifc.XCAPDatebaseLocalIfc;

/**
 * 
 * <li>author slieer</li>
 * <li>Create Date 2011-10-29</li>
 * <li>version 1.0</li>
 */
@Stateless
@Local(value = XCAPDatebaseLocalIfc.class)
@LocalBinding(jndiBinding = XCAPDatebaseLocalIfc.SING_SPACE_CONTACTS_LOCAL_JNDI)
public class SingSpacesContactsAppEjb implements XCAPDatebaseLocalIfc{
	
	@PersistenceContext(unitName="SingSpacesXCAP")
	EntityManager em;

	private SingSpacesContactsDao contactsDao;
	
	@PostConstruct
	public void postConstruct() {
		contactsDao = new SingSpacesContactsDao(em);
	}
	
	
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
