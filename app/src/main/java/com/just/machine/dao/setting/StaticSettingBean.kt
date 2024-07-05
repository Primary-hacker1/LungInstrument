package com.just.machine.dao.setting

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.just.machine.dao.ChatItemConverter
import com.just.machine.model.CPETParameter


/**
 *create by 2024/3/15
 *@author zt
 */
@Entity(tableName = "static_setting")
@TypeConverters(ChatItemConverter::class)
data class StaticSettingBean(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    var settingId: Long = 0,
    var xTimeSvc: String? = "30",
    var yTimeUpSvc: String? = "5",
    var yTimeDownSvc: String? = "-5",

    var xTimeFvc: String? = "",
    var yTimeUpFvc: String? = "",
    var yTimeDownFvc: String? = "",

    var xTimeMvv: String? = "",
    var yTimeUpMvv: String? = "",
    var yTimeDownMvv: String? = "",

    var radioVt: Boolean? = true,//是否显示vt
    var radioVideoAutoplaySvc: Boolean? = true,//是否自动播放
    var radioPredictionRing: Boolean? = true,//预测值环是否显示
    var radioVideoAutoplayFvc: Boolean? = true,//是否自动播放
    var radioStartRuler: Boolean? = true,//开始结算标尺是否显示
    var radioVentilationCurve: Boolean? = true,//通气曲线是否显示
    var radioVideoAutoplayMvv: Boolean? = true,//是否自动播放

    var settingSVC: MutableList<CPETParameter> = ArrayList(),
    var settingFVC: MutableList<CPETParameter> = ArrayList(),
    var settingMVV: MutableList<CPETParameter> = ArrayList()
)