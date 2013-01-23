package com.studygolang.test;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import sun.misc.BASE64Encoder;

public class DES {
	private DESKeySpec desKeySpec;
	private IvParameterSpec ivSpec;
	
	public DES(String key) {
		try {
			byte[] keyBytes = key.getBytes();
			this.desKeySpec = new DESKeySpec(keyBytes);
			this.ivSpec = new IvParameterSpec(keyBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public byte[] encrypt(byte[] origData) {
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
			SecretKey key = factory.generateSecret(this.desKeySpec);
			Cipher cipher = Cipher.getInstance("DES/CBC/SSL3Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, this.ivSpec);
			return cipher.doFinal(origData);
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public byte[] decrypt(byte[] crypted) {
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
			SecretKey key = factory.generateSecret(this.desKeySpec);
			Cipher cipher = Cipher.getInstance("DES/CBC/SSL3Padding");
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
		DES des = new DES("sfe023f_");
		String data = "polaris@studygolang";
		byte[] crypted = des.encrypt(data.getBytes());
		System.out.println(base64Encode(crypted));
		System.out.println(new String(des.decrypt(crypted)));
	}
	
	public static String base64Encode(byte[] data) {
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);
	}

}
