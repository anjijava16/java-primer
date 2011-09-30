package com.xcap.services;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.xcap.Constants;

public class Token {
	public static final Logger log = Logger.getLogger(Token.class);
	
	static class TokenContentHandler extends DefaultHandler {
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
			
			if (tagName == Constants.STATUS_TAG && status == null) {
				this.status = value;
			} else if (tagName == Constants.TOKEN_EXPIRED_TIME_TAG
					&& tokenExpiredTime == null) {
				tokenExpiredTime = value;
			} else if (tagName == Constants.UID_TAG && uid == null) {
				uid = value;
			} else if (tagName == Constants.MSISDN_TAG && msisdn == null) {
				msisdn = value;
			}
		}
		
		public Map<String, String> getResult(){
			Map<String, String> result = new HashMap<String, String>(8);
			result.put(Constants.STATUS_TAG, this.status);
			result.put(Constants.TOKEN_EXPIRED_TIME_TAG, this.tokenExpiredTime);
			result.put(Constants.UID_TAG, this.uid);
			result.put(Constants.MSISDN_TAG, this.msisdn);
			return result;
		}
	}

	public static Map<String,String> parseTokenXML(String token){
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
	
	public static void main(String[] args) {
		try {
			String token = "lBCzuDXqQptyckKnNdfMagmZUEjBkoAztj3VbvE90t5SwAeSqwV0bw**";
			Map<String, String> map = new Token().parseTokenXML(token);
			System.out.println(map);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}
