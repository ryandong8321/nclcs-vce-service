package org.ryan.nclcs.vce.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.ryan.nclcs.vce.annotation.SystemLogIsCheck;
import org.ryan.nclcs.vce.annotation.SystemUserLoginIsCheck;
import org.ryan.nclcs.vce.entity.AppStudentUploadAssignment;
import org.ryan.nclcs.vce.entity.AppTutorAppointmentAssignmentToStudent;
import org.ryan.nclcs.vce.entity.AppTutorAppointmentAssignmentToTutor;
import org.ryan.nclcs.vce.entity.SysGroups;
import org.ryan.nclcs.vce.entity.SysRoles;
import org.ryan.nclcs.vce.entity.SysUsers;
import org.ryan.nclcs.vce.service.appassignment.IAppStudentUploadAssignmentSerivce;
import org.ryan.nclcs.vce.service.appassignment.IAppTutorAppointmentToStudentService;
import org.ryan.nclcs.vce.service.appassignment.IAppTutorAppointmentToTutorService;
import org.ryan.nclcs.vce.service.sysroles.ISysRolesManagementService;
import org.ryan.nclcs.vce.service.sysusers.ISysUsersManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
@RequestMapping("/appassignmenttutormanagement")
public class AppAssignmentTutorManagementController {
	
	protected static Logger logger = LoggerFactory.getLogger(AppAssignmentTutorManagementController.class);
	
	@Autowired
	private IAppStudentUploadAssignmentSerivce appStudentUploadAssignmentSerivce;
	
	@Autowired
	private IAppTutorAppointmentToStudentService appTutorAppointmentToStudentService;
	
	@Autowired
	private IAppTutorAppointmentToTutorService appTutorAppointmentToTutorService;
	
	@Autowired
	private ISysUsersManagementService sysUsersManagementService;
	
	@Autowired
	private ISysRolesManagementService sysRolesManagementService;
	
	//local
//	protected final String _filePath="/usr/local/vce-uploadfiles";
	
	//server
	protected final String _filePath="/var/www/vce-uploadfiles";
	
