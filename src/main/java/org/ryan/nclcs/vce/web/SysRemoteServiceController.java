package org.ryan.nclcs.vce.web;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.ryan.nclcs.vce.annotation.SystemLogIsCheck;
import org.ryan.nclcs.vce.dao.Pagination;
import org.ryan.nclcs.vce.entity.AppStudentUploadAssignment;
import org.ryan.nclcs.vce.entity.AppStudentsScores;
import org.ryan.nclcs.vce.entity.AppTutorAppointmentAssignmentToStudent;
import org.ryan.nclcs.vce.entity.AppTutorAppointmentAssignmentToTutor;
import org.ryan.nclcs.vce.entity.SysDeviceToken;
import org.ryan.nclcs.vce.entity.SysGroups;
import org.ryan.nclcs.vce.entity.SysNotification;
import org.ryan.nclcs.vce.entity.SysNotificationDetail;
import org.ryan.nclcs.vce.entity.SysProperties;
import org.ryan.nclcs.vce.entity.SysRoles;
import org.ryan.nclcs.vce.entity.SysUsers;
import org.ryan.nclcs.vce.service.appassignment.IAppStudentUploadAssignmentSerivce;
import org.ryan.nclcs.vce.service.appassignment.IAppTutorAppointmentToStudentService;
import org.ryan.nclcs.vce.service.appassignment.IAppTutorAppointmentToTutorService;
import org.ryan.nclcs.vce.service.appstudents.IAppStudentsManagementService;
import org.ryan.nclcs.vce.service.devicetoken.ISysDeviceTokenManagementService;
import org.ryan.nclcs.vce.service.sysgroups.ISysGroupsManagementService;
import org.ryan.nclcs.vce.service.sysnotification.ISysNotificationDetailManagementService;
import org.ryan.nclcs.vce.service.sysnotification.ISysNotificationManagementService;
import org.ryan.nclcs.vce.service.sysproperties.ISysPropertiesManagementService;
import org.ryan.nclcs.vce.service.sysroles.ISysRolesManagementService;
import org.ryan.nclcs.vce.service.sysusers.ISysUsersManagementService;
import org.ryan.nclcs.vce.web.util.MD5;
import org.ryan.nclcs.vce.web.util.WebApplicationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/sysremoteservice")
public class SysRemoteServiceController {
	
	protected static Logger logger = LoggerFactory.getLogger(SysRemoteServiceController.class);
	
	@Autowired
	private ISysUsersManagementService sysUsersManagementService;
	
	@Autowired
	private ISysPropertiesManagementService sysPropertiesManagementService;
	
	@Autowired
	private ISysGroupsManagementService sysGroupsManagementService;
	
	@Autowired
	private ISysRolesManagementService sysRolesManagementService;
	
	@Autowired
	private IAppStudentsManagementService appStudentsManagementService;
	
	@Autowired
	private ISysNotificationManagementService sysNotificationManagementService;
	
	@Autowired
	private ISysNotificationDetailManagementService sysNotificationDetailManagementService;
	
	@Autowired
	private ISysDeviceTokenManagementService sysDeviceTokenManagementService;
	
	@Autowired
	private IAppStudentUploadAssignmentSerivce appStudentUploadAssignmentSerivce;
	
	@Autowired
	private IAppTutorAppointmentToStudentService appTutorAppointmentToStudentService;
	
	@Autowired
	private IAppTutorAppointmentToTutorService appTutorAppointmentToTutorService;
	
	//local
//	protected final String _filePath="/usr/local/vce-uploadfiles";
	
	//server
	protected final String _filePath="/var/www/vce-uploadfiles";
	
