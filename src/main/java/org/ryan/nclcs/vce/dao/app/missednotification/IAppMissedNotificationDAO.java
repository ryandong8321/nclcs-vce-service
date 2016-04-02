package org.ryan.nclcs.vce.dao.app.missednotification;

import java.util.List;

import org.ryan.nclcs.vce.dao.INclcsVceServiceBaseDAO;
import org.ryan.nclcs.vce.entity.AppMissedNotification;

public interface IAppMissedNotificationDAO extends INclcsVceServiceBaseDAO<AppMissedNotification, Integer>{
	
	public boolean saveMultipleMissedNotification(List<AppMissedNotification> missedNotifications); 
	
	public List<AppMissedNotification> findMissedNotification(String userName);
	
	public boolean deleteMissedNotification(String userName);

}
