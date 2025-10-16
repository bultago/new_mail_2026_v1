package com.terracetech.tims.webmail.setting.dao;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class AttachSettingDao extends SqlMapClientDaoSupport implements IAttachSettingDao {

	public String readAttachInfo(String configName) {
		return (String) getSqlMapClientTemplate().queryForObject("Setting.readAttachInfo", configName);
	}
	
}
