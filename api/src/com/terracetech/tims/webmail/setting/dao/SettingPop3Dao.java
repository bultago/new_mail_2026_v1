package com.terracetech.tims.webmail.setting.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.terracetech.tims.webmail.setting.vo.Pop3VO;

/**
 * SettingPop3Dao MyBatis Mapper Interface
 * 원본: SettingPop3Dao extends SqlSessionDaoSupport
 * 총 메서드 수: 6개
 */
@Mapper
public interface SettingPop3Dao {
    /** 원본: public void savePop3(Pop3VO pop3VO) */
    void savePop3(Pop3VO pop3VO);
    
    /** 원본: public List<Pop3VO> readPop3List(int userSeq) */
    List<Pop3VO> readPop3List(@Param("userSeq") int userSeq);
    
    /** 원본: public Pop3VO readPop3(int userSeq, String pop3Host, String pop3Id) */
    Pop3VO readPop3(@Param("userSeq") int userSeq, @Param("pop3Host") String pop3Host, @Param("pop3Id") String pop3Id);
    
    /** 원본: public int deletePop3(int userSeq, String pop3Host, String pop3Id) */
    int deletePop3(@Param("userSeq") int userSeq, @Param("pop3Host") String pop3Host, @Param("pop3Id") String pop3Id);
    
    /** 원본: public int modifyPop3(Pop3VO pop3Vo) */
    int modifyPop3(Pop3VO pop3Vo);
    
    Pop3VO readPop3BySeq(@Param("pop3Seq") int pop3Seq);
}