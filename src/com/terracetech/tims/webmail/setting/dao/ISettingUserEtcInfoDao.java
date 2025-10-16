package com.terracetech.tims.webmail.setting.dao;

import java.util.List;
import java.util.Map;

import com.terracetech.tims.webmail.setting.vo.UserEtcInfoVO;
import com.terracetech.tims.webmail.setting.vo.UserInfoVO;
import com.terracetech.tims.webmail.setting.vo.UserPhotoVO;
import com.terracetech.tims.webmail.setting.vo.ZipcodeVO;

public interface ISettingUserEtcInfoDao {

	public abstract UserEtcInfoVO readUserEtcInfo(int mailUserSeq);

	public abstract boolean modifyUserEtcInfo(UserEtcInfoVO vo);

	public abstract boolean saveUserEtcInfo(UserEtcInfoVO vo);

	public abstract boolean deleteUserEtcInfo(int userSeq);

	public void initAutoSaveID(int userSeq);
	
	public List<ZipcodeVO> readZipcodeList(String dong, int skipResult, int pageBase);
	
	public int readZipcodeListCount(String dong);
	
	public UserInfoVO readUserInfo (int mailUserSeq);
	
	public void modifyUserInfo(UserInfoVO userInfoVo);
	
	public void modifyPKIUserDN(int mailUserSeq, String userDN);
	
	public void modifyMyPassword(int mailUserSeq, String password);
	
	public void modifyMyPasswordChangeTime(int mailUserSeq, String currentTime);
	
	public void modifyAutoSaveInfo(int mailUserSeq, int term, String mode);
	
	public Map<String, Object> readUserEtcInfoMap (int mailUserSeq);
	
	public void updateMailHome(String mailUserSeq, String homeSet);
	
	
	public void saveUserPhoto(UserPhotoVO photo);
	
	public void deleteUserPhoto(int userSeq);
	
	public void modifyUserPhoto(UserPhotoVO photo);
	
	public UserPhotoVO readUserPhoto(int userSeq);
	
}