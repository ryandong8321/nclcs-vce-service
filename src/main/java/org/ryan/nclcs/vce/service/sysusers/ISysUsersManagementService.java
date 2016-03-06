package org.ryan.nclcs.vce.service.sysusers;

import java.util.List;
import java.util.Map;

import org.ryan.nclcs.vce.entity.SysGroups;
import org.ryan.nclcs.vce.entity.SysRoles;
import org.ryan.nclcs.vce.entity.SysUsers;
import org.ryan.nclcs.vce.service.INclcsVceServiceBaseService;

public interface ISysUsersManagementService extends INclcsVceServiceBaseService<SysUsers, Integer>{
	
	/**
	 * 
	 * @param displayLength
	 * @param displayStart
	 * @param sEcho
	 * @param parameters
	 * @return
	 */
	public Map<String, Object> searchDataForAjax(int displayLength, int displayStart, int sEcho, Map<String, Object> parameters);
	
	/**
	 * 
	 * @param ids
	 * @return
	 */
	public Map<String, Object> deleteMultiple(String ids);
	
	/**
	 * 根据查询条件查找用户，主要用于授权操作时过滤用户
	 * @param parameters 查询条件
	 * @return
	 */
	public List<Map<String, Object>> findAllSysUsersByPrivilege(Map<String, Object> parameters);
	
	/**
	 * 根据查询条件查找用户，主要用于授权操作时过滤用户
	 * @param parameters 查询条件
	 * @param groupId 用于分配用户的群组id
	 * @return
	 */
	public List<Map<String, Object>> findAllSysUsersByPrivilege(Map<String, Object> parameters, Integer groupId);
	
	/**
	 * 根据查询条件查找用户是否存在
	 * @param parameters 查询条件
	 * @return
	 */
	public SysUsers isExistByParameters(Map<String,Object> parameters);
	
	public List<Map<String, Object>> findAllSysUsersByParameters(Map<String, Object> parameters);
	
	public List<Map<String, Object>> findUsersByGroupIds(List<Integer> campusIds, List<Integer> classIds);
	
	public boolean saveRegisterUser(SysUsers user, SysGroups group, SysRoles role);
	
	public SysUsers findATutorFromGroup(Integer groupId);

}
