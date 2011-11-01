package com.xcap.ejb;

import java.text.MessageFormat;
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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.LocalBinding;

import com.xcap.dao.SingSpacesContactsDao;
import com.xcap.dao.entity.SingSpacesContactEntity;
import com.xcap.ifc.XCAPDatebaseLocalIfc;

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
	public static final Logger log = Logger.getLogger(UABContactsAppEjb.class);

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
		if(nodeSelector == null || nodeSelector.equals(NODE_CONTACTS)){
			int status = 1;
			StringBuilder builder = new StringBuilder();
			builder.append("<contacts xmlns=\"UABContacts\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"contacts site/UABContacts.xsd\">");

			List<SingSpacesContactEntity> list = contactsDao.getList(userIdTemp, status);
			boolean re = getContacts(list, builder);
			builder.append("</contacts>");
			if(re){
				return new ResultData(ResultData.STATUS_200, builder.toString());
			}
		}else if(nodeSelector != null ){
			String[] nodePartArr = nodeSelector.split("/");
			if(nodePartArr.length > 1 && nodePartArr[0].equals(NODE_CONTACTS)){
				String secondSelector =  nodePartArr[1];
				if(nodePartArr.length == 2 && nodePartArr[1].contains(NODE_CONTACT)){  //second layer selector.
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
							xml = constructItemNode(thridSelector,part);
						} else if (entityFieldName.equals(NODE_NAME)){							
							String[] namePartArr = fieldValue.split(";");
							xml = SelectorPattern.NAME_NODE_FORMAT.format(new Object[]{namePartArr[0], namePartArr[1]});
						}
						if(xml != null){
							return new ResultData(ResultData.STATUS_200, xml);							
						}
					}					
					
				}else if(nodePartArr.length == 4){ //fourth layer selector.
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
							
							 if(fourthSelector.equals(NODE_ITEM)){
									
							}else if (fourthSelector.matches(SelectorPattern.PATTERN_FOURTH_SELECTOR_INDEX)){
								
							}else if(fourthSelector.matches(SelectorPattern.PATTERN_FOURTH_SELECTOR_UNIQUE_ATTR)){
								
							}else if(fourthSelector.equals(NODE_FN)){
								
							}else if(fourthSelector.equals(NODE_LN)){
								
							}else {
								
							}
						}
						//NODE_ITEM or NODE_FN  NODE_LN.
					}
				}
				
			}
		}
		return new ResultData(ResultData.STATUS_404, ""); 
	}
	
	@Override
	public ResultData put(String userId, String nodeSelector, String xml) {
		return null;
	}

	@Override
	public ResultData delete(String userId, String nodeSelector) {
		return null;
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
			return contactsDao.getByIndex(userIdTemp, 0);
		} else if (secondSelector
				.matches(SelectorPattern.PATTERN_CONTACT_INDEX)) {
			// by index
			Pattern p = Pattern.compile("\\d+");
			Matcher match = p.matcher(secondSelector);
			if (match.find()) {
				int index = Integer.valueOf(match.group(0));
				if (index >= 1) {
					return contactsDao.getByIndex(userIdTemp, index - 1);
				}

			} else if (secondSelector
					.matches(SelectorPattern.PATTERN_CONTACT_UNIQUE_ATTR)) {
				// by unique attr
				int beginIndex = secondSelector.indexOf("=\"");
				int endIndex = secondSelector.indexOf("\"]");

				if (beginIndex != -1 && endIndex != -1 && beginIndex < endIndex) {
					String idTemp = secondSelector.substring(beginIndex + 2,
							endIndex);

					return contactsDao.getByUniqueAttr(userIdTemp,
							Integer.valueOf(idTemp));
				}
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
			
			builder.append(SelectorPattern.SIMPLE_NODE_FORMAT.format(new Object[]{NODE_DISPNAME, fn}))
			.append(SelectorPattern.NAME_NODE_FORMAT.format(new Object[]{namePartArr[0], namePartArr[1]}))
			.append(SelectorPattern.SIMPLE_NODE_FORMAT.format(new Object[]{NODE_BDAY, bday}))
			.append(constructItemNode(NODE_ADR, adrInfo))
			.append(constructItemNode(NODE_TEL, telInfo))
			.append(constructItemNode(NODE_EMAIL, emailInfo))
			.append(SelectorPattern.SIMPLE_NODE_FORMAT.format(new Object[]{NODE_TITLE,title}))
			.append(constructItemNode(NODE_ORG, orgInfo ))    /*org*/
			.append(SelectorPattern.SIMPLE_NODE_FORMAT.format(new Object[]{NODE_NOTE, note}))
			.append(constructItemNode(NODE_URL, urlInfo))
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
	 * @return
	 */
	public static String constructItemNode(String parentNode, String[][] itemInfo){
		if(itemInfo != null){
			boolean hasFlag = true;
			String head = "<".concat(parentNode).concat(">");
			StringBuilder result = new StringBuilder(head);
			for(int i = 0; i < itemInfo.length; i ++){
				String[] nameValuePair = itemInfo[i];
				if(nameValuePair != null && nameValuePair.length == 2){
					hasFlag = false;
					String name = nameValuePair[0];
					String value = nameValuePair[1];
					result.append("<".concat(NODE_ATTR_TYPE).concat("=\"").concat(name).concat("\">"));
					result.append(value);
					result.append("<".concat(NODE_ATTR_TYPE).concat(">"));
				}
			}
			result.append("<".concat(parentNode).concat(">"));
			
			if(! hasFlag){
				return result.toString();
			}
		}
		return null;
	}
}
