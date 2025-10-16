package com.terracetech.tims.webmail.mail.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.terracetech.tims.webmail.common.advice.Transactional;
import com.terracetech.tims.webmail.mail.dao.SharedFolderDao;
import com.terracetech.tims.webmail.mail.vo.SharedFolderUserVO;
import com.terracetech.tims.webmail.mail.vo.SharedFolderVO;

public class SharedFolderHandler {
	private SharedFolderDao dao = null;

	public void setDao(SharedFolderDao dao) {
		this.dao = dao;
	}
	
	public Map<String, SharedFolderVO> getUserSharedFolderMap(int userSeq){
		List<SharedFolderVO> folderList = dao.readUserSharedFolderList(userSeq);
		Map<String, SharedFolderVO> folderMap = null;
		
		if(folderList != null && folderList.size() > 0){
			folderMap = new HashMap<String, SharedFolderVO>();
			for (SharedFolderVO vo : folderList) {
				folderMap.put(vo.getFolderName(), vo);
			}			
		}		
		
		return folderMap;		
	}
	
	public List<SharedFolderVO> getSharringFolders(int userSeq){
		return dao.readSharedFolderList(userSeq);
	}
	
	public List<SharedFolderUserVO> getSharedFolderReaderList(int folderUid){
		return dao.readSharedFolderReaderList(folderUid);
	}
	
	@Transactional
	public void saveSharedFolderInfo(SharedFolderVO sharedFolderVO, 
			SharedFolderUserVO[] sharringUsers){
		int folderUid = sharedFolderVO.getFolderUid();		
		if(folderUid > 0){			
			dao.removeSharedFolderReader(folderUid);
		}
		
		if(folderUid == 0){
			dao.saveSharedFolder(sharedFolderVO);
			folderUid = dao.getFolderUid(sharedFolderVO);
		}
		if(sharringUsers != null){
			for (SharedFolderUserVO sharedFolderUserVO : sharringUsers) {
				sharedFolderUserVO.setFolderUid(folderUid);
				dao.saveSharedFolderReader(sharedFolderUserVO);			
			}
		}
	}
	
	@Transactional
	public void deleteSharedFolderInfo(int folderUid){
		dao.removeSharedFolder(folderUid);
		dao.removeSharedFolderReader(folderUid);
	}
	
	@Transactional
	public void updateSharedFolderInfo(SharedFolderVO sharedFolderVO){
		dao.updateSharedFolder(sharedFolderVO);
	}
}
