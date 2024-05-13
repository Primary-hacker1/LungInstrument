package com.just.machine.ui.activity

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.common.base.CommonBaseActivity
import com.google.gson.Gson
import com.just.machine.model.Constants
import com.just.machine.model.PatientInfoBean
import com.just.machine.model.SharedPreferencesUtils
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
import com.just.machine.model.systemsetting.SixMinSysSettingBean
import com.just.machine.ui.dialog.CommonDialogFragment
import com.just.machine.ui.fragment.sixmin.SixMinPreReportFragment
import com.just.machine.ui.fragment.sixmin.SixMinReportFragment
import com.just.machine.util.FileUtil
import com.just.machine.util.USBTransferUtil
import com.just.news.R
import com.just.news.databinding.ActivitySixMinDetectBinding
import com.justsafe.libview.util.SystemUtil
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class SixMinDetectActivity : CommonBaseActivity<ActivitySixMinDetectBinding>() {

    lateinit var usbTransferUtil: USBTransferUtil //usb工具类
    lateinit var patientBean: PatientInfoBean//患者信息
    lateinit var sysSettingBean: SixMinSysSettingBean//系统设置
    var sixMinReportBloodOxy: SixMinBloodOxygen = SixMinBloodOxygen()//6分钟血氧
    var sixMinReportBloodOther: SixMinReportOther = SixMinReportOther()//6分钟其它信息
    var sixMinReportBloodHeart: SixMinReportHeartBeat = SixMinReportHeartBeat()//6分钟心率
    var sixMinReportBloodHeartEcg: SixMinHeartEcg = SixMinHeartEcg()//6分钟心电
    var sixMinReportWalk: SixMinReportWalk = SixMinReportWalk()//6分钟步数
    var sixMinReportStride: SixMinReportStride = SixMinReportStride()//6分钟步速
    var sixMinReportEvaluation: SixMinReportEvaluation = SixMinReportEvaluation()//6分钟综合评估
    var sixMinReportInfo: SixMinReportInfo = SixMinReportInfo()//6分钟报告信息
    var sixMinReportPrescription: SixMinReportPrescription = SixMinReportPrescription()//6分钟报告处方信息
    var sixMinReportBreathing: SixMinReportBreathing = SixMinReportBreathing()//6分钟报告呼吸率信息
    var selfCheckSelection = "" //试验前疲劳和呼吸量级
    var sixMinPatientId = "" //试验的患者id
    var sixMinReportNo = "" //报告id
    var sixMinReportType = "" //跳转类型 1新增 2编辑

    override fun getViewBinding() = ActivitySixMinDetectBinding.inflate(layoutInflater)

    override fun initView() {
        initUsbConnection()
        initNavigationView()
        initSystemInfo()
        initClickListener()
        copyAssetsFilesToSD()
    }

    private fun initSystemInfo() {
        val gson = Gson()
        sysSettingBean = SixMinSysSettingBean()
        val sixMinSysSetting = SharedPreferencesUtils.instance.sixMinSysSetting
        if (sixMinSysSetting != null && sixMinSysSetting != "") {
            sysSettingBean = gson.fromJson(
                sixMinSysSetting, SixMinSysSettingBean::class.java
            )
        }
    }

    private fun initUsbConnection() {
        selfCheckSelection =
            intent.extras?.getString(Constants.sixMinSelfCheckViewSelection, "").toString()
        sixMinPatientId = intent.extras?.getString(Constants.sixMinPatientInfo, "").toString()
        sixMinReportNo = intent.extras?.getString(Constants.sixMinReportNo, "").toString()
        sixMinReportType = intent.extras?.getString(Constants.sixMinReportType, "").toString()

        usbTransferUtil = USBTransferUtil.getInstance()
        usbTransferUtil.init(this)
        usbTransferUtil.connect()
        usbTransferUtil.bloodOxyLineData.clear()
        usbTransferUtil.setOnUSBDateReceive {
            runOnUiThread {
                if (it.equals("android.hardware.usb.action.USB_DEVICE_DETACHED")) {
                    //usb设备拔出
                    binding.sixminIvEcg.setImageResource(R.mipmap.xinlvno)
                    binding.sixminIvBloodPressure.setImageResource(R.mipmap.xueyano)
                    binding.sixminIvBloodOxygen.setImageResource(R.mipmap.xueyangno)
                    binding.sixminIvBatteryStatus.setImageResource(R.mipmap.dianchi00)
                }
            }
        }

        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");
    }

    private fun initClickListener() {
        binding.sixminIvClose.setOnClickListener {
            val navHostFragment =
                supportFragmentManager.primaryNavigationFragment as NavHostFragment?
            val currentFragment: Fragment? =
                navHostFragment!!.childFragmentManager.primaryNavigationFragment
            if (currentFragment is SixMinPreReportFragment) {
                if (sixMinReportType.isEmpty() || sixMinReportType == "1") {
                    val startCommonDialogFragment = CommonDialogFragment.startCommonDialogFragment(
                        supportFragmentManager, "退出将视为放弃生成报告，是否确定?"
                    )
                    startCommonDialogFragment.setCommonDialogOnClickListener(object :
                        CommonDialogFragment.CommonDialogClickListener {
                        override fun onPositiveClick() {
                            finish()
                        }

                        override fun onNegativeClick() {

                        }

                        override fun onStopNegativeClick(stopReason: String) {

                        }
                    })
                }else{
                    finish()
                }
            } else if (currentFragment is SixMinReportFragment) {
                if (sixMinReportType.isEmpty() || sixMinReportType == "1") {
                    PatientActivity.startPatientActivity(this, "finishSixMinTest")
                }
                finish()
            } else {
                if (usbTransferUtil.isBegin) {
                    val startCommonDialogFragment = CommonDialogFragment.startCommonDialogFragment(
                        supportFragmentManager,
                        getString(R.string.sixmin_test_start_exit_test_tips)
                    )
                    startCommonDialogFragment.setCommonDialogOnClickListener(object :
                        CommonDialogFragment.CommonDialogClickListener {
                        override fun onPositiveClick() {
                            finish()
                        }

                        override fun onNegativeClick() {

                        }

                        override fun onStopNegativeClick(stopReason: String) {

                        }
                    })
                } else {
                    onBackPressed()
                }
            }
        }
    }

    private fun initNavigationView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.sixmin_detect_layout) as NavHostFragment
        val navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.nav_sixmin)

        // 假设您有条件决定启动的目的地
        val startDestinationId = if (sixMinReportType == "1" || sixMinReportType.isEmpty()) {
            R.id.sixMinFragment
        } else {
            R.id.sixMinPreReportFragment
        }
        navGraph.setStartDestination(startDestinationId)
        navController.graph = navGraph
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            SystemUtil.immersive(this, true)
        }
    }

    /**
     * 将assets文件复制到内存卡
     */
    private fun copyAssetsFilesToSD() {
        try {
            val stringNames = assets.list("templates/png")
            var srcFolderSize: Long = 0
            stringNames?.forEach { name ->
                val length = assets.open("templates/png/$name").available()
                srcFolderSize += length
            }
            val dstFolderSize = FileUtil.getInstance(this)
                .getFolderSize(getExternalFilesDir("")?.absolutePath + File.separator + "templates")
            if (srcFolderSize != dstFolderSize) {
                FileUtil.getInstance(this).copyAssetsToSD("templates/png", "templates")
                    .setFileOperateCallback(object : FileUtil.FileOperateCallback {
                        override fun onSuccess() {
//                            showMsg("复制成功")
                        }

                        override fun onFailed(error: String?) {
//                            showMsg("复制失败")
                        }
                    })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setToolbarTitle(title: String) {
        binding.sixminTvTestStatus.text = title
    }

    fun setBatterStatus(res: Int) {
        binding.sixminIvBatteryStatus.setImageResource(res)
    }

    fun setEcgStatus(res: Int) {
        binding.sixminIvEcg.setImageResource(res)
    }

    fun setBloodOxyStatus(res: Int) {
        binding.sixminIvBloodOxygen.setImageResource(res)
    }

    fun setBloodPressureStatus(res: Int) {
        binding.sixminIvBloodPressure.setImageResource(res)
    }

    override fun onResume() {
        super.onResume()
        if (!usbTransferUtil.isConnectUSB) {
            binding.sixminIvEcg.setImageResource(R.mipmap.xinlvno)
            binding.sixminIvBloodPressure.setImageResource(R.mipmap.xueyano)
            binding.sixminIvBloodOxygen.setImageResource(R.mipmap.xueyangno)
        }
        SystemUtil.immersive(this, true)
    }

    override fun onBackPressed() {
        val navHostFragment =
            supportFragmentManager.primaryNavigationFragment as NavHostFragment?
        val currentFragment: Fragment? =
            navHostFragment!!.childFragmentManager.primaryNavigationFragment
        if (currentFragment is SixMinPreReportFragment) {
            if (sixMinReportType.isEmpty() || sixMinReportType == "1") {
                val startCommonDialogFragment = CommonDialogFragment.startCommonDialogFragment(
                    supportFragmentManager, "退出将视为放弃生成报告，是否确定?"
                )
                startCommonDialogFragment.setCommonDialogOnClickListener(object :
                    CommonDialogFragment.CommonDialogClickListener {
                    override fun onPositiveClick() {
                        finish()
                    }

                    override fun onNegativeClick() {

                    }

                    override fun onStopNegativeClick(stopReason: String) {

                    }
                })
            }else{
                finish()
            }
        } else if (currentFragment is SixMinReportFragment) {
            if (sixMinReportType.isEmpty() || sixMinReportType == "1") {
                PatientActivity.startPatientActivity(this, "finishSixMinTest")
            }
            finish()
        } else {
            if (usbTransferUtil.isBegin) {
                val startCommonDialogFragment = CommonDialogFragment.startCommonDialogFragment(
                    supportFragmentManager,
                    getString(R.string.sixmin_test_start_exit_test_tips)
                )
                startCommonDialogFragment.setCommonDialogOnClickListener(object :
                    CommonDialogFragment.CommonDialogClickListener {
                    override fun onPositiveClick() {
                        finish()
                    }

                    override fun onNegativeClick() {

                    }

                    override fun onStopNegativeClick(stopReason: String) {

                    }
                })
            } else {
                super.onBackPressed()
            }
        }
    }
}