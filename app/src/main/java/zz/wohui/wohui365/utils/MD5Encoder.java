package zz.wohui.wohui365.utils;


import java.security.MessageDigest;

/**
 * 说明：MD5加密的工具类
 * 作者：陈杰宇
 * 时间： 2016/1/21 15:28
 * 版本：V1.0
 * 修改历史：
 */
public class MD5Encoder {

    /**
     * 加密字符串为大写
     * @param string 要加密的字符串
     * @return  加密完成的字符串
     */
    public static String encode(String string) {
        StringBuilder hex = null;
        try {
            byte[] hash = MessageDigest.getInstance("MD5").digest(string.getBytes("GBK"));
            hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 0xFF) < 0x10) {
                    hex.append("0");
                }
                hex.append(Integer.toHexString(b & 0xFF));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hex.toString().toUpperCase();
    }

    /**
     * 加密字符串为小写
     * @param string 要加密的字符串
     * @return  加密完成的字符串
     */
    public static String encodeXX(String string) {
        StringBuilder hex = null;
        try {
            String str = string.toLowerCase();
            byte[] hash = MessageDigest.getInstance("MD5").digest(str.getBytes("GBK"));
            hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 0xFF) < 0x10) {
                    hex.append("0");
                }
                hex.append(Integer.toHexString(b & 0xFF));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hex.toString().toLowerCase();
    }
}

