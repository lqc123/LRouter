package com.lrouter.compiler;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @Description: 工具类
 * @Author: lqc
 * @Date: 2018/8/29
 * @Version:1.0
 */
public class Utils {
    /**
     * 获取字段hash值
     * @param str
     * @return
     */
    public static String hash(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            return Integer.toHexString(str.hashCode());
        }
    }

    /**
     * 判断是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

}
