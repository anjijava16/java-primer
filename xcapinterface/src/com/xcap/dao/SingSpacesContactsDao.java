package com.xcap.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.xcap.dao.entity.SingSpacesContactEntity;

/**
 * 
 *<li>author slieer</li>
 *<li>Create Date 2011-10-29</li>
 *<li>version 1.0</li>
 */
public class SingSpacesContactsDao {
	private EntityManager em;
	public SingSpacesContactsDao(EntityManager em){
		this.em = em; 
	}
	
	public SingSpacesContactEntity getById(long userId,long id){
		Query query = em.createNamedQuery("getByUserIdAndIdAndStatus");
		query.setParameter("userId", userId);
		query.setParameter("id", id);
		query.setParameter("status", 1);
		
		@SuppressWarnings("unchecked")
		List<SingSpacesContactEntity> list = query.getResultList();
		return list.size() > 0 ? list.get(0) : null;
	}
	
	public SingSpacesContactEntity getByIndex(long userId, int index){
		Query query = em.createNamedQuery("byIndexAndStatus");
		query.setParameter("userId", userId);
		query.setParameter("index", index);
		query.setParameter("status", 1);

		@SuppressWarnings("unchecked")
		List<SingSpacesContactEntity> list = query.getResultList();
		
		return list.size() > 0 ? list.get(0) : null;
	}
	
	public SingSpacesContactEntity getByUniqueAttr(long userId, int uniqueAttr){
		return getById(userId, uniqueAttr);
	}
	
	public List<SingSpacesContactEntity> getList(long userId, int status){
		Query query = em.createNamedQuery("listByUserIdAndStatus");
		query.setParameter("userId", userId);
		query.setParameter("status", status);
		
		@SuppressWarnings("unchecked")
		List<SingSpacesContactEntity> list = query.getResultList();
		return list.size() > 0 ? list : null;
	}
}
