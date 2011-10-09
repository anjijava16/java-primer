package com.xcap.ifc;

public class Constants {
	
	/**url constant contract**/
	public final static String APP_USAGE = "resource-lists";
	public final static String USERS = "users";
	public final static String INTERVAL_SIGN = "~~"; 
	
	
	public final static String STATUS_TAG = "status";
	public final static String TOKEN_EXPIRED_TIME_TAG = "tokenExpiredTime";
	public final static String UID_TAG = "uid";
	public final static String MSISDN_TAG = "msisdn";

	public final static String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT)";
	public final static String TOKEN_URL = "http://10.24.1.244/ucenter/ifc/get-userinfo-by-token";

	public final static String TOKEN_OK = "0";
	public final static String TOKEN_INVALID = "1";
	
	
	public final static String BASIC_AUTH_LOST_PASSWORD = "Input password please!";
	public final static String BASIC_AUTH_PASSWORD_ERROR = "Sorry, authentication failure!";
	//public final static String BASIC_AUTH_PASSWORD_ERROR_INFO = "用户名或者密码错误";
	
	public static enum http_op {get,put,delete};
}
