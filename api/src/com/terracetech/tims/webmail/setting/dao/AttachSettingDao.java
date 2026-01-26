package com.terracetech.tims.webmail.setting.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * AttachSettingDao MyBatis Mapper Interface
 * 원본: AttachSettingDao extends SqlSessionDaoSupport
 * 총 메서드 수: 1개
 */
@Mapper
public interface AttachSettingDao {
    /** 원본: public String readAttachInfo(String configName) */
    String readAttachInfo(@Param("configName") String configName);
}