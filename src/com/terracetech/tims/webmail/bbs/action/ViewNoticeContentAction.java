package com.terracetech.tims.webmail.bbs.action;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.bbs.exception.BoardContentNotFoundException;
import com.terracetech.tims.webmail.bbs.manager.BbsManager;
import com.terracetech.tims.webmail.bbs.vo.BoardContentVO;
import com.terracetech.tims.webmail.bbs.vo.BoardVO;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.MessageParserInfoBean;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;

public class ViewNoticeContentAction extends BaseAction {
	
	private static final long serialVersionUID = 5772732483691389306L;
	private BbsManager bbsManager = null;
	private MailUserManager mailUserManager = null;
	private final static String SYSTEM_ADMIN_UID = "mailadm";
	private BoardVO boardVo = null;
	private BoardContentVO contentVo = null;
	private int bbsId = 0;
	private int contentId = 0;
	private String viewType = null;
	private int bbsIndex = 0;
	
	public ViewNoticeContentAction(boolean authcheck) {		
		setAuthCheck(authcheck);		
	}
	
	public void setBbsManager(BbsManager bbsManager) {
		this.bbsManager = bbsManager;
	}

	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}

	public String execute() throws Exception {
		
		I18nResources resource = getMessageResource("bbs");
		
		String domain = mailUserManager.readDefaultDomain();
		int defaultDomainSeq = mailUserManager.searchMailDomainSeq(domain);
		
		List<BoardVO> noticeBbsList = bbsManager.readNoticeBbsList(defaultDomainSeq);
		
		boolean isNotice = false;
		for(int i = 0; i < noticeBbsList.size(); i++){
			BoardVO noticeBbsVO = noticeBbsList.get(i);
			
			if(noticeBbsVO.getBbsId() == bbsId){
				isNotice = true;
			}
		}
		
		if(!isNotice){
            request.setAttribute("errMsg", resource.getMessage("bbs.error.001"));
            return "alertback";
		}
		contentVo = bbsManager.readBoardContent(bbsId, contentId, defaultDomainSeq);
		
		if ("text".equals(contentVo.getContentType())) {
			
			String content = new String((contentVo.getContent()), "UTF-8");
			contentVo.setHtmlContent(content);
			
		} else {
			TMailStoreFactory factory = new TMailStoreFactory();
			Map<String, String> confMap = bbsManager.getBbsConnectionInfo(domain);
			TMailStore store = factory.connect(request.getRemoteAddr(), confMap);
			TMailFolder folder = store.getFolder(Integer.toString(bbsId));
			
			String attachesDir = context.getRealPath("/") + EnvConstants.getAttachSetting("attach.dir");
			String hostStr = request.getScheme() +"://"+ request.getServerName() +":"+ request.getServerPort();
			MessageParserInfoBean infoBean = new MessageParserInfoBean();
			infoBean.setAttachesDir(attachesDir);
			infoBean.setAttachesUrl(EnvConstants.getAttachSetting("attach.url"));
			infoBean.setDefaultCharset(EnvConstants.getMailSetting("default.charset"));
			infoBean.setDefaultImg("/design/common/images/blank.gif");
			infoBean.setHiddenImg(false);
			infoBean.setHiddenTag(false);
			infoBean.setLocale(new Locale(EnvConstants.getMailSetting("default.charset")));
			infoBean.setStrLocalhost(hostStr);
			infoBean.setUserId(SYSTEM_ADMIN_UID);
			
			try {
				contentVo = bbsManager.readBoardContent(folder, contentVo, infoBean);
			} catch (NumberFormatException e) {
				LogManager.writeErr(this, e.getMessage(), e);
				throw new BoardContentNotFoundException(resource.getMessage("bbs.content.notfound"));
			} catch (Exception e) {
				LogManager.writeErr(this, e.getMessage(), e);
			}finally {
				if (store != null && store.isConnected()) {
					store.close();
				}
			}
		}
		
		boardVo = bbsManager.readNoticeBoard(bbsId, defaultDomainSeq);
		bbsManager.modifyHitCount(bbsId, contentId, defaultDomainSeq);
		
		return "success";
	}

	public int getBbsId() {
		return bbsId;
	}

	public void setBbsId(int bbsId) {
		this.bbsId = bbsId;
	}

	public int getContentId() {
		return contentId;
	}

	public void setContentId(int contentId) {
		this.contentId = contentId;
	}

	public BoardVO getBoardVo() {
		return boardVo;
	}

	public BoardContentVO getContentVo() {
		return contentVo;
	}

	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	public int getBbsIndex() {
		return bbsIndex;
	}

	public void setBbsIndex(int bbsIndex) {
		this.bbsIndex = bbsIndex;
	}
}
