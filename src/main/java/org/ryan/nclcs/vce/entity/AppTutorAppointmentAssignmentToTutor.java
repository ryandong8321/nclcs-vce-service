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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

@SuppressWarnings("serial")
@Entity
@Table(name = "app_tutor_appointment_assignment_tutor", catalog = "nclcs_vce_management")
@DynamicInsert(true)
public class AppTutorAppointmentAssignmentToTutor implements Serializable{

	/**
	 * 编号
	 */
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "appointment_tutor_id", unique = true, nullable = false)
	private Integer id;
	
	/**
	 * 上传作业学生
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="appointment_tutor_student_id")
	private SysUsers student;
	
	/**
	 * 学生班级老师(学生指定的老师)
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="appointment_tutor_original_tutor_id")
	private SysUsers originalTutor;
	
	/**
	 * 老师指定代工老师
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="appointment_tutor_target_tutor_id")
	private SysUsers targetTutor;
	
	/**
	 * 作业名称
	 */
	@Column(name = "appointment_tutor_assignment_name", nullable = true, length=128)
	private String assignmentName;
	
	/**
	 * 学生指定老师指派作业时间
	 */
	@Column(name = "appointment_tutor_appointment_time", nullable = false)
	private Date uploadTime;
	
	/**
	 * 被指派老师下载作业时间
	 */
	@Column(name = "appointment_tutor_download_time", nullable = true)
	private Date downloadTime;
	
	/**
	 * 作业存放地址
	 */
	@Column(name = "appointment_tutor_assignment_path", nullable = false, length=128)
	private String filePath;
	
	/**
	 * 作业文件名
	 */
	@Column(name = "appointment_tutor_assignment_file_name", nullable = false, length=128)
	private String fileName;
	
	/**
	 * appointment_tutor_assignment_original_upload_id
	 */
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="appointment_tutor_assignment_original_upload_id")
	private AppStudentUploadAssignment uploadAssignment;

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

	public SysUsers getOriginalTutor() {
		return originalTutor;
	}

	public void setOriginalTutor(SysUsers originalTutor) {
		this.originalTutor = originalTutor;
	}

	public SysUsers getTargetTutor() {
		return targetTutor;
	}

	public void setTargetTutor(SysUsers targetTutor) {
		this.targetTutor = targetTutor;
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

	public AppStudentUploadAssignment getUploadAssignment() {
		return uploadAssignment;
	}

	public void setUploadAssignment(AppStudentUploadAssignment uploadAssignment) {
		this.uploadAssignment = uploadAssignment;
	}
}
