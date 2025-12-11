package com.terracetech.tims.webmail.webfolder.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import java.util.Map;

import jakarta.mail.Header;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.bouncycastle.util.encoders.Base64;

import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.dao.SystemConfigDao;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.UserInfo;
import com.terracetech.tims.webmail.mailuser.dao.MailUserDao;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;
import com.terracetech.tims.webmail.util.FileUtil;
import com.terracetech.tims.webmail.util.StringUtils;
import com.terracetech.tims.webmail.webfolder.vo.WebfolderQuotaVO;

/**
 * �����ڷ�� Mime, fileUpload ��ƿ
 */
public class WebFolderUtils {
    
    // Upload ���� ��ü
    private FileItem uploadFile = null;
    
    // �� �Ķ���� ó��
    private HashMap ht = new HashMap();
    
    // ���� ���ε� �⺻��
    private int yourMaxMemorySize = 10 * 1024 * 1024;	    	// 10 MB
    private int yourMaxRequestSize = 30 * 1024 * 1024 * 1024;   // 30 * 1024 MB
    private String encoding = "UTF-8";
    
    // �ӽõ��丮
    private File yourTempDirectory = new File(EnvConstants.getBasicSetting("tmpdir"));
    
    // ��������
    private String filename = null;
    private String filesize = null;
    private String fileext = null;
    
    // ������ ��� ũ�Ⱚ
    int FixHeaderSize = 1024;
    
    // HttpServletRequest request
    private HttpServletRequest request = null;
    private MultiPartRequestWrapper multiWrapper = null;	
    
    /**
     * Creates a new instance of FolderUtils
     */
    public WebFolderUtils() {
    }
    
    /**
     * Creates a new instance of FolderUtils
     * @param request ��Ƽ��Ʈ�� �Ѿ�� HttpServletRequest ��ü
     */
    public WebFolderUtils(HttpServletRequest request) {
    	this.request=request;
    }
    
    /**
     * Form ������ ���޹��� Param�� ���� Hashmap ��ü ����
     * @param request ��Ƽ��Ʈ�� �Ѿ�� HttpServletRequest ��ü
     * @return Form ������ ���޹��� Param�� ���� Hashmap ��ü
     */
    public void processParam(HttpServletRequest request) {
	
		if(request instanceof MultiPartRequestWrapper) {
			multiWrapper = (MultiPartRequestWrapper) request;
			System.out.println("MultiPartRequestWrapper~!!");
		}
		File attFile = multiWrapper.getFiles("theFile")[0];
		long attFileSize = attFile.length();
		System.out.println("attFileSize:"+attFileSize);
		String attFileName = multiWrapper.getFileNames("theFile")[0];
		
		// Process the uploaded items	
		Iterator itr = 	multiWrapper.getParameterMap().keySet().iterator();
		String field,fieldValue;
		while(itr.hasNext()) {			
			field = (String)itr.next();
			fieldValue = multiWrapper.getParameter(field);
			ht.put(field, fieldValue);		
		}
    }    
    
    /**
     * Apache Common FileUpload �� �̿��� ���� ���ε��� FileItem ��ü ����
     * @param request ��Ƽ��Ʈ�� �Ѿ�� HttpServletRequest ��ü
     * @return ���ε�� ���������� ��� �ִ� FileItem ��ü
     */
    public FileItem uploadFile(HttpServletRequest request) {
	
	// Create a factory for disk-based file items
	DiskFileItemFactory factory = new DiskFileItemFactory();
	
	// Set factory constraints
	factory.setSizeThreshold(yourMaxMemorySize);
	factory.setRepository(yourTempDirectory);
	
	// Create a new file upload handler
	ServletFileUpload upload = new ServletFileUpload(factory);
	upload.setHeaderEncoding(encoding);
	
	// Set overall request size constraint
	upload.setSizeMax(yourMaxRequestSize);
	List items = null;
	try {
	    items = upload.parseRequest(request);
	} catch (FileUploadException ex) {	    
	    LogManager.writeErr(this, ex.getMessage(), ex);
	}
	
	// Process the uploaded items
	Iterator iter = items.iterator();
	while (iter.hasNext()) {
	    FileItem item = (FileItem) iter.next();
	    try {
		if (item.isFormField()) {
		    // Check Request Parameter
		    String field = item.getFieldName();
		    String fieldValue = item.getString(encoding);
		    ht.put(field, fieldValue);
		} else {
		    // Upload File
		    filename = getBaseFileName(item.getName());
		    filesize = String.valueOf(item.getSize());
		    int backslashIndex = filename.lastIndexOf(".");
		    fileext =  filename.substring(backslashIndex + 1);
		    uploadFile = item;
		}
	    } catch (UnsupportedEncodingException ex) {
	    	LogManager.writeErr(this, ex.getMessage(), ex);
	    }
	}
	return uploadFile;
    }
    
