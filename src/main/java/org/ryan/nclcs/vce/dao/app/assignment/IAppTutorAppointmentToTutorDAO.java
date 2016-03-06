package org.ryan.nclcs.vce.dao.app.assignment;

import java.util.Map;

import org.ryan.nclcs.vce.dao.INclcsVceServiceBaseDAO;
import org.ryan.nclcs.vce.dao.Pagination;
import org.ryan.nclcs.vce.entity.AppTutorAppointmentAssignmentToTutor;

public interface IAppTutorAppointmentToTutorDAO extends INclcsVceServiceBaseDAO<AppTutorAppointmentAssignmentToTutor, Integer>{
	
	public Pagination<AppTutorAppointmentAssignmentToTutor> searchDataForAppointmentTutor(int displayLength, int displayStart, int sEcho, Map<String, Object> parameters, Integer userId);
	
	public Pagination<AppTutorAppointmentAssignmentToTutor> searchDataForTutorAppointmentTutor(int displayLength, int displayStart, int sEcho, Map<String, Object> parameters, Integer tutorId);
	
	/**
	 * 收回老师指派给老师的作业(学生提交的)
	 * @param studentUploadAssignmentId 学生上传作业记录ID;
	 * @return
	 * @throws Exception 
	 */
	public void deleteTutorToTutorFromStudent(Integer studentUploadAssignmentId) throws Exception;
	
	/**
	 * 收回老师指派给老师的作业(教师提交的)
	 * @param deletedIds 教师指派给教师的作业记录ID;
	 * @return
	 */
	public boolean deleteTutorToTutor(String deletedIds);

}
