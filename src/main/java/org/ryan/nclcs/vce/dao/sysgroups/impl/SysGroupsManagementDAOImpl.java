package org.ryan.nclcs.vce.dao.sysgroups.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.ryan.nclcs.vce.dao.NclcsVceServiceBaseDAOImpl;
import org.ryan.nclcs.vce.dao.devicetoken.ISysDeviceTokenManagementDAO;
import org.ryan.nclcs.vce.dao.sysgroups.ISysGroupsManagementDAO;
import org.ryan.nclcs.vce.dao.sysnotification.ISysNotificationManagementDAO;
import org.ryan.nclcs.vce.dao.sysusers.ISysUsersManagementDAO;
import org.ryan.nclcs.vce.entity.SysDeviceToken;
import org.ryan.nclcs.vce.entity.SysGroups;
import org.ryan.nclcs.vce.entity.SysNotification;
import org.ryan.nclcs.vce.entity.SysNotificationDetail;
import org.ryan.nclcs.vce.entity.SysRoles;
import org.ryan.nclcs.vce.entity.SysUsers;
import org.ryan.nclcs.vce.service.devicetoken.ISysDeviceTokenManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("sysGroupsManagementDAO")
public class SysGroupsManagementDAOImpl extends NclcsVceServiceBaseDAOImpl<SysGroups, Integer> implements ISysGroupsManagementDAO {
	
	@Autowired
	private ISysUsersManagementDAO sysUsersManagementDAO;
	
	@Autowired
	private ISysNotificationManagementDAO sysNotificationManagementDAO;
	
	@Autowired
	private ISysDeviceTokenManagementDAO sysDeviceTokenManagementDAO;
	
	@Autowired
	private ISysDeviceTokenManagementService sysDeviceTokenManagementService;

