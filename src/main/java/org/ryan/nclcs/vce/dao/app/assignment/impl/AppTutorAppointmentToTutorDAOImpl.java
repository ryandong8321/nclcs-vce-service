package org.ryan.nclcs.vce.dao.app.assignment.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ryan.nclcs.vce.dao.NclcsVceServiceBaseDAOImpl;
import org.ryan.nclcs.vce.dao.Pagination;
import org.ryan.nclcs.vce.dao.app.assignment.IAppStudentUploadAssignmentDAO;
import org.ryan.nclcs.vce.dao.app.assignment.IAppTutorAppointmentToTutorDAO;
import org.ryan.nclcs.vce.entity.AppStudentUploadAssignment;
import org.ryan.nclcs.vce.entity.AppTutorAppointmentAssignmentToTutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("appTutorAppointmentToTutorDAO")
public class AppTutorAppointmentToTutorDAOImpl extends NclcsVceServiceBaseDAOImpl<AppTutorAppointmentAssignmentToTutor, Integer> implements IAppTutorAppointmentToTutorDAO {
	
	@Autowired
	private IAppStudentUploadAssignmentDAO appStudentUploadAssignmentDAO;

	@Override
	public Pagination<AppTutorAppointmentAssignmentToTutor> searchDataForAppointmentTutor(int displayLength,
			int displayStart, int sEcho, Map<String, Object> parameters, Integer userId) {
		List<Object> param=new ArrayList<Object>();
		StringBuffer hql=new StringBuffer("from AppTutorAppointmentAssignmentToTutor taatt where taatt.targetTutor.id = ?");
		param.add(userId);
		
		if (parameters.containsKey("assignmentName")){
			hql.append(" and taatt.assignmentName like ?");
			param.add("%"+parameters.get("assignmentName")+"%");
		}
		
		if (parameters.containsKey("startTime")&&parameters.containsKey("endTime")){
			hql.append(" and (taatt.uploadTime between timestamp(?,'yyyy-MM-dd HH:mi:ss') and timestamp(?,'yyyy-MM-dd HH:mi:ss'))");
			param.add(parameters.get("startTime"));
			param.add(parameters.get("endTime"));
		}else if (parameters.containsKey("startTime")){
			hql.append(" and taatt.uploadTime > timestamp(?,'yyyy-MM-dd HH:mi:ss')");
			param.add(parameters.get("startTime"));
		}else if (parameters.containsKey("endTime")){
			hql.append(" and taatt.uploadTime < timestamp(?,'yyyy-MM-dd HH:mi:ss')");
			param.add(parameters.get("endTime"));
		}
		
		if (parameters.containsKey("originalTutor.chineseName")){
			hql.append(" and taatt.originalTutor.chineseName like ?");
			param.add("%"+parameters.get("originalTutor.chineseName")+"%");
		}
		
		if (parameters.containsKey("student.chineseName")){
			hql.append(" and taatt.student.chineseName like ?");
			param.add("%"+parameters.get("student.chineseName")+"%");
		}
		
		if (parameters.containsKey("sort")){
			hql.append(" order by taatt.");
			hql.append(getSortColumn(Integer.parseInt(""+parameters.get("sort"))));
		}
		if (parameters.containsKey("order")){
			hql.append(" ");
			hql.append(parameters.get("order"));
		}
		Pagination<AppTutorAppointmentAssignmentToTutor> page=new Pagination<AppTutorAppointmentAssignmentToTutor>();
		page.setFirst(displayStart);
		page.setPageSize(displayLength);
		
		page=this.findPage(page, hql.toString(), param.toArray());
		return page;
	}
	
	private String getSortColumn(int index){
		List<String> lstColumn=new ArrayList<String>();
		lstColumn.add("id");
		lstColumn.add("assignmentName");
		lstColumn.add("student.chineseName");
		lstColumn.add("originalTutor.chineseName");
		lstColumn.add("uploadTime");
		lstColumn.add("downloadTime");		
		return lstColumn.get(index-1);
	}

