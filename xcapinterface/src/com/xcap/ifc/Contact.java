package com.xcap.ifc;

public class Contact {
	private Long id;
	private String msisdn;
	private String contactMethod;
	private String contactName;
	private String op;
	private Integer blocking;
	private String contactNickName;
	private Long   deviceID;
	private Long   rawID;
	
	public Integer getBlocking() {
		return blocking;
	}
	public void setBlocking(Integer blocking) {
		this.blocking = blocking;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	public String getContactMethod() {
		return contactMethod;
	}
	public void setContactMethod(String contactMethod) {
		this.contactMethod = contactMethod;
	}
	
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getXML(String op,String mobile){
		String m = "";
		if(mobile!=null){
			m = mobile;
		}else{
			m = this.contactMethod;
		}
		if(m==null){
			return null;
		}
		return "<contact username='" + m +"' op='" + op + "'/>";
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getContactNickName() {
		return contactNickName;
	}
	public void setContactNickName(String contactNickName) {
		this.contactNickName = contactNickName;
	}
	public Long getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(Long deviceID) {
		this.deviceID = deviceID;
	}
	public Long getRawID() {
		return rawID;
	}
	public void setRawID(Long rawID) {
		this.rawID = rawID;
	}
	
	
}
