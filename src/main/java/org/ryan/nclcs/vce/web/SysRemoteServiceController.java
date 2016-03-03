
package org.ryan.nclcs.vce.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.ryan.nclcs.vce.annotation.SystemLogIsCheck;
import org.ryan.nclcs.vce.dao.Pagination;
import org.ryan.nclcs.vce.entity.AppStudentsScores;
import org.ryan.nclcs.vce.entity.SysDeviceToken;
import org.ryan.nclcs.vce.entity.SysGroups;
import org.ryan.nclcs.vce.entity.SysNotification;
import org.ryan.nclcs.vce.entity.SysNotificationDetail;
import org.ryan.nclcs.vce.entity.SysProperties;
import org.ryan.nclcs.vce.entity.SysRoles;
import org.ryan.nclcs.vce.entity.SysUsers;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
		
		try{
			JSONObject json=JSONObject.fromString(data);
			userId=0;
			if (json.has("userId")){
				userId=json.getInt("userId");
			}
			if (json.has("groupId")){
				groupId=json.getInt("groupId");
			}
			if (json.has("studentId")){
				studentId=json.getInt("studentId");
			}
			logger.info("this is [findstudentgroup.do] userId ["+userId+"] groupId ["+groupId+"] ...");
		}catch(Exception ex){
			logger.info("this is [findstudentgroup.do] get parameter error ...");
			result.put("status", -1);
			result.put("info", "parameters error");
			ex.printStackTrace();
		}
		
		List<Map<String,Object>> groupsInfo=null;
		Map<String, Object> parameters=new HashMap<String, Object>();
		if (userId!=0){
//			Integer currentUserId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
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
			
			SysUsers student=sysUsersManagementService.get(studentId);//当前要维护的学生对象
			SysGroups group=null;
			if (isAssistant&&groupId==0){//是校区助理角色，又要查校区的时候，直接用学生的校区群组ID查就可以了，校区助理角色不可以跨校区给学生转班
				group=student.getSysGroups().get(0);//学生只可能有一个组
				if (group!=null&&group.getGroupCategory()!=null&&group.getGroupCategory()==1){//判断这个多啊，就是想知道得到的这个群组是不是班级，是班级就查他的父群组(只适用于当想的数据结构，校区下只有班级)
					group=sysGroupsManagementService.get(group.getGroupParentId());
					parameters.put("id", group.getId());//学生的校区群组ID
				}
			}
			
			parameters.put("groupParentId", !isAssistant&&groupId==0?1:groupId);//groupId==0说明是查校区，不是校区助理就让groupParentId=1，就是查全部
			
			groupsInfo=sysGroupsManagementService.findGroup(parameters,userId);
			
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
		}else{
			parameters.put("groupParentId", groupId==0?1:groupId);//groupId==0说明是查校区，不是校区助理就让groupParentId=1，就是查全部
			groupsInfo=sysGroupsManagementService.findGroup(parameters,null);
		}
		
		result.put("status", 1);
		result.put("groupsInfo", groupsInfo);
		
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
				this.sysUsersManagementService.saveRegisterUser(user, groupNewClass, sysRolesManagementService.get(5));
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
				result.put("status", 2);
				result.put("info", "username is exist, try again!");
				logger.info("this is [saveuserregister.do] register failed for username is exist ...");
			}
			
		} catch (NumberFormatException e) {
			result.put("status", 0);
			result.put("info", "register failed, try again!");
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
					result.put("data", sysGroupsManagementService.findAllCampusOrClass(null, groupCategory));
					result.put("status", 1);
					result.put("info", "operation success");
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
					notification.setNotificationReceiveGroupIds(groupIds);
					notification.setNotificationUserInfo(currentUser);
					
					int roleCategory=-1;//1:全查，2:查校区(用户所在)和班级，3:查班级
					
					List<SysRoles> lstRoles=currentUser.getSysRoles();
					for (SysRoles role:lstRoles){
						if (role.getId()==1||role.getId()==2){//管理者|管理助理
							roleCategory=1;
							break;
						}else if (role.getId()==3){//校区助理
							roleCategory=2;
						}else if (role.getId()==4){//教师
							roleCategory=3;
						}
					}
					logger.info("this is [saveandsendnotification.do] get [u_sr] is ["+roleCategory+"]...");
					List <SysGroups> lstGroups=null;
					if (roleCategory==-1){
						result.put("status",0);
						result.put("info", "wrong role, login again please");
					}else if (roleCategory!=1){
						lstGroups=currentUser.getSysGroups();
					}
					
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
}
