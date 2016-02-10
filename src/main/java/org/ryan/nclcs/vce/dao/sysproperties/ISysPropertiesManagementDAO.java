package org.ryan.nclcs.vce.dao.sysproperties;

import java.util.Map;

import org.ryan.nclcs.vce.dao.INclcsVceServiceBaseDAO;
import org.ryan.nclcs.vce.entity.SysProperties;

public interface ISysPropertiesManagementDAO extends INclcsVceServiceBaseDAO<SysProperties, Integer>{
	
	public Map<String, Object> deleteProperties(String ids);
	
}
