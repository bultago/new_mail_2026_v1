/**
 * MessageParser.java 2008. 12. 4.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.manager;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.internet.ContentType;
import javax.mail.internet.ParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailPart;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.mail.ibean.ImageInfoBean;
import com.terracetech.tims.webmail.mail.ibean.MailMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageParserInfoBean;
import com.terracetech.tims.webmail.util.StringReplaceUtil;

/**
 * <p>
 * <strong>MessageParser.java</strong> Class Description
 * </p>
 * <p>
 * �ֿ伳��
 * </p>
 * <ul>
 * <li>���� �޼����� ������ �ش� MIME�� �����Ͽ� ÷�� �������� ���� �۾��� ó���ϴ� Ŭ����</li>
 * </ul>
 * 
 * @author sshyun
 * @since Tims7
 * @version 7.0
 */
public class MessageParser {

	public Log log = LogFactory.getLog(this.getClass());

	/**
	 * <p>���ϸ޼����� �޾� �� ��Ʈ���� �и��ѵ� ÷�� ó���� ����ó���� �Ͽ� MailMessaageBean Ŭ������ ������ ���� �� ��ȯ.</p>
	 *
	 * @param message			������ �޼��� Ŭ����
	 * @param infoBean			���� �޼����� ����� �ʿ��� ���� ���� Ŭ����
	 * @return
	 * @throws Exception
	 * @return MailMessageBean		�и��Ǿ� ������ ������ ��� ��ȯ.
	 */
	@SuppressWarnings("unchecked")
	public MailMessageBean parseMessage(TMailMessage message,
			MessageParserInfoBean infoBean) throws Exception {
		
		Folder folder = message.getFolder();
		String folderName = null;
		if(folder != null){
			folderName = message.getFolderFullName();
		}
				
		String defaultCharset = infoBean.getDefaultCharset();
		boolean isHiddenImg = infoBean.isHiddenImg();
		boolean isHiddenTag = infoBean.isHiddenTag();
		String userId = infoBean.getUserId();
		String strLocalhost = infoBean.getStrLocalhost();
		String attachesDir = infoBean.getAttachesDir();
		String attachesUrl = infoBean.getAttachesUrl();
		String defaultImg = infoBean.getDefaultImg();
		boolean isImgResize = infoBean.isImgResize();
		int imgResizeWidth = infoBean.getImgResizeWidth();

		message.setLocale(infoBean.getLocale());
		message.setDefaultCharset((defaultCharset == null) ? "euc-kr"
				: defaultCharset);
		boolean isSeen = message.isSeen(); 
		if(!isSeen){
			parseMDNAddr(message);
		}
		boolean isMDNCheck = message.isMDNMessage() && !isSeen
				&& !FolderHandler.SENT.equals(folderName);

		TMailPart[] bodyContents = message.getTextContent(TMailMessage.TEXT_HTML);
		StringReplaceUtil sr = new StringReplaceUtil();
		if(!isSeen && bodyContents.length == 0){
			message.setFlags(new Flags(javax.mail.Flags.Flag.SEEN), true);
		}
		boolean isInImage = parseBody(bodyContents, sr, isHiddenImg,
				isHiddenTag);

		TMailPart[] files = message.getAttachFiles();
		TMailPart[] vcards = message.getAttachVcards();
		TMailPart[] images = message.getAttachImagesNCid();
		TMailPart[] imageAttach = message.getAttachImages();
		// ���� ���� �̹��� �����.
		// �̹��� ���� ��.
		Map<String, ImageInfoBean> resizeImgMap = new HashMap<String, ImageInfoBean>();
		
		createCIDImages(images, isImgResize, imgResizeWidth, userId, message.getUid(), attachesDir, resizeImgMap);		
		String[] imageContents = createAttachImages(imageAttach, 
				userId, message.getUid(), attachesDir, attachesUrl);		
		Hashtable cidTable = getCIDImagesMap(images, strLocalhost, attachesUrl,
				defaultImg, resizeImgMap);
		replaceContentCIDImagesUrl(sr, bodyContents, cidTable);
		
		if(resizeImgMap.size() > 0){
			replaceContentCIDResizeImagesUrl(sr, bodyContents, resizeImgMap);
		}

		parseMessageCharset(message, bodyContents);

		MailMessageBean messageBean = new MailMessageBean(message);
        messageBean.setAddrTo(message.getTo());
        messageBean.setAddrCc(message.getCc());
        if(FolderHandler.SENT.equals(folderName)){
        	messageBean.setAddrBcc(message.getBcc());
        }
		messageBean.setHiddenImg(isInImage && isHiddenImg);
		messageBean.setBodyContents(bodyContents);
		messageBean.setAttachContent(files);
		messageBean.setVcardContent(vcards);
		messageBean.setImageContent(imageContents);
		messageBean.setMdnCheck(isMDNCheck);

		folderName = null;
		defaultCharset = null;
		userId = null;
		strLocalhost = null;
		attachesDir = null;
		attachesUrl = null;
		defaultImg = null;
		cidTable = null;
		resizeImgMap = null;

		return messageBean;
	}