    /**
     * ��ο��� ���ϸ��� ����
     * @param filePath ��ü ���� ��� (full path)
     * @return ���ϸ�
     */
    public String getBaseFileName(String filePath) {
	// First, ask the JDK for the base file name.
	String fileName = new File(filePath).getName();

	// Now check for a Windows file name parsed incorrectly.
	int colonIndex = fileName.indexOf(":");
	if (colonIndex == -1) {
	    // Check for a Windows SMB file path.
	    colonIndex = fileName.indexOf("\\\\");
	}
	int backslashIndex = fileName.lastIndexOf("\\");
	
	if (colonIndex > -1 && backslashIndex > -1) {
	    // Consider this filename to be a full Windows path, and parse it
	    // accordingly to retrieve just the base file name.
	    fileName = fileName.substring(backslashIndex + 1);
	}
	return fileName;
    }
    
    /**
     * �Ķ���Ͱ� ����
     * @param key ������ �Ķ���͸�
     * @return ������ �Ķ���Ͱ�
     */
    public String getParamValue(String key) {
	return (String)this.ht.get(key);
    }
    
    /**
     * ���ϸ� ����
     * @return ���ε�� ���ϸ�
     */
    public String getFilename() {
	return this.filename;
    }

    /**
     * ���ϸ� ����
     */
    public void setFilename(String filename) {
	this.filename = filename;
    }
    
    /**
     * ����ũ�� ����
     * @return ���ε�� ����ũ��
     */
    public String getFilesize() {
	return this.filesize;
    }
    
    /**
     * ����ũ�� ����
     */
    public void setFilesize(String filesize) {
	this.filesize=filesize;
    }
    
    /**
     * ����Ȯ���� ����
     * @return ���ε�� ���� Ȯ����
     */
    public String getFileext() {
	return this.fileext;
    }
    
    /**
     * ����Ȯ���� ����
     */
    public void setFileext(String fileext) {
	this.fileext=fileext;
    }

    /**
     * �ʿ� ����� �Է�
     * @param msg �Է��� MimeMessage
     * @param email ������ �̸���
     * @return �Է°��� �Էµ� MimeMessage ��ü
     */
    public MimeMessage makeHeaderAll(MimeMessage msg, String email) {
	try {
	    // �ʿ� ����� ����
	    msg = this.makeHeader(msg, this.filename, email, this.filesize, this.fileext);
	    // Message-ID �� ���ϱ� (MessageID �� �����Ѵ�)
	    msg.saveChanges(); // this.makeMessageID(msg, "/tmp/");
	    // ��� ũ�⸦ ī��Ʈ�ؼ� ���� �κ��� X �� ä���ش�
	    msg = this.makeHeaderSizeFix(msg, this.getHeaderSize(msg), FixHeaderSize);
	} catch (MessagingException ex) {
		LogManager.writeErr(this, ex.getMessage(), ex);
	}
	return msg;
    }
    
    /**
     * �ʿ� ����� �Է�
     * @param msg �Է��� MimeMessage
     * @param filename ���ϸ�
     * @param email ������ �̸���
     * @param filesize ���� ũ��
     * @param fileext ���� Ȯ����
     * @return �Է°��� �Էµ� MimeMessage ��ü
     */
    public MimeMessage makeHeader(MimeMessage msg, String filename, String email, String filesize, String fileext) {
	try {					
	    msg.setSubject(filename,"UTF-8");
	    filename = this.getEncodeFilename(msg);
	    msg.addHeader("From", email);
	    msg.addHeader("To", filesize);
	    msg.addHeader("Cc", fileext);
	    msg.addHeader("X-DATAFORMAT-VERSION","1.0");
	    msg.setSentDate(new Date());
	    msg.addHeader("Content-Type", "application/octet-stream;");
	    msg.addHeader("Content-Transfer-Encoding","binary");
	    msg.addHeader("Mime-Version", "1.0");
	} catch (MessagingException e) {	    	    
	    LogManager.writeErr(this, e.getMessage(), e);
	}
	return msg;
    }
    
