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
        const val serialCallback = "环境定标"//串口返回消息
        const val patientBean = "patientBean"
        const val editBloodPressure = "editBloodPressure"//6分钟预生成报告编辑血压
        const val finishSixMinTest = "finishSixMinTest"//完成6分钟试验
        const val sixMinSelfCheck = "sixMinSelfCheck"//6分钟试验选择试验前状况评级
        const val sixMinSelfCheckView = "sixMinSelfCheckView"//6分钟预生成报告查询试验前状况评级
        const val sixMinSelfCheckViewSelection = "sixMinSelfCheckViewSelection"//6分钟预生成报告查询试验前状况评级选择
        const val sixMinPatientInfo = "sixMinPatientInfo"//6分钟试验患者信息
        const val sixMinReportNo = "sixMinReportNo"//6分钟试验吧
        const val commonDialogContent = "commonDialogContent"//标准dialog内容
        const val commonDialogType = "commonDialogType"//标准dialog类型
        const val commonDialogStopType = "commonDialogStopType"//标准dialog是否提前停止
        const val deleteWarnDialogContent= "deleteWarnDialogContent"//删除提示对话框内容
    }
}
