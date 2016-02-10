package org.ryan.nclcs.vce.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "sys_roles", catalog = "nclcs_vce_management")
public class SysRoles implements Serializable{
	
	/**
	 * 编号
	 */
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "role_id", unique = true, nullable = false)
	private Integer id;
	
	/**
	 * 角色名
	 */
	@Column(name = "role_name", nullable = false, length=32)
	private String roleName;
	
	/**
	 * 角色备注
	 */
	@Column(name = "role_memo", nullable = true, length=64)
	private String roleMemo;
	
	@ManyToMany
	@JoinTable(name="sys_users_roles_relationship", 
		joinColumns={ @JoinColumn(name="role_id") }, inverseJoinColumns={ @JoinColumn(name="user_id") })
	private List<SysUsers> sysRolesUsers;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleMemo() {
		return roleMemo;
	}

	public void setRoleMemo(String roleMemo) {
		this.roleMemo = roleMemo;
	}
	
	public List<SysUsers> getSysRolesUsers() {
		return sysRolesUsers;
	}

	public void setSysRolesUsers(List<SysUsers> sysRolesUsers) {
		this.sysRolesUsers = sysRolesUsers;
	}

	@Override
	public String toString() {
		return "SysRoles [id=" + id + ", roleName=" + roleName + ", roleMemo=" + roleMemo + "]";
	}
	
	
}
