package org.ryan.nclcs.vce.dao.sysusers.impl;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.ryan.nclcs.vce.dao.NclcsVceServiceBaseDAOImpl;
import org.ryan.nclcs.vce.dao.Pagination;
import org.ryan.nclcs.vce.dao.sysgroups.ISysGroupsManagementDAO;
import org.ryan.nclcs.vce.dao.sysroles.ISysRolesManagementDAO;
import org.ryan.nclcs.vce.dao.sysusers.ISysUsersManagementDAO;
import org.ryan.nclcs.vce.entity.SysGroups;
import org.ryan.nclcs.vce.entity.SysRoles;
import org.ryan.nclcs.vce.entity.SysUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("sysUsersManagementDAO")
public class SysUsersManagementDAOImpl extends NclcsVceServiceBaseDAOImpl<SysUsers, Integer> implements ISysUsersManagementDAO {
	
	@Autowired
	private ISysRolesManagementDAO sysRolesManagementDAO;
	
	@Autowired
	private ISysGroupsManagementDAO sysGroupsManagementDAO;
	
	@Override
	public Pagination<SysUsers> searchData(int displayLength, int displayStart, int sEcho, Map<String, Object> parameters){
		Pagination<SysUsers> page=new Pagination<SysUsers>();
		
		Object[] param = null;
		StringBuffer hql = new StringBuffer("from SysUsers sus ");
		
		page.setFirst(displayStart);
		page.setPageSize(displayLength);
		
		if (parameters != null && !parameters.isEmpty()) {
			Object[] tmp = new Object[parameters.size()];
			int ind = 0;
			
			for (String key : parameters.keySet()) {
				if (key=="sort"||key.equals("order")){
					continue;
				}
				
				if (ind==0){
					hql.append(" where ");
				}else{
					hql.append(" and ");
				}
//				hql.append("sus.");
				hql.append(key);
				//除id字段外，其它字段模糊查询
				hql.append(key.equals("id")?" = ?":" like ?");
//				param[ind++]=key.equals("id")?parameters.get(key):"%"+parameters.get(key)+"%";
				tmp[ind++]=key.equals("id")?parameters.get(key):"%"+parameters.get(key)+"%";
			}
			
			if (parameters.containsKey("sort")){
				hql.append(" order by sus.");
				hql.append(getSortColumn(Integer.parseInt(""+parameters.get("sort"))));
			}
			if (parameters.containsKey("order")){
				hql.append(" ");
				hql.append(parameters.get("order"));
			}
			
			if (ind>0){
				param=new Object[ind];
				int idx=0;
				for (Object obj:tmp){
					if (obj==null){
						continue;
					}
					param[idx++]=obj;
				}
			}
		}
		
		page= this.findPage(page, hql.toString(), param);
		
		return page;
	}
	
	private String getSortColumn(int index){
		List<String> lstColumn=new ArrayList<String>();
		lstColumn.add("id");
		lstColumn.add("userName");
		lstColumn.add("chineseName");
		lstColumn.add("englishName");
		lstColumn.add("mobilePhone");
		lstColumn.add("emailAddress");
		
		return lstColumn.get(index-1);
	}

	@Override
	public Map<String, Object> deleteUsers(String userIds) {
		String[] ids=null;
		if (userIds!=null&&!userIds.equals("")&&userIds.contains(",")){
			ids=userIds.split(",");
		}else if (userIds!=null&&!userIds.equals("")&&!userIds.contains(",")){
			ids=new String[1];
			ids[0]=userIds;
		}
		Object[] obj=new Object[ids.length];
		StringBuffer hql=new StringBuffer("delete from SysUsers sus where id in (");
		for (int i=0;i<ids.length;i++){
			if (i==0){
				hql.append("?");
			}else{
				hql.append(",?");
			}
			obj[i]=Integer.parseInt(ids[i]);
		}
		hql.append(")");
		
		Map<String,Object> result=new Hashtable<String,Object>();
		try{
			this.batchExecute(hql.toString(), obj);
			result.put("status", 1);
			result.put("data", "Operation success.");
		}catch(Exception ex){
			ex.printStackTrace();
			result.put("status", 1);
			result.put("data", "Delete Users failed, try again.");
		}
		
		return result;
	}

