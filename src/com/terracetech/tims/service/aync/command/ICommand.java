package com.terracetech.tims.service.aync.command;

import java.io.IOException;

import com.terracetech.tims.service.aync.util.WbxmlException;
import com.terracetech.tims.service.aync.util.WbxmlSerializer;

public interface ICommand {
	
	public int countSyncData();
	
	public void process(String deviceId, int windowSize);
	
	public void encodeResponse(WbxmlSerializer serializer)  throws IOException, WbxmlException;
	
}
