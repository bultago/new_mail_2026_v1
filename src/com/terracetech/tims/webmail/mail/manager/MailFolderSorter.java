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
 * 주요설명
 * </p>
 * <ul>
 * <li>메일함의 이름과 순서를 가지고 정렬 해주는 클래스</li>
 * </ul>
 * 
 * @author jpjung
 * @since Tims7
 * @version 7.0
 */
public class MailFolderSorter {

	/**
	 * <p>기본메일함을 찾아 정렬하여 이를 반환.</p>
	 *
	 * @param folders			메일함 배열
	 * @return
	 * @return TMailFolder[]	정렬된 기본메일함 배열
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
	 * <p>기본메일함의 순서 정보를 반환.</p>
	 * <p>순서 정보가 -1일경우 기ㅗㄴ메일함이 아님.</p>
	 *
	 * @param folderName		메일함 이름
	 * @return
	 * @return int					순서 값.
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
