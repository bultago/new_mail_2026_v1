package com.terracetech.tims.webmail.setting.dao;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.terracetech.tims.webmail.setting.vo.AutoReplyListVO;
import com.terracetech.tims.webmail.setting.vo.AutoReplyVO;

public class SettingAutoReplyDao extends SqlMapClientDaoSupport {
	
	public AutoReplyVO readAutoReply(int userSeq) {
		return (AutoReplyVO)getSqlMapClientTemplate().queryForObject("Setting.readAutoReply", userSeq);
	}
	
	@SuppressWarnings("unchecked")
	public List<AutoReplyListVO> readAutoReplyWhiteList(int userSeq) {
		return getSqlMapClientTemplate().queryForList("Setting.readAutoReplyWhiteList", userSeq);
	}
	
	public int modifyAutoReply(AutoReplyVO autoReplyVO) {
		return (Integer)getSqlMapClientTemplate().update("Setting.modifyAutoReply", autoReplyVO);
	}
	
	public int deleteAutoReplyWhiteList(int userSeq) {
		return (Integer)getSqlMapClientTemplate().delete("Setting.deleteAutoReplyWhiteList", userSeq);
	}
	
	public void saveAutoReplyWhiteList(AutoReplyListVO[] autoReplyListVOs) {
		for(AutoReplyListVO autoReplyListVO : autoReplyListVOs)
			getSqlMapClientTemplate().insert("Setting.saveAutoReplyWhiteList", autoReplyListVO);
	}

}
