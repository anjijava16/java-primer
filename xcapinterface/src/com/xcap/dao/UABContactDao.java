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
	public List<UABContactEntity> getList(String userId){
		return get(userId,-1);	
	}
	
	@TransactionAttribute(value=TransactionAttributeType.NEVER)
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
	@TransactionAttribute(value=TransactionAttributeType.NEVER)
	public UABContactEntity getById(String userId,long contactId){
		StringBuilder temp = new StringBuilder("select * from t_contacts where id=:id and user_id=:userId");
		Query query = em.createNativeQuery(temp.toString(), UABContactEntity.class);
		query.setParameter("id", contactId);
		query.setParameter("userId", userId);
		
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
	public UABContactEntity getByIndex(String userId,int index){
		List<UABContactEntity> list = get(userId, index);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}
	
	@TransactionAttribute(value=TransactionAttributeType.NEVER)
	public UABContactEntity getByContactMethod(String userId,String method){
		StringBuilder temp = new StringBuilder("select * from t_contacts where contact_method=:method and user_id=:userId");
		Query query = em.createNativeQuery(temp.toString(), UABContactEntity.class);
		query.setParameter("method", method);
		query.setParameter("userId", userId);
		
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
	private List<UABContactEntity> get(String userId,int index){
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
			Query query = em.createNativeQuery(sql, UABContactEntity.class);
			query.setParameter("user_id", userId);
			
			List<UABContactEntity> list = query.getResultList();
			return list.size() > 0 ? list : null;			
		}
		return null;
	}	
	
	@SuppressWarnings("unchecked")
	public Contact saveOrUpdate(UABContactEntity contact) {
		String ql = "from UABContactEntity c where c.userId=?1 and (c.contactMethod like concat('%',?2) or ?2 like concat('%',c.contactMethod))";
		Query query = em.createQuery(ql);
		query.setParameter(1, contact.getUserId());
		query.setParameter(2, contact.getContactMethod());
		log.info("userId = " + contact.getUserId() + ",method = "
				+ contact.getContactMethod());
		List<UABContactEntity> list = query.getResultList();
		UABContactEntity et = null;
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
			UABContactEntity entity = list.get(0);
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
