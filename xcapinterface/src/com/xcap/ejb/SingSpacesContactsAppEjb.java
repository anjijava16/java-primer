package com.xcap.ejb;

import http.singcontacts.TestBase;

import java.io.File;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.LocalBinding;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.xcap.dao.SingSpacesContactsDao;
import com.xcap.dao.entity.SingSpacesContactEntity;
import com.xcap.ifc.XCAPDatebaseLocalIfc;
import com.xcap.ifc.error.XCAPErrors;

/**
 * 
 * <li>author slieer</li>
 * <li>Create Date 2011-10-29</li>
 * <li>version 1.0</li>
 */
@Stateless
@Local(value = XCAPDatebaseLocalIfc.class)
@LocalBinding(jndiBinding = XCAPDatebaseLocalIfc.SING_SPACE_CONTACTS_LOCAL_JNDI)
public class SingSpacesContactsAppEjb implements XCAPDatebaseLocalIfc{
	public static final Logger log = Logger.getLogger(SingSpacesContactsAppEjb.class);

	final static String NODE_CONTACTS = "contacts";
	final static String NODE_CONTACT = "contact";
		
	final static String NODE_ADR = "adr";  // thrid node, has leaf node.
	final static String NODE_NAME = "name";
	final static String NODE_TEL = "tel";
	final static String NODE_EMAIL = "email";
	final static String NODE_ORG = "org";
	final static String NODE_URL = "url";
	
	final static String NODE_FN = "fn";   //fourth node, leaf node.
	final static String NODE_LN = "ln";	
	final static String NODE_ITEM = "item";
	
	final static String NODE_DISPNAME = "dispName"; //thrid node.
	final static String NODE_BDAY = "bday";
	final static String NODE_TITLE = "title";
	final static String NODE_NOTE = "note";
	final static String NODE_LASTMODIFY = "lastModify";
	
	/**
	 * contact unique attribute is id.
	 */
	final static String NODE_ATTR_ID = "id"; 
	final static String NODE_ATTR_TYPE = "type";

	@PersistenceContext(unitName="SingSpacesXCAP")
	EntityManager em;	

	private static SingSpacesContactsDao contactsDao;
	
