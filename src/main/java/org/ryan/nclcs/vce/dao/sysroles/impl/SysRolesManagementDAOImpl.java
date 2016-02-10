package org.ryan.nclcs.vce.dao.sysroles.impl;

import java.util.HashMap;
import java.util.Map;
import org.ryan.nclcs.vce.dao.NclcsVceServiceBaseDAOImpl;
import org.ryan.nclcs.vce.dao.sysroles.ISysRolesManagementDAO;
import org.ryan.nclcs.vce.entity.SysRoles;
import org.springframework.stereotype.Repository;

@Repository("sysRolesManagementDAO")
public class SysRolesManagementDAOImpl extends NclcsVceServiceBaseDAOImpl<SysRoles, Integer> implements ISysRolesManagementDAO {

	@Override
	public Map<String, Object> deleteRoles(String roleIds) {
		Map<String, Object> result=new HashMap<String, Object>();
		if (roleIds==null||roleIds.equals("")){
			result.put("status", 0);
			result.put("data", "parameters error");
		}
		if (result.isEmpty()){
//			StringBuffer hql=new StringBuffer("select count(*) from items_input_output iio where iio.bill_id in (");
//			hql.append(ids);
//			hql.append(") and iio.bill_verify <> 0");
//			List<?> lst=this.getSession().createSQLQuery(hql.toString()).list();
//			if (lst!=null&&!lst.isEmpty()){
//				Integer count=Integer.parseInt(lst.get(0).toString());
//				if (count>0){
//					result.put("status", 0);
//					result.put("data", "Some rows have verified, please check.");
//				}
//			}
			
			if (result.isEmpty()){
				String[] ids=null;
				if (roleIds!=null&&!roleIds.equals("")&&roleIds.contains(",")){
					ids=roleIds.split(",");
				}else if (roleIds!=null&&!roleIds.equals("")&&!roleIds.contains(",")){
					ids=new String[1];
					ids[0]=roleIds;
				}
				Object[] obj=new Object[ids.length];
				StringBuffer hql=new StringBuffer("delete from SysRoles srs where id in (");
				for (int i=0;i<ids.length;i++){
					if (i==0){
						hql.append("?");
					}else{
						hql.append(",?");
					}
					obj[i]=Integer.parseInt(ids[i]);
				}
				hql.append(")");
				try{
					this.batchExecute(hql.toString(), obj);
					result.put("status", 1);
					result.put("data", "Operation success.");
				}catch(Exception ex){
					ex.printStackTrace();
					result.put("status", 1);
					result.put("data", "Delete Roles failed, try again.");
				}
			}
		}
		return result;
	}

}
