package simple.foundation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegexTest {
	final static String METHOD_NODE = "method"; 
	final static String CONTACT_NAME_NODE = "contactName";
	final static String USER_ID_NODE = "userId";
	final static String CREATE_DATE_NODE = "createDate";                                    
	
	public static void main(String[] args) {
		//test();
		System.out.println(secondLevelUrlValidate("method"));
		System.out.println(secondLevelUrlValidate("method[1]"));
	}
	
	
	
	/**
	 *   method or method[1] 
     *   contactName or contactName[1] 
     *   userId or userId[1]
     *   createDate createDate[1]
	 *
	 * @param condition2
	 * @return
	 */
	public static String secondLevelUrlValidate(String condition2){
		if (condition2.equals(CONTACT_NAME_NODE) || condition2.equals(METHOD_NODE)
			|| condition2.equals(USER_ID_NODE) || condition2.equals(CREATE_DATE_NODE) ){
			return condition2;
		}else if(condition2.matches("^method\\[1\\]$") || condition2.matches("^contactName\\[1\\]$")
				|| condition2.matches("^userId\\[1\\]$") || condition2.matches("^createDate\\[1\\]$") ){
			Pattern pattern = Pattern.compile("method|contactName|userId|createDate");
			Matcher match = pattern.matcher(condition2);
			if(match.find()){
				return match.group(0);
			}
		}
		return null;
	}

	
	public static void test(){
		String condition1 = "contact[2]";
		
			
		if(condition1.matches("^contact\\[\\d+\\]$")){
			System.out.println("OK");
		}else{
			System.out.println("failure");
		}
		
		Pattern p = Pattern.compile("\\d+");
		Matcher match = p.matcher(condition1);
		if(match.find()){
			System.out.println(match.group(0));
		}		
		
		String condition2 = "contact[@id=\"11\"]";
		if(condition2.matches("^contact\\[@id=\"\\d+\"\\]$")){
			System.out.println("OK");
		}else{
			System.out.println("failure");
		}	
	}
}
