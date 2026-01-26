package com.terracetech.tims.hybrid.mail.action;

import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.hybrid.common.HybridErrorCode;
import com.terracetech.tims.hybrid.common.action.BaseAction;
import com.terracetech.tims.mail.TMailAddress;
import com.terracetech.tims.service.manager.IMailServiceManager;
import com.terracetech.tims.service.tms.vo.SendCondVO;
import com.terracetech.tims.service.tms.vo.SendResultVO;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.MailAddressBean;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.SearchEmailManager;
import com.terracetech.tims.webmail.util.HangulEmail;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringReplaceUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class MailSendServiceAction extends BaseAction {
    
    private String signSeq;
    private String sendType;
    private String to;
    private String cc;
    private String bcc;
    private String subject;
    private String encode;
    private String receivnoti;
    private String onesend;
    private String savesent;
    private String signUse;
    private String content;
    private String senderEmail;
    private String senderName;
    private String attachList;
    private String sendFlag;
    private String uid;
    private String folder;
    private String draftMid;
    
    private IMailServiceManager mailServiceManager = null;
    private SearchEmailManager searchEmailManager = null;
    
    public void setMailServiceManager(IMailServiceManager mailServiceManager) {
        this.mailServiceManager = mailServiceManager;
    }
    
    public void setSearchEmailManager(SearchEmailManager searchEmailManager) {
		this.searchEmailManager = searchEmailManager;
	}

	@SuppressWarnings("unchecked")
    public String execute() throws Exception {
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		String domain = user.get(User.MAIL_DOMAIN);
        int errorCode = HybridErrorCode.SUCCESS;
        JSONObject result = new JSONObject();
        try {
            String readMailNotiMode = readMailNotiMode();
            
            SendCondVO sendVO = new SendCondVO();

            sendVO.setSendType(sendType);
            sendVO.setLocale(user.get(User.LOCALE));

            sendVO.setToAddr(this.getAddrArray(this.getCheckEmailAddress(mailUserSeq, mailDomainSeq, to, domain)));
            sendVO.setCcAddr(this.getAddrArray(this.getCheckEmailAddress(mailUserSeq, mailDomainSeq, cc, domain)));
            sendVO.setBccAddr(this.getAddrArray(this.getCheckEmailAddress(mailUserSeq, mailDomainSeq, bcc, domain)));
            sendVO.setSubject(subject);
            sendVO.setEncode(encode);

            sendVO.setReceivnoti("on".equalsIgnoreCase(receivnoti));
            sendVO.setOnesend("on".equalsIgnoreCase(onesend));
            sendVO.setSavesent("on".equalsIgnoreCase(savesent));
            sendVO.setSignUse("on".equalsIgnoreCase(signUse));
            sendVO.setSignSeq((signSeq != null) ? Integer.parseInt(signSeq) : -1);

            if ("link".equalsIgnoreCase(readMailNotiMode) && sendVO.isReceivnoti() && !"draft".equalsIgnoreCase(sendType)) {
                StringReplaceUtil replace = new StringReplaceUtil();
                sendVO.setContent(replace.getTextToHTML(content));
                sendVO.setEditMode("html");
            } else {
                sendVO.setEditMode("text");
                sendVO.setContent(content);
            }
            
            sendVO.setSenderEmail(senderEmail);
            sendVO.setSenderName(senderName);
            sendVO.setAttachListStr(attachList);
            sendVO.setSendFlag(sendFlag);

            String remoteIP = request.getRemoteAddr();
            sendVO.setRemoteIp(remoteIP);
            String port = EnvConstants.getBasicSetting("web.port");
            port = (port != null && port.length() > 0) ? port : Integer.toString(request.getServerPort());
            String mdnHost = EnvConstants.getMailSetting("mdn.host");
            String localhost = request.getScheme() + "://" + request.getServerName() + ":" + port;
            mdnHost = (mdnHost != null) ? mdnHost : localhost;
            sendVO.setMdnHost(mdnHost);
            sendVO.setLocalhost(localhost);
            sendVO.setUid(uid);
            sendVO.setFolder(folder);
            sendVO.setDraftMid(draftMid);
            sendVO.setNotiMode(readMailNotiMode);
            SendResultVO sendResultVO = mailServiceManager.doSimpleMailSend(sendVO, user);
            
            result.put("sendResult", convertSendResult(sendResultVO));

            
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            errorCode = HybridErrorCode.ERROR;
        }
        
        result.put(HybridErrorCode.CODE_NAME, errorCode);
        String returnName = request.getParameter("sendprocess");
        ResponseUtil.makeAjaxMessage(response, returnName+"("+result+")");
        return null;
    }

    public void setSignSeq(String signSeq) {
        this.signSeq = signSeq;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public void setReceivnoti(String receivnoti) {
        this.receivnoti = receivnoti;
    }

    public void setOnesend(String onesend) {
        this.onesend = onesend;
    }

    public void setSavesent(String savesent) {
        this.savesent = savesent;
    }

    public void setSignUse(String signUse) {
        this.signUse = signUse;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setAttachList(String attachList) {
        this.attachList = attachList;
    }

    public void setSendFlag(String sendFlag) {
        this.sendFlag = sendFlag;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public void setDraftMid(String draftMid) {
        this.draftMid = draftMid;
    }

    @SuppressWarnings("unchecked")
    private JSONObject convertSendResult(SendResultVO sendResultVO) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("errorOccur", sendResultVO.isErrorOccur());
        jsonObject.put("checkErrorOccur", sendResultVO.isCheckErrorOccur());
        jsonObject.put("sendType", sendResultVO.getSendType());
        jsonObject.put("messageId", sendResultVO.getMessageId());
        jsonObject.put("sendAddress", makeAddressJson(sendResultVO.getSendArrayAddrs()));
        jsonObject.put("invalidAddress", makeAddressJson(sendResultVO.getInvalidArrayAddrs()));
        
        return jsonObject;
    }
    
    @SuppressWarnings("unchecked")
    private JSONArray makeAddressJson(String[] addressList) {
        JSONArray array = new JSONArray();
        if (addressList != null && addressList.length >= 0) {
            for (String addr : addressList) {
                array.add(StringUtils.EscapeHTMLTag(addr));
            }
        }
        return array;
    }

    private String[] getAddrArray(String addr) {
        String[] addrArray = null;
        if (addr != null && !addr.trim().equals("")) {
            addrArray = addr.split("[;,\r\n]");
        }
        return addrArray;
    }
    
    private String getCheckEmailAddress(int userSeq, int domainSeq, String addr, String domain) {
        if (addr == null || addr.length() <= 0) {
            return null;
        }

        String[] addrs = addr.split("[;,\r\n]");
        String addrstr = "";
        String addrTrim = null;
        String searchToken = null;
        List<MailAddressBean> addrList = null;
        String tempAddrStr = null;

        for (int i = 0; i < addrs.length; i++) {
            addrTrim = TMailAddress.getTrimAddress(addrs[i]);

            if (addrTrim.indexOf("@") < 0) {
                addrTrim = TMailAddress.getEmailAddress(addrTrim);
            }

            if (addrTrim.length() == 0) {
                continue;
            } else if (TMailAddress.isPersonalString(addrTrim)) {
                addrstr += addrTrim + " ";
                continue;
            } else if (addrTrim.charAt(0) == '&' && addrTrim.indexOf("@") < 0) {
                searchToken = addrTrim.substring(1);
                addrList = searchEmailManager.readSharedAddrEmailList(userSeq, searchToken);
                tempAddrStr = getMailAddressStr(addrList);
                if (tempAddrStr.length() > 0) {
                    addrstr += tempAddrStr;
                } else {
                    continue;
                }
            } else if (addrTrim.charAt(0) == '$' && addrTrim.indexOf("@") < 0) {
                searchToken = addrTrim.substring(1);
                addrList = searchEmailManager.readPrivateAddrEmailList(userSeq, searchToken);
                tempAddrStr = getMailAddressStr(addrList);
                if (tempAddrStr.length() > 0) {
                    addrstr += tempAddrStr;
                } else {
                    continue;
                }
            } else if (addrTrim.indexOf("#") == 0 && addrTrim.indexOf("@") < 0) {
                searchToken = addrTrim.substring(1);

                String[] tokens = searchToken.split("\\.");

                if (tokens.length > 3) {
                    String orgCode = tokens[0];
                    String codeType = tokens[1];
                    String code = tokens[2];
                    boolean isSearchHierarchy = Boolean.parseBoolean(tokens[3]);

                    addrList = searchEmailManager
                            .readAddressList(domainSeq, orgCode, codeType, code, isSearchHierarchy);
                    tempAddrStr = getMailAddressStr(addrList);
                    if (tempAddrStr.length() > 0) {
                        addrstr += tempAddrStr;
                    } else {
                        continue;
                    }
                } else if (tokens.length == 2) {
                    String orgName = tokens[0];
                    String orgCode = tokens[1];

                    addrList = searchEmailManager.readDeptAddressList(domainSeq, orgName, orgCode);

                    tempAddrStr = getMailAddressStr(addrList);
                    if (tempAddrStr.length() > 0) {
                        addrstr += tempAddrStr;
                    } else {
                        continue;
                    }
                } else if (tokens.length == 1) {
                    String orgName = tokens[0];

                    addrList = searchEmailManager.readDeptAddressList2(domainSeq, orgName);

                    tempAddrStr = getMailAddressStr(addrList);
                    if (tempAddrStr.length() > 0) {
                        addrstr += tempAddrStr;
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            } else if (HangulEmail.isHangulEmail(addrTrim)) {
                String s = HangulEmail.getHangulEmail(addrTrim);

                if (s.length() <= 0)
                    continue;

                addrstr += s;
            } else if (addrTrim.indexOf("@") < 0) {
                addrstr += addrTrim + "@" + domain;
            } else {
                addrstr += addrTrim;
            }

            if ((i + 1) != addrs.length) {
                addrstr += ",";
            }
        }

        String[] addrstrs = addrstr.split(",");
        String emailAddr = "";
        String email_trim = null;
        String[] email_spilit = null;
        String tmp = null;
        for (int i = 0; i < addrstrs.length; i++) {

            email_trim = TMailAddress.getTrimAddress(addrstrs[i]);

            email_spilit = email_trim.split("<");
            if (email_spilit.length != 1) {
                if (existEmailAddress(emailAddr, email_spilit[1])) {
                    continue;
                }
                tmp = email_spilit[0].replaceAll("\"", "").trim();
                if (tmp.length() > 0) {
                    email_trim = "\"" + tmp + "\"<" + email_spilit[1];
                } else {
                    email_trim = email_spilit[1].replace(">", "");
                }
            } else {
                if (existEmailAddress(emailAddr, email_trim)) {
                    continue;
                }
            }
            emailAddr += email_trim;
            if ((i + 1) != addrstrs.length) {
                emailAddr += ",";
            }

        }
        email_trim = null;
        email_spilit = null;
        tmp = null;
        return emailAddr;
    }
    
    private String getMailAddressStr(List<MailAddressBean> list) {
        StringBuffer sb = new StringBuffer();
        MailAddressBean mailAddressBean = null;
        if (list.size() > 0) {
            for (Iterator iterator = list.iterator(); iterator.hasNext();) {
                mailAddressBean = (MailAddressBean) iterator.next();
                sb.append(mailAddressBean.getAddress());
                sb.append(",");
            }
        }
        mailAddressBean = null;
        return sb.toString();
    }
    
    private boolean existEmailAddress(String emails, String email) {
        boolean exist = false;
        String tmpEmail = null;
        String[] email_spilit = emails.split(",");
        if (email_spilit.length != 1) {
            for (int i = 0; i < email_spilit.length; i++) {
                tmpEmail = email_spilit[i];
                tmpEmail = TMailAddress.getAddress(TMailAddress.getTrimAddress(tmpEmail));
                email = TMailAddress.getAddress(TMailAddress.getTrimAddress(email));
                if (tmpEmail.equals(email)) {
                    exist = true;
                    break;
                }
            }
        } else {
            emails = TMailAddress.getAddress(TMailAddress.getTrimAddress(emails));
            email = TMailAddress.getAddress(TMailAddress.getTrimAddress(email));
            if (emails.equals(email))
                exist = true;
        }
        return exist;
    }
}
