package com.terracetech.tims.mail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.MalformedInputException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.CharsetUtility;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;
import javax.mail.internet.MimeUtility;

import org.alfresco.jlan.util.UTF8Normalizer;
import org.apache.commons.io.IOUtils;

import net.freeutils.tnef.Attachment;
import net.freeutils.tnef.Attr;
import net.freeutils.tnef.CompressedRTFInputStream;
import net.freeutils.tnef.MAPIProp;
import net.freeutils.tnef.MAPIProps;
import net.freeutils.tnef.RawInputStream;
import net.freeutils.tnef.TNEFInputStream;
import net.freeutils.tnef.TNEFUtils;
import net.freeutils.tnef.mime.RawDataSource;
import net.freeutils.tnef.mime.TNEFMime;
import net.freeutils.tnef.mime.TNEFMimeBodyPart;
import net.freeutils.tnef.mime.TNEFMimeMessage;

import com.sun.mail.imap.IMAPMessage;

public class TMailPart {

	private String attachMimeSubject = null;
    private String path 	= null;
    private Part part 		= null;
    private String text 	= null;
    private String imagename = null;
    private boolean messagerfc822 = false;
    private boolean tnefType = false;
    private boolean deleteAttachFlag = false;
    private TMailTnefAttach[] tnefFiles = null;
    private Map<String, TMailTnefAttach> tnefAttachMap = null;

    private boolean directRead = false;

    public TMailPart(Part part) throws MessagingException {
        this.path = "";
        this.part = part;
	}

    public TMailPart(String path, Part part) {
        this.path = path;
        this.part = part;

		try {
			String description = part.getDescription();
			deleteAttachFlag = "DELETE-ATTACH".equals(description);

			if (part.isMimeType("text/richtext")) {
				StringBuffer sb	= new StringBuffer();
				byte[] buffer 	= new byte[1024];
				InputStream is 	= part.getInputStream();
				while ((int)is.read(buffer) != -1) {
					sb.append(new String(buffer));
					buffer 	= new byte[1024];
				}

				text = sb.toString();
			}

			if (part.isMimeType("message/rfc822")) {
            	Object msg = part.getContent();
            	if(msg instanceof IMAPMessage){
            		attachMimeSubject = ((IMAPMessage)msg).getSubject();
            	}
			}

			if (part.isMimeType("application/ms-tnef")) {

				try{
				tnefAttachMap = new HashMap<String, TMailTnefAttach>();
				int pos = 0;
				extractTnefContent(pos, tnefAttachMap, new TNEFInputStream(part.getInputStream()));
				if(tnefAttachMap.size() > 0){
					tnefFiles = new TMailTnefAttach[tnefAttachMap.size()];
					Iterator<String> iter = tnefAttachMap.keySet().iterator();
					int i = 0;
					while (iter.hasNext()) {
						tnefFiles[i] = tnefAttachMap.get(iter.next());
						i++;
					}
					tnefType = true;
				} else {
					tnefType = false;
				}
				}catch (RuntimeException re) {
					tnefType = false;
				}
				catch (Exception e) {
					tnefType = false;
				}
			}
		} catch (MessagingException e) {
		} catch (IOException e1) {}
    }

    public Object getContent() throws IOException, MessagingException {
        	return part.getContent();
    }

    public String getContentID() throws MessagingException {
        return ((MimePart) part).getContentID();
    }

    public String getEncoding() throws MessagingException {
        return ((MimePart) part).getEncoding();
    }

    public String getCharset() throws MessagingException {
		String contentType 	= getContentType();
		String charset 		= null;

		int s = contentType.indexOf("charset=");

		if (s != -1)  {
			int e = contentType.indexOf(";", s);

			charset = (e != -1) ?
				contentType.substring(s + 8, e) : contentType.substring(s + 8);
		} else {
			   charset = MimeUtility.getDefaultJavaCharset();
		}

		return charset;
    }

