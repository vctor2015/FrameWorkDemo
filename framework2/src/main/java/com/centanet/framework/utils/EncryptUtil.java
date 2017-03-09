package com.centanet.framework.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密util
 */
public final class EncryptUtil {

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'};

    private EncryptUtil() {
        // Utility class.
    }

    /**
     * md5加密
     */
    public static String md5(String... inputs) {
        if (inputs == null)
            return "";
        StringBuilder builder = new StringBuilder();
        for (String s : inputs) {
            builder.append(s);
        }
        try {
            byte[] btInput = builder.toString().getBytes();
            //获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            //使用指定的字节更新摘要
            mdInst.update(btInput);
            //获得密文
            byte[] md = mdInst.digest();
            //把密文转换成十六进制的字符串形式
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            int i = 0;
            do {
                byte byte0 = md[i];
                str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
                str[k++] = HEX_DIGITS[byte0 & 0xf];
                i++;
            } while (i < j);
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * SHA1加密
     */
    public static String sha1(String... inputs) {
        if (inputs == null)
            return "";
        StringBuilder builder = new StringBuilder();
        for (String s : inputs) {
            builder.append(s);
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("sha1");
            messageDigest.update(builder.toString().getBytes());
            byte[] bytes = messageDigest.digest();
            int len = bytes.length;
            char[] resultCharArray = new char[len * 2];
            int index = 0;
            for (byte b : bytes) {
                resultCharArray[index++] = HEX_DIGITS[b >>> 4 & 0xf];
                resultCharArray[index++] = HEX_DIGITS[b & 0xf];
            }
            return new String(resultCharArray);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
