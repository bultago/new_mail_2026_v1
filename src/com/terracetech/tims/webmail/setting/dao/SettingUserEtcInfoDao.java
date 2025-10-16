package com.terracetech.tims.webmail.setting.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.terracetech.tims.webmail.setting.vo.UserEtcInfoVO;
import com.terracetech.tims.webmail.setting.vo.UserInfoVO;
import com.terracetech.tims.webmail.setting.vo.UserPhotoVO;
import com.terracetech.tims.webmail.setting.vo.ZipcodeVO;

public class SettingUserEtcInfoDao extends SqlMapClientDaoSupport implements ISettingUserEtcInfoDao {
	
	public UserEtcInfoVO readUserEtcInfo (int mailUserSeq) {
		Object result = getSqlMapClientTemplate().queryForObject("Setting.readUserInfoEtc", mailUserSeq);
		return result instanceof UserEtcInfoVO ? (UserEtcInfoVO) result : null; 
	}

	public boolean modifyUserEtcInfo (UserEtcInfoVO vo) {
		return getSqlMapClientTemplate().update("Setting.modifyUserInfoEtc", vo) == 1;
	}
	
	public boolean saveUserEtcInfo (UserEtcInfoVO vo) {
		return getSqlMapClientTemplate().insert("Setting.saveUserInfoEtc", vo) == null;
	}
	
	public boolean deleteUserEtcInfo (int userSeq) {
		return getSqlMapClientTemplate().delete("Setting.deleteUserInfoEtc", userSeq) == 1;
	}
	
	public void initAutoSaveID(int userSeq){
		getSqlMapClientTemplate().update("Setting.initAutoSaveID");
	}
	
	@SuppressWarnings("unchecked")
	public List<ZipcodeVO> readZipcodeList(String dong, int skipResult, int pageBase) {
		dong = "%"+dong+"%";
		return getSqlMapClientTemplate().queryForList("Setting.readZipcode", dong, skipResult, pageBase);
	}
	
	public int readZipcodeListCount(String dong) {
		dong = "%"+dong+"%";
		return (Integer)getSqlMapClientTemplate().queryForObject("Setting.readZipcodeCount", dong);
	}
	
	public UserInfoVO readUserInfo (int mailUserSeq) {
		return (UserInfoVO)getSqlMapClientTemplate().queryForObject("Setting.readUserInfo", mailUserSeq);
	}
	
	public void modifyUserInfo(UserInfoVO userInfoVo) {
		getSqlMapClientTemplate().update("Setting.modifyUserInfo", userInfoVo);
	}
	
	public void modifyPKIUserDN(int mailUserSeq, String userDN) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("mailUserSeq", mailUserSeq);
		paramMap.put("userDN", userDN);
		getSqlMapClientTemplate().update("Setting.modifyUserDN", paramMap);
	}
	
	public void modifyMyPassword(int mailUserSeq, String password) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("mailUserSeq", mailUserSeq);
		paramMap.put("password", password);
		getSqlMapClientTemplate().update("Setting.modifyMyPassword", paramMap);
	}
	
	public void modifyMyPasswordChangeTime(int mailUserSeq, String currentTime) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("mailUserSeq", mailUserSeq);
		paramMap.put("currentTime", currentTime);
		getSqlMapClientTemplate().update("Setting.modifyMyPasswordChangeTime", paramMap);
	}
	
	public void modifyAutoSaveInfo(int mailUserSeq, int term, String mode){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("mailUserSeq", mailUserSeq);
		paramMap.put("autoSaveMode", mode);
		paramMap.put("autoSaveTerm", term);		
		getSqlMapClientTemplate().update("Setting.modifyAutoSaveTerm", paramMap);
	}	
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> readUserEtcInfoMap (int mailUserSeq) {
		Object result = getSqlMapClientTemplate().queryForObject("Setting.readUserEtcInfo", mailUserSeq);
		return result instanceof Map ? (Map)result : null;
	}
	
	public void updateMailHome(String mailUserSeq, String homeSet){
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("mailUserSeq", mailUserSeq);
		paramMap.put("home", homeSet);
		getSqlMapClientTemplate().update("Setting.modifyMailHome", paramMap);	
	}
	
	public void saveUserPhoto(UserPhotoVO photo) {
		getSqlMapClientTemplate().insert("Setting.saveUserPhoto", photo);
    }
	
	public void deleteUserPhoto(int userSeq) {
		getSqlMapClientTemplate().delete("Setting.deleteUserPhoto", userSeq);
	}

	public void modifyUserPhoto(UserPhotoVO photo) {
		getSqlMapClientTemplate().update("Setting.modifyUserPhoto", photo);
	}
	
	public UserPhotoVO readUserPhoto(int userSeq) {
		return (UserPhotoVO)getSqlMapClientTemplate().queryForObject("Setting.readUserPhoto", userSeq);
	}
	
}