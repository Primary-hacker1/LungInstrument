package com.justsafe.libview.view

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object DateManagementTool {

    // 获取当前日期和时间
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDateTimes(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return currentDateTime.format(formatter)
    }

    // 获取当前日期和时间
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDateTime(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        return currentDateTime.format(formatter)
    }

    // 计算两个日期之间的天数差
    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateDaysBetween(startDate: String, endDate: String): Long {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val start = LocalDate.parse(startDate, formatter)
        val end = LocalDate.parse(endDate, formatter)
        return ChronoUnit.DAYS.between(start, end)
    }

    // 添加天数到某个日期
    @RequiresApi(Build.VERSION_CODES.O)
    fun addDaysToDate(date: String, daysToAdd: Long): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val localDate = LocalDate.parse(date, formatter)
        val newDate = localDate.plusDays(daysToAdd)
        return newDate.format(formatter)
    }

    // 获取某个日期的星期几
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDayOfWeek(date: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val localDate = LocalDate.parse(date, formatter)
        return localDate.dayOfWeek.toString()
    }

    // 检查一个年份是否是闰年
    @RequiresApi(Build.VERSION_CODES.O)
    fun isLeapYear(year: Int): Boolean {
        return LocalDate.of(year, 1, 1).isLeapYear
    }
}