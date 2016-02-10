package org.ryan.nclcs.vce.service.sysgroups.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ryan.nclcs.vce.dao.Pagination;
import org.ryan.nclcs.vce.dao.sysgroups.ISysGroupsManagementDAO;
import org.ryan.nclcs.vce.dao.sysusers.ISysUsersManagementDAO;
import org.ryan.nclcs.vce.entity.SysGroups;
import org.ryan.nclcs.vce.entity.SysUsers;
import org.ryan.nclcs.vce.service.NclcsVceServiceBaseServiceImpl;
import org.ryan.nclcs.vce.service.sysgroups.ISysGroupsManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("sysGroupsManagementService")
public class SysGroupsManagementServiceImpl extends NclcsVceServiceBaseServiceImpl<SysGroups, Integer, ISysGroupsManagementDAO> implements ISysGroupsManagementService {
	
	@Autowired
	private ISysGroupsManagementDAO sysGroupsManagementDAO;
	
	@Autowired
	private ISysUsersManagementDAO sysUsersManagementDAO;
	
	@Override
	protected ISysGroupsManagementDAO getCurrentDAO() {
		return this.sysGroupsManagementDAO;
	}
	
	@Override
	public List<Map<String, Object>> findGroup(Map<String, Object> parameters, Integer currentUserId) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
//		SysUsers currentUser=sysUsersManagementDAO.get(currentUserId);
		List<Object> param = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("From SysGroups sgs ");
		
//		if (parameters.containsKey("groupParentId")&&Integer.parseInt(""+parameters.get("groupParentId"))==0){
//			List<SysRoles> lstRoles=currentUser.getSysRoles();
//			List<SysGroups> lstGroups=currentUser.getSysGroups();
//			SysGroups sysGroup=null;
//			StringBuffer hql_role = new StringBuffer();
//			for (SysRoles role:lstRoles){
//				if (role.getId()==1||role.getId()==2){
//					hql_role=null;
//					parameters.put("groupParentId",1);
//					break;
//				}
//				if (role.getId()==3){
//					for (int i=0;i<lstGroups.size();i++){
//						sysGroup=lstGroups.get(i);
//						if (sysGroup.getGroupCategory()==0){
//							if (hql_role.length()>0){
//								hql_role.append(" or ");
//							}
//							hql_role.append(" sgs.id = ?");
//							param.add(sysGroup.getId());
//						}
//					}
//					
//				}
//			}
//			
//			if (hql_role!=null&&hql_role.length()>0){
//				hql.append(" where (");
//				hql.append(hql_role.toString());
//				hql.append(")");
//			}
//		}
		
