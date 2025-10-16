package com.terracetech.tims.webmail.setting.action;

import java.util.List;
import java.util.Locale;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.manager.SignManager;
import com.terracetech.tims.webmail.setting.vo.SignDataVO;
import com.terracetech.tims.webmail.setting.vo.SignVO;

public class DeleteSignDataAction extends BaseAction {
	
	private SignManager signManager = null;
	private int[] checkSignSeq = null;
	private String deleteType = null;

	public void setSignManager(SignManager signManager) {
		this.signManager = signManager;
	}
	
	public String execute() throws Exception {
		if (!checkPAID()) {
             return "paidError";
        }
		I18nResources resource = getMessageResource("setting");
		
		User user = UserAuthManager.getUser(request);
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		String msg = null;
		try{
			signManager.deleteSignData(userSeq, checkSignSeq);
			
			if ("all".equals(deleteType)) {
				SignVO signVo = new SignVO();
				signVo.setMailUserSeq(userSeq);
				signVo.setSignApply("F");
				signManager.updateSignInfo(signVo);
			}
			else {
				SignDataVO signDataVo = signManager.getDefaultSignData(userSeq);
				if (signDataVo == null) {
					List<SignDataVO> signDataList = signManager.getSignDataList(userSeq);
					if (signDataList != null && signDataList.size() > 0) {
						SignDataVO firstSignDataVo = signDataList.get(0);
						firstSignDataVo.setDefaultSign("T");
						signManager.modifySignData(firstSignDataVo);
					}
				}
			}
			msg = resource.getMessage("del.ok");
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			msg = resource.getMessage("del.fail");
		}
		
		request.setAttribute("msg", msg);
		return "success";
	}

	public int[] getCheckSignSeq() {
		return checkSignSeq;
	}

	public void setCheckSignSeq(int[] checkSignSeq) {
		this.checkSignSeq = checkSignSeq;
	}

	public String getDeleteType() {
		return deleteType;
	}

	public void setDeleteType(String deleteType) {
		this.deleteType = deleteType;
	}
}
