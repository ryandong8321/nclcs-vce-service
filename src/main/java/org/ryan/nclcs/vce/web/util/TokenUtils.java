package org.ryan.nclcs.vce.web.util;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class TokenUtils {
	private volatile static TokenUtils tokenUtils=null;
	
	private long previous;
	
	public static final int PBKDF2_ITERATIONS = 1000;
	public static final int HASH_BYTE_SIZE = 24;
	public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";
	
	private Map<String, String> loginTokenMap;
	
	private Map<String, String> sessionTokenMap;
	
	private TokenUtils(){
		loginTokenMap=new ConcurrentHashMap<String, String>();
		sessionTokenMap=new ConcurrentHashMap<String, String>();
	}
	
	public static TokenUtils getInstance(){
		if (tokenUtils==null){
			synchronized (TokenUtils.class) {
				if (tokenUtils==null){
					tokenUtils=new TokenUtils();
				}
			}
		}
		return tokenUtils;
	}
	
	public synchronized String generateToken(String sessionId) {
        try {
            long current = System.currentTimeMillis();
            System.out.println(current);
            if (current == previous){
                current++;
            }
            previous = current;

            byte now[] = (new Long(current)).toString().getBytes();
            PBEKeySpec spec = new PBEKeySpec(sessionId.toCharArray(), now, PBKDF2_ITERATIONS, HASH_BYTE_SIZE * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
			return toHex(skf.generateSecret(spec).getEncoded());
        } catch (InvalidKeySpecException e) {
            return null;
        } catch (NoSuchAlgorithmException e) {
        	return null;
		}
    }
	
	public synchronized boolean setLoginTokenMap(String tokenName, String token){
		boolean result=true;
		try {
			if (loginTokenMap!=null){
				loginTokenMap.put(tokenName, token);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result=false;
		}
		return result;
	}
	
	public synchronized boolean setSessionTokenMap(String tokenName, String token){
		boolean result=true;
		try {
			if (sessionTokenMap!=null){
				sessionTokenMap.put(tokenName, token);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result=false;
		}
		return result;
	}
	
	public synchronized boolean validateLoginToken(String tokenName, String token){
		boolean result=false;
		try{
			if (loginTokenMap!=null&&loginTokenMap.containsKey(tokenName)&&token.equals(loginTokenMap.get(tokenName))){
				result=true;
				loginTokenMap.remove(tokenName);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}
	
	public synchronized boolean validateSessionToken(String tokenName, String token){
		boolean result=false;
		try{
			if (sessionTokenMap!=null&&sessionTokenMap.containsKey(tokenName)&&token==sessionTokenMap.get(tokenName)){
				result=true;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}
	
	public synchronized boolean removeSessionToken(String tokenName){
		boolean result=false;
		try{
			if (sessionTokenMap!=null&&sessionTokenMap.containsKey(tokenName)){
				result=true;
				sessionTokenMap.remove(tokenName);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}
	
	private static String toHex(byte[] array){
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0) 
            return String.format("%0" + paddingLength + "d", 0) + hex;
        else
            return hex;
    }
}
