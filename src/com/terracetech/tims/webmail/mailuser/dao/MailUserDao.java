/**
 * MailUserDao.java 2008  2008-09-26
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mailuser.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.terracetech.tims.webmail.bbs.vo.BoardQuotaVO;
import com.terracetech.tims.webmail.common.vo.SearchVO;
import com.terracetech.tims.webmail.mailuser.vo.MailUserVO;
import com.terracetech.tims.webmail.mailuser.vo.MailUserInfoVO;
import com.terracetech.tims.webmail.mailuser.vo.SearchUserVO;
import com.terracetech.tims.webmail.webfolder.vo.WebfolderQuotaVO;

/**
 * <p><strong>MailUserDao.java</strong> MyBatis Mapper Interface</p>
 * <p>주요기능</p>
 * <ul>
 * <li>DB 에서 사용자 정보를 조회하고 저장하는 Mapper 인터페이스.</li> 
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0
 * 
 * 원본 클래스: MailUserDao extends SqlSessionDaoSupport
 * 변환 내용: iBATIS → MyBatis Mapper 인터페이스
 * 변환일: 2025-10-20
 * 총 메서드 수: 50개 (원본 기준)
 */
@Mapper
public interface MailUserDao {

    /** 원본: public SearchUserVO[] searchSimpleUserInfo(SearchVO vo, int skip, int max) */
    SearchUserVO[] searchSimpleUserInfo(SearchVO vo, @Param("skip") int skip, @Param("max") int max);

    /** 원본: public Map<String, Object> readMailUserAuthInfo(String id, String domain) */
    Map<String, Object> readMailUserAuthInfo(@Param("id") String id, @Param("domain") String domain);

    /** 원본: public Map<String, Object> readMailUserAuthInfoByEmpno(String empno, String domain) */
    Map<String, Object> readMailUserAuthInfoByEmpno(@Param("empno") String empno, @Param("domain") String domain);

    /** 원본: public Map<String, Object> readMailUserAuthDn(String userDN) */
    Map<String, Object> readMailUserAuthDn(@Param("userDN") String userDN);

    /** 원본: public Map<String, Object> readMailUserOtherInfo(int userSeq, int domainSeq) */
    Map<String, Object> readMailUserOtherInfo(@Param("userSeq") int userSeq, @Param("domainSeq") int domainSeq);

    /** 원본: public Map<String, Object> readMailUserConnectionInfo(int userSeq, int domainSeq) */
    Map<String, Object> readMailUserConnectionInfo(@Param("userSeq") int userSeq, @Param("domainSeq") int domainSeq);

    /** 원본: public Map<String, ?> readMailUser(int mailUserSeq) */
    Map<String, ?> readMailUser(@Param("mailUserSeq") int mailUserSeq);

    /** 원본: public boolean deleteMailUser(int mailUserSeq) */
    boolean deleteMailUser(@Param("mailUserSeq") int mailUserSeq);

    /** 원본: public void saveMailUser(MailUserVO vo) */
    void saveMailUser(MailUserVO vo);

    /** 원본: public boolean modifyMailUser(MailUserVO vo) */
    boolean modifyMailUser(MailUserVO vo);

    /** 원본: public String readMailUserSsoInfoByUid(Map paramMap) */
    String readMailUserSsoInfoByUid(Map paramMap);

    /** 원본: public String readMailUserSsoInfoByEmpno(Map paramMap) */
    String readMailUserSsoInfoByEmpno(Map paramMap);

    /** 원본: public String readMailUserSsoInfoBySsn(Map paramMap) */
    String readMailUserSsoInfoBySsn(Map paramMap);

    /** 원본: public MailUserInfoVO readMailUserInfo(int domainSeq, String userId) */
    MailUserInfoVO readMailUserInfoByDomainAndUser(@Param("domainSeq") int domainSeq, @Param("userId") String userId);

    /** 원본: public MailUserInfoVO readMailUserInfo(String userId, String domain) */
    MailUserInfoVO readMailUserInfoByUserAndDomain(@Param("userId") String userId, @Param("domain") String domain);

    /** 원본: public Map<String, String> readUserInfoByUid(int userSeq) */
    Map<String, String> readUserInfoByUid(@Param("userSeq") int userSeq);

    /** 원본: public WebfolderQuotaVO readWebfolderInfo(int userSeq) */
    WebfolderQuotaVO readWebfolderInfo(@Param("userSeq") int userSeq);

    /** 원본: public BoardQuotaVO readBbsInfo(int userSeq) */
    BoardQuotaVO readBbsInfo(@Param("userSeq") int userSeq);

    /** 원본: public String readUserPass(int userSeq) */
    String readUserPass(@Param("userSeq") int userSeq);

    /** 원본: public int readUserIdDupCheck(String userId, int mailDomainSeq) */
    int readUserIdDupCheck(@Param("userId") String userId, @Param("mailDomainSeq") int mailDomainSeq);

    /** 원본: public int readUserSeq(String userId, int mailDomainSeq) */
    int readUserSeqByUserIdAndDomain(@Param("userId") String userId, @Param("mailDomainSeq") int mailDomainSeq);

    /** 원본: public String searchUserId(String userName, String ssn, int mailDomainSeq) */
    String searchUserId(@Param("userName") String userName, @Param("ssn") String ssn, @Param("mailDomainSeq") int mailDomainSeq);

    /** 원본: public String searchUserIdByJpInfo(String userName, String postalCode, String birthday, int mailDomainSeq) */
    String searchUserIdByJpInfo(@Param("userName") String userName, @Param("postalCode") String postalCode, 
                               @Param("birthday") String birthday, @Param("mailDomainSeq") int mailDomainSeq);