	@PostConstruct
	public void postConstruct() {
		//contactsDao = new SingSpacesContactsDao(em);
	}
	
	
	@Override
	public ResultData get(String userId, String nodeSelector) {
		long userIdTemp = Long.valueOf(userId);
		if(isDocSelector(nodeSelector)){
			log.info("------------------get singspaces document ");
			
			StringBuilder builder = new StringBuilder();
			builder.append("<contacts xmlns=\"UABContacts\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"contacts site/UABContacts.xsd\">");

			List<SingSpacesContactEntity> list = contactsDao.getList(userIdTemp);
			boolean re = getContacts(list, builder);
			builder.append("</contacts>");
			if(re){
				return new ResultData(ResultData.STATUS_200, builder.toString());
			}
		}else if(nodeSelector != null ){
			String[] nodePartArr = nodeSelector.split("/");
			if(nodePartArr.length > 1 && nodePartArr[0].equals(NODE_CONTACTS)){
				String secondSelector =  nodePartArr[1];
				log.info("get contact, secondSelector:" + secondSelector);
				if(nodePartArr.length == 2 && nodePartArr[1].contains(NODE_CONTACT)){  //second layer selector.
					log.info("second layer selector proccess .....");
					SingSpacesContactEntity entity = getEntity(secondSelector, userIdTemp);
					if(entity != null){
						StringBuilder builder = new StringBuilder();
						if(getContact(entity,builder)){
							return new ResultData(ResultData.STATUS_200, builder.toString());
						}
					}
				}else if(nodePartArr.length == 3){ //third layer selector.
					String thridSelector =  nodePartArr[2];
					SingSpacesContactEntity entity = getEntity(secondSelector, userIdTemp);
					if(thridSelector.matches(SelectorPattern.PATTERN_THIRD_LAYER_SELECTOR_BY_INDEX)){
						int end = thridSelector.indexOf("[");
						log.info("--------thridSelector  by index:" + thridSelector);
						thridSelector = thridSelector.substring(0, end);
					}
					String entityFieldName = SelectorPattern.titleFieldMapping.get(thridSelector);
					if(entityFieldName != null && entity != null){
						String fieldValue = null; 
						try {
							fieldValue = BeanUtils.getSimpleProperty(entity, entityFieldName);
						} catch (Exception e) {
							e.printStackTrace();
							log.error(e.getMessage());
						}
						String xml = null;
						if(isThirdLayerNotLeafNode(thridSelector)){
							xml = SelectorPattern.SIMPLE_NODE_FORMAT.format(new Object[]{secondSelector,fieldValue});
							
						}else if(isThirdLayerLeafNode(thridSelector)){
							String[][] part = splitByVerticalLineAndColon(fieldValue);
							xml = constructItemsNode(thridSelector,part);
						} else if (entityFieldName.equals(NODE_NAME)){							
							String[] namePartArr = fieldValue.split(";");
							xml = SelectorPattern.NAME_NODE_FORMAT.format(new Object[]{namePartArr[0], namePartArr[1]});
						}
						if(xml != null){
							return new ResultData(ResultData.STATUS_200, xml);							
						}
					}					
					
				}else if(nodePartArr.length == 4){ //fourth layer selector.  //NODE_ITEM or NODE_FN  NODE_LN.
					String thridSelector =  nodePartArr[2];
					String fourthSelector =  nodePartArr[3];
					
					if(thridSelector.matches(SelectorPattern.PATTERN_THIRD_LAYER_SELECTOR_BY_INDEX)){
						int end = thridSelector.indexOf("[");
						log.info("--------thridSelector  by index:" + thridSelector);
						thridSelector = thridSelector.substring(0, end);
					}
					
					if(isThirdLayerNotLeafNode(thridSelector) || thridSelector.equals(NODE_NAME)){
						SingSpacesContactEntity entity = getEntity(secondSelector, userIdTemp);
						
						String entityFieldName = SelectorPattern.titleFieldMapping.get(thridSelector);
						if(entityFieldName != null && entity != null){
							String fieldValue = null;
							try {
								fieldValue = BeanUtils.getSimpleProperty(entity, entityFieldName);
							} catch (Exception e) {
								e.printStackTrace();
							} 							
							
							String[][] valueArray = splitByVerticalLineAndColon(fieldValue);
							if(fourthSelector.equals(NODE_ITEM)){
								if(valueArray != null && valueArray.length == 1){
									StringBuilder result = new StringBuilder();
									constructItemNode(result,valueArray[0]); 
									return new ResultData(ResultData.STATUS_200, result.toString());
								}
							}else if (fourthSelector.matches(SelectorPattern.PATTERN_FOURTH_SELECTOR_INDEX)){
								if(valueArray != null){
									Pattern p = Pattern.compile("\\d+");
									Matcher match = p.matcher(nodeSelector);
									if(match.find()){
										String indexTemp = match.group(0);
										int index = Integer.valueOf(indexTemp);
										if(index < valueArray.length){
											StringBuilder result = new StringBuilder();
											constructItemNode(result,valueArray[index]); 
											return new ResultData(ResultData.STATUS_200, result.toString());											
										}
 									}
								}
							}else if(fourthSelector.matches(SelectorPattern.PATTERN_FOURTH_SELECTOR_UNIQUE_ATTR)){
								if(valueArray != null){
									int beginIndex = secondSelector.indexOf("=\"");
									int endIndex = secondSelector.indexOf("\"]");

									if (beginIndex != -1 && endIndex != -1 && beginIndex < endIndex) {
										String contactIdTemp = fourthSelector.substring(beginIndex, endIndex); //is attribute type
										int isUnique = -1;  //0 OK, 1 not unique.
										String[] attrNameValue = null;
										for (int i = 0; i < valueArray.length; i++) {
											String[] mapping = valueArray[i];
											if(mapping.length > 1){
												String attr = mapping[0];
												if(attr.equals(contactIdTemp)){
													if(isUnique == -1){
														isUnique = 0;
														attrNameValue = mapping;
													}
													if(isUnique == 0){
														isUnique = 1;
													}
												}
											}
										}
										switch (isUnique) {
										case -1:
											//404
											break;
										case 0:
											//ok
											StringBuilder xml = new StringBuilder();
											constructItemNode(xml, attrNameValue);
											return new ResultData(ResultData.STATUS_200, xml.toString()); 
										case 1:
											return new ResultData(ResultData.STATUS_409, new XCAPErrors.UniquenessFailureConflictException().getResponseContent());
										}										
									}
								}
							}else if(fourthSelector.equals(NODE_FN) || fourthSelector.matches(SelectorPattern.PATTERN_FOURTH_SELECTOR_FN_INDEX)){
								if(fieldValue != null){
									String[] nameArray = fieldValue.split(";");
									if(nameArray.length > 1){
										return  new ResultData(ResultData.STATUS_200, "<".concat(NODE_FN).concat(">").concat(nameArray[0]).concat("</" + NODE_FN+ ">"));
										
									}
								}
								
							}else if(fourthSelector.equals(NODE_LN) || fourthSelector.matches(SelectorPattern.PATTERN_FOURTH_SELECTOR_LN_INDEX)){
								String[] nameArray = fieldValue.split(";");
								if(nameArray.length > 1){
									return  new ResultData(ResultData.STATUS_200, "<".concat(NODE_LN).concat(">").concat(nameArray[1]).concat("</" + NODE_LN + ">"));
									
								}								
							}
						}
					}
				}			
			}
		}
		return new ResultData(ResultData.STATUS_404, ""); 
	}
	
