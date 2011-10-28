package http.contacts;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.xcap.web.Token;
import com.xcap.web.Token.TokenInfo;

/**
	 *  # pHJlE_jdISlHeopL0RSI_QZ8T7ZzPcTXyKRTRUHHjMNSwAeSqwV0bw**  8613828724021<br>
	 *	# eE60nQE4So54sNTD59vl-JzFNkMl1lxDqbgptVFd1-FSwAeSqwV0bw**  8613923406916<br>
	 *
	 *	# DDcs3x7JwQQwqvOT751dhyp3s2od75lFbuwRL9UfCpJSwAeSqwV0bw**  8613480783139<br>
	 *	
	 *	# _MfVVlhCuyfw-nlZpt6AqncsOzPC3jjYQ3Pw6wyJoAZSwAeSqwV0bw**  8615989380390<br>
	 *	
	 *	# YPRG0MWas7e6lM6sQ9CL4mxbtGk49jB-SNgtULIamd1SwAeSqwV0bw**  8613714532530<br>

 * @author slieer
 * Create Date2011-10-28
 * version 1.0
 */
public class TokenTest {
	
	@Test
	public void tokenValidate(){
		String token = "cTaXq1hZE8KFZKvglufGdmJ3Wsph5ilq53_7QKIcjblSwAeSqwV0bw**";
		Map<Token.TokenInfo, String> re = Token.parseTokenXML(token);
		System.out.println(re);
		Assert.assertEquals("OK", re.get(TokenInfo.STATUS_TAG), "0");
	}
}
