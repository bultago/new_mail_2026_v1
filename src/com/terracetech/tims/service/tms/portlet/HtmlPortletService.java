package com.terracetech.tims.service.tms.portlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.terracetech.tims.service.tms.IMailService;
import com.terracetech.tims.service.tms.vo.ListCondVO;
import com.terracetech.tims.service.tms.vo.ListInfoVO;
import com.terracetech.tims.service.tms.vo.ReadCondVO;
import com.terracetech.tims.service.tms.vo.SendCondVO;
import com.terracetech.tims.service.tms.vo.SendResultVO;
import com.terracetech.tims.service.tms.vo.ViewContentVO;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.exception.MailNotFoundException;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.UserEtcInfoVO;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class HtmlPortletService implements IMailPortletService {
	
	public Logger log = Logger.getLogger(this.getClass());
	
	private IMailService service = null;

	public HtmlPortletService(IMailService service) {
		this.service = service;
	}

	public String doSimpleMailList(HttpServletRequest request, HttpServletResponse response) {
		try{
			User user = null;
			try {
				user = UserAuthManager.getUser(request);
			} catch (Exception e) {
				throw new Exception("FAIL GET USER INFO!!");
			}
			
			service.setRemoteIp(request.getRemoteAddr());
			String folder = request.getParameter("fn");
			String pageStr = request.getParameter("page");
			String cnt = request.getParameter("cnt");
			String flag = request.getParameter("flag");
			
			ListCondVO listVO = new ListCondVO();			
			SettingManager userSettingManager =  (SettingManager)ApplicationBeanUtil.getApplicationBean("settingManager");
			UserEtcInfoVO userSettingVo = userSettingManager.readUserEtcInfo(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));			
			listVO.setFolderName(folder);			
			
			int page;
			if(StringUtils.isEmpty(pageStr)){
				page = 1;
			}else{
				try {
					page = Integer.parseInt(pageStr);
				} catch (Exception e) {
					page = 1;
				}
			}
			listVO.setPage(page);			
			
			if(StringUtils.isEmpty(cnt)){
				listVO.setPageBase(userSettingVo.getPageLineCnt());
			} else {
				try {
					listVO.setPageBase(Integer.parseInt(cnt));	
				} catch (Exception e) {
					listVO.setPageBase(userSettingVo.getPageLineCnt());
				}
			}
			listVO.setEmail(user.get(User.EMAIL));
			listVO.setLocale(user.get(User.LOCALE));			
			
			char flagType = (flag != null && flag.length() > 0)?flag.charAt(0):'x';
			listVO.setFlagType(flagType);
						
			
			ListInfoVO listVo = service.getContentsMailList(listVO);		
			request.setAttribute("mailbox",listVo);	
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		return "list";
	}

	public String doSimpleMailRead(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		try{
			User user = null;
			try {
				user = UserAuthManager.getUser(request);
			} catch (Exception e) {
				throw new Exception("FAIL GET USER INFO!!");
			}
			
			service.setRemoteIp(request.getRemoteAddr());
			
			String viewImg = request.getParameter("viewImg");
			String uid = request.getParameter("uid");
			String folder = request.getParameter("fn");	
			
			String attachesDir = request.getSession().getServletContext().getRealPath("/") + EnvConstants.getAttachSetting("attach.dir");
			String hostStr = request.getScheme() +"://"+ request.getServerName() +":"+ request.getServerPort(); 
			
			ReadCondVO readVO = new ReadCondVO();
			readVO.setEmail(user.get(User.EMAIL));
			readVO.setUid(Long.parseLong(uid));
			readVO.setFolder(folder);
			readVO.setViewImage("on".equalsIgnoreCase(viewImg));
			readVO.setLocale(user.get(User.LOCALE));
			readVO.setLocalURL(hostStr);
			readVO.setAttachDir(attachesDir);
			
			
			ViewContentVO viewVO = service.getViewMailContent(readVO);
			request.setAttribute("mail",viewVO);	
			
		}catch(MailNotFoundException ignore){
			log.warn(ignore.getMessage());
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		return "read";
	}

	public String doSimpleMailSend(HttpServletRequest request,
			HttpServletResponse response, User user) throws Exception {
		
		String sendType = request.getParameter("savesent");
		String[] toAddr = request.getParameterValues("to");
		String[] ccAddr = request.getParameterValues("cc");
		String[] bccAddr = request.getParameterValues("bcc");
		String subject = request.getParameter("subject");
		String content = request.getParameter("contents");
		String encode = request.getParameter("encode");
		String signSeq = request.getParameter("signseq");
		String editorMode = request.getParameter("editmode");
		String port = EnvConstants.getBasicSetting("web.port");		
		port = (port != null && port.length() > 0)?port:Integer.toString(request.getServerPort());
		String mdnHost = EnvConstants.getMailSetting("mdn.host");
		String localhost = request.getScheme() + "://" 
				+ request.getServerName() + ":" + port;		
		try{
			
			if(toAddr == null){
				throw new Exception("NO TO RCPT ADDR!!");
			}
			sendType = (sendType == null)?"normal":sendType;
			subject = (subject == null)?"":subject;
			content = (content == null)?"":content;
			encode = (encode == null)?"UTF-8":encode;
			editorMode = (editorMode == null)?"html":editorMode;
			
			service.setRemoteIp(request.getRemoteAddr());
			SendCondVO sendVO = new SendCondVO();			
			sendVO.setToAddr(toAddr);
			sendVO.setCcAddr(ccAddr);
			sendVO.setBccAddr(bccAddr);
			sendVO.setSubject(subject);
			sendVO.setContent(content);
			sendVO.setEditMode("html");
			sendVO.setEncode(encode);
			sendVO.setLocale(user.get(User.LOCALE));
			sendVO.setMdnHost(mdnHost);
			sendVO.setLocalhost(localhost);
			sendVO.setRemoteIp(request.getRemoteAddr());
			sendVO.setSenderEmail(user.get(User.EMAIL));
			sendVO.setSenderName(user.get(User.USER_NAME));			
			sendVO.setOnesend("on".equals(request.getParameter("onesend")));
			sendVO.setPriority("on".equals(request.getParameter("priority")));
			sendVO.setReceivnoti("on".equals(request.getParameter("receivnoti")));
			sendVO.setSavesent("on".equals(request.getParameter("savesent")));
			sendVO.setVcard("on".equals(request.getParameter("vcard")));
			sendVO.setEditMode(editorMode);
			if(signSeq != null){
				sendVO.setSignUse("on".equals(request.getParameter("sign")));
				sendVO.setSignSeq(Integer.parseInt(signSeq));
			} else {
				sendVO.setSignUse(false);
			}			
			sendVO.setSendType(sendType);
			SendResultVO sendResultVO = service.sendMailMessage(sendVO);
			
			request.setAttribute("sendResult",sendResultVO);	
		}catch(Exception e){
			log.error(e.getMessage(), e);			
		}

		return "send";
	}

	public String doReturnError(HttpServletRequest request,
			HttpServletResponse response, String rootName) throws Exception {
		return "error";
	}

	
}
