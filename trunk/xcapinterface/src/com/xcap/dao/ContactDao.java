package com.xcap.dao;

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

	public void updateContact(ContactEntity contact) {

	}

	public void deleteContactById(Long id) {
		ContactEntity entity = em.find(ContactEntity.class, id);
		if (entity != null) {
			em.remove(entity);
		}

	}

	public void deleteContact(Contact contact) {
		ContactEntity entity = em.find(ContactEntity.class, contact.getId());
		if (entity != null) {
			if (contact.getContactName() == null
					|| entity.getContactName() == null) {
				em.remove(entity);
			} else {
				if (contact.getContactName().equals(entity.getContactName())) {
					em.remove(entity);
				}
			}

		}
	}

	@SuppressWarnings("unchecked")
	public List<ContactEntity> queryUserContacts(Long userId) {
		String ql = "select c from ContactEntity c where c.userId=?1";
		Query query = em.createQuery(ql);
		query.setParameter(1, userId);

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ContactEntity> queryUserRosters(Long userId) {
		log.info("**********" + userId + "************");
		String ql = "select c from ContactEntity c where c.userId=?1 and exists (select ur from UserRefEntity ur where (ur.mobile like concat('%',c.contactMethod) or c.contactMethod like concat('%',ur.mobile)) and ur.rel=?2) order by c.id asc";
		Query query = em.createQuery(ql);
		query.setParameter(1, userId);
		query.setParameter(2, "IM");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ContactEntity> queryRecommendFriends(Long userId, String mobile) {
		String ql = "from ContactEntity c where c.contactMethod=?1 and c.contactMethod not in (select cc.contactMethod from ContactEntity cc where cc.userId=?2)";
		Query query = em.createQuery(ql);
		query.setParameter(1, mobile);
		query.setParameter(2, userId);
		List<ContactEntity> list = query.getResultList();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<ContactEntity> queryToUserList(String mobile) {
		String ql = "from ContactEntity c where c.contactMethod like concat('%',?1) or ?1 like concat('%',c.contactMethod)";
		Query query = em.createQuery(ql);
		query.setParameter(1, mobile);
		return query.getResultList();
	}

/*	public boolean isIMUser(String mobile) {
		UserRefEntity entity = queryUserRef(mobile,"IM");
		if (entity!=null) {
			return true;
		}
		return false;
	}*/

	public void userPhoneNoReceivedNotify(Long userId, String phoneNo) {
		/*
		 * String ql = "from ContactEntity c where c.contactMethod=?1"; Query
		 * query = em.createQuery(ql); query.setParameter(1, phoneNo);
		 * List<ContactEntity> entities = query.getResultList();
		 * for(ContactEntity entity : entities){
		 * entity.setContactUserId(userId); em.merge(entity); }
		 */
	}

	public void updateUserRef() {

	}

	public ContactEntity getContactById(Long id) {
		ContactEntity entity = em.find(ContactEntity.class, id);
		return entity;
	}

	@SuppressWarnings("unchecked")
	public void updateUserBlockingStatus(Long userId, String MSISDN,
			Integer blocking) {
		String ql = "from ContactEntity c where c.userId=?1 and c.contactMethod=?2";
		Query query = em.createQuery(ql);
		query.setParameter(1, userId);
		query.setParameter(2, MSISDN);
		List<ContactEntity> entities = query.getResultList();
		if (entities.size() > 0) {
			ContactEntity entity = entities.get(0);
			entity.setBlocking(blocking);
			em.merge(entity);
		}
	}

	@SuppressWarnings("unchecked")
	public void updatePhoneUserId(String phoneNumber, Long newUserId) {
		/*
		 * String ql = "from ContactEntity c where c.contactMethod=?1"; Query
		 * query = em.createQuery(ql); query.setParameter(1, phoneNumber);
		 * List<ContactEntity> entities = query.getResultList();
		 * for(ContactEntity entity : entities){
		 * entity.setContactUserId(newUserId); em.merge(entity); }
		 */
	}

	/*
	 * delete if there exists self contact
	 */
	@SuppressWarnings("unchecked")
	public void deleteSelfContact(String phoneNumber, Long newUserId) {
		String ql = "from ContactEntity c where c.userId=?1 and c.contactMethod=?2";
		Query query = em.createQuery(ql);
		query.setParameter(1, newUserId);
		query.setParameter(2, phoneNumber);

		List<ContactEntity> entities = query.getResultList();
		if (entities.size() > 0) {
			em.remove(entities.get(0));
		}
	}

}
