package com.terracetech.tims.webmail.setting.dao;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.terracetech.tims.webmail.setting.vo.SignDataVO;
import com.terracetech.tims.webmail.setting.vo.SignVO;

/**
 * SignDataDao MyBatis Mapper Interface
 * 
 * 원본 클래스: SignDataDao extends SqlSessionDaoSupport
 * 변환 내용: iBATIS → MyBatis Mapper 인터페이스
 * 변환일: 2025-10-20
 * 총 메서드 수: 12개 (원본 기준)
 */
@Mapper
public interface SignDataDao {

    /** 원본: public File readSignImageFile(int memberSeq, String path, String signName, String thumbnailName) throws IOException */
    File readSignImageFile(@Param("memberSeq") int memberSeq, @Param("path") String path, 
                          @Param("signName") String signName, @Param("thumbnailName") String thumbnailName) throws IOException;

    /** 원본: public void saveSignData(SignDataVO data) throws IOException */
    void saveSignData(SignDataVO data) throws IOException;

    /** 원본: public List<SignDataVO> readSignDataList(int userSeq) throws Exception */
    List<SignDataVO> readSignDataList(@Param("userSeq") int userSeq) throws Exception;

    /** 원본: public List<SignDataVO> readSignSimpleDataList(int userSeq) throws Exception */
    List<SignDataVO> readSignSimpleDataList(@Param("userSeq") int userSeq) throws Exception;

    /** 원본: public void deleteSignData(int userSeq, int[] signSeqs) throws Exception */
    void deleteSignData(@Param("userSeq") int userSeq, @Param("signSeqs") int[] signSeqs) throws Exception;

    /** 원본: public void modifySignData(SignDataVO data) throws IOException */
    void modifySignData(SignDataVO data) throws IOException;

    /** 원본: public SignDataVO readSignData(int userSeq, int signSeq) throws IOException */
    SignDataVO readSignData(@Param("userSeq") int userSeq, @Param("signSeq") int signSeq) throws IOException;

    /** 원본: public SignDataVO readDefaultSignData(int userSeq) throws Exception */
    SignDataVO readDefaultSignData(@Param("userSeq") int userSeq) throws Exception;

    /** 원본: public void saveSign(SignVO sign) throws Exception */
    void saveSign(SignVO sign) throws Exception;

    /** 원본: public void updateSign(SignVO signVo) throws Exception */
    void updateSign(SignVO signVo) throws Exception;

    /** 원본: public SignVO readSign(int userSeq) throws Exception */
    SignVO readSign(@Param("userSeq") int userSeq) throws Exception;

    /** 원본: public void updateCheckAllNotDefault(int userSeq) throws Exception */
    void updateCheckAllNotDefault(@Param("userSeq") int userSeq) throws Exception;
}