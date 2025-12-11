package com.terracetech.tims.webmail.mailuser.dao;

import org.apache.ibatis.annotations.Mapper;

import com.terracetech.tims.webmail.common.vo.MailConfigVO;

/**
 * SettingSecureDao MyBatis Mapper Interface
 * 원본: SettingSecureDao extends SqlSessionDaoSupport
 * 총 메서드 수: 1개
 */
@Mapper
public interface SettingSecureDao {
    /** 원본: public MailConfigVO[] readPasswordPolicy() */
    MailConfigVO[] readPasswordPolicy();
}