package com.terracetech.tims.webmail.setting.dao;

import java.util.List;

import com.terracetech.tims.webmail.setting.vo.Pop3VO;

public interface ISettingPop3Dao {

	public void savePop3(Pop3VO pop3VO);

	public List<Pop3VO> readPop3List(int userSeq);

	public Pop3VO readPop3(int userSeq, String pop3Host, String pop3Id);

	public int deletePop3(int userSeq, String pop3Host, String pop3Id);

	public int modifyPop3(Pop3VO pop3Vo);

	public int modifyPop3Msgname(int userSeq, String pop3Host, String pop3Id,
			String pop3Msgname);

}