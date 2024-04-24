package com.just.machine.model

import androidx.room.Embedded
import androidx.room.Relation
import com.just.machine.dao.calibration.EnvironmentalCalibrationBean
import com.just.machine.model.sixminreport.SixMinBloodOxygen
import com.just.machine.model.sixminreport.SixMinHeartEcg
import com.just.machine.model.sixminreport.SixMinReportBreathing
import com.just.machine.model.sixminreport.SixMinReportEvaluation
import com.just.machine.model.sixminreport.SixMinReportHeartBeat
import com.just.machine.model.sixminreport.SixMinReportInfo
import com.just.machine.model.sixminreport.SixMinReportOther
import com.just.machine.model.sixminreport.SixMinReportPrescription
import com.just.machine.model.sixminreport.SixMinReportStride
import com.just.machine.model.sixminreport.SixMinReportWalk

data class SixMinRecordsBean(
    @Embedded var infoBean: SixMinReportInfo = SixMinReportInfo(),

    @Relation(
        parentColumn = "reportNo",
        entityColumn = "reportId"
    )
    var bloodOxyBean: List<SixMinBloodOxygen> = mutableListOf(),

    @Relation(
        parentColumn = "reportNo",
        entityColumn = "reportId"
    )
    var heartEcgBean: List<SixMinHeartEcg> = mutableListOf(),

    @Relation(
        parentColumn = "reportNo",
        entityColumn = "reportId"
    )
    var breathingBean: List<SixMinReportBreathing> = mutableListOf(),

    @Relation(
        parentColumn = "reportNo",
        entityColumn = "reportId"
    )
    var evaluationBean: List<SixMinReportEvaluation> = mutableListOf(),

    @Relation(
        parentColumn = "reportNo",
        entityColumn = "reportId"
    )
    var heartBeatBean: List<SixMinReportHeartBeat> = mutableListOf(),

    @Relation(
        parentColumn = "reportNo",
        entityColumn = "reportId"
    )
    var otherBean: List<SixMinReportOther> = mutableListOf(),

    @Relation(
        parentColumn = "reportNo",
        entityColumn = "reportId"
    )
    var prescriptionBean: List<SixMinReportPrescription> = mutableListOf(),

    @Relation(
        parentColumn = "reportNo",
        entityColumn = "reportId"
    )
    var walkBean: List<SixMinReportWalk> = mutableListOf(),

    @Relation(
        parentColumn = "reportNo",
        entityColumn = "reportId"
    )
    var strideBean: List<SixMinReportStride> = mutableListOf()
)