    /** 원본: public String searchUserIdByEmpno(String userName, String empno, int mailDomainSeq) */
    String searchUserIdByEmpno(@Param("userName") String userName, @Param("empno") String empno, 
                              @Param("mailDomainSeq") int mailDomainSeq);

    /** 원본: public int searchPassword(String userId, String passCode, String passAnswer, int mailDomainSeq) */
    int searchPassword(@Param("userId") String userId, @Param("passCode") String passCode, 
                      @Param("passAnswer") String passAnswer, @Param("mailDomainSeq") int mailDomainSeq);

    /** 원본: public void modifyUserPassword(int mailUserSeq, String mailPassword) */
    void modifyUserPassword(@Param("mailUserSeq") int mailUserSeq, @Param("mailPassword") String mailPassword);

    /** 원본: public List<SearchUserVO> searchMailUser(String userId, String userName, int mailDomainSeq, String equal) */
    List<SearchUserVO> searchMailUser(@Param("userId") String userId, @Param("userName") String userName, 
                                     @Param("mailDomainSeq") int mailDomainSeq, @Param("equal") String equal);

    /** 원본: public int readUserPassFailCount(int mailUserSeq) */
    int readUserPassFailCount(@Param("mailUserSeq") int mailUserSeq);

    /** 원본: public void updateUserPassFailCount(int mailUserSeq, int count) */
    void updateUserPassFailCount(@Param("mailUserSeq") int mailUserSeq, @Param("count") int count);

    /** 원본: public String readMailUid(int mailUserSeq) */
    String readMailUid(@Param("mailUserSeq") int mailUserSeq);

    /** 원본: public String readMailUserName(int mailUserSeq) */
    String readMailUserName(@Param("mailUserSeq") int mailUserSeq);

    /** 원본: public void updateLoginInfo(String mailUserSeq, String loginTimeInfo) */
    void updateLoginInfo(@Param("mailUserSeq") String mailUserSeq, @Param("loginTimeInfo") String loginTimeInfo);

    /** 원본: public String readAlternateId(String mailUid, String domainName) */
    String readAlternateId(@Param("mailUid") String mailUid, @Param("domainName") String domainName);

    /** 원본: public String readMassSend(int userSeq) */
    String readMassSend(@Param("userSeq") int userSeq);

    /** 원본: public String readMassUpload(int userSeq) */
    String readMassUpload(@Param("userSeq") int userSeq);

    /** 원본: public String readUserDN(int userSeq) */
    String readUserDN(@Param("userSeq") int userSeq);

    /** 원본: public String readAccountDN(String uid, String domain) */
    String readAccountDN(@Param("uid") String uid, @Param("domain") String domain);

    /** 원본: public Map<String, String> readUserSetting(int domainSeq, int groupSeq, int userSeq) */
    Map<String, String> readUserSetting(@Param("domainSeq") int domainSeq, @Param("groupSeq") int groupSeq, 
                                       @Param("userSeq") int userSeq);

    /** 원본: public int readUserSeq(String userId, String mailDomain) */
    int readUserSeq(@Param("userId") String userId, @Param("mailDomain") String mailDomain);

    /** 원본: public SearchUserVO readUserIdAndName(int mailUserSeq) */
    SearchUserVO readUserIdAndName(@Param("mailUserSeq") int mailUserSeq);

    /** 원본: public String readUserInfoMobileNo(int mailUserSeq) */
    String readUserInfoMobileNo(@Param("mailUserSeq") int mailUserSeq);

    /** 원본: public List<String> readMailUids(int mailDomainSeq) */
    List<String> readMailUids(@Param("mailDomainSeq") int mailDomainSeq);

    /** 원본: public void changeUserAccount(int mailUserSeq, String status) */
    void changeUserAccount(@Param("mailUserSeq") int mailUserSeq, @Param("status") String status);

    /** 원본: public int readNoteSaveCount(int mailDomainSeq, int mailGroupSeq, int mailUserSeq) */
    int readNoteSaveCount(@Param("mailDomainSeq") int mailDomainSeq, @Param("mailGroupSeq") int mailGroupSeq, 
                         @Param("mailUserSeq") int mailUserSeq);

    /** 원본: public int readNoteSaveDate(int mailDomainSeq, int mailGroupSeq, int mailUserSeq) */
    int readNoteSaveDate(@Param("mailDomainSeq") int mailDomainSeq, @Param("mailGroupSeq") int mailGroupSeq, 
                        @Param("mailUserSeq") int mailUserSeq);

    /** 원본: public String readMailUserConfig(int mailUserSeq, String configName) */
    String readMailUserConfig(@Param("mailUserSeq") int mailUserSeq, @Param("configName") String configName);

    /** 원본: public void saveMailUserConfig(int mailUserSeq, String configName, String configValue) */
    void saveMailUserConfig(@Param("mailUserSeq") int mailUserSeq, @Param("configName") String configName, 
                           @Param("configValue") String configValue);

    /** 원본: public void deleteMailUserConfig(int mailUserSeq, String configName) */
    void deleteMailUserConfig(@Param("mailUserSeq") int mailUserSeq, @Param("configName") String configName);

    /** 원본: public String readMassSenderUser(int mailDomainSeq) */
    String readMassSenderUser(@Param("mailDomainSeq") int mailDomainSeq);

    /** 원본: public String readFileUploadUser(int mailDomainSeq) */
    String readFileUploadUser(@Param("mailDomainSeq") int mailDomainSeq);
}