package com.just.machine.helper

import com.just.machine.App

/**
 *create by 2020/6/19
 *@author zt
 */
object UriConfig {
    const val BASE_URL = "http://192.168.1.105:8080/inspect/" //本地测试

    const val DATABASE_NAME = "news-db"

    const val PLANT_DATA_FILENAME = "plants.json"

    val LOG_PATH: String = App.instance?.getExternalFilesDir("log")!!.path//本地日志输出
}