    /**
     * ���ϸ��� encode String ���ϱ�
     * @param msg �Է��� MimeMessage
     * @return �⺻ ĳ���Ͱ����� encoding �� ���ϸ�
     */
    public String getEncodeFilename(MimeMessage msg) {
	String encodeFilename = null;
	try {
	    encodeFilename = msg.getHeader("Subject")[0];
	} catch (MessagingException e) {
		LogManager.writeErr(this, e.getMessage(), e);
	}
	return encodeFilename;
    }
    
    /**
     * MimeMessage �� ��� ũ�⸦ ����
     * @param msg ��� ũ�⸦ ���� MimeMessage
     * @return MimeMessage �� ��� ũ��
     */
    public int getHeaderSize(MimeMessage msg) {
	Enumeration enumAllHeaders = null;
	Header hd = null;
	String headerStr = null;
	int headerTot = 0;
	int headerLen = 0;
	int loopcnt = 0;
	try {
	    enumAllHeaders = msg.getAllHeaders();
	    while (enumAllHeaders.hasMoreElements()) {
		hd = (javax.mail.Header)enumAllHeaders.nextElement();
		headerStr = hd.getName()+": "+hd.getValue();
		headerLen = headerStr.length()+2;
		headerTot = headerTot + headerLen;
		loopcnt++;
		//System.out.println("::: mime header ["+loopcnt+"] : "+headerStr+ " ("+headerLen+")("+headerTot+")");
	    }
	} catch (MessagingException e) {
		LogManager.writeErr(this, e.getMessage(), e);
	}
	return headerTot;
    }
    
    /**
     * X-SIZE-DUMMAY ����� �� �߰��Ͽ�, ���ذ����� ��� ũ�⸦ ����
     * @param msg �Է��� MimeMessage
     * @param headerLength ���� ��� ũ��
     * @param maxHeaderSize ������ ��� ũ��
     * @return ��� ũ�Ⱑ ������ MimeMessage
     */
    public MimeMessage makeHeaderSizeFix(MimeMessage msg, int headerLength, int maxHeaderSize) {
	String xSizeDummyStr = "X-SIZE-DUMMY";
	int xSizeDummyLen = xSizeDummyStr.length()+4;  // ": "+ \r\n
	
	try {
	    String dummyHeader = "";
	    for (int i = 0; i < (maxHeaderSize - headerLength - xSizeDummyLen); i++) {
		dummyHeader = dummyHeader + "X";
	    }
	    msg.addHeader(xSizeDummyStr, dummyHeader);
	} catch (MessagingException e) {
		LogManager.writeErr(this, e.getMessage(), e);
	}
	return msg;
    }
    
    /**
     * �ӽ����ϰ�ο� �ӽ� ������ ���� �� MessageID ����
     * @param msg �Է��� MimeMessage
     * @param tmpPath �ӽ� ������ ������ ���丮 ���
     * @return �Էµ� MimeMessage �� MessageID
     */
    public File makeMessageID(MimeMessage msg, String tmpPath) {
	
	String tmp = String.valueOf(Math.random());
	tmp = tmp.substring(2, tmp.length());
	File file = new File(tmpPath+"/"+tmp+".tmp");
	if (msg != null) {
	    try {
		OutputStream os = new FileOutputStream(file);
		msg.writeTo(os);
	    } catch (MessagingException e) {
	    	LogManager.writeErr(this, e.getMessage(), e);
	    } catch (IOException ie) {
	    	LogManager.writeErr(this, ie.getMessage(), ie);
	    }
	}
	return file;
    }
    
