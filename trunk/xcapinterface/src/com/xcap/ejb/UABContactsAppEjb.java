package com.xcap.ejb;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
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

import com.borqs.contact.ifc.Contact;
import com.borqs.contact.ifc.ContactExchangeIfc;
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
	final static String NODE_LEAF_DEVICE_ID = "deviceId";
	final static String NODE_LEAF_RAW_ID = "rawId";

	final static String NODE_ATTR_METHOD = "method";

	final static String PATTERN_CONTACT_INDEX = "^contact\\[\\d+\\]$";
	final static String PATTERN_CONTACT_UNIQUE_ATTR = "^contact\\[@".concat(
			NODE_ATTR_METHOD).concat("=\"\\S+\"\\]$");
	final static String PATTERN_CONTACTNAME_INDEX = "^contactName\\[1\\]$";
	final static String PATTERN_CREATEDATE_INDEX = "^createDate\\[1\\]$";
	
	final static String PATTERN_DEVICE_ID_INDEX = "^deviceId\\[1\\]$";
	final static String PATTERN_RAW_ID_INDEX = "^rawId\\[1\\]$";
	final static String PATTERN_DESC_INDEX = "^rawId\\[1\\]$";
	
	private ContactExchangeIfc contactExchangeIfc;	
	
	@Override
	@TransactionAttribute(value=TransactionAttributeType.NEVER)	
	public ResultData get(String userInfo, String nodeSelector) {
		StringBuilder xmlBuilder = new StringBuilder(
				"<?xml version='1.0' encoding='UTF-8'?>");
		log.info("get, nodeSelector:" + nodeSelector);

		if (nodeSelector == null || nodeSelector.equals(NODE_CONTACTS)) {
			//log.info("----------nodeSelector11:" + nodeSelector);
			xmlBuilder
					.append("<".concat(NODE_CONTACTS))
					.append(" xmlns=\"contacts\" ")
					.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ")
					.append("xsi:schemaLocation=\"contacts ")
					.append(  "xcap-schema/UABContacts\">");
			List<Contact> list = queryContactListByMsisdn(userInfo);
			constructContactNode(xmlBuilder, list);

			xmlBuilder.append("</".concat(NODE_CONTACTS).concat(">"));
			if (list != null && list.size() != 0) {
				return new ResultData(ResultData.STATUS_200,
						xmlBuilder.toString());
			}
		} else {
			log.info("node selector:" + nodeSelector + " userMsisdn:" + userInfo);
			if (nodeSelector.startsWith(NODE_CONTACTS)) {
				// sel.indexOf(Constants.NODE_CONTACTS.concat("/"));
				String condition[] = nodeSelector.split("/");

				if (condition.length == 2) {
					return getSecondLevelXml(userInfo, condition[1]);
				} else if (condition.length == 3) {
					log.info("selector is three layer...");
					ResultData data = getSecondLevelXml(userInfo, condition[1]);
					String tagName = null; // node selector second level.
					if ((tagName = thirdLevelUrlValidate(condition[2])) != null) {
						log.info("proccess Contact's leaf node.");
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

	public ResultData put(String userMsisdn, String nodeSelector, String xml) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true); // never forget this!

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
		log.info("document top node tag name from xml text,value is: " + topTagName);

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
		log.info("document top tag name from url ,value is:" + topTagName
				+ ". node selector:" + secondLevelSelector);

		if (NODE_CONTACTS.equals(topTagName)) {
			if (nodeSelector == null
					|| (nodeSelector != null && nodeSelector
							.equals(NODE_CONTACTS))) {
				// put document, replace user all contacts.
				log.info("delete contacts by user msisdn, put document.");
				deleteContacts(userMsisdn);
				return contactNode(doc, NODE_CONTACTS, userMsisdn);
			}
		} else if (NODE_CONTACT.equals(topTagName)) {// document top node is
														// contact.
			// add or replace one contact.
			if (secondLevelSelector.equals(NODE_CONTACT)) { //
				log.info("put operate by node tag name, node selector is contact.");
				int size = (int) queryContactListByMsisdnSize(userMsisdn);
				switch (size) {
				case 0: // add
					contactNode(doc, NODE_CONTACT, userMsisdn);
					return new ResultData(ResultData.STATUS_200, "");

				case 1: // merge
					// first delete existed node, then add.
					contactNode(doc, NODE_CONTACT, userMsisdn);
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
					int size = (int) queryContactListByMsisdnSize(userMsisdn);
					if (index > 0 && index <= size + 1) { // index valid
						log.info("index valid, value is " + index);

						Map<String, String> re = getContactXmlValue(element);
						String newContactName = re.get(NODE_LEAF_CONTACT_NAME);
						String method = re.get(NODE_ATTR_METHOD);
						String rawId = re.get(NODE_LEAF_RAW_ID);
						String deviceId = re.get(NODE_LEAF_DEVICE_ID);
						
						Contact contact = null;
						if (index <= size) {
							log.info("merge contact by index");
							contact = getByIndex(userMsisdn, index,1);
						} else if (index == size + 1) {
							// add
							contact = new Contact();
						}
						
						if (contact != null) {
							contact.setMsisdn(userMsisdn);
							contact.setContactName(newContactName);
							contact.setContactMethod(method);
							contact.setRawID(rawId != null ? Long.valueOf(rawId) : null);
							contact.setDeviceID(deviceId != null ? Long.valueOf(deviceId) : null);
							saveOrUpdate(contact);
							log.info("merge contact by index." + contact);
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
					String rawId = re.get(NODE_LEAF_RAW_ID);
					String deviceId = re.get(NODE_LEAF_DEVICE_ID);
					if (method.equals(docAttrMethod)) {
						Contact en = getByContactMethod(
								userMsisdn, method);
						if (en == null) {
							// add.
							log.info("add contact by unique attr. user msisdn " + userMsisdn);
							en = new Contact();
						} else {
							// merge.
							log.info("merge contact by unique attr.");
						}
						
						en.setMsisdn(userMsisdn);
						en.setContactName(newContactName);
						en.setContactMethod(docAttrMethod);
						en.setRawID(rawId != null ? Long.valueOf(rawId) : null);
						en.setDeviceID(deviceId != null ? Long.valueOf(deviceId) : null);
						saveOrUpdate(en);
						return new ResultData(ResultData.STATUS_200, "");
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
					Contact entity = null;
					if (secondLevelSelector.equals(NODE_CONTACT)) {
						int size = (int) queryContactListByMsisdnSize(userMsisdn);
						if (size == 1) {
							List<Contact> tempList = queryContactListByMsisdn(userMsisdn);
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
							entity = getByIndex(userMsisdn, index, 1);
						}
					} else if (secondLevelSelector
							.matches(PATTERN_CONTACT_UNIQUE_ATTR)) {
						int beginIndex = nodeSelector.indexOf("=\"");
						int endIndex = nodeSelector.indexOf("\"]");

						if (beginIndex != -1 && endIndex != -1
								&& beginIndex < endIndex) {
							String method = nodeSelector.substring(
									beginIndex + 2, endIndex);
							entity = getByContactMethod(userMsisdn,
									method);
						}
					}

					log.info("modify contact attribue,contact entity is:" + entity);
					// replace a contact attribute.
					NodeList tempNodeList = doc
							.getElementsByTagName(thirdLevelTagName);
					Node leafNode = tempNodeList.item(0);
					if (entity != null && leafNode != null ) {
						String nodeValue = null;
						if(leafNode.getFirstChild() != null){
							nodeValue = leafNode.getFirstChild().getNodeValue();
							ConvertUtils.register(new LongConvert(), Long.class);
						}
						log.info("thirdLevelTagName, nodeValue:" + thirdLevelTagName + "," + nodeValue);
						String fieldName = xmlMappingEntityField(thirdLevelTagName);
						try {
							BeanUtils.setProperty(entity, fieldName, nodeValue);
						} catch (Exception e) {
							e.printStackTrace();
						} 
						saveOrUpdate(entity);
						return new ResultData(ResultData.STATUS_200, "");
					}
				}
			}
		} else if (NODE_LIST.equals(topTagName)) {
			// implement later
		}

		return new ResultData(ResultData.STATUS_404, "");
	}

	public ResultData delete(String userMsisdn, String nodeSelector) {
		log.info("delete operator, nodeSelector :" + nodeSelector);
		// delete all
		// delete a contact
		if (nodeSelector == null || nodeSelector.equals(NODE_CONTACTS)) {
			log.info("delete all by userMsisdn " + userMsisdn);
			deleteContacts(userMsisdn);
			return new ResultData(ResultData.STATUS_200, "");
		} else {
			String[] selectors = nodeSelector.split("/");
			if (selectors.length == 2) {
				String secondLevelSelector = selectors[1];
				if (secondLevelSelector.equals(NODE_CONTACT)) { // by tagName
																// selector.
					long size = queryContactListByMsisdnSize(userMsisdn);
					if (size == 1) {
						//only one record.
						int affectRows = deleteContacts(userMsisdn);
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
						int affectRows = deleteContactByIndexSelector(userMsisdn, index);
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
						int affectRows = deleteContactByUniqueAttr(
								userMsisdn, method); // return delete row count.
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
		Map<String, String> re = new HashMap<String, String>();
		re.put(NODE_ATTR_METHOD, method);

		NodeList nodeList = element.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			String nodeName = node.getNodeName();
			if (!nodeName.equals("#text")) {
				NodeList nodeList2 = node.getChildNodes();
				String nodeVal = nodeList2.getLength() > 0 ? nodeList2
						.item(0).getNodeValue() : null;
				if (NODE_LEAF_CONTACT_NAME.equals(nodeName)) {					
					re.put(NODE_LEAF_CONTACT_NAME, nodeVal );
				}else if(NODE_LEAF_DEVICE_ID.equals(nodeName)){
					re.put(NODE_LEAF_DEVICE_ID, nodeVal);
				}else if(NODE_LEAF_RAW_ID.equals(nodeName)){
					re.put(NODE_LEAF_RAW_ID, nodeVal);
				}
			}
		}
		return re;
	}

	/**
	 * put contact nodes.
	 */
	private ResultData contactNode(Document doc, String tagName, String userInfo) {
		NodeList nodes = null;

		if (thirdLevelUrlValidate(tagName) == null) {
			if (tagName.equals(NODE_CONTACT) || tagName.equals(NODE_LIST)) {
				log.info("saveOrOutdate contact or list node");
				nodes = doc.getElementsByTagName(NODE_CONTACT);
			} else {
				log.info("saveOrOutdate contacts node");
				nodes = doc.getDocumentElement().getChildNodes();
			}

			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				String nodeName = node.getNodeName();
				//log.info("--nodeName:" + nodeName);
				if (nodeName.equals(NODE_CONTACT)) {
					NamedNodeMap map = node.getAttributes();
					Node n = map.getNamedItem(NODE_ATTR_METHOD);
					String method = n.getNodeValue();
					// log.info("-------------method:" + method);

					NodeList list = node.getChildNodes();

					Contact contact = new Contact();
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
							} else if(name.equals(NODE_LEAF_DEVICE_ID)){
								contact.setDeviceID(value == null ? null : Long.valueOf(value));
							}else if(name.equals(NODE_LEAF_RAW_ID)){
								contact.setRawID(value == null ? null : Long.valueOf(value));
							}
						}
					}
					contact.setContactMethod(method);
					contact.setMsisdn(userInfo);
					log.info("saveOrUpdate--->" + contact.toString());
					saveOrUpdate(contact);
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
	 * 3rd selector validator and get 3rd layer tag name from selector.
	 * createDate createDate[1]
	 * 
	 * @param condition2
	 * @return
	 */
	private String thirdLevelUrlValidate(String condition2) {
		if (condition2.equals(NODE_LEAF_CONTACT_NAME)
				|| condition2.equals(NODE_LEAF_DEVICE_ID)
				|| condition2.equals(NODE_LEAF_RAW_ID)) {
			return condition2;
		} else if (condition2.matches(PATTERN_CONTACTNAME_INDEX)
				|| condition2.matches(PATTERN_DEVICE_ID_INDEX)
				|| condition2.matches(PATTERN_RAW_ID_INDEX)
				|| condition2.matches(PATTERN_CREATEDATE_INDEX)
				|| condition2.matches(PATTERN_DESC_INDEX)) {
			
			String patternStr = NODE_LEAF_CONTACT_NAME.concat("|")
				.concat(NODE_LEAF_DEVICE_ID)
				.concat(NODE_LEAF_RAW_ID);
			Pattern pattern = Pattern.compile(patternStr);
			Matcher match = pattern.matcher(condition2);
			if (match.find()) {
				return match.group(0);
			}
		}
		return null;
	}

	/**
	 * @param user msisdn
	 * @param condition
	 * @return
	 */
	private ResultData getSecondLevelXml(String userInfo, String condition1) {
		log.info("getSecondLevelXml, condition1:" + condition1);
		if (condition1.startsWith(UABContactsAppEjb.NODE_CONTACT)) {
			if (condition1.equals(UABContactsAppEjb.NODE_CONTACT)) {
				long size = queryContactListByMsisdnSize(userInfo);
				if (size == 0) {
					log.info("was not found in the document.");
					return new ResultData(ResultData.STATUS_404, "");
				} else if (size == 1) {
					StringBuilder contactXml = new StringBuilder();

					constructContactNode(contactXml,
							queryContactListByMsisdn(userInfo));
					return new ResultData(ResultData.STATUS_200,
							contactXml.toString());
				} else {
					new ResultData(
							ResultData.STATUS_409,
							new XCAPErrors.NoParentConflictException("Contacts")
									.getResponseContent());
				}
			} else if (condition1.matches(PATTERN_CONTACT_INDEX)) {
				log.info("get contact node by index");
				Pattern p = Pattern.compile("\\d+");
				Matcher match = p.matcher(condition1);
				if (match.find()) {
					int indexTemp = Integer.valueOf(match.group(0));
					log.info("index:" + indexTemp);
					Contact entity = getByIndex(userInfo, indexTemp, 1);

					return getStatusAndXml(entity);
				}

			} else if (condition1.matches(PATTERN_CONTACT_UNIQUE_ATTR)) {
				log.info("get contact node by database id.");

				int beginIndex = condition1.indexOf("=\"");
				int endIndex = condition1.indexOf("\"]");

				if (beginIndex != -1 && endIndex != -1 && beginIndex < endIndex) {
					String method = condition1.substring(beginIndex + 2,
							endIndex);
					Contact entity = getByContactMethod(
							userInfo, method);
					return getStatusAndXml(entity);
				}
			} else {
				return new ResultData(ResultData.STATUS_404, "");
			}
		} else if (condition1.startsWith(UABContactsAppEjb.NODE_LIST)) {

		}

		return new ResultData(ResultData.STATUS_404, null);
	}

	private ResultData getStatusAndXml(Contact entity) {
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
			List<Contact> list) {
		if (list != null) {
			for (Contact en : list) {
				constructContactNode(xmlBuilder, en);
			}
		}
	}

	private static void constructContactNode(StringBuilder xmlBuilder,
			Contact en) {
		String method = en.getContactMethod();
		if (en != null && method != null) {
			String contactName = en.getContactName();
			Long rawId = en.getRawID();
			Long deviceId = en.getDeviceID();

			// String id = en.getId().toString();

			xmlBuilder
					.append("<contact method=\"".concat(method).concat("\">"))
					.append("<contactName>".concat(
							contactName != null ? contactName : "").concat("</contactName>"))
					.append("<rawId>".concat(
							rawId != null ? rawId.toString() : "").concat("</rawId>"))
					.append("<deeviceId>".concat(
							deviceId != null ? deviceId.toString() : "").concat("</deeviceId>"))
					.append("</contact>");
		}
	}
	
	/**
	 * @param nodeTagName
	 * @return
	 */
	public static String xmlMappingEntityField(String nodeTagName){
		if(nodeTagName.equals(NODE_LEAF_RAW_ID)){
			return "rawID";
		}else if(nodeTagName.equals(NODE_LEAF_DEVICE_ID)){
			return "deviceID";
		}
		return nodeTagName;
	}
	
	public class LongConvert implements Converter{
		public Object convert(Class arg0, Object arg1) {
			String p = (String)arg1;
			return (p != null && p.trim().length() != 0) ?  Long.valueOf(p) : null;
		}
	}
	
	public void saveOrUpdate(Contact contact) {
		contactExchangeIfc.createContact(contact);
	}
		
	public List<Contact> queryContactListByMsisdn(String msisdn){
		//from UABContactEntity u where u.id=:id and u.msisdn=:msisdn
		return null;
	}

	/**
	 * 
	 * @param userId
	 * @param contactId
	 * @return null or a contact
	 */
	public Contact getById(String msisdn, long contactId) {
		Contact contact = contactExchangeIfc.getContactById(contactId);
		return contact != null && contact.getMsisdn().equals(msisdn) ? contact : null; 	
	}
	
	public long queryContactListByMsisdnSize(String msisdn){
		//select count(id) size from t_contacts where msisdn = :msisdn
		return 0;
	}

	/**
	 * @param userId
	 * @param index
	 *            > 1, other is invalidate
	 * @return a contact or null;
	 */
	public Contact getByIndex(String msisdn, int begin, int end) {
		//select * from t_contacts where msisdn = :msisdn limit :index,1
		return null;
	}

	public Contact getByContactMethod(String msisdn,String method){
		//from UABContactEntity where contactMethod=:method and msisdn=:msisdn
		return null;
	}	
	
	public int deleteContacts(String msisdn) {
		//delete from UABContactEntity where msisdn = :msisdn
		return 0;
	}

	/**
	 * @param userId
	 * @param index
	 *            >= 1
	 * @return -1 index invalid ,or delete row mount.
	 */
	public int deleteContactByIndexSelector(String msisdn, int index) {
		//select * from t_contacts where msisdn = :msisdn limit :index,1
		return 0;
	}

	public int deleteContactByUniqueAttr(String msisdn, String method) {
		//delete from UABContactEntity where msisdn = :msisdn and contactMethod = :method
		return 0;
	}
	
}
