package org.ryan.nclcs.vce.dao.devicetoken.impl;

import java.util.List;
import org.ryan.nclcs.vce.dao.NclcsVceServiceBaseDAOImpl;
import org.ryan.nclcs.vce.dao.devicetoken.ISysDeviceTokenManagementDAO;
import org.ryan.nclcs.vce.entity.SysDeviceToken;
import org.springframework.stereotype.Repository;

@Repository("sysDeviceTokenManagementDAO")
public class SysDeviceTokenManagementDAOImpl extends NclcsVceServiceBaseDAOImpl<SysDeviceToken, Integer> implements ISysDeviceTokenManagementDAO {

	@Override
	public SysDeviceToken findDeviceTokenByUserId(Integer userId) {
		SysDeviceToken deviceToken=null;
		String hql="from SysDeviceToken sdt where sdt.sysUserId = ?";
		List<SysDeviceToken> lst=this.find(hql, userId);
		if (lst!=null&&!lst.isEmpty()){
			deviceToken=lst.get(0);
		}
		return deviceToken;
	}

	@Override
	public List<SysDeviceToken> findDeviceTokensByUserId(List<Integer> userId) {
		List<SysDeviceToken> result = null;
		StringBuffer hql = new StringBuffer("from SysDeviceToken sdt");
		if (userId != null && !userId.isEmpty()) {
			hql.append(" where sdt.sysUserId in (");
			for (int idx = 0; idx < userId.size(); idx++) {
				if (idx == 0) {
					hql.append("?");
				} else {
					hql.append(",?");
				}
			}
			hql.append(")");
		}
		result = this.find(hql.toString(), userId.toArray());
		return result;
	}

}
