/**
 * BbsManager.java 2008. 11. 26.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.bbs.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import jakarta.mail.Flags;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailPart;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.webmail.bbs.dao.BoardContentDao;
import com.terracetech.tims.webmail.bbs.dao.BoardDao;
import com.terracetech.tims.webmail.bbs.exception.BoardContentNotFoundException;
import com.terracetech.tims.webmail.bbs.vo.AttachFileVO;
import com.terracetech.tims.webmail.bbs.vo.BoardContentReplyVO;
import com.terracetech.tims.webmail.bbs.vo.BoardContentVO;
import com.terracetech.tims.webmail.bbs.vo.BoardQuotaVO;
import com.terracetech.tims.webmail.bbs.vo.BoardVO;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.dao.SystemConfigDao;
import com.terracetech.tims.webmail.exception.NotExistBoardException;
import com.terracetech.tims.webmail.mail.ibean.MailMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MailSortMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageParserInfoBean;
import com.terracetech.tims.webmail.mail.ibean.MessageSortInfoBean;
import com.terracetech.tims.webmail.mail.manager.Cid2Image;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;
import com.terracetech.tims.webmail.mail.manager.Image2Cid;
import com.terracetech.tims.webmail.mail.manager.MessageParser;
import com.terracetech.tims.webmail.mail.manager.XCommandHandler;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.UserInfo;
import com.terracetech.tims.webmail.mailuser.dao.MailUserDao;
import com.terracetech.tims.webmail.util.FileUtil;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.StringReplaceUtil;
import com.terracetech.tims.webmail.util.StringUtils;
import com.terracetech.tims.webmail.util.Validation;

/**
 * <p>
 * <strong>BbsManager.java</strong> Class Description
 * </p>
 * <p>
 * �ֿ伳��
 * </p>
 * <ul>
 * <li></li>
 * </ul>
 * 
 * @author jhlee
 * @since Tims7
 * @version 7.0
 */
@Service
@Transactional
public class BbsManager {

	private BoardDao boardDao;

	private BoardContentDao contentDao;
	
	private MailUserDao mailUserDao;
	
	private SystemConfigDao systemConfigDao;
	
	private XCommandHandler commandHandler = null;

	public void setBoardDao(BoardDao boardDao) {
		this.boardDao = boardDao;
	}

	public void setContentDao(BoardContentDao contentDao) {
		this.contentDao = contentDao;
	}
	
	public void setMailUserDao(MailUserDao mailUserDao) {
		this.mailUserDao = mailUserDao;
	}
	
	public void setSystemConfigDao(SystemConfigDao systemConfigDao) {
		this.systemConfigDao = systemConfigDao;
	}
	
	public List<BoardVO> readBoardList(int domainSeq) {
		return boardDao.readBoardList(domainSeq);
	}
	
	public void setCommandHandler(XCommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}

	/**
	 * 
	 * <p>DB���� �Խù��� �а�, ������ ÷�δ� IMap���� �����´�.</p>
	 *
	 * @param folder Imap���� ������ ����� ��
	 * @param userSeq ����� ����
	 * @param bbsId �Խ��� ������ȣ
	 * @param contentId �Խù� ������ȣ
	 * @param attachesdir ÷�ΰ� ����� ���
	 * @param attachesurl ÷�θ� ��ũ�� �ɶ� ���Ǵ� ���
	 * @return
	 * @throws IOException
	 * @throws MessagingException 
	 * @throws BoardContentNotFoundException Imap���� ������ ã�� ���� ��� �߻�
	 * @return BoardContentVO
	 */
	public BoardContentVO readBoardContent(TMailFolder folder, BoardContentVO contentVO, MessageParserInfoBean mPaserInfoBean)
			throws IOException, MessagingException, BoardContentNotFoundException, Exception {

		BbsMessageUtil bbsUtil = new BbsMessageUtil();
		folder.open(false);
		long bbsuid = bbsUtil.getBbsid2Uid(folder, contentVO.getMsgId());
		contentVO.setBbsUid(bbsuid);
		
		TMailPart[] files = new TMailPart[0];

		if (bbsuid != -1) {
			
			long[] uids ={bbsuid};
			TMailMessage[] messages = folder.getMessagesByUID(uids);

			StringBuffer newText = new StringBuffer();
			
			MessageParser mParser = new MessageParser();
			MailMessageBean messageBean = mParser.parseMessage(messages[0], mPaserInfoBean);
			
			TMailPart[] htmlContentPart = messageBean.getBodyContent();
			String htmlContent = (htmlContentPart != null && htmlContentPart.length > 0)?
					StringUtils.getCRLFEscape(htmlContentPart[0].getText()):"";;
			contentVO.setHtmlContent(htmlContent);
			
			files = BbsMessageUtil.getMessageAttachFiles(messageBean.getAttachContent());
			
			contentVO.setAttachFiles(files);
			folder.close(false);
		}

		return contentVO;
	}
	
