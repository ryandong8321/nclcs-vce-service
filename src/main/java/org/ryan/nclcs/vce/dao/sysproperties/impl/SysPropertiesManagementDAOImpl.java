package org.ryan.nclcs.vce.dao.sysproperties.impl;

import java.util.Map;

import org.ryan.nclcs.vce.dao.NclcsVceServiceBaseDAOImpl;
import org.ryan.nclcs.vce.dao.sysproperties.ISysPropertiesManagementDAO;
import org.ryan.nclcs.vce.entity.SysProperties;
import org.springframework.stereotype.Repository;

@Repository("sysPropertiesManagementDAO")
public class SysPropertiesManagementDAOImpl extends NclcsVceServiceBaseDAOImpl<SysProperties, Integer> implements ISysPropertiesManagementDAO {

	@Override
	public Map<String, Object> deleteProperties(String ids) {
		return null;
	}

}
