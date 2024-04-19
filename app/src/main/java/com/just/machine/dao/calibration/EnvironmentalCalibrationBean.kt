package com.just.machine.dao.calibration

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.just.machine.dao.PatientBean

/*
系统定标界面
环境定标，创建时间、温度、湿度、大气压

*/
@Entity(tableName = "environmental")
data class EnvironmentalCalibrationBean(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    val environmentalId: Long = 0,

    val userId: Long,

    var createTime: String? = "",//创建时间
    var temperature: String? = "",//温度
    var humidity: String? = "",//湿度
    var pressure: String? = "",//大气压

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(environmentalId)
        parcel.writeLong(userId)
        parcel.writeString(createTime)
        parcel.writeString(temperature)
        parcel.writeString(humidity)
        parcel.writeString(pressure)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EnvironmentalCalibrationBean> {
        override fun createFromParcel(parcel: Parcel): EnvironmentalCalibrationBean {
            return EnvironmentalCalibrationBean(parcel)
        }

        override fun newArray(size: Int): Array<EnvironmentalCalibrationBean?> {
            return arrayOfNulls(size)
        }
    }
}

data class PlantWithEnvironmental(
    @Embedded val plant: PatientBean,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val environmental: List<EnvironmentalCalibrationBean>
)