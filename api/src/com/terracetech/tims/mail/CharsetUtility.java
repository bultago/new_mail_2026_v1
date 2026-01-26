package com.terracetech.tims.mail;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Charset 변환 유틸리티 클래스
 */
public class CharsetUtility {
    
    /**
     * InputStream을 지정된 charset으로 String으로 변환
     * 
     * @param is InputStream
     * @param charset 캐릭터셋
     * @return 변환된 문자열
     * @throws Exception
     */
    public static String convertByteToStr(InputStream is, String charset) throws Exception {
        if (is == null) {
            return "";
        }
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int bytesRead;
        
        while ((bytesRead = is.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }
        
        byte[] bytes = baos.toByteArray();
        
        if (charset == null || charset.trim().isEmpty()) {
            return new String(bytes);
        }
        
        return new String(bytes, charset);
    }
}


