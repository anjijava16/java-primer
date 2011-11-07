/**
 * @author Brin.Liu
 * @email liubilin@gmail.com
 * @version 0.1
 *          <p>
 */
package com.xcap.dao.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
/**
 * all field of t_user_contacts table.
 * @author slieer
 * Create Date2011-10-29
 * version 1.0
 */

@NamedQueries({
    @NamedQuery(name="listByUserIdAndStatus", 
    		query="from SingSpacesContactEntity s where s.userId = :userId and s.contactStatus = :status"),
    @NamedQuery(name="getByUserIdAndIdAndStatus", 
    		query="from SingSpacesContactEntity s where s.userId = :userId and s.contactId = :id and s.contactStatus = :status")
})
@NamedNativeQueries({
	@NamedNativeQuery(name="byIndexAndStatus",
			query="select * from t_user_contacts s where s.user_id = :userId and s.contact_status = :status limit :index,1",
			resultClass=SingSpacesContactEntity.class),
	@NamedNativeQuery(name="listSizeByUserIdAndStatus",
		    query="select count(s.contact_id) size from t_user_contacts s where s.user_id = :userId and s.contact_status = :status",		   
		    resultSetMapping="listSizeByUserIdAndStatusMap"
	)
})

@SqlResultSetMapping(name="listSizeByUserIdAndStatusMap",columns=@ColumnResult(name="size"))
 
@Entity
@Table(name = "t_user_contacts")
public class SingSpacesContactEntity implements Serializable {
	private static final long serialVersionUID = -3973755907252488439L;

	private Long contactId;
	private String contactFN;
	private String contactN;
	private String contactNickName;
	private String contactBDay;
	private String contactADR;
	private String contactLabel;
	private String contactTEL;
	private String contactEmail;
	private String contactTitle;
	private String contactORG;
	private String contactNote;
	private Date lastModify;
	private String contactURL;
	private String contactXtends;
	private String contactMobile;
	private Character contactIndex;
	private Byte contactStatus;
	private Integer origin;
	private Byte syncPhone;

	//private PhotoEntity photo;

	private Long userId;

	public SingSpacesContactEntity() {

	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "contact_id")
	public Long getContactId() {
		return contactId;
	}

	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}

	@Column(name = "contact_fn", length = 128)
	public String getContactFN() {
		return contactFN;
	}

	public void setContactFN(String contactFN) {
		this.contactFN = contactFN;
	}

	@Column(name = "contact_n", length = 256)
	public String getContactN() {
		return contactN;
	}

	public void setContactN(String contactN) {
		this.contactN = contactN;
	}

	@Column(name = "contact_nickname", length = 64)
	public String getContactNickName() {
		return contactNickName;
	}

	public void setContactNickName(String contactNickName) {
		this.contactNickName = contactNickName;
	}

	@Column(name = "contact_birthday", length = 16)
	public String getContactBDay() {
		return contactBDay;
	}

	public void setContactBDay(String contactBDay) {
		this.contactBDay = contactBDay;
	}

	@Column(name = "contact_adr", length = 512)
	public String getContactADR() {
		return contactADR;
	}

	public void setContactADR(String contactADR) {
		this.contactADR = contactADR;
	}

	@Column(name = "contact_label", length = 512)
	public String getContactLabel() {
		return contactLabel;
	}

	public void setContactLabel(String contactLabel) {
		this.contactLabel = contactLabel;
	}

	@Column(name = "contact_tel", length = 512)
	public String getContactTEL() {
		return contactTEL;
	}

	public void setContactTEL(String contactTEL) {
		this.contactTEL = contactTEL;
	}

	@Column(name = "contact_email", length = 512)
	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	@Column(name = "contact_title", length = 128)
	public String getContactTitle() {
		return contactTitle;
	}

	public void setContactTitle(String contactTitle) {
		this.contactTitle = contactTitle;
	}

	@Column(name = "contact_org", length = 255)
	public String getContactORG() {
		return contactORG;
	}

	public void setContactORG(String contactORG) {
		this.contactORG = contactORG;
	}

	@Column(name = "contact_note", length = 256)
	public String getContactNote() {
		return contactNote;
	}

	public void setContactNote(String contactNote) {
		this.contactNote = contactNote;
	}

	@Column(name = "last_modify", length = 11)
	public Date getLastModify() {
		return lastModify;
	}

	public void setLastModify(Date lastModify) {
		this.lastModify = lastModify;
	}

	@Column(name = "contact_url", length = 512)
	public String getContactURL() {
		return contactURL;
	}

	public void setContactURL(String contactURL) {
		this.contactURL = contactURL;
	}

	@Column(name = "contact_mobile", length = 256)
	public String getContactMobile() {
		return contactMobile;
	}

	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}

	@Column(name = "contact_index", length = 1)
	public Character getContactIndex() {
		return contactIndex;
	}

	public void setContactIndex(Character contactIndex) {
		this.contactIndex = contactIndex;
	}

	@Column(name = "contact_status")
	public Byte getContactStatus() {
		return contactStatus;
	}

	public void setContactStatus(Byte contactStatus) {
		this.contactStatus = contactStatus;
	}

	@Column(name = "origin")
	public Integer getOrigin() {
		return origin;
	}

	public void setOrigin(Integer origin) {
		this.origin = origin;
	}

	@Column(name = "user_id", length = 12, nullable = false)
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

/*	@OneToOne(cascade = CascadeType.ALL, optional = true)
	@JoinColumn(name = "photo_id")
	public PhotoEntity getPhoto() {
		return photo;
	}

	public void setPhoto(PhotoEntity photo) {
		this.photo = photo;
	}
*/

	@Column(name = "contact_xtends", length = 512)
	public String getContactXtends() {
		return contactXtends;
	}

	public void setContactXtends(String contactXtends) {
		this.contactXtends = contactXtends;
	}

	@Column(name = "sync_phone")
	public Byte getSyncPhone() {
		return syncPhone;
	}

	public void setSyncPhone(Byte syncPhone) {
		this.syncPhone = syncPhone;
	}

	@Override
	public String toString() {
		return "SingSpacesContactEntity [contactId=" + contactId
				+ ", contactFN=" + contactFN + ", contactN=" + contactN
				+ ", contactNickName=" + contactNickName + ", contactBDay="
				+ contactBDay + ", contactADR=" + contactADR
				+ ", contactLabel=" + contactLabel + ", contactTEL="
				+ contactTEL + ", contactEmail=" + contactEmail
				+ ", contactTitle=" + contactTitle + ", contactORG="
				+ contactORG + ", contactNote=" + contactNote + ", lastModify="
				+ lastModify + ", contactURL=" + contactURL
				+ ", contactXtends=" + contactXtends + ", contactMobile="
				+ contactMobile + ", contactIndex=" + contactIndex
				+ ", contactStatus=" + contactStatus + ", origin=" + origin
				+ ", syncPhone=" + syncPhone + ", userId=" + userId + "]";
	}
	
	
}
