package com.terracetech.tims.webmail.util;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

/**
 * 암호화 도구
 * 
 * @since 7.4.5
 * @version 1.0
 * @date 2014.07.23
 * 
 */
public class CryptoSession {
		
	private static final String ALGORITHM = "RSA";
	
	public static final String CRYPTO_SESSION_KEY_NAME_PREFIX = "CRYTPTO_";
	
	public static final String PUBLIC_KEY_MODULUS_NAME_PREFIX = "PUBLIC_MODULUS_";
	
	public static final String PUBLIC_KEY_EXPONENT_NAME_PREFIX = "PUBLIC_EXPONENT_";
	
	private KeyPair keyPair;
	
	public CryptoSession() {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
			generator.initialize(1024);
			this.keyPair = generator.genKeyPair();
		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		}
	}

	public PrivateKey getPrivateKey() throws GeneralSecurityException {
		PrivateKey privateKey = this.keyPair.getPrivate();
		return privateKey;
	}

	public RSAPublicKeySpec getPublicKeySpec() throws GeneralSecurityException {
		PublicKey publicKey = this.keyPair.getPublic();

		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);

		return publicSpec;
	}

	public String decrypt(PrivateKey privateKey, String securedValue) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		byte[] encryptedBytes = hexToByteArray(securedValue);

		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

		return new String(decryptedBytes, "utf-8");
	}

	private byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() % 2 != 0) {
			return new byte[] {};
		}

		byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0; i < hex.length(); i += 2) {
			byte value = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
			bytes[(int) Math.floor(i / 2)] = value;
		}
		return bytes;
	}
}
