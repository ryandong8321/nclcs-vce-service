package org.ryan.nclcs.vce.service.appmissednotification.impl;

import java.util.List;

import org.ryan.nclcs.vce.dao.app.missednotification.IAppMissedNotificationDAO;
import org.ryan.nclcs.vce.entity.AppMissedNotification;
import org.ryan.nclcs.vce.service.NclcsVceServiceBaseServiceImpl;
import org.ryan.nclcs.vce.service.appmissednotification.IAppMissedNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("appMissedNotificationService")
public class AppMissedNotificationServiceImpl extends NclcsVceServiceBaseServiceImpl<AppMissedNotification, Integer, IAppMissedNotificationDAO> implements IAppMissedNotificationService {

	@Autowired
	private IAppMissedNotificationDAO appMissedNotificationDAO;
	
	@Override
	protected IAppMissedNotificationDAO getCurrentDAO() {
		return this.appMissedNotificationDAO;
	}

	@Override
	public boolean saveMultipleMissedNotification(List<AppMissedNotification> missedNotifications) {
		return this.getCurrentDAO().saveMultipleMissedNotification(missedNotifications);
	}

	@Override
	public List<AppMissedNotification> findMissedNotification(String userName) {
		List<AppMissedNotification> result=null;
		try{
			result=this.getCurrentDAO().findMissedNotification(userName);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean deleteMissedNotification(String userName) {
		return this.getCurrentDAO().deleteMissedNotification(userName);
	}
	
	

}
