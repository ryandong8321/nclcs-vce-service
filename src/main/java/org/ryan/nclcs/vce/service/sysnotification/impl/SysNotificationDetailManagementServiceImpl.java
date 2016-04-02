package org.ryan.nclcs.vce.service.sysnotification.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ryan.nclcs.vce.dao.Pagination;
import org.ryan.nclcs.vce.dao.sysnotification.ISysNotificationDetailManagementDAO;
import org.ryan.nclcs.vce.dao.sysnotification.ISysNotificationManagementDAO;
import org.ryan.nclcs.vce.entity.SysNotification;
import org.ryan.nclcs.vce.entity.SysNotificationDetail;
import org.ryan.nclcs.vce.service.NclcsVceServiceBaseServiceImpl;
import org.ryan.nclcs.vce.service.sysnotification.ISysNotificationDetailManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

@Service("sysNotificationDetailManagementService")
public class SysNotificationDetailManagementServiceImpl extends NclcsVceServiceBaseServiceImpl<SysNotificationDetail, Integer, ISysNotificationDetailManagementDAO> implements ISysNotificationDetailManagementService {

	@Autowired
	private ISysNotificationDetailManagementDAO sysNotificationDetailManagementDAO;
	
	@Autowired
	private ISysNotificationManagementDAO sysNotificationManagementDAO;
	
	@Override
	protected ISysNotificationDetailManagementDAO getCurrentDAO() {
		return this.sysNotificationDetailManagementDAO;
	}

	@Override
	public Map<String, Object> searchDataForAjax(int displayLength, int displayStart, int sEcho,
			Map<String, Object> parameter) {
		Map<String, Object> result=new HashMap<String, Object>();
		List<ArrayList<String>> data=new ArrayList<ArrayList<String>>();
		
		Pagination<SysNotification> page=sysNotificationManagementDAO.searchDataForDetail(displayLength, displayStart, sEcho, parameter);
		
		Integer userId=Integer.parseInt(""+parameter.get("snd.detailReceiveUserInfo.id"));
		
		if (page.getRows() != null && !page.getRows().isEmpty()) {
			ArrayList<String> tmp=null;
			int idx=0;
			Integer notificationDetailId=-1;
			List<SysNotificationDetail> lst=null;
			for (SysNotification notification : page.getRows()) {
				tmp=new ArrayList<String>();
				lst=notification.getSysNotificationDetailInfo();
				for (SysNotificationDetail detail:lst){
					try {
						if (detail!=null&&detail.getDetailReceiveUserInfo()!=null&&detail.getDetailReceiveUserInfo().getId()==userId){
							notificationDetailId=detail.getId();
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
				tmp.add("<input type='checkbox' name='id[]' value='"+notificationDetailId+"'/>");
				tmp.add(""+(++idx));
				tmp.add("<a href=\"javascript:showNotification('"+notification.getId()+"')\">"+HtmlUtils.htmlEscape(notification.getNotificationTitle())+"</a>");
				tmp.add(HtmlUtils.htmlEscape(notification.getNotificationMessage()));
				tmp.add("<a href=\"javascript:deleteInfo('"+notificationDetailId+"');\" class=\"btn btn-sm red\"><i class=\"fa fa-times\"></i> DELETE</a>");
				data.add(tmp);
			}
		}
		
		result.put("data", data);
		result.put("recordsTotal", page.getTotal());
		result.put("recordsFiltered", "");
		return result;
	}

	@Override
	public Map<String, Object> deleteMultiple(String notificationDetailIds) {
		return this.getCurrentDAO().deleteMultiple(notificationDetailIds);
	}

	@Override
	public Pagination<SysNotification> searchData(int displayLength, int displayStart, Map<String, Object> parameter) {
		Pagination<SysNotification> page=sysNotificationManagementDAO.searchDataForDetail(displayLength, displayStart, 0, parameter);
		return page;
	}

	@Override
	public Map<String, Object> deleteMultiple(String notificationIds, Integer notificationReceivceUserId) {
		return this.getCurrentDAO().deleteMultiple(notificationIds,notificationReceivceUserId);
	}

}
