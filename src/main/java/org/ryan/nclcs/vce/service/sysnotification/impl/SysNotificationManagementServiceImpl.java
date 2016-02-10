package org.ryan.nclcs.vce.service.sysnotification.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ryan.nclcs.vce.dao.Pagination;
import org.ryan.nclcs.vce.dao.sysnotification.ISysNotificationManagementDAO;
import org.ryan.nclcs.vce.entity.SysNotification;
import org.ryan.nclcs.vce.service.NclcsVceServiceBaseServiceImpl;
import org.ryan.nclcs.vce.service.sysnotification.ISysNotificationManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("sysNotificationManagementService")
public class SysNotificationManagementServiceImpl extends NclcsVceServiceBaseServiceImpl<SysNotification, Integer, ISysNotificationManagementDAO> implements ISysNotificationManagementService {

	@Autowired
	private ISysNotificationManagementDAO sysNotificationManagementDAO;
	
	@Override
	protected ISysNotificationManagementDAO getCurrentDAO() {
		return this.sysNotificationManagementDAO;
	}

	@Override
	public Map<String, Object> searchDataForAjax(int displayLength, int displayStart, int sEcho,
			Map<String, Object> parameter) {
		Map<String, Object> result=new HashMap<String, Object>();
		List<ArrayList<String>> data=new ArrayList<ArrayList<String>>();
		
		Pagination<SysNotification> page=this.getCurrentDAO().searchData(displayLength, displayStart, sEcho, parameter);
		
		if (page.getRows() != null && !page.getRows().isEmpty()) {
			ArrayList<String> tmp=null;
			int idx=0;
			for (SysNotification notification : page.getRows()) {
				tmp=new ArrayList<String>();
				tmp.add("<input type='checkbox' name='id[]' value='"+notification.getId()+"'/>");
				tmp.add(""+(++idx));
				tmp.add("<a href=\"javascript:showNotification('"+notification.getId()+"')\">"+notification.getNotificationTitle()+"</a>");
				tmp.add(notification.getNotificationMessage());
				if (notification.getIsSend()==0){
					tmp.add("<a href=\"javascript:deleteInfo('"+notification.getId()+"');\" class=\"btn btn-sm red\"><i class=\"fa fa-times\"></i> DELETE</a>"
							+"&nbsp;<a href=\"javascript:sendNotification('"+notification.getId()+"');\" class=\"btn btn-sm green\"><i class=\"fa fa-bullhorn\"></i> SEND</a>");
				}else if (notification.getIsSend()==1){
					tmp.add("<a href=\"javascript:deleteInfo('"+notification.getId()+"');\" class=\"btn btn-sm red\"><i class=\"fa fa-times\"></i> DELETE</a>"
							+"&nbsp;<span class=\"label label-success\">SENDED </span>");
				}
				
				data.add(tmp);
			}
		}
		
		result.put("data", data);
		result.put("recordsTotal", page.getTotal());
		result.put("recordsFiltered", "");
		return result;
	}

	@Override
	public Map<String, Object> deleteMultiple(String ids) {
		return this.getCurrentDAO().deleteMultiple(ids);
	}
}
