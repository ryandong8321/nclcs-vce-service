package org.ryan.nclcs.vce.service.sysroles.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ryan.nclcs.vce.dao.Pagination;
import org.ryan.nclcs.vce.dao.sysroles.ISysRolesManagementDAO;
import org.ryan.nclcs.vce.entity.SysRoles;
import org.ryan.nclcs.vce.service.NclcsVceServiceBaseServiceImpl;
import org.ryan.nclcs.vce.service.sysroles.ISysRolesManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("sysRolesManagementService")
public class SysRolesManagementServiceImpl extends NclcsVceServiceBaseServiceImpl<SysRoles, Integer, ISysRolesManagementDAO> implements ISysRolesManagementService {

	@Autowired
	private ISysRolesManagementDAO sysRolesManagementDAO;
	
	@Override
	protected ISysRolesManagementDAO getCurrentDAO() {
		return this.sysRolesManagementDAO;
	}

	@Override
	public SysRoles findRoleByRoleName(String roleName) {
		StringBuffer hql=new StringBuffer("from SysRoles srs where srs.roleName = ?");
		List<Object> lst=this.getCurrentDAO().find(hql.toString(), roleName);
		return lst!=null&&!lst.isEmpty()?(SysRoles)lst.get(0):null;
	}

	@Override
	public Map<String, Object> findSysRolesByParameters(Integer rowNum, Integer pageSize,
			Map<String, Object> parameters, Map<String, String> sort) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Object[] param = null;
		StringBuffer hql = new StringBuffer("from SysRoles srs ");

		Pagination<SysRoles> page = new Pagination<SysRoles>();
		page.setFirst(rowNum);
		page.setPageSize(pageSize);

		if (parameters != null && !parameters.isEmpty()) {
			hql.append(" where ");
			param = new Object[parameters.size()];
			int ind = 0;
			
			for (String key : parameters.keySet()) {
				if (ind!=0){
					hql.append(" or ");
				}
				hql.append(key);
				
				hql.append(" like ?");
				param[ind++] = "%"+parameters.get(key)+"%";
			}
		}
		
		if (sort!=null&&!sort.isEmpty()){
			hql.append(" order by ");
			hql.append(sort.get("sort"));
			hql.append(" ");
			hql.append(sort.get("order"));
		}
		
		page= this.getCurrentDAO().findPage(page, hql.toString(), param);

		if (page.getRows() != null && !page.getRows().isEmpty()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (SysRoles role : page.getRows()) {
				map = new HashMap<String, Object>();
				map.put("id", role.getId());
				map.put("roleName", role.getRoleName());
				map.put("roleMemo", role.getRoleMemo());
				result.add(map);
			}
		}

		resultMap.put("total", page.getTotal());
		resultMap.put("rows", result);
		return resultMap;
	}

	@Override
	public Map<String, Object> deleteRoles(String ids) {
		return this.getCurrentDAO().deleteRoles(ids);
	}

	@Override
	public SysRoles findTheHighestRole(List<?> lstRoles) {
		SysRoles highestRole=null;
		SysRoles role=null;
		if (lstRoles!=null&&!lstRoles.isEmpty()){
			try {
				for (int i=0;i<lstRoles.size();i++){
					role=(SysRoles)lstRoles.get(i);
					if (highestRole==null){
						highestRole=role;
					}
					if (role.getId()==1){
						break;
					}
					if (highestRole.getId()>role.getId()){
						highestRole=role;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				highestRole=null;
			}
		}
		return highestRole;
	}

}
