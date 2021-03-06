package com.example.demo;

import com.example.demo.until.CopyFile;
import com.sun.tools.javac.util.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class DemoApplicationTests {
    private static int blockCount = 12;

    public final static String srcPath = "/Users/zyy/Downloads/day14installpkg.zip";
    public final static String destPath = "/Users/zyy/Desktop/文档/day14installpkg.zip";



    /**
     * 例 srcPath="/Users/zyy/Downloads/copyfile.zip"
     *   destPath="/Users/zyy/file/copyfile.zip"
     * */
    @Test
    void contextLoads() {
        Assert.check(!srcPath.equals(""), "源文件不能为null");
        Assert.check(!destPath.equals(""), "目标文件不能为null");
        File soure = new File(srcPath);
        long len = soure.length();
        Assert.check(len != 0L, "文件不能为null");
        long oneNum = len / blockCount;
        for (int i = 0; i < blockCount - 1; i++) {
            new CopyFile(srcPath, destPath, oneNum * i, oneNum * (i + 1)).start();
        }
        //文件长度不能被整除的部分放在最后一段处理；
        new CopyFile(srcPath, destPath, oneNum * (blockCount - 1), len).start();
    }

    //线程池方式
    @Test
    void contextLoads1() {
        Assert.check(!srcPath.equals(""), "源文件不能为null");
        Assert.check(!destPath.equals(""), "目标文件不能为null");
        File soure = new File(srcPath);
        long len = soure.length();
        Assert.check(len != 0L, "文件不能为null");
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
    }

    // 零拷贝 不占用虚拟机内存 使用堆外内存
    @Test
    public void contextLoads2() {
        Assert.check(!srcPath.equals(""), "源文件不能为null");
        Assert.check(!destPath.equals(""), "目标文件不能为null");
        long startTime = System.currentTimeMillis();
        try (FileChannel sourceChannel = new FileInputStream(srcPath).getChannel();
             FileChannel destChannel = new FileOutputStream(destPath).getChannel();) {
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        // copyFileUsingFileChannel total execution time: 18842ms
        System.out.println("copyFileUsingFileChannel total execution time: "
                + (endTime - startTime) + "ms");
    }
}
