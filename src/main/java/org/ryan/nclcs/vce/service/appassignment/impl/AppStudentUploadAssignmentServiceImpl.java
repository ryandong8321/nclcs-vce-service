package org.ryan.nclcs.vce.service.appassignment.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.ryan.nclcs.vce.dao.Pagination;
import org.ryan.nclcs.vce.dao.app.assignment.IAppStudentUploadAssignmentDAO;
import org.ryan.nclcs.vce.entity.AppStudentUploadAssignment;
import org.ryan.nclcs.vce.entity.SysUsers;
import org.ryan.nclcs.vce.service.NclcsVceServiceBaseServiceImpl;
import org.ryan.nclcs.vce.service.appassignment.IAppStudentUploadAssignmentSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("appStudentUploadAssignmentService")
public class AppStudentUploadAssignmentServiceImpl extends NclcsVceServiceBaseServiceImpl<AppStudentUploadAssignment, Integer, IAppStudentUploadAssignmentDAO> implements IAppStudentUploadAssignmentSerivce {

	@Autowired
	private IAppStudentUploadAssignmentDAO appStudentUploadAssignmentDAO;
	
	@Override
	protected IAppStudentUploadAssignmentDAO getCurrentDAO() {
		return this.appStudentUploadAssignmentDAO;
	}