	public BoardContentVO writeModifyContent(TMailFolder folder, BoardContentVO contentVO, MessageParserInfoBean mPaserInfoBean)
	throws IOException, MessagingException, BoardContentNotFoundException, Exception {

	BbsMessageUtil bbsUtil = new BbsMessageUtil();
	folder.open(false);
	long bbsuid = bbsUtil.getBbsid2Uid(folder, contentVO.getMsgId());
	contentVO.setBbsUid(bbsuid);
	
	TMailPart[] files = new TMailPart[0];
	
	if (bbsuid != -1) {
		
		long[] uids ={bbsuid};
		TMailMessage[] messages = folder.getMessagesByUID(uids);
	
		TMailPart[] textParts 	= messages[0].getTextContent(TMailMessage.TEXT_HTML);
		TMailPart[] imageParts = messages[0].getAttachImagesNCid();
		TMailPart[] texts = null;
		
		String filePath = mPaserInfoBean.getAttachesDir();
		String imgUrl = mPaserInfoBean.getAttachesUrl();
		Cid2Image c2i = new Cid2Image(imageParts,textParts,filePath,imgUrl,null);
		c2i.parseCidImage();
		
		texts = c2i.getTextParts();
		
		StringBuffer newHtml = new StringBuffer();
		StringBuffer newText = new StringBuffer();
		
		for(int i = 0; i < texts.length; i++) {
			newHtml.append(getContentString(texts[i], true));
		}
		
		for(int i = 0; i < texts.length; i++) {
			newText.append(getContentString(texts[i], false));
		}

		contentVO.setHtmlContent(newHtml.toString());
		
		contentVO.setTextContent(newText.toString());
	
		files = BbsMessageUtil.getMessageAttachFiles(messages[0].getAttachFiles());
		
		contentVO.setAttachFiles(files);
		folder.close(false);
	}
	
	return contentVO;
}
	
	public BoardContentVO readBoardContent(int bbsId, int contentId, int mailDomainSeq) {
		return contentDao.readBoardContent(bbsId, contentId, mailDomainSeq);
	}
	
	/**
	 * 
	 * <p>TEXT_PLAIN �������� �����´�.</p>
	 *
	 * @param TMailMessage
	 * @throws MessagingException
	 * @throws IOException 
	 * @return String
	 */
	public String getContentString(TMailPart part, boolean isHtmlMode) 
	throws MessagingException, IOException {

		StringBuffer newtext = new StringBuffer();
		String content = part.getText();
	
		if (isHtmlMode) {
			StringReplaceUtil sr = new StringReplaceUtil();

			if (content.length() > 0 && part.isMimeType("text/html")) {
				content = sr.replace(content, false, false);
			}
			else {
				content = TMailUtility.getEscHtmlTag(content);
			}
			newtext.append(content);

		} else {
			if (content.length() > 0 && part.isMimeType("text/html")) {
				newtext.append(TMailUtility.getHtmlToText(content));
			}
			else {
				newtext.append(content);
			}
		}

		return newtext.toString();
	}
	
	/**
	 * 
	 * <p>÷�����ϸ� �����Ѵ�.</p>
	 *
	 * @param TMailPart[] ÷�� ���� ��Ʈ
	 * @throws MessagingException 
	 * @return TMailPart[] ���е� ���� ��Ʈ
	 */
	public TMailPart[] getContentAttachFile(TMailPart[] attachContent) throws MessagingException {
		TMailPart[] part = new TMailPart[0];
		part = BbsMessageUtil.getMessageAttachFiles(attachContent);
		return part;
	}
	
	public void deleteUploadFile(String attFiles) {
		if (!Validation.isNull(attFiles)) {
			BbsMessageUtil.deleteUploadFile(attFiles);
		}
	}
	
