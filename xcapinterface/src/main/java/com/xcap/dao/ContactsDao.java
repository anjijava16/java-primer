package com.xcap.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.xcap.dao.entity.ContactEntity;

public class ContactsDao {
	private EntityManager em;
	public ContactsDao(EntityManager em){
		this.em = em;
	}
	
	public List<ContactEntity> getList(String userId){
		return null;
	}
	
	public ContactEntity getByIndex(String userId,int index){
		return null;
	}
	
	public ContactEntity getByUniqueAttribute(String userId,int index){
		return null;
	}	
}
