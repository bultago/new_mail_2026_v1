
<%@page import="com.terracetech.tims.webmail.setting.manager.SettingManager"%>
<%@page import="com.terracetech.tims.webmail.exception.UserNotFoundException"%>
<%@page import="com.terracetech.tims.webmail.mail.ibean.MailQuotaBean"%>
<%@page import="com.terracetech.tims.webmail.mail.manager.FolderHandler"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="java.io.OutputStreamWriter"%>
<%@ page import="java.io.OutputStream"%>
<%@ page import="java.util.*"%>
<%@ page import="java.net.*"%>
<%@ page import="javax.mail.*"%>
<%@ page import="com.terracetech.tims.common.I18nConstants"%>
<%@ page import="com.terracetech.tims.common.I18nResources"%>
<%@ page import="com.terracetech.tims.webmail.mail.ibean.MailFolderBean"%>
<%@ page import="com.terracetech.tims.webmail.common.EnvConstants"%>
<%@ page import="com.terracetech.tims.webmail.mail.manager.MailManager"%>
<%@ page import="com.terracetech.tims.webmail.mail.manager.TMailStoreFactory"%>
<%@ page import="com.sun.mail.imap.*"%>
<%@ page import="com.terracetech.tims.mail.*"%>
<%@ page import="com.terracetech.tims.webmail.mailuser.manager.*"%>
<%@ page import="com.terracetech.tims.webmail.mailuser.User"%>
<%@ page import="com.terracetech.tims.webmail.mailuser.manager.UserAuthManager"%>
<%@ page import="com.terracetech.tims.webmail.util.*"%>

