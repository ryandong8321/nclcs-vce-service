package org.ryan.nclcs.vce.service.appmissednotification;

import java.util.List;

import org.ryan.nclcs.vce.entity.AppMissedNotification;
import org.ryan.nclcs.vce.service.INclcsVceServiceBaseService;

public interface IAppMissedNotificationService extends INclcsVceServiceBaseService<AppMissedNotification, Integer>{
	
	public boolean saveMultipleMissedNotification(List<AppMissedNotification> missedNotifications);
	
	public List<AppMissedNotification> findMissedNotification(String userName);
	
	public boolean deleteMissedNotification(String userName);

}