    /**
     * ������ ���� ��ο� ������ Mime �� ����
     * @param msg ����� MimeMessage
     * @param filePath Mime ������ ������ ���
     */
    public void saveMime(MimeMessage msg, String filePath) {
	File file = new File(filePath);
	if (msg != null) {
	    try {
		OutputStream os = new FileOutputStream(file);
		msg.writeTo(os);
	    } catch (MessagingException e) {
	    	LogManager.writeErr(this, e.getMessage(), e);
	    } catch (IOException ie) {
	    	LogManager.writeErr(this, ie.getMessage(), ie);
	    }
	}
    }
    
    
    
    
    /////////////////////////// WebFolder ���� ��ƿ ///////////////////////////
    /**
     * �Է¹��� Ȯ���ڷ� �̹��� �� ��¸� ����
     * @param extStr ���� Ȯ����
     * @param isName true : ���������� ����, false : �̹��� ��� ����
     * @return ���������� or �̹��� ���
     */
    public String getWebFolderFileExtNameOrIMG(String extStr, boolean isName) {
		if ("hwp".endsWith(extStr.toLowerCase())) {
		    if (isName) {
			extStr = "Hangul Doc";
		    } else {
			extStr = "<img src='/design/default/image/icon/icon_file_hwp.gif'>";
		    }
		} else if ("xls".endsWith(extStr.toLowerCase()) || "xlsx".endsWith(extStr.toLowerCase())) {
		    if (isName) {
			extStr = "MS Excel";
		    } else {
			extStr = "<img src='/design/default/image/icon/icon_file_xls.gif'>";
		    }
		} else if ("doc".endsWith(extStr.toLowerCase()) || "docx".endsWith(extStr.toLowerCase())) {
		    if (isName) {
			extStr = "MS Word";
		    } else {
			extStr = "<img src='/design/default/image/icon/icon_file_doc.gif'>";
		    }
		} else if ("ppt".endsWith(extStr.toLowerCase()) || "pptx".endsWith(extStr.toLowerCase())) {
		    if (isName) {
			extStr = "MS PowerPoint";
		    } else {
			extStr = "<img src='/design/default/image/icon/icon_file_ppt.gif'>";
		    }
		} else if ("gif".endsWith(extStr.toLowerCase())) {
		    if (isName) {
			extStr = "GIF Image";
		    } else {
			extStr = "<img src='/design/default/image/icon/icon_file_gif.gif'>";
		    }
		} else if ("jpg".endsWith(extStr.toLowerCase()) || "jpge".endsWith(extStr.toLowerCase())) {
		    if (isName) {
			extStr = "JPG Image";
		    } else {
			extStr = "<img src='/design/default/image/icon/icon_file_jpg.gif'>";
		    }
		} else if ("txt".endsWith(extStr.toLowerCase())) {
		    if (isName) {
			extStr = "Text";
		    } else {
			extStr = "<img src='/design/default/image/icon/icon_file_txt.gif'>";
		    }
		} else if ("html".endsWith(extStr.toLowerCase()) || "htm".endsWith(extStr.toLowerCase())) {
		    if (isName) {
			extStr = "HTML";
		    } else {
			extStr = "<img src='/design/default/image/icon/icon_file_html.gif'>";
		    }
		} else if ("pdf".endsWith(extStr.toLowerCase())) {
		    if (isName) {
			extStr = "PDF";
		    } else {
			extStr = "<img src='/design/default/image/icon/icon_file_pdf.gif'>";
		    }
		}else if ("zip".endsWith(extStr.toLowerCase())) {
		    if (isName) {
			extStr = "ZIP";
		    } else {
			extStr = "<img src='/design/default/image/icon/icon_file_zip.gif'>";
		    }
		} 
		else {
		    if (isName) {
			extStr = "unknown";
		    } else {
		    	extStr = "<img src='/design/default/image/icon/icon_file_unknown.gif'>";
		    }
		}
		return extStr;
    }

    
    public static Map<String, String> getWebfolderConf(String type, String userSeq, String domain, User user) throws Exception{
    	
    	Map<String, String> confMap = null;
    	
    	if ("user".equals(type)){
    		confMap = getWebfolderMyConnectInfo(user);
    	} else if ("share".equals(type)) {
    		confMap = getWebfolderShareConnectInfo(Integer.parseInt(userSeq));
    	} else if ("public".equals(type)) {
    		confMap = getWebfolderPublicConnectInfo(domain);
    	}
    	
    	return confMap;
    }
    
    public static Map<String, String> getWebfolderShareConnectInfo(int userSeq) {
		Map<String, String> confMap = new HashMap<String, String>();
		MailUserDao mailUserDao = (MailUserDao)ApplicationBeanUtil.getApplicationBean("mailUserDao");
		SystemConfigDao systemConfigDao = (SystemConfigDao)ApplicationBeanUtil.getApplicationBean("systemConfigDao");
		WebfolderQuotaVO webfolderQuotaVo = mailUserDao.readWebfolderInfo(userSeq);
		long webfodlerQuota = WebFolderUtils.calculQuota(EnvConstants.WEBFOLDER_QUOTA_SIZE, webfolderQuotaVo.getWebfolderQuota(), webfolderQuotaVo.getWebfolderAddQuota());
		String path = webfolderQuotaVo.getMessageStore();
		String webfolderHome = EnvConstants.getBasicSetting("webfolder.home");
		StringBuffer args = new StringBuffer();
		args.append(path);
		args.append(webfolderHome).append(" ");
		args.append(webfodlerQuota / FileUtil.SIZE_MEGA).append(" ");
		args.append(webfodlerQuota % FileUtil.SIZE_MEGA).append(" ");
		args.append("100000 0 90 50").append(" ");
		args.append(webfolderQuotaVo.getMailUserSeq()).append(" ");
		args.append(webfolderQuotaVo.getMailDomainSeq()).append(" ");	
		
		confMap.put(User.MAIL_UID, webfolderQuotaVo.getMailUid());
		confMap.put(User.MAIL_DOMAIN, webfolderQuotaVo.getMailDomain());
		confMap.put(User.EMAIL, webfolderQuotaVo.getMailUid()+"@"+webfolderQuotaVo.getMailDomain());
		confMap.put(User.MAIL_HOST, systemConfigDao.getHostName(webfolderQuotaVo.getMailHost()));
		confMap.put(User.IMAP_LOGIN_ARGS, args.toString());
		
		return confMap;
	}
	
