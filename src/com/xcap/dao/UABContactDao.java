package com.xcap.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xcap.dao.entity.UABContactEntity;
import com.xcap.ifc.Contact;

/**
 * 
 *<li>author slieer</li>
 *<li>Create Date 2011-10-29</li>
 *<li>version 1.0</li>
 */
public class UABContactDao {
	private Log log = LogFactory.getLog(this.getClass().getName());
	private EntityManager em;

	public UABContactDao(EntityManager em) {
		this.em = em;
	}

	/**
	 * @param userId
	 * @return null or list(size > 0)
	 */
	@TransactionAttribute(value=TransactionAttributeType.NEVER)
	public List<UABContactEntity> getList(String msisdn){
		return get(msisdn,-1);	
	}
	
	@TransactionAttribute(value=TransactionAttributeType.NEVER)
	public long getListSize(String msisdn){
		Query query = em.createNamedQuery("getListSizeByUserMsisdn");
		query.setParameter("msisdn", msisdn);
		BigInteger re = (BigInteger)query.getSingleResult();
		return re.longValue();
	}
	
	/**
	 * 
	 * @param userId
	 * @param contactId
	 * @return null or a contact
	 */
	@TransactionAttribute(value=TransactionAttributeType.NEVER)
	public UABContactEntity getById(String msisdn,long contactId){
		Query query = em.createNamedQuery("getByMsisdnAndId");
		query.setParameter("id", contactId);
		query.setParameter("msisdn", msisdn);
		
		@SuppressWarnings("unchecked")
		List<UABContactEntity> list = query.getResultList();
		return (UABContactEntity)(list.size() != 0 ? list.get(0) : null);
	}
	
	/**
	 * @param userId
	 * @param index > 1, other is invalidate
	 * @return a contact or null;
	 */
	@TransactionAttribute(value=TransactionAttributeType.NEVER)
	public UABContactEntity getByIndex(String msisdn,int index){
		List<UABContactEntity> list = get(msisdn, index);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}
	
	@TransactionAttribute(value=TransactionAttributeType.NEVER)
	public UABContactEntity getByContactMethod(String msisdn,String method){
		Query query = em.createNamedQuery("getByMethodAndMsisdn");
		query.setParameter("method", method);
		query.setParameter("msisdn", msisdn);
		
		@SuppressWarnings("unchecked")
		List<UABContactEntity> list = query.getResultList();
		return (UABContactEntity)(list.size() != 0 ? list.get(0) : null);
		
	}
			
	/**
	 * 
	 * @param userId
	 * @param index = -1 getList by userId; index > 1 getList by userId,index; other is invalidate;
	 * @return list size > 0 return list,or return null;
	 */
	@SuppressWarnings("unchecked")
	@TransactionAttribute(value=TransactionAttributeType.NEVER)
	private List<UABContactEntity> get(String msisdn,int index){
		Query query = null;
		if(index < 0){
			query = em.createNamedQuery("getListByMsisdn");
			query.setParameter("msisdn", msisdn);			
		}else{
			index -= 1;
			if(index >= 0){
				query = em.createNamedQuery("getByMsisdnAndIndex");
				query.setParameter("msisdn", msisdn);
				query.setParameter("index", index);
			}else{
				log.error("input index error, index =" + index);			
			}
		}
		
		if(query != null){
			List<UABContactEntity> list = query.getResultList();
			return list.size() > 0 ? list : null;						
		}
		
		return null;
	}	
	
	@SuppressWarnings("unchecked")
	public Contact saveOrUpdate(UABContactEntity contact) {
		String ql = "from UABContactEntity c where c.msisdn=?1 and (length(?2)>7 or length(?2)=length(c.contactMethod)) and (replace(c.contactMethod,'+','') like concat('%',replace(?2,'+','')) or replace(?2,'+','') like concat('%',replace(c.contactMethod,'+','')))";
		Query query = em.createQuery(ql);
		query.setParameter(1, contact.getMsisdn());
		query.setParameter(2, contact.getContactMethod());
		log.info("msisdn = " + contact.getMsisdn() + ",method = "
				+ contact.getContactMethod() + ", device_id =" + contact.getDeviceID() + ", raw_id = " +contact.getRawID() );
		List<UABContactEntity> list = query.getResultList();
		UABContactEntity et = null;
		Contact c = new Contact();
		if (list.size() < 1 || contact.getRawID() != null) {
			contact.setCreateDate(new Date(System.currentTimeMillis()));
			et = em.merge(contact);
			log.info("create contact success");
			c.setOp("");
			em.flush();
			// log.info("et = " + et );
			c.setId(et.getId());
			c.setMsisdn(et.getMsisdn());
			c.setContactMethod(et.getContactMethod());
			c.setContactName(et.getContactName());
			c.setContactNickName(et.getDescription());
			c.setDeviceID(et.getDeviceID());
			c.setRawID(et.getRawID());
		} else {
			UABContactEntity entity = list.get(0);
			entity.setContactName(contact.getContactName());
			entity.setDeviceID(contact.getDeviceID());
			entity.setRawID(contact.getRawID());
			entity.setCreateDate(new Date(System.currentTimeMillis()));
			 
			et = em.merge(entity);
			em.flush();
			c.setOp("modi");
			c.setId(et.getId());
			c.setMsisdn(et.getMsisdn());
			c.setContactMethod(contact.getContactMethod());
			c.setContactName(contact.getContactName());
			c.setContactNickName(contact.getDescription());
			c.setDeviceID(et.getDeviceID());
			c.setRawID(et.getRawID());
		}

		return c;
	}
	
	public int deleteContacts(String msisdn){
		Query query = em.createNamedQuery("delByMsisdn");
		query.setParameter("msisdn", msisdn);
		return query.executeUpdate();
	}
		
	/**
	 * @param userId
	 * @param index >= 1
	 * @return -1 index invalid ,or delete row mount.
	 */
	public int deleteContactByIndexSelector(String msisdn, int index){
		if(index >= 1){
			Query query = em.createNamedQuery("getByMsisdnAndIndex");
			query.setParameter("msisdn", msisdn);
			query.setParameter("index", index -1);  //xcap selector index start with 1, but mysql limit index start with 0.
			@SuppressWarnings("unchecked")
			List<UABContactEntity> list = query.getResultList();
			if(list.size() > 0){
				em.remove(list.get(0));
				return 1;
			}
		}	
		return -1;
	}
	
	public int deleteContactByUniqueAttr(String msisdn, String method){
		Query query = em.createNamedQuery("delByMsisdnAndMethod");
		query.setParameter("msisdn", msisdn);
		query.setParameter("method", method);
		return query.executeUpdate();
	}
	
	
}
