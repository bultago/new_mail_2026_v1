package com.terracetech.tims.hybrid.mail.action;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Stack;

import jakarta.mail.internet.MimeMessage;

import com.sun.mail.imap.IMAPMessage;
import com.terracetech.tims.hybrid.common.action.BaseAction;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailPart;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class MailDownloadServiceAction extends BaseAction {

    private String folderName = null;
    private String uid = null;
    private String part = null;

    public String execute() throws Exception {
        TMailStore store = null;
        TMailStoreFactory factory = new TMailStoreFactory();
        TMailFolder folder = null;
        TMailMessage message = null;
        Stack<String> nestedPartStatck = new Stack<String>();
        try {
            store = factory.connect(request.getRemoteAddr(), user);
            folder = store.getFolder(folderName);
            folder.open(false);
            message = folder.getMessageByUID(Long.parseLong(uid), false);
            nestedPartStatck.push(part);
            
            TMailPart attachPart = new TMailPart(message.getPart(new int[]{Integer.parseInt(part)}));
            boolean isAttachRFC822 = attachPart.isMimeType("message/rfc822");
            boolean isVcard = attachPart.isMimeType("text/x-vcard");

            String fileName = attachPart.getFileName();
            InputStream in = (!isAttachRFC822 && !isVcard) ? attachPart.getInputStream() : null;
            
            String agent = request.getHeader("user-agent");
            fileName = StringUtils.getDownloadFileName(fileName, agent);

            response.setHeader("Content-Type", "application/download; name=\"" + fileName + "\"");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            
            try {
                if (isAttachRFC822) {
                    Object msg = attachPart.getContent();
                    Enumeration enumer = null;
                    if (msg instanceof IMAPMessage) {
                        enumer = ((IMAPMessage) msg).getAllHeaderLines();
                    } else if (msg instanceof MimeMessage) {
                        enumer = ((MimeMessage) msg).getAllHeaderLines();
                    }

                    if (enumer != null) {
                        while (enumer.hasMoreElements()) {
                            out.write(((String) enumer.nextElement()).getBytes());
                            out.write("\n".getBytes());
                        }
                        out.write("\n".getBytes());
                    }
                    if (msg instanceof IMAPMessage) {
                        in = ((IMAPMessage) msg).getRawInputStream();
                    } else if (msg instanceof MimeMessage) {
                        in = ((MimeMessage) msg).getRawInputStream();
                    }

                }

                if (isVcard) {
                    String content = attachPart.getTextContent();
                    String charset = ("ko".equalsIgnoreCase(EnvConstants.getBasicSetting("setup.state"))) ? "euc-kr"
                            : "shift-jis";
                    in = new ByteArrayInputStream(content.getBytes(charset));
                }

                byte[] buffer = new byte[1024 * 1024];
                int n;

                while ((n = in.read(buffer, 0, buffer.length)) != -1) {
                    out.write(buffer, 0, n);
                }

            } catch (Exception ex) {
                LogManager.writeErr(this, ex.getMessage(), ex);
            } finally {
                out.close();
                in.close();
            }
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
        } finally {
            try {
                if (store != null && store.isConnected())
                    store.close();
                if (folder != null && folder.isOpen())
                    folder.close(false);
            } catch (Exception e2) {
                LogManager.writeErr(this, e2.getMessage(), e2);
            }
        }
        return null;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setPart(String part) {
        this.part = part;
    }
    
}
