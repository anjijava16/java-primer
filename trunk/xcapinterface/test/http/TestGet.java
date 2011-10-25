package http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestGet extends TestBase{
	Logger log = Logger.getLogger(getClass());
	static String phoneNo = "8613714532530"; 
	static String token = "YPRG0MWas7e6lM6sQ9CL4mxbtGk49jB-SNgtULIamd1SwAeSqwV0bw**";
	
	@Before 
	public void runBeforeTest(){
	} 
	
	@Test
	public void getDocument(){
		String url = constructUrl(phoneNo, token);
		//url = url.concat("/~~/contacts");
        client(url);
	}

	
	
	@Test
	public void getNodeByIndex(){
		String url = constructUrl(phoneNo, token);
		String nodeSelector = "/~~/contacts/contact{0}1{1}";
		
		MessageFormat form = new MessageFormat(nodeSelector);
		Object[] args = {LEFT_SQUARE_BRACKET, RIGHT_SQUARE_BRACKET};
		nodeSelector = form.format(args);
		
		url = url.concat(nodeSelector);
		
		System.out.println("url:" + url);
		client(url);
		
	}
	
	@Test
	public void getNodeByUniqueAttr(){
		String url = constructUrl(phoneNo, token);
		String nodeSelector = "/~~/contacts/contact{0}{1}method={2}8405566{2}{3}";
		
		MessageFormat form = new MessageFormat(nodeSelector);
		Object[] args = {LEFT_SQUARE_BRACKET, AT,DOUBLE_QUOTATION_MARKS, RIGHT_SQUARE_BRACKET};
		nodeSelector = form.format(args);
		
		url = url.concat(nodeSelector);
		
		System.out.println("url:" + url);
		client(url);		
	}
	
	@Test
	public void getNodeByTagName(){
		String url = constructUrl(phoneNo, token);
		String nodeSelector = "/~~/contacts/contact";		
		client(url.concat(nodeSelector));
	}	
	
	@Test
	public void getLeafNodeByTagName(){
		
	}

	@Test
	public void getLeafNodeByIndex(){
		
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
}