	@Override
	public List<SysUsers> findAllSysUsersForPrivilege(Map<String, Object> parameters, int intRoleOrGroup) {
//		Object[] param = null;
		List<Object> lst=new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("select sus from SysUsers sus");
		
		
		if (intRoleOrGroup==0){//角色授权，没有角色的用户也要显示出来，所以要用left join
			//hql.append(" left join sus.sysRoles srs ");
			Integer privilegeRoleId=0;
			if (parameters.containsKey("privilegeRoleId")){
				privilegeRoleId=Integer.parseInt(""+parameters.get("privilegeRoleId"));
			}
			//保证学生不能有其它角色
			if (privilegeRoleId!=5){//给除学生角色以外的角色分配用户，就不显示具有学生角色的用户
				//hql.append(" on srs.id not in (5,6)");
				//奇怪的写法，可能是自己用Hibernate的左外链接不熟悉吧，先这么写吧
				hql.append(" where sus.id not in (select srus.id from SysRoles srs join srs.sysRolesUsers srus where srs.id = 5)");
			}else{
				//给学生角色授权，只显示没有角色的用户
//				hql.append(" where sus.id not in (select srus.id from SysRoles srs join srs.sysRolesUsers srus)");
				hql.append(" where (sus.id not in (select srus.id from SysRoles srs join srs.sysRolesUsers srus)) or (sus.id in (select sgus.id from SysGroups sgs join sgs.sysGroupsUsers sgus))");
			}
		}else if (intRoleOrGroup==1){//群组授权，没有角色的用户是不可以显示出来并进行分组的，所以要用(inner)join
			hql.append(" join sus.sysRoles srs ");
			
			Integer privilegeGroupId=0;
			if (parameters.containsKey("privilegeGroupId")){
				privilegeGroupId=Integer.parseInt(""+parameters.get("privilegeGroupId"));
			}
			if (parameters.containsKey("roleIds")){
				Integer[] arr=(Integer[])parameters.get("roleIds");
				for (int i:arr){
					if (i==5){//roleIds里面包含5,说明分配用户的群组是班级，要求学生只能在一个班级里，只能变更班级
						hql.append(" where (sus.id not in (select sus1.id from SysUsers sus1 join sus1.sysRoles srs1 join sus1.sysGroups sgs1 where srs1.id = 5 and sgs1.id <> ?))");
						lst.add(privilegeGroupId);
					}
//					param[ind++]=arr[i];
				}
			}
		}
		
		if (parameters != null && !parameters.isEmpty()) {
//			param = new Object[parameters.size()];
//			int ind = 0;
			boolean hasWhere=hql.toString().contains("where");
			
			for (String key : parameters.keySet()) {
//				if (ind==0){
//					hql.append(" where ");
//				}else{
//					hql.append(" and ");
//				}
				if (key.equals("privilegeRoleId")||key.equals("privilegeGroupId")){
					continue;
				}
				if (hasWhere){
					hql.append(" and ");
				}else{
					hql.append(" where ");
					hasWhere=true;
				}
				if (key.equals("roleIds")){
					Integer[] arr=(Integer[])parameters.get(key);
					hql.append(" srs.id in (");
					for (int i=0;i<arr.length;i++){
						if (i==0){
							hql.append("?");
						}else{
							hql.append(",?");
						}
						lst.add(arr[i]);
//						param[ind++]=arr[i];
					}
					hql.append(")");
				}else{
					hql.append(key);
					hql.append(" = ?");
					lst.add(parameters.get(key));
//					param[ind++]=parameters.get(key);
				}
			}
		}
		return this.find(hql.toString(), lst.toArray());
	}

	@Override
	public List<SysUsers> findSysUsersInGroups(List<Object> groupIds) {
		StringBuffer hql = new StringBuffer("select distinct sus from SysUsers sus join sus.sysGroups sgs where sgs.id in (");
		
		for (int i=0;i<groupIds.size();i++){
			if (i==0){
				hql.append("?");
			}else{
				hql.append(",?");
			}
		}
		hql.append(")");
		
		return this.find(hql.toString(), groupIds.toArray());
	}

