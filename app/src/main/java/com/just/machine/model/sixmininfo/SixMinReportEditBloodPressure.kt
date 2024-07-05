package com.just.machine.model.sixmininfo

import android.os.Parcel
import android.os.Parcelable

data class SixMinReportEditBloodPressure(
    var lowBloodPressureBefore: String? = "",
    var highBloodPressureBefore: String? = "",
    var lowBloodPressureAfter: String? = "",
    var highBloodPressureAfter: String? = ""


) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(lowBloodPressureBefore)
        parcel.writeString(highBloodPressureBefore)
        parcel.writeString(lowBloodPressureAfter)
        parcel.writeString(highBloodPressureAfter)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SixMinReportEditBloodPressure> {
        override fun createFromParcel(parcel: Parcel): SixMinReportEditBloodPressure {
            return SixMinReportEditBloodPressure(parcel)
        }

        override fun newArray(size: Int): Array<SixMinReportEditBloodPressure?> {
            return arrayOfNulls(size)
        }
    }
}