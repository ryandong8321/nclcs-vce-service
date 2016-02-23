package org.ryan.nclcs.vce.dao.devicetoken;

import org.ryan.nclcs.vce.dao.INclcsVceServiceBaseDAO;
import org.ryan.nclcs.vce.entity.SysDeviceToken;

public interface ISysDeviceTokenManagementDAO extends INclcsVceServiceBaseDAO<SysDeviceToken, Integer>{
	
	public SysDeviceToken findDeviceTokenByUserId(Integer userId);

}
