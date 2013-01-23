package com.studygolang.test;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import sun.misc.BASE64Encoder;

public class TripleDES {
	private DESedeKeySpec desKeySpec;
	private IvParameterSpec ivSpec;
	
	public TripleDES(String key) {
		try {
			byte[] keyBytes = key.getBytes();
			this.desKeySpec = new DESedeKeySpec(keyBytes);
			this.ivSpec = new IvParameterSpec(key.substring(0, 8).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public byte[] encrypt(byte[] origData) {
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
			SecretKey key = factory.generateSecret(this.desKeySpec);
			Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, this.ivSpec);
			return cipher.doFinal(origData);
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public byte[] decrypt(byte[] crypted) {
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
			SecretKey key = factory.generateSecret(this.desKeySpec);
			Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, this.ivSpec);
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
		TripleDES des = new TripleDES("sfe023f_sefiel#fi32lf3e!");
		String data = "polaris@studygol";
		byte[] crypted = des.encrypt(data.getBytes());
		System.out.println(base64Encode(crypted));
		System.out.println(new String(des.decrypt(crypted)));
	}
	
	public static String base64Encode(byte[] data) {
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);
	}

}
