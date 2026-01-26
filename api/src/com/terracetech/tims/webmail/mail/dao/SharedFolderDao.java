package com.terracetech.tims.webmail.mail.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.terracetech.tims.webmail.mail.vo.SharedFolderVO;
import com.terracetech.tims.webmail.mail.vo.SharedFolderUserVO;

/**
 * SharedFolderDao MyBatis Mapper Interface
 * 
 * 원본 클래스: SharedFolderDao extends SqlSessionDaoSupport
 * 변환 내용: iBATIS → MyBatis Mapper 인터페이스
 * 변환일: 2025-10-20
 * 총 메서드 수: 9개 (원본 기준)
 */
@Mapper
public interface SharedFolderDao {

    /** 원본: public List<SharedFolderVO> readSharedFolderList(int userSeq) */
    List<SharedFolderVO> readSharedFolderList(@Param("userSeq") int userSeq);

    /** 원본: public List<SharedFolderUserVO> readSharedFolderReaderList(int folderUid) */
    List<SharedFolderUserVO> readSharedFolderReaderList(@Param("folderUid") int folderUid);

    /** 원본: public List<SharedFolderVO> readUserSharedFolderList(int userSeq) */
    List<SharedFolderVO> readUserSharedFolderList(@Param("userSeq") int userSeq);

    /** 원본: public int getFolderUid(SharedFolderVO vo) */
    int getFolderUid(SharedFolderVO vo);

    /** 원본: public void saveSharedFolder(SharedFolderVO vo) */
    void saveSharedFolder(SharedFolderVO vo);

    /** 원본: public void updateSharedFolder(SharedFolderVO vo) */
    void updateSharedFolder(SharedFolderVO vo);

    /** 원본: public void removeSharedFolder(int folderUid) */
    void removeSharedFolder(@Param("folderUid") int folderUid);

    /** 원본: public void saveSharedFolderReader(SharedFolderUserVO vo) */
    void saveSharedFolderReader(SharedFolderUserVO vo);

    /** 원본: public void removeSharedFolderReader(int folderUid) */
    void removeSharedFolderReader(@Param("folderUid") int folderUid);
}