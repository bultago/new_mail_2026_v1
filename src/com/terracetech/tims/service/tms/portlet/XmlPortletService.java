package com.terracetech.tims.service.tms.portlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.terracetech.tims.service.tms.impl.MailService;
import com.terracetech.tims.service.tms.vo.AttachFileVO;
import com.terracetech.tims.service.tms.vo.EmailAddressVO;
import com.terracetech.tims.service.tms.vo.ListCondVO;
import com.terracetech.tims.service.tms.vo.ListContentVO;
import com.terracetech.tims.service.tms.vo.ListInfoVO;
import com.terracetech.tims.service.tms.vo.ReadCondVO;
import com.terracetech.tims.service.tms.vo.SendCondVO;
import com.terracetech.tims.service.tms.vo.SendResultVO;
import com.terracetech.tims.service.tms.vo.ViewContentVO;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.exception.MailNotFoundException;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.UserEtcInfoVO;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;
import com.terracetech.tims.webmail.util.StringUtils;
import com.terracetech.tims.webmail.util.XMLUtils;

public class XmlPortletService implements IMailPortletService {	

	public Logger log = Logger.getLogger(this.getClass());
	
	private MailService service = null;

	public XmlPortletService(MailService service) {
		this.service = service;
	}

	/**
	 * <pre>
	 * <mailbox>
	 *    <folderName>Inbox</folderName>
	 *    <messageList count='1' total='100' page='1'>
	 *       <message>
	 *          <msgUid>1234</msgUid>
	 *          <subject>����</subject>
	 *          <personName>������� �̸�</personName>
	 *          <personEmail>������� �̸���</personEmail>
	 *          <sentDate>������¥</sentDate>
	 *          <mailType>��������</mailType>
	 *          <charset>charset</charset>
	 *          <size>ũ��</size>
	 *       </message>
	 *    </messageList>
	 * </mailbox>
	 * </pre>
	 * 
	 * @param request
	 * @param doc
	 */
	public String doSimpleMailList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		DocumentBuilderFactory factory  = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		Document doc = null;
		DocumentBuilder builder;
		Element resultElement = null;		
		try{
			User user = null;
			try {
				user = UserAuthManager.getUser(request);
			} catch (Exception e) {
				throw new Exception("FAIL GET USER INFO!!");
			}
			
			try {
				builder = factory.newDocumentBuilder();
				doc = builder.newDocument();
			} catch (ParserConfigurationException e) {			
				throw e;
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
			
			if(StringUtils.isEmpty(folder)){
				folder = "Inbox";
			}
			
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
						
			Element mailbox = doc.createElement("mailbox");
			doc.appendChild(mailbox);
			
			resultElement = doc.createElement("actionResult");
			resultElement.setTextContent("success");
			mailbox.appendChild(resultElement);
	
			ListInfoVO listVo = service.getContentsMailList(listVO,user);		
	
			if (listVo != null) {
				Element folderName = doc.createElement("folderName");
				folderName.setTextContent(listVo.getFolderName());
				mailbox.appendChild(folderName);
	
				Element messageList = doc.createElement("messageList");
				messageList.setAttribute("count", String.valueOf(listVo.getSearchCnt()));
				messageList.setAttribute("total", String.valueOf(listVo.getTotalCnt()));
				messageList.setAttribute("page", String.valueOf(listVo.getPageNo()));
				ListContentVO[] listContents = listVo.getListContents();
				if (listContents != null) {
					Element message = null;
					Element tmpElement = null;
					
					for (ListContentVO listContent : listContents) {
						message = doc.createElement("message");				 
						tmpElement = doc.createElement("msgUid");
						tmpElement.setTextContent(Long.toString(listContent.getId()));
						message.appendChild(tmpElement);
						tmpElement = doc.createElement("subject");
						tmpElement.setTextContent(listContent.getSubject());
						message.appendChild(tmpElement);
						tmpElement = doc.createElement("personName");
						tmpElement.setTextContent(listContent.getName());
						message.appendChild(tmpElement);
						tmpElement = doc.createElement("personEmail");
						tmpElement.setTextContent(listContent.getEmail());
						message.appendChild(tmpElement);
						tmpElement = doc.createElement("sentDate");
						tmpElement.setTextContent(listContent.getDate());
						message.appendChild(tmpElement);
						tmpElement = doc.createElement("size");
						tmpElement.setTextContent(listContent.getSize());		
						message.appendChild(tmpElement);
	
						messageList.appendChild(message);
					}
					tmpElement = null;
					message = null;
					
				}
				mailbox.appendChild(messageList);
			}			
			
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			try {
				builder = factory.newDocumentBuilder();
				doc = builder.newDocument();
			} catch (ParserConfigurationException pe) {			
				pe.printStackTrace();
				throw pe;
			}
			if(doc != null){
				Element mailbox = doc.createElement("mailbox");
				doc.appendChild(mailbox);
				resultElement = doc.createElement("actionResult");
				resultElement.setTextContent("error");
				mailbox.appendChild(resultElement);
			}
		}
		
		try {
			if(StringUtils.isNotEmpty(request.getParameter("xsl"))){
				XMLUtils.writeTo(doc, response, request.getParameter("xsl"));	
			}else{
				XMLUtils.writeTo(doc, response);
			}
			
		} catch (Exception e) {
			throw e;
		}
		
		return null;
	}

