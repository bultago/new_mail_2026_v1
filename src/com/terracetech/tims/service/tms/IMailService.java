package com.terracetech.tims.service.tms;

import com.terracetech.tims.service.tms.vo.ListCondVO;
import com.terracetech.tims.service.tms.vo.ListInfoVO;
import com.terracetech.tims.service.tms.vo.ReadCondVO;
import com.terracetech.tims.service.tms.vo.SendCondVO;
import com.terracetech.tims.service.tms.vo.SendResultVO;
import com.terracetech.tims.service.tms.vo.ViewContentVO;


public interface IMailService {
	
	public void setRemoteIp(String remoteIp);

	public ListInfoVO getContentsMailList(ListCondVO listVO) throws Exception;
	public ViewContentVO getViewMailContent(ReadCondVO readVO) throws Exception;
	public SendResultVO sendMailMessage(SendCondVO sendVO) throws Exception;
}
