package com.terracetech.tims.webmail.mailuser.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.terracetech.tims.webmail.mailuser.vo.MailUserInfoVO;

/**
 * UserInfoDao MyBatis Mapper Interface
 * 원본: UserInfoDao extends SqlSessionDaoSupport
 * 총 메서드 수: 7개
 */
@Mapper
public interface UserInfoDao {
    /** 원본: public MailUserInfoVO readUserInfo(int userSeq) */
    MailUserInfoVO readUserInfo(@Param("userSeq") int userSeq);
    
    /** 원본: public boolean modifyUserInfo(MailUserInfoVO vo) */
    boolean modifyUserInfo(MailUserInfoVO vo);
    
    /** 원본: public void saveUserInfo(MailUserInfoVO vo) */
    void saveUserInfo(MailUserInfoVO vo);
    
    /** 원본: public boolean deleteUserInfo(int userSeq) */
    boolean deleteUserInfo(@Param("userSeq") int userSeq);
    
    /** 원본: public int readUserInfoBySsn(String domain, String ssn) */
    int readUserInfoBySsn(@Param("domain") String domain, @Param("ssn") String ssn);
    
    /** 원본: public int readUserInfoByEmpno(String domain, String empno) */
    int readUserInfoByEmpno(@Param("domain") String domain, @Param("empno") String empno);
    
    /** 원본: public int readUserInfoByJpInfo(String domain, String name, String postalCode, String birthday) */
    int readUserInfoByJpInfo(@Param("domain") String domain, @Param("name") String name, 
                            @Param("postalCode") String postalCode, @Param("birthday") String birthday);
}