package org.ryan.nclcs.vce.service.appassignment.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.ryan.nclcs.vce.dao.Pagination;
import org.ryan.nclcs.vce.dao.app.assignment.IAppTutorAppointmentToTutorDAO;
import org.ryan.nclcs.vce.entity.AppTutorAppointmentAssignmentToTutor;
import org.ryan.nclcs.vce.entity.SysUsers;
import org.ryan.nclcs.vce.service.NclcsVceServiceBaseServiceImpl;
import org.ryan.nclcs.vce.service.appassignment.IAppTutorAppointmentToTutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("appTutorAppointmentToTutorService")
public class AppTutorAppointmentToTutorServiceImpl extends NclcsVceServiceBaseServiceImpl<AppTutorAppointmentAssignmentToTutor, Integer, IAppTutorAppointmentToTutorDAO> implements IAppTutorAppointmentToTutorService {

	@Autowired
	private IAppTutorAppointmentToTutorDAO appTutorAppointmentToTutorDAO;
	
	@Override
	protected IAppTutorAppointmentToTutorDAO getCurrentDAO() {
		return this.appTutorAppointmentToTutorDAO;
	}

	@Override
	public Map<String, Object> searchDataForAjax(int displayLength, int displayStart, int sEcho,
			Map<String, Object> parameters, Integer userId) {
		Map<String, Object> result=new HashMap<String, Object>();
		List<ArrayList<String>> data=new ArrayList<ArrayList<String>>();
		
		Pagination<AppTutorAppointmentAssignmentToTutor> page=this.getCurrentDAO().searchDataForAppointmentTutor(displayLength, displayStart, sEcho, parameters, userId);
		
		if (page.getRows() != null && !page.getRows().isEmpty()) {
			ArrayList<String> tmp=null;
			int idx=0;
			for (AppTutorAppointmentAssignmentToTutor toTutor : page.getRows()) {
				tmp=new ArrayList<String>();
				tmp.add("<input type='checkbox' name='id[]' value='"+toTutor.getId()+"'/>");
				tmp.add(""+(++idx));
				if (toTutor.getUploadAssignment()==null){
					tmp.add("<a href=\"javascript:showTutorAppointmentAssignment('"+toTutor.getId()+"')\">"+toTutor.getAssignmentName()+"</a>");
				}else{
					tmp.add("<a href=\"javascript:showStudentAssignment('"+toTutor.getUploadAssignment().getId()+"')\">"+toTutor.getAssignmentName()+"</a>");
				}
				tmp.add(toTutor.getStudent()==null?"":toTutor.getStudent().getChineseName());
				tmp.add(toTutor.getOriginalTutor().getChineseName());
				tmp.add(toTutor.getUploadTime().toString());
				tmp.add(toTutor.getDownloadTime()==null||toTutor.getDownloadTime().equals("")?"":toTutor.getDownloadTime().toString());
//				if (toTutor.getDownloadTime()!=null&&!toTutor.getDownloadTime().equals("")){
					tmp.add("");
//				}else{
//					tmp.add("<a href=\"javascript:deleteUploadAssignment('"+toTutor.getId()+"');\" class=\"btn btn-sm red\"><i class=\"fa fa-times\"></i> DELETE</a>");
//				}
				data.add(tmp);
			}
		}
		
		result.put("data", data);
		result.put("recordsTotal", page.getTotal());
		result.put("recordsFiltered", "");
		return result;
	}
	
	@Override
	public Map<String, Object> searchDataForAPP(int displayLength, int displayStart, Map<String, Object> parameters, Integer userId){
		Map<String, Object> result=new HashMap<String, Object>();
		List<Map<String, Object>> data=new ArrayList<Map<String, Object>>();
		
		Pagination<AppTutorAppointmentAssignmentToTutor> page=this.getCurrentDAO().searchDataForAppointmentTutor(displayLength, displayStart, 0, parameters, userId);
		
		if (page.getRows() != null && !page.getRows().isEmpty()) {
			Map<String, Object> tmp=null;
			for (AppTutorAppointmentAssignmentToTutor toTutor : page.getRows()) {
				tmp=new HashMap<String, Object>();
//				tmp.put("assignmentId", toTutor.getUploadAssignment()==null?toTutor.getId():toTutor.getUploadAssignment().getId());
				tmp.put("assignmentId", toTutor.getId());
				tmp.put("assignmentName", StringEscapeUtils.unescapeJavaScript(toTutor.getAssignmentName()));
				tmp.put("filePath", toTutor.getFilePath());
				tmp.put("fileName", toTutor.getFileName());
				tmp.put("studentName", toTutor.getStudent()==null?"":toTutor.getStudent().getChineseName());
				tmp.put("originalTutor", toTutor.getOriginalTutor()==null?"":toTutor.getOriginalTutor().getChineseName());
				tmp.put("uploadTime", toTutor.getUploadTime()==null?"":toTutor.getUploadTime().toString());
				tmp.put("downloadTime", toTutor.getDownloadTime()==null?"":toTutor.getDownloadTime().toString());
				data.add(tmp);
			}
		}
		result.put("data", data);
		result.put("recordsTotal", page.getTotal());
		result.put("recordsFiltered", "");
		return result;
	}
	

