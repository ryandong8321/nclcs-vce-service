package org.ryan.nclcs.vce.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@SuppressWarnings("serial")
@Entity
@Table(name = "sys_users", catalog = "nclcs_vce_management")
@DynamicInsert(true)
public class SysUsers implements Serializable{

	/**
	 * 编号
	 */
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "user_id", unique = true, nullable = false)
	private Integer id;
	
	/**
	 * 用户名
	 */
	@Column(name = "user_name", nullable = false, length=64)
	private String userName;
	
	/**
	 * 用户名
	 */
	@Column(name = "user_password", nullable = false, length=128)
	private String password;
	
	/**
	 * 中文姓名
	 */
	@Column(name = "user_chinese_name", nullable = true, length=16)
	private String chineseName;
	
	/**
	 * 英文姓名
	 */
	@Column(name = "user_english_name", nullable = true, length=16)
	private String englishName;
	
	/**
	 * 中文拼音
	 */
	@Column(name = "user_chinese_pinyin", nullable = true, length=32)
	private String pinyin;
	
	/**
	 * 家庭电话
	 */
	@Column(name = "user_home_phone", nullable = true, length=16)
	private String homePhone;
	
	/**
	 * 移动电话号码
	 */
	@Column(name = "user_mobile_phone", nullable = false, length=16)
	private String mobilePhone;
	
	/**
	 * email地址
	 */
	@Column(name = "user_email", nullable = false, length=64)
	private String emailAddress;
	
	/**
	 * 家庭住址
	 */
	@Column(name = "user_home_address", nullable = true, length=128)
	private String homeAddress;
	
	/**
	 * 日校名称
	 */
	@Column(name = "user_day_school", nullable = true, length=128)
	private String daySchool;
	
	/**
	 * 是否在日校学习中文
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_is_learn_chinese")
	private SysProperties propertyIsLearnChinese;
	
	/**
	 * 日校年级
	 */
	@Column(name = "user_day_school_grade", nullable = true, length=8)
	private String daySchoolGrade;

	/**
	 * 头像地址
	 */
	@Column(name = "user_photo", nullable = true, length=256)
	private String photoPath;
	
	/**
	 * 孩子的用户ID,用来记录家长与孩子之间的关系
	 */
	@Column(name = "user_children_id", nullable = true)
	private Integer childrenId;
	
	/**
	 * 创建时间
	 */
	@Column(name = "user_register_time", nullable = true)
	private Date registerTime;
	
	/**
	 * 出生年月
	 */
	@Column(name = "user_birth_date", nullable = true, length=8)
	private String birthDate;
	
	@ManyToMany(mappedBy="sysRolesUsers", cascade=CascadeType.ALL)
	private List<SysRoles> sysRoles;
	
	@ManyToMany(mappedBy="sysGroupsUsers", cascade=CascadeType.ALL)
	private List<SysGroups> sysGroups;
	
	/**
	 * notifiction_user_id
	 */
	@OneToMany(mappedBy="notificationUserInfo")
	@LazyCollection(LazyCollectionOption.EXTRA)
	private List<SysNotification> sysNotificationInfo;
	
	/**
	 * notification_detail_receive_user_id
	 */
	@OneToMany(mappedBy="detailReceiveUserInfo")
	@LazyCollection(LazyCollectionOption.EXTRA)
	private List<SysNotificationDetail> sysNotificationDetailInfo;
	
	/**
	 * app_students_scores_user_id
	 */
	@OneToMany(mappedBy="appScoreUser", cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.EXTRA)
	private List<AppStudentsScores> studentsScores;
	
	@Column(name = "user_vce_school_name", nullable = true, length=128)
	private String vceSchoolName;
	
	@Column(name = "user_vce_class_name", nullable = true, length=128)
	private String vceClassName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public String getDaySchool() {
		return daySchool;
	}

	public void setDaySchool(String daySchool) {
		this.daySchool = daySchool;
	}

	public String getDaySchoolGrade() {
		return daySchoolGrade;
	}

	public void setDaySchoolGrade(String daySchoolGrade) {
		this.daySchoolGrade = daySchoolGrade;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public Integer getChildrenId() {
		return childrenId;
	}

	public void setChildrenId(Integer childrenId) {
		this.childrenId = childrenId;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	
	public List<SysRoles> getSysRoles() {
		return sysRoles;
	}

	public void setSysRoles(List<SysRoles> sysRoles) {
		this.sysRoles = sysRoles;
	}
	
	public List<SysGroups> getSysGroups() {
		return sysGroups;
	}

	public void setSysGroups(List<SysGroups> sysGroups) {
		this.sysGroups = sysGroups;
	}

	public List<SysNotification> getSysNotificationInfo() {
		return sysNotificationInfo;
	}

	public void setSysNotificationInfo(List<SysNotification> sysNotificationInfo) {
		this.sysNotificationInfo = sysNotificationInfo;
	}
	
	public List<SysNotificationDetail> getSysNotificationDetailInfo() {
		return sysNotificationDetailInfo;
	}

	public void setSysNotificationDetailInfo(List<SysNotificationDetail> sysNotificationDetailInfo) {
		this.sysNotificationDetailInfo = sysNotificationDetailInfo;
	}
	
	public String getVceSchoolName() {
		return vceSchoolName;
	}

	public void setVceSchoolName(String vceSchoolName) {
		this.vceSchoolName = vceSchoolName;
	}

	public String getVceClassName() {
		return vceClassName;
	}

	public void setVceClassName(String vceClassName) {
		this.vceClassName = vceClassName;
	}
	
	public SysProperties getPropertyIsLearnChinese() {
		return propertyIsLearnChinese;
	}

	public void setPropertyIsLearnChinese(SysProperties propertyIsLearnChinese) {
		this.propertyIsLearnChinese = propertyIsLearnChinese;
	}
	
	public List<AppStudentsScores> getStudentsScores() {
		return studentsScores;
	}

	public void setStudentsScores(List<AppStudentsScores> studentsScores) {
		this.studentsScores = studentsScores;
	}

	@Override
	public String toString() {
		return "SysUsers [id=" + id + ", userName=" + userName + ", password=" + password + ", chineseName="
				+ chineseName + ", englishName=" + englishName + ", pinyin=" + pinyin + ", homePhone=" + homePhone
				+ ", mobilePhone=" + mobilePhone + ", emailAddress=" + emailAddress + ", homeAddress=" + homeAddress
				+ ", daySchool=" + daySchool + ", daySchoolGrade=" + daySchoolGrade
				+ ", daySchool=" + daySchool + ", daySchoolGrade=" + daySchoolGrade
				+ ", photoPath=" + photoPath + ", childrenId=" + childrenId + ", registerTime=" + registerTime
				+ ", birthDate=" + birthDate + ", vceSchoolName=" + vceSchoolName + ", vceClassName=" + vceClassName + "]";
	}
}
