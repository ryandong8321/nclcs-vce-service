package org.ryan.nclcs.vce.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "sys_users_device_token", catalog = "nclcs_vce_management")
public class SysDeviceToken implements Serializable{
	
	/**
	 * 编号
	 */
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "sys_device_token_id", unique = true, nullable = false)
	private Integer id;
	
	/**
	 * device_token
	 */
	@Column(name = "sys_device_token_value", nullable = false, length=64)
	private String deviceTokenValue;
	
	/**
	 * 设备类型0:iOS, 1:Android
	 */
	@Column(name = "sys_device_token_type", nullable = false)
	private Integer deviceTokenType;
	
	/**
	 * 对应用户Id
	 */
	@Column(name = "sys_users_id", nullable = false)
	private Integer sysUserId;
	
	/**
	 * 对应用户的用户名
	 */
	@Column(name = "sys_users_name", nullable = false, length=64)
	private String sysUserName;

	public String getSysUserName() {
		return sysUserName;
	}

	public void setSysUserName(String sysUserName) {
		this.sysUserName = sysUserName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDeviceTokenValue() {
		return deviceTokenValue;
	}

	public void setDeviceTokenValue(String deviceTokenValue) {
		this.deviceTokenValue = deviceTokenValue;
	}

	public Integer getDeviceTokenType() {
		return deviceTokenType;
	}

	public void setDeviceTokenType(Integer deviceTokenType) {
		this.deviceTokenType = deviceTokenType;
	}

	public Integer getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(Integer sysUserId) {
		this.sysUserId = sysUserId;
	}
}
