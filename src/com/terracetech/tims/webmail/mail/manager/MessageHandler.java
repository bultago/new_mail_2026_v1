/**
 * MessageHandler.java 2008. 11. 27.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.manager;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;

import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.Flags;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMDNResponse;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailPart;
import com.terracetech.tims.webmail.mail.ibean.MDNResponsesBean;
import com.terracetech.tims.webmail.mail.ibean.MDNResponsesRcptBean;
import com.terracetech.tims.webmail.mail.ibean.MailMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageSortInfoBean;

/**
 * <p>
 * <strong>MessageHandler.java</strong> Class Description
 * </p>
 * <p>
 * �ֿ伳��
 * </p>
 * <ul>
 * <li>���� �޼��������� ���� ó���� ����ϴ� Ŭ����</li>
 * <li>����Ʈ ���� �� ���� �߰�. ÷�� ���� ���� ���� ����</li>
 * </ul>
 * 
 * @author sshyun
 * @since Tims7
 * @version 7.0
 */
public class MessageHandler {

	public static final char FLAGED_FLAG = 'F';
	public static final char SEEN_FLAG = 'S';
	public static final char UNSEEN_FLAG = 'U';
	public static final char ATTACH_FLAG = 'T';
	public static final char REPLY_FLAG = 'A';
	public static final char MYSELF_FLAG = 'L';
	

	private TMailFolder folder = null;
	private I18nResources msgResource = null;
	private int totalMessagesCount = 0;
	private String folderName = null;

	/**
	 * <p>
	 * Handler �� �ʿ��� Folder ������ �޼��� Resource���� ����.
	 * </p>
	 * 
	 * @param folder
	 * @param msgResource
	 * @return void
	 */	
	public void setResource(TMailFolder folder, I18nResources msgResource) {
		this.folder = folder;
		this.folderName = folder.getFullName();
		this.msgResource = msgResource;
	}

	/**
	 * @param folder �Ķ���͸� folder���� ����
	 */
	public void setFolder(TMailFolder folder) {
		this.folder = folder;
		this.folderName = folder.getFullName();
	}

	/**
	 * @param msgResource �Ķ���͸� msgResource���� ����
	 */
	public void setMsgResource(I18nResources msgResource) {
		this.msgResource = msgResource;
	}
	
	
	public int getSortTotal(MessageSortInfoBean infoBean)
	throws MessagingException {
		folder.open(false);		
		this.totalMessagesCount =  folder.getSortMessageTotal(infoBean.getSearchTerm());		
		folder.close(false);

		return totalMessagesCount;
	}

	/**
	 * <p>
	 * �޽��� ����Ʈ �� Sort ���ǿ���� ������.
	 * </p>
	 * 
	 * @param infoBean
	 *            Sort���� Bean
	 * @return
	 * @throws MessagingException
	 * @return TMailMessage[] ����Ʈ �迭.
	 */
	public TMailMessage[] getMessages(MessageSortInfoBean infoBean)
			throws MessagingException {

		folder.open(false);

		folder.setSortKey(infoBean.getMessageSortBy());
		folder.setSortDirection(infoBean.getMessageSortDir());
		folder.isMDN2 = true;
		TMailMessage[] messages = 
			folder.sort(infoBean.getStartPos(),infoBean.getPageBaseSize(), 
						infoBean.getSearchTerm());		

		folder.close(false);

		return messages;

	}

	/**
	 * <p>
	 * �޽��� ����Ʈ �� Sort ���ǿ���� ������ MailMessageBean ����Ʈ�� ���Ͽ� ��ȯ.
	 * </p>
	 * 
	 * 
	 * @param infoBean
	 *            Sort���� Bean
	 * @return
	 * @throws MessagingException
	 * @return MailMessageBean[]
	 */
	public MailMessageBean[] getMessageBeans(MessageSortInfoBean infoBean)
			throws MessagingException {

		MailMessageBean[] msgBeans = null;

		TMailMessage[] messages = getMessages(infoBean);

		int size = messages.length;

		if(size < 0){
			size = 0;
		}
		
		msgBeans = new MailMessageBean[size];

		for (int i = 0; i < size; i++) {
			msgBeans[i] = new MailMessageBean(messages[i]);
			msgBeans[i].setListParam();
		}

		return msgBeans;
	}

