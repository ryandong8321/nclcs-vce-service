package org.ryan.nclcs.vce.dao.devicetoken;

import java.util.List;

import org.ryan.nclcs.vce.dao.INclcsVceServiceBaseDAO;
import org.ryan.nclcs.vce.entity.SysDeviceToken;

public interface ISysDeviceTokenManagementDAO extends INclcsVceServiceBaseDAO<SysDeviceToken, Integer>{
	
	public SysDeviceToken findDeviceTokenByUserId(Integer userId);
	
	public List<SysDeviceToken> findDeviceTokensByUserId(List<Integer> userId);

}
