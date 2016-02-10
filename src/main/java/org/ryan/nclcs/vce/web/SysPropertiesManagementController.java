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
import org.ryan.nclcs.vce.entity.SysProperties;
import org.ryan.nclcs.vce.service.sysproperties.ISysPropertiesManagementService;
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
@RequestMapping("/syspropertiesmanagement")
public class SysPropertiesManagementController {
	
	protected static Logger logger = LoggerFactory.getLogger(SysPropertiesManagementController.class);

	@Autowired
	private ISysPropertiesManagementService sysPropertiesManagementService;
	
	@RequestMapping(value = "/syspropertiestree.do")
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="系统信息查询")
	public String syspropertiesTree(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		logger.info("this is [syspropertiestree.do] start ...");
		logger.info("this is [syspropertiestree.do] end ...");
		return "sysproperties/syspropertiestree";
	}
	
	@RequestMapping(value = "/inittreedata.do")
	@ResponseBody
	@SystemLogIsCheck(description="系统信息查询")
	public String initTreeData(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		logger.info("this is [initTreeData.do] start ...");
		List<Map<String, Object>> data=sysPropertiesManagementService.findPropertiesForTree(null);
		String strJsonResult = JSONArray.fromObject(data).toString();
		logger.info("this is [initTreeData.do] json [" + strJsonResult + "]");
		logger.info("this is [initTreeData.do] end ...");
		return strJsonResult;
	}
	
	@RequestMapping(value = "/savesysproperties.do", method=RequestMethod.POST)
	@ResponseBody
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="保存系统信息")
	public Map<String, Object> saveSysProperty(HttpServletRequest request, @RequestBody String data) {
		logger.info("this is [savesysproperties.do] start ...");
		Integer propertyId=0,propertyParentId=0;
		String propertyName=null;
		Map<String, Object> result=new HashMap<String, Object>();
		
		if (data!=null&&!data.equals("")){
			try {
				logger.info("this is [savesysproperties.do] is decoding ...");
				data=URLDecoder.decode(data, "utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.info("this is [savesysproperties.do] occur error when program decoding ...");
				e.printStackTrace();
			}
		}
		
		try{
			JSONObject json=JSONObject.fromString(data);
			propertyParentId=Integer.parseInt(
					json.getString("propertyParentId")==null||json.getString("propertyParentId").equals("")||json.getString("propertyParentId").equals("null")
						?"-1":json.getString("propertyParentId"));
			propertyId=Integer.parseInt(
					json.getString("propertyId")==null||json.getString("propertyId").equals("")||json.getString("propertyId").equals("null")
						?"0":json.getString("propertyId"));
			propertyName=json.getString("propertyName")==null||json.getString("propertyName").equals("")||json.getString("propertyName").equals("null")
					?"":json.getString("propertyName");
		}catch(Exception ex){
			logger.info("this is [savesysproperties.do] get parameter error ...");
			result.put("status", -1);
			result.put("data", "parameters error");
			ex.printStackTrace();
		}
		
		if (result.isEmpty()){
			logger.info("this is [savesysproperties.do] propertyId ["+propertyId+"] ...");
			logger.info("this is [savesysproperties.do] propertyParentId ["+propertyParentId+"] ...");
			logger.info("this is [savesysproperties.do] propertyName ["+propertyName+"] ...");
			
			SysProperties property=null;
			if (propertyId!=0){
				logger.info("this is [savesysproperties.do] find property by id ["+propertyId+"] ...");
				property=sysPropertiesManagementService.get(propertyId);
			}else{
				logger.info("this is [savesysproperties.do] create new property ...");
				property=new SysProperties();
			}
			logger.info("this is [savesysproperties.do] set attributes ...");
			property.setPropertyName(propertyName);
			property.setPropertyParentId(propertyParentId);
//			property.setCreateTime(new Date(Calendar.getInstance().getTimeInMillis()));
			logger.info("this is [savesysproperties.do] show property ["+property+"] ...");
			try{
				logger.info("this is [savesysproperties.do] is saving ...");
				sysPropertiesManagementService.save(property);
				logger.info("this is [savesysproperties.do] save property done ...");
				result.put("status", 1);
				result.put("data", "save success!");
			}catch(Exception ex){
				logger.info("this is [savesysproperties.do] save property error ...");
				result.put("status", 1);
				result.put("data", "save failed, try again!");
				ex.printStackTrace();
			}
		}
		
		logger.info("this is [savesysproperties.do] show result ["+result+"] ...");
		return result;
	}
	
	@RequestMapping(value = "/deleteproperties.do",method=RequestMethod.POST)
	@ResponseBody
	@SystemUserLoginIsCheck
	@SystemLogIsCheck(description="删除系统信息")
	public Map<String, Object> deleteSysProperties(HttpServletRequest request,@RequestBody String data) {
		logger.info("this is [deleteproperties.do] start ...");
		if (data!=null&&!data.equals("")){
			try {
				logger.info("this is [deleteproperties.do] is decoding ...");
				data=URLDecoder.decode(data, "utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.info("this is [deleteproperties.do] occur error when program decoding ...");
				e.printStackTrace();
			}
		}
		
		JSONObject json=JSONObject.fromString(data);
		String ids=json.getString("propertyIds")==null||json.getString("propertyIds").equals("")||json.getString("propertyIds").equals("null")
				?"-1":json.getString("propertyIds");
		logger.info("this is [deleteproperties.do] show propertyIds ["+ids+"]");
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		try{
			
			logger.info("this is [deleteproperties.do] is deleting ...");
			sysPropertiesManagementService.deleteProperties(ids);
			parameters.put("status", 1);
			parameters.put("data", "operation success.");
		}catch(Exception ex){
			logger.info("this is [deleteproperties.do] delete error ...");
			ex.printStackTrace();
			parameters.put("status", 0);
			parameters.put("data", "operation failed, try again please.");
		}
		
		logger.info("this is [deleteproperties.do] end ...");
		return parameters;
	}
}
