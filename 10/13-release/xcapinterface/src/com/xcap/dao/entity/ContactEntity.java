package com.xcap.dao.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="t_contacts")
public class ContactEntity implements Serializable  {
	
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long userId;
	private String contactMethod;
	private String contactName;
	private String description;
	private Date createDate;
	//0->normal,1->recommend
	private Integer contactType;
	private Integer blocking;
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name="user_id")
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	@Column(name="contact_method",length=50)
	public String getContactMethod() {
		return contactMethod;
	}
	public void setContactMethod(String contactMethod) {
		this.contactMethod = contactMethod;
	}
	@Column(name="contact_name",length=100)
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	@Column(name="description",length=100)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Column(name = "create_date")
	@Temporal(value = TemporalType.TIMESTAMP)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Column(name="contact_type",length=4)
	public Integer getContactType() {
		return contactType;
	}
	public void setContactType(Integer contactType) {
		this.contactType = contactType;
	}
	@Column(name="blocking",length=4)
	public Integer getBlocking() {
		return blocking;
	}
	public void setBlocking(Integer blocking) {
		this.blocking = blocking;
	}
	
}
