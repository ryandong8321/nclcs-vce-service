package org.ryan.nclcs.vce.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ryan.nclcs.vce.annotation.SystemLogIsCheck;
import org.ryan.nclcs.vce.annotation.SystemUserLoginIsCheck;
import org.ryan.nclcs.vce.entity.SysGroups;
import org.ryan.nclcs.vce.service.sysgroups.ISysGroupsManagementService;
import org.ryan.nclcs.vce.service.sysproperties.ISysPropertiesManagementService;
import org.ryan.nclcs.vce.service.sysusers.ISysUsersManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/sysgroupsmanagement")
public class SysGroupsManagementController {
	
	protected static Logger logger = LoggerFactory.getLogger(SysGroupsManagementController.class);

	@Autowired
	private ISysGroupsManagementService sysGroupsManagementService;
	
	@Autowired
	private ISysPropertiesManagementService sysPropertiesManagementService;
	
	@Autowired
	private ISysUsersManagementService sysUsersManagementService;
	
	@RequestMapping(value = "/sysgroupstree.do")
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="查询群组信息")
	public String sysGroupsTree(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		logger.info("this is [sysgroupstree.do] start ...");
		logger.info("this is [sysgroupstree.do] end ...");
		return "sysgroups/sysgroupstree";
	}
	
	@RequestMapping(value = "/inittreedata.do")
	@ResponseBody
	@SystemLogIsCheck(description="查询群组信息")
	public String initTreeData(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		logger.info("this is [initTreeData.do] start ...");
		List<Map<String, Object>> data=sysGroupsManagementService.findGroupsForTree(new HashMap<String, Object>());
		String strJsonResult = JSONArray.fromObject(data).toString();
		logger.info("this is [initTreeData.do] json [" + strJsonResult + "]");
		logger.info("this is [initTreeData.do] end ...");
		return strJsonResult;
	}
	
	@RequestMapping(value = "/savesysgroups.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="保存群组信息")
	public Map<String, Object> saveSysGroup(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [savesysgroups.do] start ...");
		Integer groupId=0,groupParentId=0,groupCategory=0,propertyDateInfo=0,propertyClassInfo=0;
		String groupName=null,gropuTimeInfo=null;
		Map<String, Object> result=new HashMap<String, Object>();
		
		if (data!=null&&!data.equals("")){
			try {
				logger.info("this is [savesysgroups.do] is decoding ...");
				data=URLDecoder.decode(data, "utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.info("this is [savesysgroups.do] occur error when program decoding ...");
				e.printStackTrace();
			}
		}
		
		try{
			JSONObject json=JSONObject.fromString(data);
			
			groupId=Integer.parseInt(
					json.getString("groupId")==null||json.getString("groupId").equals("")||json.getString("groupId").equals("null")
						?"0":json.getString("groupId"));
			groupName=json.getString("groupName")==null||json.getString("groupName").equals("")||json.getString("groupName").equals("null")
					?"":json.getString("groupName");
			groupParentId=Integer.parseInt(
					json.getString("groupParentId")==null||json.getString("groupParentId").equals("")||json.getString("groupParentId").equals("null")
						?"-1":json.getString("groupParentId"));
			groupCategory=Integer.parseInt(
					json.getString("groupCategory")==null||json.getString("groupCategory").equals("")||json.getString("groupCategory").equals("null")
						?"-1":json.getString("groupCategory"));
			
			if (json.has("propertyDateInfo")){
				propertyDateInfo=Integer.parseInt(
						json.getString("propertyDateInfo")==null||json.getString("propertyDateInfo").equals("")||json.getString("propertyDateInfo").equals("null")
							?"-1":json.getString("propertyDateInfo"));
			}
			if (json.has("propertyClassInfo")){
				propertyClassInfo=Integer.parseInt(
						json.getString("propertyClassInfo")==null||json.getString("propertyClassInfo").equals("")||json.getString("propertyClassInfo").equals("null")
							?"-1":json.getString("propertyClassInfo"));
			}
			if (json.has("gropuTimeInfo")){
				gropuTimeInfo=json.getString("gropuTimeInfo")==null||json.getString("gropuTimeInfo").equals("")||json.getString("gropuTimeInfo").equals("null")
							?"":json.getString("gropuTimeInfo");
			}
		}catch(Exception ex){
			logger.info("this is [savesysgroups.do] get parameter error ...");
			result.put("status", -1);
			result.put("data", "parameters error");
			ex.printStackTrace();
		}
		
		if (result.isEmpty()){
			logger.info("this is [savesysgroups.do] groupId ["+groupId+"] ...");
			logger.info("this is [savesysgroups.do] groupParentId ["+groupParentId+"] ...");
			logger.info("this is [savesysgroups.do] groupName ["+groupName+"] ...");
			logger.info("this is [groupCategory.do] groupCategory ["+groupCategory+"] ...");
			
			logger.info("this is [propertyDateInfo.do] propertyDateInfo ["+propertyDateInfo+"] ...");
			logger.info("this is [propertyClassInfo.do] propertyClassInfo ["+propertyClassInfo+"] ...");
			logger.info("this is [gropuTimeInfo.do] gropuTimeInfo ["+gropuTimeInfo+"] ...");
			
			SysGroups sysGroup=null;
			if (groupId!=0){
				logger.info("this is [savesysgroups.do] find sysGroup by id ["+groupId+"] ...");
				sysGroup=sysGroupsManagementService.get(groupId);
			}else{
				logger.info("this is [savesysgroups.do] create new sysGroup ...");
				sysGroup=new SysGroups();
			}
			logger.info("this is [savesysgroups.do] set attributes ...");
			sysGroup.setGroupName(groupName);
			sysGroup.setGroupParentId(groupParentId);
			sysGroup.setGroupCategory(groupCategory);
			
			if (groupCategory==1){
				sysGroup.setPropertyDateInfo(sysPropertiesManagementService.get(propertyDateInfo));
				sysGroup.setPropertyClassInfo(sysPropertiesManagementService.get(propertyClassInfo));
				sysGroup.setGropuTimeInfo(gropuTimeInfo);
			}
			
			logger.info("this is [savesysgroups.do] show sysGroup ["+sysGroup+"] ...");
			try{
				logger.info("this is [savesysgroups.do] is saving ...");
				sysGroupsManagementService.save(sysGroup);
				logger.info("this is [savesysgroups.do] save sysGroup done ...");
				result.put("status", 1);
				result.put("data", "save success!");
			}catch(Exception ex){
				logger.info("this is [savesysgroups.do] save sysGroup error ...");
				result.put("status", 1);
				result.put("data", "save failed, try again!");
				ex.printStackTrace();
			}
		}
		
		logger.info("this is [savesysgroups.do] show result ["+result+"] ...");
		return result;
	}
	
	@RequestMapping(value = "/deletegroups.do",method=RequestMethod.POST)
	@ResponseBody
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="删除群组信息")
	public Map<String, Object> deleteSysGroups(HttpServletRequest request,@RequestBody String data) {
		logger.info("this is [deletegroups.do] start ...");
		if (data!=null&&!data.equals("")){
			try {
				logger.info("this is [deletegroups.do] is decoding ...");
				data=URLDecoder.decode(data, "utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.info("this is [deletegroups.do] occur error when program decoding ...");
				e.printStackTrace();
			}
		}
		
		JSONObject json=JSONObject.fromString(data);
		String ids=json.getString("groupIds")==null||json.getString("groupIds").equals("")||json.getString("groupIds").equals("null")
				?"-1":json.getString("groupIds");
		logger.info("this is [deletegroups.do] show propertyIds ["+ids+"]");
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		try{
			
			logger.info("this is [deletegroups.do] is deleting ...");
			sysGroupsManagementService.deleteGroups(ids);
			parameters.put("status", 1);
			parameters.put("data", "operation success.");
		}catch(Exception ex){
			logger.info("this is [deletegroups.do] delete error ...");
			ex.printStackTrace();
			parameters.put("status", 0);
			parameters.put("data", "operation failed, try again please.");
		}
		
		logger.info("this is [deletegroups.do] end ...");
		return parameters;
	}
	
	@RequestMapping(value = "/findpropertydateinfo.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemUserLoginIsCheck
	public String findPropertyDateInfo(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [findpropertydateinfo.do] start ...");
		Integer groupId=0, propertyId=0;
		Map<String, Object> result=new HashMap<String, Object>();
		
		if (data!=null&&!data.equals("")){
			try {
				logger.info("this is [findpropertydateinfo.do] is decoding ...");
				data=URLDecoder.decode(data, "utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.info("this is [findpropertydateinfo.do] occur error when program decoding ...");
				e.printStackTrace();
			}
		}
		
		try{
			JSONObject json=JSONObject.fromString(data);
			groupId=Integer.parseInt(
					json.getString("groupId")==null||json.getString("groupId").equals("")||json.getString("groupId").equals("null")
						?"0":json.getString("groupId"));
			propertyId=json.getInt("propertyId");
		}catch(Exception ex){
			logger.info("this is [findpropertydateinfo.do] get parameter error ...");
			result.put("status", -1);
			result.put("data", "parameters error");
			ex.printStackTrace();
		}
		
		Map<String, Object> parameters=new HashMap<String, Object>();
		parameters.put("propertyParentId", propertyId);
		List<Map<String,Object>> propertiesInfo=sysPropertiesManagementService.findProperty(parameters);
		
		if (groupId!=0){
			SysGroups group=sysGroupsManagementService.get(groupId);
			if (group.getPropertyDateInfo()!=null){
				for(Map<String, Object> map:propertiesInfo){
					if (map.get("id").equals(group.getPropertyDateInfo().getId())){
						//disabled: $option.prop('disabled'),
				        //selected: $option.prop('selected'),
						map.put("selected", "selected");
						break;
					}
				}
			}
		}
		
		result.put("status", 1);
		result.put("data", propertiesInfo);
		
		String json=JSONObject.fromObject(result).toString();
		logger.info("this is [findpropertydateinfo.do] data ["+json+"] ...");
		logger.info("this is [findpropertydateinfo.do] end ...");
		return json;
	}
	
	@RequestMapping(value = "/findpropertyclassinfo.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemUserLoginIsCheck
	public String findPropertyClassInfo(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [findpropertyclassinfo.do] start ...");
		Integer groupId=0, propertyId=0;
		Map<String, Object> result=new HashMap<String, Object>();
		
		if (data!=null&&!data.equals("")){
			try {
				logger.info("this is [findpropertyclassinfo.do] is decoding ...");
				data=URLDecoder.decode(data, "utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.info("this is [findpropertyclassinfo.do] occur error when program decoding ...");
				e.printStackTrace();
			}
		}
		
		try{
			JSONObject json=JSONObject.fromString(data);
			groupId=Integer.parseInt(
					json.getString("groupId")==null||json.getString("groupId").equals("")||json.getString("groupId").equals("null")
						?"0":json.getString("groupId"));
			propertyId=json.getInt("propertyId");
		}catch(Exception ex){
			logger.info("this is [findpropertyclassinfo.do] get parameter error ...");
			result.put("status", -1);
			result.put("data", "parameters error");
			ex.printStackTrace();
		}
		
		Map<String, Object> parameters=new HashMap<String, Object>();
		parameters.put("propertyParentId", propertyId);
		List<Map<String,Object>> propertiesInfo=sysPropertiesManagementService.findProperty(parameters);
		
		if (groupId!=0){
			SysGroups group=sysGroupsManagementService.get(groupId);
			if (group.getPropertyClassInfo()!=null){
				for(Map<String, Object> map:propertiesInfo){
					if (map.get("id").equals(group.getPropertyClassInfo().getId())){
						//disabled: $option.prop('disabled'),
				        //selected: $option.prop('selected'),
						map.put("selected", "selected");
						break;
					}
				}
			}
		}
		
		result.put("status", 1);
		result.put("data", propertiesInfo);
		
		String json=JSONObject.fromObject(result).toString();
		logger.info("this is [findpropertyclassinfo.do] data ["+json+"] ...");
		logger.info("this is [findpropertyclassinfo.do] end ...");
		return json;
	}
	
	@RequestMapping(value = "/findgroupinfo.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemUserLoginIsCheck
	public String findGroupinfo(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [findgroupinfo.do] start ...");
		Integer groupId=0;
		Map<String, Object> result=new HashMap<String, Object>();
		
		if (data!=null&&!data.equals("")){
			try {
				logger.info("this is [findgroupinfo.do] is decoding ...");
				data=URLDecoder.decode(data, "utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.info("this is [findgroupinfo.do] occur error when program decoding ...");
				e.printStackTrace();
			}
		}
		
		try{
			JSONObject json=JSONObject.fromString(data);
			groupId=Integer.parseInt(
					json.getString("groupId")==null||json.getString("groupId").equals("")||json.getString("groupId").equals("null")
						?"0":json.getString("groupId"));
		}catch(Exception ex){
			logger.info("this is [findgroupinfo.do] get parameter error ...");
			result.put("status", -1);
			result.put("data", "parameters error");
			ex.printStackTrace();
		}
		
		Map<String, Object> parameters=new HashMap<String, Object>();
		
		if (groupId!=0){
			SysGroups group=sysGroupsManagementService.get(groupId);
			if (group!=null){
				parameters.put("id", group.getId());
				parameters.put("groupName", group.getGroupName());
				parameters.put("groupCategory", group.getGroupCategory());
				parameters.put("groupParentId", group.getGroupParentId());
				parameters.put("groupDateInfo", group.getPropertyDateInfo()==null?"":group.getPropertyDateInfo().getId());
				parameters.put("groupClassInfo", group.getPropertyClassInfo()==null?"":group.getPropertyClassInfo().getId());
				parameters.put("groupTimeInfo", group.getGropuTimeInfo());
			}
		}
		
		result.put("status", 1);
		result.put("data", parameters);
		
		String json=JSONObject.fromObject(result).toString();
		logger.info("this is [findgroupinfo.do] data ["+json+"] ...");
		logger.info("this is [findgroupinfo.do] end ...");
		return json;
	}
	
	@RequestMapping(value = "/findsysusers.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemUserLoginIsCheck
	public String findSysUsers(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [findsysusers.do] start ...");
		Integer groupId=0;
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
			groupId=Integer.parseInt(
					json.getString("groupId")==null||json.getString("groupId").equals("")||json.getString("groupId").equals("null")
						?"0":json.getString("groupId"));
		}catch(Exception ex){
			logger.info("this is [findsysusers.do] get parameter error ...");
			result.put("status", -1);
			result.put("data", "parameters error");
			ex.printStackTrace();
		}
		
		Map<String, Object> parameters=new HashMap<String, Object>();
		List<Map<String,Object>> allUsersInfo=null;
		if (groupId!=0){
			allUsersInfo=sysUsersManagementService.findAllSysUsersByPrivilege(parameters, groupId);
		}
		
		result.put("status", 1);
		result.put("data", allUsersInfo);
		
		String json=JSONObject.fromObject(result).toString();
		logger.info("this is [findsysusers.do] data ["+json+"] ...");
		logger.info("this is [findsysusers.do] end ...");
		return json;
	}
	
	
	@RequestMapping(value = "/savegroupuserrelationship.do",method=RequestMethod.POST)
	@ResponseBody
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="保存群组授权信息")
	public String saveGroupUserRelationship(HttpServletRequest request,@RequestBody String data) {
		logger.info("this is [savegroupuserrelationship.do] start ...");
		if (data!=null&&!data.equals("")){
			try {
				logger.info("this is [savegroupuserrelationship.do] is decoding ...");
				data=URLDecoder.decode(data, "utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.info("this is [savegroupuserrelationship.do] occur error when program decoding ...");
				e.printStackTrace();
			}
		}
		
		String userIds=null,groupId=null;
		try {
			JSONObject json=JSONObject.fromString(data);
			userIds = json.getString("userIds")==null||json.getString("userIds").equals("")||json.getString("userIds").equals("null")
					?"":json.getString("userIds");
			logger.info("this is [savegroupuserrelationship.do] show userIds ["+userIds+"]");
			groupId = json.getString("groupId")==null||json.getString("groupId").equals("")||json.getString("groupId").equals("null")
					?"-1":json.getString("groupId");
			logger.info("this is [savegroupuserrelationship.do] show groupId ["+groupId+"]");
		} catch (Exception e) {
			logger.info("this is [savegroupuserrelationship.do] occur error when getting parameters ...");
			e.printStackTrace();
		}
		
		Map<String, Object> parameters=sysGroupsManagementService.saveRelationship(groupId, userIds);
		
		String result=JSONObject.fromObject(parameters).toString();
		logger.info("this is [savegroupuserrelationship.do] result ["+result+"] ...");
		logger.info("this is [savegroupuserrelationship.do] end ...");
		return result;
	}
}
