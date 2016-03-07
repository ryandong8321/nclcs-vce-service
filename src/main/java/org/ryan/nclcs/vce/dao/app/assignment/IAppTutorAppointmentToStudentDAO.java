package org.ryan.nclcs.vce.dao.app.assignment;

import java.util.Map;

import org.ryan.nclcs.vce.dao.INclcsVceServiceBaseDAO;
import org.ryan.nclcs.vce.dao.Pagination;
import org.ryan.nclcs.vce.entity.AppTutorAppointmentAssignmentToStudent;

public interface IAppTutorAppointmentToStudentDAO extends INclcsVceServiceBaseDAO<AppTutorAppointmentAssignmentToStudent, Integer>{
	
	public Pagination<AppTutorAppointmentAssignmentToStudent> searchDataForAppointmentStudent(int displayLength, int displayStart, int sEcho, Map<String, Object> parameters, Integer userId);
	
	public Pagination<AppTutorAppointmentAssignmentToStudent> searchDataForAppointmentStudentForStudent(int displayLength, int displayStart, int sEcho, Map<String, Object> parameters, Integer userId);
	
	public boolean deleteUploadAssignment(String deleteIds);

}
