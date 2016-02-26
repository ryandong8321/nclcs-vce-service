package org.ryan.nclcs.vce.service.appstudents.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ryan.nclcs.vce.dao.Pagination;
import org.ryan.nclcs.vce.dao.sysusers.ISysUsersManagementDAO;
import org.ryan.nclcs.vce.entity.SysUsers;
import org.ryan.nclcs.vce.service.NclcsVceServiceBaseServiceImpl;
import org.ryan.nclcs.vce.service.appstudents.IAppStudentsManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("appStudentsManagementService")
public class AppStudentsManagementServiceImpl extends NclcsVceServiceBaseServiceImpl<SysUsers, Integer, ISysUsersManagementDAO> implements IAppStudentsManagementService {

	@Autowired
	private ISysUsersManagementDAO sysUsersManagementDAO;
	
	@Override
	protected ISysUsersManagementDAO getCurrentDAO() {
		return this.sysUsersManagementDAO;
	}

	@Override
	public Map<String, Object> searchDataForAjax(int displayLength, int displayStart, int sEcho,
			Map<String, Object> parameters, Integer userId, boolean isShowScore) {
		Map<String, Object> result=new HashMap<String, Object>();
		List<ArrayList<String>> data=new ArrayList<ArrayList<String>>();
		
		Pagination<SysUsers> page=this.getCurrentDAO().searchDataForStudents(displayLength, displayStart, sEcho, parameters, userId);
		
		if (page.getRows() != null && !page.getRows().isEmpty()) {
			ArrayList<String> tmp=null;
			int idx=0;
			for (SysUsers user : page.getRows()) {
				tmp=new ArrayList<String>();
				tmp.add("<input type='checkbox' name='id[]' value='"+user.getId()+"'/>");
				tmp.add(""+(++idx));
				if (isShowScore){//查询学生成绩列表和学生信息列表的跳转不一样
					tmp.add("<a href=\"javascript:showScore('"+user.getId()+"')\">"+user.getUserName()+"</a>");
				}else{
					tmp.add("<a href=\"javascript:showStudent('"+user.getId()+"')\">"+user.getUserName()+"</a>");
				}
				tmp.add(user.getChineseName());
				tmp.add(user.getEnglishName());
				tmp.add(user.getVceSchoolName());
				tmp.add(user.getVceClassName());
				if (isShowScore){//查询学生成绩列表不要删除功能
					tmp.add("");
				}else{
//					tmp.add("<a href=\"javascript:deleteStudent('"+user.getId()+"');\" class=\"btn btn-sm red\"><i class=\"fa fa-times\"></i> DELETE</a>");
					tmp.add("");
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
	public Map<String, Object> searchDataForApp(int displayLength, int displayStart, Map<String, Object> parameters,
			Integer userId, boolean isShowScore) {
		Map<String, Object> result=new HashMap<String, Object>();
		List<Map<String, Object>> data=new ArrayList<Map<String, Object>>();
		
		Pagination<SysUsers> page=this.getCurrentDAO().searchDataForStudents(displayLength, displayStart, parameters, userId);
		
		if (page.getRows() != null && !page.getRows().isEmpty()) {
			Map<String, Object> tmp=null;
			for (SysUsers user : page.getRows()) {
				tmp=new HashMap<String, Object>();
				tmp.put("userId", user.getId());
				tmp.put("userChineseName", user.getChineseName());
				tmp.put("userEnglishName", user.getEnglishName());
				tmp.put("vceCampus", user.getVceSchoolName());
				tmp.put("vceClass", user.getVceClassName());
				data.add(tmp);
			}
		}
		
		result.put("data", data);
		result.put("recordsTotal", page.getTotal());
		result.put("recordsFiltered", "");
		return result;
	}

}
