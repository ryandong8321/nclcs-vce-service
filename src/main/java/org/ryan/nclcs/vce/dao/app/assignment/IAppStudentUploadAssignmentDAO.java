package org.ryan.nclcs.vce.dao.app.assignment;

import java.util.Map;

import org.ryan.nclcs.vce.dao.INclcsVceServiceBaseDAO;
import org.ryan.nclcs.vce.dao.Pagination;
import org.ryan.nclcs.vce.entity.AppStudentUploadAssignment;

public interface IAppStudentUploadAssignmentDAO extends INclcsVceServiceBaseDAO<AppStudentUploadAssignment, Integer>{
	
	/**
	 * 查找学生上传作业数据，为学生作业管理使用，可分页
	 * @param displayLength 每页数据条数
	 * @param displayStart 数据起启位置
	 * @param sEcho
	 * @param parameters 查询条件
	 * @param userId 当前登录用户ID
	 * @return
	 */
	public Pagination<AppStudentUploadAssignment> searchDataForStudents(int displayLength, int displayStart, int sEcho, Map<String, Object> parameters, Integer userId);
	
	public Pagination<AppStudentUploadAssignment> searchDataForTutors(int displayLength, int displayStart, int sEcho, Map<String, Object> parameters, String groupIds);
	
	public boolean deleteUploadAssignment(String deleteIds);

}
