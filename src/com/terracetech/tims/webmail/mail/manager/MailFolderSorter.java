/**
 * MailFoderSorter.java 2008. 11. 28.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.manager;

import java.util.Vector;

import com.terracetech.tims.mail.TMailFolder;

/**
 * <p>
 * <strong>MailFoderSorter.java</strong> Class Description
 * </p>
 * <p>
 * ÁÖ¿ä¼³¸í
 * </p>
 * <ul>
 * <li>¸ÞÀÏÇÔÀÇ ÀÌ¸§°ú ¼ø¼­¸¦ °¡Áö°í Á¤·Ä ÇØÁÖ´Â Å¬·¡½º</li>
 * </ul>
 * 
 * @author jpjung
 * @since Tims7
 * @version 7.0
 */
public class MailFolderSorter {

	/**
	 * <p>±âº»¸ÞÀÏÇÔÀ» Ã£¾Æ Á¤·ÄÇÏ¿© ÀÌ¸¦ ¹ÝÈ¯.</p>
	 *
	 * @param folders			¸ÞÀÏÇÔ ¹è¿­
	 * @return
	 * @return TMailFolder[]	Á¤·ÄµÈ ±âº»¸ÞÀÏÇÔ ¹è¿­
	 */
	public TMailFolder[] getSortedFoldersByDefault(boolean isOverQuota, TMailFolder[] folders) {

		Vector<TMailFolder> folderList = new Vector<TMailFolder>();
		int dfolderSize = 6;
		if(isOverQuota){
			dfolderSize = 7;
		}	
		
		folderList.setSize(dfolderSize);
		
		for (int i = 0; i < folders.length; i++) {
			int pos = getDefaultBoxPos(folders[i].getFullName());
			
			if(pos > -1){
				if(pos == 6 && !isOverQuota){
					continue;
				}
				folderList.setElementAt(folders[i], pos);								
			}
		}
		return folderList.toArray(new TMailFolder[dfolderSize]);
	}

	/**
	 * <p>±âº»¸ÞÀÏÇÔÀÇ ¼ø¼­ Á¤º¸¸¦ ¹ÝÈ¯.</p>
	 * <p>¼ø¼­ Á¤º¸°¡ -1ÀÏ°æ¿ì ±â¤Ç¤¤¸ÞÀÏÇÔÀÌ ¾Æ´Ô.</p>
	 *
	 * @param folderName		¸ÞÀÏÇÔ ÀÌ¸§
	 * @return
	 * @return int					¼ø¼­ °ª.
	 */
	public static int getDefaultBoxPos(String folderName) {
		int pos = -1;

		if (folderName.equalsIgnoreCase(FolderHandler.INBOX)) {
			pos = 0;
		} else if (folderName.equalsIgnoreCase(FolderHandler.SENT)) {
			pos = 1;
		} else if (folderName.equalsIgnoreCase(FolderHandler.DRAFTS)) {
			pos = 2;
		} else if (folderName.equalsIgnoreCase(FolderHandler.RESERVED)) {
			pos = 3;
		} else if (folderName.equalsIgnoreCase(FolderHandler.SPAM)) {
			pos = 4;
		} else if (folderName.equalsIgnoreCase(FolderHandler.TRASH)) {
			pos = 5;
		} else if (folderName.equalsIgnoreCase(FolderHandler.QUOTAVIOLATE)) {
			pos = 6;
		}

		return pos;
	}
}
