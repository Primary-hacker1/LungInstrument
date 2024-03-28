package com.just.machine.ui.activity

import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.common.base.CommonBaseActivity
import com.google.gson.Gson
import com.just.machine.model.SharedPreferencesUtils
import com.just.machine.model.systemsetting.SixMinSysSettingBean
import com.just.machine.util.KeyboardUtil
import com.just.machine.util.SixMinCmdUtils
import com.just.news.R
import com.just.news.databinding.ActivitySixminSystemSettingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.view_toolbar.view.tv_right

@AndroidEntryPoint
class SixMinSystemSettingActivity : CommonBaseActivity<ActivitySixminSystemSettingBinding>() {

    private var autoCircleCount: Boolean = true

    override fun getViewBinding() = ActivitySixminSystemSettingBinding.inflate(layoutInflater)

    override fun initView() {
        initToolbar()
        initSysInfo()
        initClickListener()
    }

    private fun initClickListener() {
        binding.sixminRlAutoCircleCount.setOnClickListener {
            if (!autoCircleCount) {
                binding.sixminRlAutoCircleCount.setBackgroundResource(R.drawable.sixmin_circle_select_bg)
                binding.sixminRlHandleCircleCount.setBackgroundResource(R.drawable.sixmin_circle_unselect_bg)
                binding.sixminTvAutoCircleCount.setTextColor(
                    ContextCompat.getColor(
                        this, R.color.colorPrimary
                    )
                )
                binding.sixminTvHandleCircleCount.setTextColor(
                    ContextCompat.getColor(
                        this, R.color.text3
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
                        this, R.color.text3
                    )
                )
                binding.sixminTvHandleCircleCount.setTextColor(
                    ContextCompat.getColor(
                        this, R.color.colorPrimary
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
            Toast.makeText(this, "系统设置保存成功!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initToolbar() {
        binding.toolbar.title = getString(R.string.system_setting)//标题
        binding.toolbar.tvRight.visibility = View.GONE
        binding.toolbar.ivTitleBack.visibility = View.VISIBLE
        binding.toolbar.ivTitleBack.setOnClickListener {
            finish()
        }
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
                        this, R.color.colorPrimary
                    )
                )
                binding.sixminTvHandleCircleCount.setTextColor(
                    ContextCompat.getColor(
                        this, R.color.text3
                    )
                )
            } else {
                binding.sixminRlAutoCircleCount.setBackgroundResource(R.drawable.sixmin_circle_unselect_bg)
                binding.sixminRlHandleCircleCount.setBackgroundResource(R.drawable.sixmin_circle_select_bg)
                binding.sixminTvAutoCircleCount.setTextColor(
                    ContextCompat.getColor(
                        this, R.color.text3
                    )
                )
                binding.sixminTvHandleCircleCount.setTextColor(
                    ContextCompat.getColor(
                        this, R.color.colorPrimary
                    )
                )
            }
            binding.sixminEtOtherUseOrg.setText(sysSettingBean.sysOther.useOrg)
            binding.sixminEtOtherAreaLength.setText(sysSettingBean.sysOther.areaLength)
            binding.sixminEtOtherEctType.setText(sysSettingBean.sysOther.ectType)
            //密码修改
            binding.sixminTvExportPwd.text = sysSettingBean.sysPwd.exportPwd
            binding.sixminEtModifyPwd.setText(sysSettingBean.sysPwd.modifyPwd)
            binding.sixminEtConfirmPwd.setText(sysSettingBean.sysPwd.confirmPwd)
            //蓝牙设置
            binding.sixminEtBluetoothEcg.setText(sysSettingBean.sysBlue.ecgBlue)
            binding.sixminEtBluetoothBlood.setText(sysSettingBean.sysBlue.bloodBlue)
            binding.sixminEtBluetoothBloodOxygen.setText(sysSettingBean.sysBlue.bloodOxyBlue)
            //系统信息
            binding.sixminTvSystemInfo.text =
                "发布版本: ${sysSettingBean.publishVer}                     完整版本: ${sysSettingBean.completeVer}                     ${sysSettingBean.copyRight}"
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}