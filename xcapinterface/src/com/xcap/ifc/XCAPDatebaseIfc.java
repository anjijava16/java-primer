package com.xcap.ifc;


public interface XCAPDatebaseIfc {
	public static String CONTACT_LOCAL_JNDI = "ContactListsApp";
	
	public ResultData get(String userId, String nodeSelector);
	
	public ResultData put(String userId, String nodeSelector);
	
	public ResultData delete(String userId, String nodeSelector);
	
	public class ResultData{
		public static final int STATUS_200 = 200;
		public static final int STATUS_404 = 404;
		public static final int STATUS_409 = 409;
		
		private int status;
		private String xml;
		
		public ResultData(int status, String xml) {
			this.status = status;
			this.xml = xml;
		}

		public int getstatus() {
			return status;
		}

		public String getXml() {
			return xml;
		}	
	}
}
