package org.ryan.nclcs.vce.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

@SuppressWarnings("serial")
@Entity
@Table(name = "sys_log", catalog = "nclcs_vce_management")
@DynamicInsert(true)
public class SysLogs implements Serializable{
	
	/**
	 * 编号
	 */
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "sys_log_id", unique = true, nullable = false)
	private Integer id;
	
	/**
	 * 操作方法
	 */
	@Column(name = "sys_log_operation_method", nullable = false, length=128)
	private String operationMethod;
	
	/**
	 * 操作人
	 */
	@Column(name = "sys_log_operation_user", nullable = false, length=64)
	private String operationUser;
	
	/**
	 * 操作人
	 */
	@Column(name = "sys_log_operation_description", nullable = false, length=128)
	private String operationDescription;

	/**
	 * 操作时间
	 */
	@Column(name = "sys_log_operation_time", nullable = false)
	private Date operationTime;
	
	/**
	 * 操作IP
	 */
	@Column(name = "sys_log_operation_ip", nullable = false, length=64)
	private String operationIp;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOperationMethod() {
		return operationMethod;
	}

	public void setOperationMethod(String operationMethod) {
		this.operationMethod = operationMethod;
	}

	public String getOperationUser() {
		return operationUser;
	}

	public void setOperationUser(String operationUser) {
		this.operationUser = operationUser;
	}

	public Date getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(Date operationTime) {
		this.operationTime = operationTime;
	}

	public String getOperationIp() {
		return operationIp;
	}

	public void setOperationIp(String operationIp) {
		this.operationIp = operationIp;
	}
	
	public String getOperationDescription() {
		return operationDescription;
	}

	public void setOperationDescription(String operationDescription) {
		this.operationDescription = operationDescription;
	}
}