	/**
	 * <p>
	 * MDN Address �ּ� ����. ����
	 * </p>
	 * 
	 * @param message
	 * @throws Exception
	 * @return void
	 */
	protected void parseMDNAddr(TMailMessage message) {
		try {
			String[] mdnAddress = message
					.getHeader("Disposition-Notification-To");

			if (mdnAddress != null) {
				mdnAddress[0] = mdnAddress[0].trim();
				mdnAddress[0] = mdnAddress[0].replaceAll("[\t\n\r]", "");

				message.setMDNAddresses(mdnAddress);
			}
		} catch (Exception e) {
			log.error("=== READ Message MDN ADDR Exception : [" + e.getMessage() + "]");
		}

	}

	/**
	 * <p>
	 * ���� �Ľ�. �̹��� �� �±� �κ� ġȯ �ϰ� ������ �̹��� ���� ���θ� ã�� ��ȯ
	 * </p>
	 * 
	 * @param contents				���� ��Ʈ�κ�.
	 * @param sr						���ڿ� ġȯ Ŭ����
	 * @param isHiddenImg			�̹��� �����ֱ� ����
	 * @param isHiddenTag		�±� �����ֱ⿩��
	 * @throws Exception
	 * @return boolean				������ �̹��� ���Կ���.
	 */
	protected boolean parseBody(TMailPart[] contents, StringReplaceUtil sr,
			boolean isHiddenImg, boolean isHiddenTag) throws Exception {

		int size = contents.length;
		boolean isInImage = false;

		try {
			for (int i = 0; i < size; i++) {
				if (contents[i].isMessageRFC822()) {
					continue;
				}
				//contents[i].setDirectRead(true);// ���� �б� �ɼ�
				String txt = contents[i].getText();
				boolean isHtml = contents[i].isMimeTypeNew("text/html");

				if (isHiddenImg && !isInImage) {
					isInImage = sr.isInImage(txt);
				}

				txt = sr.replace(txt, isHiddenImg, isHiddenTag, isHtml);
				contents[i].setText(txt);
			}
		} catch (Exception e) {
			log.error("=== Parse Body Exception : [" + e.getMessage() + "]");
			throw e; 
		}

		return isInImage;
	}

	/**
	 * <p>�޼��� Charset ���� ����</p>
	 *
	 * @param message				�޼��� 
	 * @param bodyContents		�޼����� ���� ��Ʈ
	 * @throws MessagingException
	 * @return void
	 */
	protected void parseMessageCharset(TMailMessage message,
			TMailPart[] bodyContents) throws MessagingException {
		for (int i = 0; i < bodyContents.length; i++) {
			try{
				ContentType ct = new ContentType(bodyContents[i].getContentType());
				String cs = ct.getParameter("charset");
	
				if (cs == null) {
					continue;
				}
	
				message.setCharset(cs);
			} catch (ParseException e) {
				String contTypeStr = bodyContents[i].getContentType();
				if(contTypeStr != null){
					contTypeStr = contTypeStr.toLowerCase();
					int pos = contTypeStr.indexOf("charset");
					if(pos > -1){
						String cs = contTypeStr.substring(pos+8, contTypeStr.length());
						message.setCharset(cs);
					} else {
						continue;
					}
				} else {
					continue;
				}
				
			}
		}
	}

