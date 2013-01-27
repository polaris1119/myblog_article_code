//package com.studygolang.test;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

import sun.misc.BASE64Encoder;

public class AES {
	private IvParameterSpec ivSpec;
	private SecretKeySpec keySpec;
	
	public AES(String key) {
		try {
			byte[] keyBytes = key.getBytes();
			byte[] buf = new byte[16];

			for (int i = 0; i < keyBytes.length && i < buf.length; i++) {
				buf[i] = keyBytes[i];
			}
			
			this.keySpec = new SecretKeySpec(buf, "AES");
			this.ivSpec = new IvParameterSpec(keyBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public byte[] encrypt(byte[] origData) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, this.keySpec, this.ivSpec);
			return cipher.doFinal(origData);
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public byte[] decrypt(byte[] crypted) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, this.keySpec, this.ivSpec);
			return cipher.doFinal(crypted);
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		AES aes = new AES("sfe023f_9fd&fwfl");
		String data = "polaris@studygolang";
		byte[] crypted = aes.encrypt(data.getBytes());
		System.out.println(base64Encode(crypted));
		System.out.println(new String(aes.decrypt(crypted)));
	}
	
	public static String base64Encode(byte[] data) {
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);
	}

}
