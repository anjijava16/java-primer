package com.xcap.dao.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * all field of t_contacts table. 
 */

@NamedQueries({
	@NamedQuery(name="getByMsisdnAndId", query="from UABContactEntity u where u.id=:id and u.msisdn=:msisdn"),
	@NamedQuery(name="getByMethodAndMsisdn", query="from UABContactEntity where contactMethod=:method and msisdn=:msisdn"),
	@NamedQuery(name="getListByMsisdn", query="from UABContactEntity where msisdn = :msisdn"),
	@NamedQuery(name="delByMsisdn", query="delete from UABContactEntity where msisdn = :msisdn"),
	@NamedQuery(name="delByMsisdnAndMethod", query="delete from UABContactEntity where msisdn = :msisdn and contactMethod = :method")
})

@NamedNativeQueries({
	@NamedNativeQuery(name="getListSizeByUserMsisdn", query="select count(id) size from t_contacts where msisdn = :msisdn",
		resultSetMapping="listSizeByUserMsisdn"
	),
	@NamedNativeQuery(name="getByMsisdnAndIndex", query="select * from t_contacts where msisdn = :msisdn limit :index,1",
		resultSetMapping="UABContactEntityMappingByMsisdnAndIndex"
	)
})

@SqlResultSetMappings({
		@SqlResultSetMapping(name = "listSizeByUserMsisdn", 
				columns = @ColumnResult(name = "size")),
		@SqlResultSetMapping(name = "UABContactEntityMappingByMsisdnAndIndex", 
				entities = { @EntityResult(entityClass = UABContactEntity.class) })
})


@Entity
@Table(name="t_contacts")
public class UABContactEntity implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private String msisdn;
	private String contactMethod;
	private String contactName;
	private String description;
	private Long   deviceID;
	private Long   rawID;
	private Date createDate;
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name="msisdn",length=16)
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
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
	
	@Column(name="device_id")
	public Long getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(Long deviceID) {
		this.deviceID = deviceID;
	}
	@Column(name="raw_id")
	public Long getRawID() {
		return rawID;
	}
	public void setRawID(Long rawID) {
		this.rawID = rawID;
	}
	@Column(name = "create_date")
	@Temporal(value = TemporalType.TIMESTAMP)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Override
	public String toString() {
		return "UABContactEntity [id=" + id + ", msisdn=" + msisdn
				+ ", contactMethod=" + contactMethod + ", contactName="
				+ contactName + ", description=" + description + ", deviceID="
				+ deviceID + ", rawID=" + rawID + ", createDate=" + createDate
				+ "]";
	}	
}
