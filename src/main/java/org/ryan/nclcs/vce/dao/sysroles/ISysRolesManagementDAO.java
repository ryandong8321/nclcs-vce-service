package org.ryan.nclcs.vce.dao.sysroles;

import java.util.Map;

import org.ryan.nclcs.vce.dao.INclcsVceServiceBaseDAO;
import org.ryan.nclcs.vce.entity.SysRoles;

public interface ISysRolesManagementDAO extends INclcsVceServiceBaseDAO<SysRoles, Integer>{
	
	public Map<String, Object> deleteRoles(String ids);

}
