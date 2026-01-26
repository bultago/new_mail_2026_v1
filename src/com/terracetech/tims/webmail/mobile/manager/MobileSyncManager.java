package com.terracetech.tims.webmail.mobile.manager;

import java.util.List;

import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mobile.dao.MobileSyncDao;
import com.terracetech.tims.webmail.mobile.vo.MobileSyncLogVO;
import com.terracetech.tims.webmail.mobile.vo.MobileSyncVO;
import com.terracetech.tims.webmail.util.FormatUtil;

/**
 * 
 * @author waitone
 * @since Tims7
 * @version 7.1.3 
 */
public class MobileSyncManager {

	private MobileSyncDao mobileSyncDao;

	public void setMobileSyncDao(MobileSyncDao mobileSyncDao) {
		this.mobileSyncDao = mobileSyncDao;
	}

	public void updateDeviceData(User user, String deviceId, String deviceType, String syncKey) {
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		int count = countMobileSync(mailUserSeq, deviceId);
		if(count == 0){
			MobileSyncVO vo = new MobileSyncVO();
			vo.setMailUserSeq(mailUserSeq);
			vo.setDeviceId(deviceId);
			vo.setDeviceType(deviceType);
			vo.setSyncKey(syncKey);
			vo.setPingCheck("off");
			
			saveMobileSync(vo);
		}
	}
	
	public void closePingCheck(int mailUserSeq, String deviceId){
		MobileSyncVO vo = new MobileSyncVO();
		vo.setMailUserSeq(mailUserSeq);
		vo.setDeviceId(deviceId);
		vo.setPingCheck("off");
		
		updateMobileSync(vo);
	}
	
	public void openPingCheck(int mailUserSeq, String deviceId){
		MobileSyncVO vo = new MobileSyncVO();
		vo.setMailUserSeq(mailUserSeq);
		vo.setDeviceId(deviceId);
		vo.setPingCheck("on");
		
		updateMobileSync(vo);
	}
	
	public void updatePingSyncDate(User user, String deviceId){
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		MobileSyncVO vo = new MobileSyncVO();
		vo.setMailUserSeq(mailUserSeq);
		vo.setDeviceId(deviceId);
		vo.setPingUpdateTime(FormatUtil.getBasicDateStr());
		vo.setPingCheck("off");
		
		updateMobileSync(vo);
	}
	
	public void updateCalendarInsertSyncDate(User user, String deviceId, String dateStr){
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		MobileSyncVO vo = new MobileSyncVO();
		vo.setMailUserSeq(mailUserSeq);
		vo.setDeviceId(deviceId);
		vo.setCalendarInsertSyncTime(dateStr);
		
		updateMobileSync(vo);
	}

	public void updateCalendarUpdateSyncDate(User user, String deviceId, String dateStr) {
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		MobileSyncVO vo = new MobileSyncVO();
		vo.setMailUserSeq(mailUserSeq);
		vo.setDeviceId(deviceId);
		vo.setCalendarUpdateSyncTime(dateStr);
		
		updateMobileSync(vo);
		
	}
	
	public void updateCalendarDeleteSyncDate(User user, String deviceId, String dateStr) {
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		MobileSyncVO vo = new MobileSyncVO();
		vo.setMailUserSeq(mailUserSeq);
		vo.setDeviceId(deviceId);
		vo.setCalendarDeleteSyncTime(dateStr);
		
		updateMobileSync(vo);
		
	}
	
	public void cleanContactsSyncDate(int mailUserSeq, String deviceId){
		
		MobileSyncVO vo = new MobileSyncVO();
		vo.setMailUserSeq(mailUserSeq);
		vo.setDeviceId(deviceId);
		vo.setContactsInsertSyncTime("");
		vo.setContactsUpdateSyncTime("");
		vo.setContactsDeleteSyncTime("");
		
		updateMobileSync(vo);
	}
	
	public void cleanCalendarSyncDate(int mailUserSeq, String deviceId){
		
		MobileSyncVO vo = new MobileSyncVO();
		vo.setMailUserSeq(mailUserSeq);
		vo.setDeviceId(deviceId);
		vo.setCalendarInsertSyncTime("");
		vo.setCalendarUpdateSyncTime("");
		vo.setCalendarDeleteSyncTime("");
		
		updateMobileSync(vo);
	}

