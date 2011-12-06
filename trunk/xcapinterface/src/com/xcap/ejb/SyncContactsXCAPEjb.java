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

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.borqs.synccontact.ifc.Contact;
import com.borqs.synccontact.ifc.ContactIfc;
import com.borqs.synccontact.ifc.spectype.AddressInVCard;
import com.borqs.synccontact.ifc.spectype.EmailInVCard;
import com.borqs.synccontact.ifc.spectype.NameInVCard;
import com.borqs.synccontact.ifc.spectype.TelInVCard;
import com.borqs.synccontact.ifc.spectype.UrlInVCard;
import com.xcap.ifc.XCAPDatebaseLocalIfc;
import com.xcap.ifc.error.XCAPErrors;

/**
 * <li>author slieer</li>
 * <li>Create Date 2011-10-29</li>
 * <li>version 1.0</li>
 */
@Stateless
@Local(value = XCAPDatebaseLocalIfc.class)
public class SyncContactsXCAPEjb implements XCAPDatebaseLocalIfc{
	public static final Logger log = Logger.getLogger(SyncContactsXCAPEjb.class);

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
	
	@EJB
	private ContactIfc contactIfc;	
	
	@Override
	@TransactionAttribute(value=TransactionAttributeType.NEVER)	
	public ResultData get(String userId, String nodeSelector) throws Exception{
		long userIdTemp = Long.valueOf(userId);
		if(SingConstant.isDocSelector(nodeSelector)){
			log.info("get singspaces document by userId, userId=" + userId);
			
			StringBuilder builder = new StringBuilder();
			builder.append("<contacts xmlns=\"contacts\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"")
			.append(" xsi:schemaLocation=\"contacts xcap-schema/SingSpacesContacts\">");

			List<Contact> list = contactIfc.getAllContact(userIdTemp);
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
					//log.info("second layer selector proccess .....");
					Contact contact = getContact(secondSelector, userIdTemp);
					if(contact != null){
						StringBuilder builder = new StringBuilder();
						if(getContact(contact, builder)){
							return new ResultData(ResultData.STATUS_200, builder.toString());
						}
					}
				}else if(nodePartArr.length == 3){ //third layer selector.
					
					String thridSelector =  nodePartArr[2];
					Contact contact = getContact(secondSelector, userIdTemp);
					if(contact == null){
						return new ResultData(ResultData.STATUS_404, "");	
					}
					if(thridSelector.matches(SingConstant.PATTERN_THIRD_LAYER_SELECTOR_BY_INDEX)){
						//enable thridSelector as node tag name. 
						int end = thridSelector.indexOf("[");
						thridSelector = thridSelector.substring(0, end);
					}
					log.info("get ,thridSelector:" + thridSelector);
					String contactFieldName = SingConstant.titleFieldMapping.get(thridSelector);
					log.info("contact.toString:" + contact);
					if(contactFieldName != null && contact != null){
						Object fieldValue = null; 
						try {
							fieldValue = PropertyUtils.getProperty(contact, contactFieldName);
							log.info("contactFieldName:" + contactFieldName + ",fieldValue:" + fieldValue);
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
							String[][] part = null;
							if(thridSelector.equals(NODE_ORG)){
								part = splitByVerticalLineAndColon((String)fieldValue);
							}else{
								part = splitByVerticalLineAndColon((List)fieldValue);
							}
							xml = constructItemsNode(thridSelector,part);
						} else if (thridSelector.equals(NODE_NAME)){
							log.info("get name node...");
							NameInVCard vCard = (NameInVCard)fieldValue;
							String fn = vCard.getFamilyName();
							String ln = vCard.getGivenName();
							xml = SingConstant.NAME_NODE_FORMAT.format(new Object[]{fn, ln});
						}
						if(xml != null){
							return new ResultData(ResultData.STATUS_200, xml);							
						}
					}					
				}else if(nodePartArr.length == 4){ //fourth layer selector.  //NODE_ITEM or NODE_FN  NODE_LN.					
					String thridSelector =  nodePartArr[2];
					String fourthSelector =  nodePartArr[3];
					log.info("3rd layer selector,4th layer Selector:" + thridSelector + ","+ fourthSelector);
					
					if(thridSelector.matches(SingConstant.PATTERN_THIRD_LAYER_SELECTOR_BY_INDEX)){
						int end = thridSelector.indexOf("[");
						log.info("thridSelector  by index:" + thridSelector);
						thridSelector = thridSelector.substring(0, end);
					}
					
					if(SingConstant.isThirdLayerNotLeafNode(thridSelector) || thridSelector.equals(NODE_NAME)){
						Contact contact = getContact(secondSelector, userIdTemp);
						
						String contactFieldName = SingConstant.titleFieldMapping.get(thridSelector);
						if(contactFieldName != null && contact != null){
							Object fieldValue = null;
							try {
								fieldValue = PropertyUtils.getProperty(contact, contactFieldName);
							} catch (Exception e) {
								log.error("PropertyUtils.getProperty error");
								e.printStackTrace();
							} 	
							String[][] valueArray = null;
							if(fieldValue instanceof List){
								valueArray = splitByVerticalLineAndColon((List)fieldValue);
							}else{
								//org
								valueArray = splitByVerticalLineAndColon((String)fieldValue);
								log.info("org value is: " + fieldValue);
							}
							if(fourthSelector.equals(NODE_ITEM)){
								log.info("get item, 4th layer tag name Selector");
								if(valueArray != null && valueArray.length == 1){
									StringBuilder result = new StringBuilder();
									constructItemNode(result,valueArray[0]); 
									return new ResultData(ResultData.STATUS_200, result.toString());
								}
							}else if (fourthSelector.matches(SingConstant.PATTERN_FOURTH_ITEM_SELECTOR_INDEX)){
								log.info("get item, 4th layer index selector.");
								if(valueArray != null){
									Pattern p = Pattern.compile("\\d+");
									Matcher match = p.matcher(fourthSelector);
									if(match.find()){
										String indexTemp = match.group(0);
										int index = Integer.valueOf(indexTemp);
										if(index >= 1 && index <= valueArray.length){
											StringBuilder result = new StringBuilder();
											log.info("index value:" + (index -1) + ", value:" + (valueArray[index -1]));
											constructItemNode(result,valueArray[index -1]); 
											return new ResultData(ResultData.STATUS_200, result.toString());											
										}
 									}
								}
							}else if(fourthSelector.matches(SingConstant.PATTERN_FOURTH_ITEM_SELECTOR_UNIQUE_ATTR)){
								if(valueArray != null){
									String[] attrNameValue = is4thLayerAttrSelectorUnique(
											fourthSelector, valueArray);
									if(attrNameValue != null){
										StringBuilder xml = new StringBuilder();
										constructItemNode(xml, attrNameValue);
										return new ResultData(
												ResultData.STATUS_200,
												xml.toString());
									}
								}
							}else if(fourthSelector.equals(NODE_FN) || fourthSelector.matches(SingConstant.PATTERN_FOURTH_SELECTOR_FN_INDEX)){
								if(fieldValue != null){
									NameInVCard name = (NameInVCard)fieldValue;
									String fn = name.getFamilyName() != null ? name.getFamilyName() : "";
									return  new ResultData(ResultData.STATUS_200, "<".concat(NODE_FN).concat(">").concat(fn).concat("</" + NODE_FN+ ">"));
								}
								
							}else if(fourthSelector.equals(NODE_LN) || fourthSelector.matches(SingConstant.PATTERN_FOURTH_SELECTOR_LN_INDEX)){
								NameInVCard name = (NameInVCard)fieldValue;
								String ln = name.getGivenName() != null ? name.getGivenName() : "";
								return  new ResultData(ResultData.STATUS_200, "<".concat(NODE_LN).concat(">").concat(ln).concat("</" + NODE_LN + ">"));
							}
						}
					}
				}			
			}
		}
		return new ResultData(ResultData.STATUS_404, ""); 
	}
	
	@Override
	public ResultData put(String userId_, String nodeSelector, String xml) throws Exception{
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
			List<Contact> conatcts = xmlToEntitys(doc);
			deleteByUserId(userId);
			contactIfc.saveList(userId,conatcts);
			return new ResultData(ResultData.STATUS_200, "");
		}else{
			log.info("put node, nodeSelector:" + nodeSelector);
			String secondLayerSelector = null; 
			String thirdLayerSeletor = null;
			String fourthLayerSelector = null;
			String selectorArr[] = nodeSelector.split("/");
			if(nodeSelector != null){
				if(selectorArr.length > 0 && !selectorArr[0].equals(NODE_CONTACTS)){
					log.info("selector error.");
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
		    			long size = contactIfc.getCountForNormal(userId);
		    			log.info("get contact by tag name selector. contact size is " + size);
		    			switch ((int)size) {
						case 0:
							List<Contact> conatcts = xmlToEntitys(doc);
							contactIfc.saveList(userId,conatcts);
							return new ResultData(ResultData.STATUS_200, "");
						case 1:
							List<Contact> list = xmlToEntitys(doc);
			    			if(list.size() > 0){
			    				Contact oldContact = getByIndex(userId, 1);
			    				Contact contact = list.get(0);
			    				contact.setContactId(oldContact.getContactId());
			    				contactIfc.updateContact(userId, contact);
			    				return new ResultData(ResultData.STATUS_200, "");				    				
			    			}
							
							return new ResultData(ResultData.STATUS_200, "");
						}
		    		}else if(secondLayerSelector.matches(SingConstant.PATTERN_CONTACT_INDEX)){
		    			//by index
		    			int index = SingConstant.getIndex(secondLayerSelector);
		    			if(index >= 1){
		    				long size = contactIfc.getCountForNormal(userId);
		    				log.info("contactIfc.getCountForNormal by userId: " + size);
		    				if(index <= size){
		    					//update
		    					log.info("update contact by contact index selector, index :" + index);
				    			List<Contact> list = xmlToEntitys(doc);
				    			Contact oldContact = getByIndex(userId, index);
				    			if(list.size() > 0 && oldContact != null){
				    				log.info("update contact by index......");
				    				Contact contact = list.get(0);
				    				contact.setContactId(oldContact.getContactId());
				    				contactIfc.updateContact(userId, contact);
				    				return new ResultData(ResultData.STATUS_200, "");				    				
				    			}
		    				}else if(index == size +1){
		    					log.info("add contact by contact index selector, index :" + index);
		    					//add
				    			List<Contact> list = xmlToEntitys(doc);
				    			contactIfc.saveList(userId, list);
				    			return new ResultData(ResultData.STATUS_200, "");
		    				}
		    			}
		    			
		    		}else if(secondLayerSelector.matches(SingConstant.PATTERN_CONTACT_UNIQUE_ATTR)){
		    			//by unique attr(contact id.)
		    			log.info("put contact by unique attribute...");
		    			long attr = SingConstant.getUniqueAttrValue(secondLayerSelector);
		    			if(attr != -1){
		    				List<Contact> list = xmlToEntitys(doc);
		    				Contact oldContact = getById(userId, attr);
		    				if(oldContact != null){
		    					//update
				    			if(list.size() > 0){
				    				Contact contact = list.get(0);
				    				contact.setContactId(oldContact.getContactId());
				    				contactIfc.updateContact(userId, contact);
				    				return new ResultData(ResultData.STATUS_200, "");				    				
				    			}
		    				}else{
		    					contactIfc.saveList(userId, list);
		    				}
		    				return new ResultData(ResultData.STATUS_200, "");
		    			}
		    		}
		    		
		    	}
		    	//is xml document node tag name equals the 3rd layer selector.
		    }else if(topTagName.equals(SingConstant.getNodeNameByThirdSelector(thirdLayerSeletor))){
		    	Contact selectorRecord = secondLayerSelectorResult(
						userId, secondLayerSelector);
	    		log.info("contact:" + selectorRecord);
		    	if(selectorRecord != null){
		    		NodeList nodelist = element.getChildNodes();
		    		Object fieldValue = null;
		    		//Object fieldValueObj = null;
		    		if(SingConstant.isThirdLayerLeafNode(topTagName)){
		    			log.info("put by tag name, third layer leaf node.");
						Node node1 = nodelist.item(0);
						if(node1 != null){
							fieldValue = node1.getNodeValue();							
						}else{
							fieldValue = null;
						}
		    		}else if(SingConstant.isIncludeItemNode(topTagName)){
		    			fieldValue = xmlItemNodeToVcardObj(topTagName, nodelist);
		    		}else if(NODE_NAME.equals(topTagName)){
						fieldValue = xmlNameNodeToVcardName(nodelist);
		    		}
		    		
	    			String fieldName = SingConstant.titleFieldMapping.get(topTagName);
	    			try {
	    				BeanUtils.setProperty(selectorRecord, fieldName, fieldValue);
	    				contactIfc.updateContact(userId, selectorRecord);
	    				
	    				return new ResultData(ResultData.STATUS_200, "");
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    			}		    					    		
		    	}
		    }else if(SingConstant.isFourthLayerLeafNode(topTagName)){
		    	String tagName = SingConstant.getNodeNameByThirdSelector(thirdLayerSeletor);
		    	log.info("tag name:" + tagName);
		    	if(! SingConstant.isThirdLayerNotLeafNode(tagName)){
		    		return new ResultData(ResultData.STATUS_404, "");
		    	}
		    	String fieldName = SingConstant.titleFieldMapping.get(tagName);
		    	log.info("contact field,4th layer selector:" + fieldName + "," + fourthLayerSelector);
		    	
		    	Object fieldValueObj = null;
		    	Contact selectorRecord = secondLayerSelectorResult(userId,secondLayerSelector);
		    	if(selectorRecord != null){
			    	try {
			    		fieldValueObj = PropertyUtils.getProperty(selectorRecord, fieldName);
					} catch (Exception e) {
						e.printStackTrace();
					}
			    	
			    	if(fourthLayerSelector.equals(NODE_FN) 
			    			||fourthLayerSelector.matches(SingConstant.PATTERN_FOURTH_SELECTOR_FN_INDEX)){
			    		log.info("put first name node...");
			    		Node n = element.getFirstChild();
						String valueFromXml = n.getNodeValue();
						
						NameInVCard nameVCard = (NameInVCard)fieldValueObj;
						nameVCard.setFamilyName(valueFromXml);
			    		//int index = fieldValue.indexOf(";");
			    		//fieldValue = valueFromXml.concat(";").concat(fieldValue.substring(index));
			    	}else if(fourthLayerSelector.equals(NODE_LN) || fourthLayerSelector.matches(SingConstant.PATTERN_FOURTH_SELECTOR_LN_INDEX)){
			    		log.info("put last name node...");
			    		Node n = element.getFirstChild();
						String valueFromXml = n.getNodeValue();;
						
						NameInVCard nameVCard = (NameInVCard)fieldValueObj;
						nameVCard.setGivenName(valueFromXml);			    		
			    	}else if(fourthLayerSelector.equals(NODE_ITEM)){
			    		log.info("put item node by tag name...fieldValueObj:" + fieldValueObj);
			    		int itemSize = 0;
			    		if(fieldValueObj != null){
			    			if(tagName.equals(NODE_ORG)){
			    				String org = (String)fieldValueObj;
			    				itemSize = org.split("|").length;
			    			}else{
			    				List list = (List)fieldValueObj;
			    				itemSize = list.size();
			    			}			    			
			    		}
			    		//put item node by tag name.
			    		if(itemSize <= 1){
			    			NodeList list = doc.getChildNodes();
			    			//log.info("" + element + ","+ list.getLength()); 
			    			fieldValueObj = xmlItemNodeToVcardObj(tagName, list);
			    			log.info("put item node by tag name, fieldValueObj value:" + fieldValueObj);
			    		}else{
				    		return new ResultData(ResultData.STATUS_404, "");			    			
			    		}
			    	}else if(fourthLayerSelector.matches(SingConstant.PATTERN_FOURTH_ITEM_SELECTOR_INDEX)){			    		
			    		log.info("put item node by index...");
			    		int index = SingConstant.getIndex(fourthLayerSelector) -1;  //xcap index selector begin 1;
			    		/*
			    		 * fieldValueObjList and fieldValueObj is an object.
			    		 */
			    		List fieldValueObjList = (List)fieldValueObj;
			    		if(index >=0 && index < fieldValueObjList.size()){
			    			//modify item
			    			String[] typeVal = getTypeVal(element);
			    			log.info("modify item. type:value:" + typeVal);
			    			
			    			fieldValueObjList.remove(index);
			    			Object object = setItemVal(tagName,  fieldValueObj, typeVal);
			    			fieldValueObjList.add(index, object);

			    		}else if(index == fieldValueObjList.size()){
			    			//add item
			    			String[] typeVal = getTypeVal(element);
			    			log.info("append item. type:value:" + typeVal);
			    			Object object = setItemVal(tagName, fieldValueObj, typeVal);
			    			fieldValueObjList.add(object);
			    		}else{
			    			return new ResultData(ResultData.STATUS_409, new XCAPErrors.CannotInsertConflictException().getResponseContent());
			    		}
			    	}else if(fourthLayerSelector.matches(SingConstant.PATTERN_FOURTH_ITEM_SELECTOR_UNIQUE_ATTR)){
			    		log.info("put item node by unique attr...");
			    		String[] typeVal = getTypeVal(element);
			    		List fieldValueObjList = (List)fieldValueObj;
			    		
			    		String[][] valueArray = null;
			    		if(tagName.equals(NODE_ORG)){
			    			valueArray = splitByVerticalLineAndColon((String)fieldValueObj);			    			
			    		}else {
			    			valueArray = splitByVerticalLineAndColon((List)fieldValueObj);
			    		}
			    		String[] re = is4thLayerAttrSelectorUnique(fourthLayerSelector, valueArray);
			    		Object object = setItemVal(tagName, fieldValueObj, typeVal);
			    		if(re == null){
			    			//add
			    			fieldValueObjList.add(object);
			    		}else{
			    			//---------------------------------------------------------------------------------------
			    			//merge
			    			int beginIndex = fourthLayerSelector.indexOf("=\"");
			    			int endIndex = fourthLayerSelector.indexOf("\"]");
			    			String type = "";
			    			if (beginIndex != -1 && endIndex != -1 && beginIndex < endIndex) {
			    				type = nodeSelector.substring(beginIndex + 2, endIndex); //is attribute type
			    			}
			    			
			    			int index = -1;
			    			for (int i = 0; i < valueArray.length; i++) {
			    				String[] mapping = valueArray[i];
			    				if (mapping != null && mapping.length > 1) {
			    					String attr = mapping[0];
			    					if(type.equals(attr)){
			    						index = i;
			    						break;
			    					}
			    				}
			    			}
			    			if(index != -1)
			    				fieldValueObjList.add(index, object);
			    		}			    		
			    	}else{
			    		return new ResultData(ResultData.STATUS_404, "");
			    	}
			    	log.info("update contact.fieldName,fieldValue:" + fieldName + " " +fieldValueObj);
			    	try {
						BeanUtils.setProperty(selectorRecord, fieldName, fieldValueObj);
						contactIfc.updateContact(userId, selectorRecord);
						return new ResultData(ResultData.STATUS_200, "");
					} catch (Exception e) {
						e.printStackTrace();
					}
		    	}
		    }
		}
		return new ResultData(ResultData.STATUS_404, "");
	}

	private Object setItemVal(String tagName, Object fieldValueObj,
			String[] typeVal) {
		Object object = null;
		if(tagName.equals(NODE_ADR)){
			object = AddressInVCard.parse(typeVal[0].concat(":").concat(typeVal[1]));
		}else if(tagName.equals(NODE_TEL)){
			TelInVCard tel = new TelInVCard();
			tel.setType(typeVal[0]);
			tel.setValue(typeVal[1]);
			object = tel;
		}else if(tagName.equals(NODE_EMAIL)){
			EmailInVCard email= new EmailInVCard();
			email.setType(typeVal[0]);
			email.setValue(typeVal[1]);
			object = email;
		}else if(tagName.equals(NODE_URL)){
			UrlInVCard url = new UrlInVCard();
			url.setType(typeVal[0]);
			url.setValue(typeVal[1]);
			object = url;
		}else if(tagName.equals(NODE_ORG)){
			String tempOrg = (String)fieldValueObj;
			StringBuilder tempOrgStringBuilder = new StringBuilder(tempOrg);
			tempOrgStringBuilder.append(typeVal[0]);
			tempOrgStringBuilder.append(":");
			tempOrgStringBuilder.append(typeVal[1]);
			tempOrgStringBuilder.append("|");
			
			object = tempOrgStringBuilder.toString();
		}
		return object;
	}



	@Override
	public ResultData delete(String userId_, String nodeSelector) throws Exception {
		long userId = Long.valueOf(userId_);
		int effectRows = -1;
		if(nodeSelector == null || nodeSelector.equals(NODE_CONTACTS)){
			//delete all
			effectRows = deleteByUserId(userId);
		}else{
			String[] selParts = nodeSelector.split("/");
			if(selParts != null && selParts.length ==2 && selParts[0].equals(NODE_CONTACTS)){
				if(selParts[1].equals(NODE_CONTACT)){
					log.info("delete contact by tagName...");
					long size = contactIfc.getCountForNormal(userId);
					
					switch (Long.valueOf(size).intValue()) {
					case 0:
						return new ResultData(ResultData.STATUS_404,"");
					case 1:
						deleteContactByIndexSelector(userId, 1);
						return new ResultData(ResultData.STATUS_200, "");

					default:
						return new ResultData(ResultData.STATUS_409,
								new XCAPErrors.CannotDeleteConflictException().getResponseContent());
					}					
				}else if(selParts[1].matches(SingConstant.PATTERN_CONTACT_INDEX)){
					int index = SingConstant.getIndex(selParts[1]);
					log.info("delete contact by index ,index is " + index);
					deleteContactByIndexSelector(userId, index);
				}else if(selParts[1].matches(SingConstant.PATTERN_CONTACT_UNIQUE_ATTR)){
					long contactId = SingConstant.getUniqueAttrValue(selParts[1]);
					log.info("delete contact by uniqueAttr, (contactId, userId)-> " + contactId + "," + userId);
				    contactIfc.removeList(userId, String.valueOf(contactId));
					
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
		final static String PATTERN_FOURTH_ITEM_SELECTOR_UNIQUE_ATTR = "^item\\[@".concat(NODE_ATTR_TYPE).concat("=\"\\S*\"\\]$");
		
		final static String PATTERN_FOURTH_SELECTOR_FN_INDEX = "^fn\\[1\\]$";
		final static String PATTERN_FOURTH_SELECTOR_LN_INDEX = "^ln\\[1\\]$";
		//final static String PATTERN_THIRD_SELECTOR_INDEX = "^\\w+\\[1\\]?+$";
		
		final static MessageFormat SIMPLE_NODE_FORMAT = new MessageFormat("<{0}>{1}</{0}>");
		final static MessageFormat NAME_NODE_FORMAT = new MessageFormat("<name><fn>{0}</fn><ln>{1}</ln></name>");
		
		/**
		 * xml node tag name--> contact bean field. 
		 */
		final static Map<String,String> titleFieldMapping = new HashMap<String, String>(32);
		
		/**	
	     long contactId;
		 String formattedName;
		 String nickName;
	
		 String birthday;
		 String title;
		 String org;
		 String note;
		 String uid;
		 Date lastModified;
	
		 NameInVCard name;
		 List<AddressInVCard> address;
		 List<TelInVCard> tels;
		 List<EmailInVCard> emails;
		 List<UrlInVCard> urls;	
		 */
		static {
			titleFieldMapping.put(NODE_NAME, "name");
			titleFieldMapping.put(NODE_DISPNAME,"formattedName");
			
			titleFieldMapping.put(NODE_BDAY, "birthday");
			titleFieldMapping.put(NODE_ADR, "address");

			titleFieldMapping.put(NODE_TEL, "tels");
			titleFieldMapping.put(NODE_EMAIL,"emails");
			
			titleFieldMapping.put(NODE_TITLE, "title");
			titleFieldMapping.put(NODE_ORG, "org");
			
			titleFieldMapping.put(NODE_NOTE, "note");
			titleFieldMapping.put(NODE_LASTMODIFY, "lastModified");
			
			titleFieldMapping.put(NODE_URL, "urls");
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
	
	private Contact getContact(String secondSelector,long userIdTemp) throws Exception{
		if (secondSelector.equals(NODE_CONTACT)) {
			// by tag name
			long count = contactIfc.getCountForNormal(userIdTemp);
			log.info("get contact by tagName, record count is " + count);
			if(count == 1){
				return getByIndex(userIdTemp, 1);				
			}
		} else if (secondSelector
				.matches(SingConstant.PATTERN_CONTACT_INDEX)) {
			// by index
			Pattern p = Pattern.compile("\\d+");
			Matcher match = p.matcher(secondSelector);
			if (match.find()) {
				int index = Integer.valueOf(match.group(0));
				if (index >= 1) {
					log.info("get contact by index, index:" + (index -1));
					return getByIndex(userIdTemp, index -1);
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

				return getById(userIdTemp, Long.valueOf(idTemp));
			}
		}
		return null;
	}	
	
	/**
	 * 
	 * @param contact
	 * @param builder
	 * @return false builder not append string.
	 */
	private static boolean getContact(Contact contact, StringBuilder builder){
		if(contact != null){
			log.info("contact[" + contact + "]");
			String id = String.valueOf(contact.getContactId());
			builder.append("<".concat(NODE_CONTACT).concat(" ").concat(NODE_ATTR_ID).concat("=\"").concat(id).concat("\">"));

			String title = contact.getTitle();
			String bday = contact.getBirthday();
			String fn = contact.getFormattedName();  
			String note = contact.getNote();
			Date last = contact.getLastModified();
			
			NameInVCard name = contact.getName();   //ContactN
			String org = contact.getOrg();		 
			List<AddressInVCard>  adrs = contact.getAddress();  //
			List<TelInVCard> tels = contact.getTels();  //
			List<EmailInVCard> email = contact.getEmails(); //
			List<UrlInVCard> urls = contact.getUrls();
			
			String[][] adrInfo = splitByVerticalLineAndColon(adrs);
			String[][] telInfo = splitByVerticalLineAndColon(tels);
			String[][] emailInfo = splitByVerticalLineAndColon(email);
			String[][] orgInfo = splitByVerticalLineAndColon(org);
			String[][] urlInfo = splitByVerticalLineAndColon(urls);
			
			builder.append(SingConstant.SIMPLE_NODE_FORMAT.format(new Object[]{NODE_DISPNAME, fn == null ? "" : fn}))
			.append(SingConstant.NAME_NODE_FORMAT.format(new Object[]{name.getFamilyName(),name.getGivenName()}))
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
	
	private boolean getContacts(List<Contact> list,StringBuilder builder){
		if(list != null && list.size() > 0){
			for(Contact en :list){
				getContact(en, builder);
			}
			return true;
		}
		return false;
	}

	/**	
	 * @param obj: 
		<li>List {@link #AddressInVCard}
		<li>List {@link #TelInVCard}
		<li>List {@link #EmailInVCard}
		<li>List {@link #UrlInVCard}
		not support List LabelInVCard
	 * @return
	 */
	private static String[][] splitByVerticalLineAndColon(List objList) {
		//log.info("splitByVerticalLineAndColon:" + objList);
		if(objList != null){
				String[][] vcardInfo = new String[objList.size()][];
				for(int i = 0; i < objList.size(); i++){
					Object object = objList.get(i);
					if(object instanceof AddressInVCard){
						AddressInVCard adr = (AddressInVCard)object;
						vcardInfo[i] = new String[]{adr.getType(),adr.getValue()};
					}else if(object instanceof TelInVCard){
						TelInVCard tel = (TelInVCard)object;
						vcardInfo[i] = new String[]{tel.getType(),tel.getValue()};
					}else if(object instanceof EmailInVCard){
						EmailInVCard email = (EmailInVCard)object;
						vcardInfo[i] = new String[]{email.getType(),email.getValue()};
					}else if(object instanceof UrlInVCard){
						UrlInVCard url = (UrlInVCard)object;
						vcardInfo[i] = new String[]{url.getType(),url.getValue()};
					}
				}
				return vcardInfo;
		}
		return null;
	}
	
	private static String[][] splitByVerticalLineAndColon(String str){
		String[][] vcardInfo = null;
		if(str != null){
			String[] adrArr = str.split("\\|");
			vcardInfo = new String[adrArr.length][];
			
			int j = 0;
			for(int i = 0; i < adrArr.length; i++){
				String temp = adrArr[i];
				int splitIndex = temp.indexOf(":");
				if(splitIndex != -1){
					vcardInfo[j] = new String[2];
					if(splitIndex == 0){
						vcardInfo[j][0] = "";
					}else{
						vcardInfo[j][0] = temp.substring(0,splitIndex);						
					}
					vcardInfo[j][1] = temp.substring(splitIndex + 1, temp.length());
					j++;
				}
			}
		}
		return vcardInfo;
	}
	
	/**
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
		if(nameValuePair != null){
			String name = nameValuePair[0];
			String value = nameValuePair[1];
			if(name != null){
				result.append("<".concat(NODE_ITEM).concat(" ").concat(NODE_ATTR_TYPE).concat("=\"").concat(name).concat("\">"));			
			}else{
				result.append("<".concat(NODE_ITEM).concat(">"));
			}
			result.append(value);
			result.append("</".concat(NODE_ITEM).concat(">"));			
		}
	}
			
	private static List<Contact> xmlToEntitys(Document conatcts){
		List<Contact> list = new ArrayList<Contact>();
		NodeList nodes = conatcts.getElementsByTagName(NODE_CONTACT);
		log.info("xml document contact node amount is " + nodes.getLength());
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			NodeList children = node.getChildNodes();
			
			Contact conatct = new Contact();
			conatct.setLastModified(new Date());
			list.add(conatct);
			
			for(int j = 0; j < children.getLength(); j++){
				Object nodeValue = null;
				String nodeName = null;
				Node ch = children.item(j);
				nodeName = ch.getNodeName();
				
				NodeList childList = ch.getChildNodes();
				if(! nodeName.equals("#text") && childList.getLength() != 0){
					
					if(SingConstant.isThirdLayerLeafNode(nodeName)){
						//log.info("-----------leaf node----");
						Node node1 = childList.item(0);
						nodeValue = node1.getNodeValue();
												
					}else if(SingConstant.isIncludeItemNode(nodeName)){
						//log.info("-----------include item node----");
						nodeValue = xmlItemNodeToVcardObj(nodeName, childList);
					}else if(nodeName.equals(NODE_NAME)){
						nodeValue = xmlNameNodeToVcardName(childList);
					}	
					
					if(nodeName.equals(NODE_ORG)){  //org node need myself proccess.
						List<String> tempList = (List<String>)nodeValue;
						String temp = "";
						for(int k = 0; tempList != null && k < tempList.size(); k++){
							temp = temp.concat(tempList.get(k)).concat("|");
						}
						if(!temp.equals("")){
							nodeValue = temp;
						}
					}
					String fieldName = SingConstant.titleFieldMapping.get(nodeName);
					//log.info("------------" + fieldName + ":" + nodeName + "=" + nodeValue);
					try {
						BeanUtils.setProperty(conatct, fieldName, nodeValue);
					} catch (Exception e) {
						log.info("bean set property Exception");
						e.printStackTrace();
					}
				}
			}

			log.info(conatct);
		}
		return list;
	}
	
	private static Object xmlItemNodeToVcardObj(String itemParentTag, NodeList childList){
		
		List<Object> list = new ArrayList<Object>();
		for(int k = 0; k < childList.getLength(); k++){
			
			Node itemNode = childList.item(k);
			//log.info("itemNode.getNodeName():" + itemNode.getNodeName());
			if(! itemNode.getNodeName().equals("#text")){
				Node item = itemNode.getChildNodes().item(0);
				NamedNodeMap attrMap = itemNode.getAttributes();
				Node type = null;
				if(attrMap != null){
					type = attrMap.getNamedItem(NODE_ATTR_TYPE);									
				}
				//log.info("type:" + type);
				String tempType = (type  != null) ? type .getNodeValue() : "";
				String nodeVal = null;
				if(item != null && item.getNodeValue() != null){
					nodeVal = item.getNodeValue();
					log.info("type,nodeValue:" + type + "," + nodeVal);
					Object obj = null;
					if(itemParentTag.equals(NODE_ADR)){
						obj = AddressInVCard.parse(tempType.concat(":").concat(nodeVal));
					}else if(itemParentTag.equals(NODE_EMAIL)){
						EmailInVCard emailObj = new EmailInVCard();
						emailObj.setType(tempType);
						emailObj.setValue(nodeVal);
						obj = emailObj;
					}else if(itemParentTag.equals(NODE_ORG)){
						obj = tempType.concat(":").concat(nodeVal).concat("|");
					}else if(itemParentTag.equals(NODE_TEL)){
						TelInVCard telObj = new TelInVCard();
						telObj.setType(tempType);
						telObj.setValue(nodeVal);
						obj = telObj;
					}else if(itemParentTag.equals(NODE_URL)){
						UrlInVCard urlObj = new UrlInVCard();
						urlObj.setType(tempType);
						urlObj.setValue(nodeVal);
						obj = urlObj;
					}
					
					list.add(obj);
				}
				
				//log.info("nodeValue".concat(":").concat(nodeValue));
			}
		}
		return list.size() > 0 ? list : null;
	}
	
	private static Object xmlNameNodeToVcardName(NodeList childList) {
		NameInVCard name = new NameInVCard();
		for (int k = 0; k < childList.getLength(); k++) {
			Node nameNode = childList.item(k);
			String nameNodeName = nameNode.getNodeName();
			NodeList nameList = nameNode.getChildNodes();
			Node node = nameList.item(0);
			String namePart = null;
			if(node != null){
				namePart = node.getNodeValue();
			}
			if (nameNodeName.equals(NODE_FN)){
				name.setFamilyName(namePart);
			}else if(nameNodeName.equals(NODE_LN)) {
				name.setGivenName(namePart);
			}
		}
		return name;
	}
	
	/**
	 * @param element
	 * @return [type, value]
	 */
	private static String[] getTypeVal(Element element) {
		String type = element.getAttribute("type");
		Node n = element.getFirstChild();
		String value = n.getNodeValue();
		
		type = type == null ? "" : type;
		value = value == null ? "" : value;
		
		return  new String[]{type,value};
	}


	private Contact secondLayerSelectorResult(long userId,
			String secondLayerSelector) throws Exception{
		Contact selectorRecord = null;
		//selector:
		//by tag name
		if(secondLayerSelector.equals(NODE_CONTACT)){
			long size = contactIfc.getCountForNormal(userId);
			if(size == 1){
				selectorRecord = getByIndex(userId, 1);
			}
		}else if(secondLayerSelector.matches(SingConstant.PATTERN_CONTACT_INDEX)){
			//by index
			int index = SingConstant.getIndex(secondLayerSelector);
			long size = contactIfc.getCountForNormal(userId);
			if(index >= 1 && index <= size){
				selectorRecord = getByIndex(userId, index);
			}
			
		}else if(secondLayerSelector.matches(SingConstant.PATTERN_CONTACT_UNIQUE_ATTR)){
			//by unique attr(contact id.)
			long attr = SingConstant.getUniqueAttrValue(secondLayerSelector);
			selectorRecord = getById(userId, attr);
		}
		return selectorRecord;
	}
	
	/**
	 * 
	 * @param fourthSelector
	 * @param valueArray
	 * @return if 4th layer attrSelector Unique return selected type:Value , or null;
	 */
	private static String[] is4thLayerAttrSelectorUnique(String fourthSelector,
			String[][] valueArray) {
		int beginIndex = fourthSelector.indexOf("=\"");
		int endIndex = fourthSelector.indexOf("\"]");
		log.info("beginIndex, endIndex:" + beginIndex + "," + endIndex);
		
		String itemTypeVal = "";  //if beginIndex == -1 and endIndex == -1
		if (beginIndex != -1 && endIndex != -1 && beginIndex < endIndex) {
			itemTypeVal = fourthSelector.substring(beginIndex + 2, endIndex); //is attribute type
		}
		int isUnique = -1; // 0 OK, 1 not unique.
		String[] attrNameValue = null;
		//log.info("valueArray.len:" + valueArray.length);
		for (int i = 0; i < valueArray.length; i++) {
			String[] mapping = valueArray[i];
			if (mapping != null && mapping.length > 1) {
				String attr = mapping[0];
				//log.info("attr type val:" + attr);
				if (itemTypeVal.equals(attr)) {
					if (isUnique == -1) {
						isUnique = 0;
						attrNameValue = mapping;
					}else if (isUnique == 0) {
						isUnique = 1;
					}
				}
			}
		}
		return attrNameValue;
	}
	
	private Contact getById(long userId,long id) throws Exception{
		Contact contact = contactIfc.getContact(id);
		//log.info("contact userId:" + contact.getUid());   //userId is null
		if(contact != null){
			//return contact.getUid().equals(String.valueOf(userId)) ? contact : null;
			return contact;
		}
		return null;
	}
	
	/**
	 * @param userId
	 * @param index begin 1.
	 * @return
	 * @throws Exception
	 */
	private Contact getByIndex(long userId, int index) throws Exception{
		if(index > 0){
			List<Contact> list = contactIfc.getContactBySize(userId, index - 1, 1);
			if(list != null && list.size() > 0){
				return list.get(0);					
			}
		}
		return null;
	}
			
	/**
	 * @param userId
	 * @return @return sql effect row
	 * @throws Exception
	 */
	private int deleteByUserId(long userId) throws Exception{
		List<Contact> list = contactIfc.getAllContact(userId);
		StringBuilder idList = new StringBuilder();
		for(Contact c : list){
			idList.append(c.getContactId());
			idList.append(",");
		}
		if(list.size() > 0){
			String ids = idList.substring(0, idList.length() -1);
			log.info("deleteByUserId, contactId list:" + ids);
			contactIfc.removeList(userId, ids);
		}
		return list.size();
	}
	
		//(userId, String.valueOf(contactId));

	/**
	 * @param userId
	 * @param index begin 1.
	 * @return sql effect row
	 * @throws Exception
	 */
	private void deleteContactByIndexSelector(long userId, int index) throws Exception{
		Contact c = getByIndex(userId, index);
		if(c != null){
			contactIfc.removeList(userId, String.valueOf(c.getContactId()));			
		}
	}
	
}

