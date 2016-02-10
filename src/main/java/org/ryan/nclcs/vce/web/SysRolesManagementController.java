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
import org.ryan.nclcs.vce.entity.SysRoles;
import org.ryan.nclcs.vce.entity.SysUsers;
import org.ryan.nclcs.vce.service.sysroles.ISysRolesManagementService;
import org.ryan.nclcs.vce.service.sysusers.ISysUsersManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/sysrolesmanagement")
public class SysRolesManagementController {
	
protected static Logger logger = LoggerFactory.getLogger(SysRolesManagementController.class);
	
	@Autowired
	private ISysRolesManagementService sysRolesManagementService;
	
	@Autowired
	private ISysUsersManagementService sysUsersManagementService;
	
	@RequestMapping(value = "/sysroleslist.do")
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="角色信息查询")
	public String sysRolesList(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		logger.info("this is [sysroleslist.do] start ...");
		logger.info("this is [sysroleslist.do] end ...");
		return "sysroles/sysroleslist";
	}
	
	@RequestMapping(value = "/initsysrolestable.do")
	@ResponseBody
	@SystemLogIsCheck(description="角色信息查询")
	public String initSysRolesTable(HttpServletRequest request) {
		logger.info("this is [initsysrolestable.do] start ...");
		
		int rowNum = ServletRequestUtils.getIntParameter(request, "offset", 0), 
			showCount = ServletRequestUtils.getIntParameter(request, "limit", 0);
		
		String strSort=ServletRequestUtils.getStringParameter(request, "sort", null), 
			strOrder=ServletRequestUtils.getStringParameter(request, "order", null), 
			search=ServletRequestUtils.getStringParameter(request, "search", null);
		
		Map<String, Object> parameters=new HashMap<String, Object>();
		
		if (search!=null&&!search.equals("")){
			parameters=new HashMap<String, Object>();
			parameters.put("roleName", search);
			parameters.put("roleMeno", search);
		}
		
		Map<String, String> mapSort=new HashMap<String, String>();
		if (strSort!=null&&!strSort.equals("")&&strOrder!=null&&!strOrder.equals("")){
			mapSort.put("sort", strSort);
			mapSort.put("order", strOrder);
		}else{
			mapSort.put("sort", "id");
			mapSort.put("order", "asc");
		}
		
		Map<String,Object> data=sysRolesManagementService.findSysRolesByParameters(rowNum,showCount,parameters,mapSort);
		
		String result=JSONObject.fromObject(data).toString();
		logger.info("this is [initBillTable.do] data ["+result+"] ...");
		logger.info("this is [initBillTable.do] end ...");
		return result;
	}
	
	@RequestMapping(value = "/savesysroles.do",method=RequestMethod.POST)
	@ResponseBody
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="保存角色信息")
	public String saveSysRoles(HttpServletRequest request,@RequestBody String data) {
		logger.info("this is [savesysroles.do] start ...");
		if (data!=null&&!data.equals("")){
			try {
				logger.info("this is [savesysroles.do] is decoding ...");
				data=URLDecoder.decode(data, "utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.info("this is [savesysroles.do] occur error when program decoding ...");
				e.printStackTrace();
			}
		}
		
		String roleName=null,roleMemo=null,roleId=null;
		try {
			JSONObject json=JSONObject.fromString(data);
			roleName = json.getString("roleName")==null||json.getString("roleName").equals("")||json.getString("roleName").equals("null")
					?"":json.getString("roleName");
			logger.info("this is [savesysroles.do] show roleName ["+roleName+"]");
			roleMemo = json.getString("roleMemo")==null||json.getString("roleMemo").equals("")||json.getString("roleMemo").equals("null")
					?"":json.getString("roleMemo");
			logger.info("this is [savesysroles.do] show roleMemo ["+roleMemo+"]");
			roleId = json.getString("roleId")==null||json.getString("roleId").equals("")||json.getString("roleId").equals("null")
					?"-1":json.getString("roleId");
			logger.info("this is [savesysroles.do] show roleId ["+roleId+"]");
		} catch (Exception e) {
			logger.info("this is [savesysroles.do] occur error when getting parameters ...");
			e.printStackTrace();
		}
		
		Map<String, Object> parameters=new HashMap<String, Object>();
		try{
			logger.info("this is [savesysroles.do] is finding role...");
			SysRoles role=sysRolesManagementService.findRoleByRoleName(roleName);
			logger.info("this is [savesysroles.do] is finding role done...");
			if (roleId!=null&&roleId.equals("-1")){//save
				if (role==null){
					logger.info("this is [savesysroles.do] is preparing saving...");
					role=new SysRoles();
					role.setRoleName(roleName);
					role.setRoleMemo(roleMemo);
					sysRolesManagementService.save(role);
					parameters.put("status", 1);
					parameters.put("data", "Operation success.");
					logger.info("this is [savesysroles.do] save done...");
				}else{
					parameters.put("status", 0);
					parameters.put("data", "role_name has existed.");
				}
			}else if (roleId!=null){//update
				if (role==null){
					role=sysRolesManagementService.get(Integer.parseInt(roleId));
					logger.info("this is [savesysroles.do] is preparing updating...");
					role.setRoleName(roleName);
					role.setRoleMemo(roleMemo);
					sysRolesManagementService.save(role);
					parameters.put("status", 1);
					parameters.put("data", "Operation success.");
					logger.info("this is [savesysroles.do] save done...");
				}else{
					parameters.put("status", 0);
					parameters.put("data", "role_name has existed.");
				}
			}
		}catch(Exception ex){
			logger.info("this is [savesysroles.do] finding or saving error ...");
			ex.printStackTrace();
			parameters.put("status", 0);
			parameters.put("data", "Operation was failed, try again.");
		}
		String result=JSONObject.fromObject(parameters).toString();
		logger.info("this is [savesysroles.do] result ["+result+"] ...");
		logger.info("this is [savesysroles.do] end ...");
		return result;
	}
	
	@RequestMapping(value = "/deletesysroles.do",method=RequestMethod.POST)
	@ResponseBody
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="删除角色信息")
	public Map<String, Object> deleteSysRoles(HttpServletRequest request,@RequestBody String data) {
		logger.info("this is [deletesysroles.do] start ...");
		if (data!=null&&!data.equals("")){
			try {
				logger.info("this is [deletesysroles.do] is decoding ...");
				data=URLDecoder.decode(data, "utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.info("this is [deletesysroles.do] occur error when program decoding ...");
				e.printStackTrace();
			}
		}
		
		JSONObject json=JSONObject.fromString(data);
		String ids=json.getString("roleIds")==null||json.getString("roleIds").equals("")||json.getString("roleIds").equals("null")
				?"-1":json.getString("roleIds");
		logger.info("this is [deletesysroles.do] show roleIds ["+ids+"]");
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		try{
			
			logger.info("this is [deletesysroles.do] is deleting ...");
			parameters=sysRolesManagementService.deleteRoles(ids);
		}catch(Exception ex){
			logger.info("this is [deletesysroles.do] delete error ...");
			ex.printStackTrace();
			parameters.put("status", 0);
			parameters.put("data", "operation failed, try again please.");
		}
		
		logger.info("this is [deletesysroles.do] end ...");
		return parameters;
	}
	
	@RequestMapping(value = "/findsysusers.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemUserLoginIsCheck
	public String findSysUsers(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [findsysusers.do] start ...");
		Integer roleId=0;
		Map<String, Object> result=new HashMap<String, Object>();
		
		if (data!=null&&!data.equals("")){
			try {
				logger.info("this is [findsysusers.do] is decoding ...");
				data=URLDecoder.decode(data, "utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.info("this is [findsysusers.do] occur error when program decoding ...");
				e.printStackTrace();
			}
		}
		
		try{
			JSONObject json=JSONObject.fromString(data);
			roleId=Integer.parseInt(
					json.getString("roleId")==null||json.getString("roleId").equals("")||json.getString("roleId").equals("null")
						?"0":json.getString("roleId"));
		}catch(Exception ex){
			logger.info("this is [findsysusers.do] get parameter error ...");
			result.put("status", -1);
			result.put("data", "parameters error");
			ex.printStackTrace();
		}
		
		Map<String, Object> parameters=new HashMap<String, Object>();
		parameters.put("privilegeRoleId", roleId);
		List<Map<String,Object>> allUsersInfo=sysUsersManagementService.findAllSysUsersByPrivilege(parameters);
		
		if (roleId!=0){
			SysRoles sysRole=sysRolesManagementService.get(roleId);
			if (sysRole.getSysRolesUsers()!=null){
				for(Map<String, Object> map:allUsersInfo){
					for (SysUsers user:sysRole.getSysRolesUsers()){
						if (map.get("value").equals(user.getId())){
							map.put("isSelected", true);
							break;
						}
					}
				}
			}
		}
		
		result.put("status", 1);
		result.put("data", allUsersInfo);
		
		String json=JSONObject.fromObject(result).toString();
		logger.info("this is [findsysusers.do] data ["+json+"] ...");
		logger.info("this is [findsysusers.do] end ...");
		return json;
	}
	
	
	@RequestMapping(value = "/saveroleuserrelationship.do",method=RequestMethod.POST)
	@ResponseBody
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="保存角色授权")
	public String saveRoleUserRelationship(HttpServletRequest request,@RequestBody String data) {
		logger.info("this is [saveroleuserrelationship.do] start ...");
		if (data!=null&&!data.equals("")){
			try {
				logger.info("this is [saveroleuserrelationship.do] is decoding ...");
				data=URLDecoder.decode(data, "utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.info("this is [saveroleuserrelationship.do] occur error when program decoding ...");
				e.printStackTrace();
			}
		}
		
		String userIds=null,roleId=null;
		try {
			JSONObject json=JSONObject.fromString(data);
			userIds = json.getString("userIds")==null||json.getString("userIds").equals("")||json.getString("userIds").equals("null")
					?"":json.getString("userIds");
			logger.info("this is [saveroleuserrelationship.do] show userIds ["+userIds+"]");
			roleId = json.getString("roleId")==null||json.getString("roleId").equals("")||json.getString("roleId").equals("null")
					?"-1":json.getString("roleId");
			logger.info("this is [saveroleuserrelationship.do] show roleId ["+roleId+"]");
		} catch (Exception e) {
			logger.info("this is [saveroleuserrelationship.do] occur error when getting parameters ...");
			e.printStackTrace();
		}
		
		Map<String, Object> parameters=new HashMap<String, Object>();
		try{
			if (roleId==null||roleId.equals("-1")){
				parameters.put("status", 0);
				parameters.put("data", "操作失败，请重试!");
				logger.info("this is [saveroleuserrelationship.do] save error roleId is ["+roleId+"]...");
			}else{
				logger.info("this is [saveroleuserrelationship.do] is finding role...");
				SysRoles role=sysRolesManagementService.get(Integer.parseInt(roleId));
				logger.info("this is [saveroleuserrelationship.do] is finding role done...");
				
				try {
					List<SysUsers> lstUsers=new ArrayList<SysUsers>();
					if (userIds!=null&&!userIds.equals("")){
						String[] arr=null;
						if (!userIds.contains(",")){
							arr=new String[1];
							arr[0]=userIds;
						}else{
							arr=userIds.split(",");
						}
						
						for (int ind=0;ind<arr.length;ind++){
							lstUsers.add(sysUsersManagementService.get(Integer.parseInt(arr[ind])));
						}
					}
					role.setSysRolesUsers(lstUsers);
				} catch (NumberFormatException e) {
					logger.info("this is [saveroleuserrelationship.do] find users failed ...");
					parameters.put("status", 0);
					parameters.put("data", "操作失败，请重试!");
					e.printStackTrace();
				}
				
				try{
					logger.info("this is [saveroleuserrelationship.do] is saving ...");
					sysRolesManagementService.save(role);
					logger.info("this is [saveroleuserrelationship.do] save role done ...");
					parameters.put("status", 1);
					parameters.put("data", "operation success!");
				}catch(Exception ex){
					logger.info("this is [saveroleuserrelationship.do] save role error ...");
					parameters.put("status", 0);
					parameters.put("data", "操作失败，请重试!");
					ex.printStackTrace();
				}
			}
			
		}catch(Exception ex){
			logger.info("this is [saveroleuserrelationship.do] finding or saving error ...");
			ex.printStackTrace();
			parameters.put("status", 0);
			parameters.put("data", "Operation was failed, try again.");
		}
		String result=JSONObject.fromObject(parameters).toString();
		logger.info("this is [saveroleuserrelationship.do] result ["+result+"] ...");
		logger.info("this is [saveroleuserrelationship.do] end ...");
		return result;
	}

}
