package com.nd.hilauncherdev.kitset.util;

import com.nd.hilauncherdev.kitset.apache.Hex;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具类
 */
public class DigestUtil {

    static MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
	
	/**
	 * MD5加密，32位
	 * @param str 待加密的字符串
	 * @return String
	 */
	public static String MD5(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return str ;
		}

		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

    private static MessageDigest getMd5Digest() {
        return getDigest("MD5");
    }

    public static byte[] md5(byte[] data) {
        return getMd5Digest().digest(data);
    }

    public static byte[] md5(String data) {
        return md5(data.getBytes());
    }

    public static String md5Hex(String data) {
        return new String(Hex.encodeHex(md5(data)));
    }


    /**
     * 将字符串编码为md5格式
     *
     * @param value
     * @return
     */
    public static String md5Encode(String value) {
        return md5Encode(value, null);
    }

    /**
     * 将字符串编码为md5格式
     *
     * @param value
     * @param encode
     *            default utf8
     * @return
     */
    public static String md5Encode(String value, String encode) {
        String tmp = null;
        encode = StringUtil.isEmpty(encode) ? "utf8" : encode;
        try {
            tmp = md5Encode(value.getBytes(encode));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(
            );
        }

        return tmp;
    }

    /**
     * 将字符串编码为md5格式
     *            default utf8
     * @return
     */
    public static String md5Encode(byte[] datas) {
        String tmp = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(datas);
            byte[] md = md5.digest();
            tmp = binToHex(md);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return tmp;
    }

    public static String binToHex(byte[] md) {
        StringBuffer sb = new StringBuffer("");
        int read = 0;
        for (int i = 0; i < md.length; i++) {
            read = md[i];
            if (read < 0)
                read += 256;
            if (read < 16)
                sb.append("0");
            sb.append(Integer.toHexString(read));
        }

        return sb.toString();
    }
}
