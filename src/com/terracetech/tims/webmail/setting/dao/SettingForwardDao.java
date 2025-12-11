package com.terracetech.tims.webmail.setting.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.terracetech.tims.webmail.setting.vo.DefineForwardingInfoVO;
import com.terracetech.tims.webmail.setting.vo.ForwardingInfoVO;

/**
 * SettingForwardDao MyBatis Mapper Interface
 * 원본: SettingForwardDao extends SqlSessionDaoSupport
 * 총 메서드 수: 7개
 */
@Mapper
public interface SettingForwardDao {
    /** 원본: public ForwardingInfoVO readForwardInfo(int userSeq) */
    ForwardingInfoVO readForwardInfo(@Param("userSeq") int userSeq);
    
    /** 원본: public void modifyForwardInfo(ForwardingInfoVO vo) */
    void modifyForwardInfo(ForwardingInfoVO vo);
    
    /** 원본: public void modifyDefineForwardingInfo(DefineForwardingInfoVO vo) */
    void modifyDefineForwardingInfo(DefineForwardingInfoVO vo);
    
    /** 원본: public List<DefineForwardingInfoVO> readDefineForwarding(int mail_user_seq) */
    List<DefineForwardingInfoVO> readDefineForwarding(@Param("mail_user_seq") int mail_user_seq);
    
    /** 원본: public List<DefineForwardingInfoVO> readDefineForwardingByForwardSeq(int define_forward_seq) */
    List<DefineForwardingInfoVO> readDefineForwardingByForwardSeq(@Param("define_forward_seq") int define_forward_seq);
    
    void saveDefineForwardingInfo(DefineForwardingInfoVO vo);
    void deleteDefineForwardingInfo(@Param("userSeq") int userSeq, @Param("forwardSeq") int forwardSeq);
    
    void deleteDefineForwarding(@Param("defineForwadingSeq") int[] defineForwadingSeq);
    int checkValidationDefineForwarding(@Param("mailUserSeq") int mailUserSeq, @Param("defineValue") String defineValue);
}