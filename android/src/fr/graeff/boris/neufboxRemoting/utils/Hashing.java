package fr.graeff.boris.neufboxRemoting.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Hash tools
 * 
 */
public class Hashing
{
	/**
	 * Convert byte array to String
	 * @param bytes
	 * @return
	 */
	public static String bytesToString(byte[] bytes)
	{
		StringBuffer hash = new StringBuffer();
		for (int i = 0; i < bytes.length; i++)
		{
	        String hex = Integer.toHexString(0xFF & bytes[i]);
	        if (hex.length() == 1)
	        	hash.append('0');
	        
	        hash.append(hex);
		}
		return hash.toString();
	}
	
	/**
	 * Hash SHA 256
	 * @param data
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String hashSHA256(String data)
	throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(data.getBytes("UTF-8"));
		byte[] bytes = md.digest();
		
		return bytesToString(bytes);
	}
	
	/**
	 * Hash Hmac SHA 256
	 * @param key
	 * @param data
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static String hashHmacSHA256(String key, String data)
	throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException
	{
		SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(secretKey);
		byte[] bytes = mac.doFinal(data.getBytes("UTF-8")); 
		  
		return bytesToString(bytes);
	}
	
}
