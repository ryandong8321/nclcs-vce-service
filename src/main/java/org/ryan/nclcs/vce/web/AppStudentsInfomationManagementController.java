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
import org.ryan.nclcs.vce.entity.AppStudentsScores;
import org.ryan.nclcs.vce.entity.SysGroups;
import org.ryan.nclcs.vce.entity.SysProperties;
import org.ryan.nclcs.vce.entity.SysRoles;
import org.ryan.nclcs.vce.entity.SysUsers;
import org.ryan.nclcs.vce.service.appstudents.IAppStudentsManagementService;
import org.ryan.nclcs.vce.service.sysgroups.ISysGroupsManagementService;
import org.ryan.nclcs.vce.service.sysproperties.ISysPropertiesManagementService;
import org.ryan.nclcs.vce.service.sysusers.ISysUsersManagementService;
import org.ryan.nclcs.vce.web.util.MD5;
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
@RequestMapping("/appstudentsinfomanagement")
public class AppStudentsInfomationManagementController {
	
	protected static Logger logger = LoggerFactory.getLogger(AppStudentsInfomationManagementController.class);
	
	@Autowired
	private ISysUsersManagementService sysUsersManagementService;
	
	@Autowired
	private IAppStudentsManagementService appStudentsManagementService;
	
	@Autowired
	private ISysPropertiesManagementService sysPropertiesManagementService;
	
	@Autowired
	private ISysGroupsManagementService sysGroupsManagementService;
	
