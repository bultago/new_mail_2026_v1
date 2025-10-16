package com.terracetech.tims.webmail.setting.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.terracetech.tims.webmail.setting.vo.Pop3VO;

public class SettingPop3Dao extends SqlMapClientDaoSupport implements ISettingPop3Dao{

	/* (non-Javadoc)
	 * @see com.terracetech.tims.webmail.setting.dao.ISettingPop3Dao#savePop3(com.terracetech.tims.webmail.setting.vo.Pop3VO)
	 */
	public void savePop3(Pop3VO pop3VO) {
		getSqlMapClientTemplate().insert("Setting.savePop3", pop3VO);
	}
	
	/* (non-Javadoc)
	 * @see com.terracetech.tims.webmail.setting.dao.ISettingPop3Dao#readPop3List(int)
	 */
	@SuppressWarnings("unchecked")
	public List<Pop3VO> readPop3List(int userSeq) {
		return getSqlMapClientTemplate().queryForList("Setting.readPop3List", userSeq);
	}
	
	/* (non-Javadoc)
	 * @see com.terracetech.tims.webmail.setting.dao.ISettingPop3Dao#readPop3(int, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public Pop3VO readPop3(int userSeq, String pop3Host, String pop3Id) {
		Map paramMap = new HashMap();
		paramMap.put("userSeq", userSeq);
		paramMap.put("pop3Host", pop3Host);
		paramMap.put("pop3Id", pop3Id);
		
		return (Pop3VO)getSqlMapClientTemplate().queryForObject("Setting.readPop3", paramMap);
	}
	
	/* (non-Javadoc)
	 * @see com.terracetech.tims.webmail.setting.dao.ISettingPop3Dao#deletePop3(int, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public int deletePop3(int userSeq, String pop3Host, String pop3Id) {
		Map paramMap = new HashMap();
		paramMap.put("userSeq", userSeq);
		paramMap.put("pop3Host", pop3Host);
		paramMap.put("pop3Id", pop3Id);
		
		return (Integer)getSqlMapClientTemplate().delete("Setting.deletePop3", paramMap);
	}
	
	/* (non-Javadoc)
	 * @see com.terracetech.tims.webmail.setting.dao.ISettingPop3Dao#modifyPop3(com.terracetech.tims.webmail.setting.vo.Pop3VO)
	 */
	public int modifyPop3(Pop3VO pop3Vo) {
		return (Integer)getSqlMapClientTemplate().update("Setting.modifyPop3", pop3Vo);
	}
	
	/* (non-Javadoc)
	 * @see com.terracetech.tims.webmail.setting.dao.ISettingPop3Dao#modifyPop3Msgname(int, java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public int modifyPop3Msgname(int userSeq, String pop3Host, String pop3Id, String pop3Msgname) {
		Map paramMap = new HashMap();
		paramMap.put("userSeq", userSeq);
		paramMap.put("pop3Host", pop3Host);
		paramMap.put("pop3Id", pop3Id);
		paramMap.put("pop3Msgname", pop3Msgname);
		
		return (Integer)getSqlMapClientTemplate().update("Setting.modifyPop3Msgname", paramMap);
	}
}
