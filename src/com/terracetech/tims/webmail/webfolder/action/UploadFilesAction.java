package com.terracetech.tims.webmail.webfolder.action;

import java.io.File;
import java.util.Map;

import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.VirusManager;
import com.terracetech.tims.webmail.common.vo.VirusCheckVO;
import com.terracetech.tims.webmail.exception.VirusCheckException;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.StringUtils;
import com.terracetech.tims.webmail.webfolder.manager.WebFolderUtils;
import com.terracetech.tims.webmail.webfolder.manager.WebfolderManager;

public class UploadFilesAction extends BaseAction {

	private WebfolderManager webfolderManager = null;
	private VirusManager virusManager = null;
	private int userSeq = 0;
	private String path = null;
	private String type = null;
	private String nodeNum = null;
	private String sroot = null;
	private String uploaderType = null;

	public void setWebfolderManager(WebfolderManager webfolderManager) {
		this.webfolderManager = webfolderManager;
	}

	public void setVirusManager(VirusManager virusManager) {
		this.virusManager = virusManager;
	}

	@Override
	public String execute() throws Exception {

		WebFolderUtils folderUtils = new WebFolderUtils();
		TMailStore store = null;
		TMailFolder folder = null;
		Map<String, String> confMap = null;
		TMailStoreFactory factory = new TMailStoreFactory();

		String tmpDir = EnvConstants.getBasicSetting("tmpdir");
		String id = user.get(User.MAIL_UID);
		String domain = user.get(User.MAIL_DOMAIN);
		String email = user.get(User.EMAIL);
		int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));

		I18nResources resource = getMessageResource("webfolder");
		I18nResources commonResource = getMessageResource("common");
		boolean isError = false;
		String msg = "";

		try {
			String attlists = request.getParameter("attlists");
			String folderType = "";
			if (StringUtils.isNotEmpty(attlists)) {
				String host = EnvConstants.getVirusSetting("virus.server");
				String portStr = EnvConstants.getVirusSetting("virus.server.port");
				int port = StringUtils.isNotEmpty(portStr) ? Integer.parseInt(portStr) : 17777;
				VirusCheckVO checkVO = virusManager.checkVirus(host, port, attlists, commonResource);
				if (!checkVO.isSuccess()) {
					msg = checkVO.getCheckResultMsg();
					throw new VirusCheckException();
				}
			}

			if(type.equals("share")){
				String encodeSroot = sroot.replaceAll(EnvConstants.getBasicSetting("webfolder.root")+"\\.", "");
				encodeSroot = StringUtils.IMAPFolderEncode(encodeSroot);

				if (!webfolderManager.vaildateShareFolder(encodeSroot, mailDomainSeq, userSeq, mailUserSeq)) {
					request.setAttribute("msg", resource.getMessage("share.alert.001"));
					request.setAttribute("path", "/");
					request.setAttribute("type", "user");
					request.setAttribute("nodeNum", "0|");
					return "result";
				}
				confMap = webfolderManager.getWebfolderShareConnectInfo(userSeq);
				folderType = "S";
			}
			else if ("public".equals(type)) {
				confMap = webfolderManager.getWebfolderPublicConnectInfo(user.get(User.MAIL_DOMAIN));
				folderType = "P";
			}
			else {
				confMap = webfolderManager.getWebfolderMyConnectInfo(user);
			}

			boolean isRoot = path.equals("/");

			store = factory.connect(request.getRemoteAddr(), confMap);

			if (isRoot) {
				folder = store.getDefaultWebFolder();
			}
			else {
			    char separator = '.';
			    String xpath = path.substring(1); // remove the leading '/'
			    xpath = xpath.replaceAll("/", "" + separator);
			    xpath = StringUtils.IMAPFolderEncode(xpath);
			    folder = store.getWebFolder(xpath);
			}

			// ���޹��� ÷������ ����Ʈ���� ���ϸ�, ���ε�� ��θ� �����Ѵ�.

			String[] attlistss = attlists.split("\\n", 0);
			String[] tmpFileStr = null;
			File uploadFile = null;
			String fileName = null;

			// ���ε�� ���� ���� ��ŭ Mime ������ Append
			for (int i = 0; i < attlistss.length; i++) {
			    tmpFileStr = attlistss[i].split("\\t");

			    // ���� ����
			    MimeMessage message = new MimeMessage(factory.getSession());

			    // upload �� file stream �� binary �� mime ������ ����.
			    uploadFile = new File(tmpDir + EnvConstants.DIR_SEPARATOR + tmpFileStr[0]);
			    FileDataSource fds = new FileDataSource(uploadFile);
			    DataHandler dh = new DataHandler(fds);
			    message.setDataHandler(dh);

			    // �ʿ� ����� ����
			    email = id+"@"+domain;

			    fileName = tmpFileStr[1];

			    folderUtils.setFilename(fileName);
			    folderUtils.setFilesize(tmpFileStr[2]);
			    folderUtils.setFileext(fileName.substring(fileName.lastIndexOf(".") + 1));
			    message = folderUtils.makeHeaderAll(message, email);
			    try {
					folder.appendMessagesBinary(new TMailMessage[]{new TMailMessage(message)});
					writeWebfolderLog(true, "wfolder_upload_file", folderType+folder.getFullName(), "", "", "", Long.parseLong(tmpFileStr[2]), message.getSubject(), "");
				}
			    catch (MessagingException e) {
			    	LogManager.writeErr(this, e.getMessage(), e);
					String em = e.getMessage();
					if (em.indexOf("exceeded") != -1 && em.indexOf("quota") != -1) {
					    request.setAttribute("msg", resource.getMessage("error.fullquota"));
					} else {
						request.setAttribute("msg", resource.getMessage("error.upload.fail"));
					}
					request.setAttribute("path", path);
				    request.setAttribute("userSeq", userSeq);
				    request.setAttribute("type", type);
				    request.setAttribute("nodeNum", nodeNum);
				    request.setAttribute("sroot", sroot);
					return "result";
				}
			    finally {
			    	uploadFile = null;
					if (store != null && store.isConnected()) store.close();
			    }
			}
		}catch (VirusCheckException e) {
			request.setAttribute("msg", msg);
			request.setAttribute("path", path);
		    request.setAttribute("userSeq", userSeq);
		    request.setAttribute("type", type);
		    request.setAttribute("nodeNum", nodeNum);
		    request.setAttribute("sroot", sroot);
			return "result";
		}
		catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			isError = true;
		}
		request.setAttribute("path", path);
	    request.setAttribute("userSeq", userSeq);
	    request.setAttribute("type", type);
	    request.setAttribute("nodeNum", nodeNum);
	    request.setAttribute("sroot", sroot);

	    if (isError) {
	    	return "subError2";
	    }

	    return "result";
	}

	public int getUserSeq() {
		return userSeq;
	}

	public void setUserSeq(int userSeq) {
		this.userSeq = userSeq;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNodeNum() {
		return nodeNum;
	}

	public void setNodeNum(String nodeNum) {
		this.nodeNum = nodeNum;
	}

	public String getSroot() {
		return sroot;
	}

	public void setSroot(String sroot) {
		this.sroot = sroot;
	}

	public void setUploaderType(String uploaderType) {
		this.uploaderType = uploaderType;
	}
}
