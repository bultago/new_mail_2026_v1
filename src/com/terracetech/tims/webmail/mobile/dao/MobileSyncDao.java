package com.terracetech.tims.webmail.mobile.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.terracetech.tims.webmail.mobile.vo.MobileSyncLogVO;
import com.terracetech.tims.webmail.mobile.vo.MobileSyncVO;
import com.terracetech.tims.webmail.util.FormatUtil;

/**
 * 
 * @author waitone
 * @since Tims7
 * @version 7.1.3 
 */
public class MobileSyncDao extends SqlMapClientDaoSupport {

	@SuppressWarnings("unchecked")
	public List<MobileSyncVO> selectMobileSyncList(int mailUserSeq){
		return getSqlMapClientTemplate().queryForList("mobile.selectMobileSyncList", mailUserSeq);
	}
	
	public MobileSyncVO selectMobileSync(int mailUserSeq, String syncKey){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("mailUserSeq", mailUserSeq);
		param.put("syncKey", syncKey);
		
		Object result = getSqlMapClientTemplate().queryForObject("mobile.selectMobileSync", param);
		if(result ==null)
			return null;
		
		return (MobileSyncVO) result;
	}
	
	public int countMobileSync(int mailUserSeq, String deviceId){
		MobileSyncVO vo = new MobileSyncVO();
		vo.setMailUserSeq(mailUserSeq);
		vo.setDeviceId(deviceId);
		
		Object result = getSqlMapClientTemplate().queryForObject("mobile.countMobileSync", vo);
		if(result != null && result instanceof Integer){
			return (Integer)result;
		}
		return 0;
	}
	
	public int countMobileSync(int mailUserSeq){
		Object result = getSqlMapClientTemplate().queryForObject("mobile.countMobileSync2", mailUserSeq);
		if(result != null && result instanceof Integer){
			return (Integer)result;
		}
		return 0;
	}
	
	public void deleteMobileSync(int mailUserSeq){
		getSqlMapClientTemplate().delete("mobile.deleteMobileSync", mailUserSeq);
	}
	
	public void deleteMobileSyncByDeviceId(int mailUserSeq, String deviceId){
		MobileSyncVO vo = new MobileSyncVO();
		vo.setMailUserSeq(mailUserSeq);
		vo.setDeviceId(deviceId);
		
		getSqlMapClientTemplate().delete("mobile.deleteMobileSync2", vo);
	}
	
	public void insertMobileSync(MobileSyncVO vo){
		getSqlMapClientTemplate().insert("mobile.insertMobileSync", vo);
	}
	
	public void updateMobileSync(MobileSyncVO vo){
		getSqlMapClientTemplate().update("mobile.updateMobileSync", vo);
	}
	
	public void insertMobileSyncLog(MobileSyncLogVO vo){
		getSqlMapClientTemplate().insert("mobile.insertMobileSyncLog", vo);
	}
	
	public void insertContactsMobileSyncLog(int mailUserSeq, int memberSeq, String eventType){
		MobileSyncLogVO vo = new MobileSyncLogVO();
		vo.setMailUserSeq(mailUserSeq);
		vo.setEventType(eventType);
		vo.setTarget("contacts");
		vo.setTargetValue(String.valueOf(memberSeq));
		vo.setUpdateTime(FormatUtil.getBasicDateStr());
		
		getSqlMapClientTemplate().insert("mobile.insertMobileSyncLog", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<MobileSyncLogVO> selectMobileSyncLog(int mailUserSeq, String target, String fromDate){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("mailUserSeq", mailUserSeq);
		param.put("target", target);
		param.put("fromDate", fromDate);
		
		return getSqlMapClientTemplate().queryForList("mobile.selectMobileSyncLogList", param); 
	}
	
	public void deleteMobileSyncLog(int mailUserSeq){
		getSqlMapClientTemplate().delete("mobile.deleteMobileSyncLog", mailUserSeq);
	}

	public MobileSyncVO selectMobileSyncBySyncKey(int mailUserSeq, String syncKey) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("mailUserSeq", mailUserSeq);
		param.put("syncKey", syncKey);
		
		Object result = getSqlMapClientTemplate().queryForObject("mobile.selectMobileSyncBySynckey", param);
		if(result ==null)
			return null;
		
		return (MobileSyncVO) result;
	}

	public MobileSyncVO selectMobileSyncByDeviceId(int mailUserSeq, String deviceId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("mailUserSeq", mailUserSeq);
		param.put("deviceId", deviceId);
		
		Object result = getSqlMapClientTemplate().queryForObject("mobile.selectMobileSyncByDeviceId", param);
		if(result ==null)
			return null;
		
		return (MobileSyncVO) result;
	}

	@SuppressWarnings("unchecked")
	public List<MobileSyncLogVO> selectMobileSyncLogByDate(int mailUserSeq, String fromDate) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("mailUserSeq", mailUserSeq);
		param.put("fromDate", fromDate);
		
		return getSqlMapClientTemplate().queryForList("mobile.selectMobileSyncLogListByDate", param); 
	}

	public void insertCalendarMobileSyncLog(int mailUserSeq, int memberSeq, String eventType) {
		MobileSyncLogVO vo = new MobileSyncLogVO();
		vo.setMailUserSeq(mailUserSeq);
		vo.setEventType(eventType);
		vo.setTarget("calendar");
		vo.setTargetValue(String.valueOf(memberSeq));
		vo.setUpdateTime(FormatUtil.getBasicDateStr());
		
		getSqlMapClientTemplate().insert("mobile.insertMobileSyncLog", vo);
		
	}

	@SuppressWarnings("unchecked")
	public List<MobileSyncVO> selectMobileSync() {
		return getSqlMapClientTemplate().queryForList("mobile.selectMobileSyncAllList"); 
	}

	public int countCaledarEvent(int mailUserSeq, MobileSyncVO syncVo) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("mailUserSeq", mailUserSeq);
		paramMap.put("insertDate", syncVo.getCalendarInsertSyncTime());
		paramMap.put("updateDate", syncVo.getCalendarUpdateSyncTime());
		paramMap.put("deleteDate", syncVo.getCalendarDeleteSyncTime());
		
		Object result = getSqlMapClientTemplate().queryForObject("mobile.selectCountSchedulerEvent", paramMap);
		if(result ==null)
			return 0;
		
		return (Integer) result;
	}
	
	public int countContactsEvent(int mailUserSeq, MobileSyncVO syncVo) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("mailUserSeq", mailUserSeq);
		param.put("insertDate", syncVo.getContactsInsertSyncTime());
		param.put("updateDate", syncVo.getContactsUpdateSyncTime());
		param.put("deleteDate", syncVo.getContactsDeleteSyncTime());
		
		Object result = getSqlMapClientTemplate().queryForObject("mobile.selectCountContactsEvent", param);
		if(result ==null)
			return 0;
		
		return (Integer) result;
	}
}
