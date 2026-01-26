package com.terracetech.tims.webmail.setting.action;

import java.io.File;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.manager.SignManager;
import com.terracetech.tims.webmail.setting.vo.SignDataVO;
import com.terracetech.tims.webmail.util.FileUtil;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class ModifySignDataAction extends BaseAction {
	
	private SignManager signManager = null;
	private int signSeq = 0;
	private SignDataVO signDataVo = null;

	public void setSignManager(SignManager signManager) {
		this.signManager = signManager;
	}
	
	public String execute() throws Exception {
		
		User user = UserAuthManager.getUser(request);
		
		signDataVo = signManager.getSignData(Integer.parseInt(user.get(User.MAIL_USER_SEQ)), signSeq);

		if (signDataVo.getSignImage() != null) {
			String time = Long.toString(System.nanoTime());
			String viewSrc = context.getRealPath("/") + EnvConstants.getAttachSetting("attach.dir");
			String strLocalhost = request.getScheme() +"://"+ request.getServerName() +":"+ request.getServerPort();
			String viewUrl = EnvConstants.getAttachSetting("attach.url")+ "/";
			
			viewUrl = strLocalhost + viewUrl;
			String fileName = signDataVo.getSignImageName();
			fileName = time + "_" + fileName;
			
			File file = new File(viewSrc+fileName);
			if (FileUtil.writeFile(signDataVo.getSignImage(), file)) {
				signDataVo.setSignImageUrl(viewUrl+fileName);
			}
		}
		
		request.setAttribute("type", "modify");
		
		return "success";
	}

	public int getSignSeq() {
		return signSeq;
	}

	public void setSignSeq(int signSeq) {
		this.signSeq = signSeq;
	}

	public SignDataVO getSignDataVo() {
		return signDataVo;
	}

	public void setSignDataVo(SignDataVO signDataVo) {
		this.signDataVo = signDataVo;
	}
	
}
