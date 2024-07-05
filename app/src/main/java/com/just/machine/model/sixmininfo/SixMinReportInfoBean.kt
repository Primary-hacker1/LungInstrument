package com.just.machine.model.sixmininfo

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

data class SixMinReportInfoBean(
    var infoBean: SixMinReportInfo = SixMinReportInfo(),
    var bloodOxyBean: SixMinBloodOxygen = SixMinBloodOxygen(),
    var heartEcgBean: SixMinHeartEcg = SixMinHeartEcg(),
    var breathingBean: SixMinReportBreathing = SixMinReportBreathing(),
    var evaluationBean: SixMinReportEvaluation = SixMinReportEvaluation(),
    var heartBeatBean: SixMinReportHeartBeat = SixMinReportHeartBeat(),
    var otherBean: SixMinReportOther = SixMinReportOther(),
    var prescriptionBean: SixMinReportPrescription? = SixMinReportPrescription(),
    var walkBean: SixMinReportWalk = SixMinReportWalk(),
    var strideBean: SixMinReportStride = SixMinReportStride(),
)