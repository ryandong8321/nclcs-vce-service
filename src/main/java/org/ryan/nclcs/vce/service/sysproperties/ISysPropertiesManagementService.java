package org.ryan.nclcs.vce.service.sysproperties;

import java.util.List;
import java.util.Map;

import org.ryan.nclcs.vce.entity.SysProperties;
import org.ryan.nclcs.vce.service.INclcsVceServiceBaseService;

public interface ISysPropertiesManagementService extends INclcsVceServiceBaseService<SysProperties, Integer>{
	
	/**
	 * 根据查询条件查找Property
	 * @param parameters 查询条件
	 * @return
	 */
	public List<SysProperties> findProperties(Map<String, Object> parameters);
	
	/**
	 * 根据查询条件查找Property，为selecte2控件提供数据
	 * @param parameters 查询条件
	 * @return Map的Key包含id,text
	 */
	public List<Map<String,Object>> findProperty(Map<String, Object> parameters);
	
	/**
	 * 
	 * @param rowNum
	 * @param pageSize
	 * @param parameters
	 * @return
	 */
	public Map<String, Object> findProperty(Integer rowNum, Integer pageSize, Map<String, Object> parameters);
	
	/**
	 * 
	 * @param propertyId
	 * @param parentId
	 * @return
	 */
	public String findPropertyParents(Integer propertyId, Integer parentId);
	
	/**
	 * 为jstree树型控件提供数据
	 * @param parameters 查询条件
	 * @return Map的key包括id,text,parent,还可包括一个名为state的子Map，子Map的key包括opened,selected
	 */
	public List<Map<String,Object>> findPropertiesForTree(Map<String, Object> parameters);
	
	/**
	 * 批量删除系统属性
	 * @param propertyIds 多个系统属性的id，以','分隔
	 * @return
	 */
	public boolean deleteProperties(String propertyIds);

}
