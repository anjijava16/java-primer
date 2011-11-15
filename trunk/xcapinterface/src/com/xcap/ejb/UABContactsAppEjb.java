package com.xcap.ejb;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.jboss.annotation.ejb.LocalBinding;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.xcap.dao.UABContactDao;
import com.xcap.dao.entity.UABContactEntity;
import com.xcap.ifc.Contact;
import com.xcap.ifc.XCAPDatebaseLocalIfc;
import com.xcap.ifc.error.XCAPErrors;

/**
 * UAB contact xcap interface.<br>
 * author slieer<br>
 * Create Date2011-10-28<br>
 * version 1.0<br>
 */
@Stateless
@Local(value = XCAPDatebaseLocalIfc.class)
@LocalBinding(jndiBinding = XCAPDatebaseLocalIfc.UAB_CONTACTS_LOCAL_JNDI)
public class UABContactsAppEjb implements XCAPDatebaseLocalIfc {
	public final static Logger log = Logger.getLogger(UABContactsAppEjb.class);

	final static String NODE_CONTACTS = "contacts";
	final static String NODE_CONTACT = "contact";
	final static String NODE_LIST = "list";

	final static String NODE_LEAF_CONTACT_NAME = "contactName";
	final static String NODE_LEAF_DESC = "description";
	final static String NODE_LEAF_CREATE_DATE = "createDate";

	final static String NODE_ATTR_METHOD = "method";

	final static String PATTERN_CONTACT_INDEX = "^contact\\[\\d+\\]$";
	final static String PATTERN_CONTACT_UNIQUE_ATTR = "^contact\\[@".concat(
			NODE_ATTR_METHOD).concat("=\"\\S+\"\\]$");
	final static String PATTERN_CONTACTNAME_INDEX = "^contactName\\[1\\]$";
	final static String PATTERN_CREATEDATE_INDEX = "^createDate\\[1\\]$";

	@PersistenceContext(unitName = "UABXCAP")
	EntityManager em;

	private static UABContactDao contactsDao;

	@PostConstruct
	public void postConstruct() {
		contactsDao = new UABContactDao(em);
	}

	public ResultData get(String userId_, String nodeSelector) {
		long userId = Long.valueOf(userId_);
		StringBuilder xmlBuilder = new StringBuilder(
				"<?xml version='1.0' encoding='UTF-8'?>");
		log.info("----------nodeSelector:" + nodeSelector);

		if (nodeSelector == null || nodeSelector.equals(NODE_CONTACTS)) {
			log.info("----------nodeSelector11:" + nodeSelector);
			xmlBuilder
					.append("<".concat(NODE_CONTACTS))
					.append(" xmlns=\"contacts\" ")
					.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ")
					.append("xsi:schemaLocation=\"contact-lists site/contact-list-1.xsd\">");
			List<UABContactEntity> list = contactsDao.getList(userId);
			constructContactNode(xmlBuilder, list);

			xmlBuilder.append("</".concat(NODE_CONTACTS).concat(">"));
			if (list != null && list.size() != 0) {
				return new ResultData(ResultData.STATUS_200,
						xmlBuilder.toString());
			}
		} else {
			log.info("--- node selector:" + nodeSelector + " userId:" + userId);
			if (nodeSelector.startsWith(NODE_CONTACTS)) {
				// sel.indexOf(Constants.NODE_CONTACTS.concat("/"));
				String condition[] = nodeSelector.split("/");

				if (condition.length == 2) {
					return getSecondLevelXml(userId, condition[1]);
				} else if (condition.length == 3) {
					log.info("--------------condition.length == 3");
					ResultData data = getSecondLevelXml(userId, condition[1]);
					String tagName = null; // node selector second level.
					if ((tagName = thirdLevelUrlValidate(condition[2])) != null) {
						log.info("--------------proccess Contact's leaf node.");
						if (data.getstatus() == ResultData.STATUS_200) {
							String xml = data.getXml();

							String value = null;
							try {
								value = getXmlByTagName(xml, tagName);
							} catch (Exception e) {
								log.info("dom parser exception:"
										+ e.getMessage());
							}

							StringBuilder xmlBui = new StringBuilder();
							if (value != null) {
								xmlBui.append("<").append(tagName).append(">")
										.append(value).append("</")
										.append(tagName).append(">");

								return new ResultData(ResultData.STATUS_200,
										xmlBui.toString());
							}
						}
					} else {
						// node selector second level is list node ,or other.
					}

				}
			}
		}

		log.info("ejb server return 404 error.");
		return new ResultData(ResultData.STATUS_404, "");
	}

