package com.terracetech.tims.webmail.setting.dao;

import com.terracetech.tims.webmail.setting.vo.VCardVO;

public interface IVCardDao {

	public VCardVO readVcard(int userSeq) throws Exception;
	
	public void saveVcard(VCardVO vcard) throws Exception;
	
	public void modifyVcard(VCardVO vcard) throws Exception;
}