    @SuppressWarnings("unchecked")
	public String getCharsetForOnePart() throws MessagingException {

		String contentType 	= getContentType();
		String charset 		= null;

		int s = contentType.indexOf("charset=");

		if (s != -1)  {
			int e = contentType.indexOf(";", s);

			charset = (e != -1) ?
				contentType.substring(s + 8, e) : contentType.substring(s + 8);
		} else {

			boolean existsSubject = false;
			String subject = "";
			boolean existsFrom = false;
			String from = "";
			javax.mail.Header hd = null;
			Enumeration<javax.mail.Header> enumer = part.getAllHeaders();
		    while (enumer.hasMoreElements()) {
				hd = (javax.mail.Header)enumer.nextElement();
				if("Subject".equalsIgnoreCase(hd.getName())){
					subject = hd.getValue();
					existsSubject = true;
				}
				if("From".equalsIgnoreCase(hd.getName())){
					from = hd.getValue();
					existsFrom = true;
				}
		    }
		    //### Subject : =?iso-2022-jp?B?GyRCJCo/PSQ3OX4kXyQsNDBOOyQ3JF4kNyQ/GyhC?=
		    if(existsSubject){
		    	// 마임 인코딩 되어 있으면, charset을 가져온다.
		    	if (subject != null && subject.indexOf("=?") != -1 && subject.startsWith("=?")){
		    		subject = subject.substring(subject.indexOf("=?") + 2, subject.length());
		    		if (subject.indexOf("?") != -1 ){
		    			charset = subject.substring(0, subject.indexOf("?"));
		    			if(charset == null || charset.length() == 0){
		    				charset = MimeUtility.getDefaultJavaCharset();
		    			}
		    		}else{
		    			charset = MimeUtility.getDefaultJavaCharset();
		    		}
		    	}else{
		    		charset = MimeUtility.getDefaultJavaCharset();
		    	}
		    }else{
		    	if(existsFrom){
			    	// 제목없는 메일도 있기  때문에 From의 charset 값을 가져온다.
			    	// 마임 인코딩 되어 있으면, charset을 가져온다.
			    	if (from != null && from.indexOf("=?") != -1 && from.startsWith("=?")){
			    		from = from.substring(from.indexOf("=?") + 2, from.length());
			    		if (from.indexOf("?") != -1 ){
			    			charset = from.substring(0, from.indexOf("?"));
			    			if(charset == null || charset.length() == 0){
			    				charset = MimeUtility.getDefaultJavaCharset();
			    			}
			    		}else{
			    			charset = MimeUtility.getDefaultJavaCharset();
			    		}
			    	}else{
			    		charset = MimeUtility.getDefaultJavaCharset();
			    	}
		    	}else{
		    		charset = MimeUtility.getDefaultJavaCharset();
		    	}
		    }
		}

		return charset;
    }

	public String getDisposition() throws MessagingException {
		return part.getDisposition();
    }

    public String getContentType() throws MessagingException {
        return part.getContentType();
    }

