package com.xcap.ejb;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.LocalBinding;

import com.arjuna.ats.internal.arjuna.objectstore.jdbc.accessors.apache_accessor;
import com.mysql.jdbc.log.Log;
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
		
	final static String NODE_NAME = "name";
	final static String NODE_ADR = "adr";
	final static String NODE_TEL = "tel";
	final static String NODE_EMAIL = "email";
	final static String NODE_ORG = "org";
	
	final static String NODE_FN = "fn";
	final static String NODE_LN = "ln";	
	final static String NODE_ITEM = "item";
	
	final static String NODE_DISPNAME = "dispName";
	final static String NODE_BDAY = "bday";
	final static String NODE_TITLE = "title";
	final static String NODE_NOTE = "note";
	final static String NODE_URL = "url";
	final static String NODE_LASTMODIFY = "lastModify";
	
	/**
	 * contact unique attribute is id.
	 */
	final static String NODE_ATTR_ID = "id"; 
	final static String NODE_ATTR_Type = "type";

	@PersistenceContext(unitName="SingSpacesXCAP")
	EntityManager em;	

	private SingSpacesContactsDao contactsDao;
	
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

	
	final static MessageFormat SIMPLE_NODE_FORMAT = new MessageFormat("<{0}>{1}</{0}>");
	final static MessageFormat NAME_NODE_FORMAT = new MessageFormat("<name><fn>{0}</fn><ln>{1}</ln></name>");
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
			
			builder.append(SIMPLE_NODE_FORMAT.format(new Object[]{NODE_DISPNAME, fn}))
			.append(NAME_NODE_FORMAT.format(new Object[]{namePartArr[0], namePartArr[1]}))
			.append(SIMPLE_NODE_FORMAT.format(new Object[]{NODE_BDAY, bday}))
			.append(constructItemNode(NODE_ADR, adrInfo))
			.append(constructItemNode(NODE_TEL, telInfo))
			.append(constructItemNode(NODE_EMAIL, emailInfo))
			.append(SIMPLE_NODE_FORMAT.format(new Object[]{NODE_TITLE,title}))
			.append(constructItemNode(NODE_ORG, orgInfo ))    /*org*/
			.append(SIMPLE_NODE_FORMAT.format(new Object[]{NODE_NOTE, note}))
			.append(constructItemNode(NODE_URL, urlInfo))
			.append(SIMPLE_NODE_FORMAT.format(new Object[]{NODE_LASTMODIFY,dateFormat.format(last)}));
			
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
					result.append("<".concat(NODE_ATTR_Type).concat("=\"").concat(name).concat("\">"));
					result.append(value);
					result.append("<".concat(NODE_ATTR_Type).concat(">"));
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
