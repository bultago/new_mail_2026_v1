/**
 * StoreHandler.java 2008. 11. 20.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.manager;

import java.util.Properties;

import javax.mail.Session;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.mailuser.User;

/**
 * <p><strong>StoreHandler.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li>���� Store�� �� ���� �ϱ����� Ŭ����</li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class StoreHandler {	
	
	/**
	 * <p>IMAP ���� ������ Store Ŭ������ �� ��ȯ. ȣ��Ʈ������ �޾� Properties �� �����Ѵ�.(X-WEBLOGIN �� �ʿ�)</p>
	 *
	 * @param remoteHost			ȣ��� ȣ��Ʈ ����.
	 * @return
	 * @throws Exception
	 * @return TMailStore			��ȯ�� Store ��ü
	 */
	public TMailStore getImapStore(String remoteHost) throws Exception{
		StoreConfigure storeConfigure = new StoreConfigure(remoteHost);
        Properties pros = storeConfigure.getStoreProperties();
		return new TMailStore(Session.getInstance(pros).getStore());
	}
	
	/**
	 * <p>host, email, xpass ������ �޾� IMAP�� �����  Store ��ü�� ��ȯ.</p>
	 *
	 * @param remoteHost			ȣ�� ȣ��Ʈ ����
	 * @param host					IMAPȣ��Ʈ
	 * @param email					����� email ����
	 * @param xpass				�α��ν��� Arg����
	 * @return
	 * @throws Exception
	 * @return TMailStore			���ӵ�Store ��ü.
	 */
	public TMailStore getImapConnectStore(String remoteHost,
												String host,	String email, String xpass) 
	throws Exception{
		
		TMailStore store = getImapStore(remoteHost);
		store.connect(host, email, xpass);
		
		return store;		
	}
	
	public TMailStore getImapConnectStore(String remoteHost,User user) 
	throws Exception{
		TMailStore store = getImapStore(remoteHost);
		store.connect(user.get(User.MAIL_HOST), user.get(User.EMAIL), user.get(User.IMAP_LOGIN_ARGS));
		
		return store;	
	}

}
