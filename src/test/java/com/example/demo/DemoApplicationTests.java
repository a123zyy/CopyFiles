package com.example.demo;

import com.example.demo.until.CopyFile;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest
class DemoApplicationTests {
    private static int blockCount = 8;

    @Test
    void contextLoads() {
        String srcPath = System.getProperty("user.dir")+"/src/main/resources/static/day14installpkg.zip";
        String destPath = System.getProperty("user.dir")+"/src/main/resources/templates/day14installpkg.zip";
        File soure = new File(srcPath);
        long len = soure.length();
        long oneNum = len/blockCount;
        System.out.println(len);
        System.out.println(oneNum);
        for (int i = 0;i<blockCount-1;i++){
            new CopyFile(srcPath,destPath,oneNum*i,oneNum*(i+1)).start();
        }
        //文件长度不能被整除的部分放在最后一段处理；
        new CopyFile(srcPath,destPath,oneNum*(blockCount-1),len).start();

    }

}
