package com.just.machine.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.common.viewmodel.LiveDataEvent
import com.just.machine.model.Constants
import com.just.machine.model.SharedPreferencesUtils
import com.just.machine.ui.activity.CardiopulmonaryActivity
import com.just.machine.ui.activity.PatientActivity
import com.just.machine.ui.activity.SixMinDetectActivity
import com.just.machine.ui.dialog.CommonDialogFragment
import com.just.machine.ui.dialog.PatientDialogFragment
import com.just.machine.ui.dialog.SixMinReportSelfCheckBeforeTestFragment
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.USBTransferUtil
import com.just.news.R
import com.just.news.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2020/6/19
 * 主界面
 *@author zt
 */
@AndroidEntryPoint
class MainFragment : CommonBaseFragment<FragmentMainBinding>() {

    private val viewModel by viewModels<MainViewModel>()
    private var patientListSize = 0
    private lateinit var usbTransferUtil: USBTransferUtil
    override fun loadData() {//懒加载
        viewModel.getPatients()
        viewModel.mEventHub.observe(this) {
            when (it.action) {
                LiveDataEvent.QuerySuccess -> {
                    if (it.any is List<*>) {
                        val datas = it.any as MutableList<*>
                        patientListSize = datas.size
                    }
                }
            }
        }
    }

    override fun initView() {
        initSixMinUsbConnection()
    }

    override fun initListener() {
        binding.walkTestButton.setNoRepeatListener {
            when (patientListSize) {
                0 -> {
                    val patientDialogFragment =
                        PatientDialogFragment.startPatientDialogFragment(parentFragmentManager)//添加患者修改患者信息
                    patientDialogFragment.setDialogOnClickListener(object :
                        PatientDialogFragment.PatientDialogListener {
                        override fun onClickConfirmBtn(patientId:String) {//确认
                            SharedPreferencesUtils.instance.isClickBtn = "1"
                            patientDialogFragment.dismiss()
                            checkBluetoothAndSelfCheck(patientId)
                        }

                        override fun onClickCleanBtn() {//取消
                            patientDialogFragment.dismiss()
                        }
                    })
                }

                else -> {
//                    val intent = Intent(activity, SixMinActivity::class.java)
//                    startActivity(intent)
                    checkBluetoothAndSelfCheck("")
                }
            }
        }

        binding.btnEcg.setNoRepeatListener {//心肺测试
            val isClick = SharedPreferencesUtils.instance.isClickBtn
//            if (Constants.isDebug) {
//                isClick = "1"
//            }
            when (isClick) {
                "" -> {
                    val patientDialogFragment =
                        PatientDialogFragment.startPatientDialogFragment(parentFragmentManager)//添加患者修改患者信息
                    patientDialogFragment.setDialogOnClickListener(object :
                        PatientDialogFragment.PatientDialogListener {
                        override fun onClickConfirmBtn(patientId:String) {//确认
//                            SharedPreferencesUtils.instance.isClickBtn = "1"
                            patientDialogFragment.dismiss()
                            CardiopulmonaryActivity.startCardiopulmonaryActivity(context)
                        }

                        override fun onClickCleanBtn() {//取消
                            patientDialogFragment.dismiss()
                        }
                    })
                }

                "1" -> {
                    CardiopulmonaryActivity.startCardiopulmonaryActivity(context)
                }

                null -> TODO()
            }
        }

        binding.btnPatientInformation.setNoRepeatListener {
            PatientActivity.startPatientActivity(context, null)
        }

        binding.btnClose.setNoRepeatListener {
            SharedPreferencesUtils.instance.logout()
            activity?.finish()
        }
    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMainBinding.inflate(inflater, container, false)

    private fun initSixMinUsbConnection() {
        usbTransferUtil = USBTransferUtil.getInstance()
        usbTransferUtil.init(requireContext())
        usbTransferUtil.connect()
    }

    private fun checkBluetoothAndSelfCheck(patientId:String) {
        if (usbTransferUtil.isConnectUSB && usbTransferUtil.bloodOxygenConnection && usbTransferUtil.ecgConnection && usbTransferUtil.bloodPressureConnection) {
            val selfCheckBeforeTestDialogFragment =
                SixMinReportSelfCheckBeforeTestFragment.startPatientSelfCheckDialogFragment(
                    activity!!.supportFragmentManager, "1", "1"
                )
            selfCheckBeforeTestDialogFragment.setSelfCheckBeforeTestDialogOnClickListener(object :
                SixMinReportSelfCheckBeforeTestFragment.SixMinReportSelfCheckBeforeTestDialogListener {
                override fun onClickConfirm(
                    befoFatigueLevel: Int,
                    befoBreathingLevel: Int,
                    befoFatigueLevelStr: String,
                    befoBreathingLevelStr: String,
                    faceMaskStr:String
                ) {
                    val intent = Intent(activity, SixMinDetectActivity::class.java)
                    val bundle = Bundle()
                    bundle.putString(Constants.sixMinSelfCheckViewSelection,"$befoFatigueLevelStr&$befoBreathingLevelStr")
                    bundle.putString(Constants.sixMinPatientInfo, patientId)
                    bundle.putString(Constants.sixMinReportType, "1")
                    bundle.putString(Constants.sixMinFaceMask,faceMaskStr)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }

                override fun onClickClose() {

                }
            })
        } else {
            activity?.supportFragmentManager?.let { it1 ->
                val startCommonDialogFragment = CommonDialogFragment.startCommonDialogFragment(
                    it1, getString(R.string.sixmin_test_enter_test_without_device_connection)
                )
                startCommonDialogFragment.setCommonDialogOnClickListener(object :
                    CommonDialogFragment.CommonDialogClickListener {
                    override fun onPositiveClick() {
                        val selfCheckBeforeTestDialogFragment =
                            SixMinReportSelfCheckBeforeTestFragment.startPatientSelfCheckDialogFragment(
                                activity!!.supportFragmentManager, "1", "1"
                            )
                        selfCheckBeforeTestDialogFragment.setSelfCheckBeforeTestDialogOnClickListener(
                            object :
                                SixMinReportSelfCheckBeforeTestFragment.SixMinReportSelfCheckBeforeTestDialogListener {
                                override fun onClickConfirm(
                                    befoFatigueLevel: Int,
                                    befoBreathingLevel: Int,
                                    befoFatigueLevelStr: String,
                                    befoBreathingLevelStr: String,
                                    faceMaskStr:String
                                ) {
                                    val intent = Intent(activity, SixMinDetectActivity::class.java)
                                    val bundle = Bundle()
                                    bundle.putString(Constants.sixMinSelfCheckViewSelection,"$befoFatigueLevelStr&$befoBreathingLevelStr")
                                    bundle.putString(Constants.sixMinPatientInfo,"")
                                    bundle.putString(Constants.sixMinReportType, "1")
                                    bundle.putString(Constants.sixMinFaceMask,faceMaskStr)
                                    intent.putExtras(bundle)
                                    startActivity(intent,bundle)
                                }

                                override fun onClickClose() {

                                }
                            })
                    }

                    override fun onNegativeClick() {

                    }

                    override fun onStopNegativeClick(stopReason: String) {

                    }
                })
            }
        }
    }

    override fun onDestroy() {
        usbTransferUtil.disconnect()
        super.onDestroy()
    }
}