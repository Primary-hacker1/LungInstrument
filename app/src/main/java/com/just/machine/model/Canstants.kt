package com.just.machine.model

interface Constants {
    companion object {
        const val isDebug = true//是否是测试模式
        const val news = "首页"
        const val assessmentSaves = "运动评估保存"
        const val assessmentReset = "运动评估重置"
        const val addPatient = "添加患者信息"
        const val patientInformation = "患者管理"
        const val cardiopulmonary = "心肺测试"
        const val me = "我的"
        const val save = "保存"
        const val reset = "重置"
        const val settingsAreSaved = "设置保存"
        const val setting = "设置"
        const val time: String = "time"
        const val serialCallback = "环境定标"//串口返回消息
        const val patientDialog = "patientDialog"
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
        const val prescriptionBean= "prescriptionBean"//6分钟处方参数
        const val sixMinReportType= "sixMinReportType"//6分钟跳转预生成报告类型 1,试验结束跳转 2，患者管理跳转去编辑
        const val loadingDialogContent = "loadingDialogContent"//loading弹窗文案
        const val sixMinLiveDataBusKey = "simMinTest"//6分钟USB数据key
        const val sixMinFaceMask= "sixMinFaceMask"//6分钟是否佩戴面罩
        const val sixMinEcgVisibleLeft = "sixMinEcgVisibleLeft"//6分钟心电回放位置

        const val envCaliSerialCallback = "环境定标"//串口返回消息
        const val getDevInfoSerialCallback = "获取设备信息"//串口返回消息
        const val LungData = "动态心肺测试"
        const val twoSensorSerialCallback = "二类传感器"

        /**
         * 定标
         */
        const val clickStartFlowCalibra= "clickFlowStart"//点击开始流量定标
        const val clickStopFlowCalibra= "clickFlowStop"//点击停止流量定标
        const val flowHandleCalibra= "handleFlow"//流量手动定标
        const val flowAutoCalibra= "autoFlow"//流量自动定标
        const val startFlowHCalibra= "flowStart"//开始流量定标
        const val stopFlowCalibra= "flowStop"//停止流量定标
        const val oneKeyCalibraEvent = "oneKeyCalibra"//一键定标事件
        const val oneKeyCalibraEventEnvironment = "environment"//一键定标事件-环境定标
        const val oneKeyCalibraEventFlowAuto = "flowAuto"//一键定标事件-自动流量定标
        const val oneKeyCalibraEventIngredient = "ingredient"//一键定标事件-成分定标
        const val oneKeyCalibraResultEnvironmentSuccess = "environmentSuccess"//一键定标事件-环境定标成功
        const val oneKeyCalibraResultEnvironmentFailed = "environmentFailed"//一键定标事件-环境定标失败
        const val oneKeyCalibraResultFlowAutoSuccess = "flowAutoSuccess"//一键定标事件-自动流量定标成功
        const val oneKeyCalibraResultFlowAutoFailed = "flowAutoFailed"//一键定标事件-自动流量定标失败
        const val oneKeyCalibraResultIngredientSuccess = "ingredientSuccess"//一键定标事件-成分定标定标成功
        const val oneKeyCalibraResultIngredientFailed = "ingredientFailed"//一键定标事件-成分定标定标失败
    }
}
