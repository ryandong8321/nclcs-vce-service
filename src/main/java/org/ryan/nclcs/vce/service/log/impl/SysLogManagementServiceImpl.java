package org.ryan.nclcs.vce.service.log.impl;

import org.ryan.nclcs.vce.dao.log.ISysLogManagementDAO;
import org.ryan.nclcs.vce.entity.SysLogs;
import org.ryan.nclcs.vce.service.NclcsVceServiceBaseServiceImpl;
import org.ryan.nclcs.vce.service.log.ISysLogManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("sysLogManagementService")
public class SysLogManagementServiceImpl extends NclcsVceServiceBaseServiceImpl<SysLogs, Integer, ISysLogManagementDAO> implements ISysLogManagementService {

	@Autowired
	private ISysLogManagementDAO sysLogManagementDAO;
	
	@Override
	protected ISysLogManagementDAO getCurrentDAO() {
		return this.sysLogManagementDAO;
	}
}
