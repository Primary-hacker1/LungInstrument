package com.just.machine.ui.fragment.sixmin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.common.base.CommonBaseFragment
import com.common.network.LogUtils
import com.google.gson.Gson
import com.just.machine.model.SharedPreferencesUtils
import com.just.machine.model.systemsetting.SixMinSysSettingBean
import com.just.machine.ui.activity.SixMinDetectActivity
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.KeyboardUtil
import com.just.machine.util.SixMinCmdUtils
import com.just.news.R
import com.just.news.databinding.FragmentSixminReportBinding
import com.just.news.databinding.FragmentSixminSystemSettingBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2020/6/19
 *@author zt
 */
@AndroidEntryPoint
class SixMinSystemSettingFragment : CommonBaseFragment<FragmentSixminSystemSettingBinding>() {

    private var autoCircleCount: Boolean = true
    private lateinit var mActivity: SixMinDetectActivity

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
        binding.sixminLlSave.setOnClickListener {
            val gson = Gson()
            val sysSettingBean = SixMinSysSettingBean()

            //报警设置
            sysSettingBean.sysAlarm.highPressure = binding.sixminEtAlarmSysPressure.text.toString()
            sysSettingBean.sysAlarm.lowPressure = binding.sixminEtAlarmDiasPressure.text.toString()
            sysSettingBean.sysAlarm.heartBeat = binding.sixminEtAlarmEcg.text.toString()
            sysSettingBean.sysAlarm.bloodOxy = binding.sixminEtAlarmBloodOxygen.text.toString()
            //其它设置
            sysSettingBean.sysOther.circleCountType = if (autoCircleCount) "0" else "1"
            sysSettingBean.sysOther.useOrg = binding.sixminEtOtherUseOrg.text.toString()
            sysSettingBean.sysOther.areaLength = binding.sixminEtOtherAreaLength.text.toString()
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
            //密码修改
            sysSettingBean.sysPwd.exportPwd = binding.sixminTvExportPwd.text.toString()
            sysSettingBean.sysPwd.modifyPwd = binding.sixminEtModifyPwd.text.toString()
            sysSettingBean.sysPwd.confirmPwd = binding.sixminEtConfirmPwd.text.toString()
            //蓝牙设置
            if (binding.sixminEtBluetoothEcg.text.toString() == "") {
                mActivity.showMsg("心电蓝牙不可为空")
                return@setOnClickListener
            }
            if (binding.sixminEtBluetoothEcg.text.toString().length != 12) {
                mActivity.showMsg("请检查心电蓝牙长度")
                return@setOnClickListener
            }
            if (binding.sixminEtBluetoothBlood.text.toString() == "") {
                mActivity.showMsg("血压蓝牙不可为空")
                return@setOnClickListener
            }
            if (binding.sixminEtBluetoothBlood.text.toString().length != 12) {
                mActivity.showMsg("请检查血压蓝牙长度")
                return@setOnClickListener
            }
            if (binding.sixminEtBluetoothBloodOxygen.text.toString() == "") {
                mActivity.showMsg("血氧蓝牙不可为空")
                return@setOnClickListener
            }
            if (binding.sixminEtBluetoothBloodOxygen.text.toString().length != 12) {
                mActivity.showMsg("请检查血氧蓝牙长度")
                return@setOnClickListener
            }
            if (sysSettingBean.sysBlue.ecgBlue != binding.sixminEtBluetoothEcg.text.toString() || sysSettingBean.sysBlue.bloodBlue != binding.sixminEtBluetoothBlood.text.toString() || sysSettingBean.sysBlue.bloodOxyBlue != binding.sixminEtBluetoothBloodOxygen.text.toString()) {
                //蓝牙配置有变动需要同步到设备
                SixMinCmdUtils.dealBluetooth(
                    binding.sixminEtBluetoothEcg.text.toString(),
                    binding.sixminEtBluetoothBlood.text.toString(),
                    binding.sixminEtBluetoothBloodOxygen.text.toString()
                )
            }
            sysSettingBean.sysBlue.ecgBlue = binding.sixminEtBluetoothEcg.text.toString()
            sysSettingBean.sysBlue.bloodBlue = binding.sixminEtBluetoothBlood.text.toString()
            sysSettingBean.sysBlue.bloodOxyBlue =
                binding.sixminEtBluetoothBloodOxygen.text.toString()

            SharedPreferencesUtils.instance.sixMinSysSetting = gson.toJson(sysSettingBean)
            mActivity.showMsg("系统设置保存成功!")
        }
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSixminSystemSettingBinding.inflate(inflater, container, false)

    private fun initToolbar() {
        mActivity.setToolbarTitle(getString(R.string.system_setting))
        KeyboardUtil.setEditTextFilter(binding.sixminEtOtherUseOrg)
        KeyboardUtil.setEditTextFilter(binding.sixminEtModifyPwd)
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
            //密码修改
            binding.sixminTvExportPwd.text = sysSettingBean.sysPwd.exportPwd
            binding.sixminEtModifyPwd.setText(sysSettingBean.sysPwd.modifyPwd)
            binding.sixminEtConfirmPwd.setText(sysSettingBean.sysPwd.confirmPwd)
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
