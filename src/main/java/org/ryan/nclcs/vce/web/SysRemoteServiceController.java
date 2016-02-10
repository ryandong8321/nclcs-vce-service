package org.ryan.nclcs.vce.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.ryan.nclcs.vce.annotation.SystemLogIsCheck;
import org.ryan.nclcs.vce.annotation.SystemUserLoginIsCheck;
import org.ryan.nclcs.vce.entity.AppStudentsScores;
import org.ryan.nclcs.vce.entity.SysGroups;
import org.ryan.nclcs.vce.entity.SysProperties;
import org.ryan.nclcs.vce.entity.SysRoles;
import org.ryan.nclcs.vce.entity.SysUsers;
import org.ryan.nclcs.vce.service.appstudents.IAppStudentsManagementService;
import org.ryan.nclcs.vce.service.sysgroups.ISysGroupsManagementService;
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
		String userName=json.getString("userName")==null||json.getString("userName").equals("")||json.getString("userName").equals("null")
				?"":json.getString("userName");
		String userPWD=json.getString("userPWD")==null||json.getString("userPWD").equals("")||json.getString("userPWD").equals("null")
				?"":json.getString("userPWD");
		
		logger.info("this is [userlogin.do] userName ["+userName+"] pwd ["+userPWD+"]...");
		
		Map<String,Object> parameters=new HashMap<String,Object>();
		parameters.put("userName", userName);
		parameters.put("password", MD5.string2MD5(MD5.string2MD5(userPWD)));
		SysUsers user=sysUsersManagementService.isExistByParameters(parameters);
		if (user!=null){
			result.put("status", 1);
			result.put("info", "login success!");
			
			logger.info("this is [userlogin.do] login success ...");
			WebApplicationUtils.setNewToken(user.getId(), user.getUserName()+user.getPassword());
			result.put("token", WebApplicationUtils.getToken(user.getId()));
			result.put("userid", user.getId());
			result.put("username", user.getUserName());
			result.put("chinesename", user.getChineseName());
			
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
		Integer userId=0, groupId=0;
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
			logger.info("this is [findstudentgroup.do] userId ["+userId+"] groupId ["+groupId+"] ...");
		}catch(Exception ex){
			logger.info("this is [findstudentgroup.do] get parameter error ...");
			result.put("status", -1);
			result.put("data", "parameters error");
			ex.printStackTrace();
		}
		
		List<Map<String,Object>> groupsInfo=null;
		Map<String, Object> parameters=new HashMap<String, Object>();
		if (userId!=0){
			Integer currentUserId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
			SysUsers currentUser=sysUsersManagementService.get(currentUserId);
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
			
			groupsInfo=sysGroupsManagementService.findGroup(parameters,currentUserId);
			
			if (user.getSysGroups()!=null&&!user.getSysGroups().isEmpty()){
				group=group==null?user.getSysGroups().get(0):group;
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
			parameters.put("groupParentId", groupId);//groupId==0说明是查校区，不是校区助理就让groupParentId=1，就是查全部
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
		
		logger.info("this is [saveuserregister.do] is decoding ...");
		JSONObject json=JSONObject.fromString(this.decodeParameters(data));
		logger.info("this is [saveuserregister.do] decode done ...");
		
		try {
			logger.info("this is [saveuserregister.do] check is user_name exist...");
			String userName=json.getString("userName")==null||json.getString("userName").equals("")||json.getString("userName").equals("null")
					?"":json.getString("userName");
			
			//check user_name is exist
			Map<String,Object> parameters=new HashMap<String,Object>();
			parameters.put("userName", userName);
			SysUsers user=sysUsersManagementService.isExistByParameters(parameters);
			
			if (user==null){//not exist
				String userPWD=json.getString("userPWD")==null||json.getString("userPWD").equals("")||json.getString("userPWD").equals("null")
						?"":json.getString("userPWD");
				String pinyin=json.getString("pinyin")==null||json.getString("pinyin").equals("")||json.getString("pinyin").equals("null")
						?"":json.getString("pinyin");
				String chineseName=json.getString("chineseName")==null||json.getString("chineseName").equals("")||json.getString("chineseName").equals("null")
						?"":json.getString("chineseName");
				String englishName=json.getString("englishName")==null||json.getString("englishName").equals("")||json.getString("englishName").equals("null")
						?"":json.getString("englishName");
				String homeAddress=json.getString("homeAddress")==null||json.getString("homeAddress").equals("")||json.getString("homeAddress").equals("null")
						?"":json.getString("homeAddress");
				String mobilePhone=json.getString("mobilePhone")==null||json.getString("mobilePhone").equals("")||json.getString("mobilePhone").equals("null")
						?"":json.getString("mobilePhone");
				String homePhone=json.getString("homePhone")==null||json.getString("homePhone").equals("")||json.getString("homePhone").equals("null")
						?"":json.getString("homePhone");
				String emailAddress=json.getString("emailAddress")==null||json.getString("emailAddress").equals("")||json.getString("emailAddress").equals("null")
						?"":json.getString("emailAddress");
				String daySchool=json.getString("daySchool")==null||json.getString("daySchool").equals("")||json.getString("daySchool").equals("null")
						?"":json.getString("daySchool");
				String daySchoolGrade=json.getString("daySchoolGrade")==null||json.getString("daySchoolGrade").equals("")||json.getString("daySchoolGrade").equals("null")
						?"":json.getString("daySchoolGrade");
				String isLearnChinese=json.getString("isLearnChinese")==null||json.getString("isLearnChinese").equals("")||json.getString("isLearnChinese").equals("null")
						?"-1":json.getString("isLearnChinese");
				String campus=json.getString("campus")==null||json.getString("campus").equals("")||json.getString("campus").equals("null")
						?"0":json.getString("campus");
				String campusClass=json.getString("campusClass")==null||json.getString("campusClass").equals("")||json.getString("campusClass").equals("null")
						?"0":json.getString("campusClass");
				
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
				
				//设置角色为学生
				List<SysRoles> lstRoles=new ArrayList<SysRoles>();
				lstRoles.add(sysRolesManagementService.get(5));
				user.setSysRoles(lstRoles);
				
				SysGroups groupNewCampus=sysGroupsManagementService.get(Integer.parseInt(campus));
				user.setVceSchoolName(groupNewCampus.getGroupName());
				
				SysGroups groupNewClass=sysGroupsManagementService.get(Integer.parseInt(campusClass));
				user.setVceClassName(groupNewClass.getGroupName());
				List<SysUsers> lstUsers=groupNewClass.getSysGroupsUsers();
				if (lstUsers==null||lstUsers.isEmpty()){
					lstUsers=new ArrayList<SysUsers>();
				}
				lstUsers.add(user);
				groupNewClass.setSysGroupsUsers(lstUsers);
				logger.info("this is [saveuserregister.do] is saving user ...");
				sysGroupsManagementService.save(groupNewClass);
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
		Integer currentUserId=-1;
		String token=null;
		SysUsers user=null;
		Map<String, Object> jsonResult=new HashMap<String, Object>();
		if (!json.has("userId")||!json.has("token")){
			result.put("state", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [showpersonalinfo.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				userId=json.getInt("userId");
				token=json.getString("token");
				String localToken=WebApplicationUtils.getToken(userId);
				if (request.getSession().getAttribute("u_id")!=null&&!request.getSession().getAttribute("u_id").equals("")){
					currentUserId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
				}
				if (localToken!=null&&localToken.equals(token)&&userId.equals(currentUserId)){
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
					}
					result.put("state", 1);
					result.put("info", "operation success");
					result.put("data", JSONObject.fromMap(jsonResult).toString());
				}else{
					result.put("state", -2);
					result.put("info", "illegal user");
					logger.info("this is [showpersonalinfo.do] illegal user ...");
				}
				
			}catch(Exception ex){
				ex.printStackTrace();
				result.put("state", 0);
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
		Integer currentUserId=-1;
		String token=null;
		SysUsers user=null;
		if (!json.has("userId")||!json.has("token")){
			result.put("state", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [savepersonalinfo.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				userId=json.getInt("userId");
				token=json.getString("token");
				String localToken=WebApplicationUtils.getToken(userId);
				if (request.getSession().getAttribute("u_id")!=null&&!request.getSession().getAttribute("u_id").equals("")){
					currentUserId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
				}
				if (localToken!=null&&localToken.equals(token)&&userId.equals(currentUserId)){
					user=sysUsersManagementService.get(userId);
					String userName=json.getString("userName")==null||json.getString("userName").equals("")||json.getString("userName").equals("null")
							?"":json.getString("userName");
					String chineseName=json.getString("chineseName")==null||json.getString("chineseName").equals("")||json.getString("chineseName").equals("null")
							?"":json.getString("chineseName");
					String englishName=json.getString("englishName")==null||json.getString("englishName").equals("")||json.getString("englishName").equals("null")
							?"":json.getString("englishName");
					String mobilePhone=json.getString("mobilePhone")==null||json.getString("mobilePhone").equals("")||json.getString("mobilePhone").equals("null")
							?"":json.getString("mobilePhone");
					String emailAddress=json.getString("emailAddress")==null||json.getString("emailAddress").equals("")||json.getString("emailAddress").equals("null")
							?"":json.getString("emailAddress");
					
					user.setUserName(userName);
					user.setChineseName(chineseName);
					user.setEnglishName(englishName);
					user.setMobilePhone(mobilePhone);
					user.setEmailAddress(emailAddress);
					
					if (isStudent(user)){
						String pinyin=json.getString("pinyin")==null||json.getString("pinyin").equals("")||json.getString("pinyin").equals("null")
								?"":json.getString("pinyin");
						String homeAddress=json.getString("homeAddress")==null||json.getString("homeAddress").equals("")||json.getString("homeAddress").equals("null")
								?"":json.getString("homeAddress");
						String homePhone=json.getString("homePhone")==null||json.getString("homePhone").equals("")||json.getString("homePhone").equals("null")
								?"":json.getString("homePhone");
						String daySchool=json.getString("daySchool")==null||json.getString("daySchool").equals("")||json.getString("daySchool").equals("null")
								?"":json.getString("daySchool");
						String daySchoolGrade=json.getString("daySchoolGrade")==null||json.getString("daySchoolGrade").equals("")||json.getString("daySchoolGrade").equals("null")
								?"":json.getString("daySchoolGrade");
						user.setPinyin(pinyin);
						user.setHomeAddress(homeAddress);
						user.setHomePhone(homePhone);
						user.setDaySchool(daySchool);
						user.setDaySchoolGrade(daySchoolGrade);
					}
					logger.info("this is [savepersonalinfo.do] show user ["+user+"] ...");
					sysUsersManagementService.save(user);
					result.put("state", 1);
					result.put("info", "operation success");
				}else{
					result.put("state", -2);
					result.put("info", "illegal user");
					logger.info("this is [savepersonalinfo.do] illegal user ...");
				}
				
			}catch(Exception ex){
				ex.printStackTrace();
				result.put("state", 0);
				result.put("info", "find user info error");
				logger.info("this is [savepersonalinfo.do] exception ...");
			}
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [savepersonalinfo.do] return ["+tmp+"] ...");
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
		Integer currentUserId=-1;
		String token=null;
		if (!json.has("userId")||!json.has("token")){
			result.put("state", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [showstudentlist.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				userId=json.getInt("userId");
				token=json.getString("token");
				String localToken=WebApplicationUtils.getToken(userId);
				if (request.getSession().getAttribute("u_id")!=null&&!request.getSession().getAttribute("u_id").equals("")){
					currentUserId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
				}
				if (localToken!=null&&localToken.equals(token)&&userId.equals(currentUserId)){
					//每页大小
					int displayLength=Integer.parseInt(json.getString("displayLength")==null||json.getString("displayLength").equals("")||json.getString("displayLength").equals("null")
							?"10":json.getString("displayLength"));
					//起始值
					int displayStart=Integer.parseInt(json.getString("displayStart")==null||json.getString("displayStart").equals("")||json.getString("displayStart").equals("null")
							?"0":json.getString("displayStart"));
					//查询条件
					String param=json.getString("displayStart")==null||json.getString("displayStart").equals("")||json.getString("displayStart").equals("null")
							?"":json.getString("displayStart");
					
					logger.info("this is [showstudentlist.do] requset pram [displayLength = {"+displayLength+"}],[displayStart = {"+displayStart+"}]");
					
					Map<String, Object> parameters=new HashMap<String, Object>();
					if (param!=null&&!param.equals("")){
						parameters.put("userName", param);
						parameters.put("schoolName", param);
						parameters.put("sgs.groupName", param);
					}
					
					result=appStudentsManagementService.searchDataForApp(displayLength, displayStart, parameters, currentUserId, false);
					result.put("state", 1);
					result.put("info", "operation success");
				}else{
					result.put("state", -2);
					result.put("info", "illegal user");
					logger.info("this is [showstudentlist.do] illegal user ...");
				}
				
			}catch(Exception ex){
				ex.printStackTrace();
				result.put("state", 0);
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
		Integer currentUserId=-1;
		String token=null;
		SysUsers studentUser=null;
		if (!json.has("userId")||!json.has("token")){
			result.put("state", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [showstudentinfo.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				userId=json.getInt("userId");
				token=json.getString("token");
				String localToken=WebApplicationUtils.getToken(userId);
				if (request.getSession().getAttribute("u_id")!=null&&!request.getSession().getAttribute("u_id").equals("")){
					currentUserId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
				}
				if (localToken!=null&&localToken.equals(token)&&userId.equals(currentUserId)){
					Map<String, Object> jsonResult=new HashMap<String, Object>();
					studentUser=sysUsersManagementService.get(userId);
					
					jsonResult.put("userId", studentUser.getId());
					jsonResult.put("userName", studentUser.getUserName());
					jsonResult.put("userChineseName", studentUser.getChineseName());
					jsonResult.put("userEnglishName", studentUser.getEnglishName());
					jsonResult.put("mobilePhone", studentUser.getMobilePhone());
					jsonResult.put("emailAddress", studentUser.getEmailAddress());
					jsonResult.put("pinyin", studentUser.getPinyin());
					jsonResult.put("homeAddress", studentUser.getHomeAddress());
					jsonResult.put("homePhone", studentUser.getHomePhone());
					jsonResult.put("daySchool", studentUser.getDaySchool());
					jsonResult.put("daySchoolGrade", studentUser.getDaySchoolGrade());
					
					Map<String, Object> parameters=new HashMap<String, Object>();
					parameters.put("propertyParentId", 5);//是否在日校学习中文
					List<Map<String,Object>> propertiesInfo=sysPropertiesManagementService.findProperty(parameters);
					if (studentUser.getPropertyIsLearnChinese()!=null){
						for(Map<String, Object> map:propertiesInfo){
							if (map.get("id").equals(studentUser.getPropertyIsLearnChinese().getId())){
								map.put("selected", "selected");
								break;
							}
						}
					}
					jsonResult.put("isLearnChinese", propertiesInfo);
					
					//校区群组
					List<Map<String,Object>> campusInfo=null;
					List<Map<String,Object>> classInfo=null;
					Integer groupId=0;
					SysUsers currentUser=sysUsersManagementService.get(currentUserId);
					boolean isAssistant=false;//是否是校区助理
					for (SysRoles tmp:currentUser.getSysRoles()){
						if (tmp.getId()==1||tmp.getId()==2){//管理者或管理助理
							isAssistant=false;
							break;
						}else if (tmp.getId()==3){//校区助理
							isAssistant=true;
						}
					}
					SysGroups group=studentUser.getSysGroups().get(0);//学生只可能有一个组
					if (group!=null&&group.getGroupCategory()!=null&&group.getGroupCategory()==1){//判断这个多啊，就是想知道得到的这个群组是不是班级，是班级就查他的父群组(只适用于当想的数据结构，校区下只有班级)
						group=sysGroupsManagementService.get(group.getGroupParentId());
						parameters.put("id", group.getId());//学生的校区群组ID
					}
					parameters.put("groupParentId", !isAssistant&&groupId==0?1:groupId);//groupId==0说明是查校区，不是校区助理就让groupParentId=1，就是查全部
					//取校区信息
					logger.info("this is [showstudentinfo.do] find campus isAssistant ["+isAssistant+"] groupId ["+groupId+"]...");
					campusInfo=sysGroupsManagementService.findGroup(parameters,currentUserId);
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
									classInfo=sysGroupsManagementService.findGroup(parameters,currentUserId);
									break;
								}
							}
						}
					}
					jsonResult.put("campusInfo", campusInfo);
					
					if (studentUser.getSysGroups()!=null&&!studentUser.getSysGroups().isEmpty()){
						group=group==null?studentUser.getSysGroups().get(0):group;
						if (group!=null&&group.getGroupCategory()!=null){
							if (group.getGroupCategory()==1&&groupId==0){
								group=sysGroupsManagementService.get(group.getGroupParentId());
							}
							for(Map<String, Object> map:classInfo){
								if (map.get("id").equals(group.getId())){
									map.put("selected", "selected");
									break;
								}
							}
						}
					}
					jsonResult.put("classInfo", classInfo);
					
					result.put("state", 1);
					result.put("info", "operation success");
					result.put("data", JSONObject.fromMap(jsonResult).toString());
				}else{
					result.put("state", -2);
					result.put("info", "illegal user");
					logger.info("this is [showstudentinfo.do] illegal user ...");
				}
				
			}catch(Exception ex){
				ex.printStackTrace();
				result.put("state", 0);
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
		Integer currentUserId=-1;
		String token=null;
		SysUsers originalStudent=null;
		if (!json.has("userId")||!json.has("token")){
			result.put("state", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [savestudentinfo.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				userId=json.getInt("userId");
				token=json.getString("token");
				String localToken=WebApplicationUtils.getToken(userId);
				if (request.getSession().getAttribute("u_id")!=null&&!request.getSession().getAttribute("u_id").equals("")){
					currentUserId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
				}
				Integer isLearnChinese=Integer.parseInt(json.getString("isLearnChinese")==null||json.getString("isLearnChinese").equals("")||json.getString("isLearnChinese").equals("null")
						?"-1":json.getString("isLearnChinese"));
				Integer studentGroupClassId=Integer.parseInt(json.getString("studentGroupClassId")==null||json.getString("studentGroupClassId").equals("")||json.getString("studentGroupClassId").equals("null")
						?"-1":json.getString("studentGroupClassId"));
				
				if (localToken!=null&&localToken.equals(token)&&userId.equals(currentUserId)&&!isLearnChinese.equals(-1)&&!studentGroupClassId.equals(-1)){
					originalStudent=sysUsersManagementService.get(userId);
					String userName=json.getString("userName")==null||json.getString("userName").equals("")||json.getString("userName").equals("null")
							?"":json.getString("userName");
					String chineseName=json.getString("chineseName")==null||json.getString("chineseName").equals("")||json.getString("chineseName").equals("null")
							?"":json.getString("chineseName");
					String englishName=json.getString("englishName")==null||json.getString("englishName").equals("")||json.getString("englishName").equals("null")
							?"":json.getString("englishName");
					String mobilePhone=json.getString("mobilePhone")==null||json.getString("mobilePhone").equals("")||json.getString("mobilePhone").equals("null")
							?"":json.getString("mobilePhone");
					String emailAddress=json.getString("emailAddress")==null||json.getString("emailAddress").equals("")||json.getString("emailAddress").equals("null")
							?"":json.getString("emailAddress");
					
					originalStudent.setUserName(userName);
					originalStudent.setChineseName(chineseName);
					originalStudent.setEnglishName(englishName);
					originalStudent.setMobilePhone(mobilePhone);
					originalStudent.setEmailAddress(emailAddress);
					
					String pinyin=json.getString("pinyin")==null||json.getString("pinyin").equals("")||json.getString("pinyin").equals("null")
							?"":json.getString("pinyin");
					String homeAddress=json.getString("homeAddress")==null||json.getString("homeAddress").equals("")||json.getString("homeAddress").equals("null")
							?"":json.getString("homeAddress");
					String homePhone=json.getString("homePhone")==null||json.getString("homePhone").equals("")||json.getString("homePhone").equals("null")
							?"":json.getString("homePhone");
					String daySchool=json.getString("daySchool")==null||json.getString("daySchool").equals("")||json.getString("daySchool").equals("null")
							?"":json.getString("daySchool");
					String daySchoolGrade=json.getString("daySchoolGrade")==null||json.getString("daySchoolGrade").equals("")||json.getString("daySchoolGrade").equals("null")
							?"":json.getString("daySchoolGrade");
					originalStudent.setPinyin(pinyin);
					originalStudent.setHomeAddress(homeAddress);
					originalStudent.setHomePhone(homePhone);
					originalStudent.setDaySchool(daySchool);
					originalStudent.setDaySchoolGrade(daySchoolGrade);
					
					SysProperties property=sysPropertiesManagementService.get(isLearnChinese);
					originalStudent.setPropertyIsLearnChinese(property);
					logger.info("this is [savestudentinfo.do] show user ["+originalStudent+"] ...");
					
					result=sysGroupsManagementService.saveStudentGroupChange(originalStudent,studentGroupClassId,sysUsersManagementService.get(currentUserId));
					result.put("info", "operation success");
				}else{
					result.put("state", -2);
					result.put("info", "illegal user");
					logger.info("this is [savestudentinfo.do] illegal user ...");
				}
				
			}catch(Exception ex){
				ex.printStackTrace();
				result.put("state", 0);
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
		Integer currentUserId=-1;
		String token=null;
		if (!json.has("userId")||!json.has("token")){
			result.put("state", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [deletestudents.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				userId=json.getInt("userId");
				token=json.getString("token");
				String localToken=WebApplicationUtils.getToken(userId);
				if (request.getSession().getAttribute("u_id")!=null&&!request.getSession().getAttribute("u_id").equals("")){
					currentUserId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
				}
				String studentIds=json.getString("studentIds")==null||json.getString("studentIds").equals("")||json.getString("studentIds").equals("null")
						?"":json.getString("studentIds");
				
				if (localToken!=null&&localToken.equals(token)&&userId.equals(currentUserId)&&!studentIds.equals("")){
					logger.info("this is [deletestudents.do] ready to delete ...");
					sysUsersManagementService.deleteMultiple(studentIds);
					result.put("status", 1);
					result.put("info", "operation success!");
					logger.info("this is [deletestudents.do] to delete done...");
				}
			}catch(Exception ex){
				ex.printStackTrace();
				result.put("state", 0);
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
		Integer currentUserId=-1;
		String token=null;
		if (!json.has("userId")||!json.has("token")){
			result.put("state", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [showstudentssorceinfo.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				userId=json.getInt("userId");
				token=json.getString("token");
				String localToken=WebApplicationUtils.getToken(userId);
				if (request.getSession().getAttribute("u_id")!=null&&!request.getSession().getAttribute("u_id").equals("")){
					currentUserId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
				}
				logger.info("this is [showstudentssorceinfo.do] userId ["+userId+"] ...");
				if (localToken!=null&&localToken.equals(token)&&userId.equals(currentUserId)){
					List<SysProperties> examNames=null;
					List<Map<String, Object>> lstEN=new ArrayList<Map<String, Object>>();
					List<Map<String, Object>> scores=new ArrayList<Map<String, Object>>();
					Map<String, Object> jsonTmp=null;
					Map<String, Object> resultData=new HashMap<String,Object>();
					
					logger.info("this is [showstudentssorceinfo.do] find user ...");
					SysUsers studentUser=sysUsersManagementService.get(userId);
					if (studentUser.getStudentsScores()!=null&&!studentUser.getStudentsScores().isEmpty()){
						for (AppStudentsScores score:studentUser.getStudentsScores()){
							jsonTmp=new HashMap<String, Object>();
							jsonTmp.put("id", score.getAppScoreProperty().getId());
							jsonTmp.put("scoreValue", score.getScoreValue());
							scores.add(jsonTmp);
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
					}
					resultData.put("userId", studentUser.getId());
					resultData.put("userChineseName", studentUser.getChineseName());
					resultData.put("userScores", scores);
					resultData.put("userAllExam", lstEN);
					
					result.put("status", 1);
					result.put("data", resultData);
					result.put("info", "operation success!");
				}
				
			}catch(Exception ex){
				ex.printStackTrace();
				result.put("state", 0);
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
		Integer currentUserId=-1;
		String token=null;
		if (!json.has("userId")||!json.has("token")){
			result.put("state", -1);
			result.put("info", "the lack of parameter");
			logger.info("this is [showstudentssorceinfo.do] the lack of parameter ...");
		}
		
		if (result.isEmpty()){
			try{
				userId=json.getInt("userId");
				token=json.getString("token");
				String localToken=WebApplicationUtils.getToken(userId);
				if (request.getSession().getAttribute("u_id")!=null&&!request.getSession().getAttribute("u_id").equals("")){
					currentUserId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
				}
				logger.info("this is [showstudentssorceinfo.do] userId ["+userId+"] ...");
				if (localToken!=null&&localToken.equals(token)&&userId.equals(currentUserId)){
					SysUsers studentUser=sysUsersManagementService.get(userId);
					
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
				}
			}catch(Exception ex){
				ex.printStackTrace();
				result.put("state", 0);
				result.put("info", "find user info error");
				logger.info("this is [showstudentssorceinfo.do] exception ...");
			}
		}
		String tmp=JSONObject.fromMap(result).toString();
		logger.info("this is [savestudentinfo.do] return ["+tmp+"] ...");
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
}
