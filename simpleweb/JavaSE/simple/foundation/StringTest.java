package simple.foundation;

import java.io.UnsupportedEncodingException;

public class StringTest {
	static String encode = "utf-8";
	public static void main(String[] args) {
		String str = "证书启用成功abc!";
		//String str = "aaaa";
	    try {
			//char[] arr = str.toCharArray();
			System.out.println(en(str));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
	}
	
	static String en(String str) throws UnsupportedEncodingException{
    	StringBuilder re = new StringBuilder();
		byte[] b = str.getBytes(encode);
		for(int i = 0; i < b.length; i++){
			re.append(b[i]);
		}
		return re.toString();
	}
}
