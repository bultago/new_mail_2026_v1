package com.terracetech.tims.webmail.setting.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.terracetech.tims.webmail.setting.vo.AutoReplyListVO;
import com.terracetech.tims.webmail.setting.vo.AutoReplyVO;

/**
 * SettingAutoReplyDao MyBatis Mapper Interface
 * 원본: SettingAutoReplyDao extends SqlSessionDaoSupport
 * 총 메서드 수: 5개
 */
@Mapper
public interface SettingAutoReplyDao {
    /** 원본: public AutoReplyVO readAutoReply(int userSeq) */
    AutoReplyVO readAutoReply(@Param("userSeq") int userSeq);
    
    /** 원본: public List<AutoReplyListVO> readAutoReplyWhiteList(int userSeq) */
    List<AutoReplyListVO> readAutoReplyWhiteList(@Param("userSeq") int userSeq);
    
    /** 원본: public int modifyAutoReply(AutoReplyVO autoReplyVO) */
    int modifyAutoReply(AutoReplyVO autoReplyVO);
    
    /** 원본: public int deleteAutoReplyWhiteList(int userSeq) */
    int deleteAutoReplyWhiteList(@Param("userSeq") int userSeq);
    
    /** 원본: public void saveAutoReplyWhiteList(AutoReplyListVO[] autoReplyListVOs) */
    void saveAutoReplyWhiteList(AutoReplyListVO[] autoReplyListVOs);
}