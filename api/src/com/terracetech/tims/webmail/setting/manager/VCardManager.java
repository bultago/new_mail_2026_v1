/**
 * VCardManager.java 2008. 12. 5.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.setting.manager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeUtility;

import com.terracetech.secure.Base64;
import com.terracetech.tims.webmail.setting.dao.VCardDao;
import com.terracetech.tims.webmail.setting.vo.VCardVO;
import com.terracetech.tims.webmail.util.CharsetToolkit;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * <p><strong>VCardManager.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author jpjung
 * @since Tims7
 * @version 7.0 
 */
public class VCardManager {
	
	private VCardDao dao;
	
	public void setDao(VCardDao dao) {
		this.dao = dao;
	}

	public String getVcardString(int userSeq) throws Exception
	{
		return getVcardString(dao.readVcard(userSeq));
	}

	public String getVcardString(VCardVO vo) {
		if(vo==null)
			return "";
		
		String vcard = "BEGIN:VCARD" + "\nVERSION:2.1" + "\nN:;;";
		
		if (! StringUtils.isEmpty(vo.getMemberName())) { // name
			vcard += "\nFN:" + vo.getMemberName();
		}
		if (! StringUtils.isEmpty(vo.getMemberEmail())) { // Email
			vcard += "\nEMAIL;INTERNET:" + vo.getMemberEmail();
		}
		if (! StringUtils.isEmpty(vo.getCompanyName())) { // org
			vcard += "\nORG:" + vo.getCompanyName();

			if (! StringUtils.isEmpty(vo.getDepartmentName())) { // dep
				vcard += ";" + vo.getDepartmentName();
			} else {
				vcard += ";";
			}
		}
		if (! StringUtils.isEmpty(vo.getTitleName())) { // title
			vcard += "\nTITLE:" + vo.getTitleName();
		}
		if (! StringUtils.isEmpty(vo.getHomeTel())) { // h phone
			vcard += "\nTEL;HOME;VOICE:" + vo.getHomeTel();
		}
		if (! StringUtils.isEmpty(vo.getMobileNo())) { // cell
			vcard += "\nTEL;CELL:" + vo.getMobileNo();
		}
		if (! StringUtils.isEmpty(vo.getOfficeTel())) { // o phone
			vcard += "\nTEL;WORK;VOICE:" + vo.getOfficeTel();
		}
		if (! StringUtils.isEmpty(vo.getOfficeFax()))  { // o fax
			vcard += "\nTEL;WORK;FAX:" + vo.getOfficeFax();
		}
		if (! StringUtils.isEmpty(vo.getOfficeHomepage()))  { // url
			vcard += "\nURL;WORK:" + vo.getOfficeHomepage();
		}
		if (! StringUtils.isEmpty(vo.getOfficeExtAddress())) { // address
			vcard += "\nADR;WORK:;;" + vo.getOfficeExtAddress();

			if (! StringUtils.isEmpty(vo.getOfficeStreet())) { // street
				vcard += ";" + vo.getOfficeStreet();
			} else {
				vcard += ";";
			}
			if (! StringUtils.isEmpty(vo.getOfficeCity())) { // city
				vcard += ";" + vo.getOfficeCity();
			} else {
				vcard += ";";
			}
			if (! StringUtils.isEmpty(vo.getOfficePostalCode())) { // zip
				vcard += ";" + vo.getOfficePostalCode();
			} else {
				vcard += ";";
			}
			if (! StringUtils.isEmpty(vo.getOfficeCountry())) { // country
				vcard += ";" + vo.getOfficeCountry();
			} else {
				vcard += ";";
			}
		}

		if (! StringUtils.isEmpty(vo.getDescription())) { // memo
			String note = vo.getDescription();
			byte[] nb = note.getBytes();
			CharsetToolkit charsetTool = new CharsetToolkit(nb);
			String charsetStr = charsetTool.guessEncoding().displayName();
			//US-ASCII: ASCII 7 bit: ??
			if(StringUtils.isNotEmpty(charsetStr)){
				String[] charset = charsetStr.split(":");	
				if(charset.length >1){
					vcard += "\nNOTE;CHARSET="+charset[1]+";ENCODING=BASE64:" +Base64.encode(note.getBytes());
				}else{
					vcard += "\nNOTE;CHARSET="+charsetStr+";ENCODING=BASE64:" +Base64.encode(note.getBytes());
				}
			}else{
				vcard += "\nNOTE;CHARSET=US-ASCII;ENCODING=BASE64:" +Base64.encode(note.getBytes());
			}
			
			
						
		}
		
		vcard += "\nEND:VCARD\n";
		return vcard;
	}
	
	public VCardVO getVcardInfo(int userSeq) throws Exception {
		return dao.readVcard(userSeq);
	}
	
	public void saveVcardInfo(VCardVO vcardVo) throws Exception {
		dao.saveVcard(vcardVo);
	}
	
	public void modifyVcardInfo(VCardVO vCardVo) throws Exception {
		dao.modifyVcard(vCardVo);
	}
}
