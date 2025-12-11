package com.terracetech.tims.webmail.note.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.terracetech.tims.webmail.note.vo.NotePolicyCondVO;
import com.terracetech.tims.webmail.note.vo.NotePolicyVO;

/**
 * NotePolicyDao MyBatis Mapper Interface
 * 원본: NotePolicyDao extends SqlSessionDaoSupport
 * 총 메서드 수: 7개
 */
@Mapper
public interface NotePolicyDao {
    /** 원본: public NotePolicyVO readNotePolicy(int mailUserSeq) */
    NotePolicyVO readNotePolicy(@Param("mailUserSeq") int mailUserSeq);
    
    /** 원본: public List<NotePolicyCondVO> readNotePolicyCondList(int mailUserSeq) */
    List<NotePolicyCondVO> readNotePolicyCondList(@Param("mailUserSeq") int mailUserSeq);
    
    /** 원본: public void saveNotePolicy(NotePolicyVO notePolicyVo) */
    void saveNotePolicy(NotePolicyVO notePolicyVo);
    
    /** 원본: public void modifyNotePolicy(NotePolicyVO notePolicyVo) */
    void modifyNotePolicy(NotePolicyVO notePolicyVo);
    
    /** 원본: public void saveNotePolicyCond(NotePolicyCondVO notePolicyCondVo) */
    void saveNotePolicyCond(NotePolicyCondVO notePolicyCondVo);
    
    /** 원본: public void deleteNotePolicyCond(int mailUserSeq) */
    void deleteNotePolicyCond(@Param("mailUserSeq") int mailUserSeq);
    
    /** 원본: public int checkNotePolicyCondMe(int mailUserSeq, int condTarget) */
    int checkNotePolicyCondMe(@Param("mailUserSeq") int mailUserSeq, @Param("condTarget") int condTarget);
}