package com.terracetech.tims.mail;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import org.alfresco.jlan.util.UTF8Normalizer;

import com.sun.mail.imap.protocol.BASE64MailboxEncoder;

public class TMailMessage {
    public Message message = null;

	protected long prevUid 		= -1;
    protected long nextUid 		= -1;
    protected long uid 			= -1;
    protected float spamrate 	= 0;
    protected String charset 	= null;
    protected String defaultcharset = null;
    protected Locale locale 	= null;

    private InternetAddress[] mdnaddresses 	= null;	// use of MDN receiver
    private TMailMDNResponse[] mdnresponses 	= null;	// use of MDN sender
    private TMailConfirm myConfirm 	= null;			// confirm info
    private int mdnreadcnt 		= 0;					// MDN READ COUNT
    private Date cachedReservedDate 		= null;
	private Date cachedSentDate 			= null;
    private ByteArrayOutputStream mimeSource = null; // for Initech S/MIME
	private String myFolderAlias = null;
    private long indexNo = 0;

	private boolean isSetText 	= false;

	private Folder folder 		= null;
	private String folderFullName 	= null;
	private String folderEncName 	= null;

	private boolean directRead = false;
    /**
     * Constructor
     */
    public TMailMessage(Message message) {
        this.message = message;
    }

    public boolean fetchMimeSource() throws IOException, MessagingException {
        mimeSource = new ByteArrayOutputStream();
        Enumeration enumer = ((MimeMessage) message).getAllHeaderLines();
        InputStream in = ((MimeMessage) message).getRawInputStream();
        byte[] buffer = new byte[1024 * 1024];
        int n;

        while (enumer.hasMoreElements()) {
            String header = (String) enumer.nextElement();

            mimeSource.write(header.getBytes());
            mimeSource.write('\n');
        }

        mimeSource.write('\n');
 
        while ((n = in.read(buffer, 0, buffer.length)) != -1) {
             mimeSource.write(buffer, 0, n);
        }

        in.close();
        mimeSource.close();

        return true;
    }

    public String getContentType() throws MessagingException {
        return message.getContentType();
    }

    public TMailSecurity getSecurity() throws IOException, MessagingException {
        TMailSecurity security = new TMailSecurity((MimeMessage) message);
     
        return security;
    }

    public Enumeration getAllHeaderLines() throws MessagingException {
        return ((MimeMessage) message).getAllHeaderLines();
    }

    public InternetAddress[] getAllRecipients() throws MessagingException {
		InternetAddress[] iasTo 	= getTo();
		InternetAddress[] iasCc 	= getCc();
		InternetAddress[] iasBcc 	= getBcc();

        Vector v = new Vector();

        for (int i = 0; iasTo != null && i < iasTo.length; i++) {
            v.addElement(iasTo[i]);
        }

        for (int i = 0; iasCc != null && i < iasCc.length; i++) {
            v.addElement(iasCc[i]);
        }

		for (int i = 0; iasBcc != null && i < iasBcc.length; i++) {
			v.addElement(iasBcc[i]);
        }

        InternetAddress[] ias = new InternetAddress[v.size()];

        v.copyInto(ias);

        return ias;
    }

    public InternetAddress[] getCc() throws MessagingException {
        try {
        InternetAddress[] ias = (InternetAddress[]) message.getRecipients(
            Message.RecipientType.CC);
        	return ias;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("-- Exception getCc : ["+e.getMessage()+"]");
		}

        return null;
    }

    public String getCcString() throws MessagingException {
		return TMailAddress.getAddressString(getCc());
    }
    
    public InternetAddress[] getBcc() throws MessagingException {
    	try {
        InternetAddress[] ias = (InternetAddress[]) message.getRecipients(
            Message.RecipientType.BCC);
        	return ias;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("-- Exception getBcc : ["+e.getMessage()+"]");
		}

        return null;
    }

    public String getBccString() throws MessagingException {
		return TMailAddress.getAddressString(getBcc());
    }

    public InternetAddress[] getXBcc() throws MessagingException {
		try {
			String[] headers = message.getHeader("X-Bcc");

			if (headers != null) {
				String bcc = headers[0];

				if (bcc != null) {
					bcc = MimeUtility.decodeText(bcc);
					InternetAddress[] ibcc = InternetAddress.parse(bcc);

					return TMailAddress.removeInvalidAddress(ibcc);
				}
			}
		} catch (Exception ex) {
			System.out.println("-- Exception getBcc : ["+ex.getMessage()+"]");
		}

		return null;
    }

    public String getXBccString() throws MessagingException {
		return TMailAddress.getAddressString(getXBcc());
    }

    public String getFileName() throws MessagingException {
        return message.getFileName();
    }

    public InputStream getInputStream() throws IOException, MessagingException {
        return message.getInputStream();
    }

    public String getMimeSource() {
        if (mimeSource == null) {
            return null;
        }
        
        return mimeSource.toString();
    }

    public Part getPart(int[] path) throws IOException,
        MessagingException {

        Part part = message;

        for (int i = 0; i < path.length; ) {
            if (part.isMimeType("multipart/*")) {
                Multipart mp = (Multipart) part.getContent();

                part = mp.getBodyPart(path[i++]);
            }
            else if (part.isMimeType("message/rfc822")) {
                part = (Message) part.getContent();
            }
            else {
                return null;
            }
        }

        return part;
    }

    public static final int TEXT_PLAIN = 1;
    public static final int TEXT_HTML = 2;

