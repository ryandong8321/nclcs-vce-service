package org.ryan.nclcs.vce.service.devicetoken.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.ryan.nclcs.vce.app.notification.UmengNotification;
import org.ryan.nclcs.vce.app.notification.android.AndroidListcast;
import org.ryan.nclcs.vce.app.notification.android.AndroidNotification;
import org.ryan.nclcs.vce.app.notification.ios.IOSListcast;
import org.ryan.nclcs.vce.dao.devicetoken.ISysDeviceTokenManagementDAO;
import org.ryan.nclcs.vce.entity.SysDeviceToken;
import org.ryan.nclcs.vce.service.NclcsVceServiceBaseServiceImpl;
import org.ryan.nclcs.vce.service.devicetoken.ISysDeviceTokenManagementService;
import org.ryan.nclcs.vce.web.util.WebUtilConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("sysDeviceTokenManagementService")
public class SysDeviceTokenManagementServiceImpl
		extends NclcsVceServiceBaseServiceImpl<SysDeviceToken, Integer, ISysDeviceTokenManagementDAO>
		implements ISysDeviceTokenManagementService {
	
	protected final Logger logger=LoggerFactory.getLogger(ISysDeviceTokenManagementService.class);

	@Autowired
	private ISysDeviceTokenManagementDAO sysDeviceTokenManagementDAO;

	@Override
	protected ISysDeviceTokenManagementDAO getCurrentDAO() {
		return this.sysDeviceTokenManagementDAO;
	}

	@Override
	public List<SysDeviceToken> findDeviceTokenByUserId(List<Integer> userId) {
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
		result = this.getCurrentDAO().find(hql.toString(), userId.toArray());
		return result;
	}

	@Override
	public boolean sendNotificationToApp(List<SysDeviceToken> deviceTokens, String text, String title, String ticker) {
		boolean result = false;
		List<String> iOSTokens = new ArrayList<String>();
		List<String> androidTokens = new ArrayList<String>();
		try {
			if (deviceTokens != null && !deviceTokens.isEmpty()) {
				for (SysDeviceToken device : deviceTokens) {
					if (device.getDeviceTokenType()==0){
						iOSTokens.add(device.getDeviceTokenValue());
					}else if (device.getDeviceTokenType()==1){
						iOSTokens.add(device.getDeviceTokenValue());
					}
					
				}

				if (!iOSTokens.isEmpty()){
					IOSListcast iListcast = new IOSListcast(WebUtilConstant._ios_appkey, WebUtilConstant._ios_appMasterSecret);
					// TODO Set your device token
					iListcast.setDeviceToken(iOSTokens);
					iListcast.setAlert(text);
					iListcast.setBadge(0);
					iListcast.setSound("default");
					// TODO set 'production_mode' to 'true' if your app is under
					// production mode
					iListcast.setTestMode();
					// Set customized fields
					iListcast.setCustomizedField("test", "helloworld");
					result=this.sendNotificationToApp(iListcast,WebUtilConstant._ios_appMasterSecret);
				}
				
				if (!androidTokens.isEmpty()){
					AndroidListcast aListcast=new AndroidListcast(WebUtilConstant._android_appkey, WebUtilConstant._android_appMasterSecret);
					aListcast.setDeviceToken(androidTokens);
					aListcast.setTicker(ticker);
					aListcast.setTitle(title);
					aListcast.setText(text);
					aListcast.goAppAfterOpen();
					aListcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
					// TODO Set 'production_mode' to 'false' if it's a test device. 
					// For how to register a test device, please see the developer doc.
					aListcast.setProductionMode();
					// Set customized fields
					aListcast.setExtraField("test", "helloworld");
					result=this.sendNotificationToApp(aListcast, WebUtilConstant._android_appMasterSecret);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result=false;
			logger.info("this is method [sendNotificationToApp] occur Exception...");
		}

		return result;
	}
	
	private boolean sendNotificationToApp(UmengNotification notification, String secret){
		boolean result=false;
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
			// Send the post request and get the response
			HttpResponse response = client.execute(post);
			int status = response.getStatusLine().getStatusCode();
			logger.info("this is method [sendNotificationToApp] Response Code ["+status+"]");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer buf = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				buf.append(line);
			}
			logger.info("this is method [sendNotificationToApp] buf ["+buf.toString()+"]");
			if (status == 200) {
				result=true;
				logger.info("this is method [sendNotificationToApp] alert [Notification sent successfully!]");
			} else {
				result=false;
				logger.info("this is method [sendNotificationToApp] alert [Failed to send the notification!]");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result=false;
			logger.info("this is method [sendNotificationToApp] occur Exception...");
		}
		
		return result;
	}

	@Override
	public boolean setNewDeviceToken(Integer userId, String deviceToken) {
		boolean result=true;
		try {
			List<SysDeviceToken> lst=null;
			if (userId!=null&&!userId.equals(0)){
				SysDeviceToken device=null;
				StringBuffer hql=new StringBuffer("from SysDeviceToken sdt where sdt.sysUserId = ?");
				lst=this.getCurrentDAO().findUnique(hql.toString(), userId);
				
				if (lst==null||lst.isEmpty()){
					device=new SysDeviceToken();
				}else if (lst!=null&&!lst.isEmpty()){
					device=lst.get(0);
				}
				
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
}
