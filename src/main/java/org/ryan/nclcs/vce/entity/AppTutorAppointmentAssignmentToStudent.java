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
@Table(name = "app_tutor_appointment_assignment_student", catalog = "nclcs_vce_management")
@DynamicInsert(true)
public class AppTutorAppointmentAssignmentToStudent implements Serializable{
	
	/**
	 * 编号
	 */
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "appointment_student_id", unique = true, nullable = false)
	private Integer id;
	
	/**
	 * 上传作业老师
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="appointment_student_tutor_id")
	private SysUsers sourceTutor;
	
	/**
	 * 指定接收作业的学生
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="appointment_student_target_student_id")
	private SysUsers targetStudent;
	
	/**
	 * 作业名称
	 */
	@Column(name = "appointment_student_assignment_name", nullable = true, length=128)
	private String assignmentName;
	
	/**
	 * 老师上传作业时间
	 */
	@Column(name = "appointment_student_upload_time", nullable = false)
	private Date uploadTime;
	
	/**
	 * 学生下载作业时间
	 */
	@Column(name = "appointment_student_download_time", nullable = true)
	private Date downloadTime;
	
	/**
	 * 作业存放地址
	 */
	@Column(name = "appointment_student_assginment_path", nullable = false, length=128)
	private String filePath;
	
	/**
	 * 作业文件名
	 */
	@Column(name = "appointment_student_assginment_file_name", nullable = false, length=128)
	private String fileName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SysUsers getSourceTutor() {
		return sourceTutor;
	}

	public void setSourceTutor(SysUsers sourceTutor) {
		this.sourceTutor = sourceTutor;
	}

	public SysUsers getTargetStudent() {
		return targetStudent;
	}

	public void setTargetStudent(SysUsers targetStudent) {
		this.targetStudent = targetStudent;
	}

	public String getAssignmentName() {
		return assignmentName;
	}

	public void setAssignmentName(String assignmentName) {
		this.assignmentName = assignmentName;
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
	
}
