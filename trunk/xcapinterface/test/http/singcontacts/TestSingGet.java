package http.singcontacts;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class TestSingGet extends TestBase{
	public static Logger log = Logger.getLogger(TestSingGet.class);	
	
	final static String phoneNo = "8613480783139"; 
	final static String token = "DDcs3x7JwQQwqvOT751dhxPUTartlMV70DRk28fiJjRSwAeSqwV0bw**";

	final static String url = constructUrl(phoneNo, token);
	
	@Before 
	public void runBeforeTest(){
	} 
	
	@Test
	public void getDocument(){
		log.info("url:" + url);
		getReqClient(url);
	}

	@Test
	public void getDocumentByTagName(){
		String u = url.concat("/~~/contacts");
		getReqClient(u);
	}
	
	@Test
	public void getContactNodebyTagName(){
		String sel = "/~~/contacts/contact";
		String u = url.concat(sel);
		getReqClient(u);
	}
	
	@Test
	public void getContactNodebyIndex(){
		String sel = constructSecondLayerSelectorByIndex(10);
		String u = url.concat(sel);
		getReqClient(u);		
	}

	@Test
	public void getContactNodebyUniqueAttr(){
		long contactId = 23479;
		String sel = constructSecondLayerSelectorByUniqueAttr(String.valueOf(contactId));
		String u = url.concat(sel);
		getReqClient(u);
	}
	
	/**
	 * second layer is unique attribute selector.
	 * third layer is tag name selector/index selector.
	 */
	@Test
	public void getNameBy_a_i(){
		long contactId = 73950;
		String sel = constructSecondLayerSelectorByUniqueAttr(String.valueOf(contactId));
		//String u = url.concat(sel).concat("/name");
		String u = url.concat(sel).concat("/name").concat(LEFT_SQUARE_BRACKET).concat("1").concat(RIGHT_SQUARE_BRACKET);
		getReqClient(u);
	}
	
	/**
	 * lst layer  attr sel
	 * sec layer tag name sel
	 */
	@Test
	public void getDispNameBy_a_t(){
		long contactId = 73950;
		String sel = constructSecondLayerSelectorByUniqueAttr(String.valueOf(contactId)).concat("/dispName");
		getReqClient(url.concat(sel));
	}

	@Test
	public void getEmalBy_a_t(){
		long contactId = 73950;
		String sel = constructSecondLayerSelectorByUniqueAttr(String.valueOf(contactId)).concat("/email");
		getReqClient(url.concat(sel));
	}

	/**
	 * 1st layer  index selector
	 * 2nd layer tag name selector
	 * 3rd layer index selector
	 */	
	@Test
	public void getEmailItemBy_i_t_i(){
		
	}
	
}
