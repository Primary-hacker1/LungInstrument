package com.just.machine.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {

    private static String reg = "^\\+?[1-9][0-9]*$";

    /**
     * 根据出生日期计算年龄
     * @param birthDateString
     * @return
     */
    public static int getAge(String birthDateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate;
        try {
            birthDate = sdf.parse(birthDateString);
            Calendar cal = Calendar.getInstance();

            if (cal.before(birthDate)) {
                throw new IllegalArgumentException("Birthdate cannot be in the future");
            }

            int yearNow = cal.get(Calendar.YEAR);
            int monthNow = cal.get(Calendar.MONTH);
            int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

            cal.setTime(birthDate);
            int yearBirth = cal.get(Calendar.YEAR);
            int monthBirth = cal.get(Calendar.MONTH);
            int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

            int age = yearNow - yearBirth;

            if (monthNow <= monthBirth) {
                if (monthNow == monthBirth) {
                    if (dayOfMonthNow < dayOfMonthBirth) age--;
                } else {
                    age--;
                }
            }

            return age;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 根据身高和体重计算BMI
     * @param height
     * @param weight
     * @return
     */
    public static double calculateBmi(double height, double weight) {
        return weight / (height * height);
    }

    /**
     * 1 man 2 girl 果是奇数性别为男，偶数则为女。
     */
    public static int isSex(String idCard) {
        if (!TextUtils.isEmpty(idCard) && idCard.length() == 18) {
            if (Integer.parseInt(idCard.substring(16, 17)) %2==0 ) {
                return 2;
            } else {
                return 1;
            }
        }
        return 0;
    }

    /**
     * 根据身份证号码获取生日
     *
     * @param idCard 身份证号码
     * @return 生日(yyyy-MM-dd)
     */
    public static String getBirthdayFromIdCard(String idCard) {
        if (idCard == null || (idCard.length() != 15 && idCard.length() != 18)) {
            return "";
        }
        return idCard.substring(6, idCard.length() - 4);
    }

    /**
     * 根据身份证号码获取年龄
     *
     * @param idCard 身份证号码
     * @return 年龄
     */
    public static String getAgeFromIdCard(String idCard) {
        String birthday = getBirthdayFromIdCard(idCard);
        if (birthday == null) {
            return "";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date birthDate = sdf.parse(birthday);
            Date now = new Date();
            int age = now.getYear() - birthDate.getYear();
            // 再判断月份和日期
            if (now.getMonth() < birthDate.getMonth() + 1
                    || (now.getMonth() == birthDate.getMonth() + 1 && now.getDate() < birthDate.getDate())) {
                age--;
            }
            return String.valueOf(age);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 检验数字(整数和小数)
     * "0+"包括0的正数
     *
     * @param num
     * @param type
     * @return
     */
    public static boolean checkNum(String num, String type) {
        String eL = "";
        if (type.equals("0+")) {
            eL = "^(\\+)?\\d+(\\.\\d{1,1})?$";// 非负整数
        }
        Pattern p = Pattern.compile(eL);
        Matcher m = p.matcher(num);
        return m.matches();
    }

    /**
     * 图片转base64
     *
     * @param imagePath
     * @return
     */
    public static String imageTobase64(String imagePath) {
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(imagePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    /**
     * 获取时间时间
     *
     * @return
     */
    public static String getCurrentTime() {
        String time = null;
        long round = System.currentTimeMillis() / 1000;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(round * 1000);
        String[] split = date.split("\\s");
        if (split.length > 1) {
            time = split[1];
        }
        return time;
    }

    /**
     * 图片转流
     * @param filePath
     * @return
     */
    public static InputStream convertFileToInputStream(String filePath) {
        File imageFile = new File(filePath);
        try {
            return new FileInputStream(imageFile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 是否是mac地址
     * @param address
     * @return
     */
    public static boolean isValidMacAddress(String address) {
        String macAddress = addColonsToSerial(address);
        String regex = "^([0-9a-fA-F]{2}:){5}[0-9a-fA-F]{2}$";
        return macAddress.matches(regex);
    }

    private static String addColonsToSerial(String serial) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < serial.length(); i++) {
            sb.append(serial.charAt(i));
            if (i % 2 == 1 && i != 11) {
                sb.append(':');
            }
        }
        return sb.toString();
    }

    public static int dip2px(Context context,int dpValue){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 秒转成分秒格式
     * @param seconds
     * @return
     */
    public static String secondsToMMSS(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;

        String result = String.format("%02d:%02d", minutes, secs);
        return result;
    }

    /**
     * 秒转成分秒格式
     * @param seconds
     * @return
     */
    public static String secondsToMSS(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;

        String result = String.format("第%1d分%02d秒", minutes, secs);
        return result;
    }

    /**
     * 验证系统设置参数
     *
     * @param parameter
     * @param messageLog
     * @return
     */
    public static String checkSystem(String parameter, String messageLog) {
        String reault = "";
        if (TextUtils.isEmpty(parameter)) {
            reault = messageLog + "不可为空";
            return reault;
        }
        if (!Pattern.matches(reg, parameter)) {
            reault = messageLog + "只能填写非0的正整数";
            return reault;
        }
        if (messageLog.equals("血氧报警值")) {
            if (parameter.length() >= 3) {
                reault = messageLog + "超出长度";
                return reault;
            }
        } else if (messageLog.equals("场地长度")) {
            Integer changdu = Integer.valueOf(parameter);
            if (changdu > 30) {
                reault = messageLog + "不可大于30";
                return reault;
            }
        } else {
            if (parameter.length() >= 4) {
                reault = messageLog + "超出长度";
                return reault;
            }
        }
        return reault;
    }

    /**
     * 获取一个 View 的缓存视图
     *  (前提是这个View已经渲染完成显示在页面上)
     * @param view
     * @return
     */
    public static Bitmap getCacheBitmapFromView(View view,int width,int height) {
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        view.measure(measuredWidth, measuredHeight);
        //调用layout方法布局后，可以得到viewiew的尺寸大小
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap bmp = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        view.draw(c);
        return bmp;
    }

    /**
     * 将dp值转换为px值，保证尺寸大小不变
     *
     * @return
     */
    public static int dpAdapt(Activity activity, float dp, float widthDpBase) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int heightPixels = dm.heightPixels;//高的像素
        int widthPixels = dm.widthPixels;//宽的像素
//        int densityDpi = dm.densityDpi;//dpi
//        float xdpi = dm.xdpi;//xdpi
//        float ydpi = dm.ydpi;//ydpi
        float density = dm.density;//density=dpi/160,密度比
//        float scaledDensity = dm.scaledDensity;//scaledDensity=dpi/160 字体缩放密度比
        float heightDP = heightPixels / density;//高度的dp
        float widthDP = widthPixels / density;//宽度的dp
        float w=widthDP>heightDP?heightDP:widthDP;
//        final float scale = activity.getResources().getDisplayMetrics().density;
        return (int) (dp *w/widthDpBase* density + 0.5f);
    }
    /**
     * 将sp值转换为px值，保证尺寸大小不变
     *
     * @return
     */
    public static int spAdapt(Activity activity, float sp,float widthDpBase) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int heightPixels = dm.heightPixels;//高的像素
        int widthPixels = dm.widthPixels;//宽的像素
//        int densityDpi = dm.densityDpi;//dpi
//        float xdpi = dm.xdpi;//xdpi
//        float ydpi = dm.ydpi;//ydpi
        float density = dm.density;//density=dpi/160,密度比
//        float scaledDensity = dm.scaledDensity;//scaledDensity=dpi/160 字体缩放密度比
        float heightDP = heightPixels / density;//高度的dp
        float widthDP = widthPixels / density;//宽度的dp
        float w=widthDP>heightDP?heightDP:widthDP;
//        final float scale = activity.getResources().getDisplayMetrics().density;
        return (int) (sp *w/widthDpBase + 0.5f);
    }
}