	/**
	 * <p>
	 * �޼����� �̿� ����/���� �޼����� UID ���� ��ȯ
	 * </p>
	 * 
	 * @param uid
	 *            ���� �޼��� UID
	 * @param infoBean
	 *            Sort ���� Bean
	 * @throws MessagingException
	 * @return long[] �̿� UID���� long[0] ����UID , long[1] ���� UID
	 */
	public long[] getNeighborUIDs(long uid, MessageSortInfoBean infoBean)
			throws MessagingException {
		folder.setSort(infoBean.getMessageSortBy(), infoBean.getMessageSortDir());
		return folder.getNeighborUIDs(uid, infoBean.getSearchTerm());
	}

	/**
	 * <p>
	 * ������ �޼��� ����Ʈ�� �� ����
	 * </p>
	 * 
	 * @return int
	 */
	public int getTotalMessagesCount() {
		return totalMessagesCount;
	}

	/**
	 * <p>
	 * �޼��� ����. �������� ��� ���� ����. �ƴѰ�� ������ �̵�
	 * </p>
	 * 
	 * @param uids
	 *            ������ �޼����� UID
	 * @throws MessagingException
	 * @return void
	 */
	public void deleteMessages(long[] uids) throws MessagingException {
		folder.open(true);
		TMailMessage[] messages = folder.getMessagesByUID(uids);

		if (folderName.equals(FolderHandler.TRASH)) {
			folder.setFlags(messages, new Flags(Flags.Flag.DELETED), true);
		} else {
			folder.xmove(uids, FolderHandler.TRASH);
		}
		folder.close(true);
	}

	/**
	 * <p>
	 * �޼����� ���� ����.
	 * </p>
	 * 
	 * @param uids
	 *            ������ �޼����� UID
	 * @throws MessagingException
	 * @return void
	 */
	public void cleanMessages(long[] uids) throws MessagingException {
		folder.open(true);
		TMailMessage[] messages = folder.getMessagesByUID(uids);
		folder.setFlags(messages, new Flags(Flags.Flag.DELETED), true);
		folder.close(true);
	}

	/**
	 * <p>
	 * �޼����� FLag ����. ��߸��ϰ� ���� ������ ���� ����
	 * </p>
	 * 
	 * @param uids
	 *            ������ �޼��� UID
	 * @param flagType
	 *            Flag Type 'f' FLAGED_FLAG, 's' SEEN_FLAG
	 * @param used
	 *            ���� ���� true/false
	 * @throws MessagingException
	 * @return void
	 */
	public void switchFlags(long[] uids, char flagType, boolean used)
			throws MessagingException {
		Flags flag = null;

		if (flagType == FLAGED_FLAG) {
			flag = new Flags(javax.mail.Flags.Flag.FLAGGED);
		} else if (flagType == SEEN_FLAG) {
			flag = new Flags(javax.mail.Flags.Flag.SEEN);
		}
		
		if(flag == null){
			throw new MessagingException("FLAG IS NULL!!");
		}

		folder.open(true);
		TMailMessage[] messages = folder.getMessagesByUID(uids);

		int size = uids.length;
		for (int i = 0; i < size; i++) {
			messages[i].setFlags(flag, used);
		}

		folder.close(true);
	}

	/**
	 * <p>
	 * �����Կ� �޼����� �߰�.
	 * </p>
	 * 
	 * @param messages
	 * @throws MessagingException
	 * @return void
	 */
	public void addMessages(TMailMessage[] messages) throws MessagingException {
		folder.open(true);
		folder.appendMessages(messages);
		folder.close(true);
	}

	/**
	 * <p>
	 * Ư�� UID �޼����� ������
	 * </p>
	 * 
	 * @param uid
	 * @return
	 * @throws MessagingException
	 * @return TMailMessage
	 */
	public TMailMessage getMessge(long uid) throws MessagingException {
		TMailMessage message = folder.getMessageByUID(uid, true);
		return message;
	}
	
