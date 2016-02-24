package org.ryan.nclcs.vce.service.sysgroups;

import java.util.List;
import java.util.Map;

import org.ryan.nclcs.vce.entity.SysGroups;
import org.ryan.nclcs.vce.entity.SysUsers;
import org.ryan.nclcs.vce.service.INclcsVceServiceBaseService;

public interface ISysGroupsManagementService extends INclcsVceServiceBaseService<SysGroups, Integer>{
	
	/**
	 * 查群组信息，只为修改学生信息时的校区和班级使用
	 * @param parameters 查询条件
	 * @param currentUserId 当前登录用户ID
	 * @return Map里的key包括id,text
	 */
	public List<Map<String,Object>> findGroup(Map<String, Object> parameters, Integer currentUserId);
	
	/**
	 * 查群组信息，可分页，目前还没用到
	 * @param rowNum
	 * @param pageSize
	 * @param parameters
	 * @return
	 */
	public Map<String, Object> findGroup(Integer rowNum, Integer pageSize, Map<String, Object> parameters);
	
	/**
	 * 递规调用，查找群组的全部父类
	 * @param groupId
	 * @param parentId
	 * @return
	 */
	public String findGroupParents(Integer groupId, Integer parentId);
	
	/**
	 * 为jstree树型控件提供数据
	 * @param parameters 查询条件
	 * @return Map的key包括id,text,parent,还可包括一个名为state的子Map，子Map的key包括opened,selected
	 */
	public List<Map<String,Object>> findGroupsForTree(Map<String, Object> parameters);
	
	/**
	 * 为jstree树型控件提供数据，主要针对发送通知时选择校区和班级使用
	 * @param parameters 查询条件，Map的key包括groupId, groupCategory
	 * @return Map的key包括id,text,parent,还可包括一个名为state的子Map，子Map的key包括opened,selected
	 */
	public List<Map<String,Object>> findGroupsForTree(List<Map<String,Integer>> parameters);
	
	/**
	 * 批量删除群组
	 * @param groupIds 多个群组的id，以','分隔
	 * @return
	 */
	public boolean deleteGroups(String groupIds);
	
	/**
	 * 查找群组下的用户数据
	 * @param groupIds 要查询的群组id，以','分隔
	 * @param roleCategory 1:全查，2:查校区(用户所在)和班级，3:查班级
	 * @param lstGroups 当前登录用户的所在的群组List
	 * @return
	 */
	public List<SysUsers> findSysUsersInGroups(String groupIds, int roleCategory, List<SysGroups> lstGroups);
	
	/**
	 * 保存用户的群组授权
	 * @param groupId 用户被分配群组id
	 * @param userIds 用户id，以','分隔
	 * @return 操作是否成功
	 */
	public Map<String, Object> saveRelationship(String groupId, String userIds);
	
	/**
	 * 保存学生校区或班级发生的变化，同时发送通知
	 * @param studentUser 学生的SysUsers对象
	 * @param changeToGroupId 要变更的班级id
	 * @param currentOperationUser 当前登录用户的SysUsers对象
	 * @return
	 */
	public Map<String, Object> saveStudentGroupChange(SysUsers studentUser, Integer changeToGroupId, SysUsers currentOperationUser);
	
	public List<Map<String, Object>> findAllCampusOrClass(Map<String, Object> parameters, Integer groupCategory);

}
