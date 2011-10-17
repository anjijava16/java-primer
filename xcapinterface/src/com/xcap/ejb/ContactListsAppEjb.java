package com.xcap.ejb;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	public ResultData get(String userId, String nodeSelector) {
		StringBuilder xmlBuilder = new StringBuilder("<?xml version='1.0' encoding='UTF-8'?>");
		//log.info("----------nodeSelector:" + nodeSelector);
		
		if (nodeSelector == null || nodeSelector.equals(Constants.APP_USAGE_CONTACT)) {
			xmlBuilder.append("<contacts xmlns=\"contact-lists\" ")
			.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ")					
			.append("xsi:schemaLocation=\"contact-lists site/contact-list-1.xsd\">");
			List<ContactEntity> list = contactsDao.getList(userId);
			constructContactNode(xmlBuilder, list);
			
			xmlBuilder.append("</contacts>");
			return new ResultData(ResultData.STATUS_200,xmlBuilder.toString());
		}else{
			String sel = null;
			try {
				sel = URLDecoder.decode(nodeSelector,"utf-8");
			} catch (UnsupportedEncodingException e) {
				log.error("url UnsupportedEncodingException");
				return  new ResultData(ResultData.STATUS_409, new XCAPErrors.NotUTF8ConflictException().getResponseContent());
			}
			//by tagName ,                        
			//by unique unique attr,   
			//by index 
			/*
0			contacts/contact
			contacts/contact[1]
			contacts/contact[@id="1"]
			
1、			contacts/list
			contacts/list[1]
			contacts/list[@id="close-friends"]			
			
2、			contacts/list[@id="close-friends"]/contact
			contacts/list[@id="close-friends"]/contact[1]
			contacts/list[@id="close-friends"]/contact[@id="1"]
	
3、                       1,2 组合

4、			method/contactName/userId/createDate
			*/
			
			log.info("decode node selector:" + sel);
			if(sel.startsWith(Constants.APP_USAGE_CONTACT)){
				//sel.indexOf(Constants.APP_USAGE_CONTACT.concat("/"));
				String condition[] = sel.split("/");
				
				if(condition.length == 2){
					String condition1 = condition[1];
					log.info("--------------condition1:" + condition1);
					if(condition1.startsWith(ContactListsAppEjb.CONTACT_NODE)){
						if(condition1.equals(ContactListsAppEjb.CONTACT_NODE)){
							long size = contactsDao.getListSize(userId);
							if(size == 0){
								log.info("was not found in the document.");
								return new ResultData(ResultData.STATUS_404,"");
							}else if(size == 1){
								StringBuilder contactXml = new StringBuilder();
								
								constructContactNode(contactXml,
										contactsDao.getList(userId));
								return new ResultData(ResultData.STATUS_200, contactXml.toString());
							}else{
								new ResultData(ResultData.STATUS_409, new XCAPErrors.NoParentConflictException("Contacts").getResponseContent());
							}
						}else if(condition1.matches("^contact\\[\\d+\\]$")){
							log.info("----------get contact node by index");
							Pattern p = Pattern.compile("\\d+");
							Matcher match = p.matcher(condition1);
							if(match.find()){
								int id = Integer.valueOf(match.group(0));
								log.info("-----------------id:" + id);
								ContactEntity entity = contactsDao.getById(userId, id);
								return new ResultData(entity != null ? ResultData.STATUS_200 : ResultData.STATUS_404, "");
							}							
							
						}else if(condition1.matches("contact\\[@id=\"\\d+\"\\]")){
							log.info("----------get contact node by database id.");
						}else{
							return new ResultData(ResultData.STATUS_404, "");
						}
					}else if(condition1.startsWith(ContactListsAppEjb.LIST_NODE)){
						 
					}
				}
				
				
			}
		}
		
		log.info("ejb server return 404 error.");
		return new ResultData(ResultData.STATUS_404, null);
	}

	private static void constructContactNode(StringBuilder xmlBuilder,
			List<ContactEntity> list) {
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
	}

	public ResultData put(String userId, String nodeSelector) {
		return null;
	}

	public ResultData delete(String userId, String nodeSelector) {
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