    public String getFileName() {
        String fileName = "unknown"+path;

        try {
			/*
			 IOS 에서 첨부를 하면
			 disposition : attachment;
				filename*=euc-kr''%C0%E5%BE%D6%B4%EB%C0%C0%5F%B8%F0%C0%C7%C8%C6%B7%C3%5F2015%B3%E2%5F5%C2%F7%B5%EE%B1%E2%5F%B0%E8%C8%B9%BC%AD%5FV1.1.hwp
			형태로 헤더에 박혀있다.
			아래는 따로 처리를 위해 타는 로직 추가
			 */
			String[] dispositions = getHeader("Content-Disposition");
			String disposition = null;
			if(dispositions != null && dispositions.length > 0){
				if(dispositions != null && dispositions.length > 0){
					disposition = dispositions[0];
					disposition = disposition.replaceAll("[\t\n\r]", " ");
					if(disposition.indexOf(";") != -1){
					   String[] tempArr = disposition.split(";");
					   String encodeCharset = "UTF-8";
					   String encodedFileName = "";
					   int encodedFileNameCount = 0;
					   String[] tempArr2 = null;
					   String[] iosEncFullFileName = null;
					   if(tempArr != null && tempArr.length >= 2){

						   for (int i = 1; i < tempArr.length; i++) {
							   if(tempArr[i] != null){
								   //System.out.println("### tempArr["+i+"] : "+tempArr[i]);
								   // IOS 에서 첨부를 하는 형태 "filename*=" 이다.
								   // IOS 에서 첨부가 길어질 경우에는 0, 1 순으로 이어진다 ("filename*0*=", "filename*1*=")
								   if(tempArr[i].trim().toLowerCase().startsWith("filename*")){
									   encodedFileNameCount++;
								   }
							   }
					      	}

						   for (int i = 1; i < tempArr.length; i++) {
							   if(tempArr[i] != null){
								   if(tempArr[i].trim().toLowerCase().startsWith("filename*")){
									   if(tempArr[i].indexOf("=") != -1){
										   iosEncFullFileName = tempArr[i].split("=");
										   if(iosEncFullFileName[1].indexOf("''") != -1){
											   tempArr2 = iosEncFullFileName[1].split("''");
											   encodeCharset = tempArr2[0];
											   // First 처리
											   if (encodedFileNameCount == 1) {
												   encodedFileName = tempArr2[1];
												   break;
											   }else{
												   encodedFileName += tempArr2[1];
											   }
										   } else {
											   encodedFileName += iosEncFullFileName[1];
										   }
									   }
								   }
							   }
					      	} // end for
						   if(encodedFileName.length() >0){
							   System.out.println("#### TMailPart - IOS encodedFileName : "+encodedFileName);
							   fileName = URLDecoder.decode(encodedFileName, encodeCharset);
							   System.out.println("#### TMailPart - IOS fileName : "+fileName);
						   }
						 }
					}
				}
			}

			if(fileName.startsWith("unknown"+path)){
				fileName = part.getFileName();

	        	if (fileName != null) {
	        		try{
	        			fileName = MimeUtility.decodeText(fileName);
	        			//System.out.println("#### TMailPart - decodeText fileName : "+fileName);
	        		}catch (Exception e) {
						fileName = "Unknown";
					}


					/*
					 * added by doyoung
					 * escape character ( \ / : * ? " < > | )
					 * 2006.02.02
					 */
					fileName = fileName.replaceAll("\\\\", "");
					fileName = fileName.replaceAll("[\t\r\n]", " ");
					fileName = fileName.replaceAll("[/:*?\"<>|]", "_");
		        /* filename rule add by hkkim - 2008.06.30 S */
	            }else if (part.isMimeType("message/rfc822")) {
	            	Object msg = part.getContent();
	            	if(attachMimeSubject != null){
	            		fileName = attachMimeSubject;
	            	} else {
	            		fileName = ((Message)msg).getSubject();
	            	}
	            	if(fileName != null){
	            		fileName = fileName.replaceAll("\\\\", "");
	    				fileName = fileName.replaceAll("[\t\r\n]", " ");
	    				fileName = fileName.replaceAll("[/:*?\"<>|]", "_");
	    				fileName = fileName +".eml";
	            	} else {
	            		fileName = "unknown.eml";
	            	}

	        	}else if (part.isMimeType("message/delivery-status")) {
	        		//20170112 TCUSTOM-2627 - bugfix  
	            	Object msg = null;
	            	try {
	            		msg = part.getContent();
	            	} catch (Exception e) {
	            		msg = "";
	            	}

	            	if(attachMimeSubject != null){
	            		fileName = attachMimeSubject;
	            	} else {
	            		if(msg instanceof String){
	            			fileName = "delivery-status.txt";
	            		}else{
	            			fileName = "unknown.txt";
	            		}
	            	}

	        	} else {
					fileName = "unknown"+path;
				}
			}


			//TCUSTOM-3466 201708
			if(fileName.indexOf("=_utf-8_B_") > -1) {
				try {
					String[] descriptions = getHeader("Content-Description");
					if(descriptions != null && descriptions.length > 0){
						fileName = MimeUtility.decodeText(descriptions[0]);
					}
				} catch (Exception ex){
					ex.printStackTrace();
				}
			}


			// TCUSTOM-2121
			fileName = UTF8Normalizer.getInstance().normalize(fileName);
		} catch (MessagingException e) {
			System.out.println("-- MyPart.getFileName."
				+"MessagingException : ["+e.getMessage()+"]");
		} catch (IOException e) {
			System.out.println("-- MyPart.getFileName."
				+"UnsupportedEncodingException : ["+e.getMessage()+"]["
				+fileName+"]");
		} catch(IndexOutOfBoundsException e){
			System.out.println("-- MyPart.getFileName."
					+"IndexOutOfBoundsException : ["+e.getMessage()+"]["
					+fileName+"]");
		}


		String tmpFileName = "";
	
		//20170112 TCUSTOM-2627 - bugfix 
		if(fileName != null){
			for(int i=0; i<fileName.length(); i++){
				if(!"202e".equals(Integer.toHexString(fileName.charAt(i)))){
					tmpFileName += fileName.charAt(i);
				}
			}

			if(!fileName.equals(tmpFileName)){
				fileName = tmpFileName;
			}
		}

        return fileName;
    }

