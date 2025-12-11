package com.terracetech.tims.mail;

import java.io.*;
import java.text.*;
import java.util.*;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import org.eclipse.angus.mail.imap.protocol.BASE64MailboxDecoder;
import org.eclipse.angus.mail.imap.protocol.BASE64MailboxEncoder;

public class TMailUtility {

	public static void webfolderFolderSort(TMailFolder[] myFolders)
			throws MessagingException {

		Arrays.sort(myFolders);

		for (int i = 0; i < myFolders.length; i++) {
			myFolders[i].setIndex(i);
		}
	}
	
	public static String getCRLFEscape(String str){
		StringBuffer result = new StringBuffer();
		
		if(str != null){
			StringCharacterIterator iterator = new StringCharacterIterator(str);
			char character =  iterator.current();
			while (character != CharacterIterator.DONE ){
				if(Character.getType(character) != 15){
						switch (character) {
						case '\n':
						result.append("");
						    break;
						
						case '\t':
						result.append("");
						    break;
						
						case '\r':                
						result.append("");
						    break;
						    
						default :
							result.append(character);
							break;
							
					}
				}
				character = iterator.next();
			}
		}
		return result.toString();
	}

	public static void folderSort(TMailFolder[] myFolders)
			throws MessagingException {

		TMailFolder[] myFolders2 = new TMailFolder[myFolders.length];
		Arrays.sort(myFolders);

		int len = TMailStore.DEFAULT_FOLDERS.length;
		int cnt = 0;

		for (int i = 0, j = len; i < myFolders.length; i++) {
			boolean isDefault = false;
			String name = myFolders[i].getFullName();

			for (int k = 0; k < len && cnt < len; k++) {
				if (name.equalsIgnoreCase(TMailStore.DEFAULT_FOLDERS[k])) {
					myFolders2[k] = myFolders[i];
					myFolders2[k].setIndex(k);
					isDefault = true;
					cnt++;
				}
			}

			if (!isDefault) {
				myFolders2[j] = myFolders[i];
				myFolders2[j].setIndex(j);

				j++;
			}
		}

		System.arraycopy(myFolders2, 0, myFolders, 0, myFolders.length);
	}

	public static String IMAPFolderDecode(String folder) {
		if (!isNull(folder) && folder.indexOf("&") >= 0) {
			return BASE64MailboxDecoder.decode(folder);
		}

		return folder;
	}

	public static String IMAPFolderEncode(String folder) {
		if (!isNull(folder) && folder.indexOf("&") < 0) {
			return BASE64MailboxEncoder.encode(folder);
		}

		return folder;
	}

	public static String getEscHtmlTag(String str) {
		if (isNull(str)) {
			return str;
		}

		String tmp = str.trim();
		tmp = tmp.replaceAll("<", "&lt;");
		tmp = tmp.replaceAll(">", "&gt;");
		tmp = tmp.replaceAll("\n", "<br>");

		return tmp;
	}

	public static String getTextToHtml(String content) {
		if (content == null) {
			return "";
		}

		String tmp_content = content.replaceAll("<", "&lt;");
		tmp_content = tmp_content.replaceAll(">", "&gt;");
		tmp_content = tmp_content.replaceAll("\n", "<br>\n");

		return tmp_content;
	}

	public static String getHtmlToText(String content) {
		StringBuffer newtext = new StringBuffer();
		int cnt = 0;

		if (content == null) {
			return "";
		}

		int s = content.indexOf("style>") + 6;

		String strTmp = (s < 6) ? content : content.substring(s);

		strTmp = strTmp.replaceAll("<BR>", "\n");
		strTmp = strTmp.replaceAll("<br/>", "\n");
		strTmp = strTmp.replaceAll("<br />", "\n");
		strTmp = strTmp.replaceAll("<br>", "\n");
		strTmp = strTmp.replaceAll("&nbsp;", " ");
		strTmp = strTmp.replaceAll("&lt;", "<");
		strTmp = strTmp.replaceAll("&gt;", ">");

		StringTokenizer st = new StringTokenizer(strTmp, ">");

		while (st.hasMoreElements()) {
			String strElem = (String) st.nextElement();
			int lastIndex = strElem.indexOf("<");

			if (lastIndex <= 0)
				continue;

			newtext.append(strElem.substring(0, lastIndex));

			if (cnt > 1000)
				break;
			cnt++;
		}

		newtext.append("\n");

		return newtext.toString();
	}