<%
	try{
		
		String remoteIp = request.getRemoteAddr();
		String email = request.getParameter("email");
		if(email == null || email.length() == 0){
			throw new Exception("EMAIL IS NULL");
		}
		String[] userValues = email.split("@");
		if(userValues == null || userValues.length < 2){
			throw new Exception("EMAIL IS NOT MATCH");
		}
		String id = userValues[0];
		String domain = userValues[1];
		
		try{
			SettingManager settingManager = (SettingManager)ApplicationBeanUtil.getApplicationBean("settingManager");
			if(!settingManager.isApiAccessAllow(remoteIp)){
				throw new Exception("IS NOT ALLOW ACCESS IP!!!");				
			}
		} catch (Exception e){
			throw new Exception("-- ALLOWIP ["+id+"][" + remoteIp + "] : [" + e.getMessage() + "]");
		}
		
		int newmail = 0;
		int unseen	= 0;
		int totalMail = 0;
		long usage =0;
		long limit=0;
		int persent = 0;	
		
		String type  = (request.getParameter("type") == null) ? "basic": request.getParameter("type") ;	
		String folder = (request.getParameter("folder")==null) ?  "" : request.getParameter("folder");
		String folderBounds = request.getParameter("folderBounds");
		String []datacase = {"folderName","folderUTF8","totalMail","newmail","unseen"};
		String usecase = (request.getParameter("usecase")==null ) ? "abcde" : request.getParameter("usecase") ;
		String []usecaseStr = null;
		String useQuota = (request.getParameter("useQuota")==null) ? "off" : request.getParameter("useQuota");
		
		
		ArrayList<String[]> folderData = new ArrayList<String[]>();
		final int FOLDER_NAME = 0;
		final int FOLDER_NAME_UTF8 = 1;
		final int TOTAL = 2;
		final int NEWMAIL = 3;
		final int UNSEEN = 4;		
			
		if(usecase!=null){
		usecaseStr=new String[usecase.length()];
		for(int i=0;i<usecase.length();i++){
			if(usecase.subSequence(i,i+1).equals("a")){
				usecaseStr[i]=new String(datacase[FOLDER_NAME]);
			}
			if(usecase.subSequence(i,i+1).equals("b")){
				usecaseStr[i]=new String(datacase[FOLDER_NAME_UTF8]);
			}
			if(usecase.subSequence(i,i+1).equals("c")){
				usecaseStr[i]=new String(datacase[TOTAL]);
			}
			if(usecase.subSequence(i,i+1).equals("d")){
				usecaseStr[i]=new String(datacase[NEWMAIL]);
			}	
			if(usecase.subSequence(i,i+1).equals("e")){
				usecaseStr[i]=new String(datacase[UNSEEN]);
			}	
		
		  }
		}
	
		
		String outData="";
		String outStr="";
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		MailFolderBean[] defaultFolderList = null;
		MailFolderBean[] userFolderList = null;
		
		try {
			MailUserManager userManager = (MailUserManager)ApplicationBeanUtil.getApplicationBean("mailUserManager");
			MailManager mailManager = (MailManager)ApplicationBeanUtil.getApplicationBean("mailManager");
			User user = userManager.readUserConnectionInfo(id,domain);
			
			if (user == null) {
				throw new UserNotFoundException(id);
			}
			
			userManager.setUserSystemInfo(user);
			user.put(User.QUOTA_STR, QuotaUtil.getQuotaStr(user));		
			user.put(User.IMAP_LOGIN_ARGS, user.get(User.MESSAGE_STORE) + " "+
					user.get(User.QUOTA_STR) + " " +
					user.get(User.MAIL_USER_SEQ) + " " + 
					user.get(User.MAIL_DOMAIN_SEQ));
			
			store = factory.connect(request.getRemoteAddr(), user);
			I18nResources resource = new I18nResources(I18nConstants.getUserLocale(request),"mail");
			mailManager.setProcessResource(store,resource);	
			
					
			String folderNameKey = null;
			String[] folderDataElement = null;
			
			if(folder.equals("")){
				if(folderBounds.equals("all") || folderBounds.equals("default")){
					defaultFolderList = mailManager.getFolderList(EnvConstants.DEFAULT_FOLDER,false,-1);
					for(MailFolderBean tempFolder:defaultFolderList){
						folderNameKey = tempFolder.getFullName();
						folderDataElement = new String[5]; //name,name-utf8, totall, newmail ,unseen
						
						folderDataElement[FOLDER_NAME] = StringUtils.EscapeHTMLTag(folderNameKey);
						folderDataElement[FOLDER_NAME_UTF8] = URLEncoder.encode(folderNameKey, "UTF-8");
						folderDataElement[TOTAL] = 	Long.toString(tempFolder.getTotalCnt());
						folderDataElement[NEWMAIL] = Long.toString(tempFolder.getNewCnt());
						folderDataElement[UNSEEN] = Long.toString(tempFolder.getUnseenCnt());
						
						folderData.add(folderDataElement);	
					}
				}
				if(folderBounds.equals("all") || folderBounds.equals("my")){
					userFolderList = mailManager.getFolderList(EnvConstants.USER_FOLDER,false,-1);
					for(MailFolderBean tempFolder:userFolderList){
						folderNameKey = tempFolder.getFullName();
						folderDataElement = new String[5]; //name,name-utf8, totall, newmail ,unseen
						
						folderDataElement[FOLDER_NAME] = StringUtils.EscapeHTMLTag(folderNameKey);
						folderDataElement[FOLDER_NAME_UTF8] = URLEncoder.encode(folderNameKey, "UTF-8");
						folderDataElement[TOTAL] = 	Long.toString(tempFolder.getTotalCnt());
						folderDataElement[NEWMAIL] = Long.toString(tempFolder.getNewCnt());
						folderDataElement[UNSEEN] = Long.toString(tempFolder.getUnseenCnt());
						
						folderData.add(folderDataElement);	
					}
				}
			} else {			
				TMailFolder tempFolder = mailManager.getFolder(folder);
				folderNameKey = tempFolder.getFullName();
				folderDataElement = new String[5];			
				folderDataElement[FOLDER_NAME] = folderNameKey;
				folderDataElement[FOLDER_NAME_UTF8] = URLEncoder.encode(folderNameKey, "UTF-8");
				folderDataElement[TOTAL] = 	Long.toString(tempFolder.getMessageCount());
				folderDataElement[NEWMAIL] = Long.toString(tempFolder.getNewMessageCount());
				folderDataElement[UNSEEN] = Long.toString(tempFolder.getUnreadMessageCount());
				folderData.add(folderDataElement);	
			}
			
			folderNameKey = null;
			folderDataElement = null;
			
			if(useQuota.equals("on")){
				MailQuotaBean quota = mailManager.getQuotaRootInfo(FolderHandler.INBOX);			
				usage = quota.getUsage();
				limit = quota.getLimit();
				persent = quota.getPercent();
			}
			
			if(type!=null){
				outData = "";
				if(type.equals("basic") ){				
					for(int i=0;i<folderData.size();i++){
						String[] folderElement=folderData.get(i);					
						for(int j=0;j<usecaseStr.length;j++)
						{
							if(usecaseStr[j].equals( datacase[FOLDER_NAME]) ){
								outData += folderElement[FOLDER_NAME]+"|";	
							}
							if(usecaseStr[j].equals( datacase[FOLDER_NAME_UTF8]) ){
								outData += folderElement[FOLDER_NAME_UTF8]+"|";
							}
							if(usecaseStr[j].equals( datacase[TOTAL]) ){
								outData += folderElement[TOTAL]+"|";
							}
							if(usecaseStr[j].equals( datacase[NEWMAIL]) ){
								outData += folderElement[NEWMAIL]+"|";
							}
							if(usecaseStr[j].equals( datacase[UNSEEN]) ){
								outData += folderElement[UNSEEN]+"|";
							}
						}
						outData +="\n";
					}
					if(useQuota.equals("on")){
						outData += usage+"|"+limit+"|"+persent+"\n";
					}
					response.setHeader("Content-Type", "text/html; charset=UTF-8");
					PrintWriter wout = response.getWriter();
					wout.print(outData);	
					wout.close();
					outData = null;
								
				}else if(type.equals("xml")){				
					outData += "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
					outData += "<message-info>\n";
		
					for(int i=0;i<folderData.size();i++){
						String[] folderElement=folderData.get(i);
						outData += "<mailBox>\n";
						for(int j=0;j<usecaseStr.length;j++)
						{
							if(usecaseStr[j].equals( datacase[FOLDER_NAME]) ){
								outData += "<folderName>"+folderElement[FOLDER_NAME]+"</folderName>\n";	
							}
							if(usecaseStr[j].equals( datacase[FOLDER_NAME_UTF8]) ){
								outData += "<folderName-utf8>"+folderElement[FOLDER_NAME_UTF8]+"</folderName-utf8>\n";
							}
							if(usecaseStr[j].equals( datacase[TOTAL]) ){
								outData += "<totalCount>"+folderElement[TOTAL]+"</totalCount>\n";
							}
							if(usecaseStr[j].equals( datacase[NEWMAIL]) ){
								outData += "<newCount>"+folderElement[NEWMAIL]+"</newCount>\n";
							}
							if(usecaseStr[j].equals( datacase[UNSEEN]) ){
								outData += "<unseenCount>"+folderElement[UNSEEN]+"</unseenCount>\n";
							}
						}
						outData+="</mailBox>\n";
					}
					
					if(useQuota.equals("on")){
						outData += "<quota>\n";
						outData += "<quota-usage>"+usage+"</quota-usage>\n";
						outData += "<quota-limit>"+limit+"</quota-limit>\n";
						outData += "<quota-persent>"+persent+"</quota-persent>\n";
						outData += "</quota>\n";
					}
					outData += "</message-info>\n";
					response.setHeader("Content-Type", "application/xml; charset=UTF-8");
					PrintWriter wout = response.getWriter();
					wout.println(outData);	
					wout.close();
					outData = null;
					
				}else if(type.equals("design")){
					
					%>				
						<%@ include file="mailInfoDesign.jsp" %>
					<%
					
				}
			}else if(type==null){
				out.print("type no select");
			}
		} catch(Exception e){		
			//e.printStackTrace();
			System.out.println("-- ERR GET MESSAGE COUNT [" + id+ " ] : [" + e.getMessage() + "]");
		} finally{
			if(store !=null && store.isConnected())
				store.close();			
			defaultFolderList = null;
			userFolderList = null;
		}
	}catch(Exception e){
		System.out.println(" ERR GET INFO :["+e.getMessage()+"]");
	}
%>
<%-- newmail/unseen --%>
