package com.just.machine.ui.fragment.sixmin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.common.network.LogUtils
import com.google.gson.Gson
import com.just.machine.model.SharedPreferencesUtils
import com.just.machine.model.systemsetting.SixMinSysSettingBean
import com.just.machine.ui.activity.SixMinDetectActivity
import com.just.machine.ui.dialog.SixMinPermissionDialogFragment
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.CommonUtil
import com.just.machine.util.KeyboardUtil
import com.just.machine.util.SixMinCmdUtils
import com.just.news.R
import com.just.news.databinding.FragmentSixminReportBinding
import com.just.news.databinding.FragmentSixminSystemSettingBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 6分钟系统设置界面
 */
@AndroidEntryPoint
class SixMinSystemSettingFragment : CommonBaseFragment<FragmentSixminSystemSettingBinding>() {

    private var autoCircleCount: Boolean = true
    private lateinit var mActivity: SixMinDetectActivity
    private var time = 0
    private var hasPassPermission = false

    override fun loadData() {//懒加载

    }


    override fun initView() {
        if (activity is SixMinDetectActivity) {
            mActivity = activity as SixMinDetectActivity
        }
        initToolbar()
        initSysInfo()
    }

    override fun initListener() {
        binding.sixminRlAutoCircleCount.setOnClickListener {
            if (!autoCircleCount) {
                binding.sixminRlAutoCircleCount.setBackgroundResource(R.drawable.sixmin_circle_select_bg)
                binding.sixminRlHandleCircleCount.setBackgroundResource(R.drawable.sixmin_circle_unselect_bg)
                binding.sixminTvAutoCircleCount.setTextColor(
                    ContextCompat.getColor(
                        mActivity, R.color.colorPrimary
                    )
                )
                binding.sixminTvHandleCircleCount.setTextColor(
                    ContextCompat.getColor(
                        mActivity, R.color.text3
                    )
                )
            }
            autoCircleCount = !autoCircleCount
        }
        binding.sixminRlHandleCircleCount.setOnClickListener {
            if (autoCircleCount) {
                binding.sixminRlAutoCircleCount.setBackgroundResource(R.drawable.sixmin_circle_unselect_bg)
                binding.sixminRlHandleCircleCount.setBackgroundResource(R.drawable.sixmin_circle_select_bg)
                binding.sixminTvAutoCircleCount.setTextColor(
                    ContextCompat.getColor(
                        mActivity, R.color.text3
                    )
                )
                binding.sixminTvHandleCircleCount.setTextColor(
                    ContextCompat.getColor(
                        mActivity, R.color.colorPrimary
                    )
                )
            }
            autoCircleCount = !autoCircleCount
        }
        binding.sixminLlSave.setNoRepeatListener {
            mActivity.initSystemInfo()
            val gson = Gson()
            val sysSettingBean = mActivity.sysSettingBean

            //报警设置
            sysSettingBean.sysAlarm.highPressure =
                binding.sixminEtAlarmSysPressure.text.toString().trim()
            sysSettingBean.sysAlarm.lowPressure =
                binding.sixminEtAlarmDiasPressure.text.toString().trim()
            sysSettingBean.sysAlarm.heartBeat = binding.sixminEtAlarmEcg.text.toString().trim()
            sysSettingBean.sysAlarm.bloodOxy =
                binding.sixminEtAlarmBloodOxygen.text.toString().trim()
            //其它设置
            sysSettingBean.sysOther.circleCountType = if (autoCircleCount) "0" else "1"
            sysSettingBean.sysOther.useOrg = binding.sixminEtOtherUseOrg.text.toString().trim()
            sysSettingBean.sysOther.areaLength =
                binding.sixminEtOtherAreaLength.text.toString().trim()
            sysSettingBean.sysOther.broadcastVoice =
                if (binding.rbBroadcastGuidanceVoiceYes.isChecked) "1" else "0"
            sysSettingBean.sysOther.autoMeasureBlood =
                if (binding.rbAutoMearsureBloodYes.isChecked) "1" else "0"
            sysSettingBean.sysOther.autoStart =
                if (binding.rbAutoStartTestYes.isChecked) "1" else "0"
            sysSettingBean.sysOther.showResetTime =
                if (binding.rbShowRestTimeYes.isChecked) "1" else "0"
            sysSettingBean.sysOther.stepsOrBreath =
                if (binding.rbStepsOrBreathBreath.isChecked) "1" else "0"

            SharedPreferencesUtils.instance.sixMinSysSetting = gson.toJson(sysSettingBean)
            mActivity.showMsg("系统参数修改成功!")
        }

        binding.sixminLlSavePwd.setNoRepeatListener {
            mActivity.initSystemInfo()
            val gson = Gson()
            val sysSettingBean = mActivity.sysSettingBean

            if (binding.sixminEtOldPwd.text.toString().trim().isEmpty()) {
                mActivity.showMsg("原密码不可为空")
                return@setNoRepeatListener
            }
            if (binding.sixminEtNewPwd.text.toString().trim().isEmpty()) {
                mActivity.showMsg("新密码不可为空")
                return@setNoRepeatListener
            }
            if (binding.sixminEtConfirmPwd.text.toString().trim().isEmpty()) {
                mActivity.showMsg("确认密码不可为空")
                return@setNoRepeatListener
            }

            if (sysSettingBean.sysPwd.exportPwd != binding.sixminEtOldPwd.text.toString().trim()) {
                mActivity.showMsg("原密码错误，请重新输入")
                return@setNoRepeatListener
            }

            if (binding.sixminEtNewPwd.text.toString() != binding.sixminEtConfirmPwd.text.toString()
                    .trim()
            ) {
                mActivity.showMsg("新密码与确认密码不同，请重新输入")
                return@setNoRepeatListener
            }

            sysSettingBean.sysPwd.exportPwd = binding.sixminEtNewPwd.text.toString().trim()
            sysSettingBean.sysPwd.modifyPwd = binding.sixminEtNewPwd.text.toString().trim()
            sysSettingBean.sysPwd.confirmPwd = binding.sixminEtConfirmPwd.text.toString().trim()

            SharedPreferencesUtils.instance.sixMinSysSetting = gson.toJson(sysSettingBean)
            mActivity.showMsg("密码修改成功!")
        }


        binding.sixminLlSaveBlue.setNoRepeatListener {
            mActivity.initSystemInfo()
            val gson = Gson()
            val sysSettingBean = mActivity.sysSettingBean

            if (!hasPassPermission) {
                val startPermissionDialogFragment =
                    SixMinPermissionDialogFragment.startPermissionDialogFragment(mActivity.supportFragmentManager)
                startPermissionDialogFragment.setOnConfirmClickListener(object :
                    SixMinPermissionDialogFragment.SixMinPermissionDialogListener {
                    override fun onClickConfirm(pwd: String) {
                        if (pwd.isEmpty()) {
                            mActivity.showMsg("权限密码不能为空")
                            return
                        }
                        if (sysSettingBean.sysPwd.exportPwd == pwd) {
                            startPermissionDialogFragment.dismiss()
                            hasPassPermission = true
                        } else {
                            mActivity.showMsg("权限密码错误，请重新输入")
                            return
                        }
                    }
                })
                return@setNoRepeatListener
            }

            if (binding.sixminEtBluetoothEcg.text.toString().trim().isEmpty()) {
                mActivity.showMsg("心电蓝牙不可为空")
                return@setNoRepeatListener
            }
            if (binding.sixminEtBluetoothBlood.text.toString().trim().isEmpty()) {
                mActivity.showMsg("血压蓝牙不可为空")
                return@setNoRepeatListener
            }
            if (binding.sixminEtBluetoothBloodOxygen.text.toString().trim().isEmpty()) {
                mActivity.showMsg("血氧蓝牙不可为空")
                return@setNoRepeatListener
            }
            if (binding.sixminEtBluetoothEcg.text.toString().trim().length != 12) {
                mActivity.showMsg("请检查心电蓝牙长度")
                return@setNoRepeatListener
            }
            if (binding.sixminEtBluetoothBlood.text.toString().trim().length != 12) {
                mActivity.showMsg("请检查血压蓝牙长度")
                return@setNoRepeatListener
            }
            if (binding.sixminEtBluetoothBloodOxygen.text.toString().trim().length != 12) {
                mActivity.showMsg("请检查血氧蓝牙长度")
                return@setNoRepeatListener
            }
            if (!CommonUtil.isValidMacAddress(
                    binding.sixminEtBluetoothEcg.text.toString().trim()
                )
            ) {
                mActivity.showMsg("心电蓝牙地址不是有效的MAC地址，请检查后重新输入！")
                return@setNoRepeatListener
            }
            if (!CommonUtil.isValidMacAddress(
                    binding.sixminEtBluetoothBlood.text.toString().trim()
                )
            ) {
                mActivity.showMsg("心血压蓝牙地址不是有效的MAC地址，请检查后重新输入！")
                return@setNoRepeatListener
            }
            if (!CommonUtil.isValidMacAddress(
                    binding.sixminEtBluetoothBloodOxygen.text.toString().trim()
                )
            ) {
                mActivity.showMsg("血氧蓝牙地址不是有效的MAC地址，请检查后重新输入！")
                return@setNoRepeatListener
            }

            if (mActivity.usbTransferUtil.isConnectUSB && mActivity.usbTransferUtil.ecgConnection && mActivity.usbTransferUtil.bloodOxygenConnection && mActivity.usbTransferUtil.bloodPressureConnection) {
                //蓝牙配置有变动需要同步到设备
                SixMinCmdUtils.dealBluetooth(
                    binding.sixminEtBluetoothEcg.text.toString().trim(),
                    binding.sixminEtBluetoothBlood.text.toString().trim(),
                    binding.sixminEtBluetoothBloodOxygen.text.toString().trim()
                )
                sysSettingBean.sysBlue.ecgBlue = binding.sixminEtBluetoothEcg.text.toString().trim()
                sysSettingBean.sysBlue.bloodBlue =
                    binding.sixminEtBluetoothBlood.text.toString().trim()
                sysSettingBean.sysBlue.bloodOxyBlue =
                    binding.sixminEtBluetoothBloodOxygen.text.toString().trim()

                SharedPreferencesUtils.instance.sixMinSysSetting = gson.toJson(sysSettingBean)
                if (mActivity.usbTransferUtil.updateBluetooth == 1) {
                    mActivity.showMsg("修改蓝牙参数成功!")
                    mActivity.usbTransferUtil.updateBluetooth = 0
                }
                hasPassPermission = false
            } else {
                mActivity.showMsg("设备未接入，请检查后重试")
                return@setNoRepeatListener
            }
        }

        binding.sixminTvOldPwd.setOnClickListener {
            time++
            if (time >= 5) {
                mActivity.initSystemInfo()
                val sysSettingBean = mActivity.sysSettingBean
                binding.sixminEtOldPwd.setText(sysSettingBean.sysPwd.exportPwd)
                time = 0
            }
        }
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSixminSystemSettingBinding.inflate(inflater, container, false)

    private fun initToolbar() {
        mActivity.setToolbarTitle(getString(R.string.system_setting))
        KeyboardUtil.setEditTextFilter(binding.sixminEtOtherUseOrg)
        KeyboardUtil.setEditTextFilter(binding.sixminEtNewPwd)
        KeyboardUtil.setEditTextFilter(binding.sixminEtConfirmPwd)
        KeyboardUtil.setEditTextFilter(binding.sixminEtBluetoothEcg)
        KeyboardUtil.setEditTextFilter(binding.sixminEtBluetoothBlood)
        KeyboardUtil.setEditTextFilter(binding.sixminEtBluetoothBloodOxygen)
    }


    private fun initSysInfo() {
        try {
            val gson = Gson()
            var sysSettingBean = SixMinSysSettingBean()
            val sixMinSysSetting = SharedPreferencesUtils.instance.sixMinSysSetting
            if (sixMinSysSetting != null && sixMinSysSetting != "") {
                sysSettingBean = gson.fromJson(
                    sixMinSysSetting, SixMinSysSettingBean::class.java
                )
            }
            autoCircleCount = sysSettingBean.sysOther.circleCountType == "0"
            //报警设置
            binding.sixminEtAlarmSysPressure.setText(sysSettingBean.sysAlarm.highPressure)
            binding.sixminEtAlarmDiasPressure.setText(sysSettingBean.sysAlarm.lowPressure)
            binding.sixminEtAlarmEcg.setText(sysSettingBean.sysAlarm.heartBeat)
            binding.sixminEtAlarmBloodOxygen.setText(sysSettingBean.sysAlarm.bloodOxy)
            //其它设置
            binding.rbBroadcastGuidanceVoiceYes.isChecked =
                sysSettingBean.sysOther.broadcastVoice == "1"
            binding.rbBroadcastGuidanceVoiceNo.isChecked =
                sysSettingBean.sysOther.broadcastVoice == "0"
            binding.rbAutoMearsureBloodYes.isChecked =
                sysSettingBean.sysOther.autoMeasureBlood == "1"
            binding.rbAutoMearsureBloodNo.isChecked =
                sysSettingBean.sysOther.autoMeasureBlood == "0"
            binding.rbAutoStartTestYes.isChecked = sysSettingBean.sysOther.autoStart == "1"
            binding.rbAutoStartTestNo.isChecked = sysSettingBean.sysOther.autoStart == "0"
            binding.rbStepsOrBreathSteps.isChecked = sysSettingBean.sysOther.stepsOrBreath == "0"
            binding.rbStepsOrBreathBreath.isChecked = sysSettingBean.sysOther.stepsOrBreath == "1"
            binding.rbShowRestTimeYes.isChecked = sysSettingBean.sysOther.showResetTime == "1"
            binding.rbShowRestTimeNo.isChecked = sysSettingBean.sysOther.showResetTime == "0"

            if (sysSettingBean.sysOther.circleCountType == "0") {
                binding.sixminRlAutoCircleCount.setBackgroundResource(R.drawable.sixmin_circle_select_bg)
                binding.sixminRlHandleCircleCount.setBackgroundResource(R.drawable.sixmin_circle_unselect_bg)
                binding.sixminTvAutoCircleCount.setTextColor(
                    ContextCompat.getColor(
                        mActivity, R.color.colorPrimary
                    )
                )
                binding.sixminTvHandleCircleCount.setTextColor(
                    ContextCompat.getColor(
                        mActivity, R.color.text3
                    )
                )
            } else {
                binding.sixminRlAutoCircleCount.setBackgroundResource(R.drawable.sixmin_circle_unselect_bg)
                binding.sixminRlHandleCircleCount.setBackgroundResource(R.drawable.sixmin_circle_select_bg)
                binding.sixminTvAutoCircleCount.setTextColor(
                    ContextCompat.getColor(
                        mActivity, R.color.text3
                    )
                )
                binding.sixminTvHandleCircleCount.setTextColor(
                    ContextCompat.getColor(
                        mActivity, R.color.colorPrimary
                    )
                )
            }
            binding.sixminEtOtherUseOrg.setText(sysSettingBean.sysOther.useOrg)
            binding.sixminEtOtherAreaLength.setText(sysSettingBean.sysOther.areaLength)
            binding.sixminEtOtherEctType.setText(if (sysSettingBean.sysOther.ectType == "1") "单导联" else if (sysSettingBean.sysOther.ectType == "7") "7导联" else "12导联")
            //蓝牙设置
            binding.sixminEtBluetoothEcg.setText(sysSettingBean.sysBlue.ecgBlue)
            binding.sixminEtBluetoothBlood.setText(sysSettingBean.sysBlue.bloodBlue)
            binding.sixminEtBluetoothBloodOxygen.setText(sysSettingBean.sysBlue.bloodOxyBlue)
            //系统信息
            val packageInfo = mActivity.packageManager.getPackageInfo(mActivity.packageName, 0)
            sysSettingBean.publishVer = "v${packageInfo.versionName.substring(0, 1)}"
            sysSettingBean.completeVer = "v${packageInfo.versionName}"
            binding.sixminTvSystemInfo.text =
                "发布版本: ${sysSettingBean.publishVer}                     完整版本: ${sysSettingBean.completeVer}                     ${sysSettingBean.copyRight}"
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun popBackStack() {
        val navController = findNavController()//fragment返回数据处理
        navController.previousBackStackEntry?.savedStateHandle?.set("key", "返回")
        navController.popBackStack()
    }
}