	/**
	 * <p>
	 * Ư�� UID �迭�� �޼����� ������
	 * </p>
	 * 
	 * @param uids
	 * @return
	 * @throws MessagingException
	 * @return TMailMessage[]
	 */
	public TMailMessage[] getMessges(long[] uids) throws MessagingException {
		return folder.getMessagesByUID(uids);		
	}

	/**
	 * <p>
	 * �޼����� ÷�ε� ó�������� ����. ÷�� ������ 0kb �� ����.
	 * </p>
	 * 
	 * @param uid
	 *            �޼����� UID
	 * @param part
	 *            ������ ÷�� ��Ʈ
	 * @param userId
	 *            ����� ID
	 * @param tmpPath
	 *            �ӽ� �������
	 * @throws Exception
	 * @return long ������ ���� ��� �޼����� UID����
	 */
	public long removeAttachFile(long uid, String part, String userId,
			String tmpPath) throws Exception {

		folder.open(true);

		long newMsgUid = -1;

		TMailMessage message = folder.getMessageByUID(uid, false);
		MimeMessage currentMsg = (MimeMessage) message.getMessage();
		MimeMessage newMsg = new MimeMessage(currentMsg);

		String searchField = "Message-ID";
		String searchHeader = currentMsg.getHeader("Message-ID", null);
		File tmpFile = null;
		removedAttachMime(newMsg, part, userId, tmpPath, tmpFile);

		if (searchHeader != null) {
			newMsg.setHeader("Message-ID", searchHeader);			
		} else {
			newMsg.saveChanges();
			searchHeader = newMsg.getHeader("Message-ID", null);			
		}
		
		searchHeader = searchHeader.replaceAll("<", "");
		searchHeader = searchHeader.replaceAll(">", "");

		// Append new Message
		TMailMessage newAttachMessage = new TMailMessage(newMsg);
		String command = "UID STORE " + uid
				+ " +FLAGS.SILENT (\\Deleted $MDNSent)";
		folder.xcommand(command, null);
		folder.appendMessages(new TMailMessage[] { newAttachMessage });
		folder.close(true);
		
		try {
			if (tmpFile != null) {
				tmpFile.delete();
			}
		} catch (Exception e) {
		}
		tmpFile = null;

		// Search Append new Message Uid
		folder.open(true);
		newMsgUid = getSearchHeaderUid(searchField, searchHeader);
		command = "UID STORE " + newMsgUid + " -FLAGS.SILENT ($MDNSent)";
		folder.xcommand(command, null);

		folder.close(true);
		
		

		return newMsgUid;
	}

	/**
	 * <p>
	 * �޼������� ÷�� Part�� ã�� �����ϰ� ���ο� MIME Part �� ����� ��ȯ.
	 * </p>
	 * 
	 * @param newMsg
	 *            ���ο� Mime Message
	 * @param part
	 *            Part�������
	 * @param userId
	 *            ����� ID
	 * @param tmpPath
	 *            �ӽð��
	 * @throws Exception
	 * @return MimeMessage ���ο� �޼���
	 */
	@SuppressWarnings("unchecked")
	public MimeMessage removedAttachMime(MimeMessage newMsg, String part,
			String userId, String tmpPath, File tmpFile) throws Exception {

		Stack messageStack = null;
		Stack partStack = null;
		Stack contentIdStack = null;		
		try {

			int[] partPos = getPartPosition(part);

			// Get Attach File Part
			messageStack = new Stack();
			partStack = new Stack();
			contentIdStack = new Stack();
			String charset = getCharset(newMsg);

			Part attachMimePart = getAttachPart(partPos, newMsg, messageStack,
					partStack, contentIdStack);

			// Remove Attach File And New Message Save
			String attachfileName = new TMailPart(attachMimePart).getFileName();

			tmpFile = createTempFile(tmpPath, attachfileName);

			MimeBodyPart newAttachPart = getRemovedAttachFilePart(
					attachMimePart, charset, tmpFile);
			newMsg.setContent(makePartFromStack(newAttachPart, messageStack,
					partStack, contentIdStack));
			newMsg.saveChanges();

			return newMsg;
		} catch (Exception e) {
		} finally {
			messageStack = null;
			partStack = null;
			contentIdStack = null;
			
		}

		return null;
	}

