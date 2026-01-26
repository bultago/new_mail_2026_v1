package com.terracetech.tims.webmail.setting.action;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SignManager;
import com.terracetech.tims.webmail.setting.vo.SignDataVO;
import com.terracetech.tims.webmail.setting.vo.SignVO;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringReplacer;

@SuppressWarnings("all")
public class ViewSignAction extends BaseAction {
	private StringReplacer sr = null;
	private SignManager signManager = null;
	private SignVO signVo = null;

	public void setSignManager(SignManager signManager) {
		this.signManager = signManager;
	}

	public String execute() throws Exception {

        int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        
        try {
            signVo = signManager.getSignInfo(mailUserSeq);
    
            if (signVo == null) {
                signVo = new SignVO();
                signVo.setMailUserSeq(mailUserSeq);
                signVo.setSignApply("F");
                signVo.setSignLocation("outside");
    
                signManager.saveSignInfo(signVo);
            }
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
        }
        return "success";
    }

	
	public String executeAjax() throws Exception {
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		sr = new StringReplacer();
		JSONObject signInfo = new JSONObject();
		JSONArray signList = new JSONArray();
		JSONObject signData = null;
		
		try {
			List<SignDataVO> signDataList = signManager.getSignSimpleDataList(mailUserSeq);
			if (signDataList != null && signDataList.size() > 0) {
				for (int i=0; i<signDataList.size(); i++) {
					signData = new JSONObject();
					String signName = sr.replaceXXS(signDataList.get(i).getSignName());
					
					signData.put("signSeq", Integer.toString(signDataList.get(i).getSignSeq()));
					signData.put("signName", signName);
					signData.put("isDefault", "T".equals(signDataList.get(i).getDefaultSign()));
					signList.add(signData);
				}
			}
			
			signInfo.put("isSuccess", true);
			signInfo.put("signList", signList);
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			signInfo.put("isSuccess", false);
		}
		
		ResponseUtil.processResponse(response, signInfo);
		
		return null;
	}

	public SignVO getSignVo() {
		return signVo;
	}

	public void setSignVo(SignVO signVo) {
		this.signVo = signVo;
	}

}
