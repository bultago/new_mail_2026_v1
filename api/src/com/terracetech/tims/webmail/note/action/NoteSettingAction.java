package com.terracetech.tims.webmail.note.action;

import java.util.List;

import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.note.manager.NoteManager;
import com.terracetech.tims.webmail.note.vo.NotePolicyCondVO;
import com.terracetech.tims.webmail.note.vo.NotePolicyVO;
import com.terracetech.tims.webmail.util.ResponseUtil;

public class NoteSettingAction extends BaseAction {

	private final String ALLOWALL = "allowAll";
	private final String REJECTALL = "rejectAll";
	private final String WHITEONLY = "whiteOnly";
	private final String BLACKONLY = "blackOnly";
	
	private String policyType = null;
	private String[] condTarget;
	
	private NotePolicyVO noteResult = null;
	private List<NotePolicyCondVO> noteCondResultList = null;
	
	private NoteManager noteManager = null;

	public void setNoteManager(NoteManager noteManager) {
		this.noteManager = noteManager;
	}
	
	public String execute() throws Exception {
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		noteResult = noteManager.readNotePolicy(mailUserSeq);
		if (noteResult != null) {
			if (WHITEONLY.equalsIgnoreCase(noteResult.getPolicyType()) || BLACKONLY.equalsIgnoreCase(noteResult.getPolicyType())) {
				noteCondResultList = noteManager.readNotePolicyCondList(mailUserSeq);
			}
		}
		
		return "success";
	}
	
	public String executeSave() throws Exception {
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		JSONObject result = new JSONObject();
		
		NotePolicyVO notePolicyVo = new NotePolicyVO();
		notePolicyVo.setMailUserSeq(mailUserSeq);
		notePolicyVo.setPolicyType(policyType);
		
		try {
			NotePolicyVO beforeNotePolicyVo = noteManager.readNotePolicy(mailUserSeq);
			if (beforeNotePolicyVo != null) {
				noteManager.modifyNotePolicy(notePolicyVo);
			} else {
				noteManager.saveNotePolicy(notePolicyVo);
			}
			
			if (WHITEONLY.equalsIgnoreCase(policyType) || BLACKONLY.equalsIgnoreCase(policyType)) {
				noteManager.deleteNotePolicyCond(mailUserSeq);
				if (condTarget != null && condTarget.length > 0) {
					NotePolicyCondVO notePolicyCondVo = null;
					for (int i=0; i <condTarget.length; i++) {
						notePolicyCondVo = new NotePolicyCondVO();
						notePolicyCondVo.setMailUserSeq(mailUserSeq);
						notePolicyCondVo.setCondTarget(Integer.parseInt(condTarget[i]));
						noteManager.saveNotePolicyCond(notePolicyCondVo);
					}
				}
			}
			result.put("isSuccess", true);
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage());
			result.put("isSuccess", false);
		}
		
		ResponseUtil.processResponse(response, result);
		
		return null;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

	public void setCondTarget(String[] condTarget) {
		this.condTarget = condTarget;
	}

	public NotePolicyVO getNoteResult() {
		return noteResult;
	}

	public List<NotePolicyCondVO> getNoteCondResultList() {
		return noteCondResultList;
	}
	
}
