/**
 * SortMessage.java 2009. 1. 12.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.mail.sort;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import jakarta.mail.Flags;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.InternetHeaders;
import jakarta.mail.internet.MimeUtility;

import org.eclipse.angus.mail.imap.protocol.BASE64MailboxEncoder;
import org.eclipse.angus.mail.imap.protocol.Item;

/**
 * <p><strong>SortMessage.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class SortMessage {
	
	private String folderName = null;
	private String folderEncName = null;
	private String[] tagNames = null;
	private String preview = null;
    private Flags flags = null;
    private TIMSENVELOPE envelope = null;
    private TIMSBODYSTRUCTURE bs = null;
    private InternetHeaders headers = null;
    private Date internalDate  = null;
    private int size = -1;
    private long uid = -1;

    public SortMessage(XAllSortResponse xasr){
    	folderName = xasr.getFolderName();
    	folderEncName = BASE64MailboxEncoder.encode(folderName);
        tagNames = xasr.getTagNames();
        preview = xasr.getPriview();
        
        int count = xasr.getItemCount();

        for (int j = 0; j < count; j++) {
            Item item = xasr.getItem(j);

            if (item instanceof Flags) {
                flags = (Flags) item;
            }
            else if (item instanceof TIMSENVELOPE) {
                envelope = (TIMSENVELOPE) item;
            }
            else if (item instanceof TIMSINTERNALDATE) {
            	internalDate = ((TIMSINTERNALDATE) item).getDate();
            }
            else if (item instanceof TIMSRFC822SIZE) {
                size = ((TIMSRFC822SIZE) item).size;
            }
            else if (item instanceof TIMSBODYSTRUCTURE) {
                bs = (TIMSBODYSTRUCTURE) item;
            }
            else if (item instanceof TIMSUID) {
                TIMSUID u = (TIMSUID) item;
                this.uid = u.uid; // set uid
            }
            else if (item instanceof TIMSRFC822DATA ||
                item instanceof TIMSBODY) {

                InputStream headerStream;
                if (item instanceof TIMSRFC822DATA) { // IMAP4
                    headerStream = 
                        ((TIMSRFC822DATA) item).getByteArrayInputStream();
                }
                else { // IMAP4rev1
                    headerStream = 
                        ((TIMSBODY) item).getByteArrayInputStream();
                }

                if (headers == null) {
                    headers = new InternetHeaders();
                }
                
                try {
                	headers.load(headerStream);
				} catch (MessagingException e) {
					System.out.println("--- header setting error!! ---");
				}                
            }
        }
    }

    public Date getInternalDate() throws MessagingException {
        return internalDate;
    }
    
    public Date getEnvelopeDate() throws MessagingException {
    	if(envelope.date != null){
    		return envelope.date;
    	} else {
    		return internalDate;
    	}
    }

    public String getFlagString() throws MessagingException {
        StringBuffer sb = new StringBuffer();
        
        try {			
			Flags.Flag[] sf = flags.getSystemFlags();

			for (int i = 0; i < sf.length; i++) {
				if (sf[i] == Flags.Flag.ANSWERED) {
					sb.append("A");
				}
				else if (sf[i] == Flags.Flag.DRAFT) {
					sb.append("D");
				}
				else if (sf[i] == Flags.Flag.FLAGGED) {
					sb.append("F");
				}
				else if (sf[i] == Flags.Flag.RECENT) {
					sb.append("R");
				}
				else if (sf[i] == Flags.Flag.SEEN) {
					sb.append("S");
				}
				else if (sf[i] == Flags.Flag.USER) {
					sb.append("U");
				}
			}

			String uf[] = flags.getUserFlags();
			for (int i = 0; i < uf.length; i++) {
				if (uf[i].toUpperCase().indexOf("CUSTOM") >= 0) {
					sb.append("C");
				}
			}

			if (hasAttachment(bs)) {
				sb.append("T");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("-- Exception getFlagsString : ["+e.getMessage()+"]");
		}

        return sb.toString();
    }

    private boolean hasAttachment(TIMSBODYSTRUCTURE part)
        throws MessagingException {

        if (part.isMulti()) {
            //MimeMultipart mmp = (MimeMultipart) part.getContent();
            TIMSBODYSTRUCTURE[] mmp = part.bodies;
            //int n = mmp.getCount();
            int n = mmp.length;
            for (int i = 0; i < n; i++) {
                //MimeBodyPart mbp = (MimeBodyPart) mmp.getBodyPart(i);
                TIMSBODYSTRUCTURE mbp = mmp[i];
                if (hasAttachment(mbp)) {
                    return true;
                }
            }
        } else if(part.isNested()){
        	 return true;
        }
        else {
            //return part.getFileName() != null;
		    String filename = null;
		    String disposition = part.disposition;
		    String contentId = part.id;
		    boolean isAttached = false; 
		    
		    if (part.dParams != null) {
		    	filename = part.dParams.get("filename");
		    	//disposition = part.dParams.get("");
		    }
		    if (filename == null && part.cParams != null) {
		    	filename = part.cParams.get("name");
		    }	    
		    
		    if(filename != null && 
		    		("attachment".equalsIgnoreCase(disposition) ||
		    		("inline".equalsIgnoreCase(disposition) &&
				    contentId == null))){
		    	isAttached = true;
		    }
		    
		    return isAttached;
        }

        return false;
    }

    public String getFolderName() {
        return folderName;
    }
    
    public String getFolderEncName() {
        return folderEncName;
    }
    
    public String[] getTagNames() {
        return tagNames;
    }
    
    public String getPreview() {
        return preview;
    }
    
    public String[] getHeader(String name) {
        return headers.getHeader(name);
    }
    
    public String getMessageID(){
    	return envelope.messageId;
    }

    public InternetAddress getFrom() throws MessagingException {
        InternetAddress[] from = envelope.from;
        return from != null ? from[0] : null;
    }
    
    public InternetAddress getTo() throws MessagingException {
        InternetAddress[] to = envelope.to;
        return to != null ? to[0] : null;
    }
    
    public InternetAddress[] getCc() throws MessagingException {
    	InternetAddress[] cc = envelope.cc;
    	return cc != null ? cc : null;
    }
    
    

    //public MimeMessage getMimeMessage() {
    //    return mm;
    //}

    public String getSubject() throws MessagingException {
        try {
        	if(envelope.subject != null){
        		return MimeUtility.decodeText(envelope.subject);
        	} else {
        		return null;
        	}            
        }
        catch (UnsupportedEncodingException ex) {
            return envelope.subject;
        } catch (Exception e) {
			return "";
		}
    }

    public int getSize() throws MessagingException {
        return size;
    }

    public long getUid() {
        return uid;
    }

    public void setUID(long uid) {
        this.uid = uid;
    }
}
