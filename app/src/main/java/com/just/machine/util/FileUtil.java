package com.just.machine.util;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileUtil {

    private static FileUtil instance;
    private static final int SUCCESS = 1;
    private static final int FAILED = 0;
    private Context context;
    private FileOperateCallback callback;
    private volatile boolean isSuccess;
    private String errorStr;

    public static FileUtil getInstance(Context context) {
        if (instance == null) instance = new FileUtil(context);
        return instance;
    }

    private FileUtil(Context context) {
        this.context = context;
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (callback != null) {
                if (msg.what == SUCCESS) {
                    callback.onSuccess();
                }
                if (msg.what == FAILED) {
                    callback.onFailed(msg.obj.toString());
                }
            }
        }
    };

    public FileUtil copyAssetsToSD(final String srcPath, final String sdPath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                copyAssetsToDst(context, srcPath, sdPath);
                if (isSuccess) handler.obtainMessage(SUCCESS).sendToTarget();
                else handler.obtainMessage(FAILED, errorStr).sendToTarget();
            }
        }).start();
        return this;
    }

    public void setFileOperateCallback(FileOperateCallback callback) {
        this.callback = callback;
    }

    private void copyAssetsToDst(Context context, String srcPath, String dstPath) {
        try {
            String[] fileNames = context.getAssets().list(srcPath);
            if (fileNames.length > 0) {
                File file = new File(context.getExternalFilesDir("").getAbsolutePath(), dstPath);
                if (!file.exists()) file.mkdirs();
                for (String fileName : fileNames) {
                    File outFile = new File(file.getAbsolutePath(), fileName);
                    InputStream is = context.getAssets().open(srcPath + File.separator + fileName);
                    FileOutputStream fos = new FileOutputStream(outFile);
                    byte[] buffer = new byte[1024];
                    int byteCount;
                    while ((byteCount = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, byteCount);
                    }
                    fos.flush();
                    is.close();
                    fos.close();
                }
            }
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
            errorStr = e.getMessage();
            isSuccess = false;
        }
    }

    /**
     * 获取文件夹大小
     * @param folderPath
     * @return
     */
    public long getFolderSize(String folderPath) {
        File file = new File(folderPath);

        if (file.isDirectory()) { // 判断路径对应的是否为文件夹
            long size = 0;

            File[] files = file.listFiles(); // 列举文件夹内所有文件及子文件夹

            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) { // 如果当前项是文件
                    size += files[i].length(); // 将文件大小加到总大小上
                } else if (files[i].isDirectory()) { // 如果当前项是文件夹
                    size += getFolderSize(files[i].getAbsolutePath()); // 递归调用自身计算文件夹大小并相加
                }
            }

            return size;
        } else {
            throw new IllegalArgumentException("The path is not a directory.");
        }
    }

    public interface FileOperateCallback {
        void onSuccess();

        void onFailed(String error);
    }
}