	/**
	 * 
	 * <p>Ư�� �Խ����� ���� �Խù��� �����´�.</p>
	 *
	 * @param bbsId			�Խ��� ��ȣ
	 * @param mailDomainSeq		������ ������
	 * @return List
	 */
	public List<BoardContentVO> readBoardContentNoticeList(int bbsId, int mailDomainSeq, int pageBase) {
		return contentDao.readBoardContentNoticeList(bbsId, mailDomainSeq, pageBase);
	}
	
	public int readBoardContentNoticeListCount(int bbsId, int mailDomainSeq) {
		return contentDao.readBoardContentNoticeListCount(bbsId, mailDomainSeq);
	}
	
	/**
	 * 
	 * <p>Ư�� �Խ����� �Խù��� �����´�.</p>
	 *
	 * @param bbsId			�Խ��� ��ȣ
	 * @param mailDomainSeq		������ ������
	 * @return List
	 */
	public List<BoardContentVO> readBoardContentList(int bbsId, int mailDomainSeq, int creatorSeq, String searchType, String keyWord, int skipResult, int maxResult) {
		return contentDao.readBoardContentList(bbsId, mailDomainSeq, creatorSeq, searchType, keyWord, skipResult, maxResult);
	}
	
	/**
	 * 
	 * <p>Ư�� �Խ����� ������ �����´�.</p>
	 *
	 * @param bbsId			�Խ��� ��ȣ
	 * @param domainSeq		������ Seq
	 * @return BoardVO
	 */
	public BoardVO readBoard(int bbsId, int domainSeq) {
		BoardVO boardVo = boardDao.readBoard(bbsId, domainSeq); 
		if(boardVo ==null)
			throw new NotExistBoardException("bbsId=" +bbsId + ", domainSeq=" + domainSeq);
		
		return boardVo;
	}
	
	public BoardVO readNoticeBoard(int bbsId, int domainSeq) {
		BoardVO boardVo = boardDao.readNoticeBoard(bbsId, domainSeq); 
		if(boardVo ==null)
			throw new NotExistBoardException("bbsId=" +bbsId + ", domainSeq=" + domainSeq);
		
		return boardVo;
	}
	
	/**
	 * 
	 * <p>Ư�� �Խ����� �Խù� ������ �����´�.</p>
	 *
	 * @param bbsId			�Խ��� ��ȣ
	 * @param mailDomainSeq		������ ������
	 * @param searchType	�˻� ����
	 * @param keyWord		�˻���
	 * @return int
	 */
	public int readBoardContentListCount(int bbsId, int mailDomainSeq, int creatorSeq, String searchType, String keyWord) {
		return contentDao.readBoardContentListCount(bbsId, mailDomainSeq, creatorSeq, searchType, keyWord);
	}
	
	/**
	 * 
	 * <p>÷�� ������ �����Ѵ�.</p>
	 *
	 * @param bbsId			�Խ��� ��ȣ
	 * @param contentId		�Խù� ��ȣ
	 * @param mailDomainSeq		������ ������
	 */
	public void deleteAttachFile(int bbsId, int[] contentIds, int mailDomainSeq) {
		contentDao.deleteBoardContentAttachFile(bbsId, contentIds, mailDomainSeq);
	}
	
	/**
	 * 
	 * <p>��ȸ���� ������Ʈ �Ѵ�.</p>
	 *
	 * @param bbsId			�Խ��� ��ȣ
	 * @param contentId		�Խù� ��ȣ
	 * @param mailDomainSeq		������ ������
	 */
	public void modifyHitCount(int bbsId, int contentId, int mailDomainSeq) {
		contentDao.modifyHitCount(bbsId, contentId, mailDomainSeq);
	}
	
	/**
	 * 
	 * <p>�Խù��� �����Ѵ�.</p>
	 *
	 * @param boardContentVO	�Խ��� ��ȣ
	 * @param writeMode			HTML ��� / TEXT ���
	 * @param isReply			�������� �ƴ���
	 * @param attList			÷����������
	 */
	public void saveBoardContent(BoardContentVO boardContentVO, String writeMode, boolean isReply, String attList) throws Exception {
		String subject = boardContentVO.getSubject().replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		subject = StringUtils.DBEncode(subject);
		String creatorName = StringUtils.DBEncode(boardContentVO.getCreatorName());

		boardContentVO.setSubject(subject);
		boardContentVO.setCreatorName(creatorName);
		boardContentVO.setCreateTime(FormatUtil.getBasicDateStr());
		
		String content = StringUtils.DBEncode(boardContentVO.getHtmlContent());
		
		if (writeMode == "0") {
			content = StringReplaceUtil.convertHtmlBr(content);
		}
		
		boardContentVO.setContent(content.getBytes("UTF-8"));

		if (isReply) {
			int depth = boardContentVO.getDepth();
			int orderNo = boardContentVO.getOrderNo();
			contentDao.modifyOrderNo(boardContentVO.getBbsId(), boardContentVO.getParentId(), boardContentVO.getOrderNo(), boardContentVO.getMailDomainSeq());
			boardContentVO.setDepth(depth+1);
			boardContentVO.setOrderNo(orderNo+1);
		} else {
			boardContentVO.setParentId(0);
		}
		contentDao.saveBoardContent(boardContentVO);
		contentDao.modifyBoardContentAttCnt(boardContentVO.getBbsId(), boardContentVO.getContentId(), boardContentVO.getMailDomainSeq(), getAttachFileCount(attList));
	}
	
