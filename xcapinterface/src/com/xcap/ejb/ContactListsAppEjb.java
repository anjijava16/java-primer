package com.xcap.ejb;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.LocalBinding;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.xcap.dao.ContactDao;
import com.xcap.dao.ContactsOnlyReadDao;
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

	final static String NODE_CONTACTS = "contacts";
	final static String NODE_CONTACT = "contact";
	final static String NODE_LIST = "list";
	
	final static String NODE_LEAF_METHOD = "method"; 
	final static String NODE_LEAF_CONTACT_NAME = "contactName";
	//final static String NODE_LEAF_USER_ID = "userId";
	final static String NODE_LEAF_DESC = "description";
	final static String NODE_LEAF_CREATE_DATE = "createDate";                                    

	final static String ATTR_ID = "id";
	
	@PersistenceContext(unitName="xcap")
	EntityManager em;
	ContactsOnlyReadDao onlyReadContactsDao;
	ContactDao contactsDao;
	
	@PostConstruct
	public void postConstruct() {
		onlyReadContactsDao = new ContactsOnlyReadDao(em);
		contactsDao = new ContactDao(em);
	}

	public ResultData get(String userId, String nodeSelector) {
		StringBuilder xmlBuilder = new StringBuilder("<?xml version='1.0' encoding='UTF-8'?>");
		//log.info("----------nodeSelector:" + nodeSelector);
		
		if (nodeSelector == null || nodeSelector.equals(Constants.APP_USAGE_CONTACT)) {
			xmlBuilder.append("<contacts xmlns=\"contact-lists\" ")
			.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ")					
			.append("xsi:schemaLocation=\"contact-lists site/contact-list-1.xsd\">");
			List<ContactEntity> list = onlyReadContactsDao.getList(userId);
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
			
			log.info("decode node selector:" + sel + " userId:" + userId);
			if(sel.startsWith(Constants.APP_USAGE_CONTACT)){
				//sel.indexOf(Constants.APP_USAGE_CONTACT.concat("/"));
				String condition[] = sel.split("/");
				
				if(condition.length == 2){   
					return getSecondLevelXml(userId, condition[1]);
				}else if(condition.length == 3){
					log.info("--------------condition.length == 3");
					ResultData data = getSecondLevelXml(userId, condition[1]);
					String tagName = null;   //node selector second level.
					if((tagName = secondLevelUrlValidate(condition[2])) != null){
						log.info("--------------proccess Contact's leaf node.");
						if(data.getstatus() == ResultData.STATUS_200){
							String xml = data.getXml();
							
							String value = null;
							try {
								value = getXmlByTagName(xml, tagName);
							} catch (Exception e) {
								log.info("dom parser exception:" + e.getMessage());
							}
							
							StringBuilder xmlBui = new StringBuilder();
							if(value != null){
								xmlBui.append("<").append(tagName).append(">")
								.append(value)
								.append("</").append(tagName).append(">");
								
								return new ResultData(ResultData.STATUS_200, xmlBui.toString());
							}
						}
					}else{
						//node selector second level is list node ,or other.
					}
					
				}
			}
		}
		
		log.info("ejb server return 404 error.");
		return new ResultData(ResultData.STATUS_404, null);
	}

	public ResultData put(String userId, String nodeSelector, String xml) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setNamespaceAware(true); // never forget this!
	    DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(xml)));	
			
			//String topNodeName = doc.getNodeName();  #document
			String topTagName = doc.getDocumentElement().getTagName();
			log.info("----------------top node name: " + topTagName);
			if(NODE_CONTACTS.equals(topTagName)){
				if(nodeSelector == null ||(nodeSelector != null && nodeSelector.equals(Constants.APP_USAGE_CONTACT))){
					//put document.replace user all contacts.
					
				}
			}else if(NODE_CONTACT.equals(topTagName)){
				//判断选择器是否准确
				//add or replace one contact.
			}else if(isLeafNodeName(topTagName) != null){
				//判断选择器是否准确
				//replace a contact attribute.
				
			}else if(NODE_LIST.equals(topTagName)){
				//implement later
				
			}
			
			contactNode(doc,topTagName, Long.valueOf(userId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public ResultData delete(String userId, String nodeSelector) {
		//delete all
		//delete a contact
		
		return null;
	}
	
	private void contactNode(Document doc,String tagName, long userId){
		NodeList nodes = null;
		
		if(isLeafNodeName(tagName) == null ){
			if(tagName.equals(NODE_CONTACT) || tagName.equals(NODE_LIST)){
				log.info("-------------------saveOrOutdate contact node");
				nodes = doc.getElementsByTagName(NODE_CONTACT);
			}else{
				log.info("-------------------saveOrOutdate other node");
				nodes = doc.getChildNodes().item(0).getChildNodes();
			}
			//tagName.equals(NODE_CONTACTS) || tagName.equals(NODE_CONTACT)
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				String nodeName = node.getNodeName();
				log.info("-------------nodeName:" + nodeName);
				if(nodeName.equals(NODE_CONTACT)){
					NamedNodeMap map = node.getAttributes();
					Node n = map.getNamedItem(ATTR_ID);
					String id  = n.getNodeValue();
					//log.info("-------------id:" + id);
					
					NodeList list = node.getChildNodes();
					
					ContactEntity contact = new ContactEntity();
					for(int j = 0; j <list.getLength(); j++){
						Node leafNode = list.item(j);
						String name = leafNode.getNodeName();
						if(!name.equals("#text")){
							NodeList textNodes = leafNode.getChildNodes();
							String value = null;							
							int len = textNodes.getLength();
							//log.info("---------len:" + len);
							if(len == 1){
								value = textNodes.item(0).getNodeValue();
							}else{
								//value is null
								value = null;
							}
							//log.info(name + ":" + value);
							if(name.equals(NODE_LEAF_CONTACT_NAME)){
								contact.setContactName(value);
							}else if(name.equals(NODE_LEAF_CREATE_DATE)){
								//contact.setCreateDate(value);
							}else if(name.equals(NODE_LEAF_METHOD)){
								contact.setContactMethod(value);
							}
						}
						
					}
					log.info("------------saveOrUpdate--->" + contact.toString());
					if(id != null){
						contact.setId(Long.valueOf(id));
					}
					contact.setUserId(userId);
					contactsDao.saveOrUpdate(contact);
				}else{
					//list node...
				} 
				System.out.println();
			}			
		}else {
			
			//leaf node
		}
		
	}
		
	private static String getXmlByTagName(String xmlText,String tagName) throws Exception {
		class Handler extends DefaultHandler {
			private String tagName;
			private String value;

			private String elementName;

			Handler(String elementName){
				this.elementName = elementName;
			}		
			
			public void startElement(String uri, String localName, String qName,
					Attributes atts) throws SAXException {
				tagName = qName;
			}

			public void characters(char[] ch, int start, int length)
					throws SAXException {
				String valueTemp = String.valueOf(ch);
				valueTemp = valueTemp.substring(start, start + length);

				if (tagName.equals(elementName) && value == null) {
					this.value = valueTemp;
				}
			}
			
			public String getValue(){
				return value;
			}
		}		
		
		StringReader stringReader = new StringReader(xmlText);
		InputSource source = new InputSource(stringReader);

		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser saxParser = null;
		saxParser = spf.newSAXParser();

		Handler h = new Handler(tagName);
		saxParser.parse(source, h);
		stringReader.close();
		return h.getValue();
	}	

	/**
	 * @param nodeName
	 * @return nodeName is validate leaf node name return nodeName ,or null
	 */
	public String isLeafNodeName(String nodeName){
		if(NODE_LEAF_CONTACT_NAME.equals(nodeName)
				|| NODE_LEAF_METHOD.equals(nodeName) || NODE_LEAF_CREATE_DATE.equals(nodeName)){
			return nodeName;
		}else{
			return null;
		}
	}
	
	/**
	 *   method or method[1] 
     *   contactName or contactName[1] 
     *   userId or userId[1]
     *   createDate createDate[1]
	 *
	 * @param condition2
	 * @return
	 */
	private String secondLevelUrlValidate(String condition2){
		if (condition2.equals(NODE_LEAF_CONTACT_NAME) || condition2.equals(NODE_LEAF_METHOD)
			|| condition2.equals(NODE_LEAF_CREATE_DATE) ){
			return condition2;
		}else if(condition2.matches("^method\\[1\\]$") || condition2.matches("^contactName\\[1\\]$")
				|| condition2.matches("^createDate\\[1\\]$") ){
			Pattern pattern = Pattern.compile("method|contactName|createDate");
			Matcher match = pattern.matcher(condition2);
			if(match.find()){
				return match.group(0);
			}
		}
		return null;
	}
	
	/**
	 * 		//by tagName ,                        
			//by unique unique attr,   
			//by index

	0		contacts/contact
			contacts/contact[1]
			contacts/contact[@id="1"]
				
	1、		contacts/list
			contacts/list[1]
			contacts/list[@id="close-friends"]			
				
	2、		contacts/list[@id="close-friends"]/contact
			contacts/list[@id="close-friends"]/contact[1]
			contacts/list[@id="close-friends"]/contact[@id="1"]
		
	        ...... 1,2 组合,其余六种情况
	
	4、	    method/contactName/userId/createDate 例如
	        contacts/list[@id="close-friends"]/contact[@id="1"]/method
	        contacts/list[@id="close-friends"]/contact[@id="1"]/method[1]
		
	 * @param userId
	 * @param condition
	 * @return
	 */
	private ResultData getSecondLevelXml(String userId, String condition1) {
		log.info("--------------condition1:" + condition1);
		if(condition1.startsWith(ContactListsAppEjb.NODE_CONTACT)){
			if(condition1.equals(ContactListsAppEjb.NODE_CONTACT)){
				long size = onlyReadContactsDao.getListSize(userId);
				if(size == 0){
					log.info("was not found in the document.");
					return new ResultData(ResultData.STATUS_404,"");
				}else if(size == 1){
					StringBuilder contactXml = new StringBuilder();
					
					constructContactNode(contactXml,
							onlyReadContactsDao.getList(userId));
					return new ResultData(ResultData.STATUS_200, contactXml.toString());
				}else{
					new ResultData(ResultData.STATUS_409, new XCAPErrors.NoParentConflictException("Contacts").getResponseContent());
				}
			}else if(condition1.matches("^contact\\[\\d+\\]$")){
				log.info("----------get contact node by index");
				Pattern p = Pattern.compile("\\d+");
				Matcher match = p.matcher(condition1);
				if(match.find()){
					int indexTemp = Integer.valueOf(match.group(0));
					log.info("-----------------index:" + indexTemp);
					ContactEntity entity = onlyReadContactsDao.getByIndex(userId, indexTemp);
					
					return getStatusAndXml(entity);
				}							
				
			}else if(condition1.matches("^contact\\[@id=\"\\d+\"\\]$")){
				log.info("----------get contact node by database id.");

				Pattern p = Pattern.compile("\\d+");
				Matcher match = p.matcher(condition1);
				if(match.find()){
					int id = Integer.valueOf(match.group(0));
					ContactEntity entity = onlyReadContactsDao.getById(userId, id);
					return getStatusAndXml(entity);								
				}
			}else{
				return new ResultData(ResultData.STATUS_404, "");
			}
		}else if(condition1.startsWith(ContactListsAppEjb.NODE_LIST)){
			 
		}
		
		return new ResultData(ResultData.STATUS_404, null);
	}

	private ResultData getStatusAndXml(ContactEntity entity) {
		String xml = "";
		int status = ResultData.STATUS_404;
		if(entity != null){
			status = ResultData.STATUS_200;
			StringBuilder builder = new StringBuilder();
			constructContactNode(builder, entity);
			xml = builder.toString();
			
		}
		return new ResultData(status, xml);
	}	
	
	private static void constructContactNode(StringBuilder xmlBuilder,
			List<ContactEntity> list) {
		if(list != null){
			for(ContactEntity en : list){
				constructContactNode(xmlBuilder, en);				
			}
		}
	}

	private static void constructContactNode(StringBuilder xmlBuilder,
			ContactEntity en) {
		if (en != null) {
			String method = en.getContactMethod();
			String contactName = en.getContactName();
			String createDate = dateFormat.format(en.getCreateDate());
			String id = en.getId().toString();

			xmlBuilder
					.append("<contact id=\"".concat(id).concat("\">"))
					.append("<method>".concat(method != null ? method : "").concat("</method>"))
					.append("<contactName>".concat(contactName != null ? contactName : "").concat("</contactName>"))
					.append("<createDate>".concat(createDate).concat("</createDate>")).append("</contact>");
		}
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
