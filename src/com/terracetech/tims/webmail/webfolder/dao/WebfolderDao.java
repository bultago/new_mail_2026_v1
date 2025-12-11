package com.terracetech.tims.webmail.webfolder.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.terracetech.tims.webmail.webfolder.vo.WebfolderDataVO;
import com.terracetech.tims.webmail.webfolder.vo.WebfolderShareVO;

/**
 * WebfolderDao MyBatis Mapper Interface
 * 원본: WebfolderDao extends SqlSessionDaoSupport implements IWebfolderDao
 * 총 메서드 수: 32개
 */
@Mapper
public interface WebfolderDao {
    /** 원본: public List<WebfolderShareVO> readMemberList(String type, String pattern, String uid, int domain) */
    List<WebfolderShareVO> readMemberList(Map<String, Object> param);
    
    /** 원본: public List<WebfolderShareVO> readShareMemberList(int userSeq, String path) */
    List<WebfolderShareVO> readShareMemberList(@Param("userSeq") int userSeq, @Param("path") String path);
    
    /** 원본: public int readShareAllFolderMaxCount() */
    int readShareAllFolderMaxCount();
    
    /** 원본: public WebfolderShareVO readShareAllFolder(int fuid) */
    WebfolderShareVO readShareAllFolder(@Param("fuid") int fuid);
    
    /** 원본: public int readShareTargetFolderCount(int fuid) */
    int readShareTargetFolderCount(@Param("fuid") int fuid);
    
    /** 원본: public void saveShareAllFolder(WebfolderShareVO webfolderShareVO) */
    void saveShareAllFolder(WebfolderShareVO webfolderShareVO);
    
    /** 원본: public void saveShareFolder(int userSeq, int fUid) */
    void saveShareFolder(@Param("userSeq") int userSeq, @Param("fUid") int fUid);
    
    /** 원본: public void saveShareTarget(WebfolderShareVO webfolderShareVO) */
    void saveShareTarget(WebfolderShareVO webfolderShareVO);
    
    /** 원본: public void modifyShareAllFolder(WebfolderShareVO webfolderShareVO) */
    void modifyShareAllFolder(WebfolderShareVO webfolderShareVO);
    
    /** 원본: public void modifyAuthShareTargetFolder(WebfolderShareVO webfolderShareVO) */
    void modifyAuthShareTargetFolder(WebfolderShareVO webfolderShareVO);
    
    /** 원본: public int deleteShareTargetFolder(int fUid) */
    int deleteShareTargetFolder(@Param("fUid") int fUid);
    
    /** 원본: public int readShareFolderCount(int fUid) */
    int readShareFolderCount(@Param("fUid") int fUid);
    
    /** 원본: public int deleteShareAllFolder(int userSeq, String path) */
    int deleteShareAllFolder(@Param("userSeq") int userSeq, @Param("path") String path);
    
    /** 원본: public int deleteShareFolder(int fuid, int userSeq) */
    int deleteShareFolder(@Param("fuid") int fuid, @Param("userSeq") int userSeq);
    
    /** 원본: public List<WebfolderDataVO> readShareAllFolderData(int userSeq, int myUserSeq, String path) */
    List<WebfolderDataVO> readShareAllFolderData(@Param("userSeq") int userSeq, @Param("myUserSeq") int myUserSeq, 
                                                 @Param("path") String path);
    
    /** 원본: public WebfolderShareVO readShareAllFolder1(int userSeq, String path) */
    WebfolderShareVO readShareAllFolder1(@Param("userSeq") int userSeq, @Param("path") String path);
    
    /** 원본: public int readShareAllFolderCount() */
    int readShareAllFolderCount();
    
    /** 원본: public List<WebfolderShareVO> readSearchShareFolder(int userSeq, String mailUid, String type, String pattern, int skipResult, int maxResult) */
    List<WebfolderShareVO> readSearchShareFolder(Map<String, Object> param);
    
    /** 원본: public int readSearchShareFolderCount(int userSeq, String mailUid, String type, String pattern) */
    int readSearchShareFolderCount(Map<String, Object> param);
    
    /** 원본: public int readMyShareFolderCount(int userSeq, int fUid) */
    int readMyShareFolderCount(@Param("userSeq") int userSeq, @Param("fUid") int fUid);
    
    List<WebfolderShareVO> readShareFolderList(@Param("userSeq") int userSeq);
    List<WebfolderShareVO> readMyShareFolderList(@Param("userSeq") int userSeq);
    WebfolderShareVO readShareTargetFolder(@Param("fuid") int fuid, @Param("userSeq") int userSeq);
    int getWebfolderQuota(@Param("userSeq") int userSeq);
    int getWebfolderUsedSize(@Param("userSeq") int userSeq);
    void updateWebfolderUsedSize(@Param("userSeq") int userSeq, @Param("usedSize") long usedSize);
    List<String> readAllowedFileExtensions();
    List<String> readBlockedFileExtensions();
    int getMaxFileSize(@Param("domainSeq") int domainSeq);
    int getMaxUploadSize(@Param("domainSeq") int domainSeq);
}