	private int getAttachFileCount(String attList) {
		int cnt = 0;
		if ((attList != null) && (attList.length()>0)) {
			StringTokenizer st = new StringTokenizer(attList, "\n");
			cnt = st.countTokens();
		}
		return cnt;
	}
	
	public void modifyBoardContent(BoardContentVO boardContentVO, String writeMode, String attList) throws Exception {
		String subject = boardContentVO.getSubject().replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		subject = StringUtils.DBEncode(subject);
		String creatorName = StringUtils.DBEncode(boardContentVO.getCreatorName());

		boardContentVO.setSubject(subject);
		boardContentVO.setCreatorName(creatorName);
		boardContentVO.setCreateTime(FormatUtil.getBasicDateStr());
		
		String content = StringUtils.DBEncode(boardContentVO.getHtmlContent());
		
		if (writeMode == "0") {
			content = StringReplaceUtil.convertHtmlBr(content);
		}
		
		boardContentVO.setContent(content.getBytes("UTF-8"));
		
		contentDao.modifyBoardContent(boardContentVO);
		contentDao.modifyBoardContentAttCnt(boardContentVO.getBbsId(), boardContentVO.getContentId(), boardContentVO.getMailDomainSeq(), getAttachFileCount(attList));
	}
	
	/**
	 * 
	 * <p>������ MimeMessage ���·� �����´�</p>
	 *
	 * @param session		��������
	 * @param content		�������� ���� �� ����
	 * @param attachList	÷����������
	 * @param charset		����
	 * @param tmpDir		÷������ �ӽ� ���� ���
	 * @return MimeMessage
	 */
	public MimeMessage getBbsMimeMessage(Session session, String content, String attachList, String charset, String tmpDir)
	throws MessagingException, IOException {
		BbsMessageUtil bbsUtil = new BbsMessageUtil();
		
		Image2Cid cid = new Image2Cid();
		String parseContent = cid.parseUploadImage(content);
		Hashtable<String, String> image2cid = cid.getImage2Cid();
		
		return bbsUtil.getBbsMimePart(session, parseContent, image2cid, attachList, charset, tmpDir);
	}
	
	/**
	 * 
	 * <p>���� ��Ʈ���� �޽����� ����� ����� �����Ѵ�.</p>
	 *
	 * @param mimeString		���ӽ�Ʈ��
	 * @param folder			���
	 * @param subject			����
	 * @param email			�̸���
	 * @param name			�̸�
	 * @param charset		charset
	 * @return bbsmsgid		�޽��� ID
	 * @throws UnsupportedEncodingException 
	 */
	public Map<String, String> setMimeToFolder(MimeMessage mimeMessage, TMailFolder folder, String subject, String email, String name, String charset) 
	throws MessagingException, IOException,UnsupportedEncodingException {
		Map<String, String> dataMap = new HashMap<String, String>();				
		MimeMessage msg = new MimeMessage(mimeMessage);
		msg.setSubject(subject, charset);
		msg.setFrom(new InternetAddress("", name, charset));
		msg.setSentDate(new Date());
		msg.saveChanges();
		// BBS �� ���� ID
		String bbsmsgid = msg.getMessageID();
		String contentType = msg.getContentType();
		
		TMailMessage[] messages = new TMailMessage[1];
		messages[0] = new TMailMessage(msg);
		folder.appendMessages(messages);
		
		String type = "att";
		if (contentType.indexOf("text/html") != -1) {
			type = "text";
		}
		dataMap.put("msgId", bbsmsgid);
		dataMap.put("contentType", type);
	
		
		return dataMap;
	}
	