	@Override
	public Pagination<SysUsers> searchDataForStudents(int displayLength, int displayStart, int sEcho,
			Map<String, Object> parameters, Integer userId) {
		
		SysUsers currentUser=this.get(userId);
		if (currentUser.getSysRoles()==null||currentUser.getSysRoles().isEmpty()
				||currentUser.getSysGroups()==null||currentUser.getSysGroups().isEmpty()){
			return null;
		}
		Pagination<SysUsers> page=new Pagination<SysUsers>();
		
		List<Object> param=new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("select sus from SysUsers sus join sus.sysRoles srs join sus.sysGroups sgs where srs.id = 5");//5是学生角色的ID
		
		page.setFirst(displayStart);
		page.setPageSize(displayLength);
		
		List<SysRoles> lstRoles=currentUser.getSysRoles();
		List<SysGroups> lstGroups=currentUser.getSysGroups();
		SysGroups group=null;
		StringBuffer hql_3 = new StringBuffer();
		List<Object> param_3=new ArrayList<Object>();
		StringBuffer hql_4 = new StringBuffer();
		List<Object> param_4=new ArrayList<Object>();
		for (SysRoles role:lstRoles){
			if (role.getId()==1||role.getId()==2){
				break;
			}else if (role.getId()==3){//当前用户是校区助理，查找校区下的所有班级
				for (int i=0;i<lstGroups.size();i++){
					group=lstGroups.get(i);
					if (group.getGroupCategory()==0){
						if (hql_3.length()>0){
							hql_3.append(" or ");
						}
						hql_3.append(" sgs.groupParentId = ?");
						param_3.add(group.getId());
					}
				}
			}else if (role.getId()==4){//当前用户是教师，查找当前用户所在的班级
				for (int i=0;i<lstGroups.size();i++){
					group=lstGroups.get(i);
					if (group.getGroupCategory()==1){
						if (hql_4.length()>0){
							hql_4.append(" or ");
						}
						hql_4.append(" sgs.id = ?");
						param_4.add(group.getId());
					}
				}
			}
		}
		
		if (hql_3.length()>0&&hql_4.length()>0){
			hql.append(" and (");
			hql.append(hql_3.toString());
			param.addAll(param_3);
			hql.append(" or ");
			hql.append(hql_4.toString());
			param.addAll(param_4);
			hql.append(")");
		}else if (hql_3.length()>0){
			hql.append(" and (");
			hql.append(hql_3.toString());
			param.addAll(param_3);
			hql.append(")");
		}else if (hql_4.length()>0){
			hql.append(" and (");
			hql.append(hql_4.toString());
			param.addAll(param_4);
			hql.append(")");
		}
		
		if (parameters != null && !parameters.isEmpty()) {
			for (String key : parameters.keySet()) {
				if (key=="sort"||key.equals("order")){
					continue;
				}
				
				hql.append(" and ");
				
				//sgs.id in (select sgrs.id from SysGroups sgrs where sgrs.groupParentId in (select sgrs1.id from SysGroups sgrs1 where sgrs1.groupName like ?))
				if (key.equals("schoolName")){//查询条件有报读校区是做特殊处理，要从sys_groups表中去查询group_name
					hql.append("sgs.groupParentId in (select sgrs.id from SysGroups sgrs where sgrs.groupName like ?)");
					param.add("%"+parameters.get("schoolName")+"%");
				}else{
					hql.append(" sus."+key);
					//除id字段外，其它字段模糊查询
					hql.append(key.equals("id")?" = ?":" like ?");
					param.add(key.equals("id")?parameters.get(key):"%"+parameters.get(key)+"%");
				}
			}
			
			//排序
			if (parameters.containsKey("sort")){
				hql.append(" order by sus.");
				hql.append(getSortColumn(Integer.parseInt(""+parameters.get("sort"))));
			}
			if (parameters.containsKey("order")){
				hql.append(" ");
				hql.append(parameters.get("order"));
			}
		}
		
		page=this.findPage(page, hql.toString(), param.toArray());
		return page;
	}

