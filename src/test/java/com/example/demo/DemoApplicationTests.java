package com.example.demo;

import com.example.demo.until.CopyFile;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
@SpringBootTest
class DemoApplicationTests {
    private static int blockCount = 8;

    /**
     * 例 srcPath="/Users/zyy/Downloads/copyfile.zip"
     *   destPath="/Users/zyy/file/copyfile.zip"
     * */
    @Test
    void contextLoads() {
        String srcPath = "源文件路径";
        String destPath = "要复制位置全路径";
        File soure = new File(srcPath);
        long len = soure.length();
        long oneNum = len / blockCount;
        System.out.println(len);
        System.out.println(oneNum);
        for (int i = 0; i < blockCount - 1; i++) {
            new CopyFile(srcPath, destPath, oneNum * i, oneNum * (i + 1)).start();
        }
        //文件长度不能被整除的部分放在最后一段处理；
        new CopyFile(srcPath, destPath, oneNum * (blockCount - 1), len).start();

    }

}
