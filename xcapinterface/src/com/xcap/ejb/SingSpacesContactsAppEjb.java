package com.xcap.ejb;

import java.io.StringReader;
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
 * <li>author slieer</li>
 * <li>Create Date 2011-10-29</li>
 * <li>version 1.0</li>
 */
@Stateless
@Local(value = XCAPDatebaseLocalIfc.class)
@org.jboss.annotation.ejb.LocalBinding(jndiBinding = XCAPDatebaseLocalIfc.SING_SPACE_CONTACTS_LOCAL_JNDI)
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
		contactsDao = new SingSpacesContactsDao(em);
	}
	
	
	@Override
	public ResultData get(String userId, String nodeSelector) {
		long userIdTemp = Long.valueOf(userId);
		if(SingConstant.isDocSelector(nodeSelector)){
			log.info("get singspaces document by userId, userId=" + userId);
			
			StringBuilder builder = new StringBuilder();
			builder.append("<contacts xmlns=\"contacts\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"")
			.append(" xsi:schemaLocation=\"contacts xcap-schema/SingSpacesContacts\">");

			List<SingSpacesContactEntity> list = contactsDao.getList(userIdTemp);
			boolean re = getContacts(list, builder);
			builder.append("</contacts>");
			if(re){
				return new ResultData(ResultData.STATUS_200, builder.toString());
			}
		}else if(nodeSelector != null ){
			String[] nodePartArr = nodeSelector.split("/");
			log.info("selector layer amount is " + nodePartArr.length);
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
					if(thridSelector.matches(SingConstant.PATTERN_THIRD_LAYER_SELECTOR_BY_INDEX)){
						//enable thridSelector as node tag name. 
						int end = thridSelector.indexOf("[");
						thridSelector = thridSelector.substring(0, end);
					}
					log.info("get ,thridSelector:" + thridSelector);
					String entityFieldName = SingConstant.titleFieldMapping.get(thridSelector);
					log.info("entityFieldName:" + entityFieldName);
					log.info("entity.toString:" + entity);
					if(entityFieldName != null && entity != null){
						String fieldValue = null; 
						try {
							fieldValue = BeanUtils.getSimpleProperty(entity, entityFieldName);
							log.info("fieldValue:" + fieldValue);
						} catch (Exception e) {
							e.printStackTrace();
							log.error(e.getMessage());
						}
						String xml = null;
						if(SingConstant.isThirdLayerLeafNode(thridSelector)){
							log.info("get ThirdLayerLeafNode...");
							xml = SingConstant.SIMPLE_NODE_FORMAT.format(new Object[]{thridSelector,fieldValue});							
						}else if(SingConstant.isIncludeItemNode(thridSelector)){
							log.info("get ThirdLayerNotLeafNode and not name node......");
							String[][] part = splitByVerticalLineAndColon(fieldValue);
							xml = constructItemsNode(thridSelector,part);
						} else if (thridSelector.equals(NODE_NAME)){
							log.info("get name node...");
							String[] namePartArr = fieldValue.split(";");
							String fn = "", ln = "";
							if(namePartArr.length == 1){
								fn = namePartArr[0];
							}else {
								fn = namePartArr[0];
								ln = namePartArr[1];
							}
							xml = SingConstant.NAME_NODE_FORMAT.format(new Object[]{fn, ln});
						}
						if(xml != null){
							return new ResultData(ResultData.STATUS_200, xml);							
						}
					}					
					
				}else if(nodePartArr.length == 4){ //fourth layer selector.  //NODE_ITEM or NODE_FN  NODE_LN.					
					String thridSelector =  nodePartArr[2];
					String fourthSelector =  nodePartArr[3];
					log.info("four layer......thridSelector,fourthSelector:" + thridSelector + ","+ fourthSelector);
					
					if(thridSelector.matches(SingConstant.PATTERN_THIRD_LAYER_SELECTOR_BY_INDEX)){
						int end = thridSelector.indexOf("[");
						log.info("--------thridSelector  by index:" + thridSelector);
						thridSelector = thridSelector.substring(0, end);
					}
					
					if(SingConstant.isThirdLayerNotLeafNode(thridSelector) || thridSelector.equals(NODE_NAME)){
						SingSpacesContactEntity entity = getEntity(secondSelector, userIdTemp);
						
						String entityFieldName = SingConstant.titleFieldMapping.get(thridSelector);
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
							}else if (fourthSelector.matches(SingConstant.PATTERN_FOURTH_ITEM_SELECTOR_INDEX)){
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
							}else if(fourthSelector.matches(SingConstant.PATTERN_FOURTH_ITEM_SELECTOR_UNIQUE_ATTR)){
								if(valueArray != null){
									int beginIndex = secondSelector.indexOf("=\"");
									int endIndex = secondSelector.indexOf("\"]");

									if (beginIndex != -1 && endIndex != -1 && beginIndex < endIndex) {
										String contactIdTemp = fourthSelector.substring(beginIndex + 2, endIndex); //is attribute type
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
							}else if(fourthSelector.equals(NODE_FN) || fourthSelector.matches(SingConstant.PATTERN_FOURTH_SELECTOR_FN_INDEX)){
								if(fieldValue != null){
									String[] nameArray = fieldValue.split(";");
									if(nameArray.length > 1){
										return  new ResultData(ResultData.STATUS_200, "<".concat(NODE_FN).concat(">").concat(nameArray[0]).concat("</" + NODE_FN+ ">"));
										
									}
								}
								
							}else if(fourthSelector.equals(NODE_LN) || fourthSelector.matches(SingConstant.PATTERN_FOURTH_SELECTOR_LN_INDEX)){
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
		
		if(SingConstant.isDocSelector(nodeSelector)){
			log.info("put contacts node.");
			List<SingSpacesContactEntity> conatcts = xmlToEntitys(doc);
			contactsDao.deleteByUserId(userId);
			contactsDao.save(userId,conatcts);
			return new ResultData(ResultData.STATUS_200, "");
		}else{
			String secondLayerSelector = null; 
			String thirdLayerSeletor = null;
			String fourthLayerSelector = null;
			String selectorArr[] = nodeSelector.split("/");
			if(nodeSelector != null){
				if(selectorArr.length > 0 && !selectorArr[0].equals(NODE_CONTACTS)){
					return new ResultData(ResultData.STATUS_404, "");
				}
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
		    log.info("document top tag name is:" + topTagName + ". secondLayerSelector,thirdLayerSeletor,thirdLayerSeletor:" + secondLayerSelector
		    		+ "," + thirdLayerSeletor + "," + fourthLayerSelector);

		    if(topTagName.equals(NODE_CONTACT)){
		    	if(thirdLayerSeletor == null && thirdLayerSeletor == null){
		    		//selector:
		    		//by tag name
		    		if(secondLayerSelector.equals(NODE_CONTACT)){
		    			long size = contactsDao.getListSize(userId);
		    			switch ((int)size) {
						case 0:
							List<SingSpacesContactEntity> conatcts = xmlToEntitys(doc);
							contactsDao.save(userId,conatcts);
							return new ResultData(ResultData.STATUS_200, "");
						case 1:
							//delete first record by userId
							contactsDao.deleteContactByIndexSelector(userId, 1);
							conatcts = xmlToEntitys(doc);
							contactsDao.save(userId,conatcts);
							return new ResultData(ResultData.STATUS_200, "");
						}
		    		}else if(secondLayerSelector.matches(SingConstant.PATTERN_CONTACT_INDEX)){
		    			//by index
		    			int index = SingConstant.getIndex(secondLayerSelector);
		    			if(index >= 1){
		    				long size = contactsDao.getListSize(userId);
		    				if(index <= size){
		    					//update
		    					contactsDao.deleteContactByIndexSelector(userId, index);
				    			List<SingSpacesContactEntity> list = xmlToEntitys(doc);
				    			contactsDao.save(userId, list);
				    			return new ResultData(ResultData.STATUS_200, "");
		    				}else if(index == size +1){
		    					//add
				    			List<SingSpacesContactEntity> list = xmlToEntitys(doc);
				    			contactsDao.save(userId, list);
				    			return new ResultData(ResultData.STATUS_200, "");
		    				}
		    			}
		    			
		    		}else if(secondLayerSelector.matches(SingConstant.PATTERN_CONTACT_UNIQUE_ATTR)){
		    			//by unique attr(contact id.)
		    			log.info("put contact by unique attribute...");
		    			long attr = SingConstant.getUniqueAttrValue(secondLayerSelector);
		    			if(attr != -1){
		    				SingSpacesContactEntity en = contactsDao.getByUniqueAttr(userId, attr);
		    				if(en != null){
		    					em.remove(en);
		    				}		    				
		    			}
		    			List<SingSpacesContactEntity> list = xmlToEntitys(doc);
		    			contactsDao.save(userId, list);
		    			return new ResultData(ResultData.STATUS_200, "");
		    		}
		    		
		    	}
		    	//is xml document node tag name equals the 3rd layer selector.
		    }else if(topTagName.equals(SingConstant.getNodeNameByThirdSelector(thirdLayerSeletor))){
		    	SingSpacesContactEntity selectorRecord = secondLayerSelectorResult(
						userId, secondLayerSelector);
	    		
		    	if(selectorRecord != null){
		    		NodeList nodelist = element.getChildNodes();
		    		String fieldValue = null;
		    		if(SingConstant.isThirdLayerLeafNode(topTagName)){
						Node node1 = nodelist.item(0);
						if(node1 != null){
							fieldValue = node1.getNodeValue();							
						}else{
							fieldValue = null;
						}
		    		}else if(SingConstant.isIncludeItemNode(topTagName)){
		    			fieldValue = xmlItemNodeToString(nodelist);
		    		}else if(NODE_NAME.equals(topTagName)){
						fieldValue = xmlNameNodeToString(nodelist);
		    		}
		    		
	    			String fieldName = SingConstant.titleFieldMapping.get(topTagName);
	    			try {
	    				BeanUtils.setProperty(selectorRecord, fieldName, fieldValue);
	    				em.merge(selectorRecord);
	    				return new ResultData(ResultData.STATUS_200, "");
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    			}		    					    		
		    	}
		    }else if(SingConstant.isFourthLayerLeafNode(topTagName)){
		    	String tagName = SingConstant.getNodeNameByThirdSelector(thirdLayerSeletor);
		    	if(! SingConstant.isThirdLayerNotLeafNode(tagName)){
		    		return new ResultData(ResultData.STATUS_404, "");
		    	}
		    	String fieldName = SingConstant.titleFieldMapping.get(tagName);
		    	log.info("entity field,4th layer selector:" + fieldName + "," + fourthLayerSelector);
		    	String fieldValue = null;
		    	SingSpacesContactEntity selectorRecord = secondLayerSelectorResult(userId,secondLayerSelector);
		    	if(selectorRecord != null){
			    	try {
						fieldValue = BeanUtils.getProperty(selectorRecord, fieldName);
					} catch (Exception e) {
						e.printStackTrace();
					}
			    	
			    	if(fourthLayerSelector.equals(NODE_FN) 
			    			||fourthLayerSelector.matches(SingConstant.PATTERN_FOURTH_SELECTOR_FN_INDEX)){
			    		log.info("put first name node...");
			    		Node n = element.getFirstChild();
						String valueFromXml = n.getNodeValue();;
			    		int index = fieldValue.indexOf(";");
			    		fieldValue = valueFromXml.concat(";").concat(fieldValue.substring(index));
			    	}else if(fourthLayerSelector.equals(NODE_LN) || fourthLayerSelector.matches(SingConstant.PATTERN_FOURTH_SELECTOR_LN_INDEX)){
			    		log.info("put last name node...");
			    		Node n = element.getFirstChild();
						String valueFromXml = n.getNodeValue();;
						
			    		int index = fieldValue.indexOf(";");
			    		int nextIndex = fieldValue.indexOf(";", index);
			    		fieldValue = valueFromXml.concat(fieldValue.substring(index, nextIndex));
			    		
			    	}else if(fourthLayerSelector.equals(NODE_ITEM)){
			    		log.info("put item node by tag name...");
			    		//item.
			    		String[] part = fieldValue.split("\\|");
			    		if(part.length <= 1){
			    			NodeList list = element.getChildNodes();
			    			fieldValue = xmlItemNodeToString(list);		    			
			    		}else{
			    			return new ResultData(ResultData.STATUS_404, "");
			    		}
			    		
			    	}else if(fourthLayerSelector.matches(SingConstant.PATTERN_FOURTH_ITEM_SELECTOR_INDEX)){
			    		log.info("put item node by index...");
			    		String[] part = fieldValue.split("\\|");
			    		int index = SingConstant.getIndex(fourthLayerSelector) -1;  //xcap index selector begin 1;
			    		if(index >=0 && index < part.length){
			    			//modify item
			    			String typeVal = getTypeVal(element);
			    			log.info("modify item. type:value:" + typeVal);
			    			fieldValue = fieldValue.replace(part[index], typeVal);
			    		}else if(index == part.length){
			    			//add item
			    			String typeVal = getTypeVal(element);
			    			log.info("append item. type:value:" + typeVal);
			    			fieldValue = fieldValue.concat(typeVal).concat("\\|");
			    		}else{
			    			return new ResultData(ResultData.STATUS_409, new XCAPErrors.CannotInsertConflictException().getResponseContent());
			    		}
			    	}else if(fourthLayerSelector.matches(SingConstant.PATTERN_FOURTH_ITEM_SELECTOR_UNIQUE_ATTR)){
			    		log.info("put item node by unique attr...");
			    		String type = element.getAttribute("type");
			    		type = type == null ? "" : type;
			    		String typeVal = getTypeVal(element);
			    		String[] part = fieldValue.split("\\|");
			    		
			    		boolean flag = false;
			    		for(int k = 0; k < part.length; k++){
			    			String typeValPair = part[k];
			    			String[] typeValArr = typeValPair.split(":");
			    			if(typeValArr.length == 2){
			    				String typeTemp = typeValArr[0] == null ? "" : typeValArr[0];
			    				String ValTemp = typeValArr[1] == null ? "" : typeValArr[1];
			    				if(type.equals(typeTemp)){
			    					flag = true;
			    					String typeValPart = typeTemp.concat(":").concat(ValTemp);
			    					fieldValue = fieldValue.replace(typeValPart, typeVal);
			    				}
			    			}
			    		}
			    		
			    		if(! flag){
			    			if(fieldValue.endsWith("|")){
			    				fieldValue = fieldValue.concat(typeVal).concat("|");
			    			}else{
			    				fieldValue = fieldValue.concat("|").concat(typeVal).concat("|");			    				
			    			}
			    		}
			    	}else{
			    		return new ResultData(ResultData.STATUS_404, "");
			    	}
			    	log.info("update contact.fieldName,fieldValue:" + fieldName + " " +fieldValue);
			    	try {
						BeanUtils.setProperty(selectorRecord, fieldName, fieldValue);
						em.merge(selectorRecord);
						return new ResultData(ResultData.STATUS_200, "");
					} catch (Exception e) {
						e.printStackTrace();
					}
		    	}
		    }
		}
		
		return new ResultData(ResultData.STATUS_404, "");
	}



	@Override
	public ResultData delete(String userId_, String nodeSelector) {
		long userId = Long.valueOf(userId_);
		int effectRows = -1;
		if(nodeSelector == null || nodeSelector.equals(NODE_CONTACTS)){
			//delete all
			effectRows = contactsDao.deleteByUserId(userId);
		}else{
			String[] selParts = nodeSelector.split("/");
			if(selParts != null && selParts.length ==2 && selParts[0].equals(NODE_CONTACTS)){
				if(selParts[1].equals(NODE_CONTACT)){
					log.info("delete contact by tagName...");
					long size = contactsDao.getListSize(userId);
					
					switch (Long.valueOf(size).intValue()) {
					case 0:
						return new ResultData(ResultData.STATUS_404,"");
					case 1:
						contactsDao.deleteContactByIndexSelector(userId, 1);
						return new ResultData(ResultData.STATUS_200, "");

					default:
						return new ResultData(ResultData.STATUS_409,
								new XCAPErrors.CannotDeleteConflictException().getResponseContent());
					}					
				}else if(selParts[1].matches(SingConstant.PATTERN_CONTACT_INDEX)){
					int index = SingConstant.getIndex(selParts[1]);
					log.info("delete contact by index ,index is " + index);
					effectRows = contactsDao.deleteContactByIndexSelector(userId, index);
				}else if(selParts[1].matches(SingConstant.PATTERN_CONTACT_UNIQUE_ATTR)){
					long contactId = SingConstant.getUniqueAttrValue(selParts[1]);
					log.info("delete contact by uniqueAttr, (contactId, userId)-> " + contactId + "," + userId);
				    effectRows = contactsDao.deleteByUserId(userId, contactId);
					
				}
			}
		}
		log.info("delete ok, effectRows is " + effectRows);
		return new ResultData(effectRows == 0 ? ResultData.STATUS_404 : ResultData.STATUS_200, "");
	}

	/**
	 * @author slieer
	 * Create Date2011-11-1
	 * version 1.0
	 */
	static class SingConstant{
		final static String PATTERN_THIRD_LAYER_SELECTOR_BY_INDEX = "^\\w+\\[1\\]$";

		final static String PATTERN_CONTACT_INDEX = "^contact\\[\\d+\\]$";
		final static String PATTERN_CONTACT_UNIQUE_ATTR  = "^contact\\[@".concat(NODE_ATTR_ID).concat("=\"\\S+\"\\]$");
		
		final static String PATTERN_FOURTH_ITEM_SELECTOR_INDEX = "^item\\[\\d+\\]$";
		final static String PATTERN_FOURTH_ITEM_SELECTOR_UNIQUE_ATTR = "^item\\[@".concat(NODE_ATTR_TYPE).concat("=\"\\S+\"\\]$");
		
		final static String PATTERN_FOURTH_SELECTOR_FN_INDEX = "^fn\\[1\\]$";
		final static String PATTERN_FOURTH_SELECTOR_LN_INDEX = "^ln\\[1\\]$";
		//final static String PATTERN_THIRD_SELECTOR_INDEX = "^\\w+\\[1\\]?+$";
		
		final static MessageFormat SIMPLE_NODE_FORMAT = new MessageFormat("<{0}>{1}</{0}>");
		final static MessageFormat NAME_NODE_FORMAT = new MessageFormat("<name><fn>{0}</fn><ln>{1}</ln></name>");
		
		/**
		 * xml node tag name--> entity bean field. 
		 */
		final static Map<String,String> titleFieldMapping = new HashMap<String, String>(32);
		static {
			titleFieldMapping.put(NODE_NAME, "contactN");
			titleFieldMapping.put(NODE_DISPNAME,"contactFN");
			
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
		
		/**
		 *is 3rd layer leaf node.
		 * @param tagName
		 * @return
		 */
		static boolean isThirdLayerLeafNode(String tagName){
			return tagName == null ? false :
			tagName.equals(NODE_DISPNAME) 
			|| tagName.equals(NODE_BDAY) 
			|| tagName.equals(NODE_TITLE) 
			|| tagName.equals(NODE_NOTE) 
			|| tagName.equals(NODE_LASTMODIFY); 
		}
		
		/**
		 * is 3rd laeyer not leaf node.(has child node)
		 * @param tagName
		 * @return
		 */
		static boolean isThirdLayerNotLeafNode(String tagName){
			return isIncludeItemNode(tagName) || tagName.equals(NODE_NAME);
		}
		
		/**
		 * has item node ? yes ,return ture ,or false;
		 * @param tagName
		 * @return
		 */
		static boolean isIncludeItemNode(String tagName){
			return tagName == null ? false : tagName.equals(NODE_ADR)
			|| tagName.equals(NODE_TEL) 
			||tagName.equals(NODE_EMAIL) 
			||tagName.equals(NODE_ORG) || tagName.equals(NODE_URL);
		}
		
		/**
		 * is fn,ln,item node?
		 * @param tagName
		 * @return
		 */
		static boolean isFourthLayerLeafNode(String tagName){
			return tagName == null ? false : tagName.equals(NODE_FN) 
			|| tagName.equals(NODE_LN) 
			|| tagName.equals(NODE_ITEM);
		}
		
		/**
		 * @param thirdSelector
		 * @return 3rd layer node tag name.
		 */
		static String getNodeNameByThirdSelector(String thirdSelector){
			if(thirdSelector != null){
				if(isThirdLayerLeafNode(thirdSelector) || isThirdLayerNotLeafNode(thirdSelector)){
					return thirdSelector;
				}else if(thirdSelector.matches(PATTERN_THIRD_LAYER_SELECTOR_BY_INDEX)){
					String temp = thirdSelector.substring(0, thirdSelector.length() -3);
					return (isThirdLayerLeafNode(temp) || isThirdLayerNotLeafNode(temp)) ? temp : null;
				}				
			}
			return null;
		}
		
		/**
		 * @param nodeSelector
		 * @return param 1 is document selector return true;
		 */
		static boolean isDocSelector(String nodeSelector) {
			return nodeSelector == null || nodeSelector.equals(NODE_CONTACTS);
		}
		
		/**
		 * 
		 * @param nodeSelector
		 * @return match \\d+ ,return first match, or -1
		 */
		static int getIndex(String nodeSelector){
			Pattern p = Pattern.compile("\\d+");
		
			Matcher match = p.matcher(nodeSelector);
			if(match.find()){
				String indexTemp = match.group(0);
				return Integer.valueOf(indexTemp);
			}
		 return -1;
		}
		
		/**
		 * 
		 * @param nodeSelector
		 * @return contactId or -1
		 */
		static long getUniqueAttrValue(String nodeSelector){
			int beginIndex = nodeSelector.indexOf("=\"");
			int endIndex = nodeSelector.indexOf("\"]");
			if (beginIndex != -1 && endIndex != -1 && beginIndex < endIndex) {
				String contactId = nodeSelector.substring(beginIndex + 2, endIndex); //is attribute type
				log.info("getUniqueAttrValue..." + contactId);
				if(contactId.matches("\\d+")){
					return Long.valueOf(contactId);					
				}
			
			}
			return -1;
		}
	}
	
	private static SingSpacesContactEntity getEntity(String secondSelector,long userIdTemp) {
		if (secondSelector.equals(NODE_CONTACT)) {
			// by tag name
			long count = contactsDao.getListSize(userIdTemp);
			log.info("get contact by tagName, record count is " + count);
			if(count == 1){
				return contactsDao.getByIndex(userIdTemp, 1);				
			}
		} else if (secondSelector
				.matches(SingConstant.PATTERN_CONTACT_INDEX)) {
			// by index
			log.info("get contact by index, index selector:" + secondSelector);
			Pattern p = Pattern.compile("\\d+");
			Matcher match = p.matcher(secondSelector);
			if (match.find()) {
				int index = Integer.valueOf(match.group(0));
				if (index >= 1) {
					return contactsDao.getByIndex(userIdTemp, index);
				}

			}
		}else if (secondSelector
				.matches(SingConstant.PATTERN_CONTACT_UNIQUE_ATTR)) {
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
			log.info(entity);
			String id = entity.getContactId().toString();
			builder.append("<".concat(NODE_CONTACT).concat(" ").concat(NODE_ATTR_ID).concat("=\"").concat(id).concat("\">"));

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
			
			builder.append(SingConstant.SIMPLE_NODE_FORMAT.format(new Object[]{NODE_DISPNAME, fn == null ? "" : fn}))
			.append(SingConstant.NAME_NODE_FORMAT.format(new Object[]{namePartArr.length > 0 ?namePartArr[0] : "", namePartArr.length > 1 ?namePartArr[1] : ""}))
			.append(SingConstant.SIMPLE_NODE_FORMAT.format(new Object[]{NODE_BDAY, bday == null ? "" : bday}))
			.append(constructItemsNode(NODE_ADR, adrInfo))
			.append(constructItemsNode(NODE_TEL, telInfo))
			.append(constructItemsNode(NODE_EMAIL, emailInfo))
			.append(SingConstant.SIMPLE_NODE_FORMAT.format(new Object[]{NODE_TITLE,title == null ? "" : title}))
			.append(constructItemsNode(NODE_ORG, orgInfo ))    /*org*/
			.append(SingConstant.SIMPLE_NODE_FORMAT.format(new Object[]{NODE_NOTE, note == null ? "" : note}))
			.append(constructItemsNode(NODE_URL, urlInfo))
			.append(SingConstant.SIMPLE_NODE_FORMAT.format(new Object[]{NODE_LASTMODIFY,dateFormat.format(last)}));
			
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
	 * <li>:http://www.google.com.hk/|:http://www.google.com.hk/|
	 * <li>first split by vertical line.
	 * <li>second split by coln(:)
	 * @param adr
	 * @return
	 */
	private static String[][] splitByVerticalLineAndColon(String adr) {
		log.info("splitByVerticalLineAndColon:" + adr);
		if(adr != null){
			String[] adrArr = adr.split("\\|");
			String[][] adrInfo = new String[adrArr.length][];
			for(int i = 0; i < adrArr.length; i++){
				String temp = adrArr[i];
				int splitIndex = temp.indexOf(":");
				if(splitIndex != -1){
					adrInfo[i] = new String[2];
					if(splitIndex == 0){
						adrInfo[i][0] = "";
					}else{
						adrInfo[i][0] = temp.substring(0,splitIndex);						
					}
					adrInfo[i][1] = temp.substring(splitIndex + 1, temp.length());
				}
			}
			return adrInfo;
		}else {
			return null;
		}
	}
	
	/**
	 * construct and return this node:
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
			
	private static List<SingSpacesContactEntity> xmlToEntitys(Document conatcts){
		List<SingSpacesContactEntity> list = new ArrayList<SingSpacesContactEntity>();
		NodeList nodes = conatcts.getElementsByTagName(NODE_CONTACT);
		log.info("xml contact size is " + nodes.getLength());
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			NodeList children = node.getChildNodes();
			
			SingSpacesContactEntity entity = new SingSpacesContactEntity();
			entity.setLastModify(new Date());
			list.add(entity);
			
			for(int j = 0; j < children.getLength(); j++){
				String nodeValue = null;
				String nodeName = null;
				Node ch = children.item(j);
				nodeName = ch.getNodeName();
				
				NodeList childList = ch.getChildNodes();
				if(! nodeName.equals("#text") && childList.getLength() != 0){
					
					if(SingConstant.isThirdLayerLeafNode(nodeName)){
						//System.out.println("-----------leaf node----");
						Node node1 = childList.item(0);
						nodeValue = node1.getNodeValue();
												
					}else if(SingConstant.isIncludeItemNode(nodeName)){
						//System.out.println("-----------include item node----");
						nodeValue = xmlItemNodeToString(childList);
					}else if(nodeName.equals(NODE_NAME)){
						nodeValue = xmlNameNodeToString(childList);
					}	
					
					String fieldName = SingConstant.titleFieldMapping.get(nodeName);
					//System.out.println("------------" + fieldName + ":" + nodeName + "=" + nodeValue);
					try {
						BeanUtils.setProperty(entity, fieldName, nodeValue);
					} catch (Exception e) {
						log.info("bean set property Exception");
						e.printStackTrace();
					}
				}
			}

			log.info(entity);
		}
		return list;
	}
	
	private static String xmlItemNodeToString(NodeList childList){
		String nodeValue = null;
		for(int k = 0; k < childList.getLength(); k++){
			Node itemNode = childList.item(k);
			if(! itemNode.getNodeName().equals("#text")){
				Node item = itemNode.getChildNodes().item(0);
				NamedNodeMap attrMap = itemNode.getAttributes();
				Node type = null;
				if(attrMap != null){
					type = attrMap.getNamedItem(NODE_ATTR_TYPE);									
				}
				
				String tempType = (type  != null) ? type .getNodeValue() : "";
				String nodeVal = null;
				if(item != null && item.getNodeValue() != null){
					nodeVal = item.getNodeValue();

					String tempVal = tempType.concat(":").concat(nodeVal);
					if(nodeValue == null){
						nodeValue = tempVal.concat("|");
					}else{
						nodeValue = nodeValue.concat(tempVal).concat("|");
					}
				}
				
				//log.info("nodeValue".concat(":").concat(nodeValue));
			}
		}
		return nodeValue;
	}
	
	private static String xmlNameNodeToString(NodeList childList) {
		String nodeValue = null;
		for (int k = 0; k < childList.getLength(); k++) {
			Node nameNode = childList.item(k);
			String nameNodeName = nameNode.getNodeName();
			if (nameNodeName.equals(NODE_FN) || nameNodeName.equals(NODE_LN)) {
				String namePart = nameNode.getChildNodes().item(0)
						.getNodeValue()
						+ ";";
				if (nodeValue == null) {
					nodeValue = namePart;
				} else {
					nodeValue = nodeValue.concat(namePart);
				}
			}
		}
		return nodeValue;
	}
	
	private static String getTypeVal(Element element) {
		String type = element.getAttribute("type");
		Node n = element.getFirstChild();
		String value = n.getNodeValue();
		
		type = type == null ? "" : type;
		value = value == null ? "" : value;
		
		String typeVal = type.concat(":").concat(value);
		return typeVal;
	}


	private static SingSpacesContactEntity secondLayerSelectorResult(long userId,
			String secondLayerSelector) {
		SingSpacesContactEntity selectorRecord = null;
		//selector:
		//by tag name
		if(secondLayerSelector.equals(NODE_CONTACT)){
			long size = contactsDao.getListSize(userId);
			if(size == 1){
				selectorRecord = contactsDao.getByIndex(userId, 1);
			}
		}else if(secondLayerSelector.matches(SingConstant.PATTERN_CONTACT_INDEX)){
			//by index
			int index = SingConstant.getIndex(secondLayerSelector);
			long size = contactsDao.getListSize(userId);
			if(index >= 1 && index <= size){
				selectorRecord = contactsDao.getByIndex(userId, index);
			}
			
		}else if(secondLayerSelector.matches(SingConstant.PATTERN_CONTACT_UNIQUE_ATTR)){
			//by unique attr(contact id.)
			long attr = SingConstant.getUniqueAttrValue(secondLayerSelector);
			selectorRecord = contactsDao.getByUniqueAttr(userId, attr);
		}
		return selectorRecord;
	}
	
}
