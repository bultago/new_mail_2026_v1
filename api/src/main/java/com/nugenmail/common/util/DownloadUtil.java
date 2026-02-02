package com.nugenmail.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Utility for handling file downloads.
 * 
 * @author nugenmail
 */
public class DownloadUtil {

    /**
     * Encodes filename for different browsers.
     * 
     * @param fileName  Original filename
     * @param userAgent Browser User-Agent string
     * @return Encoded filename safe for Content-Disposition header
     */
    public static String getDownloadFileName(String fileName, String userAgent) {
        if (fileName == null) {
            fileName = "unknown_file";
        }
        String newFileName;
        try {
            if (userAgent != null
                    && (userAgent.contains("MSIE") || userAgent.contains("Trident") || userAgent.contains("Edge"))) {
                newFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", " ");
            } else {
                // Standard (Chrome, Firefox, Safari)
                newFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            }
        } catch (UnsupportedEncodingException e) {
            newFileName = "unknown_file";
        }
        return newFileName;
    }
}
