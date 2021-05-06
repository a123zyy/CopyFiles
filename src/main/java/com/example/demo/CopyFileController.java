package com.example.demo;

import com.example.demo.until.CopyFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
public class CopyFileController {


    private static int blockCount = 8;

    @GetMapping("/copyFile")
    public String copyfiles(String srcPath, String destPath) {
        File soure = new File(srcPath);
        long len = soure.length();
        getThredNum(len);
        long oneNum = len / blockCount;
        System.out.println(oneNum + "最终线程数每次拷贝文件大小！");
        System.out.println(blockCount + "最终线程数。。。。。。。");

        for (int i = 0; i < blockCount - 1; i++) {
            new CopyFile(srcPath, destPath, oneNum * i, oneNum * (i + 1)).start();
        }
        //文件长度不能被整除的部分放在最后一段处理；
        new CopyFile(srcPath, destPath, oneNum * (blockCount - 1), len).start();
        return "success";
    }

    private int getThredNum(long fileSize) {
        while ((fileSize / blockCount) > (200 * 1024 * 1024)) {
            blockCount++;
        }
        return blockCount;
    }
}
