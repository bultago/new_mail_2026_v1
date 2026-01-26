package com.terracetech.tims.webmail.setting.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.terracetech.tims.webmail.setting.vo.PSpamRuleVO;
import com.terracetech.tims.webmail.setting.vo.PSpameListItemVO;

/**
 * SettingSpamDao MyBatis Mapper Interface
 * 원본: SettingSpamDao extends SqlSessionDaoSupport
 * 총 메서드 수: 14개
 */
@Mapper
public interface SettingSpamDao {
    /** 원본: public void savePSpamRule(PSpamRuleVO vo) */
    void savePSpamRule(PSpamRuleVO vo);
    
    /** 원본: public PSpamRuleVO readPSpamRule(int userSeq) */
    PSpamRuleVO readPSpamRule(@Param("userSeq") int userSeq);
    
    /** 원본: public boolean modifyPSpamRule(PSpamRuleVO vo) */
    boolean modifyPSpamRule(PSpamRuleVO vo);
    
    /** 원본: public boolean deletePSpameRule(int userSeq) */
    boolean deletePSpameRule(@Param("userSeq") int userSeq);
    
    /** 원본: public List<PSpameListItemVO> readPSpamWhiteList(int userSeq) */
    List<PSpameListItemVO> readPSpamWhiteList(@Param("userSeq") int userSeq);
    
    /** 원본: public void savePSpamWhiteList(PSpameListItemVO item) */
    void savePSpamWhiteListItem(PSpameListItemVO item);
    
    /** 원본: public void savePSpamWhiteList(PSpameListItemVO[] vos) */
    void savePSpamWhiteListArray(PSpameListItemVO[] vos);
    
    /** 원본: public void deletePSpamWhiteList(int userSeq) */
    void deletePSpamWhiteList(@Param("userSeq") int userSeq);
    
    /** 원본: public List<PSpameListItemVO> readPSpamBlackList(int userSeq) */
    List<PSpameListItemVO> readPSpamBlackList(@Param("userSeq") int userSeq);
    
    /** 원본: public void savePSpamBlackList(PSpameListItemVO item) */
    void savePSpamBlackListItem(PSpameListItemVO item);
    
    /** 원본: public void savePSpamBlackList(PSpameListItemVO[] vos) */
    void savePSpamBlackListArray(PSpameListItemVO[] vos);
    
    /** 원본: public void deletePSpamBlackList(int userSeq) */
    void deletePSpamBlackList(@Param("userSeq") int userSeq);
    
    /** 원본: public void deletePSpamWhiteList(int userSeq, String[] blackList) */
    void deletePSpamWhiteList(@Param("userSeq") int userSeq, @Param("blackList") String[] blackList);
    
    /** 원본: public void deletePSpamBlackList(int userSeq, String[] blackList) */
    void deletePSpamBlackList(@Param("userSeq") int userSeq, @Param("blackList") String[] blackList);
}