	@RequestMapping(value = "/appstudentsinfolist.do")
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="查询学生信息")
	public String appStudentsInfoList(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		logger.info("this is [appstudentsinfolist.do] start ...");
		logger.info("this is [appstudentsinfolist.do] end ...");
		return "appstudents/appstudentslist";
	}
	
	@RequestMapping(value = "/initsysuserstable.do")
	@ResponseBody
	@SystemLogIsCheck(description="查询学生信息")
	public String initSysUsersTable(HttpServletRequest request) {
		logger.info("this is [initsysuserstable.do] start ...");
		
		int displayLength = ServletRequestUtils.getIntParameter(request, "length", 0), //每页大小
			displayStart = ServletRequestUtils.getIntParameter(request, "start", 0), //起始值
			sEcho = ServletRequestUtils.getIntParameter(request, "draw", 0);
		
		
		logger.info("this is [initsysuserstable.do] requset pram [displayLength = "+displayLength+"],[displayStart = {"+displayStart+"}],[sEcho = {"+sEcho+"}]");
		
		String userName=ServletRequestUtils.getStringParameter(request, "sysusers_name", null), 
			chineseName=ServletRequestUtils.getStringParameter(request, "sysusers_chinesename", null), 
			englishName=ServletRequestUtils.getStringParameter(request, "sysusers_englishname", null),
			schoolName=ServletRequestUtils.getStringParameter(request, "appstudents_schoolname", null),
			className=ServletRequestUtils.getStringParameter(request, "appstudents_classname", null),
			sort=ServletRequestUtils.getStringParameter(request, "order[0][column]", null),
			dir=ServletRequestUtils.getStringParameter(request, "order[0][dir]", null);
		
		logger.info("this is [initsysuserstable.do] requset pram [userName = {"+userName+"}],[chineseName = {"+chineseName+"}]"
				+ ",[englistName = {"+englishName+"},[schoolName = {"+schoolName+"}],[className = {"+className+"}]"
				+ ",[sort = {"+sort+"}],[dir = {"+dir+"}]");
		
		Map<String, Object> parameters=new HashMap<String, Object>();
		if (userName!=null&&!userName.equals("")){
			parameters.put("userName", userName);
		}
		if (chineseName!=null&&!chineseName.equals("")){
			parameters.put("chineseName", chineseName);
		}
		if (englishName!=null&&!englishName.equals("")){
			parameters.put("englishName", englishName);
		}
		if (schoolName!=null&&!schoolName.equals("")){
			parameters.put("schoolName", schoolName);
		}
		if (className!=null&&!className.equals("")){
			parameters.put("sgs.groupName", className);
		}
		
		if (sort!=null&&!sort.equals("")){
			parameters.put("sort", sort);
		}
		if (dir!=null&&!dir.equals("")){
			parameters.put("order", dir);
		}
		
		Integer userId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
		
		Map<String,Object> returnData=appStudentsManagementService.searchDataForAjax(displayLength, displayStart, sEcho, parameters, userId, false);
		
		returnData.put("draw", sEcho);
		
		String result=JSONObject.fromObject(returnData).toString();
		logger.info("this is [initsysuserstable.do] data ["+result+"] ...");
		logger.info("this is [initsysuserstable.do] end ...");
		return result;
	}
	
	@RequestMapping(value = "/showstudentinfo.do", method=RequestMethod.POST)
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="显示学生信息")
	public String showSysUsersInfo(HttpServletRequest request, Integer userId) {
		logger.info("this is [showstudentinfo.do] start ...");
		logger.info("this is [showstudentinfo.do] userId ["+userId+"] ...");
		SysUsers user=null;
		try{
			if (userId!=null&&userId!=-1){
				logger.info("this is [showstudentinfo.do] find user ...");
				user=sysUsersManagementService.get(userId);
			}
		}catch(Exception ex){
			logger.info("this is [showstudentinfo.do] find user failed...");
			ex.printStackTrace();
		}
		
		logger.info("this is [showstudentinfo.do] data ["+user+"] ...");
		
		request.setAttribute("sysuser", user);
		logger.info("this is [showstudentinfo.do] end ...");
		return "appstudents/appstudentsinfo";
	}
	
	@RequestMapping(value = "/findpropertyislearnchinese.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemUserLoginIsCheck
	public String findPropertyIsLearnChinese(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [findpropertyislearnchinese.do] start ...");
		Integer userId=0, propertyId=0;
		Map<String, Object> result=new HashMap<String, Object>();
		
		if (data!=null&&!data.equals("")){
			try {
				logger.info("this is [findpropertyislearnchinese.do] is decoding ...");
				data=URLDecoder.decode(data, "utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.info("this is [findpropertyislearnchinese.do] occur error when program decoding ...");
				e.printStackTrace();
			}
		}
		
		try{
			JSONObject json=JSONObject.fromString(data);
			userId=Integer.parseInt(
					json.getString("userId")==null||json.getString("userId").equals("")||json.getString("userId").equals("null")
						?"0":json.getString("userId"));
			propertyId=json.getInt("propertyId");
		}catch(Exception ex){
			logger.info("this is [findpropertyislearnchinese.do] get parameter error ...");
			result.put("status", -1);
			result.put("data", "parameters error");
			ex.printStackTrace();
		}
		
		Map<String, Object> parameters=new HashMap<String, Object>();
		parameters.put("propertyParentId", propertyId);
		List<Map<String,Object>> propertiesInfo=sysPropertiesManagementService.findProperty(parameters);
		
		if (userId!=0){
			SysUsers user=sysUsersManagementService.get(userId);
			if (user.getPropertyIsLearnChinese()!=null){
				for(Map<String, Object> map:propertiesInfo){
					if (map.get("id").equals(user.getPropertyIsLearnChinese().getId())){
						map.put("selected", "selected");
						break;
					}
				}
			}
		}
		
		result.put("status", 1);
		result.put("data", propertiesInfo);
		
		String json=JSONObject.fromObject(result).toString();
		logger.info("this is [findpropertyislearnchinese.do] data ["+json+"] ...");
		logger.info("this is [findpropertyislearnchinese.do] end ...");
		return json;
	}
	
	@RequestMapping(value = "/findstudentgroup.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemUserLoginIsCheck
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
			userId=Integer.parseInt(
					json.getString("userId")==null||json.getString("userId").equals("")||json.getString("userId").equals("null")
						?"0":json.getString("userId"));
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
		if (userId!=0){
			Map<String, Object> parameters=new HashMap<String, Object>();
			
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
		}
		
		result.put("status", 1);
		result.put("data", groupsInfo);
		
		String json=JSONObject.fromObject(result).toString();
		logger.info("this is [findstudentgroup.do] data ["+json+"] ...");
		logger.info("this is [findstudentgroup.do] end ...");
		return json;
	}
	
	@RequestMapping(value = "/savestudentinfo.do", method=RequestMethod.POST)
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="保存学生信息")
	public String saveStudentInfo(HttpServletRequest request, @ModelAttribute("sysuser") SysUsers sysUsers, Integer userId, Integer propertyIsLearnChineseId, Integer studentGroupId, Integer studentGroupClassId) {
		logger.info("this is [savestudentinfo.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		
		logger.info("this is [savestudentinfo.do] check is user_name exist...");
		Map<String,Object> parameters=new HashMap<String,Object>();
		if (userId==null||userId==0){
			parameters.put("userName", sysUsers.getUserName());
			if (sysUsersManagementService.isExistByParameters(parameters)!=null){
				result.put("status", 0);
				result.put("data", "save failed because user_name has exist!");
			}
		}
		
		if (result.isEmpty()){
			SysUsers originalUser=null;
			if (userId!=null&&userId!=0){
//				sysUsers.setId(userId);
				originalUser=sysUsersManagementService.get(userId);
				
				originalUser.setEnglishName(sysUsers.getEnglishName()==null?originalUser.getEnglishName():sysUsers.getEnglishName());
				originalUser.setChineseName(sysUsers.getChineseName()==null?originalUser.getChineseName():sysUsers.getChineseName());
				originalUser.setPinyin(sysUsers.getPinyin()==null?originalUser.getPinyin():sysUsers.getPinyin());
				originalUser.setMobilePhone(sysUsers.getMobilePhone()==null?originalUser.getMobilePhone():sysUsers.getMobilePhone());
				originalUser.setEmailAddress(sysUsers.getEmailAddress()==null?originalUser.getEmailAddress():sysUsers.getEmailAddress());
				originalUser.setHomeAddress(sysUsers.getHomeAddress()==null?originalUser.getHomeAddress():sysUsers.getHomeAddress());
				originalUser.setHomePhone(sysUsers.getHomePhone()==null?originalUser.getHomePhone():sysUsers.getHomePhone());
				originalUser.setDaySchool(sysUsers.getDaySchool()==null?originalUser.getDaySchool():sysUsers.getDaySchool());
				originalUser.setDaySchoolGrade(sysUsers.getDaySchoolGrade()==null?originalUser.getDaySchoolGrade():sysUsers.getDaySchoolGrade());
				
				if (propertyIsLearnChineseId!=null){
					SysProperties property=sysPropertiesManagementService.get(propertyIsLearnChineseId);
					originalUser.setPropertyIsLearnChinese(property);
				}
				
			}else if (userId==null||userId==0){
				sysUsers.setPassword(MD5.string2MD5(MD5.string2MD5(sysUsers.getPassword())));
			}
			
			try{
				logger.info("this is [savestudentinfo.do] is saving ...");
//				sysUsers.setCreateTime(new Date(Calendar.getInstance().getTimeInMillis()));
				logger.info("this is [savestudentinfo.do] show sysUsers ["+originalUser+"] ...");
				
				if (studentGroupClassId!=null){
					result=sysGroupsManagementService.saveStudentGroupChange(originalUser,studentGroupClassId
							,sysUsersManagementService.get(Integer.parseInt(""+request.getSession().getAttribute("u_id"))));
					
//					if (originalUser.getSysGroups()!=null&&!originalUser.getSysGroups().isEmpty()){
//						SysGroups currentGroup=originalUser.getSysGroups().get(0);
//						if (!currentGroup.getId().equals(studentGroupClassId)){
//							result=sysGroupsManagementService.saveUserGroupChange(originalUser,studentGroupClassId);
//							
//							SysGroups group=sysGroupsManagementService.get(studentGroupClassId);
//							originalUser.setVceClassName(group.getGroupName());
//							SysGroups parentGroup=sysGroupsManagementService.get(group.getGroupParentId());
//							originalUser.setVceSchoolName(parentGroup.getGroupName());
//							
//							if (group.getSysGroupsUsers()!=null&&!group.getSysGroupsUsers().isEmpty()){
//								lstUsers=group.getSysGroupsUsers();
////								SysUsers tmp=null;
////								boolean isChanged=false;
////								for (int i=0;i<lstUsers.size();i++){
////									tmp=lstUsers.get(i);
////									if (tmp.getId().equals(originalUser.getId())){
////										lstUsers.set(i, originalUser);
////										isChanged=true;
////										break;
////									}
////								}
////								if (!isChanged){
////									lstUsers.add(originalUser);
////								}
//							}
//							
//						}
//					}
//					
//					SysGroups group=sysGroupsManagementService.get(studentGroupClassId);
//					originalUser.setVceClassName(group.getGroupName());
//					SysGroups parentGroup=sysGroupsManagementService.get(group.getGroupParentId());
//					originalUser.setVceSchoolName(parentGroup.getGroupName());
//					sysGroupsManagementService.save(group);
				}else{
					sysUsersManagementService.save(originalUser);
					result.put("status", 1);
					result.put("data", "operation success!");
					logger.info("this is [savestudentinfo.do] save sysUsers done ...");
				}
			}catch(Exception ex){
				logger.info("this is [savestudentinfo.do] save sysUsers error ...");
				result.put("status", 0);
				result.put("data", "save failed, try again!");
				ex.printStackTrace();
			}
			logger.info("this is [savestudentinfo.do] show result ["+result+"] ...");
		}
		
		request.setAttribute("result", result.get("data"));
		request.setAttribute("sysuser", sysUsers);
		return result.get("status").equals(1)?"forward:/appstudentsinfomanagement/appstudentsinfolist.do":"forward:/appstudentsinfomanagement/showstudentinfo.do";
	}
	
	@RequestMapping(value = "/appstudentsscorelist.do")
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="查询学生成绩信息")
	public String appStudentsScoreList(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		logger.info("this is [appstudentsscorelist.do] start ...");
		logger.info("this is [appstudentsscorelist.do] end ...");
		return "appstudents/appstudentsscorelist";
	}
	
	@RequestMapping(value = "/initscoretable.do")
	@ResponseBody
	@SystemLogIsCheck(description="查询学生成绩信息")
	public String initScoreTable(HttpServletRequest request) {
		logger.info("this is [initscoretable.do] start ...");
		
		int displayLength = ServletRequestUtils.getIntParameter(request, "length", 0), //每页大小
			displayStart = ServletRequestUtils.getIntParameter(request, "start", 0), //起始值
			sEcho = ServletRequestUtils.getIntParameter(request, "draw", 0);
		
		
		logger.info("this is [initscoretable.do] requset pram [displayLength = "+displayLength+"],[displayStart = {"+displayStart+"}],[sEcho = {"+sEcho+"}]");
		
		String userName=ServletRequestUtils.getStringParameter(request, "sysusers_name", null), 
			chineseName=ServletRequestUtils.getStringParameter(request, "sysusers_chinesename", null), 
			englishName=ServletRequestUtils.getStringParameter(request, "sysusers_englishname", null),
			schoolName=ServletRequestUtils.getStringParameter(request, "appstudents_schoolname", null),
			className=ServletRequestUtils.getStringParameter(request, "appstudents_classname", null),
			sort=ServletRequestUtils.getStringParameter(request, "order[0][column]", null),
			dir=ServletRequestUtils.getStringParameter(request, "order[0][dir]", null);
		
		logger.info("this is [initscoretable.do] requset pram [userName = {"+userName+"}],[chineseName = {"+chineseName+"}]"
				+ ",[englistName = {"+englishName+"},[schoolName = {"+schoolName+"}],[className = {"+className+"}]"
				+ ",[sort = {"+sort+"}],[dir = {"+dir+"}]");
		
		Map<String, Object> parameters=new HashMap<String, Object>();
		if (userName!=null&&!userName.equals("")){
			parameters.put("userName", userName);
		}
		if (chineseName!=null&&!chineseName.equals("")){
			parameters.put("chineseName", chineseName);
		}
		if (englishName!=null&&!englishName.equals("")){
			parameters.put("englishName", englishName);
		}
		if (schoolName!=null&&!schoolName.equals("")){
			parameters.put("schoolName", schoolName);
		}
		if (className!=null&&!className.equals("")){
			parameters.put("sgs.groupName", className);
		}
		
		if (sort!=null&&!sort.equals("")){
			parameters.put("sort", sort);
		}
		if (dir!=null&&!dir.equals("")){
			parameters.put("order", dir);
		}
		
		Integer userId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
		
		Map<String,Object> returnData=appStudentsManagementService.searchDataForAjax(displayLength, displayStart, sEcho, parameters, userId, true);
		
		returnData.put("draw", sEcho);
		
		String result=JSONObject.fromObject(returnData).toString();
		logger.info("this is [initscoretable.do] data ["+result+"] ...");
		logger.info("this is [initscoretable.do] end ...");
		return result;
	}
	
	@RequestMapping(value = "/showstudentssorceinfo.do", method=RequestMethod.POST)
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="显示学生成绩信息")
	public String showStudentsSorceInfo(HttpServletRequest request, Integer userId) {
		logger.info("this is [showstudentssorceinfo.do] start ...");
		logger.info("this is [showstudentssorceinfo.do] userId ["+userId+"] ...");
		SysUsers user=null;
		List<SysProperties> examNames=null;
		List<Map<String, Object>> lstEN=new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> scores=new ArrayList<Map<String, Object>>();
		Map<String, Object> jsonTmp=null;
		try{
			if (userId!=null&&userId!=-1){
				logger.info("this is [showstudentssorceinfo.do] find user ...");
				user=sysUsersManagementService.get(userId);
				if (user.getStudentsScores()!=null&&!user.getStudentsScores().isEmpty()){
					for (AppStudentsScores score:user.getStudentsScores()){
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
			}
		}catch(Exception ex){
			logger.info("this is [showstudentssorceinfo.do] find user failed...");
			ex.printStackTrace();
		}
		
		logger.info("this is [showstudentssorceinfo.do] data ["+user+"] ...");

		request.setAttribute("sysuser", user);
		request.setAttribute("jsonScore", JSONArray.fromCollection(scores).toString());
		request.setAttribute("examNames", examNames);
		request.setAttribute("jsonExamNames", JSONArray.fromCollection(lstEN).toString());
		logger.info("this is [showstudentssorceinfo.do] end ...");
		return "appstudents/appstudentsscroeinfo";
	}
	
	@RequestMapping(value = "/savestudentscoresinfo.do", method=RequestMethod.POST)
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="保存学生成绩信息")
	public String saveStudentScoresInfo(HttpServletRequest request, Integer userId) {
		logger.info("this is [savestudentscoresinfo.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		SysUsers originalUser=null;
		if (userId!=null&&userId!=0){
			try {
				logger.info("this is [savestudentscoresinfo.do] loading data from request ...");
				logger.info("this is [savestudentscoresinfo.do] loading data from request userId ["+userId+"] ...");
				originalUser=sysUsersManagementService.get(userId);
				
				
				List<AppStudentsScores> studentScores=new ArrayList<AppStudentsScores>();
				AppStudentsScores score=null;
				
				Map<String,Object> parameters=new HashMap<String,Object>();
				parameters.put("propertyParentId", 4);//change here to find exam_name;
				List<SysProperties> examNames=sysPropertiesManagementService.findProperties(parameters);
				int examValue=-1;
				for (SysProperties property:examNames){
					examValue=ServletRequestUtils.getIntParameter(request, "property_"+property.getId(), -1);
					if (examValue!=-1){
						logger.info("this is [savestudentscoresinfo.do] loading data from request property_"+property.getId()+" ["+examValue+"] ...");
						score=new AppStudentsScores();
						score.setAppScoreProperty(property);
						score.setAppScoreUser(originalUser);
						score.setScoreValue(examValue);
						studentScores.add(score);
					}
				}
				if (originalUser.getStudentsScores()==null||originalUser.getStudentsScores().isEmpty()){
					originalUser.setStudentsScores(studentScores);
				}else{
					List<AppStudentsScores> currentScores=originalUser.getStudentsScores();
					for (AppStudentsScores currentScore:currentScores){
						for (AppStudentsScores studentScore:studentScores){
							if (studentScore.getAppScoreProperty().getId()==currentScore.getAppScoreProperty().getId()){
								currentScore.setScoreValue(studentScore.getScoreValue());
								break;
							}
						}
					}
					originalUser.setStudentsScores(currentScores);
				}
				
//				if (studentScores.isEmpty()&&(originalUser.getStudentsScores()==null||originalUser.getStudentsScores().isEmpty())){//没有学生成绩
//					result.put("status", 1);
//					result.put("data", "operation success!");
//				}else if (!studentScores.isEmpty()&&(originalUser.getStudentsScores()==null||originalUser.getStudentsScores().isEmpty())){//有新提交上来的成绩，但之前学生没有成绩
//					originalUser.setStudentsScores(studentScores);
//				}else if (!studentScores.isEmpty()&&originalUser.getStudentsScores()!=null&&!originalUser.getStudentsScores().isEmpty()){//有新提交上来的成绩，学生之前也有成绩
//					List<AppStudentsScores> currentScores=originalUser.getStudentsScores();
//					boolean isExist=false;
//					if (currentScores!=null&&!currentScores.isEmpty()){
//						for (AppStudentsScores currentScore:currentScores){
//							for (AppStudentsScores studentScore:studentScores){
//								if (studentScore.getAppScoreProperty().getId().equals(currentScore.getAppScoreProperty().getId())){
//									currentScore.setScoreValue(studentScore.getScoreValue());
//									isExist=true;
//									break;
//								}
//							}
//							if (!isExist){
//								currentScore.setScoreValue(null);
//								isExist=false;
//							}
//						}
//					}
//					originalUser.setStudentsScores(currentScores);
//				}else if (studentScores.isEmpty()&&originalUser.getStudentsScores()!=null&&!originalUser.getStudentsScores().isEmpty()){//没有新提交上来的成绩，学生之前也有成绩
//					List<AppStudentsScores> currentScores=originalUser.getStudentsScores();
//					for (AppStudentsScores currentScore:currentScores){
//						currentScore.setScoreValue(null);
//					}
//					originalUser.setStudentsScores(currentScores);
//				}
				if (result.isEmpty()){
					logger.info("this is [savestudentscoresinfo.do] saving user ...");
					sysUsersManagementService.save(originalUser);
					result.put("status", 1);
					result.put("data", "operation success!");
					logger.info("this is [savestudentscoresinfo.do] saving user done...");
				}
			} catch (Exception e) {
				logger.info("this is [savestudentinfo.do] save user error ...");
				result.put("status", 0);
				result.put("data", "save failed, try again!");
				e.printStackTrace();
			}
			logger.info("this is [savestudentinfo.do] show result ["+result+"] ...");
		}
		
		request.setAttribute("result", result.get("data"));
		request.setAttribute("sysuser", originalUser);
		return result.get("status").equals(1)?"forward:/appstudentsinfomanagement/appstudentsscorelist.do":"forward:/appstudentsinfomanagement/showstudentssorceinfo.do";
	}
}
