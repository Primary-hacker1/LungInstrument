package com.just.machine.ui.activity

import androidx.core.content.ContextCompat
import com.common.base.CommonBaseActivity
import com.google.gson.Gson
import com.just.machine.model.SharedPreferencesUtils
import com.just.machine.model.systemsetting.SixMinSysSettingBean
import com.just.machine.util.KeyboardUtil
import com.just.news.R
import com.just.news.databinding.ActivitySixminSystemSettingBinding
import dagger.hilt.android.AndroidEntryPoint

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
    }

    private fun initToolbar() {
        binding.toolbar.title = getString(R.string.system_setting)//标题
//        binding.toolbar.tvRight.gone()
//        binding.toolbar.ivTitleBack.visible()
//        binding.toolbar.ivTitleBack.setNoRepeatListener {
//            finish()
//        }
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}