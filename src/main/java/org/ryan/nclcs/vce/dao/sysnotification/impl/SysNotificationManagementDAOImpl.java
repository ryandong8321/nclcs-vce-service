package org.ryan.nclcs.vce.dao.sysnotification.impl;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.ryan.nclcs.vce.dao.NclcsVceServiceBaseDAOImpl;
import org.ryan.nclcs.vce.dao.Pagination;
import org.ryan.nclcs.vce.dao.sysnotification.ISysNotificationManagementDAO;
import org.ryan.nclcs.vce.entity.SysNotification;
import org.springframework.stereotype.Repository;

@Repository("sysNotificationManagementDAO")
public class SysNotificationManagementDAOImpl extends NclcsVceServiceBaseDAOImpl<SysNotification, Integer> implements ISysNotificationManagementDAO {

	@Override
	public Pagination<SysNotification> searchData(int displayLength, int displayStart, int sEcho,
			Map<String, Object> parameters) {
		Pagination<SysNotification> page=new Pagination<SysNotification>();
		
		Object[] param = null;
		StringBuffer hql = new StringBuffer("from SysNotification snn ");
		
		page.setFirst(displayStart);
		page.setPageSize(displayLength);
		
		if (parameters != null && !parameters.isEmpty()) {
			Object[] tmp = new Object[parameters.size()];
			int ind = 0;
			
			for (String key : parameters.keySet()) {
				if (key=="sort"||key.equals("order")){
					continue;
				}
				
				if (ind==0){
					hql.append(" where ");
				}else{
					hql.append(" and ");
				}
//				hql.append("sus.");
				hql.append(key);
				//除id字段外，其它字段模糊查询
				hql.append(key.contains("id")?" = ?":" like ?");
//				param[ind++]=key.equals("id")?parameters.get(key):"%"+parameters.get(key)+"%";
				tmp[ind++]=key.contains("id")?parameters.get(key):"%"+parameters.get(key)+"%";
			}
			
			if (parameters.containsKey("sort")){
				hql.append(" order by snn.");
				hql.append(getSortColumn(Integer.parseInt(""+parameters.get("sort"))));
			}
			if (parameters.containsKey("order")){
				hql.append(" ");
				hql.append(parameters.get("order"));
			}
			
			if (ind>0){
				param=new Object[ind];
				int idx=0;
				for (Object obj:tmp){
					if (obj==null){
						continue;
					}
					param[idx++]=obj;
				}
			}
		}
		
		page= this.findPage(page, hql.toString(), param);
		
		return page;
	}
	
	private String getSortColumn(int index){
		List<String> lstColumn=new ArrayList<String>();
		lstColumn.add("id");
		lstColumn.add("notificationTitle");
		lstColumn.add("notificationMessage");
		
		return lstColumn.get(index-1);
	}

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
		StringBuffer hql=new StringBuffer("delete from SysNotification snn where id in (");
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
			result.put("data", "Delete Notification failed, try again.");
		}
		
		return result;
	}

	@Override
	public Pagination<SysNotification> searchDataForDetail(int displayLength, int displayStart, int sEcho,
			Map<String, Object> parameters) {
		Pagination<SysNotification> page=new Pagination<SysNotification>();
		
		Object[] param = null;
		StringBuffer hql = new StringBuffer("select distinct snn from SysNotification snn join snn.sysNotificationDetailInfo snd ");
		
		page.setFirst(displayStart);
		page.setPageSize(displayLength);
		
		if (parameters != null && !parameters.isEmpty()) {
			Object[] tmp = new Object[parameters.size()];
			int ind = 0;
			
			for (String key : parameters.keySet()) {
				if (key=="sort"||key.equals("order")){
					continue;
				}
				
				if (ind==0){
					hql.append(" where ");
				}else{
					hql.append(" and ");
				}
//				hql.append("sus.");
				hql.append(key);
				//除id字段外，其它字段模糊查询
				hql.append(key.contains("id")?" = ?":" like ?");
//				param[ind++]=key.equals("id")?parameters.get(key):"%"+parameters.get(key)+"%";
				tmp[ind++]=key.contains("id")?parameters.get(key):"%"+parameters.get(key)+"%";
			}
			
			if (parameters.containsKey("sort")){
				hql.append(" order by snn.");
				hql.append(getSortColumn(Integer.parseInt(""+parameters.get("sort"))));
			}
			if (parameters.containsKey("order")){
				hql.append(" ");
				hql.append(parameters.get("order"));
			}
			
			if (ind>0){
				param=new Object[ind];
				int idx=0;
				for (Object obj:tmp){
					if (obj==null){
						continue;
					}
					param[idx++]=obj;
				}
			}
		}
		
		page= this.findPage(page, hql.toString(), param);
		
		return page;
	}

}
