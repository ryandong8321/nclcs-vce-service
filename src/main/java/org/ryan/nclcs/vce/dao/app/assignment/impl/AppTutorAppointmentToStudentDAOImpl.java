package org.ryan.nclcs.vce.dao.app.assignment.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ryan.nclcs.vce.dao.NclcsVceServiceBaseDAOImpl;
import org.ryan.nclcs.vce.dao.Pagination;
import org.ryan.nclcs.vce.dao.app.assignment.IAppTutorAppointmentToStudentDAO;
import org.ryan.nclcs.vce.entity.AppTutorAppointmentAssignmentToStudent;
import org.springframework.stereotype.Repository;

@Repository("appTutorAppointmentToStudentDAO")
public class AppTutorAppointmentToStudentDAOImpl extends NclcsVceServiceBaseDAOImpl<AppTutorAppointmentAssignmentToStudent, Integer> implements IAppTutorAppointmentToStudentDAO {

	@Override
	public Pagination<AppTutorAppointmentAssignmentToStudent> searchDataForAppointmentStudent(int displayLength,
			int displayStart, int sEcho, Map<String, Object> parameters, Integer userId) {
		List<Object> param=new ArrayList<Object>();
		StringBuffer hql=new StringBuffer("from AppTutorAppointmentAssignmentToStudent taats where taats.sourceTutor.id = ?");
		param.add(userId);
		
		if (parameters.containsKey("assignmentName")){
			hql.append(" and taats.assignmentName like ?");
			param.add("%"+parameters.get("assignmentName")+"%");
		}
		
		if (parameters.containsKey("startTime")&&parameters.containsKey("endTime")){
			hql.append(" and (taats.uploadTime between timestamp(?,'yyyy-MM-dd HH:mi:ss') and timestamp(?,'yyyy-MM-dd HH:mi:ss'))");
			param.add(parameters.get("startTime"));
			param.add(parameters.get("endTime"));
		}else if (parameters.containsKey("startTime")){
			hql.append(" and taats.uploadTime > timestamp(?,'yyyy-MM-dd HH:mi:ss')");
			param.add(parameters.get("startTime"));
		}else if (parameters.containsKey("endTime")){
			hql.append(" and taats.uploadTime < timestamp(?,'yyyy-MM-dd HH:mi:ss')");
			param.add(parameters.get("endTime"));
		}
		
		if (parameters.containsKey("student.chineseName")){
			hql.append(" and taats.targetStudent.chineseName like ?");
			param.add("%"+parameters.get("student.chineseName")+"%");
		}
		
		if (parameters.containsKey("sort")){
			hql.append(" order by taats.");
			hql.append(getSortColumn(Integer.parseInt(""+parameters.get("sort"))));
		}
		if (parameters.containsKey("order")){
			hql.append(" ");
			hql.append(parameters.get("order"));
		}
		Pagination<AppTutorAppointmentAssignmentToStudent> page=new Pagination<AppTutorAppointmentAssignmentToStudent>();
		page.setFirst(displayStart);
		page.setPageSize(displayLength);
		
		page=this.findPage(page, hql.toString(), param.toArray());
		return page;
	}
	
	private String getSortColumn(int index){
		List<String> lstColumn=new ArrayList<String>();
		lstColumn.add("id");
		lstColumn.add("assignmentName");
		lstColumn.add("targetStudent.chineseName");
		lstColumn.add("uploadTime");
		lstColumn.add("downloadTime");		
		return lstColumn.get(index-1);
	}
	
	private String getSortColumnForStudent(int index){
		List<String> lstColumn=new ArrayList<String>();
		lstColumn.add("id");
		lstColumn.add("assignmentName");
		lstColumn.add("uploadTime");
		lstColumn.add("downloadTime");		
		return lstColumn.get(index-1);
	}

	@Override
	public Pagination<AppTutorAppointmentAssignmentToStudent> searchDataForAppointmentStudentForStudent(
			int displayLength, int displayStart, int sEcho, Map<String, Object> parameters, Integer userId) {
		List<Object> param=new ArrayList<Object>();
		StringBuffer hql=new StringBuffer("from AppTutorAppointmentAssignmentToStudent taats where taats.targetStudent.id = ?");
		param.add(userId);
		
		if (parameters.containsKey("assignmentName")){
			hql.append(" and taats.assignmentName like ?");
			param.add("%"+parameters.get("assignmentName")+"%");
		}
		
		if (parameters.containsKey("startTime")&&parameters.containsKey("endTime")){
			hql.append(" and (taats.uploadTime between timestamp(?,'yyyy-MM-dd HH:mi:ss') and timestamp(?,'yyyy-MM-dd HH:mi:ss'))");
			param.add(parameters.get("startTime"));
			param.add(parameters.get("endTime"));
		}else if (parameters.containsKey("startTime")){
			hql.append(" and taats.uploadTime > timestamp(?,'yyyy-MM-dd HH:mi:ss')");
			param.add(parameters.get("startTime"));
		}else if (parameters.containsKey("endTime")){
			hql.append(" and taats.uploadTime < timestamp(?,'yyyy-MM-dd HH:mi:ss')");
			param.add(parameters.get("endTime"));
		}
		
		if (parameters.containsKey("sort")){
			hql.append(" order by taats.");
			hql.append(getSortColumnForStudent(Integer.parseInt(""+parameters.get("sort"))));
		}
		if (parameters.containsKey("order")){
			hql.append(" ");
			hql.append(parameters.get("order"));
		}
		Pagination<AppTutorAppointmentAssignmentToStudent> page=new Pagination<AppTutorAppointmentAssignmentToStudent>();
		page.setFirst(displayStart);
		page.setPageSize(displayLength);
		
		page=this.findPage(page, hql.toString(), param.toArray());
		return page;
	}

	@Override
	public boolean deleteUploadAssignment(String deleteIds) {
		boolean result=false;
		String[] ids=null;
		if (deleteIds!=null&&!deleteIds.equals("")&&deleteIds.contains(",")){
			ids=deleteIds.split(",");
		}else if (deleteIds!=null&&!deleteIds.equals("")&&!deleteIds.contains(",")){
			ids=new String[1];
			ids[0]=deleteIds;
		}
		Object[] obj=new Object[ids.length];
		StringBuffer hql=new StringBuffer("delete from AppTutorAppointmentAssignmentToStudent taats where taats.id in (");
		for (int i=0;i<ids.length;i++){
			if (i==0){
				hql.append("?");
			}else{
				hql.append(",?");
			}
			obj[i]=Integer.parseInt(ids[i]);
		}
		hql.append(") and taats.downloadTime is null");
		
		try{
			this.batchExecute(hql.toString(), obj);
			result=true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return result;
	}
}