	@Override
	public Map<String, Object> saveRelationship(String groupId, String userIds) {
		Map<String, Object> parameters=new HashMap<String, Object>();
		try{
			if (groupId==null||groupId.equals("-1")){
				parameters.put("status", 0);
				parameters.put("data", "操作失败，请重试!");
			}else{
				SysGroups group=this.get(Integer.parseInt(groupId));
				try {
					List<SysUsers> lstUsers=new ArrayList<SysUsers>();
					SysUsers sysUser=null;
					SysGroups parentGroup=null;
					//利用事务进行控制，方便数据库回滚
					Session session=this.sessionFactory.openSession();
					Transaction tran=session.beginTransaction();
					if (userIds!=null&&!userIds.equals("")){
						String[] arr=null;
						if (!userIds.contains(",")){
							arr=new String[1];
							arr[0]=userIds;
						}else{
							arr=userIds.split(",");
						}
						
						if (group.getGroupCategory()==1){//群组为班级时，才会将校区和班级加入到sys_users表中
							Integer[] nowGroupUsers=new Integer[group.getSysGroupsUsers().size()];
							for (int i=0;i<group.getSysGroupsUsers().size();i++){
								nowGroupUsers[i]=group.getSysGroupsUsers().get(i).getId();
							}
							
							//nowGroupUsers-现有的用户与群组的关系，在进行完比对之后，这个数组里留下来的就是要被删除的用户ID
							//keepGroupUsers-没有发生变化的用户与群组的关系，在进行完比对之后，这个数组里留下来的就是不需要发生变化的用户ID
							//arr-页面传过来的用户与群组的关系，在进行完比对之后，这个数组里留下来的就是需要新添加的用户ID
							List<Integer> keepGroupUsers=new ArrayList<Integer>();
							Integer ngu=-1,sendedUser=-1;
							boolean flag=false;
							for (int i=0;i<nowGroupUsers.length;i++){
								flag=false;
								ngu=nowGroupUsers[i];
								for (int j=0;j<arr.length;j++){
									sendedUser=Integer.parseInt(arr[j]);
									if (ngu.equals(sendedUser)){
										keepGroupUsers.add(sendedUser);
										arr[j]="0";
										flag=true;
										break;
									}
								}
								if (flag){
									nowGroupUsers[i]=0;
								}
							}
							
							//处理要被删除的与群组关系的用户，去掉sys_users表中user_vce_school_name和user_vce_class_name中的数据
							for (int i=0;i<nowGroupUsers.length;i++){
								if (nowGroupUsers[i]!=0){
									sysUser=sysUsersManagementDAO.get(nowGroupUsers[i]);
									if (group.getGroupCategory()==1){
										parentGroup=this.get(group.getGroupParentId());
										if (sysUser.getVceClassName()!=null&&!sysUser.getVceClassName().equals("")){
											sysUser.setVceClassName(sysUser.getVceClassName().replaceAll(group.getGroupName(), ""));
										}
										if (sysUser.getVceSchoolName()!=null&&!sysUser.getVceSchoolName().equals("")){
											sysUser.setVceSchoolName(sysUser.getVceSchoolName().replaceAll(parentGroup.getGroupName(), ""));
										}
									}
									sysUsersManagementDAO.save(sysUser);
								}
							}
							
							//处理没有发生变化的用户与群组关系，保持sys_users表中user_vce_school_name和user_vce_class_name中的数据不变
							for (int i=0;i<keepGroupUsers.size();i++){
								lstUsers.add(sysUsersManagementDAO.get(keepGroupUsers.get(i)));
							}
						}
						
						//处理新添加的用户与群组的关系，正常流程
						for (int ind=0;ind<arr.length;ind++){
							if (arr[ind].equals("0")){
								continue;
							}
							sysUser=sysUsersManagementDAO.get(Integer.parseInt(arr[ind]));
							if (group.getGroupCategory()==1){
								parentGroup=this.get(group.getGroupParentId());
								sysUser.setVceClassName(sysUser.getVceClassName()==null||sysUser.getVceClassName().equals("")
										?group.getGroupName():sysUser.getVceClassName()+"|"+group.getGroupName());
								sysUser.setVceSchoolName(sysUser.getVceSchoolName()==null||sysUser.getVceSchoolName().equals("")
										?parentGroup.getGroupName():sysUser.getVceSchoolName()+"|"+parentGroup.getGroupName());
							}
							lstUsers.add(sysUser);
						}
					}
					group.setSysGroupsUsers(lstUsers);
					this.save(group);
					tran.commit();
					session.flush();
					session.close();
					parameters.put("status", 1);
					parameters.put("data", "operation success!");
				} catch (NumberFormatException e) {
					parameters.put("status", 0);
					parameters.put("data", "操作失败，请重试!");
					e.printStackTrace();
				}
				
//				try{
//					this.save(group);
//					parameters.put("status", 1);
//					parameters.put("data", "operation success!");
//				}catch(Exception ex){
//					parameters.put("status", 0);
//					parameters.put("data", "操作失败，请重试!");
//					ex.printStackTrace();
//				}
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
			parameters.put("status", 0);
			parameters.put("data", "Operation was failed, try again.");
		}
		return parameters;
	}

	@Override
	public Map<String, Object> saveStudentGroupChange(SysUsers userStudent, Integer changeToGroupId, SysUsers currentOperationUser) {
		Map<String, Object> parameters=new HashMap<String, Object>();
		
		SysGroups groupNewClass=null;
		List<SysUsers> lstUsers=null;
		
		StringBuffer notificationMessage=new StringBuffer();
		notificationMessage.append(userStudent.getChineseName()+" 同学");
		StringBuffer receiveGroupId=new StringBuffer();
		
		try {
			
			List<SysDeviceToken> devices=new ArrayList<SysDeviceToken>();
			
			//add parent SysUsers object for student change class
			SysUsers parentUser=sysUsersManagementDAO.findStudentsParent(userStudent.getId());
			//end
			
			if (userStudent.getSysGroups()==null||userStudent.getSysGroups().isEmpty()){//未分配班级，直接分配保存
				groupNewClass=this.get(changeToGroupId);
				lstUsers=groupNewClass.getSysGroupsUsers();
				if (lstUsers==null||lstUsers.isEmpty()){
					lstUsers=new ArrayList<SysUsers>();
				}
				lstUsers.add(userStudent);
				groupNewClass.setSysGroupsUsers(lstUsers);
				this.save(groupNewClass);
			}else if (userStudent.getSysGroups()!=null&&!userStudent.getSysGroups().isEmpty()){//已分配班级
				SysGroups currentGroup=userStudent.getSysGroups().get(0);
				if (currentGroup.getId().equals(changeToGroupId)){//班级没有发生变化
					sysUsersManagementDAO.save(userStudent);
				}else{//班级发生变化
					Integer originalClassGroupId=currentGroup.getId(),originalSchoolGroupId=currentGroup.getGroupParentId();
					//新班级group类
					groupNewClass=this.get(changeToGroupId);
					userStudent.setVceClassName(groupNewClass.getGroupName());
					//新校区group类
					SysGroups groupNewSchool=this.get(groupNewClass.getGroupParentId());
					userStudent.setVceSchoolName(groupNewSchool.getGroupName());
					
					List<SysUsers> newSysGroupsUsersList=new ArrayList<SysUsers>();
					SysUsers tmpUser=null;
					//将原来群组里的SysUsers对象去除掉
					for (int index=0;index<currentGroup.getSysGroupsUsers().size();index++){
						tmpUser=currentGroup.getSysGroupsUsers().get(index);
//						if (!tmpUser.getId().equals(userStudent.getId())){
//							newSysGroupsUsersList.add(tmpUser);
//						}
						if (!tmpUser.getId().equals(userStudent.getId())){
							if (parentUser==null){//学生没家长
								newSysGroupsUsersList.add(tmpUser);
							}else if (!parentUser.getId().equals(tmpUser)){
								newSysGroupsUsersList.add(tmpUser);
							}
						}
					}
					currentGroup.setSysGroupsUsers(newSysGroupsUsersList);
					
					//在新组里加入SysUsers对象
					if (groupNewClass.getSysGroupsUsers()==null||groupNewClass.getSysGroupsUsers().isEmpty()){
						lstUsers=new ArrayList<SysUsers>();
					}else if (groupNewClass.getSysGroupsUsers()!=null&&!groupNewClass.getSysGroupsUsers().isEmpty()){
						lstUsers=groupNewClass.getSysGroupsUsers();
					}
					lstUsers.add(userStudent);
					
					//add parent
					if (parentUser!=null){
						lstUsers.add(parentUser);
					}
					//end
					
					groupNewClass.setSysGroupsUsers(lstUsers);
					
					List<SysUsers> lstNotifiedUsers=new ArrayList<SysUsers>();
					//判断学生是否变更校区
					if (groupNewClass.getGroupParentId()==originalSchoolGroupId){//只是变更班级, 只给新班级老师，原班级老师，和学生发通知
						//新班级老师
						if (groupNewClass.getSysGroupsUsers()!=null){
							for (SysUsers newClassUsers:groupNewClass.getSysGroupsUsers()){
								if (newClassUsers.getSysRoles()!=null&&!newClassUsers.getSysRoles().isEmpty()){
									for (SysRoles newRole:newClassUsers.getSysRoles()){
										if (newRole.getId()==3){
											lstNotifiedUsers.add(newClassUsers);
											//find mobile device token for sending notification to user mobile phone
											devices.add(sysDeviceTokenManagementDAO.findDeviceTokenByUserId(newClassUsers.getId()));
											//end
											break;
										}
									}
								}
							}
						}
						
						//原班级老师
						SysGroups originalClassGroup=this.get(originalClassGroupId);
						if (originalClassGroup.getSysGroupsUsers()!=null){
							for (SysUsers originalClassUsers:originalClassGroup.getSysGroupsUsers()){
								if (originalClassUsers.getSysRoles()!=null&&!originalClassUsers.getSysRoles().isEmpty()){
									for (SysRoles newRole:originalClassUsers.getSysRoles()){
										if (newRole.getId()==4){
											lstNotifiedUsers.add(originalClassUsers);
											//find mobile device token for sending notification to user mobile phone
											devices.add(sysDeviceTokenManagementDAO.findDeviceTokenByUserId(originalClassUsers.getId()));
											//end
											break;
										}
									}
								}
							}
						}
						//学生
						lstNotifiedUsers.add(userStudent);
						//find mobile device token for sending notification to user mobile phone
						devices.add(sysDeviceTokenManagementDAO.findDeviceTokenByUserId(userStudent.getId()));
						//end
						
						//家长
						if (parentUser!=null){
							lstNotifiedUsers.add(parentUser);
							//find mobile device token for sending notification to user mobile phone
							devices.add(sysDeviceTokenManagementDAO.findDeviceTokenByUserId(parentUser.getId()));
							//end
						}
						
						notificationMessage.append("由 [");
						notificationMessage.append(groupNewSchool.getGroupName()+"] 校区－[");
						notificationMessage.append(originalClassGroup.getGroupName()+ "] 转至 [");
						notificationMessage.append(groupNewSchool.getGroupName()+"] 校区－[");
						notificationMessage.append(groupNewClass.getGroupName()+"]");
						notificationMessage.append("\n\n特此通知");
						
						receiveGroupId.append(groupNewSchool.getId()+","+groupNewClass.getId()+","+originalClassGroup.getId());
						
					}else{//校区和班级都发生变化，给新校区助理，原校区助理，新班级老师，原班级老师和学生发通知
						//新班级老师
						if (groupNewClass.getSysGroupsUsers()!=null){
							for (SysUsers newClassUsers:groupNewClass.getSysGroupsUsers()){
								if (newClassUsers.getSysRoles()!=null&&!newClassUsers.getSysRoles().isEmpty()){
									for (SysRoles newRole:newClassUsers.getSysRoles()){
										if (newRole.getId()==4){
											lstNotifiedUsers.add(newClassUsers);
											//find mobile device token for sending notification to user mobile phone
											devices.add(sysDeviceTokenManagementDAO.findDeviceTokenByUserId(newClassUsers.getId()));
											//end
											break;
										}
									}
								}
							}
						}
						//新校区助理
//						SysGroups groupNewSchool=this.get(groupNewClass.getGroupParentId());
						if (groupNewSchool.getSysGroupsUsers()!=null){
							for (SysUsers newSchoolUsers:groupNewSchool.getSysGroupsUsers()){
								if (newSchoolUsers.getSysRoles()!=null&&!newSchoolUsers.getSysRoles().isEmpty()){
									for (SysRoles newRole:newSchoolUsers.getSysRoles()){
										if (newRole.getId()==3){
											lstNotifiedUsers.add(newSchoolUsers);
											//find mobile device token for sending notification to user mobile phone
											devices.add(sysDeviceTokenManagementDAO.findDeviceTokenByUserId(newSchoolUsers.getId()));
											//end
											break;
										}
									}
								}
							}
						}
						
						//原班级老师
						SysGroups originalClassGroup=this.get(originalClassGroupId);
						if (originalClassGroup.getSysGroupsUsers()!=null){
							for (SysUsers originalClassUsers:originalClassGroup.getSysGroupsUsers()){
								if (originalClassUsers.getSysRoles()!=null&&!originalClassUsers.getSysRoles().isEmpty()){
									for (SysRoles newRole:originalClassUsers.getSysRoles()){
										if (newRole.getId()==3){
											lstNotifiedUsers.add(originalClassUsers);
											//find mobile device token for sending notification to user mobile phone
											devices.add(sysDeviceTokenManagementDAO.findDeviceTokenByUserId(originalClassUsers.getId()));
											//end
											break;
										}
									}
								}
							}
						}
						//原校区助理
						SysGroups originalSchoolGroup=this.get(originalSchoolGroupId);
						if (originalSchoolGroup.getSysGroupsUsers()!=null){
							for (SysUsers originalSchoolUsers:originalSchoolGroup.getSysGroupsUsers()){
								if (originalSchoolUsers.getSysRoles()!=null&&!originalSchoolUsers.getSysRoles().isEmpty()){
									for (SysRoles newRole:originalSchoolUsers.getSysRoles()){
										if (newRole.getId()==3){
											lstNotifiedUsers.add(originalSchoolUsers);
											//find mobile device token for sending notification to user mobile phone
											devices.add(sysDeviceTokenManagementDAO.findDeviceTokenByUserId(originalSchoolUsers.getId()));
											//end
											break;
										}
									}
								}
							}
						}
						//学生
						lstNotifiedUsers.add(userStudent);
						//find mobile device token for sending notification to user mobile phone
						devices.add(sysDeviceTokenManagementDAO.findDeviceTokenByUserId(userStudent.getId()));
						//end
						
						//家长
						if (parentUser!=null){
							lstNotifiedUsers.add(parentUser);
							//find mobile device token for sending notification to user mobile phone
							devices.add(sysDeviceTokenManagementDAO.findDeviceTokenByUserId(parentUser.getId()));
							//end
						}
						
						notificationMessage.append("由 [");
						notificationMessage.append(originalSchoolGroup.getGroupName()+"] 校区－[");
						notificationMessage.append(originalClassGroup.getGroupName()+ "] 转至 [");
						notificationMessage.append(groupNewSchool.getGroupName()+"] 校区－[");
						notificationMessage.append(groupNewClass.getGroupName()+"]");
						notificationMessage.append("\n\n特此通知");
						
						receiveGroupId.append(groupNewSchool.getId()+","+groupNewClass.getId()+","+originalSchoolGroup.getId()+","+originalClassGroup.getId());
					}
					
					SysNotification notification=new SysNotification();
					notification.setIsSend(1);
					notification.setNotificationMessage(notificationMessage.toString());
					notification.setNotificationReceiveGroupIds(receiveGroupId.toString());
					notification.setNotificationTitle(userStudent.getChineseName()+" 同学转班通知");
					notification.setNotificationUserInfo(currentOperationUser);
					notification.setSysNotificationDetailInfo(null);
					
					List<SysNotificationDetail> notificationDetails=new ArrayList<SysNotificationDetail>();
					SysNotificationDetail detail=null;
					for(SysUsers receiveUser:lstNotifiedUsers){
						detail=new SysNotificationDetail();
						detail.setIsRead(0);
						detail.setDetailReceiveUserInfo(receiveUser);
						detail.setDetailNotificationInfo(notification);
						notificationDetails.add(detail);
					}
					notification.setSysNotificationDetailInfo(notificationDetails);
					
					Session session=null;
					try {
						session = this.sessionFactory.openSession();
						Transaction tran = session.beginTransaction();
						this.save(currentGroup);
						this.save(groupNewClass);
						sysNotificationManagementDAO.save(notification);
						tran.commit();
						
						//send notification to mobile
						sysDeviceTokenManagementService.sendNotificationToApp(devices, notification.getNotificationTitle(), "通知", "通知");
						//end
					} finally {
						if (session!=null){
							session.flush();
							session.close();
						}
					}
				}
			}
			parameters.put("status", 1);
			parameters.put("data", "operation success!");
		} catch (HibernateException e) {
			e.printStackTrace();
			parameters.put("status", 0);
			parameters.put("data", "operation failed!");
		}
		return parameters;
	}
}
