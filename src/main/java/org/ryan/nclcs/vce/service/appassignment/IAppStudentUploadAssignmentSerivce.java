package org.ryan.nclcs.vce.service.appassignment;

import java.util.List;
import java.util.Map;

import org.ryan.nclcs.vce.entity.AppStudentUploadAssignment;
import org.ryan.nclcs.vce.service.INclcsVceServiceBaseService;

public interface IAppStudentUploadAssignmentSerivce extends INclcsVceServiceBaseService<AppStudentUploadAssignment, Integer>{
	
	/**
	 * 查找学生上传作业数据，为学生作业管理使用，可分页
	 * @param displayLength 每页数据条数
	 * @param displayStart 数据起启位置
	 * @param sEcho
	 * @param parameters 查询条件
	 * @param userId 当前登录用户ID
	 * @return Map<String, Object>
	 */
	public Map<String, Object> searchDataForAjax(int displayLength, int displayStart, int sEcho, Map<String, Object> parameters, Integer userId);
	
	/**
	 * 查找学生上传作业数据，为教师作来管理使用，可分页
	 * @param displayLength 每页数据条数
	 * @param displayStart 数据起启位置
	 * @param sEcho
	 * @param parameters 查询条件
	 * @param groupIds 教师所在班级Id
	 * @return
	 */
	public Map<String, Object> searchDataForAjax(int displayLength, int displayStart, int sEcho, Map<String, Object> parameters, String groupIds);
	
	/**
	 * 查询上传过作业的学生
	 * @param tutorId 教师id
	 * @param studentId 学生id
	 * @return
	 */
	public List<Map<String, Object>> searchUploadedAssignmentStudentsByTutorId(Integer tutorId, Integer studentId);
	
	public boolean deleteMultiple(String deleteIds);
}
