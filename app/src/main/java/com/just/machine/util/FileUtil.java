package com.just.machine.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.DateUtils;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.just.machine.model.sixmininfo.SixMinEcgBean;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
//                File file = new File(context.getExternalFilesDir("").getAbsolutePath(), dstPath);
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), dstPath);
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
     *
     * @param folderPath
     * @return
     */
    public long getFolderSize(String folderPath) {
        File file = new File(folderPath);
        if (!file.exists()) {
            file.mkdirs();
        }

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

    /**
     * 获取指定路径下文件
     *
     * @param pathStr
     * @return
     */
    public List<File> getPathFiles(String pathStr) {
        List<File> listFile = new ArrayList<>();
        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), pathStr);
        if (directory.exists() && directory.isDirectory()) {
            // 获取目录下所有文件和目录
            File[] files = directory.listFiles();

            if (files != null) {
                listFile.addAll(Arrays.asList(files));
            }

        } else {
            System.out.println("目录不存在或不是一个目录");
        }
        return listFile;
    }

    public interface FileOperateCallback {
        void onSuccess();

        void onFailed(String error);
    }

    /**
     * 创建异常文件
     */
    public static File createErrorFile(Context context) {
        File file = null;
        try {
//            String DIR = context.getExternalFilesDir("").getAbsolutePath() + "/LungInstruments/log/";
            String DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LungInstruments/log/";
            String NAME = CommonUtil.getCurrentTime() + ".txt";
            File dir = new File(DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            file = new File(dir, NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 保存图片到本地
     *
     * @param bitmap
     * @param filePath
     */
    public Boolean saveBitmapToFile(Bitmap bitmap, String filePath) {
        boolean save = false;
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            save = true;
        } catch (Exception e) {
            e.printStackTrace();
            save = false;
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return save;
    }

    /**
     * 写入心电数据
     *
     * @param map
     * @param path
     * @throws IOException
     */
    public static void writeEcg(Map<Integer, SixMinEcgBean> map, String path) {
        //建立一个File对象
        File file = new File(path);
        FileOutputStream fos = null;
        BufferedWriter bw = null;
        //判断该文件的所属文件夹存不存在，不存在则创建文件夹
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        //判断file是否存在
        if (!file.exists()) {
            //如果不存在file文件，则创建
            try {
                file.createNewFile();
                fos = new FileOutputStream(file, true);
                bw = new BufferedWriter(new OutputStreamWriter(fos));
                if (!map.isEmpty()) {
//                    for (Integer time : map.keySet()) {
//                        String byteStr = new Gson().toJson(map.get(time));
//                        bw.write("[" + time + ":" + byteStr + "]");
//                        //这里要说明一下，write方法是写入缓存区，并没有写进file文件里面，要使用flush方法才写进去
//                        bw.flush();
//                        bw.newLine();
//                    }
                    bw.write(new Gson().toJson(map));
                    bw.flush();
                    bw.close();
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != bw) {
                        bw.close();
                    }
                    if (null != fos) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读取心电数据
     *
     * @param path
     * @throws IOException
     */
    public static Map<Integer, SixMinEcgBean> readEcg(String path) {
        Map<Integer, SixMinEcgBean> ecgBeanMap = new TreeMap<>();
        File file = new File(path);
        FileInputStream reader = null;//定义一个fileReader对象，用来初始化BufferedReader
        BufferedReader bReader = null;
        try {
            reader = new FileInputStream(file);
            bReader = new BufferedReader(new InputStreamReader(reader));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            if (stringBuilder.length() > 0) {
                ecgBeanMap = new Gson().fromJson(stringBuilder.toString(), new TypeToken<Map<Integer, SixMinEcgBean>>() {
                }.getType());
            }
            bReader.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != bReader) {
                    bReader.close();
                }
                if (null != reader) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ecgBeanMap;
    }
}
