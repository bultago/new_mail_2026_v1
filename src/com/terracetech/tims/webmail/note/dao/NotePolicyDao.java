package com.terracetech.tims.webmail.note.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.terracetech.tims.webmail.note.vo.NotePolicyCondVO;
import com.terracetech.tims.webmail.note.vo.NotePolicyVO;

public class NotePolicyDao extends SqlMapClientDaoSupport implements INotePolicyDao {

	public NotePolicyVO readNotePolicy(int mailUserSeq) {
		return (NotePolicyVO)getSqlMapClientTemplate().queryForObject("Setting.readNotePolicy", mailUserSeq);
	}
	
	@SuppressWarnings("unchecked")
	public List<NotePolicyCondVO> readNotePolicyCondList(int mailUserSeq) {
		return getSqlMapClientTemplate().queryForList("Setting.readNotePolicyCondList", mailUserSeq);
	}
	
	public void saveNotePolicy(NotePolicyVO notePolicyVo) {
		getSqlMapClientTemplate().insert("Setting.saveNotePolicy", notePolicyVo);
	}
	
	public void modifyNotePolicy(NotePolicyVO notePolicyVo) {
		getSqlMapClientTemplate().update("Setting.modifyNotePolicy", notePolicyVo);
	}
	
	public void saveNotePolicyCond(NotePolicyCondVO notePolicyCondVo) {
		getSqlMapClientTemplate().insert("Setting.saveNotePolicyCond", notePolicyCondVo);
	}
	
	public void deleteNotePolicyCond(int mailUserSeq) {
		getSqlMapClientTemplate().delete("Setting.deleteNotePolicyCond", mailUserSeq);
	}
	
	public int checkNotePolicyCondMe(int mailUserSeq, int condTarget) {
		Map<String, Integer> paramMap = new HashMap<String, Integer>();
		paramMap.put("mailUserSeq", mailUserSeq);
		paramMap.put("condTarget", condTarget);
		
		return (Integer)getSqlMapClientTemplate().queryForObject("Setting.checkNotePolicyCondMe", paramMap);
	}
}
