package com.terracetech.tims.webmail.webfolder.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.terracetech.tims.webmail.webfolder.vo.WebfolderDataVO;
import com.terracetech.tims.webmail.webfolder.vo.WebfolderShareVO;

public class WebfolderDao extends SqlMapClientDaoSupport implements IWebfolderDao {

	@SuppressWarnings("unchecked")
	public List<WebfolderShareVO> readMemberList(String type ,String pattern ,String uid, int domain) {
		pattern = "%"+pattern+"%";
		Map paramMap = new HashMap();
		paramMap.put("type", type);
		paramMap.put("pattern", pattern);
		paramMap.put("uid", uid);
		paramMap.put("domain", domain);
		
		return getSqlMapClientTemplate().queryForList("Webfolder.readMemberList", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<WebfolderShareVO> readShareMemberList(int userSeq, String path) {
		Map paramMap = new HashMap();
		paramMap.put("userSeq", userSeq);
		paramMap.put("path", path);
		
		return getSqlMapClientTemplate().queryForList("Webfolder.readShareMemberList", paramMap);
	}
	
	public int readShareAllFolderMaxCount() {
		return (Integer)getSqlMapClientTemplate().queryForObject("Webfolder.readShareAllFolderMaxCount");
	}
	
	public WebfolderShareVO readShareAllFolder(int fuid) {
		return (WebfolderShareVO)getSqlMapClientTemplate().queryForObject("Webfolder.readShareAllFolder", fuid);
	}
	
	public int readShareTargetFolderCount(int fuid) {
		return (Integer)getSqlMapClientTemplate().queryForObject("Webfolder.readShareTargetFolderCount", fuid);
	}
	
	public void saveShareAllFolder(WebfolderShareVO webfolderShareVO) {
		getSqlMapClientTemplate().insert("Webfolder.saveShareAllFolder", webfolderShareVO);
	}
	
	public void saveShareFolder(int userSeq, int fUid) {
		Map<String , Integer> paramMap = new HashMap<String, Integer>();
		paramMap.put("userSeq", userSeq);
		paramMap.put("fUid", fUid);
		getSqlMapClientTemplate().insert("Webfolder.saveShareFolder", paramMap);
	}
	
	public void saveShareTarget(WebfolderShareVO webfolderShareVO) {
		getSqlMapClientTemplate().insert("Webfolder.saveShareTarget", webfolderShareVO);
	}
	
	public void modifyShareAllFolder(WebfolderShareVO webfolderShareVO) {
		getSqlMapClientTemplate().update("Webfolder.modifyShareAllFolder", webfolderShareVO);
	}
	
	public void modifyAuthShareTargetFolder(WebfolderShareVO webfolderShareVO) {
		getSqlMapClientTemplate().update("Webfolder.modifyAuthShareTargetFolder", webfolderShareVO);
	}
	
	public int deleteShareTargetFolder(int fUid) {
		return (Integer)getSqlMapClientTemplate().delete("Webfolder.deleteShareTargetFolder", fUid);
	}
	
	public int readShareFolderCount(int fUid) {
		return (Integer)getSqlMapClientTemplate().queryForObject("Webfolder.readShareFolderCount", fUid);
	}
	
	public int deleteShareAllFolder(int userSeq, String path) {
		Map paramMap = new HashMap();
		paramMap.put("userSeq", userSeq);
		paramMap.put("path", path);
		return (Integer)getSqlMapClientTemplate().delete("Webfolder.deleteShareAllFolder", paramMap);
	}
	
	public int deleteShareFolder(int fuid, int userSeq) {
		Map<String , Integer> paramMap = new HashMap<String, Integer>();
		paramMap.put("userSeq", userSeq < 0 ? null : userSeq );
		paramMap.put("fuid", fuid);
		return (Integer)getSqlMapClientTemplate().delete("Webfolder.deleteShareFolder", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<WebfolderDataVO> readShareAllFolderData(int userSeq, int myUserSeq, String path) {
		Map paramMap = new HashMap();
		paramMap.put("userSeq", userSeq);
		paramMap.put("myUserSeq", myUserSeq);
		paramMap.put("path", path);
		return getSqlMapClientTemplate().queryForList("Webfolder.readShareAllFolderData", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public WebfolderShareVO readShareAllFolder1(int userSeq, String path) {
		Map paramMap = new HashMap();
		paramMap.put("userSeq", userSeq);
		paramMap.put("path", path);
		return (WebfolderShareVO)getSqlMapClientTemplate().queryForObject("Webfolder.readShareAllFolder1", paramMap);
	}
	
	public int readShareAllFolderCount() {
		return (Integer)getSqlMapClientTemplate().queryForObject("Webfolder.readShareAllFolderCount");
	}
	
	@SuppressWarnings("unchecked")
	public List<WebfolderShareVO> readSearchShareFolder(int userSeq, String mailUid, String type, String pattern, int skipResult, int maxResult) {
		pattern = "%"+pattern+"%";
		Map paramMap = new HashMap();
		paramMap.put("userSeq", userSeq);
		paramMap.put("mailUid", mailUid);
		paramMap.put("type", type);
		paramMap.put("pattern", pattern);
		
		paramMap.put("skipResult", skipResult);
		paramMap.put("maxResult", maxResult);
		
		return getSqlMapClientTemplate().queryForList("Webfolder.readSearchShareFolder", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public int readSearchShareFolderCount(int userSeq, String mailUid, String type, String pattern) {
		pattern = "%"+pattern+"%";
		Map paramMap = new HashMap();
		paramMap.put("userSeq", userSeq);
		paramMap.put("mailUid", mailUid);
		paramMap.put("type", type);
		paramMap.put("pattern", pattern);
		return (Integer)getSqlMapClientTemplate().queryForObject("Webfolder.readSearchShareFolderCount", paramMap);
	}
	
	public int readMyShareFolderCount(int userSeq, int fUid) {
		Map<String , Integer> paramMap = new HashMap<String, Integer>();
		paramMap.put("userSeq", userSeq);
		paramMap.put("fUid", fUid);
		return (Integer)getSqlMapClientTemplate().queryForObject("Webfolder.readMyShareFolderCount", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public WebfolderShareVO readShareTargetFolder(int fuid, String email) {
		Map paramMap = new HashMap();
		paramMap.put("fuid", fuid);
		paramMap.put("email", email);
		return (WebfolderShareVO)getSqlMapClientTemplate().queryForObject("Webfolder.readShareTargetFolder", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<WebfolderDataVO> readShareFolderList(int domainSeq, int userSeq) {
		Map paramMap = new HashMap();
		paramMap.put("mailDomainSeq", domainSeq);
		paramMap.put("mailUserSeq", userSeq);
		return getSqlMapClientTemplate().queryForList("Webfolder.readShareFolder", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public int deleteNotInShareFolders(int fuid, int[] userSeqs) {
		Map paramMap = new HashMap();
		paramMap.put("fuid", fuid);
		paramMap.put("userSeqs", userSeqs);
		return (Integer)getSqlMapClientTemplate().delete("Webfolder.deleteNotInShareFolders", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public int readShareFolderCountByUid(int userSeq, int fuid) {
		Map paramMap = new HashMap();
		paramMap.put("fuid", fuid);
		paramMap.put("userSeq", userSeq);
		return (Integer)getSqlMapClientTemplate().queryForObject("Webfolder.readShareFolderCountByUid", paramMap);
	}
	
	public int readPublicFolderAuth(int userSeq) {
		return (Integer)getSqlMapClientTemplate().queryForObject("Webfolder.readPublicFolderAuth", userSeq);
	}
	
	@SuppressWarnings("unchecked")
	public int readPublicFolderAdminAuth(int domainSeq, int userSeq) {
		Map paramMap = new HashMap();
		paramMap.put("mailDomainSeq", domainSeq);
		paramMap.put("mailUserSeq", userSeq);
		return (Integer)getSqlMapClientTemplate().queryForObject("Webfolder.readPublicFolderAdminAuth", paramMap);
	}
	
	public int readWebFolderAttachMaxSize(int mailDomainSeq) {
		return (Integer)getSqlMapClientTemplate().queryForObject("Webfolder.readWebFolderAttachMaxSize", mailDomainSeq);
	}
	
	@SuppressWarnings("unchecked")
	public List<WebfolderDataVO> readAllShareFolderList(int mailDomainSeq, int mailUserSeq) {
		Map paramMap = new HashMap();
		paramMap.put("mailDomainSeq", mailDomainSeq);
		paramMap.put("mailUserSeq", mailUserSeq);
		return getSqlMapClientTemplate().queryForList("Webfolder.readAllShareFolderList", paramMap);
	}
	
	public WebfolderDataVO readAllShareFolder(int mailUserSeq, String path) {
		Map paramMap = new HashMap();
		paramMap.put("mailUserSeq", mailUserSeq);
		paramMap.put("path", path);
		return (WebfolderDataVO)getSqlMapClientTemplate().queryForObject("Webfolder.readAllShareFolder", paramMap);
	}
	
	public int readSubOrgFullCodeCount(int mailDomainSeq, int mailUserSeq, String orgFullCode) {
		orgFullCode = "%"+orgFullCode+"%";
		Map paramMap = new HashMap();
		paramMap.put("mailDomainSeq", mailDomainSeq);
		paramMap.put("mailUserSeq", mailUserSeq);
		paramMap.put("orgFullCode", orgFullCode);
		return (Integer)getSqlMapClientTemplate().queryForObject("Webfolder.readSubOrgFullCodeCount", paramMap);
	}
	
	public int readOrgFullCodeCount(int mailDomainSeq, int mailUserSeq, String orgFullCode) {
		Map paramMap = new HashMap();
		paramMap.put("mailDomainSeq", mailDomainSeq);
		paramMap.put("mailUserSeq", mailUserSeq);
		paramMap.put("orgFullCode", orgFullCode);
		return (Integer)getSqlMapClientTemplate().queryForObject("Webfolder.readOrgFullCodeCount", paramMap);
	}
	
	public int readMyShareFolderUid(int mailUserSeq, String folderDir) {
		Map paramMap = new HashMap();
		paramMap.put("mailUserSeq", mailUserSeq);
		paramMap.put("folderDir", folderDir);
		
		int folderUid = 0;
		Object obj = getSqlMapClientTemplate().queryForObject("Webfolder.readMyShareFolderUid", paramMap);
		if (obj != null) {
			folderUid = (Integer)obj;
		}
		return folderUid;
	}
}