	public static Map<String, String> getWebfolderMyConnectInfo(User user) {
		Map<String, String> confMap = new HashMap<String, String>();
		
		confMap.put(User.MAIL_UID, user.get(User.MAIL_UID));
		confMap.put(User.MAIL_DOMAIN, user.get(User.MAIL_DOMAIN));
		confMap.put(User.EMAIL, user.get(User.MAIL_UID)+"@"+user.get(User.MAIL_DOMAIN));
		confMap.put(User.MAIL_HOST, user.get(User.MAIL_HOST));
		confMap.put(User.IMAP_LOGIN_ARGS, user.get(User.WEBFOLDER_LOGIN_ARGS));		
		
		return confMap;
	}
	
	public static Map<String, String> getWebfolderPublicConnectInfo(String domain) {
		Map<String, String> confMap = new HashMap<String, String>();
		String adminId = EnvConstants.getUtilSetting("webfolder.admin");
		MailUserDao mailUserDao = (MailUserDao)ApplicationBeanUtil.getApplicationBean("mailUserDao");
		SystemConfigDao systemConfigDao = (SystemConfigDao)ApplicationBeanUtil.getApplicationBean("systemConfigDao");
		Map<String, Object> adminMap = mailUserDao.readMailUserAuthInfo(adminId, domain);
		if (adminMap == null || adminMap.isEmpty()) {
			return null;
		}	
		UserInfo userInfo = new UserInfo();
		userInfo.setUserInfoMap(adminMap);
		int userSeq = Integer.parseInt(userInfo.get(User.MAIL_USER_SEQ));
			
		WebfolderQuotaVO webfolderQuotaVo = mailUserDao.readWebfolderInfo(userSeq);
		long webfodlerQuota = WebFolderUtils.calculQuota(EnvConstants.WEBFOLDER_QUOTA_SIZE, webfolderQuotaVo.getWebfolderQuota(), webfolderQuotaVo.getWebfolderAddQuota());
		String path = webfolderQuotaVo.getMessageStore();
		String webfolderHome = EnvConstants.getBasicSetting("webfolder.home");
		StringBuffer args = new StringBuffer();
		args.append(path);
		args.append(webfolderHome).append(" ");
		args.append(webfodlerQuota / FileUtil.SIZE_MEGA).append(" ");
		args.append(webfodlerQuota % FileUtil.SIZE_MEGA).append(" ");
		args.append("100000 0 90 50").append(" ");
		args.append(webfolderQuotaVo.getMailUserSeq()).append(" ");
		args.append(webfolderQuotaVo.getMailDomainSeq());		
		
		
		confMap.put(User.MAIL_UID, webfolderQuotaVo.getMailUid());
		confMap.put(User.MAIL_DOMAIN, webfolderQuotaVo.getMailDomain());
		confMap.put(User.EMAIL, webfolderQuotaVo.getMailUid()+"@"+webfolderQuotaVo.getMailDomain());
		confMap.put(User.MAIL_HOST, systemConfigDao.getHostName(webfolderQuotaVo.getMailHost()));
		confMap.put(User.IMAP_LOGIN_ARGS, args.toString());
		
		return confMap;
	}
  
    public static long calculQuota (long defaultValue, String baseValue, String addValue) {
    	long returnValue = defaultValue;
    	
		if (StringUtils.isNotEmpty(baseValue)) {
			returnValue = Long.parseLong(baseValue);
		}	
		if (StringUtils.isNotEmpty(addValue)) {
			returnValue += Long.parseLong(addValue);
		}
		
		return returnValue;
	}
    
    public static String base64Decode(String str) throws Exception {
    	str = str.replaceAll(" ", "+");
		byte[] strByte = Base64.decode(str);	
		return new String(strByte, "utf-8");
    }
}
