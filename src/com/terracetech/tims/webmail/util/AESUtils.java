package com.terracetech.tims.webmail.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
 
public class AESUtils {
 
        public static byte[] hexToByteArray(String hex) {
                if (hex == null || hex.length() == 0) {
                        return null;
                }
 
                byte[] ba = new byte[hex.length() / 2];
                for (int i = 0; i < ba.length; i++) {
                        ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
                }
                return ba;
        }
 
        public static String decryptPass(String encryptPass) {
                SecretKeySpec skeySpec = null;
                Cipher cipher = null;
                byte[] byteArray = null;
                String decryptPass = null;
                try {
                        String key = "tErRaCeNtWeBmAiL";
 
                        skeySpec = new SecretKeySpec(key.getBytes(), "AES");
                        cipher = Cipher.getInstance("AES/ECB/NoPadding");
                        byteArray = hexToByteArray(encryptPass);
                        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
 
                        byte[] original = cipher.doFinal(byteArray);
                        decryptPass = new String(original);
                        decryptPass = decryptPass.trim();
                } catch (Exception e) {
                        decryptPass = encryptPass;
                        e.printStackTrace();
                }
                return decryptPass;
        }
}
