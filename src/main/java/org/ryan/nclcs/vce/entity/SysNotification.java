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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.StringEscapeUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@SuppressWarnings("serial")
@Entity
@Table(name = "sys_notification", catalog = "nclcs_vce_management")
@DynamicInsert(true)
public class SysNotification implements Serializable{
	
	/**
	 * 编号
	 */
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "notification_id", unique = true, nullable = false)
	private Integer id;
	
	/**
	 * title
	 */
	@Column(name = "notification_title", nullable = false, length=64)
	private String notificationTitle;
	
	/**
	 * message
	 */
	@Column(name = "notification_message", nullable = false, length=256)
	private String notificationMessage;
	
	/**
	 * 创建时间
	 */
	@Column(name = "notification_create_time", nullable = true)
	private Date notificationCreateTime;
	
	/**
	 * 接收组ID
	 */
	@Column(name = "notification_receive_group_ids", nullable = true, length=256)
	private String notificationReceiveGroupIds;
	
	/**
	 * 是否发送(0-未发送,1-已发送)
	 */
	@Column(name = "notification_is_send", nullable = false)
	private Integer isSend;

	/**
	 * user_id
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="notification_create_user_id")
	private SysUsers notificationUserInfo;
	
	/**
	 * detail_notifiction_id
	 */
	@OneToMany(mappedBy="detailNotificationInfo", cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.EXTRA)
	private List<SysNotificationDetail> sysNotificationDetailInfo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNotificationTitle() {
		return StringEscapeUtils.unescapeHtml(notificationTitle);
	}

	public void setNotificationTitle(String notificationTitle) {
		this.notificationTitle = notificationTitle;
	}

	public String getNotificationMessage() {
		return StringEscapeUtils.unescapeHtml(notificationMessage);
	}

	public void setNotificationMessage(String notificationMessage) {
		this.notificationMessage = notificationMessage;
	}

	public Date getNotificationCreateTime() {
		return notificationCreateTime;
	}

	public void setNotificationCreateTime(Date notificationCreateTime) {
		this.notificationCreateTime = notificationCreateTime;
	}

	public SysUsers getNotificationUserInfo() {
		return notificationUserInfo;
	}

	public void setNotificationUserInfo(SysUsers notificationUserInfo) {
		this.notificationUserInfo = notificationUserInfo;
	}
	
	public List<SysNotificationDetail> getSysNotificationDetailInfo() {
		return sysNotificationDetailInfo;
	}

	public void setSysNotificationDetailInfo(List<SysNotificationDetail> sysNotificationDetailInfo) {
		this.sysNotificationDetailInfo = sysNotificationDetailInfo;
	}
	
	public String getNotificationReceiveGroupIds() {
		return notificationReceiveGroupIds;
	}

	public void setNotificationReceiveGroupIds(String notificationReceiveGroupIds) {
		this.notificationReceiveGroupIds = notificationReceiveGroupIds;
	}
	
	public Integer getIsSend() {
		return isSend;
	}

	public void setIsSend(Integer isSend) {
		this.isSend = isSend;
	}

	@Override
	public String toString() {
		return "SysNotification [id=" + id + ", notificationTitle=" + notificationTitle + ", notificationMessage="
				+ notificationMessage + ", notificationCreateTime=" + notificationCreateTime
				+ ", notificationReceiveGroupIds=" + notificationReceiveGroupIds + ", isSend+"+(isSend==null||isSend==0?"no":"yes")+"]";
	}
	
	
}
