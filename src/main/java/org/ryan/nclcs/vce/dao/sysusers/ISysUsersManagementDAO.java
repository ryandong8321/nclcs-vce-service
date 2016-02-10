package org.ryan.nclcs.vce.dao.sysusers;

import java.util.List;
import java.util.Map;

import org.ryan.nclcs.vce.dao.INclcsVceServiceBaseDAO;
import org.ryan.nclcs.vce.dao.Pagination;
import org.ryan.nclcs.vce.entity.SysUsers;

public interface ISysUsersManagementDAO extends INclcsVceServiceBaseDAO<SysUsers, Integer>{
	
	public Pagination<SysUsers> searchData(int displayLength, int displayStart, int sEcho, Map<String, Object> parameters);
	
	/**
	 * 查找学生数据，为学生信息管理和学生成绩管理使用，可分页
	 * @param displayLength 每页数据条数
	 * @param displayStart 数据起启位置
	 * @param sEcho
	 * @param parameters 查询条件
	 * @param userId 当前登录用户ID
	 * @return
	 */
	public Pagination<SysUsers> searchDataForStudents(int displayLength, int displayStart, int sEcho, Map<String, Object> parameters, Integer userId);
	
	/**
	 * 查找学生数据，为APP中学生信息管理和学生成绩管理使用，可分页
	 * @param displayLength 每页数据条数
	 * @param displayStart 数据起启位置
	 * @param parameters 查询条件
	 * @param userId 当前登录用户ID
	 * @return
	 */
	public Pagination<SysUsers> searchDataForStudents(int displayLength, int displayStart, Map<String, Object> parameters, Integer userId);
	
	public List<SysUsers> findAllSysUsersForPrivilege(Map<String, Object> parameters, int intRoleOrGroup);
	
	public Map<String, Object> deleteUsers(String ids);
	
	public List<SysUsers> findSysUsersInGroups(List<Object> groupIds);
	
}
