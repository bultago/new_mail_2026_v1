package com.terracetech.tims.webmail.common.action;

import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.manager.DocTemplateManager;
import com.terracetech.tims.webmail.common.vo.DocTemplateVO;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.ResponseUtil;

@SuppressWarnings("all")
public class DocTemplateAction extends BaseAction{
	
	private static final long serialVersionUID = 8791381459232102520L;
	
	private DocTemplateManager docTemplateManager = null;
	
	public void setDocTemplateManager(DocTemplateManager docTemplateManager) {
		this.docTemplateManager = docTemplateManager;
	}
	
	public String excuteLoadTemplateList() throws Exception{
		String pageStr = request.getParameter("page");
		int page = (pageStr != null)?Integer.parseInt(pageStr):1;
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));		
		JSONObject jObj = new JSONObject();
		
		try {
			int templateBase = 5;
			int startPos = (page - 1) * templateBase;	
			int totalCount = docTemplateManager.getTotalCount(domainSeq);
			
			if(startPos < totalCount && totalCount > (startPos+templateBase)){
				jObj.put("next", page+1);
			} else {
				jObj.put("next", -1);			
			}
			
			if( startPos < totalCount && startPos > 0){
				jObj.put("pre", page-1);			
			} else {
				jObj.put("pre", -1);
			}
			
			jObj.put("page", page);
			jObj.put("list", docTemplateManager.getDocTemplateJsonList(domainSeq, startPos, templateBase));
			jObj.put("result", "success");
		} catch (Exception e) {
			jObj.put("result", "error");
		}		
		
		ResponseUtil.processResponse(response, jObj);
		return null;
	}
	
	public String excuteLoadTemplate() throws Exception{
		String templateSeqStr = request.getParameter("templateSeq");
		JSONObject jObj = new JSONObject();
		try {
			if(templateSeqStr != null){
				int templateSeq = Integer.parseInt(templateSeqStr);		
				int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));			
				DocTemplateVO vo = docTemplateManager.getDocTemplate(templateSeq);
				jObj.put("template", new String(vo.getTemplate()));
				jObj.put("result", "success");			
			} else {
				
			}
		} catch (Exception e) {
			jObj.put("result", "error");
		}
		
		ResponseUtil.processResponse(response, jObj);
		
		return null;
	}
	

}
