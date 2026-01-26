package com.terracetech.tims.webmail.mail.manager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

public class Image2Cid {

	private Hashtable<String, String> image2cid = null;

	public Image2Cid() {
		image2cid = new Hashtable<String, String>();
	}

	public Hashtable<String, String> getImage2Cid() {
		return image2cid;
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
		SimpleDateFormat sdf = new SimpleDateFormat(
				"SSSssmmHH-12032001-MMddDDDyyyy");
		String defaultcidstr = sdf.format(tm);

		while ((end = worktextLowerCase.indexOf("<img ", start)) != -1) {
			int endpos = worktext.indexOf(">", end + 1) + 1;
			String newstr = "";
			String cidstr = "";

			if (endpos > end) {
				newstr = text.substring(end, endpos);

				int paramstart = newstr.indexOf("viewImage.do?img=") + 17;
				int paramend = newstr.indexOf("\"", paramstart);

				if (paramstart > 17 && paramend > paramstart) {
					cidstr = defaultcidstr + ((cnt < 10) ? (cnt * 100) : cnt);

					image2cid.put(newstr.substring(paramstart, paramend),
							cidstr);

					newstr = replaceImageSrc(text.substring(end, endpos),
							cidstr);
					cnt++;
				}

				sb.append(text.substring(start, end) + newstr);
				start = end + (endpos - end);
			} else {
				break;
			}
		}

		sb.append(text.substring(start));

		return sb.toString();
	}

	private String replaceImageSrc(String imgTag, String cidStr) {

		String newImgTag = "";

		if (imgTag != null && cidStr != null) {
			int start = imgTag.indexOf("src=") + 5;
			int end = imgTag.indexOf("\"", start + 1);

			if (end > start) {
				newImgTag = imgTag.substring(0, start) + "cid:" + cidStr
						+ imgTag.substring(end);
			}
		}

		return newImgTag;
	}

	public boolean isInUploadImage(String text) {
		if (text == null || "".equals(text.trim()))
			return false;

		if (text.indexOf("viewImage.do?img=") > 0) {
			return true;
		} else {
			return false;
		}
	}
}