	public static String getMIMEContentType(String name) {
		if (isNull(name)) {
			return "application/octet-stream";
		}

		name = name.toUpperCase();

		// MS-OFFICE
		if (name.endsWith(".DOC")) {
			return "application/msword";
		} else if (name.endsWith("XLS")) {
			return "application/vnd.ms-excel";
		} else if (name.endsWith(".PPT")) {
			return "application/vnd.ms-powerpoint";
		}

		// WORD
		else if (name.endsWith(".PDF")) {
			return "application/pdf";
		} else if (name.endsWith(".RTF")) {
			return "application/rtf";
		} else if (name.endsWith(".HWP")) {
			return "application/hwp";
		}

		// IMAGE
		else if (name.endsWith(".JPEG") || name.endsWith(".JPG")
				|| name.endsWith(".JPE")) {
			return "image/jpeg";
		} else if (name.endsWith(".GIF")) {
			return "image/gif";
		} else if (name.endsWith(".PNG")) {
			return "image/png";
		} else if (name.endsWith(".BMP")) {
			return "image/bmp";
		} else if (name.endsWith(".TIF")) {
			return "image/tiff";
		} else if (name.endsWith(".EMF")) {
			return "image/emf";
		}

		// AUDIO & VIDEO
		else if (name.endsWith(".WAV")) {
			return "audio/x-wav";
		} else if (name.endsWith(".MPEG") || name.endsWith(".MPG")
				|| name.endsWith(".MPE")) {
			return "video/mpeg";
		} else if (name.endsWith(".AVI")) {
			return "video/x-msvideo";
		}

		// APPLICATION TYPE
		else if (name.endsWith(".GTAR")) {
			return "application/x-gtar";
		} else if (name.endsWith(".TAR")) {
			return "application/x-tar";
		} else if (name.endsWith(".ZIP")) {
			return "application/zip";
		} else {
			return "application/octet-stream";
		}

	}

	public static String getImageFileExtension(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);

			byte[] bi = new byte[10];

			fis.read(bi, 0, bi.length);

			fis.close();

			return getImageFileExtension(bi);
		} catch (Exception ex) {
			System.out.println("-- ERR getImageFileExtension : ["
					+ ex.getMessage() + "]");
		}

		return "";
	}

	public static String getImageFileExtension(byte[] bi) {
		if (bi == null || bi.length < 5) {
			return "";
		}

		String byte1 = Integer.toHexString(bi[0]);
		String byte2 = Integer.toHexString(bi[1]);
		String byte3 = Integer.toHexString(bi[2]);

		// GIF (0x47 0x49 0x46 0x38 at file offset 0x00)
		if (byte1.endsWith("47") && byte2.endsWith("49")) {
			return "gif";
		}
		// JPEG, JPG (0xff 0xd8 at file offset 0x00)
		else if (byte1.endsWith("ff") && byte2.endsWith("d8")) {
			return "jpg";
		}
		// PNG (0x89 0x50 0x4e 0x47 0x0d 0x0a 0x1a 0x0a at file offset 0x00)
		else if (byte1.endsWith("89") && byte2.endsWith("50")) {
			return "png";
		}
		// BMP (0x42 0x4d at file offset 0x00)
		else if (byte1.endsWith("42") && byte2.endsWith("4d")) {
			return "bmp";
		}
		// TIFF (0x49 0x49 0x2a 0x00 (little-endian) or 0x4d 0x4d 0x00 0x2a
		// (big-endian))
		else if ((byte1.endsWith("49") && byte2.endsWith("49"))
				|| (byte1.endsWith("4d") && byte2.endsWith("4d"))) {
			return "tif";
		}

		return "img";
	}

	public static boolean isNull(String s) {
		if (s == null || s.trim().length() < 1) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isNull(Object o) {
		if (o == null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * imap cache�� ENVELOPE ������ �ƴ϶� <br>
	 * �޼���(Part) ����� ENVELOPE���� ��巹���� �Ľ��ؼ� �����Ѵ�
	 * 
	 * @param ias
	 *            - imap cache�� ����Ǿ� �ִ� InternetAddress������ ���� �ּҵ�
	 * @param source
	 *            - Part�� ������ Message, BodyPart ��
	 * @param what
	 *            - to/ cc/ from ���� �Ѱ���Header���� �Է��Ѵ�.
	 * @return - InternetAddress ������ ���� �ּҵ�
	 * @throws MessagingException
	 */
	public static InternetAddress[] getAllAddressFromSource(
			InternetAddress[] ias, Part source, String what)
			throws MessagingException {
		if (source != null) {
			String[] tmpStr = source.getHeader(what);

			try {
				if (tmpStr != null && tmpStr.length == 1) {
					ias = InternetAddress.parse(tmpStr[0]);
				}
			} catch (MessagingException e) {
				System.out.println("[InternetAddress.parse Exception]:"+e.getMessage());
			}

			tmpStr = null; // ������ null ó��
		}
		return ias;
	}
}
