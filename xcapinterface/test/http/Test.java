package http;

import java.io.File;
import java.text.MessageFormat;

public abstract class Test {
	
	/**
	 *  # pHJlE_jdISlHeopL0RSI_QZ8T7ZzPcTXyKRTRUHHjMNSwAeSqwV0bw**  8613828724021<br>
	 *	# eE60nQE4So54sNTD59vl-JzFNkMl1lxDqbgptVFd1-FSwAeSqwV0bw**  8613923406916<br>
	 *
	 *	# DDcs3x7JwQQwqvOT751dhyp3s2od75lFbuwRL9UfCpJSwAeSqwV0bw**  8613480783139<br>
	 *	
	 *	# _MfVVlhCuyfw-nlZpt6AqncsOzPC3jjYQ3Pw6wyJoAZSwAeSqwV0bw**  8615989380390<br>
	 *	
	 *	# YPRG0MWas7e6lM6sQ9CL4mxbtGk49jB-SNgtULIamd1SwAeSqwV0bw**  8613714532530<br>
	*/
	public static String constructUrl(String phoneNo, String token) {
		String url = "http://localhost:8080/xcap-root/contacts/{0}/{1}/index";
		MessageFormat form = new MessageFormat(url);
		Object[] args = { phoneNo, token };
		return form.format(args);
	}
	
	public static File getXmlFilePath(String fileName){
		return new File("test/http/".concat(fileName));
	}
}
