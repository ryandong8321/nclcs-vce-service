package org.ryan.nclcs.vce.app.notification.android;

import java.util.List;

public class AndroidListcast extends AndroidNotification {
	public AndroidListcast(String appkey, String appMasterSecret) throws Exception {
		setAppMasterSecret(appMasterSecret);
		setPredefinedKeyValue("appkey", appkey);
		this.setPredefinedKeyValue("type", "unicast");
	}

	public void setDeviceToken(List<String> tokens) throws Exception {
		setPredefinedKeyValue("device_tokens", tokens);
	}

}
