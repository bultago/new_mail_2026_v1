package com.terracetech.tims.webmail.setting.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.terracetech.tims.webmail.setting.vo.FilterCondVO;
import com.terracetech.tims.webmail.setting.vo.FilterSubCondVO;
import com.terracetech.tims.webmail.setting.vo.FilterVO;

/**
 * SettingFilterDao MyBatis Mapper Interface
 * 
 * 원본 클래스: SettingFilterDao extends SqlSessionDaoSupport
 * 변환 내용: iBATIS → MyBatis Mapper 인터페이스
 * 변환일: 2025-10-20
 * 총 메서드 수: 14개 (원본 기준)
 */
@Mapper
public interface SettingFilterDao {

    /** 원본: public List<FilterSubCondVO> readFilterSubcondList(int mailUserSeq, int condSeq) */
    List<FilterSubCondVO> readFilterSubcondList(@Param("mailUserSeq") int mailUserSeq, @Param("condSeq") int condSeq);

    /** 원본: public List<FilterCondVO> readFilterCondList(int mailUserSeq) */
    List<FilterCondVO> readFilterCondList(@Param("mailUserSeq") int mailUserSeq);

    /** 원본: public int readMaxFilterCondSeq(int mailUserSeq) */
    int readMaxFilterCondSeq(@Param("mailUserSeq") int mailUserSeq);

    /** 원본: public FilterVO readFilter(int mailUserSeq) */
    FilterVO readFilter(@Param("mailUserSeq") int mailUserSeq);

    /** 원본: public boolean saveFilterSubcond(FilterSubCondVO vo) */
    boolean saveFilterSubcond(FilterSubCondVO vo);

    /** 원본: public boolean saveFilterCond(FilterCondVO vo) */
    boolean saveFilterCond(FilterCondVO vo);

    /** 원본: public void saveFilter(FilterVO vo) */
    void saveFilter(FilterVO vo);

    /** 원본: public int deleteFilterSubcond(int mailUserSeq, int condSeq, int[] subcondSeqs) */
    int deleteFilterSubcondByArray(@Param("mailUserSeq") int mailUserSeq, @Param("condSeq") int condSeq, 
                                   @Param("subcondSeqs") int[] subcondSeqs);

    /** 원본: public int deleteFilterCond(int mailUserSeq, int[] condSeqs) */
    int deleteFilterCond(@Param("mailUserSeq") int mailUserSeq, @Param("condSeqs") int[] condSeqs);

    /** 원본: public int deleteFilter(int mailUserSeq) */
    int deleteFilter(@Param("mailUserSeq") int mailUserSeq);

    /** 원본: public boolean modifyFilterSubcond(FilterSubCondVO vo) */
    boolean modifyFilterSubcond(FilterSubCondVO vo);

    /** 원본: public boolean modifyFilterCond(FilterCondVO vo) */
    boolean modifyFilterCond(FilterCondVO vo);

    /** 원본: public boolean modifyFilter(FilterVO vo) */
    boolean modifyFilter(FilterVO vo);

    /** 원본: public int deleteFilterSubcond(int mailUserSeq, int condSeq) */
    int deleteFilterSubcond(@Param("mailUserSeq") int mailUserSeq, @Param("condSeq") int condSeq);
}