package org.ryan.nclcs.vce.dao.app.assignment.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ryan.nclcs.vce.dao.NclcsVceServiceBaseDAOImpl;
import org.ryan.nclcs.vce.dao.Pagination;
import org.ryan.nclcs.vce.dao.app.assignment.IAppStudentUploadAssignmentDAO;
import org.ryan.nclcs.vce.entity.AppStudentUploadAssignment;
import org.springframework.stereotype.Repository;

@Repository("appStudentUploadAssignmentDAO")
public class AppStudentUploadAssignmentDAOImpl extends NclcsVceServiceBaseDAOImpl<AppStudentUploadAssignment, Integer> implements IAppStudentUploadAssignmentDAO {

	@Override
	public Pagination<AppStudentUploadAssignment> searchDataForStudents(int displayLength, int displayStart, int sEcho,
			Map<String, Object> parameters, Integer userId) {
		List<Object> param=new ArrayList<Object>();
		StringBuffer hql=new StringBuffer("from AppStudentUploadAssignment sula where sula.student.id = ?");
		param.add(userId);
		
		if (parameters.containsKey("assignmentName")){
			hql.append(" and sula.assignmentName like ?");
			param.add("%"+parameters.get("assignmentName")+"%");
		}
		
		if (parameters.containsKey("startTime")&&parameters.containsKey("endTime")){
			hql.append(" and (sula.uploadTime between timestamp(?,'yyyy-MM-dd HH:mi:ss') and timestamp(?,'yyyy-MM-dd HH:mi:ss'))");
			param.add(parameters.get("startTime"));
			param.add(parameters.get("endTime"));
		}else if (parameters.containsKey("startTime")){
			hql.append(" and sula.uploadTime > timestamp(?,'yyyy-MM-dd HH:mi:ss')");
			param.add(parameters.get("startTime"));
		}else if (parameters.containsKey("endTime")){
			hql.append(" and sula.uploadTime < timestamp(?,'yyyy-MM-dd HH:mi:ss')");
			param.add(parameters.get("endTime"));
		}
		
		if (parameters.containsKey("sort")){
			hql.append(" order by sula.");
			hql.append(getSortColumnForStudent(Integer.parseInt(""+parameters.get("sort"))));
		}
		if (parameters.containsKey("order")){
			hql.append(" ");
			hql.append(parameters.get("order"));
		}
		Pagination<AppStudentUploadAssignment> page=new Pagination<AppStudentUploadAssignment>();
		page.setFirst(displayStart);
		page.setPageSize(displayLength);
		
		page=this.findPage(page, hql.toString(), param.toArray());
		return page;
	}
	
	private String getSortColumnForStudent(int index){
		List<String> lstColumn=new ArrayList<String>();
		lstColumn.add("id");
		lstColumn.add("assignmentName");
		lstColumn.add("uploadTime");
		lstColumn.add("downloadTime");		
		return lstColumn.get(index-1);
	}
	
	private String getSortColumn(int index){
		List<String> lstColumn=new ArrayList<String>();
		lstColumn.add("id");
		lstColumn.add("assignmentName");
		lstColumn.add("student.chineseName");
		lstColumn.add("student.vceSchoolName");
		lstColumn.add("student.vceClasssName");
		lstColumn.add("uploadTime");
		lstColumn.add("downloadTime");		
		return lstColumn.get(index-1);
	}

	@Override
	public Pagination<AppStudentUploadAssignment> searchDataForTutors(int displayLength, int displayStart, int sEcho,
			Map<String, Object> parameters, String groupIds) {
		List<Object> param=new ArrayList<Object>();
		Pagination<AppStudentUploadAssignment> page=new Pagination<AppStudentUploadAssignment>();
		if (groupIds!=null&&!groupIds.equals("")){
			StringBuffer hql=new StringBuffer("from AppStudentUploadAssignment sula where sula.student.id in (select sus.id from SysUsers sus join sus.sysGroups sgs where sgs.id in (");
			boolean hasPoint=false;
			for (String id:groupIds.split(",")){
				if (!hasPoint){
					hql.append("?");
					hasPoint=true;
				}else{
					hql.append(",?");
				}
				param.add(Integer.parseInt(id));
			}
			
			hql.append("))");
			
			if (parameters.containsKey("assignmentName")){
				hql.append(" and sula.assignmentName like ?");
				param.add("%"+parameters.get("assignmentName")+"%");
			}
			
			if (parameters.containsKey("startTime")&&parameters.containsKey("endTime")){
				hql.append(" and (sula.uploadTime between timestamp(?,'yyyy-MM-dd HH:mi:ss') and timestamp(?,'yyyy-MM-dd HH:mi:ss'))");
				param.add(parameters.get("startTime"));
				param.add(parameters.get("endTime"));
			}else if (parameters.containsKey("startTime")){
				hql.append(" and sula.uploadTime > timestamp(?,'yyyy-MM-dd HH:mi:ss')");
				param.add(parameters.get("startTime"));
			}else if (parameters.containsKey("endTime")){
				hql.append(" and sula.uploadTime < timestamp(?,'yyyy-MM-dd HH:mi:ss')");
				param.add(parameters.get("endTime"));
			}
			
			if (parameters.containsKey("studentName")){
				hql.append(" and sula.student.chineseName like ?");
				param.add("%"+parameters.get("studentName")+"%");
			}
			
			if (parameters.containsKey("campusName")){
				hql.append(" and sula.student.vceSchoolName like ?");
				param.add("%"+parameters.get("campusName")+"%");
			}
			
			if (parameters.containsKey("className")){
				hql.append(" and sula.student.vceClassName like ?");
				param.add("%"+parameters.get("className")+"%");
			}
			
			if (parameters.containsKey("sort")){
				hql.append(" order by sula.");
				hql.append(getSortColumn(Integer.parseInt(""+parameters.get("sort"))));
			}
			if (parameters.containsKey("order")){
				hql.append(" ");
				hql.append(parameters.get("order"));
			}
			
			page.setFirst(displayStart);
			page.setPageSize(displayLength);
			
			page=this.findPage(page, hql.toString(), param.toArray());
			return page;
			
		}
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
		StringBuffer hql=new StringBuffer("delete from AppStudentUploadAssignment asua where asua.id in (");
		for (int i=0;i<ids.length;i++){
			if (i==0){
				hql.append("?");
			}else{
				hql.append(",?");
			}
			obj[i]=Integer.parseInt(ids[i]);
		}
		hql.append(") and asua.downloadTime is null and asua.hasAppointment = 0");
		
		try{
			this.batchExecute(hql.toString(), obj);
			result=true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return result;
	}
}
