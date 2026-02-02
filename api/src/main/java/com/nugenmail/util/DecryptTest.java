package com.nugenmail.util;

import org.bouncycastle.crypto.engines.TwofishEngine;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.paddings.ZeroBytePadding;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class DecryptTest {
    public static void main(String[] args) throws Exception {
        // Key from Twofish_Algorithm.java
        String keyHex = "000102030405060708090A0B0C0D0E0F";
        byte[] keyBytes = hexStringToByteArray(keyHex);

        String context = "2wxLeZ7vcg7VpM6cLC1pcw==";
        byte[] encryptedBytes = Base64.getDecoder().decode(context);

        // 2. ECB + ZeroByte (Most likely)
        tryDecrypt("ECB + ZeroByte", new PaddedBufferedBlockCipher(new TwofishEngine(), new ZeroBytePadding()),
                keyBytes, encryptedBytes);
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    static void tryDecrypt(String name, PaddedBufferedBlockCipher cipher, byte[] key, byte[] data) {
        try {
            cipher.init(false, new KeyParameter(key));
            byte[] out = new byte[cipher.getOutputSize(data.length)];
            int len = cipher.processBytes(data, 0, data.length, out, 0);
            len += cipher.doFinal(out, len);
            System.out.println(name + ": SUCCESS -> " + new String(out, 0, len, StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.out.println(name + ": FAILED -> " + e.getMessage());
        }
    }

    static void tryDecryptCBC(String name, PaddedBufferedBlockCipher cipher, byte[] key, byte[] data) {
        try {
            // Zero IV
            cipher.init(false, new ParametersWithIV(new KeyParameter(key), new byte[16]));
            byte[] out = new byte[cipher.getOutputSize(data.length)];
            int len = cipher.processBytes(data, 0, data.length, out, 0);
            len += cipher.doFinal(out, len);
            System.out.println(name + ": SUCCESS -> " + new String(out, 0, len, StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.out.println(name + ": FAILED -> " + e.getMessage());
        }
    }
}
