package com.just.machine.dao

import androidx.room.TypeConverter
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import com.just.machine.model.CPETParameter
import com.just.machine.model.CardiopulmonaryRecordsBean
import com.just.machine.model.sixmininfo.SixMinRecordsBean

/**
 *中转list不然room不能用
 */
class ChatItemConverter {
    @TypeConverter
    fun objectToTestRecordsBean(list: List<CardiopulmonaryRecordsBean?>?): String? {
        return GsonInstance.gson.toJson(list)
    }

    @TypeConverter
    fun stringToTestRecordsBean(json: String?): List<CardiopulmonaryRecordsBean>? {
        val listType = object : TypeToken<List<CardiopulmonaryRecordsBean?>?>() {}.type
        return GsonInstance.gson.fromJson(json, listType)
    }

    @TypeConverter
    fun objectToSixMinRecordsBean(list: List<SixMinRecordsBean?>?): String? {
        return GsonInstance.gson.toJson(list)
    }
    @TypeConverter

    fun stringToSixMinRecordsBean(json: String?): List<SixMinRecordsBean>? {
        val listType = object : TypeToken<List<SixMinRecordsBean?>?>() {}.type
        return GsonInstance.gson.fromJson(json, listType)
    }

    @TypeConverter
    fun objectToCPETParameter(list: List<CPETParameter?>?): String? {
        return GsonInstance.gson.toJson(list)
    }

    @TypeConverter
    fun stringToCPETParameter(json: String?): List<CPETParameter>? {
        val listType = object : TypeToken<List<CPETParameter?>?>() {}.type
        return GsonInstance.gson.fromJson(json, listType)
    }

    object GsonInstance {
        val gson: Gson by lazy { Gson() }
    }

}