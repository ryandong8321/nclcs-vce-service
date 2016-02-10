package org.ryan.nclcs.vce.dao.sysnotification;

import java.util.Map;

import org.ryan.nclcs.vce.dao.INclcsVceServiceBaseDAO;
import org.ryan.nclcs.vce.dao.Pagination;
import org.ryan.nclcs.vce.entity.SysNotification;

public interface ISysNotificationManagementDAO extends INclcsVceServiceBaseDAO<SysNotification, Integer>{
	
	public Pagination<SysNotification> searchData(int displayLength, int displayStart, int sEcho, Map<String, Object> parameter);
	
	public Pagination<SysNotification> searchDataForDetail(int displayLength, int displayStart, int sEcho, Map<String, Object> parameter);
	
	public Map<String, Object> deleteMultiple(String ids);

}
