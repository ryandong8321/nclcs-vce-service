package org.ryan.nclcs.vce.dao.app.missednotification.impl;

import java.util.List;

import org.ryan.nclcs.vce.dao.NclcsVceServiceBaseDAOImpl;
import org.ryan.nclcs.vce.dao.app.missednotification.IAppMissedNotificationDAO;
import org.ryan.nclcs.vce.entity.AppMissedNotification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("appMissedNotificationDAO")
public class AppMissedNotificationDAOImpl extends NclcsVceServiceBaseDAOImpl<AppMissedNotification, Integer> implements IAppMissedNotificationDAO {

	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean saveMultipleMissedNotification(List<AppMissedNotification> missedNotifications) {
		boolean result=true;
		if (missedNotifications!=null&&!missedNotifications.isEmpty()){
			try {
				for (AppMissedNotification missed:missedNotifications){
					logger.info("saveMultipleMissedNotification missed notification username ["+missed.getSysUserName()+"]");
					this.save(missed);
				}
			} catch (Exception e) {
				result=false;
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public List<AppMissedNotification> findMissedNotification(String userName) {
		List<AppMissedNotification> result=null;
		if (userName!=null&&!userName.isEmpty()){
			String hql="from AppMissedNotification appmissed where appmissed.sysUserName = ?";
			result=this.find(hql, userName);
		}
		return result;
	}

	@Override
	public boolean deleteMissedNotification(String userName) {
		boolean result=true;
		if (userName!=null&&!userName.isEmpty()){
			String hql="delete from AppMissedNotification appmissed where appmissed.sysUserName = ?";
			try {
				this.batchExecute(hql, userName);
			} catch (Exception e) {
				result=false;
				e.printStackTrace();
			}
		}
		return result;
	}

}
