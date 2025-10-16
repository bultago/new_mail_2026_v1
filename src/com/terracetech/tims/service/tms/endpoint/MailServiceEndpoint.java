package com.terracetech.tims.service.tms.endpoint;

import java.io.File;

import javax.activation.DataHandler;

import com.terracetech.tims.service.tms.IMailService;
import com.terracetech.tims.service.tms.vo.ListCondVO;
import com.terracetech.tims.service.tms.vo.ListInfoVO;
import com.terracetech.tims.service.tms.vo.ReadCondVO;
import com.terracetech.tims.service.tms.vo.SendCondVO;
import com.terracetech.tims.service.tms.vo.SendResultVO;
import com.terracetech.tims.service.tms.vo.ViewContentVO;

public class MailServiceEndpoint implements IMailService {

	public void setRemoteIp(String remoteIp) {
		// TODO Auto-generated method stub

	}
	
	public ListInfoVO getContentsMailList(ListCondVO listVO) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public ViewContentVO getViewMailContent(ReadCondVO readVO) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public SendResultVO sendMailMessage(SendCondVO sendVO) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void sendMailMessage2(byte[] filedata) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
}