	private void saveMobileSync(MobileSyncVO vo){
		int count = mobileSyncDao.countMobileSync(vo.getMailUserSeq(), vo.getDeviceId());
		if(count == 0){
			mobileSyncDao.insertMobileSync(vo);	
		}
	}
	
	public boolean isUseMobileSync(int mailUserSeq){
		int count = mobileSyncDao.countMobileSync(mailUserSeq);
		return count >= 1 ? true : false;
	}
	
	private int countMobileSync(int mailUserSeq, String deviceId){
		return mobileSyncDao.countMobileSync(mailUserSeq, deviceId);
	}
	
	private void updateMobileSync(MobileSyncVO vo) {
		mobileSyncDao.updateMobileSync(vo);		
	}
	
	public MobileSyncVO selectMobileSyncBySyncKey(int mailUserSeq, String syncKey){
		return mobileSyncDao.selectMobileSyncBySyncKey(mailUserSeq, syncKey);
	}
	
	public MobileSyncVO selectMobileSyncByDeviceId(int mailUserSeq, String deviceId){
		return mobileSyncDao.selectMobileSyncByDeviceId(mailUserSeq, deviceId);
	}
	
	public List<MobileSyncLogVO> selectMobileSyncLog(int mailUserSeq, String target, String fromDate){
		return mobileSyncDao.selectMobileSyncLog(mailUserSeq, target, fromDate);
	}
	
	@Deprecated
	public void saveContactsMobileSyncLog(int mailUserSeq, int memberSeq, String eventType){
		int count = mobileSyncDao.countMobileSync(mailUserSeq);
		boolean useCheck = (count >= 1) ? true : false;
		if(useCheck){
			mobileSyncDao.insertContactsMobileSyncLog(mailUserSeq, memberSeq, eventType);	
		}
	}
	
	public void saveCalendarMobileSyncLog(int mailUserSeq, int memberSeq, String eventType){
		int count = mobileSyncDao.countMobileSync(mailUserSeq);
		boolean useCheck = (count >= 1) ? true : false;
		if(useCheck){
//			mobileSyncDao.insertCalendarMobileSyncLog(mailUserSeq, memberSeq, eventType);	
		}
	}

	public  List<MobileSyncLogVO> selectMobileSyncLogByDate(int mailUserSeq, String fromDate) {
		return mobileSyncDao.selectMobileSyncLogByDate(mailUserSeq, fromDate);
	}

	public List<MobileSyncVO> selectMobileSync() {
		return mobileSyncDao.selectMobileSync();
	}
	
	public int countContactsEvent(int mailUserSeq, MobileSyncVO syncVo){
		return mobileSyncDao.countContactsEvent(mailUserSeq, syncVo);
	}
	
	public int countCaledarEvent(int mailUserSeq, MobileSyncVO syncVo) {
		return mobileSyncDao.countCaledarEvent(mailUserSeq, syncVo);
	}
	
	public void updateContactsInsertSyncDate(User user, String deviceId, String dateStr){
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		MobileSyncVO vo = new MobileSyncVO();
		vo.setMailUserSeq(mailUserSeq);
		vo.setDeviceId(deviceId);
		vo.setContactsInsertSyncTime(dateStr);
		
		updateMobileSync(vo);
	}

	public void updateContactsUpdateSyncDate(User user, String deviceId, String dateStr) {
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		MobileSyncVO vo = new MobileSyncVO();
		vo.setMailUserSeq(mailUserSeq);
		vo.setDeviceId(deviceId);
		vo.setContactsUpdateSyncTime(dateStr);
		
		updateMobileSync(vo);
		
	}
	
	public void updateContactsDeleteSyncDate(User user, String deviceId, String dateStr) {
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		MobileSyncVO vo = new MobileSyncVO();
		vo.setMailUserSeq(mailUserSeq);
		vo.setDeviceId(deviceId);
		vo.setContactsDeleteSyncTime(dateStr);
		
		updateMobileSync(vo);
		
	}
}
