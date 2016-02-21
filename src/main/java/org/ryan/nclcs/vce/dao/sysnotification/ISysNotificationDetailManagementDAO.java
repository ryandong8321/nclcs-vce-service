package org.ryan.nclcs.vce.dao.sysnotification;

import java.util.Map;

import org.ryan.nclcs.vce.dao.INclcsVceServiceBaseDAO;
import org.ryan.nclcs.vce.entity.SysNotificationDetail;

public interface ISysNotificationDetailManagementDAO extends INclcsVceServiceBaseDAO<SysNotificationDetail, Integer>{
	
	public Map<String, Object> deleteMultiple(String notificationDetailIds);
	
	public Map<String, Object> deleteMultiple(String notificationIds, Integer notificationReceivceUserId);

}
