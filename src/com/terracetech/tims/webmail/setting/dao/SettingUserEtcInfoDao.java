package com.terracetech.tims.webmail.setting.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.terracetech.tims.webmail.setting.vo.UserEtcInfoVO;

/**
 * SettingUserEtcInfoDao MyBatis Mapper Interface
 * 원본: SettingUserEtcInfoDao extends SqlSessionDaoSupport
 * 총 메서드 수: 19개
 */
@Mapper
public interface SettingUserEtcInfoDao {
    /** 원본: public UserEtcInfoVO readUserEtcInfo(int mailUserSeq) */
    UserEtcInfoVO readUserEtcInfo(@Param("mailUserSeq") int mailUserSeq);
    
    /** 원본: public boolean modifyUserEtcInfo(UserEtcInfoVO vo) */
    boolean modifyUserEtcInfo(UserEtcInfoVO vo);
    
    /** 원본: public boolean saveUserEtcInfo(UserEtcInfoVO vo) */
    boolean saveUserEtcInfo(UserEtcInfoVO vo);
    
    /** 원본: public boolean deleteUserEtcInfo(int userSeq) */
    boolean deleteUserEtcInfo(@Param("userSeq") int userSeq);
    
    /** 원본: public void initAutoSaveID(int userSeq) */
    void initAutoSaveID(@Param("userSeq") int userSeq);
    
    String readAutoSaveFolder(@Param("userSeq") int userSeq);
    String readSendedItemFolder(@Param("userSeq") int userSeq);
    String readDisplayLang(@Param("userSeq") int userSeq);
    String readMessageReadType(@Param("userSeq") int userSeq);
    String readDefaultHtmlEditor(@Param("userSeq") int userSeq);
    String readFontSize(@Param("userSeq") int userSeq);
    String readReturnUrl(@Param("userSeq") int userSeq);
    String readMailListDisplay(@Param("userSeq") int userSeq);
    String readTimeZone(@Param("userSeq") int userSeq);
    String readDeletedItemFolder(@Param("userSeq") int userSeq);
    String readPageBase(@Param("userSeq") int userSeq);
    String readMailSendPossible(@Param("userSeq") int userSeq);
    String readArrivalAlarm(@Param("userSeq") int userSeq);
    String readSkinType(@Param("userSeq") int userSeq);
    
    // 추가 메서드 (원본 DAO에서 누락됨, 2025-10-23)
    com.terracetech.tims.webmail.setting.vo.UserInfoVO readUserInfo(@Param("userSeq") int userSeq);
    void modifyUserInfo(com.terracetech.tims.webmail.setting.vo.UserInfoVO vo);
    void modifyMyPassword(@Param("userSeq") int userSeq, @Param("password") String password);
    void modifyMyPasswordChangeTime(@Param("userSeq") int userSeq, @Param("changeTime") String changeTime);
    void modifyPKIUserDN(@Param("userSeq") int userSeq, @Param("pkiUserDN") String pkiUserDN);
    void modifyAutoSaveInfo(@Param("userSeq") int userSeq, @Param("autoSaveID") int autoSaveID, @Param("autoSaveFolder") String autoSaveFolder);
    
    com.terracetech.tims.webmail.setting.vo.UserPhotoVO readUserPhoto(@Param("userSeq") int userSeq);
    void modifyUserPhoto(com.terracetech.tims.webmail.setting.vo.UserPhotoVO vo);
    void deleteUserPhoto(@Param("userSeq") int userSeq);
    void saveUserPhoto(com.terracetech.tims.webmail.setting.vo.UserPhotoVO vo);
    
    List<com.terracetech.tims.webmail.setting.vo.ZipcodeVO> readZipcodeList(@Param("dong") String dong, @Param("skipResult") int skipResult, @Param("maxResult") int maxResult);
    int readZipcodeListCount(@Param("dong") String dong);
    
    java.util.Map<String, Object> readUserEtcInfoMap(@Param("userSeq") int userSeq);

    /** 원본: public void updateMailHome(String userSeq, String setMailHome) */
    void updateMailHome(@Param("userSeq") String userSeq, @Param("setMailHome") String setMailHome);
}