	@RequestMapping(value = "/userlogin.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="用户登录")
	public String userLogin(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [userlogin.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		
		logger.info("this is [userlogin.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [userlogin.do] decode done ...");
		
		logger.info("this is [userlogin.do] check is user_name exist...");
		String userName=json.has("userName")?json.getString("userName"):"";
		String userPWD=json.has("userPWD")?json.getString("userPWD"):"";
		String deviceToken=json.has("deviceToken")?json.getString("deviceToken"):"";
		
		logger.info("this is [userlogin.do] userName ["+userName+"] pwd ["+userPWD+"] deviceToken ["+deviceToken+"]");
		
		Map<String,Object> parameters=new HashMap<String,Object>();
		parameters.put("userName", userName);
		parameters.put("password", MD5.string2MD5(MD5.string2MD5(userPWD)));
		SysUsers user=sysUsersManagementService.isExistByParameters(parameters);
		if (user!=null){
			result.put("status", 1);
			result.put("info", "login success!");
			
			//set new device token
			if (deviceToken!=null&&!deviceToken.equals("")){
				sysDeviceTokenManagementService.setNewDeviceToken(user.getId(), deviceToken);
			}
			//end
			
			logger.info("this is [userlogin.do] login success ...");
			WebApplicationUtils.setNewToken(user.getId(), user.getUserName()+user.getPassword());
			result.put("token", WebApplicationUtils.getToken(user.getId()));
			result.put("userid", user.getId());
			result.put("username", user.getUserName());
			result.put("chinesename", user.getChineseName());
			
			List<Map<String,Object>> param=new ArrayList<Map<String,Object>>();
			if (user.getSysRoles()!=null&&!user.getSysRoles().isEmpty()){
				Map<String,Object> map=null;
				for (SysRoles role:user.getSysRoles()){
					map=new HashMap<String,Object>();
					map.put("roleId", role.getId());
					map.put("roleName", role.getRoleName());
					param.add(map);
				}
			}
			result.put("roles", param);
			
			param=new ArrayList<Map<String,Object>>();
			if (user.getSysGroups()!=null&&!user.getSysGroups().isEmpty()){
				Map<String,Object> map=null;
				for (SysGroups group:user.getSysGroups()){
					map=new HashMap<String,Object>();
					map.put("groupId", group.getId());
					map.put("groupName", group.getGroupName());
					map.put("groupCategory", group.getGroupCategory());
					param.add(map);
				}
			}
			result.put("groups", param);
			
			
			logger.info("this is [userlogin.do] set value to session...");
			request.getSession().setAttribute("u_id", user.getId());
			request.getSession().setAttribute("u_name", user.getUserName());
			
		}else{
			result.put("status", 0);
			result.put("info", "login failed, try again!");
			logger.info("this is [userlogin.do] login failed ...");
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [userlogin.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/findstudentgroup.do", method=RequestMethod.POST)
	@ResponseBody
	public String findStudentGroup(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [findstudentgroup.do] start ...");
		Integer userId=0, groupId=0, studentId=0;
		Map<String, Object> result=new HashMap<String, Object>();
		
		if (data!=null&&!data.equals("")){
			try {
				logger.info("this is [findstudentgroup.do] is decoding ...");
				data=URLDecoder.decode(data, "utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.info("this is [findstudentgroup.do] occur error when program decoding ...");
				e.printStackTrace();
			}
		}
		
		JSONObject json=null;
		try{
			json=JSONObject.fromString(data);
		}catch(Exception ex){
			logger.info("this is [findstudentgroup.do] translation json error");
			ex.printStackTrace();
			result.put("status", -1);
			result.put("info", "parameters error");
		}
		
		if (json!=null&&result.isEmpty()){
			try {
				if (json.has("userId")){
					userId=json.getInt("userId");
				}
			} catch (Exception e) {
				logger.info("this is [findstudentgroup.do] get userId error");
				userId=0;
				e.printStackTrace();
			}
			try {
				if (json.has("groupId")){
					groupId=json.getInt("groupId");
				}
			} catch (Exception e) {
				logger.info("this is [findstudentgroup.do] get groupId error");
				e.printStackTrace();
			}
			try {
				if (json.has("studentId")){
					studentId=json.getInt("studentId");
				}
			} catch (Exception e) {
				logger.info("this is [findstudentgroup.do] get studentId error");
				e.printStackTrace();
			}
		}
			
		if (result.isEmpty()){
			List<Map<String,Object>> groupsInfo=null;
			Map<String, Object> parameters=new HashMap<String, Object>();
			if (userId!=0){
				SysUsers currentUser=sysUsersManagementService.get(userId);
				boolean isAssistant=false;//是否是校区助理
				boolean isTutor=false;
				for (SysRoles tmp:currentUser.getSysRoles()){
					if (tmp.getId()==1||tmp.getId()==2){//管理者或管理助理
						isAssistant=false;
						break;
					}else if (tmp.getId()==3){//校区助理
						isAssistant=true;
					}else if (tmp.getId()==4&&!isAssistant){//tutor
						isTutor=true;
					}
				}
				
				SysUsers student=null;
				SysGroups group=null;
				
				if (isTutor&&!isAssistant){
					groupsInfo=new ArrayList<Map<String,Object>>();
					student=sysUsersManagementService.get(studentId);//当前要维护的学生对象
					group=student.getSysGroups().get(0);//学生只可能有一个组
					Map<String, Object> map=new HashMap<String, Object>();
					if (groupId==0){
						group=sysGroupsManagementService.get(group.getGroupParentId());
					}
					map.put("id",group.getId());
					map.put("text",group.getGroupName());
					groupsInfo.add(map);
				}else{
					student=sysUsersManagementService.get(studentId);//当前要维护的学生对象
					if (isAssistant&&groupId==0){//是校区助理角色，又要查校区的时候，直接用学生的校区群组ID查就可以了，校区助理角色不可以跨校区给学生转班
						group=student.getSysGroups().get(0);//学生只可能有一个组
						if (group!=null&&group.getGroupCategory()!=null&&group.getGroupCategory()==1){//判断这个多啊，就是想知道得到的这个群组是不是班级，是班级就查他的父群组(只适用于当想的数据结构，校区下只有班级)
							group=sysGroupsManagementService.get(group.getGroupParentId());
							parameters.put("id", group.getId());//学生的校区群组ID
						}
					}
					
					parameters.put("groupParentId", !isAssistant&&groupId==0?1:groupId);//groupId==0说明是查校区，不是校区助理就让groupParentId=1，就是查全部
					
					groupsInfo=sysGroupsManagementService.findGroup(parameters,userId);
					
					if (groupsInfo.isEmpty()){
						Map<String, Object> map=new HashMap<String, Object>();
						map.put("id",-1);
						map.put("text", "");
						groupsInfo.add(map);
					}
					
					if (student.getSysGroups()!=null&&!student.getSysGroups().isEmpty()){
						group=group==null?student.getSysGroups().get(0):group;
						if (group!=null&&group.getGroupCategory()!=null){
							if (group.getGroupCategory()==1&&groupId==0){
								group=sysGroupsManagementService.get(group.getGroupParentId());
							}
							
							for(Map<String, Object> map:groupsInfo){
								if (map.get("id").equals(group.getId())){
									map.put("selected", "selected");
									break;
								}
							}
						}
					}
				}
			}else{
				parameters.put("groupParentId", groupId==0?1:groupId);//groupId==0说明是查校区，不是校区助理就让groupParentId=1，就是查全部
				groupsInfo=sysGroupsManagementService.findGroup(parameters,null);
				if (groupsInfo.isEmpty()){
					Map<String, Object> map=new HashMap<String, Object>();
					map.put("id",-1);
					map.put("text", "");
					groupsInfo.add(map);
				}
			}
			
			result.put("status", 1);
			result.put("groupsInfo", groupsInfo);
			
		}
		
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [findstudentgroup.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/userregister.do", method=RequestMethod.POST)
	@ResponseBody
	public String userRegister(HttpServletRequest request) {
		logger.info("this is [userregister.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
//		logger.info("this is [userregister.do] is decoding ...");
//		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
//		logger.info("this is [userregister.do] decode done ...");
		
		Map<String,Object> parameters=new HashMap<String,Object>();
		parameters.put("propertyParentId", 5);//isLearnChinese's id, change it here 
		List<Map<String,Object>> isLearnChinese=sysPropertiesManagementService.findProperty(parameters);
		
		parameters.clear();
		parameters.put("groupParentId", 1);//查全部的校区
		List<Map<String,Object>> lstCampus=sysGroupsManagementService.findGroup(parameters, null);
		
		result.put("isLearnChinese", isLearnChinese);
		result.put("campus", lstCampus);
		
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [userregister.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/saveuserregister.do", method=RequestMethod.POST, produces="application/json; charset=utf-8")
	@ResponseBody
	public String saveUserRegister(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [saveuserregister.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		
		try {
			logger.info("this is [saveuserregister.do] is decoding ...");
			logger.info("this is [saveuserregister.do] data ["+data+"] ...");
			JSONObject json=JSONObject.fromString(this.decodeParameters(data));
			logger.info("this is [saveuserregister.do] decode done ...");
			
			logger.info("this is [saveuserregister.do] check is user_name exist...");
			String userName=json.has("userName")?json.getString("userName"):"";
			
			//check user_name is exist
			Map<String,Object> parameters=new HashMap<String,Object>();
			parameters.put("userName", userName);
			SysUsers user=sysUsersManagementService.isExistByParameters(parameters);
			
			if (user==null){//not exist
				String userPWD=json.has("userPWD")?json.getString("userPWD"):"";
				String pinyin=json.has("pinyin")?json.getString("pinyin"):"";
				String chineseName=json.has("chineseName")?json.getString("chineseName"):"";
				String englishName=json.has("englishName")?json.getString("englishName"):"";
				String homeAddress=json.has("homeAddress")?json.getString("homeAddress"):"";
				String mobilePhone=json.has("mobilePhone")?json.getString("mobilePhone"):"";
				String homePhone=json.has("homePhone")?json.getString("homePhone"):"";
				String emailAddress=json.has("emailAddress")?json.getString("emailAddress"):"";
				String daySchool=json.has("daySchool")?json.getString("daySchool"):"";
				String daySchoolGrade=json.has("daySchoolGrade")?json.getString("daySchoolGrade"):"";
				String isLearnChinese=json.has("isLearnChinese")?json.getString("isLearnChinese"):"-1";
				String campus=json.has("campus")?json.getString("campus"):"-1";
				String campusClass=json.has("campusClass")?json.getString("campusClass"):"-1";
				
				user=new SysUsers();
				user.setUserName(userName);
				user.setPinyin(pinyin);
				user.setChineseName(chineseName);
				user.setEnglishName(englishName);
				user.setHomeAddress(homeAddress);
				user.setHomePhone(homePhone);
				user.setMobilePhone(mobilePhone);
				user.setEmailAddress(emailAddress);
				user.setDaySchool(daySchool);
				user.setDaySchoolGrade(daySchoolGrade);
				logger.info("this is [saveuserregister.do] user ["+user+"]");
				
				//是否在日校学中文
				if (isLearnChinese!=null){
					SysProperties property=sysPropertiesManagementService.get(Integer.parseInt(isLearnChinese));
					user.setPropertyIsLearnChinese(property);
				}
				
				user.setPassword(MD5.string2MD5(MD5.string2MD5(userPWD)));
				
				SysGroups groupNewCampus=sysGroupsManagementService.get(Integer.parseInt(campus));
				user.setVceSchoolName(groupNewCampus.getGroupName());
				
				SysGroups groupNewClass=sysGroupsManagementService.get(Integer.parseInt(campusClass));
				user.setVceClassName(groupNewClass.getGroupName());
				
				logger.info("this is [saveuserregister.do] is saving user ...");
				if (this.sysUsersManagementService.saveRegisterUser(user, groupNewClass, sysRolesManagementService.get(5))){
					logger.info("this is [saveuserregister.do] save user done...");
					
					user=sysUsersManagementService.isExistByParameters(parameters);
					if (user!=null){
						result.put("status", 1);
						result.put("info", "register success!");
						logger.info("this is [saveuserregister.do] register success ...");
						
						WebApplicationUtils.setNewToken(user.getId(), user.getUserName()+user.getPassword());
						result.put("token", WebApplicationUtils.getToken(user.getId()));
						result.put("userid", user.getId());
						result.put("username", user.getUserName());
						result.put("chinesename", user.getChineseName());
						
						logger.info("this is [saveuserregister.do] set value to session...");
						request.getSession().setAttribute("u_id", user.getId());
						request.getSession().setAttribute("u_name", user.getUserName());
					}else{
						result.put("status", 0);
						result.put("info", "register failed, try again!");
						logger.info("this is [saveuserregister.do] register failed ...");
					}
				}else{
					result.put("status", 0);
					result.put("info", "register failed, try again!");
					logger.info("this is [saveuserregister.do] register failed ...");
				}
			}else{
				result.put("status", 2);
				result.put("info", "username is exist, try again!");
				logger.info("this is [saveuserregister.do] register failed for username is exist ...");
			}
			
		} catch (Exception e) {
			result.put("status", 0);
			result.put("info", "register failed, try again!");
			logger.info("this is [saveuserregister.do] register failed for occur exception ...");
			e.printStackTrace();
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [saveuserregister.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/saveparentregister.do", method=RequestMethod.POST, produces="application/json; charset=utf-8")
	@ResponseBody
	public String saveParentRegister(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [saveparentregister.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		
		logger.info("this is [saveparentregister.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [saveparentregister.do] decode done ...");
		
		try {
			if (!json.has("childrenId")){
				result.put("status", -1);
				result.put("info", "the lack of parameter");
				logger.info("this is [saveparentregister.do] the lack of parameter ...");
			}else{
				logger.info("this is [saveparentregister.do] check is user_name exist...");
				String userName=json.has("userName")?json.getString("userName"):"";
				
				//check user_name is exist
				Map<String,Object> parameters=new HashMap<String,Object>();
				parameters.put("userName", userName);
				SysUsers parentUser=sysUsersManagementService.isExistByParameters(parameters);
				
				if (parentUser==null){//not exist
					
					Integer childrenId=json.getInt("childrenId");
					SysUsers studentUser=sysUsersManagementService.get(childrenId);
					
					if (studentUser!=null){//student exist
						
						if (studentUser.getSysGroups()==null||studentUser.getSysGroups().isEmpty()||studentUser.getSysGroups().size()>1){//class setting was wrong
							result.put("status", 4);
							result.put("info", "student class was wrong");
							logger.info("this is [saveparentregister.do] student class was wrong ...");
						}else{
							String userPWD=json.has("userPWD")?json.getString("userPWD"):"";
							String pinyin=json.has("pinyin")?json.getString("pinyin"):"";
							String chineseName=json.has("chineseName")?json.getString("chineseName"):"";
							String englishName=json.has("englishName")?json.getString("englishName"):"";
							String homeAddress=json.has("homeAddress")?json.getString("homeAddress"):"";
							String mobilePhone=json.has("mobilePhone")?json.getString("mobilePhone"):"";
							String homePhone=json.has("homePhone")?json.getString("homePhone"):"";
							String emailAddress=json.has("emailAddress")?json.getString("emailAddress"):"";
							
							parentUser=new SysUsers();
							parentUser.setUserName(userName);
							parentUser.setPinyin(pinyin);
							parentUser.setChineseName(chineseName);
							parentUser.setEnglishName(englishName);
							parentUser.setHomeAddress(homeAddress);
							parentUser.setHomePhone(homePhone);
							parentUser.setMobilePhone(mobilePhone);
							parentUser.setEmailAddress(emailAddress);
							parentUser.setChildrenId(childrenId);
							logger.info("this is [saveparentregister.do] user ["+parentUser+"]");
							
							parentUser.setPassword(MD5.string2MD5(MD5.string2MD5(userPWD)));
							
							logger.info("this is [saveparentregister.do] is saving user ...");
							this.sysUsersManagementService.saveRegisterUser(parentUser, studentUser.getSysGroups().get(0), sysRolesManagementService.get(6));
							logger.info("this is [saveparentregister.do] save user done...");
							
							parentUser=sysUsersManagementService.isExistByParameters(parameters);
							if (parentUser!=null){
								result.put("status", 1);
								result.put("info", "register success!");
								logger.info("this is [saveparentregister.do] register success ...");
								
								WebApplicationUtils.setNewToken(parentUser.getId(), parentUser.getUserName()+parentUser.getPassword());
								result.put("token", WebApplicationUtils.getToken(parentUser.getId()));
								result.put("userid", parentUser.getId());
								result.put("username", parentUser.getUserName());
								result.put("chinesename", parentUser.getChineseName());
								
								logger.info("this is [saveparentregister.do] set value to session...");
								request.getSession().setAttribute("u_id", parentUser.getId());
								request.getSession().setAttribute("u_name", parentUser.getUserName());
								
							}else{
								result.put("status", 0);
								result.put("info", "register failed, try again!");
								logger.info("this is [saveparentregister.do] register failed ...");
							}
						}
					}else{
						result.put("status", 3);
						result.put("info", "student is not exist, try again!");
						logger.info("this is [saveparentregister.do] register failed for student is not exist ...");
					}
				}else{
					result.put("status", 2);
					result.put("info", "username is exist, try again!");
					logger.info("this is [saveparentregister.do] register failed for username is exist ...");
				}
			}
		} catch (NumberFormatException e) {
			result.put("status", 0);
			result.put("info", "register failed, try again!");
			logger.info("this is [saveparentregister.do] occur exception ...");
			e.printStackTrace();
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [saveparentregister.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/showpersonalinfo.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="查询个人信息")
	public String showPersonalInfo(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [showpersonalinfo.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [showpersonalinfo.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [showpersonalinfo.do] decode done ...");
		
		Integer userId=-1;
//		Integer currentUserId=-1;
		String token=null;
		SysUsers user=null;
		Map<String, Object> jsonResult=new HashMap<String, Object>();
		if (!json.has("userId")||!json.has("token")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [showpersonalinfo.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				userId=json.getInt("userId");
				token=json.getString("token");
				String localToken=WebApplicationUtils.getToken(userId);
//				if (request.getSession().getAttribute("u_id")!=null&&!request.getSession().getAttribute("u_id").equals("")){
//					currentUserId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
//				}
				if (localToken!=null&&localToken.equals(token)){
					user=sysUsersManagementService.get(userId);
					jsonResult.put("userId", user.getId());
					jsonResult.put("userName", user.getUserName());
					jsonResult.put("userChineseName", user.getChineseName());
					jsonResult.put("userEnglishName", user.getEnglishName());
					jsonResult.put("mobilePhone", user.getMobilePhone());
					jsonResult.put("emailAddress", user.getEmailAddress());
					if (this.isStudent(user)){
						jsonResult.put("pinyin", user.getPinyin());
						jsonResult.put("homeAddress", user.getHomeAddress());
						jsonResult.put("homePhone", user.getHomePhone());
						jsonResult.put("daySchool", user.getDaySchool());
						jsonResult.put("daySchoolGrade", user.getDaySchoolGrade());
//						jsonResult.put("campusName", user.getVceSchoolName());
//						jsonResult.put("className", user.getVceClassName());
						String isLearnChinese="";
						if (user.getPropertyIsLearnChinese()!=null){
							isLearnChinese=user.getPropertyIsLearnChinese().getId()==10?"是":"否";
						}
						jsonResult.put("isLearnChinese", isLearnChinese);
						jsonResult.put("campus", user.getVceSchoolName());
						jsonResult.put("campusClass", user.getVceClassName());
					}else if (this.isParent(user)){
						jsonResult.put("pinyin", user.getPinyin());
						jsonResult.put("homeAddress", user.getHomeAddress());
						jsonResult.put("homePhone", user.getHomePhone());
					}
					result.put("status", 1);
					result.put("info", "operation success");
					result.put("data", jsonResult);
				}else{
					result.put("status", -2);
					result.put("info", "illegal user");
					logger.info("this is [showpersonalinfo.do] illegal user ...");
				}
			}catch(Exception ex){
				ex.printStackTrace();
				result.put("status", 0);
				result.put("info", "find user info error");
				logger.info("this is [showpersonalinfo.do] exception ...");
			}
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [showpersonalinfo.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/savepersonalinfo.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="保存个人信息")
	public String savePersonalInfo(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [savepersonalinfo.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [savepersonalinfo.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [savepersonalinfo.do] decode done ...");
		
		Integer userId=-1;
//		Integer currentUserId=-1;
		String token=null;
		SysUsers user=null;
		if (!json.has("userId")||!json.has("token")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [savepersonalinfo.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				userId=json.getInt("userId");
				token=json.getString("token");
				String localToken=WebApplicationUtils.getToken(userId);
//				if (request.getSession().getAttribute("u_id")!=null&&!request.getSession().getAttribute("u_id").equals("")){
//					currentUserId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
//				}
				if (localToken!=null&&localToken.equals(token)){
					user=sysUsersManagementService.get(userId);
//					String userName=json.has("userName")?json.getString("userName"):"";
					String chineseName=json.has("chineseName")?json.getString("chineseName"):"";
					String englishName=json.has("englishName")?json.getString("englishName"):"";
					String mobilePhone=json.has("mobilePhone")?json.getString("mobilePhone"):"";
					String emailAddress=json.has("emailAddress")?json.getString("emailAddress"):"";
					
//					user.setUserName(userName);
					user.setChineseName(chineseName);
					user.setEnglishName(englishName);
					user.setMobilePhone(mobilePhone);
					user.setEmailAddress(emailAddress);
					
					if (isStudent(user)){
						String pinyin=json.has("pinyin")?json.getString("pinyin"):"";
						String homeAddress=json.has("homeAddress")?json.getString("homeAddress"):"";
						String homePhone=json.has("homePhone")?json.getString("homePhone"):"";
						String daySchool=json.has("daySchool")?json.getString("daySchool"):"";
						String daySchoolGrade=json.has("daySchoolGrade")?json.getString("daySchoolGrade"):"";
						String isLearnChinese=json.has("isLearnChinese")?json.getString("isLearnChinese"):"0";
						
						SysProperties property=sysPropertiesManagementService.get(Integer.parseInt(isLearnChinese));
						if (property!=null){
							user.setPropertyIsLearnChinese(property);
						}
						
						user.setPinyin(pinyin);
						user.setHomeAddress(homeAddress);
						user.setHomePhone(homePhone);
						user.setDaySchool(daySchool);
						user.setDaySchoolGrade(daySchoolGrade);
					}else if (isParent(user)){
						String homeAddress=json.has("homeAddress")?json.getString("homeAddress"):"";
						String homePhone=json.has("homePhone")?json.getString("homePhone"):"";
						
						user.setHomeAddress(homeAddress);
						user.setHomePhone(homePhone);
					}
					logger.info("this is [savepersonalinfo.do] show user ["+user+"] ...");
					sysUsersManagementService.save(user);
					result.put("status", 1);
					result.put("info", "operation success");
				}else{
					result.put("status", -2);
					result.put("info", "illegal user");
					logger.info("this is [savepersonalinfo.do] illegal user ...");
				}
				
			}catch(Exception ex){
				ex.printStackTrace();
				result.put("status", 0);
				result.put("info", "find user info error");
				logger.info("this is [savepersonalinfo.do] exception ...");
			}
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [savepersonalinfo.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/changepassword.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="修改密码")
	public String changePassword(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [changepassword.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [changepassword.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [changepassword.do] decode done ...");
		
		Integer userId=-1;
//		Integer currentUserId=-1;
		String token=null;
		SysUsers user=null;
		if (!json.has("userId")||!json.has("token")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [changepassword.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				userId=json.getInt("userId");
				token=json.getString("token");
				String localToken=WebApplicationUtils.getToken(userId);
//				if (request.getSession().getAttribute("u_id")!=null&&!request.getSession().getAttribute("u_id").equals("")){
//					currentUserId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
//				}
				if (localToken!=null&&localToken.equals(token)){
					user=sysUsersManagementService.get(userId);
					String originalPWD=json.has("originalPWD")?json.getString("originalPWD"):"";
					String newPWD=json.has("newPWD")?json.getString("newPWD"):"";
					
					
					logger.info("this is [changepassword.do] is checking ...");
					if (!MD5.string2MD5(MD5.string2MD5(originalPWD)).equals(user.getPassword())){
						result.put("status", 0);
						result.put("info", "原始密码输入错误，请重新输入！");
					}else{
						user.setPassword(MD5.string2MD5(MD5.string2MD5(newPWD)));
						logger.info("this is [changepassword.do] is saving ...");
						sysUsersManagementService.save(user);
						result.put("status", 1);
						result.put("info", "密码修改成功！");
						logger.info("this is [changepassword.do] is done ...");
					}
				}else{
					result.put("status", -2);
					result.put("info", "illegal user");
					logger.info("this is [changepassword.do] illegal user ...");
				}
			}catch(Exception ex){
				ex.printStackTrace();
				result.put("status", 0);
				result.put("info", "find user info error");
				logger.info("this is [changepassword.do] exception ...");
			}
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [changepassword.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/showstudentlist.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="查询学生列表")
	public String showStudentList(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [showstudentlist.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [showstudentlist.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [showstudentlist.do] decode done ...");
		
		Integer userId=-1;
//		Integer currentUserId=-1;
		String token=null;
		if (!json.has("userId")||!json.has("token")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [showstudentlist.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				userId=json.getInt("userId");
				token=json.getString("token");
				String localToken=WebApplicationUtils.getToken(userId);
//				if (request.getSession().getAttribute("u_id")!=null&&!request.getSession().getAttribute("u_id").equals("")){
//					currentUserId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
//				}
				if (localToken!=null&&localToken.equals(token)){
					//每页大小
					int displayLength=Integer.parseInt(json.has("displayLength")?json.getString("displayLength"):"10");
					//起始值
					int displayStart=Integer.parseInt(json.has("displayStart")?json.getString("displayStart"):"0");
					//查询条件
					String param=json.has("param")?json.getString("param"):"";
					
					logger.info("this is [showstudentlist.do] requset parameters [displayLength = {"+displayLength+"}],[displayStart = {"+displayStart+"}],[param = {"+param+"}]");
					
					Map<String, Object> parameters=new HashMap<String, Object>();
					if (param!=null&&!param.equals("")){
						parameters.put("chineseName", param);
						parameters.put("schoolName", param);
						parameters.put("sgs.groupName", param);
					}
					
					result=appStudentsManagementService.searchDataForApp(displayLength, displayStart, parameters, userId, false);
					result.put("status", 1);
					result.put("info", "operation success");
					result.put("displayLength", displayLength);
					result.put("displayStart", displayStart+displayLength);
					result.put("param", param);
				}else{
					result.put("status", -2);
					result.put("info", "illegal user");
					logger.info("this is [showstudentlist.do] illegal user ...");
				}
			}catch(Exception ex){
				ex.printStackTrace();
				result.put("status", 0);
				result.put("info", "find user info error");
				logger.info("this is [showstudentlist.do] exception ...");
			}
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [showstudentlist.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/showstudentinfo.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="查询学生信息")
	public String showStudentInfo(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [showstudentinfo.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [showstudentinfo.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [showstudentinfo.do] decode done ...");
		
		Integer userId=-1;
//		Integer currentUserId=-1;
		String token=null;
		SysUsers studentUser=null;
		if (!json.has("userId")||!json.has("token")||!json.has("studentId")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [showstudentinfo.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				userId=json.getInt("userId");
				token=json.getString("token");
				String localToken=WebApplicationUtils.getToken(userId);
//				if (request.getSession().getAttribute("u_id")!=null&&!request.getSession().getAttribute("u_id").equals("")){
//					currentUserId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
//				}
				if (localToken!=null&&localToken.equals(token)){
					Map<String, Object> jsonResult=new HashMap<String, Object>();
					Integer studentId=json.getInt("studentId");
					studentUser=sysUsersManagementService.get(studentId);
					
					jsonResult.put("userId", studentUser.getId());
					jsonResult.put("userName", studentUser.getUserName());
					jsonResult.put("userChineseName", studentUser.getChineseName()==null?"":studentUser.getChineseName());
					jsonResult.put("userEnglishName", studentUser.getEnglishName()==null?"":studentUser.getEnglishName());
					jsonResult.put("mobilePhone", studentUser.getMobilePhone()==null?"":studentUser.getMobilePhone());
					jsonResult.put("emailAddress", studentUser.getEmailAddress()==null?"":studentUser.getEmailAddress());
					jsonResult.put("pinyin", studentUser.getPinyin()==null?"":studentUser.getPinyin());
					jsonResult.put("homeAddress", studentUser.getHomeAddress()==null?"":studentUser.getHomeAddress());
					jsonResult.put("homePhone", studentUser.getHomePhone()==null?"":studentUser.getHomePhone());
					jsonResult.put("daySchool", studentUser.getDaySchool()==null?"":studentUser.getDaySchool());
					jsonResult.put("daySchoolGrade", studentUser.getDaySchoolGrade()==null?"":studentUser.getDaySchoolGrade());
					
					jsonResult.put("campus", studentUser.getVceSchoolName()==null?"":studentUser.getVceSchoolName());
					jsonResult.put("campusClass", studentUser.getVceClassName()==null?"":studentUser.getVceClassName());
					
					Map<String, Object> parameters=new HashMap<String, Object>();
//					parameters.put("propertyParentId", 5);//是否在日校学习中文
//					List<Map<String,Object>> propertiesInfo=sysPropertiesManagementService.findProperty(parameters);
//					if (studentUser.getPropertyIsLearnChinese()!=null){
//						for(Map<String, Object> map:propertiesInfo){
//							if (map.get("id").equals(studentUser.getPropertyIsLearnChinese().getId())){
//								map.put("selected", "selected");
//								break;
//							}
//						}
//					}
//					jsonResult.put("isLearnChinese", propertiesInfo);
//					parameters.clear();
					String isLearnChinese="";
					if (studentUser.getPropertyIsLearnChinese()!=null){
						isLearnChinese=studentUser.getPropertyIsLearnChinese().getId()==10?"是":"否";
					}
					jsonResult.put("isLearnChinese", isLearnChinese);
					
					//校区群组
					List<Map<String,Object>> campusInfo=null;
					List<Map<String,Object>> classInfo=null;
					Integer groupId=0;
					SysUsers currentUser=sysUsersManagementService.get(userId);
					boolean isAssistant=false;//是否是校区助理
					for (SysRoles tmp:currentUser.getSysRoles()){
						if (tmp.getId()==1||tmp.getId()==2){//管理者或管理助理
							isAssistant=false;
							break;
						}else if (tmp.getId()==3){//校区助理
							isAssistant=true;
						}
					}
					
					SysUsers user=sysUsersManagementService.get(userId);//当前要维护的学生对象
					SysGroups group=null;
					if (isAssistant&&groupId==0){//是校区助理角色，又要查校区的时候，直接用学生的校区群组ID查就可以了，校区助理角色不可以跨校区给学生转班
						group=user.getSysGroups().get(0);//学生只可能有一个组
						if (group!=null&&group.getGroupCategory()!=null&&group.getGroupCategory()==1){//判断这个多啊，就是想知道得到的这个群组是不是班级，是班级就查他的父群组(只适用于当想的数据结构，校区下只有班级)
							group=sysGroupsManagementService.get(group.getGroupParentId());
							parameters.put("id", group.getId());//学生的校区群组ID
						}
					}
					
					parameters.put("groupParentId", !isAssistant&&groupId==0?1:groupId);//groupId==0说明是查校区，不是校区助理就让groupParentId=1，就是查全部
					//取校区信息
					logger.info("this is [showstudentinfo.do] find campus isAssistant ["+isAssistant+"] groupId ["+groupId+"]...");
					campusInfo=sysGroupsManagementService.findGroup(parameters,userId);
					if (studentUser.getSysGroups()!=null&&!studentUser.getSysGroups().isEmpty()){
						group=group==null?studentUser.getSysGroups().get(0):group;
						if (group!=null&&group.getGroupCategory()!=null){
							if (group.getGroupCategory()==1&&groupId==0){
								group=sysGroupsManagementService.get(group.getGroupParentId());
							}
							
							for(Map<String, Object> map:campusInfo){
								if (map.get("id").equals(group.getId())){
									map.put("selected", "selected");
									//取班级信息
									parameters.clear();
									parameters.put("groupParentId", group.getId());
									logger.info("this is [showstudentinfo.do] find campus isAssistant ["+isAssistant+"] groupId ["+group.getId()+"]...");
									classInfo=sysGroupsManagementService.findGroup(parameters,userId);
									break;
								}
							}
						}
					}
					jsonResult.put("campusInfo", campusInfo);
					
					if (studentUser.getSysGroups()!=null&&!studentUser.getSysGroups().isEmpty()){
						group=studentUser.getSysGroups().get(0);
						if (group!=null&&group.getGroupCategory()!=null){
//							if (group.getGroupCategory()==1&&groupId==0){
//								group=sysGroupsManagementService.get(group.getGroupParentId());
//							}
							for(Map<String, Object> map:classInfo){
								if (map.get("id").equals(group.getId())){
									map.put("selected", "selected");
									break;
								}
							}
						}
					}
					jsonResult.put("classInfo", classInfo);
					
					result.put("status", 1);
					result.put("info", "operation success");
					result.put("data", jsonResult);
				}else{
					result.put("status", -2);
					result.put("info", "illegal user");
					logger.info("this is [showstudentinfo.do] illegal user ...");
				}
			}catch(Exception ex){
				ex.printStackTrace();
				result.put("status", 0);
				result.put("info", "find user info error");
				logger.info("this is [showstudentinfo.do] exception ...");
			}
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [showstudentinfo.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/savestudentinfo.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="保存学生信息")
	public String saveStudentInfo(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [savestudentinfo.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [savestudentinfo.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [savestudentinfo.do] decode done ...");
		
		Integer userId=-1;
//		Integer currentUserId=-1;
		String token=null;
		SysUsers originalStudent=null;
		if (!json.has("userId")||!json.has("token")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [savestudentinfo.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				userId=json.getInt("userId");
				token=json.getString("token");
				String localToken=WebApplicationUtils.getToken(userId);
//				if (request.getSession().getAttribute("u_id")!=null&&!request.getSession().getAttribute("u_id").equals("")){
//					currentUserId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
//				}
				Integer studentId=Integer.parseInt(json.has("studentId")?json.getString("studentId"):"-1");
				Integer isLearnChinese=Integer.parseInt(json.has("isLearnChinese")?json.getString("isLearnChinese"):"-1");
				Integer studentGroupClassId=Integer.parseInt(json.has("studentGroupClassId")?json.getString("studentGroupClassId"):"-1");
				
				if (localToken!=null&&localToken.equals(token)
						&&!studentId.equals(-1)&&!isLearnChinese.equals(-1)&&!studentGroupClassId.equals(-1)){
					originalStudent=sysUsersManagementService.get(studentId);
//					String userName=json.has("studentName")?json.getString("studentName"):null;
					String chineseName=json.has("chineseName")?json.getString("chineseName"):null;
					String englishName=json.has("englishName")?json.getString("englishName"):null;
					String mobilePhone=json.has("mobilePhone")?json.getString("mobilePhone"):null;
					String emailAddress=json.has("emailAddress")?json.getString("emailAddress"):null;
					
//					if (userName!=null){
//						originalStudent.setUserName(userName);
//					}
					if (chineseName!=null){
						originalStudent.setChineseName(chineseName);
					}
					if (englishName!=null){
						originalStudent.setEnglishName(englishName);
					}
					if (mobilePhone!=null){
						originalStudent.setMobilePhone(mobilePhone);
					}
					if (emailAddress!=null){
						originalStudent.setEmailAddress(emailAddress);
					}
					
					String pinyin=json.has("pinyin")?json.getString("pinyin"):null;
					String homeAddress=json.has("homeAddress")?json.getString("homeAddress"):null;
					String homePhone=json.has("homePhone")?json.getString("homePhone"):null;
					String daySchool=json.has("daySchool")?json.getString("daySchool"):null;
					String daySchoolGrade=json.has("daySchoolGrade")?json.getString("daySchoolGrade"):null;
					if (pinyin!=null){
						originalStudent.setPinyin(pinyin);
					}
					if (homeAddress!=null){
						originalStudent.setHomeAddress(homeAddress);
					}
					if (homePhone!=null){
						originalStudent.setHomePhone(homePhone);
					}
					if (daySchool!=null){
						originalStudent.setDaySchool(daySchool);
					}
					if (daySchoolGrade!=null){
						originalStudent.setDaySchoolGrade(daySchoolGrade);
					}
					
					SysProperties property=sysPropertiesManagementService.get(isLearnChinese);
					originalStudent.setPropertyIsLearnChinese(property);
					logger.info("this is [savestudentinfo.do] show user ["+originalStudent+"] ...");
					
					result=sysGroupsManagementService.saveStudentGroupChange(originalStudent,studentGroupClassId,sysUsersManagementService.get(userId));
					result.put("info", "operation success");
				}else{
					result.put("status", -2);
					result.put("info", "illegal user");
					logger.info("this is [savestudentinfo.do] illegal user ...");
				}
			}catch(Exception ex){
				ex.printStackTrace();
				result.put("status", 0);
				result.put("info", "find user info error");
				logger.info("this is [savestudentinfo.do] exception ...");
			}
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [savestudentinfo.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/deletestudents.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="批量删除学生信息")
	public String deleteMultipleStudents(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [deletestudents.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [deletestudents.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [deletestudents.do] decode done ...");
		
		Integer userId=-1;
//		Integer currentUserId=-1;
		String token=null;
		if (!json.has("userId")||!json.has("token")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [deletestudents.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				userId=json.getInt("userId");
				token=json.getString("token");
				String localToken=WebApplicationUtils.getToken(userId);
//				if (request.getSession().getAttribute("u_id")!=null&&!request.getSession().getAttribute("u_id").equals("")){
//					currentUserId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
//				}
				String studentIds=json.getString("studentIds")==null||json.getString("studentIds").equals("")||json.getString("studentIds").equals("null")
						?"":json.getString("studentIds");
				
				if (localToken!=null&&localToken.equals(token)){
					logger.info("this is [deletestudents.do] ready to delete ...");
					sysUsersManagementService.deleteMultiple(studentIds);
					result.put("status", 1);
					result.put("info", "operation success!");
					logger.info("this is [deletestudents.do] to delete done...");
				}else{
					result.put("status", -2);
					result.put("info", "illegal user");
					logger.info("this is [showpersonalinfo.do] illegal user ...");
				}
			}catch(Exception ex){
				ex.printStackTrace();
				result.put("status", 0);
				result.put("info", "find user info error");
				logger.info("this is [deletestudents.do] exception ...");
			}
		}
		
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [savestudentinfo.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/showstudentssorceinfo.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="显示学生成绩信息")
	public String showStudentsSorceInfo(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [showstudentssorceinfo.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [showstudentssorceinfo.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [showstudentssorceinfo.do] decode done ...");
		
		Integer userId=-1;
//		Integer currentUserId=-1;
		Integer studentId=-1;
		String token=null;
		if (!json.has("userId")||!json.has("token")||!json.has("studentId")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [showstudentssorceinfo.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				userId=json.getInt("userId");
				token=json.getString("token");
				String localToken=WebApplicationUtils.getToken(userId);
//				if (request.getSession().getAttribute("u_id")!=null&&!request.getSession().getAttribute("u_id").equals("")){
//					currentUserId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
//				}
				logger.info("this is [showstudentssorceinfo.do] userId ["+userId+"] ...");
				if (localToken!=null&&localToken.equals(token)){
					List<SysProperties> examNames=null;
					List<Map<String, Object>> lstEN=new ArrayList<Map<String, Object>>();
					List<Map<String, Object>> scores=new ArrayList<Map<String, Object>>();
					Map<String, Object> jsonTmp=null;
					Map<String, Object> resultData=new HashMap<String,Object>();
					
					logger.info("this is [showstudentssorceinfo.do] find user ...");
					studentId=json.getInt("studentId");
					SysUsers studentUser=sysUsersManagementService.get(studentId);
					if (studentUser!=null&&studentUser.getStudentsScores()!=null&&!studentUser.getStudentsScores().isEmpty()){
						for (AppStudentsScores score:studentUser.getStudentsScores()){
							jsonTmp=new HashMap<String, Object>();
							jsonTmp.put("id", score.getAppScoreProperty().getId());
							jsonTmp.put("scoreValue", score.getScoreValue());
							scores.add(jsonTmp);
						}
					}
					
					Map<String, Object> parameters=new HashMap<String, Object>();
					parameters.put("propertyParentId", 4);//change here to find exam_name;
					examNames=sysPropertiesManagementService.findProperties(parameters);
					for (SysProperties property:examNames){
						jsonTmp=new HashMap<String, Object>();
						jsonTmp.put("id", property.getId());
						jsonTmp.put("text", property.getPropertyName());
						lstEN.add(jsonTmp);
					}
					
					resultData.put("studentId", studentUser.getId());
					resultData.put("studentChineseName", studentUser.getChineseName());
					resultData.put("studentScores", scores);
					resultData.put("studentAllExam", lstEN);
					
					result.put("status", 1);
					result.put("data", resultData);
					result.put("info", "operation success!");
				}else{
					result.put("status", -2);
					result.put("info", "illegal user");
					logger.info("this is [showpersonalinfo.do] illegal user ...");
				}
			}catch(Exception ex){
				ex.printStackTrace();
				result.put("status", 0);
				result.put("info", "find user info error");
				logger.info("this is [showstudentssorceinfo.do] exception ...");
			}
		}

		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [savestudentinfo.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/savestudentscoresinfo.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="保存学生成绩信息")
	public String saveStudentScoresInfo(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [savestudentscoresinfo.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [savestudentscoresinfo.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [savestudentscoresinfo.do] decode done ...");
		
		Integer userId=-1;
		Integer studentId=-1;
		String token=null;
		if (!json.has("userId")||!json.has("token")||!json.has("studentId")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [showstudentssorceinfo.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				userId=json.getInt("userId");
				token=json.getString("token");
				String localToken=WebApplicationUtils.getToken(userId);
//				if (request.getSession().getAttribute("u_id")!=null&&!request.getSession().getAttribute("u_id").equals("")){
//					currentUserId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
//				}
				logger.info("this is [showstudentssorceinfo.do] userId ["+userId+"] ...");
				if (localToken!=null&&localToken.equals(token)){
					studentId=json.getInt("studentId");
					SysUsers studentUser=sysUsersManagementService.get(studentId);
					if (studentUser!=null){
						List<AppStudentsScores> studentScores=new ArrayList<AppStudentsScores>();
						AppStudentsScores score=null;
						
						Map<String,Object> parameters=new HashMap<String,Object>();
						parameters.put("propertyParentId", 4);//change here to find exam_name;
						List<SysProperties> examNames=sysPropertiesManagementService.findProperties(parameters);
						int examValue=-1;
						for (SysProperties property:examNames){
							if (json.has("property_"+property.getId())){
								examValue=json.getInt("property_"+property.getId());
							}
							
							if (examValue!=-1){
								logger.info("this is [savestudentscoresinfo.do] loading data from request property_"+property.getId()+" ["+examValue+"] ...");
								score=new AppStudentsScores();
								score.setAppScoreProperty(property);
								score.setAppScoreUser(studentUser);
								score.setScoreValue(examValue);
								studentScores.add(score);
							}
						}
						if (studentUser.getStudentsScores()==null||studentUser.getStudentsScores().isEmpty()){
							studentUser.setStudentsScores(studentScores);
						}else{
							List<AppStudentsScores> currentScores=studentUser.getStudentsScores();
							for (AppStudentsScores currentScore:currentScores){
								for (AppStudentsScores studentScore:studentScores){
									if (studentScore.getAppScoreProperty().getId()==currentScore.getAppScoreProperty().getId()){
										currentScore.setScoreValue(studentScore.getScoreValue());
										break;
									}
								}
							}
							studentUser.setStudentsScores(currentScores);
						}
						
						logger.info("this is [savestudentscoresinfo.do] saving user ...");
						sysUsersManagementService.save(studentUser);
						result.put("status", 1);
						result.put("info", "operation success!");
						logger.info("this is [savestudentscoresinfo.do] saving user done...");
					}else{
						result.put("status", 0);
						result.put("info", "find user info error");
						logger.info("this is [showstudentssorceinfo.do] find user info error ...");
					}
				}else{
					result.put("status", -2);
					result.put("info", "illegal user");
					logger.info("this is [showpersonalinfo.do] illegal user ...");
				}
			}catch(Exception ex){
				ex.printStackTrace();
				result.put("status", 0);
				result.put("info", "find user info error");
				logger.info("this is [showstudentssorceinfo.do] exception ...");
			}
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [showstudentssorceinfo.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/initsysnotificationdetailtable.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="查询通知列表信息")
	public String initSysNotificationDetailTable(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [initsysnotificationdetailtable.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [initsysnotificationdetailtable.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [initsysnotificationdetailtable.do] decode done ...");
		
		Integer userId=-1;
//		Integer currentUserId=-1;
		String token=null;
		if (!json.has("userId")||!json.has("token")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [initsysnotificationdetailtable.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				userId=json.getInt("userId");
				token=json.getString("token");
				String localToken=WebApplicationUtils.getToken(userId);
//				if (request.getSession().getAttribute("u_id")!=null&&!request.getSession().getAttribute("u_id").equals("")){
//					currentUserId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
//				}
				logger.info("this is [initsysnotificationdetailtable.do] userId ["+userId+"] ...");
				if (localToken!=null&&localToken.equals(token)){
					//每页大小
					int displayLength=Integer.parseInt(json.has("displayLength")?json.getString("displayLength"):"10");
					//起始值
					int displayStart=Integer.parseInt(json.has("displayStart")?json.getString("displayStart"):"0");
					//查询条件
					String param=json.has("param")?json.getString("param"):"";
					
					logger.info("this is [showstudentlist.do] requset parameters [displayLength = {"+displayLength+"}],[displayStart = {"+displayStart+"}],[param = {"+param+"}]");
					
					Map<String, Object> parameters=new HashMap<String, Object>();
					if (param!=null&&!param.equals("")){
						parameters.put("snn.notificationTitle", param);
						parameters.put("snn.notificationMessage", param);
					}
//					if (notificationId!=-1){
//						parameters.put("id", notificationId);
//					}
//					if (message!=null&&!message.equals("")){
//						parameters.put("notificationMessage", message);
//					}
//					
//					if (sort!=null&&!sort.equals("")){
						parameters.put("sort", 3);
//					}
//					if (dir!=null&&!dir.equals("")){
						parameters.put("order", "desc");
//					}
					
//					if (request.getSession().getAttribute("u_id")!=null){
//						Integer userId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
						parameters.put("snd.detailReceiveUserInfo.id", userId);
//					}
					
					logger.info("this is [initsysnotificationdetailtable.do] finding notification ...");
					Pagination<SysNotification> page=sysNotificationDetailManagementService.searchData(displayLength, displayStart, parameters);
					
					List<Map<String, Object>> resultData=new ArrayList<Map<String, Object>>();
					Map<String, Object> map=null;
					for (SysNotification noti:page.getRows()){
						map=new HashMap<String, Object>();
						map.put("notificationId", noti.getId());
						map.put("notificationTitle", noti.getNotificationTitle());
						map.put("notificationMessage", noti.getNotificationMessage());
						map.put("notificationSendUser", noti.getNotificationUserInfo().getChineseName());
						resultData.add(map);
					}
					
					result.put("data", resultData);
					result.put("status", 1);
					result.put("info", "operation success!");
					result.put("recordsTotal", page.getTotal());
					result.put("displayLength", displayLength);
					result.put("displayStart", displayStart+displayLength);
					result.put("param", param);
					logger.info("this is [initsysnotificationdetailtable.do] finding notification done...");
				}else{
					result.put("status", -2);
					result.put("info", "illegal user");
					logger.info("this is [showpersonalinfo.do] illegal user ...");
				}
			}catch(Exception ex){
				ex.printStackTrace();
				result.put("status", 0);
				result.put("info", "find user info error");
				logger.info("this is [initsysnotificationdetailtable.do] exception ...");
			}
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [initsysnotificationdetailtable.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/searchfreindlist.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="查询好友列表")
	public String searchFreindList(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [searchfreindlist.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [searchfreindlist.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [searchfreindlist.do] decode done ...");
		
		Integer userId=-1;
		String token=null;
		if (!json.has("userId")||!json.has("token")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [searchfreindlist.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				userId=json.getInt("userId");
				token=json.getString("token");
				String localToken=WebApplicationUtils.getToken(userId);
				logger.info("this is [searchfreindlist.do] userId ["+userId+"] ...");
				if (localToken!=null&&localToken.equals(token)){
					SysUsers currentUser=sysUsersManagementService.get(userId);
					
					List<SysRoles> roles=currentUser.getSysRoles();
					if (roles!=null&&!roles.isEmpty()){
						int roleCategory=-1;//1-amdin, 2-assistant of campus, 3-teacher or student or parent
						for (SysRoles role:roles){
							if(role.getId()==1||role.getId()==2){
								roleCategory=1;
								break;
							}else if (role.getId()==3){
								roleCategory=2;
							}else{
								roleCategory=roleCategory==2?2:3;
							}
						}
						Map<String, Object> parameters=new HashMap<String, Object>();
						if (roleCategory==1){//find all users
							result.put("data", sysUsersManagementService.findAllSysUsersByParameters(parameters));
						}else if (roleCategory==2){//find campus and classes
							if (currentUser.getSysGroups()==null||currentUser.getSysGroups().isEmpty()){
								result.put("data", "");
							}else{
								List<Integer> campusIds=new ArrayList<>();
								List<Integer> classIds=new ArrayList<>();
								for (SysGroups group:currentUser.getSysGroups()){
									if (group.getGroupCategory()==0){
										campusIds.add(group.getId());
									}else if(group.getGroupCategory()==1){
										classIds.add(group.getId());
									}
								}
								result.put("data", sysUsersManagementService.findUsersByGroupIds(campusIds, classIds));
							}
						}else if (roleCategory==3){
							if (currentUser.getSysGroups()==null||currentUser.getSysGroups().isEmpty()){
								result.put("data", "");
							}else{
								List<Integer> classIds=new ArrayList<>();
								for (SysGroups group:currentUser.getSysGroups()){
									if(group.getGroupCategory()==1){
										classIds.add(group.getId());
									}
								}
								result.put("data", sysUsersManagementService.findUsersByGroupIds(null, classIds));
							}
						}
						@SuppressWarnings("unchecked")
						List<Map<String, Object>> lst=(List<Map<String, Object>>)result.get("data");
						
						if (roleCategory==2||roleCategory==3){
							boolean needAndAdmin=false;
							for (SysRoles role:roles){
								if (role.getId()==3||role.getId()==4){
									needAndAdmin=true;
								}
							}
							
							if (needAndAdmin){
								List<SysUsers> lstAdminAssistant=sysRolesManagementService.get(2).getSysRolesUsers();
								if (lstAdminAssistant!=null&&!lstAdminAssistant.isEmpty()){
									Map<String, Object> map=null;
									for (SysUsers assistant:lstAdminAssistant){
										map=new HashMap<String, Object>();
										map.put("id", assistant.getId());
										map.put("userName", assistant.getUserName());
										map.put("chineseName", assistant.getChineseName());
										map.put("englishName", assistant.getEnglishName());
										lst.add(0,map);
									}
								}
								
								List<SysUsers> lstAdmin=sysRolesManagementService.get(1).getSysRolesUsers();
								if (lstAdmin!=null&&!lstAdmin.isEmpty()){
									Map<String, Object> map=null;
									for (SysUsers admin:lstAdmin){
										map=new HashMap<String, Object>();
										map.put("id", admin.getId());
										map.put("userName", admin.getUserName());
										map.put("chineseName", admin.getChineseName());
										map.put("englishName", admin.getEnglishName());
										lst.add(0,map);
									}
								}
							}
						}
						
						List<Map<String, Object>> lstData=new ArrayList<Map<String, Object>>();
						Integer tmpInt=-1;
						for (Map<String, Object> map:lst){
							tmpInt=Integer.parseInt(""+map.get("id"));
							if (tmpInt!=userId){
								lstData.add(map);
							}
						}
						result.put("data", lstData);
						result.put("status", 1);
						result.put("info", "operation success!");
						logger.info("this is [searchfreindlist.do] finding notification done...");
					}else{
						result.put("status", -1);
						result.put("info", "operation failed!");
						logger.info("this is [searchfreindlist.do] found roles is empty...");
					}
				}else{
					result.put("status", -2);
					result.put("info", "illegal user");
					logger.info("this is [searchfreindlist.do] illegal user ...");
				}
			}catch(Exception ex){
				ex.printStackTrace();
				result.put("status", 0);
				result.put("info", "find user info error");
				logger.info("this is [searchfreindlist.do] exception ...");
			}
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [searchfreindlist.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/deletenotifications.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="批量删除通知信息")
	public String deleteMultipleNotifications(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [deletenotifications.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [deletenotifications.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [deletenotifications.do] decode done ...");
		
		Integer userId=-1;
		String token=null;
		if (!json.has("userId")||!json.has("token")||!json.has("deleteIds")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [deletenotifications.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				userId=json.getInt("userId");
				token=json.getString("token");
				String localToken=WebApplicationUtils.getToken(userId);
				if (localToken!=null&&localToken.equals(token)){
					String deleteIds=json.getString("deleteIds");
					if (deleteIds!=null&&!deleteIds.equals("")){
						try {
							logger.info("this is [deletenotifications.do] ready to delete ...");
							sysNotificationDetailManagementService.deleteMultiple(deleteIds, userId);
							result.put("status", 1);
							result.put("info", "operation success!");
							logger.info("this is [deletenotifications.do] to delete done...");
						} catch (Exception e) {
							logger.info("this is [deletenotifications.do] to trough exception when delete ...");
							result.put("status", 0);
							result.put("info", "delete failed, try again!");
							e.printStackTrace();
						}
					}
				}else{
					result.put("status", -2);
					result.put("info", "illegal user");
					logger.info("this is [searchfreindlist.do] illegal user ...");
				}
			}catch(Exception ex){
				ex.printStackTrace();
				result.put("status", 0);
				result.put("info", "find user info error");
				logger.info("this is [deletenotifications.do] exception ...");
			}
		}
		
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [deletenotifications.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/findAllCampusOrClass.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="查询全部校区或班级")
	public String findAllCampusOrClass(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [findAllCampusOrClass.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [findAllCampusOrClass.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [findAllCampusOrClass.do] decode done ...");
		
		Integer userId=-1;
		String token=null;
		if (!json.has("userId")||!json.has("token")||!json.has("groupCategory")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [findAllCampusOrClass.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				userId=json.getInt("userId");
				token=json.getString("token");
				String localToken=WebApplicationUtils.getToken(userId);
				if (localToken!=null&&localToken.equals(token)){
					Integer groupCategory=json.getInt("groupCategory");
//					result.put("data", sysGroupsManagementService.findAllCampusOrClass(null, groupCategory));
//					result.put("status", 1);
//					result.put("info", "operation success");
					SysUsers currentUser=sysUsersManagementService.get(userId);
					if (currentUser!=null&&currentUser.getSysGroups()!=null&&!currentUser.getSysGroups().isEmpty()){
						List<SysGroups> lstGroups=currentUser.getSysGroups();
						boolean isAll=false;
						
						List<Map<String,Object>> resultList=new ArrayList<Map<String,Object>>();
						Map<String,Object> contentMap=null;
						for (SysGroups group:lstGroups){
							if (group.getGroupParentId()==-1){
								isAll=true;
								break;
							}
							
							if (group.getGroupCategory()==groupCategory){
								contentMap=new HashMap<String,Object>();
								contentMap.put("id", group.getId());
								contentMap.put("text", group.getGroupName());
								resultList.add(contentMap);
							}
						}
						List<Map<String,Object>> lstMaps=null;
						if (isAll){
							lstMaps=sysGroupsManagementService.findGroupsForTree(new HashMap<String, Object>());
							if (lstMaps!=null){
								resultList=new ArrayList<Map<String,Object>>();
								String parent=null;
								for (Map<String,Object> map:lstMaps){
									parent=""+map.get("parent");
									if (groupCategory==0&&parent.equals("1")){
										resultList.add(map);
									}else if (groupCategory==1&&!parent.equals("#")&&!parent.equals("1")){
										resultList.add(map);
									}
								}
							}
						}
						result.put("data", resultList);
						result.put("status", 1);
						result.put("info", "operation success");
					}
				}else{
					result.put("status", -2);
					result.put("info", "illegal user");
					logger.info("this is [findAllCampusOrClass.do] illegal user ...");
				}
			}catch(Exception ex){
				ex.printStackTrace();
				result.put("status", 0);
				result.put("info", "find user info error");
				logger.info("this is [findAllCampusOrClass.do] exception ...");
			}
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [findAllCampusOrClass.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/saveandsendnotification.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="保存并发送通知")
	public String saveAndSendNotification(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [saveandsendnotification.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [saveandsendnotification.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [saveandsendnotification.do] decode done ...");
		
		Integer userId=-1;
		String token=null;
		if (!json.has("userId")||!json.has("token")||!json.has("groupIds")||!json.has("notificationTitle")||!json.has("notificationMessage")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [saveandsendnotification.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				userId=json.getInt("userId");
				token=json.getString("token");
				String localToken=WebApplicationUtils.getToken(userId);
				if (localToken!=null&&localToken.equals(token)){
					SysUsers currentUser=sysUsersManagementService.get(userId);
					
					String groupIds=json.getString("groupIds");
					String notificationTitle=json.getString("notificationTitle");
					String notificationMessage=json.getString("notificationMessage");
					
					SysNotification notification=new SysNotification();
					notification.setNotificationTitle(notificationTitle);
					notification.setNotificationMessage(notificationMessage);
//					notification.setNotificationReceiveGroupIds(groupIds);
					notification.setNotificationUserInfo(currentUser);
					
					int roleCategory=-1;//1:全查，2:查校区(用户所在)和班级，3:查班级
					
					List<SysRoles> lstRoles=currentUser.getSysRoles();
					for (SysRoles role:lstRoles){
						if (role.getId()==1||role.getId()==2){//管理者|管理助理
							roleCategory=1;
							break;
						}else if (role.getId()==3){//校区助理
							roleCategory=2;
						}else if (role.getId()==4&&roleCategory!=2){//校区助理
							roleCategory=3;
						}
					}
					logger.info("this is [saveandsendnotification.do] get [u_sr] is ["+roleCategory+"]...");
					List <SysGroups> lstGroups=null;
					if (roleCategory==-1){
						result.put("status",0);
						result.put("info", "wrong role, login again please");
					}else if (roleCategory!=4){//非教师
						List<SysGroups> lst=sysGroupsManagementService.findSubGroups(groupIds);
						if (lst!=null&&!lst.isEmpty()){
							for (SysGroups group:lst){
								groupIds+=","+group.getId();
							}
						}
					}
					notification.setNotificationReceiveGroupIds(groupIds);
					List<SysUsers> lstReceiveUsers=sysGroupsManagementService.findSysUsersInGroups(groupIds, roleCategory, lstGroups);
					
					//for send notification to App users
					List<Integer> userIds=new ArrayList<Integer>();
					//end
					
					SysNotificationDetail notificationDetail=null;
					List<SysNotificationDetail> details=new ArrayList<SysNotificationDetail>();
					for (SysUsers receiveUser:lstReceiveUsers){
						if (receiveUser.getId().equals(notification.getNotificationUserInfo().getId())){
							continue;
						}
						notificationDetail=new SysNotificationDetail();
						notificationDetail.setDetailReceiveUserInfo(receiveUser);
						logger.info("this is [savesysnotification.do] get [receive_user] is ["+receiveUser.getUserName()+"]...");
						notificationDetail.setIsRead(0);
						notificationDetail.setDetailNotificationInfo(notification);
						details.add(notificationDetail);
						
						//for send notification to App users
						userIds.add(receiveUser.getId());
						//end
					}
					notification.setSysNotificationDetailInfo(details);
					notification.setIsSend(1);
					logger.info("this is [saveandsendnotification.do] is saving...");
					sysNotificationManagementService.save(notification);
					
					//send notification to App user
					List<SysDeviceToken> deviceTokens=sysDeviceTokenManagementService.findDeviceTokenByUserId(userIds);
					String text="您有一条新通知";
					String ticker="通知";
					String title="通知";
					sysDeviceTokenManagementService.sendNotificationToApp(deviceTokens, text, title, ticker);
					//end
					
					result.put("status", 1);
					result.put("info", "operation success");
				}else{
					result.put("status", -2);
					result.put("info", "illegal user");
					logger.info("this is [saveandsendnotification.do] illegal user ...");
				}
			}catch(Exception ex){
				ex.printStackTrace();
				result.put("status", 0);
				result.put("info", "find user info error");
				logger.info("this is [saveandsendnotification.do] exception ...");
			}
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [saveandsendnotification.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	private String decodeParameters(String parameters){
		if (parameters!=null&&!parameters.equals("")){
			try {
				logger.info("this is [changepassword.do] is decoding ...");
				parameters=URLDecoder.decode(parameters, "utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.info("this is [changepassword.do] occur error when program decoding ...");
				e.printStackTrace();
			}
		}
		return parameters;
	}
	
	private boolean isStudent(SysUsers user){
		boolean isStudent=false;
		for (SysRoles role:user.getSysRoles()){
			if (role.getId().equals(5)){
				isStudent=true;
				break;
			}
		}
		return isStudent;
	}
	
	private boolean isParent(SysUsers user){
		boolean isParent=false;
		for (SysRoles role:user.getSysRoles()){
			if (role.getId().equals(6)){
				isParent=true;
				break;
			}
		}
		return isParent;
	}
	
	//--------------------------学生作业管理---------------------------------//
	
	@RequestMapping(value = "/initstudentuploadassignmentlist.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="查询学生上传作业列表")
	public String initStudentUploadAssignmentList(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [initstudentuploadassignmentlist.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [initstudentuploadassignmentlist.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [initstudentuploadassignmentlist.do] decode done ...");
		
		Integer currentStudentId=-1;
		String token=null;
		if (!json.has("userId")||!json.has("token")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [initstudentuploadassignmentlist.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				currentStudentId=json.getInt("userId");
			}catch(Exception ex){
				result.put("status", 0);
				result.put("info", "wrong parameter is userId");
				logger.info("this is [initstudentuploadassignmentlist.do] wrong parameter is userId ...");
			}
			
			try{
				token=json.getString("token");
			}catch(Exception ex){
				result.put("status", 0);
				result.put("info", "wrong parameter is token");
				logger.info("this is [initstudentuploadassignmentlist.do] wrong parameter is token ...");
			}
			
			if (result.isEmpty()){
				try {
					String localToken=WebApplicationUtils.getToken(currentStudentId);
					
					if (localToken!=null&&localToken.equals(token)){
						logger.info("this is [initstudentuploadassignmentlist.do] confirm identity...");
						//每页大小
						int displayLength=Integer.parseInt(json.has("displayLength")?json.getString("displayLength"):"10");
						//起始值
						int displayStart=Integer.parseInt(json.has("displayStart")?json.getString("displayStart"):"0");
						//查询条件
						String param=json.has("param")?json.getString("param"):"";
						
						logger.info("this is [initstudentuploadassignmentlist.do] requset parameters [displayLength = {"+displayLength+"}],[displayStart = {"+displayStart+"}],[param = {"+param+"}]");
						
						Map<String, Object> parameters=new HashMap<String, Object>();
						if (param!=null&&!param.equals("")){
							parameters.put("assignmentName", param);
						}
						parameters.put("sort", 3);
						parameters.put("order", "desc");
						
						result=appStudentUploadAssignmentSerivce.searchDataForAPP(displayLength, displayStart, parameters, currentStudentId);
						result.put("status", 1);
						result.put("info", "operation success");
						result.put("displayLength", displayLength);
						result.put("displayStart", displayStart+displayLength);
						result.put("param", param);
						logger.info("this is [initstudentuploadassignmentlist.do] find assignment list done ...");
					}else{
						result.put("status", -2);
						result.put("info", "illegal user");
						logger.info("this is [initstudentuploadassignmentlist.do] illegal user ...");
					}
				} catch (Exception e) {
					e.printStackTrace();
					result.put("status", 0);
					result.put("info", "operation failed");
					logger.info("this is [initstudentuploadassignmentlist.do] occur error ...");
				}
			}
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [initstudentuploadassignmentlist.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/savestudentuploadassignmentinfo.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="保存学生上传作业信息")
	public String saveStudentUploadAssignmentInfo(HttpServletRequest request, @RequestParam("uploadFile") CommonsMultipartFile[] uploadFile) {
		logger.info("this is [savestudentuploadassignmentinfo.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		
		logger.info("this is [savestudentuploadassignmentinfo.do] get page parameters ...");
		Integer currentStudentId=0;
		String uploadAssignmentName=null, token=null;
		AppStudentUploadAssignment uploadAssignment=null;
		
		try {
			uploadAssignmentName=ServletRequestUtils.getRequiredStringParameter(request, "assignmentName");
		} catch (ServletRequestBindingException e) {
			e.printStackTrace();
			logger.info("this is [savestudentuploadassignmentinfo.do] the lack of parameter is assignmentName ...");
		}
		
		try {
			currentStudentId=ServletRequestUtils.getRequiredIntParameter(request, "userId");
		}catch (ServletRequestBindingException e) {
			//e.printStackTrace();
			try {
				currentStudentId=Integer.parseInt(ServletRequestUtils.getRequiredStringParameter(request, "userId").trim());
			}catch (Exception e1) {
				e1.printStackTrace();
				result.put("status", -1);
				result.put("info", "the lack of parameter");
				logger.info("this is [savestudentuploadassignmentinfo.do] the lack of parameter [userId] ...");
			}
		}
		
		try {
//			currentStudentId=ServletRequestUtils.getRequiredIntParameter(request, "userId");
			token=ServletRequestUtils.getRequiredStringParameter(request, "token").trim();
		} catch (ServletRequestBindingException e) {
			e.printStackTrace();
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [savestudentuploadassignmentinfo.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			String localToken=WebApplicationUtils.getToken(currentStudentId);
			if (localToken!=null&&localToken.equals(token)){
				logger.info("this is [initstudentuploadassignmentlist.do] confirm identity...");
				Calendar now=new GregorianCalendar();
				String year="/"+now.get(Calendar.YEAR)+"/";
				String month=(now.get(Calendar.MONTH)+1)>9?"/"+(now.get(Calendar.MONTH)+1)+"/":"/0"+(now.get(Calendar.MONTH)+1)+"/";
				String day=now.get(Calendar.DAY_OF_MONTH)>9?"/"+now.get(Calendar.DAY_OF_MONTH)+"/":"/0"+now.get(Calendar.DAY_OF_MONTH)+"/";
				
				String _localPath=year;
				File directory=new File(_filePath+_localPath);
				if (!directory.exists()){
					directory.mkdir();
					logger.info("this is [savestudentuploadassignmentinfo.do] create directory ["+directory+"] success...");
				}
				
				_localPath+=month;
				directory=new File(_filePath+_localPath);
				if (!directory.exists()){
					directory.mkdir();
					logger.info("this is [savestudentuploadassignmentinfo.do] create directory ["+directory+"] success...");
				}
				
				_localPath+=day;
				directory=new File(_filePath+_localPath);
				if (!directory.exists()){
					directory.mkdir();
					logger.info("this is [savestudentuploadassignmentinfo.do] create directory ["+directory+"] success...");
				}
				
				_localPath+=File.separator+"student_"+currentStudentId;
				directory=new File(_filePath+_localPath);
				if (!directory.exists()){
					directory.mkdir();
					logger.info("this is [savestudentuploadassignmentinfo.do] create directory ["+directory+"] success...");
				}
				logger.info("this is [savestudentuploadassignmentinfo.do] show directory name ["+directory+"]");
				
				File file=null;
				String strFileName=null;
				String strSuffix=null;
				int dotPosition=-1;
				
				for (int i=0;i<uploadFile.length;i++){
					if (!uploadFile[i].isEmpty()){
						strFileName=uploadFile[i].getOriginalFilename();
						logger.info("this is [savestudentuploadassignmentinfo.do] show file name ["+strFileName+"]");
						
						dotPosition=strFileName.lastIndexOf(".");
						strSuffix=strFileName.substring(dotPosition);
						
						if (strSuffix!=null&&!strSuffix.equals("")&&(strSuffix.equals(".doc")||strSuffix.equals(".docx")||strSuffix.equals(".pdf"))){
							file=new File(directory+File.separator+Calendar.getInstance().getTimeInMillis());
							
							while(file.exists()){
								logger.info("this is [savestudentuploadassignmentinfo.do] file ["+file.getName()+"] exist...");
								file=new File(directory+File.separator+Calendar.getInstance().getTimeInMillis());
								logger.info("this is [savestudentuploadassignmentinfo.do] change file name ["+file.getName()+"]...");
							}
						}
						
						if (!file.exists()){
							logger.info("this is [savestudentuploadassignmentinfo.do] file ["+file.getName()+"] do not exist...");
							try {
								file.createNewFile();
								logger.info("this is [savestudentuploadassignmentinfo.do] create file ["+file.getName()+"] success...");
								uploadFile[i].transferTo(file);
								logger.info("this is [savestudentuploadassignmentinfo.do] file ["+file.getName()+"] transfer...");
							} catch (IOException e) {
								logger.info("this is [savestudentuploadassignmentinfo.do] create file ["+file.getName()+"] failed...");
								result.put("status", 0);
								result.put("info", "save failed, try again!");
								e.printStackTrace();
							}
						}
						
						if (result.isEmpty()){
							uploadAssignment=new AppStudentUploadAssignment();
							String assignmentName=(uploadAssignmentName==null||uploadAssignmentName.equals(""))?strFileName:uploadAssignmentName;
							uploadAssignment.setAssignmentName(assignmentName);
							logger.info("this is [savestudentuploadassignmentinfo.do] assignment name ["+uploadAssignment.getAssignmentName()+"]...");
							uploadAssignment.setFilePath(_localPath+File.separator+file.getName());
							uploadAssignment.setFileName(strFileName);
							logger.info("this is [savestudentuploadassignmentinfo.do] set file name ["+strFileName+"]...");
							
							logger.info("this is [savestudentuploadassignmentinfo.do] find student ["+currentStudentId+"]...");
							SysUsers student=sysUsersManagementService.get(currentStudentId);
							uploadAssignment.setStudent(student);
							
							logger.info("this is [savestudentuploadassignmentinfo.do] find tutor...");
							SysGroups classGroup=student.getSysGroups().get(0);
							uploadAssignment.setTutor(sysUsersManagementService.findATutorFromGroup(classGroup.getId()));
							
							logger.info("this is [savestudentuploadassignmentinfo.do] is saving ...");
							appStudentUploadAssignmentSerivce.save(uploadAssignment);
							result.put("status", 1);
							result.put("info", "operation success!");
							logger.info("this is [savestudentuploadassignmentinfo.do] save uploadAssignment done ...");
						}
					}else{
						logger.info("this is [savestudentuploadassignmentinfo.do] save uploadAssignment error ...");
						result.put("status", 0);
						result.put("info", "save failed, try again!");
					}
				}
			}else{
				result.put("status", -2);
				result.put("info", "illegal user");
				logger.info("this is [savestudentuploadassignmentinfo.do] illegal user ...");
			}
		}
		
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [savestudentuploadassignmentinfo.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/initstudentdownloadassignmentlist.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="查询学生下载作业列表")
	public String initStudentDownloadAssignmentList(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [initstudentdownloadassignmentlist.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [initstudentdownloadassignmentlist.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [initstudentdownloadassignmentlist.do] decode done ...");
		
		Integer userId=-1;//studentId
		String token=null;
		if (!json.has("userId")||!json.has("token")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [initstudentdownloadassignmentlist.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				try {
					userId=json.getInt("userId");
				} catch (Exception e) {
					result.put("status", -1);
					result.put("info", "the lack of parameter");
					logger.info("this is [initstudentdownloadassignmentlist.do] get userId error ...");
					e.printStackTrace();
				}
				try {
					token=json.getString("token");
				} catch (Exception e) {
					result.put("status", -1);
					result.put("info", "the lack of parameter");
					logger.info("this is [initstudentdownloadassignmentlist.do] get token error ...");
					e.printStackTrace();
				}
				
				if (result.isEmpty()){
					String localToken=WebApplicationUtils.getToken(userId);
					logger.info("this is [initstudentdownloadassignmentlist.do] studentId ["+userId+"] ...");
					if (localToken!=null&&localToken.equals(token)){
						//每页大小
						int displayLength=Integer.parseInt(json.has("displayLength")?json.getString("displayLength"):"10");
						//起始值
						int displayStart=Integer.parseInt(json.has("displayStart")?json.getString("displayStart"):"0");
						//查询条件
						String param=json.has("param")?json.getString("param"):"";
						
						logger.info("this is [initstudentdownloadassignmentlist.do] requset parameters [displayLength = {"+displayLength+"}],[displayStart = {"+displayStart+"}],[param = {"+param+"}]");
						
						Map<String, Object> parameters=new HashMap<String, Object>();
						if (param!=null&&!param.equals("")){
							parameters.put("assignmentName", param);
						}
						parameters.put("sort", 3);
						parameters.put("order", "desc");
						
						logger.info("this is [initstudentdownloadassignmentlist.do] finding assignment ...");
						Pagination<AppTutorAppointmentAssignmentToStudent> page=appTutorAppointmentToStudentService.searchDataToStudent(displayLength, displayStart, parameters,userId);
						
						List<Map<String, Object>> resultData=new ArrayList<Map<String, Object>>();
						Map<String, Object> map=null;
						for (AppTutorAppointmentAssignmentToStudent toStudent:page.getRows()){
							map=new HashMap<String, Object>();
							map.put("assignmentId", toStudent.getId());
							map.put("assignmentName", toStudent.getAssignmentName());
							map.put("uploadTime", toStudent.getUploadTime()==null?"":toStudent.getUploadTime().toString());
							map.put("downloadTime", toStudent.getDownloadTime()==null?"":toStudent.getDownloadTime().toString());
							map.put("filePath", toStudent.getFilePath());
							map.put("fileName", toStudent.getFileName());
							map.put("tutorName", toStudent.getSourceTutor()==null?"":toStudent.getSourceTutor().getChineseName());
							resultData.add(map);
						}
						
						result.put("data", resultData);
						result.put("status", 1);
						result.put("info", "operation success!");
						result.put("recordsTotal", page.getTotal());
						result.put("displayLength", displayLength);
						result.put("displayStart", displayStart+displayLength);
						result.put("param", param);
						logger.info("this is [initstudentdownloadassignmentlist.do] finding assignment done...");
					}else{
						result.put("status", -2);
						result.put("info", "illegal user");
						logger.info("this is [initstudentdownloadassignmentlist.do] illegal user ...");
					}
				}
			}catch(Exception ex){
				ex.printStackTrace();
				result.put("status", 0);
				result.put("info", "find user info error");
				logger.info("this is [initstudentdownloadassignmentlist.do] exception ...");
			}
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [initstudentdownloadassignmentlist.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/inittutordownloadassignmentlist.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="查询教师下载学生作业列表")
	public String initTutorDownloadAssignmentList(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [inittutordownloadassignmentlist.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [inittutordownloadassignmentlist.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [inittutordownloadassignmentlist.do] decode done ...");
		
		Integer currentTutorId=-1;
		String token=null;
		if (!json.has("userId")||!json.has("token")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [inittutordownloadassignmentlist.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				try {
					currentTutorId=json.getInt("userId");
				} catch (Exception e) {
					result.put("status", -1);
					result.put("info", "the lack of parameter");
					logger.info("this is [inittutordownloadassignmentlist.do] get userId error ...");
					e.printStackTrace();
				}
				try {
					token=json.getString("token");
				} catch (Exception e) {
					result.put("status", -1);
					result.put("info", "the lack of parameter");
					logger.info("this is [inittutordownloadassignmentlist.do] get token error ...");
					e.printStackTrace();
				}
				
				if (result.isEmpty()){
					String localToken=WebApplicationUtils.getToken(currentTutorId);
					logger.info("this is [inittutordownloadassignmentlist.do] currentTutorId ["+currentTutorId+"] ...");
					if (localToken!=null&&localToken.equals(token)){
						//每页大小
						int displayLength=Integer.parseInt(json.has("displayLength")?json.getString("displayLength"):"10");
						//起始值
						int displayStart=Integer.parseInt(json.has("displayStart")?json.getString("displayStart"):"0");
						//查询条件
						String param=json.has("param")?json.getString("param"):"";
						
						logger.info("this is [inittutordownloadassignmentlist.do] requset parameters [displayLength = {"+displayLength+"}],[displayStart = {"+displayStart+"}],[param = {"+param+"}]");
						
						Map<String, Object> parameters=new HashMap<String, Object>();
						if (param!=null&&!param.equals("")){
							parameters.put("assignmentName", param);
//							parameters.put("campusName", param);
//							parameters.put("studentName", param);
						}
						parameters.put("sort", 6);
						parameters.put("order", "desc");
						
						SysUsers currentTutor=sysUsersManagementService.get(currentTutorId);
						if (currentTutor!=null&&currentTutor.getSysGroups()!=null){
							StringBuffer groupIds=new StringBuffer();
							List<SysGroups> groups=currentTutor.getSysGroups();
							if (!groups.isEmpty()){
								for (SysGroups group:groups){
									if (group.getGroupCategory()==1){
										if (groupIds.length()==0){
											groupIds.append(group.getId());
										}else{
											groupIds.append(",");
											groupIds.append(group.getId());
										}
									}
								}
							}
							
							logger.info("this is [inittutordownloadassignmentlist.do] finding assignment ...");
							result=appStudentUploadAssignmentSerivce.searchDataForAPP(displayLength, displayStart, parameters, groupIds.toString());
							result.put("status", 1);
							result.put("info", "operation success!");
							result.put("displayLength", displayLength);
							result.put("displayStart", displayStart+displayLength);
							result.put("param", param);
							logger.info("this is [inittutordownloadassignmentlist.do] finding assignment done...");
						}else{
							result.put("status", 0);
							result.put("info", "find user info error");
							logger.info("this is [inittutordownloadassignmentlist.do] find user info error ...");
						}
					}else{
						result.put("status", -2);
						result.put("info", "illegal user");
						logger.info("this is [inittutordownloadassignmentlist.do] illegal user ...");
					}
				}
			}catch(Exception ex){
				ex.printStackTrace();
				result.put("status", 0);
				result.put("info", "occur exception");
				logger.info("this is [inittutordownloadassignmentlist.do] exception ...");
			}
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [inittutordownloadassignmentlist.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/findalltutor.do", method=RequestMethod.POST)
	@ResponseBody
	public String findAllTutor(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [findalltutor.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [findalltutor.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [findalltutor.do] decode done ...");
		
		Integer currentTutorId=-1;
		String token=null;
		if (!json.has("userId")||!json.has("token")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [findalltutor.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try {
				currentTutorId=json.getInt("userId");
			} catch (Exception e) {
				result.put("status", -1);
				result.put("info", "the lack of parameter");
				logger.info("this is [findalltutor.do] get userId error ...");
				e.printStackTrace();
			}
			try {
				token=json.getString("token");
			} catch (Exception e) {
				result.put("status", -1);
				result.put("info", "the lack of parameter");
				logger.info("this is [findalltutor.do] get token error ...");
				e.printStackTrace();
			}
			
			if (result.isEmpty()){
				try {
					String localToken=WebApplicationUtils.getToken(currentTutorId);
					logger.info("this is [findalltutor.do] currentTutorId ["+currentTutorId+"] ...");
					if (localToken!=null&&localToken.equals(token)){
						logger.info("this is [findalltutor.do] confirm identity...");
						List<Map<String, Object>> lstData=new ArrayList<Map<String, Object>>();
						SysRoles role=sysRolesManagementService.get(4);
						logger.info("this is [findalltutor.do] get tutor roles...");
						if (role!=null&&role.getSysRolesUsers()!=null&&!role.getSysRolesUsers().isEmpty()){
							logger.info("this is [findalltutor.do] get tutor roles is not empty...");
							List<SysUsers> lstTutors=role.getSysRolesUsers();
							logger.info("this is [findalltutor.do] find all Tutors...");
							Map<String, Object> map=null;
							
							for (SysUsers tutor:lstTutors){
								if (tutor.getId().equals(currentTutorId)){
									continue;
								}
								map=new HashMap<String, Object>();
								map.put("id", tutor.getId());
								logger.info("this is [findalltutor.do] tutor chinesename ["+tutor.getChineseName()+"]...");
								map.put("text", tutor.getChineseName()+"["+((tutor.getEmailAddress()==null||tutor.getEmailAddress().equals(""))?"EMAIL":tutor.getEmailAddress())+"]");
								lstData.add(map);
							}
						}
						result.put("status", 1);
						result.put("data", lstData);
					}else{
						result.put("status", -2);
						result.put("info", "illegal user");
						logger.info("this is [findalltutor.do] illegal user ...");
					}
				} catch (Exception e) {
					result.put("status", 0);
					result.put("info", "occur exception");
					logger.info("this is [findalltutor.do] exception ...");
					e.printStackTrace();
				}
			}
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [findalltutor.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/findallstudents.do", method=RequestMethod.POST)
	@ResponseBody
	public String findAllStudents(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [findallstudents.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [findallstudents.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [findallstudents.do] decode done ...");
		
		Integer currentUserId=-1, studentId=-1;
		String token=null;
//		if (!json.has("userId")||!json.has("token")||!json.has("studentId")){
		if (!json.has("userId")||!json.has("token")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [findallstudents.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try {
				currentUserId=json.getInt("userId");
			} catch (Exception e) {
				result.put("status", -1);
				result.put("info", "the lack of parameter");
				logger.info("this is [findallstudents.do] get userId error ...");
				e.printStackTrace();
			}
			try {
				token=json.getString("token");
			} catch (Exception e) {
				result.put("status", -1);
				result.put("info", "the lack of parameter");
				logger.info("this is [findallstudents.do] get token error ...");
				e.printStackTrace();
			}
			try {
				studentId=json.getInt("studentId");
			} catch (Exception e) {
				logger.info("this is [findallstudents.do] get studentId error ...");
				studentId=-1;
//				e.printStackTrace();
			}
			
			if (result.isEmpty()){
				try {
					String localToken=WebApplicationUtils.getToken(currentUserId);
					logger.info("this is [findallstudents.do] currentTutorId ["+currentUserId+"] ...");
					if (localToken!=null&&localToken.equals(token)){
						logger.info("this is [findallstudents.do] confirm identity...");
						logger.info("this is [findallstudents.do] studentId ["+studentId+"]");
						result.put("data", appStudentUploadAssignmentSerivce.searchUploadedAssignmentStudentsByTutorId(currentUserId, studentId));
						result.put("info", "operation success");
						result.put("status", 1);
					}else{
						result.put("status", -2);
						result.put("info", "illegal user");
						logger.info("this is [findalltutor.do] illegal user ...");
					}
				} catch (Exception e) {
					result.put("status", 0);
					result.put("info", "occur exception");
					logger.info("this is [findalltutor.do] exception ...");
					e.printStackTrace();
				}
			}
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [findalltutor.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/findallappointmenttutors.do", method=RequestMethod.POST)
	@ResponseBody
	public String findAllAppointmentTutors(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [findallappointmenttutors.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [findallappointmenttutors.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [findallappointmenttutors.do] decode done ...");
		
		Integer currentUserId=-1, targetTutorId=-1;
		String token=null;
		if (!json.has("userId")||!json.has("token")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [findallappointmenttutors.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try {
				currentUserId=json.getInt("userId");
			} catch (Exception e) {
				result.put("status", -1);
				result.put("info", "the lack of parameter");
				logger.info("this is [findallappointmenttutors.do] get userId error ...");
				e.printStackTrace();
			}
			try {
				token=json.getString("token");
			} catch (Exception e) {
				result.put("status", -1);
				result.put("info", "the lack of parameter");
				logger.info("this is [findallappointmenttutors.do] get token error ...");
				e.printStackTrace();
			}
			try {
				targetTutorId=json.getInt("targetTutorId");
			} catch (Exception e) {
				targetTutorId=-1;
				logger.info("this is [findallappointmenttutors.do] get targetTutorId error ...");
//				e.printStackTrace();
			}
			
			if (result.isEmpty()){
				try {
					String localToken=WebApplicationUtils.getToken(currentUserId);
					logger.info("this is [findallappointmenttutors.do] currentTutorId ["+currentUserId+"] ...");
					if (localToken!=null&&localToken.equals(token)){
						logger.info("this is [findallappointmenttutors.do] confirm identity...");
						logger.info("this is [findallappointmenttutors.do] targetTutorId ["+targetTutorId+"]");
						result.put("data", appTutorAppointmentToTutorService.findTutorsForTutorToTutor(currentUserId, targetTutorId));
						result.put("info", "operation success");
						result.put("status", 1);
					}else{
						result.put("status", -2);
						result.put("info", "illegal user");
						logger.info("this is [findallappointmenttutors.do] illegal user ...");
					}
				} catch (Exception e) {
					result.put("status", 0);
					result.put("info", "occur exception");
					logger.info("this is [findallappointmenttutors.do] exception ...");
					e.printStackTrace();
				}
			}
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [findallappointmenttutors.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/appointmenttotutor.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="保存指派教师代审作业")
	public String appointmentToTutor(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [appointmenttotutor.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [appointmenttotutor.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [appointmenttotutor.do] decode done ...");
		
		Integer currentTutorId=-1,assignmentId=-1,toTutorId=-1;
		String token=null;
		if (!json.has("userId")||!json.has("token")||!json.has("assignmentId")||!json.has("toTutorId")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [appointmenttotutor.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try {
				currentTutorId=json.getInt("userId");
			} catch (Exception e) {
				result.put("status", -1);
				result.put("info", "the lack of parameter");
				logger.info("this is [appointmenttotutor.do] get userId error ...");
				e.printStackTrace();
			}
			try {
				token=json.getString("token");
			} catch (Exception e) {
				result.put("status", -1);
				result.put("info", "the lack of parameter");
				logger.info("this is [appointmenttotutor.do] get token error ...");
				e.printStackTrace();
			}
			try {
				assignmentId=json.getInt("assignmentId");
			} catch (Exception e) {
				result.put("status", -1);
				result.put("info", "the lack of parameter");
				logger.info("this is [appointmenttotutor.do] get assignmentId error ...");
				e.printStackTrace();
			}
			try {
				toTutorId=json.getInt("toTutorId");
			} catch (Exception e) {
				result.put("status", -1);
				result.put("info", "the lack of parameter");
				logger.info("this is [appointmenttotutor.do] get toTutorId error ...");
				e.printStackTrace();
			}
			
			if (result.isEmpty()){
				try {
					String localToken=WebApplicationUtils.getToken(currentTutorId);
					logger.info("this is [appointmenttotutor.do] currentTutorId ["+currentTutorId+"] ...");
					if (localToken!=null&&localToken.equals(token)){
						logger.info("this is [appointmenttotutor.do] find assignment ["+assignmentId+"] ...");
						AppStudentUploadAssignment assignment=appStudentUploadAssignmentSerivce.get(assignmentId);
						assignment.setHasAppointment(1);
						
						logger.info("this is [appointmenttotutor.do] create AppTutorAppointmentAssignmentToTutor ...");
						AppTutorAppointmentAssignmentToTutor toTutor=new AppTutorAppointmentAssignmentToTutor();
						toTutor.setAssignmentName(assignment.getAssignmentName());
						toTutor.setFilePath(assignment.getFilePath());
						toTutor.setFileName(assignment.getFileName());
						
						toTutor.setStudent(assignment.getStudent());
						logger.info("this is [appointmenttotutor.do] create AppTutorAppointmentAssignmentToTutor currentTutor ["+currentTutorId+"] ...");
						toTutor.setOriginalTutor(sysUsersManagementService.get(currentTutorId));
						logger.info("this is [appointmenttotutor.do] create AppTutorAppointmentAssignmentToTutor toTutor ["+toTutorId+"] ...");
						toTutor.setTargetTutor(sysUsersManagementService.get(toTutorId));
						logger.info("this is [appointmenttotutor.do] create AppTutorAppointmentAssignmentToTutor ...");
						
						assignment.setAssignmentToTutor(toTutor);
						toTutor.setUploadAssignment(assignment);
						logger.info("this is [appointmenttotutor.do] save AppTutorAppointmentAssignmentToTutor ...");
						appStudentUploadAssignmentSerivce.save(assignment);
						result.put("status", 1);
						result.put("info", "operation success!");
						logger.info("this is [appointmenttotutor.do] save AppTutorAppointmentAssignmentToTutor done ...");
					}else{
						result.put("status", -2);
						result.put("info", "illegal user");
						logger.info("this is [appointmenttotutor.do] illegal user ...");
					}
				} catch (Exception e) {
					result.put("status", 0);
					result.put("info", "occur exception");
					logger.info("this is [appointmenttotutor.do] exception ...");
					e.printStackTrace();
				}
			}
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [appointmenttotutor.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/showappointmentstatusinfo.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="显示指派老师状态信息")
	public String showAppointmentStatusInfo(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [showappointmentstatusinfo.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [showappointmentstatusinfo.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [showappointmentstatusinfo.do] decode done ...");
		
		Integer currentUserId=-1,assignmentId=-1;
		String token=null;
		if (!json.has("userId")||!json.has("token")||!json.has("assignmentId")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [showappointmentstatusinfo.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try {
				currentUserId=json.getInt("userId");
			} catch (Exception e) {
				result.put("status", -1);
				result.put("info", "the lack of parameter");
				logger.info("this is [showappointmentstatusinfo.do] get userId error ...");
				e.printStackTrace();
			}
			try {
				token=json.getString("token");
			} catch (Exception e) {
				result.put("status", -1);
				result.put("info", "the lack of parameter");
				logger.info("this is [showappointmentstatusinfo.do] get token error ...");
				e.printStackTrace();
			}
			try {
				assignmentId=json.getInt("assignmentId");
			} catch (Exception e) {
				result.put("status", -1);
				result.put("info", "the lack of parameter");
				logger.info("this is [showappointmentstatusinfo.do] get assignmentId error ...");
				e.printStackTrace();
			}
			
			if (result.isEmpty()){
				try {
					String localToken=WebApplicationUtils.getToken(currentUserId);
					logger.info("this is [showappointmentstatusinfo.do] currentTutorId ["+currentUserId+"] ...");
					if (localToken!=null&&localToken.equals(token)){
						logger.info("this is [showappointmentstatusinfo.do] confirm identity...");
						logger.info("this is [showappointmentstatusinfo.do] find assignment ["+assignmentId+"] ...");
						AppStudentUploadAssignment assignment=appStudentUploadAssignmentSerivce.get(assignmentId);
						Map<String,Object> map=new HashMap<String,Object>();
						map.put("assignmentId", assignment.getId());
						map.put("assignmentName", assignment.getAssignmentName());
						map.put("filePath", assignment.getFilePath());
						map.put("fileName",assignment.getFilePath());
						map.put("assignmentToTutor", assignment.getAssignmentToTutor()==null?"":
							assignment.getAssignmentToTutor().getTargetTutor()==null?"":assignment.getAssignmentToTutor().getTargetTutor().getChineseName());
						map.put("studentName", assignment.getStudent()==null?"":assignment.getStudent().getChineseName());
						map.put("uploadTime", assignment.getAssignmentToTutor()==null?"":
							assignment.getAssignmentToTutor().getUploadTime()==null?"":assignment.getAssignmentToTutor().getUploadTime().toString());
						map.put("downloadTime", assignment.getAssignmentToTutor()==null?"":
							assignment.getAssignmentToTutor().getDownloadTime()==null?"":assignment.getAssignmentToTutor().getDownloadTime().toString());
						result.put("status", 1);
						result.put("data", map);
						result.put("info", "operation success!");
						logger.info("this is [showappointmentstatusinfo.do] save AppTutorAppointmentAssignmentToTutor done ...");
					}else{
						result.put("status", -2);
						result.put("info", "illegal user");
						logger.info("this is [showappointmentstatusinfo.do] illegal user ...");
					}
				} catch (Exception e) {
					result.put("status", 0);
					result.put("info", "occur exception");
					logger.info("this is [showappointmentstatusinfo.do] exception ...");
					e.printStackTrace();
				}
			}
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [showappointmentstatusinfo.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/revokeappointmenttotutor.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="收回指派教师代审作业")
	public String revokeAppointmentToTutor(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [revokeappointmenttotutor.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [revokeappointmenttotutor.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [revokeappointmenttotutor.do] decode done ...");
		
		Integer currentUserId=-1,assignmentId=-1;
		String token=null;
		if (!json.has("userId")||!json.has("token")||!json.has("assignmentId")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [revokeappointmenttotutor.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try {
				currentUserId=json.getInt("userId");
			} catch (Exception e) {
				result.put("status", -1);
				result.put("info", "the lack of parameter");
				logger.info("this is [revokeappointmenttotutor.do] get userId error ...");
				e.printStackTrace();
			}
			try {
				token=json.getString("token");
			} catch (Exception e) {
				result.put("status", -1);
				result.put("info", "the lack of parameter");
				logger.info("this is [revokeappointmenttotutor.do] get token error ...");
				e.printStackTrace();
			}
			try {
				assignmentId=json.getInt("assignmentId");
			} catch (Exception e) {
				result.put("status", -1);
				result.put("info", "the lack of parameter");
				logger.info("this is [revokeappointmenttotutor.do] get assignmentId error ...");
				e.printStackTrace();
			}
			
			if (result.isEmpty()){
				try {
					String localToken=WebApplicationUtils.getToken(currentUserId);
					logger.info("this is [revokeappointmenttotutor.do] currentTutorId ["+currentUserId+"] ...");
					if (localToken!=null&&localToken.equals(token)){
						logger.info("this is [revokeappointmenttotutor.do] confirm identity...");
						logger.info("this is [revokeappointmenttotutor.do] find assignment ["+assignmentId+"] ...");
						
						if (appTutorAppointmentToTutorService.revokeTutorToTutorFromStudent(assignmentId)){
							result.put("status", 1);
							result.put("info", "operation success!");
							logger.info("this is [revokeappointmenttotutor.do] revokeappointmenttotutor operation done ...");
						}else{
							result.put("status", 0);
							result.put("info", "operation failed!");
						}
					}else{
						result.put("status", -2);
						result.put("info", "illegal user");
						logger.info("this is [revokeappointmenttotutor.do] illegal user ...");
					}
				} catch (Exception e) {
					result.put("status", 0);
					result.put("info", "occur exception");
					logger.info("this is [revokeappointmenttotutor.do] exception ...");
					e.printStackTrace();
				}
			}
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [revokeappointmenttotutor.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/initdownloadappointmentassignmentlist.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="查询教师下载代审作业列表")
	public String initDownloadAppointmentAssignmentlist(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [initdownloadappointmentassignmentlist.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [initdownloadappointmentassignmentlist.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [initdownloadappointmentassignmentlist.do] decode done ...");
		
		Integer currentTutorId=-1;
		String token=null;
		if (!json.has("userId")||!json.has("token")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [initdownloadappointmentassignmentlist.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				try {
					currentTutorId=json.getInt("userId");
				} catch (Exception e) {
					result.put("status", -1);
					result.put("info", "the lack of parameter");
					logger.info("this is [initdownloadappointmentassignmentlist.do] get userId error ...");
					e.printStackTrace();
				}
				try {
					token=json.getString("token");
				} catch (Exception e) {
					result.put("status", -1);
					result.put("info", "the lack of parameter");
					logger.info("this is [initdownloadappointmentassignmentlist.do] get token error ...");
					e.printStackTrace();
				}
				
				if (result.isEmpty()){
					String localToken=WebApplicationUtils.getToken(currentTutorId);
					logger.info("this is [initdownloadappointmentassignmentlist.do] currentTutorId ["+currentTutorId+"] ...");
					if (localToken!=null&&localToken.equals(token)){
						//每页大小
						int displayLength=Integer.parseInt(json.has("displayLength")?json.getString("displayLength"):"10");
						//起始值
						int displayStart=Integer.parseInt(json.has("displayStart")?json.getString("displayStart"):"0");
						//查询条件
						String param=json.has("param")?json.getString("param"):"";
						
						logger.info("this is [initdownloadappointmentassignmentlist.do] requset parameters [displayLength = {"+displayLength+"}],[displayStart = {"+displayStart+"}],[param = {"+param+"}]");
						Map<String, Object> parameters=new HashMap<String, Object>();
						if (param!=null&&!param.equals("")){
							parameters.put("assignmentName", param);
//							parameters.put("originalTutor.chineseName", param);
//							parameters.put("student.chineseName", param);
						}
						parameters.put("sort", 5);
						parameters.put("order", "desc");
						
						SysUsers currentTutor=sysUsersManagementService.get(currentTutorId);
						if (currentTutor!=null&&currentTutor.getSysGroups()!=null){
							result = appTutorAppointmentToTutorService.searchDataForAPP(displayLength, displayStart, parameters, currentTutorId);
							
							logger.info("this is [initdownloadappointmentassignmentlist.do] finding assignment ...");
							result.put("status", 1);
							result.put("info", "operation success!");
							result.put("displayLength", displayLength);
							result.put("displayStart", displayStart+displayLength);
							result.put("param", param);
							logger.info("this is [initdownloadappointmentassignmentlist.do] finding assignment done...");
						}else{
							result.put("status", 0);
							result.put("info", "find user info error");
							logger.info("this is [initdownloadappointmentassignmentlist.do] find user info error ...");
						}
					}else{
						result.put("status", -2);
						result.put("info", "illegal user");
						logger.info("this is [initdownloadappointmentassignmentlist.do] illegal user ...");
					}
				}
			}catch(Exception ex){
				ex.printStackTrace();
				result.put("status", 0);
				result.put("info", "occur exception");
				logger.info("this is [initdownloadappointmentassignmentlist.do] exception ...");
			}
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [initdownloadappointmentassignmentlist.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/inittutoruploadassignmentlist.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="查询教师上传已审作业列表")
	public String initTutorUploadAssignmentList(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [inittutoruploadassignmentlist.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [inittutoruploadassignmentlist.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [inittutoruploadassignmentlist.do] decode done ...");
		
		Integer userId=-1;
		String token=null;
		if (!json.has("userId")||!json.has("token")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [inittutoruploadassignmentlist.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				userId=json.getInt("userId");
			}catch(Exception ex){
				result.put("status", 0);
				result.put("info", "wrong parameter is userId");
				logger.info("this is [inittutoruploadassignmentlist.do] wrong parameter is userId ...");
			}
			
			try{
				token=json.getString("token");
			}catch(Exception ex){
				result.put("status", 0);
				result.put("info", "wrong parameter is token");
				logger.info("this is [inittutoruploadassignmentlist.do] wrong parameter is token ...");
			}
			
			if (result.isEmpty()){
				try {
					String localToken=WebApplicationUtils.getToken(userId);
					
					if (localToken!=null&&localToken.equals(token)){
						//每页大小
						int displayLength=Integer.parseInt(json.has("displayLength")?json.getString("displayLength"):"10");
						//起始值
						int displayStart=Integer.parseInt(json.has("displayStart")?json.getString("displayStart"):"0");
						//查询条件
						String param=json.has("param")?json.getString("param"):"";
						
						logger.info("this is [inittutoruploadassignmentlist.do] requset parameters [displayLength = {"+displayLength+"}],[displayStart = {"+displayStart+"}],[param = {"+param+"}]");
						
						Map<String, Object> parameters=new HashMap<String, Object>();
						if (param!=null&&!param.equals("")){
							parameters.put("assignmentName", param);
//							parameters.put("student.chineseName", param);
						}
						parameters.put("sort", 4);
						parameters.put("order", "desc");
						
						result=appTutorAppointmentToStudentService.searchDataForAPP(displayLength, displayStart, parameters, userId);
						result.put("status", 1);
						result.put("info", "operation success");
						result.put("displayLength", displayLength);
						result.put("displayStart", displayStart+displayLength);
						result.put("param", param);
						logger.info("this is [initstudentuploadassignmentlist.do] find assignment list done ...");
					}else{
						result.put("status", -2);
						result.put("info", "illegal user");
						logger.info("this is [initstudentuploadassignmentlist.do] illegal user ...");
					}
				} catch (Exception e) {
					e.printStackTrace();
					result.put("status", 0);
					result.put("info", "operation failed");
					logger.info("this is [initstudentuploadassignmentlist.do] occur error ...");
				}
			}
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [initstudentuploadassignmentlist.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/savetutoruploadassignmentinfo.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="保存教师上传作业信息")
	public String saveTutorUploadAssignmentInfo(HttpServletRequest request, @RequestParam("uploadFile") CommonsMultipartFile[] uploadFile) {
		logger.info("this is [savetutoruploadassignmentinfo.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		
		logger.info("this is [savetutoruploadassignmentinfo.do] get page parameters ...");
		Integer currentTutorId=-1,toStudentId=-1;
		String uploadAssignmentName=null, token=null;
		AppTutorAppointmentAssignmentToStudent uploadAssignment=null;
		
		try {
			currentTutorId=ServletRequestUtils.getRequiredIntParameter(request, "userId");
		}catch (ServletRequestBindingException e) {
			try {
				currentTutorId=Integer.parseInt(ServletRequestUtils.getRequiredStringParameter(request, "userId").trim());
			}catch (Exception e1) {
				e1.printStackTrace();
				result.put("status", -1);
				result.put("info", "the lack of parameter");
				logger.info("this is [savestudentuploadassignmentinfo.do] the lack of parameter [userId] ...");
			}
		}
		
		try {
			toStudentId=ServletRequestUtils.getRequiredIntParameter(request, "toStudentId");
		}catch (ServletRequestBindingException e) {
			try {
				toStudentId=Integer.parseInt(ServletRequestUtils.getRequiredStringParameter(request, "toStudentId").trim());
			}catch (Exception e1) {
				e1.printStackTrace();
				result.put("status", -1);
				result.put("info", "the lack of parameter");
				logger.info("this is [savestudentuploadassignmentinfo.do] the lack of parameter [toStudentId] ...");
			}
		}
		
		try {
			uploadAssignmentName=ServletRequestUtils.getRequiredStringParameter(request, "assignmentName");
		} catch (ServletRequestBindingException e) {
			e.printStackTrace();
			logger.info("this is [savestudentuploadassignmentinfo.do] the lack of parameter is assignmentName ...");
		}
		
		try {
			token=ServletRequestUtils.getRequiredStringParameter(request, "token").trim();
		} catch (ServletRequestBindingException e1) {
			e1.printStackTrace();
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [savetutoruploadassignmentinfo.do] the lack of parameter ...");
		}
		
		String localToken=WebApplicationUtils.getToken(currentTutorId);
		if (localToken!=null&&localToken.equals(token)){
			
			Calendar now=new GregorianCalendar();
			String year="/"+now.get(Calendar.YEAR)+"/";
			String month=(now.get(Calendar.MONTH)+1)>9?"/"+(now.get(Calendar.MONTH)+1)+"/":"/0"+(now.get(Calendar.MONTH)+1)+"/";
			String day=now.get(Calendar.DAY_OF_MONTH)>9?"/"+now.get(Calendar.DAY_OF_MONTH)+"/":"/0"+now.get(Calendar.DAY_OF_MONTH)+"/";
			
			String _localPath=year;
			File directory=new File(_filePath+_localPath);
			if (!directory.exists()){
				directory.mkdir();
				logger.info("this is [savetutoruploadassignmentinfo.do] create directory ["+directory+"] success...");
			}
			
			_localPath+=month;
			directory=new File(_filePath+_localPath);
			if (!directory.exists()){
				directory.mkdir();
				logger.info("this is [savetutoruploadassignmentinfo.do] create directory ["+directory+"] success...");
			}
			
			_localPath+=day;
			directory=new File(_filePath+_localPath);
			if (!directory.exists()){
				directory.mkdir();
				logger.info("this is [savetutoruploadassignmentinfo.do] create directory ["+directory+"] success...");
			}
			
			_localPath+=File.separator+"tutor_"+currentTutorId;
			directory=new File(_filePath+_localPath);
			logger.info("this is [savetutoruploadassignmentinfo.do] show directory name ["+directory+"]");
			if (!directory.exists()){
				directory.mkdir();
				logger.info("this is [savetutoruploadassignmentinfo.do] create directory ["+directory+"] success...");
			}
			
			File file=null;
			String strFileName=null;
			String strSuffix=null;
			int dotPosition=-1;
			
			for (int i=0;i<uploadFile.length;i++){
				if (!uploadFile[i].isEmpty()){
					strFileName=uploadFile[i].getOriginalFilename();
					logger.info("this is [savetutoruploadassignmentinfo.do] show file name ["+strFileName+"]");
					
					dotPosition=strFileName.lastIndexOf(".");
					strSuffix=strFileName.substring(dotPosition);
					
					if (strSuffix!=null&&!strSuffix.equals("")&&(strSuffix.equals(".doc")||strSuffix.equals(".docx")||strSuffix.equals(".pdf"))){
						file=new File(directory+File.separator+Calendar.getInstance().getTimeInMillis());
						
						while(file.exists()){
							logger.info("this is [savetutoruploadassignmentinfo.do] file ["+file.getName()+"] exist...");
							file=new File(directory+File.separator+Calendar.getInstance().getTimeInMillis());
							logger.info("this is [savetutoruploadassignmentinfo.do] change file name ["+file.getName()+"]...");
						}
					}
					
					if (!file.exists()){
						logger.info("this is [savetutoruploadassignmentinfo.do] file ["+file.getName()+"] do not exist...");
						try {
							file.createNewFile();
							logger.info("this is [savetutoruploadassignmentinfo.do] create file ["+file.getName()+"] success...");
							uploadFile[i].transferTo(file);
							logger.info("this is [savetutoruploadassignmentinfo.do] file ["+file.getName()+"] transfer...");
						} catch (IOException e) {
							logger.info("this is [savetutoruploadassignmentinfo.do] create file ["+file.getName()+"] failed...");
							result.put("status", 0);
							result.put("info", "save failed, try again!");
							e.printStackTrace();
						}
					}
					
					if (result.isEmpty()){
						uploadAssignment=new AppTutorAppointmentAssignmentToStudent();
						
						String assignmentName=(uploadAssignmentName==null||uploadAssignmentName.equals(""))?strFileName:uploadAssignmentName;
						uploadAssignment.setAssignmentName(assignmentName);
						logger.info("this is [savetutoruploadassignmentinfo.do] set assignment name ["+uploadAssignment.getAssignmentName()+"]...");
						uploadAssignment.setFilePath(_localPath+File.separator+file.getName());
						uploadAssignment.setFileName(strFileName);
						logger.info("this is [savetutoruploadassignmentinfo.do] set file name ["+strFileName+"]...");
						
						logger.info("this is [savetutoruploadassignmentinfo.do] set toStudentId ["+toStudentId+"]...");
						uploadAssignment.setTargetStudent(sysUsersManagementService.get(toStudentId));
						logger.info("this is [savetutoruploadassignmentinfo.do] set currentTutorId ["+currentTutorId+"]...");
						uploadAssignment.setSourceTutor(sysUsersManagementService.get(currentTutorId));
						
						logger.info("this is [savetutoruploadassignmentinfo.do] is saving ...");
						appTutorAppointmentToStudentService.save(uploadAssignment);
						result.put("status", 1);
						result.put("info", "operation success!");
						logger.info("this is [savetutoruploadassignmentinfo.do] save uploadAssignment done ...");
					}
				}else{
					logger.info("this is [savetutoruploadassignmentinfo.do] save uploadAssignment error ...");
					result.put("status", 0);
					result.put("info", "save failed, try again!");
				}
			}
		}else{
			result.put("status", -2);
			result.put("info", "illegal user");
			logger.info("this is [savetutoruploadassignmentinfo.do] illegal user ...");
		}
		
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [savetutoruploadassignmentinfo.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/inittutoruploadappointmentassignmentlist.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="查询教师上传代审作业列表")
	public String initTutorUploadAppointmentAssignmentList(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [inittutoruploadappointmentassignmentlist.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [inittutoruploadappointmentassignmentlist.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [inittutoruploadappointmentassignmentlist.do] decode done ...");
		
		Integer userId=-1;
		String token=null;
		if (!json.has("userId")||!json.has("token")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [inittutoruploadappointmentassignmentlist.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				userId=json.getInt("userId");
			}catch(Exception ex){
				result.put("status", 0);
				result.put("info", "wrong parameter is userId");
				logger.info("this is [inittutoruploadappointmentassignmentlist.do] wrong parameter is userId ...");
			}
			
			try{
				token=json.getString("token");
			}catch(Exception ex){
				result.put("status", 0);
				result.put("info", "wrong parameter is token");
				logger.info("this is [inittutoruploadappointmentassignmentlist.do] wrong parameter is token ...");
			}
			
			if (result.isEmpty()){
				try {
					String localToken=WebApplicationUtils.getToken(userId);
					
					if (localToken!=null&&localToken.equals(token)){
						//每页大小
						int displayLength=Integer.parseInt(json.has("displayLength")?json.getString("displayLength"):"10");
						//起始值
						int displayStart=Integer.parseInt(json.has("displayStart")?json.getString("displayStart"):"0");
						//查询条件
						String param=json.has("param")?json.getString("param"):"";
						
						logger.info("this is [inittutoruploadappointmentassignmentlist.do] requset parameters [displayLength = {"+displayLength+"}],[displayStart = {"+displayStart+"}],[param = {"+param+"}]");
						
						Map<String, Object> parameters=new HashMap<String, Object>();
						if (param!=null&&!param.equals("")){
							parameters.put("assignmentName", param);
//							parameters.put("student.chineseName", param);
						}
						parameters.put("sort", 4);
						parameters.put("order", "desc");
						
						SysUsers currentTutor=sysUsersManagementService.get(userId);
						if (currentTutor!=null&&currentTutor.getSysGroups()!=null){
							result = appTutorAppointmentToTutorService.searchDataForAPPToTutor(displayLength, displayStart, parameters, userId);
						}
						
						result.put("status", 1);
						result.put("info", "operation success");
						result.put("displayLength", displayLength);
						result.put("displayStart", displayStart+displayLength);
						result.put("param", param);
						logger.info("this is [inittutoruploadappointmentassignmentlist.do] find assignment list done ...");
					}else{
						result.put("status", -2);
						result.put("info", "illegal user");
						logger.info("this is [inittutoruploadappointmentassignmentlist.do] illegal user ...");
					}
				} catch (Exception e) {
					e.printStackTrace();
					result.put("status", 0);
					result.put("info", "operation failed");
					logger.info("this is [inittutoruploadappointmentassignmentlist.do] occur error ...");
				}
			}
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [inittutoruploadappointmentassignmentlist.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/savetutoruploadappointmentassignmentinfo.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="保存教师上传代审作业信息")
	public String saveUploadAppointmentAssignmentInfo(HttpServletRequest request, @RequestParam("uploadFile") CommonsMultipartFile[] uploadFile) {
		logger.info("this is [savetutoruploadappointmentassignmentinfo.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		
		logger.info("this is [savetutoruploadappointmentassignmentinfo.do] get page parameters ...");
		Integer currentTutorId=-1,toTutorId=-1;
		String uploadAssignmentName=null, token=null;
		AppTutorAppointmentAssignmentToTutor uploadAssignment=null;
		
		try {
			currentTutorId=ServletRequestUtils.getRequiredIntParameter(request, "userId");
		}catch (ServletRequestBindingException e) {
			try {
				currentTutorId=Integer.parseInt(ServletRequestUtils.getRequiredStringParameter(request, "userId").trim());
			}catch (Exception e1) {
				e1.printStackTrace();
				result.put("status", -1);
				result.put("info", "the lack of parameter");
				logger.info("this is [savestudentuploadassignmentinfo.do] the lack of parameter [userId] ...");
			}
		}
		
		try {
			toTutorId=ServletRequestUtils.getRequiredIntParameter(request, "toTutorId");
		}catch (ServletRequestBindingException e) {
			try {
				toTutorId=Integer.parseInt(ServletRequestUtils.getRequiredStringParameter(request, "toTutorId").trim());
			}catch (Exception e1) {
				e1.printStackTrace();
				result.put("status", -1);
				result.put("info", "the lack of parameter");
				logger.info("this is [savestudentuploadassignmentinfo.do] the lack of parameter [toTutorId] ...");
			}
		}
		
		try {
			uploadAssignmentName=ServletRequestUtils.getRequiredStringParameter(request, "assignmentName");
		} catch (ServletRequestBindingException e) {
			e.printStackTrace();
			logger.info("this is [savestudentuploadassignmentinfo.do] the lack of parameter is assignmentName ...");
		}
		
		try {
			token=ServletRequestUtils.getRequiredStringParameter(request, "token").trim();
		} catch (ServletRequestBindingException e) {
			e.printStackTrace();
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [savetutoruploadappointmentassignmentinfo.do] the lack of parameter ...");
		}
		
		String localToken=WebApplicationUtils.getToken(currentTutorId);
		if (localToken!=null&&localToken.equals(token)){
			
			Calendar now=new GregorianCalendar();
			String year="/"+now.get(Calendar.YEAR)+"/";
			String month=(now.get(Calendar.MONTH)+1)>9?"/"+(now.get(Calendar.MONTH)+1)+"/":"/0"+(now.get(Calendar.MONTH)+1)+"/";
			String day=now.get(Calendar.DAY_OF_MONTH)>9?"/"+now.get(Calendar.DAY_OF_MONTH)+"/":"/0"+now.get(Calendar.DAY_OF_MONTH)+"/";
			
			String _localPath=year;
			File directory=new File(_filePath+_localPath);
			if (!directory.exists()){
				directory.mkdir();
				logger.info("this is [savetutoruploadappointmentassignmentinfo.do] create directory ["+directory+"] success...");
			}
			
			_localPath+=month;
			directory=new File(_filePath+_localPath);
			if (!directory.exists()){
				directory.mkdir();
				logger.info("this is [savetutoruploadappointmentassignmentinfo.do] create directory ["+directory+"] success...");
			}
			
			_localPath+=day;
			directory=new File(_filePath+_localPath);
			if (!directory.exists()){
				directory.mkdir();
				logger.info("this is [savetutoruploadappointmentassignmentinfo.do] create directory ["+directory+"] success...");
			}
			
			_localPath+=File.separator+"tutor_"+currentTutorId;
			directory=new File(_filePath+_localPath);
			logger.info("this is [savetutoruploadappointmentassignmentinfo.do] show directory name ["+directory+"]");
			if (!directory.exists()){
				directory.mkdir();
				logger.info("this is [savetutoruploadappointmentassignmentinfo.do] create directory ["+directory+"] success...");
			}
			
			File file=null;
			String strFileName=null;
			String strSuffix=null;
			int dotPosition=-1;
			
			for (int i=0;i<uploadFile.length;i++){
				if (!uploadFile[i].isEmpty()){
					strFileName=uploadFile[i].getOriginalFilename();
					logger.info("this is [savetutoruploadappointmentassignmentinfo.do] show file name ["+strFileName+"]");
					
					dotPosition=strFileName.lastIndexOf(".");
					strSuffix=strFileName.substring(dotPosition);
					
					if (strSuffix!=null&&!strSuffix.equals("")&&(strSuffix.equals(".doc")||strSuffix.equals(".docx")||strSuffix.equals(".pdf"))){
						file=new File(directory+File.separator+Calendar.getInstance().getTimeInMillis());
						
						while(file.exists()){
							logger.info("this is [savetutoruploadappointmentassignmentinfo.do] file ["+file.getName()+"] exist...");
							file=new File(directory+File.separator+Calendar.getInstance().getTimeInMillis());
							logger.info("this is [savetutoruploadappointmentassignmentinfo.do] change file name ["+file.getName()+"]...");
						}
					}
					
					if (!file.exists()){
						logger.info("this is [savetutoruploadappointmentassignmentinfo.do] file ["+file.getName()+"] do not exist...");
						try {
							file.createNewFile();
							logger.info("this is [savetutoruploadappointmentassignmentinfo.do] create file ["+file.getName()+"] success...");
							uploadFile[i].transferTo(file);
							logger.info("this is [savetutoruploadappointmentassignmentinfo.do] file ["+file.getName()+"] transfer...");
						} catch (IOException e) {
							logger.info("this is [savetutoruploadappointmentassignmentinfo.do] create file ["+file.getName()+"] failed...");
							result.put("status", 0);
							result.put("info", "save failed, try again!");
							e.printStackTrace();
						}
					}
					
					if (result.isEmpty()){
						uploadAssignment=new AppTutorAppointmentAssignmentToTutor();
						
						String assignmentName=(uploadAssignmentName==null||uploadAssignmentName.equals(""))?strFileName:uploadAssignmentName;
						uploadAssignment.setAssignmentName(assignmentName);
						logger.info("this is [savetutoruploadappointmentassignmentinfo.do] set assignment name ["+uploadAssignment.getAssignmentName()+"]...");
						uploadAssignment.setFilePath(_localPath+File.separator+file.getName());
						uploadAssignment.setFileName(strFileName);
						logger.info("this is [savetutoruploadappointmentassignmentinfo.do] set file name ["+strFileName+"]...");
						
						logger.info("this is [savetutoruploadappointmentassignmentinfo.do] set toTutorId ["+toTutorId+"]...");
						uploadAssignment.setTargetTutor(sysUsersManagementService.get(toTutorId));
						logger.info("this is [savetutoruploadappointmentassignmentinfo.do] set currentTutorId ["+currentTutorId+"]...");
						uploadAssignment.setOriginalTutor(sysUsersManagementService.get(currentTutorId));
						
						logger.info("this is [savetutoruploadappointmentassignmentinfo.do] is saving ...");
						appTutorAppointmentToTutorService.save(uploadAssignment);
						result.put("status", 1);
						result.put("info", "operation success!");
						logger.info("this is [savetutoruploadappointmentassignmentinfo.do] save uploadAssignment done ...");
					}
				}else{
					logger.info("this is [savetutoruploadappointmentassignmentinfo.do] save uploadAssignment error ...");
					result.put("status", 0);
					result.put("info", "save failed, try again!");
				}
			}
		}else{
			result.put("status", -2);
			result.put("info", "illegal user");
			logger.info("this is [savetutoruploadappointmentassignmentinfo.do] illegal user ...");
		}
		
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [savetutoruploadappointmentassignmentinfo.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/deletemultipleappointmenttostudent.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="批量收回上传已审作业")
	public String deleteMultipleAssignmentToStudent(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [deletemultipleappointmenttostudent.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [deletemultipleappointmenttostudent.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [deletemultipleappointmenttostudent.do] decode done ...");
		
		Integer userId=-1;
		String token=null, deleteIds=null;
		if (!json.has("userId")||!json.has("token")||!json.has("deleteIds")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [deletemultipleappointmenttostudent.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				userId=json.getInt("userId");
			}catch(Exception ex){
				result.put("status", 0);
				result.put("info", "wrong parameter is userId");
				logger.info("this is [deletemultipleappointmenttostudent.do] wrong parameter is userId ...");
			}
			
			try{
				token=json.getString("token");
			}catch(Exception ex){
				result.put("status", 0);
				result.put("info", "wrong parameter is token");
				logger.info("this is [deletemultipleappointmenttostudent.do] wrong parameter is token ...");
			}
			
			try{
				deleteIds=json.getString("deleteIds");
			}catch(Exception ex){
				result.put("status", 0);
				result.put("info", "wrong parameter is deleteIds");
				logger.info("this is [deletemultipleappointmenttostudent.do] wrong parameter is deleteIds ...");
			}
			
			if (result.isEmpty()){
				try {
					String localToken=WebApplicationUtils.getToken(userId);
					
					if (localToken!=null&&localToken.equals(token)){
						logger.info("this is [deletemultipleappointmenttostudent.do] confirm identity...");
						if (appTutorAppointmentToStudentService.revokeTutorToStudent(deleteIds)){
							result.put("status", 1);
							result.put("info", "operation success");
							logger.info("this is [deletemultipleappointmenttostudent.do] revoke assignment done ...");
						}else{
							result.put("status", 0);
							result.put("info", "operation failed");
							logger.info("this is [deletemultipleappointmenttostudent.do] revoke assignment done ...");
						}
					}else{
						result.put("status", -2);
						result.put("info", "illegal user");
						logger.info("this is [deletemultipleappointmenttostudent.do] illegal user ...");
					}
				} catch (Exception e) {
					e.printStackTrace();
					result.put("status", 0);
					result.put("info", "operation failed");
					logger.info("this is [deletemultipleappointmenttostudent.do] occur error ...");
				}
			}
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [deletemultipleappointmenttostudent.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/deletemultipleassignmenttotutor.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="批量收回上传代审作业")
	public String deleteMultipleAssignmentToTutor(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [deletemultipleassignmenttotutor.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [deletemultipleassignmenttotutor.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [deletemultipleassignmenttotutor.do] decode done ...");
		
		Integer userId=-1;
		String token=null, deleteIds=null;
		if (!json.has("userId")||!json.has("token")||!json.has("deleteIds")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [deletemultipleassignmenttotutor.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				userId=json.getInt("userId");
			}catch(Exception ex){
				result.put("status", 0);
				result.put("info", "wrong parameter is userId");
				logger.info("this is [deletemultipleassignmenttotutor.do] wrong parameter is userId ...");
			}
			
			try{
				token=json.getString("token");
			}catch(Exception ex){
				result.put("status", 0);
				result.put("info", "wrong parameter is token");
				logger.info("this is [deletemultipleassignmenttotutor.do] wrong parameter is token ...");
			}
			
			try{
				deleteIds=json.getString("deleteIds");
			}catch(Exception ex){
				result.put("status", 0);
				result.put("info", "wrong parameter is deleteIds");
				logger.info("this is [deletemultipleassignmenttotutor.do] wrong parameter is deleteIds ...");
			}
			
			if (result.isEmpty()){
				try {
					String localToken=WebApplicationUtils.getToken(userId);
					
					if (localToken!=null&&localToken.equals(token)){
						logger.info("this is [deletemultipleassignmenttotutor.do] confirm identity...");
						if (appTutorAppointmentToTutorService.revokeTutorToTutor(deleteIds)){
							result.put("status", 1);
							result.put("info", "operation success");
							logger.info("this is [deletemultipleassignmenttotutor.do] revoke assignment done ...");
						}else{
							result.put("status", 0);
							result.put("info", "operation failed");
							logger.info("this is [deletemultipleassignmenttotutor.do] revoke assignment done ...");
						}
					}else{
						result.put("status", -2);
						result.put("info", "illegal user");
						logger.info("this is [deletemultipleassignmenttotutor.do] illegal user ...");
					}
				} catch (Exception e) {
					e.printStackTrace();
					result.put("status", 0);
					result.put("info", "operation failed");
					logger.info("this is [deletemultipleassignmenttotutor.do] occur error ...");
				}
			}
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [deletemultipleassignmenttotutor.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/deletemultipleassignment.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="批量收回学生上传的作业")
	public String deleteＭultipleAssignment(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [deletemultipleassignment.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [deletemultipleassignment.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [deletemultipleassignment.do] decode done ...");
		
		Integer userId=-1;
		String token=null, deleteIds=null;
		if (!json.has("userId")||!json.has("token")||!json.has("deleteIds")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [deletemultipleassignment.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				userId=json.getInt("userId");
			}catch(Exception ex){
				result.put("status", 0);
				result.put("info", "wrong parameter is userId");
				logger.info("this is [deletemultipleassignment.do] wrong parameter is userId ...");
			}
			
			try{
				token=json.getString("token");
			}catch(Exception ex){
				result.put("status", 0);
				result.put("info", "wrong parameter is token");
				logger.info("this is [deletemultipleassignment.do] wrong parameter is token ...");
			}
			
			try{
				deleteIds=json.getString("deleteIds");
			}catch(Exception ex){
				result.put("status", 0);
				result.put("info", "wrong parameter is deleteIds");
				logger.info("this is [deletemultipleassignment.do] wrong parameter is deleteIds ...");
			}
			
			if (result.isEmpty()){
				try {
					String localToken=WebApplicationUtils.getToken(userId);
					
					if (localToken!=null&&localToken.equals(token)){
						logger.info("this is [deletemultipleassignment.do] confirm identity...");
						if (appStudentUploadAssignmentSerivce.deleteMultiple(deleteIds)){
							result.put("status", 1);
							result.put("info", "operation success");
							logger.info("this is [deletemultipleassignment.do] revoke assignment done ...");
						}else{
							result.put("status", 0);
							result.put("info", "operation failed");
							logger.info("this is [deletemultipleassignment.do] revoke assignment done ...");
						}
					}else{
						result.put("status", -2);
						result.put("info", "illegal user");
						logger.info("this is [deletemultipleassignment.do] illegal user ...");
					}
				} catch (Exception e) {
					e.printStackTrace();
					result.put("status", 0);
					result.put("info", "operation failed");
					logger.info("this is [deletemultipleassignment.do] occur error ...");
				}
			}
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [deletemultipleassignment.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/studentdownloadassignment.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="学生下载作业")
	public String studentDownloadAssignment(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [studentdownloadassignment.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [studentdownloadassignment.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [studentdownloadassignment.do] decode done ...");
		
		Integer userId=-1,assignmentId=-1;
		String token=null;
		if (!json.has("userId")||!json.has("token")||!json.has("assignmentId")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [studentdownloadassignment.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				userId=json.getInt("userId");
			}catch(Exception ex){
				result.put("status", 0);
				result.put("info", "wrong parameter is userId");
				logger.info("this is [studentdownloadassignment.do] wrong parameter is userId ...");
			}
			
			try{
				token=json.getString("token");
			}catch(Exception ex){
				result.put("status", 0);
				result.put("info", "wrong parameter is token");
				logger.info("this is [studentdownloadassignment.do] wrong parameter is token ...");
			}
			
			try{
				assignmentId=json.getInt("assignmentId");
			}catch(Exception ex){
				result.put("status", 0);
				result.put("info", "wrong parameter is assignmentId");
				logger.info("this is [studentdownloadassignment.do] wrong parameter is assignmentId ...");
			}
			
			if (result.isEmpty()){
				try {
					String localToken=WebApplicationUtils.getToken(userId);
					
					if (localToken!=null&&localToken.equals(token)){
						logger.info("this is [studentdownloadassignment.do] confirm identity...");
						
						AppTutorAppointmentAssignmentToStudent assignment=appTutorAppointmentToStudentService.get(assignmentId);
						
						if (assignment.getDownloadTime()==null||assignment.getDownloadTime().equals("")){
							assignment.setDownloadTime(Calendar.getInstance().getTime());
							appTutorAppointmentToStudentService.save(assignment);
						}
						
						result.put("status", 1);
						result.put("info", "operation success");
						logger.info("this is [studentdownloadassignment.do] download assignment done ...");
						
//						if (assignment!=null){
//							response.setCharacterEncoding("utf-8");
//					        response.setContentType("multipart/form-data");
//					        
//					        String encodedfileName = null;
//					        String agent = request.getHeader("USER-AGENT");
//					        if(null != agent && -1 != agent.indexOf("MSIE")){//IE
//					            encodedfileName = java.net.URLEncoder.encode(assignment.getFileName(),"UTF-8");
//					        }else if(null != agent && -1 != agent.indexOf("Mozilla")){
//					            encodedfileName = new String (assignment.getFileName().getBytes("UTF-8"),"iso-8859-1");
//					        }else{
//					            encodedfileName = java.net.URLEncoder.encode(assignment.getFileName(),"UTF-8");
//					        }
//					        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");
//					        
//					        InputStream inputStream=null;
//							OutputStream os=null;
//							try {
//								inputStream = new FileInputStream(new File(_filePath+assignment.getFilePath()));
//								os = response.getOutputStream();
//								byte[] b = new byte[2048];
//								int length;
//								while ((length = inputStream.read(b)) > 0) {
//								    os.write(b, 0, length);
//								}
//								os.flush();
//							} catch (Exception e) {
//								logger.info("this is [showstudentassignment.do] send file exception ...");
//								e.printStackTrace();
//							}finally{
//								if (os!=null){
//									os.close();
//								}
//								if (inputStream!=null){
//									inputStream.close();
//								}
//							}
//						}
					}else{
						result.put("status", -2);
						result.put("info", "illegal user");
						logger.info("this is [studentdownloadassignment.do] illegal user ...");
					}
				} catch (Exception e) {
					e.printStackTrace();
					result.put("status", 0);
					result.put("info", "operation failed");
					logger.info("this is [studentdownloadassignment.do] occur error ...");
				}
			}
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [studentdownloadassignment.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/tutordownloadstudentassignment.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="教师下载学生作业")
	public String tutorDownloadStudentAssignment(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [tutordownloadstudentassignment.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [tutordownloadstudentassignment.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [tutordownloadstudentassignment.do] decode done ...");
		
		Integer userId=-1,assignmentId=-1;
		String token=null;
		if (!json.has("userId")||!json.has("token")||!json.has("assignmentId")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [tutordownloadstudentassignment.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				userId=json.getInt("userId");
			}catch(Exception ex){
				result.put("status", 0);
				result.put("info", "wrong parameter is userId");
				logger.info("this is [tutordownloadstudentassignment.do] wrong parameter is userId ...");
			}
			
			try{
				token=json.getString("token");
			}catch(Exception ex){
				result.put("status", 0);
				result.put("info", "wrong parameter is token");
				logger.info("this is [tutordownloadstudentassignment.do] wrong parameter is token ...");
			}
			
			try{
				assignmentId=json.getInt("assignmentId");
			}catch(Exception ex){
				result.put("status", 0);
				result.put("info", "wrong parameter is assignmentId");
				logger.info("this is [tutordownloadstudentassignment.do] wrong parameter is assignmentId ...");
			}
			
			if (result.isEmpty()){
				try {
					String localToken=WebApplicationUtils.getToken(userId);
					
					if (localToken!=null&&localToken.equals(token)){
						logger.info("this is [tutordownloadstudentassignment.do] confirm identity...");
						
						AppStudentUploadAssignment assignment=appStudentUploadAssignmentSerivce.get(assignmentId);
						if (assignment.getDownloadTime()==null||assignment.getDownloadTime().equals("")){
							assignment.setDownloadTime(Calendar.getInstance().getTime());
							appStudentUploadAssignmentSerivce.save(assignment);
						}
//						if (assignment.getAssignmentToTutor()!=null){
//							AppTutorAppointmentAssignmentToTutor toTutor=assignment.getAssignmentToTutor();
//							toTutor.setDownloadTime(Calendar.getInstance().getTime());
//							assignment.setAssignmentToTutor(toTutor);
//						}
						
						
						result.put("status", 1);
						result.put("info", "operation success");
						logger.info("this is [tutordownloadstudentassignment.do] download assignment done ...");
						
//						if (assignment!=null){
//							response.setCharacterEncoding("utf-8");
//					        response.setContentType("multipart/form-data");
//					        
//					        String encodedfileName = null;
//					        String agent = request.getHeader("USER-AGENT");
//					        if(null != agent && -1 != agent.indexOf("MSIE")){//IE
//					            encodedfileName = java.net.URLEncoder.encode(assignment.getFileName(),"UTF-8");
//					        }else if(null != agent && -1 != agent.indexOf("Mozilla")){
//					            encodedfileName = new String (assignment.getFileName().getBytes("UTF-8"),"iso-8859-1");
//					        }else{
//					            encodedfileName = java.net.URLEncoder.encode(assignment.getFileName(),"UTF-8");
//					        }
//					        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");
//					        
//					        InputStream inputStream=null;
//							OutputStream os=null;
//							try {
//								inputStream = new FileInputStream(new File(_filePath+assignment.getFilePath()));
//								os = response.getOutputStream();
//								byte[] b = new byte[2048];
//								int length;
//								while ((length = inputStream.read(b)) > 0) {
//								    os.write(b, 0, length);
//								}
//								os.flush();
//							} catch (Exception e) {
//								logger.info("this is [showstudentassignment.do] send file exception ...");
//								e.printStackTrace();
//							}finally{
//								if (os!=null){
//									os.close();
//								}
//								if (inputStream!=null){
//									inputStream.close();
//								}
//							}
//						}
					}else{
						result.put("status", -2);
						result.put("info", "illegal user");
						logger.info("this is [tutordownloadstudentassignment.do] illegal user ...");
					}
				} catch (Exception e) {
					e.printStackTrace();
					result.put("status", 0);
					result.put("info", "operation failed");
					logger.info("this is [tutordownloadstudentassignment.do] occur error ...");
				}
			}
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [tutordownloadstudentassignment.do] return ["+tmp+"] ...");
		return tmp;
	}
	
	@RequestMapping(value = "/tutordownloadtutorassignment.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="教师下载代审作业")
	public String tutorDownloadTutorAssignment(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [tutordownloadtutorassignment.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		logger.info("this is [tutordownloadtutorassignment.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [tutordownloadtutorassignment.do] decode done ...");
		
		Integer userId=-1,assignmentId=-1;
		String token=null;
		if (!json.has("userId")||!json.has("token")||!json.has("assignmentId")){
			result.put("status", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [tutordownloadtutorassignment.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				userId=json.getInt("userId");
			}catch(Exception ex){
				result.put("status", 0);
				result.put("info", "wrong parameter is userId");
				logger.info("this is [tutordownloadtutorassignment.do] wrong parameter is userId ...");
			}
			
			try{
				token=json.getString("token");
			}catch(Exception ex){
				result.put("status", 0);
				result.put("info", "wrong parameter is token");
				logger.info("this is [tutordownloadtutorassignment.do] wrong parameter is token ...");
			}
			
			try{
				assignmentId=json.getInt("assignmentId");
			}catch(Exception ex){
				result.put("status", 0);
				result.put("info", "wrong parameter is assignmentId");
				logger.info("this is [tutordownloadtutorassignment.do] wrong parameter is assignmentId ...");
			}
			
			if (result.isEmpty()){
				try {
					String localToken=WebApplicationUtils.getToken(userId);
					
					if (localToken!=null&&localToken.equals(token)){
						logger.info("this is [tutordownloadtutorassignment.do] confirm identity...");
						
						AppTutorAppointmentAssignmentToTutor assignment=appTutorAppointmentToTutorService.get(assignmentId);
						if (assignment.getDownloadTime()==null||assignment.getDownloadTime().equals("")){
							assignment.setDownloadTime(Calendar.getInstance().getTime());
							appTutorAppointmentToTutorService.save(assignment);
						}
						result.put("status", 1);
						result.put("info", "operation success");
						logger.info("this is [tutordownloadtutorassignment.do] download assignment done ...");
						
//						if (assignment!=null){
//							response.setCharacterEncoding("utf-8");
//					        response.setContentType("multipart/form-data");
//					        
//					        String encodedfileName = null;
//					        String agent = request.getHeader("USER-AGENT");
//					        if(null != agent && -1 != agent.indexOf("MSIE")){//IE
//					            encodedfileName = java.net.URLEncoder.encode(assignment.getFileName(),"UTF-8");
//					        }else if(null != agent && -1 != agent.indexOf("Mozilla")){
//					            encodedfileName = new String (assignment.getFileName().getBytes("UTF-8"),"iso-8859-1");
//					        }else{
//					            encodedfileName = java.net.URLEncoder.encode(assignment.getFileName(),"UTF-8");
//					        }
//					        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");
//					        
//					        InputStream inputStream=null;
//							OutputStream os=null;
//							try {
//								inputStream = new FileInputStream(new File(_filePath+assignment.getFilePath()));
//								os = response.getOutputStream();
//								byte[] b = new byte[2048];
//								int length;
//								while ((length = inputStream.read(b)) > 0) {
//								    os.write(b, 0, length);
//								}
//								os.flush();
//							} catch (Exception e) {
//								logger.info("this is [showstudentassignment.do] send file exception ...");
//								e.printStackTrace();
//							}finally{
//								if (os!=null){
//									os.close();
//								}
//								if (inputStream!=null){
//									inputStream.close();
//								}
//							}
//						}
					}else{
						result.put("status", -2);
						result.put("info", "illegal user");
						logger.info("this is [tutordownloadtutorassignment.do] illegal user ...");
					}
				} catch (Exception e) {
					e.printStackTrace();
					result.put("status", 0);
					result.put("info", "operation failed");
					logger.info("this is [tutordownloadtutorassignment.do] occur error ...");
				}
			}
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [tutordownloadtutorassignment.do] return ["+tmp+"] ...");
		return tmp;
	}
}