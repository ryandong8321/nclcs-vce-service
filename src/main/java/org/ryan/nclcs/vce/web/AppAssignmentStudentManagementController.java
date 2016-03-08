package org.ryan.nclcs.vce.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ryan.nclcs.vce.annotation.SystemLogIsCheck;
import org.ryan.nclcs.vce.annotation.SystemUserLoginIsCheck;
import org.ryan.nclcs.vce.entity.AppStudentUploadAssignment;
import org.ryan.nclcs.vce.entity.AppTutorAppointmentAssignmentToStudent;
import org.ryan.nclcs.vce.entity.SysGroups;
import org.ryan.nclcs.vce.entity.SysUsers;
import org.ryan.nclcs.vce.service.appassignment.IAppStudentUploadAssignmentSerivce;
import org.ryan.nclcs.vce.service.appassignment.IAppTutorAppointmentToStudentService;
import org.ryan.nclcs.vce.service.sysusers.ISysUsersManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/appassignmentstudentmanagement")
public class AppAssignmentStudentManagementController {
	
	protected static Logger logger = LoggerFactory.getLogger(AppAssignmentStudentManagementController.class);
	
	@Autowired
	private IAppStudentUploadAssignmentSerivce appStudentUploadAssignmentSerivce;
	
	@Autowired
	private IAppTutorAppointmentToStudentService appTutorAppointmentToStudentService;
	
	@Autowired
	private ISysUsersManagementService sysUsersManagementService;
	
//	@Autowired
//	private ISysGroupsManagementService sysGroupsManagementService;
	
//	protected final String _filePath="/usr/local/vce-uploadfiles";
	protected final String _filePath="/usr/local/www/vce-uploadfiles";
	
