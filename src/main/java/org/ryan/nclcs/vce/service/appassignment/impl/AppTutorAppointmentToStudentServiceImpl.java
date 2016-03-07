package org.ryan.nclcs.vce.service.appassignment.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ryan.nclcs.vce.dao.Pagination;
import org.ryan.nclcs.vce.dao.app.assignment.IAppTutorAppointmentToStudentDAO;
import org.ryan.nclcs.vce.entity.AppTutorAppointmentAssignmentToStudent;
import org.ryan.nclcs.vce.service.NclcsVceServiceBaseServiceImpl;
import org.ryan.nclcs.vce.service.appassignment.IAppTutorAppointmentToStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("appTutorAppointmentToStudentService")
public class AppTutorAppointmentToStudentServiceImpl extends NclcsVceServiceBaseServiceImpl<AppTutorAppointmentAssignmentToStudent, Integer, IAppTutorAppointmentToStudentDAO> implements IAppTutorAppointmentToStudentService {

	@Autowired
	private IAppTutorAppointmentToStudentDAO appTutorAppointmentToStudentDAO;
	
	@Override
	protected IAppTutorAppointmentToStudentDAO getCurrentDAO() {
		return this.appTutorAppointmentToStudentDAO;
	}
	
	@Override
	public Map<String, Object> searchDataForAjax(int displayLength, int displayStart, int sEcho,
			Map<String, Object> parameters, Integer userId) {
		Map<String, Object> result=new HashMap<String, Object>();
		List<ArrayList<String>> data=new ArrayList<ArrayList<String>>();
		
		Pagination<AppTutorAppointmentAssignmentToStudent> page=this.getCurrentDAO().searchDataForAppointmentStudent(displayLength, displayStart, sEcho, parameters, userId);
		
		if (page.getRows() != null && !page.getRows().isEmpty()) {
			ArrayList<String> tmp=null;
			int idx=0;
			for (AppTutorAppointmentAssignmentToStudent toStudent : page.getRows()) {
				tmp=new ArrayList<String>();
				tmp.add("<input type='checkbox' name='id[]' value='"+toStudent.getId()+"'/>");
				tmp.add(""+(++idx));
//				tmp.add("<a href=\"javascript:showUploadAssignment('"+toStudent.getId()+"')\">"+toStudent.getAssignmentName()+"</a>");
				tmp.add(toStudent.getAssignmentName());
				tmp.add(toStudent.getTargetStudent().getChineseName());
				tmp.add(toStudent.getUploadTime().toString());
				tmp.add(toStudent.getDownloadTime()==null||toStudent.getDownloadTime().equals("")?"":toStudent.getDownloadTime().toString());
				if (toStudent.getDownloadTime()!=null&&!toStudent.getDownloadTime().equals("")){
					tmp.add("");
				}else{
					tmp.add("<a href=\"javascript:deleteUploadAssignment('"+toStudent.getId()+"');\" class=\"btn btn-sm red\"><i class=\"fa fa-times\"></i> REVOKE</a>");
				}
				data.add(tmp);
			}
		}
		
		result.put("data", data);
		result.put("recordsTotal", page.getTotal());
		result.put("recordsFiltered", "");
		return result;
	}

	@Override
	public Map<String, Object> searchDataForAjaxToStudent(int displayLength, int displayStart, int sEcho,
			Map<String, Object> parameters, Integer userId) {
		Map<String, Object> result=new HashMap<String, Object>();
		List<ArrayList<String>> data=new ArrayList<ArrayList<String>>();
		
		Pagination<AppTutorAppointmentAssignmentToStudent> page=this.getCurrentDAO().searchDataForAppointmentStudentForStudent(displayLength, displayStart, sEcho, parameters, userId);
		
		if (page.getRows() != null && !page.getRows().isEmpty()) {
			ArrayList<String> tmp=null;
			int idx=0;
			for (AppTutorAppointmentAssignmentToStudent toStudent : page.getRows()) {
				tmp=new ArrayList<String>();
				tmp.add("<input type='checkbox' name='id[]' value='"+toStudent.getId()+"'/>");
				tmp.add(""+(++idx));
				tmp.add("<a href=\"javascript:showStudentAssignment('"+toStudent.getId()+"')\">"+toStudent.getAssignmentName()+"</a>");
				tmp.add(toStudent.getUploadTime().toString());
				tmp.add(toStudent.getDownloadTime()==null||toStudent.getDownloadTime().equals("")?"":toStudent.getDownloadTime().toString());
//				if (toStudent.getDownloadTime()!=null&&!toStudent.getDownloadTime().equals("")){
//					tmp.add("");
//				}else{
//					tmp.add("<a href=\"javascript:deleteUploadAssignment('"+toStudent.getId()+"');\" class=\"btn btn-sm red\"><i class=\"fa fa-times\"></i> DELETE</a>");
//				}
				tmp.add("");
				data.add(tmp);
			}
		}
		
		result.put("data", data);
		result.put("recordsTotal", page.getTotal());
		result.put("recordsFiltered", "");
		return result;
	}

	@Override
	public boolean revokeTutorToStudent(String deleteIds) {
		return this.getCurrentDAO().deleteUploadAssignment(deleteIds);
	}

	@Override
	public Pagination<AppTutorAppointmentAssignmentToStudent> searchDataToStudent(int displayLength, int displayStart,Map<String, Object> parameters, Integer userId) {
		return this.getCurrentDAO().searchDataForAppointmentStudentForStudent(displayLength, displayStart, 0,parameters, userId);
	}

}
