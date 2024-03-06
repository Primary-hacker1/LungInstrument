package com.just.machine.dao

import androidx.room.TypeConverter

import com.google.gson.reflect.TypeToken
import com.just.machine.model.CardiopulmonaryRecordsBean
import com.just.machine.model.SixMinRecordsBean

/**
 *中转list不然room不能用
 */
class ChatItemConverter {
    @TypeConverter
    fun objectToTestRecordsBean(list: List<CardiopulmonaryRecordsBean?>?): String? {
        return GsonInstance.instance?.gson?.toJson(list)
    }

    @TypeConverter
    fun stringToTestRecordsBean(json: String?): List<CardiopulmonaryRecordsBean>? {
        val listType = object : TypeToken<List<CardiopulmonaryRecordsBean?>?>() {}.type
        return GsonInstance.instance?.gson?.fromJson(json, listType)
    }

    @TypeConverter
    fun objectToSixMinRecordsBean(list: List<SixMinRecordsBean?>?): String? {
        return GsonInstance.instance?.gson?.toJson(list)
    }

    @TypeConverter
    fun stringToSixMinRecordsBean(json: String?): List<SixMinRecordsBean>? {
        val listType = object : TypeToken<List<SixMinRecordsBean?>?>() {}.type
        return GsonInstance.instance?.gson?.fromJson(json, listType)
    }

}