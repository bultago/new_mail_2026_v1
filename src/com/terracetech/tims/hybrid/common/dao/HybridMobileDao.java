package com.terracetech.tims.hybrid.common.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class HybridMobileDao extends SqlMapClientDaoSupport {

    public void insertHybridAuth(String authKey, String authValue) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("authKey", authKey);
        paramMap.put("authValue", authValue);
        getSqlMapClientTemplate().insert("mobile.insertHybridAuth", paramMap);
    }

    public void deleteHybridAuth(String authKey) {
        getSqlMapClientTemplate().delete("mobile.deleteHybridAuth", authKey);
    }

    public String readHybridAuth(String authKey) {
        return (String)getSqlMapClientTemplate().queryForObject("mobile.readHybridAuth", authKey);
    }

    public String readMobileAccessConfig(int mailDomainSeq) {
        return (String)getSqlMapClientTemplate().queryForObject("mobile.readMobileAccessConfig", mailDomainSeq);
    }

    public Map<String, String> readUserMobileAccessKey(int mailUserSeq) {
        return (Map)getSqlMapClientTemplate().queryForMap("mobile.readUserMobileAccessKey",mailUserSeq,"key","value");
    }
}
