package com.terracetech.tims.webmail.mobile.manager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mortbay.log.Log;

import com.terracetech.tims.webmail.mobile.vo.MobileSyncVO;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * 
 * @author waitone
 * @since Tims7
 * @version 7.1.3 
 */
public class SyncCheckThread extends Thread {
	public Logger log = LoggerFactory.getLogger(this.getClass());
	
	private int periodTime = 10;
	private boolean stopFlag = false;	
	
	private String lastUpdateTime = "";
	
	public SyncCheckThread() {
		start();
	}

	public void run() {
		MobileSyncManager manager = (MobileSyncManager) ApplicationBeanUtil.getApplicationBean("mobileSyncManager");
		
		try {
			while (!stopFlag) {
				String curretTime = FormatUtil.getBasicDateStr();
				if(StringUtils.isEmpty(lastUpdateTime))
					lastUpdateTime = FormatUtil.getBasicDateStr();
				
				List<MobileSyncVO> mobileList = manager.selectMobileSync();
				for (MobileSyncVO mobile : mobileList) {
					log.debug("ActiveSync userSeq="+mobile.getMailUserSeq() +" deviceId="+ mobile.getDeviceId());
					
					int contactsEventCount = manager.countContactsEvent(mobile.getMailUserSeq(), mobile);
					if(contactsEventCount > 0){
						log.debug("ActiveSync userSeq="+mobile.getMailUserSeq() +" contactsEventCount="+ contactsEventCount);
						
						SyncListener.notifyPendingChanges(mobile.getMailUserSeq(), mobile.getDeviceId(), "contacts");
					}
					
					int calendarEventCount = manager.countCaledarEvent(mobile.getMailUserSeq(), mobile);
					if(calendarEventCount > 0){
						log.debug("ActiveSync userSeq="+mobile.getMailUserSeq() +" calendarEventCount="+ calendarEventCount);
						
						SyncListener.notifyPendingChanges(mobile.getMailUserSeq(), mobile.getDeviceId(), "calendar");
					}
				}
				
				try {
					lastUpdateTime = curretTime;
					Thread.sleep(periodTime * 1000);
				} catch (InterruptedException e) {
					log.error(e.getMessage(), e);
				}
			}
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			
			stopFlag = true;
		}
	}
}
