import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class Test {
	public static void main(String[] args) {
		try {
			String string  = URLEncoder.encode(",","UTF-8");
			System.out.println(string);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
