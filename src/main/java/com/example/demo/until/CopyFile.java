package com.example.demo.until;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class CopyFile extends Thread {

    private String srcPath;
    private String destPath;
    private long start,end;

    public CopyFile(String srcPath, String destPath, long start, long end) {
        this.srcPath = srcPath;
        this.destPath = destPath;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        try {
            long beginTime = System.currentTimeMillis();
            //创建只读的随机访问数
            RandomAccessFile in = new RandomAccessFile(srcPath,"r");
            //创建可读可写的随机访问文件
            RandomAccessFile out = new RandomAccessFile(destPath,"rw");
            //将输入跳转到指定位置
            in.seek(start);
            //从指定位置开始写
            out.seek(start);
            //文件输入通道；
            FileChannel inChannel = in.getChannel();
            //文件输出通道；
            FileChannel outChannel = out.getChannel();
            //锁住需要操作的区间
            FileLock lock = outChannel.lock(start,(end-start),false);
            //将字节从此通道
            inChannel.transferTo(start,(end-start),outChannel);
            lock.release();
            out.close();
            in.close();
            //计算耗时
            long endTime = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName()+"-结束时间:"+endTime+"-共用时长:"+(endTime-beginTime)+"ms");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
