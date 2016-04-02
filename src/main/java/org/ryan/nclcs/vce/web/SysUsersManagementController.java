package org.ryan.nclcs.vce.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ryan.nclcs.vce.annotation.SystemLogIsCheck;
import org.ryan.nclcs.vce.annotation.SystemUserLoginIsCheck;
import org.ryan.nclcs.vce.entity.SysGroups;
import org.ryan.nclcs.vce.entity.SysRoles;
import org.ryan.nclcs.vce.entity.SysUsers;
import org.ryan.nclcs.vce.service.sysusers.ISysUsersManagementService;
import org.ryan.nclcs.vce.web.util.PasswordHash;
import org.ryan.nclcs.vce.web.util.TokenUtils;
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

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/sysusersmanagement")
public class SysUsersManagementController {
	
	protected static Logger logger = LoggerFactory.getLogger(SysUsersManagementController.class);
	
	@Autowired
	private ISysUsersManagementService sysUsersManagementService;
	
//	@Autowired
//	private ISysPropertiesManagementService sysPropertiesManagementService;
//	
//	@Autowired
//	private ISysGroupsManagementService sysGroupsManagementService;
//	
//	@Autowired
//	private ISysRolesManagementService sysRolesManagementService;
	
	@RequestMapping(value = "/sysuserslist.do")
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="用户信息查询")
	public String sysUsersList(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		logger.info("this is [sysuserslist.do] start ...");
		logger.info("this is [sysuserslist.do] end ...");
		return "sysusers/sysuserslist";
	}
	
	@RequestMapping(value = "/initsysuserstable.do")
	@ResponseBody
	@SystemLogIsCheck(description="用户信息查询")
	public String initSysUsersTable(HttpServletRequest request) {
		logger.info("this is [initsysuserstable.do] start ...");
		
		int displayLength = ServletRequestUtils.getIntParameter(request, "length", 0), //每页大小
			displayStart = ServletRequestUtils.getIntParameter(request, "start", 0), //起始值
			sEcho = ServletRequestUtils.getIntParameter(request, "draw", 0),
			userId=ServletRequestUtils.getIntParameter(request, "sysusers_id", -1);
		
		logger.info("this is [initsysuserstable.do] requset pram [displayLength = "+displayLength+"],[displayStart = {"+displayStart+"}],[sEcho = {"+sEcho+"}]");
		
		String userName=ServletRequestUtils.getStringParameter(request, "sysusers_name", null), 
			chineseName=ServletRequestUtils.getStringParameter(request, "sysusers_chinesename", null), 
			englishName=ServletRequestUtils.getStringParameter(request, "sysusers_englishname", null),
			mobilePhone=ServletRequestUtils.getStringParameter(request, "sysusers_mobile", null),
			emailAddress=ServletRequestUtils.getStringParameter(request, "sysusers_email", null),
			sort=ServletRequestUtils.getStringParameter(request, "order[0][column]", null),
			dir=ServletRequestUtils.getStringParameter(request, "order[0][dir]", null);
		
		logger.info("this is [initsysuserstable.do] requset pram [userName = {"+userName+"}],[chineseName = {"+chineseName+"}]"
				+ ",[englistName = {"+englishName+"},[mobilePhone = {"+mobilePhone+"}],[emailAddress = {"+emailAddress+"}]"
				+ ",[sort = {"+sort+"}],[dir = {"+dir+"}]");
		
		int currentUserId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
		
		Map<String, Object> parameters=new HashMap<String, Object>();
		if (userName!=null&&!userName.equals("")){
			parameters.put("userName", userName);
		}
		if (userId!=-1){
			parameters.put("id", userId);
		}
		if (chineseName!=null&&!chineseName.equals("")){
			parameters.put("chineseName", chineseName);
		}
		if (englishName!=null&&!englishName.equals("")){
			parameters.put("englishName", englishName);
		}
		if (mobilePhone!=null&&!mobilePhone.equals("")){
			parameters.put("mobilePhone", mobilePhone);
		}
		if (emailAddress!=null&&!emailAddress.equals("")){
			parameters.put("emailAddress", emailAddress);
		}
		
		if (sort!=null&&!sort.equals("")){
			parameters.put("sort", sort);
		}
		if (dir!=null&&!dir.equals("")){
			parameters.put("order", dir);
		}
		
		Map<String,Object> returnData=sysUsersManagementService.searchDataForAjax(displayLength, displayStart, sEcho, parameters, currentUserId);
		
		returnData.put("draw", sEcho);
		
		String result=JSONObject.fromObject(returnData).toString();
		logger.info("this is [initsysuserstable.do] data ["+result+"] ...");
		logger.info("this is [initsysuserstable.do] end ...");
		return result;
	}
	
	@RequestMapping(value = "/showsysusersinfo.do", method=RequestMethod.POST)
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="显示用户信息")
	public String showSysUsersInfo(HttpServletRequest request, Integer userId) {
		logger.info("this is [showsysusersinfo.do] start ...");
		logger.info("this is [showsysusersinfo.do] userId ["+userId+"] ...");
		SysUsers user=null;
		try{
			if (userId!=null&&userId!=-1){
				logger.info("this is [showsysusersinfo.do] find user ...");
				user=sysUsersManagementService.get(userId);
			}
		}catch(Exception ex){
			logger.info("this is [showsysusersinfo.do] find user failed...");
			ex.printStackTrace();
		}
		
		logger.info("this is [showsysusersinfo.do] data ["+user+"] ...");
		
		if (user!=null){
			request.setAttribute("sysuser", user);
		}
		logger.info("this is [showsysusersinfo.do] end ...");
		return "sysusers/sysusersinfo";
	}
	
	@RequestMapping(value = "/savesysusers.do", method=RequestMethod.POST)
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="保存用户信息")
	public String saveSysUsers(HttpServletRequest request, @ModelAttribute("sysuser") SysUsers sysUsers, Integer userId) {
		logger.info("this is [savesysusers.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		
		logger.info("this is [savesysusers.do] check is user_name exist...");
		Map<String,Object> parameters=new HashMap<String,Object>();
		if (userId==null||userId==0){
			parameters.put("userName", sysUsers.getUserName());
			if (sysUsersManagementService.isExistByParameters(parameters)!=null){
				result.put("status", 0);
				result.put("data", "用户名已存在，请修改后重试!");
			}
		}
		if (result.isEmpty()){
			logger.info("this is [savesysusers.do] check token ...");
			String userName=request.getSession().getAttribute("u_name")==null?"":request.getSession().getAttribute("u_name").toString();
			if (userName!=null&&!userName.equals("")){
				String sessionToken=request.getSession().getAttribute(userName)==null?"":request.getSession().getAttribute(userName).toString();
				if (sessionToken!=null&&!sessionToken.equals("")&&TokenUtils.getInstance().validateSessionToken(userName, sessionToken)){
					logger.info("this is [savesysusers.do] check token success...");
				}else{
					result.put("status", -2);
					result.put("data", "illegal user!");
					logger.info("this is [savesysusers.do] to delete failed...");
				}
			}else{
				result.put("status", -2);
				result.put("data", "illegal user!");
				logger.info("this is [savesysusers.do] to delete failed...");
			}
		}
		
		if (result.isEmpty()){
			if (userId!=null&&userId!=0){
				sysUsers.setId(userId);
			}else if (userId==null||userId==0){
//				sysUsers.setPassword(MD5.string2MD5(MD5.string2MD5(sysUsers.getPassword())));
				try {
					logger.info("this is [savesysusers.do] generate password ...");
					sysUsers.setPassword(PasswordHash.createHash(PasswordHash.giveMeSalt(sysUsers.getPassword(), sysUsers.getUserName())));
					logger.info("this is [savesysusers.do] generate password done...");
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
					result.put("status", 0);
					result.put("data", "密码生成错误，请重试!");
				} catch (InvalidKeySpecException e) {
					e.printStackTrace();
					result.put("status", 0);
					result.put("data", "密码生成错误，请重试!");
				}
			}
			
			if (result.isEmpty()){
				try{
					logger.info("this is [savesysusers.do] is saving ...");
					logger.info("this is [savesysusers.do] show sysUsers ["+sysUsers+"] ...");
					sysUsersManagementService.save(sysUsers);
					result.put("status", 1);
					result.put("data", "operation success!");
					logger.info("this is [savesysusers.do] save sysUsers done ...");
				}catch(Exception ex){
					logger.info("this is [savesysusers.do] save sysUsers error ...");
					result.put("status", 0);
					result.put("data", "save failed, try again!");
					ex.printStackTrace();
				}
			}
			logger.info("this is [savesysusers.do] show result ["+result+"] ...");
		}
		
		request.setAttribute("result", result.get("data"));
		request.setAttribute("sysuser", sysUsers);
		return result.get("status").equals(1)?"forward:/sysusersmanagement/sysuserslist.do":"forward:/sysusersmanagement/showsysusersinfo.do";
	}
	
	@RequestMapping(value = "/deletesysusers.do", method=RequestMethod.POST)
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="删除用户信息")
	public String deleteSysUsers(HttpServletRequest request, Integer deleteId) {
		logger.info("this is [deletesysusers.do] start ...");
		logger.info("this is [deletesysusers.do] values [deleteId={"+deleteId+"}]");
		Map<String, Object> result=new HashMap<String, Object>();
		if (deleteId!=null&&deleteId!=0){
			try {
				logger.info("this is [deletesysusers.do] check token ...");
				String userName=request.getSession().getAttribute("u_name")==null?"":request.getSession().getAttribute("u_name").toString();
				if (userName!=null&&!userName.equals("")){
					String sessionToken=request.getSession().getAttribute(userName)==null?"":request.getSession().getAttribute(userName).toString();
					if (sessionToken!=null&&!sessionToken.equals("")&&TokenUtils.getInstance().validateSessionToken(userName, sessionToken)){
						logger.info("this is [deletesysusers.do] check token success...");
						logger.info("this is [deletesysusers.do] ready to delete ...");
						sysUsersManagementService.delete(sysUsersManagementService.get(deleteId));
						result.put("status", 1);
						result.put("data", "operation success!");
						logger.info("this is [deletesysusers.do] to delete done...");
					}else{
						result.put("status", -2);
						result.put("data", "illegal user!");
						logger.info("this is [deletesysusers.do] to delete failed...");
					}
				}else{
					result.put("status", -2);
					result.put("data", "illegal user!");
					logger.info("this is [deletesysusers.do] to delete failed...");
				}
			} catch (Exception e) {
				logger.info("this is [deletesysusers.do] to trough exception when delete ...");
				result.put("status", 0);
				result.put("data", "delete failed, try again!");
				e.printStackTrace();
			}
		}
		logger.info("this is [deletesysusers.do] show result ["+result+"] ...");
		request.setAttribute("result", result.get("data"));
		return "forward:/sysusersmanagement/sysuserslist.do";
	}
	
	@RequestMapping(value = "/deletemultiplesysusers.do", method=RequestMethod.POST)
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="批量删除用户信息")
	public String deleteMultipleSysUsers(HttpServletRequest request, String deleteIds) {
		logger.info("this is [deletemultiplesysusers.do] start ...");
		logger.info("this is [deletemultiplesysusers.do] values [deleteId={"+deleteIds+"}]");
		Map<String, Object> result=new HashMap<String, Object>();
		if (deleteIds!=null&&!deleteIds.equals("")){
			try {
				logger.info("this is [deletemultiplesysusers.do] check token ...");
				String userName=request.getSession().getAttribute("u_name")==null?"":request.getSession().getAttribute("u_name").toString();
				if (userName!=null&&!userName.equals("")){
					String sessionToken=request.getSession().getAttribute(userName)==null?"":request.getSession().getAttribute(userName).toString();
					if (sessionToken!=null&&!sessionToken.equals("")&&TokenUtils.getInstance().validateSessionToken(userName, sessionToken)){
						logger.info("this is [deletemultiplesysusers.do] check token success...");
						logger.info("this is [deletemultiplesysusers.do] ready to delete ...");
						sysUsersManagementService.deleteMultiple(deleteIds);
						result.put("status", 1);
						result.put("data", "operation success!");
						logger.info("this is [deletemultiplesysusers.do] to delete done...");
					}else{
						result.put("status", -2);
						result.put("data", "illegal user!");
						logger.info("this is [deletemultiplesysusers.do] to delete failed...");
					}
				}else{
					result.put("status", -2);
					result.put("data", "illegal user!");
					logger.info("this is [deletemultiplesysusers.do] to delete failed...");
				}
			} catch (Exception e) {
				logger.info("this is [deletemultiplesysusers.do] to trough exception when delete ...");
				result.put("status", 0);
				result.put("data", "delete failed, try again!");
				e.printStackTrace();
			}
		}
		
		logger.info("this is [deletemultiplesysusers.do] show result ["+result+"] ...");
		request.setAttribute("result", result.get("data"));
		return "forward:/sysusersmanagement/sysuserslist.do";
	}
	
	@RequestMapping(value = "/douserlogin.do")
	public String doUserLogin(HttpServletRequest request) {
		logger.info("this is [douserlogin.do] start ...");
		
		TokenUtils tokenUtils=TokenUtils.getInstance();
		String tokenName="vce.newchinese.vic.edu.au."+request.getSession().getId();
		String token=tokenUtils.generateToken(request.getSession().getId());
		System.out.println("token=="+token);
		tokenUtils.setLoginTokenMap(tokenName, token);
		
		request.setAttribute("tokenName", tokenName);
		request.setAttribute("token", token);
		return "forward:/login.jsp";
	}
	
	@RequestMapping(value = "/userlogin.do", method=RequestMethod.POST)
	@SystemLogIsCheck(description="用户登录")
	public String userLogin(HttpServletRequest request, @ModelAttribute("sysuser") SysUsers sysUsers) {
		logger.info("this is [userlogin.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		
		String tokenName="vce.newchinese.vic.edu.au."+request.getSession().getId();
		logger.info("this is [userlogin.do] get token name ["+tokenName+"]");
		String token=ServletRequestUtils.getStringParameter(request, tokenName, "");
		logger.info("this is [userlogin.do] get token ["+token+"]");
		
		if (TokenUtils.getInstance().validateLoginToken(tokenName, token)){
			logger.info("this is [userlogin.do] validateLoginToken done...");
			logger.info("this is [userlogin.do] check is user_name exist...");
			Map<String,Object> parameters=new HashMap<String,Object>();
			parameters.put("userName", sysUsers.getUserName());
//			parameters.put("password", MD5.string2MD5(sysUsers.getPassword()));
//			parameters.put("password", sysUsers.getPassword());
			
			SysUsers user=sysUsersManagementService.isExistByParameters(parameters);
			if (user!=null){
				try {
					if (PasswordHash.validatePassword(PasswordHash.giveMeSalt(sysUsers.getPassword(), sysUsers.getUserName()), user.getPassword())){
						result.put("status", 1);
						result.put("data", "login success!");
						logger.info("this is [userlogin.do] login success ...");

						String sessionToken=TokenUtils.getInstance().generateToken(request.getSession().getId());
						TokenUtils.getInstance().setSessionTokenMap(user.getUserName(), sessionToken);
						request.getSession().setAttribute(user.getUserName(), sessionToken);
						
						request.getSession().setAttribute("u_id", user.getId());
						request.getSession().setAttribute("u_name", user.getUserName());
						if (user.getSysRoles()!=null&&!user.getSysRoles().isEmpty()){
							List<SysRoles> lstRoles=new ArrayList<SysRoles>();
							for(SysRoles role:user.getSysRoles()){
								lstRoles.add(role);
							}
							request.getSession().setAttribute("u_sr", lstRoles);//roles
						}
						if (user.getSysGroups()!=null&&!user.getSysGroups().isEmpty()){
							List<SysGroups> lstGroups=new ArrayList<SysGroups>();
							for(SysGroups group:user.getSysGroups()){
								lstGroups.add(group);
							}
							request.getSession().setAttribute("u_sg", lstGroups);//groups
						}
					}else{
						result.put("status", 0);
						result.put("data", "login failed, try again!");
						logger.info("this is [userlogin.do] login failed ...");
					}
				} catch (Exception e) {
					e.printStackTrace();
					result.put("status", 0);
					result.put("data", "login failed, try again!");
					logger.info("this is [userlogin.do] login failed ...");
				}
			}else{
				result.put("status", 0);
				result.put("data", "login failed, try again!");
				logger.info("this is [userlogin.do] login failed ...");
			}
		}
		
		request.setAttribute("result", result.get("data"));
		request.setAttribute("sysuser", sysUsers);
		return result.get("status").equals(1)?"forward:/sysnotificationmanagement/sysnotificationdetail.do":"forward:/sysusersmanagement/douserlogin.do";
	}
	
	@RequestMapping(value = "/userlogout.do", method=RequestMethod.GET)
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="用户登录")
	public String userLogout(HttpServletRequest request) {
		logger.info("this is [userlogout.do] start ...");
		try{
			logger.info("this is [userlogout.do] remove token ...");
			if (request.getSession().getAttribute("u_name")!=null){
				TokenUtils.getInstance().removeSessionToken(request.getSession().getAttribute("u_name").toString());
			}
			logger.info("this is [userlogout.do] remove token done...");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		try {
			logger.info("this is [userlogout.do] remove u_id ...");
			request.getSession().setAttribute("u_id", null);
			logger.info("this is [userlogout.do] remove u_id done...");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			logger.info("this is [userlogout.do] remove u_name ...");
			request.getSession().setAttribute("u_name", null);
			logger.info("this is [userlogout.do] remove u_name done...");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			logger.info("this is [userlogout.do] remove u_sr ...");
			request.getSession().setAttribute("u_sr", null);//roles
			logger.info("this is [userlogout.do] remove u_sr done...");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			logger.info("this is [userlogout.do] remove u_sg ...");
			request.getSession().setAttribute("u_sg", null);//groups
			logger.info("this is [userlogout.do] remove u_sg done...");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/sysusersmanagement/douserlogin.do";
	}
	
	@RequestMapping(value = "/changepassword.do",method=RequestMethod.POST)
	@ResponseBody
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="变更用户密码")
	public Map<String, Object> changePassword(HttpServletRequest request,@RequestBody String data) {
		logger.info("this is [changepassword.do] start ...");
		if (data!=null&&!data.equals("")){
			try {
				logger.info("this is [changepassword.do] is decoding ...");
				data=URLDecoder.decode(data, "utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.info("this is [changepassword.do] occur error when program decoding ...");
				e.printStackTrace();
			}
		}
		
		JSONObject json=JSONObject.fromString(data);
		String op=json.getString("op")==null||json.getString("op").equals("")||json.getString("op").equals("null")
				?"":json.getString("op");
		String np=json.getString("np")==null||json.getString("np").equals("")||json.getString("np").equals("null")
				?"":json.getString("np");
		String ud=json.getString("ud")==null||json.getString("ud").equals("")||json.getString("ud").equals("null")
				?"":json.getString("ud");
		logger.info("this is [changepassword.do] ud ["+ud+"] ...");
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		if (ud==null||ud.equals("")||op==null||op.equals("")||np==null||np.equals("")){
			parameters.put("status", 0);
			parameters.put("data", "parameters error, try again please.");
		}
		try{
			logger.info("this is [changepassword.do] check token ...");
			String userName=request.getSession().getAttribute("u_name")==null?"":request.getSession().getAttribute("u_name").toString();
			if (userName!=null&&!userName.equals("")){
				String sessionToken=request.getSession().getAttribute(userName)==null?"":request.getSession().getAttribute(userName).toString();
				if (sessionToken!=null&&!sessionToken.equals("")&&TokenUtils.getInstance().validateSessionToken(userName, sessionToken)){
					logger.info("this is [changepassword.do] check token success...");
					logger.info("this is [changepassword.do] is checking password...");
					SysUsers user=sysUsersManagementService.get(Integer.parseInt(ud));
//					if (!MD5.string2MD5(MD5.string2MD5(op)).equals(user.getPassword())){
					if (!PasswordHash.validatePassword(PasswordHash.giveMeSalt(op, user.getUserName()), user.getPassword())){
						parameters.put("status", 0);
						parameters.put("data", "原始密码输入错误，请重新输入！");
					}else{
//						user.setPassword(MD5.string2MD5(MD5.string2MD5(np)));
						user.setPassword(PasswordHash.createHash(PasswordHash.giveMeSalt(np, user.getUserName())));
						logger.info("this is [changepassword.do] is saving ...");
						sysUsersManagementService.save(user);
						parameters.put("status", 1);
						parameters.put("data", "密码修改成功！");
						logger.info("this is [changepassword.do] is done ...");
					}
				}else{
					parameters.put("status", -2);
					parameters.put("data", "illegal user!");
					logger.info("this is [changepassword.do] to delete failed...");
				}
			}else{
				parameters.put("status", -2);
				parameters.put("data", "illegal user!");
				logger.info("this is [changepassword.do] to delete failed...");
			}
		}catch(Exception ex){
			logger.info("this is [changepassword.do] saving error ...");
			ex.printStackTrace();
			parameters.put("status", 0);
			parameters.put("data", "operation failed, try again please.");
		}
		
		logger.info("this is [changepassword.do] end ...");
		return parameters;
	}
	
	@RequestMapping(value = "/sysuserspersonal.do")
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="显示用户个人信息")
	public String showPersonalInfo(HttpServletRequest request, Integer userId) {
		logger.info("this is [sysuserspersonal.do] start ...");
		logger.info("this is [sysuserspersonal.do] userId ["+userId+"] ...");
		SysUsers user=null;
		int isStudent=0;
		try{
			userId=Integer.parseInt(""+request.getSession().getAttribute("u_id"));
			logger.info("this is [sysuserspersonal.do] find user ["+userId+"]...");
			user=sysUsersManagementService.get(userId);
			if (user.getSysRoles()!=null&&!user.getSysRoles().isEmpty()){
				for (SysRoles role:user.getSysRoles()){
					if (role.getId()==5||role.getId()==6){
						isStudent=1;
					}
				}
			}
		}catch(Exception ex){
			logger.info("this is [sysuserspersonal.do] find user failed...");
			ex.printStackTrace();
		}
		
		logger.info("this is [sysuserspersonal.do] data ["+user+"] ...");
		
		request.setAttribute("sysuser", user);
		request.setAttribute("isStudent", isStudent);
		logger.info("this is [sysuserspersonal.do] end ...");
		return "sysusers/personalinfo";
	}
	
	@RequestMapping(value = "/savepersonalinfo.do", method=RequestMethod.POST)
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="保存个人信息")
	public String savePersonalInfo(HttpServletRequest request, @ModelAttribute("sysuser") SysUsers sysUsers, Integer userId) {
		logger.info("this is [savepersonalinfo.do] start ...");
		Map<String, Object> result=new HashMap<String, Object>();
		
		logger.info("this is [savepersonalinfo.do] check is user_name exist...");
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
				
				String tmp=null;
				if (sysUsers.getEnglishName()!=null){
					tmp=sysUsers.getEnglishName();
					originalUser.setEnglishName(tmp);
				}
				
				if (sysUsers.getPinyin()!=null){
					tmp=sysUsers.getPinyin();
					originalUser.setPinyin(tmp);
				}
				
				if (sysUsers.getHomeAddress()!=null){
					tmp=sysUsers.getHomeAddress();
					originalUser.setHomeAddress(tmp);
				}
				
				if (sysUsers.getDaySchool()!=null){
					tmp=sysUsers.getDaySchool();
					originalUser.setDaySchool(tmp);
				}
				
				if (sysUsers.getDaySchoolGrade()!=null){
					tmp=sysUsers.getDaySchoolGrade();
					originalUser.setDaySchoolGrade(tmp);
				}
				
				originalUser.setChineseName(sysUsers.getChineseName()==null?originalUser.getChineseName():sysUsers.getChineseName());
				originalUser.setMobilePhone(sysUsers.getMobilePhone()==null?originalUser.getMobilePhone():sysUsers.getMobilePhone());
				originalUser.setEmailAddress(sysUsers.getEmailAddress()==null?originalUser.getEmailAddress():sysUsers.getEmailAddress());
				originalUser.setHomePhone(sysUsers.getHomePhone()==null?originalUser.getHomePhone():sysUsers.getHomePhone());
			}else if (userId==null||userId==0){
//				sysUsers.setPassword(MD5.string2MD5(MD5.string2MD5(sysUsers.getPassword())));
			}
			
			try{
				logger.info("this is [savepersonalinfo.do] is saving ...");
				logger.info("this is [savepersonalinfo.do] show sysUsers ["+originalUser+"] ...");
				sysUsersManagementService.save(originalUser);
				result.put("status", 1);
				result.put("data", "operation success!");
				logger.info("this is [savepersonalinfo.do] save sysUsers done ...");
			}catch(Exception ex){
				logger.info("this is [savepersonalinfo.do] save sysUsers error ...");
				result.put("status", 0);
				result.put("data", "save failed, try again!");
				ex.printStackTrace();
			}
			logger.info("this is [savepersonalinfo.do] show result ["+result+"] ...");
		}
		
		request.setAttribute("result", result.get("data"));
		request.setAttribute("sysuser", sysUsers);
		return "forward:/sysusersmanagement/sysuserspersonal.do";
	}
	
	@RequestMapping(value = "/changePasswordonce.do")
	public String changePasswordOnce(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		logger.info("this is [sysuserregister.do] start ...");
		
		logger.info("this is [sysuserregister.do] find all users ...");
		List<Map<String, Object>> list=sysUsersManagementService.findAllSysUsersByParameters(new HashMap<String,Object>());
		SysUsers user=null;
		for (Map<String, Object> map:list){
			logger.info("this is [sysuserregister.do] change password one by one ...");
			user=sysUsersManagementService.get(Integer.parseInt(""+map.get("id")));
			if (user!=null&&!user.getUserName().startsWith("1000")){
				logger.info("this is [sysuserregister.do] change password of user ["+user.getUserName()+"] ...");
				try {
					user.setPassword(PasswordHash.createHash(PasswordHash.giveMeSalt(user.getPassword(), user.getUserName())));
					sysUsersManagementService.save(user);
					logger.info("this is [sysuserregister.do] has changed password of user ["+user.getUserName()+"] ...");
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
					logger.info("this is [sysuserregister.do] changed password of user ["+user.getUserName()+"] failed...");
				} catch (InvalidKeySpecException e) {
					e.printStackTrace();
					logger.info("this is [sysuserregister.do] changed password of user ["+user.getUserName()+"] failed...");
				}
			}
		}
		
		logger.info("this is [sysuserregister.do] end ...");
		return null;
	}
	
