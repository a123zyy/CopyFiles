package com.example.demo;

import com.example.demo.until.CopyFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zyy
 */
@RestController
public class CopyFileController {

    /**
     * 使用线程池方式copy
     * srcPath 源文件地址
     * destPath 目标文件地址
     * 例 srcPath = '/Users/zyy/Downloads/day14installpkg.zip'
     * destPath = '/Users/zyy/Desktop/文档/day14installpkg.zip'
     */
    @GetMapping("/copyFileThredPool")
    public String copyfiles(String srcPath, String destPath) {
        long beginTime = System.currentTimeMillis();
        File soure = new File(srcPath);
        long len = soure.length();
        int blockCount = this.getThredNum(len);
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(2);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(6, 10, 0L, TimeUnit.NANOSECONDS, workQueue);
        threadPoolExecutor.prestartAllCoreThreads(); // 预启动所有核心线程
        long oneNum = len / blockCount;
        for (int i = 0; i < blockCount - 1; i++) {
            CopyFile copyFile = new CopyFile(srcPath, destPath, oneNum * i, oneNum * (i + 1));
            threadPoolExecutor.execute(copyFile);
        }
        //文件长度不能被整除的部分放在最后一段处理；
        new CopyFile(srcPath, destPath, oneNum * (blockCount - 1), len).start();
        long endTime = System.currentTimeMillis();
        System.out.println(oneNum + "最终线程数每次拷贝文件大小！");
        System.out.println(blockCount + "最终线程数。。。。。。。");
        System.out.println(Thread.currentThread().getName() + "-结束时间:" + endTime + "-共用时长:" + (endTime - beginTime) + "ms");
        return "success";
    }

    /**
     * 使用多个线程方式copy
     * srcPath 源文件地址
     * destPath 目标文件地址
     * 例 srcPath = '/Users/zyy/Downloads/day14installpkg.zip'
     *    destPath = '/Users/zyy/Desktop/文档/day14installpkg.zip'
     * */
    @GetMapping("/copyFiles")
    public String copyfile(String srcPath, String destPath) {
        long beginTime = System.currentTimeMillis();
        File soure = new File(srcPath);
        long len = soure.length();
        int blockCount = this.getThredNum(len);
        long oneNum = len / blockCount;
        for (int i = 0; i < blockCount - 1; i++) {
            new CopyFile(srcPath, destPath, oneNum * i, oneNum * (i + 1)).start();
        }
        //文件长度不能被整除的部分放在最后一段处理；
        new CopyFile(srcPath, destPath, oneNum * (blockCount - 1), len).start();
        long endTime = System.currentTimeMillis();
        System.out.println(oneNum + "最终线程数每次拷贝文件大小！");
        System.out.println(blockCount + "最终线程数。。。。。。。");
        System.out.println(Thread.currentThread().getName() + "-结束时间:" + endTime + "-共用时长:" + (endTime - beginTime) + "ms");
        return "success";
    }

    private int getThredNum(long fileSize) {
        return fileSize > (200 * 1024 * 1024) ? (int) (fileSize / (200 * 1024 * 1024)) : 4;
    }
}