	/**
	 * <pre>
	 * <mail-content>
	 * 	  <actionResult>success</actionResult>
	 * 	  <folder>Inbox</folder>	 *    
	 *    <sentDate>2009/01/04</sentDate>
	 *    <subject>TEST</subject>
	 *    <from>
	 *       <personal>test1</to>
	 *       <address>test1@test.com</address>
	 *    </from>
	 *    <to>
	 *       <email>
	 *       	<personal>test1</personal>
	 *       	<address>test1@test.com</address>
	 *       </email>
	 *    </to>
	 *    <cc>
	 *       <email>
	 *       	<personal>test1</personal>
	 *       	<address>test1@test.com</address>
	 *       </email>
	 *    </cc>    
	 *    <attachList>
	 *       <attach type="vcard">
	 *          <size>1234</size>
	 *          <name>����</name>
	 *          <pos>1</pos>
	 *       </attach>
	 *       <attach type="image">
	 *          <size>1234</size>
	 *          <name>����</name>
	 *          <pos>1:1</pos>
	 *       </attach>
	 *       <attach type="file">
	 *          <size>1234</size>
	 *          <name>����</name>
	 *          <pos>1:1</pos>
	 *       </attach>
	 *    </attachList>
	 *    <content>test</content>
	 *    <preUid></preUid>
	 *    <nextUid></nextUid>
	 * </mail-content>
	 * <pre>
	 * 
	 * 
	 * @param request
	 * @param doc
	 */
	public String doSimpleMailRead(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DocumentBuilderFactory factory  = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		Document doc = null;
		DocumentBuilder builder;
		Element resultElement = null;	
		
		try{
			User user = null;
			try {
				user = UserAuthManager.getUser(request);
			} catch (Exception e) {
				throw new Exception("FAIL GET USER INFO!!");
			}
			
			try {
				builder = factory.newDocumentBuilder();
				doc = builder.newDocument();
			} catch (ParserConfigurationException e) {			
				throw e;
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
			
			
			ViewContentVO viewVO = service.getViewMailContent(readVO,user);
			Element mailContent = doc.createElement("mail-content");
			doc.appendChild(mailContent);
			
			Element tempElement = null;
			Element tempInnerElement = null;
			Element tempInner2Element = null;
			
			tempElement = doc.createElement("actionResult");
			tempElement.setTextContent("success");
			mailContent.appendChild(tempElement);
			
			if(viewVO != null){
				tempElement = doc.createElement("folder");
				tempElement.setTextContent(viewVO.getFolderName());
				mailContent.appendChild(tempElement);
				
				tempElement = doc.createElement("sentDate");
				tempElement.setTextContent(viewVO.getDate());
				mailContent.appendChild(tempElement);
				
				tempElement = doc.createElement("subject");
				tempElement.setTextContent(viewVO.getSubject());
				mailContent.appendChild(tempElement);
				
				EmailAddressVO from = viewVO.getFrom();
				if(from != null){
					tempElement = doc.createElement("from");
					tempInnerElement = doc.createElement("personal");
					tempInnerElement.setTextContent(from.getPersonal());
					tempElement.appendChild(tempInnerElement);
					
					tempInnerElement = doc.createElement("address");
					tempInnerElement.setTextContent(from.getAddress());
					tempElement.appendChild(tempInnerElement);
					
					mailContent.appendChild(tempElement);					
				}
				EmailAddressVO[] to = viewVO.getTos();
				if(to != null){
					tempElement = doc.createElement("to");					
					for (int i = 0; i < to.length; i++) {
						tempInnerElement = doc.createElement("email");
						
						tempInner2Element = doc.createElement("personal");
						tempInner2Element.setTextContent(to[i].getPersonal());
						tempInnerElement.appendChild(tempInner2Element);
						
						tempInner2Element = doc.createElement("address");
						tempInner2Element.setTextContent(to[i].getAddress());
						tempInnerElement.appendChild(tempInner2Element);
						
						tempElement.appendChild(tempInnerElement);
					}
					mailContent.appendChild(tempElement);
				}
				EmailAddressVO[] cc = viewVO.getCcs();
				if(cc != null){
					tempElement = doc.createElement("cc");					
					for (int i = 0; i < cc.length; i++) {
						tempInnerElement = doc.createElement("email");
						
						tempInner2Element = doc.createElement("personal");
						tempInner2Element.setTextContent(cc[i].getPersonal());
						tempInnerElement.appendChild(tempInner2Element);
						
						tempInner2Element = doc.createElement("address");
						tempInner2Element.setTextContent(cc[i].getAddress());
						tempInnerElement.appendChild(tempInner2Element);
						
						tempElement.appendChild(tempInnerElement);
					}
					mailContent.appendChild(tempElement);
				}
				AttachFileVO[] attachVOs = viewVO.getAttachs();
				if(attachVOs != null){					
					tempElement = doc.createElement("attachList");		
					for (AttachFileVO attachFileVO : attachVOs) {
						tempInnerElement = doc.createElement("attach");
						tempInnerElement.setAttribute("type", attachFileVO.getType());
						
						tempInner2Element = doc.createElement("size");
						tempInner2Element.setTextContent(Long.toString(attachFileVO.getSize()));
						tempInnerElement.appendChild(tempInner2Element);
						
						tempInner2Element = doc.createElement("name");
						tempInner2Element.setTextContent(attachFileVO.getName());
						tempInnerElement.appendChild(tempInner2Element);
						
						tempInner2Element = doc.createElement("pos");
						tempInner2Element.setTextContent(attachFileVO.getDepth());
						tempInnerElement.appendChild(tempInner2Element);
						
						tempElement.appendChild(tempInnerElement);						
					}
					
					mailContent.appendChild(tempElement);
				}
				String content = viewVO.getContents();
				Element contentElement = doc.createElement("content");
				
				CDATASection contentCDATA = doc.createCDATASection("content");
				contentCDATA.setTextContent((content != null)?content:"");
				contentElement.appendChild(contentCDATA);
				mailContent.appendChild(contentElement);
			}
			
		}catch(MailNotFoundException ignore){
			log.warn(ignore.getMessage());
			
			try {
				builder = factory.newDocumentBuilder();
				doc = builder.newDocument();
			} catch (ParserConfigurationException pe) {			
				pe.printStackTrace();
				throw pe;
			}
			if(doc != null){
				Element mailbox = doc.createElement("mail-content");
				doc.appendChild(mailbox);
				resultElement = doc.createElement("actionResult");
				resultElement.setTextContent("error");
				mailbox.appendChild(resultElement);
			}
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			
			try {
				builder = factory.newDocumentBuilder();
				doc = builder.newDocument();
			} catch (ParserConfigurationException pe) {			
				pe.printStackTrace();
				throw pe;
			}
			if(doc != null){
				Element mailbox = doc.createElement("mail-content");
				doc.appendChild(mailbox);
				resultElement = doc.createElement("actionResult");
				resultElement.setTextContent("error");
				mailbox.appendChild(resultElement);
			}
		}
		
		
		try {
			if(StringUtils.isNotEmpty(request.getParameter("xsl"))){
				XMLUtils.writeTo(doc, response, request.getParameter("xsl"));	
			}else{
				XMLUtils.writeTo(doc, response);
			}
			
		} catch (Exception e) {
			throw e;
		}
		
		return null;
	}
	
	/**
	 * <pre>
	 * <mail-send>
	 *	<actionResult>success</actionResult>
	 *	<send-type>normal</send-type>
	 *	<message-id></message-id>
	 * 	<send-emails>
	 * 		<email>aaaa@aa.com</email>
	 * 		<email>aaaa@aa.com</email>
	 * 	</send-emails>
	 * 	<fail-emails>
	 * 		<email>aaaa@aa.com</email>
	 * 		<email>aaaa@aa.com</email>
	 * 	</fail-emails>
	 * </mail-send>
	 * <pre>
	 * 
	 * 
	 * @param request
	 * @param doc
	 */
	public String doSimpleMailSend(HttpServletRequest request,
			HttpServletResponse response, User user) throws Exception {
		DocumentBuilderFactory factory  = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		Document doc = null;
		DocumentBuilder builder;
		Element resultElement = null;
		
		String sendType = request.getParameter("savesent");
		String sender = request.getParameter("sender");
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
			if(StringUtils.isEmpty(sender))
				throw new Exception("NOT EXIST USER");
			
			String[] senderInfo = sender.split("@");
			if(senderInfo.length !=2)
				throw new Exception("NOT EXIST USER");
			
			
			if(toAddr == null){
				throw new Exception("NO TO RCPT ADDR!!");
			}
			sendType = (sendType == null)?"normal":sendType;
			subject = (subject == null)?"":subject;
			content = (content == null)?"":content;
			encode = (encode == null)?"UTF-8":encode;
			editorMode = (editorMode == null)?"html":editorMode;
			
			try {
				builder = factory.newDocumentBuilder();
				doc = builder.newDocument();
			} catch (ParserConfigurationException e) {			
				throw e;
			}
			
			Element mailContent = doc.createElement("mail-send");
			doc.appendChild(mailContent);
			
			Element tempElement = null;						
			
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
			
			SendResultVO sendResultVO = service.sendMailMessage(sendVO,user);
			String[] sendAddrs = sendResultVO.getSendArrayAddrs();
			String[] invalidAddrs = sendResultVO.getInvalidArrayAddrs();
			
			tempElement = doc.createElement("actionResult");
			if(sendResultVO.isErrorOccur()){
				tempElement.setTextContent("fail");
				mailContent.appendChild(tempElement);				
			} else {
				tempElement.setTextContent("success");
				mailContent.appendChild(tempElement);
				
				tempElement = doc.createElement("message-id");
				if(sendResultVO.getMessageId() != null){
					tempElement.setTextContent(sendResultVO.getMessageId());
					mailContent.appendChild(tempElement);
				}
				
				tempElement = doc.createElement("send-emails");
				Element tempInnerElement = null;
				if(sendAddrs != null){
					for (int i = 0; i < sendAddrs.length; i++) {
						tempInnerElement = doc.createElement("email");
						tempInnerElement.setTextContent(sendAddrs[i]);
						tempElement.appendChild(tempInnerElement);
					}
					mailContent.appendChild(tempElement);
				}
				
				tempElement = doc.createElement("fail-emails");
				if(invalidAddrs != null){
					for (int i = 0; i < invalidAddrs.length; i++) {
						tempInnerElement = doc.createElement("email");
						tempInnerElement.setTextContent(invalidAddrs[i]);
						tempElement.appendChild(tempInnerElement);
					}
					mailContent.appendChild(tempElement);
				}				
			}
			
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			
			try {
				builder = factory.newDocumentBuilder();
				doc = builder.newDocument();
			} catch (ParserConfigurationException ignore) {			
				LogManager.writeErr(this, ignore.getMessage(), ignore);
			} catch (Exception ex){
				LogManager.writeErr(this, ex.getMessage(), ex);
			}
			if(doc != null){
				Element mailbox = doc.createElement("mail-send");
				doc.appendChild(mailbox);
				resultElement = doc.createElement("actionResult");
				resultElement.setTextContent("error");
				mailbox.appendChild(resultElement);
			}
		} finally {			
			toAddr = null;
			ccAddr = null;
			bccAddr = null;
			subject = null;
			content = null;
		}
		
		try {
			if(StringUtils.isNotEmpty(request.getParameter("xsl"))){
				XMLUtils.writeTo(doc, response, request.getParameter("xsl"));	
			}else{
				XMLUtils.writeTo(doc, response);
			}
			
		} catch (Exception e) {
			throw e;
		}
		
		return null;
	}

	public String doReturnError(HttpServletRequest request,HttpServletResponse response, String rootName) throws Exception {
		DocumentBuilderFactory factory  = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		Document doc = null;
		DocumentBuilder builder;
		Element resultElement = null;
		
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.newDocument();
		} catch (ParserConfigurationException ignore) {	
			LogManager.writeErr(this, ignore.getMessage(), ignore);
		}
		if(doc != null){
			Element mailbox = doc.createElement(rootName);
			doc.appendChild(mailbox);
			resultElement = doc.createElement("actionResult");
			resultElement.setTextContent("error");
			mailbox.appendChild(resultElement);
		}
		

		try {
			if(StringUtils.isNotEmpty(request.getParameter("xsl"))){
				XMLUtils.writeTo(doc, response, request.getParameter("xsl"));	
			}else{
				XMLUtils.writeTo(doc, response);
			}
			
		} catch (Exception e) {
			throw e;
		}
		
		return null;
	}
	
	
}
