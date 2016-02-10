package org.ryan.nclcs.vce.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;


@SuppressWarnings("serial")
@Entity
@Table(name = "sys_groups", catalog = "nclcs_vce_management")
@DynamicInsert(true)
public class SysGroups implements Serializable{
	
	/**
	 * 编号
	 */
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "group_id", unique = true, nullable = false)
	private Integer id;
	
	/**
	 * 群组名称
	 */
	@Column(name = "group_name", nullable = false, length=64)
	private String groupName;
	
	/**
	 * 群组类别(0-校区,1-班级)
	 */
	@Column(name = "group_category", nullable = false)
	private Integer groupCategory;
	
	/**
	 * 群组上级ID
	 */
	@Column(name = "group_parent_id", nullable = false)
	private Integer groupParentId;
	
	/**
	 * 班级上课日期
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="group_date_info")
	private SysProperties propertyDateInfo;
	
	/**
	 * 班级系统属性
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="group_class_info")
	private SysProperties propertyClassInfo;
	
	/**
	 * 班级上课时间
	 */
	@Column(name = "group_time_info", nullable = true, length=16)
	private String gropuTimeInfo;
	
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name="sys_users_groups_relationship", 
		joinColumns={ @JoinColumn(name="relationship_group_id") }, inverseJoinColumns={ @JoinColumn(name="relationship_user_id") })
	private List<SysUsers> sysGroupsUsers;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getGroupCategory() {
		return groupCategory;
	}

	public void setGroupCategory(Integer groupCategory) {
		this.groupCategory = groupCategory;
	}

	public Integer getGroupParentId() {
		return groupParentId;
	}

	public void setGroupParentId(Integer groupParentId) {
		this.groupParentId = groupParentId;
	}

	public SysProperties getPropertyDateInfo() {
		return propertyDateInfo;
	}

	public void setPropertyDateInfo(SysProperties propertyDateInfo) {
		this.propertyDateInfo = propertyDateInfo;
	}

	public SysProperties getPropertyClassInfo() {
		return propertyClassInfo;
	}

	public void setPropertyClassInfo(SysProperties propertyClassInfo) {
		this.propertyClassInfo = propertyClassInfo;
	}

	public String getGropuTimeInfo() {
		return gropuTimeInfo;
	}

	public void setGropuTimeInfo(String gropuTimeInfo) {
		this.gropuTimeInfo = gropuTimeInfo;
	}
	
	public List<SysUsers> getSysGroupsUsers() {
		return sysGroupsUsers;
	}

	public void setSysGroupsUsers(List<SysUsers> sysGroupsUsers) {
		this.sysGroupsUsers = sysGroupsUsers;
	}

	@Override
	public String toString() {
		return "SysGroups [id=" + id + ", groupName=" + groupName + ", groupCategory=" + groupCategory
				+ ", groupParentId=" + groupParentId + ", gropuTimeInfo=" + gropuTimeInfo + "]";
	}
	
}