	/**
	 * 
	 * <p>���� �ٷ� ������ �Խù� ������ �����´�.</p>
	 *
	 * @param bbsId				�Խ��� ��ȣ
	 * @param contentId			�Խù� ��ȣ
	 * @param parentId			�θ� �Խù� ��ȣ
	 * @param orderNo		
	 * @param mailDomainSeq		������ ������
	 * @param searchType		�˻� ����
	 * @param keyWord			�˻���
	 * @return List		
	 */
	public List<BoardContentVO> readPrevBoardContent(int bbsId, int contentId, int parentId, int orderNo, int mailDomainSeq, int creatorSeq, String searchType, String keyWord) {
		
		List<BoardContentVO> list = contentDao.readPrevBoardContent(bbsId, contentId, parentId, orderNo, mailDomainSeq, creatorSeq, searchType, keyWord);
		if ((contentId > parentId) && (list.size() == 0)) {
			list = contentDao.readPrevIfNullBoardContent(bbsId, contentId, parentId, orderNo, mailDomainSeq, creatorSeq, searchType, keyWord);
		}
		return list;
	}
	
	/**
	 * 
	 * <p>���� �ٷ� ������ �Խù� ������ �����´�.</p>
	 *
	 * @param bbsId				�Խ��� ��ȣ
	 * @param contentId			�Խù� ��ȣ
	 * @param parentId			�θ� �Խù� ��ȣ
	 * @param orderNo		
	 * @param mailDomainSeq		������ ������
	 * @param searchType		�˻� ����
	 * @param keyWord			�˻���
	 * @return List		
	 */
	public List<BoardContentVO> readNextBoardContent(int bbsId, int contentId, int parentId, int orderNo, int mailDomainSeq, int creatorSeq, String searchType, String keyWord) {
		
		List<BoardContentVO> list = contentDao.readNextBoardContent(bbsId, contentId, parentId, orderNo, mailDomainSeq, creatorSeq, searchType, keyWord);
		if ((contentId > parentId) && (list.size() == 0)) {
			list = contentDao.readNextIfNullBoardContent(bbsId, contentId, parentId, orderNo, mailDomainSeq, creatorSeq, searchType, keyWord);
		}
		return list;
	}
	
	public List<BoardContentVO> readPrevBoardContentByMessageId(int bbsId, int contentId, int parentId, int orderNo, int mailDomainSeq, int creatorSeq, String[] messageIds) {
		
		List<BoardContentVO> list = contentDao.readPrevBoardContentByMessageId(bbsId, contentId, parentId, orderNo, mailDomainSeq, creatorSeq, messageIds);
		if ((contentId > parentId) && (list.size() == 0)) {
			list = contentDao.readPrevIfNullBoardContentByMessageId(bbsId, contentId, parentId, orderNo, mailDomainSeq, creatorSeq, messageIds);
		}
		return list;
	}
	
	public List<BoardContentVO> readNextBoardContentByMessageId(int bbsId, int contentId, int parentId, int orderNo, int mailDomainSeq, int creatorSeq, String[] messageIds) {
		
		List<BoardContentVO> list = contentDao.readNextBoardContentByMessageId(bbsId, contentId, parentId, orderNo, mailDomainSeq, creatorSeq, messageIds);
		if ((contentId > parentId) && (list.size() == 0)) {
			list = contentDao.readNextIfNullBoardContentByMessageId(bbsId, contentId, parentId, orderNo, mailDomainSeq, creatorSeq, messageIds);
		}
		return list;
	}
	
	/**
	 * 
	 * <p>�޼��� ID�� �����´�.</p>
	 *
	 * @param bbsId				�Խ��� ��ȣ
	 * @param contentSel		�Խù� ��ȣ ����Ʈ (1,2,3,4)
	 * @param mailDomainSeq		������ ������
	 * @return String[]		
	 */
	public String[] readBoardContentMsgIds(int bbsId, int[] contentSel, int mailDomainSeq) {
		
		List<String> msgIdList = contentDao.readBoardContentMsgIds(bbsId, contentSel, mailDomainSeq);
		String[] msgIds = null;
		if (msgIdList.size()> 0) {
			msgIds = new String[msgIdList.size()];
			msgIdList.toArray(msgIds);
		}
		return msgIds;
	}
	
