package com.xcap.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xcap.dao.entity.ContactEntity;
import com.xcap.ifc.Contact;


public class ContactDao {
	private Log log = LogFactory.getLog(this.getClass().getName());
	private EntityManager em;

	public ContactDao(EntityManager em) {
		this.em = em;
	}

	@SuppressWarnings("unchecked")
	public Contact saveOrUpdate(ContactEntity contact) {
		String ql = "from ContactEntity c where c.userId=?1 and (c.contactMethod like concat('%',?2) or ?2 like concat('%',c.contactMethod))";
		Query query = em.createQuery(ql);
		query.setParameter(1, contact.getUserId());
		query.setParameter(2, contact.getContactMethod());
		log.info("userId = " + contact.getUserId() + ",method = "
				+ contact.getContactMethod());
		List<ContactEntity> list = query.getResultList();
		ContactEntity et = null;
		Contact c = new Contact();
		if (list.size() < 1) {
			contact.setCreateDate(new Date(System.currentTimeMillis()));
			et = em.merge(contact);
			log.info("create contact success");
			c.setOp("");
			em.flush();
			// log.info("et = " + et );
			c.setId(et.getId());
			c.setUserId(et.getUserId());
			c.setContactMethod(et.getContactMethod());
			c.setContactName(et.getContactName());
			c.setContactNickName(et.getDescription());
			c.setContactType(et.getContactType());
		} else {
			ContactEntity entity = list.get(0);
			/*entity.setContactName(contact.getContactName());
			entity.setContactType(contact.getContactType());*/
			et = entity;
			c.setOp("modi");
			c.setId(et.getId());
			c.setUserId(et.getUserId());
			c.setContactMethod(contact.getContactMethod());
			c.setContactName(contact.getContactName());
			c.setContactNickName(contact.getDescription());
			c.setContactType(contact.getContactType());
		}
		
		return c;
	}
	
	public int deleteContacts(long userId){
		String sql = "delete from t_contacts where user_id = :userId";
		Query query = em.createNativeQuery(sql);
		query.setParameter("userId", userId);
		return query.executeUpdate();
	}
	
	public int deleteContactByTagNameSelector(long userId){
		String sql = "delete from t_contacts where user_id= :userId  limit 1";
		Query query = em.createNativeQuery(sql);
		query.setParameter("userId", userId);
		return query.executeUpdate();
	}
	
	/**
	 * @param userId
	 * @param index >= 1
	 * @return -1 index invalid ,or delete row mount.
	 */
	public int deleteContactByIndexSelector(long userId, int index){
		
		if(index >= 1){
			String queryString = "select id from t_contacts where user_id = :userId limit :index,1";
			Query queryId = em.createNativeQuery(queryString);
			queryId.setParameter("userId", userId);
			queryId.setParameter("index", index -1);  //xcap selector index start with 1, but mysql limit index start with 0.
			
			@SuppressWarnings("rawtypes")
			List list = queryId.getResultList();
			if(list.size() > 0){
				BigInteger idTemp = (BigInteger)list.get(0);
				
				StringBuilder temp = new StringBuilder("delete from t_contacts where id = :id");
				Query delQuery = em.createNativeQuery(temp.toString());
				delQuery.setParameter("id", idTemp.longValue());
				return delQuery.executeUpdate();
			}
		}
		return -1;
	}
	
	public int deleteContactByUniqueAttr(long userId, String method){
		String sql = "delete from t_contacts where user_id = :userId and contact_method = :method";
		Query query = em.createNativeQuery(sql);
		query.setParameter("userId", userId);
		query.setParameter("method", method);
		return query.executeUpdate();
	}
}
