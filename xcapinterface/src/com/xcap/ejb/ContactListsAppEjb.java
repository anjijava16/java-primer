package com.xcap.ejb;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import com.xcap.ifc.Constants;
import com.xcap.ifc.Contact;
import com.xcap.ifc.XCAPDatebaseIfc;
import com.xcap.ifc.error.XCAPErrors;

@Stateless
@Local(value = XCAPDatebaseIfc.class)
@LocalBinding(jndiBinding = "ContactListsApp")
public class ContactListsAppEjb implements XCAPDatebaseIfc {
	public static final Logger log = Logger.getLogger(ContactListsAppEjb.class);

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
		contactsDao = new ContactsDao(em);
	}

	public String get(String userId, String nodeSelector) {
		StringBuilder xmlBuilder = new StringBuilder("<?xml version='1.0' encoding='UTF-8'?>");
		//log.info("----------nodeSelector:" + nodeSelector);
		
		if (nodeSelector == null || nodeSelector.equals(Constants.APP_USAGE_CONTACT)) {
			xmlBuilder.append("<contacts xmlns=\"contact-lists\" ")
			.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ")					
			.append("xsi:schemaLocation=\"contact-lists site/contact-list-1.xsd\">");
			List<ContactEntity> list = contactsDao.getList(userId);
			if(list != null){
					
				for(ContactEntity en : list){
					String method = en.getContactMethod();
					String contactName = en.getContactName();
					String createDate = dateFormat.format(en.getCreateDate());
					String userIdTemp = en.getUserId().toString();
					String id = en.getId().toString();
					
					xmlBuilder.append("<contact id=\"".concat(id).concat("\">"))
					.append("<method>".concat(method != null ? method : "").concat("</method>"))
					.append("<contactName>".concat(contactName != null ? contactName : "").concat("</contactName>"))
					.append("<userId>".concat(userIdTemp).concat("</userId>"))
					.append("<createDate>".concat(createDate).concat("</createDate>"))
					.append("</contact>");
					
				}
			}
			
			xmlBuilder.append("</contacts>");
		}else{
			String sel = null;
			try {
				sel = URLDecoder.decode(nodeSelector,"utf-8");
			} catch (UnsupportedEncodingException e) {
				log.error("url UnsupportedEncodingException");
				return new XCAPErrors.NotUTF8ConflictException().getResponseContent();
			}
			//by tagName ,           contacts/contact             method/contactName/userId/createDate
			//by unique unique attr, contacts/contact[@id="sex"]  
			//by index->             contacts/contact[1]         
			log.info("decode node selector:" + sel);
			sel.indexOf(Constants.APP_USAGE_CONTACT);
			
		}
		return xmlBuilder.toString();
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
