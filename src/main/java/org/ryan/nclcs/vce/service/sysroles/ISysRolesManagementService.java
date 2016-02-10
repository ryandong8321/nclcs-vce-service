package org.ryan.nclcs.vce.service.sysroles;

import java.util.List;
import java.util.Map;

import org.ryan.nclcs.vce.entity.SysRoles;
import org.ryan.nclcs.vce.service.INclcsVceServiceBaseService;

public interface ISysRolesManagementService extends INclcsVceServiceBaseService<SysRoles, Integer>{
	
	/**
	 * 查询角色信息，可分页
	 * @param rowNum
	 * @param pageSize
	 * @param parameters
	 * @param sort
	 * @return
	 */
	public Map<String, Object> findSysRolesByParameters(Integer rowNum, Integer pageSize, Map<String, Object> parameters, Map<String, String> sort);
	
	/**
	 * 批量删除角色
	 * @param ids 多个角色的id，以','分隔
	 * @return
	 */
	public Map<String, Object> deleteRoles(String ids);
	
	/**
	 * 根据角色名称查找角色信息
	 * @param roleName 角色名称
	 * @return
	 */
	public SysRoles findRoleByRoleName(String roleName);
	
	/**
	 * 
	 * @param lstRoles
	 * @return
	 */
	public SysRoles findTheHighestRole(List<?> lstRoles);

}
