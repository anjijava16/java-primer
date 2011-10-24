package com.xcap.dao;


import java.math.BigInteger;
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
public class ContactsOnlyReadDao {
	public static final Logger log = Logger.getLogger(ContactsOnlyReadDao.class);	
	
	private EntityManager em;
	public ContactsOnlyReadDao(EntityManager em){
		this.em = em;
	}
	
	
	
	/**
	 * @param userId
	 * @return null or list(size > 0)
	 */
	public List<ContactEntity> getList(String userId){
		return get(userId,-1);	
	}
	
	public long getListSize(String userId){
		String sql = "select count(id) from t_contacts where user_id = :user_id";
		Query query = em.createNativeQuery(sql);
		query.setParameter("user_id", userId);
		BigInteger re = (BigInteger)query.getSingleResult();
		return re.longValue();
	}
	
	/**
	 * 
	 * @param userId
	 * @param contactId
	 * @return null or a contact
	 */
	public ContactEntity getById(String userId,long contactId){
		StringBuilder temp = new StringBuilder("select * from t_contacts where id=:id and user_id=:userId");
		Query query = em.createNativeQuery(temp.toString(), ContactEntity.class);
		query.setParameter("id", contactId);
		query.setParameter("userId", userId);
		
		@SuppressWarnings("unchecked")
		List<ContactEntity> list = query.getResultList();
		return (ContactEntity)(list.size() != 0 ? list.get(0) : null);
	}
	
	/**
	 * @param userId
	 * @param index > 1, other is invalidate
	 * @return a contact or null;
	 */
	public ContactEntity getByIndex(String userId,int index){
		List<ContactEntity> list = get(userId, index);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}
	
	public ContactEntity getByContactMethod(String userId,String method){
		StringBuilder temp = new StringBuilder("select * from t_contacts where contact_method=:method and user_id=:userId");
		Query query = em.createNativeQuery(temp.toString(), ContactEntity.class);
		query.setParameter("method", method);
		query.setParameter("userId", userId);
		
		@SuppressWarnings("unchecked")
		List<ContactEntity> list = query.getResultList();
		return (ContactEntity)(list.size() != 0 ? list.get(0) : null);
		
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
			index -= 1;
			if(index >= 0){
				StringBuilder temp = new StringBuilder("select * from t_contacts where user_id = :user_id limit ");
				temp.append(index)
					.append(",1");
				sql = temp.toString();
				log.info(sql);
			}else{
				sql = null;
				log.error("input index ");			
			}
		}
		
		if(sql != null){
			Query query = em.createNativeQuery(sql, ContactEntity.class);
			query.setParameter("user_id", userId);
			
			List<ContactEntity> list = query.getResultList();
			return list.size() > 0 ? list : null;			
		}
		return null;
	}
}
