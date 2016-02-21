package org.ryan.nclcs.vce.dao.sysnotification.impl;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.ryan.nclcs.vce.dao.NclcsVceServiceBaseDAOImpl;
import org.ryan.nclcs.vce.dao.sysnotification.ISysNotificationDetailManagementDAO;
import org.ryan.nclcs.vce.entity.SysNotificationDetail;
import org.springframework.stereotype.Repository;

@Repository("sysNotificationDetailManagementDAO")
public class SysNotificationDetailManagementDAOImpl extends NclcsVceServiceBaseDAOImpl<SysNotificationDetail, Integer> implements ISysNotificationDetailManagementDAO {

	@Override
	public Map<String, Object> deleteMultiple(String notificationDetailIds) {
		String[] ids=null;
		if (notificationDetailIds!=null&&!notificationDetailIds.equals("")&&notificationDetailIds.contains(",")){
			ids=notificationDetailIds.split(",");
		}else if (notificationDetailIds!=null&&!notificationDetailIds.equals("")&&!notificationDetailIds.contains(",")){
			ids=new String[1];
			ids[0]=notificationDetailIds;
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
			result.put("status", 0);
			result.put("data", "Delete SysNotificationDetail failed, try again.");
		}
		
		return result;
	}

	@Override
	public Map<String, Object> deleteMultiple(String notificationIds, Integer notificationReceivceUserId) {
		String[] ids=null;
		if (notificationIds!=null&&!notificationIds.equals("")&&notificationIds.contains(",")){
			ids=notificationIds.split(",");
		}else if (notificationIds!=null&&!notificationIds.equals("")&&!notificationIds.contains(",")){
			ids=new String[1];
			ids[0]=notificationIds;
		}

		List<Object> param=new ArrayList<Object>();
		StringBuffer hql=new StringBuffer("delete from SysNotificationDetail snd where snd.detailNotificationInfo.id in (");
		for (int i=0;i<ids.length;i++){
			if (i==0){
				hql.append("?");
			}else{
				hql.append(",?");
			}
			param.add(Integer.parseInt(ids[i]));
		}
		hql.append(")");
		
		hql.append(" and snd.detailReceiveUserInfo.id = ?");
		param.add(notificationReceivceUserId);
		
		Map<String,Object> result=new Hashtable<String,Object>();
		try{
			this.batchExecute(hql.toString(), param.toArray());
			result.put("status", 1);
			result.put("data", "Operation success.");
		}catch(Exception ex){
			ex.printStackTrace();
			result.put("status", 0);
			result.put("data", "Delete SysNotificationDetail failed, try again.");
		}
		
		return result;
	}

}
