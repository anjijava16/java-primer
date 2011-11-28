package http.contacts;


import java.io.File;
import java.text.SimpleDateFormat;

import org.apache.commons.beanutils.Converter;
import org.junit.Test;

//import com.borqs.contact.ifc.Contact;


public class TestPut extends TestBase{
	final static String phoneNo = "8613480783139"; 
	final static String token = "DDcs3x7JwQQwqvOT751dhxPUTartlMV70DRk28fiJjRSwAeSqwV0bw**";

	final String url = constructUrl(phoneNo, token);

	/**
	 * put contacts node.
	 */
	@Test
	public void putDocument(){
		try {
			File file = getXmlFilePath("xml/example-new-contacts.xml");
			System.out.println("file path:" + file.getCanonicalPath());
			putReqClient(url,file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * put contact node by index.
	 */
	@Test
	public void putContactNodeByIndex(){
		int index = 0; // illegal
		index = 1;     //merge  (index >= 1 && index <= size)
		index = 5;      //add ,(db record size is 4)
		String nodeSelector = constructSelectorByIndex(index);
		String u = url.concat(nodeSelector);
		try {
			File file = getXmlFilePath("xml/example-new-contact.xml");
			putReqClient(u,file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * put contact node by node tag name.
	 */
	@Test
	public void putContactNodeByTagName(){
		String nodeSelector = constructSelectorByTagName();
		String u = url.concat(nodeSelector);
		
		String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + 
					 "	<contact method=\"14712580\">" + 
					 "		<contactName>super dan</contactName>" + 
					 "      <deviceId>09</deviceId>" + 
					 "      <rawId>10</rawId>" + 
					 "	</contact>";

		try {
			putReqClient(u, getInputSteam(str));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * put contact node by unique attr.
	 */
	@Test
	public void putContactNodeByUniqueAttr(){
		String method = "12593";
		String nodeSelector = constructSelectorByUniqueAttr(method);
		String u = url.concat(nodeSelector);
		
		String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + 
		 "	<contact method=\"12593\">" + 
		 "		<contactName>super star</contactName>" + 
		 "      <deviceId>1235422</deviceId>" + 
		 "      <rawId>154558888</rawId>" + 
		 "	</contact>";

		try {
			putReqClient(u, getInputSteam(str));
		
		} catch (Exception e) {
		e.printStackTrace();
		}
	}
	
	/**
	 * put contactName node.
	 * 
	 * 1st layer selector by unique attr.
	 * 2nd layer selector by tag name.
	 */
	@Test
	public void putContactNameByAttr(){		
		//six case.
		String method = "12593";
		String nodeSelector = construct_A_T_Selector(method, TagName.contactName);
		String u = url.concat(nodeSelector);
				
		String str = "<contactName>c plus plus</contactName>"; 
		try {
			putReqClient(u, getInputSteam(str));
		
		} catch (Exception e) {
		e.printStackTrace();
		}
	}
	
	/**
	 * put rawId node
	 * 
	 * 1st layer selector by unique attr.
	 * 2nd layer selector by tag name.
	 */
	@Test
	public void putRawIdByAttr(){		
		//six case.
		String method = "9999939999";
		String nodeSelector = construct_A_T_Selector(method, TagName.rawId);
		String u = url.concat(nodeSelector);
				
		String str = "<rawId>123456</rawId>"; 
		try {
			putReqClient(u, getInputSteam(str));
		
		} catch (Exception e) {
		e.printStackTrace();
		}
	}
	
	/**
	 * put deviceId node.
	 * 
	 * 1st layer selector by unique attr.
	 * 2nd layer selector by tag name.
	 */
	@Test
	public void putDeviceIdByAttr(){		
		//six case.
		String method = "9999939999";
		String nodeSelector = construct_A_T_Selector(method, TagName.deviceId);
		String u = url.concat(nodeSelector);
				
		String str = "<deviceId>999</deviceId>";
		//str = "<deviceId></deviceId>";
		try {
			putReqClient(u, getInputSteam(str));
		
		} catch (Exception e) {
		e.printStackTrace();
		}
	}
	
	/**
	 * put contactName node.
	 * 
	 * 1st layer selector by unique attr.
	 * 2nd layer selector by index.
	 */
	@Test
	public void putContactNameByIndex(){		
		//six case.
		String method = "13510776272";
		int index = 1;
		String nodeSelector = construct_A_T_Selector(method, TagName.contactName).concat(LEFT_SQUARE_BRACKET) + index + RIGHT_SQUARE_BRACKET;
		String u = url.concat(nodeSelector);
				
		String str = "<contactName>c</contactName>"; 
		try {
			putReqClient(u, getInputSteam(str));
		
		} catch (Exception e) {
		e.printStackTrace();
		}
	}
	
/*	@Test
	public void testBeanUtil(){
		Contact en = new Contact();
		en.setDeviceID(10L);
		//en.setCreateDate(new Date());
		try {
			ConvertUtils.register(new LongConvert(), Long.class);   
			ConvertUtils.register(new DateConvert(), Date.class);  
			
			BeanUtils.setProperty(en, "deviceID", null);
			//BeanUtils.setProperty(en, "createDate", null); // not need implement Convert.
			BeanUtils.setProperty(en, "createDate", "2011-5-15");  //need implement Convert.
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(en.getDeviceID(), null);
		//Assert.assertEquals(en.getCreateDate(), null);
	}
*/	
	/**
	 * call --->ConvertUtils.register(new DateConvert(), java.util.Date.class);
	 * @author slieer
	 * Create Date2011-11-23
	 * version 1.0
	 */
	public class DateConvert implements Converter{

		public Object convert(Class arg0, Object arg1) {
			String p = (String)arg1;
			if(p== null || p.trim().length()==0){
				return null;
			}
			try{
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				return df.parse(p.trim());
			}
			catch(Exception e){
				return null;
			}
		}
	}
	
	public class LongConvert implements Converter{

		public Object convert(Class arg0, Object arg1) {
			String p = (String)arg1;
			return (p != null && p.trim().length() != 0) ?  Long.valueOf(p) : null;
		}
	}
	
}
