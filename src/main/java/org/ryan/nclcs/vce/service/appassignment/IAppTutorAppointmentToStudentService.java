package org.ryan.nclcs.vce.service.appassignment;

import java.util.Map;

import org.ryan.nclcs.vce.dao.Pagination;
import org.ryan.nclcs.vce.entity.AppTutorAppointmentAssignmentToStudent;
import org.ryan.nclcs.vce.service.INclcsVceServiceBaseService;

public interface IAppTutorAppointmentToStudentService extends INclcsVceServiceBaseService<AppTutorAppointmentAssignmentToStudent, Integer>{
	
	/**
	 * 查找上传已审作业数据，为学生作业管理使用，可分页
	 * @param displayLength 每页数据条数
	 * @param displayStart 数据起启位置
	 * @param sEcho
	 * @param parameters 查询条件
	 * @param userId 当前登录用户ID
	 * @return Map<String, Object>
	 */
	public Map<String, Object> searchDataForAjax(int displayLength, int displayStart, int sEcho, Map<String, Object> parameters, Integer userId);
	
	/**
	 * 查找学生下载作业数据，为学生作业管理使用，可分页
	 * @param displayLength 每页数据条数
	 * @param displayStart 数据起启位置
	 * @param sEcho
	 * @param parameters 查询条件
	 * @param userId 当前登录用户ID
	 * @return
	 */
	public Map<String, Object> searchDataForAjaxToStudent(int displayLength, int displayStart, int sEcho, Map<String, Object> parameters, Integer userId);
	
	public boolean revokeTutorToStudent(String deleteIds);
	
	//for remote interface
	public Pagination<AppTutorAppointmentAssignmentToStudent> searchDataToStudent(int displayLength, int displayStart, Map<String, Object> parameters, Integer userId);
}