	@Override
	public Pagination<AppTutorAppointmentAssignmentToTutor> searchDataForTutorAppointmentTutor(int displayLength,
			int displayStart, int sEcho, Map<String, Object> parameters, Integer tutorId) {
		List<Object> param=new ArrayList<Object>();
		StringBuffer hql=new StringBuffer("from AppTutorAppointmentAssignmentToTutor taatt where taatt.originalTutor.id = ? and taatt.uploadAssignment is null");
		param.add(tutorId);
		
		if (parameters.containsKey("assignmentName")){
			hql.append(" and taatt.assignmentName like ?");
			param.add("%"+parameters.get("assignmentName")+"%");
		}
		
		if (parameters.containsKey("startTime")&&parameters.containsKey("endTime")){
			hql.append(" and (taatt.uploadTime between timestamp(?,'yyyy-MM-dd HH:mi:ss') and timestamp(?,'yyyy-MM-dd HH:mi:ss'))");
			param.add(parameters.get("startTime"));
			param.add(parameters.get("endTime"));
		}else if (parameters.containsKey("startTime")){
			hql.append(" and taatt.uploadTime > timestamp(?,'yyyy-MM-dd HH:mi:ss')");
			param.add(parameters.get("startTime"));
		}else if (parameters.containsKey("endTime")){
			hql.append(" and taatt.uploadTime < timestamp(?,'yyyy-MM-dd HH:mi:ss')");
			param.add(parameters.get("endTime"));
		}
		
		if (parameters.containsKey("targetTutor.chineseName")){
			hql.append(" and taatt.targetTutor.chineseName like ?");
			param.add("%"+parameters.get("targetTutor.chineseName")+"%");
		}
		
		if (parameters.containsKey("sort")){
			hql.append(" order by taatt.");
			hql.append(getSortColumnForTutorToTutor(Integer.parseInt(""+parameters.get("sort"))));
		}
		if (parameters.containsKey("order")){
			hql.append(" ");
			hql.append(parameters.get("order"));
		}
		Pagination<AppTutorAppointmentAssignmentToTutor> page=new Pagination<AppTutorAppointmentAssignmentToTutor>();
		page.setFirst(displayStart);
		page.setPageSize(displayLength);
		
		page=this.findPage(page, hql.toString(), param.toArray());
		return page;
	}
	
	private String getSortColumnForTutorToTutor(int index){
		List<String> lstColumn=new ArrayList<String>();
		lstColumn.add("id");
		lstColumn.add("assignmentName");
		lstColumn.add("targetTutor.chineseName");
		lstColumn.add("uploadTime");
		lstColumn.add("downloadTime");		
		return lstColumn.get(index-1);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteTutorToTutorFromStudent(Integer studentUploadAssignmentId) throws Exception {
		AppStudentUploadAssignment assignment=appStudentUploadAssignmentDAO.get(studentUploadAssignmentId);
		AppTutorAppointmentAssignmentToTutor toTutor=assignment.getAssignmentToTutor();
		this.delete(toTutor);
		assignment.setHasAppointment(0);
		assignment.setAssignmentToTutor(null);
		this.appStudentUploadAssignmentDAO.update(assignment);
	}

	@Override
	public boolean deleteTutorToTutor(String deleteIds) {
		boolean result=false;
		String[] ids=null;
		if (deleteIds!=null&&!deleteIds.equals("")&&deleteIds.contains(",")){
			ids=deleteIds.split(",");
		}else if (deleteIds!=null&&!deleteIds.equals("")&&!deleteIds.contains(",")){
			ids=new String[1];
			ids[0]=deleteIds;
		}
		Object[] obj=new Object[ids.length];
		StringBuffer hql=new StringBuffer("delete from AppTutorAppointmentAssignmentToTutor taatt where taatt.id in (");
		for (int i=0;i<ids.length;i++){
			if (i==0){
				hql.append("?");
			}else{
				hql.append(",?");
			}
			obj[i]=Integer.parseInt(ids[i]);
		}
		hql.append(") and taatt.downloadTime is null and taatt.uploadAssignment is null");
		
		try{
			this.batchExecute(hql.toString(), obj);
			result=true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return result;
	}

}
