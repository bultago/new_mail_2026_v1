package com.nugenmail.util;

import java.security.MessageDigest;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.codec.digest.Crypt;
import org.bouncycastle.crypto.engines.TwofishEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Password Utility Class
 * Handles legacy password verification supporting multiple schemes:
 * AES, SHA-256, SHA-512, SHA-1, SSHA, MD5, Crypt, Twofish, and Cleartext.
 */
@Component
public class PasswordUtils {

    @Value("${nugenmail.security.aes-key:tErRaCeNtWeBmAiL}")
    private String aesKey;

    /**
     * Verifies an input password against a stored password hash.
     * Detects the algorithm based on the prefix (e.g., {SHA}, {AES}).
     * 
     * @param inputPassword  Plaintext password input
     * @param storedPassword Stored password (hash or encrypted)
     * @return true if matches, false otherwise
     */
    public boolean checkPassword(String inputPassword, String storedPassword) {
        if (storedPassword == null || inputPassword == null) {
            return false;
        }

        if (storedPassword.startsWith("{AES}")) {
            return checkAes(inputPassword, storedPassword.substring(5));
        } else if (storedPassword.startsWith("{SHA256}")) {
            return checkHash(inputPassword, storedPassword.substring(8), "SHA-256");
        } else if (storedPassword.startsWith("{SHA512}")) {
            return checkHash(inputPassword, storedPassword.substring(8), "SHA-512");
        } else if (storedPassword.startsWith("{SHA}")) {
            return checkHash(inputPassword, storedPassword.substring(5), "SHA-1");
        } else if (storedPassword.startsWith("{SSHA}")) {
            return checkSsha(inputPassword, storedPassword.substring(6));
        } else if (storedPassword.startsWith("{MD5}")) {
            return checkHash(inputPassword, storedPassword.substring(5), "MD5");
        } else if (storedPassword.startsWith("{CRYPT}")) {
            return checkCrypt(inputPassword, storedPassword.substring(7));
        } else if (storedPassword.startsWith("{TWOFISH}")) {
            return checkTwofish(inputPassword, storedPassword.substring(9));
        } else if (storedPassword.startsWith("{CLEARTEXT}")) {
            return inputPassword.equals(storedPassword.substring(11));
        } else if (isClearText(storedPassword)) {
            // Assume cleartext if no prefix matches known patterns
            return inputPassword.equals(storedPassword);
        }

        return false;
    }

    private boolean isClearText(String storedPass) {
        return !storedPass.startsWith("{");
    }

    private boolean checkHash(String input, String storedHash, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            // Legacy systems often use Base64 for the digest string
            String encoded = Base64.getEncoder().encodeToString(digest);

            // Some legacy systems might use Hex. If verification fails, we could try Hex.
            if (encoded.equals(storedHash))
                return true;

            // Try Hex
            String hex = bytesToHex(digest);
            if (hex.equalsIgnoreCase(storedHash))
                return true;

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean checkSsha(String input, String storedHash) {
        try {
            byte[] decoded = Base64.getDecoder().decode(storedHash);
            // SSHA = SHA-1(password + salt) + salt
            // SHA-1 is 20 bytes
            if (decoded.length < 20)
                return false;

            byte[] digest = Arrays.copyOfRange(decoded, 0, 20);
            byte[] salt = Arrays.copyOfRange(decoded, 20, decoded.length);

            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(input.getBytes(StandardCharsets.UTF_8));
            md.update(salt);
            byte[] newDigest = md.digest();

            return Arrays.equals(digest, newDigest);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean checkCrypt(String input, String storedHash) {
        try {
            String calculated = Crypt.crypt(input, storedHash);
            return calculated.equals(storedHash);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkTwofish(String input, String storedHash) {
        // Twofish implementation using BouncyCastle.
        // Legacy Key from Twofish_Algorithm.java: 000102030405060708090A0B0C0D0E0F
        String keyHex = "000102030405060708090A0B0C0D0E0F";
        try {
            PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new TwofishEngine(),
                    new org.bouncycastle.crypto.paddings.ZeroBytePadding());

            byte[] keyBytes = hexToBytes(keyHex);
            KeyParameter keyParam = new KeyParameter(keyBytes);

            cipher.init(false, keyParam); // false = decrypt

            byte[] encryptedBytes;
            try {
                encryptedBytes = hexToBytes(storedHash);
            } catch (Exception e) {
                encryptedBytes = Base64.getDecoder().decode(storedHash);
            }

            byte[] out = new byte[cipher.getOutputSize(encryptedBytes.length)];
            int len = cipher.processBytes(encryptedBytes, 0, encryptedBytes.length, out, 0);
            len += cipher.doFinal(out, len);

            String decrypted = new String(out, 0, len, StandardCharsets.UTF_8);
            System.out.println("DEBUG: Decrypted: " + decrypted);
            return input.equals(decrypted);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean checkAes(String input, String encryptedStored) {
        try {
            // Decrypt the stored password
            // Assumption: AES/ECB/PKCS5Padding with the 128-bit key
            SecretKeySpec skeySpec = new SecretKeySpec(aesKey.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            // Stored string is likely Hex or Base64. Try Hex first (common in legacy SQL)
            byte[] encryptedBytes;
            try {
                encryptedBytes = hexToBytes(encryptedStored);
            } catch (Exception e) {
                encryptedBytes = Base64.getDecoder().decode(encryptedStored);
            }

            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] original = cipher.doFinal(encryptedBytes);
            String originalString = new String(original, StandardCharsets.UTF_8);

            return input.equals(originalString);
        } catch (Exception e) {
            // Decryption failed, mismatch
            return false;
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private byte[] hexToBytes(String s) {
        int len = s.length();
        if (len % 2 != 0) {
            throw new IllegalArgumentException("Hex string must have even length");
        }
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            int d1 = Character.digit(s.charAt(i), 16);
            int d2 = Character.digit(s.charAt(i + 1), 16);
            if (d1 == -1 || d2 == -1) {
                throw new IllegalArgumentException("Invalid hex character");
            }
            data[i / 2] = (byte) ((d1 << 4) + d2);
        }
        return data;
    }
}