	/**
	 * 
	 * <p>�Խù��� �����Ѵ�.</p>
	 *
	 * @param folder		���
	 * @param bbsId			�Խ��� ��ȣ
	 * @param contentSel	�Խù� ��ȣ ����Ʈ (1,2,3,4)
	 * @param msgIds		�޽��� ���̵� ����Ʈ	
	 */
	public void deleteBoardMimeContent(TMailFolder folder, int bbsId, int[] contentSel, String[] msgIds) throws MessagingException {
		BbsMessageUtil bbsUtil = new BbsMessageUtil();
		folder.open(false);
		long[] deleteUids = new long[contentSel.length];
		for (int i=0; i<contentSel.length; i++) {
			deleteUids[i] = bbsUtil.getBbsid2Uid(folder, msgIds[i]);
		}
		folder.close(false);
		
		folder.open(true);
		for (int i = 0; i < deleteUids.length; i++) {
			if (deleteUids[i] != -1) {
				long[] delUids = new long[1];
				delUids[0] = deleteUids[i];
				TMailMessage[] messages = folder.getMessagesByUID(delUids);
				folder.setFlags(messages, new Flags(Flags.Flag.DELETED), true);
			}
		}
		folder.close(true);
	}
	
	public void deleteBoardDBContent(int bbsId, int[] contentSel, int mailDomainSeq) {
		contentDao.deleteBoardContent(bbsId, contentSel, mailDomainSeq);
	}
	
	public Map<String, String> getBoardAuth(int bbsId, int domainSeq, int userSeq) {
		String isCreator = "F";
		String isAdmin = "F";
		
		BoardVO boardVo = boardDao.readBoard(bbsId, domainSeq);
		if(boardVo ==null)
			throw new NotExistBoardException("bbsId=" +bbsId + ", domainSeq=" + domainSeq);
		
		int boardLevel = findBoardLevel(boardVo.getReadAuth(), boardVo.getWriteAuth());
		
		if (boardVo.getMailUserSeq() == userSeq) { 
			isCreator = "T";
		}
		
		List<Integer> adminSeqList = boardDao.readBoardAdiminList(bbsId, domainSeq);
		if ((adminSeqList != null) && (adminSeqList.size() > 0)) {
			for (int i=0; i<adminSeqList.size(); i++) {
				if (userSeq == adminSeqList.get(i)) {
					isAdmin = "T";
					break;
				}
			}
		}
		
		Map<String, String> boardAuthMap = new HashMap<String, String>();
		boardAuthMap.put("isCreator", isCreator);
		boardAuthMap.put("isAdmin", isAdmin);
		
		return boardAuthMap;
	}
	
	public int getBoadrdLevel(int bbsId, int domainSeq) {
	    BoardVO boardVo = boardDao.readBoard(bbsId, domainSeq);
            if(boardVo ==null)
                    throw new NotExistBoardException("bbsId=" +bbsId + ", domainSeq=" + domainSeq);
            
            int boardLevel = findBoardLevel(boardVo.getReadAuth(), boardVo.getWriteAuth());
            return boardLevel;
	}	
	
	private int findBoardLevel(String readAuth, String writeAuth) {
		int boardLevel = 5;

		if(readAuth.equals("ANY") && writeAuth.equals("ANY"))	{
			boardLevel = 1;
		}else if(readAuth.equals("ANY") && writeAuth.equals("LOGIN"))	{
			boardLevel = 2;
		}else if(readAuth.equals("ANY") && writeAuth.equals("ADMIN"))	{
			boardLevel = 3;
		}else if(readAuth.equals("LOGIN") && writeAuth.equals("LOGIN"))	{
			boardLevel = 4;
		}else if(readAuth.equals("LOGIN") && writeAuth.equals("ADMIN"))	{
			boardLevel = 5;
		}

		return boardLevel;
	}
	
	public void saveDefaultBoard(int mailDomainSeq, int mailUserSeq) {
		String[] notice = {Integer.toString(mailDomainSeq), Integer.toString(mailUserSeq), "PRIVATE", "NOTICE", "LOGIN", "ADMIN",
				"15", "0", "0", "0",Integer.toString(mailUserSeq),FormatUtil.getBasicDateStr(),""};
		String[] questionAnswer = {Integer.toString(mailDomainSeq), Integer.toString(mailUserSeq), "PRIVATE", "Q&A", "LOGIN", "LOGIN",
				"15", "1", "10", "0",Integer.toString(mailUserSeq),FormatUtil.getBasicDateStr(),""};
		String[] faq = {Integer.toString(mailDomainSeq), Integer.toString(mailUserSeq), "PRIVATE", "FAQ", "LOGIN", "ADMIN",
				"15", "0", "0", "0",Integer.toString(mailUserSeq),FormatUtil.getBasicDateStr(),""};
		
		List<BoardVO> defaultBoardList = new ArrayList<BoardVO>();
		defaultBoardList.add(makeDefaultBoardVO(notice));
		defaultBoardList.add(makeDefaultBoardVO(questionAnswer));
		defaultBoardList.add(makeDefaultBoardVO(faq));
		
		boardDao.saveDefaultBoard(defaultBoardList);
	}
	