	/**
	 * <p>
	 * �����Կ��� header �� ������ �޼����� �˻�.
	 * </p>
	 * 
	 * @param searchField
	 *            �˻� Header �ʵ�
	 * @param searchHeader
	 *            �˻� Header ��
	 * @throws MessagingException
	 * @return long �˻��Ǿ� ã���� UID
	 */
	@SuppressWarnings("all")
	public long getSearchHeaderUid(String searchField, String searchHeader)
			throws MessagingException {
		
		long uid = -1;
		
		try {
			uid = folder.xsearchMID(searchHeader);		
		} catch (Exception e) {
			throw new MessagingException(e.getMessage());
		}
		
		return uid;
	}

	/**
	 * <p>
	 * ���ÿ� ����� ������ �ҷ����� ���ο� �޼��� MIME �� ����� �޼ҵ�.
	 * </p>
	 * 
	 * @param newPart
	 *            ���ο� ÷�� ��Ʈ
	 * @param messageStack
	 *            �ش� �޼����� RFC822 ��Ʈ�� ���� �ϱ����� ����
	 * @param partStack
	 *            �ش� �޼��� ��Ÿ ��Ʈ�� ���� �ϱ����� ����
	 * @param contentIdStack
	 *            Part�� Content-ID�� ���� �ϱ����� ����
	 * @throws MessagingException
	 * @return Multipart ���ο� �޼��� Multipart
	 */
	@SuppressWarnings("all")
	public Multipart makePartFromStack(MimeBodyPart newPart,
			Stack messageStack, Stack partStack, Stack contentIdStack)
			throws MessagingException {

		Multipart mp = null;
		MimeMessage m = null;

		while (!partStack.empty() && !contentIdStack.empty()) {
			int j = ((Integer) contentIdStack.pop()).intValue();

			mp = (Multipart) partStack.pop();

			mp.removeBodyPart(j);
			mp.addBodyPart(newPart, j);

			if (!messageStack.empty()) {
				m = (MimeMessage) messageStack.pop();
				m.setContent(mp);
				m.saveChanges();

				newPart = new MimeBodyPart();
				newPart.setContent(m, "message/rfc822");
				newPart.setHeader("Content-Disposition", "inline");
			}

		}

		m = null;
		return mp;
	}

	/**
	 * <p>
	 * �ӽ����Ͽ� ���ϻ�.
	 * </p>
	 * 
	 * @param tmpPath
	 *            �ӽ� ���� ���
	 * @param attachfileName
	 *            ÷�� ���ϸ�
	 * @return
	 * @throws Exception
	 * @return File File ��ü
	 */
	public File createTempFile(String tmpPath, String attachfileName)
			throws Exception {
		File file = new File(tmpPath + "/" + attachfileName);
		file.createNewFile();
		return file;
	}

	/**
	 * <p>
	 * ÷�� ��ġ ������ ������ ÷�� ��Ʈ�� ã��.
	 * </p>
	 * 
	 * @param partPos
	 *            ÷�� ��ġ
	 * @param messagePart
	 *            �޼��� Part
	 * @param messageStack
	 *            �ش� �޼����� RFC822 ��Ʈ�� ���� �ϱ����� ����
	 * @param partStack
	 *            �ش� �޼��� ��Ÿ ��Ʈ�� ���� �ϱ����� ����
	 * @param contentIdStack
	 *            Part�� Content-ID�� ���� �ϱ����� ����
	 * @throws MessagingException
	 * @throws IOException
	 * @return Part ÷�� ��Ʈ.
	 */
	@SuppressWarnings("all")
	public Part getAttachPart(int[] partPos, Part messagePart,
			Stack messageStack, Stack partStack, Stack contentIdStack)
			throws MessagingException, IOException {

		for (int i = 0; i < partPos.length;) {
			if (messagePart.isMimeType("multipart/*")) {
				Multipart tmpPart = (Multipart) messagePart.getContent();
				partStack.push(tmpPart);
				contentIdStack.push(new Integer(partPos[i]));
				messagePart = tmpPart.getBodyPart(partPos[i++]);

			} else if (messagePart.isMimeType("message/rfc822")) {
				messagePart = (MimeMessage) messagePart.getContent();
				messageStack.push(messagePart);
			}
		}

		return messagePart;
	}

