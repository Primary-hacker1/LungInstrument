package com.just.machine.model

interface Constants {
    companion object {
        const val isDebug = true//是否是测试模式
        const val news = "首页"
        const val addPatient = "添加患者信息"
        const val patientInformation = "患者管理"
        const val cardiopulmonary = "心肺测试"
        const val me = "我的"
        const val setting = "设置"
        const val serialCallback = "serialCallback"//串口返回消息
        const val patientBean = "patientBean"
        const val editBloodPressure = "editBloodPressure"//6分钟预生成报告编辑血压
        const val finishSixMinTest = "finishSixMinTest"//完成6分钟试验
        const val sixMinSelfCheck = "sixMinSelfCheck"//6分钟试验选择试验前状况评级
        const val sixMinSelfCheckView = "sixMinSelfCheckView"//6分钟预生成报告查询试验前状况评级
        const val sixMinSelfCheckViewSelection = "sixMinSelfCheckViewSelection"//6分钟预生成报告查询试验前状况评级选择
        const val sixMinPatientInfo = "sixMinSelfCheckView"//6分钟试验患者信息
        const val sixMinReportNo = "sixMinSelfCheckView"//6分钟试验患者信息
        const val commonDialogContent = "sixMinReportNo"//6分钟试验报告No
        const val commonDialogType = "commonDialogType"//标准dialog类型
        const val commonDialogStopType = "commonDialogStopType"//标准dialog是否提前停止
    }
}
