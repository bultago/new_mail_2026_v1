package com.terracetech.tims.webmail.mail.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Enumeration;

import jakarta.mail.internet.MimeMessage;

import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;

public class LocalMailManageAction extends BaseAction {

	private static final long serialVersionUID = -7775143373426448632L;	
	
	public String sendLocalMail()throws Exception{
		
		String folderName = request.getParameter("folder");
		String uid = request.getParameter("uid");
		
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailFolder folder = null;
		try {			
			store = factory.connect(request.getRemoteAddr(), user);
			folder = store.getFolder(folderName);
			folder.open(false);
			TMailMessage message = folder.getMessageByUID(Long.parseLong(uid), false);
			
			BufferedOutputStream out = new BufferedOutputStream(response
					.getOutputStream());
			Enumeration enumer = message.getAllHeaderLines();

			while (enumer.hasMoreElements()) {
				String header = (String) enumer.nextElement();
				out.write(header.getBytes());
				out.write('\r');
				out.write('\n');
			}
			out.write('\r');
			out.write('\n');

			InputStream in = message.getRawInputStream();
			byte[] buffer = new byte[1024 * 1024];
			int n;
			
			while ((n = in.read(buffer, 0, buffer.length)) != -1) {
				out.write(buffer, 0, n);
			}
				
			in.close();
			out.close();
			
			folder.close(false);
			
		} catch (Exception e) {
			throw e;
		} finally {
			if(store !=null && store.isConnected())
				store.close();
			if(folder !=null && folder.isOpen())
				folder.close(false);
		}
		
		return null;
	}
	
	public String listFolderName()throws Exception{
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailFolder[] folders = null;
		try {			
			store = factory.connect(request.getRemoteAddr(), user);
			folders = store.getWebfolderFolders();
			
			response.setHeader("Content-Type", "text/html; charset=UTF-8");	
			PrintWriter out = response.getWriter();	
			for (int i = 0; i < folders.length; i++) {
				String folder_name = folders[i].getFullName();
				out.println("* " + folder_name);
			}
		    out.close();
			out.flush();
			
			
		} catch (Exception e) {
			throw e;
		} finally {
			if(store !=null && store.isConnected())
				store.close();			
		}
		
		return null;
	}
	
	public String moveLocalMessage()throws Exception{
		MultiPartRequestWrapper multiWrapper = null;	
		
		if(request instanceof MultiPartRequestWrapper) {
			multiWrapper = (MultiPartRequestWrapper) request;		
		}
		
		String folderName = multiWrapper.getParameter("folder");		
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailFolder folder = null;
		String result = null;
		FileInputStream is = null;
		String msgId = null;
		long uid = 0;
		try {
			store = factory.connect(request.getRemoteAddr(), user);
			
			File emlFile = multiWrapper.getFiles("theFile")[0];
					
			is = new FileInputStream(emlFile);
			MimeMessage mimeMessage = new MimeMessage(factory.getSession(),is);
			mimeMessage.saveChanges();
			msgId = mimeMessage.getHeader("Message-ID", null);
			folder = store.getFolder(folderName);
			folder.open(true);
			folder.appendMessages(new TMailMessage[]{new TMailMessage(mimeMessage)});			
			folder.close(true);
			result = "RESULT=OK";
			
		} catch (Exception e) {			
			result = "RESULT=300";
		} finally {
			if(is != null){
				is.close();				
			}
			is = null;
			if(store !=null && store.isConnected())
				store.close();
			if(folder !=null && folder.isOpen())
				folder.close(false);
		}
		PrintWriter out = response.getWriter();	
		out.print(result);
		out.close();		
		
		return null;
	}
	
	public String writeLocalMessage()throws Exception{
		MultiPartRequestWrapper multiWrapper = null;	
		
		if(request instanceof MultiPartRequestWrapper) {
			multiWrapper = (MultiPartRequestWrapper) request;		
		}
		
		String folderName = FolderHandler.TRASH;
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailFolder folder = null;
		String result = null;
		FileInputStream is = null;
		String msgId = null;
		long uid = 0;
		try {
			store = factory.connect(request.getRemoteAddr(), user);
			
			File emlFile = multiWrapper.getFiles("theFile")[0];
					
			is = new FileInputStream(emlFile);
			MimeMessage mimeMessage = new MimeMessage(factory.getSession(),is);
			mimeMessage.saveChanges();
			msgId = mimeMessage.getHeader("Message-ID", null);
			folder = store.getFolder(folderName);
			folder.open(true);
			folder.appendMessages(new TMailMessage[]{new TMailMessage(mimeMessage)});
			folder.close(true);
			folder.open(false);
			uid = folder.xsearchMID(msgId);
			folder.close(false);
			result = "RESULT=OK";
			
		} catch (Exception e) {			
			result = "RESULT=400";
		} finally {
			if(is != null){
				is.close();				
			}
			is = null;
			if(store !=null && store.isConnected())
				store.close();
			if(folder !=null && folder.isOpen())
				folder.close(false);
		}
		PrintWriter out = response.getWriter();		
		out.print("UID=" + uid + "\r\n");		
		out.print(result);
		out.close();		
		
		return null;
	}
	
	public String goLocalMail() throws Exception{
		return "success";	
	}
	
}