	@Override
	public Pagination<SysUsers> searchDataForStudents(int displayLength, int displayStart,
			Map<String, Object> parameters, Integer userId) {
		SysUsers currentUser=this.get(userId);
		if (currentUser.getSysRoles()==null||currentUser.getSysRoles().isEmpty()
				||currentUser.getSysGroups()==null||currentUser.getSysGroups().isEmpty()){
			return null;
		}
		Pagination<SysUsers> page=new Pagination<SysUsers>();
		
		List<Object> param=new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("select sus from SysUsers sus join sus.sysRoles srs join sus.sysGroups sgs where srs.id = 5");//5是学生角色的ID
		
		page.setFirst(displayStart);
		page.setPageSize(displayLength);
		
		List<SysRoles> lstRoles=currentUser.getSysRoles();
		List<SysGroups> lstGroups=currentUser.getSysGroups();
		SysGroups group=null;
		StringBuffer hql_3 = new StringBuffer();
		List<Object> param_3=new ArrayList<Object>();
		StringBuffer hql_4 = new StringBuffer();
		List<Object> param_4=new ArrayList<Object>();
		for (SysRoles role:lstRoles){
			if (role.getId()==1||role.getId()==2){
				break;
			}else if (role.getId()==3){//当前用户是校区助理，查找校区下的所有班级
				for (int i=0;i<lstGroups.size();i++){
					group=lstGroups.get(i);
					if (group.getGroupCategory()==0){
						if (hql_3.length()>0){
							hql_3.append(" or ");
						}
						hql_3.append(" sgs.groupParentId = ?");
						param_3.add(group.getId());
					}
				}
			}else if (role.getId()==4){//当前用户是教师，查找当前用户所在的班级
				for (int i=0;i<lstGroups.size();i++){
					group=lstGroups.get(i);
					if (group.getGroupCategory()==1){
						if (hql_4.length()>0){
							hql_4.append(" or ");
						}
						hql_4.append(" sgs.id = ?");
						param_4.add(group.getId());
					}
				}
			}
		}
		
		if (hql_3.length()>0&&hql_4.length()>0){
			hql.append(" and (");
			hql.append(hql_3.toString());
			param.addAll(param_3);
			hql.append(" or ");
			hql.append(hql_4.toString());
			param.addAll(param_4);
			hql.append(")");
		}else if (hql_3.length()>0){
			hql.append(" and (");
			hql.append(hql_3.toString());
			param.addAll(param_3);
			hql.append(")");
		}else if (hql_4.length()>0){
			hql.append(" and (");
			hql.append(hql_4.toString());
			param.addAll(param_4);
			hql.append(")");
		}
		
		if (parameters != null && !parameters.isEmpty()) {
			hql.append(" and (");
			
			if (parameters.containsKey("userName")){
				hql.append("sus.userName like ?");
				param.add("%"+parameters.get("userName")+"%");
			}
			if (parameters.containsKey("chineseName")){
				hql.append("sus.chineseName like ?");
				param.add("%"+parameters.get("chineseName")+"%");
			}
			if (parameters.containsKey("schoolName")){
				hql.append(" or sgs.groupParentId in (select sgrs.id from SysGroups sgrs where sgrs.groupName like ?)");
				param.add("%"+parameters.get("schoolName")+"%");
			}
			if (parameters.containsKey("sgs.groupName")){
				hql.append(" or sgs.groupName like ?");
				param.add("%"+parameters.get("sgs.groupName")+"%");
			}
			hql.append(")");
		}
		
		page=this.findPage(page, hql.toString(), param.toArray());
		return page;
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean saveRegisterUser(SysUsers user, SysGroups group, SysRoles role) {
		boolean result=true;
		try{
			this.save(user);
			role.getSysRolesUsers().add(user);
			this.sysRolesManagementDAO.save(role);
			group.getSysGroupsUsers().add(user);
			this.sysGroupsManagementDAO.save(group);
		}catch(Exception ex){
			ex.printStackTrace();
			result=false;
		}
		return result;
	}

	@Override
	public SysUsers findStudentsParent(Integer studentId) {
		SysUsers parent=null;
		StringBuffer hql=new StringBuffer("from SysUsers sus where sus.childrenId = ?");
		List<SysUsers> lst=this.find(hql.toString(), studentId);
		if (lst!=null&&!lst.isEmpty()){
			parent=lst.get(0);
		}
		return parent;
	}

	@Override
	public SysUsers findATutorFromGroup(Integer groupId) {
		StringBuffer hql = new StringBuffer("select sus from SysUsers sus join sus.sysRoles srs join sus.sysGroups sgs where srs.id = 4 and sgs.id = ?");
		return this.findUnique(hql.toString(), groupId);
	}
}
