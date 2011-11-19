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
		String method = "13510776272";
		String nodeSelector = constructSelectorByUniqueAttr(method);
		String u = url.concat(nodeSelector);
		
		System.out.println("url:" + u);
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
	public void getLeafNodeByTagName(){
		int i = 2;
		String u = null;
		String nodeSelector = null;
		switch (i) {
		case 0:
			//tag name/tagName
			nodeSelector = construct_T_T_Leaf(TageName.contactName);
			u = url.concat(nodeSelector);
			break;
		case 1:
			//index/tagName
			int index = 1;
			nodeSelector = construct_I_T_Leaf(index, TageName.contactName);
			u = url.concat(nodeSelector);
			break;
		case 2:
			//attr/tagName
			String uniqueAttr = "13510776272"; // method.
			nodeSelector = construct_A_T_Leaf(uniqueAttr, TageName.contactName);
			u = url.concat(nodeSelector);
			break;
		}
		log.info(u);
		getReqClient(u);
	}

	@Test
	public void getLeafNodeByIndex(){
		TageName tagName = TageName.contact;
		int index = 1;
		String method = "";
		int i = 0;
		switch (i) {
		case 0:
			String sel = construct_T_I_Leaf(tagName,index);
			String u = url.concat(sel);
			getReqClient(u);
			break;
		case 1:
			int _3rdLayerIndex = 1;
			u = construct_I_I_eafSelector(index,_3rdLayerIndex);
			break;
		case 2:
			u = construct_A_I_eafSelector(method, index);
			break;
		}		
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
