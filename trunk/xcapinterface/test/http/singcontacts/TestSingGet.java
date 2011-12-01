package http.singcontacts;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestSingGet extends TestBase{
	public static Logger log = Logger.getLogger(TestSingGet.class);	
	
	final static String phoneNo = "8613480783139"; 
	final static String token = "DDcs3x7JwQQwqvOT751dhxPUTartlMV70DRk28fiJjRSwAeSqwV0bw**";

	final static String url = constructUrl(phoneNo, token);
	
	@Before 
	public void runBeforeTest(){
	} 
	
	/**
	 * get contact list
	 */
	@Test
	public void getDocument(){
		getReqClient(url);
	}
	
	/**
	 * get contact list.
	 */
	@Test
	public void getDocumentByTagName(){
		String u = url.concat("/~~/contacts");
		getReqClient(u);
	}
	
	/**
	 * get contact by tag name.
	 */
	@Test
	public void getContactNodebyTagName(){
		String sel = "/~~/contacts/contact";
		String u = url.concat(sel);
		getReqClient(u);
	}
	
	/**
	 * get contact by index.
	 */
	@Test
	public void getContactNodebyIndex(){
		String sel = constructSecondLayerSelectorByIndex(3);
		String u = url.concat(sel);
		getReqClient(u);		
	}

	/**
	 * get contact by unique attr.
	 */
	@Test
	public void getContactNodebyUniqueAttr(){
		long contactId = 137;
		String sel = constructSecondLayerSelectorByUniqueAttr(String.valueOf(contactId));
		String u = url.concat(sel);
		getReqClient(u);
	}
	
	/**
	 * get name node.
	 * second layer is unique attribute selector.
	 * third layer is tag name selector/index selector.
	 */
	@Test
	public void getNameBy_a_i(){
		long contactId = 73950;  //return 404
		contactId = 137;
		String sel = constructSecondLayerSelectorByUniqueAttr(String.valueOf(contactId));
		//String u = url.concat(sel).concat("/name");
		String u = url.concat(sel).concat("/name").concat(LEFT_SQUARE_BRACKET).concat("1").concat(RIGHT_SQUARE_BRACKET);
		getReqClient(u);
	}
	
	/**
	 * get dispName node.
	 * 2nd layer  attr sel
	 * 3rd layer tag name sel
	 */
	@Test
	public void getDispNameBy_a_t(){
		long contactId = 73950;  //return 404
		contactId = 137;
		String sel = constructSecondLayerSelectorByUniqueAttr(String.valueOf(contactId)).concat("/dispName");
		getReqClient(url.concat(sel));
	}

	/**
	 * get email node.
	 * 2nd layer :  attr
	 * 3rd layer :  tag name.
	 */
	@Test
	public void getEmalBy_a_t(){
		long contactId = 73998;  //return 404
		contactId = 137;
		String sel = constructSecondLayerSelectorByUniqueAttr(String.valueOf(contactId)).concat("/email");

		String itemByIndex = LEFT_SQUARE_BRACKET.concat("1").concat(RIGHT_SQUARE_BRACKET);
		sel = constructSecondLayerSelectorByUniqueAttr(String.valueOf(contactId)).concat("/email").concat(itemByIndex);
		getReqClient(url.concat(sel));
	}

	/**
	 * get org node.
	 * 2nd layer :  attr
	 * 3rd layer :  tag name.
	 */
	@Test
	public void getOrgBy_a_t(){
		long contactId = 73998;  //return 404
		contactId = 137;
		String sel = constructSecondLayerSelectorByUniqueAttr(String.valueOf(contactId)).concat("/org");
		getReqClient(url.concat(sel));
	}
	
	/**
	 * get email item node.
	 * 2nd layer  index selector
	 * 3rd layer tag name selector
	 * 4th layer index selector
	 */	
	@Test
	public void getEmailItemBy_i_t_i(){
		String byIndex = LEFT_SQUARE_BRACKET.concat("1").concat(RIGHT_SQUARE_BRACKET);
		String itemByIndex = "/item".concat(LEFT_SQUARE_BRACKET).concat("4").concat(RIGHT_SQUARE_BRACKET);
		String reqUrl = url.concat("/~~/contacts/contact" + byIndex + "/email").concat(itemByIndex);
		//reqUrl = url.concat("/~~/contacts/contact" + byIndex + "/email");
		try {
			getReqClient(reqUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * get email item node.
	 * 2nd layer  index selector
	 * 3rd layer tag name selector
	 * 4th layer unique attr
	 */	
	@Test
	public void getEmailItemBy_i_t_a(){
		String byIndex = LEFT_SQUARE_BRACKET.concat("1").concat(RIGHT_SQUARE_BRACKET);
		String attr = "";
		attr = "WORK";
		String byItemAttr = "/item".concat(LEFT_SQUARE_BRACKET).concat(AT).concat("type=").concat(DOUBLE_QUOTATION_MARKS).concat(attr).concat(DOUBLE_QUOTATION_MARKS).concat(RIGHT_SQUARE_BRACKET);
		String reqUrl = url.concat("/~~/contacts/contact" + byIndex + "/email").concat(byItemAttr);
		///reqUrl = url.concat("/~~/contacts/contact" + byIndex + "/email");
		try {
			getReqClient(reqUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * get name/fn
	 */
	@Test
	public void getNameFn(){
		String byIndex = LEFT_SQUARE_BRACKET.concat("1").concat(RIGHT_SQUARE_BRACKET);		
		String reqUrl = url.concat("/~~/contacts/contact" + byIndex + "/name/fn");
		try {
			getReqClient(reqUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test 
	public void getNameFnByIndex(){
		String contactByIndex = LEFT_SQUARE_BRACKET.concat("1").concat(RIGHT_SQUARE_BRACKET);
		String fnByIndex = LEFT_SQUARE_BRACKET.concat("1").concat(RIGHT_SQUARE_BRACKET);
		String reqUrl = url.concat("/~~/contacts/contact" + contactByIndex + "/name/fn").concat(fnByIndex);

		try {
			getReqClient(reqUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	/**
	 * get org item node.
	 * 2nd layer  index selector
	 * 3rd layer tag name selector
	 * 4th layer index selector
	 */	
	@Test
	public void getOrgItemBy_i_t_i(){
		String byIndex = LEFT_SQUARE_BRACKET.concat("2").concat(RIGHT_SQUARE_BRACKET);
		String itemByIndex = "/item".concat(LEFT_SQUARE_BRACKET).concat("1").concat(RIGHT_SQUARE_BRACKET);
		String reqUrl = url.concat("/~~/contacts/contact" + byIndex + "/org").concat(itemByIndex);
		//reqUrl = url.concat("/~~/contacts/contact" + byIndex);
		try {
			getReqClient(reqUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * get org item node.
	 * 2nd layer  index selector
	 * 3rd layer tag name selector
	 * 4th layer tag name selector
	 */	
	@Test
	public void getOrgItemBy_i_t_t(){
		String byIndex = LEFT_SQUARE_BRACKET.concat("1").concat(RIGHT_SQUARE_BRACKET);

		String reqUrl = url.concat("/~~/contacts/contact" + byIndex + "/org/item");
		try {
			getReqClient(reqUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * get org item node.
	 * 2nd layer  index selector
	 * 3rd layer tag name selector
	 * 4th layer unique attr selector
	 */	
	@Test
	public void getOrgItemBy_i_t_a(){
		String byIndex = LEFT_SQUARE_BRACKET.concat("2").concat(RIGHT_SQUARE_BRACKET);
		
		//item[@type="a"]
		String attr = "";
		String byItemAttr = "/item".concat(LEFT_SQUARE_BRACKET).concat(AT).concat("type=").concat(DOUBLE_QUOTATION_MARKS).concat(DOUBLE_QUOTATION_MARKS).concat(attr).concat(RIGHT_SQUARE_BRACKET);
		String reqUrl = url.concat("/~~/contacts/contact" + byIndex + "/org").concat(byItemAttr);
		//reqUrl = url.concat("/~~/contacts/contact" + byIndex + "/org");
		try {
			getReqClient(reqUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * <li>CELL:+8613910193672|:+861085205599|WORK:+861085205588|WORK:+861085205205|
	 * <li>:http://www.google.com.hk/|:http://www.google.com.hk/|
	 * <li>:http://www.utstar.com|WORK:http://www.utstar.com|
	 * <li>first split by vertical line.
	 * <li>second split by coln(:)
	 * @param adr
	 * @return
	 */
	@Ignore
	@Test
	public void splitByVerticalLineAndColon() {
		String str = "CELL:+8613910193672|:+861085205599|WORK:+861085205588|WORK:+861085205205|";
		//String adr = ":http://www.google.com.hk/|:http://www.google.com.hk/|";
		//String adr = ":http://www.utstar.com|WORK:http://www.utstar.com|";
		//String adr = ":borqs shenzhen||:huangjiayin yangxian||";
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
			//adrInfo;
			
			System.out.println(vcardInfo);
		}
	}	
}
