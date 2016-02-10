package org.ryan.nclcs.vce.dao.sysgroups;

import java.util.Map;
import org.ryan.nclcs.vce.dao.INclcsVceServiceBaseDAO;
import org.ryan.nclcs.vce.entity.SysGroups;
import org.ryan.nclcs.vce.entity.SysUsers;

public interface ISysGroupsManagementDAO extends INclcsVceServiceBaseDAO<SysGroups, Integer>{
	
	public Map<String, Object> saveRelationship(String groupId, String userIds);
	
	public Map<String, Object> saveStudentGroupChange(SysUsers user, Integer changeToGroupId, SysUsers currentOperationUser);

}
