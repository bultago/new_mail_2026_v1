package com.terracetech.tims.webmail.mail.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Store;

import org.eclipse.angus.mail.pop3.POP3Folder;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.MakeMessage;
import com.terracetech.tims.webmail.mail.manager.Pop3Manager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.Pop3VO;
import com.terracetech.tims.webmail.util.StringUtils;

public class ReceivePopMessageAction extends BaseAction {

	/**
	 * <p></p> 
	 */
	private static final long serialVersionUID = 9195792847523385518L;
	public Pop3Manager pop3Manager = null;
	private SettingManager manager = null;
	public String[] rules = null;

	public void setPop3Manager(Pop3Manager pop3Manager) {
		this.pop3Manager = pop3Manager;
	}
	
	public void setManager(SettingManager manager) {
		this.manager = manager;
	}
	
	public void setRules(String[] rules) {
		this.rules = rules;
	}

	public String execute() throws Exception {		
		I18nResources resource = getMessageResource("setting");
		I18nResources commonResource = getMessageResource("common");
		TMailStoreFactory factory = new TMailStoreFactory();
		Store pop3Store = null;
		TMailStore myStore = null;
		Folder pop3Folder = null;
		TMailFolder myFolder = null;
		List<Pop3VO> pop3List = null;
		boolean isQuotaFull = false;
		
		response.setHeader("Content-Type", "text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();	
		
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		if (rules != null && rules.length > 0) {
			pop3List = new ArrayList<Pop3VO>();
			for (int i=0; i<rules.length; i++) {
				StringTokenizer st = new StringTokenizer(rules[i], "|");
				if (st.countTokens() == 2) {
					Pop3VO pop3Vo = new Pop3VO();
					pop3Vo = manager.readPop3(mailUserSeq, st.nextToken(), st.nextToken());
					pop3List.add(pop3Vo);
				}
			}
		}
		System.out.println("### pop3List.size() : "+pop3List.size());
		for (int i=0; i < pop3List.size(); i++) {
			
			int totalCount = 0;
			int beforeCount = 0;
			int successCount = 0;
			int progress = 0;
			int failCount = 0;
			int lastProcessedIndex = 0;
			ArrayList<Integer> uidList = new ArrayList<Integer>();
			boolean deleteOn = false;
			
			if(isQuotaFull) break;
			
			try{
				Pop3VO pop3Vo = pop3List.get(i);
				pop3Manager.setPop3(pop3Vo);
				Map<String, String> pop3ConfMap = pop3Manager.getPop3Config();
				
				pop3Store = factory.connectWithProtocol(request.getRemoteAddr(), pop3ConfMap);
				myStore = factory.connect(request.getRemoteAddr(), user);
				
				pop3Folder = pop3Manager.getPOP3Folder(pop3Store);
				myFolder = pop3Manager.getMyFolder(myStore);
				
				pop3Folder.open(Folder.READ_WRITE);
				myFolder.open(true);
				/*
				Message[] messages = pop3Folder.getMessages();
				totalCount = messages.length;
				*/
				totalCount = pop3Folder.getMessageCount();
				lastProcessedIndex = getLastProcessedIndex(pop3Vo, pop3Folder, totalCount);
				beforeCount = lastProcessedIndex;

				/*
				for (int j=0; j < messages.length; j++) {
					if (pop3Manager.haveMsgName(pop3Folder, messages[j])) {
						beforeCount = j+1;
						break;
					}
				}
				pop3Folder.close(true);
				*/
				
				out.print(MakeMessage.printParentInnerHTML("total_"+i, Integer.toString(totalCount-beforeCount)));
				out.flush();
				
				if (beforeCount == totalCount) {
					out.print(MakeMessage.printParentStyleWidth("graph_"+i, 100+"%"));
					out.print(MakeMessage.printParentInnerHTML("percent_"+i, 100+"%"));
					out.flush();
				}
				deleteOn = "1".equalsIgnoreCase(pop3Vo.getPop3Del());
				/*
				Message[] messages1 = null;
				Message message1 = null;
				*/
				Message message = null;
				System.out.println("### deleteOn : "+deleteOn);
				//for (int k=beforeCount; k < totalCount; k++) {
				for (int index = (lastProcessedIndex + 1); index <= totalCount; index++) {
					try{
						/*
						if(!pop3Folder.isOpen())
							pop3Folder.open(Folder.READ_WRITE);
							
						messages1 = pop3Folder.getMessages();
						if ("1".equalsIgnoreCase(pop3Vo.getPop3Del())) {
							message1 = messages1[0];
						} else {
							message1 = messages1[k];
						}	
						*/
						
						//pop3Manager.getPop3Message(myFolder, pop3Folder, message1, Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
						//message = pop3Folder.getMessage(index);
						// TCUSTOM-2259 2016-10-28
						try{ 
							message = pop3Folder.getMessage(index); 
						}catch(Exception e){
							if("true".equalsIgnoreCase(EnvConstants.getMailSetting("mail.debug")))
								System.out.println("### pop3Folder.getMessage("+index+") exception - "+e.getMessage());
							
							if(!pop3Folder.isOpen()) {
								if("true".equalsIgnoreCase(EnvConstants.getMailSetting("mail.debug")))
									System.out.println("### pop3Folder is already closed! reOpening...");
								
								pop3Folder.open(Folder.READ_WRITE);
							}
							message = pop3Folder.getMessage(index);
						}

						pop3Manager.getPop3Message(myFolder, pop3Folder, message, Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
						if(deleteOn) uidList.add(index);
						
						/*
						if(pop3Folder.isOpen())
							pop3Folder.close(true);
						*/
						
						successCount++;
						out.print(MakeMessage.printParentInnerHTML("success_"+i, Integer.toString(successCount)));
					} catch (Exception ex) {
						ex.printStackTrace();
						failCount++;
						out.print(MakeMessage.printParentInnerHTML("fail_"+i, Integer.toString(failCount)));
						if (pop3Manager.isQuotaFull(ex)) {
							isQuotaFull = true;
							break;
						}
						
						if (!myStore.isConnected()) {
							myStore = factory.connect(request.getRemoteAddr(), user);
							myFolder = pop3Manager.getMyFolder(myStore);
						}
						
						if (!pop3Store.isConnected()) {
							pop3Store = factory.connectWithProtocol(request.getRemoteAddr(), pop3ConfMap);
							pop3Folder = pop3Manager.getPOP3Folder(pop3Store);
						}
					} finally {
						progress = (int)(((float)(successCount+failCount)/(float)(totalCount-beforeCount))*100);
						out.print(MakeMessage.printParentStyleWidth("graph_"+i, Integer.toString(progress)+"%"));
						out.print(MakeMessage.printParentInnerHTML("percent_"+i, Integer.toString(progress)+"%"));
					}
					out.flush();
				}
			} catch (MessagingException e) {
				e.printStackTrace();
				out.print(MakeMessage.printParentInnerHTML("graph_wrap_"+i,resource.getMessage("conf.pop.47")));
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
				out.print(MakeMessage.printParentInnerHTML("graph_wrap_"+i,commonResource.getMessage("error.msg.001")));
				out.flush();
			}
			finally {
				if (myFolder != null && myFolder.isOpen()) myFolder.close(true);
				if (myStore != null && myStore.isConnected()) myStore.close();
				try{ 
					if (pop3Folder != null && pop3Folder.isOpen()){
						if (deleteOn) {
							((POP3Folder) pop3Folder).deleteOnClose(true, uidList);
						}else{
							((POP3Folder) pop3Folder).close(true);
						}
					} 
				}catch(Exception e){
					e.printStackTrace();
				}
				if (pop3Store != null && pop3Store.isConnected()) pop3Store.close();
				
				out.print(MakeMessage.printParentStyleDisplay("pop3_loader_"+i,"none"));
				out.flush();
			}
		}

		if (isQuotaFull) {
			out.print(MakeMessage.printAlertParentCloseOnly(resource.getMessage("conf.pop.48")));
			out.flush();
			return null;
		}
		
		out.print(MakeMessage.printAlert(resource.getMessage("conf.pop.49")));
		out.flush();
		
		return null;
	}
	
	private int getLastProcessedIndex(Pop3VO pop3Vo, Folder pop3Folder, int totalCount) throws MessagingException {

		String lastProcessedId = pop3Vo.getPop3Msgname();
		if (StringUtils.isEmpty(lastProcessedId)) {
			return 0;
		}
	
		Message message = null;
		String currentUid = null;
		for (int lastProcessedIndex = totalCount ; lastProcessedIndex > 0; lastProcessedIndex--) {
			try {
				message = pop3Folder.getMessage(lastProcessedIndex);
				currentUid = ((POP3Folder)pop3Folder).getUID(message);
				if (currentUid.equals(lastProcessedId)){
					return lastProcessedIndex;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return 0;
	}
}
