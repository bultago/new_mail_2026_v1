package com.terracetech.tims.service.aync;

import java.util.UUID;

import org.apache.log4j.Logger;

import com.terracetech.tims.webmail.exception.InvalidSyncKeyException;
import com.terracetech.tims.webmail.mobile.vo.MobileSyncVO;
import com.terracetech.tims.webmail.util.FormatUtil;

public class SyncKey {
	
	private Logger log = Logger.getLogger(this.getClass());
	
	private String digestSource;
	private String clientToken;
	private int clientVersion;
	
	public SyncKey(String clientKey, String digestSource) throws InvalidSyncKeyException {
		clientVersion = 0;
		this.digestSource = digestSource;
		if (clientKey.equals("0"))
			return;
		if (!clientKey.matches("\\{\\S+\\}\\d+")) {
			log.warn((new StringBuilder()).append(
					"SyncKey malformed: ").append(clientKey).append(
					"; resetting device").toString());
			throw new InvalidSyncKeyException();
		}
		String clientToken = null;
		try {
			clientToken = clientKey.substring(1, clientKey.lastIndexOf('}'));
			UUID uuid = UUID.nameUUIDFromBytes(digestSource.getBytes());
			UUID clientUuid = UUID.fromString(clientToken);
			if (!uuid.equals(clientUuid))
				throw new InvalidSyncKeyException();
			clientVersion = Integer.parseInt(clientKey.substring(clientKey.lastIndexOf('}') + 1));
		} catch (Exception t) {
			log.warn((new StringBuilder()).append("SyncKey error: ")
					.append(clientKey).append("; resetting device").toString());
			throw new InvalidSyncKeyException();
		}
	}

	public int getClientVersion() {
		return clientVersion;
	}

	public String getServerKey(int serverVersion) {
		String token = clientToken;
		if (token == null) {
			UUID uuid = UUID.nameUUIDFromBytes(digestSource.getBytes());
			token = uuid.toString().toUpperCase();
		}
		return (new StringBuilder()).append("{").append(token).append("}")
				.append(serverVersion).toString();
	}

	public static MobileSyncVO getSyncKey(String deviceId){
		MobileSyncVO syncVo = new MobileSyncVO();
		
		SyncKey syncKey = null;
		try {
//			syncKey = new SyncKey("0", FormatUtil.getBasicDateStr());
			syncKey = new SyncKey("0", deviceId);
		} catch (InvalidSyncKeyException e) {
			e.printStackTrace();
		}
		
		syncVo.setSyncKey(syncKey.getServerKey(1));
		
		return syncVo;
	}
}