	@RequestMapping(value = "/uploadassignmentlist.do")
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="查询学生上传作业列表")
	public String uploadAssignmentList(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		logger.info("this is [uploadassignmentlist.do] start ...");
		logger.info("this is [uploadassignmentlist.do] end ...");
		return "appassignment/studentuploadlist";
	}
	
	@RequestMapping(value = "/inituploadassignmentlist.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="查询学生上传作业列表")
	public String initUploadAssignmentList(HttpServletRequest request) {
		logger.info("this is [inituploadassignmentlist.do] start ...");
		
		int displayLength = ServletRequestUtils.getIntParameter(request, "length", 0), //每页大小
			displayStart = ServletRequestUtils.getIntParameter(request, "start", 0), //起始值
			sEcho = ServletRequestUtils.getIntParameter(request, "draw", 0);
		
		
		logger.info("this is [inituploadassignmentlist.do] requset pram [displayLength = "+displayLength+"],[displayStart = {"+displayStart+"}],[sEcho = {"+sEcho+"}]");
		
		String assignmentName=ServletRequestUtils.getStringParameter(request, "assignment_name", null), 
			startTime=ServletRequestUtils.getStringParameter(request, "upload_start_time", null), 
			endTime=ServletRequestUtils.getStringParameter(request, "upload_end_time", null),
			sort=ServletRequestUtils.getStringParameter(request, "order[0][column]", null),
			dir=ServletRequestUtils.getStringParameter(request, "order[0][dir]", null);
		
		logger.info("this is [inituploadassignmentlist.do] requset pram [assignmentName = {"+assignmentName+"}],[startTime = {"+startTime+"}]"
				+ ",[endTime = {"+endTime+"},[sort = {"+sort+"}],[dir = {"+dir+"}]");
		
		Map<String, Object> parameters=new HashMap<String, Object>();
		if (assignmentName!=null&&!assignmentName.equals("")){
			parameters.put("assignmentName", assignmentName);
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
			parameters.put("sort", 3);
		}
		if (dir!=null&&!dir.equals("")){
			parameters.put("order", dir);
		}
		else{
			parameters.put("order", "desc");
		}
		
		Integer userId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
		logger.info("this is [inituploadassignmentlist.do] user ["+userId+"]");
		
		Map<String,Object> returnData=appStudentUploadAssignmentSerivce.searchDataForAjax(displayLength, displayStart, sEcho, parameters, userId);
		
		returnData.put("draw", sEcho);
		
		String result=JSONObject.fromObject(returnData).toString();
		logger.info("this is [inituploadassignmentlist.do] data ["+result+"] ...");
		logger.info("this is [inituploadassignmentlist.do] end ...");
		return result;
	}
	
	@RequestMapping(value = "/showuploadassignmentinfo.do", method=RequestMethod.POST)
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="显示学生上传作业信息")
	public String showUploadAssignmentinfo(HttpServletRequest request, Integer uploadAssignmentId) {
		logger.info("this is [showuploadassignmentinfo.do] start ...");
		logger.info("this is [showuploadassignmentinfo.do] uploadAssignmentId ["+uploadAssignmentId+"] ...");
		AppStudentUploadAssignment uploadAssignment=null;
		try{
			if (uploadAssignmentId!=null&&uploadAssignmentId!=-1){
				logger.info("this is [showuploadassignmentinfo.do] find UploadAssignment ...");
				uploadAssignment=appStudentUploadAssignmentSerivce.get(uploadAssignmentId);
			}
			
			logger.info("this is [showuploadassignmentinfo.do] find currentUserId...");
			int currentStudentId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
			logger.info("this is [showuploadassignmentinfo.do] currentUserId ["+currentStudentId+"]...");
			
			logger.info("this is [showuploadassignmentinfo.do] find student...");
			SysUsers student=sysUsersManagementService.get(currentStudentId);
			logger.info("this is [showuploadassignmentinfo.do] get student class...");
			if (student.getSysGroups()!=null&&!student.getSysGroups().isEmpty()){
				SysUsers tutor=sysUsersManagementService.findATutorFromGroup(student.getSysGroups().get(0).getId());
				logger.info("this is [showuploadassignmentinfo.do] set tutor name ["+tutor.getChineseName()+"]...");
				request.setAttribute("tutorname", tutor.getChineseName());
			}
		}catch(Exception ex){
			logger.info("this is [showuploadassignmentinfo.do] occur some problems...");
			ex.printStackTrace();
		}
		logger.info("this is [showuploadassignmentinfo.do] data ["+uploadAssignment+"] ...");
		request.setAttribute("uploadassignment", uploadAssignment);
		logger.info("this is [showuploadassignmentinfo.do] end ...");
		return "appassignment/studentuploadinfo";
	}
	
	@RequestMapping(value = "/saveuploadassignmentinfo.do", method=RequestMethod.POST)
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="保存学生上传作业信息")
	public String saveUploadAssignmentInfo(HttpServletRequest request, @RequestParam("files") CommonsMultipartFile[] uploadFile) {
		logger.info("this is [saveuploadassignmentinfo.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		
		logger.info("this is [saveuploadassignmentinfo.do] get page parameters ...");
		Integer uploadAssignmentId=-1,currentStudentId=-1;
		String uploadAssignmentName=null;
		AppStudentUploadAssignment uploadAssignment=null;
		try {
			uploadAssignmentId=ServletRequestUtils.getIntParameter(request, "uploadAssignmentId", -1);
			uploadAssignmentName=ServletRequestUtils.getRequiredStringParameter(request, "assignmentName");
			currentStudentId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
		} catch (ServletRequestBindingException e1) {
			logger.info("this is [saveuploadassignmentinfo.do] get page parameters occur exception...");
			e1.printStackTrace();
		}
		
		if (currentStudentId!=-1){
			if (uploadAssignmentId==-1){
				uploadAssignment=new AppStudentUploadAssignment();
			}else{
				uploadAssignment=appStudentUploadAssignmentSerivce.get(uploadAssignmentId);
			}
			
//			String _filePath=request.getServletContext().getRealPath("");
			
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
			
			_localPath+=File.separator+"student_"+currentStudentId;
			directory=new File(_filePath+_localPath);
			logger.info("this is [saveuploadassignmentinfo.do] show directory name ["+directory+"]");
			if (!directory.exists()){
				directory.mkdir();
				logger.info("this is [saveuploadassignmentinfo.do] create directory ["+directory+"] success...");
			}
			
			File file=null;
			String strFileName=null;
			String strSuffix=null;
			String strPrefix=null;
			int dotPosition=-1;
			
			for (int i=0;i<uploadFile.length;i++){
				if (!uploadFile[i].isEmpty()){
					int idx=1;
					
					strFileName=uploadFile[i].getOriginalFilename();
					logger.info("this is [saveuploadassignmentinfo.do] show file name ["+strFileName+"]");
					
					dotPosition=strFileName.lastIndexOf(".");
					strSuffix=strFileName.substring(dotPosition);
					strPrefix=strFileName.substring(0, dotPosition);
					
					if (strSuffix!=null&&!strSuffix.equals("")&&(strSuffix.equals(".doc")||strSuffix.equals(".docx"))){
						file=new File(directory+File.separator+strFileName);
						
						while(file.exists()){
							logger.info("this is [saveuploadassignmentinfo.do] file ["+file+"] exist...");
							file=new File(directory+File.separator+strPrefix+"("+(idx++)+")"+strSuffix);
							logger.info("this is [saveuploadassignmentinfo.do] change file name ["+file+"]...");
						}
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
						}
					}
					
					uploadAssignment.setAssignmentName((uploadAssignmentName==null||uploadAssignmentName.equals(""))?strFileName:uploadAssignmentName);
//					uploadAssignment.setFilePath(file.getAbsolutePath());
					uploadAssignment.setFilePath(_localPath+File.separator+file.getName());
					uploadAssignment.setFileName(strFileName);
					
					SysUsers student=sysUsersManagementService.get(currentStudentId);
					uploadAssignment.setStudent(student);
					SysGroups classGroup=student.getSysGroups().get(0);
					uploadAssignment.setTutor(sysUsersManagementService.findATutorFromGroup(classGroup.getId()));
					
					logger.info("this is [saveuploadassignmentinfo.do] is saving ...");
					appStudentUploadAssignmentSerivce.save(uploadAssignment);
					
					result.put("status", 1);
					result.put("data", "operation success!");
					logger.info("this is [saveuploadassignmentinfo.do] save uploadAssignment done ...");
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
		return result.get("status").equals(1)?"forward:/appassignmentstudentmanagement/uploadassignmentlist.do":"forward:/appassignmentstudentmanagement/showuploadassignmentinfo.do";
	}
	
	@RequestMapping(value = "/deletemultipleassignment.do", method=RequestMethod.POST)
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="批量收回学生上传的作业")
	public String deleteＭultipleＡssignment(HttpServletRequest request, String deleteIds) {
		logger.info("this is [deletemultipleassignment.do] start ...");
		logger.info("this is [deletemultipleassignment.do] values [deleteId={"+deleteIds+"}]");
		Map<String, Object> result=new HashMap<String, Object>();
		if (deleteIds!=null&&!deleteIds.equals("")){
			try {
				logger.info("this is [deletemultipleassignment.do] ready to delete ...");
				if (appStudentUploadAssignmentSerivce.deleteMultiple(deleteIds)){
					result.put("status", 1);
					result.put("data", "成功收回已上传的作业");
				}else{
					result.put("status", 0);
					result.put("data", "收回已上传作业失败,请重试!");
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
		return "forward:/appassignmentstudentmanagement/uploadassignmentlist.do";
	}
	
	@RequestMapping(value = "/downloadassignmentlist.do")
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="查询学生下载作业列表")
	public String downloadAssignmentList(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		logger.info("this is [downloadassignmentlist.do] start ...");
		logger.info("this is [downloadassignmentlist.do] end ...");
		return "appassignment/studentdownloadlist";
	}
	
	@RequestMapping(value = "/initdownloadassignmentlist.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemLogIsCheck(description="查询学生下载作业列表")
	public String initDownloadAssignmentList(HttpServletRequest request) {
		logger.info("this is [initdownloadassignmentlist.do] start ...");
		
		int displayLength = ServletRequestUtils.getIntParameter(request, "length", 0), //每页大小
			displayStart = ServletRequestUtils.getIntParameter(request, "start", 0), //起始值
			sEcho = ServletRequestUtils.getIntParameter(request, "draw", 0);
		
		
		logger.info("this is [initdownloadassignmentlist.do] requset pram [displayLength = "+displayLength+"],[displayStart = {"+displayStart+"}],[sEcho = {"+sEcho+"}]");
		
		String assignmentName=ServletRequestUtils.getStringParameter(request, "assignment_name", null), 
			startTime=ServletRequestUtils.getStringParameter(request, "upload_start_time", null), 
			endTime=ServletRequestUtils.getStringParameter(request, "upload_end_time", null),
			sort=ServletRequestUtils.getStringParameter(request, "order[0][column]", null),
			dir=ServletRequestUtils.getStringParameter(request, "order[0][dir]", null);
		
		logger.info("this is [initdownloadassignmentlist.do] requset pram [assignmentName = {"+assignmentName+"}],[startTime = {"+startTime+"}]"
				+ ",[endTime = {"+endTime+"},[sort = {"+sort+"}],[dir = {"+dir+"}]");
		
		Map<String, Object> parameters=new HashMap<String, Object>();
		if (assignmentName!=null&&!assignmentName.equals("")){
			parameters.put("assignmentName", assignmentName);
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
			parameters.put("sort", 3);
		}
		if (dir!=null&&!dir.equals("")){
			parameters.put("order", dir);
		}
		else{
			parameters.put("order", "desc");
		}
		
		Integer userId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
		logger.info("this is [initdownloadassignmentlist.do] user ["+userId+"]");
		
		Map<String,Object> returnData=appTutorAppointmentToStudentService.searchDataForAjaxToStudent(displayLength, displayStart, sEcho, parameters, userId);
		
		returnData.put("draw", sEcho);
		
		String result=JSONObject.fromObject(returnData).toString();
		logger.info("this is [initdownloadassignmentlist.do] data ["+result+"] ...");
		logger.info("this is [initdownloadassignmentlist.do] end ...");
		return result;
	}

	@RequestMapping(value = "/showstudentassignment.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="学生下载作业")
	public String showStudentAssignment(HttpServletRequest request, HttpServletResponse response, Integer sid) {
		logger.info("this is [showstudentassignment.do] start ...");
		try{
			if (sid!=-1){
				AppTutorAppointmentAssignmentToStudent assignment=appTutorAppointmentToStudentService.get(sid);
				
				if (assignment.getDownloadTime()==null||assignment.getDownloadTime().equals("")){
					assignment.setDownloadTime(Calendar.getInstance().getTime());
				}
				appTutorAppointmentToStudentService.save(assignment);
				
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
}
