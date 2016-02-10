package org.ryan.nclcs.vce.dao.sysnotification.impl;

import java.util.Hashtable;
import java.util.Map;

import org.ryan.nclcs.vce.dao.NclcsVceServiceBaseDAOImpl;
import org.ryan.nclcs.vce.dao.sysnotification.ISysNotificationDetailManagementDAO;
import org.ryan.nclcs.vce.entity.SysNotificationDetail;
import org.springframework.stereotype.Repository;

@Repository("sysNotificationDetailManagementDAO")
public class SysNotificationDetailManagementDAOImpl extends NclcsVceServiceBaseDAOImpl<SysNotificationDetail, Integer> implements ISysNotificationDetailManagementDAO {

	@Override
	public Map<String, Object> deleteMultiple(String notificationIds) {
		String[] ids=null;
		if (notificationIds!=null&&!notificationIds.equals("")&&notificationIds.contains(",")){
			ids=notificationIds.split(",");
		}else if (notificationIds!=null&&!notificationIds.equals("")&&!notificationIds.contains(",")){
			ids=new String[1];
			ids[0]=notificationIds;
		}
		Object[] obj=new Object[ids.length];
		StringBuffer hql=new StringBuffer("delete from SysNotificationDetail snd where id in (");
		for (int i=0;i<ids.length;i++){
			if (i==0){
				hql.append("?");
			}else{
				hql.append(",?");
			}
			obj[i]=Integer.parseInt(ids[i]);
		}
		hql.append(")");
		
		Map<String,Object> result=new Hashtable<String,Object>();
		try{
			this.batchExecute(hql.toString(), obj);
			result.put("status", 1);
			result.put("data", "Operation success.");
		}catch(Exception ex){
			ex.printStackTrace();
			result.put("status", 1);
			result.put("data", "Delete SysNotificationDetail failed, try again.");
		}
		
		return result;
	}

}
