package org.ryan.nclcs.vce.service.devicetoken.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.ryan.nclcs.vce.app.notification.UmengNotification;
import org.ryan.nclcs.vce.app.notification.android.AndroidCustomizedcast;
import org.ryan.nclcs.vce.app.notification.android.AndroidNotification;
import org.ryan.nclcs.vce.app.notification.ios.IOSCustomizedcast;
import org.ryan.nclcs.vce.dao.devicetoken.ISysDeviceTokenManagementDAO;
import org.ryan.nclcs.vce.entity.AppMissedNotification;
import org.ryan.nclcs.vce.entity.SysDeviceToken;
import org.ryan.nclcs.vce.entity.SysUsers;
import org.ryan.nclcs.vce.service.NclcsVceServiceBaseServiceImpl;
import org.ryan.nclcs.vce.service.appmissednotification.IAppMissedNotificationService;
import org.ryan.nclcs.vce.service.devicetoken.ISysDeviceTokenManagementService;
import org.ryan.nclcs.vce.web.util.WebUtilConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("sysDeviceTokenManagementService")
public class SysDeviceTokenManagementServiceImpl
		extends NclcsVceServiceBaseServiceImpl<SysDeviceToken, Integer, ISysDeviceTokenManagementDAO>
		implements ISysDeviceTokenManagementService {
	
	protected final Logger logger=LoggerFactory.getLogger(ISysDeviceTokenManagementService.class);
	
	@Autowired
	private IAppMissedNotificationService appMissedNotificationService;

	@Autowired
	private ISysDeviceTokenManagementDAO sysDeviceTokenManagementDAO;

	@Override
	protected ISysDeviceTokenManagementDAO getCurrentDAO() {
		return this.sysDeviceTokenManagementDAO;
	}

	@Override
	public List<SysDeviceToken> findDeviceTokenByUserId(List<Integer> userId) {
		return this.getCurrentDAO().findDeviceTokensByUserId(userId);
	}

	@Override
	public boolean sendNotificationToApp(List<SysDeviceToken> deviceTokens, String text, String title, String ticker) {
		boolean result = false;
		String status=null;
//		List<String> iOSTokens = new ArrayList<String>();
//		List<String> androidTokens = new ArrayList<String>();
		//APP use username as Alias to mark user, so send notification to user by Alias, this List save username(Alias)
		List<String> iOSAlias = new ArrayList<String>();
		List<String> androidAlias = new ArrayList<String>();
//		StringBuffer iOSAlias=new StringBuffer();
//		StringBuffer androidAlias=new StringBuffer();
		try {
			if (deviceTokens != null && !deviceTokens.isEmpty()) {
				for (SysDeviceToken device : deviceTokens) {
					if (device!=null&&0==device.getDeviceTokenType()){
//						iOSTokens.add(device.getDeviceTokenValue());
						iOSAlias.add(device.getSysUserName());
					}else if (device!=null&&1==device.getDeviceTokenType()){
//						androidTokens.add(device.getDeviceTokenValue());
						androidAlias.add(device.getSysUserName());
					}
				}

//				if (!iOSTokens.isEmpty()){
//					IOSListcast iListcast = new IOSListcast(WebUtilConstant._ios_appkey, WebUtilConstant._ios_appMasterSecret);
//					iListcast.setDeviceToken(iOSTokens);
//					iListcast.setAlert(text);
//					iListcast.setBadge(0);
//					iListcast.setSound("default");
////					iListcast.setProductionMode();
//					iListcast.setTestMode();
//					iListcast.setCustomizedField("test", "helloworld");
//					result=this.sendNotificationToApp(iListcast,WebUtilConstant._ios_appMasterSecret);
//				}
//				
//				if (!androidTokens.isEmpty()){
//					AndroidListcast aListcast=new AndroidListcast(WebUtilConstant._android_appkey, WebUtilConstant._android_appMasterSecret);
//					aListcast.setDeviceToken(androidTokens);
//					aListcast.setTicker(ticker);
//					aListcast.setTitle(title);
//					aListcast.setText(text);
//					aListcast.goAppAfterOpen();
//					aListcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
////					aListcast.setProductionMode();
//					aListcast.setTestMode();
//					aListcast.setExtraField("test", "helloworld");
//					result=this.sendNotificationToApp(aListcast, WebUtilConstant._android_appMasterSecret);
//				}
				String alias=null;
				if (!iOSAlias.isEmpty()){
					alias=StringUtils.join(iOSAlias.toArray(), ",");
					logger.info("Sending iOS notification all alias ["+alias+"]");
					IOSCustomizedcast iOSAliasCast=new IOSCustomizedcast(WebUtilConstant._ios_appkey, WebUtilConstant._ios_appMasterSecret);
					iOSAliasCast.setAlias(alias, WebUtilConstant.ALIASTYPE);
					iOSAliasCast.setAlert(text);
					iOSAliasCast.setBadge(1);
					iOSAliasCast.setSound("default");
//					iOSAliasCast.setProductionMode();
					iOSAliasCast.setTestMode();
					iOSAliasCast.setCustomizedField("test", "helloworld");
					status=this.sendNotificationToApp(iOSAliasCast,WebUtilConstant._ios_appMasterSecret);
					logger.info("Sending iOS notification ...");
					
					if (status!=null&&!status.equals("")){
						boolean hasSend=false;
						JSONObject notificationResult=JSONObject.fromBean(status);
						logger.info("Sending iOS notification result status ["+status+"]...");
						if (notificationResult.has("ret")){
							String ret=notificationResult.getString("ret");
							if (ret!=null&&!ret.equals("")&&ret.equals("SUCCESS")){
								hasSend=true;
							}else if (ret!=null&&!ret.equals("")&&ret.equals("FAIL")){
								hasSend=false;
							}
						}
						logger.info("Sending iOS notification hasSend ["+hasSend+"]...");
						AppMissedNotification missedNotification=null;
						JSONObject data=notificationResult.getJSONObject("data");
						ArrayList<AppMissedNotification> missedNotifications=new ArrayList<AppMissedNotification>();
				        if (hasSend&&data.has("missed")){
				        	String userName=null;
				        	JSONArray arr=data.getJSONArray("missed");
				        	for (int i=0;i<arr.length();i++){
				        		userName=arr.getString(i);
					        	logger.info("Sending iOS notification missed username ["+userName+"]");
					        	missedNotification=new AppMissedNotification();
					        	missedNotification.setSysUserName(userName);
					        	missedNotification.setDeviceTokenType(0);//iOS
					        	missedNotification.setNotifictionTitle(title);
					        	missedNotification.setNotifictionContent(text);
					        	missedNotifications.add(missedNotification);
					        }
				        	if (!missedNotifications.isEmpty()){
				        		logger.info("Sending iOS notification save missed username ...");
				        		appMissedNotificationService.saveMultipleMissedNotification(missedNotifications);
				        		logger.info("Sending iOS notification save missed username done...");
				        	}
				        }
				        if (!hasSend&&data.has("error_code")){
				        	for (String userName:iOSAlias){
					        	logger.info("Sending iOS notification failed username ["+userName+"]");
					        	missedNotification=new AppMissedNotification();
					        	missedNotification.setSysUserName(userName);
					        	missedNotification.setDeviceTokenType(0);//iOS
					        	missedNotification.setNotifictionTitle(title);
					        	missedNotification.setNotifictionContent(text);
					        	missedNotifications.add(missedNotification);
					        }
				        	if (!missedNotifications.isEmpty()){
				        		logger.info("Sending iOS notification save failed username ...");
				        		appMissedNotificationService.saveMultipleMissedNotification(missedNotifications);
				        		logger.info("Sending iOS notification save failed username done...");
				        	}
				        }
					}
				}
				
				if (!androidAlias.isEmpty()){
					AndroidCustomizedcast androidAliasCast=new AndroidCustomizedcast(WebUtilConstant._android_appkey, WebUtilConstant._android_appMasterSecret);
					alias=StringUtils.join(androidAlias.toArray(), ",");
					logger.info("Sending android notification all alias ["+alias+"]");
					androidAliasCast.setAlias(alias,WebUtilConstant.ALIASTYPE);
					androidAliasCast.setTicker(ticker);
					androidAliasCast.setTitle(title);
					androidAliasCast.setText(text);
					androidAliasCast.goAppAfterOpen();
					androidAliasCast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
//					androidAliasCast.setProductionMode();
					androidAliasCast.setTestMode();
					androidAliasCast.setExtraField("test", "helloworld");
					status=this.sendNotificationToApp(androidAliasCast, WebUtilConstant._android_appMasterSecret);
					logger.info("Sending android notification ...");
					
					if (status!=null&&!status.equals("")){
						logger.info("Sending android notification result status ["+status+"]...");
						boolean hasSend=false;
						JSONObject notificationResult=JSONObject.fromBean(status);
						if (notificationResult.has("ret")){
							String ret=notificationResult.getString("ret");
							if (ret!=null&&!ret.equals("")&&ret.equals("SUCCESS")){
								hasSend=true;
							}else if (ret!=null&&!ret.equals("")&&ret.equals("FAIL")){
								hasSend=false;
							}
						}
						logger.info("Sending android notification hasSend ["+hasSend+"]...");
						AppMissedNotification missedNotification=null;
						JSONObject data=notificationResult.getJSONObject("data");
						ArrayList<AppMissedNotification> missedNotifications=new ArrayList<AppMissedNotification>();
				        if (hasSend&&data.has("missed")){
				        	String userName=null;
				        	JSONArray arr=data.getJSONArray("missed");
				        	for (int i=0;i<arr.length();i++){
				        		userName=arr.getString(i);
					        	logger.info("Sending android notification missed username ["+userName+"]");
					        	missedNotification=new AppMissedNotification();
					        	missedNotification.setSysUserName(userName);
					        	missedNotification.setDeviceTokenType(0);//iOS
					        	missedNotification.setNotifictionTitle(title);
					        	missedNotification.setNotifictionContent(text);
					        	missedNotifications.add(missedNotification);
					        }
				        	if (!missedNotifications.isEmpty()){
				        		logger.info("Sending android notification save missed username ...");
				        		appMissedNotificationService.saveMultipleMissedNotification(missedNotifications);
				        		logger.info("Sending android notification save missed username done...");
				        	}
				        }
				        if (!hasSend&&data.has("error_code")){
				        	for (String userName:androidAlias){
					        	logger.info("Sending android notification failed username ["+userName+"]");
					        	missedNotification=new AppMissedNotification();
					        	missedNotification.setSysUserName(userName);
					        	missedNotification.setDeviceTokenType(0);//iOS
					        	missedNotification.setNotifictionTitle(title);
					        	missedNotification.setNotifictionContent(text);
					        	missedNotifications.add(missedNotification);
					        }
				        	if (!missedNotifications.isEmpty()){
				        		logger.info("Sending android notification save failed username ...");
				        		appMissedNotificationService.saveMultipleMissedNotification(missedNotifications);
				        		logger.info("Sending android notification save failed username done...");
				        	}
				        }
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result=false;
			logger.info("this is method [sendNotificationToApp] occur Exception...");
		}

		return result;
	}
	
	@Override
	public boolean sendDelayNotificationToApp(String userName) {
		List<AppMissedNotification> lstMissed=appMissedNotificationService.findMissedNotification(userName);
		if (lstMissed!=null&&!lstMissed.isEmpty()){
			int badge=lstMissed.size();
			String status=null;
			IOSCustomizedcast iOSAliasCast=null;
			AndroidCustomizedcast androidAliasCast=null;
			boolean canDelete=false;
			for (AppMissedNotification missed:lstMissed){
				if (missed.getDeviceTokenType()!=null&&missed.getDeviceTokenType().equals(0)){//iOS
					try {
						logger.info("Sending iOS delay notification username ["+missed.getSysUserName()+"]...");
						iOSAliasCast=new IOSCustomizedcast(WebUtilConstant._android_appkey, WebUtilConstant._android_appMasterSecret);
						iOSAliasCast.setAlias(missed.getSysUserName(), WebUtilConstant.ALIASTYPE);
						iOSAliasCast.setAlert(missed.getNotifictionContent());
						iOSAliasCast.setBadge(badge);
						iOSAliasCast.setSound("default");
//						iOSAliasCast.setProductionMode();
						iOSAliasCast.setTestMode();
						iOSAliasCast.setCustomizedField("test", "helloworld");
						status=this.sendNotificationToApp(iOSAliasCast,WebUtilConstant._ios_appMasterSecret);
						logger.info("Sending iOS delay notification status ["+status+"]...");
						if (status!=null&&!status.equals("")&&status.contains("SUCCESS")){
							canDelete=true;
						}
						logger.info("Sending iOS delay notification canDelete ["+canDelete+"]...");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else if (missed.getDeviceTokenType()!=null&&missed.getDeviceTokenType().equals(1)){//android
					try {
						logger.info("Sending android delay notification username ["+missed.getSysUserName()+"]...");
						androidAliasCast=new AndroidCustomizedcast(WebUtilConstant._android_appkey, WebUtilConstant._android_appMasterSecret);
						androidAliasCast.setAlias(userName,WebUtilConstant.POSTPATH);
						androidAliasCast.setTicker(missed.getNotifictionTitle());
						androidAliasCast.setTitle(missed.getNotifictionTitle());
						androidAliasCast.setText(missed.getNotifictionContent());
						androidAliasCast.goAppAfterOpen();
						androidAliasCast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
//						androidAliasCast.setProductionMode();
						androidAliasCast.setTestMode();
						androidAliasCast.setExtraField("test", "helloworld");
						status=this.sendNotificationToApp(androidAliasCast, WebUtilConstant._android_appMasterSecret);
						logger.info("Sending android delay notification status ["+status+"]...");
						if (status!=null&&!status.equals("")&&status.contains("SUCCESS")){
							canDelete=true;
						}
						logger.info("Sending android delay notification canDelete ["+canDelete+"]...");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			if (canDelete){
				logger.info("Sending android delay notification delete...");
				appMissedNotificationService.deleteMissedNotification(userName);
				logger.info("Sending android delay notification delete done...");
			}
		}
		return false;
	}
	
	private String sendNotificationToApp(UmengNotification notification, String secret){
		StringBuffer result = new StringBuffer();
		try {
			HttpClient client = new DefaultHttpClient();

			// send
			String timestamp = Integer.toString((int) (System.currentTimeMillis() / 1000));
			notification.setPredefinedKeyValue("timestamp", timestamp);
			String url = WebUtilConstant.HOST + WebUtilConstant.POSTPATH;
			String postBody = notification.getPostBody();
			String sign = DigestUtils.md5Hex(("POST" + url + postBody + secret).getBytes("utf8"));
			url = url + "?sign=" + sign;
			HttpPost post = new HttpPost(url);
			post.setHeader("User-Agent", WebUtilConstant.USER_AGENT);
			StringEntity se = new StringEntity(postBody, "UTF-8");
			post.setEntity(se);
			HttpResponse response = client.execute(post);
			int status = response.getStatusLine().getStatusCode();
			logger.info("this is method [sendNotificationToApp] Response Code ["+status+"]");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			logger.info("this is method [sendNotificationToApp] buf ["+result.toString()+"]");
			if (status == 200) {
				logger.info("this is method [sendNotificationToApp] alert [Notification sent successfully!]");
			} else {
				logger.info("this is method [sendNotificationToApp] alert [Failed to send the notification!]");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.info("this is method [sendNotificationToApp] occur Exception...");
		}
		
		return result.toString();
	}

	@Override
	public boolean setNewDeviceToken(Integer userId, String deviceToken) {
		boolean result=true;
		try {
//			List<SysDeviceToken> lst=null;
//			SysDeviceToken deviceToken=null;
			if (userId!=null&&!userId.equals(0)){
				SysDeviceToken device=null;
				StringBuffer hql=new StringBuffer("from SysDeviceToken sdt where sdt.sysUserId = ?");
				device=this.getCurrentDAO().findUnique(hql.toString(), userId);
				
				if (device==null){
					device=new SysDeviceToken();
				}
//				else if (deviceToken!=null){
//					device=lst.get(0);
//				}
				
				if (deviceToken.length()==44){
					device.setDeviceTokenType(1);
				}else if (deviceToken.length()==64){
					device.setDeviceTokenType(0);
				}
				device.setDeviceTokenValue(deviceToken);
				device.setSysUserId(userId);
				this.getCurrentDAO().save(device);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result=false;
		}
		return result;
	}

	@Override
	public boolean setNewDeviceToken(SysUsers sysUsers, String deviceToken) {
		boolean result=true;
		try {
			if (sysUsers!=null&&sysUsers.getId()!=null&&sysUsers.getUserName()!=null&&!sysUsers.getUserName().equals("")){
				SysDeviceToken device=null;
				StringBuffer hql=new StringBuffer("from SysDeviceToken sdt where sdt.sysUserId = ?");
				device=this.getCurrentDAO().findUnique(hql.toString(), sysUsers.getId());
				
				if (device==null){
					device=new SysDeviceToken();
				}
				
				if (deviceToken.length()==44){
					device.setDeviceTokenType(1);
				}else if (deviceToken.length()==64){
					device.setDeviceTokenType(0);
				}
				
				device.setDeviceTokenValue(deviceToken);
				device.setSysUserId(sysUsers.getId());
				device.setSysUserName(sysUsers.getUserName());
				
				this.getCurrentDAO().save(device);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result=false;
		}
		return result;
	}
}
