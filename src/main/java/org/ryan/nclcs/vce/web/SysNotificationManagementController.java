package org.ryan.nclcs.vce.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ryan.nclcs.vce.annotation.SystemLogIsCheck;
import org.ryan.nclcs.vce.annotation.SystemUserLoginIsCheck;
import org.ryan.nclcs.vce.entity.SysDeviceToken;
import org.ryan.nclcs.vce.entity.SysGroups;
import org.ryan.nclcs.vce.entity.SysNotification;
import org.ryan.nclcs.vce.entity.SysNotificationDetail;
import org.ryan.nclcs.vce.entity.SysRoles;
import org.ryan.nclcs.vce.entity.SysUsers;
import org.ryan.nclcs.vce.service.devicetoken.ISysDeviceTokenManagementService;
import org.ryan.nclcs.vce.service.sysgroups.ISysGroupsManagementService;
import org.ryan.nclcs.vce.service.sysnotification.ISysNotificationDetailManagementService;
import org.ryan.nclcs.vce.service.sysnotification.ISysNotificationManagementService;
import org.ryan.nclcs.vce.service.sysusers.ISysUsersManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/sysnotificationmanagement")
public class SysNotificationManagementController {
	
	protected static Logger logger = LoggerFactory.getLogger(SysNotificationManagementController.class);
	
	@Autowired
	private ISysNotificationManagementService sysNotificationManagementService;
	
	@Autowired
	private ISysNotificationDetailManagementService sysNotificationDetailManagementService;
	
	@Autowired
	private ISysGroupsManagementService sysGroupsManagementService;
	
	@Autowired
	private ISysUsersManagementService sysUsersManagementService;
	
	@Autowired
	private ISysDeviceTokenManagementService DeviceTokenManagementService;
	
