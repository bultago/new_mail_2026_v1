package com.terracetech.tims.webmail.setting.action;

import java.io.File;
import java.util.List;
import java.util.Locale;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.manager.SignManager;
import com.terracetech.tims.webmail.setting.vo.SignDataVO;
import com.terracetech.tims.webmail.util.FileUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class SaveSignDataAction extends BaseAction {
	
	private SignManager signManager = null;
	private int signSeq = 0;
	private String signName = null;
	private String defaultSign = null;
	private String signImagePath = null;
	private String signImageName = null;
	private String signMode = null;
	private String signText = null;

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
		String tmpdir = EnvConstants.getBasicSetting("tmpdir");
		String msg = null;
		try {
			List<SignDataVO> signDataList = signManager.getSignDataList(userSeq);
			
			if (signDataList == null || signDataList.size() < 1) {
				defaultSign = "T";
			}
			
			SignDataVO signDataVo = new SignDataVO();
			signDataVo.setUserSeq(userSeq);
			signDataVo.setSignName(signName);
			signDataVo.setDefaultSign(defaultSign);
			signDataVo.setSignMode(signMode);
			signDataVo.setSignText(signText);
			
			if (signImagePath != null && !"".equals(signImagePath)) {
				File file = new File(tmpdir + EnvConstants.DIR_SEPARATOR + signImagePath);
				if (file.exists()) {
					byte[] signImage = FileUtil.readFile(file);
					signDataVo.setSignImage(signImage);
					signDataVo.setSignImageName(signImageName);
				}
			}
			signManager.saveSignData(signDataVo);
			msg = resource.getMessage("save.ok");
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			msg = resource.getMessage("save.fail");
		}
		
		request.setAttribute("msg", msg);
		return "success";
	}
	
	public String executeUpdate() throws Exception {
		 if (!checkPAID()) {
             return "paidError";
         }
		I18nResources resource = getMessageResource("setting");
		
		User user = UserAuthManager.getUser(request);
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		String tmpdir = EnvConstants.getBasicSetting("tmpdir");
		
		SignDataVO signDataVo = new SignDataVO();
		signDataVo.setSignSeq(signSeq);
		signDataVo.setUserSeq(userSeq);
		signDataVo.setSignName(signName);
		signDataVo.setDefaultSign(defaultSign);
		signDataVo.setSignMode(signMode);
		signDataVo.setSignText(signText);
		
		String msg = null;
		try {
			if (signImagePath != null && !"".equals(signImagePath)) {
				signImagePath = FileUtil.replaceBlockingWrongFilePath(signImagePath);
				
				File file = new File(tmpdir + EnvConstants.DIR_SEPARATOR + signImagePath);
				if (file.exists()) {
					byte[] signImage = FileUtil.readFile(file);
					signDataVo.setSignImage(signImage);
					signDataVo.setSignImageName(signImageName);
				}
			}
	
			signManager.modifySignData(signDataVo);
			
			SignDataVO defaultSignDataVo = signManager.getDefaultSignData(userSeq);
			if (defaultSignDataVo == null) {
				List<SignDataVO> signDataList = signManager.getSignDataList(userSeq);
				if (signDataList != null && signDataList.size() > 0) {
					SignDataVO firstSignDataVo = signDataList.get(0);
					firstSignDataVo.setDefaultSign("T");
					signManager.modifySignData(firstSignDataVo);
				}
			}
			msg = resource.getMessage("update.ok");
		}catch (Exception e) {
			msg = resource.getMessage("update.fail");
		}
		
		request.setAttribute("msg", msg);
		return "success";
	}
	
	public int getSignSeq() {
		return signSeq;
	}

	public void setSignSeq(int signSeq) {
		this.signSeq = signSeq;
	}

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	public String getDefaultSign() {
		return defaultSign;
	}

	public void setDefaultSign(String defaultSign) {
		this.defaultSign = defaultSign;
	}

	public String getSignImagePath() {
		return signImagePath;
	}

	public void setSignImagePath(String signImagePath) {
		this.signImagePath = signImagePath;
	}

	public String getSignImageName() {
		return signImageName;
	}

	public void setSignImageName(String signImageName) {
		this.signImageName = signImageName;
	}

	public String getSignMode() {
		return signMode;
	}

	public void setSignMode(String signMode) {
		this.signMode = signMode;
	}

	public String getSignText() {
		return signText;
	}

	public void setSignText(String signText) {
		this.signText = signText;
	}
}
