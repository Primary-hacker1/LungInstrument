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
        const val patientBean = "patientBean"
        const val editBloodPressure = "editBloodPressure"//6分钟预生成报告编辑血压
        const val finishSixMinTest = "finishSixMinTest"//完成6分钟试验
        const val sixMinSelfCheck = "sixMinSelfCheck"//6分钟试验选择试验前状况评级
        const val sixMinSelfCheckView = "sixMinSelfCheckView"//6分钟预生成报告查询试验前状况评级
        const val sixMinPatientInfo = "sixMinSelfCheckView"//6分钟试验患者信息
        const val commonDialogContent = "commonDialogContent"//标准dialog显示内容
    }
}
