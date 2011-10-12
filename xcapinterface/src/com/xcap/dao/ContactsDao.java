package com.xcap.dao;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.xcap.dao.entity.ContactEntity;

/**
 * Create Date 2011-10-12
 * @author slieer
 * version 1.0
 */
public class ContactsDao {
	public static final Logger log = Logger.getLogger(ContactsDao.class);	
	
	private EntityManager em;
	public ContactsDao(EntityManager em){
		this.em = em;
	}
	
	/**
	 * @param userId
	 * @return null or list(size > 0)
	 */
	public List<ContactEntity> getList(String userId){
		return get(userId,-1);	
	}
	
	/**
	 * @param userId
	 * @param index > 1, other is invalidate
	 * @return a contact or null;
	 */
	public ContactEntity getByIndex(String userId,int index){
		List<ContactEntity> list = get(userId, index);
		return list.size() > 0 ? list.get(0) : null;
	}
	
	public ContactEntity getByUniqueAttribute(String userId,int index){
		return null;
	}
		
	/**
	 * 
	 * @param userId
	 * @param index = -1 getList by userId; index > 1 getList by userId,index; other is invalidate;
	 * @return list size > 0 return list,or return null;
	 */
	@SuppressWarnings("unchecked")
	private List<ContactEntity> get(String userId,int index){
		String sql = null; 
		
		if(index < 0){
			sql = "select * from t_contacts where user_id = :user_id";
		}else{
			index = - 1;
			if(index >= 0){
				StringBuilder temp = new StringBuilder("select * from t_contacts where user_id = :user_id limit ");
				temp.append(index)
					.append(",1");
				sql = temp.toString();
				log.info(sql);
			}else{
				log.error("input index ");			
			}
		}
		Query query = em.createNativeQuery(sql, ContactEntity.class);
		query.setParameter("user_id", userId);
		
		List<ContactEntity> list = query.getResultList();
		return list.size() > 0 ? list : null;
		
	}
}