	@Override
	public ResultData put(String userId_, String nodeSelector, String xml) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setNamespaceAware(true);
	    long userId = Long.valueOf(userId_);
	    
	    Document doc = null;
	    Element element = null;
	    String topTagName = null;
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(new InputSource(new StringReader(xml)));	
			
			element = doc.getDocumentElement();
			topTagName = element.getTagName();		
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		if(isDocSelector(nodeSelector)){
			
		}else{
			String secondLayerSelector = null; 
			String thirdLayerSeletor = null;
			String fourthLayerSelector = null;
			if(nodeSelector != null){
				String selectorArr[] = nodeSelector.split("/");
				if(selectorArr.length == 2){
					secondLayerSelector = selectorArr[1];
				}else if(selectorArr.length == 3){
					secondLayerSelector = selectorArr[1];
					thirdLayerSeletor = selectorArr[2];
				}else if(selectorArr.length == 4){
					secondLayerSelector = selectorArr[1];
					thirdLayerSeletor = selectorArr[2];
					fourthLayerSelector = selectorArr[3];
				}
			}
		    log.info("------------document top tag name is:" + topTagName + ". secondLayerSelector:" + secondLayerSelector
		    		+ " thirdLayerSeletor:" + thirdLayerSeletor + " fourthLayerSelector:" + fourthLayerSelector);

		    
		}
		