    private void parseTextContent(int mode, Part part, Vector v)
        throws IOException, MessagingException {

        if (part.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) part.getContent();
			int count = 0;

            try {
                count = mp.getCount();
            }
            catch (MessagingException ex) {
				System.out.println("-- MessagingException parseTextContent : ["+ex.getMessage()+"]");
                return;
            }

			if (part.isMimeType("multipart/alternative")
				|| part.isMimeType("multipart/related")) {
                int idx = 0;
                boolean isSingleText = false;

                for (int i = 0; i < count; i++) {
					Part p 		= (Part)mp.getBodyPart(i);
					String disp	= p.getDisposition();

					if (disp != null && disp.equalsIgnoreCase("ATTACHMENT")) {
						continue;
					}

					if (p.isMimeType("multipart/alternative")
						|| p.isMimeType("multipart/related")||p.isMimeType("multipart/relative")) {
                    	parseTextContent(mode, p, v);
                    } 
					else if ((mode == TEXT_HTML && p.isMimeType("text/html")) 
						|| (mode == TEXT_PLAIN && p.isMimeType("text/plain"))) {
                        v.addElement(new TMailPart(null, p));
						isSetText = true;
                        return;
                    }
					else if ((mode == TEXT_HTML && p.isMimeType("text/plain")) 
						|| (mode == TEXT_PLAIN && p.isMimeType("text/html"))) {
                        idx = i;
                        isSingleText = true;
                    }
                }

				if (!isSetText && isSingleText) {
                    v.addElement(new TMailPart(null, mp.getBodyPart(idx)));
                    return;
                }
            }
            else {            
                for (int i = 0; i < count; i++) {
                    parseTextContent(mode, mp.getBodyPart(i), v);
                }
            }
        }
        /*else if (part.isMimeType("message/rfc822")) {
            Message msg = (Message) part.getContent();
            TMailPart mypart = new TMailPart(null, msg);

            mypart.setMessageRFC822(true);
            v.addElement(mypart);
            parseTextContent(mode, msg, v);
        }*/
		else {
			//System.out.println(part.getContentType()); 
			String ctype 	= part.getContentType();
			String disp 	= part.getDisposition();
			String fileName	= part.getFileName();

			if ( fileName != null 
				|| (disp != null && (disp.equalsIgnoreCase("ATTACHMENT"))) ) {
				return ;
			}
			
			if (part.isMimeType("text/plain")
				|| part.isMimeType("text/html")
				|| part.isMimeType("text/richtext")
				|| part.isMimeType("text/enriched")		
				|| part.isMimeType("text/*")	
				|| part.isMimeType("message/delivery-status")
				|| ctype.indexOf("text/html") >= 0
				|| ctype.indexOf("text/") >= 0 //TCUSTOM-2139
				) {
              	v.addElement(new TMailPart(null, part));
			}			
		}
        
    }

	public TMailMessage getRfc822() throws IOException,
        MessagingException {
        Vector v = new Vector();
        parseRfc822Content(message, v);
		if(v.size() == 0) return null;
		else return new TMailMessage((Message)v.get(0));
	}

    private void parseRfc822Content(Part part, Vector v)
        throws IOException, MessagingException {

        if (part.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) part.getContent();
            int count;

            try {
                count = mp.getCount();
            }
            catch (MessagingException e) { // Missing start boundary
                return;
            }

			for (int i = 0; i < count; i++) 
                parseRfc822Content(mp.getBodyPart(i), v);

        } else if (part.isMimeType("message/rfc822")) {
			v.add(part.getContent());
			//	parseTextContent(TEXT_PLAIN, msg, v);
			//	parseRfc822Content(msg, v);
        }
    }

    public TMailPart[] getTextContent(int mode) throws IOException,
        MessagingException {

        Vector v = new Vector();

        parseTextContent(mode, message, v);

        TMailPart[] parts = new TMailPart[v.size()];
        v.copyInto(parts);

        return parts;
    }

    /*
     * added by doyoung
     * 2006.02.22
     * edited by hkkim
     * attachment rule : not multipart/* and not text/* and CID == null
     * 2008.06.25
     */
    private boolean isAttachFiles(Part part) throws IOException, MessagingException {
        try {
            /*
        	if(part.isMimeType("application/octet-stream")) {
                if (((MimePart) part).getContentID() == null) {
                    return true;
                } else if (part.getFileName() != null) {
                    return true;
                }
        	} else*/
            if(part.isMimeType("application/*") || part.isMimeType("image/*")) {
                if (((MimePart) part).getContentID() == null) {
                    return true;
                } else if ("attachment".equalsIgnoreCase(part.getDisposition())) {
                    return true;
                } else if (part.getFileName() != null) {
                	if("inline".equalsIgnoreCase(part.getDisposition()) &&
                    		part.isMimeType("application/*")){
                		return true;
                	}
                    TMailPart mpart = new TMailPart(part);
                    String fname = mpart.getFileName();
                    fname = fname.toUpperCase();
                    if (!fname.endsWith(".GIF") 
                        && !fname.endsWith(".JPG") 
                        && !fname.endsWith(".JPEG") 
                        && !fname.endsWith(".EMF") 
                        && !fname.endsWith(".PNG") 
                        && !fname.endsWith(".BMP")) {
                        return true;
                    }
                    mpart = null;                    
                }
            } else if (part.isMimeType("text/*")) {
                if (part.isMimeType("text/x-vcard")) {
                    return false;
                } else {
                    if (((MimePart) part).getContentID() == null && part.getFileName() != null) {
                        return true;
               	    }
                }
            } else if (part.isMimeType("message/rfc822") && ((MimePart) part).getContentID() == null) {
                return true;
            } else if (part.isMimeType("message/delivery-status") || part.isMimeType("multipart/report")) {
                return true;
            } else if (part.isMimeType("multipart/x-gedi-document") || part.isMimeType("multipart/1")) {
                return true;
            } else if (part.isMimeType("multipart/mixed") || 
            		part.isMimeType("multipart/alternative") || 
            		part.isMimeType("multipart/related") ||
            		part.isMimeType("multipart/signed") || 
            		part.isMimeType("multipart/appledouble")) {
                    Multipart mp = (Multipart) part.getContent();
                    int count = 0;
                    try {
                        count = mp.getCount();
                    } catch (MessagingException e) { // Missing start boundary
                        System.out.println("MessagingException isAttachFiles getCount : ["+e.getMessage()+"]");
                        return false;
                    }
                    for (int i = 0; i < count; i++) {
                        boolean b = isAttachFiles(mp.getBodyPart(i));
                        if (b) {
                            return b;
                        }
                    }                
            } else {
                if (((MimePart) part).getContentID() == null) {
                    return true;
                } else if (part.getFileName() != null) {
                    return true;
                } else if ("ATTACHMENT".equalsIgnoreCase(part.getDisposition())) {
                    return true;
                }
            }
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("MessagingException isAttachFiles : ["+e.getMessage()+"]");
        }
        return false;
    }

    /*
     * edited by hkkim
     * attachment rule : not multipart/* and not text/* and CID == null
     * 2008.06.25
     */
    private void parseAttachFiles(String path, Part part, Vector v) throws IOException, MessagingException {
        try {
            /*
        	if(part.isMimeType("application/octet-stream")) {	
                if (((MimePart) part).getContentID() == null) {
                    v.addElement(new MyPart(path, part));
                } else if (part.getFileName() != null) {
                    v.addElement(new MyPart(path, part));
                }                
        	} else */
            if (part.isMimeType("application/*") || part.isMimeType("image/*")) {                
                if (((MimePart) part).getContentID() == null) {                    
                    if (!part.isMimeType("application/applefile"))
                        v.addElement(new TMailPart(path, part));
                } else if ("attachment".equalsIgnoreCase(part.getDisposition())) {                    
                    v.addElement(new TMailPart(path, part));
                } else if (part.getFileName() != null) {
                	if(!"inline".equalsIgnoreCase(part.getDisposition()) &&
                		!part.isMimeType("image/*")){
	                    TMailPart mpart = new TMailPart(path,part);
	                    String fname = mpart.getFileName();
	                    fname = fname.toUpperCase();
	                    if (!fname.endsWith(".GIF") 
	                        && !fname.endsWith(".JPG") 
	                        && !fname.endsWith(".JPEG") 
	                        && !fname.endsWith(".EMF") 
	                        && !fname.endsWith(".PNG") 
	                        && !fname.endsWith(".BMP")) {
	                        v.addElement(mpart);
	                    }
	                    mpart = null;
	                // TCUSTOM-2121
                	}else if("inline".equalsIgnoreCase(part.getDisposition()) &&
                    		part.isMimeType("application/*")){
                		// TCUSTOM-2235
                		TMailPart mpart = new TMailPart(path,part);
	                    String fname = mpart.getFileName();
	                    fname = fname.toUpperCase();
	                    if (!fname.endsWith(".GIF") 
	                        && !fname.endsWith(".JPG") 
	                        && !fname.endsWith(".JPEG") 
	                        && !fname.endsWith(".EMF") 
	                        && !fname.endsWith(".PNG") 
	                        && !fname.endsWith(".BMP")) {
	                        v.addElement(mpart);
	                    }
	                    mpart = null;
                	}
                } else if (((MimePart) part).getContentID() == null && part.getFileName() != null) {
                	if(!"inline".equalsIgnoreCase(part.getDisposition())){
	                	if(part.isMimeType("image/*")){
		                    TMailPart mpart = new TMailPart(path,part);
		                    String fname = mpart.getFileName();
		                    fname = fname.toUpperCase();
		                    if (fname.endsWith(".GIF") 
		                        && fname.endsWith(".JPG") 
		                        && fname.endsWith(".JPEG") 
		                        && fname.endsWith(".EMF") 
		                        && fname.endsWith(".PNG") 
		                        && fname.endsWith(".BMP")) {
		                        v.addElement(mpart);
		                    }
		                    mpart = null;
	                	}
                	}
                }
            } else if (part.isMimeType("text/*")) {

                if (part.isMimeType("text/x-vcard")) {
                    // do nothing
                } else if (part.isMimeType("text/x-tex") && 
                		part.getFileName() != null &&
                		"ATTACHMENT".equalsIgnoreCase(part.getDisposition())) {
                	v.addElement(new TMailPart(path, part));
                } else {
                    if (((MimePart) part).getContentID() == null && part.getFileName() != null) {
                        v.addElement(new TMailPart(path, part));
                    }
                }
            } else if (part.isMimeType("message/rfc822") && 
            		((MimePart) part).getContentID() == null &&
            		!"ATTACHMENT".equalsIgnoreCase(part.getDisposition())) {            	
            	v.addElement(new TMailPart(path,part));                
	        } else if (part.isMimeType("message/delivery-status")) {
                v.addElement(new TMailPart(path, part));
            } else if (part.isMimeType("multipart/mixed")
                     || part.isMimeType("multipart/alternative")
                     || part.isMimeType("multipart/related")
                     || part.isMimeType("multipart/x-gedi-document")
                     || part.isMimeType("multipart/1")
                     || part.isMimeType("multipart/report")
                     || part.isMimeType("multipart/signed")
                     || part.isMimeType("multipart/relative")
                     || part.isMimeType("multipart/appledouble")
                     ) {
                Multipart mp = (Multipart) part.getContent();
                int count = 0;
                try {
                    count = mp.getCount();
                } catch (MessagingException e) { // Missing start boundary
                    System.out.println("MessagingException parseAttachFiles getCount : ["+e.getMessage()+"]");
                }
                for (int i = 0; i < count; i++) {
                    parseAttachFiles((path != null)?  path + ':' + i : String.valueOf(i), mp.getBodyPart(i), v);
                }
            } else {
            	String ctype = part.getContentType(); //TCUSTOM-2139
            	if(ctype != null && ctype.indexOf("text/") >= 0){ //TCUSTOM-2139
            		// skip
            	}else{ //TCUSTOM-2139
	                if (((MimePart) part).getContentID() == null) {
	                    v.addElement(new TMailPart(path, part));
	                } else if (part.getFileName() != null) {
	                    v.addElement(new TMailPart(path, part));
	                } else if ("ATTACHMENT".equalsIgnoreCase(part.getDisposition())) {
	                    v.addElement(new TMailPart(path, part));
	                }
            	}
            }
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("MessagingException parseAttachFiles : ["+e.getMessage()+"]");
        }
    }

    public TMailPart[] getAttachFiles() throws IOException, MessagingException {
        Vector v = new Vector();

		/*
		 * isAttached()�� �ʿ���� by isle4 (2007.12.17)
		 * 
		if (isAttached()) {
       		parseAttachFiles(null, message, v);
		}
		*/
		parseAttachFiles(null, message, v);

        TMailPart[] parts = new TMailPart[v.size()];
        v.copyInto(parts);

        return parts;
    }
    
    private void parseAttachImages(String path, Part part, Vector v)
        throws IOException, MessagingException {

        if (part.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) part.getContent();
			int count;

			try {
				count = mp.getCount();
			}
			catch (MessagingException e) {
				return;
			}

			for (int i = 0; i < count; i++) {
				parseAttachImages(
					(path != null)? path + ':' + i : String.valueOf(i),
					mp.getBodyPart(i), v);
			}
		}
        /*else if (part.isMimeType("message/rfc822")) {
            parseAttachImages(path, (Message) part.getContent(), v);
        }*/
        if (part.isMimeType("image/*") || part.isMimeType("application/*")) {
        	if (((MimePart) part).getContentID() == null &&
        			"attachment".equalsIgnoreCase(part.getDisposition())) {
	        	TMailPart mpart = new TMailPart(path,part);
	            String fileName = mpart.getFileName();
	
				if (fileName != null) {
					fileName = fileName.toUpperCase();
	
					if (fileName.endsWith(".GIF") 
						|| fileName.endsWith(".JPG") 
						|| fileName.endsWith(".JPEG")
						|| fileName.endsWith(".BMP")
						|| fileName.endsWith(".PNG")) {
	            		v.addElement(mpart);
					}
				}
				mpart = null;
        	} else if (((MimePart) part).getContentID() != null) {
            	TMailPart mpart = new TMailPart(path,part);
	            String fileName = mpart.getFileName();
	
				if (fileName != null) {
					fileName = fileName.toUpperCase();
					
					if(!"inline".equalsIgnoreCase(part.getDisposition())){
						System.out.println("fileNamefileName ]]] " +  fileName);
		
						if (fileName.endsWith(".GIF") 
							|| fileName.endsWith(".JPG") 
							|| fileName.endsWith(".JPEG")
							|| fileName.endsWith(".BMP")
							|| fileName.endsWith(".PNG")) {
		            		v.addElement(mpart);
						}
					}
				}
				mpart = null;        		
        	}
        }
    }

    public TMailPart[] getAttachImages() throws IOException, MessagingException {
        Vector v = new Vector();

        parseAttachImages(null, message, v);

        TMailPart[] parts = new TMailPart[v.size()];
        v.copyInto(parts);

        return parts;
    }

    private void parseAttachImagesNCid(String path, Part part, Vector v)
        throws IOException, MessagingException {

        if (part.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) part.getContent();
			int count;

			try {
				count = mp.getCount();
			}
			catch (MessagingException e) {
				return;
			}

			for (int i = 0; i < count; i++) {
				parseAttachImagesNCid(
					(path != null)? path + ':' + i : String.valueOf(i),
					mp.getBodyPart(i), v);
			}
		}
        /*else if (part.isMimeType("message/rfc822")) {
            parseAttachImagesNCid(path, (Message) part.getContent(), v);
        }*/
        else if (part.isMimeType("image/*")) {
        	if (((MimePart) part).getContentID() != null) {
        		v.addElement(new TMailPart(path, part));
        	}            
        }
        else if (part.isMimeType("application/*")) {
            TMailPart mpart = new TMailPart(path,part);
            String fileName = mpart.getFileName();
			String contentID 	= ((MimePart) part).getContentID();

			if (fileName != null) {
				fileName = fileName.toUpperCase();

				if (contentID != null
					&& (fileName.endsWith(".GIF") 
					|| fileName.endsWith(".JPG") 
					|| fileName.endsWith(".JPEG") 
					|| fileName.endsWith(".BMP")
					|| fileName.endsWith(".PNG")||(contentID.indexOf("@namo.co.kr") > -1))) {
            		v.addElement(mpart);
				}
			}
			mpart = null;
        }
    }

    public TMailPart[] getAttachImagesNCid() throws IOException, MessagingException {
        Vector v = new Vector();

        parseAttachImagesNCid(null, message, v);

        TMailPart[] parts = new TMailPart[v.size()];
        v.copyInto(parts);
        
        return parts;
    }

	/*
	 * added by doyoung
	 * 2006.02.07
	 */
    private void parseAttachVcards(String path, Part part, Vector v)
        throws IOException, MessagingException {

        if (part.isMimeType("multipart/*")) {
            if (!part.isMimeType("multipart/alternative")) {
                Multipart mp = (Multipart) part.getContent();
                int count;

                try {
                    count = mp.getCount();
                }
                catch (MessagingException e) { // Missing start boundary
                    return;
                }

                for (int i = 0; i < count; i++) {
                    parseAttachVcards(
                        (path != null)? path + ':' + i : String.valueOf(i),
                            mp.getBodyPart(i), v);
                }
            }
        }
        /*else if (part.isMimeType("message/rfc822")) {
            parseAttachVcards(path, (Message) part.getContent(), v);
        }*/
        else if (part.isMimeType("text/x-vcard")) {
            v.addElement(new TMailPart(path, part));
        }
    }

	/*
	 * added by doyoung
	 * 2006.02.07
	 */
    public TMailPart[] getAttachVcards() throws IOException, MessagingException {
        Vector v = new Vector();

        parseAttachVcards(null, message, v);

        TMailPart[] parts = new TMailPart[v.size()];
        v.copyInto(parts);

        return parts;
    }

    public InternetAddress[] getReplyTo() throws MessagingException {
		try {
			InternetAddress[] ias = (InternetAddress[]) message.getReplyTo();

        	return TMailAddress.removeInvalidAddress(ias);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("-- Exception getReplyTo : ["+e.getMessage()+"]");
		}

        return null;
    }

    public String getReplyToString() throws MessagingException {
		return TMailAddress.getAddressString(getReplyTo());
    }

    public InternetAddress[] getFrom() throws MessagingException {
		try {
        	InternetAddress[] ias = (InternetAddress[]) message.getFrom();

        	return TMailAddress.removeInvalidAddress(ias);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("-- Exception getFrom : ["+e.getMessage()+"]");
		}

        return null;
    }

    public String getFromString() throws MessagingException {
		return TMailAddress.getAddressString(getFrom());
    }

    public String getFromPersonal() throws MessagingException {
        try {
            InternetAddress[] ias = getFrom();

			if (ias != null) {
				String personal = ias[0].getPersonal();
				return (personal != null) ? personal : "";
			}
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("-- Exception getFromPersonal : ["+e.getMessage()+"]");
        }

        return "";
    }

    public String getFromAddress() throws MessagingException {
        try {
            InternetAddress[] ias = getFrom();

			if (ias != null) {
				return ias[0].getAddress();
			}
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("-- Exception getFromAddress : ["+e.getMessage()+"]");
        }

        return "";
    }

    public String[] getHeader(String name) throws MessagingException {
        return message.getHeader(name);
    }

	/*
	* added by doyoung
	* 2007.02.10
	*/
    public String getHeaderString(String name) throws MessagingException {
       	String[] values = message.getHeader(name);
		String retString = null;

		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				try {
					String value = MimeUtility.decodeText(values[i]);
					if (i == 0) {
						retString = value;
					}
					else {
						retString += " " + value;
					}
				} catch (UnsupportedEncodingException ex) { }
			}
		}

		return retString;
    }

    public InputStream getRawInputStream() throws MessagingException {
        return ((MimeMessage) message).getRawInputStream();
    }

    public String getReservedDate3() throws MessagingException {
        if (cachedReservedDate == null) {
            return "";
        }

        Date date = cachedReservedDate;

        SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy/MM/dd(HH:mm)");

        return sdf.format(date);
    }

    public Date getSentDate() throws MessagingException {
        if (cachedSentDate != null) {
            return cachedSentDate;
        }

       	Date date = null;

		try {
        	date = message.getSentDate();

			if (date == null) {
				date = this.getReceivedDate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("-- Exception getSentDate : ["+e.getMessage()+"]");
		}

		this.cachedSentDate = date;

        return date;
    }   
    
    public String getSentDateForRead() throws MessagingException {
        Date date = getSentDate();
        SimpleDateFormat sdf = null;

        if (date == null) {
            return "";
        }

        if (locale != null) {
            sdf = new SimpleDateFormat("yyyy/MM/dd EEEE a h:mm:ss", locale);
        }
        else {
            sdf = new SimpleDateFormat("yyyy/MM/dd EEEE a h:mm:ss");
        }

        return sdf.format(date);
    }    
    	
    public String getSentDateForList() throws MessagingException {
        Date date1 = new Date();
        Date date2 = getSentDate();

        if (date2 == null) {
            return "";
        }

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");

        int ms1 = Integer.parseInt(sdf2.format(date1));
        int ms2 = Integer.parseInt(sdf2.format(date2));

        SimpleDateFormat sdf =
            new SimpleDateFormat((ms1 == ms2)? "HH:mm:ss" : "yyyy/MM/dd");

        return sdf.format(date2);
    }

    public InternetAddress[] getMDNAddresses() throws MessagingException {
		try {
			String[] mdnAddress = 
				message.getHeader("Disposition-Notification-To");

			if(mdnAddress != null) {
				mdnAddress[0] = mdnAddress[0].trim();
				mdnAddress[0] = mdnAddress[0].replaceAll("[\t\n\r]", "");

				return InternetAddress.parse(mdnAddress[0], false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("-- Exception getMDNAddresses : ["+e.getMessage()+"]");
		}

		return null;
    }

	/*
	 * added by doyoung 
	 * 2006.02.07
	 */
    public Message getMessage() {
        return message;
    }

	/*
	 * added by doyoung 2005.11.10
	 */
    public int getMDNReadCount() {
        return (mdnresponses != null) ? mdnreadcnt : -1;
    }

    public int getMDNResponseCount() {
        return (mdnresponses != null)? mdnresponses.length : -1;
    }

    public TMailMDNResponse[] getMDNResponses() {
        return mdnresponses;
    }

    public String getMessageID() throws MessagingException {
		String mid = ((MimeMessage) message).getMessageID();

		/*
		 * added by doyoung
		 * 2006.03.29
		 */
		if (mid == null) {
			mid = message.getFolder().getFullName()+"@"+uid;
		}

        return mid;
    }

    public String getTrimMessageID() throws MessagingException {
		String mid = getMessageID();

		if(mid.charAt(0) == '<' && mid.charAt(mid.length()-1) == '>') {
			mid = mid.substring(1, mid.length()-1);
		}

		return mid;
    }

	public Date getReceivedDate() throws MessagingException {
		Date date = null;

		try {
			date = message.getReceivedDate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("-- Exception getReceivedDate : ["+e.getMessage()+"]");
		}

		return date;
	}

    public String getReceivedDateForRead() throws MessagingException {
        Date date = message.getReceivedDate();
        SimpleDateFormat sdf = null;

        if (date == null) {
            return "";
        }

        if (locale != null) {
            sdf = new SimpleDateFormat("yyyy/MM/dd EEEE a h:mm:ss", locale);
        }
        else {
            sdf = new SimpleDateFormat("yyyy/MM/dd EEEE a h:mm:ss");
        }

        return sdf.format(date);
    }

    public String getReceivedDateForList() throws MessagingException {
        Date date1 = new Date();
        Date date2 = null;

		try {
        	date2 = message.getReceivedDate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("-- Exception getReceivedDate3 : ["+e.getMessage()+"]");
        	return "";
		}


        if (date2 == null) {
            return null;
        }

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");

        int ms1 = Integer.parseInt(sdf2.format(date1));
        int ms2 = Integer.parseInt(sdf2.format(date2));

        SimpleDateFormat sdf =
            new SimpleDateFormat((ms1 == ms2)? "HH:mm:ss" : "yyyy/MM/dd");

        return sdf.format(date2);
    }

    /* only for WebFolder */
    public String getSizeForWebFolder() throws IOException, MessagingException {
        return message.getFileName();

        /*
        Multipart mp = (Multipart) message.getContent();
        BodyPart bp = mp.getBodyPart(0);
        
        return bp.getFileName();
        */
    }

    public int getSize() throws MessagingException {
		try {
        	return message.getSize();
		} catch (MessagingException e) {
			e.printStackTrace();
			System.out.println("MessagingException getSize : ["+e.getMessage()+"]");
		}

		return 0;
    }

    public float getSpamRate() {
        return spamrate;
    }

    public String getSubject() throws MessagingException {
		try {
			String subject = TMailUtility.getCRLFEscape(message.getSubject());
			subject = UTF8Normalizer.getInstance().normalize(subject);
			// subject = subject.replaceAll("[\t\n\r]", "");

			return subject;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("-- Exception getSubject : ["+e.getMessage()+"]");
		}

       	return null;
    }

    public InternetAddress[] getTo() throws MessagingException {
		try {
			InternetAddress[] ias = (InternetAddress[]) message.getRecipients(
				Message.RecipientType.TO);
        	return ias;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("-- Exception getTo : ["+e.getMessage()+"]");
		}

        return null;
	}

    public String getToString() throws MessagingException {
        return TMailAddress.getAddressString(getTo());
    }

    public String getToString1() throws MessagingException {
		InternetAddress[] ias = getTo();

		if (ias != null) {
        	return TMailAddress.getAddressString(ias[0]);
		}

		return "";
    }

    public long getUid() {
        return uid;
    }

    public long getPrevUid() {
        return prevUid;
    }

    public long getNextUid() {
        return nextUid;
    }

	public void setPrevUid(long prevUid) {
		this.prevUid = prevUid;
    }

	public void setNextUid(long nextUid) {
		this.nextUid = nextUid;
    }

    public int getPriority() throws MessagingException {
		try {
        	String[] values = message.getHeader("X-PRIORITY");

        	if (values != null) {
            	return Integer.parseInt(values[0]);
        	}
		} catch (NumberFormatException e) {
		} catch (MessagingException e) {
			e.printStackTrace();
			System.out.println("MessagingException getPriority : ["+e.getMessage()+"]");
		}

		return 3; // default priority
    }

    /*
     * edited by hkkim
     * attachment rule : not multipart/* and not text/* and CID == null
     * 2008.06.25
     */
    public boolean isAttached() throws IOException, MessagingException {
        try {
            if (message.isMimeType("message/delivery-status")) {
                return true;
            } else if (message.isMimeType("multipart/x-gedi-document") || message.isMimeType("multipart/1") || message.isMimeType("multipart/report")) {
                return true;
            } else if (message.isMimeType("application/*")) {
                if (!message.isMimeType("application/pkcs7-mime") && !message.isMimeType("application/x-pkcs7-mime")) {
                    return true;
                }
            } else if (message.isMimeType("multipart/*")) {
                if (message.isMimeType("multipart/mixed") || message.isMimeType("multipart/alternative") || message.isMimeType("multipart/related")) {
                    Multipart mp = (Multipart) message.getContent();
                    int count = 0;
                    try {
                        count = mp.getCount();
                    } catch (MessagingException e) { // Missing start boundary
                        return false;
                    }
                    for (int i = 0; i < count; i++) {
                        boolean b = isAttachFiles(mp.getBodyPart(i));
                        if (b) {
                            return b;
                        }
                    }
                }
            }
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("MessagingException isAttached : ["+e.getMessage()+"]");
        }
        return false;
    }

	/*
	 * added by doyoung
	 * 2006.02.07
	 */
    public boolean isVcardAttached() throws MessagingException, IOException {
		try {
        	if (message.isMimeType("multipart/*")) {
            	if (!message.isMimeType("multipart/alternative")) {
                	Multipart mp = (Multipart) message.getContent();
                	int count;
                    count = mp.getCount();

                	for (int i = 0; i < count; i++) {
						if (mp.getBodyPart(i).isMimeType("text/x-vcard")) {
							return true;
						}
                	}
            	}
        	}
        	else if (message.isMimeType("text/x-vcard")) {
				return true;
        	}
		} catch (MessagingException e) {
			e.printStackTrace();
			System.out.println("MessagingException isVcardAttached : ["+e.getMessage()+"]");
		}
		return false;
    }

    public boolean isJapaneseCharset() {
        String mc = TMailMimeUtility.mimeCharset(charset).toUpperCase();

		/*
		* ISO-2022-JP, SHIFT-JIS, EUC-JP
		*/
		if (mc.indexOf("JIS") >= 0 || mc.indexOf("JP") >= 0) {
			return true;
		}

        return false;
    }

    public boolean isDifferentCharset() {
        String mc = TMailMimeUtility.mimeCharset(charset);
        /* --
        String dc = MyMimeUtility.mimeCharset(
            MimeUtility.getDefaultJavaCharset());
        -- */

        boolean isDiff = (mc != null && !mc.equals("ISO-8859-1")
            && !mc.equals("UTF-8") && !mc.equalsIgnoreCase("UTF-7")
			&& !defaultcharset.equals(mc));

        return isDiff;
    }

    public boolean isEncrypted() throws MessagingException {
		try {
        	if (!message.isMimeType("application/pkcs7-mime") &&
            	!message.isMimeType("application/x-pkcs7-mime")) {

            	return false;
        	}

        	ContentType ct = new ContentType(message.getContentType());
        	String param = ct.getParameter("smime-type");

        	return param.equalsIgnoreCase("enveloped-data");

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("-- Exception isEncrypted : ["+e.getMessage()+"]");
		}

        return false;
    }

    public boolean isExpunged() {
		try {
        	return message.isExpunged();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("-- Exception isExpunged : ["+e.getMessage()+"]");
		}

        return false;
    }

    public boolean isAnswered() throws MessagingException {
		try {
        	return message.isSet(Flags.Flag.ANSWERED);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("-- Exception isAnswered : ["+e.getMessage()+"]");
		}

        return false;
    }

    public boolean isForwarded() throws MessagingException {
		try {
        	return message.isSet(Flags.Flag.FLAGGED);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("-- Exception isForwarded : ["+e.getMessage()+"]");
		}

        return false;
    }

    public boolean isMDNMessage() throws MessagingException {
        return mdnaddresses != null;
    }

    public boolean isMimeType(String mimeType) throws MessagingException {
		try {
        	return message.isMimeType(mimeType);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("-- Exception isMimeType : ["+e.getMessage()+"]");
		}

        return false;
    }

    public String getSeen() throws MessagingException {
		if (isSeen()) {
			return "true";
		}

		return "false";
	}

    public boolean isSeen() throws MessagingException {
		try {
        	return message.isSet(Flags.Flag.SEEN);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("-- Exception isSeen : ["+e.getMessage()+"]");
		}

        return false;
    }

    public boolean isSigned() throws MessagingException {
		try {
        	if (message.isMimeType("multipart/signed")
				|| message.isMimeType("application/pkcs7-mime")
				|| message.isMimeType("application/x-pkcs7-mime")) {
            	return true;
        	}

        	ContentType ct = new ContentType(message.getContentType());
        	String param = ct.getParameter("smime-type");

			if (param != null && param.equalsIgnoreCase("signed-data")) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("-- Exception isSigned : ["+e.getMessage()+"]");
		}

        return false;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void setDefaultCharset(String dc) {
        this.defaultcharset = dc;
    }

    public void setFlag(Flags.Flag flag, boolean set) throws MessagingException{
        message.setFlag(flag, set);
    }

    public void setFlags(Flags flags, boolean set) throws MessagingException{
        message.setFlags(flags, set);
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setMDNAddresses(String[] mdnaddresses) throws AddressException {
        if (mdnaddresses != null) {
            this.mdnaddresses = InternetAddress.parse(mdnaddresses[0], false);
        }
    }

	/*
	* added by doyoung 2005.11.10
	*/
    public void setMDNReadCount(int cnt) {
		this.mdnreadcnt = cnt;
    }

    public void setMDNResponses(TMailMDNResponse[] mdnresponses) {
        this.mdnresponses = mdnresponses;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void setReservedDate(Date date) {
		this.cachedReservedDate = date;
	}

    public void setSentDate(Date date) {
		this.cachedSentDate = date;
	}

    public void setSpamRate(float spamrate) {
        this.spamrate = spamrate;
    }

	/*
	* added by doyoung 2007.09.04
	*/
	public void setMyConfirm(TMailConfirm myConfirm) {
		this.myConfirm = myConfirm;
    }

	public TMailConfirm getMyConfirm() {
		return myConfirm;
    }

    public void writeTo(OutputStream os) throws IOException, MessagingException {
        message.writeTo(os);
    }

	/*
	 * added by doyoung
	 * 2006.03.15
	 */
	public String getFlagsString() throws MessagingException {
		String strFlags = "";

		try {
			Flags flags = message.getFlags();
			Flags.Flag[] sf = flags.getSystemFlags();

			for (int i = 0; i < sf.length; i++) {
				if (sf[i] == Flags.Flag.ANSWERED) {
					strFlags += "A";
				}
				else if (sf[i] == Flags.Flag.DRAFT) {
					strFlags += "D";
				}
				else if (sf[i] == Flags.Flag.FLAGGED) {
					strFlags += "F";
				}
				else if (sf[i] == Flags.Flag.RECENT) {
					strFlags += "R";
				}
				else if (sf[i] == Flags.Flag.SEEN) {
					strFlags += "S";
				}
				else if (sf[i] == Flags.Flag.USER) {
					strFlags += "U";
				}
			}

			/*
			 * ��߸���(�߿����)
			 */
			String uf[] = flags.getUserFlags();
			for (int i = 0; i < uf.length; i++) {
				if (uf[i].toUpperCase().indexOf("CUSTOM") >= 0) {
					strFlags += "C";
				}
			}

			if (isAttached()) {
				strFlags += "T";
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("-- Exception getFlagsString : ["+e.getMessage()+"]");
		}

		return strFlags;
    }

	public String getMessageString() throws IOException, MessagingException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		message.writeTo((OutputStream)baos);

		return baos.toString();
    }

	public Folder getFolder() {
		if (folder == null) {
			this.folder = message.getFolder();
		}

		return folder;
	}

	public String getFolderFullName() {
		if (folderFullName == null) {
			this.folderFullName = (getFolder()).getFullName();
		}

		return folderFullName;
	}

	public String getFolderEncName() {
		if (folderEncName == null) {
			this.folderEncName = BASE64MailboxEncoder.encode(getFolderFullName());
		}

		return folderEncName;
	}

	public void setMyFolderAlias(String myFolderAlias) {
		this.myFolderAlias = myFolderAlias;
	}

	public String getMyFolderAlias() {
		return myFolderAlias;
	}

	/* 
	 * ��������� ��� ��(.) ���� ������ ������� �����Ѵ�. 
	 */ 
	public String getAliasFolderName() { 
		String str = myFolderAlias; 
		int dotIdx = str.lastIndexOf("."); 
		if (dotIdx > 0) { 
			str = str.substring(dotIdx + 1, str.length()); 
		} 
		return str; 
	} 

	/*
	* listing index
	* added by doyoung
	* 2006.09.20
	*/
	public void setIndexNo(long indexNo) {
		this.indexNo = indexNo;
	}

	public long getIndexNo() {
		return indexNo;
	}


	/**
	 * only for WebFolder
	 * 
	 * @throws java.io.IOException
	 * @throws javax.mail.MessagingException
	 * @return �ڷ�� ����ũ��
	 */
	public String getWebFolderFileSize() throws IOException, MessagingException {
		String retStr = "";
		try {
			retStr = TMailAddress.getAddressString(getTo()).replaceAll("@", "");
		} catch (Exception e) {}
		return retStr;
		// return message.getHeader("To")[0];
	}

	/**
	 * only for WebFolder
	 * 
	 * @throws java.io.IOException
	 * @throws javax.mail.MessagingException
	 * @return �ڷ�� ���� Ȯ����
	 */
	public String getWebFolderFileExt() throws IOException, MessagingException {
		String retStr = "";
		try {
			retStr = TMailAddress.getAddressString(getCc()).replaceAll("@", "");
		} catch (Exception e) {}
		return retStr;
		// return message.getHeader("Cc")[0];
	}
	
	/**
	 * only for WebFolder
	 * @throws javax.mail.MessagingException 
	 * @return �ڷ�� ���� �����
	 */
	public String getWebFolderFileDate() throws MessagingException {
	    Date date = message.getReceivedDate();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
	    
	    if (date == null) {
		return "";
	    }
	    return sdf.format(date);
	}

	

	/** 
	* ����� �뷮�� ���� �ּҰ� ���� ��쿡 
	* ������ ������ �� to�� bcc�� ��巹���� �����ϱ� ���ؼ� �ѹ� ȣ���Ѵ�. 
	* @throws MessagingException 
	*/ 
	public void setDirectRead(boolean directRead) throws MessagingException{
		this.directRead = directRead;
	}
}
		