	private BoardVO makeDefaultBoardVO (String [] boardItems) {
		
		BoardVO boardVo = new BoardVO();
		boardVo.setMailDomainSeq(Integer.parseInt(boardItems[0]));
		boardVo.setMailUserSeq(Integer.parseInt(boardItems[1]));
		boardVo.setBbsType(boardItems[2]);
		boardVo.setBbsName(boardItems[3]);
		boardVo.setReadAuth(boardItems[4]);
		boardVo.setWriteAuth(boardItems[5]);
		boardVo.setPagePerCnt(Integer.parseInt(boardItems[6]));
		boardVo.setAttMaxCnt(Integer.parseInt(boardItems[7]));
		boardVo.setAttMaxSize(Integer.parseInt(boardItems[8]));
		boardVo.setAgingDay(Integer.parseInt(boardItems[9]));
		boardVo.setCreatorSeq(Integer.parseInt(boardItems[10]));
		boardVo.setCreateTime(boardItems[11]);
		boardVo.setDescription(boardItems[12]);
		
		return boardVo;
	}
	
	public Map<String, List<AttachFileVO>> checkAttachFile(String oldFiles, String newFiles) {
		
		Map<String, List<AttachFileVO>> attMap = new HashMap<String, List<AttachFileVO>>();
		
		List<AttachFileVO> oldAttList = makeAttachList(oldFiles);
		List<AttachFileVO> newAttList = makeAttachList(newFiles);
		
		List<AttachFileVO> existAttList = new ArrayList<AttachFileVO>();
		List<AttachFileVO> notExistAttList = new ArrayList<AttachFileVO>();
		
		boolean flag = false;
		for (int i=0; i < newAttList.size(); i++) {
			AttachFileVO newAttach = newAttList.get(i);
			flag = false;
			for (int j=0; j < oldAttList.size(); j++) {
				AttachFileVO oldAttach = oldAttList.get(j);
				if (newAttach.getAttPath().equals(oldAttach.getAttPath())
				 && newAttach.getAttName().equals(oldAttach.getAttName())
				 && newAttach.getAttSize() == oldAttach.getAttSize()) {
					existAttList.add(newAttach);
					flag = true;
					break;
				}
			}
			if (!flag) {
				notExistAttList.add(newAttach);
			}
		}
		attMap.put("oldAttach", existAttList);
		attMap.put("newAttach", notExistAttList);
		
		return attMap;
	}
	
	private List<AttachFileVO> makeAttachList(String attString) {
		
		List<AttachFileVO> attList = null;
		
		if ((attString != null) && (attString.length()>0)) {
			attList = new ArrayList<AttachFileVO>();
			StringTokenizer st = new StringTokenizer(attString, "\n");

			while (st.hasMoreElements()) {
				String elem = (String) st.nextElement();
	
				StringTokenizer st2 = new StringTokenizer(elem, "\t");
				AttachFileVO attFileVo = new AttachFileVO();
				String path = (String) st2.nextElement();
				String filename = (String) st2.nextElement();
				String size = (String) st2.nextElement();
				attFileVo.setAttPath(path);
				attFileVo.setAttName(filename);
				attFileVo.setAttSize(Integer.parseInt(size));
				
				attList.add(attFileVo);
			}
		}
		return attList;
	}
	
	public int[] getOldPart(TMailPart[] parts, List<AttachFileVO> list) {
		int[] indexs = new int[list.size()];
		
		return indexs;
	}
	
