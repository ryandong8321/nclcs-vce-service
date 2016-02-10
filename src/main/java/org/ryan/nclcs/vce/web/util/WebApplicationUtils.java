package org.ryan.nclcs.vce.web.util;

import java.util.HashMap;
import java.util.Map;

public class WebApplicationUtils {
	
	private static Map<Integer, String> USER_LOGIN_TOKEN=new HashMap<Integer, String>();
	
	public static String getToken(Integer key){
		String token=null;
		if (USER_LOGIN_TOKEN.containsKey(key)){
			token=USER_LOGIN_TOKEN.get(key);
		}
		return token;
	}
	
	public static boolean setNewToken(Integer key, String parameter){
		boolean result=true;
		try {
			String token=key+System.currentTimeMillis()+parameter;
			USER_LOGIN_TOKEN.put(key, MD5.string2MD5(MD5.string2MD5(token)));
		} catch (Exception e) {
			result=false;
			e.printStackTrace();
		}
		return result;
	}
	
	public static boolean removeToken(Integer key){
		boolean result=true;
		try {
			USER_LOGIN_TOKEN.remove(key);
		} catch (Exception e) {
			result=false;
			e.printStackTrace();
		}
		return result;
	}
	
	public static boolean isHasThisKey(Integer key){
		return USER_LOGIN_TOKEN.containsKey(key);
	}

}
