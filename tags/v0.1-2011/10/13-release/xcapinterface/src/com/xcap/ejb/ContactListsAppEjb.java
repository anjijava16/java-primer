package com.xcap.ejb;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.LocalBinding;

import com.xcap.dao.ContactsDao;
import com.xcap.dao.entity.ContactEntity;
import com.xcap.ifc.Contact;
import com.xcap.ifc.XCAPDatebaseIfc;

@Stateless
@Local(value = XCAPDatebaseIfc.class)
@LocalBinding(jndiBinding = "ContactListsApp")
public class ContactListsAppEjb implements XCAPDatebaseIfc {
	public static final Logger log = Logger.getLogger(ContactListsAppEjb.class);

	final static String CONTACT_LISTS_NODE = "contact-lists";
	final static String CONTACT_NODE = "contact";
	final static String LIST_NODE = "list";
	final static String NIKENAME_NODE = "nikename";

	final static String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
	final static DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
	
	@PersistenceContext(unitName="xcap")
	EntityManager em;
	ContactsDao contactsDao;

	@PostConstruct
	public void postConstruct() {
		log.info("-------------------------ejb---------entity manager:" + em);
		contactsDao = new ContactsDao(em);
	}

	public String get(String userId, String nodeSelector) {
		if (nodeSelector == null || nodeSelector.equals("/".concat(CONTACT_LISTS_NODE))) {
			List<ContactEntity> list = contactsDao.getList(userId);
			StringBuilder xmlBuilder = new StringBuilder("<?xml version='1.0' encoding='UTF-8'?>");
			if(list != null){
				xmlBuilder.append("<contacts xmlns=\"contact-lists\" ")
				.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ")					
				.append("xsi:schemaLocation=\"contact-lists site/contact-list-1.xsd\">");
					
				for(ContactEntity en : list){
					String method = en.getContactMethod();
					String contactName = en.getContactName();
					String createDate = dateFormat.format(en.getCreateDate());
					String userIdTemp = en.getUserId().toString();
					String id = en.getId().toString();
					
					xmlBuilder.append("<contact id=\"".concat(id).concat("\">"))
					.append("<method>".concat(method).concat("</method>"))
					.append("<contactName>".concat(contactName).concat("</contactName>"))
					.append("<userId>".concat(userIdTemp).concat("</userId>"))
					.append("<createDate>".concat(createDate).concat("</createDate>"))
					.append("</contact>");
					
				}
				xmlBuilder.append("</contacts>");
			}
			return xmlBuilder.toString();
		}else{
			//by tagName ,
			//by unique attribute,
			//by index;
			log.info("documentSelector not implements.......");
		}
		return null;
	}

	public String put(String userId, String nodeSelector) {
		return null;
	}

	public String delete(String userId, String nodeSelector) {
		return null;
	}

	public static Contact toBean(ContactEntity entity) {
		Contact dest = new Contact();
		return (Contact) beanConversion(dest, entity);
	}

	public static ContactEntity toEntity(Contact contact) {
		ContactEntity dest = new ContactEntity();
		return (ContactEntity) beanConversion(dest, contact);
	}

	private static Object beanConversion(Object dest, Object orig) {
		try {
			if (orig != null) {
				BeanUtils.copyProperties(dest, orig);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
