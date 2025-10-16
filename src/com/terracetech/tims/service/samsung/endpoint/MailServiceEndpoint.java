package com.terracetech.tims.service.samsung.endpoint;

import org.springframework.remoting.jaxrpc.ServletEndpointSupport;

import com.terracetech.tims.service.samsung.IMailService;
import com.terracetech.tims.service.samsung.vo.AttachEtyCSVO;
import com.terracetech.tims.service.samsung.vo.ConditionCSVO;
import com.terracetech.tims.service.samsung.vo.DetailESBVO;
import com.terracetech.tims.service.samsung.vo.ExtractedAttachESBVO;
import com.terracetech.tims.service.samsung.vo.HeaderHelperCSVO;
import com.terracetech.tims.service.samsung.vo.ListESBVO;
import com.terracetech.tims.service.samsung.vo.RecipientEtyCSVO;
import com.terracetech.tims.service.samsung.vo.ResourceCSVO;
import com.terracetech.tims.service.samsung.vo.SyncListCSVO;



public class MailServiceEndpoint extends ServletEndpointSupport implements IMailService{

	
	protected void onInit() { 
	}
	
	public void setRemoteIp(String remoteIp) {
	}

	public ExtractedAttachESBVO getExtractedAttachInfo(String msgUID,
			String[] partNos, String[] exts, String folderName,
			ResourceCSVO resourceCSVO) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getMailDetailBody(String msgUID, String folderName,
			ResourceCSVO resourceCSVO) {
		// TODO Auto-generated method stub
		return null;
	}

	public DetailESBVO getMailDetailView(int msgNo,
			ConditionCSVO conditionCSVO, ResourceCSVO resourceCSVO) {
		// TODO Auto-generated method stub
		return null;
	}

	public DetailESBVO getMailDetailViewByUID(String msgUID,
			ConditionCSVO conditionCSVO, ResourceCSVO resourceCSVO) {
		// TODO Auto-generated method stub
		return null;
	}

	public ListESBVO getMailListAfterMsgNo(int msgNo, String folderName,
			ResourceCSVO resourceCSVO) {
		// TODO Auto-generated method stub
		return null;
	}

	public ListESBVO getSimpleMailList(String listOption, String currentTime,
			ConditionCSVO conditionCSVO, ResourceCSVO resourceCSVO) {
		// TODO Auto-generated method stub
		return null;
	}

	public String sendMISMail(String bodyStr,
			HeaderHelperCSVO headerHelperCSVO,
			RecipientEtyCSVO[] recipientEtyCSVO, AttachEtyCSVO[] attachEtyCSVO,
			ResourceCSVO resourceCSVO) {
		// TODO Auto-generated method stub
		return null;
	}

	public SyncListCSVO syncMails(SyncListCSVO syncListCSVO,
			ResourceCSVO resourceCSVO) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
