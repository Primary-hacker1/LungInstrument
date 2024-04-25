package com.just.machine.model

import androidx.room.Embedded
import androidx.room.Relation
import com.just.machine.dao.PatientBean
import com.just.machine.model.sixminreport.SixMinReportInfo

data class PatientInfoBean(
    @Embedded var infoBean: PatientBean = PatientBean(),

    @Relation(
        parentColumn = "id",
        entityColumn = "patientId"
    )
    var sixMinReportInfo: MutableList<SixMinReportInfo> = mutableListOf(),
)
