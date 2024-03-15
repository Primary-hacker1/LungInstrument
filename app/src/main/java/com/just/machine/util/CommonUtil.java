package com.just.machine.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CommonUtil {

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
}
