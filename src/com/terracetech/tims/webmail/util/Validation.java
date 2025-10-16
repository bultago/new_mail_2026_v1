package com.terracetech.tims.webmail.util;

public class Validation {

    public static boolean isEmlFile(String s) {
        if (isNull(s)) {
            return false;
        }

        String st = s.toUpperCase();

        if (st.endsWith(".EML")) {
            return true;
        }

        return false;
    }

    public static boolean isImageFile(String s) {
        if (isNull(s)) {
            return false;
        }

        String st = s.toUpperCase();

        if (st.endsWith(".JPG") || st.endsWith(".JPEG") || st.endsWith(".GIF") || st.endsWith(".PNG")
                || st.endsWith(".BMP") || st.endsWith(".TIF")) {
            return true;
        }

        return false;

        /*
        * not used regex
        String regPattern = "(?i).*\\.(JPG|JPEG|GIF|PNG|BMP|TIF)$";

        return s.matches(regPattern);
        */
    }

    public static boolean isImageFormat(String s) {
        if (isNull(s)) {
            return false;
        }

        String st = s.toUpperCase();

        if (st.equals("JPG") || st.equals("JPEG") || st.equals("GIF") || st.equals("PNG") || st.equals("BMP")
                || st.equals("TIF")) {
            return true;
        }

        return false;

        /*
        * not used regex
        String regPattern = "(?i).*\\.(JPG|JPEG|GIF|PNG|BMP|TIF)$";

        return s.matches(regPattern);
        */
    }

    public static boolean isHTMLText(String s) {
        if (isNull(s)) {
            return false;
        }

        String st = s.toUpperCase();

        if (st.indexOf("<BR") >= 0 || st.indexOf("<DIV") >= 0 || st.indexOf("<P") >= 0 || st.indexOf("<TABLE") >= 0) {
            return true;
        }

        return false;
    }

    /**
     * Implementation of various methods to validate string
     */
    public static boolean isNull(String s) {
        if (s == null || s.trim().length() < 1) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNull(Object o) {
        if (o == null) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean equals(String s1, String s2) {
        if (isNull(s1) || isNull(s2)) {
            return false;
        }

        return s1.equals(s2);
    }

    public static boolean equalsIgnoreCase(String s1, String s2) {
        if (isNull(s1) || isNull(s2)) {
            return false;
        }

        return s1.equalsIgnoreCase(s2);
    }

    public static boolean isUse(String s) {
        if (isNull(s)) {
            return false;
        }

        return (equalsIgnoreCase(s, "on") || equalsIgnoreCase(s, "true"));
    }

    public static boolean isNumeric(String s) {
        if (isNull(s)) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!isNumeric(c)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAlpha(String s) {
        if (isNull(s)) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!isAlpha(c)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAlphaNumeric(String s) {
        if (isNull(s)) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!isAlphaNumeric(c)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isLength(String s, int len) {
        if (isNull(s)) {
            return false;
        }
        if (s.length() == len) {
            return true;
        }
        return false;
    }

    /*
    * added by doyoung
    * 2006.10.11
    */
    public static boolean isIncWin32InvalidChar(String s) {
        if (isNull(s)) {
            return false;
        }

        byte[] b = null;
        try {
            b = s.getBytes("UTF-8");
        } catch (Exception e) {
        }

        if (b != null) {
            for (int i = 0; i < b.length; i++) {
                char c = (char) b[i];

                if (isIncWin32InvalidChar(c)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isIncWin32InvalidChar(char c) {
        /* 
        * '\' '/' ':' '*' '?' '"' '<' '>' '|' 
        */
        if (c == '\\' || c == '/' || c == ':' || c == '*' || c == '?' || c == '"' || c == '<' || c == '>' || c == '|') {
            return true;
        }

        return false;
    }

    /**
     * Implementation of various methods to validate characters
     */
    public static boolean isNumeric(char c) {
        if (!(c > 47 && c < 58)) {
            return false;
        }
        return true;
    }

    public static boolean isAlpha(char c) {
        if (!(c > 64 && c < 91 || // uppercase letters
        c > 96 && c < 123)) // lowercase letters
        {
            return false;
        }
        return true;
    }

    public static boolean isAlphaNumeric(char c) {
        if (isAlpha(c) || isNumeric(c)) {
            return true;
        }
        return false;
    }

    public static boolean isEmail(String expStr) {

        boolean chkFlag;
        expStr = expStr.trim();
        if ("@localdomain".equalsIgnoreCase(expStr) || "@outbounddomain".equalsIgnoreCase(expStr)) {
            chkFlag = true;
        } else {
            String[] email = expStr.split("@");
            if (email.length != 2) {
                chkFlag = false;
            } else {
                chkFlag = (isValidUid(email[0]) && isDomain(email[1]));
            }
        }

        return chkFlag;

    }
    
    public static boolean isValidateEmail(String str) {

        boolean chkFlag = false;
        if (StringUtils.isNotEmpty(str)) {
            String[] email = str.split("@");
            if (email.length == 2) {
                chkFlag = (isValidUid(email[0]) && isDomain(email[1]));
            } 
        }
        return chkFlag;
    }

    public static boolean isValidUid(String expStr) {

        String validString = "~-.!#$`*+/=?^_{|}";
        if (expStr == null) {
            return false;
        }
        expStr = expStr.trim();
        char[] charArray = expStr.toCharArray();
        if (charArray.length < 1 || charArray.length > 64) {
            return false;
        }

        if (charArray[0] == '.' || charArray[0] == '-' || charArray[charArray.length - 1] == '.'
                || charArray[charArray.length - 1] == '-') {
            return false;
        }

        for (int i = 0; i < charArray.length; i++) {
            if (!isAlpha(charArray[i]) && !isNumeric(charArray[i]) && !validString.contains("" + charArray[i])) {
                return false;
            }
        }

        return true;
    }

    public static boolean isDomain(String expStr) {
        String validString = ".-";
        if (expStr == null) {
            return false;
        }
        expStr = expStr.trim();

        if (expStr.length() > 128) {
            return false;
        }

        if (expStr.indexOf(".") < 0) {
            return false;
        }

        String[] domains = expStr.split("\\.");

        for (int i = 0; i < domains.length; i++) {
            String item = domains[i];
            if (item == "" || item == null || item.length() < 2 || item.length() > 64) {
                return false;
            }
        }

        if (expStr.indexOf(".-") > 0 || expStr.indexOf("-.") > 0) {
            return false;
        }

        char[] charArray = expStr.toCharArray();
        if (charArray[0] == '.' || charArray[0] == '-' || charArray[charArray.length - 1] == '.'
                || charArray[charArray.length - 1] == '-') {
            return false;
        }

        for (int i = 0; i < charArray.length; i++) {
            if (!isAlpha(charArray[i]) && !isNumeric(charArray[i]) && !validString.contains("" + charArray[i])) {
                return false;
            }
        }

        return true;
    }
}
