package com.nd.hilauncherdev.kitset.rsa;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1Utils {

	/**
	 * <br>Description: 获取文件的SHA1值，并转化为十六进制字符串
	 * <br>Author:caizp
	 * <br>Date:2016年1月14日下午2:17:36
	 * @param path
	 * @return
	 * @throws OutOfMemoryError
	 * @throws IOException
	 */
	public static String getFileSha1(String path) throws OutOfMemoryError,
			IOException {
		File file = new File(path);
		FileInputStream in = new FileInputStream(file);
		MessageDigest messagedigest;
		try {
			messagedigest = MessageDigest.getInstance("SHA-1");

			byte[] buffer = new byte[1024 * 1024 * 10];
			int len = 0;

			while ((len = in.read(buffer)) > 0) {
				// 该对象通过使用 update（）方法处理数据
				messagedigest.update(buffer, 0, len);
			}
			// 对于给定数量的更新数据，digest 方法只能被调用一次。在调用 digest 之后，MessageDigest
			// 对象被重新设置成其初始状态。
			return byteArrayToString(messagedigest.digest()).replace("-", "").toLowerCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			throw e;
		} finally {
			in.close();
		}
		return null;
	}
	
	 /** 
     * 字节数据转字符串专用集合 
     */  
    private static final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6',  
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };  
	
	/** 
     * 字节数据转十六进制字符串 
     *  
     * @param data 
     *            输入数据 
     * @return 十六进制内容 
     */  
    public static String byteArrayToString(byte[] data) {  
        StringBuilder stringBuilder = new StringBuilder();  
        for (int i = 0; i < data.length; i++) {  
            // 取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移  
            stringBuilder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);  
            // 取出字节的低四位 作为索引得到相应的十六进制标识符  
            stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);  
        }  
        return stringBuilder.toString();  
    }  
	
}
