package org.ryan.nclcs.vce.service.appassignment;

import java.util.List;
import java.util.Map;

import org.ryan.nclcs.vce.entity.AppTutorAppointmentAssignmentToTutor;
import org.ryan.nclcs.vce.service.INclcsVceServiceBaseService;

public interface IAppTutorAppointmentToTutorService extends INclcsVceServiceBaseService<AppTutorAppointmentAssignmentToTutor, Integer>{
	
	/**
	 * 查找下载代审作业数据，为学生作业管理使用，可分页
	 * @param displayLength 每页数据条数
	 * @param displayStart 数据起启位置
	 * @param sEcho
	 * @param parameters 查询条件
	 * @param userId 当前登录用户ID
	 * @return Map<String, Object>
	 */
	public Map<String, Object> searchDataForAjax(int displayLength, int displayStart, int sEcho, Map<String, Object> parameters, Integer userId);
	
	/**
	 * 为APP提供数据，查找下载代审作业数据，为学生作业管理使用，可分页
	 * @param displayLength
	 * @param displayStart
	 * @param parameters
	 * @param userId
	 * @return
	 */
	public Map<String, Object> searchDataForAPP(int displayLength, int displayStart, Map<String, Object> parameters, Integer userId);
	
	/**
	 * 查找上传代审作业数据，为学生作业管理使用，可分页
	 * @param displayLength
	 * @param displayStart
	 * @param sEcho
	 * @param parameters
	 * @param tutorId
	 * @return
	 */
	public Map<String, Object> searchDataForAjaxToTutor(int displayLength, int displayStart, int sEcho, Map<String, Object> parameters, Integer tutorId);
	
	/**
	 * 为APP提供数据，查找上传代审作业数据，为学生作业管理使用，可分页
	 * @param displayLength
	 * @param displayStart
	 * @param sEcho
	 * @param parameters
	 * @param tutorId
	 * @return
	 */
	public Map<String, Object> searchDataForAPPToTutor(int displayLength, int displayStart, Map<String, Object> parameters, Integer tutorId);
	
	
	public List<Map<String, Object>> findTutorsForTutorToTutor(Integer currentUserId, Integer targetTutorId);
	
	/**
	 * 收回老师指派给老师的作业(学生提交的)
	 * @param studentUploadAssignmentId 学生上传作业记录ID;
	 * @return
	 */
	public boolean revokeTutorToTutorFromStudent(Integer studentUploadAssignmentId);
	
	/**
	 * 收回老师指派给老师的作业(教师提交的)
	 * @param deletedIds 教师指派给教师的作业记录ID;
	 * @return
	 */
	public boolean revokeTutorToTutor(String deletedIds);
}
