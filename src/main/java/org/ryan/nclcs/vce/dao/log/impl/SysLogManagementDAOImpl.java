package org.ryan.nclcs.vce.dao.log.impl;

import org.ryan.nclcs.vce.dao.NclcsVceServiceBaseDAOImpl;
import org.ryan.nclcs.vce.dao.log.ISysLogManagementDAO;
import org.ryan.nclcs.vce.entity.SysLogs;
import org.springframework.stereotype.Repository;

@Repository("sysLogManagementDAO")
public class SysLogManagementDAOImpl extends NclcsVceServiceBaseDAOImpl<SysLogs, Integer> implements ISysLogManagementDAO {

}
