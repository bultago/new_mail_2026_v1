package com.terracetech.tims.webmail.bbs.action;

import jakarta.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.bbs.manager.BbsManager;
import com.terracetech.tims.webmail.bbs.vo.BoardContentReplyVO;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.ResponseUtil;

public class DeleteContentReplyAction extends BaseAction {

	private BbsManager bbsManager = null;
	private int bbsId = 0;
	private int contentId = 0;
	private int isNotice = 0;
	private int replyId = 0;
	
	public void setBbsManager(BbsManager bbsManager) {
		this.bbsManager = bbsManager;
	}
	
	public String execute() throws Exception {
		 if (!checkPAID()) {
             return "paidError";
         }
		boolean isSuccess = false;
		JSONObject jsonObj = new JSONObject();
		
		BoardContentReplyVO boardContentReplyVo = new BoardContentReplyVO();
		boardContentReplyVo.setBbsId(bbsId);
		boardContentReplyVo.setContentId(contentId);
		boardContentReplyVo.setReplyId(replyId);
		boardContentReplyVo.setMailDomainSeq(Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ)));
		
		try{
			bbsManager.deleteContentReply(boardContentReplyVo);
			isSuccess = true;
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			isSuccess = false;
		}
		
		jsonObj.put("isSuccess", isSuccess);
		
		ResponseUtil.processResponse(response, jsonObj);
		return null;
	}

	public int getBbsId() {
		return bbsId;
	}

	public void setBbsId(int bbsId) {
		this.bbsId = bbsId;
	}

	public int getContentId() {
		return contentId;
	}

	public void setContentId(int contentId) {
		this.contentId = contentId;
	}

	public int getIsNotice() {
		return isNotice;
	}

	public void setIsNotice(int isNotice) {
		this.isNotice = isNotice;
	}

	public int getReplyId() {
		return replyId;
	}

	public void setReplyId(int replyId) {
		this.replyId = replyId;
	}
}
