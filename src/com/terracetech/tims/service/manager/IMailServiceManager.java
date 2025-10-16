package com.terracetech.tims.service.manager;

import com.terracetech.tims.service.tms.vo.AttachFileVO;
import com.terracetech.tims.service.tms.vo.FolderCondVO;
import com.terracetech.tims.service.tms.vo.FolderContentVO;
import com.terracetech.tims.service.tms.vo.FolderInfoVO;
import com.terracetech.tims.service.tms.vo.ListCondVO;
import com.terracetech.tims.service.tms.vo.ListInfoVO;
import com.terracetech.tims.service.tms.vo.MailWorkCondVO;
import com.terracetech.tims.service.tms.vo.MailWorkResultVO;
import com.terracetech.tims.service.tms.vo.ReadCondVO;
import com.terracetech.tims.service.tms.vo.SendCondVO;
import com.terracetech.tims.service.tms.vo.SendResultVO;
import com.terracetech.tims.service.tms.vo.ViewContentVO;
import com.terracetech.tims.service.tms.vo.WriteCondVO;
import com.terracetech.tims.service.tms.vo.WriteResultVO;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.vo.AttachInfoVO;

public interface IMailServiceManager {

	/**
	 * 특정 사용자의 메일함의 메일 목록을 제공하는 기능이다.
	 * 
	 * @param listVO
	 * @return
	 */
	public ListInfoVO doSimpleMailList(ListCondVO listVO);	
	
	public ListInfoVO doSimpleMailList(ListCondVO listVO,User user);

	/**
	 * 특정메일함의 메일의 본문을 보는 기능이다.
	 * 
	 * @param readVO
	 * @return
	 * @throws Exception
	 */
	public ViewContentVO doSimpleMailRead(ReadCondVO readVO) throws Exception;
	
	public ViewContentVO doSimpleMailRead(ReadCondVO readVO,User user) throws Exception;
	
	public AttachFileVO doSimpleMailDownLoadAttach(ReadCondVO readVO) throws Exception;
	
	public AttachFileVO doSimpleMailDownLoadAttach(ReadCondVO readVO,User user) throws Exception;
	
	
	
	/**
	 * 메일의 발송 기능이다.
	 * 
	 * @param sendVO
	 * @return
	 * @throws Exception
	 */
	public SendResultVO doSimpleMailSend(SendCondVO sendVO) throws Exception;
	
	public SendResultVO doSimpleMailSend(SendCondVO sendVO,User user) throws Exception;
	
	
	public MailWorkResultVO doSimpleMailWork(MailWorkCondVO workVO);
	
	public MailWorkResultVO doSimpleMailWork(MailWorkCondVO workVO, User user);
	
	
	public WriteResultVO doSimpleMailWrite(WriteCondVO writeVO) throws Exception;
	
	public WriteResultVO doSimpleMailWrite(WriteCondVO writeVO, User user) throws Exception;
	
	public FolderInfoVO doSimpleMailFolder(FolderCondVO folderCond) throws Exception;
	
	public FolderInfoVO doSimpleMailFolder(FolderCondVO folderCond, User user) throws Exception;
	
	public FolderContentVO doSimpleMailFolderContent(FolderCondVO folderCond) throws Exception;
	
	public FolderContentVO doSimpleMailFolderContent(FolderCondVO folderCond, User user) throws Exception;
	

}