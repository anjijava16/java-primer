package com.xcap.web;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Scanner;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.xcap.ifc.Constants;

/**
 * 
 * version 1.0
 * Create Date 2011-10-14
 * @author slieer
 */
public class Token {
	public static final Logger log = Logger.getLogger(Token.class);
	
	public enum TokenInfo{
		STATUS_TAG,TOKEN_EXPIRED_TIME_TAG,UID_TAG,MSISDN_TAG
	}
	
	static class TokenContentHandler extends DefaultHandler {
		public final static String STATUS_TAG = "status";
		public final static String TOKEN_EXPIRED_TIME_TAG = "tokenExpiredTime";
		public final static String UID_TAG = "uid";
		public final static String MSISDN_TAG = "msisdn";		
		
		private String status = null;
		private String tokenExpiredTime = null;
		private String uid = null;
		private String msisdn = null;
		private String tagName = null;
				
		public void startElement(String uri, String localName, String qName,
				Attributes atts) throws SAXException {
			tagName = qName;
		}

		public void characters(char[] ch, int start, int length)
				throws SAXException {
			String value = String.valueOf(ch);
			value = value.substring(start, start + length);
			
			if (tagName == STATUS_TAG && status == null) {
				this.status = value;
			} else if (tagName == TOKEN_EXPIRED_TIME_TAG
					&& tokenExpiredTime == null) {
				tokenExpiredTime = value;
			} else if (tagName == UID_TAG && uid == null) {
				uid = value;
			} else if (tagName == MSISDN_TAG && msisdn == null) {
				msisdn = value;
			}
		}
		
		public Map<TokenInfo, String> getResult(){
			Map<TokenInfo,String> result = new EnumMap<TokenInfo, String>(TokenInfo.class);

			result.put(TokenInfo.STATUS_TAG, this.status);
			result.put(TokenInfo.TOKEN_EXPIRED_TIME_TAG, this.tokenExpiredTime);
			result.put(TokenInfo.UID_TAG, this.uid);
			result.put(TokenInfo.MSISDN_TAG, this.msisdn);
			return result;
		}
	}

	public static Map<TokenInfo,String> parseTokenXML(String token){
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			
			TokenContentHandler handler = new TokenContentHandler();
			try {
				saxParser = spf.newSAXParser();
				
				URL url = new URL(Constants.TOKEN_URL.concat("?token=").concat(token));
				URLConnection conn = url.openConnection();
				
				Scanner scanner = new Scanner(new InputStreamReader(conn.getInputStream()));
				StringBuilder builder = new StringBuilder();
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					builder.append(line);
				}
				scanner.close();
				
				StringReader stringReader = new StringReader(builder.toString());
				InputSource source = new InputSource(stringReader); 
				saxParser.parse(source, handler);
				
				stringReader.close();
				
				return handler.getResult();
				//return handler.getResult();
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
			}
			
			return null;
	}
}
