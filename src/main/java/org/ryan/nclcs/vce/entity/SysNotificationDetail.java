package org.ryan.nclcs.vce.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

@SuppressWarnings("serial")
@Entity
@Table(name = "sys_notification_detail", catalog = "nclcs_vce_management")
@DynamicInsert(true)
public class SysNotificationDetail implements Serializable{
	
	/**
	 * 编号
	 */
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "detail_id", unique = true, nullable = false)
	private Integer id;
	
	/**
	 * notification_id
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="detail_notification_id")
	private SysNotification detailNotificationInfo;
	
	/**
	 * detail_receive_user_id
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="detail_receive_user_id")
	private SysUsers detailReceiveUserInfo;
	
	/**
	 * 是否阅读(0-未阅读,1-已阅读)
	 */
	@Column(name = "detail_is_read", nullable = false)
	private Integer isRead;
	
	/**
	 * 发送时间
	 */
	@Column(name = "detail_send_time", nullable = true)
	private Date sendTime;
	
	/**
	 * 阅读时间
	 */
	@Column(name = "detail_read_time", nullable = true)
	private Date readTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SysNotification getDetailNotificationInfo() {
		return detailNotificationInfo;
	}

	public void setDetailNotificationInfo(SysNotification detailNotificationInfo) {
		this.detailNotificationInfo = detailNotificationInfo;
	}

	public SysUsers getDetailReceiveUserInfo() {
		return detailReceiveUserInfo;
	}

	public void setDetailReceiveUserInfo(SysUsers detailReceiveUserInfo) {
		this.detailReceiveUserInfo = detailReceiveUserInfo;
	}

	public Integer getIsRead() {
		return isRead;
	}

	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Date getReadTime() {
		return readTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}

	@Override
	public String toString() {
		return "SysNotificationDetail [id=" + id + ", isRead=" + isRead + ", sendTime="
				+ sendTime + ", readTime=" + readTime + "]";
	}
}
