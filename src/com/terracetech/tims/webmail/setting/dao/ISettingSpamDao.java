package com.terracetech.tims.webmail.setting.dao;

import java.util.List;

import com.terracetech.tims.webmail.setting.vo.PSpamRuleVO;
import com.terracetech.tims.webmail.setting.vo.PSpameListItemVO;

public interface ISettingSpamDao {

	public  void savePSpamRule(PSpamRuleVO vo);

	public  PSpamRuleVO readPSpamRule(int userSeq);

	public  boolean modifyPSpamRule(PSpamRuleVO vo);

	public  boolean deletePSpameRule(int userSeq);

	public  List<PSpameListItemVO> readPSpamWhiteList(int userSeq);

	public  void savePSpamWhiteList(PSpameListItemVO[] vos);
	
	public void savePSpamWhiteList(PSpameListItemVO item);

	public  void deletePSpamWhiteList(int userSeq);

	public  List<PSpameListItemVO> readPSpamBlackList(int userSeq);

	public void savePSpamBlackList(PSpameListItemVO item);
	
	public  void savePSpamBlackList(PSpameListItemVO[] vos);

	public  void deletePSpamBlackList(int userSeq);

	public void deletePSpamWhiteList(int userSeq, String[] blackList);

	public void deletePSpamBlackList(int userSeq, String[] blackList);
}