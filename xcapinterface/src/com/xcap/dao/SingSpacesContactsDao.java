package com.xcap.dao;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import sun.util.logging.resources.logging;

import com.xcap.dao.entity.SingSpacesContactEntity;
import com.xcap.ejb.SingSpacesContactsAppEjb;

/**
 * 
 *<li>author slieer</li>
 *<li>Create Date 2011-10-29</li>
 *<li>version 1.0</li>
 */
public class SingSpacesContactsDao {
	public static final Logger log = Logger.getLogger(SingSpacesContactsDao.class);	
	final static byte STATUS = 1;
	
	private EntityManager em;
	public SingSpacesContactsDao(EntityManager em){
		this.em = em; 
	}
	
	@TransactionAttribute(value=TransactionAttributeType.NEVER)
	public SingSpacesContactEntity getById(long userId,long id){
		Query query = em.createNamedQuery("getByUserIdAndIdAndStatus");
		query.setParameter("userId", userId);
		query.setParameter("id", id);
		query.setParameter("status", STATUS);
		
		@SuppressWarnings("unchecked")
		List<SingSpacesContactEntity> list = query.getResultList();
		return list.size() > 0 ? list.get(0) : null;
	}
	
	/**
	 * 
	 * @param userId
	 * @param index >=1
	 * @return
	 */
	@TransactionAttribute(value=TransactionAttributeType.NEVER)
	public SingSpacesContactEntity getByIndex(long userId, int index){
		Query query = em.createNamedQuery("byIndexAndStatus");
		query.setParameter("userId", userId);
		query.setParameter("index", index -1);
		query.setParameter("status", STATUS);

		@SuppressWarnings("unchecked")
		List<SingSpacesContactEntity> list = query.getResultList();
		
		return list.size() > 0 ? list.get(0) : null;
	}
	
	@TransactionAttribute(value=TransactionAttributeType.NEVER)
	public SingSpacesContactEntity getByUniqueAttr(long userId, long uniqueAttr){
		return getById(userId, uniqueAttr);
	}
	
	@TransactionAttribute(value=TransactionAttributeType.NEVER)
	public List<SingSpacesContactEntity> getList(long userId){
		Query query = em.createNamedQuery("listByUserIdAndStatus");
		query.setParameter("userId", userId);
		query.setParameter("status", STATUS);
		
		@SuppressWarnings("unchecked")
		List<SingSpacesContactEntity> list = query.getResultList();
		return list.size() > 0 ? list : null;
	}
	
	@TransactionAttribute(value=TransactionAttributeType.NEVER)
	public long getListSize(long userId){
		String sql = "listSizeByUserIdAndStatus";
		Query query = em.createNamedQuery(sql);
		query.setParameter("userId", userId);
		query.setParameter("status", STATUS);

		BigInteger re = (BigInteger)query.getSingleResult();
		return re.longValue();
	}
	
	public void save(long userId, SingSpacesContactEntity en){
		en.setUserId(userId);
		en.setContactStatus(STATUS);
		em.persist(en);
		log.info("persist singspaces contact, id=" + en.getContactId());
	}
	
	public void save(long userId, List<SingSpacesContactEntity> list){
		for(SingSpacesContactEntity en : list){
			save(userId, en);
		}
	}
	
	public int deleteByUserId(long userId){
		Query query = em.createNamedQuery("deleteByUserId");
		query.setParameter("userId", userId);
		return query.executeUpdate();
	}

	public int deleteByUserId(long userId,long contactId){
		Query query = em.createNamedQuery("deleteByUserIdAndContactId");
		return query.executeUpdate();
	}
	
	
	/**
	 * @param userId
	 * @param index >= 1
	 * @return -1 index invalid ,or delete OK.
	 */
	public int deleteContactByIndexSelector(long userId, int index){
		
		if(index >= 1){
			SingSpacesContactEntity entity = getByIndex(userId,index);			
			if(entity != null){
				em.remove(entity);
				return 1;
			}
		}
		return -1;
	}
}
