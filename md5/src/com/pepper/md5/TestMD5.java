package com.pepper.md5;

import java.io.File;
import java.io.IOException;

import static com.pepper.md5.MD5.*;

/**
 * com.pepper.md5
 *
 * @Auther: CodePepper
 * @Date: 2022/09/16/15:38
 * @Description:
 */
public class TestMD5 {

    public static void main(String[] args) throws IOException {
        // ---------------不加盐加密字符串---------------
        String password = "admin";
        String notSaltMD5 = notSaltMD5(password);
        System.out.println("不加盐加密后的字符串：" + notSaltMD5);

        // ---------------加随机盐加密字符串---------------

        // 加密+加盐
        String passwordDB = generate(password);
        System.out.println("加随机盐加密字符串：" + passwordDB);
        // 验证密码是否正确
        System.out.println(addSaltMD5(password, passwordDB));
        // 加密+加盐
        String passwordDB1 = generate(password);
        System.out.println("加随机盐加密字符串：" + passwordDB1);
        // 验证密码是否正确
        System.out.println(addSaltMD5(password, passwordDB1));

        // 29B24856385264D905E8FD36C6A108B1D77C70E97D81B17B某次随机加盐加密后的字符串
        System.out.println(addSaltMD5(password, "29B24856385264D905E8FD36C6A108B1D77C70E97D81B17B"));


        // ---------------加密文件---------------
        File file = new File("D:\\blog\\md5\\src\\com\\pepper\\md5\\md5");
        String md2 = getFileMD5(file);
        System.out.println("加密文件: "+md2);


    }

}
