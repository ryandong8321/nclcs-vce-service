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
@Table(name = "app_missed_notification", catalog = "nclcs_vce_management")
@DynamicInsert(true)
public class AppMissedNotification implements Serializable{
	
	/**
	 * 编号
	 */
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "app_missed_notification_id", unique = true, nullable = false)
	private Integer id;
	
	/**
	 * 对应用户的用户名
	 */
	@Column(name = "app_missed_notification_sys_user_name", nullable = false, length=64)
	private String sysUserName;

	/**
	 * 设备类型0:iOS, 1:Android
	 */
	@Column(name = "app_missed_notification_device_type", nullable = false)
	private Integer deviceTokenType;
	
	/**
	 * 通知标题
	 */
	@Column(name = "app_missed_notification_title", nullable = false, length=64)
	private String notifictionTitle;
	
	/**
	 * 通知内容
	 */
	@Column(name = "app_missed_notification_content", nullable = false, length=128)
	private String notifictionContent;
	
	/**
	 * 错过的发送时间
	 */
	@Column(name = "app_missed_notification_time", nullable = true)
	private Date missedTime;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSysUserName() {
		return sysUserName;
	}

	public void setSysUserName(String sysUserName) {
		this.sysUserName = sysUserName;
	}

	public Integer getDeviceTokenType() {
		return deviceTokenType;
	}

	public void setDeviceTokenType(Integer deviceTokenType) {
		this.deviceTokenType = deviceTokenType;
	}

	public String getNotifictionContent() {
		return notifictionContent;
	}

	public void setNotifictionContent(String notifictionContent) {
		this.notifictionContent = notifictionContent;
	}

	public Date getMissedTime() {
		return missedTime;
	}

	public void setMissedTime(Date missedTime) {
		this.missedTime = missedTime;
	}
	
	public String getNotifictionTitle() {
		return notifictionTitle;
	}

	public void setNotifictionTitle(String notifictionTitle) {
		this.notifictionTitle = notifictionTitle;
	}

	@Override
	public String toString() {
		return "AppMissedNotification [id=" + id + ", sysUserName=" + sysUserName + ", deviceTokenType="
				+ deviceTokenType + ", notifictionTitle=" + notifictionTitle + ", notifictionContent="
				+ notifictionContent + ", missedTime=" + missedTime + "]";
	}
}
