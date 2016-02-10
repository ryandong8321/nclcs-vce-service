package org.ryan.nclcs.vce.service.appstudents;

import java.util.Map;

import org.ryan.nclcs.vce.entity.SysUsers;
import org.ryan.nclcs.vce.service.INclcsVceServiceBaseService;

public interface IAppStudentsManagementService extends INclcsVceServiceBaseService<SysUsers, Integer>{
	
	/**
	 * 查找学生数据，为学生信息管理和学生成绩管理使用，可分页
	 * @param displayLength 每页数据条数
	 * @param displayStart 数据起启位置
	 * @param sEcho
	 * @param parameters 查询条件
	 * @param userId 当前登录用户ID
	 * @param isShowScore 是否是为学生成绩管理的数据列表使用的
	 * @return Map<String, Object>
	 */
	public Map<String, Object> searchDataForAjax(int displayLength, int displayStart, int sEcho, Map<String, Object> parameters, Integer userId, boolean isShowScore);
	
	
	/**
	 * 查找学生数据，为APP中学生信息管理和学生成绩管理使用，可分页
	 * @param displayLength 每页数据条数
	 * @param displayStart 数据起启位置
	 * @param parameters 查询条件
	 * @param userId 当前登录用户ID
	 * @param isShowScore 是否是为学生成绩管理的数据列表使用的
	 * @return
	 */
	public Map<String, Object> searchDataForApp(int displayLength, int displayStart, Map<String, Object> parameters, Integer userId, boolean isShowScore);

}
