package org.ryan.nclcs.vce.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@SuppressWarnings("serial")
@Entity
@Table(name = "app_student_upload_assignment", catalog = "nclcs_vce_management")
@DynamicInsert(true)
public class AppStudentUploadAssignment implements Serializable{

	/**
	 * 编号
	 */
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "upload_assignment_id", unique = true, nullable = false)
	private Integer id;
	
	/**
	 * 上传作业学生
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="upload_assignment_student_id")
	private SysUsers student;
	
	/**
	 * 作业名称
	 */
	@Column(name = "upload_assignment_name", nullable = true, length=128)
	private String assignmentName;
	
	/**
	 * 指定接收老师
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="upload_assignment_tutor_id")
	private SysUsers tutor;
	
	/**
	 * 学生上传作业时间
	 */
	@Column(name = "upload_assignment_upload_time", nullable = false)
	private Date uploadTime;
	
	/**
	 * 老师下载作业时间
	 */
	@Column(name = "upload_assignment_download_time", nullable = true)
	private Date downloadTime;
	
	/**
	 * 是否显示记录(0-不显示,1-显示)
	 */
	@Column(name = "upload_assignment_is_show", nullable = true)
	private Integer isShow;
	
	/**
	 * 作业存放地址
	 */
	@Column(name = "upload_assignment_path", nullable = false, length=128)
	private String filePath;
	
	
	/**
	 * 作业文件名
	 */
	@Column(name = "upload_assignment_file_name", nullable = false, length=128)
	private String fileName;
	
	
	/**
	 * 是否指派(0-未指派,1-已指派)
	 */
	@Column(name = "upload_assignment_has_appointment", nullable = true)
	private Integer hasAppointment;
	
	/**
	 * appointment_tutor_assignment_original_upload_id
	 */
	@OneToOne(mappedBy="uploadAssignment", cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.EXTRA)
	private AppTutorAppointmentAssignmentToTutor assignmentToTutor;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SysUsers getStudent() {
		return student;
	}

	public void setStudent(SysUsers student) {
		this.student = student;
	}

	public String getAssignmentName() {
		return assignmentName;
	}

	public void setAssignmentName(String assignmentName) {
		this.assignmentName = assignmentName;
	}

	public SysUsers getTutor() {
		return tutor;
	}

	public void setTutor(SysUsers tutor) {
		this.tutor = tutor;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public Date getDownloadTime() {
		return downloadTime;
	}

	public void setDownloadTime(Date downloadTime) {
		this.downloadTime = downloadTime;
	}

	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public Integer getHasAppointment() {
		return hasAppointment;
	}

	public void setHasAppointment(Integer hasAppointment) {
		this.hasAppointment = hasAppointment;
	}

	public AppTutorAppointmentAssignmentToTutor getAssignmentToTutor() {
		return assignmentToTutor;
	}

	public void setAssignmentToTutor(AppTutorAppointmentAssignmentToTutor assignmentToTutor) {
		this.assignmentToTutor = assignmentToTutor;
	}

	@Override
	public String toString() {
		return "AppStudentUploadAssignment [id=" + id + ", assignmentName=" + assignmentName + ", uploadTime="
				+ uploadTime + ", downloadTime=" + downloadTime + ", filePath=" + filePath + ", fileName=" + fileName
				+ ", hasAppointment=" + hasAppointment + "]";
	}
}
