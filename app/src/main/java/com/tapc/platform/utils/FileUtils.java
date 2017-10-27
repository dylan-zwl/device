package com.tapc.platform.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by Administrator on 2017/4/6.
 */

public class FileUtils {

    public interface ProgressCallback {
        void onProgress(int progress);

        void onCompeleted(boolean isSuccessd, String msg);
    }

    //获取文件夹大小
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        File flist[] = file.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    //筛选文件名
    public static String getFilename(String path, FilenameFilter filter) {
        File files = new File(path);
        String[] list = files.list(filter);
        if (list != null && list.length > 0) {
            return list[0];
        }
        return null;
    }

    public static void copyFile(String oldPath, String newPath, ProgressCallback callback) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                long fileSize = oldfile.length();
                int oldIndex = -1;
                InputStream inStream = new FileInputStream(oldPath);
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    fs.write(buffer, 0, byteread);
                    int progress = (int) ((bytesum * 100 / fileSize));
                    if (progress != oldIndex) {
                        oldIndex = progress;
                        callback.onProgress(progress);
                    }
                }
                inStream.close();
                fs.close();
                callback.onCompeleted(true, "");
            } else {
                callback.onCompeleted(false, "no file");
            }
        } catch (Exception e) {
            e.printStackTrace();
            callback.onCompeleted(false, e.getMessage());
        }
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public static void RecursionDeleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                RecursionDeleteFile(f);
            }
            file.delete();
        }
    }

    private static final int BUFF_SIZE = 1024 * 1024; // 1M Byte

    public static void upZipFile(String zipFileString, String outPathString) throws Exception {
        File desDir = new File(outPathString);
        if (desDir.exists()) {
            RecursionDeleteFile(desDir);
        }
        desDir.mkdirs();
        ZipFile zf = new ZipFile(new File(zipFileString));
        for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = ((ZipEntry) entries.nextElement());
            InputStream in = zf.getInputStream(entry);
            String szName = entry.getName();
            if (entry.isDirectory()) {
                // get the folder name of the widget
                File folder = new File(outPathString + File.separator + szName);
                folder.mkdirs();
            } else {
                String str = outPathString + File.separator + entry.getName();
                File desFile = new File(str);
                if (!desFile.exists()) {
                    File fileParentDir = desFile.getParentFile();
                    if (!fileParentDir.exists()) {
                        fileParentDir.mkdirs();
                    }
                    desFile.createNewFile();
                }
                OutputStream out = new FileOutputStream(desFile);
                byte buffer[] = new byte[BUFF_SIZE];
                int realLength;
                while ((realLength = in.read(buffer)) > 0) {
                    out.write(buffer, 0, realLength);
                }
                in.close();
                out.close();
            }
        }
    }

    /**
     * DeCompress the ZIP to the path
     *
     * @param zipFileString name of ZIP
     * @param outPathString path to be unZIP
     * @throws Exception
     */
    public static void UnZipFolder(String zipFileString, String outPathString) throws Exception {
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
        ZipEntry zipEntry;
        String szName;

        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                // get the folder name of the widget
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(outPathString + File.separator + szName);
                folder.mkdirs();
            } else {
                File file = new File(outPathString + File.separator + szName);
                file.createNewFile();
                // get the output stream of the file
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                // read (len) bytes into buffer
                while ((len = inZip.read(buffer)) != -1) {
                    // write (len) byte from buffer at the position 0
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }
        inZip.close();
    }

    private long mCopySize;
    private int mOldProgress;
    private long mOriginFileSize;
    private ProgressCallback mCallback;
    private Object mLock;

    public synchronized void copyFolder(final String originFile, final String targetFile, final ProgressCallback
            callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mLock = new Object();
                    mCopySize = 0;
                    mOriginFileSize = getFileSize(new File(originFile));
                    mCallback = callback;
                    mOldProgress = -1;

                    copyFolder(originFile, targetFile);
                    callback.onCompeleted(true, "");
                } catch (Exception e) {
                    callback.onCompeleted(false, e.getMessage());
                }
            }
        }).start();
    }

    private synchronized void copyFolder(String originFile, String targetFile) throws Exception {
        new File(targetFile).mkdirs();
        File listFile = new File(originFile);
        final String[] file = listFile.list();
        File temp = null;

        for (int i = 0; i < file.length; i++) {
            if (originFile.endsWith(File.separator)) {
                temp = new File(originFile + file[i]);
            } else {
                temp = new File(originFile + File.separator + file[i]);
            }
            if (temp.isFile()) {
                synchronized (mLock) {
                    CopyFileUtil copyFileUtil = new CopyFileUtil();
                    String srcPath = temp.getAbsolutePath();
                    String destPath = targetFile + "/" + (temp.getName()).toString();
                    copyFileUtil.start(srcPath, destPath, 5, new CopyFileUtil.CopyListener() {
                        @Override
                        public void copyResult(boolean isCopySuccess) {
                            synchronized (mLock) {
                                mLock.notify();
                            }
                        }

                        @Override
                        public void copyCount(int size) {
                            mCopySize = mCopySize + size;
                            int progress = (int) (mCopySize * 100 / mOriginFileSize);
                            if (mOldProgress != progress) {
                                mOldProgress = progress;
                                mCallback.onProgress(progress);
                            }
                        }
                    });
                    mLock.wait();
                }
            } else if (temp.isDirectory()) {
                copyFolder(originFile + "/" + file[i], targetFile + "/" + file[i]);
            }
        }
    }
}
