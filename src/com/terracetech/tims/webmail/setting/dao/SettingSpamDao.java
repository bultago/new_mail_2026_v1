package com.terracetech.tims.webmail.setting.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.terracetech.tims.webmail.setting.vo.PSpamRuleVO;
import com.terracetech.tims.webmail.setting.vo.PSpameListItemVO;

public class SettingSpamDao extends SqlMapClientDaoSupport implements ISettingSpamDao {

	public void savePSpamRule (PSpamRuleVO vo) {
		getSqlMapClientTemplate().insert("Setting.savePSpamRule", vo);
	}
	
	public PSpamRuleVO readPSpamRule(int userSeq) {
		Object result = getSqlMapClientTemplate().queryForObject("Setting.readPSpamRule", userSeq);
		return result instanceof PSpamRuleVO ? (PSpamRuleVO)result : null;
	}
	
	public boolean modifyPSpamRule(PSpamRuleVO vo) {
		return getSqlMapClientTemplate().update("Setting.modifyPSpamRule", vo) == 1;
	}
	
	public boolean deletePSpameRule(int userSeq) {
		return getSqlMapClientTemplate().delete("Setting.deletePSpamRule", userSeq) == 1;
	}
	
	@SuppressWarnings("unchecked")
	public List<PSpameListItemVO> readPSpamWhiteList (int userSeq) {
		return getSqlMapClientTemplate().queryForList("Setting.readPSpamWhiteList", userSeq);
	}
	
	public void savePSpamWhiteList(PSpameListItemVO item) {
		getSqlMapClientTemplate().insert("Setting.savePSpameWhite", item);
		
	}
	
	public void savePSpamWhiteList (PSpameListItemVO[] vos) {
		if (vos != null && vos.length > 0) {
			for (PSpameListItemVO vo : vos) {
				getSqlMapClientTemplate().insert("Setting.savePSpameWhite", vo);
			}
		}
	}
	
	public void deletePSpamWhiteList (int userSeq) {
		getSqlMapClientTemplate().delete("Setting.deletePSpamWhiteList", userSeq);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<PSpameListItemVO> readPSpamBlackList (int userSeq) {
		return getSqlMapClientTemplate().queryForList("Setting.readPSpamBlackList", userSeq);
	}
	
	public void savePSpamBlackList(PSpameListItemVO item) {
		getSqlMapClientTemplate().insert("Setting.savePSpameBlack", item);
	}
	
	public void savePSpamBlackList (PSpameListItemVO[] vos) {
		if (vos != null && vos.length > 0) {
			for (PSpameListItemVO vo : vos) {
				getSqlMapClientTemplate().insert("Setting.savePSpameBlack", vo);
			}
		}
	}

	public void deletePSpamBlackList (int userSeq) {
		getSqlMapClientTemplate().delete("Setting.deletePSpamBlackList", userSeq);
	}

	public void deletePSpamWhiteList(int userSeq, String[] blackList) {
		Map<String, Object> param = new HashMap<String, Object>(2);
		param.put("userSeq", String.valueOf(userSeq));
		param.put("emails", blackList);
		
		getSqlMapClientTemplate().delete("Setting.deletePSpamWhiteList2", param);
	}

	public void deletePSpamBlackList(int userSeq, String[] blackList) {
		Map<String, Object> param = new HashMap<String, Object>(2);
		param.put("userSeq", String.valueOf(userSeq));
		param.put("emails", blackList);
		
		getSqlMapClientTemplate().delete("Setting.deletePSpamBlackList2", param);
	}


	
}