//	@RequestMapping(value = "/sysuserregister.do")
//	public String sysUserRegister(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
//		logger.info("this is [sysuserregister.do] start ...");
//		logger.info("this is [sysuserregister.do] end ...");
//		return "sysusers/appstudentregister";
//	}
//	
//	@RequestMapping(value = "/saveuserregister.do", method=RequestMethod.POST)
//	public String saveUserRegister(HttpServletRequest request, @ModelAttribute("sysuser") SysUsers sysUsers, Integer userId, Integer propertyIsLearnChineseId, Integer studentGroupId, Integer studentGroupClassId) {
//		logger.info("this is [saveuserregister.do] start ...");
//		Map<String, Object> result=new HashMap<String, Object>();
//		
//		try {
//			
//			logger.info("this is [saveuserregister.do] check is user_name exist...");
//			Map<String,Object> parameters=new HashMap<String,Object>();
//			
//			parameters.put("userName", sysUsers.getUserName());
//			if (sysUsersManagementService.isExistByParameters(parameters)!=null){
//				request.setAttribute("sysuser", sysUsers);
//				result.put("status", 2);
//				result.put("data", "用户名已存在，请重试!");
//			}
//			
//			if (propertyIsLearnChineseId==null||propertyIsLearnChineseId==null||studentGroupClassId==null){
//				result.put("status", 0);
//				result.put("data", "save failed because lack of parameters!");
//			}
//			
//			if (result.isEmpty()){//not exist
//				sysUsers.setPassword(MD5.string2MD5(MD5.string2MD5((sysUsers.getPassword()))));
//				
//				sysUsers.setUserName(sysUsers.getUserName());
//				sysUsers.setEnglishName(sysUsers.getEnglishName());
//				sysUsers.setChineseName(sysUsers.getChineseName());
//				sysUsers.setPinyin(sysUsers.getPinyin());
//				sysUsers.setHomeAddress(sysUsers.getHomeAddress());
//				sysUsers.setDaySchool(sysUsers.getDaySchool());
//				sysUsers.setDaySchoolGrade(sysUsers.getDaySchoolGrade());
//				
//				//是否在日校学中文
//				SysProperties property=sysPropertiesManagementService.get(propertyIsLearnChineseId);
//				sysUsers.setPropertyIsLearnChinese(property);
//				
//				SysGroups groupNewCampus=sysGroupsManagementService.get(studentGroupId);
//				sysUsers.setVceSchoolName(groupNewCampus.getGroupName());
//				
//				SysGroups groupNewClass=sysGroupsManagementService.get(studentGroupClassId);
//				sysUsers.setVceClassName(groupNewClass.getGroupName());
//				
//				logger.info("this is [saveuserregister.do] is saving user ...");
//				boolean flag=this.sysUsersManagementService.saveRegisterUser(sysUsers, groupNewClass, sysRolesManagementService.get(5));
//				if(flag){
//					result.put("status", 1);
//					result.put("data", "register success!");
//					logger.info("this is [saveuserregister.do] register success ...");
//				}else{
//					result.put("status", 0);
//					result.put("data", "register failed, try again!");
//					logger.info("this is [saveuserregister.do] register failed ...");
//				}
//				
//			}
//		} catch (Exception e) {
//			result.put("status", 0);
//			result.put("data", "register failed, try again!");
//			logger.info("this is [saveuserregister.do] occur exception ...");
//			e.printStackTrace();
//		}
//		
//		request.setAttribute("result", result.get("data"));
//		request.setAttribute("status", result.get("status"));
//		
//		return "forward:/sysusersmanagement/sysuserregister.do";
//	}
}
