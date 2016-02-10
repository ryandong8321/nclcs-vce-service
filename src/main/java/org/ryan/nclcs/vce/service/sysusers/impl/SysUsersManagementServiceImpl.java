package org.ryan.nclcs.vce.service.sysusers.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ryan.nclcs.vce.dao.Pagination;
import org.ryan.nclcs.vce.dao.sysgroups.ISysGroupsManagementDAO;
import org.ryan.nclcs.vce.dao.sysusers.ISysUsersManagementDAO;
import org.ryan.nclcs.vce.entity.SysGroups;
import org.ryan.nclcs.vce.entity.SysRoles;
import org.ryan.nclcs.vce.entity.SysUsers;
import org.ryan.nclcs.vce.service.NclcsVceServiceBaseServiceImpl;
import org.ryan.nclcs.vce.service.sysusers.ISysUsersManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("sysUsersManagementService")
public class SysUsersManagementServiceImpl extends NclcsVceServiceBaseServiceImpl<SysUsers, Integer, ISysUsersManagementDAO> implements ISysUsersManagementService {

	@Autowired
	private ISysUsersManagementDAO sysUsersManagementDAO;
	
	@Autowired
	private ISysGroupsManagementDAO sysGroupsManagementDAO;
	
	@Override
	protected ISysUsersManagementDAO getCurrentDAO() {
		return this.sysUsersManagementDAO;
	}
	
	@Override
	public Map<String, Object> searchDataForAjax(int displayLength, int displayStart, int sEcho, Map<String, Object> parameters){
		Map<String, Object> result=new HashMap<String, Object>();
		List<ArrayList<String>> data=new ArrayList<ArrayList<String>>();
		
		Pagination<SysUsers> page=this.getCurrentDAO().searchData(displayLength, displayStart, sEcho, parameters);
		
		if (page.getRows() != null && !page.getRows().isEmpty()) {
			ArrayList<String> tmp=null;
			int idx=0;
			for (SysUsers user : page.getRows()) {
				tmp=new ArrayList<String>();
				tmp.add("<input type='checkbox' name='id[]' value='"+user.getId()+"'/>");
				tmp.add(""+(++idx));
				tmp.add("<a href=\"javascript:showUser('"+user.getId()+"')\">"+user.getUserName()+"</a>");
				tmp.add(user.getChineseName());
				tmp.add(user.getEnglishName());
				tmp.add(user.getMobilePhone());
				tmp.add(user.getEmailAddress());
				tmp.add("<a href=\"javascript:deleteInfo('"+user.getId()+"');\" class=\"btn btn-sm red\"><i class=\"fa fa-times\"></i> DELETE</a>");
				data.add(tmp);
			}
		}
		
		result.put("data", data);
		result.put("recordsTotal", page.getTotal());
		result.put("recordsFiltered", "");
		return result;
	}

	@Override
	public Map<String, Object> deleteMultiple(String ids) {
		return this.getCurrentDAO().deleteUsers(ids);
	}

	@Override
	public SysUsers isExistByParameters(Map<String, Object> parameters) {
		Object[] param = null;
		StringBuffer hql = new StringBuffer("from SysUsers sus ");
		List<Object> lstResult=null;
		
		if (parameters != null && !parameters.isEmpty()) {
			param = new Object[parameters.size()];
			int ind = 0;
			
			for (String key : parameters.keySet()) {
				
				if (ind==0){
					hql.append(" where ");
				}else{
					hql.append(" and ");
				}
				hql.append(key);
				hql.append(" = ?");
				param[ind++]=parameters.get(key);
			}
			
			lstResult=this.getCurrentDAO().find(hql.toString(), param);
		}
		
		SysUsers user=null;
		if(lstResult!=null&&!lstResult.isEmpty()){
			user=(SysUsers)lstResult.get(0);
		}
		return user;
	}

	@Override
	public List<Map<String, Object>> findAllSysUsersByPrivilege(Map<String, Object> parameters) {
		List<Map<String, Object>> result=null;
		Map<String, Object> map=null;
		int intRoleOrGroup=0;//角色授权
		List<SysUsers> lst=this.getCurrentDAO().findAllSysUsersForPrivilege(parameters, intRoleOrGroup);
		
//		Integer privilegeRoleId=0;
//		if (parameters.containsKey("privilegeRoleId")){
//			privilegeRoleId=Integer.parseInt(""+parameters.get("privilegeRoleId"));
//		}
		
		if (lst!=null&&!lst.isEmpty()){
			result=new ArrayList<Map<String, Object>>();
			for (SysUsers user:lst){
				map=new HashMap<String, Object>();
				map.put("value", user.getId());
				map.put("text", user.getUserName());
				result.add(map);
			}
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> findAllSysUsersByPrivilege(Map<String, Object> parameters, Integer groupId) {
		List<Map<String, Object>> result=null;
		Map<String, Object> map=null;
		
		Integer[] arr=null;
		
		SysGroups sysGroup=sysGroupsManagementDAO.get(groupId);
		if (sysGroup.getGroupCategory()==0&&sysGroup.getGroupParentId()==-1){//总校
			arr=new Integer[2];
			arr[0]=1;//id:1-管理者角色
			arr[1]=2;//id:2-管理者助理角色
			
		}else if (sysGroup.getGroupCategory()==0&&sysGroup.getGroupParentId()!=-1){//分校
			arr=new Integer[1];
			arr[0]=3;//id:3-校区助理角色
		}else if (sysGroup.getGroupCategory()==1){//班级
			arr=new Integer[3];
			arr[0]=4;//id:4-教师角色
			arr[1]=5;//id:5-学生角色
			arr[2]=6;//id:6-家长角色
		}
		if (arr!=null){
			parameters.put("roleIds", arr);
		}
		int intRoleOrGroup=1;//群组授权
		parameters.put("privilegeGroupId", groupId);
		List<SysUsers> lst=this.getCurrentDAO().findAllSysUsersForPrivilege(parameters, intRoleOrGroup);
		if (lst!=null&&!lst.isEmpty()){
			result=new ArrayList<Map<String, Object>>();
			for (SysUsers user:lst){
				map=new HashMap<String, Object>();
				map.put("value", user.getId());
				map.put("text", user.getUserName()+"["+getUserRolesName(user.getSysRoles())+"]");
				for (SysUsers haveUsers:sysGroup.getSysGroupsUsers()){
					 if (user.getId().equals(haveUsers.getId())){
						 map.put("isSelected", true);
						 break;
					 }
				}
				result.add(map);
			}
		}
		return result;
	}
	
	private String getUserRolesName(List<SysRoles> lst){
		StringBuffer buf=new StringBuffer();
		SysRoles role=null;
		for (int i=0;i<lst.size();i++){
			role=lst.get(i);
			if (i!=0){
				buf.append("|");
			}
			buf.append(role.getRoleName());
		}
		return buf.toString();
	}
}