		return null;
	}

	@Override
	public ResultData delete(String userId, String nodeSelector) {
		return null;
	}
	
	/**
	 * 
	 * @param nodeSelector
	 * @return param 1 is document selector return true;
	 */
	private static boolean isDocSelector(String nodeSelector) {
		return nodeSelector == null || nodeSelector.equals(NODE_CONTACTS);
	}	
	
	private static boolean isThirdLayerLeafNode(String thridSelector) {
		return (thridSelector.equals(NODE_DISPNAME)
				|| thridSelector.equals(NODE_BDAY)
				|| thridSelector.equals(NODE_TITLE)
				|| thridSelector.equals(NODE_NOTE) || thridSelector
				.equals(NODE_LASTMODIFY)) ? true : false;
	}

	private static boolean isThirdLayerNotLeafNode(String thridSelector) {
		return thridSelector.equals(NODE_ADR) || thridSelector.equals(NODE_TEL)
				|| thridSelector.equals(NODE_EMAIL)
				|| thridSelector.equals(NODE_ORG)
				|| thridSelector.equals(NODE_URL) ? true : false;

	}
	
	/**
	 * @author slieer
	 * Create Date2011-11-1
	 * version 1.0
	 */
	static class SelectorPattern{
		final static String PATTERN_THIRD_LAYER_SELECTOR_BY_INDEX = "^\\w+\\[1\\]$";

		final static String PATTERN_CONTACT_INDEX = "^contact\\[\\d+\\]$";
		final static String PATTERN_CONTACT_UNIQUE_ATTR  = "^contact\\[@".concat(NODE_ATTR_ID).concat("=\"\\S+\"\\]$");
		
		final static String PATTERN_FOURTH_SELECTOR_INDEX = "^item\\[\\d+\\]$";
		final static String PATTERN_FOURTH_SELECTOR_UNIQUE_ATTR = "^item\\[@".concat(NODE_ATTR_TYPE).concat("=\"\\S+\"\\]$");
		
		final static String PATTERN_FOURTH_SELECTOR_FN_INDEX = "^fn\\[1\\]$";
		final static String PATTERN_FOURTH_SELECTOR_LN_INDEX = "^ln\\[1\\]$";
		
		final static MessageFormat SIMPLE_NODE_FORMAT = new MessageFormat("<{0}>{1}</{0}>");
		final static MessageFormat NAME_NODE_FORMAT = new MessageFormat("<name><fn>{0}</fn><ln>{1}</ln></name>");
		
		final static Map<String,String> titleFieldMapping = new HashMap<String, String>(32);
		static {
			titleFieldMapping.put(NODE_NAME, "contactFN");
			titleFieldMapping.put(NODE_DISPNAME,"contactN");
			
			titleFieldMapping.put(NODE_BDAY, "contactBDay");
			titleFieldMapping.put(NODE_ADR, "contactADR");

			titleFieldMapping.put(NODE_TEL, "contactTEL");
			titleFieldMapping.put(NODE_EMAIL,"contactEmail");
			
			titleFieldMapping.put(NODE_TITLE, "contactTitle");
			titleFieldMapping.put(NODE_ORG, "contactORG");
			
			titleFieldMapping.put(NODE_NOTE, "contactNote");
			titleFieldMapping.put(NODE_LASTMODIFY, "lastModify");
			
			titleFieldMapping.put(NODE_URL, "contactURL");
		}
	}
	
	private static SingSpacesContactEntity getEntity(String secondSelector,long userIdTemp) {
		if (secondSelector.equals(NODE_CONTACT)) {
			// by tag name
			long count = contactsDao.getListSize(userIdTemp);
			log.info("get contact by tagName, record count is " + count);
			if(count == 1){
				return contactsDao.getByIndex(userIdTemp, 0);				
			}
		} else if (secondSelector
				.matches(SelectorPattern.PATTERN_CONTACT_INDEX)) {
			// by index
			log.info("get contact by index, index selector:" + secondSelector);
			Pattern p = Pattern.compile("\\d+");
			Matcher match = p.matcher(secondSelector);
			if (match.find()) {
				int index = Integer.valueOf(match.group(0));
				if (index >= 1) {
					return contactsDao.getByIndex(userIdTemp, index - 1);
				}

			}
		}else if (secondSelector
				.matches(SelectorPattern.PATTERN_CONTACT_UNIQUE_ATTR)) {
			// by unique attr
			log.info("get contact by unique attr, unique attr selector:" + secondSelector);
			int beginIndex = secondSelector.indexOf("=\"");
			int endIndex = secondSelector.indexOf("\"]");

			if (beginIndex != -1 && endIndex != -1 && beginIndex < endIndex) {
				String idTemp = secondSelector.substring(beginIndex + 2,
						endIndex);

				return contactsDao.getByUniqueAttr(userIdTemp,
						Long.valueOf(idTemp));
			}
		}
		return null;
	}	
	
	/**
	 * 
	 * @param entity
	 * @param builder
	 * @return false builder not append string.
	 */
	private static boolean getContact(SingSpacesContactEntity entity, StringBuilder builder){
		if(entity != null){
			String id = entity.getContactId().toString();
			builder.append("<".concat(NODE_CONTACT).concat(" id=\"").concat(id).concat("\">"));

			String name = entity.getContactN();   //Greg;Zhang;;;
			String title = entity.getContactTitle();
			String bday = entity.getContactBDay();
			String adr = entity.getContactADR();  //HOME:;;\;llll;7676;76767;567567 567;76657565|HOME:;;省;城市;街道;邮政编码;国家|HOME:;;;;;77;|HOME:;;;;;;ffff|HOME:;;;67;5;777;|HOME:;;;;567;;|HOME:;;;;56756;;|
			String tel = entity.getContactTEL();  //CELL:+8613910193672|:+861085205599|WORK:+861085205588|WORK:+861085205205|
			String email = entity.getContactEmail(); //:adff@hhhk.uhh|HOME:gggj@ggghhj,.nn|HOME:ggghnnnn|:ffgg@ddff.gbb|WORK:gggg@,bvb,.hhg|WORK:gggf@ttt.mmb|WORK:ggg@gfsr.bb|
			String fn = entity.getContactFN();  
			
			String org = entity.getContactORG();		
			String url = entity.getContactURL();
			String note = entity.getContactNote();
			Date last = entity.getLastModify();
			
			String[] namePartArr = name.split(";");
			String[][] adrInfo = splitByVerticalLineAndColon(adr);
			String[][] telInfo = splitByVerticalLineAndColon(tel);
			String[][] emailInfo = splitByVerticalLineAndColon(email);
			String[][] orgInfo = splitByVerticalLineAndColon(org);
			String[][] urlInfo = splitByVerticalLineAndColon(url);
			
			builder.append(SelectorPattern.SIMPLE_NODE_FORMAT.format(new Object[]{NODE_DISPNAME, fn == null ? "" : fn}))
			.append(SelectorPattern.NAME_NODE_FORMAT.format(new Object[]{namePartArr.length > 0 ?namePartArr[0] : "", namePartArr.length > 1 ?namePartArr[1] : ""}))
			.append(SelectorPattern.SIMPLE_NODE_FORMAT.format(new Object[]{NODE_BDAY, bday == null ? "" : bday}))
			.append(constructItemsNode(NODE_ADR, adrInfo))
			.append(constructItemsNode(NODE_TEL, telInfo))
			.append(constructItemsNode(NODE_EMAIL, emailInfo))
			.append(SelectorPattern.SIMPLE_NODE_FORMAT.format(new Object[]{NODE_TITLE,title == null ? "" : title}))
			.append(constructItemsNode(NODE_ORG, orgInfo ))    /*org*/
			.append(SelectorPattern.SIMPLE_NODE_FORMAT.format(new Object[]{NODE_NOTE, note == null ? "" : note}))
			.append(constructItemsNode(NODE_URL, urlInfo))
			.append(SelectorPattern.SIMPLE_NODE_FORMAT.format(new Object[]{NODE_LASTMODIFY,dateFormat.format(last)}));
			
			builder.append("</".concat(NODE_CONTACT).concat(">"));
			return true;
		}
		
		return false;
	}
	
	private boolean getContacts(List<SingSpacesContactEntity> list,StringBuilder builder){
		if(list != null){
			for(SingSpacesContactEntity en :list){
				getContact(en, builder);
			}
			return true;
		}
		return false;
	}

	/**
	 * <li>CELL:+8613910193672|:+861085205599|WORK:+861085205588|WORK:+861085205205|
	 * <li>first split by vertical line.
	 * <li>second split by coln(:)
	 * @param adr
	 * @return
	 */
	private static String[][] splitByVerticalLineAndColon(String adr) {
		log.info("splitByVerticalLineAndColon:" + adr);
		if(adr != null){
			String[] addrArr = adr.split("\\|");
			String[][] adrInfo = new String[addrArr.length][];
			for(int i = 0; i < addrArr.length; i++){
				String temp = addrArr[i];
				String[] typeValue = temp.split(":");
				if(typeValue.length == 2){
					String[] oneAdr = new String[]{typeValue[0], typeValue[1]};				
					adrInfo[i] = oneAdr;
				}else if(typeValue.length == 1){
					String[] oneAdr = new String[]{"", typeValue[1]};				
					adrInfo[i] = oneAdr;				
				}
			}
			return adrInfo;			
		}else {
			return null;
		}
	}
	
	/**
	 * construct this node:
	 * 	<email>
	 *		<item type="">xie@gmail.com</item>
	 *		<item type="PREF">hong@hotmail.com</item>
	 *		<item type="HOME">ong@hotmail.com</item>
	 *		<item type="WORK">xie@gmail.com</item>
	 *	</email>
     *
	 * @param parentNode
	 * @param itemInfo
	 * @return if itemInfo is null return ""
	 */
	private static String constructItemsNode(String parentNode, String[][] itemInfo){
		String head = "<".concat(parentNode).concat(">");
		StringBuilder result = new StringBuilder(head);
		//boolean hasFlag = true;
		if(itemInfo != null){
			for(int i = 0; i < itemInfo.length; i ++){
				String[] nameValuePair = itemInfo[i];
				if(nameValuePair != null && nameValuePair.length == 2){
					//hasFlag = false;
					constructItemNode(result, nameValuePair);
				}
			}
		}
		result.append("</".concat(parentNode).concat(">"));
		
		return result.toString();
	}


	private static void constructItemNode(StringBuilder result,
			String[] nameValuePair) {
		String name = nameValuePair[0];
		String value = nameValuePair[1];
		result.append("<".concat(NODE_ITEM).concat(" ").concat(NODE_ATTR_TYPE).concat("=\"").concat(name).concat("\">"));
		result.append(value);
		result.append("</".concat(NODE_ITEM).concat(">"));
	}
	
	private static void deconstructItemNode(){
		
	}
	
	@Test
	public void test(){
		File xmlFile = TestBase.getXmlFilePath("example-new-contacts.xml");
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setNamespaceAware(true);
	    
	    Document doc = null;
	    Element element = null;
	    String topTagName = null;
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(xmlFile);	
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		element = doc.getDocumentElement();
		topTagName = element.getTagName();		

		//System.out.println("topTagName:" + topTagName);
		NodeList nodes = element.getElementsByTagName("contact");
		
		xmlToEntitys(element);
	}
	
	private static List<SingSpacesContactEntity> xmlToEntitys(Element conatcts){
		List<SingSpacesContactEntity> list = new ArrayList<SingSpacesContactEntity>();
		NodeList nodes = conatcts.getElementsByTagName(NODE_CONTACT);
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			NodeList children = node.getChildNodes();
			
			SingSpacesContactEntity entity = new SingSpacesContactEntity();
			list.add(entity);
			
			for(int j = 0; j < children.getLength(); j++){
				String nodeValue = null;
				String nodeName = null;
				Node ch = children.item(j);
				nodeName = ch.getNodeName();
				
				NodeList childList = ch.getChildNodes();
				if(! nodeName.equals("#text") && childList.getLength() != 0){
					
					if(nodeName.equals(NODE_DISPNAME) 
							|| nodeName.equals(NODE_BDAY) 
							|| nodeName.equals(NODE_TITLE) 
							|| nodeName.equals(NODE_NOTE) 
							|| nodeName.equals(NODE_LASTMODIFY)){
						//System.out.println("-----------leaf node----");
						Node node1 = childList.item(0);
						nodeValue = node1.getNodeValue();
												
					}else if(nodeName.equals(NODE_ADR)
							||nodeName.equals(NODE_TEL) 
							||nodeName.equals(NODE_EMAIL) 
							||nodeName.equals(NODE_ORG) 
							||nodeName.equals(NODE_URL)){
						//System.out.println("-----------include item node----");
						for(int k = 0; k < childList.getLength(); k++){
							Node itemNode = childList.item(k);
							if(! itemNode.getNodeName().equals("#text")){
								Node item = itemNode.getChildNodes().item(0);
								NamedNodeMap attrMap = itemNode.getAttributes();
								Node type = null;
								if(attrMap != null){
									type = attrMap.getNamedItem("type");									
								}
								
								String tempType = (type  != null) ? type .getNodeValue() : "";
								String nodeVal = item.getNodeValue();
								String tempVal = tempType.concat(":").concat(nodeVal).concat(";");
								if(nodeValue == null){
									nodeValue = tempVal;
								}else{
									nodeValue.concat(tempVal);
								}
								
								log.info("nodeValue".concat(":").concat(nodeValue));
								//System.out.println(tempType.concat(":").concat(nodeVal).concat(";"));
								//System.out.println(nodeName + ":"+ itemNode + ":" + item + ":" + (type  != null ? type .getNodeValue() : "-"));								
							}
						}
					}else if(nodeName.equals(NODE_NAME)){
						for(int k = 0; k < childList.getLength(); k++){
							Node nameNode = childList.item(k);
							String nameNodeName = nameNode.getNodeName();
							if(nameNodeName.equals(NODE_FN) || nameNodeName.equals(NODE_LN)){
								String namePart = nameNode.getChildNodes().item(0).getNodeValue() + ";";									
								if(nodeValue == null){
									nodeValue = namePart;
								}else{
									nodeValue = nodeValue.concat(namePart);
								}
								//System.out.println("name:" + nodeValue);
								
							}
						}
					}	
					
					
					String fieldName = SelectorPattern.titleFieldMapping.get(nodeName);
					//System.out.println("------------" + fieldName + ":" + nodeName + "=" + nodeValue);
					try {
						BeanUtils.setProperty(entity, fieldName, nodeValue);
						
					} catch (Exception e) {
						log.info("bean set property Exception");
						e.printStackTrace();
					}
				}
			}

			System.out.println(entity);
		}
		return list;
	}
}
