package com.just.machine.dao.setting

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.just.machine.dao.ChatItemConverter


/**
 *create by 2024/5/21
 *@author zt
 */
@Entity(tableName = "all_setting")
@TypeConverters(ChatItemConverter::class)
data class AllSettingBean(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    var allSettingId: Long = 0,
    var pass: String? = "",
    var hospitalName: String? = "",//医院名称
    var projectedValueScenarios: String? = "Zapletal",//预计值方案
    var breathingItem: String? = "硅胶面罩(特大)",//呼吸面罩

    var testDeadSpace: Int = 0,//测试死腔
    var ecg: String? = "",//心电仪
)