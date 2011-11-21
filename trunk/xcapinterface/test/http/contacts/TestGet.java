package http.contacts;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestGet extends TestBase{
	public static Logger log = Logger.getLogger(TestGet.class);	
	
	final static String phoneNo = "8613480783139"; 
	final static String token = "DDcs3x7JwQQwqvOT751dhxPUTartlMV70DRk28fiJjRSwAeSqwV0bw**";

	final static String url = constructUrl(phoneNo, token);

	@Before 
	public void runBeforeTest(){
	} 
	
	/**
	 * get contact list by userId.
	 */
	@Test
	public void getDocument(){
		getReqClient(url);
	}

	/**
	 * get contact list by userId.
	 */
	@Test
	public void getDocumentByTagName(){
		String u = url.concat("/~~/contacts");
		getReqClient(u);
	}

	/**
	 * get contact by index.
	 */
	@Test
	public void getNodeByIndex(){
		int index = 1;
		String nodeSelector = constructSelectorByIndex(index);
		String u = url.concat(nodeSelector);
		getReqClient(u);
	}
	
	/**
	 * get contact by unique attr.
	 */
	@Test
	public void getNodeByUniqueAttr(){		
		String method = "12593";
		String nodeSelector = constructSelectorByUniqueAttr(method);
		String u = url.concat(nodeSelector);
		
		getReqClient(u);		
	}
	
	/**
	 * get contact by tag name.
	 */
	@Test
	public void getNodeByTagName(){
		String nodeSelector = constructSelectorByTagName();		
		getReqClient(url.concat(nodeSelector));
	}	
	
	/**
	 * get conatctName.
	 */
	@Test
	public void getConatctNameBy_3rdSelectorIsTagName(){
		int index = 1;
		String uniqueAttr = "12593"; // method.
		
		TagName tagName = TagName.contactName;

		getLeafNodeByTagName(tagName, index, uniqueAttr, 0);
		getLeafNodeByTagName(tagName, index, uniqueAttr, 1);
		getLeafNodeByTagName(tagName, index, uniqueAttr, 2);
	}
	
	/**
	 * get conatctName.
	 */
	@Test
	public void getRawIdBy_3rdSelectorIsTagName(){
		int index = 1;
		String uniqueAttr = "12593"; // method.
		
		TagName tagName = TagName.rawId;

		getLeafNodeByTagName(tagName, index, uniqueAttr, 0);
		getLeafNodeByTagName(tagName, index, uniqueAttr, 1);
		getLeafNodeByTagName(tagName, index, uniqueAttr, 2);
	}
	
	@Test
	public void getConatctNameBy_3rdSelectorIsIndex(){
		int _2ndIndex = 3;
		String method = "12593";
		
		TagName tagName = TagName.contactName;
		int _3rdLayerIndex = 1;
		
		getLeafNodeByIndex(tagName, _2ndIndex, method, _3rdLayerIndex, 0);
		getLeafNodeByIndex(tagName, _2ndIndex, method, _3rdLayerIndex, 1);
		getLeafNodeByIndex(tagName, _2ndIndex, method, _3rdLayerIndex, 2);
	}
	
	
	private void getLeafNodeByTagName(TagName tagName, int index, String uniqueAttr, int i){		
		String nodeSelector = null;
		String u = null;
		switch (i) {
		case 0:
			//tag name/tagName
			nodeSelector = construct_T_T_Selector(tagName);
			u = url.concat(nodeSelector);
			break;
		case 1:
			//index/tagName
			nodeSelector = construct_I_T_Selector(index, tagName);
			u = url.concat(nodeSelector);
			break;
		case 2:
			//attr/tagName
			nodeSelector = construct_A_T_Selector(uniqueAttr, tagName);
			u = url.concat(nodeSelector);
			break;
		}
		getReqClient(u);
	}

	public void getLeafNodeByIndex(TagName tagName, int _2ndIndex, String method, int _3rdLayerIndex, int i){		
		String u = null;
		switch (i) {
		case 0:
			String sel = construct_T_I_Selector(tagName, _3rdLayerIndex);
			u = url.concat(sel);
			break;
		case 1:
			sel = construct_I_I_Selector(_2ndIndex, tagName, _3rdLayerIndex);
			u = url.concat(sel);
			break;
		case 2:
			sel = construct_A_I_Selector(method, tagName, _3rdLayerIndex);
			u = url.concat(sel);
			break;
		}
		getReqClient(u);
	}
		
	
	@Ignore("tested")
	@Test
	public void uriWord(){
		String s = "[";
		String s1 = "]";
		String s2 = "@";
		String s3 = "\"";
		
		try {
			System.out.println(URLEncoder.encode(s, CODING));
			System.out.println(URLEncoder.encode(s1, CODING));
			System.out.println(URLEncoder.encode(s2, CODING));
			System.out.println(URLEncoder.encode(s3, CODING));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	@Ignore("tested")
	@Test
	public void stringSplitTest(){
		String condition1 = "~~/contacts/contact[@contactId=\"1982332x1\"]";
		int beginIndex = condition1.indexOf("=\"");
		int endIndex = condition1.indexOf("\"]");
		
		if(beginIndex != -1 && endIndex != -1 && beginIndex < endIndex){
			String method = condition1.substring(beginIndex + 2, endIndex);
			System.out.println(method);
		}		
	}
	
	public static void main(String[] args) {
		log.info("--------------");
	}
}
