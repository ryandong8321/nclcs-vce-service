package org.ryan.nclcs.vce.dao.devicetoken.impl;

import org.ryan.nclcs.vce.dao.NclcsVceServiceBaseDAOImpl;
import org.ryan.nclcs.vce.dao.devicetoken.ISysDeviceTokenManagementDAO;
import org.ryan.nclcs.vce.entity.SysDeviceToken;
import org.springframework.stereotype.Repository;

@Repository("sysDeviceTokenManagementDAO")
public class SysDeviceTokenManagementDAOImpl extends NclcsVceServiceBaseDAOImpl<SysDeviceToken, Integer> implements ISysDeviceTokenManagementDAO {

}
