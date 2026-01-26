package com.terracetech.tims.jmobile.mail.action;

import java.util.HashMap;
import java.util.Map;

import com.terracetech.tims.jmobile.common.action.BaseAction;
import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.service.manager.MailServiceManager;
import com.terracetech.tims.service.tms.vo.ReadCondVO;
import com.terracetech.tims.service.tms.vo.ViewContentVO;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.StringUtils;

@SuppressWarnings("all")
public class MailReadAction extends BaseAction {
	
	private static final long serialVersionUID = 5685712461734979183L;
	private MailServiceManager mailServiceManager = null;	
		
	public void setMailServiceManager(MailServiceManager mailServiceManager) {
		this.mailServiceManager = mailServiceManager;
	}


	public String execute() throws Exception{
		String forward = "";		
		Map paramMap = new HashMap();
		
		try{
			String sortBy = "arrival";
			String sortDir = "desc";
			
			String uid = request.getParameter("uid");
			String folderName = request.getParameter("folderName");
			String flag = request.getParameter("flag");
			String keyWord = request.getParameter("keyword");
			String contentsReadPosStr = request.getParameter("cpage");			
			int contentsReadPos = 0;
			
			Object preParam = request.getSession().getAttribute("preParamMap");
			Map preParamMap = null;
			
			if(preParam != null){
				preParamMap = (Map)preParam;
				if(preParamMap.containsKey("keyword")){
					keyWord = (String)preParamMap.get("keyword");
				}
				
				if(preParamMap.containsKey("folderName")){
					folderName = (String)preParamMap.get("folderName");
				}
				
				if(preParamMap.containsKey("uid")){
					uid = (String)preParamMap.get("uid");
				}
				
				if(preParamMap.containsKey("cpage")){
					contentsReadPosStr = (String)preParamMap.get("cpage");
				}
				request.getSession().removeAttribute("preParamMap");
			}
						
			if (StringUtils.isEmpty(folderName)) {
				folderName = FolderHandler.INBOX;
			}
			
			paramMap.put("folderName", folderName);			
			
			if (!StringUtils.isEmpty(contentsReadPosStr)) {
				contentsReadPos = Integer.parseInt(contentsReadPosStr)-1;		
			}
			
			if(!StringUtils.isEmpty(request.getParameter("readContentPageUp"))){
				contentsReadPos--;
			}
			
			if(!StringUtils.isEmpty(request.getParameter("readContentPageDown"))){
				contentsReadPos++;
			}
			
			paramMap.put("cpage", Integer.toString(contentsReadPos));
			paramMap.put("keyword", keyWord);
			paramMap.put("uid", uid);			
			
			if(uid == null)throw new Exception("USER["+user.get(User.MAIL_UID)+"] ReadMessage UID is null!");
			if(folderName == null)throw new Exception("USER["+user.get(User.MAIL_UID)+"] ReadMessage FolderName is null!");
			
			char flagType = (flag != null && flag.length() > 0)?flag.charAt(0):'x';
			
			String attachesDir = context.getRealPath("/") + EnvConstants.getAttachSetting("attach.dir");
			String hostStr = request.getScheme() +"://"+ request.getServerName() +":"+ request.getServerPort();
			
			String folderEncodeName = TMailUtility.IMAPFolderEncode(folderName);
			
			ReadCondVO readVO = new ReadCondVO();			
			readVO.setEmail(user.get(User.EMAIL));
			readVO.setUid(Long.parseLong(uid));
			readVO.setFolder(folderEncodeName);
			readVO.setViewImage(false);
			readVO.setLocale(user.get(User.LOCALE));
			readVO.setLocalURL(hostStr);
			readVO.setAttachDir(attachesDir);
			readVO.setSortBy(sortBy);
			readVO.setSortDir(sortDir);
			readVO.setRemoteIp(request.getRemoteAddr());
			if(StringUtils.isNotEmpty(keyWord)){				
				readVO.setKeyWord(keyWord);
			}			
			readVO.setFlagType(flagType);			
			
			ViewContentVO viewContents = mailServiceManager.doSimpleMailRead(readVO, user);
			String contents = viewContents.getContents();
			byte[] contentsBytes = contents.getBytes(); 
			if(contentsBytes.length > MAIL_CONTENTS_SIZE){
				int totalPage = contentsBytes.length/MAIL_CONTENTS_SIZE;
				if((contentsBytes.length % MAIL_CONTENTS_SIZE) > 0){
					totalPage++;
				}
				viewContents.setContents(splitContents(contentsReadPos,MAIL_CONTENTS_SIZE,contentsBytes));
				request.setAttribute("", user.get(User.LOCALE));
				contentsReadPos++;
				request.setAttribute("contentsCurrentPage", contentsReadPos);
				request.setAttribute("contentsTotalPage", totalPage);
			}			
			request.setAttribute("viewContents", viewContents);
			request.setAttribute("userLocale", user.get(User.LOCALE));
			request.setAttribute("uid", uid);
			request.setAttribute("folderName", folderName);
			request.setAttribute("keyWord", keyWord);
			
			savePreAction("mailread", paramMap);
			saveCurrentAction("mailread");
			forward = "success";
		} catch (Exception e) {
			e.printStackTrace();
			forward = "errorAlert";
		}
		return forward;
	}
	
	public String splitContents(int pos, int size, byte[] contentsBytes){
		int startBytesPos = pos * size;
		if((contentsBytes.length - startBytesPos) < size){	
			size = (contentsBytes.length - startBytesPos);
		}
		
		if(size < 0){
			size = 0;
		}
		
		byte[] readContents = new byte[size];
		System.arraycopy(contentsBytes, startBytesPos, readContents, 0, size);
		int multi = 0;
		for (int i = 0; i < size; i++) {
			if( readContents[i] < 0)multi++;
		}
		if((multi % 2) != 0){
			readContents = new byte[size-(multi % 2)];
			System.arraycopy(contentsBytes, 0, readContents, 0, readContents.length);
		}
		return new String(readContents);	
	}

}
