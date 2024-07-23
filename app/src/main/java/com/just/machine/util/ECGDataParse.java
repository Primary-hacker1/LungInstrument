package com.just.machine.util;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.just.machine.model.sixmininfo.SixMinEcgBean;
import com.seeker.luckychart.model.ECGPointValue;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Seeker
 * @date 2018/10/15/015  18:02
 */
public class ECGDataParse {

    private TreeMap<Integer, SixMinEcgBean> values;

    public ECGDataParse(Context context){
        String json = parseJson(context,"ecgData2.json");
        Gson gson = new Gson();
        values = gson.fromJson(json,new TypeToken<ECGPointValue[]>(){}.getType());
    }

    public ECGDataParse(Context context,String filePath){
        String json = parseLocalFile(context,filePath);
        Gson gson = new Gson();
        values = gson.fromJson(json,new TypeToken<TreeMap<Integer, SixMinEcgBean>>(){}.getType());
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