	public ResultData put(String userId_, String nodeSelector, String xml) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true); // never forget this!
		long userId = Long.valueOf(userId_);

		Document doc = null;
		Element element = null;
		String topTagName = null;
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(new InputSource(new StringReader(xml)));

			element = doc.getDocumentElement();
			topTagName = element.getTagName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("----------------document top node tag name from xml text,value is: " + topTagName);

		String secondLevelSelector = null;
		String thirdLevelSeletor = null;
		if (nodeSelector != null) {
			String selectorArr[] = nodeSelector.split("/");
			if (selectorArr.length == 2) {
				secondLevelSelector = selectorArr[1];
			} else if (selectorArr.length == 3) {
				secondLevelSelector = selectorArr[1];
				thirdLevelSeletor = selectorArr[2];
			}
		}
		log.info("------------document top tag name from url ,value is:" + topTagName
				+ ". node selector:" + secondLevelSelector);

		if (NODE_CONTACTS.equals(topTagName)) {
			if (nodeSelector == null
					|| (nodeSelector != null && nodeSelector
							.equals(NODE_CONTACTS))) {
				// put document, replace user all contacts.
				log.info("delete contacts by userId, put document.");
				contactsDao.deleteContacts(Long.valueOf(userId));
				return contactNode(doc, NODE_CONTACTS, userId);
			}
		} else if (NODE_CONTACT.equals(topTagName)) {// document top node is
														// contact.
			// add or replace one contact.
			if (secondLevelSelector.equals(NODE_CONTACT)) { //
				log.info("put operate by node tag name, node selector is contact.");
				int size = (int) contactsDao.getListSize(userId);
				switch (size) {
				case 0: // add
					contactNode(doc, NODE_CONTACT, userId);
					return new ResultData(ResultData.STATUS_200, "");

				case 1: // merge
					// first delete existed node, then add.
					contactNode(doc, NODE_CONTACT, userId);
					return new ResultData(ResultData.STATUS_200, "");
				default:
					// error
					String errorInfo = new XCAPErrors.CannotInsertConflictException()
							.getResponseContent();
					return new ResultData(ResultData.STATUS_409, errorInfo);
				}
			} else if (secondLevelSelector.matches(PATTERN_CONTACT_INDEX)) {
				log.info("put operate by node index, node selector is "
						+ nodeSelector);

				Pattern p = Pattern.compile("\\d+");
				Matcher match = p.matcher(nodeSelector);
				if (match.find()) {
					int index = Integer.valueOf(match.group(0));
					int size = (int) contactsDao.getListSize(userId);
					if (index > 0 && index <= size + 1) { // index valid
						log.info("index valid, value is " + index);

						Map<String, String> re = getContactXmlValue(element);
						String newContactName = re.get(NODE_LEAF_CONTACT_NAME);
						String method = re.get(NODE_ATTR_METHOD);

						if (index <= size) {
							log.info("merge contact by index");
							UABContactEntity contact = contactsDao.getByIndex(
									userId, index);
							if (contact != null) {
								contact.setContactName(newContactName);
								contact.setContactMethod(method);
								contactsDao.saveOrUpdate(contact);
								log.info("merge contact by index." + contact);
								return new ResultData(ResultData.STATUS_200, "");
							} else {
								log.info("merge contact by index. 404");
							}
						} else if (index == size + 1) {
							// add
							UABContactEntity contact = new UABContactEntity();
							contact.setUserId(userId);
							contact.setContactName(newContactName);
							contact.setContactMethod(method);
							contactsDao.saveOrUpdate(contact);
							return new ResultData(ResultData.STATUS_200, "");
						}
					} else {
						// 404 error
						log.info("index overflow");
					}
				} else {
					log.info("index pattern not find.");
				}

			} else if (secondLevelSelector.matches(PATTERN_CONTACT_UNIQUE_ATTR)) {
				log.info("put operate by unique attribute, node selector is "
						+ nodeSelector);
				int beginIndex = nodeSelector.indexOf("=\"");
				int endIndex = nodeSelector.indexOf("\"]");

				if (beginIndex != -1 && endIndex != -1 && beginIndex < endIndex) {
					String method = nodeSelector.substring(beginIndex + 2,
							endIndex);

					Map<String, String> re = getContactXmlValue(element);
					String newContactName = re.get(NODE_LEAF_CONTACT_NAME);
					String docAttrMethod = re.get(NODE_ATTR_METHOD);

					if (method.equals(docAttrMethod)) {
						UABContactEntity en = contactsDao.getByContactMethod(
								userId, method);
						if (en != null) {
							// merge.
							log.info("merge contact by unique attr.");
							en.setContactName(newContactName);
							contactsDao.saveOrUpdate(en);
							return new ResultData(ResultData.STATUS_200, "");
						} else {
							// add.
							log.info("add contact by unique attr. userId "
									+ userId);
							UABContactEntity newEntity = new UABContactEntity();
							newEntity.setUserId(userId);
							newEntity.setContactName(newContactName);
							newEntity.setContactMethod(method);
							contactsDao.saveOrUpdate(newEntity);
							return new ResultData(ResultData.STATUS_200, "");
						}
					} else {
						// 404 error
					}
				}
			}

		} else if (thirdLevelUrlValidate(topTagName) != null) { // According to
																// document text
																// judge
			log.info("put operate, leaf node, node selector is " + nodeSelector);
			// second level is by tag name.
			if (thirdLevelSeletor != null) {
				String thirdLevelTagName = thirdLevelUrlValidate(thirdLevelSeletor);
				if (topTagName.equals(thirdLevelTagName)) {
					UABContactEntity entity = null;
					if (secondLevelSelector.equals(NODE_CONTACT)) {
						int size = (int) contactsDao.getListSize(userId);
						if (size == 1) {
							List<UABContactEntity> tempList = contactsDao
									.getList(userId);
							if (tempList != null && tempList.size() > 0) {
								entity = tempList.get(0);
							}
						}
					} else if (secondLevelSelector
							.matches(PATTERN_CONTACT_INDEX)) {
						Pattern p = Pattern.compile("\\d+");
						Matcher match = p.matcher(nodeSelector);
						if (match.find()) {
							int index = Integer.valueOf(match.group(0));
							entity = contactsDao.getByIndex(userId, index);
						}
					} else if (secondLevelSelector
							.matches(PATTERN_CONTACT_UNIQUE_ATTR)) {
						int beginIndex = nodeSelector.indexOf("=\"");
						int endIndex = nodeSelector.indexOf("\"]");

						if (beginIndex != -1 && endIndex != -1
								&& beginIndex < endIndex) {
							String method = nodeSelector.substring(
									beginIndex + 2, endIndex);
							entity = contactsDao.getByContactMethod(userId,
									method);
						}
					}

					log.info("modify contact attribue,contact is:" + entity);
					// replace a contact attribute.
					NodeList tempNodeList = doc
							.getElementsByTagName(NODE_LEAF_CONTACT_NAME);
					Node leafNode = tempNodeList.item(0);
					if (entity != null && leafNode != null
							&& leafNode.getFirstChild() != null) {
						log.info("----####");
						String contactName = leafNode.getFirstChild()
								.getNodeValue();
						entity.setContactName(contactName);
						contactsDao.saveOrUpdate(entity);
						return new ResultData(ResultData.STATUS_200, "");
					}
				}
			}
		} else if (NODE_LIST.equals(topTagName)) {
			// implement later
		}

		return new ResultData(ResultData.STATUS_404, "");
	}

	public ResultData delete(String userId_, String nodeSelector) {
		log.info("delete operator, nodeSelector :" + nodeSelector);
		// delete all
		// delete a contact
		long userId = Long.valueOf(userId_);
		if (nodeSelector == null || nodeSelector.equals(NODE_CONTACTS)) {
			log.info("delete all by userId " + userId);
			contactsDao.deleteContacts(Long.valueOf(userId));
			return new ResultData(ResultData.STATUS_200, "");
		} else {
			String[] selectors = nodeSelector.split("/");
			if (selectors.length == 2) {
				String secondLevelSelector = selectors[1];
				if (secondLevelSelector.equals(NODE_CONTACT)) { // by tagName
																// selector.
					long size = contactsDao.getListSize(userId);
					if (size == 1) {
						int affectRows = contactsDao.deleteContacts(userId);
						return new ResultData(ResultData.STATUS_200, "");
					} if(size == 0){
						return new ResultData(ResultData.STATUS_404, "");
					}else {
						return new ResultData(ResultData.STATUS_409,
								new XCAPErrors.CannotDeleteConflictException()
										.getResponseContent());
					}
				} else if (secondLevelSelector.matches(PATTERN_CONTACT_INDEX)) {
					log.info("delete contact by index ");
					Pattern p = Pattern.compile("\\d+");
					Matcher match = p.matcher(nodeSelector);
					if (match.find()) {
						int index = Integer.valueOf(match.group(0));
						int affectRows = contactsDao
								.deleteContactByIndexSelector(userId, index);
						if (affectRows != -1) {
							return new ResultData(ResultData.STATUS_200, "");
						}
					} else {
						return new ResultData(ResultData.STATUS_409,
								new XCAPErrors.CannotDeleteConflictException()
										.getResponseContent());
					}

				} else if (secondLevelSelector
						.matches(PATTERN_CONTACT_UNIQUE_ATTR)) {
					log.info("delete contact by unique attr.");
					int beginIndex = nodeSelector.indexOf("=\"");
					int endIndex = nodeSelector.indexOf("\"]");

					if (beginIndex != -1 && endIndex != -1
							&& beginIndex < endIndex) {
						String method = nodeSelector.substring(beginIndex + 2,
								endIndex);
						int affectRows = contactsDao.deleteContactByUniqueAttr(
								userId, method); // return delete row count.
						return new ResultData(ResultData.STATUS_200, "");
					} else {
						return new ResultData(ResultData.STATUS_409,
								new XCAPErrors.CannotDeleteConflictException()
										.getResponseContent());
					}
				}
			}
		}

		return new ResultData(ResultData.STATUS_404, "");
	}

	private static Map<String, String> getContactXmlValue(Element element) {
		String method = element.getAttribute(NODE_ATTR_METHOD);
		NodeList nodeList = element.getChildNodes();

		String newContactName = null;
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (!node.getNodeName().equals("#text")) {
				NodeList nodeList2 = node.getChildNodes();
				if (NODE_LEAF_CONTACT_NAME.equals(node.getNodeName())) {
					newContactName = nodeList2.getLength() > 0 ? nodeList2
							.item(0).getNodeValue() : null;
				}
			}
		}
		Map<String, String> re = new HashMap<String, String>();
		re.put(NODE_ATTR_METHOD, method);
		re.put(NODE_LEAF_CONTACT_NAME, newContactName);
		return re;
	}

	/**
	 * put contact nodes.
	 */
	private ResultData contactNode(Document doc, String tagName, long userId) {
		NodeList nodes = null;

		if (thirdLevelUrlValidate(tagName) == null) {
			if (tagName.equals(NODE_CONTACT) || tagName.equals(NODE_LIST)) {
				log.info("-------------------saveOrOutdate contact or list node");
				nodes = doc.getElementsByTagName(NODE_CONTACT);
			} else {
				log.info("-------------------saveOrOutdate contacts node");
				nodes = doc.getDocumentElement().getChildNodes();
			}

			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				String nodeName = node.getNodeName();
				log.info("-------------nodeName:" + nodeName);
				if (nodeName.equals(NODE_CONTACT)) {
					NamedNodeMap map = node.getAttributes();
					Node n = map.getNamedItem(NODE_ATTR_METHOD);
					String method = n.getNodeValue();
					// log.info("-------------method:" + method);

					NodeList list = node.getChildNodes();

					UABContactEntity contact = new UABContactEntity();
					for (int j = 0; j < list.getLength(); j++) {
						Node leafNode = list.item(j);
						String name = leafNode.getNodeName();
						if (!name.equals("#text")) {
							NodeList textNodes = leafNode.getChildNodes();
							String value = null;
							int len = textNodes.getLength();
							// log.info("---------len:" + len);
							if (len == 1) {
								value = textNodes.item(0).getNodeValue();
							} else {
								// value is null
								value = null;
							}
							// log.info(name + ":" + value);
							if (name.equals(NODE_LEAF_CONTACT_NAME)) {
								contact.setContactName(value);
							} else if (name.equals(NODE_LEAF_CREATE_DATE)) {
								// contact.setCreateDate(value);
							}
						}
					}
					contact.setContactMethod(method);
					contact.setUserId(userId);
					log.info("------------saveOrUpdate--->"
							+ contact.toString());
					contactsDao.saveOrUpdate(contact);
				} else {
					// list node...
				}
				log.info("put contact ...");
			}
			return new ResultData(ResultData.STATUS_200, "");
		}
		return new ResultData(ResultData.STATUS_404, "");
	}

	private static String getXmlByTagName(String xmlText, String tagName)
			throws Exception {
		class Handler extends DefaultHandler {
			private String tagName;
			private String value;

			private String elementName;

			Handler(String elementName) {
				this.elementName = elementName;
			}

			public void startElement(String uri, String localName,
					String qName, Attributes atts) throws SAXException {
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

			public String getValue() {
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
	 * method or method[1] contactName or contactName[1] userId or userId[1]
	 * createDate createDate[1]
	 * 
	 * @param condition2
	 * @return
	 */
	private String thirdLevelUrlValidate(String condition2) {
		if (condition2.equals(NODE_LEAF_CONTACT_NAME)
				|| condition2.equals(NODE_LEAF_CREATE_DATE)) {
			return condition2;
		} else if (condition2.matches(PATTERN_CONTACTNAME_INDEX)
				|| condition2.matches(PATTERN_CREATEDATE_INDEX)) {
			Pattern pattern = Pattern.compile("contactName|createDate");
			Matcher match = pattern.matcher(condition2);
			if (match.find()) {
				return match.group(0);
			}
		}
		return null;
	}

	/**
	 * //by tagName ,<br/>
	 * //by unique unique attr,<br/>
	 * //by index,<br/>
	 * 
	 * 0 contacts/contact<br/>
	 * contacts/contact[1]<br/>
	 * contacts/contact[@method="1"]<br/>
	 * 
	 * 1、 contacts/list<br/>
	 * contacts/list[1]<br/>
	 * contacts/list[@id="close-friends"]<br/>
	 * ----------------------------------only proccess
	 * 0,1------------------------------------------<br>
	 * 2、 contacts/list[@id="close-friends"]/contact<br/>
	 * contacts/list[@id="close-friends"]/contact[1]<br/>
	 * contacts/list[@id="close-friends"]/contact[@id="1"]<br/>
	 * 
	 * ...... 1,2 组合,其余六种情况<br/>
	 * 
	 * 4、 method/contactName/userId/createDate 例如<br/>
	 * contacts/list[@id="close-friends"]/contact[@method="1"]/contactName<br/>
	 * contacts/list[@id="close-friends"]/contact[@method="1"]/contactName[1]<br/>
	 * 
	 * @param userId
	 * @param condition
	 * @return
	 */
	private ResultData getSecondLevelXml(long userId, String condition1) {
		log.info("--------------condition1:" + condition1);
		if (condition1.startsWith(UABContactsAppEjb.NODE_CONTACT)) {
			if (condition1.equals(UABContactsAppEjb.NODE_CONTACT)) {
				long size = contactsDao.getListSize(userId);
				if (size == 0) {
					log.info("was not found in the document.");
					return new ResultData(ResultData.STATUS_404, "");
				} else if (size == 1) {
					StringBuilder contactXml = new StringBuilder();

					constructContactNode(contactXml,
							contactsDao.getList(userId));
					return new ResultData(ResultData.STATUS_200,
							contactXml.toString());
				} else {
					new ResultData(
							ResultData.STATUS_409,
							new XCAPErrors.NoParentConflictException("Contacts")
									.getResponseContent());
				}
			} else if (condition1.matches(PATTERN_CONTACT_INDEX)) {
				log.info("----------get contact node by index");
				Pattern p = Pattern.compile("\\d+");
				Matcher match = p.matcher(condition1);
				if (match.find()) {
					int indexTemp = Integer.valueOf(match.group(0));
					log.info("-----------------index:" + indexTemp);
					UABContactEntity entity = contactsDao.getByIndex(userId,
							indexTemp);

					return getStatusAndXml(entity);
				}

			} else if (condition1.matches(PATTERN_CONTACT_UNIQUE_ATTR)) {
				log.info("----------get contact node by database id.");

				int beginIndex = condition1.indexOf("=\"");
				int endIndex = condition1.indexOf("\"]");

				if (beginIndex != -1 && endIndex != -1 && beginIndex < endIndex) {
					String method = condition1.substring(beginIndex + 2,
							endIndex);
					UABContactEntity entity = contactsDao.getByContactMethod(
							userId, method);
					return getStatusAndXml(entity);
				}
			} else {
				return new ResultData(ResultData.STATUS_404, "");
			}
		} else if (condition1.startsWith(UABContactsAppEjb.NODE_LIST)) {

		}

		return new ResultData(ResultData.STATUS_404, null);
	}

	private ResultData getStatusAndXml(UABContactEntity entity) {
		String xml = "";
		int status = ResultData.STATUS_404;
		if (entity != null) {
			status = ResultData.STATUS_200;
			StringBuilder builder = new StringBuilder();
			constructContactNode(builder, entity);
			xml = builder.toString();

		}
		return new ResultData(status, xml);
	}

	private static void constructContactNode(StringBuilder xmlBuilder,
			List<UABContactEntity> list) {
		if (list != null) {
			for (UABContactEntity en : list) {
				constructContactNode(xmlBuilder, en);
			}
		}
	}

	private static void constructContactNode(StringBuilder xmlBuilder,
			UABContactEntity en) {
		String method = en.getContactMethod();
		if (en != null && method != null) {
			String contactName = en.getContactName();
			String createDate = dateFormat.format(en.getCreateDate());
			// String id = en.getId().toString();

			xmlBuilder
					.append("<contact method=\"".concat(method).concat("\">"))
					.append("<contactName>".concat(
							contactName != null ? contactName : "").concat(
							"</contactName>"))
					.append("<createDate>".concat(createDate).concat(
							"</createDate>")).append("</contact>");
		}
	}

	public static Contact toBean(UABContactEntity entity) {
		Contact dest = new Contact();
		return (Contact) beanConversion(dest, entity);
	}

	public static UABContactEntity toEntity(Contact contact) {
		UABContactEntity dest = new UABContactEntity();
		return (UABContactEntity) beanConversion(dest, contact);
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