	/**
	 * <p>
	 * ���� ÷�� �̹��� ��.
	 * </p>
	 * 
	 * @param images			�̹��� ��Ʈ
	 * @param id					����� ID
	 * @param attachesDir		÷�� ���
	 * @param uid					�޼��� uid
	 * @throws Exception
	 * @return void
	 */
	protected void createCIDImages(TMailPart[] images, 
			boolean isImgResize, int imgResizeWidth, String id, long uid,
			String attachesDir, Map<String, ImageInfoBean> resizeImgMap) throws Exception {

		int size = images.length;		
		if (size > 0) {			
			File imagesDir = new File(attachesDir);
			imagesDir.mkdir();
			uid = (uid < 0)?0:uid;
			String newFileName = null;
			ImageInfoBean imgBean = null;			
			for (int i = 0; i < size; i++) {
				newFileName = id + "_" + uid + "_inline" + i + "_" +Long.toString(System.nanoTime()); 
				String imageName = getImageFileName(images[i].getFileName(),newFileName);

				try {
					images[i].createImageFile(attachesDir, imageName);
				} catch (IOException e) {
					System.out.println("-- IOException : [" + imageName + "] ["
							+ e.getMessage() + "]");
					continue;
				}				
				
				if(isImgResize){
					BufferedImage imageFile = ImageIO.read(new File(attachesDir + EnvConstants.DIR_SEPARATOR + imageName));
					int width = imageFile.getWidth();
					if(width > imgResizeWidth){
						imgBean = new ImageInfoBean();
						imgBean.setOrgImgName(imageName);						
						imgBean.setResize(true);
						width = imgResizeWidth;
						int pos = imageName.lastIndexOf(".");
						String ext = null; 
						if (pos > 0) {
							ext = imageName.substring(pos+1);
							imageName = newFileName+"_th."+ext;
						}
						ext = (ext != null)?ext:"jpg";					
						FileOutputStream out = new FileOutputStream(new File(attachesDir + EnvConstants.DIR_SEPARATOR +imageName));						
						int calcHeight = (width * imageFile.getHeight() / imageFile.getWidth());  
						ImageIO.write(createResizedCopy(imageFile, width, calcHeight), ext, out);
						
						imgBean.setResizeImgName(imageName);
						imgBean.setWidth(width);
						imgBean.setHeight(calcHeight);
						
						resizeImgMap.put(images[i].getContentID(), imgBean);
					} else {
						images[i].setImageName(imageName);
					}
				} else {
					images[i].setImageName(imageName);			
				}
			}
		}
		
	}
	
	private BufferedImage createResizedCopy(Image originalImage, int scaledWidth,
			int scaledHeight) {
		BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = scaledBI.createGraphics();
		g.setComposite(AlphaComposite.Src);
		g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
		g.dispose();
		return scaledBI;
	}
	
