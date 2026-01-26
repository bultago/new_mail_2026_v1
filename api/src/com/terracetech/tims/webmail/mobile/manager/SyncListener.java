package com.terracetech.tims.webmail.mobile.manager;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mortbay.log.Log;
import org.mortbay.util.ajax.Continuation;

import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class SyncListener {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private static Map<String, Continuation> deviceSessionMap = new HashMap<String, Continuation>();
	
	public static void cancelListener(User user, String deviceId) {
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		String key = userSeq + ":" + deviceId;
		if(StringUtils.isEmpty(key))
			return;
		
		Continuation continuation = deviceSessionMap.get(key);
		if(continuation == null){
			return;
		}
			
		continuation.resume();
		
		MobileSyncManager manager = (MobileSyncManager) ApplicationBeanUtil.getApplicationBean("mobileSyncManager");
		manager.closePingCheck(userSeq, deviceId);
	}

	public static void removeListener(User user, String deviceId) {
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		String key = userSeq + ":" + deviceId;
		if(StringUtils.isEmpty(key))
			return;
		
		deviceSessionMap.remove(key);
	}
	
	public static void addListener(User user, String deviceId, Continuation continuation) {
		if(continuation == null)
			return;
		
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		String key = userSeq + ":" + deviceId;
		
		Continuation c = deviceSessionMap.get(key);
		if(c != null){
			c.resume();
		}
		
		deviceSessionMap.put(key, continuation);
		
		MobileSyncManager manager = (MobileSyncManager) ApplicationBeanUtil.getApplicationBean("mobileSyncManager");
		manager.openPingCheck(userSeq, deviceId);
	}
	
	public static void notifyPendingChanges(int userSeq, String deviceId, Object data){
		String key = userSeq + ":" + deviceId;
		
		Continuation continuation = deviceSessionMap.get(key);
		if(continuation == null){
			MobileSyncManager manager = (MobileSyncManager) ApplicationBeanUtil.getApplicationBean("mobileSyncManager");
			manager.closePingCheck(userSeq, deviceId);
			
			Log.debug("ActiveSync userSeq="+userSeq +" Invalid continuation");
			return;
		}
			
		
		Log.debug("ActiveSync userSeq="+userSeq +" continuation.resume()");
		continuation.setObject(data);
		continuation.resume();	
	}
	
	public static Object getChangeData(User user, String deviceId){
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		String key = userSeq + ":" + deviceId;
		if(StringUtils.isEmpty(key))
			return null;
		
		Continuation continuation = deviceSessionMap.get(key);
		if(continuation == null)
			return null;
		
		return continuation.getObject();
	}
}
