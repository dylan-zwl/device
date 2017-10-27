package com.tapc.platform.utils;

/**
 * Created by Administrator on 2017/7/19.
 */


import android.util.Log;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class CopyFileUtil {
    private long startTime = 0;
    private int totalThreadCount = 0;
    private int executedCount = 0;
    private boolean isCopyFailed = false;
    private boolean isFinish = false;
    private CopyListener copyListener;

    public void start(String srcPath, String destPath, int threadCount, CopyListener copyListener) {
        File srcFile = new File(srcPath);
        if (srcFile == null || srcFile.exists() == false) {
            isFinish = true;
            copyListener.copyResult(false);
            return;
        }
        long len = srcFile.length();
        int oneNum = (int) (len / threadCount);

        if (oneNum < 1024) {
            threadCount = 1;
        }

        totalThreadCount = threadCount;
        executedCount = 0;
        startTime = System.currentTimeMillis();
        isCopyFailed = false;
        isFinish = false;
        this.copyListener = copyListener;

        for (int i = 0; i < threadCount - 1; i++) {
            CopyThread ct = new CopyThread(srcPath, destPath, oneNum * i, oneNum * (i + 1));
            ct.start();
        }
        CopyThread ct = new CopyThread(srcPath, destPath, oneNum * (threadCount - 1), (int) len);
        ct.start();
    }

    public boolean isFinish() {
        return isFinish;
    }

    public interface CopyListener {
        void copyResult(boolean isCopySuccess);

        void copyCount(int size);
    }

    private void finish(String destPath) {
        executedCount++;
        if (totalThreadCount == executedCount) {
            executedCount = 0;
            long time = System.currentTimeMillis() - startTime;
            Log.d(destPath + " 复制完成", "花费时长：" + time);
            copyListener.copyResult(!isCopyFailed);
            isFinish = true;
        }
    }


    private class CopyThread extends Thread {
        private String srcPath;
        private String destPath;
        private int start, end;

        public CopyThread(String srcPath, String destPath, int start, int end) {
            //要复制的源文件路径
            this.srcPath = srcPath;
            //复制到的文件路径
            this.destPath = destPath;
            //复制起始位置
            this.start = start;
            //复制结束位置
            this.end = end;
        }

        public void run() {
            try {
                RandomAccessFile in = new RandomAccessFile(srcPath, "r");
                RandomAccessFile out = new RandomAccessFile(destPath, "rw");

                // 将输入跳转到指定位置
                in.seek(start);
                // 从指定位置开始写
                out.seek(start);

                FileChannel inChannel = in.getChannel();
                FileChannel outChannel = out.getChannel();

                //锁住需要操作的区域
                int size = (end - start);
                FileLock lock = outChannel.lock(start, size, false);

                inChannel.transferTo(start, size, outChannel);
//                outChannel.transferFrom(inChannel, start, (end - start));

                //释放锁
                lock.release();
                out.close();
                in.close();
                copyListener.copyCount(size);
            } catch (Exception e) {
                e.printStackTrace();
                isCopyFailed = true;
            }
            finish(destPath);
        }
    }
}