    public void createImageFile(String dir) throws IOException,
        MessagingException {
    	createImageFile(dir, getFileName());
    }

    public void createImageFile(String dir, String name) throws IOException,
        MessagingException {

        InputStream in = part.getInputStream(); // buffered inputstream

        File file = new File(dir + '/' +  name);
        file.createNewFile();
        file.deleteOnExit();

        FileOutputStream out = new FileOutputStream(file);

        int n = 0;
		byte[] buffer = new byte[1024];

		while ((n = in.read(buffer, 0, buffer.length)) != -1) {
            out.write(buffer, 0, n);
		}

        out.close();
        in.close();
    }

    public String getPath() {
		return (path != null ? path : "");
    }

    public Part getPart() {
		return part;
    }

    public String[] getHeader(String s) throws MessagingException {
		return part.getHeader(s);
    }

    public Date getSentDate() throws MessagingException {
        return ((Message) part).getSentDate();
	}

    public String getSentDate2() throws MessagingException {
        Date date = getSentDate();

        if (date == null) {
            return null;
        }

        SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy/MM/dd EEEE a h:mm:ss");

        return sdf.format(date);
    }

    public int getSize() throws MessagingException {
    	long size = (deleteAttachFlag)?0:part.getSize();
        return (int)(size);
    }

    public int getSize75() throws MessagingException{
    	long size = (deleteAttachFlag)?0:(long)(part.getSize() * .75);
    	return (int) (size);
    }

    public String getSubject() throws MessagingException {
        return ((Message) part).getSubject();
    }

    @SuppressWarnings("unchecked")
	public String getText() throws IOException, MessagingException {
		if (text == null) {
			try{
				Object obj = part.getContent();
				String charset = "UTF-8";
				boolean isOnePart = false;
				if (obj instanceof String) {
					Enumeration<javax.mail.Header> enumer = part.getAllHeaders();
					javax.mail.Header hd = null;

					//특정 원문은 text 파트의 헤더가 없고, 전체 원문 자체가 하나의 part라고 인식하므로,
					//해당 part header에 Message-ID 가 있다면 하나의 part라고 인지하여,
					//원문의 charset 디코딩을 해야함.

				    while (enumer.hasMoreElements()) {
						hd = (javax.mail.Header)enumer.nextElement();
						if(hd != null){
							if("Message-ID".equalsIgnoreCase(hd.getName())){
								isOnePart = true;
								break;
							}
						}
				    }
				    if(isOnePart){
				    	charset = getCharsetForOnePart();
						if(charset != null){
							if("cp932".equalsIgnoreCase(charset))
								charset = "iso-2022-jp";
						}
				    }
				}
				if(isOnePart){
					text = CharsetUtility.convertByteToStr(part.getInputStream(), charset);
				}else{
					charset = getCharset();
					if("ks_c_5601-1987".equalsIgnoreCase(charset)
					|| "s_c_5601-1987".equalsIgnoreCase(charset)
						){
						charset = "MS949";
					}
					text = CharsetUtility.convertByteToStr(part.getInputStream(), charset);
				}
			} catch(Exception e) {
				e.printStackTrace();
				Object obj = null;
				try {
					obj = part.getContent();
					if (obj instanceof String) {
						text = (String) obj;
					}
					else {
						text = getTextContent();
					}
				} catch (IOException ie) {
					try {
						MimeBodyPart bodyPart = (MimeBodyPart) part;
						InputStream stream = MimeUtility.decode(bodyPart.getRawInputStream(), "7bit");
						text = IOUtils.toString(stream, getCharset());
					} catch (Exception ex) {
						System.out.println("contents convert fail : "+ex.getMessage());
					}
				}
        	}
		}

        return text;
    }

