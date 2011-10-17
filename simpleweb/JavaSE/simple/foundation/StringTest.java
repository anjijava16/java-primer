package simple.foundation;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.EnumMap;
import java.util.Map;

import simple.foundation.a11.f;

public class StringTest {
	static String encode = "utf-8";
	public static void main(String[] args) {
		String str = "证书启用成功abc!";
		//String str = "aaaa";
	    try {
			//char[] arr = str.toCharArray();
			//System.out.println(en(str));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		UrlParser.test();
		
	}
		
	static class UrlParser{
		static void test(){
			String url = "/xcap-root/contacts/8613480783139/DDcs3x7JwQQwqvOT751dhyp3s2od75lFbuwRL9UfCpJSwAeSqwV0bw**/index/~~/contacts/list%5B@name=%22close-friends%22%5D/contact%5B1%5D";
			Map<Field, String> map = UrlParser.parserUrl(url);
			System.out.println(map);
		}
		
		enum Field{
			AUID,MSISDN,TOKEN,QUERYSTRING
		}

		/**
		 * @param url
		 * @return <li>map key: auid, uid, token, queryString;</li>
		 *         <li> map is null, parameter url is null</li>
		 *         <li> map size==0, url exception(~~ should only one)</li>
		 */
		public static Map<Field,String> parserUrl(String url){
			if(url != null){
				
				String[] temps = url.split("~~");  //split docmuent selector /node document.
				Map<Field,String> result = new EnumMap<Field, String>(Field.class);
				
				String[] urlInfo = null;
				String queryString = null;
				if(temps.length == 1){
					urlInfo = temps[0].split("/"); 
				}else if(temps.length == 2){
					urlInfo = temps[0].split("/");
					queryString = temps[1];
				}else{
					//~~ 只允许出现一次
					return null;
				}
				
				String auid = urlInfo[2];
				String msisdn = urlInfo[3];
				String token = urlInfo[4];
				
				result.put(Field.AUID, auid);
				result.put(Field.MSISDN, msisdn);
				result.put(Field.TOKEN, token);
				if(queryString != null){
					result.put(Field.QUERYSTRING, queryString);
				}
				
				return result;
			}else{
				return null;				
			}
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
	
	/**
	 * URL 解码测试
	 */
	public static void decode(){
		String url = "%20/contacts/list%5B@name=%22close-friends%22%5D/contact%5B@id=%22aa%22%5D/name";
		try {
			String str = URLDecoder.decode(url,"utf-8");
			System.out.println(str);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}
