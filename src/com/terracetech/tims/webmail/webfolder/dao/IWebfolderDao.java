package com.terracetech.tims.webmail.webfolder.dao;

import java.util.List;

import com.terracetech.tims.webmail.webfolder.vo.WebfolderDataVO;
import com.terracetech.tims.webmail.webfolder.vo.WebfolderShareVO;

public interface IWebfolderDao {

	public List<WebfolderShareVO> readMemberList(String type ,String pattern ,String uid, int domain);
	
	public List<WebfolderShareVO> readShareMemberList(int userSeq, String path);
	
	public int readShareAllFolderMaxCount();
	
	public WebfolderShareVO readShareAllFolder(int fuid);
	
	public WebfolderShareVO readShareAllFolder1(int userSeq, String path);
	
	public int readShareTargetFolderCount(int fuid);
	
	public void saveShareAllFolder(WebfolderShareVO webfolderShareVO);
	
	public void saveShareFolder(int userSeq, int fUid);
	
	public void saveShareTarget(WebfolderShareVO webfolderShareVO);
	
	public void modifyShareAllFolder(WebfolderShareVO webfolderShareVO);
	
	public void modifyAuthShareTargetFolder(WebfolderShareVO webfolderShareVO);
	
	public int deleteShareTargetFolder(int fUid);
	
	public int readShareFolderCount(int fUid);
	
	public int deleteShareAllFolder(int userSeq, String path);
	
	public int deleteShareFolder(int fuid, int userSeq);
	
	public List<WebfolderDataVO> readShareAllFolderData(int userSeq, int myUserSeq, String path);
	
	public int readShareAllFolderCount();
	
	public List<WebfolderShareVO> readSearchShareFolder(int userSeq, String mailUid, String type, String pattern, int skipResult, int maxResult);
	
	public int readSearchShareFolderCount(int userSeq, String mailUid, String type, String pattern);
	
	public int readMyShareFolderCount(int userSeq, int fUid);
	
	public WebfolderShareVO readShareTargetFolder(int fuid, String shareValue);
	
	public List<WebfolderDataVO> readShareFolderList(int domainSeq, int userSeq);
	
	public int deleteNotInShareFolders(int fuid, int[] userSeqs);
	
	public int readWebFolderAttachMaxSize(int mailDomainSeq);
	
	public int readPublicFolderAdminAuth(int domainSeq, int userSeq);
	
	public WebfolderDataVO readAllShareFolder(int mailUserSeq, String path);
	
	public List<WebfolderDataVO> readAllShareFolderList(int mailDomainSeq, int mailUserSeq);
	
	public int readSubOrgFullCodeCount(int mailDomainSeq, int mailUserSeq, String orgFullCode);
	
	public int readOrgFullCodeCount(int mailDomainSeq, int mailUserSeq, String orgFullCode);
	
	public int readMyShareFolderUid(int mailUserSeq, String folderDir);
}