	@RequestMapping(value = "/downloadassignmentlist.do")
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="查询下载学生作业列表")
	public String downloadAssignmentList(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		logger.info("this is [downloadassignmentlist.do] start ...");
		logger.info("this is [downloadassignmentlist.do] end ...");
		return "appassignment/tutordownloadlist";
	}
	
	@RequestMapping(value = "/initdownloadassignmentlist.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="初始化下载学生作业列表")
	public String initDownloadAssignmentList(HttpServletRequest request) {
		logger.info("this is [initdownloadassignmentlist.do] start ...");
		
		Map<String, Object> returnData=new HashMap<String, Object>();
		try {
			int displayLength = ServletRequestUtils.getIntParameter(request, "length", 0), //每页大小
				displayStart = ServletRequestUtils.getIntParameter(request, "start", 0), //起始值
				sEcho = ServletRequestUtils.getIntParameter(request, "draw", 0);
			
			
			logger.info("this is [initdownloadassignmentlist.do] requset pram [displayLength = "+displayLength+"],[displayStart = {"+displayStart+"}],[sEcho = {"+sEcho+"}]");
			
			String assignmentName=ServletRequestUtils.getStringParameter(request, "assignment_name", null), 
				startTime=ServletRequestUtils.getStringParameter(request, "upload_start_time", null), 
				endTime=ServletRequestUtils.getStringParameter(request, "upload_end_time", null),
				studentName=ServletRequestUtils.getStringParameter(request, "student_name", null),
				campusName=ServletRequestUtils.getStringParameter(request, "campus_name", null),
				className=ServletRequestUtils.getStringParameter(request, "class_name", null),
				sort=ServletRequestUtils.getStringParameter(request, "order[0][column]", null),
				dir=ServletRequestUtils.getStringParameter(request, "order[0][dir]", null);
			
			logger.info("this is [initdownloadassignmentlist.do] requset pram [assignmentName = {"+assignmentName+"}],[startTime = {"+startTime+"}]"
					+ ",[endTime = {"+endTime+"},[sort = {"+sort+"}],[dir = {"+dir+"}]");
			
			Map<String, Object> parameters=new HashMap<String, Object>();
			if (assignmentName!=null&&!assignmentName.equals("")){
				parameters.put("assignmentName", assignmentName);
			}
			if (campusName!=null&&!campusName.equals("")){
				parameters.put("campusName", campusName);
			}
			if (className!=null&&!className.equals("")){
				parameters.put("className", className);
			}
			if (studentName!=null&&!studentName.equals("")){
				parameters.put("studentName", studentName);
			}
			
			if (startTime!=null&&!startTime.equals("")){
				parameters.put("startTime", startTime);
			}
			
			if (endTime!=null&&!endTime.equals("")){
				parameters.put("endTime", endTime);
			}
			
			if (sort!=null&&!sort.equals("")){
				parameters.put("sort", sort);
			}else{
				parameters.put("sort", 6);
			}
			if (dir!=null&&!dir.equals("")){
				parameters.put("order", dir);
			}
			else{
				parameters.put("order", "desc");
			}
			
			Integer userId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
			SysUsers currentTutor=sysUsersManagementService.get(userId);
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
				
				returnData = appStudentUploadAssignmentSerivce.searchDataForAjax(displayLength, displayStart, sEcho, parameters, groupIds.toString());
				returnData.put("draw", sEcho);
			}
		} catch (Exception e) {
			logger.info("this is [initdownloadassignmentlist.do] occur exception ...");
			e.printStackTrace();
		}
		
		String result=JSONObject.fromObject(returnData).toString();
		logger.info("this is [initdownloadassignmentlist.do] data ["+result+"] ...");
		logger.info("this is [initdownloadassignmentlist.do] end ...");
		return result;
	}
	
	@RequestMapping(value = "/showstudentassignment.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="下载学生作业")
	public String showStudentAssignment(HttpServletRequest request, HttpServletResponse response, Integer sid) {
		logger.info("this is [showstudentassignment.do] start ...");
		try{
			if (sid!=-1){
				AppStudentUploadAssignment assignment=appStudentUploadAssignmentSerivce.get(sid);
				
				if (assignment.getDownloadTime()==null||assignment.getDownloadTime().equals("")){
					assignment.setDownloadTime(Calendar.getInstance().getTime());
					appStudentUploadAssignmentSerivce.save(assignment);
				}
//				if (assignment.getAssignmentToTutor()!=null){
//					AppTutorAppointmentAssignmentToTutor toTutor=assignment.getAssignmentToTutor();
//					toTutor.setDownloadTime(Calendar.getInstance().getTime());
//					assignment.setAssignmentToTutor(toTutor);
//				}
//				appStudentUploadAssignmentSerivce.save(assignment);
				
				if (assignment!=null){
					response.setCharacterEncoding("utf-8");
			        response.setContentType("multipart/form-data");
			        
			        String encodedfileName = null;
			        String agent = request.getHeader("USER-AGENT");
			        if(null != agent && -1 != agent.indexOf("MSIE")){//IE
			            encodedfileName = java.net.URLEncoder.encode(assignment.getFileName(),"UTF-8");
			        }else if(null != agent && -1 != agent.indexOf("Mozilla")){
			            encodedfileName = new String (assignment.getFileName().getBytes("UTF-8"),"iso-8859-1");
			        }else{
			            encodedfileName = java.net.URLEncoder.encode(assignment.getFileName(),"UTF-8");
			        }
			        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");
			        
			        InputStream inputStream=null;
					OutputStream os=null;
					try {
						inputStream = new FileInputStream(new File(_filePath+assignment.getFilePath()));
						os = response.getOutputStream();
						byte[] b = new byte[2048];
						int length;
						while ((length = inputStream.read(b)) > 0) {
						    os.write(b, 0, length);
						}
						os.flush();
					} catch (Exception e) {
						logger.info("this is [showstudentassignment.do] send file exception ...");
						e.printStackTrace();
					}finally{
						if (os!=null){
							os.close();
						}
						if (inputStream!=null){
							inputStream.close();
						}
					}
				}
			}
		}catch(Exception ex){
			logger.info("this is [showstudentassignment.do] get parameter error ...");
			ex.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/showtutorappointmentassignment.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="下载代审作业")
	public String showTutorAppointmentAssignment(HttpServletRequest request, HttpServletResponse response, Integer sid) {
		logger.info("this is [showtutorappointmentassignment.do] start ...");
		try{
			if (sid!=-1){
				AppTutorAppointmentAssignmentToTutor assignment=appTutorAppointmentToTutorService.get(sid);
				if (assignment.getDownloadTime()==null||assignment.getDownloadTime().equals("")){
					assignment.setDownloadTime(Calendar.getInstance().getTime());
					appTutorAppointmentToTutorService.save(assignment);
				}
				
				if (assignment!=null){
					response.setCharacterEncoding("utf-8");
			        response.setContentType("multipart/form-data");
			        
			        String encodedfileName = null;
			        String agent = request.getHeader("USER-AGENT");
			        if(null != agent && -1 != agent.indexOf("MSIE")){//IE
			            encodedfileName = java.net.URLEncoder.encode(assignment.getFileName(),"UTF-8");
			        }else if(null != agent && -1 != agent.indexOf("Mozilla")){
			            encodedfileName = new String (assignment.getFileName().getBytes("UTF-8"),"iso-8859-1");
			        }else{
			            encodedfileName = java.net.URLEncoder.encode(assignment.getFileName(),"UTF-8");
			        }
			        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");
			        
			        InputStream inputStream=null;
					OutputStream os=null;
					try {
						inputStream = new FileInputStream(new File(_filePath+assignment.getFilePath()));
						os = response.getOutputStream();
						byte[] b = new byte[2048];
						int length;
						while ((length = inputStream.read(b)) > 0) {
						    os.write(b, 0, length);
						}
						os.flush();
					} catch (Exception e) {
						logger.info("this is [showtutorappointmentassignment.do] send file exception ...");
						e.printStackTrace();
					}finally{
						if (os!=null){
							os.close();
						}
						if (inputStream!=null){
							inputStream.close();
						}
					}
				}
			}
		}catch(Exception ex){
			logger.info("this is [showtutorappointmentassignment.do] get parameter error ...");
			ex.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/showappointmentinfo.do", method=RequestMethod.POST)
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="显示指派老师状态信息")
	public String showAppointmentInfo(HttpServletRequest request, Integer assignmentId) {
		logger.info("this is [showappointmentinfo.do] start ...");
		logger.info("this is [showappointmentinfo.do] userId ["+assignmentId+"] ...");
		AppStudentUploadAssignment assignment=null;
		try{
			if (assignmentId!=null&&assignmentId!=-1){
				logger.info("this is [showappointmentinfo.do] find user ...");
//				user=sysUsersManagementService.get(userId);
				assignment=appStudentUploadAssignmentSerivce.get(assignmentId);
			}
		}catch(Exception ex){
			logger.info("this is [showappointmentinfo.do] find user failed...");
			ex.printStackTrace();
		}
		
		logger.info("this is [showappointmentinfo.do] data ["+assignment+"] ...");
		
		request.setAttribute("assignment", assignment);
		logger.info("this is [showappointmentinfo.do] end ...");
		return "appassignment/tutorappointmenttutor";
	}
	
	@RequestMapping(value = "/showappointmentstatusinfo.do", method=RequestMethod.POST)
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="显示指派作业状态信息")
	public String showAppointmentStatusInfo(HttpServletRequest request, Integer saId) {
		logger.info("this is [showappointmentstatusinfo.do] start ...");
		logger.info("this is [showappointmentstatusinfo.do] userId ["+saId+"] ...");
		AppStudentUploadAssignment assignment=null;
		try{
			if (saId!=null&&saId!=-1){
				logger.info("this is [showappointmentstatusinfo.do] find user ...");
//				user=sysUsersManagementService.get(userId);
				assignment=appStudentUploadAssignmentSerivce.get(saId);
			}
		}catch(Exception ex){
			logger.info("this is [showappointmentstatusinfo.do] find user failed...");
			ex.printStackTrace();
		}
		
		logger.info("this is [showappointmentstatusinfo.do] data ["+assignment+"] ...");
		
		request.setAttribute("assignment", assignment);
		logger.info("this is [showappointmentstatusinfo.do] end ...");
		return "appassignment/showappointmenttutorstatus";
	}
	
	@RequestMapping(value = "/findalltutor.do", method=RequestMethod.POST)
	@ResponseBody
	public String findAllTutor(HttpServletRequest request) {
		logger.info("this is [findalltutor.do] start ...");
		
		Map<String, Object> returnData=new HashMap<String, Object>();
		List<Map<String, Object>> lstData=new ArrayList<Map<String, Object>>();
		try {
			
			SysRoles role=sysRolesManagementService.get(4);
			if (role!=null&&role.getSysRolesUsers()!=null&&!role.getSysRolesUsers().isEmpty()){
				List<SysUsers> lstTutors=role.getSysRolesUsers();
				Map<String, Object> map=null;
				
				Integer currentUserId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
				for (SysUsers tutor:lstTutors){
					if (tutor.getId().equals(currentUserId)){
						continue;
					}
					map=new HashMap<String, Object>();
					map.put("id", tutor.getId());
					map.put("text", tutor.getChineseName()+"["+((tutor.getEmailAddress()==null||tutor.getEmailAddress().equals(""))?"EMAIL":tutor.getEmailAddress())+"]");
					lstData.add(map);
				}
			}
			returnData.put("status", 1);
			returnData.put("data", lstData);
			
		} catch (Exception e) {
			logger.info("this is [findalltutor.do] occur exception ...");
			returnData.put("status", 0);
			returnData.put("data", "");
			e.printStackTrace();
		}
		
		String result=JSONObject.fromObject(returnData).toString();
		logger.info("this is [findalltutor.do] data ["+result+"] ...");
		logger.info("this is [findalltutor.do] end ...");
		return result;
	}
	
	@RequestMapping(value = "/findallstudents.do", method=RequestMethod.POST)
	@ResponseBody
	public String findAllStudents(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [findallstudents.do] start ...");
		
		if (data!=null&&!data.equals("")){
			try {
				logger.info("this is [findallstudents.do] is decoding ...");
				data=URLDecoder.decode(data, "utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.info("this is [findallstudents.do] occur error when program decoding ...");
				e.printStackTrace();
			}
		}
		
		Map<String, Object> returnData=new HashMap<String, Object>();
		try {
			if(!"".equals(request.getSession().getAttribute("u_id"))){
				int currentUserId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
				int studentId=-1;
				try {
					JSONObject json=JSONObject.fromString(data);
					if (json.has("studentId")){
						studentId=json.getInt("studentId");
					}
				} catch (Exception e) {
					//e.printStackTrace();
				}
				logger.info("this is [findallstudents.do] studentId ["+studentId+"]");
				returnData.put("data", appStudentUploadAssignmentSerivce.searchUploadedAssignmentStudentsByTutorId(currentUserId, studentId));
				returnData.put("status", 1);
			}
		} catch (Exception e) {
			logger.info("this is [findallstudents.do] occur exception ...");
			returnData.put("status", 0);
			returnData.put("data", "");
			e.printStackTrace();
		}
		
		String result=JSONObject.fromObject(returnData).toString();
		logger.info("this is [findallstudents.do] data ["+result+"] ...");
		logger.info("this is [findallstudents.do] end ...");
		return result;
	}
	
	@RequestMapping(value = "/findallappointmenttutors.do", method=RequestMethod.POST)
	@ResponseBody
	public String findAllAppointmentTutors(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [findallappointmenttutors.do] start ...");
		
		if (data!=null&&!data.equals("")){
			try {
				logger.info("this is [findallappointmenttutors.do] is decoding ...");
				data=URLDecoder.decode(data, "utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.info("this is [findallappointmenttutors.do] occur error when program decoding ...");
				e.printStackTrace();
			}
		}
		
		Map<String, Object> returnData=new HashMap<String, Object>();
		try {
			if(!"".equals(request.getSession().getAttribute("u_id"))){
				int currentUserId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
				int targetTutorId=-1;
				try {
					JSONObject json=JSONObject.fromString(data);
					if (json.has("targetTutorId")){
						targetTutorId=json.getInt("targetTutorId");
					}
				} catch (Exception e) {
					//e.printStackTrace();
				}
				logger.info("this is [findallappointmenttutors.do] targetTutorId ["+targetTutorId+"]");
				returnData.put("data", appTutorAppointmentToTutorService.findTutorsForTutorToTutor(currentUserId, targetTutorId));
				returnData.put("status", 1);
			}
		} catch (Exception e) {
			logger.info("this is [findallappointmenttutors.do] occur exception ...");
			returnData.put("status", 0);
			returnData.put("data", "");
			e.printStackTrace();
		}
		
		String result=JSONObject.fromObject(returnData).toString();
		logger.info("this is [findallappointmenttutors.do] data ["+result+"] ...");
		logger.info("this is [findallappointmenttutors.do] end ...");
		return result;
	}
	
	@RequestMapping(value = "/appointmenttotutor.do", method=RequestMethod.POST)
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="保存指派教师代审作业")
	public String appointmentToTutor(HttpServletRequest request, Integer assignmentId, Integer toTutorId) {
		logger.info("this is [appointmenttotutor.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		
		try{
			if (assignmentId!=-1&&assignmentId!=-1){
				AppStudentUploadAssignment assignment=appStudentUploadAssignmentSerivce.get(assignmentId);
				assignment.setHasAppointment(1);
				
				AppTutorAppointmentAssignmentToTutor toTutor=new AppTutorAppointmentAssignmentToTutor();
				toTutor.setAssignmentName(assignment.getAssignmentName());
				toTutor.setFilePath(assignment.getFilePath());
				toTutor.setFileName(assignment.getFileName());
				
				toTutor.setStudent(assignment.getStudent());
				toTutor.setOriginalTutor(sysUsersManagementService.get(Integer.parseInt(""+request.getSession().getAttribute("u_id"))));
				toTutor.setTargetTutor(sysUsersManagementService.get(toTutorId));
				
				//result=appTutorAppointmentToTutorService.saveNewAppointment(assignment, toTutor);
				assignment.setAssignmentToTutor(toTutor);
				toTutor.setUploadAssignment(assignment);
				appStudentUploadAssignmentSerivce.save(assignment);
				
				result.put("status", 1);
				result.put("data", "作业已指派完成");
				
			}else{
				result.put("status", -1);
				result.put("data", "wrong assignment id");
			}
		}catch(Exception ex){
			logger.info("this is [appointmenttotutor.do] get parameter error ...");
			result.put("status", 0);
			result.put("data", "作业指派失败,请重试");
			ex.printStackTrace();
		}
		request.setAttribute("result", result.get("data"));
		return "forward:/appassignmenttutormanagement/downloadassignmentlist.do";
	}
	
	@RequestMapping(value = "/revokeappointmenttotutor.do", method=RequestMethod.POST)
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="收回指派教师代审作业")
	public String revokeAppointmentToTutor(HttpServletRequest request, Integer assignmentId) {
		logger.info("this is [revokeappointmenttotutor.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		
		try{
			if (assignmentId!=-1&&assignmentId!=-1){
				if (appTutorAppointmentToTutorService.revokeTutorToTutorFromStudent(assignmentId)){
					result.put("status", 1);
					result.put("data", "成功收回已指派作业");
				}else{
					result.put("status", 0);
					result.put("data", "收回已指派作业失败,请重试!");
				}
			}else{
				result.put("status", -1);
				result.put("data", "wrong assignment id");
			}
		}catch(Exception ex){
			logger.info("this is [appointmenttotutor.do] get parameter error ...");
			result.put("status", 0);
			result.put("data", "作业指派失败,请重试");
			ex.printStackTrace();
		}
		request.setAttribute("result", result.get("data"));
		return "forward:/appassignmenttutormanagement/downloadassignmentlist.do";
	}
	
	@RequestMapping(value = "/downloadappointmentassignmentlist.do")
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="查询下载代审作业列表")
	public String downloadAppointmentAssignmentlist(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		logger.info("this is [downloadassignmentlist.do] start ...");
		logger.info("this is [downloadassignmentlist.do] end ...");
		return "appassignment/appointmentdownloadlist";
	}
	
	@RequestMapping(value = "/initdownloadappointmentassignmentlist.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="初始化下载代审作业列表")
	public String initDownloadAppointmentAssignmentlist(HttpServletRequest request) {
		logger.info("this is [initdownloadassignmentlist.do] start ...");
		
		Map<String, Object> returnData=new HashMap<String, Object>();
		try {
			int displayLength = ServletRequestUtils.getIntParameter(request, "length", 0), //每页大小
				displayStart = ServletRequestUtils.getIntParameter(request, "start", 0), //起始值
				sEcho = ServletRequestUtils.getIntParameter(request, "draw", 0);
			
			
			logger.info("this is [initdownloadassignmentlist.do] requset pram [displayLength = "+displayLength+"],[displayStart = {"+displayStart+"}],[sEcho = {"+sEcho+"}]");
			
			String assignmentName=ServletRequestUtils.getStringParameter(request, "assignment_name", null), 
				startTime=ServletRequestUtils.getStringParameter(request, "upload_start_time", null), 
				endTime=ServletRequestUtils.getStringParameter(request, "upload_end_time", null),
				studentName=ServletRequestUtils.getStringParameter(request, "student_name", null),
				tutorName=ServletRequestUtils.getStringParameter(request, "tutor_name", null),
				sort=ServletRequestUtils.getStringParameter(request, "order[0][column]", null),
				dir=ServletRequestUtils.getStringParameter(request, "order[0][dir]", null);
			
			logger.info("this is [initdownloadassignmentlist.do] requset pram [assignmentName = {"+assignmentName+"}],[startTime = {"+startTime+"}]"
					+ ",[endTime = {"+endTime+"},[sort = {"+sort+"}],[dir = {"+dir+"}]");
			
			Map<String, Object> parameters=new HashMap<String, Object>();
			if (assignmentName!=null&&!assignmentName.equals("")){
				parameters.put("assignmentName", assignmentName);
			}
			if (tutorName!=null&&!tutorName.equals("")){
				parameters.put("originalTutor.chineseName", tutorName);
			}
			if (studentName!=null&&!studentName.equals("")){
				parameters.put("student.chineseName", studentName);
			}
			
			if (startTime!=null&&!startTime.equals("")){
				parameters.put("startTime", startTime);
			}
			
			if (endTime!=null&&!endTime.equals("")){
				parameters.put("endTime", endTime);
			}
			
			if (sort!=null&&!sort.equals("")){
				parameters.put("sort", sort);
			}else{
				parameters.put("sort", 5);
			}
			if (dir!=null&&!dir.equals("")){
				parameters.put("order", dir);
			}
			else{
				parameters.put("order", "desc");
			}
			
			Integer userId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
			SysUsers currentTutor=sysUsersManagementService.get(userId);
			if (currentTutor!=null&&currentTutor.getSysGroups()!=null){
				returnData = appTutorAppointmentToTutorService.searchDataForAjax(displayLength, displayStart, sEcho, parameters, userId);
				returnData.put("draw", sEcho);
			}
		} catch (Exception e) {
			logger.info("this is [initdownloadassignmentlist.do] occur exception ...");
			e.printStackTrace();
		}
		
		String result=JSONObject.fromObject(returnData).toString();
		logger.info("this is [initdownloadassignmentlist.do] data ["+result+"] ...");
		logger.info("this is [initdownloadassignmentlist.do] end ...");
		return result;
	}
	
	@RequestMapping(value = "/uploadassignmentlist.do")
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="查询上传已审作业列表")
	public String uploadAssignmentList(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		logger.info("this is [uploadassignmentlist.do] start ...");
		logger.info("this is [uploadassignmentlist.do] end ...");
		return "appassignment/tutoruploadlist";
	}
	
	@RequestMapping(value = "/inituploadassignmentlist.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="初始化上传已审作业列表")
	public String initUploadAssignmentList(HttpServletRequest request) {
		logger.info("this is [inituploadassignmentlist.do] start ...");
		
		Map<String, Object> returnData=new HashMap<String, Object>();
		try {
			int displayLength = ServletRequestUtils.getIntParameter(request, "length", 0), //每页大小
				displayStart = ServletRequestUtils.getIntParameter(request, "start", 0), //起始值
				sEcho = ServletRequestUtils.getIntParameter(request, "draw", 0);
			
			
			logger.info("this is [inituploadassignmentlist.do] requset pram [displayLength = "+displayLength+"],[displayStart = {"+displayStart+"}],[sEcho = {"+sEcho+"}]");
			
			String assignmentName=ServletRequestUtils.getStringParameter(request, "assignment_name", null), 
				startTime=ServletRequestUtils.getStringParameter(request, "order_date_from", null), 
				endTime=ServletRequestUtils.getStringParameter(request, "order_date_to", null),
				studentName=ServletRequestUtils.getStringParameter(request, "student_name", null),
				sort=ServletRequestUtils.getStringParameter(request, "order[0][column]", null),
				dir=ServletRequestUtils.getStringParameter(request, "order[0][dir]", null);
			
			logger.info("this is [inituploadassignmentlist.do] requset pram [assignmentName = {"+assignmentName+"}],[startTime = {"+startTime+"}]"
					+ ",[endTime = {"+endTime+"},[sort = {"+sort+"}],[dir = {"+dir+"}]");
			
			Map<String, Object> parameters=new HashMap<String, Object>();
			if (assignmentName!=null&&!assignmentName.equals("")){
				parameters.put("assignmentName", assignmentName);
			}
			if (studentName!=null&&!studentName.equals("")){
				parameters.put("student.chineseName", studentName);
			}
			
			if (startTime!=null&&!startTime.equals("")){
				parameters.put("startTime", startTime);
			}
			
			if (endTime!=null&&!endTime.equals("")){
				parameters.put("endTime", endTime);
			}
			
			if (sort!=null&&!sort.equals("")){
				parameters.put("sort", sort);
			}else{
				parameters.put("sort", 4);
			}
			if (dir!=null&&!dir.equals("")){
				parameters.put("order", dir);
			}
			else{
				parameters.put("order", "desc");
			}
			
			Integer userId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
			SysUsers currentTutor=sysUsersManagementService.get(userId);
			if (currentTutor!=null&&currentTutor.getSysGroups()!=null){
				returnData = appTutorAppointmentToStudentService.searchDataForAjax(displayLength, displayStart, sEcho, parameters, userId);
				returnData.put("draw", sEcho);
			}
		} catch (Exception e) {
			logger.info("this is [inituploadassignmentlist.do] occur exception ...");
			e.printStackTrace();
		}
		
		String result=JSONObject.fromObject(returnData).toString();
		logger.info("this is [inituploadassignmentlist.do] data ["+result+"] ...");
		logger.info("this is [inituploadassignmentlist.do] end ...");
		return result;
	}
	
	@RequestMapping(value = "/showuploadassignmentinfo.do", method=RequestMethod.POST)
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="显示上传已审作业信息")
	public String showUploadAssignmentinfo(HttpServletRequest request, Integer uploadAssignmentId) {
		logger.info("this is [showuploadassignmentinfo.do] start ...");
		logger.info("this is [showuploadassignmentinfo.do] uploadAssignmentId ["+uploadAssignmentId+"] ...");
		AppTutorAppointmentAssignmentToStudent uploadAssignment=null;
		try{
			if (uploadAssignmentId!=null&&uploadAssignmentId!=-1){
				logger.info("this is [showuploadassignmentinfo.do] find UploadAssignment ...");
				uploadAssignment=appTutorAppointmentToStudentService.get(uploadAssignmentId);
			}
		}catch(Exception ex){
			logger.info("this is [showuploadassignmentinfo.do] find user failed...");
			ex.printStackTrace();
		}
		
		logger.info("this is [showuploadassignmentinfo.do] data ["+uploadAssignment+"] ...");
		
		request.setAttribute("uploadassignment", uploadAssignment);
		logger.info("this is [showuploadassignmentinfo.do] end ...");
		return "appassignment/tutoruploadinfo";
	}
	
	@RequestMapping(value = "/saveuploadassignmentinfo.do", method=RequestMethod.POST)
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="保存上传已审作业信息")
	public String saveUploadAssignmentInfo(HttpServletRequest request, @RequestParam("files") CommonsMultipartFile[] uploadFile) {
		logger.info("this is [saveuploadassignmentinfo.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		
		logger.info("this is [saveuploadassignmentinfo.do] get page parameters ...");
		Integer uploadAssignmentId=-1,currentTutorId=-1,toStudentId=-1;
		String uploadAssignmentName=null;
		AppTutorAppointmentAssignmentToStudent uploadAssignment=null;
		try {
			uploadAssignmentId=ServletRequestUtils.getIntParameter(request, "uploadAssignmentId", -1);
			uploadAssignmentName=ServletRequestUtils.getRequiredStringParameter(request, "assignmentName");
			toStudentId=ServletRequestUtils.getIntParameter(request, "toStudentId", -1);
			currentTutorId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
		} catch (ServletRequestBindingException e1) {
			logger.info("this is [saveuploadassignmentinfo.do] get page parameters occur exception...");
			e1.printStackTrace();
		}
		
		if (currentTutorId!=-1){
			if (uploadAssignmentId==-1){
				uploadAssignment=new AppTutorAppointmentAssignmentToStudent();
			}else{
				uploadAssignment=appTutorAppointmentToStudentService.get(uploadAssignmentId);
			}
			
			Calendar now=new GregorianCalendar();
			String year="/"+now.get(Calendar.YEAR);
			String month=(now.get(Calendar.MONTH)+1)>9?"/"+(now.get(Calendar.MONTH)+1):"/0"+(now.get(Calendar.MONTH)+1);
			String day=now.get(Calendar.DAY_OF_MONTH)>9?"/"+now.get(Calendar.DAY_OF_MONTH):"/0"+now.get(Calendar.DAY_OF_MONTH);
			
			String _localPath=year;
			File directory=new File(_filePath+_localPath);
			if (!directory.exists()){
				directory.mkdir();
				logger.info("this is [saveuploadassignmentinfo.do] create directory ["+directory+"] success...");
			}
			
			_localPath+=month;
			directory=new File(_filePath+_localPath);
			if (!directory.exists()){
				directory.mkdir();
				logger.info("this is [saveuploadassignmentinfo.do] create directory ["+directory+"] success...");
			}
			
			_localPath+=day;
			directory=new File(_filePath+_localPath);
			if (!directory.exists()){
				directory.mkdir();
				logger.info("this is [saveuploadassignmentinfo.do] create directory ["+directory+"] success...");
			}
			
			_localPath+=File.separator+"tutor_"+currentTutorId;
			directory=new File(_filePath+_localPath);
			logger.info("this is [saveuploadassignmentinfo.do] show directory name ["+directory+"]");
			if (!directory.exists()){
				directory.mkdir();
				logger.info("this is [saveuploadassignmentinfo.do] create directory ["+directory+"] success...");
			}
			
			File file=null;
			String strFileName=null;
			String strSuffix=null;
//			String strPrefix=null;
			int dotPosition=-1;
			
			for (int i=0;i<uploadFile.length;i++){
				if (!uploadFile[i].isEmpty()){
//					int idx=1;
					
					strFileName=uploadFile[i].getOriginalFilename();
					logger.info("this is [saveuploadassignmentinfo.do] show file name ["+strFileName+"]");
					
					dotPosition=strFileName.lastIndexOf(".");
					strSuffix=strFileName.substring(dotPosition);
//					strPrefix=strFileName.substring(0, dotPosition);
					
					if (strSuffix!=null&&!strSuffix.equals("")&&(strSuffix.equals(".doc")||strSuffix.equals(".docx")||strSuffix.equals(".pdf"))){
//						file=new File(directory+File.separator+strFileName);
						file=new File(directory+File.separator+Calendar.getInstance().getTimeInMillis());
						
						while(file.exists()){
							logger.info("this is [saveuploadassignmentinfo.do] file ["+file.getName()+"] exist...");
							file=new File(directory+File.separator+Calendar.getInstance().getTimeInMillis());
							logger.info("this is [saveuploadassignmentinfo.do] change file name ["+file.getName()+"]...");
						}
						if (!file.exists()){
							logger.info("this is [saveuploadassignmentinfo.do] file ["+file.getAbsolutePath()+"] do not exist...");
							try {
								file.createNewFile();
								logger.info("this is [saveuploadassignmentinfo.do] create file ["+file.getAbsolutePath()+"] success...");
								uploadFile[i].transferTo(file);
								logger.info("this is [saveuploadassignmentinfo.do] file ["+file.getAbsolutePath()+"] transfer...");
							} catch (IOException e) {
								logger.info("this is [saveuploadassignmentinfo.do] create file ["+file.getAbsolutePath()+"] failed...");
								e.printStackTrace();
								result.put("status", 0);
								result.put("data", "save failed, try again!");
							}
						}
						
						if (result.isEmpty()){
							String assignmentName=(uploadAssignmentName==null||uploadAssignmentName.equals(""))?strFileName:uploadAssignmentName;
							
							uploadAssignment.setAssignmentName(StringEscapeUtils.escapeJavaScript(StringEscapeUtils.escapeHtml(assignmentName)));
//							uploadAssignment.setFilePath(file.getAbsolutePath());
							uploadAssignment.setFilePath(_localPath+File.separator+file.getName());
							uploadAssignment.setFileName(strFileName);
							
							uploadAssignment.setTargetStudent(sysUsersManagementService.get(toStudentId));
							uploadAssignment.setSourceTutor(sysUsersManagementService.get(currentTutorId));
							
							logger.info("this is [saveuploadassignmentinfo.do] is saving ...");
							appTutorAppointmentToStudentService.save(uploadAssignment);
							
							result.put("status", 1);
							result.put("data", "operation success!");
							logger.info("this is [saveuploadassignmentinfo.do] save uploadAssignment done ...");
						}
					}else{
						result.put("status", 0);
						result.put("data", "wrong file suffix");
						logger.info("this is [saveuploadassignmentinfo.do] wrong file suffix ...");
					}
				}else{
					logger.info("this is [saveuploadassignmentinfo.do] save uploadAssignment error ...");
					result.put("status", 0);
					result.put("data", "save failed, try again!");
				}
			}
		}else{
			logger.info("this is [saveuploadassignmentinfo.do] save uploadAssignment error ...");
			result.put("status", 0);
			result.put("data", "save failed, try again!");
		}
		
		logger.info("this is [saveuploadassignmentinfo.do] show result ["+result+"] ...");
		request.setAttribute("result", result.get("data"));
		request.setAttribute("uploadassignment", uploadAssignment);
		return result.get("status").equals(1)?"forward:/appassignmenttutormanagement/uploadassignmentlist.do":"forward:/appassignmenttutormanagement/showuploadassignmentinfo.do";
	}
	
	@RequestMapping(value = "/uploadappointmentassignmentlist.do")
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="查询上传代审作业列表")
	public String uploadAppointmentAssignmentList(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		logger.info("this is [uploadappointmentassignmentlist.do] start ...");
		logger.info("this is [uploadappointmentassignmentlist.do] end ...");
		return "appassignment/tutorappointmentuploadlist";
	}
	
	@RequestMapping(value = "/inituploadappointmentassignmentlist.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="初始化上传代审作业列表")
	public String initUploadAppointmentAssignmentList(HttpServletRequest request) {
		logger.info("this is [inituploadappointmentassignmentlist.do] start ...");
		
		Map<String, Object> returnData=new HashMap<String, Object>();
		try {
			int displayLength = ServletRequestUtils.getIntParameter(request, "length", 0), //每页大小
				displayStart = ServletRequestUtils.getIntParameter(request, "start", 0), //起始值
				sEcho = ServletRequestUtils.getIntParameter(request, "draw", 0);
			
			
			logger.info("this is [inituploadappointmentassignmentlist.do] requset pram [displayLength = "+displayLength+"],[displayStart = {"+displayStart+"}],[sEcho = {"+sEcho+"}]");
			
			String assignmentName=ServletRequestUtils.getStringParameter(request, "assignment_name", null), 
				startTime=ServletRequestUtils.getStringParameter(request, "order_date_from", null), 
				endTime=ServletRequestUtils.getStringParameter(request, "order_date_to", null),
				studentName=ServletRequestUtils.getStringParameter(request, "student_name", null),
				sort=ServletRequestUtils.getStringParameter(request, "order[0][column]", null),
				dir=ServletRequestUtils.getStringParameter(request, "order[0][dir]", null);
			
			logger.info("this is [inituploadappointmentassignmentlist.do] requset pram [assignmentName = {"+assignmentName+"}],[startTime = {"+startTime+"}]"
					+ ",[endTime = {"+endTime+"},[sort = {"+sort+"}],[dir = {"+dir+"}]");
			
			Map<String, Object> parameters=new HashMap<String, Object>();
			if (assignmentName!=null&&!assignmentName.equals("")){
				parameters.put("assignmentName", assignmentName);
			}
			if (studentName!=null&&!studentName.equals("")){
				parameters.put("student.chineseName", studentName);
			}
			
			if (startTime!=null&&!startTime.equals("")){
				parameters.put("startTime", startTime);
			}
			
			if (endTime!=null&&!endTime.equals("")){
				parameters.put("endTime", endTime);
			}
			
			if (sort!=null&&!sort.equals("")){
				parameters.put("sort", sort);
			}else{
				parameters.put("sort", 4);
			}
			if (dir!=null&&!dir.equals("")){
				parameters.put("order", dir);
			}
			else{
				parameters.put("order", "desc");
			}
			
			Integer userId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
			SysUsers currentTutor=sysUsersManagementService.get(userId);
			if (currentTutor!=null&&currentTutor.getSysGroups()!=null){
				returnData = appTutorAppointmentToTutorService.searchDataForAjaxToTutor(displayLength, displayStart, sEcho, parameters, userId);
				returnData.put("draw", sEcho);
			}
		} catch (Exception e) {
			logger.info("this is [inituploadappointmentassignmentlist.do] occur exception ...");
			e.printStackTrace();
		}
		
		String result=JSONObject.fromObject(returnData).toString();
		logger.info("this is [inituploadappointmentassignmentlist.do] data ["+result+"] ...");
		logger.info("this is [inituploadappointmentassignmentlist.do] end ...");
		return result;
	}
	
	@RequestMapping(value = "/showuploadappointmentassignmentinfo.do", method=RequestMethod.POST)
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="显示上传代审作业信息")
	public String showUploadAppointmentAssignmentinfo(HttpServletRequest request, Integer uploadAssignmentId) {
		logger.info("this is [showuploadappointmentassignmentinfo.do] start ...");
		logger.info("this is [showuploadappointmentassignmentinfo.do] uploadAssignmentId ["+uploadAssignmentId+"] ...");
		AppTutorAppointmentAssignmentToStudent uploadAssignment=null;
		try{
			if (uploadAssignmentId!=null&&uploadAssignmentId!=-1){
				logger.info("this is [showuploadappointmentassignmentinfo.do] find UploadAssignment ...");
				uploadAssignment=appTutorAppointmentToStudentService.get(uploadAssignmentId);
			}
		}catch(Exception ex){
			logger.info("this is [showuploadappointmentassignmentinfo.do] find user failed...");
			ex.printStackTrace();
		}
		
		logger.info("this is [showuploadappointmentassignmentinfo.do] data ["+uploadAssignment+"] ...");
		
		request.setAttribute("uploadassignment", uploadAssignment);
		logger.info("this is [showuploadappointmentassignmentinfo.do] end ...");
		return "appassignment/tutorappointmentuploadinfo";
	}
	
	@RequestMapping(value = "/saveuploadappointmentassignmentinfo.do", method=RequestMethod.POST)
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="保存上传代审作业信息")
	public String saveUploadAppointmentAssignmentInfo(HttpServletRequest request, @RequestParam("files") CommonsMultipartFile[] uploadFile) {
		logger.info("this is [saveuploadappointmentassignmentinfo.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		
		logger.info("this is [saveuploadappointmentassignmentinfo.do] get page parameters ...");
		Integer uploadAssignmentId=-1,currentTutorId=-1,toTutorId=-1;
		String uploadAssignmentName=null;
		AppTutorAppointmentAssignmentToTutor uploadAssignment=null;
		try {
			uploadAssignmentId=ServletRequestUtils.getIntParameter(request, "uploadAssignmentId", -1);
			uploadAssignmentName=ServletRequestUtils.getRequiredStringParameter(request, "assignmentName");
			toTutorId=ServletRequestUtils.getIntParameter(request, "toTutorId", -1);
			currentTutorId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
		} catch (ServletRequestBindingException e1) {
			logger.info("this is [saveuploadappointmentassignmentinfo.do] get page parameters occur exception...");
			e1.printStackTrace();
		}
		
		if (currentTutorId!=-1){
			if (uploadAssignmentId==-1){
				uploadAssignment=new AppTutorAppointmentAssignmentToTutor();
			}else{
				uploadAssignment=appTutorAppointmentToTutorService.get(uploadAssignmentId);
			}
			
			Calendar now=new GregorianCalendar();
			String year="/"+now.get(Calendar.YEAR);
			String month=(now.get(Calendar.MONTH)+1)>9?"/"+(now.get(Calendar.MONTH)+1):"/0"+(now.get(Calendar.MONTH)+1);
			String day=now.get(Calendar.DAY_OF_MONTH)>9?"/"+now.get(Calendar.DAY_OF_MONTH):"/0"+now.get(Calendar.DAY_OF_MONTH);
			
			String _localPath=year;
			File directory=new File(_filePath+_localPath);
			if (!directory.exists()){
				directory.mkdir();
				logger.info("this is [saveuploadassignmentinfo.do] create directory ["+directory+"] success...");
			}
			
			_localPath+=month;
			directory=new File(_filePath+_localPath);
			if (!directory.exists()){
				directory.mkdir();
				logger.info("this is [saveuploadassignmentinfo.do] create directory ["+directory+"] success...");
			}
			
			_localPath+=day;
			directory=new File(_filePath+_localPath);
			if (!directory.exists()){
				directory.mkdir();
				logger.info("this is [saveuploadassignmentinfo.do] create directory ["+directory+"] success...");
			}
			
			_localPath+=File.separator+"tutor_"+currentTutorId;
			directory=new File(_filePath+_localPath);
			logger.info("this is [saveuploadassignmentinfo.do] show directory name ["+directory+"]");
			if (!directory.exists()){
				directory.mkdir();
				logger.info("this is [saveuploadappointmentassignmentinfo.do] create directory ["+directory+"] success...");
			}
			
			File file=null;
			String strFileName=null;
			String strSuffix=null;
//			String strPrefix=null;
			int dotPosition=-1;
			
			for (int i=0;i<uploadFile.length;i++){
				if (!uploadFile[i].isEmpty()){
//					int idx=1;
					
					strFileName=uploadFile[i].getOriginalFilename();
					logger.info("this is [saveuploadappointmentassignmentinfo.do] show file name ["+strFileName+"]");
					
					dotPosition=strFileName.lastIndexOf(".");
					strSuffix=strFileName.substring(dotPosition);
//					strPrefix=strFileName.substring(0, dotPosition);
					
					if (strSuffix!=null&&!strSuffix.equals("")&&(strSuffix.equals(".doc")||strSuffix.equals(".docx")||strSuffix.equals(".pdf"))){
//						file=new File(directory+File.separator+strFileName);
						file=new File(directory+File.separator+Calendar.getInstance().getTimeInMillis());
						
						while(file.exists()){
							logger.info("this is [saveuploadappointmentassignmentinfo.do] file ["+file.getName()+"] exist...");
//							file=new File(directory+File.separator+strPrefix+"("+(idx++)+")"+strSuffix);
							file=new File(directory+File.separator+Calendar.getInstance().getTimeInMillis());
							logger.info("this is [saveuploadappointmentassignmentinfo.do] change file name ["+file.getName()+"]...");
						}
						
						if (!file.exists()){
							logger.info("this is [saveuploadappointmentassignmentinfo.do] file ["+file.getAbsolutePath()+"] do not exist...");
							try {
								file.createNewFile();
								logger.info("this is [saveuploadappointmentassignmentinfo.do] create file ["+file.getAbsolutePath()+"] success...");
								uploadFile[i].transferTo(file);
								logger.info("this is [saveuploadappointmentassignmentinfo.do] file ["+file.getAbsolutePath()+"] transfer...");
							} catch (IOException e) {
								logger.info("this is [saveuploadappointmentassignmentinfo.do] create file ["+file.getAbsolutePath()+"] failed...");
								e.printStackTrace();
								result.put("status", 0);
								result.put("data", "save failed, try again!");
							}
						}
						
						if (result.isEmpty()){
							String assignmentName=(uploadAssignmentName==null||uploadAssignmentName.equals(""))?strFileName:uploadAssignmentName;
							uploadAssignment.setAssignmentName(StringEscapeUtils.escapeJavaScript(StringEscapeUtils.escapeHtml(assignmentName)));
//							uploadAssignment.setFilePath(file.getAbsolutePath());
							uploadAssignment.setFilePath(_localPath+File.separator+file.getName());
							uploadAssignment.setFileName(strFileName);
							
							uploadAssignment.setTargetTutor(sysUsersManagementService.get(toTutorId));
							uploadAssignment.setOriginalTutor(sysUsersManagementService.get(currentTutorId));
							
							logger.info("this is [saveuploadappointmentassignmentinfo.do] is saving ...");
							appTutorAppointmentToTutorService.save(uploadAssignment);
							
							result.put("status", 1);
							result.put("data", "operation success!");
							logger.info("this is [saveuploadappointmentassignmentinfo.do] save uploadAssignment done ...");
						}
					}else{
						result.put("status", 0);
						result.put("data", "wrong file suffix");
						logger.info("this is [saveuploadappointmentassignmentinfo.do] wrong file suffix ...");
					}
				}else{
					logger.info("this is [saveuploadappointmentassignmentinfo.do] save uploadAssignment error ...");
					result.put("status", 0);
					result.put("data", "save failed, try again!");
				}
			}
		}else{
			logger.info("this is [saveuploadappointmentassignmentinfo.do] save uploadAssignment error ...");
			result.put("status", 0);
			result.put("data", "save failed, try again!");
		}
		
		logger.info("this is [saveuploadappointmentassignmentinfo.do] show result ["+result+"] ...");
		request.setAttribute("result", result.get("data"));
		request.setAttribute("uploadassignment", uploadAssignment);
		return result.get("status").equals(1)?"forward:/appassignmenttutormanagement/uploadappointmentassignmentlist.do":"forward:/appassignmenttutormanagement/saveuploadappointmentassignmentinfo.do";
	}
	
	@RequestMapping(value = "/deletemultipleappointmenttostudent.do", method=RequestMethod.POST)
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="批量收回上传已审作业")
	public String deleteMultipleAssignmentToStudent(HttpServletRequest request, String deleteIds) {
		logger.info("this is [deletemultipleappointmenttostudent.do] start ...");
		logger.info("this is [deletemultipleappointmenttostudent.do] values [deleteId={"+deleteIds+"}]");
		Map<String, Object> result=new HashMap<String, Object>();
		if (deleteIds!=null&&!deleteIds.equals("")){
			try {
				logger.info("this is [deletemultipleappointmenttostudent.do] ready to delete ...");
				if (appTutorAppointmentToStudentService.revokeTutorToStudent(deleteIds)){
					result.put("status", 1);
					result.put("data", "成功收回已上传的作业");
				}else{
					result.put("status", 0);
					result.put("data", "收回已上传的作业失败,请重试!");
				}
				logger.info("this is [deletemultipleappointmenttostudent.do] to delete done...");
			} catch (Exception e) {
				logger.info("this is [deletemultipleappointmenttostudent.do] to trough exception when delete ...");
				result.put("status", 0);
				result.put("data", "delete failed, try again!");
				e.printStackTrace();
			}
		}
		
		logger.info("this is [deletemultipleappointmenttostudent.do] show result ["+result+"] ...");
		request.setAttribute("result", result.get("data"));
		return "forward:/appassignmenttutormanagement/uploadassignmentlist.do";
	}
	
	@RequestMapping(value = "/deletemultipleassignment.do", method=RequestMethod.POST)
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="批量收回上传代审作业")
	public String deleteMultipleAssignmentToTutor(HttpServletRequest request, String deleteIds) {
		logger.info("this is [deletemultipleassignment.do] start ...");
		logger.info("this is [deletemultipleassignment.do] values [deleteId={"+deleteIds+"}]");
		Map<String, Object> result=new HashMap<String, Object>();
		if (deleteIds!=null&&!deleteIds.equals("")){
			try {
				logger.info("this is [deletemultipleassignment.do] ready to delete ...");
				if (appTutorAppointmentToTutorService.revokeTutorToTutor(deleteIds)){
					result.put("status", 1);
					result.put("data", "成功收回已上传的作业");
				}else{
					result.put("status", 0);
					result.put("data", "收回已上传的作业失败,请重试!");
				}
				logger.info("this is [deletemultipleassignment.do] to delete done...");
			} catch (Exception e) {
				logger.info("this is [deletemultipleassignment.do] to trough exception when delete ...");
				result.put("status", 0);
				result.put("data", "delete failed, try again!");
				e.printStackTrace();
			}
		}
		
		logger.info("this is [deletemultipleassignment.do] show result ["+result+"] ...");
		request.setAttribute("result", result.get("data"));
		return "forward:/appassignmenttutormanagement/uploadappointmentassignmentlist.do";
	}

}