	public List<AttachFileVO> makeAttachTmpFile(TMailPart[] parts, String tmpFolder, String email) throws IOException, MessagingException {
		List<AttachFileVO> list = new ArrayList<AttachFileVO>();
		for(int i=0; i< parts.length; i++) {
			TMailPart attPart = parts[i];
			SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMdd-HHmmssSSS");
			String fileName = sdf.format(new Date()) + "_" + email +".upld";
			String filePath = tmpFolder + "/" + fileName;
			File file = new File(filePath);
			
			InputStream in = attPart.getInputStream();
			FileOutputStream out = new FileOutputStream(file);
			int n = 0;
			byte[] buffer = new byte[1024 * 4];
			while ((n = in.read(buffer, 0, buffer.length)) != -1) {
				out.write(buffer, 0, n);
			}
			out.close();
			in.close();
			
			String path = file.getName().replaceAll("\\\\", "/");
			AttachFileVO fileVo = new AttachFileVO();
			fileVo.setAttPath(path);
			fileVo.setAttName(attPart.getFileName());
			fileVo.setAttSize(attPart.getSize75());
			list.add(fileVo);
		}
		return list;
	}
	
	public Map<String, String> getBbsConnectionInfo(String domain) {
		Map<String, String> confMap = new HashMap<String, String>();
		String adminId = EnvConstants.getUtilSetting("bbs.admin");

		Map<String, Object> adminMap = mailUserDao.readMailUserAuthInfo(adminId, domain);
		if (adminMap == null || adminMap.isEmpty()) {
			return null;
		}	
		UserInfo userInfo = new UserInfo();
		userInfo.setUserInfoMap(adminMap);
		int userSeq = Integer.parseInt(userInfo.get(User.MAIL_USER_SEQ));
		BoardQuotaVO boardQuotaVo = mailUserDao.readBbsInfo(userSeq);
		
		String args = BbsMessageUtil.getBbsQuotaInfo(boardQuotaVo);
		
		confMap.put(User.MAIL_UID, boardQuotaVo.getMailUid());
		confMap.put(User.MAIL_DOMAIN, boardQuotaVo.getMailDomain());
		confMap.put(User.EMAIL, boardQuotaVo.getMailUid()+"@"+boardQuotaVo.getMailDomain());
		confMap.put(User.MAIL_HOST, systemConfigDao.getHostName(boardQuotaVo.getMailHost()));
		confMap.put(User.IMAP_LOGIN_ARGS, args);
		
		return confMap;
	}
	
	public List<BoardVO> readNoticeBbsList(int domainSeq) {
		return boardDao.readNoticeBbsList(domainSeq);
	}
	
	public List<BoardContentReplyVO> getContentReplyList(int bbsId, int contentId, int isNotice, int mailDomainSeq, int skipResult, int pbase) {
		return contentDao.readContentReplyList(bbsId, contentId, isNotice, mailDomainSeq, skipResult, pbase);
	}
	
	public int getContentReplyListCount(int bbsId, int contentId, int isNotice, int mailDomainSeq) {
		return contentDao.readContentReplyListCount(bbsId, contentId, isNotice, mailDomainSeq);
	}
	
	public void saveContentReply(BoardContentReplyVO boardContentReplyVo) {
		contentDao.saveContentReply(boardContentReplyVo);
	}
	
	public void deleteContentReply(BoardContentReplyVO boardContentReplyVo) {
		contentDao.deleteContentReply(boardContentReplyVo);
	}
	
	public List<BoardContentVO> readBoardContentListByMessageIds(int bbsId, int mailDomainSeq, String[] messageIds) {
		return contentDao.readBoardContentListByMessageIds(bbsId, mailDomainSeq, messageIds);
	}
	
	public MailSortMessageBean[] getXSortMessageBeans(String folderName, MessageSortInfoBean sortInfo) throws MessagingException {
		return commandHandler.getXSortMessageBeans(sortInfo, null, folderName);
	}
	
	public int getXCommandTotal() throws Exception {		
		return commandHandler.getTotalCnt();		
	}
	
	public void setCommandResource(TMailStore store, String folderName, I18nResources msgResource) throws MessagingException {
		TMailFolder folder = store.getFolder(folderName);
		if (!folder.exists()) {
			folder.create();
		}
		commandHandler.setResource(store.getFolder(
				FolderHandler.INBOX).getCustomCommand(),msgResource.getLocale());
	}
	public String readContentMailUid(int mailUserseq){
		String mailUid = mailUserDao.readMailUserName(mailUserseq);
		return mailUid;
	}
	public boolean existNewContent(int bbsId, int domainSeq, String today){
		int newCnt = contentDao.existNewContent(bbsId,domainSeq,today);
		if(newCnt > 0){
			return true;
		}else{
			return false;
		}
	}
}
