package com.terracetech.tims.webmail.bbs.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.SocketException;
import java.util.Map;

import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailPart;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.bbs.manager.BbsManager;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.util.StringUtils;

public class DownloadAttachAction extends BaseAction {
	
	private BbsManager bbsManager = null;
	private long uid = 0;
	private int part = 0;
	private int bbsId = 0;
	
	public String execute() throws Exception {

		User user = UserAuthManager.getUser(request);
		
		TMailStoreFactory factory = new TMailStoreFactory();
		Map<String, String> confMap = bbsManager.getBbsConnectionInfo(user.get(User.MAIL_DOMAIN));
		TMailStore store = factory.connect(request.getRemoteAddr(), confMap);
		TMailFolder folder = store.getFolder(Integer.toString(bbsId));
		
		folder.open(false);
		TMailMessage message = folder.getMessageByUID(uid, false);
		TMailPart[] attParts = message.getAttachFiles();

		TMailPart attPart = attParts[part - 1];
		String fileName = new String(attPart.getFileName().getBytes());

		String agent 	= request.getHeader("user-agent");			
		fileName = StringUtils.getDownloadFileName(fileName, agent);			
		
		response.setHeader("Content-Type",
	        	"application/download; name=\"" + fileName + "\"");
		response.setHeader("Content-Disposition",
	        	"attachment; filename=\"" + fileName + "\"");			
		
		BufferedOutputStream out = new BufferedOutputStream(
				response.getOutputStream());
		
		BufferedInputStream in =  new BufferedInputStream(attPart.getInputStream());
		
		try {
			byte[] buffer = new byte[1024 * 4];
			int n;
			while ((n = in.read(buffer, 0, buffer.length)) != -1) {
				out.write(buffer, 0, n);
			}
		} catch (Exception e) {
			if(e.getCause() instanceof SocketException){
			}else{
				LogManager.writeErr(this, e.getMessage(), e);	
			}
		}finally{

			in.close();
			out.close();

			folder.close(false);
			store.close();
		}
		
		return null;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public int getPart() {
		return part;
	}

	public void setPart(int part) {
		this.part = part;
	}

	public int getBbsId() {
		return bbsId;
	}

	public void setBbsId(int bbsId) {
		this.bbsId = bbsId;
	}
	
	public void setBbsManager(BbsManager bbsManager) {
		this.bbsManager = bbsManager;
	}

	public BbsManager getBbsManager() {
		return bbsManager;
	}
}