	@Override
	public Map<String, Object> searchDataForAjax(int displayLength, int displayStart, int sEcho,
			Map<String, Object> parameters, Integer userId) {
		Map<String, Object> result=new HashMap<String, Object>();
		List<ArrayList<String>> data=new ArrayList<ArrayList<String>>();
		
		Pagination<AppStudentUploadAssignment> page=this.getCurrentDAO().searchDataForStudents(displayLength, displayStart, sEcho, parameters, userId);
		
		if (page.getRows() != null && !page.getRows().isEmpty()) {
			ArrayList<String> tmp=null;
			int idx=0;
			for (AppStudentUploadAssignment uploadAssignment : page.getRows()) {
				tmp=new ArrayList<String>();
				tmp.add("<input type='checkbox' name='id[]' value='"+uploadAssignment.getId()+"'/>");
				tmp.add(""+(++idx));
//				tmp.add("<a href=\"javascript:showUploadAssignment('"+uploadAssignment.getId()+"')\">"+uploadAssignment.getAssignmentName()+"</a>");
				tmp.add(uploadAssignment.getAssignmentName());
				tmp.add(uploadAssignment.getUploadTime().toString());
				tmp.add(uploadAssignment.getDownloadTime()==null||uploadAssignment.getDownloadTime().equals("")?"":uploadAssignment.getDownloadTime().toString());
				if (uploadAssignment.getDownloadTime()!=null&&!uploadAssignment.getDownloadTime().equals("")){
					tmp.add("");
				}else{
					tmp.add("<a href=\"javascript:deleteUploadAssignment('"+uploadAssignment.getId()+"');\" class=\"btn btn-sm red\"><i class=\"fa fa-times\"></i> REVOKE</a>");
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
	public Map<String, Object> searchDataForAjax(int displayLength, int displayStart, int sEcho,
			Map<String, Object> parameters, String groupIds) {
		Map<String, Object> result=new HashMap<String, Object>();
		List<ArrayList<String>> data=new ArrayList<ArrayList<String>>();
		
		Pagination<AppStudentUploadAssignment> page=this.getCurrentDAO().searchDataForTutors(displayLength, displayStart, sEcho, parameters, groupIds);
		
		if (page.getRows() != null && !page.getRows().isEmpty()) {
			ArrayList<String> tmp=null;
			int idx=0;
			SysUsers student=null;
			String actionButton=null;
			for (AppStudentUploadAssignment uploadAssignment : page.getRows()) {
				student=uploadAssignment.getStudent();
				tmp=new ArrayList<String>();
				tmp.add("<input type='checkbox' name='id[]' value='"+uploadAssignment.getId()+"'/>");
				tmp.add(""+(++idx));
				tmp.add("<a href=\"javascript:showStudentAssignment('"+uploadAssignment.getId()+"')\">"+uploadAssignment.getAssignmentName()+"</a>");
				tmp.add((student==null||student.getChineseName()==null)?"":student.getChineseName());
				tmp.add((student==null||student.getVceSchoolName()==null)?"":student.getVceSchoolName());
				tmp.add((student==null||student.getVceClassName()==null)?"":student.getVceClassName());
				tmp.add(uploadAssignment.getUploadTime().toString());
				tmp.add(uploadAssignment.getDownloadTime()==null||uploadAssignment.getDownloadTime().equals("")?"":uploadAssignment.getDownloadTime().toString());
				if (uploadAssignment.getHasAppointment()!=null&&uploadAssignment.getHasAppointment()==1){
					actionButton="<a href=\"javascript:showAppointmentStatus('"+uploadAssignment.getId()+"');\" class=\"btn btn-sm green\"> STATUS</a>";
					if (uploadAssignment.getAssignmentToTutor()!=null&&uploadAssignment.getAssignmentToTutor().getDownloadTime()==null){
						actionButton+="<a href=\"javascript:revokeAppointmentAssignment('"+uploadAssignment.getId()+"');\" class=\"btn btn-sm red\"><i class=\"fa fa-times\"></i> REVOKE</a>";
					}
					tmp.add(actionButton);
				}else if (uploadAssignment.getHasAppointment()!=null&&uploadAssignment.getHasAppointment()==0){
					tmp.add("<a href=\"javascript:sendToTutor('"+uploadAssignment.getId()+"');\" class=\"btn btn-sm blue\"> APPOINTMENT</a>");
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
	public List<Map<String, Object>> searchUploadedAssignmentStudentsByTutorId(Integer tutorId, Integer studentId) {
		List<Map<String, Object>> result=new ArrayList<Map<String, Object>>();
		StringBuffer hql=new StringBuffer("from AppStudentUploadAssignment sua where sua.tutor.id = ? and sua.hasAppointment=0 and sua.downloadTime is not null group by sua.student");
		List<AppStudentUploadAssignment> lst=this.getCurrentDAO().find(hql.toString(), tutorId);
		if (lst!=null&&!lst.isEmpty()){
			Map<String, Object> map=null;
			SysUsers student=null;
			for (AppStudentUploadAssignment assignment:lst){
				map=new HashMap<String, Object>();
				student=assignment.getStudent();
				map.put("id", student.getId());
				map.put("text", student.getChineseName()+"["+student.getVceSchoolName()+"|"+student.getVceClassName()+"]");
				if (studentId!=-1&&student.getId()==studentId){
					map.put("selected", "selected");
				}
				result.add(map);
			}
		}
		return result;
	}

	@Override
	public boolean deleteMultiple(String deleteIds) {
		return this.getCurrentDAO().deleteUploadAssignment(deleteIds);
	}

	@Override
	public Map<String, Object> searchDataForAPP(int displayLength, int displayStart, Map<String, Object> parameters,Integer userId) {
		Map<String, Object> result=new HashMap<String, Object>();
		List<Map<String, Object>> data=new ArrayList<Map<String, Object>>();
		
		Pagination<AppStudentUploadAssignment> page=this.getCurrentDAO().searchDataForStudents(displayLength, displayStart, 0, parameters, userId);
		
		if (page.getRows() != null && !page.getRows().isEmpty()) {
			Map<String, Object> tmp=null;
			for (AppStudentUploadAssignment uploadAssignment : page.getRows()) {
				tmp=new HashMap<String, Object>();
				tmp.put("assignmentId", uploadAssignment.getId());
				tmp.put("assignmentName",StringEscapeUtils.unescapeJavaScript(uploadAssignment.getAssignmentName()));
				tmp.put("filePath", uploadAssignment.getFilePath());
				tmp.put("fileName", uploadAssignment.getFileName());
				tmp.put("uploadTime", uploadAssignment.getUploadTime()==null?"":uploadAssignment.getUploadTime().toString());
				tmp.put("downloadTime", uploadAssignment.getDownloadTime()==null?"":uploadAssignment.getDownloadTime().toString());
				tmp.put("hasDownloaded", uploadAssignment.getDownloadTime()==null?false:true);
				data.add(tmp);
			}
		}
		
		result.put("data", data);
		result.put("recordsTotal", page.getTotal());
		result.put("recordsFiltered", "");
		return result;
	}

	@Override
	public Map<String, Object> searchDataForAPP(int displayLength, int displayStart, Map<String, Object> parameters,String groupIds) {
		Map<String, Object> result=new HashMap<String, Object>();
		List<Map<String, Object>> data=new ArrayList<Map<String, Object>>();
		
		Pagination<AppStudentUploadAssignment> page=this.getCurrentDAO().searchDataForTutors(displayLength, displayStart, 0, parameters, groupIds);
		
		if (page.getRows() != null && !page.getRows().isEmpty()) {
			Map<String, Object> tmp=null;
			SysUsers student=null;
			for (AppStudentUploadAssignment uploadAssignment : page.getRows()) {
				student=uploadAssignment.getStudent();
				tmp=new HashMap<String, Object>();
				tmp.put("assignmentId", uploadAssignment.getId());
				tmp.put("studentName", (student==null||student.getChineseName()==null)?"":student.getChineseName());
				tmp.put("studentCampus", (student==null||student.getVceSchoolName()==null)?"":student.getVceSchoolName());
				tmp.put("studentClass", (student==null||student.getVceClassName()==null)?"":student.getVceClassName());
				tmp.put("assignmentName", StringEscapeUtils.unescapeJavaScript(uploadAssignment.getAssignmentName()));
				tmp.put("filePath", uploadAssignment.getFilePath());
				tmp.put("fileName", uploadAssignment.getFileName());
				tmp.put("uploadTime", uploadAssignment.getUploadTime()==null?"":uploadAssignment.getUploadTime().toString());
				tmp.put("downloadTime", uploadAssignment.getDownloadTime()==null?"":uploadAssignment.getDownloadTime().toString());
				tmp.put("hasAppointment", (uploadAssignment.getHasAppointment()!=null&&uploadAssignment.getHasAppointment()==1)?true:false);
				data.add(tmp);
			}
		}
		
		result.put("data", data);
		result.put("recordsTotal", page.getTotal());
		result.put("recordsFiltered", "");
		return result;
	}

}
