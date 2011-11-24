package com.xcap.ifc;

/**
 * 
 *<li> version 1.0</li>
 *<li> Create Date 2011-10-14</li>
 *<li> author slieer</li>
 */
public class Constants {
	public final static String APP_USAGE_CONTACT = "UABContacts";
	public final static String APP_USAGE_SINGSPACE_CONTACT = "SingSpacesContacts";
	
	public final static String SCHEMA_DIR = "WEB-INF/classes/com/xcap/web/xmlschema";
	public final static String XML_SCHEMA_UAB_CONTACT = "UABContacts.xsd";
	public final static String XML_SCHEMA_SINGSPACES_CONTACT = "SingSpacesContacts.xsd";	
	
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
