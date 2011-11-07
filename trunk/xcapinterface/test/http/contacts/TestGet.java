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
	
	@Test
	public void getDocument(){
		getReqClient(url);
	}

	@Test
	public void getDocumentByTagName(){
		String u = url.concat("/~~/contacts");
		getReqClient(u);
	}
	
	
	@Test
	public void getNodeByIndex(){
		int index = 1;
		String nodeSelector = constructSelectorByIndex(index);
		String u = url.concat(nodeSelector);
		System.out.println("url:" + u);
		getReqClient(u);
		
	}
	
	@Test
	public void getNodeByUniqueAttr(){
/*		String nodeSelector = "/~~/contacts/contact{0}{1}method={2}8405566{2}{3}";
		MessageFormat form = new MessageFormat(nodeSelector);
		Object[] args = {LEFT_SQUARE_BRACKET, AT,DOUBLE_QUOTATION_MARKS, RIGHT_SQUARE_BRACKET};
		nodeSelector = form.format(args);
*/		
		String method = "46546464646";
		String nodeSelector = constructSelectorByUniqueAttr(method);
		String u = url.concat(nodeSelector);
		
		System.out.println("url:" + u);
		getReqClient(u);		
	}
	
	@Test
	public void getNodeByTagName(){
		String nodeSelector = constructSelectorByTagName();		
		getReqClient(url.concat(nodeSelector));
	}	
	
	@Test
	public void getLeafNodeByTagName(){
		int i = 2;
		String u = null;
		String nodeSelector = null;
		switch (i) {
		case 0:
			//tag name/tagName
			break;
		case 1:
			//index/tagName
			int index = 1;
			nodeSelector = construct_I_T_Leaf(index, TageName.contactName);
			u = url.concat(nodeSelector);
			getReqClient(u);
			break;
		case 2:
			//attr/tagName
			String uniqueAttr = "9999939999"; // method.
			nodeSelector = construct_A_T_Leaf(uniqueAttr, TageName.contactName);
			u = url.concat(nodeSelector);
			log.info(u);
			getReqClient(u);
			break;
		}
		
	}

	@Test
	public void getLeafNodeByIndex(){
		int i = 0;
		switch (i) {
		case 0:
			
			break;
		case 1:
			
			break;
		case 2:
			
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