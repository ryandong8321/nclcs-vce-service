package org.ryan.nclcs.vce.service.sysnotification;

import java.util.Map;

import org.ryan.nclcs.vce.entity.SysNotification;
import org.ryan.nclcs.vce.service.INclcsVceServiceBaseService;

public interface ISysNotificationManagementService extends INclcsVceServiceBaseService<SysNotification, Integer>{
	
	/**
	 * 通知信息查询，用于通知管理列表的ajax请求，可分页
	 * @param displayLength 页面展示数据行数
	 * @param displayStart 数据启始位置
	 * @param sEcho
	 * @param parameter 查询条件
	 * @return
	 */
	public Map<String, Object> searchDataForAjax(int displayLength, int displayStart, int sEcho, Map<String, Object> parameter);
	
	/**
	 * 删除多条通知
	 * @param ids 通知id，以','分隔
	 * @return
	 */
	public Map<String, Object> deleteMultiple(String ids);
}
