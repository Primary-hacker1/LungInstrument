package com.just.machine.util;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
     *
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

    /**
     * 初始化图表
     */
    public void initChart(LineChart lineChart) {
        /***图表设置***/
        //是否展示网格线
        lineChart.setDrawGridBackground(false);
        //是否显示边界
        lineChart.setDrawBorders(true);
        //是否可以拖动
        lineChart.setDragEnabled(false);
        //是否有触摸事件
        lineChart.setTouchEnabled(true);
        //设置XY轴动画效果
        lineChart.animateY(2500);
        lineChart.animateX(1500);

        /***XY轴的设置***/
        XAxis xAxis = lineChart.getXAxis();
        YAxis leftYAxis = lineChart.getAxisLeft();
        YAxis rightYaxis = lineChart.getAxisRight();
        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        //保证Y轴从0开始，不然会上移一点
        leftYAxis.setAxisMinimum(0f);
        rightYaxis.setAxisMinimum(0f);

        /***折线图例 标签 设置***/
        Legend legend = lineChart.getLegend();
        //设置显示类型，LINE CIRCLE SQUARE EMPTY 等等 多种方式，查看LegendForm 即可
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(12f);
        //显示位置 左下方
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);
    }


    /**
     * 展示曲线
     */
    public void showLineChart(LineChart lineChart, LineData entries) {
        lineChart.setData(entries);
        lineChart.invalidate();
    }
}
