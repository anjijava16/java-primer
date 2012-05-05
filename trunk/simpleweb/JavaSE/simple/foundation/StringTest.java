package simple.foundation;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

import org.junit.Test;

public class StringTest {
	static String encode = "utf-8";

	public static void main(String[] args) {
		// String str = "aaaa";
		try {
			// char[] arr = str.toCharArray();
			// System.out.println(en(str));
			
			//jsonEncode();
			//decode();
			
			strs[1] = "abce";
			
			for(String str : strs){
				System.out.println(str);
			}
			
			
			String a = 1 + "xxxx" + "b";
			System.out.println(a);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//UrlParser.test();

	}
	
	@Test
	public void asArray(){
		List<String>idList = new ArrayList<String>();
		idList.add("1");
		idList.add("11");
		idList.add("12");
		idList.add("13");
		
		String[] array = new String[idList.size()];
		idList.toArray(array);		
	}
	
	@Test
	public void encode(){
		String str = "证书启用成功abc!";
		try {
			String st = URLEncoder.encode(str, "utf-8");
			System.out.println(st);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}		
	}
	
	/**
	 * 可以看出java se 默认支持两种script.
	 **/
	@Test
	public void script(){
		List<ScriptEngineFactory> list = new ScriptEngineManager().getEngineFactories();
		for (ScriptEngineFactory s : list) {
			System.out.println(s.getEngineName() + "," + s.getEngineVersion() + "," + s.getLanguageName());
		}
	}

	// 看到没？ java代码可以这样写的！
	@Test
	public void f() {
		StringBuilder builder = new StringBuilder();
		builder.append("1234567");

		builder.setLength(4);
		System.out.print(builder.toString());

		if (new Object() {
			public boolean f() {
				System.out.print("Hello ");
				return false;
			};
		}.f()) {
			System.out.print("Hello ");
		} else {
			System.out.println("world!");
		}

		if (new Object(){
			{
				System.out.println("hello");
			}
		} != null) {
			System.out.print("Hello ");
		} else {
			System.out.println("world!");
		}	
	}

	@Test
	public void split1() {
		String ss = "a|";
		if (ss.endsWith("|")) {
			System.out.println("end with |");
		}

		String s = ":-xixiangbuxingjie";
		s = "-xixiangbuxingjie:";
		String[] a = s.split(":");
	}

	@Test
	public void split() {
		String s = "Greg;Zhang;;;";
		s = "HOME:;;\\;llll;7676;76767;567567 567;76657565|HOME:;;省;城市;街道;邮政编码;国家|HOME:;;;;;77;|HOME:;;;;;;ffff|HOME:;;;67;5;777;|HOME:;;;;567;;|HOME:;;;;56756;;|";
		s = "WORK:-xixiangbuxingjie;;;;;;|HOME:-kefalu;;;shenzheng;guangdong;1345641;china|";
		String[] arr = s.split("\\|");
		arr = s.split("|"); // 一个字符一个字符的分割.
		System.out.println(arr);
	}

	static class UrlParser {
		static void test() {
			String url = "/xcap-root/contacts/8613480783139/DDcs3x7JwQQwqvOT751dhyp3s2od75lFbuwRL9UfCpJSwAeSqwV0bw**/index/~~/contacts/list%5B@name=%22close-friends%22%5D/contact%5B1%5D";
			Map<Field, String> map = UrlParser.parserUrl(url);
			System.out.println(map);
		}

		enum Field {
			AUID, MSISDN, TOKEN, QUERYSTRING
		}

		/**
		 * @param url
		 * @return <li>map key: auid, uid, token, queryString;</li> <li>map is
		 *         null, parameter url is null</li> <li>map size==0, url
		 *         exception(~~ should only one)</li>
		 */
		public static Map<Field, String> parserUrl(String url) {
			if (url != null) {

				String[] temps = url.split("~~"); // split docmuent selector
													// /node document.
				Map<Field, String> result = new EnumMap<Field, String>(
						Field.class);

				String[] urlInfo = null;
				String queryString = null;
				if (temps.length == 1) {
					urlInfo = temps[0].split("/");
				} else if (temps.length == 2) {
					urlInfo = temps[0].split("/");
					queryString = temps[1];
				} else {
					// ~~ 只允许出现一次
					return null;
				}

				String auid = urlInfo[2];
				String msisdn = urlInfo[3];
				String token = urlInfo[4];

				result.put(Field.AUID, auid);
				result.put(Field.MSISDN, msisdn);
				result.put(Field.TOKEN, token);
				if (queryString != null) {
					result.put(Field.QUERYSTRING, queryString);
				}

				return result;
			} else {
				return null;
			}
		}
	}

	static String en(String str) throws UnsupportedEncodingException {
		StringBuilder re = new StringBuilder();
		byte[] b = str.getBytes(encode);
		for (int i = 0; i < b.length; i++) {
			re.append(b[i]);
		}
		return re.toString();
	}

	/**
	 * URL 解码测试
	 */
	@Test
	public static void decode() {
		String url = "%5B%7B%22type%22%3A%22book_package%22%2C%22items%22%3A%5B%7B%22rpid%22%3A6%2C%22bookid%22%3A308147%7D%2C%7B%22rpid%22%3A6%2C%22bookid%22%3A133092%7D%5D%7D%5D";
			///"%20/contacts/list%5B@name=%22close-friends%22%5D/contact%5B@id=%22aa%22%5D/name";
		try {
			String str = URLDecoder.decode(url, "utf-8");
			System.out.println(str);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void jsonEncode(){
		String s = "\"[{\"type\":\"book_package\",\"item\":[{\"rpid\":6,\"bookid\":308147},{\"rpid\":6,\"bookid\":133092}]}]\"";
		String str = s.replace("\"", "\\\"");
		System.out.println("str:" + str);
	}
	
	public static final String[] strs = new String[]{"a","b","c"};
	
	public static final String TT_STRING = "a";
	
}