	/**
	 * <p>
	 * ÷����Ʈ���� �ش� ÷�� ������ �̾Ƴ��� ����ִ� �ӽ� ������ ���ϰ� ���ο� ÷�� ��Ʈ�� ����� ��ȯ
	 * </p>
	 * 
	 * @param attachMimePart
	 *            ÷�� ���� Part
	 * @param charset
	 *            ÷�� ���� Charset
	 * @param file
	 *            �ӽ� ����
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 * @return MimeBodyPart ÷�ΰ� ����� ���ο� MimePart
	 */
	public MimeBodyPart getRemovedAttachFilePart(Part attachMimePart,
			String charset, File file) throws MessagingException, IOException {

		MimeBodyPart newAttachPart = new MimeBodyPart();

		String attachfileName = new TMailPart(attachMimePart).getFileName();

		String esc_name = MimeUtility.encodeText(attachfileName, charset, "B");

		FileDataSource fds = new FileDataSource(file);
		newAttachPart.setDataHandler(new DataHandler(fds));

		String[] cid = attachMimePart.getHeader("Content-ID");

		newAttachPart
				.setHeader("Content-Type", attachMimePart.getContentType());		

		if (cid != null) {
			newAttachPart.setHeader("Content-Disposition",
					"inline; filename=\"" + esc_name + "\"");
			newAttachPart.setHeader("Content-ID", cid[0]);
		} else {
			newAttachPart.setFileName(esc_name);
		}
		newAttachPart.addHeader("Content-Transfer-Encoding", "base64");
		newAttachPart.setDescription("DELETE-ATTACH");
		return newAttachPart;
	}

	/**
	 * <p>
	 * ÷�� ��Ʈ�� ��ġ ���� ��ȯ
	 * </p>
	 * 
	 * @param part
	 *            ÷�� ��Ʈ ���� (1:1:2 ���)
	 * @return
	 * @return int[] ��Ʈ ��ġ����
	 */
	public int[] getPartPosition(String part) {

		StringTokenizer st = new StringTokenizer(part, ":");
		int[] partPos = new int[st.countTokens()];
		int size = partPos.length;
		for (int i = 0; i < size; i++) {
			partPos[i] = Integer.parseInt(st.nextToken());
		}
		st = null;
		return partPos;
	}

	/**
	 * <p>
	 * Part �� ������ Charset �κ��� ã�� ��ȯ.
	 * </p>
	 * 
	 * @param part
	 * @throws MessagingException
	 * @return String Charset ��
	 */
	public String getCharset(Part part) throws MessagingException {
		String contentType = part.getContentType();
		String charset = null;

		int s = contentType.indexOf("charset=");

		if (s != -1) {
			int e = contentType.indexOf(";", s);

			charset = (e != -1) ? contentType.substring(s + 8, e) : contentType
					.substring(s + 8);
		}
		contentType = null;
		return charset;
	}

