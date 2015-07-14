package edu.uniandes.ecos.movilmigranha.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class EncodingUtils {
	
	public static String getMD5Hash(String input){
		return new String(Hex.encodeHex(DigestUtils.md5(input)));
	}
	
	public static String getSHA1Hash(String input){
		return new String(Hex.encodeHex(DigestUtils.sha1(input)));
	}

}
