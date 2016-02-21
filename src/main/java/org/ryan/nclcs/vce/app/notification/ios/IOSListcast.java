package org.ryan.nclcs.vce.app.notification.ios;

import java.util.List;

public class IOSListcast extends IOSNotification {
	public IOSListcast(String appkey, String appMasterSecret) throws Exception {
		setAppMasterSecret(appMasterSecret);
		setPredefinedKeyValue("appkey", appkey);
		this.setPredefinedKeyValue("type", "listcast");
	}

	public void setDeviceToken(List<String> tokens) throws Exception {
		setPredefinedKeyValue("device_tokens", tokens);
	}

}
