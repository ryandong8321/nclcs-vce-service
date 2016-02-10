package org.ryan.nclcs.vce.service.sysproperties.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ryan.nclcs.vce.dao.Pagination;
import org.ryan.nclcs.vce.dao.sysproperties.ISysPropertiesManagementDAO;
import org.ryan.nclcs.vce.entity.SysProperties;
import org.ryan.nclcs.vce.service.NclcsVceServiceBaseServiceImpl;
import org.ryan.nclcs.vce.service.sysproperties.ISysPropertiesManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("sysPropertiesManagementService")
public class SysPropertiesManagementServiceImpl extends NclcsVceServiceBaseServiceImpl<SysProperties, Integer, ISysPropertiesManagementDAO> implements ISysPropertiesManagementService {

	@Autowired
	private ISysPropertiesManagementDAO sysPropertiesManagementDAO;
	
	@Override
	protected ISysPropertiesManagementDAO getCurrentDAO() {
		return this.sysPropertiesManagementDAO;
	}
	
	@Override
	public List<SysProperties> findProperties(Map<String, Object> parameters){
		Object[] param = null;
		StringBuffer hql = new StringBuffer("From SysProperties sps where 1=1");
		if (parameters != null && !parameters.isEmpty()) {
			param = new Object[parameters.size()];
			int ind = 0;
			for (String key : parameters.keySet()) {
				hql.append(" and ");
				hql.append(key);
				hql.append(" = ?");
				param[ind++] = parameters.get(key);
			}
		}
		
		return this.getCurrentDAO().find(hql.toString(), param);
	}
	
	@Override
	public List<Map<String, Object>> findProperty(Map<String, Object> parameters) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Object[] param = null;
		StringBuffer hql = new StringBuffer("From SysProperties sps where 1=1");
		if (parameters != null && !parameters.isEmpty()) {
			param = new Object[parameters.size()];
			int ind = 0;
			for (String key : parameters.keySet()) {
				hql.append(" and ");
				hql.append(key);
				hql.append(" = ?");
				param[ind++] = parameters.get(key);
			}
		}
		List<SysProperties> lst = this.getCurrentDAO().find(hql.toString(), param);
		if (lst != null && !lst.isEmpty()) {
			Map<String, Object> map = null;
			for (SysProperties property : lst) {
				map = new HashMap<String, Object>();
				map.put("id", property.getId());
				map.put("text", property.getPropertyName());
				result.add(map);
			}
		}
		return result;
	}

	@Override
	public Map<String, Object> findProperty(Integer rowNum, Integer pageSize, Map<String, Object> parameters) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Object[] param = null;
		StringBuffer hql = new StringBuffer("from SysProperties sps where 1=1");

		Pagination<SysProperties> page = new Pagination<>();
		page.setFirst(rowNum);
		page.setPageSize(pageSize);

		if (parameters != null && !parameters.isEmpty()) {
			param = new Object[parameters.size()];
			int ind = 0;
			for (String key : parameters.keySet()) {
				hql.append(" and ");
				hql.append(key);
				hql.append(" = ?");
				param[ind++] = parameters.get(key);
			}
		}
		Pagination<SysProperties> resultPage = this.getCurrentDAO().findPage(page, hql.toString(), param);

		if (resultPage.getRows() != null && !resultPage.getRows().isEmpty()) {
			Map<String, Object> map = null;
			for (SysProperties property : resultPage.getRows()) {
				map = new HashMap<String, Object>();
				map.put("id", property.getId());
				map.put("propertyName", property.getPropertyName());
				result.add(map);
			}
		}

		resultMap.put("total", page.getTotal());
		resultMap.put("rows", result);
		return resultMap;
	}

	@Override
	public String findPropertyParents(Integer propertyId, Integer parentId) {
		return findParentId(parentId, "");
	}
	
	private String findParentId(Integer parentId,String parents){
		SysProperties property=this.getCurrentDAO().get(parentId);
		if (property!=null&&property.getPropertyParentId()!=-1){
			parents += findParentId(property.getPropertyParentId(), parents)+",";
		}
		parents+=property.getId();
		return parents;
	}

	@Override
	public List<Map<String, Object>> findPropertiesForTree(Map<String, Object> parameters) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Object[] param = null;
		StringBuffer hql = new StringBuffer("From SysProperties sps where 1=1");
		if (parameters != null && !parameters.isEmpty()) {
			param = new Object[parameters.size()];
			int ind = 0;
			for (String key : parameters.keySet()) {
				hql.append(" and ");
				hql.append(key);
				hql.append(" = ?");
				param[ind++] = parameters.get(key);
			}
		}
		
		hql.append(" order by propertyParentId , id");
		
		List<SysProperties> lst = this.getCurrentDAO().find(hql.toString(), param);
		if (lst != null && !lst.isEmpty()) {
			Map<String, Object> map = null;
			for (SysProperties property : lst) {
				map = new HashMap<String, Object>();
				map.put("id", property.getId());
				map.put("text", property.getPropertyName());
				map.put("parent", property.getPropertyParentId()==-1?"#":property.getPropertyParentId());
				if (property.getPropertyParentId()==-1){
					Map<String,Object> state=new HashMap<String,Object>();
					state.put("opened", true);
					state.put("selected", true);
					map.put("state", state);
				}
				result.add(map);
			}
		}
		return result;
	}

	@Override
	public boolean deleteProperties(String propertyIds) {
		String[] ids=propertyIds.split(",");
		Object[] obj=new Object[ids.length];
		StringBuffer hql=new StringBuffer("delete from SysProperties sps where id in (");
		for (int i=0;i<ids.length;i++){
			if (i==0){
				hql.append("?");
			}else{
				hql.append(",?");
			}
			obj[i]=Integer.parseInt(ids[i]);
		}
		hql.append(")");
		
		boolean flag=true;
		try{
			this.getCurrentDAO().batchExecute(hql.toString(), obj);
		}catch(Exception ex){
			ex.printStackTrace();
			flag=false;
		}
		return flag;
	}

}