		boolean flagWhere=hql.toString().contains("where")?true:false;
		if (parameters != null && !parameters.isEmpty()) {
			for (String key : parameters.keySet()) {
				if (key.equals("groupParentId")&&Integer.parseInt(""+parameters.get(key))==0){
					continue;
				}
				
				if (flagWhere){
					hql.append(" and ");
				}else{
					hql.append(" where ");
					flagWhere=true;
				}
				
				hql.append(key);
				hql.append(" = ?");
				param.add(parameters.get(key));
			}
		}
		List<SysGroups> lst = this.getCurrentDAO().find(hql.toString(), param.toArray());
		if (lst != null && !lst.isEmpty()) {
			Map<String, Object> map = null;
			for (SysGroups group : lst) {
				map = new HashMap<String, Object>();
				map.put("id", group.getId());
				map.put("text", group.getGroupName());
				result.add(map);
			}
		}
		return result;
	}

	@Override
	public Map<String, Object> findGroup(Integer rowNum, Integer pageSize, Map<String, Object> parameters) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Object[] param = null;
		StringBuffer hql = new StringBuffer("from SysGroups sgs where 1=1");

		Pagination<SysGroups> page = new Pagination<>();
		page.setFirst(rowNum);
		page.setPageSize(pageSize);

		if (parameters != null && !parameters.isEmpty()) {
			param = new Object[parameters.size()];
			int ind = 0;
			for (String key : parameters.keySet()) {
				hql.append(" and ");
				hql.append(key);
				hql.append(" = ?");
				param[ind++] = parameters.get(key);
			}
		}
		Pagination<SysGroups> resultPage = this.getCurrentDAO().findPage(page, hql.toString(), param);

		if (resultPage.getRows() != null && !resultPage.getRows().isEmpty()) {
			Map<String, Object> map = null;
			for (SysGroups sysGroup : resultPage.getRows()) {
				map = new HashMap<String, Object>();
				map.put("id", sysGroup.getId());
				map.put("propertyName", sysGroup.getGroupName());
				result.add(map);
			}
		}

		resultMap.put("total", page.getTotal());
		resultMap.put("rows", result);
		return resultMap;
	}

	@Override
	public String findGroupParents(Integer groupId, Integer parentId) {
		return findParentId(parentId, "");
	}
	
	private String findParentId(Integer parentId,String parents){
		SysGroups sysGroup=this.getCurrentDAO().get(parentId);
		if (sysGroup!=null&&sysGroup.getGroupParentId()!=-1){
			parents += findParentId(sysGroup.getGroupParentId(), parents)+",";
		}
		parents+=sysGroup.getId();
		return parents;
	}

	@Override
	public List<Map<String, Object>> findGroupsForTree(Map<String, Object> parameters) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Object[] param = null;
		StringBuffer hql = new StringBuffer("From SysGroups sgs where 1=1");
		if (parameters != null && !parameters.isEmpty()) {
			param = new Object[parameters.size()];
			int ind = 0;
			for (String key : parameters.keySet()) {
				hql.append(" and ");
				hql.append(key);
				hql.append(" = ?");
				param[ind++] = parameters.get(key);
			}
		}
		
		hql.append(" order by groupParentId , id");
		
		List<SysGroups> lst = this.getCurrentDAO().find(hql.toString(), param);
		if (lst != null && !lst.isEmpty()) {
			Map<String, Object> map = null;
			for (SysGroups sysGroup : lst) {
				map = new HashMap<String, Object>();
				map.put("id", sysGroup.getId());
				map.put("text", sysGroup.getGroupName());
				map.put("parent", sysGroup.getGroupParentId()==-1?"#":sysGroup.getGroupParentId());
				if (sysGroup.getGroupParentId()==-1){
					Map<String,Object> state=new HashMap<String,Object>();
					state.put("opened", true);
					state.put("selected", true);
					map.put("state", state);
				}
				result.add(map);
			}
		}
		return result;
	}

	@Override
	public boolean deleteGroups(String groupIds) {
		String[] ids=groupIds.split(",");
		Object[] obj=new Object[ids.length];
		StringBuffer hql=new StringBuffer("delete from SysGroups sgs where id in (");
		for (int i=0;i<ids.length;i++){
			if (i==0){
				hql.append("?");
			}else{
				hql.append(",?");
			}
			obj[i]=Integer.parseInt(ids[i]);
		}
		hql.append(")");
		
		boolean flag=true;
		try{
			this.getCurrentDAO().batchExecute(hql.toString(), obj);
		}catch(Exception ex){
			ex.printStackTrace();
			flag=false;
		}
		return flag;
	}

	@Override
	public List<Map<String, Object>> findGroupsForTree(List<Map<String, Integer>> parameters) {
		StringBuffer hql=new StringBuffer();
		StringBuffer strIds=new StringBuffer();
		
		List<Integer> parameterIds=new ArrayList<Integer>();//记录班级的group_id
		List<Integer> parentIds=new ArrayList<Integer>();//记录班级的group_parent_id，现阶段的数据结构group_parent_id就是校区的id
		for(Map<String, Integer> map:parameters){
			parameterIds.add(map.get("groupId"));
			if (map.get("groupCategory")==0){//校区
				parentIds.add(map.get("groupId"));
			}else if (map.get("groupCategory")==1){//班级
				if (strIds.length()>0){
					strIds.append(",");
				}
				strIds.append(this.findParentId(map.get("groupId"), strIds.toString()));
			}
		}
		
		if (strIds.length()!=0){
			for (String tmp:strIds.toString().split(",")){
				if (tmp!=null&&!tmp.equals("")){
					parameterIds.add(Integer.parseInt(tmp));
				}
			}
		}
		
		List<Object> param=new ArrayList<Object>();
		//= ? or sgs.groupParentId = ? or sgs.groupParentId = -1 order by groupParentId , id
		hql.append("from SysGroups sgs where ");
		if (parameterIds.size()>0){
			hql.append("sgs.id in (");
			for (int i=0;i<parameterIds.size();i++){
				if (i!=0){
					hql.append(" , ");
				}
				hql.append("?");
				param.add(parameterIds.get(i));
			}
			hql.append(")");
		}
		if (parentIds.size()>0){
			if (parameterIds.size()>0){
				hql.append(" or ");
			}
			hql.append("sgs.groupParentId in (");
			for (int i=0;i<parentIds.size();i++){
				if (i!=0){
					hql.append(" , ");
				}
				hql.append("?");
				param.add(parentIds.get(i));
			}
			hql.append(")");
		}
		hql.append("or sgs.groupParentId = -1 order by groupParentId , id");
		
		List<SysGroups> lst = this.getCurrentDAO().find(hql.toString(), param.toArray());
		Map<String, Object> mapTemp = null;
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		if (lst != null && !lst.isEmpty()) {
			for (SysGroups sysGroup : lst) {
				mapTemp = new HashMap<String, Object>();
				mapTemp.put("id", sysGroup.getId());
				mapTemp.put("text", sysGroup.getGroupName());
				mapTemp.put("parent", sysGroup.getGroupParentId()==-1?"#":sysGroup.getGroupParentId());
				if (sysGroup.getGroupParentId()==-1){
					Map<String,Object> state=new HashMap<String,Object>();
					state.put("opened", true);
					state.put("selected", true);
					mapTemp.put("state", state);
				}
				result.add(mapTemp);
			}
		}
		return result;
	}

	@Override
	public List<SysUsers> findSysUsersInGroups(String groupIds, int roleCategory, List<SysGroups> lstGroups) {
		List<Object> gIds=new ArrayList<Object>();
		if (groupIds!=null&&!groupIds.equals("")&&groupIds.contains(",")){
			String[] arr=groupIds.split(",");
			for (int i=0;i<arr.length;i++){
				gIds.add(Integer.parseInt(arr[i]));
			}
		}else if(groupIds!=null&&!groupIds.equals("")&&!groupIds.contains(",")){
			gIds.add(Integer.parseInt(groupIds));
		}
		
		if (roleCategory!=1){
			StringBuffer hql=new StringBuffer("from SysGroups where id in (");
			for (int i=0;i<gIds.size();i++){
				if (i==0){
					hql.append("?");
				}else{
					hql.append(",?");
				}
			}
			hql.append(")");
			List<SysGroups> lst=this.getCurrentDAO().find(hql.toString(), gIds.toArray());
			
			gIds=new ArrayList<Object>();
			if (lst!=null&&!lst.isEmpty()){
				for (SysGroups givenGroup:lstGroups){//当前登录用户所在的群组
					for (SysGroups group:lst){
						if (roleCategory==2){//校区助理角色
							if (givenGroup.getGroupCategory()==0){//所在群组为校区时，应取校区群组ID和校区下班级ID
								if (group.getId()==givenGroup.getId()||group.getGroupParentId()==givenGroup.getId()){
									gIds.add(group.getId());
								}
							}else if (givenGroup.getGroupCategory()==1){//所在群组为班级时，应只取班级ID
								if (group.getId()==givenGroup.getId()){
									gIds.add(group.getId());
								}
							}
						}else if (roleCategory==3){//教师角色
							if (givenGroup.getGroupCategory()==1){//所在群组为班级时，应只取班级ID
								if (group.getId()==givenGroup.getId()){
									gIds.add(group.getId());
								}
							}
						}
					}
				}
			}
		}
		
		return this.sysUsersManagementDAO.findSysUsersInGroups(gIds);
	}

	@Override
	public Map<String, Object> saveRelationship(String groupId, String userIds) {
		return this.getCurrentDAO().saveRelationship(groupId, userIds);
	}

	@Override
	public Map<String, Object> saveStudentGroupChange(SysUsers studentUser, Integer changeToGroupId, SysUsers currentOperationUser) {
		return this.getCurrentDAO().saveStudentGroupChange(studentUser, changeToGroupId, currentOperationUser);
	}
}
