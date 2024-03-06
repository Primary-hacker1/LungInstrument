package com.just.machine.util;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WifiApConnector {

    private static final String SETUP_WIFIAP_METHOD = "setWifiApEnabled";
    private static final String TAG="wifi_test";
    Context context = null;
    WifiManager wifiManager = null;
    static WifiApConnector wifiApConnector = null;

    public static WifiApConnector getInstance(Context context) {
        if (wifiApConnector == null) {
            wifiApConnector = new WifiApConnector();
            wifiApConnector.context = context.getApplicationContext();
            wifiApConnector.wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        }
        return wifiApConnector;
    }



    public void setupWifiAp(String name, String password) throws Exception {

        if (wifiManager.isWifiEnabled()) {
            Log.i(TAG,"手机wifi开启了"+wifiManager.isWifiEnabled());
            wifiManager.setWifiEnabled(false);
            Log.i(TAG,"手机wifi被强行");
            if (name == null || "".equals(name)) {
                throw new Exception("the name of the wifiap is cannot be null");
            }
            else{
                stratWifiAp(name, password);
                Log.i(TAG,"手机关闭wifi之后成功调用startwifiap方法");
            }

        }

        if (!wifiManager.isWifiEnabled()) {
            Log.i(TAG,"手机wifi未开启"+wifiManager.isWifiEnabled());

            if (name == null || "".equals(name)) {
                throw new Exception("the name of the wifiap is cannot be null");
            }else{
                stratWifiAp(name, password);
                Log.i(TAG,"手机不需要关闭wifi之后热点创建成功");
            }

        }
    }


    private void stratWifiAp(String name, String password) {
        Method method1 = null;
        try {
            method1 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            WifiConfiguration netConfig = new WifiConfiguration();

            netConfig.SSID = name;
            netConfig.preSharedKey = password;

            netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            method1.invoke(wifiManager, netConfig, true);
            Log.i(TAG,"成功启动start方法创建wifi热点");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }




    public boolean isWifiApEnabled() {
        try {
            Method method = wifiManager.getClass().getMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifiManager);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void closeWifiAp() {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (isWifiApEnabled()) {
            try {
                Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
                method.setAccessible(true);
                WifiConfiguration config = (WifiConfiguration) method.invoke(wifiManager);
                Method method2 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
                method2.invoke(wifiManager, config, false);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
