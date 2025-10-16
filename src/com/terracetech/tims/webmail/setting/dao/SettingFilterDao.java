package com.terracetech.tims.webmail.setting.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.terracetech.tims.webmail.setting.vo.FilterCondVO;
import com.terracetech.tims.webmail.setting.vo.FilterSubCondVO;
import com.terracetech.tims.webmail.setting.vo.FilterVO;

public class SettingFilterDao extends SqlMapClientDaoSupport implements ISettingFilterDao {
	
	@SuppressWarnings("unchecked")
	public List<FilterSubCondVO> readFilterSubcondList (int mailUserSeq, int condSeq ) {
		Map<String, Integer> param = new HashMap<String, Integer>();
		param.put("mail_user_seq", mailUserSeq);
		param.put("cond_seq", condSeq);
		return getSqlMapClientTemplate().queryForList("Setting.readFilterSubcondList", param);
	}
	
	@SuppressWarnings("unchecked")
	public List<FilterCondVO> readFilterCondList(int mailUserSeq) {
		return getSqlMapClientTemplate().queryForList("Setting.readFilterCondList", mailUserSeq);
	}
	
	public int readMaxFilterCondSeq(int mailUserSeq) {
		return (Integer) getSqlMapClientTemplate().queryForObject("Setting.readMaxFilterCondSeq", mailUserSeq);
	}
	
	public FilterVO readFilter(int mailUserSeq) {
		Object result = getSqlMapClientTemplate().queryForObject("Setting.readFilter", mailUserSeq);
		return result instanceof FilterVO ? (FilterVO)result : null;
	}
	
	public boolean saveFilterSubcond(FilterSubCondVO vo) {
		return getSqlMapClientTemplate().insert("Setting.saveFilterSubcond", vo) != null;
	}

	public boolean saveFilterCond(FilterCondVO vo) {
		return getSqlMapClientTemplate().insert("Setting.saveFilterCond", vo) != null;
	}
	
	public void saveFilter(FilterVO vo) {
		getSqlMapClientTemplate().insert("Setting.saveFilter", vo);
	}
	
	@SuppressWarnings("unchecked")
	public int deleteFilterSubcond(int mailUserSeq, int condSeq, int[] subcondSeqs) {
		Map param = new HashMap(3);
		param.put("mail_user_seq", mailUserSeq);
		param.put("cond_seq", condSeq);
		param.put("subcond_seq", subcondSeqs);
		return getSqlMapClientTemplate().delete("Setting.deleteFilterSubcond", param);
	}
	
	@SuppressWarnings("unchecked")
	public int deleteFilterCond (int mailUserSeq, int[] condSeqs) {
		Map param = new HashMap(2);
		param.put("mail_user_seq", mailUserSeq);
		param.put("cond_seq", condSeqs);
		return getSqlMapClientTemplate().delete("Setting.deleteFilterCond", param);
	}
	
	public int deleteFilter (int mailUserSeq) {
		return getSqlMapClientTemplate().delete("Setting.deleteFilter", mailUserSeq);
	}
	
	public boolean modifyFilterSubcond (FilterSubCondVO vo) {
		return getSqlMapClientTemplate().update("Setting.modifyFilterSubcond", vo) == 1;
	}
	
	public boolean modifyFilterCond (FilterCondVO vo) {
		return getSqlMapClientTemplate().update("Setting.modifyFilterCond", vo) == 1;
	}
	
	public boolean modifyFilter (FilterVO vo) {
		return getSqlMapClientTemplate().update("Setting.modifyFilter", vo) == 1;
	}
	
	public int deleteFilterSubcond(int mailUserSeq, int condSeq) {
		Map param = new HashMap(2);
		param.put("mail_user_seq", mailUserSeq);
		param.put("cond_seq", condSeq);
		
		return getSqlMapClientTemplate().delete("Setting.deleteAllFilterSubcond", param);
	}
	
	

}