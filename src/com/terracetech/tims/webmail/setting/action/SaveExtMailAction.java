package com.terracetech.tims.webmail.setting.action;

import java.util.Locale;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.exception.DuplicatePopConfigException;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.Pop3VO;
import com.terracetech.tims.webmail.util.StringUtils;


public class SaveExtMailAction extends BaseAction{
	
	private static final long serialVersionUID = 20090112L;
	
	private SettingManager manager = null;
	
	private String pop3Host;
	
	private int pop3Port;
	
	private String pop3Id;
	
	private String pop3Pw;
	
	private String boxName;
	
	private String policy;
	
	private String mbox;
	
	private String pop3del;
	 //ssl 체크
    private String sslCheck;
	
	public void setManager(SettingManager manager) {
		this.manager = manager;
	}
	
	public void setPop3Host(String pop3Host) {
		this.pop3Host = pop3Host;
	}
	
	public void setPop3Id(String pop3Id) {
		this.pop3Id = pop3Id;
	}
	
	public void setPop3Port(int pop3Port) {
		this.pop3Port = pop3Port;
	}

	public void setPop3Pw(String pop3Pw) {
		this.pop3Pw = pop3Pw;
	}
	
	public void setBoxName(String boxName) {
		this.boxName = boxName;
	}

	public void setPop3del(String pop3del) {
		this.pop3del = pop3del;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

    public void setSslCheck(String sslCheck) {
		this.sslCheck = sslCheck;
	}
    
    public void setMbox(String mbox) {
            this.mbox = mbox;
    }

	public String execute() throws Exception {
		I18nResources resource = getMessageResource("setting");
		
		User user = UserAuthManager.getUser(request);
		
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		String msg = null;
		try {
			Pop3VO pop = manager.readPop3(mailUserSeq, pop3Host, pop3Id);
			
			if (pop != null) {
				throw new DuplicatePopConfigException();
			}
			
			store = factory.connect(request.getRemoteAddr(), user);
			
			if ("on".equals(mbox)) {
				policy = "move " + boxName;
			
				TMailFolder folder = store.getFolder(boxName);
				if (!folder.exists()) {
					folder.create();
				}
			}
			
			policy = StringUtils.IMAPFolderEncode(policy);
			
			Pop3VO pop3Vo = new Pop3VO();
			pop3Vo.setUserSeq(mailUserSeq);
			pop3Vo.setPop3Host(pop3Host);
			pop3Vo.setPop3Id(pop3Id);
			pop3Vo.setPop3Passwd(pop3Pw);
			pop3Vo.setPop3Port(pop3Port);
			pop3Vo.setPop3Boxname(policy);
			
			if ("on".equals(pop3del)) {
				pop3Vo.setPop3Del("1");
			}
			else {
				pop3Vo.setPop3Del("0");
			}
			//ssl체크되면 1 아니면 0
            if("on".equals(sslCheck)){
         	   	pop3Vo.setUsedSsl("1");
            }else{
            	pop3Vo.setUsedSsl("0");
            }
			
			manager.savePop3(pop3Vo);
			msg = resource.getMessage("save.ok");
		}catch (DuplicatePopConfigException e) {
			LogManager.writeErr(this, e.getMessage(), e);
			msg = resource.getMessage("conf.pop.51");
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			msg = resource.getMessage("save.fail");
		}finally {
			if (store != null && store.isConnected()) {
				store.close();
			}
		}
		request.setAttribute("msg", msg);
		return "success";
	}
}