	protected String[] createAttachImages(TMailPart[] images, String id, long uid,
			String attachesDir, String attachesUrl) throws Exception {
		String[] imageContents = null;
		int size = images.length;
		if (size > 0) {
			imageContents = new String[size];
			File imagesDir = new File(attachesDir);
			File imagesFile = null;
			imagesDir.mkdir();
				
			for (int i = 0; i < size; i++) {
				String imageName = getImageFileName(images[i].getFileName(),
						(id + "_" + uid + "_img" + i));

				try {
					images[i].createImageFile(attachesDir, imageName);
				} catch (IOException e) {
					System.out.println("-- IOException : [" + imageName + "] ["
							+ e.getMessage() + "]");
					continue;
				}				
				
				imagesFile = new File(attachesDir+EnvConstants.DIR_SEPARATOR+imageName);				
				if(imagesFile.length() > 0){
					imageContents[i] = attachesUrl+imageName;
				} else {
					imageContents[i] = null;
				}
				
				
				
			}
			
			imagesDir = null;
			imagesFile = null;
		}
		
		return imageContents;
	}
	/**
	 * <p>
	 * ���� ÷�� �̹��� �?���� HashTable ��ȯ.
	 * </p>
	 * 
	 * @param images				�̹��� ��Ʈ
	 * @param strLocalhost		���� ȣ��Ʈ ��
	 * @param attachesUrl			÷�� URL ��
	 * @param defaultImg			�⺻ �̹��� ���
	 * @return
	 * @throws MessagingException
	 * @return Hashtable			÷�� �̹��� ���� Table
	 */
	@SuppressWarnings("unchecked")
	protected Hashtable getCIDImagesMap(TMailPart[] images,			
			String strLocalhost, String attachesUrl, String defaultImg,
			Map<String, ImageInfoBean> resizeImgMap)
			throws MessagingException {

		Hashtable ht = new Hashtable();
		String imgUrl = null;
		String cid = null;
		String tmpImgUrl = null;
		ImageInfoBean tmpImgBean = null;
		int size = images.length;

		attachesUrl = ((attachesUrl.indexOf("http") == 0) ? "" : strLocalhost)
				+ attachesUrl;

		if (size > 0) {			
			for (int i = 0; i < size; i++) {
				cid = images[i].getContentID();
				if(resizeImgMap == null || !resizeImgMap.containsKey(cid)){
					imgUrl = attachesUrl + '/' + images[i].getImageName();
					if (images[i].getSize() == 0) {
						imgUrl = strLocalhost + defaultImg;
					}
					ht.put("cid:" + cid.substring(1, cid.length() - 1), imgUrl);
				} else {
					tmpImgBean = resizeImgMap.remove(cid);
					tmpImgUrl = tmpImgBean.getOrgImgName();
					tmpImgUrl = attachesUrl + '/' + tmpImgUrl;
					tmpImgBean.setOrgImgName(tmpImgUrl);
					tmpImgUrl = tmpImgBean.getResizeImgName();
					tmpImgUrl = attachesUrl + '/' + tmpImgUrl;
					tmpImgBean.setResizeImgName(tmpImgUrl);
					resizeImgMap.put(cid.substring(1, cid.length() - 1), tmpImgBean);
				}
			}
		}

		imgUrl = null;
		cid = null;

		return ht;
	}

	/**
	 * <p>
	 * ����Text���� CID �� URL �� �ٲپ���.
	 * </p>
	 * 
	 * @param sr					���ڿ� ġȯ Ŭ����
	 * @param bodyContents	���� ��Ʈ
	 * @param cidTable			�����̹��� HashTable
	 * @throws Exception
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	protected void replaceContentCIDImagesUrl(StringReplaceUtil sr,
			TMailPart[] bodyContents, Hashtable cidTable) throws Exception {
		int size = bodyContents.length;
		for (int i = 0; i < size; i++) {
			if (bodyContents[i].isMimeTypeNew("text/html")
					&& !cidTable.isEmpty()) {
				String txt = bodyContents[i].getText();
				bodyContents[i].setText(sr.replace(txt, cidTable, false));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void replaceContentCIDResizeImagesUrl(StringReplaceUtil sr,
			TMailPart[] bodyContents, Map<String, ImageInfoBean> resizeImgMap) throws Exception {
		int size = bodyContents.length;
		for (int i = 0; i < size; i++) {
			if (bodyContents[i].isMimeTypeNew("text/html")) {
				String txt = bodyContents[i].getText();
				bodyContents[i].setText(sr.replaceResizeImg(txt,resizeImgMap));
			}
		}
	}

	/**
	 * <p>�̹��� ���ϸ��� �����Ͽ� ��ȯ</p>
	 *
	 * @param oldStr			���� �̹�����
	 * @param newStr		���ο� �̹��� ��
	 * @return
	 * @return String			�̹�����
	 */
	private String getImageFileName(String oldStr, String newStr) {
		if (oldStr == null || newStr == null) {
			return "unknown";
		}

		int pos = oldStr.lastIndexOf(".");

		if (pos > 0) {
			return (newStr + oldStr.substring(pos));
		} else {
			return newStr;
		}
	}

}