    public String getTextContent() throws IOException, MessagingException {
		/*
		InputStream is = getInputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		int n = 0;
		byte[] buffer = new byte[1024];

		while ((n = is.read(buffer, 0, buffer.length)) != -1) {
			baos.write(buffer, 0, n);
		}

		return baos.toString();
		*/

		String enc = getCharset();
		System.out.println("### getTextContent() - charset : "+enc);
		InputStreamReader is = null;

		try {
			is = new InputStreamReader(
				getInputStream(), MimeUtility.javaCharset(enc));
		}
		catch (UnsupportedEncodingException e) {
			is = new InputStreamReader(
				getInputStream(), MimeUtility.getDefaultJavaCharset());
		}

		int count;
		char buf[] = new char[8192];

		try {
			StringBuffer sb = new StringBuffer();

			while ((count = is.read(buf, 0, 8192)) != -1) {
				sb.append(buf, 0, count);
			}

			return sb.toString();
		}
		catch (MalformedInputException e) {
			is = new InputStreamReader(getInputStream(), "8859_1");
			StringBuffer sb = new StringBuffer();

			while ((count = is.read(buf, 0, 8192)) != -1) {
				sb.append(buf, 0, count);
			}

			return sb.toString();
		}
	}

    public InternetAddress[] getFrom() throws MessagingException {
        return (InternetAddress[]) ((Message) part).getFrom();
    }

	public String getFromString() throws MessagingException {
		InternetAddress[] ias = getFrom();
		return TMailAddress.getAddressString(ias);
	}

	public InternetAddress[] getTo() throws MessagingException {

		InternetAddress[] ias = null;
		if (directRead )
			ias = TMailUtility.getAllAddressFromSource(ias, part, "to");
		else
			ias = (InternetAddress[]) ((Message) part).getRecipients(Message.RecipientType.TO);

		return ias;
	}

	public String getToString() throws MessagingException {
		InternetAddress[] ias = getTo();
		return TMailAddress.getAddressString(ias);
	}

    public InternetAddress[] getCc() throws MessagingException {
		InternetAddress[] ias = null;
		if (directRead)
			ias = TMailUtility.getAllAddressFromSource(ias, part, "to");
		else
			ias = (InternetAddress[]) ((Message) part).getRecipients(Message.RecipientType.CC);

        return ias;
    }

	public String getCcString() throws MessagingException {
		InternetAddress[] ias = getCc();
		return TMailAddress.getAddressString(ias);
	}

    public boolean isMessageRFC822() {
        return messagerfc822;
    }

    public boolean isMimeType(String mimeType) throws MessagingException {
        return part.isMimeType(mimeType);
    }

	/*
	 * added by doyoung
	 * 2006.01.18
	 */
    public boolean isMimeTypeNew(String mimeType) throws MessagingException {
		if (part.isMimeType(mimeType)) {
			return true;
		}

		String ctype = part.getContentType();

		if (ctype != null && ctype.indexOf(mimeType) >= 0) {
			return true;
		}

		return false;
    }

