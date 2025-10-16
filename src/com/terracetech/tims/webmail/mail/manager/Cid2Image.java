/**
 * Cid2Image.java 2008. 11. 24.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.manager;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.mail.MessagingException;

import com.terracetech.tims.mail.TMailPart;
import com.terracetech.tims.webmail.util.StringReplaceUtil;
import com.terracetech.tims.webmail.util.StringUtils;
import com.terracetech.tims.webmail.util.Validation;


/**
 * <p><strong>Cid2Image.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author
 * @since Tims7
 * @version 7.0 
 */
public class Cid2Image {
	private TMailPart[] imageParts 	= null;
	private TMailPart[] textParts	 	= null;

	private String attachesDir 	= "/tims/webmail/webapps/ROOT/images/attaches";
	private String attachesUrl 	= "/images/attaches";
	private String messageKey 	= null;
	
	public Cid2Image() {
	}

	public Cid2Image(
			TMailPart[] imageParts, TMailPart[] textParts, 
		String attachesDir, String attachesUrl, String messageKey) {

		this.imageParts 	= imageParts;
		this.textParts 		= textParts;
		this.attachesDir 	= attachesDir;
		this.attachesUrl 	= attachesUrl;
		this.messageKey 	= messageKey;
	}

	public void setAttachesDir(String attachesDir) {
		this.attachesDir = attachesDir;
	}

	public void setAttachesUrl(String attachesUrl) {
		this.attachesUrl = attachesUrl;
	}

	public void setImagePart(TMailPart[] imageParts) {
		this.imageParts = imageParts;
	}

	public void setTextPart(TMailPart[] textParts) {
		this.textParts = textParts;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public TMailPart[] getImageParts() {
		return imageParts;
	}
	public TMailPart[] getTextParts() {
		return textParts;
	}

	public void parseCidImage() throws IOException, MessagingException {

		if (textParts == null || imageParts == null) {
			return ;
		}

		Vector noCidImages = new Vector();
		Hashtable ht = new Hashtable();

		for (int i = 0; i < imageParts.length; i++) {
			if (messageKey == null) {
				messageKey = "_" + (new Date()).getTime();
			}

			String imageName 	= imageParts[i].getFileName();
			String fileName 	= getImageFileName(imageName, (messageKey +"_"+i));
			String chkImgFileName = imageName.toUpperCase();
			boolean isImage = true;
			if ( !Validation.isImageFile(chkImgFileName) ) {
				isImage = false;
			}
			
			if (!isImage) {
				String contentType = imageParts[i].getContentType();
				String contentId = imageParts[i].getContentID();
				String fileExt = getImageFileExt(contentType);
				fileName = messageKey +"_"+i+"."+fileExt;
				imageName = contentId+"."+fileExt;
			}

			try {
				imageParts[i].createImageFile(attachesDir, fileName);
			} catch (IOException e) {
				System.out.println("-- IOException : ["+imageName+"] ["+e.getMessage()+"]");
				continue;
			}

			imageParts[i].setImageName(fileName);
			String cid = imageParts[i].getContentID();
			String imageUrl = attachesUrl;

			if (imageUrl.endsWith("?")) {
				imageUrl += "img=" + fileName + "," + imageName; //TCUSTOM-3734 201801
			}
			else {
				imageUrl += "/" + fileName;
			}

			// CID Image Part
			if (cid != null) {
				ht.put(
					"cid:" + cid.substring(1, cid.length() - 1), imageUrl);
			}
			// no CID Image Part
			else {
				noCidImages.addElement(imageParts[i]);
			}
		}

		if (noCidImages.size() < imageParts.length) {
			imageParts = new TMailPart[noCidImages.size()];
			noCidImages.copyInto(imageParts);
		}

		StringReplaceUtil sr = new StringReplaceUtil();

		for (int i = 0; i < textParts.length; i++) {
			if (textParts[i].isMimeTypeNew("text/html") && !ht.isEmpty()) {
				String txt = textParts[i].getText();
				textParts[i].setText(sr.replace(txt, ht, false));
			}
		}
	}

	private String getImageFileName(String orgStr, String newStr) {
		if (orgStr == null || newStr == null) {
			return "unknown";
		}

		int pos = orgStr.indexOf(".");
		String str = orgStr.substring((pos > 0) ?  pos : 0);
		
		return (newStr + str);
	}
	
	private String getImageFileExt(String contentType) {
		if (StringUtils.isEmpty(contentType)) {
			return "img";
		}
		String ext = contentType.substring(contentType.indexOf("/")+1);
		ext = ext.toLowerCase();
		if (ext.indexOf("jpeg") > -1) {
			ext = "jpg";
		} else if (ext.indexOf("vnd") > -1) {
			ext = "dwg";
		}
		
		return ext;
	}
}
