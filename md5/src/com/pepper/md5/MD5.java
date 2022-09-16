package com.pepper.md5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * com.pepper
 *
 * @Auther: CodePepper
 * @Date: 2022/09/16/15:29
 * @Description: 使用MD5算法
 */
public class MD5 {


    /**
     * TODO 不加盐对字符串进行加密:
     * <p>
     * 1.获取md5对象,通过"信息摘要"获取实例构造("MD5").
     * 2.md5对象对("字符串的"字节形式"-得到的数组)进行摘要",那么会返回一个"摘要的字节数组"
     * 3.包含 BigInteger 的二进制补码表示形式的 byte 数组转换为 BigInteger,之后将bigInteger 的值转换为字符串
     *
     * @param source
     * @return 加密结果
     */
    public static String notSaltMD5(String source) {
        //  1.判断 source 是否有效
        if (source == null || source.length() == 0) {
            //  2.如果不是有效的字符串抛出异常
            throw new RuntimeException("服了你啦，出错啦");
        }
        try {
            // 3.获取 MessageDigest 对象，String algorithm = "md5";可以清晰的看出是那种加密方式
            String algorithm = "md5";
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            // 4.获取明文字符串对应的字节数组，因为digest方法需要传入字节数组
            byte[] input = source.getBytes();
            // 5.执行加密
            byte[] output = messageDigest.digest(input);
            // 6.创建 BigInteger 对象，signum为1的时候是正数
            int signum = 1;
            BigInteger bigInteger = new BigInteger(signum, output);
            // 7.按照 16 进制将 bigInteger 的值转换为字符串
            int radix = 16;
            String encoded = bigInteger.toString(radix).toUpperCase();
            return encoded;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * TODO 加盐对字符串进行加密:
     * <p>
     * 将加盐之后得到的字符串跟，数据库中存储的字符串进行比较，校验密码是否正确
     *
     * @param password 用户输入的密码
     * @param md5      加盐之后得到的字符串(可从数据库中取出)
     *                 通过函数public static String generate(String password)得到
     * @return 加密结果
     */

    public static boolean addSaltMD5(String password, String md5) {
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];
        for (int i = 0; i < 48; i += 3) {
            cs1[i / 3 * 2] = md5.charAt(i);
            cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
            cs2[i / 3] = md5.charAt(i + 1);
        }
        String salt = new String(cs2);
        return notSaltMD5(password + salt).equals(new String(cs1));
    }


    /**
     * 生成含有随机盐的密码
     */
    public static String generate(String password) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = sb.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sb.append("0");
            }
        }
        String salt = sb.toString();
        password = notSaltMD5(password + salt);
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        return new String(cs);
    }

    /**
     * TODO 加密文件
     * 1.传入的是"文件",不是字符串，所以信息摘要对象.进行摘要得到数组不能像上面获得:md5.digest(bytes),因为不是source.getBytes得到bytes
     * 2.其实还是通过mdt.digest();获取到字节数组,但是前期必须要有一个方法必须是md5.update(),即"信息摘要对象"要先更新
     * 3."信息摘要更新"里面有(byte[] input),说明是读取流获取到的数组,所以我们就用这个方法.
     * 4.所以最终的逻辑就是:
     * <p>
     * 1.获取文件的读取流
     * 2.不停的读取流中的"内容"放入字符串,放一部分就"更新"一部分.直到全部完毕
     * 3.然后调用md5.digest();就会得到有内容的字节数组,剩下的就和上边一样了.
     */
    public static String getFileMD5(File file) throws IOException {

        try {
            // 1.获取 MessageDigest 对象，String algorithm = "md5";可以清晰的看出是那种加密方式
            String algorithm = "md5";
            MessageDigest md5 = MessageDigest.getInstance(algorithm);
            // 2.加载文件
            FileInputStream files = new FileInputStream(file);

            byte[] bytes = new byte[1024 * 5];

            int len = -1;
            // 3.读取文件
            while ((len = files.read(bytes)) != -1) {
                // 4.一部分一部分更新
                md5.update(bytes, 0, len);
            }
            byte[] digest = md5.digest();
            // 5..创建 BigInteger 对象，signum为1的时候是正数
            int signum = 1;
            BigInteger bigInteger = new BigInteger(signum, digest);
            // 6..按照 16 进制将 bigInteger 的值转换为字符串
            int radix = 16;
            String encoded = bigInteger.toString(radix).toUpperCase();
            return encoded;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
