package org.ryan.nclcs.vce.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@SuppressWarnings("serial")
@Entity
@Table(name = "sys_properties", catalog = "nclcs_vce_management")
@DynamicInsert(true)
public class SysProperties implements Serializable{
	
	/**
	 * 编号
	 */
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "property_id", unique = true, nullable = false)
	private Integer id;
	
	/**
	 * 系统信息名称
	 */
	@Column(name = "property_name", nullable = true, length=64)
	private String propertyName;
	
	/**
	 * 系统信息上级ID
	 */
	@Column(name = "property_parent_id", nullable = true)
	private Integer propertyParentId;
	
	/**
	 * 班级上课日期
	 */
	@OneToMany(mappedBy="propertyDateInfo")
	@LazyCollection(LazyCollectionOption.EXTRA)
	private List<SysGroups> classDateSysGroups;
	
	/**
	 * 班级
	 */
	@OneToMany(mappedBy="propertyClassInfo")
	@LazyCollection(LazyCollectionOption.EXTRA)
	private List<SysGroups> classInfoSysGroups;
	
	/**
	 * 成绩
	 */
	@OneToMany(mappedBy="appScoreProperty")
	@LazyCollection(LazyCollectionOption.EXTRA)
	private List<AppStudentsScores> appStudentsScores;

	/**
	 * 是否在日校学中文
	 */
	@OneToMany(mappedBy="propertyIsLearnChinese")
	@LazyCollection(LazyCollectionOption.EXTRA)
	private List<SysUsers> isLearnChineseSysUsers;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public Integer getPropertyParentId() {
		return propertyParentId;
	}

	public void setPropertyParentId(Integer propertyParentId) {
		this.propertyParentId = propertyParentId;
	}

	public List<SysGroups> getClassDateSysGroups() {
		return classDateSysGroups;
	}

	public void setClassDateSysGroups(List<SysGroups> classDateSysGroups) {
		this.classDateSysGroups = classDateSysGroups;
	}

	public List<SysGroups> getClassInfoSysGroups() {
		return classInfoSysGroups;
	}

	public void setClassInfoSysGroups(List<SysGroups> classInfoSysGroups) {
		this.classInfoSysGroups = classInfoSysGroups;
	}
	
	public List<SysUsers> getIsLearnChineseSysUsers() {
		return isLearnChineseSysUsers;
	}

	public void setIsLearnChineseSysUsers(List<SysUsers> isLearnChineseSysUsers) {
		this.isLearnChineseSysUsers = isLearnChineseSysUsers;
	}
	
	public List<AppStudentsScores> getAppStudentsScores() {
		return appStudentsScores;
	}

	public void setAppStudentsScores(List<AppStudentsScores> appStudentsScores) {
		this.appStudentsScores = appStudentsScores;
	}

	@Override
	public String toString() {
		return "SysProperties [id=" + id + ", propertyName=" + propertyName + ", propertyParentId=" + propertyParentId
				+ "]";
	}
}
