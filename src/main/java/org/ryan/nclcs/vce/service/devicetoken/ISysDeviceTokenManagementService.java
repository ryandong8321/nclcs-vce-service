package org.ryan.nclcs.vce.service.devicetoken;

import java.util.List;

import org.ryan.nclcs.vce.entity.SysDeviceToken;
import org.ryan.nclcs.vce.service.INclcsVceServiceBaseService;

public interface ISysDeviceTokenManagementService extends INclcsVceServiceBaseService<SysDeviceToken, Integer>{
	
	public boolean setNewDeviceToken(Integer userId, String deviceToken);
	
	public List<SysDeviceToken> findDeviceTokenByUserId(List<Integer> userId);
	
	public boolean sendNotificationToApp(List<SysDeviceToken> deviceTokens, String text, String titel, String ticker);
	
	public boolean sendDelayNotificationToApp(String userName);
}
