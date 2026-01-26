package com.terracetech.tims.webmail.common.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.terracetech.tims.webmail.common.vo.BannerVO;
import com.terracetech.tims.webmail.common.vo.MailConfigVO;

/**
 * SystemConfigDao MyBatis Mapper Interface
 * 
 * 원본 클래스: SystemConfigDao extends SqlSessionDaoSupport
 * 변환 내용: iBATIS → MyBatis Mapper 인터페이스
 * 변환일: 2025-10-20
 * 총 메서드 수: 24개 (원본 기준)
 */
@Mapper
public interface SystemConfigDao {

    /** 원본: public String getIntegrityInfo() */
    String getIntegrityInfo();

    /** 원본: public String getCryptMrthodInfo() */
    String getCryptMrthodInfo();

    /** 원본: public Map<String,String> getMailConfig(List<String> configNames) */
    Map<String,String> getMailConfig(List<String> configNames);

    /** 원본: public Map<String,String> getDomainConfig(Map<String,Object> configParam) */
    Map<String,String> getDomainConfig(Map<String,Object> configParam);

    /** 원본: public List<MailConfigVO> getMailConfigLikeList(String configName) */
    List<MailConfigVO> getMailConfigLikeList(@Param("configName") String configName);

    /** 원본: public Map<String, String> getWebAccessConfig() */
    Map<String, String> getWebAccessConfig();

    /** 원본: public BannerVO getDomainBanner(int mailDomainSeq) */
    BannerVO getDomainBanner(@Param("mailDomainSeq") int mailDomainSeq);

    /** 원본: public String getHostName(String hostId) */
    String getHostName(@Param("hostId") String hostId);

    /** 원본: public String getReservedMaxDay() */
    String getReservedMaxDay();

    /** 원본: public Map<String,String> getMailAdvanceSearchConfig() */
    Map<String,String> getMailAdvanceSearchConfig();

    /** 원본: public MailConfigVO readConfig(String key) */
    MailConfigVO readConfig(@Param("key") String key);

    /** 원본: public MailConfigVO readDomainConfig(int domainSeq, String configName) */
    MailConfigVO readDomainConfig(@Param("domainSeq") int domainSeq, @Param("configName") String configName);

    /** 원본: public List<Integer> readDomainSeqByConfigInfoList(String configName, String configValue) */
    List<Integer> readDomainSeqByConfigInfoList(@Param("configName") String configName, @Param("configValue") String configValue);

    /** 원본: public MailConfigVO readDomainGroupConfig(int domainSeq, int groupSeq, String configName) */
    MailConfigVO readDomainGroupConfig(@Param("domainSeq") int domainSeq, @Param("groupSeq") int groupSeq, 
                                       @Param("configName") String configName);

    /** 원본: public List<Integer> getDomainSystemConfigByValue(String configValue) */
    List<Integer> getDomainSystemConfigByValue(@Param("configValue") String configValue);

    /** 원본: public Map<String,String> readArchiveConfigFile(int domainSeq, int groupSeq) */
    Map<String,String> readArchiveConfigFile(@Param("domainSeq") int domainSeq, @Param("groupSeq") int groupSeq);

    /** 원본: public String readBayesianOption() */
    String readBayesianOption();

    /** 원본: public String readImapPort() */
    String readImapPort();

    /** 원본: public String readMdnPort() */
    String readMdnPort();

    /** 원본: public void deleteCache(String param) */
    void deleteCache(@Param("param") String param);

    /** 원본: public Map<String, String> readConfigFile(String configName) throws SQLException */
    Map<String, String> readConfigFile(@Param("configName") String configName) throws SQLException;

    /** 원본: public String readNoteLicense() */
    String readNoteLicense();

    /** 원본: public List<String> readIpGroup(String groupName) throws SQLException */
    List<String> readIpGroup(@Param("groupName") String groupName) throws SQLException;

    /** 원본: public MailConfigVO readDomainConfigDuplicateLogin(int mailDomainSeq) */
    MailConfigVO readDomainConfigDuplicateLogin(@Param("mailDomainSeq") int mailDomainSeq);
}