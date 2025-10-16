package com.terracetech.tims.webmail.mail.dao;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.terracetech.tims.webmail.mail.vo.SharedFolderUserVO;
import com.terracetech.tims.webmail.mail.vo.SharedFolderVO;

@SuppressWarnings("unchecked")
public class SharedFolderDao extends SqlMapClientDaoSupport {

	public List<SharedFolderVO> readSharedFolderList(int userSeq){		
		return getSqlMapClientTemplate().queryForList("MailSharedFolder.readSharedFolderList",userSeq);
	}
	
	public List<SharedFolderUserVO> readSharedFolderReaderList(int folderUid){		
		return getSqlMapClientTemplate().queryForList("MailSharedFolder.readSharedFolderReaderList",folderUid);
	}
	
	public List<SharedFolderVO> readUserSharedFolderList(int userSeq){
		return getSqlMapClientTemplate().queryForList("MailSharedFolder.readUserSharedFolder",userSeq);
	}
	
	public int getFolderUid(SharedFolderVO vo){
		return (Integer)getSqlMapClientTemplate().queryForObject("MailSharedFolder.readFolderUid",vo);
	}
	
	public void saveSharedFolder(SharedFolderVO vo){
		getSqlMapClientTemplate().insert("MailSharedFolder.saveSharedFolder",vo);
	}
	
	public void updateSharedFolder(SharedFolderVO vo){
		getSqlMapClientTemplate().update("MailSharedFolder.updateSharedFolder",vo);
	}
	
	public void removeSharedFolder(int folderUid){
		getSqlMapClientTemplate().delete("MailSharedFolder.removeSharedFolder",folderUid);
	}
	
	public void saveSharedFolderReader(SharedFolderUserVO vo){
		getSqlMapClientTemplate().insert("MailSharedFolder.saveSharedFolderReader",vo);
	}
	
	public void removeSharedFolderReader(int folderUid){
		getSqlMapClientTemplate().delete("MailSharedFolder.removeSharedFolderReader",folderUid);
	}
}