	/**
	 * <p>
	 * </p>
	 * 
	 * @param folder2
	 * @param uid
	 * @return void
	 * @throws MessagingException
	 */
	public MDNResponsesBean getMDNResponses(String uid, 
			Map<String, Long> localDomainMap,
			String page, String pageBase, String pattern)
			throws MessagingException {	
		folder.open(false);
		TMailMessage message = folder.getMessageByUID(Long.parseLong(uid),
				true);

		MDNResponsesBean rBean = new MDNResponsesBean();
		message.setDirectRead(true);	
		message.setLocale(msgResource.getLocale());
		String mid = message.getMessageID();
		mid = mid.replaceAll("<", "");
		mid = mid.replaceAll(">", "");
		
		rBean.setMessageID(mid);
		rBean.setMessageTitle(message.getSubject());
		rBean.setSendDate(message.getSentDateForRead());
		rBean.setCountTotal(message.getMDNResponseCount());		
		
		TMailMDNResponse[] allResponse = message.getMDNResponses();		
		TMailMDNResponse[] res = 
			folder.getMDNResponseInfo(mid, page, pageBase, pattern);
		
		int total = folder.getMdnTotalCnt();
		
		if(res == null && total > 0){
			int npages = (int) Math.ceil((double) total / Integer.parseInt(pageBase));
			if (0 < npages && npages < Integer.parseInt(page)) {
				page = Integer.toString(npages);				
			}
			res = folder.getMDNResponseInfo(message.getMessageID(), 
					page, pageBase, pattern);
		}		
		rBean.setMdnResponseTotal(total);
		rBean.setMdnResponsePage(Integer.parseInt(page));
		
		int ssize = res.length;
		int rsize = allResponse.length;
		
		MDNResponsesRcptBean[] rcptBeans = new MDNResponsesRcptBean[res.length];
		
		String address = null;
		String code = null;
		String[] addrVals = null;		
		int readCnt = 0;
		int unseenCnt = 0;
		int recallCnt = 0;
		int failCnt = 0;
		int etcCnt = 0;	
		
		for (int i = 0 ; i < rsize ; i++) {
			code = allResponse[i].getCode();			
			if(code.matches("200|201|300")){				
				unseenCnt++;
			}else if(code.matches("110|111|112|120|121|122|129|130|131|132|133")){				
				failCnt++;
			}else if(code.equals("1")){				
				recallCnt++;
			}else if(code.equals("1000")){				
				readCnt++;
			}else if(code.matches("100|102")){
				etcCnt++;
			}	
		}
		
		for (int i = 0 ; i < ssize ; i++) {			
			rcptBeans[i] = new MDNResponsesRcptBean();		
			address = res[i].getAddress();
			code = res[i].getCode();
			addrVals = address.split("@");
			if(localDomainMap.containsKey(addrVals[1])){
				rcptBeans[i].setLocalDomain(true);
			} else {
				rcptBeans[i].setLocalDomain(false);
			}
			rcptBeans[i].setAddress(address);
			rcptBeans[i].setPersonal(res[i].getPersonal());
			rcptBeans[i].setCode(code);
					
			if(code.matches("200|201|300")){
				rcptBeans[i].setStatus("mail.mdn.send");
				rcptBeans[i].setMessage("mdn.msg.300");				
			}			
			if(code.matches("110|111|112|120|121|122|129|130|131|132|133")){
				rcptBeans[i].setStatus("mail.mdn.fail");
				rcptBeans[i].setMessage("mdn.msg."+code);				
			}
			if(code.equals("1")){
				rcptBeans[i].setStatus("mail.mdn.recall");
				rcptBeans[i].setMessage(res[i].getSentDate4());				
			}			
			if(code.equals("1000")){
				rcptBeans[i].setStatus("mail.mdn.success");
				rcptBeans[i].setMessage(res[i].getSentDate4());				
			}			
			if(code.matches("100|102")){
				if(code.equals("100")){
					rcptBeans[i].setStatus("mail.mdn.wait");
				}else if(code.equals("102")){
					rcptBeans[i].setStatus("mail.mdn.retry");
					rcptBeans[i].setMessage("mdn.msg.102");
				}				
			}			
		}

		rBean.setRcptVos(rcptBeans);
		rBean.setCountRead(readCnt);
		rBean.setCountUnseen(unseenCnt);
		rBean.setCountRecall(recallCnt);
		rBean.setCountFail(failCnt);
		rBean.setCountEtc(etcCnt);
		
		folder.close(false);

		return rBean;
	}
	
	public String getMessageIntegrity(String uid) throws MessagingException {
		folder.open(false);
		String integrity = folder.xintegrityMID(uid);
		folder.close(false);
		return integrity;
	}

}
