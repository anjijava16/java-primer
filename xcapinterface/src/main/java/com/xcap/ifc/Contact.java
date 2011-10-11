package com.xcap.ifc;

public class Contact {
	private Long id;
	private Long userId;
	private String contactMethod;
	private String contactName;
	private String contactNickName;
	private Integer contactType;
	private String op;
	private Integer blocking;
	
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
	public Integer getContactType() {
		return contactType;
	}
	public void setContactType(Integer contactType) {
		this.contactType = contactType;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
	public String getContactNickName() {
		return contactNickName;
	}
	public void setContactNickName(String contactNickName) {
		this.contactNickName = contactNickName;
	}
	
	
}