	@Override
	public Map<String, Object> searchDataForAjaxToTutor(int displayLength, int displayStart, int sEcho,
			Map<String, Object> parameters, Integer tutorId) {
		Map<String, Object> result=new HashMap<String, Object>();
		List<ArrayList<String>> data=new ArrayList<ArrayList<String>>();
		
		Pagination<AppTutorAppointmentAssignmentToTutor> page=this.getCurrentDAO().searchDataForTutorAppointmentTutor(displayLength, displayStart, sEcho, parameters, tutorId);
		
		if (page.getRows() != null && !page.getRows().isEmpty()) {
			ArrayList<String> tmp=null;
			int idx=0;
			for (AppTutorAppointmentAssignmentToTutor toTutor : page.getRows()) {
				tmp=new ArrayList<String>();
				tmp.add("<input type='checkbox' name='id[]' value='"+toTutor.getId()+"'/>");
				tmp.add(""+(++idx));
//				tmp.add("<a href=\"javascript:showStudentAssignment('"+toTutor.getId()+"')\">"+toTutor.getAssignmentName()+"</a>");
				tmp.add(toTutor.getAssignmentName());
				tmp.add(toTutor.getTargetTutor().getChineseName());
				tmp.add(toTutor.getUploadTime().toString());
				tmp.add(toTutor.getDownloadTime()==null||toTutor.getDownloadTime().equals("")?"":toTutor.getDownloadTime().toString());
				if (toTutor.getDownloadTime()!=null&&!toTutor.getDownloadTime().equals("")){
					tmp.add("");
				}else{
					tmp.add("<a href=\"javascript:deleteUploadAssignment('"+toTutor.getId()+"');\" class=\"btn btn-sm red\"><i class=\"fa fa-times\"></i> REVOKE</a>");
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
	public Map<String, Object> searchDataForAPPToTutor(int displayLength, int displayStart, Map<String, Object> parameters, Integer tutorId){
		Map<String, Object> result=new HashMap<String, Object>();
		List<Map<String,Object>> data=new ArrayList<Map<String,Object>>();
		
		int sEcho=0;
		
		Pagination<AppTutorAppointmentAssignmentToTutor> page=this.getCurrentDAO().searchDataForTutorAppointmentTutor(displayLength, displayStart, sEcho, parameters, tutorId);
		
		if (page.getRows() != null && !page.getRows().isEmpty()) {
			Map<String,Object> tmp=null;
			for (AppTutorAppointmentAssignmentToTutor toTutor : page.getRows()) {
				tmp=new HashMap<String,Object>();
				tmp.put("assignmentId", toTutor.getId());
				tmp.put("assignmentName", StringEscapeUtils.unescapeJavaScript(toTutor.getAssignmentName()));
				tmp.put("toTutorName", toTutor.getTargetTutor()==null?"":toTutor.getTargetTutor().getChineseName());
				tmp.put("uploadName", toTutor.getUploadTime()==null?"":toTutor.getUploadTime().toString());
				tmp.put("downloadName", toTutor.getDownloadTime()==null?"":toTutor.getDownloadTime().toString());
				tmp.put("canRevoke", toTutor.getDownloadTime()==null?true:false);
				data.add(tmp);
			}
		}
		
		result.put("data", data);
		result.put("recordsTotal", page.getTotal());
		result.put("recordsFiltered", "");
		return result;
	}

	@Override
	public List<Map<String, Object>> findTutorsForTutorToTutor(Integer currentUserId, Integer targetTutorId) {
		List<Map<String, Object>> result=new ArrayList<Map<String, Object>>();
		StringBuffer hql=new StringBuffer("from AppTutorAppointmentAssignmentToTutor taatt where taatt.targetTutor.id = ? and taatt.uploadAssignment is not null group by taatt.originalTutor");
		List<AppTutorAppointmentAssignmentToTutor> lst=this.getCurrentDAO().find(hql.toString(), currentUserId);
		if (lst!=null&&!lst.isEmpty()){
			Map<String, Object> map=null;
			SysUsers originalTutor=null;
			for (AppTutorAppointmentAssignmentToTutor assignment:lst){
				map=new HashMap<String, Object>();
				originalTutor=assignment.getOriginalTutor();
				map.put("id", originalTutor.getId());
				map.put("text", originalTutor.getChineseName()+"["+originalTutor.getEmailAddress()+"]");
				if (targetTutorId!=-1&&originalTutor.getId()==targetTutorId){
					map.put("selected", "selected");
				}
				result.add(map);
			}
		}
		return result;
	}

	@Override
	public boolean revokeTutorToTutorFromStudent(Integer studentUploadAssignmentId) {
		boolean result=true;
		try {
			this.getCurrentDAO().deleteTutorToTutorFromStudent(studentUploadAssignmentId);
		} catch (Exception e) {
			result=false;
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean revokeTutorToTutor(String deletedIds) {
		return this.getCurrentDAO().deleteTutorToTutor(deletedIds);
	}

}
