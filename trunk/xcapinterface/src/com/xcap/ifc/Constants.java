package com.xcap.ifc;

/**
 * 
 * version 1.0
 * Create Date 2011-10-14
 * @author slieer
 */
public class Constants {
	public final static String APP_USAGE_CONTACT = "contacts";
	public final static String APP_USAGE_SINGSPACE_CONTACT = "SingSpaceContacts";
	
	public final static String SCHEMA_DIR = "/WEB-INF/classes/com/xcap/web/xmlschema";
	public final static String XML_SCHEMA_CONTACT = "contacts.xsd";	
	
	public final static String USERS = "users";
	public final static String INTERVAL_SIGN = "/~~/"; 
	
	public final static String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT)";
	public final static String TOKEN_URL = "http://10.24.1.244/ucenter/ifc/get-userinfo-by-token";

	public final static String TOKEN_OK = "0";
	public final static String TOKEN_INVALID = "1";
	
	
	public final static String BASIC_AUTH_LOST_PASSWORD = "Input password please!";
	public final static String BASIC_AUTH_PASSWORD_ERROR = "Sorry, authentication failure!";
	//public final static String BASIC_AUTH_PASSWORD_ERROR_INFO = "用户名或者密码错误";
	
	public static enum HttpMethod {GET,POST,PUT,DELETE};
}
