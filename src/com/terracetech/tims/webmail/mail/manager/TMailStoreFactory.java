package com.terracetech.tims.webmail.mail.manager;

import java.util.Map;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import com.sun.mail.pop3.POP3Store;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class TMailStoreFactory {
	
	//ssl ���
	private final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	private Session session;
	
	public Session getSession() {
		return session;
	}

	public TMailStore getImapStore(String remoteHost) throws Exception{
		StoreConfigure storeConfigure = new StoreConfigure(remoteHost);
        Properties pros = storeConfigure.getStoreProperties();
		
		session = Session.getInstance(pros);
		return new TMailStore(session.getStore());
	}
	
	public Store getStore(String remoteHost, String protocol) throws Exception{
		StoreConfigure storeConfigure = new StoreConfigure(remoteHost);
        Properties pros = storeConfigure.getStoreProperties();
		
		session = Session.getInstance(pros);
		return session.getStore(protocol);
	}
	
	public TMailStore connect(boolean isShared,
			String sharedUserSeq,
			String remoteHost, 
			User user) throws Exception{
		TMailStore store = getImapStore(remoteHost);		
		if(user == null)throw new Exception("User is Null");
		
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		User connectionUser = null;
		MailUserManager mailUserManager = 
			(MailUserManager)ApplicationBeanUtil.getApplicationBean("mailUserManager");
		if(isShared){
			if(sharedUserSeq == null)throw new Exception("sharedUserSeq is Null");
			connectionUser = mailUserManager.readUserMailConnectionInfo(Integer.parseInt(sharedUserSeq), domainSeq);			
		} else {
			connectionUser = user;
		}		
		
		if(connectionUser == null){
			throw new Exception("UserInfo");
		}
		LogManager.writeDebug(this, "host : " + connectionUser.get(User.MAIL_HOST));
		LogManager.writeDebug(this, "email : " + connectionUser.get(User.EMAIL));
		LogManager.writeDebug(this, "arg : " + connectionUser.get(User.IMAP_LOGIN_ARGS));	
		
		store.connect(connectionUser.get(User.MAIL_HOST), 
				connectionUser.get(User.EMAIL), 
				connectionUser.get(User.IMAP_LOGIN_ARGS));
		
		return store;
	}
	
	public TMailStore connect(String remoteHost,User user) throws Exception{
		TMailStore store = getImapStore(remoteHost);
		
		if(user == null)throw new Exception("User is Null");

		LogManager.writeDebug(this, "host : " + user.get(User.MAIL_HOST));
		LogManager.writeDebug(this, "email : " + user.get(User.EMAIL));
		LogManager.writeDebug(this, "arg : " + user.get(User.IMAP_LOGIN_ARGS));	
		
		store.connect(user.get(User.MAIL_HOST), 
				user.get(User.EMAIL), 
				user.get(User.IMAP_LOGIN_ARGS));
		
		return store;
	}
	
	public TMailStore connect(String remoteHost,Map<String, String> confMap) throws Exception{		
		TMailStore store = getImapStore(remoteHost);
		
		if(confMap == null)throw new Exception("confMap is Null");
		
		store.connect(confMap.get(User.MAIL_HOST), 
				confMap.get(User.EMAIL), 
				confMap.get(User.IMAP_LOGIN_ARGS));		
		
		
		return store;
	}
	
	public Store connectWithProtocol(String remoteHost,Map<String, String> confMap) throws Exception{
		Store store = null;
        if (confMap == null) {
            throw new Exception("confMap is Null");
        }

		if(confMap == null)throw new Exception("confMap is Null");
		
		LogManager.writeDebug(this, "host : " + confMap.get("host"));
		LogManager.writeDebug(this, "id : " + confMap.get("id"));
		LogManager.writeDebug(this, "pass : " + confMap.get("pass"));
		
	
		
		  String portStr = confMap.get("port");
	        int port = 110;
	        if (StringUtils.isNotEmpty(portStr)) {
	            port = Integer.parseInt(portStr);
	        }
	        String protocol =confMap.get("protocol");
	        //pops�� ����Ҷ�
	        if("pop3s".equals(protocol)){
	            Properties pop3Props = new Properties();
	            pop3Props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
	            pop3Props.setProperty("mail.pop3.socketFactory.fallback", "false");
	            pop3Props.setProperty("mail.pop3.port", confMap.get("port"));
	            pop3Props.setProperty("mail.pop3.socketFactory.port",  confMap.get("port"));
	            
	            URLName url = new URLName("pop3", confMap.get("host"),  port, "",
	            		confMap.get("id"), confMap.get("pass"));
	            
	            Session session = Session.getInstance(pop3Props, null);
	            
	           store= new POP3Store(session, url, "pop3s", port, true);
	           store.connect();
	        }else{
	        	store = getStore(remoteHost, confMap.get("protocol"));
	        	store.connect(confMap.get("host"), port, confMap.get("id"), confMap.get("pass"));
	        }

		
		return store;
	}
}