	@RequestMapping(value = "/sysnotificationlist.do")
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="通知信息查询")
	public String sysNotificationList(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		logger.info("this is [sysnotificationlist.do] start ...");
		logger.info("this is [sysnotificationlist.do] end ...");
		return "sysnotification/sysnotificationlist";
	}
	
	@RequestMapping(value = "/initsysnotificationtable.do")
	@ResponseBody
	@SystemLogIsCheck(description="通知信息查询")
	public String initSysNotificationTable(HttpServletRequest request) {
		logger.info("this is [initsysnotificationtable.do] start ...");
		
		int displayLength = ServletRequestUtils.getIntParameter(request, "length", 0), //每页大小
			displayStart = ServletRequestUtils.getIntParameter(request, "start", 0), //起始值
			sEcho = ServletRequestUtils.getIntParameter(request, "draw", 0),
			notificationId=ServletRequestUtils.getIntParameter(request, "sysnotification_id", -1);
		
		
		logger.info("this is [initsysnotificationtable.do] requset pram [displayLength = "+displayLength+"],[displayStart = {"+displayStart+"}],[sEcho = {"+sEcho+"}]");
		
		String title=ServletRequestUtils.getStringParameter(request, "sysnotification_title", null), 
			message=ServletRequestUtils.getStringParameter(request, "sysnotification_message", null), 
			sort=ServletRequestUtils.getStringParameter(request, "order[0][column]", null),
			dir=ServletRequestUtils.getStringParameter(request, "order[0][dir]", null);
		
		logger.info("this is [initsysnotificationtable.do] requset pram [title = {"+title+"}],[message = {"+message+"}]"
				+ ",[sort = {"+sort+"}],[dir = {"+dir+"}]");
		
		Map<String, Object> parameters=new HashMap<String, Object>();
		if (title!=null&&!title.equals("")){
			parameters.put("notificationTitle", title);
		}
		if (notificationId!=-1){
			parameters.put("id", notificationId);
		}
		if (message!=null&&!message.equals("")){
			parameters.put("notificationMessage", message);
		}
		
		if (sort!=null&&!sort.equals("")){
			parameters.put("sort", sort);
		}else{
			parameters.put("sort", 4);
		}
		if (dir!=null&&!dir.equals("")){
			parameters.put("order", dir);
		}else{
			parameters.put("order", "desc");
		}
		
		if (request.getSession().getAttribute("u_id")!=null){
			Integer userId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
			parameters.put("notificationUserInfo.id", userId);
		}
		
		Map<String,Object> returnData=sysNotificationManagementService.searchDataForAjax(displayLength, displayStart, sEcho, parameters);
		
		returnData.put("draw", sEcho);
		
		String result=JSONObject.fromObject(returnData).toString();
		logger.info("this is [initsysnotificationtable.do] data ["+result+"] ...");
		logger.info("this is [initsysnotificationtable.do] end ...");
		return result;
	}
	
	@RequestMapping(value = "/showsysnotificationinfo.do", method=RequestMethod.POST)
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="显示通知信息")
	public String showSysNotificationInfo(HttpServletRequest request, Integer notificationId) {
		logger.info("this is [showsysnotificationinfo.do] start ...");
		logger.info("this is [showsysnotificationinfo.do] userId ["+notificationId+"] ...");
		SysNotification notification=null;
		try{
			if (notificationId!=null&&notificationId!=-1){
				logger.info("this is [showsysnotificationinfo.do] find user ...");
				notification=sysNotificationManagementService.get(notificationId);
			}
		}catch(Exception ex){
			logger.info("this is [showsysnotificationinfo.do] find user failed...");
			ex.printStackTrace();
		}
		
		logger.info("this is [showsysnotificationinfo.do] data ["+notification+"] ...");
		request.setAttribute("sysnotification", notification);
		logger.info("this is [showsysnotificationinfo.do] end ...");
		return "sysnotification/sysnotificationinfo";
	}
	
	@RequestMapping(value = "/showsysnotificationdetailinfo.do", method=RequestMethod.POST)
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="显示通知详细信息")
	public String showSysNotificationDetailInfo(HttpServletRequest request, Integer notificationId) {
		logger.info("this is [showsysnotificationinfo.do] start ...");
		logger.info("this is [showsysnotificationinfo.do] userId ["+notificationId+"] ...");
		SysNotification notification=null;
		try{
			if (notificationId!=null&&notificationId!=-1){
				logger.info("this is [showsysnotificationinfo.do] find user ...");
				notification=sysNotificationManagementService.get(notificationId);
			}
		}catch(Exception ex){
			logger.info("this is [showsysnotificationinfo.do] find user failed...");
			ex.printStackTrace();
		}
		
		logger.info("this is [showsysnotificationinfo.do] data ["+notification+"] ...");
		request.setAttribute("sysnotification", notification);
		logger.info("this is [showsysnotificationinfo.do] end ...");
		return "sysnotification/sysnotificationdetailinfo";
	}
	
	@RequestMapping(value = "/savesysnotification.do", method=RequestMethod.POST)
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="保存通知信息")
	public String saveSysNotification(HttpServletRequest request, @ModelAttribute("sysnotification") SysNotification sysNotification, Integer notificationId) {
		logger.info("this is [savesysnotification.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		if (notificationId!=null&&notificationId!=0){
			sysNotification.setId(notificationId);
		}
			
		try{
			logger.info("this is [savesysnotification.do] is saving ...");
			
			if (request.getSession().getAttribute("u_id")==null||request.getSession().getAttribute("u_sr")==null||request.getSession().getAttribute("u_sg")==null){
				logger.info("this is [savesysnotification.do] there is no login info ...");
				return "redirect:/index.jsp";
			}else{
				SysUsers user=new SysUsers();
				user.setId(Integer.parseInt(""+request.getSession().getAttribute("u_id")));
				sysNotification.setNotificationUserInfo(user);
				
				logger.info("this is [savesysnotification.do] is saving...");
				sysNotificationManagementService.save(sysNotification);
				result.put("status", 1);
				result.put("data", "operation success!");
				logger.info("this is [savesysnotification.do] save sysNotification done ...");
				
//				if (sysNotification.getNotificationReceiveGroupIds()!=null&&!sysNotification.getNotificationReceiveGroupIds().equals("")){
//					logger.info("this is [savesysnotification.do] get [u_sr] ...");
//					int roleCategory=-1;//1:全查，2:查校区(用户所在)和班级，3:查班级
//					List<SysRoles> lstRoles=(List<SysRoles>)request.getSession().getAttribute("u_sr");
//					for (SysRoles role:lstRoles){
//						if (role.getId()==1||role.getId()==2){//管理者|管理助理
//							roleCategory=1;
//							break;
//						}else if (role.getId()==3){//校区助理
//							roleCategory=2;
//						}else if (role.getId()==4){//教师
//							roleCategory=3;
//						}
//					}
//					logger.info("this is [savesysnotification.do] get [u_sr] is ["+roleCategory+"]...");
//					List <SysGroups> lstGroups=null;
//					if (roleCategory==-1){
//						result.put("status",0);
//						result.put("data", "用户权限错误，请重新登录！");
//					}else if (roleCategory!=1){
//						lstGroups=(List<SysGroups>)request.getSession().getAttribute("u_sg");
//					}
//					
//					List<SysUsers> lstReceiveUsers=sysGroupsManagementService.findSysUsersInGroups(sysNotification.getNotificationReceiveGroupIds(), roleCategory, lstGroups);
//					
//					SysUsers user=new SysUsers();
//					user.setId(Integer.parseInt(""+request.getSession().getAttribute("u_id")));
//					sysNotification.setNotificationUserInfo(user);
//					
//					SysNotificationDetail notificationDetail=null;
//					List<SysNotificationDetail> details=new ArrayList<SysNotificationDetail>();
//					for (SysUsers receiveUser:lstReceiveUsers){
//						if (receiveUser.getId().equals(user.getId())){
//							continue;
//						}
//						notificationDetail=new SysNotificationDetail();
//						notificationDetail.setDetailReceiveUserInfo(receiveUser);
//						logger.info("this is [savesysnotification.do] get [receive_user] is ["+receiveUser.getUserName()+"]...");
//						notificationDetail.setIsRead(0);
//						notificationDetail.setDetailNotificationInfo(sysNotification);
//						details.add(notificationDetail);
//					}
//					sysNotification.setSysNotificationDetailInfo(details);
//					logger.info("this is [savesysnotification.do] is saving...");
//					sysNotificationManagementService.save(sysNotification);
//					result.put("status", 1);
//					result.put("data", "operation success!");
//					logger.info("this is [savesysnotification.do] save sysNotification done ...");
//				}
			}
		}catch(Exception ex){
			logger.info("this is [savesysnotification.do] save sysNotification error ...");
			result.put("status", 0);
			result.put("data", "save failed, try again!");
			ex.printStackTrace();
		}
		logger.info("this is [savesysnotification.do] show result ["+result+"] ...");
		request.setAttribute("result", result.get("data"));
		request.setAttribute("sysnotification", sysNotification);
		return result.get("status").equals(1)?"forward:/sysnotificationmanagement/sysnotificationlist.do":"forward:/sysnotificationmanagement/showsysnotificationinfo.do";
	}
	
	@RequestMapping(value = "/sendsysnotification.do", method=RequestMethod.POST)
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="发送通知")
	public String sendSysNotification(HttpServletRequest request, Integer sendNotificationId) {
		logger.info("this is [sendsysnotification.do] start ...");
//		if (request.getSession().getAttribute("u_id")==null||request.getSession().getAttribute("u_sr")==null||request.getSession().getAttribute("u_sg")==null){
//			logger.info("this is [sendsysnotification.do] there is no login info ...");
//			return "redirect:/index.jsp";
//		}
		
		Map<String, Object> result=new HashMap<String, Object>();
		if (sendNotificationId!=null&&sendNotificationId!=0){
			logger.info("this is [sendsysnotification.do] notificationId is ["+sendNotificationId+"]");
			SysNotification sysNotification=sysNotificationManagementService.get(sendNotificationId);
			logger.info("this is [sendsysnotification.do] notification is ["+sysNotification+"]");
			
			if (sysNotification.getNotificationReceiveGroupIds()!=null&&!sysNotification.getNotificationReceiveGroupIds().equals("")){
				logger.info("this is [sendsysnotification.do] NotificationReceiveGroupIds is ["+sysNotification.getNotificationReceiveGroupIds()+"]");
				
				logger.info("this is [sendsysnotification.do] get [u_sr] ...");
				int roleCategory=-1;//1:全查，2:查校区(用户所在)和班级，3:查班级
				
//				List<SysRoles> lstRoles=(List<SysRoles>)request.getSession().getAttribute("u_sr");
				SysUsers currentUser=sysUsersManagementService.get(Integer.parseInt(""+request.getSession().getAttribute("u_id")));
				List<SysRoles> lstRoles=currentUser.getSysRoles();
				for (SysRoles role:lstRoles){
					if (role.getId()==1||role.getId()==2){//管理者|管理助理
						roleCategory=1;
						break;
					}else if (role.getId()==3){//校区助理
						roleCategory=2;
					}else if (role.getId()==4&&roleCategory!=2){//教师
						roleCategory=3;
					}
				}
				logger.info("this is [sendsysnotification.do] get [u_sr] is ["+roleCategory+"]...");
				List <SysGroups> lstGroups=null;
				if (roleCategory==-1){
					result.put("status",0);
					result.put("data", "用户权限错误，请重新登录！");
				}else if (roleCategory!=1){
//					lstGroups=(List<SysGroups>)request.getSession().getAttribute("u_sg");
					lstGroups=currentUser.getSysGroups();
				}
				
				List<SysUsers> lstReceiveUsers=sysGroupsManagementService.findSysUsersInGroups(sysNotification.getNotificationReceiveGroupIds(), roleCategory, lstGroups);
				
//				SysUsers user=new SysUsers();
//				user.setId(Integer.parseInt(""+request.getSession().getAttribute("u_id")));
//				sysNotification.setNotificationUserInfo(user);
				
				//for send notification to App users
				List<Integer> userIds=new ArrayList<Integer>();
				//end
				
				SysNotificationDetail notificationDetail=null;
				List<SysNotificationDetail> details=new ArrayList<SysNotificationDetail>();
				for (SysUsers receiveUser:lstReceiveUsers){
					if (receiveUser.getId().equals(sysNotification.getNotificationUserInfo().getId())){
						continue;
					}
					notificationDetail=new SysNotificationDetail();
					notificationDetail.setDetailReceiveUserInfo(receiveUser);
					logger.info("this is [savesysnotification.do] get [receive_user] is ["+receiveUser.getUserName()+"]...");
					notificationDetail.setIsRead(0);
					notificationDetail.setDetailNotificationInfo(sysNotification);
					details.add(notificationDetail);
					
					//for send notification to App users
					userIds.add(receiveUser.getId());
					//end
				}
				sysNotification.setSysNotificationDetailInfo(details);
				sysNotification.setIsSend(1);
				logger.info("this is [sendsysnotification.do] is saving...");
				sysNotificationManagementService.save(sysNotification);
				
				//send notification to App user
				List<SysDeviceToken> deviceTokens=DeviceTokenManagementService.findDeviceTokenByUserId(userIds);
				String text="您有一条新通知";
				String ticker="";
				String title="通知";
				DeviceTokenManagementService.sendNotificationToApp(deviceTokens, text, title, ticker);
				//end
				
				result.put("status", 1);
				result.put("data", "operation success!");
				logger.info("this is [sendsysnotification.do] save sysNotification done ...");
				
			}
			
		}
		logger.info("this is [savesysnotification.do] show result ["+result+"] ...");
		request.setAttribute("result", result.get("data"));
		return "forward:/sysnotificationmanagement/sysnotificationlist.do";
	}
	
	@RequestMapping(value = "/deletesysnotification.do", method=RequestMethod.POST)
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="删除通知信息")
	public String deleteSysNotification(HttpServletRequest request, Integer deleteId) {
		logger.info("this is [deletesysnotification.do] start ...");
		logger.info("this is [deletesysnotification.do] values [deleteId={"+deleteId+"}]");
		Map<String, Object> result=new HashMap<String, Object>();
		if (deleteId!=null&&deleteId!=0){
			try {
				logger.info("this is [deletesysnotification.do] ready to delete ...");
				sysNotificationManagementService.delete(sysNotificationManagementService.get(deleteId));
				result.put("status", 1);
				result.put("data", "operation success!");
				logger.info("this is [deletesysnotification.do] to delete done...");
			} catch (Exception e) {
				logger.info("this is [deletesysnotification.do] to trough exception when delete ...");
				result.put("status", 0);
				result.put("data", "delete failed, try again!");
				e.printStackTrace();
			}
		}
		
		logger.info("this is [deletesysnotification.do] show result ["+result+"] ...");
		request.setAttribute("result", result.get("data"));
		return "forward:/sysnotificationmanagement/sysnotificationlist.do";
	}
	
	@RequestMapping(value = "/deletemultiplesysnotification.do", method=RequestMethod.POST)
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="批量删除通知信息")
	public String deleteMultipleSysNotification(HttpServletRequest request, String deleteIds) {
		logger.info("this is [deletemultiplesysnotification.do] start ...");
		logger.info("this is [deletemultiplesysnotification.do] values [deleteId={"+deleteIds+"}]");
		Map<String, Object> result=new HashMap<String, Object>();
		if (deleteIds!=null&&!deleteIds.equals("")){
			try {
				logger.info("this is [deletemultiplesysnotification.do] ready to delete ...");
				sysNotificationManagementService.deleteMultiple(deleteIds);
				result.put("status", 1);
				result.put("data", "operation success!");
				logger.info("this is [deletemultiplesysnotification.do] to delete done...");
			} catch (Exception e) {
				logger.info("this is [deletemultiplesysnotification.do] to trough exception when delete ...");
				result.put("status", 0);
				result.put("data", "delete failed, try again!");
				e.printStackTrace();
			}
		}
		
		logger.info("this is [deletemultiplesysnotification.do] show result ["+result+"] ...");
		request.setAttribute("result", result.get("data"));
		return "forward:/sysnotificationmanagement/sysnotificationlist.do";
	}
	
	@RequestMapping(value = "/deletemultiplesysnotificationdetail.do", method=RequestMethod.POST)
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="批量删除通知详细信息")
	public String deleteMultipleSysNotificationDetail(HttpServletRequest request, String deleteIds) {
		logger.info("this is [deletemultiplesysnotificationdetail.do] start ...");
		logger.info("this is [deletemultiplesysnotificationdetail.do] values [deleteId={"+deleteIds+"}]");
		Map<String, Object> result=new HashMap<String, Object>();
		if (deleteIds!=null&&!deleteIds.equals("")){
			try {
				logger.info("this is [deletemultiplesysnotificationdetail.do] ready to delete ...");
				sysNotificationDetailManagementService.deleteMultiple(deleteIds);
				result.put("status", 1);
				result.put("data", "operation success!");
				logger.info("this is [deletemultiplesysnotificationdetail.do] to delete done...");
			} catch (Exception e) {
				logger.info("this is [deletemultiplesysnotificationdetail.do] to trough exception when delete ...");
				result.put("status", 0);
				result.put("data", "delete failed, try again!");
				e.printStackTrace();
			}
		}
		
		logger.info("this is [deletemultiplesysnotificationdetail.do] show result ["+result+"] ...");
		request.setAttribute("result", result.get("data"));
		return "forward:/sysnotificationmanagement/sysnotificationdetail.do";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/inittreedata.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemUserLoginIsCheck
	public String initTreeData(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [initTreeData.do] start ...");
		List<Map<String, Object>> result=null;
		
		if (data!=null&&!data.equals("")){
			try {
				logger.info("this is [savesysgroups.do] is decoding ...");
				data=URLDecoder.decode(data, "utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.info("this is [savesysgroups.do] occur error when program decoding ...");
				e.printStackTrace();
			}
		}
		
		Integer notificationId=0;
		boolean setDisabled=false;
		try{
			JSONObject json=JSONObject.fromString(data);
			
			notificationId=Integer.parseInt(
					json.getString("notificationId")==null||json.getString("notificationId").equals("")||json.getString("notificationId").equals("null")
						?"0":json.getString("notificationId"));
			setDisabled=json.getBoolean("setDisabled");
		}catch(Exception ex){
			logger.info("this is [savesysgroups.do] get parameter error ...");
			ex.printStackTrace();
		}
		
		if (request.getSession().getAttribute("u_sg")!=null){
			List<SysGroups> lstGroups=(List<SysGroups>)request.getSession().getAttribute("u_sg");
			boolean isAll=false;
			List<Map<String, Integer>> lstGroup=new ArrayList<Map<String, Integer>>();
			Map<String, Integer> mapGroupInfo=null;
			for (SysGroups group:lstGroups){
				if (group.getGroupParentId()==-1){
					isAll=true;
					break;
				}
				mapGroupInfo=new HashMap<String,Integer>();
				mapGroupInfo.put("groupCategory", group.getGroupCategory());
				mapGroupInfo.put("groupId", group.getId());
				lstGroup.add(mapGroupInfo);
			}
			if (isAll){
				result=sysGroupsManagementService.findGroupsForTree(new HashMap<String, Object>());
			}else{
				result=sysGroupsManagementService.findGroupsForTree(lstGroup);
			}
			if (result != null && !result.isEmpty()) {
				if (notificationId!=0){
					SysNotification notification=sysNotificationManagementService.get(notificationId);
					String[] receiveGroups=null;
					if (notification.getNotificationReceiveGroupIds()!=null&&!notification.getNotificationReceiveGroupIds().equals("")){
						if (notification.getNotificationReceiveGroupIds().contains(",")){
							receiveGroups=notification.getNotificationReceiveGroupIds().split(",");
						}else{
							receiveGroups=new String[1];
							receiveGroups[0]=notification.getNotificationReceiveGroupIds();
						}
					}
					
					Map<String,Object> state=null;
					for (Map<String, Object> mapTemp : result) {
						if (mapTemp.containsKey("state")){
							state=(HashMap<String,Object>)mapTemp.get("state");
							state.put("disabled", setDisabled);
						}else{
							state=new HashMap<String,Object>();
							state.put("disabled", setDisabled);
						}
						for(String rg:receiveGroups){
							logger.info("this is [initTreeData.do] json rg [" + rg + "],mapTemp.get('id') ["+mapTemp.get("id")+"]");
							if (String.valueOf(mapTemp.get("id")).equals(rg)){
								state.put("checked", true);
								break;
							}
						}
						mapTemp.put("state", state);
					}
				}
			}
		}
				
		String strJsonResult = JSONArray.fromObject(result).toString();
		logger.info("this is [initTreeData.do] json [" + strJsonResult + "]");
		logger.info("this is [initTreeData.do] end ...");
		return strJsonResult;
	}
	
	@RequestMapping(value = "/sysnotificationdetail.do")
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="查询通知详细信息")
	public String sysNotificationDetail(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		logger.info("this is [sysnotificationdetail.do] start ...");
		logger.info("this is [sysnotificationdetail.do] end ...");
		return "sysnotification/sysnotificationlistdetail";
	}
	
	@RequestMapping(value = "/initsysnotificationdetailtable.do")
	@ResponseBody
	@SystemLogIsCheck(description="查询通知详细信息")
	public String initSysNotificationDetailTable(HttpServletRequest request) {
		logger.info("this is [initsysnotificationdetailtable.do] start ...");
		
		int displayLength = ServletRequestUtils.getIntParameter(request, "length", 0), //每页大小
			displayStart = ServletRequestUtils.getIntParameter(request, "start", 0), //起始值
			sEcho = ServletRequestUtils.getIntParameter(request, "draw", 0),
			notificationId=ServletRequestUtils.getIntParameter(request, "sysnotification_id", -1);
		
		
		logger.info("this is [initsysnotificationdetailtable.do] requset pram [displayLength = "+displayLength+"],[displayStart = {"+displayStart+"}],[sEcho = {"+sEcho+"}]");
		
		String title=ServletRequestUtils.getStringParameter(request, "sysnotification_title", null), 
			message=ServletRequestUtils.getStringParameter(request, "sysnotification_message", null), 
			sort=ServletRequestUtils.getStringParameter(request, "order[0][column]", null),
			dir=ServletRequestUtils.getStringParameter(request, "order[0][dir]", null);
		
		logger.info("this is [initsysnotificationdetailtable.do] requset pram [title = {"+title+"}],[message = {"+message+"}]"
				+ ",[sort = {"+sort+"}],[dir = {"+dir+"}]");
		
		Map<String, Object> parameters=new HashMap<String, Object>();
		if (title!=null&&!title.equals("")){
			parameters.put("notificationTitle", title);
		}
		if (notificationId!=-1){
			parameters.put("id", notificationId);
		}
		if (message!=null&&!message.equals("")){
			parameters.put("notificationMessage", message);
		}
		
		if (sort!=null&&!sort.equals("")){
			parameters.put("sort", sort);
		}
		if (dir!=null&&!dir.equals("")){
			parameters.put("order", dir);
		}
		
		if (request.getSession().getAttribute("u_id")!=null){
			Integer userId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
			parameters.put("snd.detailReceiveUserInfo.id", userId);
		}
		
		Map<String,Object> returnData=sysNotificationDetailManagementService.searchDataForAjax(displayLength, displayStart, sEcho, parameters);
		
		returnData.put("draw", sEcho);
		
		String result=JSONObject.fromObject(returnData).toString();
		logger.info("this is [initsysnotificationdetailtable.do] data ["+result+"] ...");
		logger.info("this is [initsysnotificationdetailtable.do] end ...");
		return result;
	}

}
