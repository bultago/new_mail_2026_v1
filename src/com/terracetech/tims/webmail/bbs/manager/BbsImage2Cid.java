package com.terracetech.tims.webmail.bbs.manager;

import java.net.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.util.*;

public class BbsImage2Cid {
    
    private Hashtable image2cid = new Hashtable();
    private String content = null;
    
    public BbsImage2Cid() {
    }
    
    public BbsImage2Cid(String content) {
	this.content = content;
    }
    
    public String getContent() {
	return content;
    }
    
    public Hashtable getImage2Cid() {
	return image2cid;
    }
    
    public String parseUploadImage() {
	return parseUploadImage(content);
    }
    
    public String parseUploadImage(String text) {
	
	String worktext = text;
	String worktextLowerCase = text.toLowerCase();
	StringBuffer sb = new StringBuffer();
	int start = 0;
	int end = 0;
	int cnt = 0;
	
	if (!isInUploadImage(text)) {
	    return text;
	}
	
	Date tm = new Date();
	SimpleDateFormat sdf =
	    new SimpleDateFormat("SSSssmmHH-12032001-MMddDDDyyyy");
	String defaultcidstr = sdf.format(tm);
	
	while ((end = worktextLowerCase.indexOf("<img ", start)) != -1) {
	    int endpos = worktext.indexOf(">", end+1)+1;
	    String newstr = "";
	    String cidstr = "";
	    String sizestr = "";
	    
	    if (endpos > end) {
		newstr = text.substring(end, endpos);
		
		// Size 저장 - by hkkim - 2007.09.22
		int sizestart = newstr.indexOf("style=") + 7;
		int sizeend   = newstr.indexOf("\"", sizestart);

		if (sizeend > sizestart) {
		    sizestr = "style=\""+newstr.substring(sizestart, sizeend)+"\"";
		}
		// Size 저장 - by hkkim - 2007.09.22

		int paramstart = newstr.indexOf("viewImage.do?img=") + 17;
		int paramend   = newstr.indexOf("\"", paramstart);
		
		int signstart 	= newstr.indexOf("viewSignImage.do?") + 17;
		int signend    	= newstr.indexOf("\"", signstart);

		cidstr = defaultcidstr + ((cnt < 10) ? ("0" + cnt) : cnt);

		if (paramstart > 17 && paramend > paramstart) {
		    image2cid.put(
			newstr.substring(paramstart, paramend), cidstr);
		    
		    newstr = "<img "+sizestr+" src=\"cid:" + cidstr + "\">";
		    cnt++;
		} else if (signstart > 17 && signend > signstart) {
		    String keyValue = getKeyValue(newstr, cnt);
		    
		    if (!Validation.isNull(keyValue)) {
			image2cid.put(keyValue, cidstr);
			newstr = "<img "+sizestr+" src=\"cid:" + cidstr + "\">";
			cnt++;
		    }
		}
		
		sb.append(text.substring(start, end) + newstr);
		start = end + (endpos - end);
	    } else {
		break;
	    }
	}
	
	sb.append(text.substring(start));
	String text_ = sb.toString();
	
	
	
	StringBuffer sb_ = new StringBuffer();
	start = 0;
	end = 0;
	while ((end = text_.indexOf("<v:imagedata ", start)) != -1) {
	    int endpos = text_.indexOf(">", end+1)+1;
	    String newstr = "";
	    String cidstr = "";
	    
	    if (endpos > end) {
		newstr = text_.substring(end, endpos);
		
		int paramstart = newstr.indexOf("viewImage.do?img=") + 17;
		int paramend   = newstr.indexOf("\"", paramstart);
		
		if (paramstart > 17 && paramend > paramstart) {
		    cidstr = defaultcidstr + cnt;
		    image2cid.put(
			newstr.substring(paramstart, paramend), cidstr);
		    
		    newstr = "<v:imagedata src=\"cid:" + cidstr + "\">";
		    // newstr = "<img src=\"cid:" + cidstr + "\">";
		    cnt++;
		}
		
		sb_.append(text_.substring(start, end) + newstr);
		start = end + (endpos - end);
	    } else {
		break;
	    }
	}
	
	sb_.append(text_.substring(start));
	// return sb_.toString().replaceAll("</v:imagedata>", "");
	
	return sb_.toString();
    }
    
    public String getKeyValue(String str, int cnt) {
	if (str == null) {
	    return null;
	}
	
	int start 	= str.indexOf("src=\"") + 5;
	int end		= str.indexOf("\"", start);
	String linkStr 	= "";
	
	Date date = new Date();
	String fileName = date.getTime() + "_" + cnt + ".img";
	String filePath = EnvConstants.getBasicSetting("tmpdir") + "/" + fileName;
	
	int size = 0;
	
	if (start > 5 && end > start) {
	    linkStr = str.substring(start, end);
	    linkStr = linkStr.replaceAll("&amp;", "&");
	    
	    try {
		URL url = new URL(linkStr);
		
		File file = new File(filePath);
		
		InputStream in = url.openStream();
		OutputStream os = new FileOutputStream(file);
		
		byte[] buffer = new byte[1024];
		
		int n = 0;
		
		while((n = in.read(buffer)) != -1) {
		    os.write(buffer, 0, n);
		    size += n;
		}
		
		os.close();
		in.close();
	    } catch (Exception ex) {
		System.out.println("-- ERR : ["+ex.getMessage()+"]");
	    }
	}
	
	return (size > 0) ? fileName + "|signimage.jpg" : null;
    }
    
    
    public boolean isInUploadImage() {
	return isInUploadImage(content);
    }
    
    public boolean isInUploadImage(String text) {
	if (text.indexOf("viewImage.do?img=") > 0
	    || text.indexOf("viewSignImage.do?") > 0) {
	    return true;
	} else {
	    return false;
	}
    }
}