    public boolean isImageType() throws MessagingException {
        return isMimeType("image/*");
    }

    public void setMessageRFC822(boolean messagerfc822) {
        this.messagerfc822 = messagerfc822;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setImageName(String imagename) {
        this.imagename = imagename;
    }

    public String getImageName() {
		return imagename;
    }

	/*
	* added by doyoung
	* 2006.10.10
	*/
	public InputStream getInputStream() throws IOException, MessagingException {
		return part.getInputStream();
	}

	public void setDirectRead(boolean directRead) throws MessagingException{
		this.directRead = directRead;
		this.getTo();
		this.getCc();
	}

	private void extractTnefContent(int pos, Map<String, TMailTnefAttach> tnefMap,
			net.freeutils.tnef.Message message)
	throws IOException {
        List<Attachment> attachments = message.getAttachments();
        String key = "tnef_";
        String fileName = null;

        net.freeutils.tnef.Message nestedMessage = null;
		Session session = null;
		TNEFMimeMessage mime = null;
        Attachment attachment = null;
        TMailTnefAttach attach = null;
        Attr attr = null;
        Multipart mp = null;
        ByteArrayOutputStream baos = null;
        TMailMessage tMessage = null;
        for (int i = 0; i < attachments.size(); i++) {
            attachment = (Attachment)attachments.get(i);

            if (attachment.getNestedMessage() != null) { // nested message
            	//extractTnefContent(pos,tnefMap, attachment.getNestedMessage());
            	//extractTnefContent(pos,tnefMap, attachment.getNestedMessage());
            	nestedMessage = attachment.getNestedMessage();

        		session = Session.getInstance(new Properties());
        		mime = new TNEFMimeMessage(session);
        		// add TNEF attributes
                mime.setTNEFAttributes(nestedMessage.getAttributes());
                // translate TNEF attributes to mime
                // original headers

                try{
	                addHeaders(nestedMessage, mime);
	                // from
	                attr = nestedMessage.getAttribute(Attr.attFrom);
	                if (attr != null) {
	                    net.freeutils.tnef.Address address = (net.freeutils.tnef.Address)attr.getValue();
	                    mime.setFrom(new InternetAddress(address.getAddress(), address.getDisplayName()));
	                }
	                // date sent
	                attr = nestedMessage.getAttribute(Attr.attDateSent);
	                if (attr != null)
	                    mime.setSentDate((Date)attr.getValue());
	                // recipients
	                addRecipients(nestedMessage, mime);
	                // subject
	                attr = nestedMessage.getAttribute(Attr.attSubject);
	                if (attr != null)
	                    mime.setSubject((String)attr.getValue());
	                //System.out.println("### mime.getSubject() : "+mime.getSubject());

	                // body
	                mp = new MimeMultipart();
	                addBody(nestedMessage, mp);

	                //System.out.println("### mime.getAllHeaderLines() : "+mime.getAllHeaderLines());

	                // attachments and nested messages
	                addAttachments(nestedMessage, mime, mp, session);

	                //System.out.println("### mime.getContent() : "+mp.getCount());


	                // finish up
	                if (mp.getCount() > 0)
	                    mime.setContent(mp);
	                else
	                    mime.setContent("", "text/plain; charset=utf-8"); // because an empty multipart is illegal

	                //System.out.println("### mime.getContent() : "+mime.getContent());

	                mime.saveChanges();
                }catch(Exception e){
                	e.printStackTrace();
                }

            	fileName = attachment.getFilename();
        		fileName = new String(fileName.getBytes("8859_1"));
        		fileName = fileName.replaceAll("\\\\", "");
				fileName = fileName.replaceAll("[\t\r\n]", " ");
				fileName = fileName.replaceAll("[/:*?\"<>|]", "_");
				//System.out.println("### fileName : "+fileName);
				attach = new TMailTnefAttach();
				attachment = new Attachment();

				try{
					tMessage = new TMailMessage(mime);
					baos = new ByteArrayOutputStream();
					tMessage.writeTo(baos);
					attachment.setRawData(new RawInputStream(baos.toByteArray()));
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					try{if(baos != null){baos.flush(); baos.close();}}catch(Exception e){}
				}

				attach.setAttachment(attachment);
				attach.setFileName(fileName+".eml");
				attach.setAttachKey(key+pos);
            	tnefMap.put(key+pos,attach);
            	pos++;
            } else {
            	fileName = attachment.getFilename();
        		fileName = new String(fileName.getBytes("8859_1"));
        		fileName = fileName.replaceAll("\\\\", "");
				fileName = fileName.replaceAll("[\t\r\n]", " ");
				fileName = fileName.replaceAll("[/:*?\"<>|]", "_");

				attach = new TMailTnefAttach();
				attach.setAttachment(attachment);
				attach.setFileName(fileName);
				attach.setAttachKey(key+pos);
            	tnefMap.put(key+pos,attach);
            	pos++;
            }
        }
    }

	private void extractTnefContent(int pos, Map<String, TMailTnefAttach> tnefMap,
			TNEFInputStream in)throws IOException {
		extractTnefContent(pos, tnefMap, new net.freeutils.tnef.Message(in));
	}

	public boolean isTnefType() {
		return tnefType;
	}

	public TMailTnefAttach[] getTnefFiles() {
		return tnefFiles;
	}

	public Map<String, TMailTnefAttach> getTnefAttachMap() {
		return tnefAttachMap;
	}

	@SuppressWarnings("unchecked")
	protected void addHeaders(net.freeutils.tnef.Message message, TNEFMimeMessage mime)
            throws IOException, MessagingException {
        MAPIProps props = message.getMAPIProps();
        if (props != null) {
            String headers = (String)props.getPropValue(MAPIProp.PR_TRANSPORT_MESSAGE_HEADERS);
            //System.out.println("### headers : "+headers);
            if (headers != null) {
                InternetHeaders ih = new InternetHeaders(
                    new ByteArrayInputStream(headers.getBytes("ISO8859_1")));
                Enumeration<Header> e = ih.getAllHeaders();
                while (e.hasMoreElements()) {
                    Header h = (Header)e.nextElement();
                    mime.addHeader(h.getName(), h.getValue());
                }
            }
        }
    }

	protected void addRecipients(net.freeutils.tnef.Message message, TNEFMimeMessage mime)
            throws MessagingException, IOException {
        Attr attr;
        attr = message.getAttribute(Attr.attRecipTable);
        if (attr != null) {
            // remove previously set recipients (from original headers)
            mime.removeHeader("To");
            mime.removeHeader("Cc");
            mime.removeHeader("Bcc");
            String name = null;
            String address = null;
            InternetAddress internetAddress = null;
            int type = 0;
            // add recipients
            for (MAPIProps recipient : (MAPIProps[])attr.getValue()) {
                name = (String)recipient.getPropValue(MAPIProp.PR_DISPLAY_NAME);
                address = (String)recipient.getPropValue(MAPIProp.PR_EMAIL_ADDRESS);
                internetAddress = new InternetAddress(address, name);
                type = (Integer)recipient.getPropValue(MAPIProp.PR_RECIPIENT_TYPE);

                //System.out.println("### name["+name+"], address["+address+"], type["+type+"] ");

                javax.mail.Message.RecipientType recipientType;
                switch (type) {
                    case MAPIProp.MAPI_TO: recipientType = javax.mail.Message.RecipientType.TO; break;
                    case MAPIProp.MAPI_CC: recipientType = javax.mail.Message.RecipientType.CC; break;
                    case MAPIProp.MAPI_BCC: recipientType = javax.mail.Message.RecipientType.BCC; break;
                    default: throw new IllegalArgumentException("invalid PR_RECIPIENT_TYPE: " + type);
                }
                mime.addRecipient(recipientType, internetAddress);
            }
        }
    }

	protected Multipart addBody(net.freeutils.tnef.Message message, Multipart mp)
            throws IOException, MessagingException {
        Attr attr;
        attr = message.getAttribute(Attr.attBody);
        if (attr != null) {
            String text = (String)attr.getValue();
            addTextPart(mp, text, "text/plain");
        }

        MAPIProps props = message.getMAPIProps();
        if (props != null) {
            // compressed RTF body
            RawInputStream ris = (RawInputStream)props.getPropValue(MAPIProp.PR_RTF_COMPRESSED);
            //System.out.println("### ris["+ris+"]");
            if (ris != null) {
                try {
                    // we use readSafely with a CRTF stream rather than the faster array-based
                    // CompressedRTFInputStream.decompressRTF to prevent OOME/DoS
                    // due to maliciously crafted uncompressed length field
                    CompressedRTFInputStream rtfStream = new CompressedRTFInputStream(ris);
                    byte[] rtfBytes = TNEFUtils.readSafely(
                            rtfStream, rtfStream.getUncompressedSize(), 1024 * 1024);

                    //System.out.println("### rtfBytes["+new String(rtfBytes)+"]");
                    addTextPart(mp, new String(rtfBytes), "text/rtf");
                } finally {
                    ris.close();
                }
            } else {
                // HTML body (either PR_HTML or PR_BODY_HTML - both have the
                // same ID, but one is a string and one is a byte array)
                Object html = props.getPropValue(MAPIProp.PR_HTML);
                //System.out.println("### html["+html+"]");
                if (html != null) {
                    String text;
                    if (html instanceof RawInputStream) {
                        ris = (RawInputStream)html;
                        try {
                            text = new String(ris.toByteArray(), "UTF-8");
                        } finally {
                            ris.close();
                        }
                    } else {
                        text = (String)html;
                    }
                    addTextPart(mp, text, "text/html");
                }
            }
        }
        return mp;
    }

    @SuppressWarnings("resource")
	protected void addAttachments(net.freeutils.tnef.Message message, TNEFMimeMessage mime, Multipart mp, Session session)
            throws MessagingException, IOException {
        for (Object one : message.getAttachments()) {
        	if(!(one instanceof Attachment)){
        		return;
        	}
        	Attachment attachment = (Attachment)one;
            TNEFMimeBodyPart part = new TNEFMimeBodyPart();
            if (attachment.getNestedMessage() == null) {
                // add TNEF attributes
                part.setTNEFAttributes(attachment.getAttributes());
                // translate TNEF attributes to Mime
                String filename = attachment.getFilename();
                if (filename != null)
                    part.setFileName(filename);
                String mimeType = null;
                if (attachment.getMAPIProps() != null)
                    mimeType = (String)attachment.getMAPIProps().getPropValue(MAPIProp.PR_ATTACH_MIME_TAG);
                if (mimeType == null && filename != null)
                    mimeType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(filename);
                if (mimeType == null)
                    mimeType = "application/octet-stream";
                DataSource ds = new RawDataSource(attachment.getRawData(), mimeType, filename);
                part.setDataHandler(new DataHandler(ds));
                mp.addBodyPart(part);
            } else { // nested message
                MimeMessage nested = TNEFMime.convert(session, attachment.getNestedMessage());
                part.setDataHandler(new DataHandler(nested, "message/rfc822"));
                mp.addBodyPart(part);
            }
        }
    }

    public static MimeBodyPart addTextPart(Multipart mp, String text, String contentType)
            throws MessagingException {
    	MimeBodyPart part = null;
    	try{
	        part = new MimeBodyPart();
	        part.setText(text, "UTF-8");
	        part.setHeader("Content-Type", contentType + "; charset=utf-8");
	        if("text/rtf".equalsIgnoreCase(contentType)){
	        	part.addHeader("Content-Transfer-Encoding", "7bit");
	        	part.addHeader("Content-Disposition", "attachment; filename=content.rtf");
	        }else{
	        	part.addHeader("Content-Transfer-Encoding", "base64");
	        }
	        mp.addBodyPart(part);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
        return part;
    }
}
