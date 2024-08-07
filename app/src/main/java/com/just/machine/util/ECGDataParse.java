package com.just.machine.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.just.machine.model.sixmininfo.SixMinEcgBean;
import com.seeker.luckychart.model.ECGPointValue;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeMap;

/**
 * @author Seeker
 * @date 2018/10/15/015  18:02
 */
public class ECGDataParse {

    private TreeMap<Integer, SixMinEcgBean> values;

    public ECGDataParse(Context context) {
        String json = parseJson(context, "ecgData.json");
        Gson gson = new Gson();
        values = gson.fromJson(json, new TypeToken<ECGPointValue[]>() {
        }.getType());
    }

    public ECGDataParse(Context context, String filePath) throws Exception {
        String json = parseLocalFile(context, filePath);
        Gson gson = new Gson();
        if (!TextUtils.isEmpty(json)) {
            values = gson.fromJson(json, new TypeToken<TreeMap<Integer, SixMinEcgBean>>() {}.getType());
            if(values == null){
                throw new Exception("解析心电数据失败!");
            }
        } else {
            throw new Exception("解析心电数据失败!");
        }
    }

    private static String parseJson(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private String parseLocalFile(Context context, String filePath) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileInputStream fis = new FileInputStream(filePath);
            BufferedReader bf = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public TreeMap<Integer, SixMinEcgBean> getValues() {
        return values;
    }
}
