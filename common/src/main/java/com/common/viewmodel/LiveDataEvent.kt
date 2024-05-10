package com.common.viewmodel


class LiveDataEvent {

    companion object {

        /**
         * @param SUCCESS -通用数据库成功
         * @return
         */
        const val SUCCESS: Int = 0

        const val QuerySuccess: Int = 0x01
        const val QueryNameId: Int = 0x02

        const val QueryPatient: Int = 0x03

        const val QueryPatientNull: Int = 0x04

        const val EnvironmentalsSuccess: Int = 0x05

        const val QuerySixMinReportEvaluationSuccess: Int = 0x06//6分钟报告综合评估

        const val QuerySixMinReportInfoSuccess: Int = 0x07//6分钟报告信息

        const val QuerySixMinReportWalkSuccess: Int = 0x08//6分钟报告步数

        const val QuerySixMinReportBloodOxySuccess: Int = 0x09//6分钟报告血氧

        const val QuerySixMinReportHeartEcgSuccess: Int = 0x10//6分钟报告心电

        const val QuerySixMinReportHeartBeatSuccess: Int = 0x11//6分钟报告心率

        const val QuerySixMinReportOtherSuccess: Int = 0x12//6分钟报告其它

        const val QuerySixMinReportPrescriptionSuccess: Int = 0x13//6分钟报告处方建议

        const val QuerySixMinReportStrideSuccess: Int = 0x14//6分钟报告步速

        const val QuerySixMinReportBreathingSuccess: Int = 0x15//6分钟报告呼吸率

        const val FLOWSUCCESS: Int = 0x017

        const val INGREDIENTSSUCCESS: Int = 0x019

        const val STATICSETTINGSSUCCESS: Int = 0x021


        const val MaxPatient: Int = 0x16//6分钟报告呼吸率

        /**
         * @param FAIL -通用错误
         * @return -
         */
        const val FAIL: Int = 0x88

    }

    var action = 0
    var any: Any? = null
    var anyOne: Any? = null


    //有参次构造器
    constructor(action: Int, any: Any) {
        this.action = action
        this.any = any
    }

    //有双参次构造器
    constructor(action: Int, any: Any, anyOne: Any?) {
        this.action = action
        this.any = any
        this.anyOne = anyOne
    }
}